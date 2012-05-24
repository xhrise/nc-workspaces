package nc.ui.eh.cw.h10115;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;

/**
 * ���ܣ�жԭ����
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
         }
         //����ʱ������Ϊ��
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();         
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

}
