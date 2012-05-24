package nc.ui.eh.valid;

import nc.ui.eh.body.AbstractController;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.valid.EhValidoperateVO;
import nc.vo.eh.valid.MyBillVO;

/**
 * <b> 列表UI控制器类</b><br>
 *
 * <p>
 *     设置界面按钮，数据，是否平台相关等信息
 * </p>
 * <br>
 *
 * Create on 2006-4-6 16:00:51
 *
 * @author authorName
 * @version tempProject version
 */

public class ClientUICtrl extends AbstractController {

	/**
	 * 设置界面按钮
	 */
	public int[] getCardButtonAry() {
	
		        	        return new int[]{
	             IBillButton.Query,
	             IBillButton.Add,
	             IBillButton.Edit,
	             IBillButton.Delete,
//	             IBillButton.Line,
	             IBillButton.Save,
	             IBillButton.Cancel,
	             IBillButton.Refresh
	        };
   }


	public String getBillType() {
		return "H00199";
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			EhValidoperateVO.class.getName(),
			EhValidoperateVO.class.getName()
		};
	}


	public String getPkField() {
		return "pk_operate";
	}

}
