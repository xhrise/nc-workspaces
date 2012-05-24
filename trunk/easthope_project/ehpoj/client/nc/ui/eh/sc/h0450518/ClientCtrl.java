package nc.ui.eh.sc.h0450518;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0450516.ScBomapplyaBVO;
import nc.vo.eh.sc.h0450516.ScBomapplyaVO;

/**
 * 
���ܣ�BOM������һ��
���ߣ�wm
���ڣ�2008-10-26 ����10:56:36
 */

public class ClientCtrl extends AbstractCtrl {

    public ClientCtrl() {
        super();
      
    }
    @Override
	public int[] getListButtonAry() {
        return nc.ui.eh.pub.PubTools.getSPLButton();
    }

	@Override
	public int[] getCardButtonAry() {
		return nc.ui.eh.pub.PubTools.getSPCButton();
	}

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO �Զ����ɷ������
    	return IBillType.eh_z0450516;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            ScBomapplyaVO.class.getName(),
            ScBomapplyaBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_applya_b";
    }

    @Override
	public String getPkField() {
        return "pk_applya";
    }


}
