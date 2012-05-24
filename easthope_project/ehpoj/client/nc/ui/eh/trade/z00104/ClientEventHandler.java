package nc.ui.eh.trade.z00104;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 说明：仓库维护
 * @author 张起源 
 * 时间：2008-4-24
 */
public class ClientEventHandler extends ManageEventHandler {
	

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

	@Override
	public void onBoSave() throws Exception {
		//非空判断
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();  
            
        super.onBoSave();
		}
	
}