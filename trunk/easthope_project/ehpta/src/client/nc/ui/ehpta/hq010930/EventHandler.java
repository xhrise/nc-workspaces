package nc.ui.ehpta.hq010930;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.ehpta.hq010901.ClientUICheckRuleGetter;
import nc.ui.ehpta.pub.IAdjustType;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.calc.CalcFunc;
import nc.ui.ehpta.pub.convert.ConvertFunc;
import nc.ui.ehpta.pub.convert.MarkDlg;
import nc.ui.ehpta.pub.gen.GeneraterBillNO;
import nc.ui.ehpta.pub.valid.Validata;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.ehpta.hq010403.AdjustVO;
import nc.vo.ehpta.hq010901.CalcInterestBVO;
import nc.vo.ehpta.hq010930.CalcUnderTransfeeBVO;
import nc.vo.ehpta.hq010930.CalcUnderTransfeeHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.HYBillVO;

/**
 * 
 * 该类是一个抽象类，主要目的是生成按钮事件处理的框架
 * 
 * @author author
 * @version tempProject version
 */

public class EventHandler extends ManageEventHandler {

	public EventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {
		
		switch (intBtn) {
			case DefaultBillButton.Statistics :
				onBoStatistics();
				break;
				
			case DefaultBillButton.Mark :
				onBoMark();
				break;
				
			case DefaultBillButton.SelAll : 
				onBoSelAll();
				break;
				
			case DefaultBillButton.SelNone : 
				onBoSelNone();
				break;
				
			default :
				
				break;
		}
		
	}
	
