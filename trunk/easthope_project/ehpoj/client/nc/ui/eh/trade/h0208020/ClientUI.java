
package nc.ui.eh.trade.h0208020;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.trade.button.ButtonVO;

/**
 * 
���ܣ�ӯ�������쳣��
���ߣ�zqy
���ڣ�2008-10-26 ����10:56:36
 */

public class ClientUI extends AbstractClientUI {
    
    public ClientUI() {
        super();
    }

    public ClientUI(Boolean arg0) {
        super(arg0);
    }

    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId){
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

    @Override
	protected AbstractManageController createController() {
        return new ClientCtrl();
    }

    @Override
	public ManageEventHandler createEventHandler() {
        return new ClientEventHandler(this,this.getUIControl());
    }
    
    @Override
	public void setDefaultData() throws Exception {
        super.setDefaultData();
    }
	
    @Override
	public void initPrivateButton() {
        super.initPrivateButton();
        ButtonVO confirm = ButtonFactory.createButtonVO(IEHButton.CONFIRMBUG, "��ѯ", "��ѯ");
        confirm.setOperateStatus(new int[] { IBillOperate.OP_ALL });
        addPrivateButton(confirm);
    }
    
 }
		
