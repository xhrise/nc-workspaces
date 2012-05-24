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
 * 功能：临时折扣
 * 时间：2009-11-18下午06:14:28
 * 作者：张志远
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
            		new int[]{IEHButton.GENRENDETAIL}, 0);//生成明细
            int[] btnsss=nc.ui.eh.button.ButtonTool.insertButtons(
            		new int[] { IBillButton.Busitype},
            		btnss, 0);//业务类型
            return btnsss;
        }

	public int[] getListButtonAry() {
           return nc.ui.eh.button.ButtonTool.insertButtons(
                   new int[] {},ISHSHConst.LIST_BUTTONS_M, 0);
        }


}
