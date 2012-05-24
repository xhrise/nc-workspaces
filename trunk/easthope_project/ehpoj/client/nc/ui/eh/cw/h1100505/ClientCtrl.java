package nc.ui.eh.cw.h1100505;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.h1100505.ArapFkqsBVO;
import nc.vo.eh.cw.h1100505.ArapFkqsVO;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;


/**
 * 功能说明：付款申请单
 * @author zqy
 * 2008-05-28 下午04:03:18
 */
public class ClientCtrl extends AbstractCtrl {

	 @Override
	public int[] getCardButtonAry() {        
	        int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(new int[] { IBillButton.Busitype},ISHSHConst.CARD_BUTTONS_M, 0);	        
	        int[] btnss = nc.ui.eh.button.ButtonTool.insertButtons(
	        		btns,
	        		new int[]{IEHButton.BusinesBtn}, 0);
	        
	    	return btnss;
	    }

	 @Override
	public int[] getListButtonAry() {
	        return nc.ui.eh.button.ButtonTool.insertButtons(
	        new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
	    }

     @Override
	public String getBillType() {
            return IBillType.eh_h1100505;
     }

     @Override
	public String[] getBillVoName() {
            return new String[]{
            PubBillVO.class.getName(),
            ArapFkqsVO.class.getName(),
            ArapFkqsBVO.class.getName()  
        };
     }

     @Override
	public String getChildPkField() {
            return "pk_fkqs_b";
     }

     @Override
	public String getPkField() {
            return "pk_fkqs";
     }

}
