/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.trade.z0207502;


import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * @author wangming
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class ClientUI extends BillManageUI {

    
    public ClientUI() {
        super();
    }

    public ClientUI(Boolean arg0) {
        super(arg0);
    }

    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

    @Override
	protected AbstractManageController createController() {
        return new ClientCtrl();
    }

    @Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
            throws Exception {

    }

    @Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
            int arg1) throws Exception {

    }

    @Override
	protected void setTotalHeadSpecialData(
            CircularlyAccessibleValueObject[] arg0) throws Exception {

    }
    
    @Override
	public void setDefaultData() throws Exception { 
        //��ͷ���õ�
    	String billType=getUIControl().getBillType(); 	
        BillCodeObjValueVO objVO = new BillCodeObjValueVO();
        objVO.setAttributeValue(billType, getUIControl().getBillType());
        String billNo = BillcodeRuleBO_Client.getBillCode(billType, _getCorp().getPrimaryKey(), null,
                                                          objVO);
        getBillCardPanel().setHeadItem("billno",billNo);
        getBillCardPanel().setHeadItem("invoicedate", _getDate());
        //��β���õ�
    	getBillCardPanel().setTailItem("coperatorid", _getOperator());
    	getBillCardPanel().setTailItem("dmakedate", _getDate());
    	
    	 //����״̬
        getBillCardPanel().setHeadItem("vbillstatus",Integer.valueOf(String.valueOf(IBillStatus.FREE)));
    	
    }

    @Override
	protected void initSelfData(){
    	 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark,true);
         getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark,true);
    	
    }
    
    @Override
	protected void initPrivateButton() {
   
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
		//���������ӵĴ���
		if(strKey.equals("amount")||strKey.equals("price")){
            int rows=getBillCardPanel().getBillTable().getRowCount();
            UFDouble ze=new UFDouble(0);
            for(int i=0;i<rows;i++){
                UFDouble jgf=new UFDouble(getBillCardPanel().getBodyValueAt(i,"hsprice")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"hsprice").toString());
                ze=ze.add(jgf);
            }
            getBillCardPanel().setHeadItem("totalprice",ze);
            
        }
		super.afterEdit(e);
	}


    @Override
	public ManageEventHandler createEventHandler() {
    	return new AbstractSPEventHandler(this, this.getUIControl());
    }

}

   
    

