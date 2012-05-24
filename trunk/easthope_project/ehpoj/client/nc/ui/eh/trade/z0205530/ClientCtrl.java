package nc.ui.eh.trade.z0205530;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0205525.DiscountAdjustBVO;
import nc.vo.eh.trade.z0205525.DiscountAdjustVO;

/** 
 * 说明：折扣调整单 
 * @author 张起源 
 * 时间：2008-4-12
 */
public class ClientCtrl extends AbstractManageController {

	
	public String getBillType() {
        return IBillType.eh_z0205525;
    }

	 public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            DiscountAdjustVO.class.getName(),
	            DiscountAdjustBVO.class.getName()    
	        };
	    }

	public String getChildPkField() {
		return "pk_discountadjust_b";
	}

	public String getPkField() {
		return "pk_discountadjust";
	}

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
		return IBusinessActionType.PLATFORM;
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
		return true;
	}

	public String[] getListBodyHideCol() {
		return null;
	}

	public String[] getListHeadHideCol() {
		return null;
	}

	public boolean isShowListRowNo() {
		return true;
	}

	public boolean isShowListTotal() {
		return true;
	}

	  public int[] getListButtonAry() {
	        return nc.ui.eh.pub.PubTools.getSPLButton();
	    }

		public int[] getCardButtonAry() {
			return nc.ui.eh.pub.PubTools.getSPCButton();
		}
		

}
