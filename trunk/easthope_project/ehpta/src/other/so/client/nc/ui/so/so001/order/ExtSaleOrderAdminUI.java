package nc.ui.so.so001.order;

import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillItemEvent;
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
public class ExtSaleOrderAdminUI extends SaleBillUI implements BillCardBeforeEditListener {

	//��ť��ʼ�����
	private boolean b_init;
	
	public ExtSaleOrderAdminUI() {
		super();
		
		// ���ñ�ͷ����β�ı༭ǰ�¼�
		// add by river for 2012-07-23
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}
	
	public ExtSaleOrderAdminUI(String pk_corp, String billtype, String busitype, String operator, String id) {
		super(pk_corp, billtype, busitype, operator, id);
		
		// ���ñ�ͷ����β�ı༭ǰ�¼�
		// add by river for 2012-07-23
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
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
		
		// ��� ��ͬ��� ��ť
		// add by river for 2012-07-30
		// start ..
		boAsstntQry.addChildButton(new ButtonObject("��ͬ���" , "" , 0 , "��ͬ���"));
		// ..end 

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
	
	/**
	 *  ���� : ����ʱ���ƺ�ͬ�Ƿ��Ѿ���ӣ�������ͬ�������ơ�
	 *  
	 *  Author : river
	 *  
	 *  Create Date : 2012-07-26
	 *  
	 */
	@Override
	protected void onNewByOther(AggregatedValueObject[] saleVOs)
			throws Exception {
		
		if(saleVOs != null && saleVOs.length > 0) {
			Object pk_contract = saleVOs[0].getParentVO().getAttributeValue("pk_contract");
			if(pk_contract != null) {
				Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select count(1) from so_sale where pk_contract = '"+pk_contract+"' and nvl(dr,0)=0 and contracttype = 10 ", new ColumnProcessor());
			
				if(count > 0 )
					throw new Exception("�ú�ͬ�Ѿ�¼�����۶�����");
			}
		}
		
		super.onNewByOther(saleVOs);
	}
	
	@Override
	protected void onCard() {
		super.onCard();
		
	}
	
	// ��ͷ����βITEM�ı༭ǰ�¼�
	// add by river for 2012-07-23
	public boolean beforeEdit(BillItemEvent e) {
		
		if(getBillCardPanel().getHeadItem("contracttype") != null) {
			
			UIComboBox comboBox = ((UIComboBox)getBillCardPanel().getHeadItem("contracttype").getComponent());
			if(comboBox != null && comboBox.getSelectdItemValue() != null) {
				if("ccustomerid".equals(e.getItem().getKey()) || "creceiptcorpid".equals(e.getItem().getKey()) || "creceiptcustomerid".equals(e.getItem().getKey())) {
				
					Object pk_contract = getBillCardPanel().getHeadItem("pk_contract").getValueObject();
				
					UIRefPane ccustomerRef = ((UIRefPane)e.getItem().getComponent());
					
					String wherePart = " 1 = 1  and (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='2') ";
					
					String nextWherePart = "";
					
					switch(Integer.valueOf(comboBox.getSelectdItemValue().toString())) {
					case 10 :
						nextWherePart = wherePart + " and pk_cumandoc in (select purchcode from ehpta_sale_contract where pk_contract = '"+pk_contract+"') ";
						break;
						
					case 20 : 
						
						nextWherePart = wherePart + " and pk_cumandoc in (select pk_custdoc from ehpta_aidcust where pk_contract = '"+pk_contract+"') ";
						break;
						
					default :
						
						break;
					}
					
						
					ccustomerRef.setWhereString(nextWherePart);
					
				} else if("cdeptid".equals(e.getItem().getKey())) {
					UIRefPane deptRef = (UIRefPane) e.getItem().getComponent();
					deptRef.setWhereString(" ( pk_corp = '"+getCorpPrimaryKey()+"' and pk_deptdoc = '"+deptRef.getRefPK()+"') ");
				} else if("cemployeeid".equals(e.getItem().getKey())) {
					UIRefPane employeeRef =  (UIRefPane) e.getItem().getComponent();
					employeeRef.setWhereString(" bd_psndoc.pk_corp='" + getCorpPrimaryKey() + "'  and bd_psndoc.indocflag='Y' and bd_psndoc.pk_psndoc = (select pk_psndoc from ehpta_sale_contract where pk_contract = '"+getBillCardPanel().getHeadItem("pk_contract").getValueObject()+"')");
				} else if("period".equals(e.getItem().getKey())) {
					switch(Integer.valueOf(comboBox.getSelectdItemValue().toString())) {
					case 10 :

						break;
						
					case 20 : 
						
						// ѡ���ڼ�ʱ�Զ����ձ������еĴ������������Ƽۡ�����ۡ���˰���ۣ�������ҽ�۲�
						
						
						break;
						
					default :
						
						break;
					}
				}
			}
		}
		return true;
	}

}
