
package nc.ui.eh.cw.h1200202;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.h1200202.HjlhsBVO;
import nc.vo.eh.cw.h1200202.HjlhsVO;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;

/**
 * ªÿª˙¡œ∫ÀÀ„
 * @author houcq
 * 2011-06-10 10:41:21 
 */
public class ClientCtrl extends AbstractCtrl {

	public ClientCtrl() {
		super();
	}
	
	public int[] getCardButtonAry() {
				return new int[] { IBillButton.Query,IBillButton.Add, IBillButton.Edit, IBillButton.Delete,
				            IBillButton.Line, IBillButton.Save, IBillButton.Action,IBillButton.Cancel,IBillButton.Return,IBillButton.Refresh,
				            IBillButton.Print,IBillButton.ApproveInfo,IEHButton.Prev,IEHButton.Next,IEHButton.GENRENDETAIL};
	}
	 @Override
		public int[] getListButtonAry() {
		       return nc.ui.eh.button.ButtonTool.insertButtons(
		               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
		    }

	public String getBillType() {
		return IBillType.eh_h1200202;
	}
	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
				HjlhsVO.class.getName(),
				HjlhsBVO.class.getName(),
		};
	}
	public String getChildPkField() {
		return "pk_hjlhs_b";
	}

	public String getPkField() {
		return "pk_hjlhs";
	}	
}
