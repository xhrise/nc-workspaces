package nc.ui.eh.stock.z0502510;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0502505.ProcheckapplyBVO;
import nc.vo.eh.stock.z0502505.ProcheckapplyVO;



/**
 * 功能说明：成品检测申请
 * @author 王明
 * 2008-03-24 下午04:03:18
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
        return IBillType.eh_z0502505;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            ProcheckapplyVO.class.getName(),
            ProcheckapplyBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_procheckapply_b";
    }

    @Override
	public String getPkField() {
        return "pk_procheckapply";
    }
}
