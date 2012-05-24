package nc.ui.eh.sc.h0450516;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0450516.ScBomapplyaBVO;
import nc.vo.eh.sc.h0450516.ScBomapplyaVO;



/**
 * 功能说明：BOM生报表（一）
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
	    	
	    	return btns;//modify by houcq 2010-12-03 取消BOM申报（一）终止单据按钮
	    }

	 @Override
	public int[] getListButtonAry() {
	       return nc.ui.eh.button.ButtonTool.insertButtons(
	               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
	    }

    @Override
	public String getBillType() {
        return IBillType.eh_z0450516;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            ScBomapplyaVO.class.getName(),
            ScBomapplyaBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_applya_b";
    }

    @Override
	public String getPkField() {
        return "pk_applya";
    }

}
