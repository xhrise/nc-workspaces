/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.stock.h0150125;

import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 功能:超量采购计划审批
 * ZB17
 * @author WB
 * 2008-12-25 11:18:27
 *
 */
public class ClientUI extends AbstractClientUI {

	public ClientUI() {
		super();
	}

	/**
	 * @param arg0
	 */
	public ClientUI(Boolean arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 */
	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.trade.manage.BillManageUI#createController()
	 */
	@Override
	protected AbstractManageController createController() {
		// TODO 自动生成方法存根
		return new ClientCtrl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.manage.BillManageUI#createEventHandler()
	 */
	@Override
	public ManageEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		return new AbstractSPEventHandler(this, this.getUIControl());
	}

	@Override
	public void setDefaultData() throws Exception {
		super.setDefaultData();
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
	}

	/*
	 * 注册自定义按钮 2008-04-02
	 */
	@Override
	protected void initPrivateButton() {
		super.initPrivateButton();
	}


}
