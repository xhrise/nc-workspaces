package nc.ui.eh.stock.h0150225;


import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.stock.h0150220.StockDecisionBVO;
import nc.vo.eh.stock.h0150220.StockDecisionBillVO;
import nc.vo.eh.stock.h0150220.StockDecisionCVO;
import nc.vo.eh.stock.h0150220.StockDecisionDVO;
import nc.vo.eh.stock.h0150220.StockDecisionEVO;
import nc.vo.eh.stock.h0150220.StockDecisionVO;

/**
 * 采购决策(审批)
 * ZB19	
 * @author wangbing
 * 2008-12-29 8:57:25
 */
public class ClientCtrl extends AbstractManageController {
 
    public ClientCtrl() {
        super();
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

    public String[] getListBodyHideCol() {
        return null;
    }
    public int[] getListButtonAry() {
        return nc.ui.eh.pub.PubTools.getSPLButton();
    }

	public int[] getCardButtonAry() {
		return nc.ui.eh.pub.PubTools.getSPCButton();
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
    public String[] getBillVoName() {
        return new String[]{
        		StockDecisionBillVO.class.getName(),
                StockDecisionVO.class.getName(),
                StockDecisionBVO.class.getName(),
                StockDecisionCVO.class.getName(),
                StockDecisionDVO.class.getName(),
                StockDecisionEVO.class.getName()
        };
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

    public String getChildPkField() {
        return null;
    }

    public String getHeadZYXKey() {
        return null;
    }

    public String getPkField() {
        return "pk_decision";
    }
    public Boolean isEditInGoing() throws Exception {
        return null;
    }

    public boolean isExistBillStatus() {
        return true;
    }

    public boolean isLoadCardFormula() {
        return true;
    }

    public String getBillType() {
        return IBillType.eh_h0150220;
    }
}