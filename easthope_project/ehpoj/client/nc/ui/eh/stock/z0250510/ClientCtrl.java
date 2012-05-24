package nc.ui.eh.stock.z0250510;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0250505.StockInBVO;
import nc.vo.eh.stock.z0250505.StockInVO;



/**
 * 功能说明：入库单
 * @author 王明
 * 2008-03-24 下午04:03:18
 */
public class ClientCtrl extends AbstractCtrl {

	  @Override
	public int[] getListButtonAry() {
	        return nc.ui.eh.pub.PubTools.getSPLButton();
	    }

		@Override
		public int[] getCardButtonAry() {
			return nc.ui.eh.pub.PubTools.getSPCButton();
		}
		

    @Override
	public String getBillType() {
        return IBillType.eh_z0250505;
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

}
