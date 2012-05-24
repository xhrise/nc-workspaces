package nc.ui.eh.kc.h0251005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.kc.h0251005.ScCkdBVO;
import nc.vo.eh.kc.h0251005.ScCkdVO;
import nc.vo.eh.pub.PubBillVO;

/** 
 * 说明：材料出库单 
 * @author 张起源 
 * 时间：2008-5-08
 */
public class ClientCtrl extends AbstractCtrl {

	@Override
	public int[] getCardButtonAry() {        
//        return new int[] {
//                IBillButton.Busitype,
//                IBillButton.Add, 
//                IBillButton.Edit, 
//                IBillButton.Delete,
//                IBillButton.Line, 
//                IBillButton.Save, 
//                IBillButton.Action,
//                IBillButton.Cancel,
//                IBillButton.Return,
//                IBillButton.Refresh,
//                IBillButton.Print
//        };
		int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                new int[] { IBillButton.Busitype},
                ISHSHConst.CARD_BUTTONS_M, 0);
    	int[] btnss=nc.ui.eh.button.ButtonTool.insertButtons(btns,new int[]{IEHButton.BusinesBtn},0);
    	
    	return btns;//modify by houcq 2010-11-29 取消材料出库单据的终止单据按钮
    }

    @Override
	public int[] getListButtonAry() {
       return nc.ui.eh.button.ButtonTool.insertButtons(
               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
    }
	
	@Override
	public String getBillType() {
        return IBillType.eh_h0251005;
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
