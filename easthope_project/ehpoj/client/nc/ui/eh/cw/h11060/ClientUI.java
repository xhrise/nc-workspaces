
package nc.ui.eh.cw.h11060;


import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
/**
 * 说明: 成品核销
 * @author 王明
 * 2008年8月26日14:38:55
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
    /**
     * 功能: 自动显示单句的内容
     * @throws Exception
     * @author 王明
     * 2008-03-24 下午04:03:18
     */
    @Override
	public void setDefaultData() throws Exception {
//        try {
//
//            Class c = Class.forName(getUIControl().getBillVoName()[1]);
//            SuperVO[] vos = getBusiDelegator().queryByCondition(c, getBodyWherePart());
//            //需要先清空
//            getBufferData().clear();
//
//            if (vos != null) {
//                HYBillVO billVO = new HYBillVO();
//                //加载数据到单据
//                billVO.setChildrenVO(vos);
//                //加载数据到缓冲
//                if (getBufferData().isVOBufferEmpty()) {
//                    getBufferData().addVOToBuffer(billVO);
//                } else {
//                    getBufferData().setCurrentVO(billVO);
//                }
//
//                //设置当前行
//                getBufferData().setCurrentRow(0);
//            } else {
//                getBufferData().setCurrentRow(-1);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
    }
    /**
     * 功能: 初始化调用setDefaultData()
     * @return:void
     * @author 王明
     * 2008-03-24 下午04:03:18
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
     * 功能: 按一定的顺序初始化值
     * @return
     * @return:String
     * @author 王明
     * 2008-03-24 下午04:03:18
     */
    protected String getBodyWherePart() {
//        return " pk_corp='" + _getCorp().getPk_corp() + "' order by poscode ";
    	return null;
    }
    @Override
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"查询","查询");
        addPrivateButton(btnPrev); 
        nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"核销","核销");
        addPrivateButton(btnNext);
        super.initPrivateButton();
    }
//  //前后台校验
//	@Override
//	public Object getUserObject() {
//		return new ClientUICheckRuleGetter();
//	}
}