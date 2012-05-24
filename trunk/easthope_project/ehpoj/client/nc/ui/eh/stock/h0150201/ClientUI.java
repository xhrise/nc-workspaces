/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.stock.h0150201;

import java.util.ArrayList;

import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.stock.h0150201.StockStandardBVO;

/**
 * 
���ܣ�ԭ�ϲɹ���׼
���ߣ�zqy
���ڣ�2008-12-11 ����02:54:46
 */

public class ClientUI extends AbstractClientUI{
	UIRefPane ref = null;
	static String pk_ftstandard_b = null;
	static int flag = 0;
	public ClientUI() {
        super();
        ref=(UIRefPane)getBillCardPanel().getBillModel().getItemByKey("invcode").getComponent();
        ref.setMultiSelectedEnabled(true);
        ref.setTreeGridNodeMultiSelected(true);
    }

    /**
     * @param arg0
     */
    public ClientUI(Boolean arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */
    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.manage.BillManageUI#createController()
     */
    @Override
	protected AbstractManageController createController() {
        // TODO �Զ����ɷ������
        return new ClientCtrl();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.trade.manage.BillManageUI#createEventHandler()
     */
    @Override
	public ManageEventHandler createEventHandler() {
        // TODO Auto-generated method stub
        return new ClientEventHandler(this,this.getUIControl());
    }
    
    @Override
	public void setDefaultData() throws Exception {
        super.setDefaultData();
    }
  
    @Override
	protected void initSelfData() {
    }
   
    @Override
	public void afterEdit(BillEditEvent e) {
        String str=e.getKey();
        if(e.getPos()==HEAD){
           String[] formual=getBillCardPanel().getHeadItem(str).getEditFormulas();//��ȡ�༭��ʽ
           getBillCardPanel().execHeadFormulas(formual);
       }else if (e.getPos()==BODY){
           String[] formual=getBillCardPanel().getBodyItem(str).getEditFormulas();//��ȡ�༭��ʽ
           getBillCardPanel().execBodyFormulas(e.getRow(),formual);
       }else{
           getBillCardPanel().execTailEditFormulas();
       }    
       //��������
        if(str.equals("invcode") && e.getPos()==BODY ){
            String[] refpks=ref.getRefPKs();
            getBodyDataByRef(refpks, "pk_invbasdoc","invcode"); 
        }      
        
       super.afterEdit(e);
   }

    @Override
	public void getBodyDataByRef(String[] refpks, String string, String string2) {
       int selectedRow=getBillCardPanel().getBillTable().getSelectedRow();
       StockStandardBVO selectfbvo = null;
       ArrayList arr=new ArrayList();
       StockStandardBVO[] Fbvo;
       try {
           Fbvo = (StockStandardBVO[]) getVOFromUI().getChildrenVO();
           String[] pk_fbvo = new String[Fbvo.length];
           for(int i=0;i<Fbvo.length;i++){
               if(i!=selectedRow){
            	   StockStandardBVO fbvo = Fbvo[i];
                   String pk_ftstandard_b = fbvo.getPk_standard_b();
                   pk_fbvo[i]=pk_ftstandard_b;
                   arr.add(fbvo);
               }
               if(i==selectedRow){
                   selectfbvo = Fbvo[i];
                   String pk_ftstandard_b = selectfbvo.getPk_standard_b();
                   pk_fbvo[i]=pk_ftstandard_b;
               }
           }
           pk_ftstandard_b = PubTools.combinArrayToString(pk_fbvo);
           for(int i=0;i<refpks.length;i++){
        	   StockStandardBVO Ftbvo = null;
               String pk_invbasdoc = refpks[i];
               if(i==0){
                   Ftbvo = selectfbvo;
               }else{
                   Ftbvo = new StockStandardBVO();
               }
               Ftbvo.setPk_invbasdoc(pk_invbasdoc);
               arr.add(Ftbvo);
           }
           
           int rowcount=getBillCardPanel().getBillTable().getRowCount();
           int[] rowss=new int[rowcount];
           for(int i=rowcount - 1;i>=0;i--){
               rowss[i]=i;
           }           
           getBillCardPanel().getBillModel().delLine(rowss);
           
           flag = 1;// �ڱ���ǰ�Ƚ�ԭ����ɾ��
           for(int i=0;i<arr.size();i++){
        	   StockStandardBVO ftstandbvo = (StockStandardBVO) arr.get(i);
               getBillCardPanel().getBillModel().addLine();
               getBillCardPanel().getBillModel().setBodyRowVO(ftstandbvo, i);
               
               String[] invbasdocformual1 =getBillCardPanel().getBodyItem("invcode").getEditFormulas();//��ȡ��ʾ��ʽ
               getBillCardPanel().execBodyFormulas(i,invbasdocformual1);
           }            
           
       } catch (Exception e) {
           e.printStackTrace();
       }
   }

}

   
    

