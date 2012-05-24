package nc.ui.eh.iso.z0501510;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.iso.z0501505.StockCheckreportBVO;
import nc.vo.eh.iso.z0501505.StockSampleVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 说明：抽样单 （审批）
 * @author 张起源
 * 时间：2008-4-14
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
        return IBillType.eh_z0501505;
    }

	 @Override
	public String[] getBillVoName() {
		 return new String[]{
		            PubBillVO.class.getName(),
		            StockSampleVO.class.getName(),
		            StockCheckreportBVO.class.getName()   
	        };
	    }

	@Override
	public String getChildPkField() {
		return "pk_checkreport_b";
	}
	
	@Override
	public String getPkField() {
		return "pk_sample";
	}


}
