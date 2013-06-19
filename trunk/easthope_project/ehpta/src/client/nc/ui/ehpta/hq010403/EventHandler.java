package nc.ui.ehpta.hq010403;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.convert.ConvertFunc;
import nc.ui.ehpta.pub.valid.Validata;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.IFuncWindow;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable.SortItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.filesystem.FileManageUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.ehpta.hq010403.AdjustVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

/**
 * 
 * ������һ�������࣬��ҪĿ�������ɰ�ť�¼������Ŀ��
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
				
			case DefaultBillButton.Confirm : 
				onBoConfirm();
				break;
				
			case DefaultBillButton.SelAll : 
				onBoSelAll();
				break;
				
			case DefaultBillButton.SelNone : 
				onBoSelNone();
				break;
				
			case DefaultBillButton.ENABLED :
				onBoEnabled();
				break;
				
			case DefaultBillButton.DISABLED : 
				onBoDisabled();
				break;
				
			default:
	
				break;
		}
	}
	
	protected final void onBoEnabled() throws Exception {
		
		AggregatedValueObject[] selectedVOs = ((ClientUI) getBillUI()).getBillListPanel().getMultiSelectedVOs(HYBillVO.class.getName(), AdjustVO.class.getName(), AdjustVO.class.getName());
		
		if(selectedVOs != null && selectedVOs.length > 0) {
			boolean check = false;
			
			List<AdjustVO> adjustList = new ArrayList<AdjustVO>();
			for(AggregatedValueObject billVO : selectedVOs) {
				String type = ((AdjustVO)billVO.getParentVO()).getType();
				
				if("4".equals(type)) {
					((AdjustVO)billVO.getParentVO()).setDef6("Y");
					adjustList.add((AdjustVO) billVO.getParentVO());
				} else 
					check = true;
				
			}
			
			if(adjustList == null || adjustList.size() == 0) {
				getBillUI().showErrorMessage("��ѡ�������͵ĵ��ݣ�");
				return ;
			}
			
			if(!(getBillUI().showYesNoMessage("ȷ��Ҫ����ѡ�еĽ����") == UIDialog.ID_YES))
				return ;
			
			if(check)
				getBillUI().showWarningMessage("ѡ�еļ�¼�д��ڷǷ��������ͣ���Щ��¼�ڴ����н������ԣ�");
			
			
			HYPubBO_Client.updateAry(adjustList.toArray(new AdjustVO[0]));
			
			CircularlyAccessibleValueObject[] headVOs = getBufferData().getAllHeadVOsFromBuffer();
			if(headVOs != null && headVOs.length > 0 ) {
				List<String> pkList = new ArrayList<String>();
				for(CircularlyAccessibleValueObject headVO : headVOs) {
					pkList.add("'" + headVO.getPrimaryKey() + "'");
				}
				
				AggregatedValueObject[] billVOs = HYPubBO_Client.queryBillVOByCondition(new String[] { HYBillVO.class.getName(), AdjustVO.class.getName() }, " pk_adjust in ("+ConvertFunc.change(pkList.toArray(new String[0]))+") order by vbillno desc ");
				getBufferData().clear();
				getBufferData().addVOsToBuffer(billVOs);
				updateBuffer();
			
			}
		}
		
	}
	
	protected final void onBoDisabled() throws Exception {
		
		AggregatedValueObject[] selectedVOs = ((ClientUI) getBillUI()).getBillListPanel().getMultiSelectedVOs(HYBillVO.class.getName(), AdjustVO.class.getName(), AdjustVO.class.getName());
		
		if(selectedVOs != null && selectedVOs.length > 0) {
			boolean check = false;
			
			List<AdjustVO> adjustList = new ArrayList<AdjustVO>();
			for(AggregatedValueObject billVO : selectedVOs) {
				String type = ((AdjustVO)billVO.getParentVO()).getType();
				
				if("4".equals(type)) {
					((AdjustVO)billVO.getParentVO()).setDef6("N");
					adjustList.add((AdjustVO) billVO.getParentVO());
				} else 
					check = true;
				
			}
			
			if(adjustList == null || adjustList.size() == 0) {
				getBillUI().showErrorMessage("��ѡ�������͵ĵ��ݣ�");
				return ;
			}
			
			if(!(getBillUI().showYesNoMessage("ȷ��Ҫͣ��ѡ�еĽ����") == UIDialog.ID_YES))
				return ;
			
			if(check)
				getBillUI().showWarningMessage("ѡ�еļ�¼�д��ڷǷ��������ͣ���Щ��¼�ڴ����н������ԣ�");
			
			
			HYPubBO_Client.updateAry(adjustList.toArray(new AdjustVO[0]));
			
			CircularlyAccessibleValueObject[] headVOs = getBufferData().getAllHeadVOsFromBuffer();
			if(headVOs != null && headVOs.length > 0 ) {
				List<String> pkList = new ArrayList<String>();
				for(CircularlyAccessibleValueObject headVO : headVOs) {
					pkList.add("'" + headVO.getPrimaryKey() + "'");
				}
				
				AggregatedValueObject[] billVOs = HYPubBO_Client.queryBillVOByCondition(new String[] { HYBillVO.class.getName(), AdjustVO.class.getName() } , " pk_adjust in ("+ConvertFunc.change(pkList.toArray(new String[0]))+") order by vbillno desc ");
				getBufferData().clear();
				getBufferData().addVOsToBuffer(billVOs);
				updateBuffer();
			
			}
		}
		
	}
	
	protected final void onBoConfirm() throws Exception {
		
		Integer rowCount = ((ClientUI)getBillUI()).getBillListPanel().getHeadBillModel().getRowCount();
		List<AdjustVO> adjustList = new ArrayList<AdjustVO>();
		for(Integer row = 0 ; row < rowCount ; row++) {
			
			if(((ClientUI)getBillUI()).getBillListPanel().getHeadBillModel().getRowState(row) == BillModel.SELECTED) {
				adjustList.add((AdjustVO) getBufferData().getVOByRowNo(row).getParentVO());
			}
			
		}
		
		((ClientUI)getBillUI()).invUI.setAdjustVOs(adjustList.toArray(new AdjustVO[0]));
		
		((ClientUI)getBillUI()).invUI.onBoConfirm();
		
		Component comp = ((ClientUI)getBillUI()).getRootPane().getParent();
		((IFuncWindow)comp).closeWindow();
		
	}
	
	@Override
	protected void onBoSelAll() throws Exception {
		selectAll((BillManageUI) getBillUI(), true);
	}

	@Override
	protected void onBoSelNone() throws Exception {
		selectAll((BillManageUI) getBillUI(), false);
	}

	public void selectAll(BillManageUI billUI, boolean isNeedSelected) {
		int row = billUI.getBillListPanel().getHeadTable().getRowCount();
		BillModel headModel = billUI.getBillListPanel().getHeadBillModel();
		if (isNeedSelected) {
			for (int n = 0; n < row; n++) {
				if (headModel.getRowState(n) != BillModel.SELECTED) {
					headModel.setRowState(n, BillModel.SELECTED);
				}
			}
		} else {
			for (int n = 0; n < row; n++) {
				if (headModel.getRowState(n) != BillModel.UNSTATE) {
					headModel.setRowState(n, BillModel.UNSTATE);
				}
			}
		}

		billUI.getBillListPanel().updateUI();
		
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
			
			String def4 = (String) currAggVO.getParentVO().getAttributeValue("def4");
			if(def4 != null && "Y".equals(def4))
				throw new Exception ("��ǰ��¼��ʹ�ã����ܽ������������");
			
		}
		
		super.onBoCancelAudit();
	}
	
	public IBusinessController getBusiAction() {
		return getBusinessAction();
	}
	
	@Override
	protected void onBoQuery() throws Exception {
		super.onBoQuery();
		
		
		// ��ѯ��vbillno�ֶν�������
		int[] ints = { 0 };//��һ�п�ʼ����
		List<SortItem> msgList = new ArrayList<SortItem>();
		
		SortItem sort = new SortItem(((ClientUI)getBillUI()).getBillListPanel().getHeadBillModel().getBodyColByKey("vbillno") , false); // �����к�0
		msgList.add(sort); // ���ӵ��б�
		
		((ClientUI)getBillUI()).getBillListPanel().getHeadBillModel().sortByColumns(msgList, ints); // �����õ��Ǽ��ϣ�
		
	}

}