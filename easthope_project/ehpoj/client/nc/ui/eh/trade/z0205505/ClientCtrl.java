package nc.ui.eh.trade.z0205505;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0205505.FirstdiscountBVO;
import nc.vo.eh.trade.z0205505.FirstdiscountVO;


/**
 * 功能 一次折扣管理
 * @author 洪海
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
        int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
        		ISHSHConst.CARD_BUTTONS_M,
        		new int[] {IEHButton.GENRENDETAIL,IEHButton.STOCKCHANGE},
                0);//modify by houcq 2010-11-30取消一次折扣终止单据按钮
        return btns;
    }

    @Override
	public int[] getListButtonAry() {
       return nc.ui.eh.button.ButtonTool.insertButtons(
               new int[] {},ISHSHConst.LIST_BUTTONS_M, 0);
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillType()
     */
    @Override
	public String getBillType() {
        // TODO 自动生成方法存根
        return IBillType.eh_z0205505;
    }

    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
     */
    @Override
	public String[] getBillVoName() {
        // TODO 自动生成方法存根
        return new String[]{
            PubBillVO.class.getName(),
            FirstdiscountVO.class.getName(),
            FirstdiscountBVO.class.getName()
        };
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
     */
    @Override
	public String getChildPkField() {
        // TODO 自动生成方法存根
        return "pk_firstdiscount_b";
    }


    /* （非 Javadoc）
     * @see nc.ui.trade.controller.IControllerBase#getPkField()
     */
    @Override
	public String getPkField() {
        // TODO 自动生成方法存根
        return "pk_firstdiscount";
    }

}
