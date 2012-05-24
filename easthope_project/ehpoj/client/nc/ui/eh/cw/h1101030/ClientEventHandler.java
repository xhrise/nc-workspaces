package nc.ui.eh.cw.h1101030;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

/**
 * 功能说明：运费支付单
 * @author wb
 * 2009-4-15 15:48:58
 */

public class ClientEventHandler extends AbstractSPEventHandler{
	
	

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
	  
    
     @Override
     public String addCondtion() {
     	return  " vbilltype = '" + IBillType.eh_h1101025 + "' ";
     }
         
}
