package nc.ui.eh.trade.z0205515;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0205505.FirstdiscountBVO;
import nc.vo.eh.trade.z0205505.FirstdiscountVO;


/**
 * ���� һ���ۿ۹���������
 * @author �麣
 * 2008-04-08
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
	

   
    @Override
	public String getBillType() {
        // TODO �Զ����ɷ������
        return IBillType.eh_z0205505;
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
        return "pk_firstdiscount_b";
    }

  

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_firstdiscount";
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            FirstdiscountVO.class.getName(),
            FirstdiscountBVO.class.getName()
        };
    }

}
