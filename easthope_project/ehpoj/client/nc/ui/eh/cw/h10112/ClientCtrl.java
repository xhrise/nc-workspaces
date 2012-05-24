
package nc.ui.eh.cw.h10112;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.cw.h10112.ElectricitycostsBVO;
import nc.vo.eh.cw.h10112.ElectricitycostsVO;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;

/**
 * 说明: 电费基础
 * @author zqy
 * 时间：2008-9-10 14:58:09
 */
public class ClientCtrl extends AbstractManageController {


    public ClientCtrl() {
        super();
    }

    public String[] getCardBodyHideCol() {
        return null;
    }

    public int[] getCardButtonAry() {
        return ISHSHConst.CARD_BUTTONS_M;
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
        return ISHSHConst.LIST_BUTTONS_M;
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
        return IBillType.eh_h10112;
    }

    public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            ElectricitycostsVO.class.getName(),
            ElectricitycostsBVO.class.getName()
        };
    }

    public String getBodyZYXKey() {
        return null;
    }

    public int getBusinessActionType() {
        return IBusinessActionType.BD;
    }

    public String getChildPkField() {
    	return "pk_electricitycosts_b";
    }

    public String getHeadZYXKey() {
        return null;
    }

    public String getPkField() {
        return "pk_electricitycosts";
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

	public String getBodyCondition() {
		return null;
	}

}
