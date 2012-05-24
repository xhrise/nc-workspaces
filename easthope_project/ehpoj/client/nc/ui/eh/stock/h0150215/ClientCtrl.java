package nc.ui.eh.stock.h0150215;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.h0150206.StockQuerypriceBVO;
import nc.vo.eh.stock.h0150206.StockQuerypriceVO;


/**
 * 功能说明：询价单审批
 * @author 王兵
 * 2008-12-25 16:39:00
 */
public class ClientCtrl extends AbstractCtrl {

    /**
     * 
     */
    public ClientCtrl() {
        super();
      
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
        return IBillType.eh_h0150210;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO 自动生成方法存根
        return new String[]{
            PubBillVO.class.getName(),
            StockQuerypriceVO.class.getName(),
            StockQuerypriceBVO.class.getName()  
        };
    }


    @Override
	public String getChildPkField() {
        // TODO 自动生成方法存根
        return "pk_queryprice_b";
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO 自动生成方法存根
        return "pk_queryprice";
    }
    

}
