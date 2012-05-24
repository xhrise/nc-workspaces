package nc.ui.eh.stock.h0150310;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.h0150305.StockApplyBVO;
import nc.vo.eh.stock.h0150305.StockApplyVO;


/**
 * ����:���ɹ�����(����)
 * ZB22
 * @author WB
 * 2009-1-5 15:33:07
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
		return nc.ui.eh.pub.PubTools.getSPCButton();
	}

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO �Զ����ɷ������
        return IBillType.eh_h0150305;
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO �Զ����ɷ������
        return new String[]{
        		PubBillVO.class.getName(),
                StockApplyVO.class.getName(),
                StockApplyBVO.class.getName()
        };
    }


    @Override
	public String getChildPkField() {
        // TODO �Զ����ɷ������
        return "pk_apply_b";
    }


    /* ���� Javadoc��
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO �Զ����ɷ������
        return "pk_apply";
    }
    

}
