/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.trade.z0205515;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * ���� һ���ۿ۹���������
 * @author �麣
 * 2008-04-08
 */
public class ClientUI extends AbstractClientUI {

    


    /**
     * 
     */
    public ClientUI() {
        super();
        // TODO �Զ����ɹ��캯�����
    }

    /**
     * @param arg0
     */
    public ClientUI(Boolean arg0) {
        super(arg0);
        // TODO �Զ����ɹ��캯�����
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

    /* ���� Javadoc��
     * @see nc.ui.trade.manage.BillManageUI#createController()
     */
    @Override
	protected AbstractManageController createController() {
        // TODO �Զ����ɷ������
        return new ClientCtrl();
    }
   
    @Override
    public ManageEventHandler createEventHandler() {
    	// TODO Auto-generated method stub
    	return new ClientEventHandler(this, this.getUIControl());
    }
 
    @Override
	protected void initSelfData() {
    	
    }
}

   
    

