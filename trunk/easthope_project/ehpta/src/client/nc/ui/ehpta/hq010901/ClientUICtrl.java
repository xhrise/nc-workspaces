package nc.ui.ehpta.hq010901;

import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.ehpta.hq010901.CalcInterestBVO;
import nc.vo.ehpta.hq010901.CalcInterestVO;
import nc.vo.trade.pub.HYBillVO;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
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

		return new int[] { IBillButton.Query , IBillButton.Add,
				 IBillButton.Edit, IBillButton.Refresh,
				IBillButton.Save, IBillButton.Cancel, IBillButton.Delete
				 ,DefaultBillButton.Maintain ,
				IBillButton.Commit, IBillButton.Audit, IBillButton.CancelAudit,
				IBillButton.ApproveInfo,  IBillButton.Brow, IBillButton.Return };

	}

	public int[] getListButtonAry() {
		return new int[] { IBillButton.Query , IBillButton.Add,
				IBillButton.Edit, IBillButton.Refresh, IBillButton.Delete,
				IBillButton.Card

		};

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return "HQ09";
	}

	public String[] getBillVoName() {
		return new String[]{
			HYBillVO.class.getName(),
			CalcInterestVO.class.getName(),
			CalcInterestBVO.class.getName()
		};
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
