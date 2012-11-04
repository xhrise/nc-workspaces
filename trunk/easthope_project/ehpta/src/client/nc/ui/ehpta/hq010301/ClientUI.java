package nc.ui.ehpta.hq010301;

import javax.swing.table.TableColumn;

import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.calc.CalcFunc;
import nc.ui.ehpta.pub.renderer.RowRenderer;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
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
public class ClientUI extends nc.ui.trade.manage.BillManageUI
		implements ILinkQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6620934680358842064L;
	protected AggregatedValueObject nowAggVO = null;
	
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
		
	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();

		String[] itemkeys = new String[] { fileDef.getField_Corp(),
				fileDef.getField_Operator(), fileDef.getField_Billtype(),
				fileDef.getField_BillStatus(),"dmakedate"
				};
		
		Object[] values = new Object[] { pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype, new Integer(IBillStatus.FREE).toString(),_getDate()
				};

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
	public void afterEdit(BillEditEvent e) {

		super.afterEdit(e);
		
		try{
			if("maindate".equals(e.getKey())){
				afterEdit_maind(e);
				
			}else if("type".equals(e.getKey())){
				afterEdit_type(e);
			}
		}catch(Exception e2){
			AppDebug.debug(e2);
		}
	}
	

	private final void afterEdit_type(BillEditEvent e) throws Exception{
	
		String pjfenlei = (String) ((UIComboBox)getBillCardPanel().getHeadItem("type").getComponent()).getSelectedItemName();
	
		if("结算价".equals(pjfenlei) || "现货均价".equals(pjfenlei)) {
			StringBuilder builder = new StringBuilder();
			
			String temp = ((UIRefPane)getBillCardPanel().getHeadItem("maindate").getComponent()).getRefName();
			UFDate maindate = new UFDate(temp);
			UFDate firstDate = new UFDate(maindate.getYear() + "-" + maindate.getMonth() + "-01");
			
			String lastDay = CalcFunc.builder(maindate);
			UFDate lastDate = new UFDate(maindate.getYear() + "-" + maindate.getMonth() + "-" + lastDay);
			
			String type = "";
			if("结算价".equals(pjfenlei))
				type = "2";
			
			else if("现货均价".equals(pjfenlei))
				type = "3";
			
			builder.append("select nvl(count(1),0) from ehpta_maintain where");
			builder.append(" maindate >= '"+firstDate.toString()+"' and maindate <= '"+lastDate.toString()+"'");
			builder.append(" and type='"+type+"' and pk_corp='"+_getCorp().getPk_corp()+"' and nvl(dr,0) = 0");
			
			int i = (Integer) UAPQueryBS.getInstance().executeQuery(builder.toString(), new ColumnProcessor());

			if( i > 0 ){
				
				showErrorMessage("当前期间已存在"+pjfenlei+"记录!");
				((UIComboBox)getBillCardPanel().getHeadItem(e.getKey()).getComponent()).setSelectedIndex(-1);
				return;
				
			}
			
			((UIRefPane)getBillCardPanel().getHeadItem("settlemny").getComponent()).setEnabled(true);
			((UIRefPane)getBillCardPanel().getHeadItem("settlemny").getComponent()).setEditable(true);
			getBillCardPanel().getHeadItem("settlemny").setNull(true);
			getBillCardPanel().getHeadItem("listingmny").getComponent().setEnabled(false);
			getBillCardPanel().getHeadItem("listingmny").setNull(false);
			getBillCardPanel().getHeadItem("listingmny").setValue(null);
			
		} else {
			
			((UIRefPane)getBillCardPanel().getHeadItem("listingmny").getComponent()).setEnabled(true);
			((UIRefPane)getBillCardPanel().getHeadItem("listingmny").getComponent()).setEditable(true);
			getBillCardPanel().getHeadItem("listingmny").setNull(true);
			getBillCardPanel().getHeadItem("settlemny").getComponent().setEnabled(false);
			getBillCardPanel().getHeadItem("settlemny").setNull(false);
			getBillCardPanel().getHeadItem("settlemny").setValue(null);
		}
	
	}
	
	private final void afterEdit_maind(BillEditEvent e) throws Exception{ 
		
		String weihu = ((UIRefPane)getBillCardPanel().getHeadItem(e.getKey()).getComponent()).getRefName();
		getBillCardPanel().getHeadItem("maindate").setValue(weihu);
		
		String pjfenlei = (String)((UIComboBox)getBillCardPanel().getHeadItem("type").getComponent()).getSelectedItemName();
	
		if("结算价".equals(pjfenlei) || "现货均价".equals(pjfenlei)){
			
			StringBuilder builder = new StringBuilder();
			
			String temp = ((UIRefPane)getBillCardPanel().getHeadItem("maindate").getComponent()).getRefName();
			UFDate maindate = new UFDate(temp);
			UFDate firstDate = new UFDate(maindate.getYear() + "-" + maindate.getMonth() + "-01");
			
			String lastDay = CalcFunc.builder(maindate);
			UFDate lastDate = new UFDate(maindate.getYear() + "-" + maindate.getMonth() + "-" + lastDay);
			
			String type = "";
			if("结算价".equals(pjfenlei))
				type = "2";
			
			else if("现货均价".equals(pjfenlei))
				type = "3";
			
			builder.append("select nvl(count(1),0) from ehpta_maintain where");
			builder.append(" maindate >= '"+firstDate.toString()+"' and maindate <= '"+lastDate.toString()+"'");
			builder.append(" and type='"+type+"' and pk_corp='"+_getCorp().getPk_corp()+"' and nvl(dr,0) = 0");
			
			int i = (Integer)UAPQueryBS.getInstance().executeQuery(builder.toString(), new ColumnProcessor());
			if(i>0){
				showErrorMessage("当前期间已存在"+pjfenlei+"记录!");
				getBillCardPanel().getHeadItem("maindate").setValue(null);
				getBillCardPanel().getHeadItem("settlemny").setValue(null);
				return;
			}
		}
	}
	
	
	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		
		nowAggVO = vo;
		TableColumn tableColumn = null;
		for(int i = 0; i<(getBillListPanel().getHeadTable().getColumnCount()); i++){
			tableColumn = getBillListPanel().getHeadTable().getColumn(getBillListPanel().getHeadTable().getColumnName(i));
		
			if("审核状态".equals(tableColumn.getHeaderValue())){
				continue;
			}
				
			tableColumn.setCellRenderer(new RowRenderer(getBufferData()));
		}
		
		return super.getExtendStatus(vo);
	}
	
	
}
