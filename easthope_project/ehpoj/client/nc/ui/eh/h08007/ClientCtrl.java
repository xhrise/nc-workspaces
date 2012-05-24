/**
 * 
 */
package nc.ui.eh.h08007;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.h08007.CubasdocVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 
���ܣ���NC�аѹ�Ӧ�����ݵ��뵽U8���̱���
���ߣ�zqy
���ڣ�2008-11-6 ����09:10:14
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
                IEHButton.CONFIRMBUG, //��ȡ
                IEHButton.CARDAPPROVE //����
        };
    }

    public boolean isShowCardRowNo() {
        return true;
    }

    public boolean isShowCardTotal() {
        return true;
    }

    public String getBillType() {
        return IBillType.eh_z08007;
    }

    public String[] getBillVoName() {
        return new String[]{
                PubBillVO.class.getName(),
                CubasdocVO.class.getName(),
                CubasdocVO.class.getName(),
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
        return "pk_cubasdoc";
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
