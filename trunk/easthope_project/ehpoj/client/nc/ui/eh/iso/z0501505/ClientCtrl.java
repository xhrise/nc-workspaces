package nc.ui.eh.iso.z0501505;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.iso.z0501505.StockCheckreportBVO;
import nc.vo.eh.iso.z0501505.StockSampleVO;
import nc.vo.eh.pub.PubBillVO;

/** 
 * ˵���������� 
 * @author ����Դ 
 * ʱ�䣺2008-4-14
 */
public class ClientCtrl extends AbstractCtrl {

	@Override
	public int[] getCardButtonAry() {        
        int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                new int[] { IBillButton.Busitype},
                ISHSHConst.CARD_BUTTONS_M, 0);
        int[] btns1= nc.ui.eh.button.ButtonTool.insertButtons(new int[] { IEHButton.BusinesBtn}, btns, 14);
        return btns;//modify houcq 2010-11-29ȡ��ԭ�ϼ�����뵥����ֹ��ť
    }

    @Override
	public int[] getListButtonAry() {
       return nc.ui.eh.button.ButtonTool.insertButtons(
               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
    }
	
	@Override
	public String getBillType() {
        return IBillType.eh_z0501505;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            StockSampleVO.class.getName(),
	            StockCheckreportBVO.class.getName()    
	        };
	    }

	@Override
	public String getChildPkField() {
		return "pk_checkreport_b";
	}

	@Override
	public String getPkField() {
		return "pk_sample";
	}

	
}
