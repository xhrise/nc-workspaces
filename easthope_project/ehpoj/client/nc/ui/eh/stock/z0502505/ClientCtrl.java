package nc.ui.eh.stock.z0502505;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0502505.ProcheckapplyBVO;
import nc.vo.eh.stock.z0502505.ProcheckapplyVO;



/**
 * 功能说明：成品检测申请
 * @author 王明
 * 2008-03-24 下午04:03:18
 */
public class ClientCtrl extends AbstractCtrl {

	 @Override
	public int[] getCardButtonAry() {        
	        int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
	                new int[] { IBillButton.Busitype},
	                ISHSHConst.CARD_BUTTONS_M, 0);
	        int[] btns1=nc.ui.eh.button.ButtonTool.insertButtons(new int[] {IEHButton.BusinesBtn}, btns, 14);
	        
	        return btns;//modify houcq 2010-11-29取消成品检测单的终止单据按钮
	    }

	 @Override
	public int[] getListButtonAry() {
	       return nc.ui.eh.button.ButtonTool.insertButtons(
	               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
	    }

    @Override
	public String getBillType() {
        return IBillType.eh_z0502505;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            ProcheckapplyVO.class.getName(),
            ProcheckapplyBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_procheckapply_b";
    }

    @Override
	public String getPkField() {
        return "pk_procheckapply";
    }

}
