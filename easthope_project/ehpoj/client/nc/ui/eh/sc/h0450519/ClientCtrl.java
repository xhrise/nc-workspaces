package nc.ui.eh.sc.h0450519;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0450517.ScBomapplybBVO;
import nc.vo.eh.sc.h0450517.ScBomapplybVO;

/**
 * 
功能：BOM生报表（二）
作者：wm
日期：2008-10-26 上午10:56:36
 */

public class ClientCtrl extends AbstractCtrl {

    public ClientCtrl() {
        super();
      
    }
    @Override
	public int[] getListButtonAry() {
        return nc.ui.eh.pub.PubTools.getSPLButton();
    }

	@Override
	public int[] getCardButtonAry() {
		return nc.ui.eh.pub.PubTools.getSPCButton();
	}

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        return IBillType.eh_z0450517;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            ScBomapplybVO.class.getName(),
            ScBomapplybBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_applyb_b";
    }

    @Override
	public String getPkField() {
        return "pk_applyb";
    }


}
