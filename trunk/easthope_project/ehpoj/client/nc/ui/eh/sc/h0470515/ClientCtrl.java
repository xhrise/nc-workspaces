
package nc.ui.eh.sc.h0470515;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treemanage.AbstractTreeManageController;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0470515.ScSbbasdocBVO;
import nc.vo.eh.sc.h0470515.ScSbbasdocVO;

/**
 * 功能：设备档案
 * ZB27
 * @author 王兵
 * 2009-1-9 15:28:11
 */

public class ClientCtrl  extends AbstractTreeManageController {

	public boolean isAutoManageTree() {
		return true;
	}

	public boolean isChildTree() {
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
				IBillButton.Line,
				IBillButton.Delete,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Return,
				IBillButton.Refresh
	        };
	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return IBillType.eh_h0470515;
	}

	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
				ScSbbasdocVO.class.getName(),
				ScSbbasdocBVO.class.getName()
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
		return "pk_sb_b";
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return "pk_sb";
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

	
	public String[] getListBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getListButtonAry() {
		  return new int[]{ IBillButton.Query,IBillButton.Add, IBillButton.Edit, 
	                IBillButton.Card,IBillButton.Refresh};
	}

	public String[] getListHeadHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isShowListRowNo() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isShowListTotal() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
