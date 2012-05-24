/**
 * ����:Ʒ��ά��
 * @author ����
 * 2008-03-24 ����04:03:18
 */
package nc.ui.eh.trade.z00125;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;

public class ClientEventHandler extends CardEventHandler {
	
    
    
    public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
        super(arg0, arg1);
    }

	
    @Override
	protected void onBoSave() throws Exception {
            //�ǿյ���Ч���ж�
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
            for (int i = 0; i < row; i++) {
                //���ù�˾����
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");               
            }
            getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
            super.onBoSave();
            ((ClientUI)getBillUI()).setDefaultData();
    }
    
    @Override
	protected void onBoEdit() throws Exception {
        // TODO Auto-generated method stub
        super.onBoEdit();
        
    }
    
    @Override
	protected void onBoBodyQuery() throws Exception {
        StringBuffer sbWhere = new StringBuffer();
        if(askForQueryCondition(sbWhere)==false) 
            return; 
        String pk_corp = _getCorp().getPrimaryKey();
        SuperVO[] queryVos = queryHeadVOs(sbWhere.toString()+" and pk_corp = '"+pk_corp+"' ");

        getBufferData().clear();
        // �������ݵ�Buffer
        addDataToBuffer(queryVos);
        updateBuffer(); 
    }
    
    @Override
	protected void onBoLineDel() throws Exception {     
        super.onBoLineDel();
    }
}
