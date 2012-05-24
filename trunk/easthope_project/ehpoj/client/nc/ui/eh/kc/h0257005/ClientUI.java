
package nc.ui.eh.kc.h0257005;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * ˵����ԭ�Ͽ���±���
 * ���ͣ�ZA48
 * ���ߣ�wb
 * ʱ�䣺2008��5��8��16:34:56
 */
public class ClientUI extends BillManageUI {

    
    
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
	protected ManageEventHandler createEventHandler() {
        // TODO Auto-generated method stub
        return new ClientEventHandler(this,this.getUIControl());
    }
    
    /* ���� Javadoc��
     * @see nc.ui.trade.manage.BillManageUI#setBodySpecialData(nc.vo.pub.CircularlyAccessibleValueObject[])
     */
    @Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
            throws Exception {
        // TODO �Զ����ɷ������

    }

    /* ���� Javadoc��
     * @see nc.ui.trade.manage.BillManageUI#setHeadSpecialData(nc.vo.pub.CircularlyAccessibleValueObject, int)
     */
    @Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
            int arg1) throws Exception {
        // TODO �Զ����ɷ������

    }

    /* ���� Javadoc��
     * @see nc.ui.trade.manage.BillManageUI#setTotalHeadSpecialData(nc.vo.pub.CircularlyAccessibleValueObject[])
     */
    @Override
	protected void setTotalHeadSpecialData(
            CircularlyAccessibleValueObject[] arg0) throws Exception {
        // TODO �Զ����ɷ������

    }
    
    /* ���� Javadoc��
     * @see nc.ui.trade.base.AbstractBillUI#setDefaultData()
     */
    @Override
	public void setDefaultData() throws Exception {

        // ��ͷ���ù�˾����        
        getBillCardPanel().setHeadItem("pk_corp",_getCorp().getPk_corp());
        // ��ͷ��ɾ�����  
        getBillCardPanel().setHeadItem("dr",new Integer(0));
       

    }

    @Override
    protected void initSelfData() {
      // ��ʾ���ݿ��е�0.
      getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);  
    }
   
    /*
     * ע���Զ��尴ť
     * 2008��5��7��13:42:54
     */
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.CALCKCYBB,"�����±���","�����±���");
        addPrivateButton(btn1);
    }




}

   
    

