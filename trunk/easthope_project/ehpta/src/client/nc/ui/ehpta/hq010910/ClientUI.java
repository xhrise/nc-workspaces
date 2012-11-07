package nc.ui.ehpta.hq010910;

import java.awt.Color;
import java.awt.Component;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import nc.bs.logging.Logger;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.IBillStatus;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * �ڴ˴���Ӵ����������Ϣ
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class ClientUI extends nc.ui.trade.manage.BillManageUI
		implements ILinkQuery {

	protected AbstractManageController createController() {
		return new ClientUICtrl();
	}

	/**
	 * ������ݲ���ƽ̨ʱ��UI����Ҫ���ش˷��������ز���ƽ̨��ҵ�������
	 * 
	 * @return BusinessDelegator ����ƽ̨��ҵ�������
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new Delegator();
	}

	/**
	 * ע���Զ��尴ť
	 */
	protected void initPrivateButton() {
		int[] listButns = getUIControl().getListButtonAry();
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
		for (int i = 0; i < listButns.length; i++) {
			if (listButns[i] == nc.ui.trade.button.IBillButton.Commit)
				hasCommit = true;
			if (listButns[i] == nc.ui.trade.button.IBillButton.Audit)
				hasAudit = true;
			if (listButns[i] == nc.ui.trade.button.IBillButton.CancelAudit)
				hasCancelAudit = true;
		}
		int[] cardButns = getUIControl().getCardButtonAry();
		for (int i = 0; i < cardButns.length; i++) {
			if (cardButns[i] == nc.ui.trade.button.IBillButton.Commit)
				hasCommit = true;
			if (cardButns[i] == nc.ui.trade.button.IBillButton.Audit)
				hasAudit = true;
			if (cardButns[i] == nc.ui.trade.button.IBillButton.CancelAudit)
				hasCancelAudit = true;
		}
		if (hasCommit) {
			ButtonVO btnVo = nc.ui.trade.button.ButtonVOFactory.getInstance()
					.build(nc.ui.trade.button.IBillButton.Commit);
			btnVo.setBtnCode(null);
			addPrivateButton(btnVo);
		}

		if (hasAudit) {
			ButtonVO btnVo2 = nc.ui.trade.button.ButtonVOFactory.getInstance()
					.build(nc.ui.trade.button.IBillButton.Audit);
			btnVo2.setBtnCode(null);
			addPrivateButton(btnVo2);
		}

		if (hasCancelAudit) {
			ButtonVO btnVo3 = nc.ui.trade.button.ButtonVOFactory.getInstance()
					.build(nc.ui.trade.button.IBillButton.CancelAudit);
			btnVo3.setBtnCode(null);
			addPrivateButton(btnVo3);
		}
		
		addPrivateButton(DefaultBillButton.getStatisticsButtonVO());
		addPrivateButton(DefaultBillButton.getConfirmButtonVO());
		addPrivateButton(DefaultBillButton.getCancelconfirmButtonVO());
	}

	/**
	 * ע��ǰ̨У����
	 */
	public Object getUserObject() {
		return new ClientUICheckRuleGetter();
	}

	public void doQueryAction(ILinkQueryData querydata) {
		String billId = querydata.getBillID();
		if (billId != null) {
			try {
				setCurrentPanel(BillTemplateWrapper.CARDPANEL);
				AggregatedValueObject vo = loadHeadData(billId);
				getBufferData().addVOToBuffer(vo);
				setListHeadData(new CircularlyAccessibleValueObject[] { vo
						.getParentVO() });
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
				setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	protected ManageEventHandler createEventHandler() {
		return new EventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void initSelfData() {
		getBillListPanel().setParentMultiSelect(true);
	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();

		String[] itemkeys = new String[] { fileDef.getField_Corp(),
				fileDef.getField_Operator(), fileDef.getField_Billtype(),
				fileDef.getField_BillStatus() };
		Object[] values = new Object[] { pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype, new Integer(IBillStatus.FREE).toString() };

		for (int i = 0; i < itemkeys.length; i++) {
			BillItem item = null;
			item = getBillCardPanel().getHeadItem(itemkeys[i]);
			if (item == null)
				item = getBillCardPanel().getTailItem(itemkeys[i]);
			if (item != null)
				item.setValue(values[i]);
		}
	}
	
	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		
		if(vo != null && vo.getParentVO() != null && vo.getParentVO().getAttributeValue("settleflag") != null) {
			
			if(((UFBoolean)vo.getParentVO().getAttributeValue("settleflag")).booleanValue()) {
				getButtonManager().getButton(DefaultBillButton.Confirm).setEnabled(false);
				getButtonManager().getButton(DefaultBillButton.Cancelconfirm).setEnabled(true);
			} else {
				getButtonManager().getButton(DefaultBillButton.Confirm).setEnabled(true);
				getButtonManager().getButton(DefaultBillButton.Cancelconfirm).setEnabled(false);
			}
			
		}
		
		AggregatedValueObject[] billVOs = getBillListPanel().getMultiSelectedVOs(getUIControl().getBillVoName()[0], getUIControl().getBillVoName()[1], getUIControl().getBillVoName()[2]);
		
		if(billVOs != null && billVOs.length > 0) {
			getButtonManager().getButton(IBillButton.Audit).setEnabled(true);
			getButtonManager().getButton(IBillButton.CancelAudit).setEnabled(true);
			getButtonManager().getButton(IBillButton.Commit).setEnabled(true);
		} else {
			
			try {
				
				Integer vbillstatus = 0;
				
				try {
					vbillstatus = (Integer) getBufferData().getCurrentVO().getParentVO().getAttributeValue("vbillstatus");
				} catch(Exception e) { }
				
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
				
			} catch(Exception e) {
				Logger.error(e.getMessage(), e, this.getClass(), "getExtendStatus");
			}
			
		}
		
		updateButtons();
		
		return super.getExtendStatus(vo);
	}
	
}
