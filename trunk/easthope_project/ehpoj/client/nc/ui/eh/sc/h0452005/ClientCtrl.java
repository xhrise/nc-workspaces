package nc.ui.eh.sc.h0452005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0452005.ScCprkdBVO;
import nc.vo.eh.sc.h0452005.ScCprkdVO;



/**
 * 功能说明：成品入库单
 * @author 王明
 * 2008-05-08 下午02:03:18
 */
public class ClientCtrl extends AbstractCtrl {

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
	               new int[] {IBillButton.Busitype  },ISHSHConst.LIST_BUTTONS_M, 0);
	
	    }

    @Override
	public String getBillType() {
        return IBillType.eh_h0452005;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            ScCprkdVO.class.getName(),
            ScCprkdBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_rkd_b";
    }

    @Override
	public String getPkField() {
        return "pk_rkd";
    }


}
