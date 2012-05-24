package nc.ui.eh.cw.h10108;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.h10108.ArapVouchertypeVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 凭证类型
 * @throws Exception
 * @author 张起源
 * 2008年8月18日10:41:58
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
				IBillButton.Cancel,
				IBillButton.Refresh
		};
	}
    
	public boolean isShowCardRowNo() {
		return true;
	}
    
	public boolean isShowCardTotal() {
		return false;
	}
    
	public String getBillType() {
		return IBillType.eh_h10108;
	}
    
	public String[] getBillVoName() {
		return new String[] { 
				PubBillVO.class.getName(),
                ArapVouchertypeVO.class.getName(),
                ArapVouchertypeVO.class.getName() 
				};
	}
    
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}
    
	public String getPkField() {
		return "pk_vouchertype";
	}
    
	public Boolean isEditInGoing() throws Exception {
		return null;
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
