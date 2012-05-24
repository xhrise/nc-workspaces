
package nc.ui.eh.sc.h0470510;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0470510.ScSbtypeVO;

/** 
 * 功能：设备型号维护
 * ZA27
 * 2009-1-9 14:19:58
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
//                   IBillButton.Delete,
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
		return IBillType.eh_h0470510;
	}

	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
				ScSbtypeVO.class.getName(),
				ScSbtypeVO.class.getName(),
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
		return "pk_sbtype";
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
