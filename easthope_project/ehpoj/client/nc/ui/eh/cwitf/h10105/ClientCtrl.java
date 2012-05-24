
package nc.ui.eh.cwitf.h10105;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cwitf.h10105.ItfBilltypeVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 凭证单据类型
 * @author 王兵
 * 2008年7月9日19:42:43
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
		return IBillType.eh_h10105;
	}
	
	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
				ItfBilltypeVO.class.getName(),
				ItfBilltypeVO.class.getName(),
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
		return "pk_billtype";
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
