package nc.ui.eh.cw.h1104010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.h1104005.ArapFyVO;
import nc.vo.eh.pub.PubBillVO;

/** 
 * ˵�������õ�����
 * @author wb
 * ʱ�䣺2008-8-20 20:44:15
 */
public class ClientCtrl extends AbstractCtrl {

	
	@Override
	public String getBillType() {
        return IBillType.eh_h1104005;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            ArapFyVO.class.getName(),
	            ArapFyVO.class.getName()    
	        };
	    }
	
	@Override
	public String getChildPkField() {
		return null;
	}

	@Override
	public String getPkField() {
		return "pk_fy";
	}

	@Override
	public int[] getCardButtonAry() {
        return new int[]{
                IBillButton.Audit,
                IBillButton.Refresh,
                IBillButton.Return
        };
	}
	
	 @Override
	public int[] getListButtonAry() {
         return new int[]{
                 IBillButton.Query,
                 IBillButton.Card,
                 IBillButton.Refresh
         };
	    }	    
}
