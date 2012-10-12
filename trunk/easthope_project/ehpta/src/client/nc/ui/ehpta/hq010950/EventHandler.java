package nc.ui.ehpta.hq010950;

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
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.ehpta.hq010403.AdjustVO;
import nc.vo.ehpta.hq010950.CalcRebatesBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;

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
			case DefaultBillButton.Statistics:
				onBoStatistics();
				break;
	
			case DefaultBillButton.Mark:
				onBoMark();
				break;
	
			case DefaultBillButton.SelAll:
				onBoSelAll();
				break;
	
			case DefaultBillButton.SelNone:
				onBoSelNone();
				break;
	
			case DefaultBillButton.DataIn : 
				onBoDataIn();
				break;
				
			case DefaultBillButton.CancleDataIn : 
				onBoCancleDataIn();
				break;
				
			default:
	
				break;
		}
	}

	protected void onBoDataIn() throws Exception {
		
		AggregatedValueObject currVO = getBufferData().getCurrentVO();
		
		if(currVO != null && currVO.getChildrenVO() != null && currVO.getChildrenVO().length > 0) {
			
			if("Y".equals(currVO.getParentVO().getAttributeValue("def1"))) {
				getBillUI().showErrorMessage("当前单据已经生成余额调整单记录，不能重复生成！");
				return ;
			}			
			
			List<HYBillVO> adjustList = new ArrayList<HYBillVO>();
			
			for(CircularlyAccessibleValueObject bodyVO : currVO.getChildrenVO() ) {
				if(bodyVO.getAttributeValue("actmny") == null || ((UFDouble)bodyVO.getAttributeValue("actmny")).doubleValue() == 0 )
					continue;
				
				adjustList.add(createAdjust((CalcRebatesBVO)bodyVO));
			}
			
			if (adjustList != null && adjustList.size() > 0) {
				Object userObj = new ClientUICheckRuleGetter();
				AggregatedValueObject[] adjustAggVOs = HYPubBO_Client.saveBDs(adjustList.toArray(new HYBillVO[0]), userObj);

				for (AggregatedValueObject billVO : adjustAggVOs) {

					try {
						SuperVO adjust = HYPubBO_Client.queryByPrimaryKey(AdjustVO.class, billVO.getParentVO().getPrimaryKey());
						HYBillVO newBillVO = new HYBillVO();
						newBillVO.setParentVO(adjust);

						getBusinessAction().approve(newBillVO,"HQ07", billVO.getParentVO().getAttributeValue("dmakedate").toString(), userObj);

					} catch (Exception e) {
						Logger.error(e);
					}

				}
				
				currVO.getParentVO().setAttributeValue("def1", "Y");
				currVO.getParentVO().setAttributeValue("def2", _getDate().toString());
				HYPubBO_Client.update((SuperVO) currVO.getParentVO());
				
				AggregatedValueObject billVO = HYPubBO_Client.queryBillVOByPrimaryKey(getUIController().getBillVoName(), currVO.getParentVO().getPrimaryKey());
				
				int currRow = getBufferData().getCurrentRow();
				getBufferData().setCurrentVO(billVO);
				updateBuffer();
				getBufferData().setCurrentRow(currRow);
				
			}
			
		}
		
	}
	
	protected void onBoCancleDataIn() throws Exception {
		
		AggregatedValueObject currVO = getBufferData().getCurrentVO();
		
		if(currVO != null && currVO.getChildrenVO() != null && currVO.getChildrenVO().length > 0) {
			
			if(currVO.getParentVO().getAttributeValue("def1") == null || "N".equals(currVO.getParentVO().getAttributeValue("def1"))) {
				getBillUI().showErrorMessage("当前单据未生成余额调整单记录，不能进行取消操作！");
				return ;
			}
			
			List<String> pkList = new ArrayList<String>();
			
			for(CircularlyAccessibleValueObject bodyVO : currVO.getChildrenVO() ) {
				pkList.add("'" + bodyVO.getPrimaryKey() + "'");
			}
			
			AdjustVO[] adjustVOs = (AdjustVO[]) HYPubBO_Client.queryByCondition(AdjustVO.class, " def1 in ("+ConvertFunc.change(pkList.toArray(new String[0]))+") ");
			
			for(AdjustVO adjust : adjustVOs) {
				
				if("Y".equals(adjust.getDef4())) 
					throw new Exception ("当前取消的单据中存在已经被使用的记录，不能进行取消推入操作！");
				
			}
			
			
			List<HYBillVO> billVOs = new ArrayList<HYBillVO>();
			for (SuperVO superVO : adjustVOs) {
				HYBillVO billVO = new HYBillVO();
				billVO.setParentVO(superVO);
				billVOs.add(billVO);
			}
			
			if (billVOs != null && billVOs.size() > 0) {
				
				Object userObj = new ClientUICheckRuleGetter();
				
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
			
			currVO.getParentVO().setAttributeValue("def1", "N");
			currVO.getParentVO().setAttributeValue("def2", null);
			HYPubBO_Client.update((SuperVO) currVO.getParentVO());
			
			AggregatedValueObject billVO = HYPubBO_Client.queryBillVOByPrimaryKey(getUIController().getBillVoName(), currVO.getParentVO().getPrimaryKey());
			
			int currRow = getBufferData().getCurrentRow();
			getBufferData().setCurrentVO(billVO);
			updateBuffer();
			getBufferData().setCurrentRow(currRow);
			
		}
	}
	
	protected void onBoStatistics() throws Exception {
		
		String period = ((UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("period").getComponent()).getRefName();
		
		if(period == null || "".equals(period))
			throw new Exception("请选择表头期间后再进行统计");
		

//		create or replace view vw_pta_rebates as
//		select sale.ccustomerid pk_cumandoc ,cubas.custname , salecont.pk_contract , salecont.vbillno concode, orderb.cinventoryid pk_invmandoc ,  invbas.invname  ,  sale.period  ,
//		salecontb.num , sum(orderb.nnumber) nnumber , decode(nvl(salecontb.num,0),0,0,(nvl(sum(orderb.nnumber),0) / nvl(salecontb.num,0) * 100)) comprate ,
//		nvl(salecontb.preprice,0) preprice , (nvl(sum(orderb.nnumber),0) * nvl(salecontb.preprice,0)) premny ,
//		0 adjustmny , (nvl(sum(orderb.nnumber),0) * nvl(salecontb.preprice,0)) actmny ,  WMSYS.WM_CONCAT(chr(39) || orderb.corder_bid || chr(39)) orderbids
//		, sale.pk_corp
//		from so_sale sale
//		
//		left join so_saleorder_b orderb on orderb.csaleid = sale.csaleid
//		left join ehpta_sale_contract salecont on salecont.pk_contract = sale.pk_contract
//		left join ehpta_sale_contract_b salecontb on salecontb.pk_contract = sale.pk_contract
//		left join bd_invbasdoc invbas on invbas.pk_invbasdoc = orderb.cinvbasdocid
//		left join bd_cumandoc cuman on cuman.pk_cumandoc = sale.ccustomerid
//		left join bd_cubasdoc cubas on cubas.pk_cubasdoc = cuman.pk_cubasdoc
//		
//		where sale.pk_contract is not null and sale.contracttype = 20 and sale.fstatus = 2
//		
//		and nvl(sale.dr,0)=0
//		and nvl(orderb.dr,0)=0
//		and nvl(salecont.dr,0)=0
//		and nvl(salecontb.dr,0)=0
//		and nvl(invbas.dr,0)=0
//		and nvl(cuman.dr,0)=0
//		and nvl(cubas.dr,0)=0
//		and nvl(orderb.rebateflag,'N') = 'N'
		
//		group by sale.ccustomerid , salecont.vbillno, invbas.invname  , sale.period ,
//		salecontb.num,salecontb.preprice,cubas.custname,salecont.pk_contract,orderb.cinventoryid ,
//		sale.pk_corp
//		order by salecont.vbillno asc;		
		
		String sql = "select * from vw_pta_rebates where period = '"+period+"' and pk_corp = '"+_getCorp().getPk_corp()+"'";
		
		List<HashMap> retList = (ArrayList) UAPQueryBS.iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		if(retList != null && retList.size() > 0) {
			
			CalcRebatesBVO[] rebateBVOs = new CalcRebatesBVO[retList.size()];
			int row = 0;
			for(HashMap map : retList) {
				
				CalcRebatesBVO rebateBVO = new CalcRebatesBVO();
				String[] Attribute = rebateBVO.getAttributeNames();
				
				for(String attr : Attribute) {
					rebateBVO.setAttributeValue(attr, ConvertFunc.change(CalcRebatesBVO.class, attr, map.get(attr)));
				}
				
				rebateBVO.setAttributeValue("rowno", String.valueOf(row));
				
				rebateBVOs[row] = rebateBVO;
				
				row ++;
			}
			
			HYBillVO billVO = new HYBillVO();
			billVO.setParentVO(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
			billVO.setChildrenVO(rebateBVOs);
			getBillCardPanelWrapper().setCardData(billVO);
			
		} else 
			getBillUI().showWarningMessage("没有可统计的记录...");
	
	}
	
	protected void onBoMark() throws Exception {
		MarkDlg markDlg = MarkDlg.getInstance(getBillUI() , getUIController().getBillType());
		if(markDlg.showModal() == UIDialog.ID_OK) {
			
			if(markDlg.getFieldRef().getRefPK() == null || "".equals(markDlg.getFieldRef().getRefPK())) 
				throw new Exception ("属性为空，不能进行批改操作。");
			
			CalcRebatesBVO[] bodyVOs = (CalcRebatesBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
			CalcRebatesBVO[] selBodyVOs = (CalcRebatesBVO[]) getBillCardPanelWrapper().getBillCardPanel().getBillModel().getBodySelectedVOs(CalcRebatesBVO.class.getName());
			if(selBodyVOs == null || selBodyVOs.length == 0)
				selBodyVOs = (CalcRebatesBVO[]) getBillCardPanelWrapper().getSelectedBodyVOs();
			
			for(CalcRebatesBVO selbodyVO : selBodyVOs) {
				for(CalcRebatesBVO bodyVO : bodyVOs) {
					if(selbodyVO.getRowno().equals(bodyVO.getRowno())) {
						
						if(markDlg.getTxtValue() instanceof UITextField) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcRebatesBVO.class, markDlg.getFieldRef().getRefCode(), ((UITextField)markDlg.getTxtValue()).getText()));
						} else if(markDlg.getTxtValue() instanceof UICheckBox) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcRebatesBVO.class, markDlg.getFieldRef().getRefCode(), new UFBoolean(((UICheckBox)markDlg.getTxtValue()).isSelected())));
						} else if(markDlg.getTxtValue() instanceof UIRefPane) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcRebatesBVO.class, markDlg.getFieldRef().getRefCode(), ((UIRefPane)markDlg.getTxtValue()).getRefName()));
						}
						
						break;
					}
				}
				
			}
			
			HYBillVO billVO = new HYBillVO();
			billVO.setParentVO(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
			billVO.setChildrenVO(bodyVOs);
			getBillCardPanelWrapper().setCardData(billVO);
			
			for(int row = 0 , len = bodyVOs.length ; row < len ; row ++)
				getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row, ((ClientUI)getBillUI()).formulas);
			
			
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
		BillModel headModel = getBillCardPanelWrapper().getBillCardPanel().getBillModel("ehpta_calc_rebates_b");
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
			getButtonManager().getButton(DefaultBillButton.Support).setEnabled(false);
		} else if(getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {
			getButtonManager().getButton(DefaultBillButton.Maintain).setEnabled(true);
			getButtonManager().getButton(DefaultBillButton.Statistics).setEnabled(false);
			getButtonManager().getButton(DefaultBillButton.Support).setEnabled(false);
		} else {
			getButtonManager().getButton(DefaultBillButton.Maintain).setEnabled(false);
			getButtonManager().getButton(DefaultBillButton.Statistics).setEnabled(true);
		} 
		
		getBillUI().updateButtons();
		
	}
	
	@Override
	protected void onBoSave() throws Exception {
		
		Validata.saveValidataIsNull(getBillCardPanelWrapper().getBillCardPanel(), getBillCardPanelWrapper().getBillVOFromUI(), new String[]{"ehpta_calc_rebates_b"});
		
		AggregatedValueObject billVO = getBillCardPanelWrapper().getBillVOFromUI();
		
		if(billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0) {
			UFDate redate = new UFDate(billVO.getChildrenVO()[0].getAttributeValue("period") + "-01");
			if(redate != null) {
				String period = (String) billVO.getParentVO().getAttributeValue("period");
				if(!((redate.getYear() + "-" + (redate.getMonth() < 10 ? "0" + redate.getMonth() : "" + redate.getMonth())).equals(period))) {
					getBillCardPanelWrapper().getBillCardPanel().setHeadItem("period", redate.getYear() + "-" + (redate.getMonth() < 10 ? "0" + redate.getMonth() : "" + redate.getMonth()));
					throw new Exception("表体统计的数据与表头期间不一致，期间自动修改为正确期间");
				}
			}
			
			if(billVO.getParentVO().getPrimaryKey() != null) {
				billVO.getParentVO().setStatus(VOStatus.UPDATED);
				
				for(CircularlyAccessibleValueObject bodyVO : billVO.getChildrenVO()) {
					bodyVO.setStatus(VOStatus.UPDATED);
				}
				
				// 保存前先进行撤销的反写，以免单据内容修改导致反写的内容不一致
				afterOnBoDelete(getBufferData().getCurrentVO());
			}
			
		}
		
		
		
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = billVO;
		setTSFormBufferToVO(checkVO);
		// 进行数据晴空
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = billVO.getParentVO();
				billVO.setParentVO(null);
			} else {
				o = billVO.getChildrenVO();
				billVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// 判断是否有存盘数据
		if (billVO.getParentVO() == null
				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			if (getBillUI().isSaveAndCommitTogether())
				billVO = getBusinessAction().saveAndCommit(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
			else

				// write to database
				billVO = getBusinessAction().save(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
		}

		// 进行数据恢复处理
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			} else {
				getBufferData().addVOsToBuffer(
						new AggregatedValueObject[] { billVO });
				nCurrentRow = getBufferData().getVOBufferSize() - 1;
			}
		}

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRowWithOutTriggerEvent(nCurrentRow);
		}
		
		setAddNewOperate(isAdding(), billVO);

		// 设置保存后状态
		setSaveOperateState();
		
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
		
		afterOnBoSave();
		
	}
	
	protected final void afterOnBoSave() throws Exception {
		
		CalcRebatesBVO[] bodyVOs = (CalcRebatesBVO[]) getBufferData().getCurrentVO().getChildrenVO();
		List<String> orderbids = new ArrayList<String>();
		for(CalcRebatesBVO bodyVO : bodyVOs) {
			orderbids.add(bodyVO.getOrderbids());
		}
		
		String bidStr = ConvertFunc.change(orderbids.toArray(new String[0]));
		
		try { UAPQueryBS.iUAPQueryBS.executeQuery("update so_saleorder_b set rebateflag = 'Y' where corder_bid in ("+bidStr+")", null); } catch(Exception e) { Logger.info("返利统计保存后续操作，反写销售订单表体统计标志", this.getClass() , "afterOnBoSave"); }
		
	}
	
	@Override
	protected void onBoDelete() throws Exception {
		AggregatedValueObject currAggVO = getBufferData().getCurrentVO();
		
		super.onBoDelete();
		
		if(currAggVO != null) {
			Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(count(1),0) from ehpta_calc_rebates_h where pk_rebates = '"+currAggVO.getParentVO().getPrimaryKey()+"' and nvl(dr,0)=1", new ColumnProcessor());
			if(count > 0) {
				afterOnBoDelete(currAggVO);
			}
		}
		
	}
	
	protected final void afterOnBoDelete(AggregatedValueObject currAggVO) throws Exception {
		
		CalcRebatesBVO[] bodyVOs = (CalcRebatesBVO[]) currAggVO.getChildrenVO();
		List<String> orderbids = new ArrayList<String>();
		for(CalcRebatesBVO bodyVO : bodyVOs) {
			orderbids.add(bodyVO.getOrderbids());
		}
		
		String bidStr = ConvertFunc.change(orderbids.toArray(new String[0]));
		
		try { UAPQueryBS.iUAPQueryBS.executeQuery("update so_saleorder_b set rebateflag = 'N' where corder_bid in ("+bidStr+")", null); } catch(Exception e) { Logger.info("返利统计删除后续操作，反写销售订单表体统计标志", this.getClass() , "afterOnBoSave"); }
		
		
	}
	
	@Override
	protected void onBoCancelAudit() throws Exception {
		
		AggregatedValueObject  currVO = getBufferData().getCurrentVO();
		
		if(currVO != null && "Y".equals(currVO.getParentVO().getAttributeValue("def1"))) {
			getBillUI().showErrorMessage("当前记录已经生成余额调整单记录，不能进行弃审操作，请先进行取消推入操作后再进行弃审！");
			return ;
		}
			
		super.onBoCancelAudit();
	}
	
	/**
	 * 功能 ： 构造推入余额调整表的数据
	 * 
	 * 
	 * @param bodyVO
	 * @return
	 * @throws Exception
	 */
	protected final HYBillVO createAdjust(CalcRebatesBVO bodyVO) throws Exception {
		AdjustVO adjust = new AdjustVO();
		adjust.setAttributeValue("type", IAdjustType.Rebates);
		adjust.setAttributeValue("reason", "返利结算录入");
		adjust.setAttributeValue("mny", bodyVO.getActmny());
		adjust.setAttributeValue("pk_contract",bodyVO.getPk_contract());
		adjust.setAttributeValue("pk_cubasdoc", bodyVO.getPk_cumandoc());
		adjust.setAttributeValue("memo", "返利结算推式生成");
		adjust.setAttributeValue("vbillno",GeneraterBillNO.getInstanse().build("HQ07", _getCorp().getPk_corp()));
		adjust.setAttributeValue("adjustdate", _getDate());
		adjust.setAttributeValue("managerid", _getOperator());
		adjust.setAttributeValue("vbillstatus", 8);
		adjust.setAttributeValue("pk_corp", _getCorp().getPk_corp());
		adjust.setAttributeValue("pk_billtype", "HQ07");
		adjust.setAttributeValue("voperatorid", _getOperator());
		adjust.setAttributeValue("dmakedate", _getDate());
		adjust.setAttributeValue("def1", bodyVO.getPk_rebates_b());
		adjust.setAttributeValue("def2", "Y");
		adjust.setAttributeValue("def3", "N");
		
		HYBillVO billVO = new HYBillVO();
		billVO.setParentVO(adjust);
		
		return billVO;
	}

}