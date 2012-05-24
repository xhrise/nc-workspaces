
package nc.ui.eh.cw.h1300610;


import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * 功能：工资总额计算
 * ZB07
 * 作者:WB
 * 时间：2008-11-3 10:25:38
 */
public class ClientUI extends BillCardUI {

    public ClientUI() {
        super();
        initilize();
    }

    public ClientUI(String arg0, String arg1, String arg2, String arg3, String arg4) {
        super(arg0, arg1, arg2, arg3, arg4);
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
        return " pk_corp='" + _getCorp().getPk_corp() + "' and isnull(dr,0)=0 order by nyear,nmonth ";
    }


	@Override
	public Object getUserObject() {
		return null;
	}

	/*
     * 注册自定义按钮
     * 2008-04-02
     */
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.CALCKCYBB,"计算工资总额","计算工资总额");
        btn.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn);
        super.initPrivateButton();
    }

}