package nc.ui.eh.cw.h1104010;

import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/** 
 * ˵�������õ�����
 * @author wb
 * ʱ�䣺2008-8-20 20:44:15
 */
public class ClientUI extends AbstractClientUI {
   
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
        
		//��ͷ���տ����������˵�
		getBillCardWrapper().initHeadComboBox("direction",ICombobox.STR_DIRECTION, true);
        getBillListWrapper().initHeadComboBox("direction",ICombobox.STR_DIRECTION, true);
	}

	@Override
	public void setDefaultData() throws Exception {
        //��ʼ��ȷ������
        getBillCardPanel().setHeadItem("skrq", _getDate());
        getBillCardPanel().setHeadItem("qr_rq", _getDate());
		super.setDefaultData();
	}
     	
}