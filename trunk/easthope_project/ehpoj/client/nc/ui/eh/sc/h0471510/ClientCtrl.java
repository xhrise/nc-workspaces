package nc.ui.eh.sc.h0471510;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0471505.ScSbserviceBVO;
import nc.vo.eh.sc.h0471505.ScSbserviceVO;


/**
 * ����:�豸ά������
 * ZB30
 * @author WB
 * 2009-2-11 11:10:48
 * 
 */
public class ClientCtrl extends AbstractCtrl {

    /**
     * 
     */
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
        return IBillType.eh_h0471505;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            ScSbserviceVO.class.getName(),
            ScSbserviceBVO.class.getName()
        };
    }


    @Override
	public String getChildPkField() {
        // TODO �Զ����ɷ������
        return "pk_service_b";
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_service";
    }
    

}
