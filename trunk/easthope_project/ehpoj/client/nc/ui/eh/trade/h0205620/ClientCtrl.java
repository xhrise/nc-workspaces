package nc.ui.eh.trade.h0205620;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.h0205615.TradePeriodplanBVO;
import nc.vo.eh.trade.h0205615.TradePeriodplanVO;


/**
 * ����:����Ѯ�ƻ�����
 * ZB14
 * @author WB
 * 2008-12-22 16:26:54 
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
        return IBillType.eh_h0205615;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            TradePeriodplanVO.class.getName(),
            TradePeriodplanBVO.class.getName()
        };
    }


    @Override
	public String getChildPkField() {
        // TODO �Զ����ɷ������
        return "pk_periodplan_b";
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_periodplan";
    }
    

}
