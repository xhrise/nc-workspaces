package nc.ui.eh.cw.h1104010;

import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/** 
 * 说明：费用单审批
 * @author wb
 * 时间：2008-8-20 20:44:15
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
		getBillCardWrapper().initHeadComboBox("direction",ICombobox.STR_DIRECTION, true);
        getBillListWrapper().initHeadComboBox("direction",ICombobox.STR_DIRECTION, true);
	}

	@Override
	public void setDefaultData() throws Exception {
        //初始化确认日期
        getBillCardPanel().setHeadItem("skrq", _getDate());
        getBillCardPanel().setHeadItem("qr_rq", _getDate());
		super.setDefaultData();
	}
     	
}