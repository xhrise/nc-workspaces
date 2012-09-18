package nc.ui.ehpta.hq010403;

import java.awt.BorderLayout;

import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.valid.Validata;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.filesystem.FileManageUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * 
 * ������һ�������࣬��ҪĿ�������ɰ�ť�¼�����Ŀ��
 * 
 * @author author
 * @version tempProject version
 */

public class EventHandler extends ManageEventHandler {

	public EventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case DefaultBillButton.DOCUMENT:

			documentManage();

			break;

		default:

			break;
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
	 * �����ť��Ĵ���
	 */
	private final void afterOnButton() {
		
		if(getBillUI().getBillOperate() == IBillOperate.OP_ADD || getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {
			getButtonManager().getButton(DefaultBillButton.DOCUMENT).setEnabled(false);
		} else {
			getButtonManager().getButton(DefaultBillButton.DOCUMENT).setEnabled(true);
		} 
		
		getBillUI().updateButtons();
		
	}
	
	private final void documentManage() throws BusinessException {
		try {

			AggregatedValueObject aggVO = null;

			if (getBillUI().getBillOperate() == IBillOperate.OP_ADD)
				aggVO = getBillCardPanelWrapper().getBillVOFromUI();
			else
				aggVO = getBufferData().getCurrentVO();

			if (aggVO == null || aggVO.getParentVO() == null) {
				getBillUI().showErrorMessage("������ѡ��һ����¼...");
				return;
			}

			String ids = aggVO.getParentVO().getPrimaryKey();

			if (ids == null || "".equals(ids)) {
				getBillUI().showErrorMessage("����ʱ���ܽ����ļ��ϴ�����...");
				return;
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
					"tm502comm", "UPPTM502Comm-000050")/* @res"�ĵ�����" */);
			if (dlg.showModal() == UIDialog.ID_CANCEL)
				return;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void onBoSave() throws Exception {
		
		Validata.saveValidataIsNull(getBillCardPanelWrapper().getBillCardPanel() , getBillCardPanelWrapper().getBillVOFromUI() , (String[]) null);
		
		super.onBoSave();
		
	}
	
	@Override
	protected void onBoCancelAudit() throws Exception {

		AggregatedValueObject currAggVO = getBufferData().getCurrentVO();
		if(currAggVO != null && currAggVO.getParentVO() != null) {
			String def2 = (String) currAggVO.getParentVO().getAttributeValue("def2");
			if(def2 != null && "Y".equals(def2))
				throw new Exception ("��ǰ��¼Ϊ��ʽ���ɣ����ܽ������������");
		}
		
		super.onBoCancelAudit();
	}
	
	public IBusinessController getBusiAction() {
		return getBusinessAction();
	}

}