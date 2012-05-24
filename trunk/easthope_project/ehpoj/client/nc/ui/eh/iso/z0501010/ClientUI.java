
package nc.ui.eh.iso.z0501010;

import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * ˵����ԭ��������׼����������
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
		//����Ĵ���ʽ�����˵�
		getBillCardWrapper().initBodyComboBox("treatype", ICombobox.STR_treatype,true);
		getBillListWrapper().initBodyComboBox("treatype", ICombobox.STR_treatype,true);
	}

	@Override
	public void setDefaultData() throws Exception {
		super.setDefaultData();
        getBillCardPanel().setHeadItem("outdate", _getDate());
	}
}