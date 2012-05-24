
package nc.ui.eh.stock.h0150340;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 功能:五金采购出库
 * ZB25
 * @author WB
 * 2009-1-9 10:42:36
 *
 */
public class ClientUI extends AbstractClientUI {
	public ClientUI() {
	     super();
	 }
   
	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	
	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,this.getUIControl());
	}

	@Override
	protected void initSelfData() {
		super.initSelfData();
	}

	@Override
	public void setDefaultData() throws Exception {
		super.setDefaultData();
	}
}