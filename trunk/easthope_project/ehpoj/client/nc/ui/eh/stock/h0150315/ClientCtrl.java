package nc.ui.eh.stock.h0150315;


import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.stock.h0150315.StockWjDecisionBillVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionBVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionCVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionDVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionVO;

/**
 * 五金采购决策
 * ZB23
 * @author wangbing
 * 2009-1-7 17:38:41
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
                 new int[] {IEHButton.BusinesBtn},
                 0);
         return btns;//modify by houcq 2010-11-30 取消五金采购决策终止单据按钮
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
            StockWjDecisionBillVO.class.getName(),
            StockWjdecisionVO.class.getName(),            
            StockWjdecisionCVO.class.getName(),
            StockWjdecisionBVO.class.getName(),
            StockWjdecisionDVO.class.getName()//cvo和bvo交换位置
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
        return "pk_wjdecision";
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
        return IBillType.eh_h0150315;
    }


}