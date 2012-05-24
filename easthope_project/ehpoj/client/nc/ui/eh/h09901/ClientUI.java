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
 * 说明: BUG文档
 * @author 牛冶
 * 2007-11-26 下午06:32:54
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
            //需要先清空
            getBufferData().clear();

            if (vos != null) {
                HYBillVO billVO = new HYBillVO();
                //加载数据到单据
                billVO.setChildrenVO(vos);
                //加载数据到缓冲
                if (getBufferData().isVOBufferEmpty()) {
                    getBufferData().addVOToBuffer(billVO);
                } else {
                    getBufferData().setCurrentVO(billVO);
                }

                //设置当前行
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
     * 功能: 初始化调用setDefaultData()
     * @return:void
     * @author 王建超
     * 2007-10-23 下午06:30:45
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
     * 功能: 表体查询条件
     * @return
     * @return:String
     * @author 王建超
     * 2007-10-23 下午06:25:25
     */
    protected String getBodyWherePart() {
        return " 1=1 " +
        " and vbilltype= '"+this.getUIControl().getBillType()+"' ";
    }

    @Override
	public void initPrivateButton() {
        super.initPrivateButton();
        ButtonVO confirm = ButtonFactory.createButtonVO(IEHButton.CONFIRMBUG, "确认", "确认");
        confirm.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
        addPrivateButton(confirm);
//        ButtonVO confirm2 = ButtonFactory.createButtonVO(IEHButton.CARDAPPROVE, "数据填充", "数据填充");
//        confirm.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
//        addPrivateButton(confirm2);
    }
}
