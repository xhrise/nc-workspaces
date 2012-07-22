package nc.ui.yto.org;

import nc.ui.pub.body.AbstractController;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.yto.org.OrganizeVO;

public class OrgController extends AbstractController {

	public int[] getCardButtonAry() {
		return new int[]{
			IBillButton.Query , 
			IBillButton.Delete ,
			IBillButton.Refresh
		};
	}

	public String getBillType() {
		return "ORGA";
	}

	public String[] getBillVoName() {
		return new String[]{HYBillVO.class.getName() , OrganizeVO.class.getName() , OrganizeVO.class.getName()};
	}

	public String getPkField() {
		return "pk_organizational";
	}

}
