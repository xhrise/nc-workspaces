package nc.ui.eh.trade.z0205524;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0205523.TradeLsdiscBVO;
import nc.vo.eh.trade.z0205523.TradeLsdiscVO;

/**
 * 
 * ���ܣ���ʱ�ۿ�
 * ʱ�䣺2009-11-20����11:12:50
 * ���ߣ���־Զ
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

    
    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBusinessActionType()
     */
	public int getBusinessActionType() {
        return IBusinessActionType.PLATFORM;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
	public String getChildPkField() {
        return "pk_trade_lsdisc_b";
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
	public String getPkField() {
        return "pk_trade_lsdisc";
    }

    /* ���� Javadoc��
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
