package nc.ui.eh.kc.h0251010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.kc.h0251005.ScCkdBVO;
import nc.vo.eh.kc.h0251005.ScCkdVO;
import nc.vo.eh.pub.PubBillVO;

/** 
 * 说明：	其它材料出库单 
 * @author 张起源 
 * 时间：2008-5-08
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
        return IBillType.eh_h0251010;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            ScCkdVO.class.getName(),
	            ScCkdBVO.class.getName()      
	        };
	    }

	@Override
	public String getChildPkField() {
		return "pk_ckd_b";
	}

	@Override
	public String getPkField() {
		return "pk_ckd";
	}

	
}
