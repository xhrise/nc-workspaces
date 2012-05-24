
package nc.ui.eh.trade.z0205524;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 
 * 功能：临时折扣审批
 * 时间：2009-11-20上午11:11:12
 * 作者：张志远
 */
public class ClientUI extends AbstractClientUI {

	private static final long serialVersionUID = 1L;

	public ClientUI() {
        super();
    }

    /**
     * @param arg0
     */
    public ClientUI(Boolean arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */
    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.manage.BillManageUI#createController()
     */
	protected AbstractManageController createController() {
        return new ClientCtrl();
    }
   
    public ManageEventHandler createEventHandler() {
    	return new ClientEventHandler(this, this.getUIControl());
    }
 
	protected void initSelfData() {
    	
    }
}

   
    

