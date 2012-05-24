package nc.ui.eh.cw.h1101010;

import java.util.ArrayList;
import java.util.List;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.h1101005.ArapFkBVO;
import nc.vo.eh.cw.h1101005.ArapFkVO;
import nc.vo.eh.pub.PubBillVO;


/**
 * ����˵�����������
 * @author ����
 * 2008-05-28 ����02:03:18
 */
public class ClientCtrl extends AbstractCtrl {
	
	  @Override
	public int[] getListButtonAry() {
		// return nc.ui.eh.pub.PubTools.getSPLButton();

		
		
		int[] ListButton = new int[] { IBillButton.Query, IBillButton.Card,
				IBillButton.Refresh  };
		return ListButton;

	}

	@Override
	public int[] getCardButtonAry() {
		// return nc.ui.eh.pub.PubTools.getSPCButton();

		int[] CardButton = new int[] { IBillButton.Refresh, IBillButton.Audit,
				IBillButton.Print, IBillButton.Return, IBillButton.CancelAudit , IBillButton.ApproveInfo };

		return CardButton;
	}
		
	

	 @Override
	public String getBillType() {
	        return IBillType.eh_h1101005;
	    }

	    @Override
		public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            ArapFkVO.class.getName(),
	            ArapFkBVO.class.getName()  
	        };
	    }

	    @Override
		public String getChildPkField() {
	        return "pk_fk_b";
	    }

	    @Override
		public String getPkField() {
	        return "pk_fk";
	    }



}
