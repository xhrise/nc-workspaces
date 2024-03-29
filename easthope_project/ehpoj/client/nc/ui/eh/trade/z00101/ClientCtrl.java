
package nc.ui.eh.trade.z00101;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treecard.ITreeCardController;
import nc.vo.eh.pub.PubBillVO;


/**
 * 功能：片区管理
 * @author 张起源
 * 日期：2008-3-25
 */

public class ClientCtrl  implements ITreeCardController ,ISingleController {

	public boolean isAutoManageTree() {
		return false;
	}

	public boolean isChildTree() {
		return false;
	}

	public boolean isTableTree() {
		return false;
	}

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Query,
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Delete,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Refresh
	        };
	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return IBillType.eh_z00101;
	}

	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
				nc.vo.eh.trade.z00101.AreaclVO.class.getName(),
				nc.vo.eh.trade.z00101.AreaclVO.class.getName()
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
		return "pk_areacl";
	}

	public Boolean isEditInGoing() throws Exception {
		return null;
	}

	public boolean isExistBillStatus() {
		return false;
	}

	public boolean isLoadCardFormula() {
		return false;
	}

	public boolean isSingleDetail() {
		return false;
	}
	

}
