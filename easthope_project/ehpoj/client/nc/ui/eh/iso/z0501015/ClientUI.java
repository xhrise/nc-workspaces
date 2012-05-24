
package nc.ui.eh.iso.z0501015;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * ˵������Ʒ������׼�� 
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
	}

	@Override
	public void setDefaultData() throws Exception {
		super.setDefaultData();
		getBillCardPanel().setHeadItem("ver", 1);	
		getBillCardPanel().setHeadItem("def_1","Y");
	}

	/*
	 * ����˵�����Զ��尴ť���汾�����
	 */
	@Override
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(
				IEHButton.EditionChange, "�汾���", "�汾���");
        btn.setOperateStatus(new int[]{IBillOperate.OP_NOADD_NOTEDIT});
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"�ر�","�ر�");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
		addPrivateButton(btn);
        addPrivateButton(btn1);
        super.initPrivateButton();
	}
}