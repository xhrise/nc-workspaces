package nc.ui.eh.trade.h0208005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.h0208005.TradeMaterialpriceBVO;
import nc.vo.eh.trade.h0208005.TradeMaterialpriceVO;


/**
 * 功能 原料价格
 * ZA98
 * @author WB
 * 2008-10-14 13:45:27
 */
public class ClientCtrl extends AbstractCtrl {

    /**
     * 
     */
    public ClientCtrl() {
        super();
      
    }
    @Override
	public int[] getCardButtonAry() {        
    	int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
        		ISHSHConst.CARD_BUTTONS_M,
        		new int[] {IEHButton.GENRENDETAIL,IBillButton.Copy},
                0);
    	int[] btns1 = nc.ui.eh.button.ButtonTool.deleteButton(IBillButton.Action, btns);
    	return btns1;
        
    }

    @Override
	public int[] getListButtonAry() {
       return nc.ui.eh.button.ButtonTool.insertButtons(
               new int[] {},ISHSHConst.LIST_BUTTONS_M, 0);
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO 自动生成方法存根
        return IBillType.eh_h0208005;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO 自动生成方法存根
        return new String[]{
            PubBillVO.class.getName(),
            TradeMaterialpriceVO.class.getName(),
            TradeMaterialpriceBVO.class.getName()
        };
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO 自动生成方法存根
        return "pk_price_b";
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO 自动生成方法存根
        return "pk_price";
    }

    @Override
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}
}
