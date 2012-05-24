package nc.ui.eh.sc.h0452010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0452005.ScCprkdBVO;
import nc.vo.eh.sc.h0452005.ScCprkdVO;


/**
 * 功能说明：成品入库单审批
 * @author 王明
 * 2008-05-08 下午02:03:18
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
	        return IBillType.eh_h0452005;
	    }

	    @Override
		public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            ScCprkdVO.class.getName(),
	            ScCprkdBVO.class.getName()  
	        };
	    }

	    @Override
		public String getChildPkField() {
	        return "pk_rkd_b";
	    }

	    @Override
		public String getPkField() {
	        return "pk_rkd";
	    }





}
