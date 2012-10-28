package nc.ui.ehpta.hq010910;

import java.util.ArrayList;
import java.util.List;

import nc.bs.logging.Logger;
import nc.itf.ia.bill.IBill;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.hq010901.ClientUICheckRuleGetter;
import nc.ui.ehpta.pub.IAdjustType;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.calc.CalcFunc;
import nc.ui.ehpta.pub.convert.ConvertFunc;
import nc.ui.ehpta.pub.dlg.PeriodDlg;
import nc.ui.ehpta.pub.gen.GeneraterBillNO;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.ehpta.hq010403.AdjustVO;
import nc.vo.ehpta.hq010910.CalcSettlementVO;
import nc.vo.ehpta.hq010910.View_CalcSettlementVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.pub.IExAggVO;

/**
 * 
 * ������һ�������࣬��ҪĿ�������ɰ�ť�¼�����Ŀ��
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

		case DefaultBillButton.Confirm:
			onBoConfirm();
			break;
			
		case DefaultBillButton.Cancelconfirm :
			onBoCancelconfirm();
			break;
			
		default:
			break;
		}
	}

	protected final void onBoStatistics() throws Exception {

		PeriodDlg peroidDlg = new PeriodDlg(getBillUI());
		
		if(peroidDlg.showModal() != UIDialog.ID_OK)
			return ;
		
		if(peroidDlg.getFieldRef().getRefName() == null || "".equals(peroidDlg.getFieldRef().getRefName())) {
			
			getBufferData().clear();
			updateBuffer();
			
			throw new Exception("����ڼ䲻��Ϊ��...");
			
		}
		
		String peroid = peroidDlg.getFieldRef().getRefName();
		
		UFDate firstDate = new UFDate(peroid + "-01");
		UFDate lastDate = new UFDate(peroid + "-" + CalcFunc.builder(firstDate));
		
		View_CalcSettlementVO[] vsettleVOs = (View_CalcSettlementVO[]) HYPubBO_Client.queryByCondition(View_CalcSettlementVO.class, " cmakedate >= '"+firstDate.toString()+"' and cmakedate <= '" + lastDate.toString() + "' and nvl(settleflag , 'N') = 'N' ");

		if (vsettleVOs != null && vsettleVOs.length > 0) {
			int row = 0;

			CalcSettlementVO[] settleVOs = new CalcSettlementVO[vsettleVOs.length];

			BillField fileDef = BillField.getInstance();

			for (View_CalcSettlementVO vsettle : vsettleVOs) {

				CalcSettlementVO settle = new CalcSettlementVO();
				String[] Attribute = settle.getAttributeNames();
				for (String attr : Attribute) {
					settle.setAttributeValue(attr, ConvertFunc.change(CalcSettlementVO.class, attr, vsettle.getAttributeValue(attr)));
				}

				settle.setSettleflag(new UFBoolean(false));
				
				settle.setAttributeValue(fileDef.getField_Corp(), _getCorp().getPk_corp());
				settle.setAttributeValue(fileDef.getField_Operator(), _getOperator());
				settle.setAttributeValue("dmakedate", _getDate());
				settle.setAttributeValue(fileDef.getField_Billtype(), getUIController().getBillType());
				settle.setAttributeValue(fileDef.getField_BillStatus(), Integer.valueOf(IBillStatus.FREE));
				settle.setAttributeValue( "vbillno", GeneraterBillNO.getInstanse().build( getUIController().getBillType(), _getCorp().getPk_corp()));

				settleVOs[row] = settle;

				row++;
			}

			getBufferData().clear();

			addDataToBuffer(settleVOs);

			updateBuffer();

		} else {
			
			getBufferData().clear();
			updateBuffer();
			
			getBillUI().showWarningMessage("û�п���ͳ�Ƶļ�¼...");
		}
	}

	protected final void onBoConfirm() throws Exception {

//		HYBillVO[] selAggVOs = (HYBillVO[]) ((BillManageUI) getBillUI()).getBillListPanel().getBillListData().getBillSelectValueVOs(HYBillVO.class.getName(), CalcSettlementVO.class.getName(), CalcSettlementVO.class.getName());
//		selAggVOs = filterData(selAggVOs , Boolean.valueOf(false));
		CircularlyAccessibleValueObject[] selVOs = getBufferData().getAllHeadVOsFromBuffer();
		CalcSettlementVO[] nowSettleVOs = null;
		String[] csaleids = null;
		if (selVOs != null && selVOs.length > 0) {
			nowSettleVOs = new CalcSettlementVO[selVOs.length];
			csaleids = new String[selVOs.length];
			int row = 0;
			for (CircularlyAccessibleValueObject settleVO : selVOs) {
				
				CalcSettlementVO nowSettleVO = new CalcSettlementVO();
				String[] Attribute = nowSettleVO.getAttributeNames();
				
				for(String attr : Attribute) {
					nowSettleVO.setAttributeValue(attr, ConvertFunc.change(CalcSettlementVO.class, attr, settleVO.getAttributeValue(attr)));
				}
				
				nowSettleVO.setAttributeValue("settleflag", new UFBoolean(true));
				nowSettleVO.setAttributeValue("settledate", _getDate());
				
				csaleids[row] = "'" + nowSettleVO.getAttributeValue("csaleid") + "'";
				
				nowSettleVOs[row] = nowSettleVO;
				row ++;
			}

		} else 
			throw new Exception("������ѡ��һ����Ч��¼");

		String[] pk_settlements = HYPubBO_Client.insertAry(nowSettleVOs);
		
		int row = 0;
		for (String pk_settlement : pk_settlements) {
			pk_settlements[row] = "'" + pk_settlement + "'";
			
			row ++;
		}

		CalcSettlementVO[] queryVOs = (CalcSettlementVO[]) queryHeadVOs(" pk_settlement in (" + ConvertFunc.change(pk_settlements) + ") ");
		Object userObj = new ClientUICheckRuleGetter();
		List<CalcSettlementVO> newVO = new ArrayList<CalcSettlementVO>();
		if(queryVOs != null && queryVOs.length > 0) {
			for(CalcSettlementVO queryVO : queryVOs) {
				HYBillVO billVO = new HYBillVO();
				billVO.setParentVO(queryVO);
				
				billVO = (HYBillVO) getBusinessAction().save(billVO, getUIController().getBillType(), _getDate().toString(), userObj , billVO);
				newVO.add((CalcSettlementVO) billVO.getParentVO());
			}
		}
		
		getBufferData().clear();

		addDataToBuffer(newVO.toArray(new CalcSettlementVO[0]));

		updateBuffer();
		
		afterOnBoConfirm(nowSettleVOs);
		
	}
	
	protected final void afterOnBoConfirm(CalcSettlementVO[] nowSettleVOs) throws Exception {
		
		List<HYBillVO> adjustList = new ArrayList<HYBillVO>();
		
		for(CalcSettlementVO headVO : nowSettleVOs) {
			Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from so_sale where csaleid = '"+headVO.getCsaleid()+"' and nvl(settleflag,'N') = 'Y'", new ColumnProcessor());
			
			if(count == 0) {
				try { UAPQueryBS.getInstance().executeQuery("update so_sale set settleflag = 'Y' , settledate = '"+_getDate().toString()+"' where csaleid = '"+headVO.getCsaleid()+"' ", null); } catch(Exception e) { }
				adjustList.add(createAdjust(headVO));
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
	
	protected final void onBoCancelconfirm() throws Exception {
		
		HYBillVO[] selAggVOs = (HYBillVO[]) ((BillManageUI) getBillUI()).getBillListPanel().getBillListData().getBillSelectValueVOs(HYBillVO.class.getName(), CalcSettlementVO.class.getName(), CalcSettlementVO.class.getName());
		selAggVOs = filterData(selAggVOs , Boolean.valueOf(true));
		if(selAggVOs == null || selAggVOs.length == 0)
			throw new Exception("������ѡ��һ����Ч��¼");
		
		Object userObj = new ClientUICheckRuleGetter();
		
		List<String> adjustList = new ArrayList<String>();
		for(HYBillVO billVO : selAggVOs) {
			
			HYPubBO_Client.delete((SuperVO) billVO.getParentVO());
			
			Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from so_sale where csaleid = '"+billVO.getParentVO().getAttributeValue("csaleid")+"' and nvl(settleflag,'N') = 'Y'", new ColumnProcessor());
			
			if(count > 0) {
				try { UAPQueryBS.getInstance().executeQuery("update so_sale set settleflag = 'N' , settledate = null where csaleid = '"+billVO.getParentVO().getAttributeValue("csaleid")+"' ", null); } catch(Exception e) { }
				adjustList.add("'" + billVO.getParentVO().getAttributeValue("csaleid") + "'");
			}	
		}
		
		SuperVO[] superVos = HYPubBO_Client.queryByCondition(AdjustVO.class, " def1 in (" + ConvertFunc.change(adjustList.toArray(new String[0])) + ") and nvl(dr,0) = 0 ");
		
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
		
		CircularlyAccessibleValueObject[] cavos = getBufferData().getAllHeadVOsFromBuffer();
		List<CircularlyAccessibleValueObject> newCavos = new ArrayList<CircularlyAccessibleValueObject>();
		if(cavos != null && cavos.length > 0) {
			for(CircularlyAccessibleValueObject cavo : cavos) {
				boolean check = true;
				for(HYBillVO billVO : selAggVOs) {
					if(cavo.getPrimaryKey().equals(billVO.getParentVO().getPrimaryKey())) {
						check = false;
						break;
					}
				}
				
				if(check)
					newCavos.add(cavo);
			}
		}
		
		getBufferData().clear();
		
		addDataToBuffer(newCavos.toArray(new SuperVO[0]));
		
		updateBuffer();
		
	}
	
	@Override
	protected void onBoCommit() throws Exception {
		// �������
		HYBillVO[] selAggVOs = (HYBillVO[]) ((BillManageUI) getBillUI()).getBillListPanel().getBillListData().getBillSelectValueVOs(HYBillVO.class.getName(), CalcSettlementVO.class.getName(), CalcSettlementVO.class.getName());
		
		if(selAggVOs != null && selAggVOs.length > 0) {
			
			for(HYBillVO modelVo : selAggVOs) {
				
				// ������̬�ĵ��������ύ������
				if(!((Integer) modelVo.getParentVO().getAttributeValue("vbillstatus") == IBillStatus.FREE))
					continue;
				
				// ��ʱ�������Ƶ���Ϊ��ǰ�����ˣ�
				modelVo.getParentVO().setAttributeValue(
						getBillField().getField_Operator(), getBillUI()._getOperator());
				beforeOnBoAction(IBillButton.Commit, modelVo);
				String strTime = ClientEnvironment.getServerTime()
						.toString();
				ArrayList retList = getBusinessAction().commit(modelVo,
						getUIController().getBillType(),
						getBillUI()._getDate().toString() + strTime.substring(10),
						getBillUI().getUserObject());

				if (PfUtilClient.isSuccess()) {
					Object o = retList.get(1);
					if (o instanceof AggregatedValueObject) {
						AggregatedValueObject retVo = (AggregatedValueObject) o;
						afterOnBoAction(IBillButton.Commit, retVo);
						CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
						if (childVos == null)
							modelVo.setParentVO(retVo.getParentVO());
						else
							modelVo = (HYBillVO) retVo;
					}
				}
				
			}
			
			afterUpdateBuffer();
			
			CircularlyAccessibleValueObject vo = null;
			if (getBufferData().getCurrentVO() != null) {
				vo = getBufferData().getCurrentVO().getParentVO();
				((BillManageUI) getBillUI()).getBillListWrapper().updateListVo(vo,getBufferData().getCurrentRow());
	
				//ִ�й�ʽ
			}
		} else 
			super.onBoCommit();
	}
	
	
	
	@Override
	public void onBoAudit() throws Exception {
		
		// �������
		HYBillVO[] selAggVOs = (HYBillVO[]) ((BillManageUI) getBillUI()).getBillListPanel().getBillListData().getBillSelectValueVOs(HYBillVO.class.getName(), CalcSettlementVO.class.getName(), CalcSettlementVO.class.getName());
		
		if(selAggVOs != null && selAggVOs.length > 0) {
			Integer vbillstatus = -1;
			for(HYBillVO modelVo : selAggVOs) {
				
				vbillstatus = (Integer) modelVo.getParentVO().getAttributeValue("vbillstatus");
				
				if(!(vbillstatus == IBillStatus.COMMIT || vbillstatus == IBillStatus.CHECKGOING))
					continue;
				
				setCheckManAndDate(modelVo);
				// ���״̬һ�����˳�
				if (checkVOStatus(modelVo, new int[] { IBillStatus.CHECKPASS })) {
					System.out.println("��Ч����괦�����");
					return;
				}
				beforeOnBoAction(IBillButton.Audit, modelVo);
				// *******************
				AggregatedValueObject retVo = (AggregatedValueObject) getBusinessAction()
						.approve(modelVo, getUIController().getBillType(),
								getBillUI()._getDate().toString(),
								getBillUI().getUserObject());
		
				if (PfUtilClient.isSuccess()) {
		
					afterOnBoAction(IBillButton.Audit, retVo);
					CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
					if (childVos == null)
						modelVo.setParentVO(retVo.getParentVO());
					else
						modelVo = (HYBillVO) retVo;
					
				}
			}
			
			afterUpdateBuffer();
			
			CircularlyAccessibleValueObject vo = null;
			if (getBufferData().getCurrentVO() != null) {
				vo = getBufferData().getCurrentVO().getParentVO();
				((BillManageUI)getBillUI()).getBillListWrapper().updateListVo(vo,
						getBufferData().getCurrentRow());
	
				//ִ�й�ʽ
			}
		
		} else 
			super.onBoAudit();
	}
	
	@Override
	protected void onBoCancelAudit() throws Exception {
		
//		throw new Exception("��˺�����ݲ��ܽ����������");
		
		beforeOnBoCancelAudit();
		
		AggregatedValueObject[] selectBillVO = ((ClientUI)getBillUI()).getBillListPanel().getMultiSelectedVOs(getUIController().getBillVoName()[0], getUIController().getBillVoName()[1], getUIController().getBillVoName()[2]);
		
		Integer vbillstatus = -1;
		
		if(selectBillVO != null && selectBillVO.length > 0) {
			for(AggregatedValueObject modelVo : selectBillVO) {
				
				vbillstatus = (Integer) modelVo.getParentVO().getAttributeValue("vbillstatus");
				
				if(!(vbillstatus == IBillStatus.CHECKPASS))
					continue;
				
				// ���뷴�������ڡ�������
				setCheckManAndDate(modelVo);
				// ���״̬һ�����˳�
				if (checkVOStatus(modelVo, new int[] { IBillStatus.FREE })) {
					System.out.println("��Ч����괦�����");
					return;
				}
				beforeOnBoAction(IBillButton.CancelAudit, modelVo);
				// *******************
				AggregatedValueObject retVo = (AggregatedValueObject) getBusinessAction()
						.unapprove(modelVo, getUIController().getBillType(),
								getBillUI()._getDate().toString(),
								getBillUI().getUserObject());
		
				if (PfUtilClient.isSuccess()) {
					afterOnBoAction(IBillButton.CancelAudit, retVo);
					CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
					if (childVos == null)
						modelVo.setParentVO(retVo.getParentVO());
					else
						modelVo = retVo;
		
					Integer intState = (Integer) modelVo.getParentVO()
							.getAttributeValue(getBillField().getField_BillStatus());
					if (intState.intValue() == IBillStatus.FREE) {
						modelVo.getParentVO().setAttributeValue(
								getBillField().getField_CheckMan(), null);
						modelVo.getParentVO().setAttributeValue(
								getBillField().getField_CheckDate(), null);
					}
					
				}
			
				afterUpdateBuffer();
				
				CircularlyAccessibleValueObject vo = null;
				if (getBufferData().getCurrentVO() != null) {
					vo = getBufferData().getCurrentVO().getParentVO();
					((BillManageUI)getBillUI()).getBillListWrapper().updateListVo(vo,
							getBufferData().getCurrentRow());
		
				}
				
			}
		} else 
			super.onBoCancelAudit();
	}
	
	protected final void beforeOnBoCancelAudit() throws Exception {
		
		AggregatedValueObject[] selectBillVO = ((ClientUI)getBillUI()).getBillListPanel().getMultiSelectedVOs(getUIController().getBillVoName()[0], getUIController().getBillVoName()[1], getUIController().getBillVoName()[2]);
		List<String> pks = new ArrayList<String>();
		Integer vbillstatus = -1;
		for(AggregatedValueObject billVO : selectBillVO) {
			vbillstatus = (Integer) billVO.getParentVO().getAttributeValue("vbillstatus");
			if(vbillstatus == IBillStatus.CHECKPASS)
				pks.add("'" + billVO.getParentVO().getPrimaryKey() + "'");
		}
		
		String pkStr = ConvertFunc.change(pks.toArray(new String[0]));
		
		Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from ehpta_adjust where def1 in ("+pkStr+") and def5 is not null ", new ColumnProcessor());
		
		if(count > 0) 
			throw new Exception("����ʧ��\n��ǰ�б���ѡ�еļ�¼�д����Ѿ���ʹ�õĹҽ�۲�");
		
		
	}
	
	/**
	 * ����ӱ����ݡ� �������ڣ�(2004-3-11 17:44:14)
	 * 
	 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	private CircularlyAccessibleValueObject[] getChildVO(
			AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject[] childVos = null;
		if (retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else
			childVos = retVo.getChildrenVO();
		return childVos;
	}
	
	private void setCheckManAndDate(AggregatedValueObject vo) throws Exception {
		// �����������ڡ�������
		vo.getParentVO().setAttributeValue(getBillField().getField_CheckDate(),
				getBillUI()._getDate());
		vo.getParentVO().setAttributeValue(getBillField().getField_CheckMan(),
				getBillUI()._getOperator());
	}
	
	@Override
	protected void onBoSelAll() throws Exception {
		selectAll((BillManageUI) getBillUI(), true);
	}

	@Override
	protected void onBoSelNone() throws Exception {
		selectAll((BillManageUI) getBillUI(), false);
	}

	public void selectAll(BillManageUI billUI, boolean isNeedSelected) {
		int row = billUI.getBillListPanel().getHeadTable().getRowCount();
		BillModel headModel = billUI.getBillListPanel().getHeadBillModel();
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

		billUI.getBillListPanel().updateUI();
		
		try {
			if(getBufferData().getCurrentVO().getParentVO().getPrimaryKey() == null)
				getButtonManager().getButton(DefaultBillButton.Confirm).setEnabled(true);
			else 
				getButtonManager().getButton(DefaultBillButton.Confirm).setEnabled(false);
			
		} catch(Exception e) {
			Logger.error(e.getMessage() , e);
		}
			
		
		getButtonManager().getButton(DefaultBillButton.Cancelconfirm).setEnabled(true);
		
		AggregatedValueObject[] billVOs = ((ClientUI)getBillUI()).getBillListPanel().getMultiSelectedVOs(getUIController().getBillVoName()[0], getUIController().getBillVoName()[1], getUIController().getBillVoName()[2]);
		
		if(billVOs != null && billVOs.length > 0) {
			getButtonManager().getButton(IBillButton.Audit).setEnabled(true);
			getButtonManager().getButton(IBillButton.CancelAudit).setEnabled(true);
			getButtonManager().getButton(IBillButton.Commit).setEnabled(true);
		} else {
			Integer vbillstatus = (Integer) getBufferData().getCurrentVO().getParentVO().getAttributeValue("vbillstatus");
			
			switch (vbillstatus) {
			
				case IBillStatus.CHECKPASS:
					getButtonManager().getButton(IBillButton.Audit).setEnabled(false);
					getButtonManager().getButton(IBillButton.CancelAudit).setEnabled(true);
					getButtonManager().getButton(IBillButton.Commit).setEnabled(false);
					break;
					
				case IBillStatus.FREE :
					getButtonManager().getButton(IBillButton.Audit).setEnabled(false);
					getButtonManager().getButton(IBillButton.CancelAudit).setEnabled(false);
					getButtonManager().getButton(IBillButton.Commit).setEnabled(true);
					break;
					
				case IBillStatus.COMMIT :
					getButtonManager().getButton(IBillButton.Audit).setEnabled(true);
					getButtonManager().getButton(IBillButton.CancelAudit).setEnabled(false);
					getButtonManager().getButton(IBillButton.Commit).setEnabled(false);
					break;
					
				default:
					break;
			}
		}
		
		getBillUI().updateButtons();
		
	}
	
	protected final HYBillVO[] filterData(HYBillVO[] billVOs , Boolean type) throws Exception {
		StringBuilder builder = new StringBuilder();
		if(billVOs != null && billVOs.length > 0 ) {
			List<HYBillVO> newBillVOs = new ArrayList<HYBillVO>();
			if(type.booleanValue()) {
				
				for(HYBillVO billVO : billVOs) {
					if(((UFBoolean)billVO.getParentVO().getAttributeValue("settleflag")).booleanValue() && Integer.valueOf(billVO.getParentVO().getAttributeValue("vbillstatus").toString()) == IBillStatus.FREE) {
						if(Integer.valueOf(billVO.getParentVO().getAttributeValue("vbillstatus").toString()) != IBillStatus.FREE)
							builder.append("�ᵥ��Ϊ" + billVO.getParentVO().getAttributeValue("vreceiptcode") + "�Ĺҽ�۲��ѽ��㲢���ͨ�����ü�¼�Զ�����");
						newBillVOs.add(billVO);
					}
				}
			} else {
				
				for(HYBillVO billVO : billVOs) {
					if(!((UFBoolean)billVO.getParentVO().getAttributeValue("settleflag")).booleanValue() && Integer.valueOf(billVO.getParentVO().getAttributeValue("vbillstatus").toString()) == IBillStatus.FREE) {
						if(Integer.valueOf(billVO.getParentVO().getAttributeValue("vbillstatus").toString()) != IBillStatus.FREE)
							builder.append("�ᵥ��Ϊ" + billVO.getParentVO().getAttributeValue("vreceiptcode") + "�Ĺҽ�۲��ѽ��㲢���ͨ�����ü�¼�Զ�����");
						
						newBillVOs.add(billVO);
					}
				}
				
			}
			
			if(builder.length() > 0)
				getBillUI().showErrorMessage(builder.toString());
			
			return newBillVOs.toArray(new HYBillVO[0]);
			
		}
		
		return billVOs;
	}
	
	protected final void afterUpdateBuffer() throws Exception {
		
		CircularlyAccessibleValueObject[] allHeadVO = getBufferData().getAllHeadVOsFromBuffer();
		List<String> pk_settlements = new ArrayList<String>();
		if(allHeadVO != null && allHeadVO.length > 0) {
			for(CircularlyAccessibleValueObject headVO : allHeadVO) {
				pk_settlements.add("'" + headVO.getPrimaryKey() + "'");
			}
		}
		
		SuperVO[] queryVOs = queryHeadVOs("pk_settlement in ("+ConvertFunc.change(pk_settlements.toArray(new String[0]))+")");
		
		getBufferData().clear();
		
		addDataToBuffer(queryVOs);
		
		updateBuffer();
		
	}
	
	/**
	 * ���� �� ���������������������
	 * 
	 * 
	 * @param bodyVO
	 * @return
	 * @throws Exception
	 */
	protected final HYBillVO createAdjust(CalcSettlementVO headVO) throws Exception {
		AdjustVO adjust = new AdjustVO();
		adjust.setAttributeValue("type", IAdjustType.LSSubPrice);
		adjust.setAttributeValue("reason", "�ҽ�۲����¼��");
		adjust.setAttributeValue("mny", headVO.getClsmny());
		adjust.setAttributeValue("pk_contract",headVO.getPk_contract());
		adjust.setAttributeValue("pk_cubasdoc", headVO.getCcustomerid());
		adjust.setAttributeValue("memo", "�ҽ�۲������ʽ����");
		adjust.setAttributeValue("vbillno",GeneraterBillNO.getInstanse().build("HQ07", _getCorp().getPk_corp()));
		adjust.setAttributeValue("adjustdate", _getDate());
		adjust.setAttributeValue("managerid", _getOperator());
		adjust.setAttributeValue("vbillstatus", 8);
		adjust.setAttributeValue("pk_corp", _getCorp().getPk_corp());
		adjust.setAttributeValue("pk_billtype", "HQ07");
		adjust.setAttributeValue("voperatorid", _getOperator());
		adjust.setAttributeValue("dmakedate", _getDate());
		adjust.setAttributeValue("def1", headVO.getPk_settlement());
		adjust.setAttributeValue("def2", "Y");
		adjust.setAttributeValue("def3", "N");
		
		HYBillVO billVO = new HYBillVO();
		billVO.setParentVO(adjust);
		
		return billVO;
	}

}