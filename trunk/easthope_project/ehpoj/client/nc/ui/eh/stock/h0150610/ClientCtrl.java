package nc.ui.eh.stock.h0150610;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.h0150605.ArapStockinvoiceVO;
import nc.vo.eh.stock.h0150605.ArapStockinvoicesBVO;


/**
 * 功能说明：采购发票审批
 * @author 王明
 * 2008-05-29 下午02:03:18
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
