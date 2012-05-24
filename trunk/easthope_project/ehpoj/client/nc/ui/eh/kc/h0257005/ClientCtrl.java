/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.kc.h0257005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.kc.h0257005.CalcKcybbBVO;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.eh.pub.PubBillVO;


/**
 * ˵����ԭ�Ͽ���±���
 * ���ͣ�ZA48
 * ���ߣ�wb
 * ʱ�䣺2008��5��8��16:34:56
 */
public class ClientCtrl extends AbstractManageController {

    /**
     * 
     */
    public ClientCtrl() {
        super();
        // TODO �Զ����ɹ��캯�����
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.bill.ICardController#getCardBodyHideCol()
     */
    public String[] getCardBodyHideCol() {
        // TODO �Զ����ɷ������
        return null;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.bill.ICardController#getCardButtonAry()
     */
    public int[] getCardButtonAry() {
        // TODO �Զ����ɷ������
        return new int[]{
                IBillButton.Query,
                IBillButton.Save,
                IEHButton.CALCKCYBB,
                IBillButton.Return,
                IBillButton.Refresh,
                IBillButton.Print
               
        };
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.bill.ICardController#isShowCardRowNo()
     */
    public boolean isShowCardRowNo() {
        // TODO �Զ����ɷ������
        return true;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.bill.ICardController#isShowCardTotal()
     */
    public boolean isShowCardTotal() {
        // TODO �Զ����ɷ������
        return true;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.bill.IListController#getListBodyHideCol()
     */
    public String[] getListBodyHideCol() {
        // TODO �Զ����ɷ������
        return null;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.bill.IListController#getListButtonAry()
     */
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
     * @see nc.ui.trade.bill.IListController#getListHeadHideCol()
     */
    public String[] getListHeadHideCol() {
        // TODO �Զ����ɷ������
        return null;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.bill.IListController#isShowListRowNo()
     */
    public boolean isShowListRowNo() {
        // TODO �Զ����ɷ������
        return true;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.bill.IListController#isShowListTotal()
     */
    public boolean isShowListTotal() {
        // TODO �Զ����ɷ������
        return true;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    public String getBillType() {
        // TODO �Զ����ɷ������
        return IBillType.eh_h0257005;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            CalcKcybbVO.class.getName(),
            CalcKcybbBVO.class.getName()
        };
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBodyCondition()
     */
    public String getBodyCondition() {
        // TODO �Զ����ɷ������
        return null;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBodyZYXKey()
     */
    public String getBodyZYXKey() {
        // TODO �Զ����ɷ������
        return null;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBusinessActionType()
     */
    public int getBusinessActionType() {
        // TODO �Զ����ɷ������
        return IBusinessActionType.BD;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    public String getChildPkField() {
        // TODO �Զ����ɷ������
        return null;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getHeadZYXKey()
     */
    public String getHeadZYXKey() {
        // TODO �Զ����ɷ������
        return null;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_kcybb";
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#isEditInGoing()
     */
    public Boolean isEditInGoing() throws Exception {
        // TODO �Զ����ɷ������
        return null;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#isExistBillStatus()
     */
    public boolean isExistBillStatus() {
        // TODO �Զ����ɷ������
        return false;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#isLoadCardFormula()
     */
    public boolean isLoadCardFormula() {
        // TODO �Զ����ɷ������
//      return true;
        return true;
    }
    
    

}
