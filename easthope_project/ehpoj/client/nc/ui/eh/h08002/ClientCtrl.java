/**
 * 
 */
package nc.ui.eh.h08002;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.h08002.CubasdocVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 说明: 客商数据导入
 * @author zqy
 * 2008-8-5 11:02:18
 * 导入可销料和营销代表的导入
 * edit by wb 2009-11-18 11:34:44 
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
                IEHButton.CONFIRMBUG, //读取
                IEHButton.CARDAPPROVE //导入
        };
    }

    public boolean isShowCardRowNo() {
        return true;
    }

    public boolean isShowCardTotal() {
        return true;
    }

    public String getBillType() {
        return IBillType.eh_z08002;
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
