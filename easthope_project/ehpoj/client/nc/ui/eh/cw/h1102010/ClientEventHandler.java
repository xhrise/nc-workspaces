package nc.ui.eh.cw.h1102010;

import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

/**
 * 说明：查款单(审批) 
 * @author 张起源 
 * 时间：2008-5-28 10:30:13
 */
public class ClientEventHandler extends AbstractSPEventHandler {
    
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}   
    
   
        
    

     @Override
    protected void onBoCard() throws Exception {
        super.onBoCard();
        setBoEnabled();         
    }
    
}