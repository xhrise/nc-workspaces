package nc.ui.eh.trade.z0600301;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class ClientUI extends BillManageUI {

	public ClientUI() {
		super();
	}

	public ClientUI(Boolean arg0) {
		super(arg0);
	}

	public ClientUI(String arg0, String arg1, String arg2, String arg3,
			String arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	protected AbstractManageController createController() {
		return  new ClientCtrl();
	}

	@Override
	protected ManageEventHandler createEventHandler() {
		return new ClientEH(this,getUIControl());
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
			throws Exception {

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
			int arg1) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(
			CircularlyAccessibleValueObject[] arg0) throws Exception {

	}

    @Override
    protected void initSelfData() {
        
    }
    
	@Override
	public void setDefaultData() throws Exception {
	    //�ڱ�ͷ���ù�˾
		getBillCardPanel().setHeadItem("pk_corp",_getCorp().getPrimaryKey());
        getBillCardPanel().setHeadItem("dr", new Integer(0));
	}

    /*
     * ����˵�����Զ��尴ť
     */
    @Override
	protected void initPrivateButton() {
//        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.FIRSTREADDATE, "��һ�ζ���", "��һ�ζ���");
//        nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.FIRSTPRINT, "��һ�δ�ӡ", "��һ�δ�ӡ");
//        nc.vo.trade.button.ButtonVO btn3 = ButtonFactory.createButtonVO(IEHButton.SECONDREADDATE, "�ڶ��ζ���", "�ڶ��ζ���");
//        nc.vo.trade.button.ButtonVO btn4 = ButtonFactory.createButtonVO(IEHButton.SECONDPRINT, "�ڶ��δ�ӡ", "�ڶ��δ�ӡ");
//        
//        addPrivateButton(btn1);
//        addPrivateButton(btn2);
//        addPrivateButton(btn3);
//        addPrivateButton(btn4);
    }

}
