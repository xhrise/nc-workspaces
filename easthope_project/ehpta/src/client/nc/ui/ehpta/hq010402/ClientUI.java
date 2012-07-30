package nc.ui.ehpta.hq010402;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
import nc.vo.ehpta.hq010402.AidcustVO;
import nc.vo.ehpta.hq010402.MultiBillVO;
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
@SuppressWarnings({"rawtypes" , "unchecked"})
public class ClientUI extends nc.ui.trade.multichild.MultiChildBillManageUI
		implements ILinkQuery {

	private static final long serialVersionUID = 5102983593663822670L;

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
		addPrivateButton(DefaultBillButton.getMakeNewContractButtonVO());
		
		addPrivateButton(DefaultBillButton.getLinkButtonVO());
		addPrivateButton(DefaultBillButton.getReceivableButtonVO());
		addPrivateButton(DefaultBillButton.getDeliveryButtonVO());
		addPrivateButton(DefaultBillButton.getInvoiceButtonVO());
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

	protected void initSelfData() { }
	
	
	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		
		return super.getExtendStatus(sortValueObject(vo));
	}
	
	protected final AggregatedValueObject sortValueObject(AggregatedValueObject vo ) {
		if(vo != null ) {
			CircularlyAccessibleValueObject[] cavos = ((MultiBillVO)vo).getTableVO(getEventHandler().getTableCodes()[1]);
			for(int i = 0 , j = cavos == null ? 0 : cavos.length ; i < j ; i ++ ) {
				Object obj = cavos[i].getAttributeValue(AidcustVO.DEF1);
				if(obj != null) {
					if(!((i + 1) + "0").equals(obj)) {
						try { 
							List<CircularlyAccessibleValueObject> bodyVOs = new ArrayList<CircularlyAccessibleValueObject>();
							bodyVOs.addAll(Arrays.asList(cavos));
							Collections.sort(bodyVOs, new Comparator(){
	
								public int compare(Object obj1, Object obj2) {
									try {
										if(Integer.valueOf(((CircularlyAccessibleValueObject)obj1).getAttributeValue(AidcustVO.DEF1).toString()) > Integer.valueOf(((CircularlyAccessibleValueObject)obj2).getAttributeValue(AidcustVO.DEF1).toString())) {
											return 1;
										} else 
											return 0;
											
									} catch(Exception e) {
										Logger.error(e);
										AppDebug.error(e);
									}
									
									return 0;
									
								}
								
							});
							
							((MultiBillVO)vo).setTableVO(getEventHandler().getTableCodes()[1], bodyVOs.toArray(new CircularlyAccessibleValueObject[0]));
							getBufferData().updateView();
						} catch(Exception e) {}
						
						break;
					}
				}
			}
		}
		
		return vo;
	}
	
	@Override
	protected void updateListVo() throws Exception {
		super.updateListVo();
	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();

		String[] itemkeys = new String[] { fileDef.getField_Corp(),
				fileDef.getField_Operator(), fileDef.getField_Billtype(),
				fileDef.getField_BillStatus(), "orderdate", "sdate", "edate",
				"dmakedate" , "version" , fileDef.getField_Busitype() , "pk_psndoc" , "pk_deptdoc" };
		
		Vector retVector = (Vector) UAPQueryBS.iUAPQueryBS.executeQuery("select pk_psndoc , pk_deptdoc from bd_psndoc where pk_psnbasdoc in (select pk_psndoc from sm_userandclerk where userid = '"+ClientEnvironment.getInstance().getUser().getPrimaryKey()+"' and nvl(dr,0)=0) and nvl(dr,0)=0", new VectorProcessor());
		
		Object[] values = new Object[] { _getCorp().getPk_corp(),
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype, new Integer(IBillStatus.FREE).toString(), _getDate(),
				_getDate(), _getDate(), _getDate() , 1 , getBusinessType(), retVector == null || retVector.size() == 0 ? null : ((Vector)retVector.get(0)).get(0) , retVector == null || retVector.size() == 0 ? null : ((Vector)retVector.get(0)).get(1)};
		
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
		
		((UIRefPane)getBillCardPanel().getBodyItem("sdate").getComponent()).setWhereString(" period >= '" + _getDate().toString().substring(0, 7) + "' and period <= '" + _getDate().toString().substring(0, 7) + "'");
		((UIRefPane)getBillCardPanel().getBodyItem("edate").getComponent()).setWhereString(" period >= '" + _getDate().toString().substring(0, 7) + "' and period <= '" + _getDate().toString().substring(0, 7) + "'");
		
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
			
			else if("num".equals(e.getKey()))
				afterSetNum(e);
			
			else if("totalnum".equals(e.getKey()))
				afterSetTotalnum(e);
			
			else if("defpk_cubasdoc".equals(e.getKey()))
				afterSetDefpk_cubasdoc(e);
			
			else if("sdate".equals(e.getKey()) && e.getTableCode() == null) 
				afterSetSdate(e , null);
			
			else if("edate".equals(e.getKey()) && e.getTableCode() == null)
				afterSetEdate(e , null);
			
			else if("sdate".equals(e.getKey()) && e.getTableCode() != null)
				afterSetSdate(e , e.getTableCode());
			
			else if("edate".equals(e.getKey()) && e.getTableCode() != null)
				afterSetEdate(e , e.getTableCode());
			
		} catch (Exception ex) {
			Logger.error(ex);
			AppDebug.debug(ex);
		}
	}

	private final void afterSetPk_psndoc(BillEditEvent e) throws Exception {

		UIRefPane psnRef = (UIRefPane) getBillCardPanel().getHeadItem("pk_psndoc").getComponent();
		Object pk_deptdoc = UAPQueryBS.iUAPQueryBS.executeQuery("select pk_deptdoc from bd_psndoc where pk_psndoc = '"+ psnRef.getRefPK() + "'", new ColumnProcessor());
		((UIRefPane) getBillCardPanel().getHeadItem("pk_deptdoc").getComponent()).setPK(pk_deptdoc);
		pk_deptdoc = null;

	}

	private final void afterSetPurchcode(BillEditEvent e) throws Exception {

		UIRefPane purchRef = (UIRefPane)getBillCardPanel().getHeadItem("purchcode").getComponent();
		Object custname = UAPQueryBS.iUAPQueryBS.executeQuery("select custname from bd_cubasdoc where pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '"+purchRef.getRefPK()+"')", new ColumnProcessor());
		getBillCardPanel().getHeadItem("purchname").setValue(custname);
		
		Object custcode = UAPQueryBS.iUAPQueryBS.executeQuery("select custcode from bd_cubasdoc where pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '"+purchRef.getRefPK()+"')", new ColumnProcessor());
		getBillCardPanel().getHeadItem("custcode").setValue(custcode);

		getBillCardPanel().setBodyValueAt(purchRef.getRefPK(), 0, "pk_custdoc", getEventHandler().getTableCodes()[1]);
		getBillCardPanel().setBodyValueAt(custcode, 0, "defpk_cubasdoc", getEventHandler().getTableCodes()[1]);
		getBillCardPanel().setBodyValueAt(custcode, 0, "custcode", getEventHandler().getTableCodes()[1]);
		getBillCardPanel().setBodyValueAt(custname, 0, "custname", getEventHandler().getTableCodes()[1]);
		getBillCardPanel().setBodyValueAt("10", 0, "def1" , getEventHandler().getTableCodes()[1]);
		
		custname = null;
		custcode = null;
	}
	
	private final void afterSetDefpk_invbasdoc(BillEditEvent e) throws Exception {
		
		Object obj = getBillCardPanel().getBodyValueAt(e.getRow(), "defpk_invbasdoc");
		InvbasdocVO[] invVO = (InvbasdocVO[])HYPubBO_Client.queryByCondition(InvbasdocVO.class, " invcode = '"+obj+"' and nvl(dr , 0) = 0 ");
		
		if(invVO != null && invVO.length > 0) {

			Object pk_invmandoc = UAPQueryBS.iUAPQueryBS.executeQuery("select pk_invmandoc from bd_invmandoc where pk_invbasdoc = '"+invVO[0].getAttributeValue("pk_invbasdoc")+"' and pk_corp = '"+_getCorp().getPk_corp()+"' and nvl(dr , 0 ) = 0 ", new ColumnProcessor());
			getBillCardPanel().setBodyValueAt(pk_invmandoc == null ? invVO[0].getAttributeValue("pk_invbasdoc") : pk_invmandoc , e.getRow(), "pk_invbasdoc");
			getBillCardPanel().setBodyValueAt(invVO[0].getAttributeValue("invname"), e.getRow(), "invname");
			getBillCardPanel().setBodyValueAt(invVO[0].getAttributeValue("invspec"), e.getRow(), "invspec");
			getBillCardPanel().setBodyValueAt(invVO[0].getAttributeValue("pk_measdoc"), e.getRow(), "pk_measdoc");
			
			BigDecimal taxratio = (BigDecimal) UAPQueryBS.iUAPQueryBS.executeQuery("select taxratio from bd_taxitems where pk_taxitems = '"+invVO[0].getAttributeValue("pk_taxitems")+"'", new ColumnProcessor());
			getBillCardPanel().setBodyValueAt(taxratio, e.getRow(), "taxrate");
			
			if(invVO[0].getAttributeValue("pk_measdoc") != null && !"".equals(invVO[0].getAttributeValue("pk_measdoc"))) {
				
				Object measname = UAPQueryBS.iUAPQueryBS.executeQuery("select measname from bd_measdoc where pk_measdoc = '"+invVO[0].getAttributeValue("pk_measdoc")+"'", new ColumnProcessor());
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
			
			// 设置行号
			getBillCardPanel().setBodyValueAt((e.getRow() + 1) + "0", e.getRow(), "def1" , getEventHandler().getTableCodes()[0]);
			
			num = null;
			invspec = null;
			
		}
		
		invVO = null;
		
	}
	
	private final void afterSetNum(BillEditEvent e) throws Exception {
		
		UFDouble num = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "num");
		Integer exec = (Integer) getBillCardPanel().getBodyValueAt(e.getRow(), "execduration");
		
		if(num != null && num.doubleValue() > 0) {
			getBillCardPanel().setBodyValueAt(num.multiply(exec) , e.getRow(), "totalnum", getEventHandler().getTableCodes()[0]);
			afterSetTotalnum(e);
		}
	
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
		
		Object pk_cubasdoc = UAPQueryBS.iUAPQueryBS.executeQuery("select pk_cumandoc from bd_cumandoc where pk_cubasdoc in (select pk_cubasdoc from bd_cubasdoc where custcode = '"+custcode+"')" , new ColumnProcessor());
		Object custname = UAPQueryBS.iUAPQueryBS.executeQuery("select custname from bd_cubasdoc where custcode = '"+custcode+"'",  new ColumnProcessor());

		getBillCardPanel().setBodyValueAt(pk_cubasdoc, e.getRow(), "pk_custdoc", getEventHandler().getTableCodes()[1]);
		getBillCardPanel().setBodyValueAt(custcode, e.getRow(), "custcode", getEventHandler().getTableCodes()[1]);
		getBillCardPanel().setBodyValueAt(custname, e.getRow(), "custname", getEventHandler().getTableCodes()[1]);
		getBillCardPanel().setBodyValueAt((e.getRow() + 1) + "0" , e.getRow(), "def1" , getEventHandler().getTableCodes()[1]);
	
	}
	
	private final void afterSetSdate(BillEditEvent e , String tableCode) throws Exception {
		setExec(e);
	}
	
	
	
	private final void afterSetEdate(BillEditEvent e , String tableCode) throws Exception {
		setExec(e);
	}
	
	private final void setRefPaneWhere() throws Exception {
		
		if(getBillCardPanel().getHeadItem("sdate").getValueObject() != null && getBillCardPanel().getHeadItem("edate").getValueObject() == null) {
			
			((UIRefPane)getBillCardPanel().getBodyItem("sdate").getComponent()).setWhereString(" period >= '" + getBillCardPanel().getHeadItem("sdate").getValueObject().toString().substring(0, 7) + "'");
			((UIRefPane)getBillCardPanel().getBodyItem("edate").getComponent()).setWhereString(" period >= '" + getBillCardPanel().getHeadItem("sdate").getValueObject().toString().substring(0, 7) + "'");
		
		} else if(getBillCardPanel().getHeadItem("sdate").getValueObject() == null && getBillCardPanel().getHeadItem("edate").getValueObject() == null) {
			
			((UIRefPane)getBillCardPanel().getBodyItem("sdate").getComponent()).setWhereString(" period >= '9999-12-31' ");
			((UIRefPane)getBillCardPanel().getBodyItem("edate").getComponent()).setWhereString(" period >= '9999-12-31' ");
		
		} else if(getBillCardPanel().getHeadItem("sdate").getValueObject() == null && getBillCardPanel().getHeadItem("edate").getValueObject() != null) {
			
			((UIRefPane)getBillCardPanel().getBodyItem("sdate").getComponent()).setWhereString(" period <= '" + getBillCardPanel().getHeadItem("edate").getValueObject().toString().substring(0, 7) + "'");
			((UIRefPane)getBillCardPanel().getBodyItem("edate").getComponent()).setWhereString(" period <= '" + getBillCardPanel().getHeadItem("edate").getValueObject().toString().substring(0, 7) + "'");
			
		} else if(getBillCardPanel().getHeadItem("sdate").getValueObject() != null && getBillCardPanel().getHeadItem("edate").getValueObject() != null) {
			
			((UIRefPane)getBillCardPanel().getBodyItem("sdate").getComponent()).setWhereString(" period >= '" + getBillCardPanel().getHeadItem("sdate").getValueObject().toString().substring(0, 7) + "' and period <= '" + getBillCardPanel().getHeadItem("edate").getValueObject().toString().substring(0, 7) + "'");
			((UIRefPane)getBillCardPanel().getBodyItem("edate").getComponent()).setWhereString(" period >= '" + getBillCardPanel().getHeadItem("sdate").getValueObject().toString().substring(0, 7) + "' and period <= '" + getBillCardPanel().getHeadItem("edate").getValueObject().toString().substring(0, 7) + "'");
			
		}
		
	}
	
	private final void setExec(BillEditEvent e) throws Exception {
		Object sdate = getBillCardPanel().getBodyValueAt(e.getRow(), "sdate");
		Object edate = getBillCardPanel().getBodyValueAt(e.getRow(), "edate");
		
		if(sdate != null && edate != null) {
			
			int syear = Integer.valueOf(sdate.toString().substring(0 , 4));
			int eyear = Integer.valueOf(edate.toString().substring(0 , 4));
			
			int smonth = Integer.valueOf(sdate.toString().substring(5 , 7));
			int emonth = Integer.valueOf(edate.toString().substring(5 , 7));
			
			if(eyear < syear || (emonth < smonth && eyear <= syear)) {
				showErrorMessage("开始期间不能晚于结束期间！");
				getBillCardPanel().setBodyValueAt(null , e.getRow(), e.getKey(), getEventHandler().getTableCodes()[0]);
				getBillCardPanel().setBodyValueAt(null , e.getRow(), "execduration", getEventHandler().getTableCodes()[0]);
				getBillCardPanel().setBodyValueAt(null , e.getRow(), "totalnum", getEventHandler().getTableCodes()[0]);
				getBillCardPanel().setBodyValueAt(null , e.getRow(), "numof", getEventHandler().getTableCodes()[0]);
				return ;
			}
			
			UFDouble num = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "num");
			if(num != null)
				getBillCardPanel().setBodyValueAt(num.multiply((eyear - syear) * 12 + (emonth - smonth) + 1) , e.getRow(), "totalnum", getEventHandler().getTableCodes()[0]);
			
			getBillCardPanel().setBodyValueAt((eyear - syear) * 12 + (emonth - smonth) + 1, e.getRow(), "execduration", getEventHandler().getTableCodes()[0]);
			afterSetTotalnum(e);
		}
	}
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		
		if(getEventHandler().getTableCodes()[1].equals(e.getTableCode())) 
			if(e.getRow() == 0)
				return false;
		
		if(getEventHandler().getTableCodes()[0].equals(e.getTableCode())) {
			if("sdate".equals(e.getKey()) || "edate".equals(e.getKey()))
				try { setRefPaneWhere(); } catch(Exception ex) { Logger.debug(ex); }
		}
		
		return super.beforeEdit(e);
	}
}
