
package nc.ui.eh.cw.h10115;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.h10115.LoadinvVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * ���ܣ�жԭ����
 * ���ߣ�zqy
 * ʱ�䣺2008-9-10 14:16:51 
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
		return IBillType.eh_h10115;
	}
	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
                LoadinvVO.class.getName(),
                LoadinvVO.class.getName(),
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
		return " pk_loadinv";
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
