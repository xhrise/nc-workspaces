package nc.ui.eh.kc.h0251015;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.kc.h0251005.ScCkdBVO;
import nc.vo.eh.kc.h0251005.ScCkdVO;
import nc.vo.eh.pub.PubBillVO;

/** 
 * 说明：材料出库单(审批)
 * @author 张起源 
 * 时间：2008-5-08
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
        return IBillType.eh_h0251005;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            ScCkdVO.class.getName(),
	            ScCkdBVO.class.getName()    
	        };
	    }

	@Override
	public String getChildPkField() {
		return "pk_ckd_b";
	}

	@Override
	public String getPkField() {
		return "pk_ckd";
	}

	
}