	protected void onBoStatistics() throws Exception {
		
		CalcUnderTransfeeHVO undertrans = (CalcUnderTransfeeHVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		
		if(undertrans.getPeriod() == null || "".equals(undertrans.getPeriod()))
			throw new Exception("请选择表头期间后再进行统计");
		
		UFDate period = new UFDate(undertrans.getPeriod() + "-01");
		String lastDay = CalcFunc.getLastDay(period);
		UFDate endPeriod = new UFDate(undertrans.getPeriod() + "-" + lastDay);
		
//		create or replace view vw_pta_under_transfee as
//		select genh.cgeneralhid , genh.vbillcode cbillno , genb.cinventoryid def1 , invbas.invname def2 , genh.dbilldate sdate ,
//		genh.cwarehouseid pk_sstordoc , stordoc.storname def3 , stordoc.storaddr saddress ,
//		address.addrname eaddress , transcontb.piersfee , transcontb.inlandshipfee , transcontb.carfee , genh.pk_transport , genh.pk_transport_b ,
//		genb.noutnum num , genh.transprice fee , (nvl(genh.transprice,0) * nvl(genb.noutnum,0)) transmny
//		,genb.cgeneralbid def5 , genh.ccustomerid def6 , genh.pk_contract def4
//		from ic_general_h genh
//		left join ic_general_b genb on genh.cgeneralhid = genb.cgeneralhid
//		left join bd_stordoc stordoc on stordoc.pk_stordoc = genh.cwarehouseid
//		left join ehpta_transport_contract_b transcontb on transcontb.pk_transport_b = genh.pk_transport_b
//		left join bd_address address on address.pk_address = transcontb.estoraddr
//		left join bd_invbasdoc invbas on invbas.pk_invbasdoc = genb.cinvbasid
//
//		where decode(genh.vuserdef3 , 'Y' , 'Y' , 'N' , 'N' , 'N') = 'N' and genh.concode is not null
//		and (nvl(genh.contracttype , 0) = 10 or nvl(genh.contracttype , 0) = 20) and nvl(genh.transprice,0) > 0
//		and genh.fbillflag = 3
//		and genh.daccountdate is not null;
		
		String sql = "select * from vw_pta_under_transfee where sdate >= '"+period.toString()+"' and sdate <= '"+endPeriod.toString()+"'";
		List<HashMap> retList = (ArrayList) UAPQueryBS.getInstance().executeQuery(sql, new MapListProcessor());
		if(retList != null && retList.size() > 0) {
			
			CalcUnderTransfeeBVO[] undertransBVOs = new CalcUnderTransfeeBVO[retList.size()];
			int row = 0;
			for(HashMap map : retList) {
				
				CalcUnderTransfeeBVO undertransBVO = new CalcUnderTransfeeBVO();
				String[] Attribute = undertransBVO.getAttributeNames();
				
				for(String attr : Attribute) {
					undertransBVO.setAttributeValue(attr, ConvertFunc.change(CalcUnderTransfeeBVO.class, attr, map.get(attr)));
				}
				
				undertransBVO.setAttributeValue("rowno", String.valueOf(row));
				
				undertransBVOs[row] = undertransBVO;
				
				row ++;
			}
			
			HYBillVO billVO = new HYBillVO();
			billVO.setParentVO(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
			billVO.setChildrenVO(undertransBVOs);
			getBillCardPanelWrapper().setCardData(billVO);
			
		} else 
			getBillUI().showWarningMessage("没有可统计的记录...");
		
	}
	
	/**
	 * 功能 ： 批改按钮操作
	 * 
	 * Author : river 
	 * 
	 * Create : 2012-08-21
	 * 
	 * @throws Exception
	 */
	protected void onBoMark() throws Exception {
		
		MarkDlg markDlg = MarkDlg.getInstance(getBillUI() , getUIController().getBillType());
		if(markDlg.showModal() == UIDialog.ID_OK) {
			
			if(markDlg.getFieldRef().getRefPK() == null || "".equals(markDlg.getFieldRef().getRefPK())) 
				throw new Exception ("属性为空，不能进行批改操作。");
			
			CalcUnderTransfeeBVO[] bodyVOs = (CalcUnderTransfeeBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
			CalcUnderTransfeeBVO[] selBodyVOs = (CalcUnderTransfeeBVO[]) getBillCardPanelWrapper().getBillCardPanel().getBillModel().getBodySelectedVOs(CalcUnderTransfeeBVO.class.getName());
			if(selBodyVOs == null || selBodyVOs.length == 0)
				selBodyVOs = (CalcUnderTransfeeBVO[]) getBillCardPanelWrapper().getSelectedBodyVOs();
			
			if(selBodyVOs == null || selBodyVOs.length == 0)
				throw new Exception ("请至少选择一条记录进行批改操作。");
			
			for(CalcUnderTransfeeBVO selbodyVO : selBodyVOs) {
				for(CalcUnderTransfeeBVO bodyVO : bodyVOs) {
					if(selbodyVO.getRowno().equals(bodyVO.getRowno())) {
						
						if(markDlg.getTxtValue() instanceof UITextField) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcUnderTransfeeBVO.class, markDlg.getFieldRef().getRefCode(), ((UITextField)markDlg.getTxtValue()).getText()));
						} else if(markDlg.getTxtValue() instanceof UICheckBox) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcUnderTransfeeBVO.class, markDlg.getFieldRef().getRefCode(), new UFBoolean(((UICheckBox)markDlg.getTxtValue()).isSelected())));
						} else if(markDlg.getTxtValue() instanceof UIRefPane) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcUnderTransfeeBVO.class, markDlg.getFieldRef().getRefCode(), ((UIRefPane)markDlg.getTxtValue()).getRefName()));
						}
						
						break;
					}
				}
				
			}
			
			HYBillVO billVO = new HYBillVO();
			billVO.setParentVO(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
			billVO.setChildrenVO(bodyVOs);
			getBillCardPanelWrapper().setCardData(billVO);
			
//			for(int row = 0 , count = bodyVOs.length ; row < count ; row ++) 
//				getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row, ((ClientUI)getBillUI()).bodyFamulas);
			
			
		}
	}
	
	protected void onBoSelAll() throws Exception {
		selectAll( true);
	}
	
	protected void onBoSelNone() throws Exception {
		selectAll( false);
	}
	
	/**
	 * 功能 ： 全选或全消行记录
	 * 
	 * Author : river 
	 * 
	 * Create : 2012-08-21
	 * 
	 * @throws Exception
	 */
	private final void selectAll( boolean isNeedSelected) throws Exception {
		int row = getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO().length;
		BillModel headModel = getBillCardPanelWrapper().getBillCardPanel().getBillModel("ehpta_calc_under_transfee_b");
		if (isNeedSelected) {
			for (int n = 0; n < row; n++) {
				if (headModel.getRowState(n) != BillModel.SELECTED) {
					headModel.setRowState(n, BillModel.SELECTED);
				}
			}
		} else {
			for (int n = 0; n < row; n++) {
				if (headModel.getRowState(n) != BillModel.UNSTATE) {
					headModel.setRowState(n, BillModel.UNSTATE);
				}
			}
		}

		getBillUI().updateUI();
	}
	
	@Override
	protected void onBoCard() throws Exception {
		super.onBoCard();
		
		afterOnButton();
	}
	
	@Override
	public void onButton(ButtonObject bo) {

		super.onButton(bo);
		
		afterOnButton();
		
	}
	
	/**
	 * 点击按钮后的处理
	 */
	private final void afterOnButton() {
		
		if(getBillUI().getBillOperate() == IBillOperate.OP_ADD) {
			getButtonManager().getButton(DefaultBillButton.Maintain).setEnabled(true);
		} else if(getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {
			getButtonManager().getButton(DefaultBillButton.Maintain).setEnabled(true);
			getButtonManager().getButton(DefaultBillButton.Statistics).setEnabled(false);
		} else {
			getButtonManager().getButton(DefaultBillButton.Maintain).setEnabled(false);
			getButtonManager().getButton(DefaultBillButton.Statistics).setEnabled(true);
		} 
		
		getBillUI().updateButtons();
		
	}
	
	@Override
	protected void onBoSave() throws Exception {
		
		Validata.saveValidataIsNull(getBillCardPanelWrapper().getBillCardPanel(), getBillCardPanelWrapper().getBillVOFromUI(), new String[]{"ehpta_calc_under_transfee_b"});
		
		AggregatedValueObject billVO = getBillCardPanelWrapper().getBillVOFromUI();
		
		if(billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0) {
			UFDate redate = (UFDate) billVO.getChildrenVO()[0].getAttributeValue("edate");
			if(redate != null) {
				String period = (String) billVO.getParentVO().getAttributeValue("period");
				if(!((redate.getYear() + "-" + (redate.getMonth() < 10 ? "0" + redate.getMonth() : "" + redate.getMonth())).equals(period))) {
					getBillCardPanelWrapper().getBillCardPanel().setHeadItem("period", redate.getYear() + "-" + (redate.getMonth() < 10 ? "0" + redate.getMonth() : "" + redate.getMonth()));
					throw new Exception("表体统计的数据与表头期间不一致，期间自动修改为正确期间");
				}
			}
		}
		
		super.onBoSave();
		
		afterOnBoSave();
		
		validPrevious();
		
	}
	
	/**
	 * 功能 ： 构造推入余额调整表的数据
	 * 
	 * 
	 * @param bodyVO
	 * @return
	 * @throws Exception
	 */
	protected final HYBillVO createAdjust(CalcUnderTransfeeBVO bodyVO) throws Exception {
		AdjustVO adjust = new AdjustVO();
		adjust.setAttributeValue("type", IAdjustType.Subsidies);
		adjust.setAttributeValue("reason", "下游运费结算录入");
		adjust.setAttributeValue("mny", bodyVO.getTransmny());
		adjust.setAttributeValue("pk_contract",bodyVO.getDef4());
		adjust.setAttributeValue("pk_cubasdoc", bodyVO.getDef6());
		adjust.setAttributeValue("memo", "下游运费结算推式生成");
		adjust.setAttributeValue("vbillno",GeneraterBillNO.getInstanse().build("HQ07", _getCorp().getPk_corp()));
		adjust.setAttributeValue("adjustdate", _getDate());
		adjust.setAttributeValue("managerid", _getOperator());
		adjust.setAttributeValue("vbillstatus", 8);
		adjust.setAttributeValue("pk_corp", _getCorp().getPk_corp());
		adjust.setAttributeValue("pk_billtype", "HQ07");
		adjust.setAttributeValue("voperatorid", _getOperator());
		adjust.setAttributeValue("dmakedate", _getDate());
		adjust.setAttributeValue("def1", bodyVO.getDef5());
		adjust.setAttributeValue("def2", "Y");
		adjust.setAttributeValue("def3", "N");
		
		HYBillVO billVO = new HYBillVO();
		billVO.setParentVO(adjust);
		
		return billVO;
	}

	/**
	 * 功能 ： 保存后续验证
	 * 
	 * Author : river 
	 * 
	 * Create : 2012-08-21	
	 * 
	 * @throws Exception
	 */
	protected final void validPrevious() throws Exception {
		Object period = getBufferData().getCurrentVO().getParentVO().getAttributeValue("period");
		String prePeriod = "";
		if(period != null) {
			period = period + "-01";
			UFDate periodDate = new UFDate(period.toString());
			prePeriod = periodDate.getYear() + "-" + (periodDate.getMonth() - 1 < 10 ? "0" + (periodDate.getMonth() - 1) : "" + (periodDate.getMonth() - 1));
		}
		
		Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from ehpta_calc_under_transfee_h where period = '"+prePeriod+"'", new ColumnProcessor());
		if(count == 0) 
			getBillUI().showWarningMessage("前一期间的下游运费未统计...");
	}
	
	protected final void afterOnBoSave() throws Exception {
		
		CalcUnderTransfeeBVO[] currBodyVOs = (CalcUnderTransfeeBVO[]) getBufferData().getCurrentVO().getChildrenVO();
		
		for(CalcUnderTransfeeBVO bodyVO : currBodyVOs) {
			bodyVO.setSettleflag(new UFBoolean("Y"));
			bodyVO.setSettledate(_getDate());
		}
		
		HYPubBO_Client.updateAry(currBodyVOs);
		
		AggregatedValueObject newAggVO = HYPubBO_Client.queryBillVOByPrimaryKey(getUIController().getBillVoName(), getBufferData().getCurrentVO().getParentVO().getPrimaryKey());
		
		getBufferData().setVOAt(getBufferData().getCurrentRow(), newAggVO);
		getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		
		List<HYBillVO> adjustList = new ArrayList<HYBillVO>();
		List<String> flagPks = new ArrayList<String>();
		
		for(CalcUnderTransfeeBVO bodyVO : currBodyVOs) {
			Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from ic_general_h where cgeneralhid = '"+bodyVO.getCgeneralhid()+"' and nvl(vuserdef3,'N') = 'Y'", new ColumnProcessor());
			
			if(count == 0) {
				try { UAPQueryBS.getInstance().executeQuery("update ic_general_h set vuserdef3 = 'Y' where cgeneralhid = '"+bodyVO.getCgeneralhid()+"' ", null); } catch(Exception e) { }
//				remove by river for 2012-09-24
//				adjustList.add(createAdjust(bodyVO));
			}	// else {
//				flagPks.add("'" + bodyVO.getDef5() + "'");
//			}
		}
		
		// 下游运费不推送至余额调整表
		//　remove by river for 2012-09-24
//		if (adjustList != null && adjustList.size() > 0) {
//			Object userObj = new ClientUICheckRuleGetter();
//			AggregatedValueObject[] adjustAggVOs = HYPubBO_Client.saveBDs(adjustList.toArray(new HYBillVO[0]), userObj);
//
//			for (AggregatedValueObject billVO : adjustAggVOs) {
//
//				try {
//					SuperVO adjust = HYPubBO_Client.queryByPrimaryKey(AdjustVO.class, billVO.getParentVO().getPrimaryKey());
//					HYBillVO newBillVO = new HYBillVO();
//					newBillVO.setParentVO(adjust);
//
//					getBusinessAction().approve(newBillVO,"HQ07", billVO.getParentVO().getAttributeValue("dmakedate").toString(), userObj);
//
//				} catch (Exception e) {
//					Logger.error(e);
//				}
//
//			}
//		}
//		
//		if(flagPks.size() > 0) {
//			AdjustVO[] adjustArr = (AdjustVO[]) HYPubBO_Client.queryByCondition(AdjustVO.class, " def1 in ("+ConvertFunc.change(flagPks.toArray(new String[0]))+") and nvl(dr,0)=0 ");
//			try { 
//				for(AdjustVO adjust : adjustArr) {
//					for(CalcUnderTransfeeBVO bodyVO : currBodyVOs) {
//						if(adjust.getDef1().equals(bodyVO.getDef5()))
//							adjust.setMny(bodyVO.getTransmny());
//							
//					}
//				}
//				
//				HYPubBO_Client.updateAry(adjustArr); 
//			} catch(Exception e) { 
//				Logger.error(e.getMessage()); 
//			}
//		}
		
		
	}
	
	@Override
	protected void onBoDelete() throws Exception {
		AggregatedValueObject currAggVO = getBufferData().getCurrentVO();
		
		super.onBoDelete();
		
		if(currAggVO != null) {
			Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from ehpta_calc_under_transfee_h where pk_transfee = '"+currAggVO.getParentVO().getPrimaryKey()+"' and nvl(dr,0)=1", new ColumnProcessor());
			if(count > 0) {
				if(currAggVO.getChildrenVO() != null && currAggVO.getChildrenVO().length > 0) {
					SuperVO[] vos = (SuperVO[]) currAggVO.getChildrenVO().clone();
					for(SuperVO vo : vos) {
						vo.setAttributeValue("dr", 1);
					}
					
					HYPubBO_Client.updateAry(vos);
				}
				
				afterOnBoDelete(currAggVO);
			}
		}
		
		
	}
	
	/**
	 * 功能 ：删除后续操作
	 * 
	 * Author : river 
	 * 
	 * Create : 2012-08-21
	 * 
	 * @throws Exception
	 */
	protected final void afterOnBoDelete(AggregatedValueObject currAggVO) throws Exception {
		
		Object userObj = new ClientUICheckRuleGetter();
		
		List<String> adjustList = new ArrayList<String>();
		
		if(currAggVO == null || currAggVO.getChildrenVO() == null || currAggVO.getChildrenVO().length == 0)
			return ;
		
		for(CalcUnderTransfeeBVO bodyVO : (CalcUnderTransfeeBVO[])currAggVO.getChildrenVO()) {
			Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from ic_general_h where cgeneralhid = '"+bodyVO.getCgeneralhid()+"' and nvl(vuserdef3,'N') = 'Y'", new ColumnProcessor());
			
			if(count > 0) {
				try { UAPQueryBS.getInstance().executeQuery("update ic_general_h set vuserdef3 = 'N' where cgeneralhid = '"+bodyVO.getCgeneralhid()+"' ", null); } catch(Exception e) { }
				adjustList.add("'" + bodyVO.getDef5() + "'");
			}	
		}
		
		SuperVO[] superVos = HYPubBO_Client.queryByCondition(AdjustVO.class, " def1 in (" + ConvertFunc.change(adjustList.toArray(new String[0])) + ") and type = "+IAdjustType.Subsidies+" and nvl(dr,0) = 0 ");
		
		List<HYBillVO> billVOs = new ArrayList<HYBillVO>();
		for (SuperVO superVO : superVos) {
			HYBillVO billVO = new HYBillVO();
			billVO.setParentVO(superVO);
			billVOs.add(billVO);
		}
		
		if (billVOs != null && billVOs.size() > 0) {
			for (AggregatedValueObject billVO : billVOs) {

				try {
					getBusinessAction().unapprove(billVO, "HQ07", billVO.getParentVO().getAttributeValue("dapprovedate").toString(), userObj);
				
				} catch (Exception e) {
					Logger.error(e);
					throw new Exception(e);
				}
				
				try {
					SuperVO adjust = HYPubBO_Client.queryByPrimaryKey( AdjustVO.class, billVO.getParentVO().getPrimaryKey());
					HYBillVO newBillVO = new HYBillVO();
					newBillVO.setParentVO(adjust);

					getBusinessAction().delete(newBillVO, "HQ07", billVO.getParentVO().getAttributeValue("dapprovedate").toString(), userObj);
				
				} catch (Exception e) {

					Logger.error(e);
					throw new Exception(e);
					
				}

			}

		}
		
		for(CalcUnderTransfeeBVO bodyVO : (CalcUnderTransfeeBVO[])currAggVO.getChildrenVO()) {
			bodyVO.setSettleflag(new UFBoolean("N"));
			bodyVO.setSettledate(null);
		}
		
		HYPubBO_Client.updateAry((CalcUnderTransfeeBVO[])currAggVO.getChildrenVO());
		
			
	}
	
	@Override
	protected void onBoCancelAudit() throws Exception {
		
		CircularlyAccessibleValueObject[] bodyVOs = getBufferData().getCurrentVO().getChildrenVO();
		List<String> flagPks = new ArrayList<String>();
		for(CircularlyAccessibleValueObject bodyVO : bodyVOs) {
			flagPks.add("'" + bodyVO.getAttributeValue("def5") + "'");
		}
		
		if(flagPks.size() > 0) {
			AdjustVO[] adjustArr = (AdjustVO[]) HYPubBO_Client.queryByCondition(AdjustVO.class, " def1 in ("+ConvertFunc.change(flagPks.toArray(new String[0]))+") and nvl(dr,0)=0 ");
			for(AdjustVO adjust : adjustArr) {
				if("Y".equals(adjust.getDef4())) {
					getBillUI().showErrorMessage("表体行中存在已被使用的记录，不能进行弃审操作！");
					return ;
				}
			}
		}
		
		super.onBoCancelAudit();
	}

}