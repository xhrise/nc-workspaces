
package nc.ui.eh.h08007;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.button.ButtonVO;

/**
 * 
���ܣ���NC�аѹ�Ӧ�����ݵ��뵽U8���̱���
���ߣ�zqy
���ڣ�2008-11-6 ����09:10:14
 */

public class ClientUI extends BillCardUI {

    public static String pk_corp=null;
    public static UFDate dmakedate = null;
    public static String coperatrid = null;
    
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
    }

    @Override
	public void setDefaultData() throws Exception {
        pk_corp = _getCorp().getPrimaryKey();
        dmakedate = _getDate();
        coperatrid = _getOperator();
    }
    

    private void initilize() {
        getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
        try {
            setDefaultData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getBodyWherePart() {
        return null;
    }

    @Override
	public void initPrivateButton() {
        super.initPrivateButton();
        ButtonVO confirm = ButtonFactory.createButtonVO(IEHButton.CONFIRMBUG, "��ȡ", "��ȡ");
        confirm.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
        addPrivateButton(confirm);
        ButtonVO confirm2 = ButtonFactory.createButtonVO(IEHButton.CARDAPPROVE, "����", "����");
        confirm.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
        addPrivateButton(confirm2);
    }
}
