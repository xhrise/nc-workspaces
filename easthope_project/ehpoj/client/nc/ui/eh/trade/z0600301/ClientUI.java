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
	    //在表头设置公司
		getBillCardPanel().setHeadItem("pk_corp",_getCorp().getPrimaryKey());
        getBillCardPanel().setHeadItem("dr", new Integer(0));
	}

    /*
     * 功能说明：自定义按钮
     */
    @Override
	protected void initPrivateButton() {
//        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.FIRSTREADDATE, "第一次读数", "第一次读数");
//        nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.FIRSTPRINT, "第一次打印", "第一次打印");
//        nc.vo.trade.button.ButtonVO btn3 = ButtonFactory.createButtonVO(IEHButton.SECONDREADDATE, "第二次读数", "第二次读数");
//        nc.vo.trade.button.ButtonVO btn4 = ButtonFactory.createButtonVO(IEHButton.SECONDPRINT, "第二次打印", "第二次打印");
//        
//        addPrivateButton(btn1);
//        addPrivateButton(btn2);
//        addPrivateButton(btn3);
//        addPrivateButton(btn4);
    }

}
