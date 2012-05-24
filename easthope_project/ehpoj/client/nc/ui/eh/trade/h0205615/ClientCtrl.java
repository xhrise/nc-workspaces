package nc.ui.eh.trade.h0205615;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.h0205615.TradePeriodplanBVO;
import nc.vo.eh.trade.h0205615.TradePeriodplanVO;


/**
 * 功能:销售旬计划
 * ZB14
 * @author WB
 * 2008-12-22 13:29:43
 *
 */
public class ClientCtrl extends AbstractCtrl {

    /**
     * 
     */
    public ClientCtrl() {
        super();
      
    }
    @Override
	public int[] getCardButtonAry() {        
    	 int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(ISHSHConst.CARD_BUTTONS_M,
                 new int[] { IBillButton.Busitype},
                 99);
         int[] btns1=nc.ui.eh.button.ButtonTool.insertButtons(btns,
                 new int[] { IBillButton.Copy,IEHButton.GENRENDETAIL,IEHButton.STOCKCHANGE},
                 0);//modify by houcq 2010-12-03取消销售旬计划录入终止单据按钮
         return btns1;
    }

    @Override
	public int[] getListButtonAry() {
    	 return nc.ui.eh.button.ButtonTool.insertButtons(
                 new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO 自动生成方法存根
        return IBillType.eh_h0205615;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO 自动生成方法存根
        return new String[]{
            PubBillVO.class.getName(),
            TradePeriodplanVO.class.getName(),
            TradePeriodplanBVO.class.getName()
        };
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO 自动生成方法存根
        return "pk_periodplan_b";
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO 自动生成方法存根
        return "pk_periodplan";
    }
    

}
