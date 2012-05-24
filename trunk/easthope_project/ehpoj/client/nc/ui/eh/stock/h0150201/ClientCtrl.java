package nc.ui.eh.stock.h0150201;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.h0150201.StockStandardBVO;
import nc.vo.eh.stock.h0150201.StockStandardVO;

/**
 * 
���ܣ�ԭ�ϲɹ���׼
���ߣ�zqy
���ڣ�2008-12-11 ����02:54:46
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
    	int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
        		new int[] {},ISHSHConst.CARD_BUTTONS_M,0);
    	int[] btns1 = nc.ui.eh.button.ButtonTool.deleteButton(IBillButton.Action, btns);
    	return btns1;
        
    }

    @Override
	public int[] getListButtonAry() {
       return nc.ui.eh.button.ButtonTool.insertButtons(
               new int[] {},ISHSHConst.LIST_BUTTONS_M, 0);
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO �Զ����ɷ������
        return IBillType.eh_h0100105;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            StockStandardVO.class.getName(),
            StockStandardBVO.class.getName()
        };
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO �Զ����ɷ������
        return "pk_standard_b";
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_standard";
    }

    @Override
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}
    
    /**
     * �Ƿ���ڵ���״̬�� �������ڣ�(2004-2-5 13:04:45)
     * 
     * @return boolean
     */
    @Override
	public boolean isExistBillStatus() {
        return false;
    }
}
