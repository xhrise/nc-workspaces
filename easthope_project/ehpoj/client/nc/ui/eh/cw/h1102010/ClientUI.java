package nc.ui.eh.cw.h1102010;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.trade.pub.IBillStatus;

/**
 * ˵������(����) 
 * @author ����Դ
 * ʱ�䣺2008-5-28 10:30:26
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
        //��ʼ��ȷ������
        getBillCardPanel().setHeadItem("qr_rq", _getDate());
        getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
        getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	}

	@Override
	public void setDefaultData() throws Exception {
		
		super.setDefaultData();
	}
	
}