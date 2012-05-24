
package nc.ui.eh.kc.h0251015;


import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.trade.pub.IBillStatus;


/**
 * ˵�������ϳ��ⵥ(����)
 * @author ����Դ
 * ʱ�䣺2008-5-08
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
        return new nc.ui.eh.kc.h0251015.ClientEventHandler(this,this.getUIControl());
    }
	
	@Override
	protected void initSelfData() {
	    //������
         getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
         getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
		super.initSelfData();
	}

	
	@Override
	public void setDefaultData() throws Exception {
		super.setDefaultData();

	}

}