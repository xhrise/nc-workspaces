package nc.ui.eh.sc.h0450517;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0450517.ScBomapplybBVO;
import nc.vo.eh.sc.h0450517.ScBomapplybVO;



/**
 * 功能说明：BOM生报表（二）
 * @author 王明
 * 2008年12月30日9:10:07
 */
public class ClientCtrl extends AbstractCtrl {

	 @Override
	public int[] getCardButtonAry() {        
		 int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
	                new int[] { IBillButton.Busitype},
	                ISHSHConst.CARD_BUTTONS_M, 0);
	    	int[] btnss=nc.ui.eh.button.ButtonTool.insertButtons(btns,new int[]{IEHButton.BusinesBtn},0);
	    	
	    	return btns;//modify by houcq 2010-12-03 取消BOM申报（二）终止单据按钮
	    }

	 @Override
	public int[] getListButtonAry() {
	       return nc.ui.eh.button.ButtonTool.insertButtons(
	               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
	    }

    @Override
	public String getBillType() {
        return IBillType.eh_z0450517;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            ScBomapplybVO.class.getName(),
            ScBomapplybBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_applyb_b";
    }

    @Override
	public String getPkField() {
        return "pk_applyb";
    }

}
