
package nc.ui.eh.cw.h11055;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * ˵�����ɱ�����
 * ���ͣ�ZA72
 * ���ߣ�wb
 * ʱ�䣺2008-8-11 15:34:32
 */
public class ClientUI extends AbstractClientUI {

    
    
    public ClientUI() {
        super();        
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

    /*
     * (non-Javadoc)
     * @see nc.ui.trade.manage.BillManageUI#createEventHandler()
     */
    @Override
	public ManageEventHandler createEventHandler() {
        // TODO Auto-generated method stub
        return new ClientEventHandler(this,this.getUIControl());
    }
    
  
    @Override
    protected void initSelfData() {
    	 getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
    }
   
    /*
     * ע���Զ��尴ť
     * 2008��5��7��13:42:54
     */
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.CALCKCYBB,"�ɱ�����","�ɱ�����");
        addPrivateButton(btn1);
        
        nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.GENRENDETAIL,"����ƾ֤","����ƾ֤");
        btn2.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn2);
    }




}

   
    

