
package nc.ui.eh.voucher.h10115;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.trade.button.ButtonVO;

/**
 * 说明: 凭证导入
 * 
 * @author wb 2009-9-27 13:49:49
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
    }

    @Override
	public void setDefaultData() throws Exception {
    }
    

    private void initilize() {
        getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
        try {
            setDefaultData();
            getBillCardPanel().getBodyItem("itemcode1").setName("29120");
            updateUI();
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
        ButtonVO confirm2 = ButtonFactory.createButtonVO(IEHButton.ExcelImport, "生成凭证", "生成凭证");
        confirm2.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
        addPrivateButton(confirm2);
        ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.GENRENDETAIL, "生成XML", "生成XML");
        btn1.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
        addPrivateButton(btn1);
    }
}
