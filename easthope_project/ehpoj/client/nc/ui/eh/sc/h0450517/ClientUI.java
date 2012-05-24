package nc.ui.eh.sc.h0450517;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.trade.pub.IBillStatus;

/**
 * ����˵����BOM����������
 * @author ����
 * 2008��12��30��9:10:07
 */

public class ClientUI extends AbstractClientUI {

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}

	@Override
	public void setDefaultData() throws Exception {
		 //��������
		 getBillCardPanel().setHeadItem("applydate", _getDate()); 
		super.setDefaultData();
	}
    
	@Override
	protected void initSelfData() {
		//������
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
	     super.initSelfData();
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);	
	}
	 @Override
	    protected void initPrivateButton() {
		 	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"�ر�","�ر�");
	        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
	        addPrivateButton(btn1);
	    	super.initPrivateButton();
	    }
}
