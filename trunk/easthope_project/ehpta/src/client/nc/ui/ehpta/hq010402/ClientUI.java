package nc.ui.ehpta.hq010402;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.gen.GeneraterBillNO;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.bd.invdoc.InvbasdocVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.IBillStatus;
import com.ufida.iufo.pub.tools.AppDebug;

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
public class ClientUI extends nc.ui.trade.multichild.MultiChildBillManageUI
		implements ILinkQuery {

	private static final long serialVersionUID = 5102983593663822670L;

	private final IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);

	private EventHandler eventHandler = null;
	
	private ClientUICtrl controller = null;
	
	protected ClientUICtrl getController() {
		if(controller == null)
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
	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();

		String[] itemkeys = new String[] { fileDef.getField_Corp(),
				fileDef.getField_Operator(), fileDef.getField_Billtype(),
				fileDef.getField_BillStatus(), "orderdate", "sdate", "edate",
				"dmakedate" , "version" };
		Object[] values = new Object[] { _getCorp().getPk_corp(),
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype, new Integer(IBillStatus.FREE).toString(), _getDate(),
				_getDate(), _getDate(), _getDate() , 1 };
		
		for (int i = 0; i < itemkeys.length; i++) {
			BillItem item = null;
			item = getBillCardPanel().getHeadItem(itemkeys[i]);
			if (item == null)
				item = getBillCardPanel().getTailItem(itemkeys[i]);
			if (item != null)
				item.setValue(values[i]);
		}
		
		getBillCardPanel().addLine(getEventHandler().getTableCodes()[0]);
		getBillCardPanel().addLine(getEventHandler().getTableCodes()[1]);
//		getBillCardPanel().addLine(getEventHandler().getTableCodes()[2]);
		
	}
	
	@Override
	protected String getBillNo() throws Exception {
		return GeneraterBillNO.getInstanse().build(getController().getBillType(), _getCorp().getPk_corp());
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);

		try {

			if ("pk_psndoc".equals(e.getKey()))
				afterSetPk_psndoc(e);

			else if ("purchcode".equals(e.getKey()))
				afterSetPurchcode(e);
			
			else if("defpk_invbasdoc".equals(e.getKey()))
				afterSetDefpk_invbasdoc(e);
			
			else if("totalnum".equals(e.getKey()))
				afterSetTotalnum(e);
			
			else if("defpk_cubasdoc".equals(e.getKey()))
				afterSetDefpk_cubasdoc(e);

		} catch (Exception ex) {
			Logger.error(ex);
			AppDebug.debug(ex);
		}
	}

	private final void afterSetPk_psndoc(BillEditEvent e) throws Exception {

		UIRefPane psnRef = (UIRefPane) getBillCardPanel().getHeadItem("pk_psndoc").getComponent();
		Object pk_deptdoc = iUAPQueryBS.executeQuery("select pk_deptdoc from bd_psndoc where pk_psndoc = '"+ psnRef.getRefPK() + "'", new ColumnProcessor());
		((UIRefPane) getBillCardPanel().getHeadItem("pk_deptdoc").getComponent()).setPK(pk_deptdoc);
		pk_deptdoc = null;

	}

	private final void afterSetPurchcode(BillEditEvent e) throws Exception {

		UIRefPane purchRef = (UIRefPane)getBillCardPanel().getHeadItem("purchcode").getComponent();
		Object custname = iUAPQueryBS.executeQuery("select custname from bd_cubasdoc where pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '"+purchRef.getRefPK()+"')", new ColumnProcessor());
		getBillCardPanel().getHeadItem("purchname").setValue(custname);
		
		Object custcode = iUAPQueryBS.executeQuery("select custcode from bd_cubasdoc where pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '"+purchRef.getRefPK()+"')", new ColumnProcessor());
		getBillCardPanel().getHeadItem("custcode").setValue(custcode);

		getBillCardPanel().setBodyValueAt(purchRef.getRefPK(), 0, "pk_custdoc", getEventHandler().getTableCodes()[1]);
		getBillCardPanel().setBodyValueAt(custcode, 0, "defpk_cubasdoc", getEventHandler().getTableCodes()[1]);
		getBillCardPanel().setBodyValueAt(custcode, 0, "custcode", getEventHandler().getTableCodes()[1]);
		getBillCardPanel().setBodyValueAt(custname, 0, "custname", getEventHandler().getTableCodes()[1]);
		
		custname = null;
		custcode = null;
	}
	
	private final void afterSetDefpk_invbasdoc(BillEditEvent e) throws Exception {
		
		Object obj = getBillCardPanel().getBodyValueAt(e.getRow(), "defpk_invbasdoc");
		InvbasdocVO[] invVO = (InvbasdocVO[])HYPubBO_Client.queryByCondition(InvbasdocVO.class, " invcode = '"+obj+"' and nvl(dr , 0) = 0 ");
		
		if(invVO != null && invVO.length > 0) {
			
			getBillCardPanel().setBodyValueAt(invVO[0].getAttributeValue("pk_invbasdoc"), e.getRow(), "pk_invbasdoc");
			getBillCardPanel().setBodyValueAt(invVO[0].getAttributeValue("invname"), e.getRow(), "invname");
			getBillCardPanel().setBodyValueAt(invVO[0].getAttributeValue("invspec"), e.getRow(), "invspec");
			getBillCardPanel().setBodyValueAt(invVO[0].getAttributeValue("pk_measdoc"), e.getRow(), "pk_measdoc");
			
			if(invVO[0].getAttributeValue("pk_measdoc") != null && !"".equals(invVO[0].getAttributeValue("pk_measdoc"))) {
				
				Object measname = iUAPQueryBS.executeQuery("select measname from bd_measdoc where pk_measdoc = '"+invVO[0].getAttributeValue("pk_measdoc")+"'", new ColumnProcessor());
				getBillCardPanel().setBodyValueAt(measname, e.getRow(), "defpk_measdoc");
				
			}
			
			UFDouble num = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "totalnum");
			Object invspec = getBillCardPanel().getBodyValueAt(e.getRow(), "invspec");
			
			if(invspec != null && !"".equals(invspec) && num != null && num.doubleValue() > 0) {
				
				try {
					UFDouble invspecNum = new UFDouble(invspec.toString());
					if(invspecNum.doubleValue() > 0)
						getBillCardPanel().setBodyValueAt(num.div(invspecNum).intValue() , e.getRow(), "numof");
					else 
						getBillCardPanel().setBodyValueAt(num , e.getRow(), "numof");
				} catch(Exception ex) {
					getBillCardPanel().setBodyValueAt(num, e.getRow(), "numof");
				}
				
			} else  
				getBillCardPanel().setBodyValueAt(num , e.getRow(), "numof");
			
			
			num = null;
			invspec = null;
			
		}
		
		invVO = null;
		
	}
	
	private final void afterSetTotalnum(BillEditEvent e) throws Exception {
		
		UFDouble num = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "totalnum");
		Object invspec = getBillCardPanel().getBodyValueAt(e.getRow(), "invspec");
		
		if(invspec != null && !"".equals(invspec) && num != null && num.doubleValue() > 0) {
			
			try {
				UFDouble invspecNum = new UFDouble(invspec.toString());
				if(invspecNum.doubleValue() > 0)
					getBillCardPanel().setBodyValueAt(num.div(invspecNum).intValue() , e.getRow(), "numof");
				else 
					getBillCardPanel().setBodyValueAt(num , e.getRow(), "numof");
			} catch(Exception ex) {
				getBillCardPanel().setBodyValueAt(num, e.getRow(), "numof");
			}
			
		} else  
			getBillCardPanel().setBodyValueAt(num , e.getRow(), "numof");
		
	}
	
	private final void afterSetDefpk_cubasdoc(BillEditEvent e) throws Exception {
		
		Object custcode = getBillCardPanel().getBodyValueAt(e.getRow(), e.getKey());
		
		Object pk_cubasdoc = iUAPQueryBS.executeQuery("select pk_cubasdoc from bd_cubasdoc where custcode = '"+custcode+"'" , new ColumnProcessor());
		Object custname = iUAPQueryBS.executeQuery("select custname from bd_cubasdoc where custcode = '"+custcode+"'",  new ColumnProcessor());

		getBillCardPanel().setBodyValueAt(pk_cubasdoc, e.getRow(), "pk_custdoc", getEventHandler().getTableCodes()[1]);
		getBillCardPanel().setBodyValueAt(custcode, e.getRow(), "custcode", getEventHandler().getTableCodes()[1]);
		getBillCardPanel().setBodyValueAt(custname, e.getRow(), "custname", getEventHandler().getTableCodes()[1]);
		
	}
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		
		if(getEventHandler().getTableCodes()[1].equals(e.getTableCode())) 
			if(e.getRow() == 0)
				return false;
		
		return super.beforeEdit(e);
	}
}
