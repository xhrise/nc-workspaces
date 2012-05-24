package nc.ui.eh.iso.z0503005;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.iso.z0503005.KzkjBVO;
import nc.vo.eh.iso.z0503005.KzkjVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 说明：扣重扣价公式
 * @author 王明
 * 时间：2008年10月9日15:02:48 
 */
public class ClientCtrl extends AbstractCtrl {

	
	@Override
	public String getBillType() {
        return IBillType.eh_h0503005;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            KzkjVO.class.getName(),
	            KzkjBVO.class.getName()    
	        };
	    }

	@Override
	public String getChildPkField() {
		return "pk_kzkj_b";
	}

	@Override
	public String getPkField() {
		return "pk_kzkj";
	}

	@Override
	public int[] getCardButtonAry() {
		//将执行按钮删除2009-12-08
		int[] btns = nc.ui.eh.button.ButtonTool.deleteButton(IBillButton.Action, ISHSHConst.CARD_BUTTONS_M);
        return btns;
	}

	@Override
	public int[] getListButtonAry() {
		 return ISHSHConst.LIST_BUTTONS_M;
	}
	 @Override
	public int getBusinessActionType() {
	        return nc.ui.trade.businessaction.IBusinessActionType.BD;
	    }
	 @Override
	public boolean isExistBillStatus() {
	        return false;
	    }

}
