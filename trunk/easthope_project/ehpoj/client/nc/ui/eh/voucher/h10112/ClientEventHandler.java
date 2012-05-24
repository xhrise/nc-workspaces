package nc.ui.eh.voucher.h10112;

import nc.ui.eh.pub.PubTools;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

/**
 * ���ܣ���Ŀ��Ӧ
 * ���ߣ�wb
 * ʱ�䣺2009-9-27 11:29:22 
 */

public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	 @Override
	protected void onBoSave() throws Exception {
		 //    	���ù�˾����
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
         for (int i = 0; i < row; i++) {               
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");               
         }
		 //����ʱ������Ϊ��
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();         
//         //Ψһ��У��
//         BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();       
//         int res=new PubTools().uniqueCheck(bm, new String[]{"pk_ncitem"});
//         if(res==1){
//             getBillUI().showErrorMessage("NC������������ظ���");
//             return;
//         }        

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
     
    @Override
    protected void onBoLineAdd() throws Exception {
    	super.onBoLineAdd();
    	int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
    	if((row+1)>20){
    		getBillUI().showErrorMessage("������Ŀ���ܳ���20��!");
    		super.onBoLineDel();
    		return;
    	}
    	row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
    	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Item"+(row+1), row, "itemcode");
    }
}
