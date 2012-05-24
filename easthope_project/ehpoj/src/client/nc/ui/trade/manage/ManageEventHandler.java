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
 * �������İ�ť�������� �������ڣ�(2004-1-9 11:55:11)
 * 
 * @author�����ھ�
 */
public class ManageEventHandler extends BillEventHandler {
	/**
	 * ManageButtonController ������ע�⡣ 
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
	 * ����ģ���װ��ķ������� �������ڣ�(2004-1-6 22:29:36)
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	protected BillCardPanelWrapper getBillCardPanelWrapper() {
		return getBillManageUI().getBillCardWrapper();
	}

	/*
	 * ���� Javadoc��
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
	 * �˴����뷽��˵���� �������ڣ�(2004-1-9 8:37:34)
	 * 
	 * @return nc.ui.trade.pub.BillCardUI
	 */
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

	/**
	 * Bill��Ӧ�Ļ�������,CARD,LIST�������ش˷����� �������ڣ�(2004-1-7 8:44:06)
	 * 
	 * @return nc.ui.trade.buffer.BillUIBuffer
	 */
	protected nc.ui.trade.buffer.BillUIBuffer getBufferData() {
		return getBillManageUI().getBufferData();
	}

	/**
	 * ��ó������������� �������ڣ�(2004-1-7 11:42:27)
	 * 
	 * @return nc.ui.trade.controller.ICardController
	 */
	protected AbstractManageController getUIManageController() {
		return (AbstractManageController) getUIController();
	}

	/**
	 * Action��ť���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	public void onBoActionElse(ButtonObject bo) throws Exception {
		super.onBoActionElse(bo);
		getBillManageUI().updateListVo();
	}

	/**
	 * �������ӵĴ��� �������ڣ�(2002-12-23 12:43:15)
	 */
	public void onBoAdd(ButtonObject bo) throws Exception {
		if (getBillManageUI().isListPanelSelected())
			getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		super.onBoAdd(bo);
	}

	/**
	 * ��ťm_boAudit���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	public void onBoAudit() throws Exception {
		super.onBoAudit();
		getBillManageUI().updateListVo();
	}

	/**
	 * ��ťm_boCancelAudit���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoCancelAudit() throws Exception {
		super.onBoCancelAudit();
		getBillManageUI().updateListVo();
	}

	/**
	 * ��ťm_boCard���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoCard() throws Exception {

		getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		getBufferData().updateView();
		
	}

	/**
	 * ��ťm_boCommit���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoCommit() throws Exception {
		super.onBoCommit();
		getBillManageUI().updateListVo();
	}

	/**
	 * ��ťm_boCopy���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoCopy() throws Exception {
		getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		super.onBoCopy();
	}

	/**
	 * ��ťm_boDel���ʱִ�еĶ���,���б�Ҫ���븲��. ���ݵ����ϴ���
	 */
	protected void onBoDel() throws Exception {
		super.onBoDel();
		getBillManageUI().updateListVo();
	}

	/**
	 * ��ťm_boEdit���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoEdit() throws Exception {
		if (getBillManageUI().isListPanelSelected()) {
			getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
			getBufferData().updateView();
		}
		super.onBoEdit();
	}

	/**
	 * ��ťm_boRefresh���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoRefresh() throws Exception {
		//���ˢ�µ���һ���Ѿ���ɾ���������ݣ���ô�����û���ѡ������ڻ���Ĵ����˽����������ɾ����
		int count_before_refresh = getBufferData().getVOBufferSize();
		super.onBoRefresh();
		int count_after_refresh = getBufferData().getVOBufferSize();
		if(count_before_refresh!=count_after_refresh) //˵����İ������ݴӽ���ͻ�����ɾ����
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
	 * ��ťm_boReturn���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoReturn() throws Exception {
		getBillManageUI().setCurrentPanel(BillTemplateWrapper.LISTPANEL);
		getBufferData().updateView();
		//����ģ��Ķ���ԭ���ĵ��õ�isCellditableû�б�Ҫ���ҿ��ܳ���
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
		//��������б���棬ʹ��ListPanelPRTS����Դ
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
		// ����ǿ�Ƭ���棬ʹ��CardPanelPRTS����Դ
		else{
			super.onBoPrint();
		}
	}	

	/**
	 * ���������Ĳ������� �������ڣ�(2004-4-1 10:11:58)
	 */
	protected void setAddNewOperate(boolean isAdding,
			AggregatedValueObject billVO) throws Exception {
		super.setAddNewOperate(isAdding, billVO);
		getBillManageUI().setSaveListData(isAdding);
	}
	//�����������ʹ����BillManagerUI���ܹ����õõ�
	@Override
	protected void onBoSave() throws Exception {
		super.onBoSave();
	}
	
	
}