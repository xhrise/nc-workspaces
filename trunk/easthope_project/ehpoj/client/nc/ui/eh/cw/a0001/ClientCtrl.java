package nc.ui.eh.cw.a0001;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.a0001.ArapSfkfsBVO;
import nc.vo.eh.cw.a0001.ArapSfkfsVO;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;

public class ClientCtrl extends AbstractCtrl {

	public String getBillType() {
		return "ZB99";
	}

	public String[] getBillVoName() {
		return new String []{
				PubBillVO.class.getName(),	
				ArapSfkfsVO.class.getName(),
				ArapSfkfsBVO.class.getName()
		};
	}

	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Query,
				IBillButton.Line,
				IBillButton.Save,
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Delete,
				IBillButton.Return,
				IBillButton.Cancel,
				IBillButton.Refresh
				
		};
	}

	public int[] getListButtonAry() {
		return new int[]{
				IBillButton.Query,
				IBillButton.Save,
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Delete,
				IBillButton.Return,
				IBillButton.Cancel,
				IBillButton.Refresh
				
		};
	}

	public String getPkField() {
		return "pk_id";
	}
	public String getChildPkField() {
		return "pk_id_b";
	}
	
	   @Override
		public int getBusinessActionType() {
	        return IBusinessActionType.BD;
	    }
	   
	    @Override
		public boolean isExistBillStatus() {
	        return true;
	    }

	    @Override
		public boolean isLoadCardFormula() {
	        return true;
	    }
	    
	    
	
}
