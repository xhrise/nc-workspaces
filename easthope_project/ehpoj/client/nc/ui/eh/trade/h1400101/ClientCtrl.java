package nc.ui.eh.trade.h1400101;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.h1400101.CustoverageHVO;
import nc.vo.eh.trade.h1400101.CustoverageVO;


/**
 * 说明：客商余额结算
 * 类型：ZB41
 * 作者：张志远
 * 时间：2010年01月26日
 */
public class ClientCtrl extends AbstractManageController {

    /**
     * 
     */
    public ClientCtrl() {
        super();
    }

    public String[] getCardBodyHideCol() {
        return null;
    }

    public int[] getCardButtonAry() {
        return new int[]{
                IBillButton.Query,
//                IBillButton.Add,
//                IBillButton.Line,
//                IBillButton.Save,
                IEHButton.CALCKCYBB,
                IBillButton.Return,
                IBillButton.Refresh,
                IBillButton.Print
               
        };
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
        return new int[]{
                IBillButton.Query,
                IEHButton.CALCKCYBB,
                IBillButton.Card,
                IBillButton.Refresh
        };
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

    public String getBillType() {
        return IBillType.eh_H14001;//客商余额结算
    }

    public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            CustoverageHVO.class.getName(),
            CustoverageVO.class.getName()
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
        return "pk_custoverage";
    }

    public String getHeadZYXKey() {
        return null;
    }

    public String getPkField() {
        return "pk_custoverage_h";
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
    
}
