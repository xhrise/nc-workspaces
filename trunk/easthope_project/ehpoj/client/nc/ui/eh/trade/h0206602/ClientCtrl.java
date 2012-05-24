package nc.ui.eh.trade.h0206602;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.h0206602.TradeDriverVO;


/**
 * 功能:司机档案
 * ZB08
 * @author WB
 * 2008-11-21 20:08:18
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
         return ISHSHConst.CARD_BUTTONS;
    }

    @Override
	public int[] getListButtonAry() {
    	 return ISHSHConst.LIST_BUTTONS;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO 自动生成方法存根
        return IBillType.eh_h0206602;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO 自动生成方法存根
        return new String[]{
            PubBillVO.class.getName(),
            TradeDriverVO.class.getName(),
            TradeDriverVO.class.getName()
        };
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO 自动生成方法存根
        return null;
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO 自动生成方法存根
        return "pk_driver";
    }
    
    @Override
	public int getBusinessActionType() {
        return nc.ui.trade.businessaction.IBusinessActionType.BD;
    }
    
    /**
     * 是否存在单据状态。 创建日期：(2004-2-5 13:04:45)
     * 
     * @return boolean
     */
    @Override
	public boolean isExistBillStatus() {
        return false;
    }

}
