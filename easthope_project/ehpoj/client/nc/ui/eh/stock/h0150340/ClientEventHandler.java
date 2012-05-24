package nc.ui.eh.stock.h0150340;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

/**
 * 功能:五金采购出库
 * ZB25
 * @author WB
 * 2009-1-9 10:42:36
 *
 */
public class ClientEventHandler extends AbstractSPEventHandler {
    
		
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

    
    @Override
    public String addCondtion() {
    	// TODO Auto-generated method stub
    	return " vbilltype = '" + IBillType.eh_h0150335 + "' ";
    }
    
   
    
}
