package nc.ui.eh.stock.z0151010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0151001.StockReceiptBVO;
import nc.vo.eh.stock.z0151001.StockReceiptVO;


/**
 * 功能说明：收货通知单
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
        return IBillType.eh_z0151001;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            StockReceiptVO.class.getName(),
            StockReceiptBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_receipt_b";
    }

    @Override
	public String getPkField() {
        return "pk_receipt";
    }



}
