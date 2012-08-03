package nc.ui.ehpta.hq010301;

import javax.swing.table.TableColumn;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
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
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.IBillStatus;

import com.ufida.iufo.pub.tools.AppDebug;

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

	protected AggregatedValueObject nowAggVO = null;
	
	protected IUAPQueryBS iUAPQueryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
	
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
	

	private final void afterEdit_type(BillEditEvent e) throws BusinessException{
	
		String pjfenlei = (String) ((UIComboBox)getBillCardPanel().getHeadItem("type").getComponent()).getSelectedItemName();
	
		if(pjfenlei.equals("�����")){
			StringBuilder builder = new StringBuilder();
			builder.append("select count(1) from ehpta_maintain where");
			builder.append(" maindate = '"+getBillCardPanel().getHeadItem("maindate").getValueObject()+"'");
			builder.append(" and type='2' and pk_corp='"+_getCorp().getPk_corp()+"' and nvl(dr,0) = 0");
			
			int i = (Integer) iUAPQueryBS.executeQuery(builder.toString() , new ColumnProcessor());
			System.out.println("���ݿ��ﹲ��"+i+"������");
			if(i>0){
				showErrorMessage("��ǰ�ڼ䣺"+((UIRefPane)getBillCardPanel().getHeadItem("maindate").getComponent()).getRefName()+",�Ѵ��ڽ���ۼ�¼!");
				((UIComboBox)getBillCardPanel().getHeadItem(e.getKey()).getComponent()).setSelectedIndex(-1);
				return;
			}
			
			((UIRefPane)getBillCardPanel().getHeadItem("settlemny").getComponent()).setEnabled(true);
			((UIRefPane)getBillCardPanel().getHeadItem("settlemny").getComponent()).setEditable(true);
			getBillCardPanel().getHeadItem("settlemny").setNull(true);
			getBillCardPanel().getHeadItem("listingmny").getComponent().setEnabled(false);
			getBillCardPanel().getHeadItem("listingmny").setNull(false);
			getBillCardPanel().getHeadItem("listingmny").setValue(null);
			
			}else{
			((UIRefPane)getBillCardPanel().getHeadItem("listingmny").getComponent()).setEnabled(true);
			((UIRefPane)getBillCardPanel().getHeadItem("listingmny").getComponent()).setEditable(true);
			getBillCardPanel().getHeadItem("listingmny").setNull(true);
			getBillCardPanel().getHeadItem("settlemny").getComponent().setEnabled(false);
			getBillCardPanel().getHeadItem("settlemny").setNull(false);
			getBillCardPanel().getHeadItem("settlemny").setValue(null);
		}
	
	}
	
private final void afterEdit_maind(BillEditEvent e) throws BusinessException{ 
	
	String weihu = ((UIRefPane)getBillCardPanel().getHeadItem(e.getKey()).getComponent()).getRefName();
	getBillCardPanel().getHeadItem("maindate").setValue(weihu);
	
	String pjfenlei = (String)((UIComboBox)getBillCardPanel().getHeadItem("type").getComponent()).getSelectedItemName();
	System.out.println(pjfenlei);
	if(pjfenlei.equals("�����")){
		StringBuilder builder = new StringBuilder();
		builder.append("select count(1) from ehpta_maintain where");
		builder.append(" maindate='"+getBillCardPanel().getHeadItem("maindate").getValueObject()+"'");
		builder.append(" and type='2' and pk_corp='"+_getCorp().getPk_corp()+"' and nvl(dr,0)=0");
		
		int i;
			i = (Integer) iUAPQueryBS.executeQuery(builder.toString(), new ColumnProcessor());
			if(i>0){
				showErrorMessage("��ǰ�ڼ䣺"+((UIRefPane)getBillCardPanel().getHeadItem("maindate").getComponent()).getRefName()+",�Ѵ��ڽ���ۼ�¼!");
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
		
			if("���״̬".equals(tableColumn.getHeaderValue())){
				continue;
			}
				
			tableColumn.setCellRenderer(new RowRenderer(getBufferData()));
		}
		
		return super.getExtendStatus(vo);
	}
	
	
}
