package nc.ui.eh.trade.z00104;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * ˵�����ֿ�ά��
 * @author ����Դ 
 * ʱ�䣺2008-4-24
 */
public class ClientEventHandler extends ManageEventHandler {
	

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

	@Override
	public void onBoSave() throws Exception {
		//�ǿ��ж�
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();  
            
        super.onBoSave();
		}
	
}