package nc.ui.eh.stock.z06010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z06005.SbbillBVO;
import nc.vo.eh.stock.z06005.SbbillVO;

/**���ܣ�˾��������
 * @author wb
 * 2008-10-27 13:43:47
 */

public class ClientCtrl extends AbstractCtrl {

    public ClientCtrl() {
        super();
        // TODO �Զ����ɹ��캯�����
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.bill.ICardController#getCardBodyHideCol()
     */
    @Override
	public String[] getCardBodyHideCol() {
        // TODO �Զ����ɷ������
        return null;
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
        return IBillType.eh_z06005;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
        		 PubBillVO.class.getName(),
                 SbbillVO.class.getName(),
                 SbbillBVO.class.getName()
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
        return "pk_sbbill_b";
    }

    
    @Override
	public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_sbbill";
    }

    

}
