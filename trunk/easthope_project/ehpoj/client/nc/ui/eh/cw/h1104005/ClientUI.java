package nc.ui.eh.cw.h1104005;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.constant.ct.OperationState;
import nc.vo.trade.pub.IBillStatus;

/**���ܣ����õ�¼��
 * @author ����Դ
 * ʱ�䣺2008-5-29 10:45:29
 */

public class ClientUI extends BillManageUI {

	public ClientUI() {
		super();
	}

	public ClientUI(Boolean arg0) {
		super(arg0);
	}

	public ClientUI(String arg0, String arg1, String arg2, String arg3,
			String arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	protected AbstractManageController createController() {
		return  new ClientCtrl();
	}

	@Override
	protected ManageEventHandler createEventHandler() {
		return new ClientEH(this,getUIControl());
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
			throws Exception {

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
			int arg1) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(
			CircularlyAccessibleValueObject[] arg0) throws Exception {

	}

    
	@Override
	public void setDefaultData() throws Exception {
	    //�ڱ�ͷ���ù�˾
		getBillCardPanel().setHeadItem("pk_corp",_getCorp().getPrimaryKey());
        getBillCardPanel().setHeadItem("dr", new Integer(0));
        getBillCardPanel().setHeadItem("fyrq", _getDate());
        getBillCardPanel().setTailItem("coperatorid", _getOperator());
        getBillCardPanel().setTailItem("dmakedate", _getDate());
        BillItem busitype = getBillCardPanel().getHeadItem("pk_busitype");
        if (busitype != null)
            getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
        getBillCardPanel().getHeadItem("vbillstatus").setValue(
                Integer.toString(IBillStatus.FREE));
        
        //���ݺ�
        String pk_corp = _getCorp().getPrimaryKey();
        String billNo = BillcodeRuleBO_Client.getBillCode(getUIControl().getBillType(), pk_corp,
                null, null);
        getBillCardPanel().getHeadItem("billno").setValue(billNo);
	}

    
    /*
     * ����˵�����Զ��尴ť��ȷ�ϣ�
     */
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.CONFIRMBUG, "ȷ��", "ȷ��");
        btn.setOperateStatus(new int[]{OperationState.EDIT});
        addPrivateButton(btn);
        nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"��һҳ","��һҳ");
        btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btnPrev);
        nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"��һҳ","��һҳ");
        btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btnNext);
    }
    
    @Override
	protected void initSelfData() {
        getBillCardWrapper().initHeadComboBox("direction",ICombobox.STR_DIRECTION, true);
        getBillListWrapper().initHeadComboBox("direction",ICombobox.STR_DIRECTION, true);
        getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
        getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
    }

}
