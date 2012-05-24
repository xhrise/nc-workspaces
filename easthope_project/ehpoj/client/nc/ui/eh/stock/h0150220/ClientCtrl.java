package nc.ui.eh.stock.h0150220;


import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.stock.h0150220.StockDecisionBVO;
import nc.vo.eh.stock.h0150220.StockDecisionBillVO;
import nc.vo.eh.stock.h0150220.StockDecisionCVO;
import nc.vo.eh.stock.h0150220.StockDecisionDVO;
import nc.vo.eh.stock.h0150220.StockDecisionEVO;
import nc.vo.eh.stock.h0150220.StockDecisionVO;

/**
 * 采购决策
 * ZB19	
 * @author wangbing
 * 2008-12-29 8:57:25
 */
public class ClientCtrl extends AbstractCtrl {
 
    public ClientCtrl() {
        super();
    }

    @Override
	public String[] getCardBodyHideCol() {
        return null;
    }

    @Override
	public int[] getCardButtonAry() {
    	 int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(ISHSHConst.CARD_BUTTONS_M,
                 new int[] { IBillButton.Busitype},
                 99);
         int[] btns1=nc.ui.eh.button.ButtonTool.insertButtons(btns,
                 new int[] {IEHButton.BusinesBtn },
                 0);
         return btns;//modify houcq 2010-11-30取消采购决策终止单据按钮
    }



    @Override
	public boolean isShowCardRowNo() {
        return true;
    }

    @Override
	public boolean isShowCardTotal() {
        return true;
    }

    @Override
	public String[] getListBodyHideCol() {
        return null;
    }

    @Override
	public int[] getListButtonAry() {
    	int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                new int[] { IBillButton.Busitype},
                ISHSHConst.LIST_BUTTONS_M, 0);
        return btns;
    }
    @Override
	public String[] getListHeadHideCol() {
        return null;
    }
    @Override
	public boolean isShowListRowNo() {
        return true;
    }

    @Override
	public boolean isShowListTotal() {
        return true;
    }
    @Override
	public String[] getBillVoName() {
        return new String[]{
            StockDecisionBillVO.class.getName(),
            StockDecisionVO.class.getName(),
            StockDecisionBVO.class.getName(),
            StockDecisionCVO.class.getName(),
            StockDecisionDVO.class.getName(),
            StockDecisionEVO.class.getName()
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
        return IBusinessActionType.PLATFORM;
    }

    @Override
	public String getChildPkField() {
        return null;
    }

    @Override
	public String getHeadZYXKey() {
        return null;
    }

    @Override
	public String getPkField() {
        return "pk_decision";
    }
    @Override
	public Boolean isEditInGoing() throws Exception {
        return null;
    }

    @Override
	public boolean isExistBillStatus() {
        return true;
    }

    @Override
	public boolean isLoadCardFormula() {
        return true;
    }

    @Override
	public String getBillType() {
        return IBillType.eh_h0150220;
    }


}