/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.trade.z0206010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0206005.OrderBVO;
import nc.vo.eh.trade.z0206005.OrderVO;


/**
 * @author answer
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class ClientCtrl extends AbstractCtrl {

    public ClientCtrl() {
        super();
        // TODO �Զ����ɹ��캯�����
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
        return IBillType.eh_z0206005;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            OrderVO.class.getName(),
            OrderBVO.class.getName()
        };
    }

    
    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBusinessActionType()
     */
    @Override
	public int getBusinessActionType() {
        // TODO �Զ����ɷ������
        //return IBusinessActionType.PLATFORM;
        return IBusinessActionType.PLATFORM;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO �Զ����ɷ������
        return "pk_order_b";
    }

 

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_order";
    }

}
