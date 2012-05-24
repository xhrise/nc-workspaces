package nc.ui.eh.stock.h0150225;

/**
 * 采购决策(审批)
 * ZB19	
 * @author wangbing
 * 2008-12-29 8:57:25
 */

import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.stock.h0150220.ClientBaseBD;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.IBillStatus;


public class ClientUI extends MultiChildBillManageUI {
	

	public ClientUI() {
		super();
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	@Override
	protected ManageEventHandler createEventHandler() {
		return new AbstractSPEventHandler(this, this.getUIControl());
	}
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBaseBD();
	}
	@Override
	protected void initSelfData() {
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);	
	     getBillCardWrapper().initHeadComboBox("invtype",ICombobox.CG_DECISION, true);
	     getBillListWrapper().initHeadComboBox("invtype",ICombobox.CG_DECISION, true);	
	     getBillCardWrapper().initHeadComboBox("judge",ICombobox.CG_HQ, true);
	     getBillListWrapper().initHeadComboBox("judge",ICombobox.CG_HQ, true);	
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
	public void setDefaultData() throws Exception {
		
	}

  
}