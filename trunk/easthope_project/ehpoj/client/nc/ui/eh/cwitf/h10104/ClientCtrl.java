
package nc.ui.eh.cwitf.h10104;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cwitf.h10104.ItfDatasourceVO;
import nc.vo.eh.pub.PubBillVO;

/**
 * U8数据源配置
 * @author 王兵
 * 2008-7-8 14:44:46
 */
public class ClientCtrl extends AbstractManageController {

	public ClientCtrl() {
		super();
	}
	public String[] getCardBodyHideCol() {
		return null;
	}
	public int[] getCardButtonAry() {
		return new int[]{
				   IBillButton.Add,
	               IBillButton.Edit,
                   IBillButton.Delete,
				   IBillButton.Save,
				   IEHButton.CREATEVOUCHER,					//测试连接
				   IBillButton.Cancel,
				   IBillButton.Return,
				   IBillButton.Refresh
	              };
	}
	public boolean isShowCardRowNo() {
		return true;
	}
	public boolean isShowCardTotal() {
		return false;
	}
	public String getBillType() {
		return IBillType.eh_h10104;
	}
	
	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
				ItfDatasourceVO.class.getName(),
				ItfDatasourceVO.class.getName(),
		};
	}
	public String getBodyCondition() {
		return null;
	}
	public String getBodyZYXKey() {
		return null;
	}
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}
	public String getChildPkField() {
		return null;
	}
	public String getHeadZYXKey() {
		return null;
	}
	public String getPkField() {
		return "pk_datasource";
	}
	public Boolean isEditInGoing() throws Exception {
		return null;
	} 
	public boolean isExistBillStatus() {
		return false;
	}
	public boolean isLoadCardFormula() {
		return false;
	}
	public String[] getListBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int[] getListButtonAry() {
	       return new int[]{
	    		   IBillButton.Query, IBillButton.Add, IBillButton.Edit, IBillButton.Refresh
	       };
	    }
	public String[] getListHeadHideCol() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isShowListRowNo() {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean isShowListTotal() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
