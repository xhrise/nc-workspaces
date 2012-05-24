package nc.ui.eh.stock.h0150110;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.h0150110.StockSpecplanBVO;
import nc.vo.eh.stock.h0150110.StockSpecplanVO;


/**
 * 功能:特殊采购计划
 * ZB16
 * @author WB
 * 2008-12-24 15:45:31
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
                 new int[] { },
                 0);
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
        return IBillType.eh_h0150110;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO 自动生成方法存根
        return new String[]{
            PubBillVO.class.getName(),
            StockSpecplanVO.class.getName(),
            StockSpecplanBVO.class.getName()
        };
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO 自动生成方法存根
        return "pk_plan_b";
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO 自动生成方法存根
        return "pk_plan";
    }
    

}
