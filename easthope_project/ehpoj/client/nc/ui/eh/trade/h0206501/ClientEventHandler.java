package nc.ui.eh.trade.h0206501;

import nc.ui.eh.pub.PubTools;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;

/**
 * ���ܣ�����տ�Ʊ����׼
 * ZB15
 * ����:WB
 * 2008-12-22 13:34:08
 */
public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	 @Override
	protected void onBoSave() throws Exception {
		 //����ʱ������Ϊ��
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
         //Ψһ��У��
         BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
         int res=new PubTools().uniqueCheck(bm, new String[]{"beginamount","endamount"});
         if(res==1){
             getBillUI().showErrorMessage("�����ظ����ã������������");
             return;
         }    
         super.onBoSave();
	 }

     @Override
	protected void onBoBodyQuery() throws Exception {
            StringBuffer sbWhere = new StringBuffer();
            if(askForQueryCondition(sbWhere)==false) 
                return; 
            SuperVO[] queryVos = queryHeadVOs(sbWhere.toString());

            getBufferData().clear();
            addDataToBuffer(queryVos);
            updateBuffer(); 
        }
        
     @Override
    protected void onBoRefresh() throws Exception {
    	super.onBoRefresh();
    	((ClientUI)getBillUI()).setDefaultData();
    }
}
