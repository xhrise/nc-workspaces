package nc.ui.ehpta.hq010950;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.bs.logging.Logger;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.gen.GeneraterBillNO;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.linkoperate.*;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.pub.AggregatedValueObject;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class ClientUI extends nc.ui.trade.manage.BillManageUI
		implements ILinkQuery {

	protected final String[] formulas = new String[]{
		"actmny-> premny - adjustmny" , 
	};
	
	protected AbstractManageController createController() {
		return new ClientUICtrl();
	}

	/**
	 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类
	 * 
	 * @return BusinessDelegator 不走平台的业务代理类
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new Delegator();
	}

	/**
	 * 注册自定义按钮
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
		
		addPrivateButton(DefaultBillButton.getMaintainButtonVO());
		addPrivateButton(DefaultBillButton.getStatisticsButtonVO());
		addPrivateButton(DefaultBillButton.getMarkButtonVO());
		addPrivateButton(DefaultBillButton.getSelAllButtonVO());
		addPrivateButton(DefaultBillButton.getSelNoneButtonVO());
		
		addPrivateButton(DefaultBillButton.getSupportButtonVO());
		addPrivateButton(DefaultBillButton.getDataInButtonVO());
		addPrivateButton(DefaultBillButton.getCancleDataInButtonVO());
		
	}

	/**
	 * 注册前台校验类
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
		getBillListPanel().setMultiSelect(true);
		getBillCardPanel().setBodyMultiSelect(true);
		
		getButtonManager().getButton(DefaultBillButton.Support).setEnabled(false);
		getButtonManager().getButton(DefaultBillButton.DataIn).setEnabled(false);
		getButtonManager().getButton(DefaultBillButton.CancleDataIn).setEnabled(false);
		
	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();

		String[] itemkeys = new String[] { fileDef.getField_Corp(),
				fileDef.getField_Operator(), fileDef.getField_Billtype(),
				fileDef.getField_BillStatus() , "dmakedate" };
		Object[] values = new Object[] { pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype, new Integer(IBillStatus.FREE).toString() , _getDate() };

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
	protected boolean isSetRowNormalState() {
		return false;
	}
	
	@Override
	protected String getBillNo() throws Exception {
		return GeneraterBillNO.getInstanse().build(getUIControl().getBillType(), _getCorp().getPk_corp());
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		
		try {
		if("adjustmny".equals(e.getKey()) ) 
			afterSetAdjustmny( e) ;
		
		} catch(Exception ex) {
			Logger.error(ex.getMessage(), ex, this.getClass(), "afterEdit");
			showErrorMessage(ex.getMessage());
		}
	}
	
	protected final void afterSetAdjustmny(BillEditEvent e) throws Exception {
		
		getBillCardPanel().execBodyFormulas(e.getRow(), formulas);
		
	}
	
	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		
		if(vo != null && vo.getParentVO() != null && vo.getChildrenVO() != null && vo.getChildrenVO().length > 0) { 
			if((Integer)vo.getParentVO().getAttributeValue("vbillstatus") == IBillStatus.CHECKPASS) 
				getButtonManager().getButton(DefaultBillButton.Support).setEnabled(true);
			else 
				getButtonManager().getButton(DefaultBillButton.Support).setEnabled(false);
			
			
			if("Y".equals(vo.getParentVO().getAttributeValue("def1"))) {
				getButtonManager().getButton(DefaultBillButton.DataIn).setEnabled(false);
				getButtonManager().getButton(DefaultBillButton.CancleDataIn).setEnabled(true);
			} else {
				getButtonManager().getButton(DefaultBillButton.DataIn).setEnabled(true);
				getButtonManager().getButton(DefaultBillButton.CancleDataIn).setEnabled(false);
			}
		}
		
		updateButtons();
		
		return super.getExtendStatus(vo);
	}
}
