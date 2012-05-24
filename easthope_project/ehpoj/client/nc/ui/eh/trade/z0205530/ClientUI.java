
package nc.ui.eh.trade.z0205530;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * ˵�����ۿ۵�����
 * @author ����Դ
 * ʱ�䣺2008-4-12
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
	protected void initSelfData() {
		super.initSelfData();
	}

	
	@Override
	public void setDefaultData() throws Exception {
		super.setDefaultData();

	}
	@Override
	public ManageEventHandler createEventHandler() {
    	return new AbstractSPEventHandler(this, this.getUIControl());
    }


}