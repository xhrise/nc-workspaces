package nc.ui.eh.stock.h0150605;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.h0150605.ArapStockinvoiceVO;
import nc.vo.eh.stock.h0150605.ArapStockinvoicesBVO;


/**
 * 功能说明：采购发票
 * @author 王明
 * 2008-05-29 下午04:03:18
 */
public class ClientCtrl extends AbstractCtrl {

	 @Override
	public int[] getCardButtonAry() {        
	        int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
	                new int[] { IBillButton.Busitype},
	                ISHSHConst.CARD_BUTTONS_M, 0);
	        int[] btnss = nc.ui.eh.button.ButtonTool.insertButtons(
	        		btns,
	        		new int[]{IEHButton.SUREMONEY}, 0);
            int[] btnss1= nc.ui.eh.button.ButtonTool.insertButtons(new int[]{IEHButton.BusinesBtn},btnss,15);
	        return btnss;//modify by houcq 2010-11-30取消采购发票终止单据按钮
	    }

	 @Override
	public int[] getListButtonAry() {
	       return nc.ui.eh.button.ButtonTool.insertButtons(
	               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
	    }

    @Override
	public String getBillType() {
        return IBillType.eh_h0150605;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            ArapStockinvoiceVO.class.getName(),
            ArapStockinvoicesBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_stockinvoices_b";
    }

    @Override
	public String getPkField() {
        return "pk_stockinvoice";
    }

}
