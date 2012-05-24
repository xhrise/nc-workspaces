package nc.ui.eh.cw.h1103010;

import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 说明：收款单(审批) 
 * @author 张起源
 * 时间：2008-5-28 14:36:07
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
        
		//表头的收款类型下拉菜单
		getBillCardWrapper().initHeadComboBox("pk_sfkfs", ICombobox.STR_pk_sfkfs,true);
		getBillListWrapper().initHeadComboBox("pk_sfkfs", ICombobox.STR_pk_sfkfs,true);
	}

	@Override
	public void setDefaultData() throws Exception {
        //初始化确认日期
        getBillCardPanel().setHeadItem("skrq", _getDate());
        getBillCardPanel().setHeadItem("qr_rq", _getDate());
		super.setDefaultData();
	}
     	
}