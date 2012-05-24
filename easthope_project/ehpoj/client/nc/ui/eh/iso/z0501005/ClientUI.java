
package nc.ui.eh.iso.z0501005;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.trade.pub.IBillStatus;

/**
 * 说明：原料质量标准单 
 * @author 张起源
 * 时间：2008-4-11 
 */
public class ClientUI extends AbstractClientUI 
{
   
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
		//表体的处理方式下拉菜单
		getBillCardWrapper().initBodyComboBox("treatype", ICombobox.STR_treatype,true);
		getBillListWrapper().initBodyComboBox("treatype", ICombobox.STR_treatype,true);
//		 getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
		getBillCardPanel().setHeadItem("ver", 1);	
		getBillCardPanel().setHeadItem("def_1","Y");
		super.setDefaultData();
	}
    
	/*
	 * 功能说明：自定义按钮（版本变更）
	 */
	@Override
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(
				IEHButton.EditionChange, "版本变更", "版本变更");
		btn.setOperateStatus(new int[]{IBillStatus.COMMIT,IBillStatus.CHECKGOING,IBillStatus.CHECKPASS,IBillStatus.NOPASS});
        btn.setOperateStatus(new int[]{IBillOperate.OP_NOADD_NOTEDIT}); //初始化版本变更为灰色
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"关闭","关闭");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn);
		addPrivateButton(btn1);
        super.initPrivateButton();
	}
	
}