package nc.ui.eh.cw.h1101010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.cw.h1101005.ArapFkBVO;
import nc.vo.eh.cw.h1101005.ArapFkVO;
import nc.vo.eh.pub.PubBillVO;


/**
 * 功能说明：付款单审批
 * @author 王明
 * 2008-05-28 下午02:03:18
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
	        return IBillType.eh_h1101005;
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
