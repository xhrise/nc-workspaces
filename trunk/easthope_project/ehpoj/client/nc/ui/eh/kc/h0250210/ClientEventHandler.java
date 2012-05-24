/**
 * 
 */
package nc.ui.eh.kc.h0250210;

import nc.ui.eh.pub.PubTools;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;

/**
 * 说明: 会计期间
 * @author 牛冶
 * 2007-9-20 下午01:05:42
 */
public class ClientEventHandler extends CardEventHandler {


    public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
        super(arg0, arg1);
    }

    @Override
	protected void onBoSave() throws Exception {
            //非空的有效性判断
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
            for (int i = 0; i < row; i++) {
                //设置公司编码
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");
                
            }
            getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
   		 //唯一性校验
          BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();       
          int res=new PubTools().uniqueCheck(bm, new String[]{"nyear","nmonth"});
          if(res==1){
              getBillUI().showErrorMessage("期间已经存在，不允许操作！");
              return;
          }
            super.onBoSave();
            ((ClientUI)getBillUI()).setDefaultData();
    }
    
    @Override
	protected void onBoEdit() throws Exception {
        super.onBoEdit();
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

}
