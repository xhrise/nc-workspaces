package nc.ui.ehpta.hq010401;

import java.awt.BorderLayout;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.valid.Validata;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.filesystem.FileManageUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IBillStatus;

/**
 * 
 * 该类是一个抽象类，主要目的是生成按钮事件处理的框架
 * 
 * @author author
 * @version tempProject version
 */

public class EventHandler extends ManageEventHandler {

	public EventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	public final String[] getTableCodes() {
		return new String[]{"ehpta_sale_contract_bs"};
	}

	protected void onBoElse(int intBtn) throws Exception {
		switch(intBtn) {
			case DefaultBillButton.DOCUMENT :
				
				documentManage();
				break;
				
			case DefaultBillButton.OpenOrClose :
				
				onOpenOrClose();
				break;
				
			default : 
				
				break;
		}
	}
	
	private final void onOpenOrClose() throws Exception {
		
		int currRow = getBufferData().getCurrentRow();
		
		AggregatedValueObject  billVO = getBufferData().getCurrentVO();
		if(billVO != null ) {
			
			if(billVO.getParentVO().getPrimaryKey() != null && !"".equals(billVO.getParentVO().getPrimaryKey())) {
				
				UFBoolean openOrCloseFlag = (UFBoolean) billVO.getParentVO().getAttributeValue("close_flag");
				if(openOrCloseFlag == null || "N".equals(openOrCloseFlag.toString()) || !openOrCloseFlag.booleanValue()) {
					
					billVO.getParentVO().setAttributeValue("close_flag", new UFBoolean(true));
					HYPubBO_Client.update((SuperVO) billVO.getParentVO());
					
				} else {
					
					billVO.getParentVO().setAttributeValue("close_flag", new UFBoolean(false));
					HYPubBO_Client.update((SuperVO) billVO.getParentVO());
					
				}
					
				billVO = HYPubBO_Client.queryBillVOByPrimaryKey(getUIController().getBillVoName(), billVO.getParentVO().getPrimaryKey());
				getBufferData().setCurrentVO(billVO);
				updateBuffer();
				getBufferData().setCurrentRow(currRow);
				
			}
			
		}
	}
	
	
	private final void documentManage() throws BusinessException {
		try {
			
			AggregatedValueObject aggVO = null;
			
			if(getBillUI().getBillOperate() == IBillOperate.OP_ADD)
				aggVO = getBillCardPanelWrapper().getBillVOFromUI();
			else
				aggVO = getBufferData().getCurrentVO();

			if (aggVO == null || aggVO.getParentVO() == null) {
				getBillUI().showErrorMessage("请至少选择一条记录...");
				return ;
			}
			
			String ids = aggVO.getParentVO().getPrimaryKey();
			
			if(ids == null || "".equals(ids)) {
				getBillUI().showErrorMessage("新增时不能进行文件上传操作...");
				return ;
			}
				
			
			FileManageUI fileui = new FileManageUI();
			fileui.setRootDirStr(ids);
			fileui.setTreeRootVisible(false);
			UIDialog dlg = new UIDialog(getBillUI());
			dlg.getContentPane().setLayout(new BorderLayout());
			dlg.getContentPane().add(fileui, "Center");
			dlg.setResizable(true);
			dlg.setSize(600, 400);
			dlg.setTitle(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"tm502comm", "UPPTM502Comm-000050")/* @res"文档管理" */);
			if (dlg.showModal() == UIDialog.ID_CANCEL)
				return;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void onBoSave() throws Exception {
		
		Validata.saveValidataIsNull(getBillCardPanelWrapper().getBillCardPanel() , getBillCardPanelWrapper().getBillVOFromUI() , getTableCodes()  );
		
		super.onBoSave();
	}
	
	@Override
	protected void onBoCancelAudit() throws Exception {
		
		cancleAuditValid(getBufferData().getCurrentVO());
		
		super.onBoCancelAudit();
	}
	
	@Override
	protected void onBoDelete() throws Exception {
		
		// 删除合同前验证合同引用。被引用合同无法被删除。
		// add by river for 2012-10-12
		Validata.cancleAuditValid(getBufferData().getCurrentVO());
		
		super.onBoDelete();
	}
	
	public final void cancleAuditValid(AggregatedValueObject currVO) throws Exception {
		if(currVO != null && currVO.getParentVO() != null) {
			Object pk_contract = currVO.getParentVO().getAttributeValue("pk_contract");
			Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select count(1) from so_sale where pk_contract = '"+pk_contract+"' and nvl(dr,0)=0 ", new ColumnProcessor());
			
			// 合同收款后可以进行弃审操作。
//			count += (Integer) UAPQueryBS.iUAPQueryBS.executeQuery(" select count(1) from arap_djzb where zyx6 = '"+pk_contract+"' and nvl(dr,0)=0 ", new ColumnProcessor());
			
			if(count > 0)
				throw new Exception("当前合同已被引用，不能进行弃审操作！");
			
			
		}
	}
	
	@Override
	protected void onBoCard() throws Exception {
		super.onBoCard();
		
		afterOnButton();
	}
	
	@Override
	public void onButton(ButtonObject bo) {

		super.onButton(bo);
		
		afterOnButton();
		
	}
	
	/**
	 * 点击按钮后的处理
	 */
	private final void afterOnButton() {
		
		if(getBillUI().getBillOperate() == IBillOperate.OP_ADD || getBillUI().getBillOperate() == IBillOperate.OP_EDIT || getBillUI().getBillOperate() == IBillOperate.OP_REFADD) {
			getButtonManager().getButton(DefaultBillButton.OpenOrClose).setEnabled(false);
			getButtonManager().getButton(DefaultBillButton.DOCUMENT).setEnabled(false);
			
		} else {
			
			AggregatedValueObject  vo = getBufferData().getCurrentVO();
			
			try {
				if(vo != null && vo.getParentVO() != null && vo.getParentVO().getPrimaryKey() != null) {
					if((Integer)vo.getParentVO().getAttributeValue("vbillstatus") == IBillStatus.CHECKPASS)
						getButtonManager().getButton(DefaultBillButton.OpenOrClose).setEnabled(true);
					else 
						getButtonManager().getButton(DefaultBillButton.OpenOrClose).setEnabled(false);
				} else 
					getButtonManager().getButton(DefaultBillButton.OpenOrClose).setEnabled(false);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e, this.getClass(), "afterOnButton");
			}
			
			getButtonManager().getButton(DefaultBillButton.DOCUMENT).setEnabled(true);
			
		}
		
		getBillUI().updateButtons();
		
	}
	
}


