
package nc.ui.eh.stock.h0150205;

import nc.ui.eh.button.ButtonTool;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.h0150205.StockRwcarriageVO;

/**
 * 
功能：运费明细
作者：wb
日期：2008-12-12 上午09:55:54
 */

public class ClientCtrl extends AbstractCtrl implements ISingleController{

	public ClientCtrl() {
		super();
	}

	@Override
	public String[] getCardBodyHideCol() {
		return null;
	}

	@Override
	public int[] getCardButtonAry() {
		//return ISHSHConst.CARD_BUTTONS;
		int[] btns = ISHSHConst.CARD_BUTTONS;
		int[] btns2 = ButtonTool.deleteButton(IBillButton.Line, btns);		
		return btns2;
	}
	
	@Override
	public int[] getListButtonAry() {
		return ISHSHConst.LIST_BUTTONS;
	}
	
	@Override
	public boolean isShowCardRowNo() {
		return true;
	}

	@Override
	public boolean isShowCardTotal() {
		return false;
	}

	@Override
	public String getBillType() {
		return IBillType.eh_h0150205;
	}

	@Override
	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
                StockRwcarriageVO.class.getName(),
                StockRwcarriageVO.class.getName(),
		};
	}

	@Override
	public String getBodyCondition() {
		return null;
	}

	@Override
	public String getBodyZYXKey() {
		return null;
	}

	@Override
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}

	@Override
	public String getChildPkField() {
		return null;
	}

	@Override
	public String getHeadZYXKey() {
		return null;
	}

	@Override
	public String getPkField() {
		return "pk_rwcarriage";
	}

	@Override
	public Boolean isEditInGoing() throws Exception {
		return null;
	}

	@Override
	public boolean isExistBillStatus() {
		return false;
	}

	@Override
	public boolean isLoadCardFormula() {
		return true;
	}

	public boolean isSingleDetail() {
		return false;
	}

	
	

}
