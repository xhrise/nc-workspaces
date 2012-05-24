
package nc.ui.eh.iso.z0501020;


import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;


/**
 * ˵������Ʒ������׼����������
 * @author ����Դ
 * ʱ�䣺2008-4-11 
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