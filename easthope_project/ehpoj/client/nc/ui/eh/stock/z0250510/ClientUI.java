package nc.ui.eh.stock.z0250510;


import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;



/**
 * 功能说明：入库单
 * @author 王明
 * 2008-03-24 下午04:03:18
 */
public class ClientUI extends AbstractClientUI {

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	@Override
	public ManageEventHandler createEventHandler() {
		return new AbstractSPEventHandler(this, this.getUIControl());
	}

	@Override
	public void setDefaultData() throws Exception {		
//		super.setDefaultData();
	}

}
