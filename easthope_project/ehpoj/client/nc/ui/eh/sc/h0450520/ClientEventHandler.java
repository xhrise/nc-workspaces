
package nc.ui.eh.sc.h0450520;

import nc.ui.eh.pub.PubTools;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;

/** 
 * ˵������Ʒ��ȫ���
 * wb 2008-10-20 16:09:21
 * 2008-10-20 16:08:58
 */
public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	 @Override
	protected void onBoSave() throws Exception {
         //�ǿյ���Ч���ж�
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
         BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();           
         int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
         if(res==1){
             getBillUI().showErrorMessage("�������ظ���");
             return;
         }
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
         for (int i = 0; i < row; i++) {
             //���ù�˾����
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp"); 
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
