/*
 * �������� 2006-6-20
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.trade.z0205510;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.trade.z0205510.SeconddiscountBillVO;
import nc.vo.eh.trade.z0205510.SeconddiscountCheckinvVO;
import nc.vo.eh.trade.z0205510.SeconddiscountPoliceVO;
import nc.vo.eh.trade.z0205510.SeconddiscountRangeVO;
import nc.vo.eh.trade.z0205510.SeconddiscountVO;



/**
 * ����˵���������ۿ�
 * 2008-4-8 16:43:39
 */
public class ClientCtrl extends AbstractManageController {

    public ClientCtrl() {
        super();
        // TODO �Զ����ɹ��캯�����
    }


    public String[] getCardBodyHideCol() {
        // TODO �Զ����ɷ������
        return null;
    }

 
    public int[] getCardButtonAry() {
        // TODO �Զ����ɷ������
    	 int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
         		ISHSHConst.CARD_BUTTONS_M,
         		new int[] {IEHButton.GENRENDETAIL,IEHButton.STOCKCHANGE},
                 0);//modify by houcq 2010-11-30ȡ�������ۿ���ֹ���ݰ�ť
    	 return btns;
//        return new int[]{
//                IBillButton.Query,
//                IBillButton.Add,
//                IBillButton.Line,
//                IBillButton.Edit,
//                IBillButton.Delete,
//                IBillButton.Save,
//                IBillButton.Action,
//                IBillButton.Return,
//                IBillButton.Cancel,
//                IBillButton.Refresh,
////                IBillButton.Copy,
//                IEHButton.GENRENDETAIL
//            };
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
    	return nc.ui.eh.button.ButtonTool.insertButtons(
                new int[] {},ISHSHConst.LIST_BUTTONS_M, 0);
        // TODO �Զ����ɷ������
//        return new int[]{
//                IBillButton.Query,
//                IBillButton.Add,
//                IBillButton.Edit,
//                IBillButton.Delete,
//                IBillButton.Card
//
//            };
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
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            SeconddiscountBillVO.class.getName(),
            SeconddiscountVO.class.getName(),
            SeconddiscountRangeVO.class.getName(),
            SeconddiscountPoliceVO.class.getName(),
            SeconddiscountCheckinvVO.class.getName()
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
        return "pk_seconddiscount";
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

    public String getBillType() {
        // TODO Auto-generated method stub
        return IBillType.eh_z0205510;
    }


}