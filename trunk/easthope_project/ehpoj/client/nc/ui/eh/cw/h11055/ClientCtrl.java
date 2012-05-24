/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.cw.h11055;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.h11055.ArapCosthsBVO;
import nc.vo.eh.cw.h11055.ArapCosthsVO;
import nc.vo.eh.pub.PubBillVO;


/**
 * ˵�����ɱ�����
 * ���ͣ�ZA72
 * ���ߣ�wb
 * ʱ�䣺2008-8-11 15:34:32
 */
public class ClientCtrl extends AbstractCtrl {

    /**
     * 
     */
    public ClientCtrl() {
        super();
        // TODO �Զ����ɹ��캯�����
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.bill.ICardController#getCardButtonAry()
     */
    @Override
	public int[] getCardButtonAry() {
        // TODO �Զ����ɷ������
        return new int[]{
                IBillButton.Query,
                IEHButton.CALCKCYBB,
                IBillButton.Return,
                IBillButton.Refresh,
                IBillButton.Print,
                IEHButton.GENRENDETAIL
               
        };
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.bill.IListController#getListButtonAry()
     */
    @Override
	public int[] getListButtonAry() {
        // TODO �Զ����ɷ������
        return new int[]{
                IBillButton.Query,
                IEHButton.CALCKCYBB,
                IBillButton.Card,
                IBillButton.Refresh
        };
    }

    

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO �Զ����ɷ������
        return IBillType.eh_h11055;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            ArapCosthsVO.class.getName(),
            ArapCosthsBVO.class.getName()
        };
    }



    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBusinessActionType()
     */
    @Override
	public int getBusinessActionType() {
        // TODO �Զ����ɷ������
        return IBusinessActionType.BD;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO �Զ����ɷ������
        return "pk_cosths_b";
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_cosths";
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#isExistBillStatus()
     */
    @Override
	public boolean isExistBillStatus() {
        // TODO �Զ����ɷ������
        return false;
    }


}
