package nc.ui.eh.sc.h0450710;


import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.sc.h0450705.ScMrpBVO;
import nc.vo.eh.sc.h0450705.ScMrpBillVO;
import nc.vo.eh.sc.h0450705.ScMrpCVO;
import nc.vo.eh.sc.h0450705.ScMrpVO;

/**
 * MRP…Û≈˙
 * @author wb
 * 2009-4-28 15:17:34
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
        		ScMrpBillVO.class.getName(),
                ScMrpVO.class.getName(),
                ScMrpBVO.class.getName(),
                ScMrpCVO.class.getName()
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
        return "pk_mrp_b";
    }

    public String getHeadZYXKey() {
        return null;
    }

    public String getPkField() {
        return "pk_mrp";
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
        return IBillType.eh_h0450705;
    }
}