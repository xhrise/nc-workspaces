package nc.ui.eh.body;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;

/**
 * @author Jorli
 * @date 2011-12-18 ÏÂÎç8:22:18
 * @type nc.zip.pub.body.AbstractController
 */
public abstract class AbstractController implements ICardController, ISingleController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return nc.ui.trade.businessaction.IBusinessActionType.BD;
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
		return true;
	}

}
