
package nc.ui.eh.trade.z0206510;
/**
 * ���� ���֪ͨ��
 * @author �麣
 * 2008-04-08
 */
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0206510.LadingbillBVO;
import nc.vo.eh.trade.z0206510.LadingbillVO;


public class ClientCtrl extends AbstractCtrl {

    public ClientCtrl() {
        super();
        // TODO �Զ����ɹ��캯�����
    }

    @Override
	public int[] getCardButtonAry() {        
        int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(ISHSHConst.CARD_BUTTONS_M,
                new int[] { IBillButton.Busitype},
                99);
        int[] btns1=nc.ui.eh.button.ButtonTool.insertButtons(btns,
                new int[] {IEHButton.BusinesBtn},
                0);
        return btns;//modify houq 2010-11-29ȡ����ֹ���ݰ�ť
    }

    @Override
	public int[] getListButtonAry() {
       return nc.ui.eh.button.ButtonTool.insertButtons(
               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
    }


 

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO �Զ����ɷ������
        return IBillType.eh_z0206510;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            LadingbillVO.class.getName(),
            LadingbillBVO.class.getName()
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
        return "pk_ladingbill_b";
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_ladingbill";
    }



}