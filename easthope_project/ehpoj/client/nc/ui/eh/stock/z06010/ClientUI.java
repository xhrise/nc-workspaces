
package nc.ui.eh.stock.z06010;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
功能：司磅单
作者：newyear
日期：2008-4-11 下午04:36:39
 */

public class ClientUI extends AbstractClientUI {

    
    public ClientUI() {
        super();
        try {
			new AbstractSPEventHandler(this, this.getUIControl()).setBoEnabled();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public ClientUI(Boolean arg0) {
        super(arg0);
    }

    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

    @Override
	protected AbstractManageController createController() {
        return new ClientCtrl();
    }
    
    @Override
    public ManageEventHandler createEventHandler() {
    	// TODO Auto-generated method stub
    	return new AbstractSPEventHandler(this, this.getUIControl());
    }
   
}
