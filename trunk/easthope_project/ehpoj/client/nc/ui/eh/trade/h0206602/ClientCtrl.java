package nc.ui.eh.trade.h0206602;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.h0206602.TradeDriverVO;


/**
 * ����:˾������
 * ZB08
 * @author WB
 * 2008-11-21 20:08:18
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
	public int[] getCardButtonAry() {        
         return ISHSHConst.CARD_BUTTONS;
    }

    @Override
	public int[] getListButtonAry() {
    	 return ISHSHConst.LIST_BUTTONS;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO �Զ����ɷ������
        return IBillType.eh_h0206602;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            TradeDriverVO.class.getName(),
            TradeDriverVO.class.getName()
        };
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO �Զ����ɷ������
        return null;
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_driver";
    }
    
    @Override
	public int getBusinessActionType() {
        return nc.ui.trade.businessaction.IBusinessActionType.BD;
    }
    
    /**
     * �Ƿ���ڵ���״̬�� �������ڣ�(2004-2-5 13:04:45)
     * 
     * @return boolean
     */
    @Override
	public boolean isExistBillStatus() {
        return false;
    }

}