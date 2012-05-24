/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.kc.h0257515;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;


/**
 * ����:��Ʒ�̵㵥
 * ZB02
 * @author WB
 * 2008-10-15 16:30:09
 *
 */
public class ClientUI extends nc.ui.eh.kc.h0257505.ClientUI{
	
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

    /* ���� Javadoc��
     * @see nc.ui.trade.manage.BillManageUI#createController()
     */
    @Override
	protected AbstractManageController createController() {
        // TODO �Զ����ɷ������
        return new ClientCtrl();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.trade.manage.BillManageUI#createEventHandler()
     */
    @Override
	public ManageEventHandler createEventHandler() {
        // TODO Auto-generated method stub
        return new ClientEventHandler(this,this.getUIControl());
    }
    
    
   
   
 
    /*
     * ע���Զ��尴ť
     * 2008-04-02
     */
    @Override
	protected void initPrivateButton() {
    	super.initPrivateButton();
    }
 
    @Override
    public boolean beforeEdit(BillEditEvent e) {
    	return super.beforeEdit(e);
    }
    @Override
    public void afterEdit(BillEditEvent e) {
    	super.afterEdit(e);
    }
    
}

   
    


