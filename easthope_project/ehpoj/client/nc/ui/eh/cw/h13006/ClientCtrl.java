
package nc.ui.eh.cw.h13006;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treemanage.AbstractTreeManageController;
import nc.vo.eh.cw.h13006.FtstandardBVO;
import nc.vo.eh.cw.h13006.FtstandardVO;
import nc.vo.eh.pub.PubBillVO;

/*
 * 功能：成本费用分摊
 * 作者：zqy
 * 时间：2008-9-10 10:00:00
 */

public class ClientCtrl  extends AbstractTreeManageController {

	public boolean isAutoManageTree() {
		return true;
	}

	public boolean isTableTree() {
		return false;
	}

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Query,
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Delete,
				IBillButton.Line,
				IBillButton.Save,
				IBillButton.Return,
				IBillButton.Cancel,
				IBillButton.Refresh,
				IBillButton.Print
	        };
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return IBillType.eh_h13006;
	}

	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
                FtstandardVO.class.getName(),
                FtstandardBVO.class.getName()
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
		return "pk_ftstandard_b";
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return "pk_ftstandard";
	}

	public boolean isLoadCardFormula() {
		return true;
	}

	public int[] getListButtonAry() {
		return new int[]{
				IBillButton.Query,
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Delete,
				IBillButton.Line,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Refresh,
                IBillButton.Print
	        };
	}

	public boolean isShowListRowNo() {
		return true;
	}

	public boolean isShowListTotal() {
		return false;
	}

	public boolean isChildTree() {
		return false;
	}

    public String[] getListBodyHideCol() {
        return null;
    }

    public String[] getListHeadHideCol() {
        return null;
    }

	public Boolean isEditInGoing() throws Exception {
		return null;
	}

	public boolean isExistBillStatus() {
		return false;
	}

}
