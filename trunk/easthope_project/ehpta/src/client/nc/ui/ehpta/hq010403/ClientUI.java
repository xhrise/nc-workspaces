package nc.ui.ehpta.hq010403;

import java.util.ArrayList;
import java.util.List;

import nc.bs.logging.Logger;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.gen.GeneraterBillNO;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.so.so002.ExtSaleInvoiceUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.uif.pub.exception.UifException;
import nc.vo.ehpta.hq010403.AdjustVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;

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
public class ClientUI extends nc.ui.trade.manage.BillManageUI implements
		ILinkQuery {

	private static final long serialVersionUID = -8209817630237325419L;

	private EventHandler eventHandler = null;

	private ClientUICtrl controller = null;
	
	protected ExtSaleInvoiceUI invUI = null;

	protected ClientUICtrl getController() {
		if (controller == null)
			createController();

		return controller;
	}

	protected AbstractManageController createController() {
		controller = new ClientUICtrl();
		
		return controller;
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

		addPrivateButton(DefaultBillButton.getDocumentButtonVO());
		addPrivateButton(DefaultBillButton.getConfirmButtonVO());
		addPrivateButton(DefaultBillButton.getSelAllButtonVO());
		addPrivateButton(DefaultBillButton.getSelNoneButtonVO());
		addPrivateButton(DefaultBillButton.getEnabledButtonVO());
		addPrivateButton(DefaultBillButton.getDisabledButtonVO());
		
	}

	/**
	 * 注册前台校验类
	 */
	public Object getUserObject() {
		return new ClientUICheckRuleGetter();
	}

	@Override
	protected String getBillNo() throws Exception {
		return GeneraterBillNO.getInstanse().build(
				getController().getBillType(), _getCorp().getPk_corp());
	}

	public void doQueryAction(ILinkQueryData querydata) {
		String whereSql = ((Object[])querydata.getUserObject())[0].toString();
		invUI = (ExtSaleInvoiceUI) ((Object[])querydata.getUserObject())[1];
		try {
			getBufferData().clear();
			
			SuperVO[] adjustVOs = HYPubBO_Client.queryByCondition(AdjustVO.class, whereSql);
			if(adjustVOs != null && adjustVOs.length > 0) {
				
				invUI.setAdjustVOs((AdjustVO[]) adjustVOs);
				
				List<HYBillVO> billVOs = new ArrayList<HYBillVO>();
				
				for(SuperVO adjust : adjustVOs) {
					
					HYBillVO billVO = new HYBillVO();
					billVO.setParentVO(adjust);
					billVOs.add(billVO);
				}
				
				getBufferData().addVOsToBuffer(billVOs.toArray(new HYBillVO[0]));
				
				if (getBufferData().getVOBufferSize() != 0) {

					setListHeadData( getBufferData().getAllHeadVOsFromBuffer());
					setBillOperate(IBillOperate.OP_NOTEDIT);
					getBufferData().setCurrentRow(0);
				} else {
					setListHeadData(null);
					setBillOperate(IBillOperate.OP_INIT);
					getBufferData().setCurrentRow(-1);
					showHintMessage( nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000066")/* @res "没有查到任何满足条件的数据!" */);
				}
				
			}
			
		} catch (Exception e) {
			Logger.error(e);
		}
		
		getButtonManager().getButton(IBillButton.Query).setVisible(false);
		getButtonManager().getButton(IBillButton.Add).setVisible(false);
		getButtonManager().getButton(IBillButton.Edit).setVisible(false);
		getButtonManager().getButton(IBillButton.Refresh).setVisible(false);
		getButtonManager().getButton(IBillButton.Delete).setVisible(false);
		getButtonManager().getButton(IBillButton.Card).setVisible(false);
		getButtonManager().getButton(DefaultBillButton.DOCUMENT).setVisible(false);
		getButtonManager().getButton(IBillButton.Save).setVisible(false);
		getButtonManager().getButton(IBillButton.Cancel).setVisible(false);
		getButtonManager().getButton(IBillButton.Commit).setVisible(false);
		getButtonManager().getButton(IBillButton.Audit).setVisible(false);
		getButtonManager().getButton(IBillButton.CancelAudit).setVisible(false);
		getButtonManager().getButton(IBillButton.ApproveInfo).setVisible(false);
		getButtonManager().getButton(IBillButton.Brow).setVisible(false);
		getButtonManager().getButton(DefaultBillButton.ENABLED).setVisible(false);
		getButtonManager().getButton(DefaultBillButton.DISABLED).setVisible(false);
		
		getButtonManager().getButton(DefaultBillButton.Confirm).setVisible(true);
		getButtonManager().getButton(DefaultBillButton.SelAll).setVisible(true);
		getButtonManager().getButton(DefaultBillButton.SelNone).setVisible(true);
		
		updateButtons();
		
	}

	protected EventHandler getEventHandler() {
		if(eventHandler == null)
			createEventHandler();
		
		return eventHandler;
		
	}

	protected EventHandler createEventHandler() {
		eventHandler = new EventHandler(this, getUIControl());
		return eventHandler;
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
		getButtonManager().getButton(DefaultBillButton.Confirm).setVisible(false);
		getButtonManager().getButton(DefaultBillButton.SelAll).setVisible(false);
		getButtonManager().getButton(DefaultBillButton.SelNone).setVisible(false);
		
		updateButtons();
	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();

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
	public void updateButtons() {
		
		if(getBillOperate() == IBillOperate.OP_EDIT || 
				getBillOperate() == IBillOperate.OP_ADD ||
				getBillOperate() == IBillOperate.OP_REFADD) {
			if("4".equals(getBillCardPanel().getHeadItem("type").getValueObject())) {
				getBillCardPanel().getHeadItem("def7").setEdit(true);
				getBillCardPanel().getHeadItem("def7").setNull(true);
			} else {
				getBillCardPanel().getHeadItem("def7").setEdit(false);
				getBillCardPanel().getHeadItem("def7").setNull(false);
			}
		}
		
		
		super.updateButtons();
	}
	
	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		
		if(vo != null && vo.getParentVO() != null) {
			if("4".equals(vo.getParentVO().getAttributeValue(AdjustVO.TYPE))) {
				getBillCardPanel().getHeadItem("def7").setNull(true);
			} else {
				getBillCardPanel().getHeadItem("def7").setValue(null);
				getBillCardPanel().getHeadItem("def7").setNull(false);
			}
			
		}
		
		return super.getExtendStatus(vo);
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		
		if("type".equals(e.getKey())) {
			if("4".equals(getBillCardPanel().getHeadItem("type").getValueObject())) {
				getBillCardPanel().getHeadItem("def7").setNull(true);
				getBillCardPanel().getHeadItem("def7").setEdit(true);
			} else {
				getBillCardPanel().getHeadItem("def7").setValue(null);
				getBillCardPanel().getHeadItem("def7").setNull(false);
				getBillCardPanel().getHeadItem("def7").setEdit(false);
			}
				
		}
		
		super.afterEdit(e);
	}
	
}
