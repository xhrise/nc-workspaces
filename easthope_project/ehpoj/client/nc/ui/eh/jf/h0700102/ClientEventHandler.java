package nc.ui.eh.jf.h0700102;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;

/**
 * 功能：会员级别
 * ZB45
 * 作者:houcq
 * 2011-10-28 
 */
public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	 @Override
	protected void onBoSave() throws Exception {
		 //保存时不允许为空
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        
         super.onBoSave();
	 }

     @Override
	protected void onBoBodyQuery() throws Exception {
            StringBuffer sbWhere = new StringBuffer();
            if(askForQueryCondition(sbWhere)==false) 
                return; 
            SuperVO[] queryVos = queryHeadVOs(sbWhere.toString());

            getBufferData().clear();
            addDataToBuffer(queryVos);
            updateBuffer(); 
        }
        
     @Override
    protected void onBoRefresh() throws Exception {
    	super.onBoRefresh();
    	((ClientUI)getBillUI()).setDefaultData();
    }
     @Override
     protected void onBoEdit() throws Exception {
     	super.onBoEdit();
     	getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
     } 
}
