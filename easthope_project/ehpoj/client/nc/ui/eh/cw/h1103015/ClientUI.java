package nc.ui.eh.cw.h1103015;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.scm.constant.ct.OperationState;

/**
 * ˵�����տȷ�� 
 * @author ����Դ
 * ʱ�䣺2008-5-28 14:36:07
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
        
		//��ͷ���տ����������˵�
		getBillCardWrapper().initHeadComboBox("sktype", ICombobox.STR_pk_sfkfs,true);
		getBillListWrapper().initHeadComboBox("sktype", ICombobox.STR_pk_sfkfs,true);
	}

	@Override
	public void setDefaultData() throws Exception {
        //��ʼ��ȷ������
        getBillCardPanel().setHeadItem("skrq", _getDate());
        
		super.setDefaultData();
	}
     	
    /*
     * ����˵�����Զ��尴ť��ȷ�ϣ�
     */
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.CONFIRMBUG, "ȷ��", "ȷ��");
        
        btn.setOperateStatus(new int[]{OperationState.EDIT});
        addPrivateButton(btn);
    }
}