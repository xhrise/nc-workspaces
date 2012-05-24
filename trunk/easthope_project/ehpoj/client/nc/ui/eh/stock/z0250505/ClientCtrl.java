package nc.ui.eh.stock.z0250505;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0250505.StockInBVO;
import nc.vo.eh.stock.z0250505.StockInVO;



/**
 * 功能说明：入库单
 * @author 王明
 * 2008-03-24 下午04:03:18
 */
public class ClientCtrl extends AbstractCtrl {

	 @Override
	public int[] getCardButtonAry() {        
	        int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
	                new int[] { IBillButton.Busitype},
	                ISHSHConst.CARD_BUTTONS_M, 0);
	    	int[] btnss=nc.ui.eh.button.ButtonTool.insertButtons(btns,new int[]{IEHButton.BusinesBtn},0);
	        return btns;//modify by houcq 2010-11-29取消采购入库单终止单据按钮
	    }

	 @Override
	public int[] getListButtonAry() {
	       return nc.ui.eh.button.ButtonTool.insertButtons(
	    		   new int[]{IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
	    }


    @Override
	public String getBillType() {
        return IBillType.eh_z0250505;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            StockInVO.class.getName(),
            StockInBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_in_b";
    }

    @Override
	public String getPkField() {
        return "pk_in";
    }

}
