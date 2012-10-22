package nc.ui.ehpta.hq010401;

import java.math.BigDecimal;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
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
import nc.vo.pub.BusinessException;
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
@SuppressWarnings({"rawtypes"})
public class ClientUI extends nc.ui.trade.manage.BillManageUI
		implements ILinkQuery {

	
	private static final long serialVersionUID = -652591135205093447L;

	protected ClientUICtrl controller = null;
	
	private EventHandler eventHandler = null;
	
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
		
//		addPrivateButton(DefaultBillButton.getLinkButtonVO());
//		addPrivateButton(DefaultBillButton.getReceivableButtonVO());
//		addPrivateButton(DefaultBillButton.getDeliveryButtonVO());
//		addPrivateButton(DefaultBillButton.getInvoiceButtonVO());
		
		addPrivateButton(DefaultBillButton.getOpenOrCloseButtonVO());
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
		getButtonManager().getButton(DefaultBillButton.OpenOrClose).setEnabled(false);
		updateButtons();
	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();

		String[] itemkeys = new String[] { fileDef.getField_Corp(),
				fileDef.getField_Operator(), fileDef.getField_Billtype(),
				fileDef.getField_BillStatus() , "orderdate" , "sdate" , 
				"edate" , "dmakedate" , fileDef.getField_Busitype() , "pk_psndoc" , "pk_deptdoc" };
		
		Vector retVector = (Vector) UAPQueryBS.getInstance().executeQuery("select pk_psndoc , pk_deptdoc from bd_psndoc where pk_psnbasdoc in (select pk_psndoc from sm_userandclerk where userid = '"+ClientEnvironment.getInstance().getUser().getPrimaryKey()+"' and nvl(dr,0)=0) and nvl(dr,0)=0", new VectorProcessor());
		
		Object[] values = new Object[] { pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype, new Integer(IBillStatus.FREE).toString() , 
				_getDate() , _getDate() , _getDate() , _getDate() , getBusinessType() , retVector == null || retVector.size() == 0 ? null : ((Vector)retVector.get(0)).get(0) , retVector == null || retVector.size() == 0 ? null : ((Vector)retVector.get(0)).get(1) };

		for (int i = 0; i < itemkeys.length; i++) {
			BillItem item = null;
			item = getBillCardPanel().getHeadItem(itemkeys[i]);
			if (item == null)
				item = getBillCardPanel().getTailItem(itemkeys[i]);
			if (item != null)
				item.setValue(values[i]);
		}
		
		getBillCardPanel().addLine();
		
	}
	
	@Override
	protected String getBillNo() throws Exception {
		return GeneraterBillNO.getInstanse().build(getController().getBillType(), _getCorp().getPk_corp());
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		
		try {
			
			if("pk_psndoc".equals(e.getKey())) 
				afterSetPk_psndoc(e);
				
			else if("purchcode".equals(e.getKey())) 
				afterSetPurchcode(e);
				
			else if("defpk_invbasdoc".equals(e.getKey()))
				afterSetDefpk_invbasdoc(e);
				
			else if("num".equals(e.getKey()))
				afterSetNum(e);

			else if("taxprice".equals(e.getKey())) 
				afterSetTaxprice(e);
			
		} catch(Exception ex) {
			Logger.error(ex);
			AppDebug.error(ex);
		}
		
	}
	
	private final void afterSetPk_psndoc(BillEditEvent e) throws Exception {
		
		UIRefPane psnRef = (UIRefPane)getBillCardPanel().getHeadItem("pk_psndoc").getComponent();
		Object pk_deptdoc = UAPQueryBS.getInstance().executeQuery("select pk_deptdoc from bd_psndoc where pk_psndoc = '"+psnRef.getRefPK()+"'", new ColumnProcessor());
		((UIRefPane)getBillCardPanel().getHeadItem("pk_deptdoc").getComponent()).setPK(pk_deptdoc);
		pk_deptdoc = null;
		
	}
	
	private final void afterSetPurchcode(BillEditEvent e) throws Exception {
		
		UIRefPane purchRef = (UIRefPane)getBillCardPanel().getHeadItem("purchcode").getComponent();
		Object custname = UAPQueryBS.getInstance().executeQuery("select custname from bd_cubasdoc where pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '"+purchRef.getRefPK()+"')", new ColumnProcessor());
		getBillCardPanel().getHeadItem("purchname").setValue(custname);
		
		Object custcode = UAPQueryBS.getInstance().executeQuery("select custcode from bd_cubasdoc where pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '"+purchRef.getRefPK()+"')", new ColumnProcessor());
		getBillCardPanel().getHeadItem("custcode").setValue(custcode);
		
		custname = null;
		custcode = null;
	}
	
	private final void afterSetDefpk_invbasdoc(BillEditEvent e) throws Exception {
		
		Object obj = getBillCardPanel().getBodyValueAt(e.getRow(), "defpk_invbasdoc");
		InvbasdocVO[] invVO = (InvbasdocVO[])HYPubBO_Client.queryByCondition(InvbasdocVO.class, " invcode = '"+obj+"' and nvl(dr , 0) = 0 ");
		
		if(invVO != null && invVO.length > 0) {
			Object pk_invmandoc = UAPQueryBS.getInstance().executeQuery("select pk_invmandoc from bd_invmandoc where pk_invbasdoc = '"+invVO[0].getAttributeValue("pk_invbasdoc")+"' and pk_corp = '"+_getCorp().getPk_corp()+"' and nvl(dr , 0 ) = 0 ", new ColumnProcessor());
			getBillCardPanel().setBodyValueAt(pk_invmandoc == null ? invVO[0].getAttributeValue("pk_invbasdoc") : pk_invmandoc , e.getRow(), "pk_invbasdoc");
			getBillCardPanel().setBodyValueAt(invVO[0].getAttributeValue("invname"), e.getRow(), "invname");
			getBillCardPanel().setBodyValueAt(invVO[0].getAttributeValue("invspec"), e.getRow(), "invspec");
			getBillCardPanel().setBodyValueAt(invVO[0].getAttributeValue("pk_measdoc"), e.getRow(), "pk_measdoc");
			
			BigDecimal taxratio = (BigDecimal) UAPQueryBS.getInstance().executeQuery("select taxratio from bd_taxitems where pk_taxitems = '"+invVO[0].getAttributeValue("pk_taxitems")+"'", new ColumnProcessor());
			getBillCardPanel().setBodyValueAt(taxratio, e.getRow(), "taxrate");
			
			if(invVO[0].getAttributeValue("pk_measdoc") != null && !"".equals(invVO[0].getAttributeValue("pk_measdoc"))) {
				
				Object measname = UAPQueryBS.getInstance().executeQuery("select measname from bd_measdoc where pk_measdoc = '"+invVO[0].getAttributeValue("pk_measdoc")+"'", new ColumnProcessor());
				getBillCardPanel().setBodyValueAt(measname, e.getRow(), "defmeasname");
				
			}
			
			UFDouble num = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "num");
			Object unitweight = invVO[0].getAttributeValue("unitweight");
			
			if(unitweight != null && !"".equals(unitweight) && num != null && num.doubleValue() > 0) {
				
				try {
					UFDouble invspecNum = new UFDouble(unitweight.toString());
					if(invspecNum.doubleValue() > 0)
						getBillCardPanel().setBodyValueAt(num.div(invspecNum).intValue() , e.getRow(), "numof");
					else 
						getBillCardPanel().setBodyValueAt(num , e.getRow(), "numof");
				} catch(Exception ex) {
					getBillCardPanel().setBodyValueAt(num, e.getRow(), "numof");
				}
				
			} else  
				getBillCardPanel().setBodyValueAt(num , e.getRow(), "numof");
			
			UFDouble taxprice = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "taxprice");
			
			if(taxprice != null && taxprice.doubleValue() > 0 && num != null && num.doubleValue() > 0 ) {
				
				getBillCardPanel().setBodyValueAt(new UFDouble(num.multiply(taxprice).toString() , 2) , e.getRow(), "sumpricetax");
				
				AggregatedValueObject  uiAggVO = getBillCardWrapper().getBillVOFromUI();
				if(uiAggVO != null && uiAggVO.getChildrenVO() != null && uiAggVO.getChildrenVO().length > 0) {
					UFDouble contprice = new UFDouble();
					for(CircularlyAccessibleValueObject cavo : uiAggVO.getChildrenVO()) {
						UFDouble sumpricetax = (UFDouble) (cavo.getAttributeValue("sumpricetax") == null ? new UFDouble() : cavo.getAttributeValue("sumpricetax"));
						contprice = contprice.add(sumpricetax);
					}
					
					getBillCardPanel().setHeadItem("contprice", contprice);
						
				}
				
				if(taxratio != null && taxratio.doubleValue() > 0) {
					getBillCardPanel().setBodyValueAt(new UFDouble(num.multiply(taxprice).sub(num.multiply(taxprice).div(taxratio.doubleValue() / 100 + 1)).toString() , 2), e.getRow(), "tax");
					getBillCardPanel().setBodyValueAt(new UFDouble((num.multiply(taxprice).div(taxratio.doubleValue() / 100 + 1)).toString() , 2), e.getRow(), "notaxloan");
				}
			}
			
			// 设置行号
			getBillCardPanel().setBodyValueAt((e.getRow() + 1) + "0", e.getRow(), "def1");
			
			num = null;
			unitweight = null;
			taxprice = null;
			
		}
		
		invVO = null;
		
	}
	
	private final void afterSetNum(BillEditEvent e) throws Exception {
		
		UFDouble num = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "num");
		
		Object pk_invbasdoc = getBillCardPanel().getBodyValueAt(e.getRow(), "pk_invbasdoc");
		Object unitweight = UAPQueryBS.getInstance().executeQuery("select unitweight from bd_invbasdoc where pk_invbasdoc = (select pk_invbasdoc from bd_invmandoc where pk_invmandoc = '"+pk_invbasdoc+"')", new ColumnProcessor());
		UFDouble taxprice = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "taxprice");
		
		if(unitweight != null && !"".equals(unitweight)) {
			
			try {
				UFDouble invspecNum = new UFDouble(unitweight.toString());
				if(invspecNum.doubleValue() > 0)
					getBillCardPanel().setBodyValueAt(num.div(invspecNum).intValue() , e.getRow(), "numof");
				else 
					getBillCardPanel().setBodyValueAt(num , e.getRow(), "numof");
			} catch(Exception ex) {
				getBillCardPanel().setBodyValueAt(num, e.getRow(), "numof");
			}
			
		} else  
			getBillCardPanel().setBodyValueAt(num , e.getRow(), "numof");
		
		if(taxprice != null && taxprice.doubleValue() != 0 && num != null && num.doubleValue() != 0) {
			getBillCardPanel().setBodyValueAt(new UFDouble(num.multiply(taxprice).toString() , 2) , e.getRow(), "sumpricetax");
			
			AggregatedValueObject  uiAggVO = getBillCardWrapper().getBillVOFromUI();
			if(uiAggVO != null && uiAggVO.getChildrenVO() != null && uiAggVO.getChildrenVO().length > 0) {
				UFDouble contprice = new UFDouble("0");
				for(CircularlyAccessibleValueObject cavo : uiAggVO.getChildrenVO()) {
					UFDouble sumpricetax = (UFDouble) (cavo.getAttributeValue("sumpricetax") == null ? new UFDouble("0") : cavo.getAttributeValue("sumpricetax"));
					contprice = contprice.add(sumpricetax);
				}
				
				getBillCardPanel().setHeadItem("contprice", contprice);
					
			}
			
			UFDouble taxratio = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "taxrate");
			if(taxprice != null && taxprice.doubleValue() > 0 && num != null && num.doubleValue() > 0 && taxratio != null && taxratio.doubleValue() > 0) {
				getBillCardPanel().setBodyValueAt(new UFDouble(num.multiply(taxprice).sub(num.multiply(taxprice).div(taxratio.div(100).add(1))).toString() , 2), e.getRow(), "tax");
				
				getBillCardPanel().setBodyValueAt(new UFDouble(num.multiply(taxprice).div(taxratio.div(100).add(1)).toString() , 2), e.getRow(), "notaxloan");
			}
			
		} else 
			getBillCardPanel().setBodyValueAt(0 , e.getRow(), "sumpricetax");
		
		num = null;
		unitweight = null;
		taxprice = null;
		
	}
	
	private final void afterSetTaxprice(BillEditEvent e) throws Exception {
		
		UFDouble num = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "num");
		UFDouble taxprice = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "taxprice");
		
		if(taxprice != null && taxprice.doubleValue() != 0 && num != null && num.doubleValue() != 0) {
			getBillCardPanel().setBodyValueAt(new UFDouble(num.multiply(taxprice).toString() , 2) , e.getRow(), "sumpricetax");
		
			AggregatedValueObject  uiAggVO = getBillCardWrapper().getBillVOFromUI();
			if(uiAggVO != null && uiAggVO.getChildrenVO() != null && uiAggVO.getChildrenVO().length > 0) {
				UFDouble contprice = new UFDouble();
				for(CircularlyAccessibleValueObject cavo : uiAggVO.getChildrenVO()) {
					UFDouble sumpricetax = (UFDouble) (cavo.getAttributeValue("sumpricetax") == null ? new UFDouble() : cavo.getAttributeValue("sumpricetax"));
					contprice = contprice.add(sumpricetax);
				}
				
				getBillCardPanel().setHeadItem("contprice", contprice);
					
			}
			
			UFDouble taxratio = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "taxrate");
			if(taxprice != null && taxprice.doubleValue() > 0 && num != null && num.doubleValue() > 0 && taxratio != null && taxratio.doubleValue() > 0) {
				getBillCardPanel().setBodyValueAt(new UFDouble(num.multiply(taxprice).sub(num.multiply(taxprice).div(taxratio.div(100).add(1))).toString() , 2), e.getRow(), "tax");
				
				getBillCardPanel().setBodyValueAt(new UFDouble(num.multiply(taxprice).div(taxratio.div(100).add(1)).toString() , 2), e.getRow(), "notaxloan");
			}
			
		} else 
			getBillCardPanel().setBodyValueAt(0 , e.getRow(), "sumpricetax");
			
		num = null;
		taxprice = null;
		
	}
	
	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		
		try {
			if(vo != null && vo.getParentVO() != null && vo.getParentVO().getPrimaryKey() != null) {
				if((Integer)vo.getParentVO().getAttributeValue("vbillstatus") == IBillStatus.CHECKPASS)
					getButtonManager().getButton(DefaultBillButton.OpenOrClose).setEnabled(true);
				else 
					getButtonManager().getButton(DefaultBillButton.OpenOrClose).setEnabled(false);
			} else 
				getButtonManager().getButton(DefaultBillButton.OpenOrClose).setEnabled(false);
			
			
			updateButtons();
		} catch(BusinessException e) {
			Logger.error(e.getMessage(), e, this.getClass(), "getExtendStatus");
		}
		
		return super.getExtendStatus(vo);
	}
	
}
