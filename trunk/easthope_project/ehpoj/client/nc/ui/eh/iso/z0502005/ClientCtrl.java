package nc.ui.eh.iso.z0502005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.iso.z0502005.StockCheckBillVO;
import nc.vo.eh.iso.z0502005.StockCheckreportBVO;
import nc.vo.eh.iso.z0502005.StockCheckreportCVO;
import nc.vo.eh.iso.z0502005.StockCheckreportVO;

/** 
 * 说明：检测报告 
 * @author 张起源 
 * 时间：2008-4-14
 */
public class ClientCtrl extends AbstractCtrl {

	
	@Override
	public String getBillType() {
        return IBillType.eh_z0502005;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
//                PubBillVO.class.getName(),
                StockCheckBillVO.class.getName(),
	            StockCheckreportVO.class.getName(),
	            StockCheckreportBVO.class.getName(),
                StockCheckreportCVO.class.getName()
                
	        };
	    }

	@Override
	public String getChildPkField() {
		return null;
	}

	@Override
	public String getPkField() {
		return "pk_checkreport";
	}

	@Override
	public int[] getCardButtonAry() {        
        int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                new int[] { IBillButton.Busitype},
                ISHSHConst.CARD_BUTTONS_M, 0);
        int[] btns1=nc.ui.eh.button.ButtonTool.insertButtons(new int[] { IEHButton.SpecialCG},btns,14);//modify houcq 2010-11-29取消检测报告单的终止单据按钮
        return btns1;
    }

	@Override
	public int[] getListButtonAry() {
	       return nc.ui.eh.button.ButtonTool.insertButtons(
	               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
	    }
		
}
