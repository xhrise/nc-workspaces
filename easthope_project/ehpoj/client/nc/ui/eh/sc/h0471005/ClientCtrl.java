package nc.ui.eh.sc.h0471005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0471005.ScSbplanBVO;
import nc.vo.eh.sc.h0471005.ScSbplanVO;


/**
 * ����:�豸ά�޼ƻ�
 * ZB29
 * @author WB
 * 2008-12-24 15:45:31
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
    	 int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(ISHSHConst.CARD_BUTTONS_M,
                 new int[] { IBillButton.Busitype},
                 99);
         int[] btns1=nc.ui.eh.button.ButtonTool.insertButtons(btns,
                 new int[] { IEHButton.BusinesBtn},
                 0);
         return btns;//modify by houcq 2010-11-30ȡ���豸ά���ƻ�����ֹ���ݰ�ť
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
        return IBillType.eh_h0471005;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            ScSbplanVO.class.getName(),
            ScSbplanBVO.class.getName()
        };
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO �Զ����ɷ������
        return "pk_plan_b";
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_plan";
    }
    

}
