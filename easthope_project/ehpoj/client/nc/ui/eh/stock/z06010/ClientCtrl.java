package nc.ui.eh.stock.z06010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z06005.SbbillBVO;
import nc.vo.eh.stock.z06005.SbbillVO;

/**功能：司磅单审批
 * @author wb
 * 2008-10-27 13:43:47
 */

public class ClientCtrl extends AbstractCtrl {

    public ClientCtrl() {
        super();
        // TODO 自动生成构造函数存根
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.bill.ICardController#getCardBodyHideCol()
     */
    @Override
	public String[] getCardBodyHideCol() {
        // TODO 自动生成方法存根
        return null;
    }

    @Override
	public int[] getListButtonAry() {
        return nc.ui.eh.pub.PubTools.getSPLButton();
    }

	@Override
	public int[] getCardButtonAry() {
		return nc.ui.eh.pub.PubTools.getSPCButton();
	}
	


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO 自动生成方法存根
        return IBillType.eh_z06005;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO 自动生成方法存根
        return new String[]{
        		 PubBillVO.class.getName(),
                 SbbillVO.class.getName(),
                 SbbillBVO.class.getName()
        };
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
        return "pk_sbbill_b";
    }

    
    @Override
	public String getPkField() {
        // TODO 自动生成方法存根
        return "pk_sbbill";
    }

    

}
