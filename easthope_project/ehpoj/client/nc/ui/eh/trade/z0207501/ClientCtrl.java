package nc.ui.eh.trade.z0207501;


import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0207501.InvoiceBVO;
import nc.vo.eh.trade.z0207501.InvoiceVO;

/**销售发票
 * @author 王明
 * 2008-03-24 下午04:03:18
 */
public class ClientCtrl extends AbstractCtrl {


	
    @Override
	public int[] getCardButtonAry() {        
    	int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                new int[] { IBillButton.Busitype},
                ISHSHConst.CARD_BUTTONS_M, 0);
    	int[] btns1 = nc.ui.eh.button.ButtonTool.insertButtons(btns,
                new int[] { IEHButton.BusinesBtn},
                0);
    	return btns;//modify by houcq 2010-11-30取消销售发票终止单据按钮
    }

	 @Override
	public int[] getListButtonAry() {
	       return nc.ui.eh.button.ButtonTool.insertButtons(
	               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
	    }



    @Override
	public String getBillType() {
        return IBillType.eh_z0207501;
    }


    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            InvoiceVO.class.getName(),
            InvoiceBVO.class.getName()
            
        };
    }
    @Override
	public String getChildPkField() {
        return "pk_invoice_b";
    }

    @Override
	public String getPkField() {
        return "pk_invoice";
    }


}
