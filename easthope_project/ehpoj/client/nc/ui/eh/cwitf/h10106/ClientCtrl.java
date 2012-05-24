
package nc.ui.eh.cwitf.h10106;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treemanage.AbstractTreeManageController;
import nc.vo.eh.cwitf.h10106.ItfBillmodelBVO;
import nc.vo.eh.cwitf.h10106.ItfBillmodelVO;
import nc.vo.eh.pub.PubBillVO;
/**
 * 功能：凭证模板定义
 * @author 张起源
 * 日期：2008-7-10 15:28:46
 */

public class ClientCtrl  extends AbstractTreeManageController {

	public boolean isAutoManageTree() {
		return true;
	}

	public boolean isTableTree() {
		return true;
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
				IEHButton.INSERTCDOE,
				IBillButton.Line,
				IBillButton.Save,
				IBillButton.Return,
				IBillButton.Cancel,
				IBillButton.Refresh,
				IBillButton.Print,
				IEHButton.CREATEVOUCHER
				
	        };
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return IBillType.eh_h10106;
	}

	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
                ItfBillmodelVO.class.getName(),
                ItfBillmodelBVO.class.getName()
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
		return "pk_billmodel_b";
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return "pk_billmodel";
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
				IEHButton.INSERTCDOE,
				IBillButton.Line,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Refresh,
				IEHButton.CREATEVOUCHER
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
