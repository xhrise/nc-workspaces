package nc.ui.eh.sc.h0450517;

import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

/**
 * ����˵����BOM����������
 * @author ����
 * 2008��12��30��9:10:07
 */


public class ClientEventHandler extends AbstractEventHandler {
	
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
    
//	protected void onBoElse(int intBtn) throws Exception {
//		switch (intBtn){
//         case IEHButton.LOCKBILL:    //�رյ���
//             onBoLockBill();
//             break;
//		}
//		super.onBoElse(intBtn);
//	}
	
    @Override
	@SuppressWarnings("unchecked")
	public void onBoSave() throws Exception {
		// �Էǿ���֤
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		super.onBoSave();
	}
    
    @Override
    protected void onBoDelete() throws Exception {
    	int res = onBoDeleteN(); // 1Ϊɾ�� 0Ϊȡ��ɾ��
    	if(res==0){
    		return;
    	}
    	super.onBoTrueDelete();
	}
    
    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit();
    }
    
    
}
