package nc.ui.eh.stock.h0150330;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0250505.StockInBVO;
import nc.vo.eh.stock.z0250505.StockInVO;

/** 
 * 说明：五金采购入库（审批）
 * @author 王兵
 * 时间：2009-1-8 16:39:08
 */
public class ClientCtrl extends AbstractCtrl {

	
	@Override
	public String getBillType() {
        return IBillType.eh_h0150325;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	        		PubBillVO.class.getName(),
	                StockInVO.class.getName(),
	                StockInBVO.class.getName() 
	        };
	    }

	
	@Override
	public String getChildPkField() {
		return "pk_in_b";
	}

	@Override
	public String getPkField() {
		return "pk_in";
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
