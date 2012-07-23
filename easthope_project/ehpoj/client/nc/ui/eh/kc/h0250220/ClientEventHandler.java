
package nc.ui.eh.kc.h0250220;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
/**
 * �ֿ����
 * @throws Exception
 * @author ����
 * 2008��5��14��11:39:54
 */
public class ClientEventHandler extends CardEventHandler {
    public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
        super(arg0, arg1);
    }

    @Override
	protected void onBoSave() throws Exception {
            //    	���ù�˾����
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
            for (int i = 0; i < row; i++) {               
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");               
            }
            //�ǿյ���Ч���ж�
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