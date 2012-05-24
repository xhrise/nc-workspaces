package nc.ui.eh.sc.h0450516;

import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

/**
 * 功能说明：BOM生报表（一）
 * @author 王明
 * 2008年12月30日9:10:07
 */


public class ClientEventHandler extends AbstractEventHandler {
	
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
    
//	protected void onBoElse(int intBtn) throws Exception {
//		switch (intBtn){
//         case IEHButton.LOCKBILL:    //关闭单据
//             onBoLockBill();
//             break;
//		}
//		super.onBoElse(intBtn);
//	}
	
    @Override
	@SuppressWarnings("unchecked")
	public void onBoSave() throws Exception {
		// 对非空验证
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		super.onBoSave_withBillno();
	}
    
    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit();
    }
    
    
}
