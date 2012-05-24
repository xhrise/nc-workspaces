package nc.ui.eh.stock.z0502520;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0502515.StockBackBVO;
import nc.vo.eh.stock.z0502515.StockBackVO;

public class ClientCtrl extends AbstractCtrl {

	
	@Override
	public String getBillType() {
        return IBillType.eh_z0502515;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            StockBackVO.class.getName(),
            StockBackBVO.class.getName()  
        };
    }

	
	@Override
	public String getChildPkField() {
		return "pk_back_b";
	}

	@Override
	public String getPkField() {
		return "pk_back";
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
