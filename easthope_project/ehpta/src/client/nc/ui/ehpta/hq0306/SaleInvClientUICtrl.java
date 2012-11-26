package nc.ui.ehpta.hq0306;

import nc.ui.ehpta.hq0305.ClientUICtrl;
import nc.vo.ehpta.hq0306.SaleinvBalanceBVO;
import nc.vo.ehpta.hq0306.SaleinvBalanceHVO;
import nc.vo.trade.pub.HYBillVO;

public class SaleInvClientUICtrl extends ClientUICtrl {
	@Override
	public String getBillType() {
		return "HQ17";
	}
	
	@Override
	public String[] getBillVoName() {
		return new String[]{
			HYBillVO.class.getName() ,
			SaleinvBalanceHVO.class.getName() , 
			SaleinvBalanceBVO.class.getName()
		};
	}
	
	
}
