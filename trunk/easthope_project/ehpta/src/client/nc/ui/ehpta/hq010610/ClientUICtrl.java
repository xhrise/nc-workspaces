package nc.ui.ehpta.hq010610;

import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.ehpta.hq010610.SaleMnymodifyVO;
import nc.vo.trade.pub.HYBillVO;

public class ClientUICtrl implements ISingleController, ICardController {

	public String getBillType() {
		return "HQ15";
	}

	public String[] getBillVoName() {
		return new String[]{
			HYBillVO.class.getName(),
			SaleMnymodifyVO.class.getName(),
			SaleMnymodifyVO.class.getName(),
		};
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}

	public String getChildPkField() {
		return null;
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return null;
	}

	public Boolean isEditInGoing() throws Exception {
		return null;
	}

	public boolean isExistBillStatus() {
		return true;
	}

	public boolean isLoadCardFormula() {
		return true;
	}

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[]{
			DefaultBillButton.Maintain , 
			IBillButton.Edit , 
			DefaultBillButton.Confirm,
			IBillButton.Cancel,
		};
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	// 是否单表体,=true单表体，=false单表头。
	public boolean isSingleDetail() {
		return true;
	}

}
