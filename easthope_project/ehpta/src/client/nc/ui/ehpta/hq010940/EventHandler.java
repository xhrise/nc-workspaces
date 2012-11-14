package nc.ui.ehpta.hq010940;

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
import nc.vo.ehpta.hq010940.CalcStorfeeBVO;
import nc.vo.ehpta.hq010940.CalcStorfeeHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
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
	
			default:
	
				break;
		}
	}
	
	protected void onBoStatistics() throws Exception {
		
		CalcStorfeeHVO undertrans = (CalcStorfeeHVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		
		if(undertrans.getPeriod() == null || "".equals(undertrans.getPeriod()))
			throw new Exception("请选择表头期间后再进行统计");
		
		UFDate period = new UFDate(undertrans.getPeriod() + "-01");
		String lastDay = CalcFunc.getLastDay(period);
		UFDate endPeriod = new UFDate(undertrans.getPeriod() + "-" + lastDay);
		
//		create or replace view vw_pta_storfee as
//		select genb.vbatchcode , genh.cwarehouseid pk_stordoc , genh.pk_contract , genh.concode , decode(genh.contracttype , 10 , '现货合同' , 20 , '长单合同' , genh.contracttype) contracttype ,
//		genb.cinventoryid pk_invmandoc , invbas.invname ,
//		(select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0) indate , 
//		-- to_char(to_date(trim(substr(genb.vbatchcode, instr(genb.vbatchcode, ' - ') + 3 , 8)) , 'yyyy-MM-dd'),'yyyy-MM-dd') indate ,
//		genh.dbilldate outdate , genh.ccustomerid pk_cumandoc , sale.vreceiptcode ,
//		genb.cgeneralbid , genb.cgeneralhid , genh.vbillcode outcode , saleb.deliverydate overdate , genb.noutnum ,
//		decode(storcontb.feetype , 1 , '仓库费' , 2 , '直驳费' , 3 , '船-库-船' , 4 , '船-库-车' , 5 , '直驳费（船-船）' , 6 , '直驳费（船-车）' , null) feetype ,
//		-- storcontb.signprice , nvl(genb.noutnum,0) * nvl(storcontb.signprice,0) hmny ,
//
//		(case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genh.dbilldate , 'yyyy-MM-dd') > 0 then to_date(genh.dbilldate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0) , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0) , 'yyyy-MM-dd') end ) + 1 stordays ,
//		nvl(storcontb1.concesessionsday,0) freedays ,
//		case when ((case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genh.dbilldate , 'yyyy-MM-dd') > 0 then to_date(genh.dbilldate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0) , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0) , 'yyyy-MM-dd') end ) + 1) - nvl(storcontb1.concesessionsday,0) < 0 then 0 else ((case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genh.dbilldate , 'yyyy-MM-dd') > 0 then to_date(genh.dbilldate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0) , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0) , 'yyyy-MM-dd') end ) + 1) - nvl(storcontb1.concesessionsday,0) end days ,
//		storcontb1.signprice storprice ,
//		(case when ((case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genh.dbilldate , 'yyyy-MM-dd') > 0 then to_date(genh.dbilldate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0) , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0) , 'yyyy-MM-dd') end ) + 1) - nvl(storcontb1.concesessionsday,0) < 0 then 0 else ((case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genh.dbilldate , 'yyyy-MM-dd') > 0 then to_date(genh.dbilldate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0) , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0) , 'yyyy-MM-dd') end ) + 1) - nvl(storcontb1.concesessionsday,0) end) * storcontb1.signprice * genb.noutnum stormny
//
//		from ic_general_h genh
//		left join ic_general_b genb on genb.cgeneralhid = genh.cgeneralhid
//		left join so_sale sale on sale.csaleid = genb.csourcebillhid
//		left join so_saleorder_b saleb on saleb.corder_bid = genb.csourcebillbid
//		left join ehpta_storcontract_b storcontb on storcontb.pk_storcontract_b = genh.pk_storcontract_b
//		left join ehpta_storcontract storcont on storcont.pk_stordoc = genh.cwarehouseid
//		left join ehpta_storcontract_b storcontb1 on storcontb1.pk_storagedoc = storcont.pk_storagedoc
//		left join bd_invbasdoc invbas on invbas.pk_invbasdoc = genb.cinvbasid
//
//		where genh.daccountdate is not null
//
//		and decode(genh.vuserdef4 , 'Y' , 'Y' , 'N' , 'N' , 'N') = 'N' and genh.concode is not null
//		and (nvl(genh.contracttype , 0) = 10 or nvl(genh.contracttype , 0) = 20) and nvl(genh.storprice,0) > 0
//		and genb.vbatchcode is not null and nvl(storcontb1.feetype , '0') = '1'
//		and storcont.vbillstatus = 1 and genh.fbillflag = 3
//
//		and nvl(genh.dr,0) = 0 and nvl(genb.dr,0) = 0
//		and nvl(sale.dr,0) = 0 and nvl(saleb.dr,0) = 0
//		and nvl(storcontb.dr,0) = 0 and nvl(storcont.dr,0) = 0 and nvl(storcontb1.dr,0) = 0
//		and nvl(invbas.dr , 0 ) = 0;

		String sql = "select * from vw_pta_storfee where outdate >= '"+period.toString()+"' and outdate <= '"+endPeriod.toString()+"'";
		List<HashMap> retList = (ArrayList) UAPQueryBS.getInstance().executeQuery(sql, new MapListProcessor());
		if(retList != null && retList.size() > 0) {
			
			CalcStorfeeBVO[] storfeeBVOs = new CalcStorfeeBVO[retList.size()];
			int row = 0;
			for(HashMap map : retList) {
				
				CalcStorfeeBVO storfeeBVO = new CalcStorfeeBVO();
				String[] Attribute = storfeeBVO.getAttributeNames();
				
				for(String attr : Attribute) {
					storfeeBVO.setAttributeValue(attr, ConvertFunc.change(CalcStorfeeBVO.class, attr, map.get(attr)));
				}
				
				storfeeBVO.setAttributeValue("rowno", String.valueOf(row));
				
				storfeeBVOs[row] = storfeeBVO;
				
				row ++;
			}
			
			HYBillVO billVO = new HYBillVO();
			billVO.setParentVO(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
			billVO.setChildrenVO(storfeeBVOs);
			getBillCardPanelWrapper().setCardData(billVO);
			
		} else 
			getBillUI().showWarningMessage("没有可统计的记录...");
		
	}
	
	protected void onBoMark() throws Exception {
		
		MarkDlg markDlg = MarkDlg.getInstance(getBillUI() , getUIController().getBillType());
		if(markDlg.showModal() == UIDialog.ID_OK) {
			
			if(markDlg.getFieldRef().getRefPK() == null || "".equals(markDlg.getFieldRef().getRefPK())) 
				throw new Exception ("属性为空，不能进行批改操作。");
			
			CalcStorfeeBVO[] bodyVOs = (CalcStorfeeBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
			CalcStorfeeBVO[] selBodyVOs = (CalcStorfeeBVO[]) getBillCardPanelWrapper().getBillCardPanel().getBillModel().getBodySelectedVOs(CalcStorfeeBVO.class.getName());
			if(selBodyVOs == null || selBodyVOs.length == 0)
				selBodyVOs = (CalcStorfeeBVO[]) getBillCardPanelWrapper().getSelectedBodyVOs();
			
			if(selBodyVOs == null || selBodyVOs.length == 0)
				throw new Exception ("请至少选择一条记录进行批改操作。");
			
			for(CalcStorfeeBVO selbodyVO : selBodyVOs) {
				for(CalcStorfeeBVO bodyVO : bodyVOs) {
					if(selbodyVO.getRowno().equals(bodyVO.getRowno())) {
						
						if(markDlg.getTxtValue() instanceof UITextField) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcStorfeeBVO.class, markDlg.getFieldRef().getRefCode(), ((UITextField)markDlg.getTxtValue()).getText()));
						} else if(markDlg.getTxtValue() instanceof UICheckBox) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcStorfeeBVO.class, markDlg.getFieldRef().getRefCode(), new UFBoolean(((UICheckBox)markDlg.getTxtValue()).isSelected())));
						} else if(markDlg.getTxtValue() instanceof UIRefPane) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcStorfeeBVO.class, markDlg.getFieldRef().getRefCode(), ((UIRefPane)markDlg.getTxtValue()).getRefName()));
						}
						
						break;
					}
				}
				
			}
			
			HYBillVO billVO = new HYBillVO();
			billVO.setParentVO(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
			billVO.setChildrenVO(bodyVOs);
			getBillCardPanelWrapper().setCardData(billVO);
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
		BillModel headModel = getBillCardPanelWrapper().getBillCardPanel().getBillModel("ehpta_calc_storfee_b");
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
		
		Validata.saveValidataIsNull(getBillCardPanelWrapper().getBillCardPanel(), getBillCardPanelWrapper().getBillVOFromUI(), new String[]{"ehpta_calc_storfee_b"});
		
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
	
	protected final void validPrevious() throws Exception {
		Object period = getBufferData().getCurrentVO().getParentVO().getAttributeValue("period");
		String prePeriod = "";
		if(period != null) {
			period = period + "-01";
			UFDate periodDate = new UFDate(period.toString());
			prePeriod = periodDate.getYear() + "-" + (periodDate.getMonth() - 1 < 10 ? "0" + (periodDate.getMonth() - 1) : "" + (periodDate.getMonth() - 1));
		}
		
		Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from ehpta_calc_storfee_h where period = '"+prePeriod+"'", new ColumnProcessor());
		if(count == 0) 
			getBillUI().showWarningMessage("前一期间的仓储及装卸费未统计...");
	}
	
	protected final void afterOnBoSave() throws Exception {

		CalcStorfeeBVO[] currBodyVOs = (CalcStorfeeBVO[]) getBufferData().getCurrentVO().getChildrenVO();
		
		for(CalcStorfeeBVO bodyVO : currBodyVOs) {
			bodyVO.setSettleflag(new UFBoolean("Y"));
			bodyVO.setSettledate(_getDate());
		}
		
		HYPubBO_Client.updateAry(currBodyVOs);
		
		AggregatedValueObject newAggVO = HYPubBO_Client.queryBillVOByPrimaryKey(getUIController().getBillVoName(), getBufferData().getCurrentVO().getParentVO().getPrimaryKey());
		
		getBufferData().setVOAt(getBufferData().getCurrentRow(), newAggVO);
		getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		
//		List<HYBillVO> adjustList = new ArrayList<HYBillVO>();
//		List<String> flagPks = new ArrayList<String>();
		
		for(CalcStorfeeBVO bodyVO : currBodyVOs) {
			Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from ic_general_h where cgeneralhid = '"+bodyVO.getCgeneralhid()+"' and nvl(vuserdef4,'N') = 'Y'", new ColumnProcessor());
			
			if(count == 0) {
				try { UAPQueryBS.getInstance().executeQuery("update ic_general_h set vuserdef4 = 'Y' where cgeneralhid = '"+bodyVO.getCgeneralhid()+"' ", null); } catch(Exception e) { }
//				adjustList.add(createAdjust(bodyVO , IAdjustType.Storfee , bodyVO.getStormny())); // 仓储费
//				adjustList.add(createAdjust(bodyVO , IAdjustType.Handlingfee , bodyVO.getHmny())); // 装卸费
			}	//else {
//				flagPks.add("'" + bodyVO.getCgeneralbid() + "'");
//			}
		}
		
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
//					for(CalcStorfeeBVO bodyVO : currBodyVOs) {
//						if(adjust.getDef1().equals(bodyVO.getDef5())) {
//							
//							if(IAdjustType.Storfee.equals(adjust.getType()))
//								adjust.setMny(bodyVO.getStormny());
//							else if(IAdjustType.Handlingfee.equals(adjust.getType()))
//								adjust.setMny(bodyVO.getHmny());
//							
//						}
//							
//					}
//				}
//				
//				HYPubBO_Client.updateAry(adjustArr); 
//				
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
			Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from ehpta_calc_storfee_h where pk_storfee = '"+currAggVO.getParentVO().getPrimaryKey()+"' and nvl(dr,0)=1", new ColumnProcessor());
			if(count > 0) {
				afterOnBoDelete(currAggVO);
			}
		}
		
		
	}
	
	protected final void afterOnBoDelete(AggregatedValueObject currAggVO) throws Exception {
		
//		Object userObj = new ClientUICheckRuleGetter();
		
//		List<String> adjustList = new ArrayList<String>();
		
		if(currAggVO == null || currAggVO.getChildrenVO() == null || currAggVO.getChildrenVO().length == 0)
			return ;
		
		for(CalcStorfeeBVO bodyVO : (CalcStorfeeBVO[])currAggVO.getChildrenVO()) {
			Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from ic_general_h where cgeneralhid = '"+bodyVO.getCgeneralhid()+"' and nvl(vuserdef4,'N') = 'Y'", new ColumnProcessor());
			
			if(count > 0) {
				try { UAPQueryBS.getInstance().executeQuery("update ic_general_h set vuserdef4 = 'N' where cgeneralhid = '"+bodyVO.getCgeneralhid()+"' ", null); } catch(Exception e) { }
//				adjustList.add("'" + bodyVO.getCgeneralbid() + "'");
			}	
		}
		
//		SuperVO[] superVos = HYPubBO_Client.queryByCondition(AdjustVO.class, " def1 in (" + ConvertFunc.change(adjustList.toArray(new String[0])) + ") and ( type = "+IAdjustType.Storfee+" or type = "+IAdjustType.Handlingfee+" ) and nvl(dr,0) = 0 ");
//		
//		List<HYBillVO> billVOs = new ArrayList<HYBillVO>();
//		for (SuperVO superVO : superVos) {
//			HYBillVO billVO = new HYBillVO();
//			billVO.setParentVO(superVO);
//			billVOs.add(billVO);
//		}
		
//		if (billVOs != null && billVOs.size() > 0) {
//			for (AggregatedValueObject billVO : billVOs) {
//
//				try {
//					getBusinessAction().unapprove(billVO, "HQ07", billVO.getParentVO().getAttributeValue("dapprovedate").toString(), userObj);
//				
//				} catch (Exception e) {
//					Logger.error(e);
//					throw new Exception(e);
//				}
//				
//				try {
//					SuperVO adjust = HYPubBO_Client.queryByPrimaryKey( AdjustVO.class, billVO.getParentVO().getPrimaryKey());
//					HYBillVO newBillVO = new HYBillVO();
//					newBillVO.setParentVO(adjust);
//
//					getBusinessAction().delete(newBillVO, "HQ07", billVO.getParentVO().getAttributeValue("dapprovedate").toString(), userObj);
//				
//				} catch (Exception e) {
//
//					Logger.error(e);
//					throw new Exception(e);
//					
//				}
//
//			}
//
//		}
		
		for(CalcStorfeeBVO bodyVO : (CalcStorfeeBVO[])currAggVO.getChildrenVO()) {
			bodyVO.setSettleflag(new UFBoolean("N"));
			bodyVO.setSettledate(null);
		}
		
		HYPubBO_Client.updateAry((CalcStorfeeBVO[])currAggVO.getChildrenVO());
		
	}
	
	/**
	 * 功能 ： 构造推入余额调整表的数据
	 * 
	 * 
	 * @param bodyVO
	 * @return
	 * @throws Exception
	 */
	protected final HYBillVO createAdjust(CalcStorfeeBVO bodyVO , String type , UFDouble mny) throws Exception {
		AdjustVO adjust = new AdjustVO();
		adjust.setAttributeValue("type", type);
		adjust.setAttributeValue("reason", type == IAdjustType.Storfee ? "仓储费结算录入" : type == IAdjustType.Handlingfee ? "装卸费结算录入" : null);
		adjust.setAttributeValue("mny", mny);
		adjust.setAttributeValue("pk_contract",bodyVO.getPk_contract());
		adjust.setAttributeValue("pk_cubasdoc", bodyVO.getPk_cumandoc());
		adjust.setAttributeValue("memo", type == IAdjustType.Storfee ? "仓储费结算推式生成" : type == IAdjustType.Handlingfee ? "装卸费结算推式生成" : null);
		adjust.setAttributeValue("vbillno",GeneraterBillNO.getInstanse().build("HQ07", _getCorp().getPk_corp()));
		adjust.setAttributeValue("adjustdate", _getDate());
		adjust.setAttributeValue("managerid", _getOperator());
		adjust.setAttributeValue("vbillstatus", 8);
		adjust.setAttributeValue("pk_corp", _getCorp().getPk_corp());
		adjust.setAttributeValue("pk_billtype", "HQ07");
		adjust.setAttributeValue("voperatorid", _getOperator());
		adjust.setAttributeValue("dmakedate", _getDate());
		adjust.setAttributeValue("def1", bodyVO.getCgeneralbid());
		adjust.setAttributeValue("def2", "Y");
		adjust.setAttributeValue("def3", "N");
		
		HYBillVO billVO = new HYBillVO();
		billVO.setParentVO(adjust);
		
		return billVO;
	}
	
	@Override
	protected void onBoCancelAudit() throws Exception {
		
		CircularlyAccessibleValueObject[] bodyVOs = getBufferData().getCurrentVO().getChildrenVO();
		List<String> flagPks = new ArrayList<String>();
		for(CircularlyAccessibleValueObject bodyVO : bodyVOs) {
			flagPks.add("'" + bodyVO.getAttributeValue("cgeneralbid") + "'");
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