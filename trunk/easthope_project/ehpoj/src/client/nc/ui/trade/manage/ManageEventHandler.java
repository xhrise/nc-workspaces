package nc.ui.trade.manage;

import nc.newinstall.env.NcSetupCmdLine;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UITable;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bill.BillEventHandler;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.pub.BillDirectPrint;
import nc.ui.trade.pub.ListPanelPRTS;
import nc.vo.pub.AggregatedValueObject;

/**
 * 管理界面的按钮控制器。 创建日期：(2004-1-9 11:55:11)
 * 
 * @author：樊冠军
 */
public class ManageEventHandler extends BillEventHandler {
	/**
	 * ManageButtonController 构造子注解。 
	 * 
	 * @param billUI
	 *            nc.ui.trade.pub.AbstractBillUI
	 * @param control
	 *            nc.ui.trade.controller.IControllerBase
	 */
	public ManageEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	/**
	 * 单据模版包装类的方法重载 创建日期：(2004-1-6 22:29:36)
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	protected BillCardPanelWrapper getBillCardPanelWrapper() {
		return getBillManageUI().getBillCardWrapper();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.trade.bill.BillEventHandler#onBoDirectPrint()
	 */
	protected void onBoDirectPrint() throws Exception {
		if (getBillManageUI().isListPanelSelected()){
	    	BillDirectPrint print = new BillDirectPrint(getBillManageUI().getBillListPanel(),getBillManageUI().getBillListPanel().getBillListData().getTitle());
	    	print.onPrint();
			
		}else{
			super.onBoDirectPrint();
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-9 8:37:34)
	 * 
	 * @return nc.ui.trade.pub.BillCardUI
	 */
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

	/**
	 * Bill对应的缓存数据,CARD,LIST必须重载此方法。 创建日期：(2004-1-7 8:44:06)
	 * 
	 * @return nc.ui.trade.buffer.BillUIBuffer
	 */
	protected nc.ui.trade.buffer.BillUIBuffer getBufferData() {
		return getBillManageUI().getBufferData();
	}

	/**
	 * 获得抽象管理控制器。 创建日期：(2004-1-7 11:42:27)
	 * 
	 * @return nc.ui.trade.controller.ICardController
	 */
	protected AbstractManageController getUIManageController() {
		return (AbstractManageController) getUIController();
	}

	/**
	 * Action按钮点击时执行的动作,如有必要，请覆盖.
	 */
	public void onBoActionElse(ButtonObject bo) throws Exception {
		super.onBoActionElse(bo);
		getBillManageUI().updateListVo();
	}

	/**
	 * 单据增加的处理 创建日期：(2002-12-23 12:43:15)
	 */
	public void onBoAdd(ButtonObject bo) throws Exception {
		if (getBillManageUI().isListPanelSelected())
			getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		super.onBoAdd(bo);
	}

	/**
	 * 按钮m_boAudit点击时执行的动作,如有必要，请覆盖.
	 */
	public void onBoAudit() throws Exception {
		super.onBoAudit();
		getBillManageUI().updateListVo();
	}

	/**
	 * 按钮m_boCancelAudit点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoCancelAudit() throws Exception {
		super.onBoCancelAudit();
		getBillManageUI().updateListVo();
	}

	/**
	 * 按钮m_boCard点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoCard() throws Exception {

		getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		getBufferData().updateView();
		
	}

	/**
	 * 按钮m_boCommit点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoCommit() throws Exception {
		super.onBoCommit();
		getBillManageUI().updateListVo();
	}

	/**
	 * 按钮m_boCopy点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoCopy() throws Exception {
		getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		super.onBoCopy();
	}

	/**
	 * 按钮m_boDel点击时执行的动作,如有必要，请覆盖. 单据的作废处理
	 */
	protected void onBoDel() throws Exception {
		super.onBoDel();
		getBillManageUI().updateListVo();
	}

	/**
	 * 按钮m_boEdit点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoEdit() throws Exception {
		if (getBillManageUI().isListPanelSelected()) {
			getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
			getBufferData().updateView();
		}
		super.onBoEdit();
	}

	/**
	 * 按钮m_boRefresh点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoRefresh() throws Exception {
		//如果刷新的是一个已经被删除的脏数据，那么根据用户的选择可能在基类的代码了将这个脏数据删除了
		int count_before_refresh = getBufferData().getVOBufferSize();
		super.onBoRefresh();
		int count_after_refresh = getBufferData().getVOBufferSize();
		if(count_before_refresh!=count_after_refresh) //说明真的把脏数据从界面和缓存中删除了
		{
			if(count_after_refresh==0)
			{
				getBillManageUI().setListHeadData(null);
			}
			else
			{
				getBillManageUI().setListHeadData(getBufferData().getAllHeadVOsFromBuffer());
			}
		}
		else
			getBillManageUI().updateListVo();
	}

	/**
	 * 按钮m_boReturn点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoReturn() throws Exception {
		getBillManageUI().setCurrentPanel(BillTemplateWrapper.LISTPANEL);
		getBufferData().updateView();
		//单据模板改动后，原来的调用的isCellditable没有必要，且可能出错
		if( getBufferData().getCurrentRow()!=-1)
		{
			int selectedRow = getBufferData().getCurrentRow();
			UITable table = getBillManageUI().getBillListPanel()
			.getHeadTable();
			table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
			table.scrollRectToVisible(table.getCellRect(getBufferData()
					.getCurrentRow(), 0, true));
			
		}
	}
	
	protected void onBoPrint() throws Exception {
		//　如果是列表界面，使用ListPanelPRTS数据源
		if( getBillManageUI().isListPanelSelected() ){
			nc.ui.pub.print.IDataSource dataSource = new ListPanelPRTS(getBillUI()
					._getModuleCode(),((BillManageUI) getBillUI()).getBillListPanel());
			nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
					dataSource);
			print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
					._getModuleCode(), getBillUI()._getOperator(), getBillUI()
					.getBusinessType(), getBillUI().getNodeKey());
			if (print.selectTemplate() == 1)
				print.preview();
		}
		// 如果是卡片界面，使用CardPanelPRTS数据源
		else{
			super.onBoPrint();
		}
	}	

	/**
	 * 设置新增的操作处理。 创建日期：(2004-4-1 10:11:58)
	 */
	protected void setAddNewOperate(boolean isAdding,
			AggregatedValueObject billVO) throws Exception {
		super.setAddNewOperate(isAdding, billVO);
		getBillManageUI().setSaveListData(isAdding);
	}
	//重载这个方法使得在BillManagerUI中能够调用得到
	@Override
	protected void onBoSave() throws Exception {
		super.onBoSave();
	}
	
	
}