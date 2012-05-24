
package nc.ui.eh.stock.h0150205;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 
功能：铁路运费
作者：zqy
日期：2008-12-12 上午09:55:54
 */

public class ClientUI extends AbstractClientUI {

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}

    @Override
    public void setDefaultData() throws Exception {
    	setDefaultData_withNObillno();
    }


}