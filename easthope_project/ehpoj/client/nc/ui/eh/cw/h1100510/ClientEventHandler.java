package nc.ui.eh.cw.h1100510;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 功能说明：付款申请单审批
 * @author 王明
 * 2008-05-28 下午02:03:18
 */

public class ClientEventHandler extends ManageEventHandler{
	
	

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
	 
     public String addCondtion() {
         // TODO Auto-generated method stub
         return " vbilltype = '" + IBillType.eh_h1100505 + "' ";
     }


}
