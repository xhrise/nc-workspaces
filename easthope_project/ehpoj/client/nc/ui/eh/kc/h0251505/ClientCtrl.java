package nc.ui.eh.kc.h0251505;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.kc.h0251505.ScBldBVO;
import nc.vo.eh.kc.h0251505.ScBldVO;
import nc.vo.eh.pub.PubBillVO;

/** 
 * 说明：生产备料单
 * @author 张起源 
 * 时间：2008-5-07
 */
public class ClientCtrl extends AbstractCtrl {

	
	@Override
	public String getBillType() {
        return IBillType.eh_z0251505;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            ScBldVO.class.getName(),
	            ScBldBVO.class.getName()    
	        };
	    }

	@Override
	public String getChildPkField() {
		return "pk_bld_b";
	}

	@Override
	public String getPkField() {
		return "pk_bld";
	}

	@Override
	public int[] getCardButtonAry() {
		int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                new int[] { IBillButton.Busitype},
                ISHSHConst.CARD_BUTTONS_M, 0);
    	int[] btnss=nc.ui.eh.button.ButtonTool.insertButtons(btns,new int[]{IEHButton.BusinesBtn},0);
    	
    	return btnss;
	}

	@Override
	public int[] getListButtonAry() {
		return nc.ui.eh.button.ButtonTool.insertButtons(
	               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
	}

}
