
package nc.ui.eh.kc.h0251020;


import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;


/**
 * 说明：其它材料出库单(审批)
 * @author 张起源
 * 时间：2008-5-08
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