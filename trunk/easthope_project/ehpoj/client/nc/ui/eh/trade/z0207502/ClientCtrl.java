/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.trade.z0207502;


import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0207501.InvoiceBVO;
import nc.vo.eh.trade.z0207501.InvoiceVO;

/**
 * @author wangming
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class ClientCtrl extends AbstractManageController {

    /**
     * @return 
     * 
     */
   

    /* ���� Javadoc��
     * @see nc.ui.trade.bill.ICardController#getCardBodyHideCol()
     */
    public String[] getCardBodyHideCol() {
        // TODO �Զ����ɷ������
        return null;
    }
    public int[] getListButtonAry() {
        return nc.ui.eh.pub.PubTools.getSPLButton();
    }

	public int[] getCardButtonAry() {
		return nc.ui.eh.pub.PubTools.getSPCButton();
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
        return false;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.bill.IListController#getListBodyHideCol()
     */
    public String[] getListBodyHideCol() {
        // TODO �Զ����ɷ������
        return null;
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
        return false;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    public String getBillType() {
        // TODO �Զ����ɷ������
        return IBillType.eh_z0207501;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            InvoiceVO.class.getName(),
            InvoiceBVO.class.getName()
            
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
        return IBusinessActionType.PLATFORM;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    public String getChildPkField() {
        // TODO �Զ����ɷ������
        return "pk_invoice_b";
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
        return "pk_invoice";
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
        return true;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#isLoadCardFormula()
     */
    public boolean isLoadCardFormula() {
        // TODO �Զ����ɷ������
        return true;
    }
    

}
