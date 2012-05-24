package nc.ui.eh.kc.h0256010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.kc.h0256005.ScCprkdBVO;
import nc.vo.eh.kc.h0256005.ScCprkdVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * ����˵�����˻���ⵥ(����)
 * @author ����Դ
 * ʱ�䣺2008-5-27 9:53:10
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
        return IBillType.eh_h0256005;
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
