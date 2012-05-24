package nc.ui.eh.cw.h1101010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

/**
 * 功能说明：付款单审批
 * @author 王明
 * 2008-05-28 下午02:03:18
 */

public class ClientEventHandler extends AbstractSPEventHandler{
	
	

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
    
     public String addCondtion() {
     	return  " vbilltype = '" + IBillType.eh_h1101005 + "' ";
     }
         
}
