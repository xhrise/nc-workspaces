package nc.ui.eh.stock.z06005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z06005.SbbillBVO;
import nc.vo.eh.stock.z06005.SbbillVO;

/**功能：原料司磅单
 * @author 牛冶
 * 2008-03-24 下午04:03:18
 */

public class ClientCtrl extends AbstractCtrl {

	 @Override
	public int[] getCardButtonAry() {        
		 	int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
	                new int[] { IBillButton.Busitype},
	                ISHSHConst.CARD_BUTTONS_M, 0);
	        int[] btnss = nc.ui.eh.button.ButtonTool.insertButtons(
	        		btns,
	        		new int[]{IEHButton.FIRSTREADDATE}, 0); //add by houcq modify 取消业务操作下面的关闭按钮          
	        return btnss;
	    }

	 @Override
	public int[] getListButtonAry() {
         int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                    new int[] {IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M,0);
         
	       return btns;
	 }


    @Override
	public String getBillType() {
        return IBillType.eh_z06005;
    }


    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            SbbillVO.class.getName(),
            SbbillBVO.class.getName()
            
        };
    }
    @Override
	public String getChildPkField() {
        return "pk_sbbill_b";
    }

    @Override
	public String getPkField() {
        return "pk_sbbill";
    }
    
    @Override
    public boolean isExistBillStatus() {
        return false;
    }
}
