package nc.ui.eh.stock.h0150320;


import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.stock.h0150315.StockWjDecisionBillVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionBVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionCVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionDVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionVO;

/**
 * 五金采购决策(审批)
 * ZB23
 * @author wangbing
 * 2009-1-7 17:38:41
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
        		StockWjDecisionBillVO.class.getName(),
                StockWjdecisionVO.class.getName(),
                StockWjdecisionBVO.class.getName(),
                StockWjdecisionCVO.class.getName(),
                StockWjdecisionDVO.class.getName()
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
        return "pk_wjdecision";
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
        return IBillType.eh_h0150315;
    }
}