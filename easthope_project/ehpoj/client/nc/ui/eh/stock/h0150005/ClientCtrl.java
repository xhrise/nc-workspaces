package nc.ui.eh.stock.h0150005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.h0150005.GyspjBVO;
import nc.vo.eh.stock.h0150005.GyspjVO;

/**
 * 
 * @author 
 功能：供应商评价
 作者：zqy
 日期：2009-3-9 下午02:42:09
 */

public class ClientCtrl extends AbstractCtrl {

    public boolean isAutoManageTree() {
        return true;
    }

    public boolean isTableTree() {
        return true;
    }

    @Override
	public String[] getCardBodyHideCol() {
        return null;
    }

    @Override
	public int[] getCardButtonAry() {
        return nc.ui.eh.button.ButtonTool.insertButtons(new int[]{IBillButton.Copy,IEHButton.GENRENDETAIL},ISHSHConst.CARD_BUTTONS,20);
    }

    @Override
	public boolean isShowCardRowNo() {
        return true;
    }

    @Override
	public boolean isShowCardTotal() {
        return false;
    }

    @Override
	public String getBillType() {
        return IBillType.eh_h0150005;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
                PubBillVO.class.getName(),
                GyspjVO.class.getName(),
                GyspjBVO.class.getName()
            };
    }

    @Override
	public String getBodyCondition() {
        return null;
    }

    @Override
	public String getBodyZYXKey() {
        return null;
    }

    @Override
	public int getBusinessActionType() {
      return IBusinessActionType.BD;
    }

    @Override
	public String getChildPkField() {
        return "pk_gyspj_b";
    }

    @Override
	public String getHeadZYXKey() {
        return null;
    }

    @Override
	public String getPkField() {
        return "pk_gyspj";
    }

    @Override
	public boolean isLoadCardFormula() {
        return true;
    }

    @Override
	public int[] getListButtonAry() {
        return ISHSHConst.LIST_BUTTONS;
    }

    @Override
	public boolean isShowListRowNo() {
        return true;
    }

    @Override
	public boolean isShowListTotal() {
        return false;
    }

    public boolean isChildTree() {
        return false;
    }

    @Override
	public String[] getListBodyHideCol() {
        return null;
    }

    @Override
	public String[] getListHeadHideCol() {
        return null;
    }

    @Override
	public Boolean isEditInGoing() throws Exception {
        return null;
    }

    @Override
	public boolean isExistBillStatus() {
        return false;
    }
}
