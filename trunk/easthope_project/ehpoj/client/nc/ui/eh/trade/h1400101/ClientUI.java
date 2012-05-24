
package nc.ui.eh.trade.h1400101;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * ˵��������������
 * ���ͣ�ZB41
 * ���ߣ���־Զ
 * ʱ�䣺2010��01��26��
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

	protected AbstractManageController createController() {
        return new ClientCtrl();
    }

	protected ManageEventHandler createEventHandler() {
        return new ClientEventHandler(this,this.getUIControl());
    }
    
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
            throws Exception {

    }

	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
            int arg1) throws Exception {
    }

	protected void setTotalHeadSpecialData(
            CircularlyAccessibleValueObject[] arg0) throws Exception {
    }
    
	public void setDefaultData() throws Exception {

        // ��ͷ���ù�˾����        
        getBillCardPanel().setHeadItem("pk_corp",_getCorp().getPk_corp());
        // ��ͷ��ɾ�����  
        getBillCardPanel().setHeadItem("dr",new Integer(0));
       

    }

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
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.CALCKCYBB,"�����������","�����������");
        addPrivateButton(btn1);
    }

}

   
    

