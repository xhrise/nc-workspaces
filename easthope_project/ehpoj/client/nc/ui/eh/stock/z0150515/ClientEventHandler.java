
package nc.ui.eh.stock.z0150515;


import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;

/**
 * 
功能：采购单价类型
作者：zqy
日期：2008-11-30 下午03:45:21
 */

public class ClientEventHandler extends CardEventHandler {    
    
    public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
        super(arg0, arg1);
    }

    @Override
	protected void onBoSave() throws Exception {
            //设置公司编码
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
            for (int i = 0; i < row; i++) {               
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");               
            }
            //非空的有效性判断
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
        // 增加数据到Buffer
        addDataToBuffer(queryVos);
        updateBuffer(); 
    }
  
    @Override
	protected void onBoRefresh() throws Exception {
        super.onBoRefresh();
        ((ClientUI)getBillUI()).setDefaultData();
    }
    
}
