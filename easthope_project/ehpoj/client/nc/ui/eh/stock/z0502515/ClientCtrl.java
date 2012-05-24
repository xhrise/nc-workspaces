package nc.ui.eh.stock.z0502515;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0502515.StockBackBVO;
import nc.vo.eh.stock.z0502515.StockBackVO;



/**
 * 功能说明：原料退货通知单
 * @author 王兵
 * 2008-7-24 10:35:03
 */
public class ClientCtrl extends AbstractCtrl {

	 @Override
	public int[] getCardButtonAry() {        
	        int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
	                new int[] { IBillButton.Busitype},
	                ISHSHConst.CARD_BUTTONS_M, 0);
	        int[] btns1=nc.ui.eh.button.ButtonTool.insertButtons(new int[] {IEHButton.BusinesBtn}, btns, 14);
	        
	        return btns;//modify by houcq 2010-11-20取消退货通知单终止单据按钮
	    }

	 @Override
	public int[] getListButtonAry() {
	       return nc.ui.eh.button.ButtonTool.insertButtons(
	               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
	    }

    @Override
	public String getBillType() {
        return IBillType.eh_z0502515;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            StockBackVO.class.getName(),
            StockBackBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_back_b";
    }

    @Override
	public String getPkField() {
        return "pk_back";
    }

}
