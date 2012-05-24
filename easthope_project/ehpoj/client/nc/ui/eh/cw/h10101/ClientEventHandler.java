package nc.ui.eh.cw.h10101;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;

/**
 * 收付款方式
 * @author 王明
 * 2008-05-28 
 */
public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	 
	 @Override
	protected void onBoSave() throws Exception {
         //非空的有效性判断
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
         for (int i = 0; i < row; i++) {
             //设置公司编码
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp"); 
         }
         //保存时不允许为空
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

	protected void onBoLineAdd() throws Exception {
		//功能：行增加时对收付款方式中的维护人初始化。时间：2010-03-01.作者：张志远
		super.onBoLineAdd();
		int row = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), row-1, "coperatorid");
		String[] formual=this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vperson").getEditFormulas();
		this.getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row-1,formual);
	}
     
}
