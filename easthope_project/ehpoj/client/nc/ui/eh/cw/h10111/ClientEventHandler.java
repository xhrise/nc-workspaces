package nc.ui.eh.cw.h10111;

import nc.ui.eh.pub.PubTools;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

/**
 * ���ܣ����ʻ���
 * ���ߣ�zqy
 * ʱ�䣺2008-9-10 14:16:51 
 */

public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	 @Override
	protected void onBoSave() throws Exception {
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
         for (int i = 0; i < row; i++) {
             //���ù�˾����
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp"); 
             //�����������ʼֵ�����ֵ�Ƚ� 
             UFDouble startamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "startamount")==null?"0":
                 getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "startamount").toString());
             UFDouble endamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "endamount")==null?"0":
                 getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "endamount").toString());
             if(startamount.compareTo(endamount)==0){
                 getBillUI().showErrorMessage("��("+(i+1)+")�����������ʼֵ�����ֵ���,���������!");
                 return;
             }             
         }
         //����ʱ������Ϊ��
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();         
         //Ψһ��У��
         BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();       
         int res=new PubTools().uniqueCheck(bm, new String[]{"startamount","endamount"});
         if(res==1){
             getBillUI().showErrorMessage("���������ʼֵ�����ֵ�Ѿ����ڣ������������");
             return;
         }        

         super.onBoSave();
         ((ClientUI)getBillUI()).setDefaultData();        
	 }
	
     @Override
	protected void onBoBodyQuery() throws Exception {
            StringBuffer sbWhere = new StringBuffer();
            if(askForQueryCondition(sbWhere)==false) 
                return; 
            String pk_corp = _getCorp().getPrimaryKey();
            SuperVO[] queryVos = queryHeadVOs(sbWhere.toString()+" and (pk_corp = '"+pk_corp+"') ");

            getBufferData().clear();
            // �������ݵ�Buffer
            addDataToBuffer(queryVos);
            updateBuffer(); 
        }
 
     @Override
    protected void onBoRefresh() throws Exception {
    	super.onBoRefresh();
    	 ((ClientUI)getBillUI()).setDefaultData();   
    }
}
