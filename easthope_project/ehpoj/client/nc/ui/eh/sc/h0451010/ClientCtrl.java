package nc.ui.eh.sc.h0451010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0451005.ScPosmBVO;
import nc.vo.eh.sc.h0451005.ScPosmVO;


/**
 * 功能说明：生产任务单审批
 * @author 王明
 * 2008-05-07 下午04:03:18
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
        return IBillType.eh_h0451005;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            ScPosmVO.class.getName(),
            ScPosmBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_posm_b";
    }

    @Override
	public String getPkField() {
        return "pk_posm";
    }



}
