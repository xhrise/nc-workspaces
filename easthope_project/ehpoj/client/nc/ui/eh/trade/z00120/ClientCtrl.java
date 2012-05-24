
package nc.ui.eh.trade.z00120;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treemanage.AbstractTreeManageController;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z00120.InvbasdocBVO;
import nc.vo.eh.trade.z00120.InvbasdocVO;
/**
 * 功能：物料档案
 * @author 张起源
 * 日期：2008-3-25
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
				IBillButton.Print,
                IEHButton.Prev,
                IEHButton.Next,
                IEHButton.LOCKBILL //关闭按钮
	        };
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return IBillType.eh_z00120;
	}

	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
				InvbasdocVO.class.getName(),
				InvbasdocBVO.class.getName()
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
		return "pk_invbasdoc_b";
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return "pk_invbasdoc";
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
				IBillButton.Refresh
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
