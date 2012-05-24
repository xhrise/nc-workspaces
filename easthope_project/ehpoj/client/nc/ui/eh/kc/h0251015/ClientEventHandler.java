package nc.ui.eh.kc.h0251015;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;



/** 
 * 说明：材料出库单(审批)
 * @author 张起源 
 * 时间：2008-5-08
 */

public class ClientEventHandler extends AbstractSPEventHandler{
	
	

	
      public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String addCondtion() {
          // TODO Auto-generated method stub
          return " vbilltype = '" + IBillType.eh_h0251005 + "' ";
      }

}
