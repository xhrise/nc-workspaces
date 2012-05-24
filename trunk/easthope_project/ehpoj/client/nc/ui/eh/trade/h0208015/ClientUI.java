/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.trade.h0208015;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * ����:��Ʒӯ����������¼��
 * ZA90
 * @author WB
 * 2008-10-14 16:19:52
 */
public class ClientUI extends AbstractClientUI{

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
    
    @Override
	public void setDefaultData() throws Exception {
        super.setDefaultData();
    }
  
    @Override
	protected void initSelfData() {
    }
   
    /*
     * ע���Զ��尴ť
     * 2008-04-02
     */
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.GENRENDETAIL,"���ɳ�Ʒ��ϸ","���ɳ�Ʒ��ϸ");
        btn.setOperateStatus(new int[]{IBillOperate.OP_EDIT,IBillOperate.OP_ADD});
        addPrivateButton(btn);
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.CALCKCYBB,"ӯ������","ӯ������");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn1);
        super.initPrivateButton();
    }
}

   
    

