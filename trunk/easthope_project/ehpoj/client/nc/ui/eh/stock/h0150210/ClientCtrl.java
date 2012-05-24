package nc.ui.eh.stock.h0150210;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.h0150206.StockQuerypriceBVO;
import nc.vo.eh.stock.h0150206.StockQuerypriceVO;


/**
 * 功能说明：询价单
 * @author 王明
 * 2008年12月11日15:38:11
 */
public class ClientCtrl extends AbstractCtrl {

	 @Override
	public int[] getCardButtonAry() {        
	        int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
	                new int[] { IBillButton.Busitype},
	                ISHSHConst.CARD_BUTTONS_M, 0);
	        return btns;
	    }

	 @Override
	public int[] getListButtonAry() {
	       return nc.ui.eh.button.ButtonTool.insertButtons(
	               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
	    }

    @Override
	public String getBillType() {
        return IBillType.eh_h0150210;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            StockQuerypriceVO.class.getName(),
            StockQuerypriceBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_queryprice_b";
    }

    @Override
	public String getPkField() {
        return "pk_queryprice";
    }

}
