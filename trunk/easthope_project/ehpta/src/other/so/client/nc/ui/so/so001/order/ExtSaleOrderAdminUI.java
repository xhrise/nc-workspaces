package nc.ui.so.so001.order;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.scm.extend.IFuncExtend;
import nc.ui.scm.so.SaleBillType;
import nc.ui.so.so001.panel.SaleBillUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.so.so001.SaleOrderVO;

/**
 * ���۶������� �������ڣ�(2012-07-22 )
 * 
 * @author river
 * 
 * 
 */
@SuppressWarnings({ "serial", "restriction" })
public class ExtSaleOrderAdminUI extends SaleBillUI {

	//��ť��ʼ�����
	private boolean b_init;
	
	private final IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);

	public ExtSaleOrderAdminUI() {
		super();
	}

	public ExtSaleOrderAdminUI(String pk_corp, String billtype, String busitype, String operator, String id) {
		super(pk_corp, billtype, busitype, operator, id);
	}

	@Override
	public String getBillButtonAction(nc.ui.pub.ButtonObject bo) {
		return null;
	}

	@Override
	public String getBillButtonState() {
		return null;
	}

	/**
	 * ��ð�ť���顣
	 * 
	 * ֧�ֲ���˵�,location�������£�0 ��Ƭ�б�;1 �˻�;2 BOM; 3 ����
	 * 
	 * �������ڣ�(2001-11-15 8:58:51)
	 * 
	 * @return nc.ui.pub.ButtonObject[]
	 */
	public nc.ui.pub.ButtonObject[] getBillButtons() {
		if (!b_init) {
			initBtnGrp();
			b_init = true;
		}

		if (strShowState.equals("�б�")) {
			return aryListButtonGroup;
		} else if (strShowState.equals("��Ƭ")) {
			initLineButton();
			return aryButtonGroup;
		} else {
			return aryBatchButtonGroup;
		}
	}

	/**
	 * V51Ҫ��Ƭ���б�İ�ťһ��
	 * 
	 */
	private void initBtnGrp() {

		// �б�ť
		ButtonObject[] aryListButtonGroup = { boBusiType, boAdd, boSave, boMaintain, boLine, boAudit, boAction,
				boQuery, boBrowse, boCard, boPrntMgr, boAssistant, boAsstntQry };

		// ��Ƭ��ť
		ButtonObject[] aryButtonGroup = { boBusiType, boAdd, boSave, boMaintain, boLine, boAudit, boAction, boQuery,
				boBrowse, boReturn, boPrntMgr, boAssistant, boAsstntQry };

		// �˻���Ƭ��ť
		ButtonObject[] aryBatchButtonGroup = { boBatch, boLine, boRefundmentDocument, boBack };

		// �������۰�ť��
		ButtonObject[] bomButtonGroup = { boBomSave, boBomEdit, boBomCancel, boBomReturn, boOrderQuery,
		//				boBomPrint 
		};

		IFuncExtend funcExtend = getFuncExtend();
		if (funcExtend != null) {
			ButtonObject[] boExtend = m_funcExtend.getExtendButton();
			if (boExtend != null && boExtend.length > 0) {

				// ��Ƭ��ť
				ButtonObject[] botempcard = new ButtonObject[aryButtonGroup.length + boExtend.length];

				System.arraycopy(aryButtonGroup, 0, botempcard, 0, aryButtonGroup.length);

				System.arraycopy(boExtend, 0, botempcard, aryButtonGroup.length, boExtend.length);

				aryButtonGroup = botempcard;

				// �б�ť
				ButtonObject[] botemplist = new ButtonObject[aryListButtonGroup.length + boExtend.length];

				System.arraycopy(aryListButtonGroup, 0, botemplist, 0, aryListButtonGroup.length);

				System.arraycopy(boExtend, 0, botemplist, aryListButtonGroup.length, boExtend.length);

				aryListButtonGroup = botemplist;
			}

		}
		ButtonObject[] boExtend = getExtendBtns();
		if (boExtend != null && boExtend.length > 0) {

			// ��Ƭ��ť
			ButtonObject[] botempcard = new ButtonObject[aryButtonGroup.length + boExtend.length];

			System.arraycopy(aryButtonGroup, 0, botempcard, 0, aryButtonGroup.length);

			System.arraycopy(boExtend, 0, botempcard, aryButtonGroup.length, boExtend.length);

			aryButtonGroup = botempcard;

			// �б�ť
			ButtonObject[] botemplist = new ButtonObject[aryListButtonGroup.length + boExtend.length];

			System.arraycopy(aryListButtonGroup, 0, botemplist, 0, aryListButtonGroup.length);

			System.arraycopy(boExtend, 0, botemplist, aryListButtonGroup.length, boExtend.length);

			aryListButtonGroup = botemplist;
		}

		// �������˵�
		ButtonObject[][] ret_butns = new SaleOrderPluginMenuUtil().addMenu(aryListButtonGroup, aryButtonGroup,
				aryBatchButtonGroup, bomButtonGroup, getModuleCode());

		this.aryListButtonGroup = ret_butns[0];
		this.aryButtonGroup = ret_butns[1];
		this.aryBatchButtonGroup = ret_butns[2];
		this.bomButtonGroup = ret_butns[3];

	}

	@Override
	public String getBillID() {

		if ("�˻�".equals(strState)) { /*-=notranslate=-*/
			return id;
		}

		if (strShowState == "�б�") {
			return (String) getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");
		} else if (strShowState == "��Ƭ") {
			return (String) getBillCardPanel().getHeadItem("csaleid").getValueObject();
		} else
			return null;
	}

	@Override
	public String getNodeCode() {
		return "40060301";
	}

	@Override
	public String getTitle() {
		return getBillListPanel().getBillListData().getTitle();
	}

	protected void initButtons() {

		// ҵ������
		PfUtilClient.retBusinessBtn(boBusiType, getCorpPrimaryKey(), SaleBillType.SaleOrder);

		if (boBusiType.getChildButtonGroup() != null && boBusiType.getChildButtonGroup().length > 0) {
			boBusiType.setTag(boBusiType.getChildButtonGroup()[0].getTag());
			boBusiType.getChildButtonGroup()[0].setSelected(true);
			boBusiType.setCheckboxGroup(true);

			// ����
			PfUtilClient.retAddBtn(boAdd, getCorpPrimaryKey(), getBillType(), boBusiType.getChildButtonGroup()[0]);

		}

		// ά��
		boMaintain.removeAllChildren();
		boMaintain.addChildButton(boEdit);
		boMaintain.addChildButton(boCancel);
		boMaintain.addChildButton(boBlankOut);
		boMaintain.addChildButton(boCopyBill);

		// �в���
		boLine.removeAllChildren();
		boLine.addChildButton(boAddLine);
		boLine.addChildButton(boDelLine);
		boLine.addChildButton(boInsertLine);
		boLine.addChildButton(boCopyLine);
		boLine.addChildButton(boPasteLine);
		boLine.addChildButton(boPasteLineToTail);
		boLine.addChildButton(boFindPrice);
		boLine.addChildButton(boCardEdit);
		boLine.addChildButton(boResortRowNo);

		// ���
		boBrowse.removeAllChildren();
		boBrowse.addChildButton(boRefresh);
		boBrowse.addChildButton(boFind);
		boBrowse.addChildButton(boFirst);
		boBrowse.addChildButton(boPre);
		boBrowse.addChildButton(boNext);
		boBrowse.addChildButton(boLast);
		boBrowse.addChildButton(boListSelectAll);
		boBrowse.addChildButton(boListDeselectAll);

		//ִ��
		retElseBtn("Order002", "Order001");
		retElseBtn("Order002", "Order003");

		//������ѯ
		boAsstntQry.removeAllChildren();
		boAsstntQry.addChildButton(boOrderQuery);
		boAsstntQry.addChildButton(boOnHandShowHidden);
		boAsstntQry.addChildButton(boAuditFlowStatus);
		boAsstntQry.addChildButton(boCustCredit);
		boAsstntQry.addChildButton(boOrderExecRpt);
		boAsstntQry.addChildButton(boCustInfo);
		boAsstntQry.addChildButton(boPrifit);

		//��ӡ
		boPrntMgr.removeAllChildren();
		boPrntMgr.addChildButton(boPreview);
		boPrntMgr.addChildButton(boPrint);
		boPrntMgr.addChildButton(boSplitPrint);

		//Эͬ
		boAssistant.addChildButton(boCoPushPo);
		boAssistant.addChildButton(boCoRefPo);

		if (strShowState.equals("�б�")) { /*-=notranslate=-*/
			setButtons(getBillButtons());
		}//end list
		else if (strShowState.equals("��Ƭ")) { /*-=notranslate=-*/

			if (strState.equals("BOM")) {
				setButtons(bomButtonGroup);
			} else {
				setButtons(getBillButtons());
			}

		}//end card
	}
	
	public void setButtonsState() { 
		super.setButtonsState();
		
		getPluginProxy().setButtonStatus();
	}

	@Override
	protected String getClientUI() {
		return ExtSaleOrderAdminUI.class.getName();
	}
	
	@Override
	protected void onNewByOther(AggregatedValueObject[] saleVOs)
			throws Exception {
		
		if(saleVOs != null && saleVOs.length > 0) {
			Object pk_contract = saleVOs[0].getParentVO().getAttributeValue("pk_contract");
			if(pk_contract != null) {
				Integer count = (Integer)iUAPQueryBS.executeQuery("select count(1) from so_sale where pk_contract = '"+pk_contract+"' and nvl(dr,0)=0 ", new ColumnProcessor());
			
				if(count > 0 )
					throw new Exception("�ú�ͬ�Ѿ�¼�����۶�����");
			}
		}
		
		super.onNewByOther(saleVOs);
	}
	
	@Override
	protected void onCard() {
		super.onCard();
		
		
		// ˫���б��������ʱ��PLUGIN���޷�����mouse_doubleclick�������������onCard�������ڴ˼������ý����ͬ���͵Ĵ����
		// add by river for 2012-07-20
		// start ..
		
		String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(getBillListPanel().getHeadTable().getSelectedRow(), "csaleid");
		SaleOrderVO saleorder = vocache.getSaleOrderVO(csaleid);
		if(getBillCardPanel().getHeadItem("contracttype") != null) {
			if(saleorder != null && saleorder.getParentVO() != null) {
				if(Integer.valueOf(saleorder.getParentVO().getAttributeValue("contracttype") == null ? "0" : saleorder.getParentVO().getAttributeValue("contracttype").toString()) == 10)
					((UIComboBox) getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("�ֻ���ͬ");
				else if(Integer.valueOf(saleorder.getParentVO().getAttributeValue("contracttype") == null ? "0" : saleorder.getParentVO().getAttributeValue("contracttype").toString()) == 20)
					((UIComboBox) getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("������ͬ");
			}
		}
		
		// .. end
	}
}
