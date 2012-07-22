package nc.ui.yto.blacklist;

import nc.ui.hr.frame.button.AbstractBtnReg;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.yto.blacklist.HiPsndocBadAppBVO;
import nc.vo.yto.blacklist.HiPsndocBadAppHVO;
import nc.vo.yto.blacklist.MyBillVO;

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
				IBillButton.Edit, IBillButton.Line, IBillButton.Brow,
				IBillButton.ApproveInfo, IBillButton.Save, IBillButton.Cancel,
				IBillButton.Del, IBillButton.Delete, IBillButton.Return,
				IBillButton.Refresh, IBillButton.Commit , IBillButton.Audit,
				IBillButton.CancelAudit , IBillButton.File  };

	}

	public int[] getListButtonAry() {
		return new int[] { IBillButton.Query, IBillButton.Add,
				IBillButton.Edit, IBillButton.Line, IBillButton.Brow,
				IBillButton.ApproveInfo, IBillButton.Save, IBillButton.Cancel,
				IBillButton.Del, IBillButton.Delete, IBillButton.Card,
				IBillButton.Refresh, IBillButton.Commit, IBillButton.Audit,
				IBillButton.CancelAudit , IBillButton.File  
		};

	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "blk";
	}

	public String[] getBillVoName() {
		return new String[] { MyBillVO.class.getName(),
				HiPsndocBadAppHVO.class.getName(),
				HiPsndocBadAppBVO.class.getName() };
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
