package nc.ui.eh.cw.h10111;

import nc.ui.eh.pub.PubTools;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能：工资基础
 * 作者：zqy
 * 时间：2008-9-10 14:16:51 
 */

public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	 @Override
	protected void onBoSave() throws Exception {
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
         for (int i = 0; i < row; i++) {
             //设置公司编码
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp"); 
             //对折算产量起始值与结束值比较 
             UFDouble startamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "startamount")==null?"0":
                 getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "startamount").toString());
             UFDouble endamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "endamount")==null?"0":
                 getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "endamount").toString());
             if(startamount.compareTo(endamount)==0){
                 getBillUI().showErrorMessage("第("+(i+1)+")行折算产量起始值与结束值相等,不允许操作!");
                 return;
             }             
         }
         //保存时不允许为空
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();         
         //唯一性校验
         BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();       
         int res=new PubTools().uniqueCheck(bm, new String[]{"startamount","endamount"});
         if(res==1){
             getBillUI().showErrorMessage("折算产量起始值与结束值已经存在，不允许操作！");
             return;
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
