package nc.ui.eh.stock.h0150115;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.h0150110.StockSpecplanBVO;
import nc.vo.eh.stock.h0150110.StockSpecplanVO;


/**
 * ����:����ɹ��ƻ�����
 * ZB16
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
	public int[] getListButtonAry() {
        return nc.ui.eh.pub.PubTools.getSPLButton();
    }

	@Override
	public int[] getCardButtonAry() {
		int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(new int[] {IEHButton.SpecialCG,IBillButton.Save,IBillButton.Cancel},
				nc.ui.eh.pub.PubTools.getSPCButton(),
                99);
		return btns;
	}

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO �Զ����ɷ������
        return IBillType.eh_h0150110;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
            PubBillVO.class.getName(),
            StockSpecplanVO.class.getName(),
            StockSpecplanBVO.class.getName()
        };
    }


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
