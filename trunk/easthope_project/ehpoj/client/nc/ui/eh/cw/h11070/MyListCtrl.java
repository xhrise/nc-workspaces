
package nc.ui.eh.cw.h11070;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;


/**
 功能：收款与发票核销
 作者：newyear
 日期：2008-9-2 11:55:00
 **/
public class MyListCtrl implements IListController {

    public MyListCtrl() {
    }

    public String[] getListBodyHideCol() {
        return null;
    }

    public int[] getListButtonAry() {
        return new int[]{
                IBillButton.Query,
                IEHButton.Next
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
        return IBillType.eh_h11070;
    }

    public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            nc.vo.eh.cw.h1103005.ArapSkVO.class.getName(),
            nc.vo.eh.trade.z0207501.InvoiceVO.class.getName(),
        };
    }


    public String getBodyCondition() {
        return " 1=2 ";
    }


    public String getBodyZYXKey() {
        return null;
    }

   
    public int getBusinessActionType() {
        return IBusinessActionType.BD;
    }

 
    public String getChildPkField() {
        return "pk_invoice";
    }

  
    public String getHeadZYXKey() {
        return null;
    }

 
    public String getPkField() {
        return "pk_sk";
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

}

