
package nc.ui.eh.trade.h0208001;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.h0208001.TradeCheckdateVO;

/**
 * 功能：盈亏考核计算日期设置
 * ZA97
 * 作者:WB
 * 时间：2008-10-14 13:16:08
 */
    public class ClientCtrl implements ICardController, ISingleController {
    
    	public ClientCtrl() {
    		super();
    	}
        
    	public String[] getCardBodyHideCol() {
    		return null;
    	}
        
    	public int[] getCardButtonAry() {
    		return new int[]{
    				   IBillButton.Add,
    	               IBillButton.Edit,
    	               IBillButton.Line,
                       IBillButton.Delete,
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
    		return IBillType.eh_h0208001;
    	}
        
    	public String[] getBillVoName() {
    		return new String[]{
    				PubBillVO.class.getName(),
                    TradeCheckdateVO.class.getName(),
                    TradeCheckdateVO.class.getName(),
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
    		return null;
    	}
        
    	public String getHeadZYXKey() {
    		return null;
    	}
        
    	public String getPkField() {
    		return "pk_checkdate";
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
