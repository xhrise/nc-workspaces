package nc.ui.eh.stock.z0151010;


import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.trade.pub.IBillStatus;



/**
 * 功能说明：收货通知单
 * @author 王明
 * 2008-03-24 下午04:03:18
 */
public class ClientUI extends AbstractClientUI {

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	@Override
	public ManageEventHandler createEventHandler() {
		return new AbstractSPEventHandler(this, this.getUIControl());
	}
	@Override
	public void setDefaultData() throws Exception {
		 getBillCardPanel().getHeadItem("vbillstatus").setValue(Integer.toString(IBillStatus.FREE));
		   
		getBillCardPanel().setHeadItem("receiptdate", _getDate());		
		super.setDefaultData();
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
	}
	@Override
	protected void initSelfData() {
		//审批流
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     super.initSelfData();
	}
	

}
