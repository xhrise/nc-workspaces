package nc.ui.eh.kc.h0251020;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;


/**
 * 说明：其它材料出库单（审批）
 * @author 张起源
 * 时间：2008-5-08
 */


public class ClientEventHandler extends AbstractSPEventHandler{
	
	

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
	   
	@Override
	public String addCondtion() {
        // TODO Auto-generated method stub
        return " vbilltype = '" + IBillType.eh_h0251010 + "' ";
    }

}
