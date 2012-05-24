
package nc.ui.eh.stock.h0150330;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/** 
 * ˵�������ɹ���⣨������
 * @author ����
 * ʱ�䣺2009-1-8 16:39:08
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