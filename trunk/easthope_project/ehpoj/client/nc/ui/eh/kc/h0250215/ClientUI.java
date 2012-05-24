/**
 * 
 */
package nc.ui.eh.kc.h0250215;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;

/**
 * 说明: 会计期间关帐
 * @author 牛冶
 * 2007-9-20 下午01:03:18
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
     * 功能: 自动显示记录
     * @throws Exception
     * @author 牛冶
     * 2007-10-23 下午06:24:59
     */
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
    /**
     * 功能: 初始化调用setDefaultData()
     * @return:void
     * @author 牛冶
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
     * @author 牛冶
     * 2007-10-23 下午06:25:25
     */
    protected String getBodyWherePart() {
        return " pk_corp='" + _getCorp().getPk_corp() + "' order by nyear,nmonth";
    }
    
    /*
     * 功能说明：自定义按钮
     */
    @Override
	protected void initPrivateButton() {
    	//add by houcq 2011-04-27增加新增年度期间按钮
        nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.PeriodClose, "结帐", "结帐");
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.PERIODCANCEL, "反结帐", "取消结帐");
        nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.DOCMANAGE, "新增年度期间", "新增年度期间");
        
        btn.setOperateStatus(new int[]{IBillStatus.COMMIT,IBillStatus.CHECKGOING,IBillStatus.CHECKPASS,IBillStatus.NOPASS});
        btn1.setOperateStatus(new int[]{IBillStatus.COMMIT,IBillStatus.CHECKGOING,IBillStatus.CHECKPASS,IBillStatus.NOPASS});
        btn2.setOperateStatus(new int[]{IBillStatus.COMMIT,IBillStatus.CHECKGOING,IBillStatus.CHECKPASS,IBillStatus.NOPASS});
        addPrivateButton(btn);
        addPrivateButton(btn1);
        addPrivateButton(btn2);
    }
    
    
}
