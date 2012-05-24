package nc.ui.eh.kc.h0251510;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.kc.h0251505.ScBldBVO;
import nc.vo.eh.kc.h0251505.ScBldVO;
import nc.vo.eh.pub.PubBillVO;

/** 
 * 说明：生产备料单(审批)
 * @author 张起源 
 * 时间：2008-5-19 17:21:52
 */
public class ClientCtrl extends AbstractCtrl {

	
	@Override
	public String getBillType() {
        return IBillType.eh_z0251505;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            ScBldVO.class.getName(),
	            ScBldBVO.class.getName()    
	        };
	    }

	@Override
	public String getChildPkField() {
		return "pk_bld_b";
	}

	@Override
	public String getPkField() {
		return "pk_bld";
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
