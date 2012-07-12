package nc.ui.ehpta.hq010402;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.valid.Validata;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.filesystem.FileManageUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.multichild.MultiChildBillCardPanelWrapper;
import nc.vo.ehpta.hq010402.MultiBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

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
	
	public final String[] getTableCodes() {
		return new String[]{"ehpta_sale_contract_b", "ehpta_aidcust", "ehpta_prepolicy"};
	}
	
	public final String getTableNames( String tableCode) {
		if(getTableCodes()[0].equals(tableCode))
			return  "������ͬ������Ϣ";
		else if(getTableCodes()[1].equals(tableCode))
			return "�����ͻ�";
		else if(getTableCodes()[2].equals(tableCode))
			return "�Ż�����";
		
		return null;
	}

	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
			case DefaultBillButton.DOCUMENT:
	
				this.documentManage();
				break;
	
			default:
	
				break;
		}
	}
	
	@Override
	protected void onBoSave() throws Exception {
		
		saveMultiValidataIsNull(getBillCardPanelWrapper().getBillCardPanel(), (MultiBillVO) getBillCardPanelWrapper().getBillVOFromUI(), getTableCodes() );
		
		super.onBoSave();
		
		if(((MultiBillVO)getBufferData().getCurrentVO()).getTableVO(getTableCodes()[1]) != null && ((MultiBillVO)getBufferData().getCurrentVO()).getTableVO(getTableCodes()[1]).length > 0)
			HYPubBO_Client.updateAry((SuperVO[]) ((MultiBillVO)getBufferData().getCurrentVO()).getTableVO(getTableCodes()[1]));
	}
	
	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
	}
	
	@Override
	protected void onBoLineDel() throws Exception {
		
		String selectedTableCode = getBillCardPanelWrapper().getBillCardPanel().getBodyTabbedPane().getSelectedTableCode();
		if(getTableCodes()[1].equals(selectedTableCode)) {
			if(getBillCardPanelWrapper().getBillCardPanel().getBillTable(selectedTableCode).getSelectedRow() == 0 )
				throw new Exception ("����ɾ�������ͻ��ĵ�һ����¼��");
				
		} 
		
		super.onBoLineDel();
		
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
	
	public final void saveMultiValidataIsNull(BillCardPanel billCardPanel , MultiBillVO currAggVO , String[] bodyTableCodes) throws Exception {
		
		BillItem[] headItems = billCardPanel.getHeadItems();
		
		for(BillItem head : headItems) {
			if(head.isNull()) {
				
				for(String attr : currAggVO.getParentVO().getAttributeNames()) {
					if(attr.equals(head.getKey())) {
						Object obj = currAggVO.getParentVO().getAttributeValue(head.getKey());
						
						if(obj == null || "".equals(obj)) 
							throw new Exception ("��ͷ ��" + head.getName() + " , ����Ϊ�գ�");
						
						break;
					} else {
						if(head.getValueObject() == null || "".equals(head.getValueObject()))
							throw new Exception ("��ͷ ��" + head.getName() + " , ����Ϊ�գ�");
						
						break;
					}
					
				}
				
			}
			
		}
		
		for(String tableCode : bodyTableCodes) {
			for(BillItem body : billCardPanel.getBillData().getBodyItemsForTable(tableCode)) {
				
				if(body.isNull()) {
					SuperVO[] childVOs = (SuperVO[]) currAggVO.getTableVO(tableCode);
					if(childVOs != null && childVOs.length > 0) {
						int row = 0;
						for(SuperVO child : childVOs) {
							if(child.getTableName().equals(tableCode)) {
								for(String attr : child.getAttributeNames()) {
									if(attr.equals(body.getKey())) {
										Object obj = child.getAttributeValue(body.getKey());
										if(obj == null || "".equals(obj))
											throw new Exception ("����[ "+getTableNames(tableCode)+" ] ��"+(row + 1)+"  ��" + body.getName() + " , ����Ϊ�գ�");
									
										break;
									} 
									
								}
							
							}
							
							row ++;
						}
						
					}
					
				}
				
			}
		
		}
		
	}

}