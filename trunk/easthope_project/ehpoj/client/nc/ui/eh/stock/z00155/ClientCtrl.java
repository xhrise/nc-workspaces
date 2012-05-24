package nc.ui.eh.stock.z00155;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treecard.ITreeCardController;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z00155.WorkshopVO;

/**
 * 车间维护 
 * @throws Exception
 * @author 王明
 * 2008-05-1 下午04:03:18
 */
public class ClientCtrl implements ITreeCardController ,ISingleController {

	public int[] getCardButtonAry() {
		return new int[] { 
				IBillButton.Query,
				IBillButton.Add, 
				IBillButton.Edit, 
				IBillButton.Save,
                IBillButton.Delete,
				IBillButton.Cancel,
                IBillButton.Refresh
		};
	}
	public String getBillType() {
		return IBillType.eh_z00155;
	}
	public String[] getBillVoName() {
		return new String[] { 
				PubBillVO.class.getName(),
				WorkshopVO.class.getName(),
				WorkshopVO.class.getName() 
				};
	}
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}
	public String getPkField() {
		return "pk_contracttype";
	}
	public boolean isAutoManageTree() {
		return false;
	}

	public boolean isChildTree() {
		return false;
	}

	public boolean isTableTree() {
		return false;
	}

	public String[] getCardBodyHideCol() {
		return null;
	}
	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
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
		return false;
	}

}
