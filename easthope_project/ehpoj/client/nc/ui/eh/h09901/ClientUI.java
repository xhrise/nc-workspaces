/**
 * 
 */
package nc.ui.eh.h09901;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * ˵��: BUG�ĵ�
 * @author ţұ
 * 2007-11-26 ����06:32:54
 */
public class ClientUI extends BillCardUI {


    public ClientUI() {
        super();
        initilize();
    }

    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater,
            String billId) {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
        initilize();
    }

    @Override
	protected ICardController createController() {
        return new ClientCtrl();
    }

    @Override
	protected CardEventHandler createEventHandler() {
        return new ClientEventHandler(this,this.getUIControl());
    }
    
    @Override
	public String getRefBillType() {
        return null;
    }

    @Override
	protected void initSelfData() {
        getBillCardWrapper().initBodyComboBox("eh_bug", "bugtype", ICombobox.STR_BUGTYPE, true);
    }

    @Override
	public void setDefaultData() throws Exception {

        try {

            Class c = Class.forName(getUIControl().getBillVoName()[1]);
            SuperVO[] vos = getBusiDelegator().queryByCondition(c, getBodyWherePart());
            //��Ҫ�����
            getBufferData().clear();

            if (vos != null) {
                HYBillVO billVO = new HYBillVO();
                //�������ݵ�����
                billVO.setChildrenVO(vos);
                //�������ݵ�����
                if (getBufferData().isVOBufferEmpty()) {
                    getBufferData().addVOToBuffer(billVO);
                } else {
                    getBufferData().setCurrentVO(billVO);
                }

                //���õ�ǰ��
                getBufferData().setCurrentRow(0);
            } else {
                getBufferData().setCurrentRow(-1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        getBillCardPanel().setHeadItem("pk_corp", _getCorp().pk_corp);
    }
    
    /**
     * ����: ��ʼ������setDefaultData()
     * @return:void
     * @author ������
     * 2007-10-23 ����06:30:45
     */
    private void initilize() {
        getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
        try {
            setDefaultData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * ����: �����ѯ����
     * @return
     * @return:String
     * @author ������
     * 2007-10-23 ����06:25:25
     */
    protected String getBodyWherePart() {
        return " 1=1 " +
        " and vbilltype= '"+this.getUIControl().getBillType()+"' ";
    }

    @Override
	public void initPrivateButton() {
        super.initPrivateButton();
        ButtonVO confirm = ButtonFactory.createButtonVO(IEHButton.CONFIRMBUG, "ȷ��", "ȷ��");
        confirm.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
        addPrivateButton(confirm);
//        ButtonVO confirm2 = ButtonFactory.createButtonVO(IEHButton.CARDAPPROVE, "�������", "�������");
//        confirm.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
//        addPrivateButton(confirm2);
    }
}
