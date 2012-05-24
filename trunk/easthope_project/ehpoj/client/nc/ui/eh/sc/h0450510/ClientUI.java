package nc.ui.eh.sc.h0450510;


import nc.bs.eh.sc.h0450510.ClientUICheckRuleGetter;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 功能说明：班组档案
 * @author 王明
 * 2008-05-07 下午04:03:18
 */
public class ClientUI extends BillManageUI {
    
	public ClientUI() {
		super();
	}
	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().pk_corp);
	}
	 public boolean isExistBillStatus() {
	        return false;
	    }
	 public int getBusinessActionType() {
	        return nc.ui.trade.businessaction.IBusinessActionType.BD;
	    }
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
	}
	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0, int arg1) throws Exception {
		
	}
	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
	}
	@Override
	protected void initSelfData() {
	}
	@Override
	public Object getUserObject() {
		return new ClientUICheckRuleGetter();
	}

}
