package nc.ui.ehpta.hq010202;

import nc.ui.ehpta.pub.valid.Validata;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 
 * 该类是一个抽象类，主要目的是生成按钮事件处理的框架
 * 
 * @author author
 * @version tempProject version
 */

public class EventHandler extends ManageEventHandler {

	public EventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {
	}
	
	@Override
	protected void onBoSave() throws Exception {
		Validata.saveValidataIsNull(getBillCardPanelWrapper().getBillCardPanel(), getBillCardPanelWrapper().getBillVOFromUI(), new String[] { "ehpta_storfare_b" });
		super.onBoSave();
	}

}