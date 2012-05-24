
package nc.ui.eh.iso.z0500201;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.iso.z0500201.BdCheckitemVO;
import nc.vo.eh.pub.PubBillVO;

/** 
 * 说明：检测项目单
 * @author 张起源 
 * 时间：2008-4-11
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
		return IBillType.eh_z0500201;
	}

	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
				BdCheckitemVO.class.getName(),
				BdCheckitemVO.class.getName(),
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
		return "pk_checkitem";
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
