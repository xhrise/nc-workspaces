package nc.ui.eh.cw.h1103005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.h1103005.ArapSkBVO;
import nc.vo.eh.cw.h1103005.ArapSkVO;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;

/** 
 * 说明：收款单
 * @author 张起源 
 * 时间：2008-5-28 14:35:52
 */
public class ClientCtrl extends AbstractCtrl {

	
	@Override
	public String getBillType() {
        return IBillType.eh_h1103005;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
                ArapSkVO.class.getName(),
                ArapSkBVO.class.getName()    
	        };
	    }
	
	@Override
	public String getChildPkField() {
		return "pk_sk_b";
	}

	@Override
	public String getPkField() {
		return "pk_sk";
	}

	@Override
	public int[] getCardButtonAry() {
		 int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
	                new int[] { IBillButton.Busitype},
	                ISHSHConst.CARD_BUTTONS_M, 0);
         int[] btns2 = nc.ui.eh.button.ButtonTool.insertButtons(
                 new int[] {IEHButton.BusinesBtn},btns,15);
	     return btns2;
	}
	
	 @Override
	public int[] getListButtonAry() {
		 return nc.ui.eh.button.ButtonTool.insertButtons(
	               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
	    }	    
}
