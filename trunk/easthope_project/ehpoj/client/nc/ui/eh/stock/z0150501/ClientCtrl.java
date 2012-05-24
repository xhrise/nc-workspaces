package nc.ui.eh.stock.z0150501;


import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.stock.z0150501.StockBillVO;
import nc.vo.eh.stock.z0150501.StockContractBVO;
import nc.vo.eh.stock.z0150501.StockContractEventVO;
import nc.vo.eh.stock.z0150501.StockContractTermsVO;
import nc.vo.eh.stock.z0150501.StockContractVO;

/**
 * 采购合同
 * @author 王明
 * 创建日期 2008-4-11 11:09:43
 */
public class ClientCtrl extends AbstractManageController {
 
    public ClientCtrl() {
        super();
    }

    public String[] getCardBodyHideCol() {
        return null;
    }

    public int[] getCardButtonAry() {
    	int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                new int[] { IBillButton.Busitype},
                ISHSHConst.CARD_BUTTONS_M, 0);
        int[] btnss = nc.ui.eh.button.ButtonTool.insertButtons(
        		btns,
        		new int[]{IEHButton.STOCKCOPE,
                        IEHButton.STOCKCHANGE}, 0);//modify by houcq 2010-11-30取消采购合同终止单据按钮
        return btnss;
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
    	int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                new int[] { IBillButton.Busitype},
                ISHSHConst.LIST_BUTTONS_M, 0);
        return btns;
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
            StockBillVO.class.getName(),
            StockContractVO.class.getName(),
            StockContractBVO.class.getName(),
            StockContractTermsVO.class.getName(),
            StockContractEventVO.class.getName()
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
        return null;
    }

    public String getHeadZYXKey() {
        return null;
    }

    public String getPkField() {
        return "pk_contract";
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
        return IBillType.eh_z0150501;
    }


}