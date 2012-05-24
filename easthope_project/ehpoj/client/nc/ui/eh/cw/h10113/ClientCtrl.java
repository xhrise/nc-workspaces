
package nc.ui.eh.cw.h10113;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.h10113.FycostVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 功能：燃料与制造费用维护
 * 作者：zqy
 * 时间：2008-9-10 14:16:51 
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
                   IBillButton.Delete,
				   IBillButton.Save,
				   IBillButton.Cancel,
				   IBillButton.Refresh
	              };
	}
	public boolean isShowCardRowNo() {
		return true;
	}
	public boolean isShowCardTotal() {
		return false;
	}
	public String getBillType() {
		return IBillType.eh_h10113;
	}
	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
                FycostVO.class.getName(),
                FycostVO.class.getName(),
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
		return " pk_fycost";
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
		return true;
	}

}
