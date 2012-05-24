package nc.ui.eh.cw.h1200203;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.cw.h1200202.HjlhsBVO;
import nc.vo.eh.cw.h1200202.HjlhsVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * 功能说明：回机料核算审批
 * @author houcq
 * 2011-06-16 下午02:03:18
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
	        return IBillType.eh_h1200202;
	    }

	    @Override
		public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
				HjlhsVO.class.getName(),
				HjlhsBVO.class.getName(), 
	        };
	    }

	    public String getChildPkField() {
			return "pk_hjlhs_b";
		}

		public String getPkField() {
			return "pk_hjlhs";
		}	
}
