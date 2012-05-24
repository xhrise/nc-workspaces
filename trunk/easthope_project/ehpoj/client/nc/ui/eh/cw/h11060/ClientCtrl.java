package nc.ui.eh.cw.h11060;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.cw.h11060.CwHxVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 说明: 成品核销
 * @author 王明
 * 2008年8月26日14:38:55
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
				IEHButton.Prev,
				IEHButton.Next
		};
	}
	public boolean isShowCardRowNo() {
		return true;
	}
	public boolean isShowCardTotal() {
		return false;
	}
	public String getBillType() {
		return IBillType.eh_h11060;
	}
	public String[] getBillVoName() {
		return new String[] { 
				PubBillVO.class.getName(),
				CwHxVO.class.getName(),
				CwHxVO.class.getName() 
				};
	}
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}
	public String getPkField() {
		return "pk_hx";
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
