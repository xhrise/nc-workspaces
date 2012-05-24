package nc.ui.eh.stock.z0150505;

/**
 * �ɹ���ͬ
 * @author ����
 * �������� 2008-4-1 16:09:43
 */


import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.stock.z0150501.ClientBaseBD;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.IBillStatus;


public class ClientUI extends MultiChildBillManageUI {
	

	public ClientUI() {
		super();
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	@Override
	protected ManageEventHandler createEventHandler() {
		return new AbstractSPEventHandler(this, this.getUIControl());
	}
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBaseBD();
	}
	@Override
	protected void initSelfData() {
//		�Ա�ͷ��ʼ��	  (��ͬ״̬)   
		getBillCardWrapper().initHeadComboBox("contstatus", ICombobox.STR_CONTSTYPE,true);
		getBillListWrapper().initHeadComboBox("contstatus", ICombobox.STR_CONTSTYPE,true);
		//������
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);			
	}
	@Override
	public void setDefaultData() throws Exception {	
//		�Ա�ͷ��ʼ��	  (��ͬ״̬)   
		getBillCardWrapper().initHeadComboBox("contstatus", ICombobox.STR_CONTSTYPE,true);
		getBillListWrapper().initHeadComboBox("contstatus", ICombobox.STR_CONTSTYPE,true);
		
	     //������
	     getBillCardPanel().getHeadItem("vbillstatus").setValue(Integer.toString(IBillStatus.FREE));
	       
		//�Ա�ͷ��ʼ��
		getBillCardWrapper().initHeadComboBox("contstatus", ICombobox.STR_custprop,true);
		 String pk_corp = _getCorp().getPrimaryKey();
		 String billNo = BillcodeRuleBO_Client.getBillCode(getUIControl().getBillType(), pk_corp,null, null);
		 getBillCardPanel().setHeadItem("billno", billNo);
		 
		 getBillCardPanel().setHeadItem("writedate", _getDate());
		 getBillCardPanel().setHeadItem("startdate", _getDate());
		 getBillCardPanel().setHeadItem("enddate", _getDate());
//		 getBillCardPanel().setHeadItem("", _getOperator());
//		 ��β�ĳ�ʼ��
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());	
		getBillCardPanel().setTailItem("dmakedate", _getDate());
		getBillCardPanel().setTailItem("coperatorid",_getOperator());
		getBillCardPanel().setHeadItem("address", _getCorp().getCountryarea());
		
	}
//	@Override
//	public Object getUserObject() {
//		return new ClientUICheckRuleGetter();
//	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0, int arg1) throws Exception {
		
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		
	}
	/*
	 * (non-Javadoc) @����˵�����Զ��尴ť
	 */
	@Override
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(
				IEHButton.STOCKCOPE, "��ͬ����", "��ͬ����");
		nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(
				IEHButton.STOCKCHANGE, "��ͬ���", "��ͬ���");
		addPrivateButton(btn);
		addPrivateButton(btn2);
	}

    @Override
	public void afterEdit(BillEditEvent e) {
        String strKey=e.getKey();

         if(e.getPos()==HEAD){
                String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
                getBillCardPanel().execHeadFormulas(formual);
            }else if (e.getPos()==BODY){
                String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
                getBillCardPanel().execBodyFormulas(e.getRow(),formual);
            }else{
                getBillCardPanel().execTailEditFormulas();
            }
        super.afterEdit(e);
    }

   
	
}