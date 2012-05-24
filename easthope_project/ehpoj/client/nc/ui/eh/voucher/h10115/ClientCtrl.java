/**
 * 
 */
package nc.ui.eh.voucher.h10115;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.voucher.h10115.PfxxVoucherVO;


/**
 * 说明: 凭证导入
 * @author wb
 * 2009-9-27 13:49:49
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
                IEHButton.CONFIRMBUG, 	//读取
                IEHButton.GENRENDETAIL,	//生成XML
                IEHButton.ExcelImport 	//导入
        };
    }

    public boolean isShowCardRowNo() {
        return true;
    }

    public boolean isShowCardTotal() {
        return true;
    }

    public String getBillType() {
        return IBillType.eh_vh10115;
    }

    public String[] getBillVoName() {
        return new String[]{
                PubBillVO.class.getName(),
                PfxxVoucherVO.class.getName(),
                PfxxVoucherVO.class.getName(),
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
        return "pk_voucher";
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
