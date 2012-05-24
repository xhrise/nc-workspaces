package nc.ui.eh.stock.z00160;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z00160.OuttypeVO;

/**
 * 名称：入库类型
 * @author 张起源
 * 2008-7-24 9:18:00
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
		return IBillType.eh_Z00160;
	}
	public String[] getBillVoName() {
		return new String[] { 
				PubBillVO.class.getName(),
				OuttypeVO.class.getName(),
                OuttypeVO.class.getName() 
				};
	}
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}
	public String getPkField() {
		return "pk_outtype";
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
