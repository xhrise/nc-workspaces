package nc.ui.ehpta.hq0306;

import nc.ui.ehpta.hq0305.ClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;

public class SaleInvClientUI extends ClientUI {
	
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new Delegator();
	}
	
	@Override
	protected ManageEventHandler createEventHandler() {
		return new EventHandler(this , getUIControl());
	}
	
	@Override
	protected AbstractManageController createController() {
		return new SaleInvClientUICtrl();
	}
}
