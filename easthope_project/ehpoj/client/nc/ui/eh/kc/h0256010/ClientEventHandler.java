package nc.ui.eh.kc.h0256010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

/**
 * ����˵�����˻���ⵥ
 * @author ����Դ
 * ʱ��:2008-5-27 9:56:12
 */

public class ClientEventHandler extends AbstractSPEventHandler {
    
		
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

    
    @Override
    public String addCondtion() {
    	// TODO Auto-generated method stub
    	return " vbilltype = '" + IBillType.eh_h0256005 + "' ";
    }
    
   
    
}
