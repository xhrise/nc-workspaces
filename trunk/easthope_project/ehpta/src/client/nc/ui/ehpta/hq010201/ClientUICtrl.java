package nc.ui.ehpta.hq010201;

import nc.ui.ehpta.pub.btn.DisabledBtn;
import nc.ui.ehpta.pub.btn.EnabledBtn;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.ehpta.hq010201.StorContractVO;
import nc.vo.ehpta.hq010201.StorcontractBVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * Create on 2006-4-6 16:00:51
 * 
 * @author authorName
 * @version tempProject version
 */

public class ClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { IBillButton.Query, IBillButton.Add,
				IBillButton.Edit, IBillButton.Line, IBillButton.Save,
				IBillButton.Cancel, IBillButton.Delete, IBillButton.Refresh,
				IBillButton.Commit, IBillButton.Audit, IBillButton.CancelAudit,
				IBillButton.ApproveInfo , EnabledBtn.NO , DisabledBtn.NO , IBillButton.Return };

	}

	public int[] getListButtonAry() {
		return new int[] { IBillButton.Query, IBillButton.Add,
				IBillButton.Edit, IBillButton.Delete, IBillButton.Refresh,
				IBillButton.Commit, IBillButton.Audit, IBillButton.CancelAudit,
				IBillButton.ApproveInfo , EnabledBtn.NO , DisabledBtn.NO , IBillButton.Card

		};

	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "HQ01";
	}

	public String[] getBillVoName() {
		return new String[] { HYBillVO.class.getName(),
				StorContractVO.class.getName(), StorcontractBVO.class.getName() };
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return IBusinessActionType.PLATFORM;
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
		return false;
	}

	public String[] getListBodyHideCol() {
		return null;
	}

	public String[] getListHeadHideCol() {
		return null;
	}

	public boolean isShowListRowNo() {
		return false;
	}

	public boolean isShowListTotal() {
		return false;
	}

}
