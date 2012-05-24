
package nc.ui.eh.iso.z0501510;


import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;


/**
 * ˵���������� (����)
 * @author ����Դ
 * ʱ�䣺2008-4-14
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