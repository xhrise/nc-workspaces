package nc.ui.eh.stock.h0150340;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.kc.h0251005.ScCkdBVO;
import nc.vo.eh.kc.h0251005.ScCkdVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 功能:五金采购出库
 * ZB25
 * @author WB
 * 2009-1-9 10:42:36
 *
 */
public class ClientCtrl extends AbstractCtrl {

	
	@Override
	public String getBillType() {
        return IBillType.eh_h0150335;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	        		 PubBillVO.class.getName(),
	                 ScCkdVO.class.getName(),
	                 ScCkdBVO.class.getName()
	        };
	    }

	
	@Override
	public String getChildPkField() {
		return "pk_ckd_b";
	}

	@Override
	public String getPkField() {
		return "pk_ckd";
	}

	  @Override
	public int[] getListButtonAry() {
	        return nc.ui.eh.pub.PubTools.getSPLButton();
	    }

	@Override
	public int[] getCardButtonAry() {
		return nc.ui.eh.pub.PubTools.getSPCButton();
	}
	

}
