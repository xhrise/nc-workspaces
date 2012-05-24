package nc.ui.eh.cw.h1101015;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.scm.constant.ct.OperationState;
import nc.vo.trade.pub.IBillStatus;



/**
 * ����˵��������ȷ��
 * @author ����
 * 2008-05-28 ����04:03:18
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
//		getBillCardPanel().getHeadItem("vbillstatus").setValue(Integer.toString(IBillStatus.FREE));	

		super.setDefaultData();
	}
	@Override
	protected void initSelfData() {
		//������
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     super.initSelfData();
	}
	/*
	 * (non-Javadoc) @����˵�����Զ��尴ť
	 */
	@Override
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(
				IEHButton.SUREMONEY, "����ȷ��", "����ȷ��");
		btn.setOperateStatus(new int[]{OperationState.EDIT});
		addPrivateButton(btn);
	}
}
