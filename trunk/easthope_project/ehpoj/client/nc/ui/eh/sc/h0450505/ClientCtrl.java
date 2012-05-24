
package nc.ui.eh.sc.h0450505;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0450505.BdDeviceVO;

/** 
 * 说明：机组档案
 * @author 张起源 
 * 时间：2008-5-07
 */

public class ClientCtrl implements ICardController, ISingleController {

	public ClientCtrl() {
		super();
	}

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[]{
				   IBillButton.Add,
	               IBillButton.Edit,
	               IBillButton.Line,
				   IBillButton.Save,
				   IBillButton.Cancel
	              };
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return IBillType.eh_z0450505;
	}

	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
				BdDeviceVO.class.getName(),
				BdDeviceVO.class.getName(),
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
		return "pk_device";
	}

	public Boolean isEditInGoing() throws Exception {
		return null;
	}

	public boolean isExistBillStatus() {
		return false;
	}

	public boolean isLoadCardFormula() {
		return true;
	}

	public boolean isSingleDetail() {
		return true;
	}

}
