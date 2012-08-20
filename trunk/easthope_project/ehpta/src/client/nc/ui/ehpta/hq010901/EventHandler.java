package nc.ui.ehpta.hq010901;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.ehpta.pub.IAdjustType;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.calc.CalcFunc;
import nc.ui.ehpta.pub.convert.ConvertFunc;
import nc.ui.ehpta.pub.convert.MarkDlg;
import nc.ui.ehpta.pub.gen.GeneraterBillNO;
import nc.ui.ehpta.pub.valid.Validata;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.ehpta.hq010403.AdjustVO;
import nc.vo.ehpta.hq010901.CalcInterestBVO;
import nc.vo.ehpta.hq010901.CalcInterestVO;
import nc.vo.fp.combase.pub01.IBillStatus;
import nc.vo.pub.AggregatedValueObject;
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
@SuppressWarnings({"unchecked" , "rawtypes"})
public class EventHandler extends ManageEventHandler {

	private final String[] bodyFamulas = new String[]{
		"interestmny->(remny * days * rate / 100) / 360",
		"actualmny->interestmny",
	};
	
	public EventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {
		
		switch(intBtn) {
		
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
	
	protected final String[] getBillTableCode() {
		return new String[]{"ehpta_calc_interest_b"};
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		
		super.onBoEdit();
	}
	
	@Override
	protected void onBoSave() throws Exception {
		
		Validata.saveValidataIsNull(getBillCardPanelWrapper().getBillCardPanel() , getBillCardPanelWrapper().getBillVOFromUI() , getBillTableCode());
		
		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
		if(billVO.getParentVO().getPrimaryKey() == null || "".equals(billVO.getParentVO().getPrimaryKey()))
			super.onBoSave();
		else {
			
			HYPubBO_Client.deleteByWhereClause(CalcInterestBVO.class, " 1 = 1 and pk_calcinterest = '"+billVO.getParentVO().getPrimaryKey()+"' ");
			super.onBoSave();
		}
			
	}
	
	protected void onBoSelAll() throws Exception {
		selectAll( true);
	}
	
	protected void onBoSelNone() throws Exception {
		selectAll( false);
	}
	
	private final void selectAll( boolean isNeedSelected) throws Exception {
		int row = getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO().length;
		BillModel headModel = getBillCardPanelWrapper().getBillCardPanel().getBillModel(getBillTableCode()[0]);
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

	
	/**
	 * 功能 ： 统计收款单记录
	 * @throws Exception
	 */
	protected void onBoStatistics() throws Exception {
		CalcInterestVO interest = (CalcInterestVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		
		if(interest.getPeriod() == null || "".equals(interest.getPeriod()))
			throw new Exception("请选择表头期间后再进行统计");
		
		UFDate period = new UFDate(interest.getPeriod() + "-01");
		String lastDay = CalcFunc.bulder(period);
		
		UFDate endPeriod = new UFDate(interest.getPeriod() + "-" + lastDay);
		
		String sqlField = ConvertFunc.change(new String[]{
				"distinct cubas.custname custname" ,
				"zb.vouchid pk_receivable" , 
				"zb.djbh djzbid" , 
				"contract.pk_contract def5" ,
				"contract.vbillno contno" , 
				"to_char(contract.version) version" , 
				"case when contract.contype = '现货合同' then '10' when contract.contype = '长单合同' then '20' else '' end transtype" , 
				"zb.effectdate redate" , 
				"fb.dfybje remny" , 
				"cubas.custname interestto" , 
				"case when zb.effectdate is null or zb.effectdate = '' then 0 else (to_date(to_char(sysdate, 'yyyy-MM-dd') ,'yyyy-MM-dd') - to_date(zb.effectdate,'yyyy-MM-dd') + 1) end days " ,
				"bala.balanname billtype" ,
				
				"zb.pj_jsfs def1" , 
				"fb.hbbm def2" ,
				"fb.hbbm def3" ,
		});
		
		StringBuilder builder = new StringBuilder();
		builder.append(" select "+ sqlField +" from arap_djzb zb  ");
		builder.append(" left join arap_djfb fb on fb.vouchid = zb.vouchid  ");
		builder.append(" left join ehpta_sale_contract contract on zb.zyx6 = contract.pk_contract ");
		builder.append(" left join bd_balatype bala on bala.pk_balatype = zb.pj_jsfs ");
		builder.append(" left join bd_cubasdoc cubas on cubas.pk_cubasdoc = fb.hbbm ");
		builder.append(" left join bd_cumandoc cuman on cuman.pk_cubasdoc = cubas.pk_cubasdoc ");
		
		if(interest.getPk_custdoc() != null && !"".equals(interest.getPk_custdoc()))
			builder.append(" where zb.zyx6 is not null and zb.effectdate >= '"+period.toString()+"' and zb.effectdate <= '"+endPeriod.toString()+"' and cuman.pk_cumandoc = '"+interest.getPk_custdoc()+"' and cuman.pk_corp = '"+_getCorp().getPk_corp()+"' ");
		else 
			builder.append(" where zb.zyx6 is not null and zb.effectdate >= '"+period.toString()+"' and zb.effectdate <= '"+endPeriod.toString()+"' and cuman.pk_corp = '"+_getCorp().getPk_corp()+"' ");
	
		builder.append(" and nvl(zb.dr,0)=0 and nvl(fb.dr,0)=0 and nvl(contract.dr,0)=0 and nvl(bala.dr,0)=0 and nvl(cubas.dr,0)=0 and nvl(cuman.dr,0)=0 ");
		builder.append(" and zb.djzt = 3 ");
		
		List<HashMap> retList = (ArrayList) UAPQueryBS.iUAPQueryBS.executeQuery(builder.toString(), new MapListProcessor());
		
		if(retList != null && retList.size() > 0) {
			CalcInterestBVO[] interestBs = new CalcInterestBVO[retList.size()];
			int row = 0;
			for(HashMap retMap : retList) {
				
				CalcInterestBVO interestB = new CalcInterestBVO();
				for(String attr : interestB.getAttributeNames()) {
						
					interestB.setAttributeValue(attr, ConvertFunc.change(CalcInterestBVO.class, attr, retMap.get(attr)));
				
					if(((Integer)retMap.get("days")) > 0) { 
						interestB.setAttributeValue("interestto", "东方希望集团有限公司");
						interestB.setAttributeValue("def2", "0001A8100000000043HA");
					}
					
				}
				
				interestB.setDef4(String.valueOf(row));
				
				interestBs[row] = interestB;
				row ++;
			}
			
			HYBillVO billVO = new HYBillVO();
			billVO.setParentVO(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
			billVO.setChildrenVO(interestBs);
			getBillCardPanelWrapper().setCardData(billVO);
			
		}
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
		
		if(getBillUI().getBillOperate() == IBillOperate.OP_EDIT || getBillUI().getBillOperate() == IBillOperate.OP_ADD) {
			getButtonManager().getButton(DefaultBillButton.Maintain).setEnabled(true);
		} else {
			getButtonManager().getButton(DefaultBillButton.Maintain).setEnabled(false);
		}
		
		getBillUI().updateButtons();
		
	}
	
	@Override
	protected void onBoCard() throws Exception {
		super.onBoCard();
		
		afterOnButton();
	}
	
	protected void onBoMark() throws Exception {
		
		MarkDlg markDlg = MarkDlg.getInstance(getBillUI() , getUIController().getBillType());
		if(markDlg.showModal() == UIDialog.ID_OK) {
			
			if(markDlg.getFieldRef().getRefPK() == null || "".equals(markDlg.getFieldRef().getRefPK())) 
				throw new Exception ("属性为空，不能进行批改操作。");
			
			CalcInterestBVO[] bodyVOs = (CalcInterestBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
			CalcInterestBVO[] selBodyVOs = (CalcInterestBVO[]) getBillCardPanelWrapper().getBillCardPanel().getBillModel().getBodySelectedVOs(CalcInterestBVO.class.getName());
			if(selBodyVOs == null || selBodyVOs.length == 0)
				selBodyVOs = (CalcInterestBVO[]) getBillCardPanelWrapper().getSelectedBodyVOs();
			
			for(CalcInterestBVO selbodyVO : selBodyVOs) {
				
				for(CalcInterestBVO bodyVO : bodyVOs) {
					if(selbodyVO.getDef4().equals(bodyVO.getDef4())) {
						bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcInterestBVO.class, markDlg.getFieldRef().getRefCode(), markDlg.getTxtValue().getText()));
						break;
					}
				}
				
			}
			
			HYBillVO billVO = new HYBillVO();
			billVO.setParentVO(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
			billVO.setChildrenVO(bodyVOs);
			getBillCardPanelWrapper().setCardData(billVO);
			
			for(int row = 0 , count = bodyVOs.length ; row < count ; row ++) 
				getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row, bodyFamulas);
		}
	}
	
	@Override
	public void onBoAudit() throws Exception {
		super.onBoAudit();
		
		afterOnBoAudit();
	}
	
	protected final void afterOnBoAudit() throws Exception {

		if(Integer.valueOf(getBufferData().getCurrentVO().getParentVO().getAttributeValue("vbillstatus").toString()) == IBillStatus.CHECKPASS) {
			
			CalcInterestBVO[] currBodyVOs = (CalcInterestBVO[]) getBufferData().getCurrentVO().getChildrenVO();
			
			for(CalcInterestBVO bodyVO : currBodyVOs) {
				bodyVO.setCalcflag(new UFBoolean("Y"));
			}
			
			HYPubBO_Client.updateAry(currBodyVOs);
			
			AggregatedValueObject newAggVO = HYPubBO_Client.queryBillVOByPrimaryKey(new String[]{
					HYBillVO.class.getName(),
					CalcInterestVO.class.getName(),
					CalcInterestBVO.class.getName(),
			}, getBufferData().getCurrentVO().getParentVO().getPrimaryKey());
			
			getBufferData().setVOAt(getBufferData().getCurrentRow(), newAggVO);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			
			List<HYBillVO> adjustList = new ArrayList<HYBillVO>();
			
			for(CalcInterestBVO bodyVO : currBodyVOs) {
				Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(count(1)) from arap_djzb where vouchid = '"+bodyVO.getPk_receivable()+"' and nvl(zyx8,'N') = 'Y'", new ColumnProcessor());
				
				if(count == 0) {
					try { UAPQueryBS.iUAPQueryBS.executeQuery("update table arap_djzb set zyx8 = 'Y' where vouchid = '"+bodyVO.getPk_receivable()+"' ", null); } catch(Exception e) { }
					adjustList.add(createAdjust(bodyVO));
				}	
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
	protected final HYBillVO createAdjust(CalcInterestBVO bodyVO) throws Exception {
		AdjustVO adjust = new AdjustVO();
		adjust.setAttributeValue("type", IAdjustType.Discount);
		adjust.setAttributeValue("reason", "贴息录入");
		adjust.setAttributeValue("mny", bodyVO.getActualmny());
		adjust.setAttributeValue("pk_contract",bodyVO.getDef5());
		adjust.setAttributeValue("pk_cubasdoc", bodyVO.getDef3());
		adjust.setAttributeValue("memo", "贴息结算推式生成");
		adjust.setAttributeValue("vbillno",GeneraterBillNO.getInstanse().build("HQ07", _getCorp().getPk_corp()));
		adjust.setAttributeValue("adjustdate", _getDate());
		adjust.setAttributeValue("managerid", _getOperator());
		adjust.setAttributeValue("vbillstatus", 8);
		adjust.setAttributeValue("pk_corp", _getCorp().getPk_corp());
		adjust.setAttributeValue("pk_billtype", "HQ07");
		adjust.setAttributeValue("voperatorid", _getOperator());
		adjust.setAttributeValue("dmakedate", _getDate());
		adjust.setAttributeValue("def1", bodyVO.getPk_receivable());
		adjust.setAttributeValue("def2", "Y");
		adjust.setAttributeValue("def3", "N");
		
		HYBillVO billVO = new HYBillVO();
		billVO.setParentVO(adjust);
		
		return billVO;
	}

}