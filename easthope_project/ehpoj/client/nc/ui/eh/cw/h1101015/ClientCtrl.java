package nc.ui.eh.cw.h1101015;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.h1101005.ArapFkBVO;
import nc.vo.eh.cw.h1101005.ArapFkVO;
import nc.vo.eh.pub.PubBillVO;


/**
 * 功能说明：付款确认
 * @author 王明
 * 2008-05-28 下午04:03:18
 */
public class ClientCtrl extends AbstractCtrl {

	 @Override
	public int[] getCardButtonAry() {        
	        return new int[]{
	        		IEHButton.SUREMONEY,
	        		IBillButton.Return,
	        		
	        		
	        };
	    }

	 @Override
	public int[] getListButtonAry() {
	       return new int[] {
	    		   IBillButton.Query,
	    		   IBillButton.Card,
	       };
	    }

    @Override
	public String getBillType() {
        return IBillType.eh_h1101005;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            ArapFkVO.class.getName(),
            ArapFkBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_fk_b";
    }

    @Override
	public String getPkField() {
        return "pk_fk";
    }


}
