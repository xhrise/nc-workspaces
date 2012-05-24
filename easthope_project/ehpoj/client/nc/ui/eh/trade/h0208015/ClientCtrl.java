package nc.ui.eh.trade.h0208015;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.h0208015.TradeBasedataBVO;
import nc.vo.eh.trade.h0208015.TradeBasedataVO;


/**
 * ����:��Ʒӯ����������¼��
 * ZA90
 * @author WB
 * 2008-10-14 16:19:52
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
        		ISHSHConst.CARD_BUTTONS_M,
        		new int[] {IEHButton.GENRENDETAIL,IBillButton.Copy,IEHButton.CALCKCYBB},
                0);
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
        return IBillType.eh_h0208015;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            TradeBasedataVO.class.getName(),
            TradeBasedataBVO.class.getName()
        };
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO �Զ����ɷ������
        return "pk_basedata_b";
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_basedata";
    }

    @Override
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}
}
