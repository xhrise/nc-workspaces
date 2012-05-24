package nc.ui.eh.iso.z0502010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.iso.z0502005.StockCheckBillVO;
import nc.vo.eh.iso.z0502005.StockCheckreportBVO;
import nc.vo.eh.iso.z0502005.StockCheckreportCVO;
import nc.vo.eh.iso.z0502005.StockCheckreportVO;

/** 
 * 说明：检测报告单（审批）
 * @author 张起源 
 * 时间：2008-4-11
 */
public class ClientCtrl extends AbstractCtrl {

	 
	@Override
	public String getBillType() {
        return IBillType.eh_z0502005;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	        		StockCheckBillVO.class.getName(),
		            StockCheckreportVO.class.getName(),
		            StockCheckreportBVO.class.getName(),
	                StockCheckreportCVO.class.getName()
	        };
	    }  

	
	@Override
	public String getPkField() {
        return "pk_checkreport";
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
