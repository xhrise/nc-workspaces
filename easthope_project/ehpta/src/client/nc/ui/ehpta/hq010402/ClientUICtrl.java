package nc.ui.ehpta.hq010402;

import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.bill.ISingleController;

import nc.vo.ehpta.hq010402.MultiBillVO;
import nc.vo.ehpta.hq010401.SaleContractVO;
import nc.vo.ehpta.hq010402.SaleContractBVO;
import nc.vo.ehpta.hq010402.AidcustVO;
import nc.vo.ehpta.hq010402.PrepolicyVO;
import nc.ui.trade.button.IBillButton;

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

		return new int[] { IBillButton.Query,IBillButton.Busitype , IBillButton.Add,
				IBillButton.Line, IBillButton.Edit, IBillButton.Refresh,
				IBillButton.Save, IBillButton.Cancel, IBillButton.Delete,
				IBillButton.Commit, IBillButton.Audit, IBillButton.CancelAudit,
				IBillButton.ApproveInfo, DefaultBillButton.MAKENEWCONTRACT , IBillButton.Brow,
				DefaultBillButton.DOCUMENT, IBillButton.Return };

	}

	public int[] getListButtonAry() {
		return new int[] { IBillButton.Query,IBillButton.Busitype , IBillButton.Add,
				IBillButton.Edit, IBillButton.Refresh, IBillButton.Delete,
				DefaultBillButton.DOCUMENT, IBillButton.Card

		};

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return "HQ06";
	}

	public String[] getBillVoName() {
		return new String[] { 
				MultiBillVO.class.getName(),
				SaleContractVO.class.getName(),
				SaleContractBVO.class.getName(),
				AidcustVO.class.getName(),
				PrepolicyVO.class.getName() };
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
		return true;
	}

	public String[] getListBodyHideCol() {
		return null;
	}

	public String[] getListHeadHideCol() {
		return null;
	}

	public boolean isShowListRowNo() {
		return true;
	}

	public boolean isShowListTotal() {
		return true;
	}

}
