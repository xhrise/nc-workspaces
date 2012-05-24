
package nc.ui.eh.h08002;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.button.ButtonVO;

/**
 * 说明: 客商数据导入
 * @author zqy
 * 2008-8-5 11:02:18
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
        ButtonVO confirm = ButtonFactory.createButtonVO(IEHButton.CONFIRMBUG, "读取", "读取");
        confirm.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
        addPrivateButton(confirm);
        ButtonVO confirm2 = ButtonFactory.createButtonVO(IEHButton.CARDAPPROVE, "导入", "导入");
        confirm.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
        addPrivateButton(confirm2);
    }
}
