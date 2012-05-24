package nc.ui.eh.iso.z0501510;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;


/**
 * 说明：抽样单 （审批）
 * @author 张起源
 * 时间：2008-4-14
 */
public class ClientEventHandler extends ManageEventHandler{
	
	

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
	 
      public String addCondtion() {
          // TODO Auto-generated method stub
          return " vbilltype = '" + IBillType.eh_z0501505 + "' ";
      }

}
