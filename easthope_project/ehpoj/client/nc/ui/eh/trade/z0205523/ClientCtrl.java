package nc.ui.eh.trade.z0205523;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0205523.TradeLsdiscBVO;
import nc.vo.eh.trade.z0205523.TradeLsdiscVO;

/**
 * 
 * ���ܣ���ʱ�ۿ�
 * ʱ�䣺2009-11-18����06:14:28
 * ���ߣ���־Զ
 */
public class ClientCtrl extends AbstractCtrl {

	
	public String getBillType() {
        return IBillType.eh_z0205523;
    }

	public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            TradeLsdiscVO.class.getName(),
	            TradeLsdiscBVO.class.getName()    
	        };
	    }

	public String getChildPkField() {
		return "pk_trade_lsdisc_b";
	}

	public String getPkField() {
		return "pk_trade_lsdisc";
	}

	public int getBusinessActionType() {
		return IBusinessActionType.PLATFORM;
	}

	public int[] getCardButtonAry() {        
            int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
                    new int[] { },
                    ISHSHConst.CARD_BUTTONS_M, 0);
            int[] btnss=nc.ui.eh.button.ButtonTool.insertButtons(
            		btns,
            		new int[]{IEHButton.GENRENDETAIL}, 0);//������ϸ
            int[] btnsss=nc.ui.eh.button.ButtonTool.insertButtons(
            		new int[] { IBillButton.Busitype},
            		btnss, 0);//ҵ������
            return btnsss;
        }

	public int[] getListButtonAry() {
           return nc.ui.eh.button.ButtonTool.insertButtons(
                   new int[] {},ISHSHConst.LIST_BUTTONS_M, 0);
        }


}
