package nc.ui.eh.stock.z0502510;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;



/**
 * 功能说明：成品检测申请
 * @author 王明
 * 2008-03-24 下午04:03:18
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
