package nc.ui.eh.trade.z0207010;
/**
 * 功能 销售退回
 */
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0207010.BackbillBVO;
import nc.vo.eh.trade.z0207010.BackbillVO;


public class ClientCtrl extends AbstractCtrl {

    @Override
	public int[] getCardButtonAry() {        
    	int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                new int[] { IBillButton.Busitype},
                ISHSHConst.CARD_BUTTONS_M, 0);
    	int[] btnss=nc.ui.eh.button.ButtonTool.insertButtons(btns,new int[]{IEHButton.BusinesBtn},0);
    	
    	return btnss;
    }

    @Override
	public int[] getListButtonAry() {
       return nc.ui.eh.button.ButtonTool.insertButtons(
               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
    }
    
	@Override
	public String getBillType() {
        return IBillType.eh_z0207010;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            BackbillVO.class.getName(),
	            BackbillBVO.class.getName()    
	        };
	    }

	@Override
	public String getChildPkField() {
		return "pk_backbill_b";
	}

	@Override
	public String getPkField() {
		return "pk_backbill";
	}

}
