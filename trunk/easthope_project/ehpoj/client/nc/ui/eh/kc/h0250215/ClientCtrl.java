/**
 * 
 */
package nc.ui.eh.kc.h0250215;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.kc.h0250210.PeriodVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 说明: 会计期间
 * @author newyear
 * 2007-9-20 下午01:05:14
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
                IEHButton.PeriodClose,
                IEHButton.PERIODCANCEL,
              //add by houcq 2011-04-27增加新增年度期间按钮
                IEHButton.DOCMANAGE,
        };
    }

    public boolean isShowCardRowNo() {
        return true;
    }

    public boolean isShowCardTotal() {
        return false;
    }

    public String getBillType() {
        return IBillType.eh_h0250210;
    }

    public String[] getBillVoName() {
        return new String[]{
                PubBillVO.class.getName(),
                PeriodVO.class.getName(),
                PeriodVO.class.getName()                
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
        return "pk_period";
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
