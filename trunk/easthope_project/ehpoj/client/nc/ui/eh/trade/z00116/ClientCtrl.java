package nc.ui.eh.trade.z00116;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
/**
 * 供应商资料完善
 * @throws Exception
 * @author 王明
 * 2008年12月26日14:59:17
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
//				IBillButton.Add, 
//				IBillButton.Edit, 
//				IBillButton.Line,
//				IBillButton.Save, 
//				IBillButton.Cancel
				IBillButton.Query,
				IEHButton.ToCusbasdoc
		};
	}


	public boolean isShowCardRowNo() {
		return true;
	}


	public boolean isShowCardTotal() {
		return false;
	}


	public String getBillType() {
		return IBillType.eh_z00116;
	}


	public String[] getBillVoName() {
		return new String[] { 
				PubBillVO.class.getName(),
				nc.vo.bd.b09.CumandocVO.class.getName(),
				nc.vo.bd.b09.CumandocVO.class.getName() };
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
		return "pk_cubasdoc";
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
