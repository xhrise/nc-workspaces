
package nc.ui.eh.trade.z0206005;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0206005.OrderBVO;
import nc.vo.eh.trade.z0206005.OrderVO;

/**
 * 功能 销售定单
 * @author 洪海
 * 2008-04-08
 */
public class ClientCtrl extends AbstractCtrl {

    public ClientCtrl() {
        super();
    }

    @Override
	public String[] getCardBodyHideCol() {
        return null;
    }

    @Override
	public int[] getCardButtonAry() {        
    	int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                new int[] { IBillButton.Busitype},
                ISHSHConst.CARD_BUTTONS, 0);
        int[] btnss = nc.ui.eh.button.ButtonTool.insertButtons(
        		btns,
        		new int[]{IEHButton.BusinesBtn}, 0);
        
    	return btns;//modify by houcq 2010-11-30取消销售订单终止单据按钮
    }
   
    @Override
	public int[] getListButtonAry() {
        return nc.ui.eh.button.ButtonTool.insertButtons(
        new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
    }

    @Override
	public String getBillType() {
        return IBillType.eh_z0206005;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            OrderVO.class.getName(),
            OrderBVO.class.getName()
        };
    }

    @Override
	public int getBusinessActionType() {
        return IBusinessActionType.PLATFORM;
    }

    @Override
	public String getChildPkField() {
        return "pk_order_b";
    }

    @Override
	public String getPkField() {
        return "pk_order";
    }

}
