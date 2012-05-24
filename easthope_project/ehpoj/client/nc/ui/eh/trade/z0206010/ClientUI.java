
package nc.ui.eh.trade.z0206010;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * ���� ���۶���
 * @author �麣
 * 2008-04-08
 */
public class ClientUI extends AbstractClientUI{


    public ClientUI() {
        super();
        // TODO �Զ����ɹ��캯�����
    }


    public ClientUI(Boolean arg0) {
        super(arg0);
    }

    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

    @Override
	protected AbstractManageController createController() {
        return new ClientCtrl();
    }

    @Override
    public ManageEventHandler createEventHandler() {
    	return new AbstractSPEventHandler(this, this.getUIControl());
    }
   
    
  

}

   
    

