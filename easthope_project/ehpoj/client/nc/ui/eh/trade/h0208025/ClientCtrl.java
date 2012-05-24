package nc.ui.eh.trade.h0208025;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.h0208020.TradeCheckBVO;
import nc.vo.eh.trade.h0208020.TradeCheckVO;

/**
 * 
功能：盈亏考核异常表(审批)
作者：zqy
日期：2008-10-26 上午10:56:36
 */

public class ClientCtrl extends AbstractCtrl {

    public ClientCtrl() {
        super();
      
    }
    @Override
	public int[] getCardButtonAry() {        
        return new int[]{
                IBillButton.Audit,
                IBillButton.Refresh,
                IBillButton.Return
        };
    }

    @Override
	public int[] getListButtonAry() {
        return new int[]{
                        IBillButton.Query,
                        IBillButton.Card
                };
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO 自动生成方法存根
        return IBillType.eh_h0208020;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO 自动生成方法存根
        return new String[]{
            PubBillVO.class.getName(),
            TradeCheckVO.class.getName(),
            TradeCheckBVO.class.getName()
        };
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO 自动生成方法存根
        return "pk_check_b";
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO 自动生成方法存根
        return "pk_check";
    }

}
