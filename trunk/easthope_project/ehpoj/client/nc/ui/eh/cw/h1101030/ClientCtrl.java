package nc.ui.eh.cw.h1101030;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.cw.h1101005.ArapFkBVO;
import nc.vo.eh.cw.h1101005.ArapFkVO;
import nc.vo.eh.pub.PubBillVO;


/**
 * 功能说明：运费支付单
 * @author wb
 * 2009-4-15 15:48:17
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
	        return IBillType.eh_h1101025;
	    }

	    @Override
		public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            ArapFkVO.class.getName(),
	            ArapFkBVO.class.getName()  
	        };
	    }

	    @Override
		public String getChildPkField() {
	        return "pk_fk_b";
	    }

	    @Override
		public String getPkField() {
	        return "pk_fk";
	    }



}
