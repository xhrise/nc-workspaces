package nc.ui.eh.stock.h0150330;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

/** 
 * ˵�������ɹ���⣨������
 * @author ����
 * ʱ�䣺2009-1-8 16:39:08
 */

public class ClientEventHandler extends AbstractSPEventHandler {
    
		
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

    
    @Override
    public String addCondtion() {
    	// TODO Auto-generated method stub
    	return " vbilltype = '" + IBillType.eh_h0150325 + "' ";
    }
    
   
    
}
