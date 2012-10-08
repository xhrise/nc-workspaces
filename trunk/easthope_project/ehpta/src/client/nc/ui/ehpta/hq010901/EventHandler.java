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
import nc.vo.ehpta.hq010901.CalcInterestBVO;
import nc.vo.ehpta.hq010901.CalcInterestVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
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

	protected Object oldPk_custdoc = null;
	
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
		String primaryKey = getBillCardPanelWrapper().getBillVOFromUI().getParentVO().getPrimaryKey();
		if(primaryKey != null && !"".equals(primaryKey))
			oldPk_custdoc = getBillCardPanelWrapper().getBillVOFromUI().getParentVO().getAttributeValue("pk_custdoc");
		
		super.onBoEdit();
	}
	
	@Override
	protected void onBoCancel() throws Exception {
		
		super.onBoCancel();
		
		oldPk_custdoc = null;
	}
	
	@Override
	protected void onBoSave() throws Exception {
		
		Validata.saveValidataIsNull(getBillCardPanelWrapper().getBillCardPanel() , getBillCardPanelWrapper().getBillVOFromUI() , getBillTableCode());
		
		AggregatedValueObject billVO = getBillCardPanelWrapper().getBillVOFromUI();
		
		if(billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0) {
			UFDate redate = (UFDate) billVO.getChildrenVO()[0].getAttributeValue("redate");
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
		
		validPrevious();
			
		
	}
	
	@Override
	protected void onBoDelete() throws Exception {
		AggregatedValueObject currAggVO = getBufferData().getCurrentVO();
		
		super.onBoDelete();
		
		if(currAggVO != null) {
			Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(count(1),0) from ehpta_calc_interest where pk_calcinterest = '"+currAggVO.getParentVO().getPrimaryKey()+"' and nvl(dr,0)=1", new ColumnProcessor());
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
		
		Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(count(1),0) from ehpta_calc_interest where period = '"+prePeriod+"'", new ColumnProcessor());
		if(count == 0) 
			getBillUI().showWarningMessage("前一期间的贴息单未统计...");
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
		
		// 暂时有问题，注释之。
//		Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(count(1) , 0) from ehpta_calc_interest where period = '"+interest.getPeriod()+"' and nvl(dr,0)=0", new ColumnProcessor());
//		if(count > 0)
//			throw new Exception("当前期间已统计，请弃审并删除当前期间统计的记录后再做统计操作。");
		
		UFDate period = new UFDate(interest.getPeriod() + "-01");
		String lastDay = CalcFunc.builder(period);
		
		UFDate endPeriod = new UFDate(interest.getPeriod() + "-" + lastDay);
		
		String sqlString = "select * from vw_pta_interest where redate >= '"+period.toString()+"' and redate <= '"+endPeriod.toString()+"' and pk_corp = '"+_getCorp().getPk_corp()+"'";
		List<HashMap> retList = (ArrayList) UAPQueryBS.iUAPQueryBS.executeQuery(sqlString, new MapListProcessor());
		
		if(retList != null && retList.size() > 0) {
			CalcInterestBVO[] interestBs = new CalcInterestBVO[retList.size()];
			int row = 0;
			for(HashMap retMap : retList) {
				
				CalcInterestBVO interestB = new CalcInterestBVO();
				for(String attr : interestB.getAttributeNames()) {
						
					interestB.setAttributeValue(attr, ConvertFunc.change(CalcInterestBVO.class, attr, retMap.get(attr)));
				
					if(((Integer)retMap.get("days")) > 0) { 
						
						interestB.setAttributeValue("interestto", "东方希望集团有限公司");
						interestB.setAttributeValue("def2", "0001B81000000001L83W");
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
			
		} else {
			getBillUI().showWarningMessage("没有可统计的记录...");
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
			
			CalcInterestBVO[] bodyVOs = (CalcInterestBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
			CalcInterestBVO[] selBodyVOs = (CalcInterestBVO[]) getBillCardPanelWrapper().getBillCardPanel().getBillModel().getBodySelectedVOs(CalcInterestBVO.class.getName());
			if(selBodyVOs == null || selBodyVOs.length == 0)
				selBodyVOs = (CalcInterestBVO[]) getBillCardPanelWrapper().getSelectedBodyVOs();
			
			for(CalcInterestBVO selbodyVO : selBodyVOs) {
				for(CalcInterestBVO bodyVO : bodyVOs) {
					if(selbodyVO.getDef4().equals(bodyVO.getDef4())) {
						
						if(markDlg.getTxtValue() instanceof UITextField) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcInterestBVO.class, markDlg.getFieldRef().getRefCode(), ((UITextField)markDlg.getTxtValue()).getText()));
						} else if(markDlg.getTxtValue() instanceof UICheckBox) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcInterestBVO.class, markDlg.getFieldRef().getRefCode(), new UFBoolean(((UICheckBox)markDlg.getTxtValue()).isSelected())));
						} else if(markDlg.getTxtValue() instanceof UIRefPane) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(CalcInterestBVO.class, markDlg.getFieldRef().getRefCode(), ((UIRefPane)markDlg.getTxtValue()).getRefName()));
							bodyVO.setAttributeValue("def2", ConvertFunc.change(CalcInterestBVO.class, markDlg.getFieldRef().getRefCode(), ((UIRefPane)markDlg.getTxtValue()).getRefPK()));
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
	
	@Override
	public void onBoAudit() throws Exception {
		super.onBoAudit();
		
		// 保存时调用
		// modify by river for 2012-09-11
//		afterOnBoAudit();
	}
	
	@Override
	protected void onBoCancelAudit() throws Exception {
		
		CircularlyAccessibleValueObject[] bodyVOs = getBufferData().getCurrentVO().getChildrenVO();
		List<String> flagPks = new ArrayList<String>();
		for(CircularlyAccessibleValueObject bodyVO : bodyVOs) {
			flagPks.add("'" + bodyVO.getAttributeValue("pk_receivable") + "'");
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
		
		// 删除时调用
		// modify by river for 2012-09-11
//		afterOnBoCancelAudit();
		
	}
	
	/**
	 * 功能 ： 保存后续操作
	 * 
	 * Author : river 
	 * 
	 * Create : 2012-08-21
	 * 
	 * @throws Exception
	 */
	protected final void afterOnBoSave() throws Exception {

		CalcInterestBVO[] currBodyVOs = (CalcInterestBVO[]) getBufferData().getCurrentVO().getChildrenVO();
		
		for(CalcInterestBVO bodyVO : currBodyVOs) {
			bodyVO.setCalcflag(new UFBoolean("Y"));
		}
		
		HYPubBO_Client.updateAry(currBodyVOs);
		
		AggregatedValueObject newAggVO = HYPubBO_Client.queryBillVOByPrimaryKey(getUIController().getBillVoName(), getBufferData().getCurrentVO().getParentVO().getPrimaryKey());
		
		getBufferData().setVOAt(getBufferData().getCurrentRow(), newAggVO);
		getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		
		List<HYBillVO> adjustList = new ArrayList<HYBillVO>();
		List<String> flagPks = new ArrayList<String>();
		
		for(CalcInterestBVO bodyVO : currBodyVOs) {
			Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(count(1),0) from arap_djzb where vouchid = '"+bodyVO.getPk_receivable()+"' and nvl(zyx8,'N') = 'Y'", new ColumnProcessor());
			
			if(count == 0) {
				try { UAPQueryBS.iUAPQueryBS.executeQuery("update arap_djzb set zyx8 = 'Y' where vouchid = '"+bodyVO.getPk_receivable()+"' ", null); } catch(Exception e) { }
				adjustList.add(createAdjust(bodyVO));
			} else {
				flagPks.add("'" + bodyVO.getPk_receivable() + "'");
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
		
		if(flagPks.size() > 0) {
			AdjustVO[] adjustArr = (AdjustVO[]) HYPubBO_Client.queryByCondition(AdjustVO.class, " def1 in ("+ConvertFunc.change(flagPks.toArray(new String[0]))+") and nvl(dr,0)=0 ");
			
			try {
				for(AdjustVO adjust : adjustArr) {
					for(CalcInterestBVO bodyVO : currBodyVOs) {
						if(adjust.getDef1().equals(bodyVO.getPk_receivable()))
							adjust.setMny(bodyVO.getActualmny());
							
					}
				}
			
				HYPubBO_Client.updateAry(adjustArr); 
			} catch(Exception e) { 
				Logger.error(e.getMessage()); 
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
		
		//　删除时调用，不再需要判断单据状态
//		if(Integer.valueOf(getBufferData().getCurrentVO().getParentVO().getAttributeValue("vbillstatus").toString()) == IBillStatus.FREE) {
			Object userObj = new ClientUICheckRuleGetter();
			
			List<String> adjustList = new ArrayList<String>();
			
			if(currAggVO == null || currAggVO.getChildrenVO() == null || currAggVO.getChildrenVO().length == 0)
				return ;
			
			for(CalcInterestBVO bodyVO : (CalcInterestBVO[])currAggVO.getChildrenVO()) {
				Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(count(1),0) from arap_djzb where vouchid = '"+bodyVO.getPk_receivable()+"' and nvl(zyx8,'N') = 'Y'", new ColumnProcessor());
				
				if(count > 0) {
					try { UAPQueryBS.iUAPQueryBS.executeQuery("update arap_djzb set zyx8 = 'N' where vouchid = '"+bodyVO.getPk_receivable()+"' ", null); } catch(Exception e) { }
					adjustList.add("'" + bodyVO.getPk_receivable() + "'");
				}	
			}
			
			SuperVO[] superVos = HYPubBO_Client.queryByCondition(AdjustVO.class, " def1 in (" + ConvertFunc.change(adjustList.toArray(new String[0])) + ") and type = "+IAdjustType.Discount+" and nvl(dr,0) = 0 ");
			
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
			
			for(CalcInterestBVO bodyVO : (CalcInterestBVO[])currAggVO.getChildrenVO()) {
				bodyVO.setCalcflag(new UFBoolean("N"));
			}
			
			HYPubBO_Client.updateAry((CalcInterestBVO[])currAggVO.getChildrenVO());
			
//		}
			
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