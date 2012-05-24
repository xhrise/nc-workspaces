
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
 * ˵����ԭ��������׼�� 
 * @author ����Դ
 * ʱ�䣺2008-4-11 
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
		//����Ĵ���ʽ�����˵�
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
	 * ����˵�����Զ��尴ť���汾�����
	 */
	@Override
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(
				IEHButton.EditionChange, "�汾���", "�汾���");
		btn.setOperateStatus(new int[]{IBillStatus.COMMIT,IBillStatus.CHECKGOING,IBillStatus.CHECKPASS,IBillStatus.NOPASS});
        btn.setOperateStatus(new int[]{IBillOperate.OP_NOADD_NOTEDIT}); //��ʼ���汾���Ϊ��ɫ
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"�ر�","�ر�");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn);
		addPrivateButton(btn1);
        super.initPrivateButton();
	}
	
}