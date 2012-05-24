package nc.ui.eh.trade.z0205524;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0205523.TradeLsdiscBVO;
import nc.vo.eh.trade.z0205523.TradeLsdiscVO;

/**
 * 
 * 功能：临时折扣
 * 时间：2009-11-20上午11:12:50
 * 作者：张志远
 */
public class ClientCtrl extends AbstractCtrl {

    public ClientCtrl() {
        super();
    }

	public int[] getListButtonAry() {
        return nc.ui.eh.pub.PubTools.getSPLButton();
    }

	public int[] getCardButtonAry() {
		return nc.ui.eh.pub.PubTools.getSPCButton();
	}
   
	public String getBillType() {
        return IBillType.eh_z0205523;
    }

    
    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBusinessActionType()
     */
	public int getBusinessActionType() {
        return IBusinessActionType.PLATFORM;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
	public String getChildPkField() {
        return "pk_trade_lsdisc_b";
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
	public String getPkField() {
        return "pk_trade_lsdisc";
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            TradeLsdiscVO.class.getName(),
            TradeLsdiscBVO.class.getName()  
        };
    }

}
