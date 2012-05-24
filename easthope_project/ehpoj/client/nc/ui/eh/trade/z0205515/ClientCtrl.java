package nc.ui.eh.trade.z0205515;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0205505.FirstdiscountBVO;
import nc.vo.eh.trade.z0205505.FirstdiscountVO;


/**
 * 功能 一次折扣管理（审批）
 * @author 洪海
 * 2008-04-08
 */
public class ClientCtrl extends AbstractCtrl {

    public ClientCtrl() {
        super();
        // TODO 自动生成构造函数存根
    }

 
    @Override
	public int[] getListButtonAry() {
        return nc.ui.eh.pub.PubTools.getSPLButton();
    }

	@Override
	public int[] getCardButtonAry() {
		return nc.ui.eh.pub.PubTools.getSPCButton();
	}
	

   
    @Override
	public String getBillType() {
        // TODO 自动生成方法存根
        return IBillType.eh_z0205505;
    }

    
    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBusinessActionType()
     */
    @Override
	public int getBusinessActionType() {
        // TODO 自动生成方法存根
        //return IBusinessActionType.PLATFORM;
        return IBusinessActionType.PLATFORM;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO 自动生成方法存根
        return "pk_firstdiscount_b";
    }

  

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO 自动生成方法存根
        return "pk_firstdiscount";
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO 自动生成方法存根
        return new String[]{
            PubBillVO.class.getName(),
            FirstdiscountVO.class.getName(),
            FirstdiscountBVO.class.getName()
        };
    }

}
