
package nc.ui.eh.cw.h1300610;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.h1300610.ArapSumgzVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 功能：工资总额计算
 * ZB07
 * 作者:WB
 * 时间：2008-11-3 10:25:38
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
    				   IBillButton.Query,
    				   IEHButton.CALCKCYBB,
    	               IBillButton.Edit,
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
    		return IBillType.eh_h1300610;
    	}
        
    	public String[] getBillVoName() {
    		return new String[]{
    				PubBillVO.class.getName(),
    				ArapSumgzVO.class.getName(),
    				ArapSumgzVO.class.getName(),
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
    		return "pk_sumgz";
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
    
    	/**
         * 列表是否显示合计行 创建日期：(2003-1-5 15:29:05)
         * 
         * @return boolean
         */
        public boolean isShowListTotal() {
            return true;
        }
    }
