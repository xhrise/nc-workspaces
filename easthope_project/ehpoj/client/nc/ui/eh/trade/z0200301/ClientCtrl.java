package nc.ui.eh.trade.z0200301;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0200301.PriceBVO;
import nc.vo.eh.trade.z0200301.PriceVO;

/**
 * 功能说明：牌价调整单
 * @author 王明
 * 2008-03-24 下午04:03:18
 */
public class ClientCtrl extends AbstractCtrl {

    @Override
	public int[] getCardButtonAry() {        
        int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                new int[] { IEHButton.ChooseInv},
                ISHSHConst.CARD_BUTTONS_M, 6);
        return btns;
    }

    @Override
	public int[] getListButtonAry() {
       return ISHSHConst.LIST_BUTTONS_M;
    }


    @Override
	public String getBillType() {
        return IBillType.eh_z0200301;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            PriceVO.class.getName(),
            PriceBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_price_b";
    }

    @Override
	public String getPkField() {
        return "pk_price";
    }

}
