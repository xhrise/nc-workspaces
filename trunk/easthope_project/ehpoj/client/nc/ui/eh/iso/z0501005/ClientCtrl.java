package nc.ui.eh.iso.z0501005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.iso.z0501005.IsoBVO;
import nc.vo.eh.iso.z0501005.IsoVO;
import nc.vo.eh.pub.PubBillVO;

/** 
 * 说明：原料质量标准单
 * @author 张起源 
 * 时间：2008-4-11
 */
public class ClientCtrl extends AbstractCtrl {

	@Override
	public String getBillType() {
        return IBillType.eh_z0501005;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            IsoVO.class.getName(),
	            IsoBVO.class.getName()    
	        };
	    }
	
	@Override
	public String getChildPkField() {
		return "pk_iso_b";
	}

	@Override
	public String getPkField() {
		return "pk_iso";
	}

	@Override
	public int[] getCardButtonAry() {
        int[] btns = nc.ui.eh.button.ButtonTool.insertButtons(new int[] {IBillButton.Busitype},ISHSHConst.CARD_BUTTONS_M,0);
        int[] btns1 = nc.ui.eh.button.ButtonTool.insertButtons(new int[] { IEHButton.EditionChange,IBillButton.Copy},btns, 15);//modify by houcq 2010取消原料标准单的关闭按钮
        return btns1;
        
	}
	
	 @Override
	public int[] getListButtonAry() {
		 return nc.ui.eh.button.ButtonTool.insertButtons(
	               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
	    }	    
}
