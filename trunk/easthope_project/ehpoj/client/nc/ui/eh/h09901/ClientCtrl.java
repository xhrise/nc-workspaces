package nc.ui.eh.h09901;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.h09901.BugVO;
import nc.vo.eh.pub.PubBillVO;


/**
 * ˵��: BUG�ĵ�
 * @author ������
 * 2007-11-26 ����06:34:49
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

                IBillButton.Edit,
                IBillButton.Line,
                IBillButton.Save,
                IEHButton.CONFIRMBUG,
//                IEHButton.CARDAPPROVE,
                
                IBillButton.Cancel,
                IBillButton.Refresh
        };
    }

    public boolean isShowCardRowNo() {
        return true;
    }

    public boolean isShowCardTotal() {
        return true;
    }

    public String getBillType() {
        return IBillType.eh_h09901;
    }

    public String[] getBillVoName() {
        return new String[]{
                PubBillVO.class.getName(),
                BugVO.class.getName(),
                BugVO.class.getName(),
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
        return "pk_bug";
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