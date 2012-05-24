package nc.ui.eh.cw.h1100510;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.cw.h1100505.ArapFkqsBVO;
import nc.vo.eh.cw.h1100505.ArapFkqsVO;
import nc.vo.eh.pub.PubBillVO;


/**
 * 功能说明：付款申请单审批
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
	        return IBillType.eh_h1100505;
	    }

	    @Override
		public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            ArapFkqsVO.class.getName(),
	            ArapFkqsBVO.class.getName()  
	        };
	    }

	    @Override
		public String getChildPkField() {
	        return "pk_fkqs_b";
	    }

	    @Override
		public String getPkField() {
	        return "pk_fkqs";
	    }



}
