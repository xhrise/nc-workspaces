package nc.ui.eh.kc.h0256010;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 功能说明：退貨入库单
 * @author 張起源
 * 時間：2008-5-27 10:00:17
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
		 //入库日期
		 getBillCardPanel().setHeadItem("rkdate", _getDate());
         
		super.setDefaultData();
	}
    
	@Override
	protected void initSelfData() {
        
	     super.initSelfData();
	}

}
