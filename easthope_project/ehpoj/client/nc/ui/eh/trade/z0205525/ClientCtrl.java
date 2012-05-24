package nc.ui.eh.trade.z0205525;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0205525.DiscountAdjustBVO;
import nc.vo.eh.trade.z0205525.DiscountAdjustVO;

/** 
 * 说明：折扣调整单 
 * @author 张起源 
 * 时间：2008-4-12
 */
public class ClientCtrl extends AbstractCtrl {

	
	@Override
	public String getBillType() {
        return IBillType.eh_z0205525;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            DiscountAdjustVO.class.getName(),
	            DiscountAdjustBVO.class.getName()    
	        };
	    }

	@Override
	public String getChildPkField() {
		return "pk_discountadjust_b";
	}

	@Override
	public String getPkField() {
		return "pk_discountadjust";
	}

	@Override
	public int getBusinessActionType() {
		return IBusinessActionType.PLATFORM;
	}

       @Override
	public int[] getCardButtonAry() {        
            int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                    new int[] { },
                    ISHSHConst.CARD_BUTTONS_M, 0);
            int[] btnss=nc.ui.eh.button.ButtonTool.insertButtons(
            		btns,
            		new int[]{IEHButton.GENRENDETAIL}, 0);
            return btnss;
        }

       @Override
	public int[] getListButtonAry() {
           return nc.ui.eh.button.ButtonTool.insertButtons(
                   new int[] {},ISHSHConst.LIST_BUTTONS_M, 0);
        }


}
