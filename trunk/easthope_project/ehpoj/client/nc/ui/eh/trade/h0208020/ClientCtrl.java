package nc.ui.eh.trade.h0208020;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.h0208020.TradeCheckBVO;
import nc.vo.eh.trade.h0208020.TradeCheckVO;

/**
 * 
���ܣ�ӯ�������쳣��
���ߣ�zqy
���ڣ�2008-10-26 ����10:56:36
 */

public class ClientCtrl extends AbstractCtrl {

    public ClientCtrl() {
        super();
    }
    
    @Override
	public int[] getCardButtonAry() {        
        return new int[]{
                IBillButton.Busitype,
//                IBillButton.Query,
                IBillButton.Action,
                IBillButton.Edit,
                IBillButton.Delete,
                IBillButton.Cancel,
                IBillButton.Return,
                IBillButton.Refresh
        };
    }

    @Override
	public int[] getListButtonAry() {
        return new int[]{
                IBillButton.Busitype,
                IEHButton.CONFIRMBUG,  //�Զ��尴ť��ѯ
                IBillButton.Action,
//                IBillButton.Query,
                IBillButton.Card
        };
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO �Զ����ɷ������
        return IBillType.eh_h0208020;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            TradeCheckVO.class.getName(),
            TradeCheckBVO.class.getName()
        };
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO �Զ����ɷ������
        return "pk_check_b";
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_check";
    }
 
//    public boolean isExistBillStatus() {
//        // TODO Auto-generated method stub
//        return false;
//    }
    
}
