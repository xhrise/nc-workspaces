package nc.ui.eh.trade.h0208001;

import java.util.HashMap;

import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.trade.h0208001.TradeCheckdateVO;
import nc.vo.pub.SuperVO;

/**
 * 功能：盈亏考核计算日期设置
 * ZA97
 * 作者:WB
 * 时间：2008-10-14 13:16:08
 */
public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	 @Override
	protected void onBoSave() throws Exception {
		 //保存时不允许为空
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
         //唯一性校验
         BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();  
         HashMap hmdate = new HashMap();
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
         TradeCheckdateVO[] bvos = (TradeCheckdateVO[])getBillUI().getVOFromUI().getChildrenVO();
         for (int i = 0; i < row; i++) {
        	 int date = bvos[i].getVdate();
        	 if(hmdate.containsKey(date)){
        		 getBillUI().showErrorMessage("有重复计算日,不允许操作!");
        		 return;
        	 }else{
        		 hmdate.put(date,date);
        	 }
        	 if(date>31||date<1){
        		 getBillUI().showErrorMessage("第"+row+"行:"+date+"不符合日期规则,请重新设置!");
        		 return;
        	 }
             //设置公司编码
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp"); 
         }
        
         super.onBoSave();
         onBoRefresh();
	 }

     @Override
	protected void onBoBodyQuery() throws Exception {
            StringBuffer sbWhere = new StringBuffer();
            if(askForQueryCondition(sbWhere)==false) 
                return; 
            String pk_corp = _getCorp().getPrimaryKey();
            SuperVO[] queryVos = queryHeadVOs(sbWhere.toString()+" and (pk_corp = '"+pk_corp+"') and nvl(dr,0)=0 ");

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
