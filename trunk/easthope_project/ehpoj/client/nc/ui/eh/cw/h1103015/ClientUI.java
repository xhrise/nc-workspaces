package nc.ui.eh.cw.h1103015;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.scm.constant.ct.OperationState;

/**
 * 说明：收款单确认 
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
		getBillCardWrapper().initHeadComboBox("sktype", ICombobox.STR_pk_sfkfs,true);
		getBillListWrapper().initHeadComboBox("sktype", ICombobox.STR_pk_sfkfs,true);
	}

	@Override
	public void setDefaultData() throws Exception {
        //初始化确认日期
        getBillCardPanel().setHeadItem("skrq", _getDate());
        
		super.setDefaultData();
	}
     	
    /*
     * 功能说明：自定义按钮（确认）
     */
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.CONFIRMBUG, "确认", "确认");
        
        btn.setOperateStatus(new int[]{OperationState.EDIT});
        addPrivateButton(btn);
    }
}