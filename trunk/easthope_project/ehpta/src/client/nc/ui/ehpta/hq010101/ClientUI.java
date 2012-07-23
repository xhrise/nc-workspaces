package nc.ui.ehpta.hq010101;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.gen.GeneraterBillNO;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.BillField;
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

	private static final long serialVersionUID = -2862050814411394246L;

	private EventHandler eventHandler = null;

	private ClientUICtrl controller = null;

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

	protected EventHandler getEventHandler() {
		if (eventHandler == null)
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
	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();

		String[] itemkeys = new String[] { fileDef.getField_Corp(),
				fileDef.getField_Operator(), fileDef.getField_Billtype(),
				fileDef.getField_BillStatus(), "dmakedate", "transdate",
				"sdate", "edate" };
		Object[] values = new Object[] { pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype, new Integer(IBillStatus.FREE).toString(), _getDate(),
				_getDate(), _getDate(), _getDate() };

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
	protected String getBillNo() throws Exception {
		return GeneraterBillNO.getInstanse().build(getController().getBillType(), _getCorp().getPk_corp());
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		
		try {
			if(e.getSource() instanceof BillCellEditor) {
				if("defpk_sstordoc".equals(e.getKey())) 
					afterSetDefpk_sstordoc(e);
				
				if("defpk_estordoc".equals(e.getKey()))
					afterSetDefpk_estordoc(e);
				
				if("defpk_sendtype".equals(e.getKey()))
					afterSetDefpk_sendtype(e);
			}
		} catch(Exception ex) {
			showErrorMessage(ex.getMessage());
			Logger.error(ex);
			return ;
		}
		
		super.afterEdit(e);
	}
	
	private final void afterSetDefpk_sstordoc(BillEditEvent e) throws Exception {
		
		if(e.getValue() != null ) {
			
			getBillCardPanel().setBodyValueAt(((DefaultConstEnum)e.getValue()).getValue(), e.getRow(), "pk_sstordoc");
			
			getBillCardPanel().setBodyValueAt(((DefaultConstEnum)e.getValue()).getName(), e.getRow(), "defsstorname");
			
			Object storaddr = UAPQueryBS.iUAPQueryBS.executeQuery("select storaddr from bd_stordoc where pk_stordoc = '"+((DefaultConstEnum)e.getValue()).getValue()+"'", new ColumnProcessor());
		
			getBillCardPanel().setBodyValueAt(storaddr, e.getRow(), "sstoraddr");
		
		}
	}
	
	private final void afterSetDefpk_estordoc(BillEditEvent e) throws Exception {
		
		if(e.getValue() != null) {
			
			getBillCardPanel().setBodyValueAt(((DefaultConstEnum)e.getValue()).getValue(), e.getRow(), "pk_estordoc");
			
			getBillCardPanel().setBodyValueAt(((DefaultConstEnum)e.getValue()).getName(), e.getRow(), "defestorname");
			
			Object etoraddr = UAPQueryBS.iUAPQueryBS.executeQuery("select storaddr from bd_stordoc where pk_stordoc = '"+((DefaultConstEnum)e.getValue()).getValue()+"'", new ColumnProcessor());
		
			getBillCardPanel().setBodyValueAt(etoraddr, e.getRow(), "estoraddr");
		
		}
	}
	
	private final void afterSetDefpk_sendtype(BillEditEvent e) throws Exception {
		
		if(e.getValue() != null) 
			getBillCardPanel().setBodyValueAt(((DefaultConstEnum)e.getValue()).getValue(), e.getRow(), "pk_sendtype");
	
	}
	
}
