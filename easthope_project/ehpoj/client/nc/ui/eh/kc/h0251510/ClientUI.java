
package nc.ui.eh.kc.h0251510;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 说明：生产备料单（审批）
 * @author 张起源
 * 时间：2008年5月19日17:21:32
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
		return new AbstractSPEventHandler(this,this.getUIControl());
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