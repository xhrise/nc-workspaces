package nc.ui.eh.trade.z00125;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z00125.BrandVO;

/**
 * 功能:品牌维护 
 * @author 王明
 * 2008-03-24 下午04:03:18
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

	/*
	 * (non-Javadoc)
	 * 取到单句类型
	 * @see nc.ui.trade.controller.IControllerBase#getBillType()
	 */
	public String getBillType() {
		return IBillType.eh_z00125;
	}
	public String[] getBillVoName() {
		return new String[] { 
				PubBillVO.class.getName(),
				BrandVO.class.getName(),
				BrandVO.class.getName() };
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

	/*
	 * (non-Javadoc)
	 * 取到主表的主键
	 * @see nc.ui.trade.controller.IControllerBase#getPkField()
	 */
	public String getPkField() {
		return "pk_brand";
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
