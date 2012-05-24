package nc.ui.eh.kc.h0250220;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.kc.h0250220.StoreTypeVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 仓库类型
 * @throws Exception
 * @author 王兵
 * 2008年5月14日11:39:54
 */
public class ClientCtrl implements ICardController, ISingleController {

	public ClientCtrl() {
		super();
	}
	public String[] getCardBodyHideCol() {
		return null;
	}
	public int[] getCardButtonAry() {
		return new int[] { 
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
		return IBillType.eh_h0250220;
	}
	public String[] getBillVoName() {
		return new String[] { 
				PubBillVO.class.getName(),
				StoreTypeVO.class.getName(),
				StoreTypeVO.class.getName() 
				};
	}
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}
	public String getPkField() {
		return "pk_storetype";
	}
	public Boolean isEditInGoing() throws Exception {
		return null;
	}
	public String getBodyCondition() {
		return null;
	}
	public String getBodyZYXKey() {
		return null;
	}
	public String getChildPkField() {
		return null;
	}
	public String getHeadZYXKey() {
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
