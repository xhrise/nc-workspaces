package nc.ui.eh.kc.h0252005;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.kc.h0252005.ScDbdBVO;
import nc.vo.eh.kc.h0252005.ScDbdVO;
import nc.vo.eh.pub.PubBillVO;



/**
 * 功能说明：材料调拨单
 * @author 王明
 * 2008-05-07 下午04:03:18
 */
public class ClientCtrl extends AbstractCtrl {

	 @Override
	public int[] getCardButtonAry() {        
	        int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
	                new int[] {IBillButton.Busitype  },
	                 ISHSHConst.CARD_BUTTONS_M, 0);
	        return btns;

	    }

	 @Override
	public int[] getListButtonAry() {
	       return nc.ui.eh.button.ButtonTool.insertButtons(
	               new int[] {IBillButton.Busitype  },ISHSHConst.LIST_BUTTONS_M, 0);
	
	    }

    @Override
	public String getBillType() {
        return IBillType.eh_h0252005;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            ScDbdVO.class.getName(),
            ScDbdBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_dbd_b";
    }

    @Override
	public String getPkField() {
        return "pk_dbd";
    }


}
