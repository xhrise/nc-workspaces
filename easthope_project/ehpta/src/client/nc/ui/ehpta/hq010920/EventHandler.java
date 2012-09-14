package nc.ui.ehpta.hq010920;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import nc.vo.ehpta.hq010920.CalcUpperTransfeeBVO;
import nc.vo.ehpta.hq010920.CalcUpperTransfeeHVO;
import nc.vo.fp.combase.pub01.IBillStatus;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
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
		BillModel headModel = getBillCardPanelWrapper().getBillCardPanel().getBillModel("ehpta_calc_upper_transfee_b");
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

	protected void onBoStatistics() throws Exception {
		
		CalcUpperTransfeeHVO uppertrans = (CalcUpperTransfeeHVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		
		if(uppertrans.getPeriod() == null || "".equals(uppertrans.getPeriod()))
			throw new Exception("请选择表头期间后再进行统计");
		
		UFDate period = new UFDate(uppertrans.getPeriod() + "-01");
		String lastDay = CalcFunc.builder(period);
		UFDate endPeriod = new UFDate(uppertrans.getPeriod() + "-" + lastDay);
		
		// 统计上游运费视图
//		create or replace view vw_upper_transfee as 
//		select ingenb.cgeneralhid def2,
//		       trim(substr(ingenb.vbatchcode, 0, instr(ingenb.vbatchcode, ' - '))) shipname,
//		       ingenh.vbillcode sourceno,
//		       ingenb.cinventoryid pk_invmandoc,
//		       invbas.invname def3,
//		       ingenh.cwarehouseid def4,
//		       instor.storname def5,
//		       outgenb.dbizdate sdate,
//		       nvl(outgenb.noutnum, 0) num,
//		       ingenb.dbizdate edate,
//		       nvl(ingenb.ninnum, 0) recnum,
//		       instor.pk_address def6,
//		       inaddr.addrname eaddr,
//		       nvl(outgenb.noutnum, 0) - nvl(ingenb.ninnum, 0) outnum,
//		       nvl(ingenh.vuserdef1, 0) fee,
//		       to_number(nvl(ingenh.vuserdef1, 0)) *
//		       to_number(nvl(ingenb.ninnum, 0)) transmny,
//		       to_number(nvl(ingenh.vuserdef1, 0)) *
//		       to_number(nvl(ingenb.ninnum, 0)) paymny
//		  from ic_general_b ingenb
//		  left join ic_general_h ingenh
//		    on ingenh.cgeneralhid = ingenb.cgeneralhid
//		  left join ic_general_b outgenb
//		    on outgenb.csourcebillbid = ingenb.csourcebillbid
//		  left join ic_general_h outgenh
//		    on outgenh.cgeneralhid = outgenb.cgeneralhid
//		  left join bd_invbasdoc invbas
//		    on invbas.pk_invbasdoc = ingenb.cinvbasid
//		  left join bd_stordoc instor
//		    on instor.pk_stordoc = ingenh.cwarehouseid
//		  left join bd_stordoc outstor
//		    on outstor.pk_stordoc = outgenh.cwarehouseid
//		  left join bd_address inaddr
//		    on inaddr.pk_address = instor.pk_address
//		 where ingenb.vbatchcode is not null
//		   and ingenb.csourcetype = '4K'
//		   and outgenb.csourcetype = '4K'
//		   and outgenh.cbilltypecode = '4I'
//		   and ingenh.cbilltypecode = '4A'
//		   
//		   and nvl(ingenh.vuserdef3, 'N') = 'N'
//		   and outgenh.cwarehouseid = '1120A7100000000Z1ZJX'
		
		
		String sql = "select * from vw_upper_transfee where edate >= '"+period.toString()+"' and edate <= '"+endPeriod.toString()+"'";
		List<HashMap> retList = (ArrayList) UAPQueryBS.iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		if(retList != null && retList.size() > 0) {
			
			CalcUpperTransfeeBVO[] uppertransBVOs = new CalcUpperTransfeeBVO[retList.size()];
			int row = 0;
			for(HashMap retMap : retList) {
				
				CalcUpperTransfeeBVO uppertransBVO = new CalcUpperTransfeeBVO();
				String[] Attribute = uppertransBVO.getAttributeNames();
				for(String attr : Attribute) {
					
					if("outnum".equals(attr)) {
						uppertransBVO.setAttributeValue(attr, new UFDouble(retMap.get("num").toString()).sub(new UFDouble(retMap.get("recnum").toString())));
					} else 
						uppertransBVO.setAttributeValue(attr, ConvertFunc.change(CalcUpperTransfeeBVO.class, attr, retMap.get(attr)));
					
				}
				
				uppertransBVO.setAttributeValue("def1", String.valueOf(row));
				
				uppertransBVOs[row] = uppertransBVO;
				
				row ++;
			}
			
			HYBillVO billVO = new HYBillVO();
			billVO.setParentVO(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
			billVO.setChildrenVO(uppertransBVOs);
			getBillCardPanelWrapper().setCardData(billVO);
			
		} else {
			
			getBillUI().showWarningMessage("没有可统计的记录...");
		}
		
		
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
			
			CalcUpperTransfeeBVO[] bodyVOs = (CalcUpperTransfeeBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
			CalcUpperTransfeeBVO[] selBodyVOs = (CalcUpperTransfeeBVO[]) getBillCardPanelWrapper().getBillCardPanel().getBillModel().getBodySelectedVOs(CalcUpperTransfeeBVO.class.getName());
			if(selBodyVOs == null || selBodyVOs.length == 0)
				selBodyVOs = (CalcUpperTransfeeBVO[]) getBillCardPanelWrapper().getSelectedBodyVOs();
			
			for(CalcUpperTransfeeBVO selbodyVO : selBodyVOs) {
				for(CalcUpperTransfeeBVO bodyVO : bodyVOs) {
					if(selbodyVO.getDef1().equals(bodyVO.getDef1())) {
						
						if(markDlg.getTxtValue() instanceof UITextField) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcUpperTransfeeBVO.class, markDlg.getFieldRef().getRefCode(), ((UITextField)markDlg.getTxtValue()).getText()));
						} else if(markDlg.getTxtValue() instanceof UICheckBox) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcUpperTransfeeBVO.class, markDlg.getFieldRef().getRefCode(), new UFBoolean(((UICheckBox)markDlg.getTxtValue()).isSelected())));
						} else if(markDlg.getTxtValue() instanceof UIRefPane) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcUpperTransfeeBVO.class, markDlg.getFieldRef().getRefCode(), ((UIRefPane)markDlg.getTxtValue()).getRefName()));
						}
						
						break;
					}
				}
				
			}
			
			HYBillVO billVO = new HYBillVO();
			billVO.setParentVO(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
			billVO.setChildrenVO(bodyVOs);
			getBillCardPanelWrapper().setCardData(billVO);
			
			for(int row = 0 , count = bodyVOs.length ; row < count ; row ++) 
				getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row, ((ClientUI)getBillUI()).bodyFamulas);
			
			
		}
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
	protected void onBoCard() throws Exception {
		super.onBoCard();
		
		afterOnButton();
	}
	
	@Override
	public void onButton(ButtonObject bo) {

		super.onButton(bo);
		
		afterOnButton();
		
	}
	
	@Override
	protected void onBoSave() throws Exception {
		
		Validata.saveValidataIsNull(getBillCardPanelWrapper().getBillCardPanel() , getBillCardPanelWrapper().getBillVOFromUI() , new String[]{"ehpta_calc_upper_transfee_b"});
		
		
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
		
		Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(count(1),0) from ehpta_calc_upper_transfee_h where period = '"+prePeriod+"'", new ColumnProcessor());
		if(count == 0) 
			getBillUI().showWarningMessage("前一期间的上游运费未统计...");
	}
	
	protected final void afterOnBoSave() throws Exception {
		
			
			CalcUpperTransfeeBVO[] currBodyVOs = (CalcUpperTransfeeBVO[]) getBufferData().getCurrentVO().getChildrenVO();
			
			for(CalcUpperTransfeeBVO bodyVO : currBodyVOs) {
				bodyVO.setSettleflag(new UFBoolean("Y"));
				bodyVO.setSettledate(_getDate());
			}
			
			HYPubBO_Client.updateAry(currBodyVOs);
			
			AggregatedValueObject newAggVO = HYPubBO_Client.queryBillVOByPrimaryKey(getUIController().getBillVoName() , getBufferData().getCurrentVO().getParentVO().getPrimaryKey());
			
			getBufferData().setVOAt(getBufferData().getCurrentRow(), newAggVO);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			
			for(CalcUpperTransfeeBVO bodyVO : currBodyVOs) {
				Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(count(1),0) from ic_general_h where cgeneralhid = '"+bodyVO.getDef2()+"' and nvl(vuserdef3,'N') = 'Y'", new ColumnProcessor());
				
				if(count == 0) {
					try { UAPQueryBS.iUAPQueryBS.executeQuery("update ic_general_h set vuserdef3 = 'Y' where cgeneralhid = '"+bodyVO.getDef2()+"' ", null); } catch(Exception e) { }
				}	
			}
			
		
	}
	
	@Override
	protected void onBoDelete() throws Exception {
		
		AggregatedValueObject currAggVO = getBufferData().getCurrentVO();
		
		super.onBoDelete();
		
		if(currAggVO != null) {
			Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(count(1),0) from ehpta_calc_upper_transfee_h where pk_transfee = '"+currAggVO.getParentVO().getPrimaryKey()+"' and nvl(dr,0)=1", new ColumnProcessor());
			if(count > 0) {
				
				afterOnBoDelete(currAggVO);
			}
		}
		
	}
	
	protected final void afterOnBoDelete(AggregatedValueObject currAggVO ) throws Exception {
			
		if(currAggVO == null || currAggVO.getChildrenVO() == null || currAggVO.getChildrenVO().length == 0)
			return ;
		
		for(CalcUpperTransfeeBVO bodyVO : (CalcUpperTransfeeBVO[])currAggVO.getChildrenVO()) {
			Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(count(1),0) from ic_general_h where cgeneralhid = '"+bodyVO.getDef2()+"' and nvl(vuserdef3,'N') = 'Y'", new ColumnProcessor());
			
			if(count > 0) {
				try { UAPQueryBS.iUAPQueryBS.executeQuery("update ic_general_h set vuserdef3 = 'N' where cgeneralhid = '"+bodyVO.getDef2()+"' ", null); } catch(Exception e) { }
			}	
		}
			
	}
	
	/**
	 * 功能 ： 构造推入余额调整表的数据
	 * 
	 * 
	 * @param bodyVO
	 * @return
	 * @throws Exception
	 */
	protected final HYBillVO createAdjust(CalcUpperTransfeeBVO bodyVO) throws Exception {
		AdjustVO adjust = new AdjustVO();
		adjust.setAttributeValue("type", IAdjustType.Discount);
		adjust.setAttributeValue("reason", "上游运费结算录入");
		adjust.setAttributeValue("mny", bodyVO.getTransmny());
		adjust.setAttributeValue("pk_contract",bodyVO.getDef5());
		adjust.setAttributeValue("pk_cubasdoc", bodyVO.getDef3());
		adjust.setAttributeValue("memo", "上游运费结算推式生成");
		adjust.setAttributeValue("vbillno",GeneraterBillNO.getInstanse().build("HQ07", _getCorp().getPk_corp()));
		adjust.setAttributeValue("adjustdate", _getDate());
		adjust.setAttributeValue("managerid", _getOperator());
		adjust.setAttributeValue("vbillstatus", 8);
		adjust.setAttributeValue("pk_corp", _getCorp().getPk_corp());
		adjust.setAttributeValue("pk_billtype", "HQ07");
		adjust.setAttributeValue("voperatorid", _getOperator());
		adjust.setAttributeValue("dmakedate", _getDate());
		adjust.setAttributeValue("def1", bodyVO.getDef2());
		adjust.setAttributeValue("def2", "Y");
		adjust.setAttributeValue("def3", "N");
		
		HYBillVO billVO = new HYBillVO();
		billVO.setParentVO(adjust);
		
		return billVO;
	}
}