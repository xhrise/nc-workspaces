package nc.ui.ar.m20060301;

/**
 * 
 * 作者：qdq 创建时间：(2001-7-6 9:35:26) 使用说明： 注意： 单据管理存在既时核销状态和补差状态，需要多两个按钮组。
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.ehpta.pub.IAdjustType;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.gen.GeneraterBillNO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.businessaction.IBusinessController;
import nc.vo.ehpta.hq010403.AdjustVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.HYBillVO;

public class Ar20060301 extends nc.ui.ep.dj.DjPflowPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5281497366769516937L;

	// 设置余额调整单的业务相关信息
	// add by river for 2012-07-25
	// start ..
	private nc.ui.ehpta.hq010403.ClientUI clientUI = null;
	private nc.ui.ehpta.hq010403.EventHandler handler = null;
	private IBusinessController businessAction = null;

	// .. end

	/**
	 * Ar20060301 构造子注解。
	 */
	public Ar20060301() {
		super(0);
		// m_bQc = false;
		// setM_Djdl("ys");
		// refactoring: set the djdl into data buffer directly
		// dataBuffer.setCurrentDjdl("ys");
		setM_Node("2006030102");
		// setDjlxbm(getBillType());
		// //setDjlxbm("D0");//设置单据类型编码
		// setDjlxbm(nc.ui.arap.global.PubData.getDjlxbmByPkcorp(
		// getClientEnvironment().getCorporation().getPrimaryKey(), 0));
		// //initialize();
		// setM_Node("2006030102");
	}

	public void postInit() {
		if (ILinkType.NONLINK_TYPE == this.getLinkedType()) {
			super.postInit();
			// setDjlxbm("D0");//设置单据类型编码
			setDjlxbm(nc.ui.arap.global.PubData.getDjlxbmByPkcorp(
					getClientEnvironment().getCorporation().getPrimaryKey(), 0));
			// initialize();

		}
	}

	public String getNodeCode() {
		return this.getDjSettingParam().getNodeID();
	}

	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
				"UPP2006030102-000413")/* @res "单据处理" */;
	}

	/**
	 * 功能　：重写afterOnButtonClicked
	 * 
	 * Authur : river
	 * 
	 * Create Date : 2012-07-25
	 * 
	 */
	@Override
	protected void afterOnButtonClicked(ButtonObject bo, boolean success) {

		if (success) {

			if ("审核".equals(bo.getName()))
				afterOnBoAudit();

			else if ("反审核".equals(bo.getName()))
				afterOnBoCancleAudit();
 
		}
	}

	/**
	 * 功能 ： 审核通过时推式生成余额调整单记录
	 * 
	 * Author : river
	 * 
	 * Create Date : 2012-07-25
	 * 
	 */
	private final void afterOnBoAudit() {

		Object userObj = new nc.ui.ehpta.hq010403.ClientUICheckRuleGetter();
		Vector<String> djpks = new Vector<String>();
		
		try {
			djpks = getAllSelectedDJPK();
		
		} catch(Exception e) { }
		
		if (djpks != null && djpks.size() == 0)
			if (getArapDjPanel1().getBillCardPanelDj().getBillData().getHeadItem("vouchid") != null)
				djpks.add(getArapDjPanel1().getBillCardPanelDj().getBillData().getHeadItem("vouchid").getValueObject().toString());

		String whereSql = "";
		if (djpks != null && djpks.size() > 0) {
			int i = 0;
			for (String djpk : djpks) {
				if (i == djpks.size() - 1)
					whereSql += "'" + djpk + "'";
				else
					whereSql += "'" + djpk + "',";

				i++;
			}
		} else
			return;

		Vector retVector = null;
		if (!"".equals(whereSql)) {
			try {
				retVector = (Vector) UAPQueryBS.getInstance().executeQuery( "select djzt , spzt , zyx6 , zyx8 , vouchid , shr , shrq , ybje from arap_djzb where vouchid in (" + whereSql + ") and dwbm = '" + getCorpPrimaryKey() + "' and zyx6 is not null and nvl(dr,0) = 0 and nvl(djzt , 0) = 3 ", new VectorProcessor());
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}

		List<HYBillVO> adjustList = new ArrayList<HYBillVO>();

		if (retVector != null && retVector.size() > 0) {
			for (Object vct : retVector) {
				try {
					Object djzt = ((Vector) vct).get(0);
					Object spzt = ((Vector) vct).get(1);
					Object pk_contract = ((Vector) vct).get(2);
					Object iscredit = ((Vector) vct).get(3);
					Object vouchid = ((Vector) vct).get(4);
					Object shr = ((Vector) vct).get(5);
					Object shrq = ((Vector) vct).get(6);
					Object ybje = ((Vector) vct).get(7);

					if (spzt != null || !"".equals(spzt)) {
						switch (Integer.valueOf(spzt.toString())) {

						case 1:
							// 审核通过
							if (Integer.valueOf(djzt.toString()) == 3) {
								
								Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select count(1) from ehpta_adjust where def1 = '" + vouchid + "' and nvl(dr,0)=0 ", new ColumnProcessor());
								if (count == 0) {

									AdjustVO adjust = new AdjustVO();
									adjust.setAttributeValue("type", IAdjustType.Receivables);
									adjust.setAttributeValue("reason", "收款录入");
									adjust.setAttributeValue("mny", new UFDouble(ybje.toString()));
									adjust.setAttributeValue("pk_contract", pk_contract);
									adjust.setAttributeValue( "pk_cubasdoc", UAPQueryBS.getInstance().executeQuery("select pk_cumandoc from bd_cumandoc where pk_cubasdoc in (select distinct hbbm from arap_djfb where vouchid = '" + vouchid + "') and pk_corp = '"+getCorpPrimaryKey()+"'", new ColumnProcessor()));
									adjust.setAttributeValue("memo", "收款录入推式生成");
									adjust.setAttributeValue( "vbillno", GeneraterBillNO.getInstanse().build("HQ07", getCorpPrimaryKey()));
									adjust.setAttributeValue("adjustdate", new UFDate(shrq.toString()));
									adjust.setAttributeValue("managerid", shr);
									adjust.setAttributeValue("vbillstatus", 8);
									adjust.setAttributeValue("pk_corp", getCorpPrimaryKey());
									adjust.setAttributeValue("pk_billtype", "HQ07");
									adjust.setAttributeValue("voperatorid", shr);
									adjust.setAttributeValue("dmakedate", new UFDate(shrq.toString()));
									adjust.setAttributeValue("def1", vouchid);
									adjust.setAttributeValue("def2", "Y");
									adjust.setAttributeValue("def3", iscredit);
									adjust.setAttributeValue("def6", "Y");
									
									HYBillVO billVO = new HYBillVO();
									billVO.setParentVO(adjust);
									adjustList.add(billVO);

								}
							}

							break;

						default:

							break;
						}
					}
				} catch (Exception e) {
					Logger.error(e);
					continue;
				}

			}

		}

		try {
			if (adjustList != null && adjustList.size() > 0) {

				AggregatedValueObject[] adjustAggVOs = HYPubBO_Client.saveBDs(
						adjustList.toArray(new HYBillVO[0]), userObj);

				for (AggregatedValueObject billVO : adjustAggVOs) {
					// 审核前不需要提交
					// try { businessAction.commit(billVO, "HQ07",
					// billVO.getParentVO().getAttributeValue("dmakedate").toString()
					// , userObj ); } catch(Exception e) { Logger.error(e); }

					try {
						SuperVO adjust = HYPubBO_Client.queryByPrimaryKey(
								AdjustVO.class, billVO.getParentVO()
										.getPrimaryKey());
						HYBillVO newBillVO = new HYBillVO();
						newBillVO.setParentVO(adjust);

						getBusiController().approve(
								newBillVO,
								"HQ07",
								billVO.getParentVO()
										.getAttributeValue("dmakedate")
										.toString(), userObj);

					} catch (Exception e) {
						Logger.error(e);
					}

				}
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	/**
	 * 功能 ： 弃审时推式生成余额调整单记录
	 * 
	 * Author : river
	 * 
	 * Create Date : 2012-07-25
	 * 
	 */
	private final void afterOnBoCancleAudit() {

		Vector<String> djpks = new Vector<String>();
		
		try {
			djpks = getAllSelectedDJPK();
		} catch(Exception e) { }
		 
		
		if (djpks != null && djpks.size() == 0)
			if (getArapDjPanel1().getBillCardPanelDj().getBillData() .getHeadItem("vouchid") != null)
				djpks.add(getArapDjPanel1().getBillCardPanelDj().getBillData().getHeadItem("vouchid").getValueObject().toString());

		if (djpks != null && djpks.size() > 0) {
			try {
				Object userObj = new nc.ui.ehpta.hq010403.ClientUICheckRuleGetter();

				String whereSql = "";
				int i = 0;
				for (String djpk : djpks) {
					if (i == djpks.size() - 1)
						whereSql += "'" + djpk + "'";
					else
						whereSql += "'" + djpk + "',";

					i++;
				}

				Vector retVector = null;
				if (!"".equals(whereSql)) {
					try {
						retVector = (Vector) UAPQueryBS.getInstance()
								.executeQuery(
										"select djzt , spzt , zyx6 , zyx8 , vouchid , shr , shrq , ybje from arap_djzb where vouchid in ("
												+ whereSql
												+ ") and dwbm = '"
												+ getCorpPrimaryKey()
												+ "' and zyx6 is not null and nvl(dr,0)=0 and nvl(djzt,0) = 3 ",
										new VectorProcessor());
					} catch (BusinessException e) {
						e.printStackTrace();
					}
				}

				SuperVO[] superVos = HYPubBO_Client.queryByCondition(
						AdjustVO.class, " def1 in (" + whereSql
								+ ") and nvl(dr,0) = 0 ");
				List<HYBillVO> billVOs = new ArrayList<HYBillVO>();
				for (SuperVO superVO : superVos) {
					HYBillVO billVO = new HYBillVO();
					billVO.setParentVO(superVO);
					billVOs.add(billVO);
				}

				AggregatedValueObject[] adjustAggVOs = billVOs
						.toArray(new HYBillVO[0]);

				if (adjustAggVOs != null && adjustAggVOs.length > 0) {
					for (AggregatedValueObject billVO : adjustAggVOs) {

						Object vouchid = billVO.getParentVO()
								.getAttributeValue("def1");
						if (vouchid != null) {
							for (Object vct : retVector) {
								Object arap_vouchid = ((Vector) vct).get(4);
								if (arap_vouchid.equals(vouchid)) {
									if (((Vector) vct).get(1) != null) {
										Logger.info("单据未弃审至制单状态，不能弃审余额调整单中的记录");
										Logger.error("单据未弃审至制单状态，不能弃审余额调整单中的记录");

										showErrorMessage("余额调整单 - "
												+ billVO.getParentVO()
														.getAttributeValue(
																"vbillno")
												+ " : 单据未弃审至制单状态，不能弃审余额调整单中的记录");

										continue;
									}
								}
							}
						}

						try {
							getBusiController().unapprove(
									billVO,
									"HQ07",
									billVO.getParentVO()
											.getAttributeValue("dapprovedate")
											.toString(), userObj);
						} catch (Exception e) {
							Logger.error(e);
						}
						try {
							SuperVO adjust = HYPubBO_Client.queryByPrimaryKey(
									AdjustVO.class, billVO.getParentVO()
											.getPrimaryKey());
							HYBillVO newBillVO = new HYBillVO();
							newBillVO.setParentVO(adjust);

							getBusiController().delete(
									newBillVO,
									"HQ07",
									billVO.getParentVO()
											.getAttributeValue("dapprovedate")
											.toString(), userObj);
						} catch (Exception e) {

							Logger.error(e);
						}

					}

				}
			} catch (Exception e) {
				Logger.error(e);
			}
		} else
			return;

	}

	/**
	 * 
	 * 功能 :　获取到余额调整单中的业务接口，以便后续的审核、弃审等操作使用
	 * 
	 * Authur : river
	 * 
	 * Create Date : 2012-07-25
	 * 
	 * @return businessAction
	 */
	private final IBusinessController getBusiController() {

		if (clientUI == null)
			clientUI = new nc.ui.ehpta.hq010403.ClientUI();

		if (handler == null)
			handler = new nc.ui.ehpta.hq010403.EventHandler(clientUI,
					new nc.ui.ehpta.hq010403.ClientUICtrl());

		if (businessAction == null)
			businessAction = handler.getBusiAction();

		return businessAction;
	}

}