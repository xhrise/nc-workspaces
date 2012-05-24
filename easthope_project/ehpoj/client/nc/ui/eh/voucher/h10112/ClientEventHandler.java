package nc.ui.eh.voucher.h10112;

import nc.ui.eh.pub.PubTools;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能：项目对应
 * 作者：wb
 * 时间：2009-9-27 11:29:22 
 */

public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	 @Override
	protected void onBoSave() throws Exception {
		 //    	设置公司编码
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
         for (int i = 0; i < row; i++) {               
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");               
         }
		 //保存时不允许为空
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();         
//         //唯一性校验
//         BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();       
//         int res=new PubTools().uniqueCheck(bm, new String[]{"pk_ncitem"});
//         if(res==1){
//             getBillUI().showErrorMessage("NC辅助项不允许有重复！");
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
            // 增加数据到Buffer
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
    		getBillUI().showErrorMessage("辅助项目不能超过20个!");
    		super.onBoLineDel();
    		return;
    	}
    	row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
    	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Item"+(row+1), row, "itemcode");
    }
}
