
package nc.ui.eh.trade.z0255002;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 说明：出库单 
 * 单据类型：ZA11
 * @author 王兵 
 * 时间：2008-4-8 19:43:27
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