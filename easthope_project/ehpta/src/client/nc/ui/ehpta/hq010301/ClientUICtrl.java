package nc.ui.ehpta.hq010301;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.bill.ISingleController;

import nc.vo.ehpta.hq010301.MyBillVO;
import nc.vo.ehpta.hq010301.EhptaHangingpriceVO;
import nc.vo.ehpta.hq010301.EhptaHangingpriceVO;
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

public class ClientUICtrl extends AbstractManageController implements ISingleController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { 
				IBillButton.Query, 
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Save, 
				IBillButton.Cancel,
				IBillButton.Delete, 
				IBillButton.Commit,
				IBillButton.Return, 
				IBillButton.Refresh,
				IBillButton.SelAll, 
				IBillButton.SelNone ,
				IBillButton.Audit,
				IBillButton.CancelAudit,
				};

	}

	public int[] getListButtonAry() {
		return new int[] { 
				IBillButton.Query, 
				IBillButton.Add,
				IBillButton.Edit, 
				IBillButton.Save, 
				IBillButton.Cancel,
				IBillButton.Delete, 
				IBillButton.Commit,
				IBillButton.Card, 
				IBillButton.Refresh,
				IBillButton.SelAll, 
				IBillButton.SelNone,
				IBillButton.Audit,
				IBillButton.CancelAudit,

		};

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return "HQ03";
	}

	public String[] getBillVoName() {
		return new String[] { 
				MyBillVO.class.getName(),
				EhptaHangingpriceVO.class.getName(),
				EhptaHangingpriceVO.class.getName() };
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
		return true;
	}

	public boolean isShowListTotal() {
		return true;
	}

	/**
	 * 是否单表
	 * @return boolean true:单表体，false:单表头
	 */ 
	public boolean isSingleDetail() {
		return false; //单表头
	}
	
	

}
