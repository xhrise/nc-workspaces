package nc.ui.eh.sc.h0450515;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0450515.BomBVO;
import nc.vo.eh.sc.h0450515.BomVO;

/** 
 * 说明：BOM档案
 * @author 张起源 
 * 时间：2008-5-07
 */
public class ClientCtrl extends AbstractManageController {

	
	public String getBillType() {
        return IBillType.eh_z0450515;
    }

	 public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            BomVO.class.getName(),
	            BomBVO.class.getName()    
	        };
	    }
	
	public String getChildPkField() {
		return "pk_bom_b";
	}

	public String getPkField() {
		return "pk_bom";
	}

	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Query,
				IBillButton.Add,
				/**删除 复制，修改，版本变更，终止单据按钮
				 * ，将 上一页，下一页移到业务操作下，将 表体复制按钮四个字改成 版本变更
				 * edit by wb 2009-11-12 10:01:15
				 * **/
				IBillButton.Edit,				
				IBillButton.Delete,
				IBillButton.Line,
				IBillButton.Save,
				IBillButton.Return,
				IBillButton.Cancel,
				IBillButton.Refresh,
				IEHButton.EditionChange,
//                IEHButton.Prev,
//                IEHButton.Next,
//                IBillButton.Copy,
//                IEHButton.CONFIRMBUG,
                IEHButton.prevedition,//上一版本
                IEHButton.nextedition,//下一版本
                IEHButton.ConfirmSC,	//确定生产
                IBillButton.Print,
                IEHButton.BusinesBtn
	        };
	}
	
	 public int[] getListButtonAry() {
		 return new int[]{
					IBillButton.Query,
					IBillButton.Add,
					IBillButton.Card,
					IEHButton.Prev,
	                IEHButton.Next,
					IBillButton.Refresh
		        };
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
		return IBusinessActionType.BD;
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
}
