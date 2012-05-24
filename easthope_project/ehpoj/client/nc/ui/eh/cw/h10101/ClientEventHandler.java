package nc.ui.eh.cw.h10101;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;

/**
 * �ո��ʽ
 * @author ����
 * 2008-05-28 
 */
public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	 
	 @Override
	protected void onBoSave() throws Exception {
         //�ǿյ���Ч���ж�
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

     @Override
    protected void onBoRefresh() throws Exception {
    	super.onBoRefresh();
    	((ClientUI)getBillUI()).setDefaultData();
    }

	protected void onBoLineAdd() throws Exception {
		//���ܣ�������ʱ���ո��ʽ�е�ά���˳�ʼ����ʱ�䣺2010-03-01.���ߣ���־Զ
		super.onBoLineAdd();
		int row = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), row-1, "coperatorid");
		String[] formual=this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vperson").getEditFormulas();
		this.getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row-1,formual);
	}
     
}
