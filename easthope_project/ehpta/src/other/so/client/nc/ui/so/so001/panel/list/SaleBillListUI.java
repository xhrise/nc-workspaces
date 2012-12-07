package nc.ui.so.so001.panel.list;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComponent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.so.so001.panel.card.SOBillCardTools;
import nc.ui.so.so001.panel.card.SOBillQueryDlg;
import nc.ui.so.so001.panel.card.SaleBillCardUI;

import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

/**
 * ���۶���UI�˳������(��Ҫ�����б�ͻ������)
 * 
 * @author zhongwei
 * 
 */
public abstract class SaleBillListUI extends SaleBillCardUI implements
		IBillExtendFun {

	private SOBillListPanel ivjSaleOrderListPane;

	private BillListTools listtools;

	// �˻���Ƭ��ť
	protected ButtonObject[] aryBatchButtonGroup;

	// ��Ƭ��ť��
	protected ButtonObject[] aryButtonGroup;

	// �б�ť��
	protected ButtonObject[] aryListButtonGroup;

	protected ButtonObject boAdd;

	protected ButtonObject boFirst;

	protected ButtonObject boPre;

	protected ButtonObject boNext;

	protected ButtonObject boLast;

	protected ButtonObject boEdit;

	protected ButtonObject boSave;

	protected ButtonObject boCancel;

	protected ButtonObject boSendAudit;

	// ��δʹ��
	protected ButtonObject boOrdPay;

	protected ButtonObject boCachPay;

	public ButtonObject boAction;

	protected ButtonObject boLine;

	protected ButtonObject boPreview;

	protected ButtonObject boPrint;
	
	protected ButtonObject boSplitPrint;

	public ButtonObject boAssistant;

	protected ButtonObject boDocument;

	protected ButtonObject boRefundmentDocument;

	protected ButtonObject boReturn;

	protected ButtonObject boAuditFlowStatus;

	protected ButtonObject boOrderQuery;

	protected ButtonObject boBatch;

	protected ButtonObject boBrowse;

	protected ButtonObject boListSelectAll;

	protected ButtonObject boListDeselectAll;

	protected ButtonObject boCard;

	protected ButtonObject boRefundment;

	protected ButtonObject boFind;

	protected ButtonObject boQuery;

	protected ButtonObject boMaintain;

	protected ButtonObject boBlankOut;

	protected ButtonObject boCopyBill;

	protected ButtonObject boAudit;

	protected ButtonObject boCancelAudit;

	protected ButtonObject boModification;

	protected ButtonObject boCancelFreeze;

	protected ButtonObject boFreeze;

	protected ButtonObject boFinish;

	protected ButtonObject boRefresh;

	protected ButtonObject boOrdBalance;

	protected ButtonObject boAsstntQry;

	protected ButtonObject boOnHandShowHidden;

	protected ButtonObject boCustCredit;

	protected ButtonObject boOrderExecRpt;

	protected ButtonObject boCustInfo;

	protected ButtonObject boPrifit;

	protected ButtonObject boPrntMgr;

	protected ButtonObject boBack;
	
	//����Эͬ�ɹ�����
	protected ButtonObject boCoRefPo;
	//����Эͬ�ɹ�����
	protected ButtonObject boCoPushPo;

	private SOBillQueryDlg dlgQuery;

	// ������ʾ״̬
	public String strShowState = "�б�";

	protected int selectRow = -1; // ѡ����

	protected int num = -1; // ��ǰID

	protected Vector vIDs; // ID

	protected JComponent curShowPanel = null;

	protected ArrayList alInvs = getBillCardPanel().alInvs;

	public SaleBillListUI() {
		super();
	}

	public SaleBillListUI(String pk_corp, String billtype, String busitype,
			String operator, String id) {
		super(pk_corp, billtype, busitype, operator, id);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	protected void initialize() {
		long st = System.currentTimeMillis();

		// ������ʾ״̬
		strShowState = "�б�"; /*-=notranslate=-*/
		selectRow = -1; // ѡ����
		num = -1; // ��ǰID

		loadAllBtns();

		super.initialize();
		loadListTemplet();
		initIDs();
		switchInterface();

		getBillListPanel().addBodyTotalLstn();
		getBillListPanel().addHeadSortRelaLstn();
		getBillListPanel().addEditListener(ivjSaleOrderListPane);

		nc.vo.scm.pub.SCMEnv.out(" admin initialize cost time "
				+ (System.currentTimeMillis() - st));
	}

	/**
	 * ��ʼ�����а�ť����û��ҵ������
	 * 
	 * �ڳ�ʼ������֮ǰ���ã�����ɸ�д
	 * 
	 */
	protected void loadAllBtns() {
		getBoAction();
		getBoAdd();
		getBoAddLine();
		getBoAssistant();
		getBoAsstntQry();
		getBoAudit();
		getBoAuditFlowStatus();
		getBoBack();
		getBoBatch();
		getBoBlankOut();
		getBoBrowse();
		getBoCancel();
		getBoCancelAudit();
		getBoCancelFreeze();
		getBoCard();
		getBoCopyBill();
		getBoCopyLine();
		getBoFindPrice();
		getBoCardEdit();
		getBoResortRowNo();
		getBoCustCredit();
		getBoCustInfo();
		getBoDelLine();
		getBoDocument();
		getBoRefundmentDocument();
		getBoEdit();
		getBoFind();
		getBoFinish();
		getBoFirst();
		getBoFreeze();
		getBoInsertLine();
		getBoLast();
		getBoLine();
		getBoListDeselectAll();
		getBoListSelectAll();
		getBoMaintain();
		getBoModification();
		getBoNext();
		getBoOnHandShowHidden();
		getBoOrdBalance();
		getBoOrderExecRpt();
		getBoOrderQuery();
		getBoCachPay();
		getBoPasteLine();
		getBoPasteLineToTail();
		getBoPre();
		getBoPreview();
		getBoPrifit();
		getBoPrint();
		getBoSplitPrint();
		getBoPrintMgr();
		getBoQuery();
		getBoRefresh();
		getBoRefundment();
		getBoReturn();
		getBoSave();
		getBoSendAudit();
		getBoStockLock();
		getBoSendInv();
		getBoSupplyInv();
		getBoDirectInv();
		getBoCoRefPo();
		getBoCoPushPo();
	}
	
	private ButtonObject getBoCoRefPo(){
		if(boCoRefPo==null){
			boCoRefPo = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("sopub", "UPPsopub-000325")/*
			 * @res "����Эͬ�ɹ�����"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("sopub", "UPPsopub-000325")/*
			 * @res
			 * "����Эͬ�ɹ�����"
			 */, 0, "����Эͬ�ɹ�����"); /*-=notranslate=-*/
			boCoRefPo.setTag("21:Эͬ");
			
		}
		return boCoRefPo;
		
	}
	private ButtonObject getBoCoPushPo(){
		if(boCoPushPo==null){
			boCoPushPo = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("sopub", "UPPsopub-000324")/*
			 * @res "����Эͬ�ɹ�����"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("sopub", "UPPsopub-000324")/*
			 * @res
			 * "����Эͬ�ɹ�����"
			 */, 0, "����Эͬ�ɹ�����"); /*-=notranslate=-*/
			
		}
		return boCoPushPo;
		
	}

	/**
	 * �����б�ģ�塣
	 * 
	 * �������ڣ�(2001-11-15 9:03:35)
	 * 
	 */
	private void loadListTemplet() {
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000135")/* @res "��ʼ�����б�ģ��...." */);
		
		BillListData bd = getBillListPanel().getBillListData();
		billtempletVO = null;
		
//		if (SO_20.booleanValue()) {
//
//			if (billtempletVO == null) {
//				billtempletVO = getBillListPanel().getDefaultTemplet(
//						getNodeCode(),// getBillType(),
//						null, getBillListPanel().getOperator(),
//						getBillListPanel().getCorp());
//			}
//			bd = new BillListData(billtempletVO);
//
//		} else {
//			if (billtempletVO == null) {
//
//				billtempletVO = getBillListPanel().getDefaultTemplet(
//						getNodeCode(),// getBillType(),
//						getBillListPanel().getBusiType(),
//						getBillListPanel().getOperator(),
//						getBillListPanel().getCorp());
//
//			}
//			bd = new BillListData(billtempletVO);
//		}
//
//		billtempletVO = null;

		BillItem bm = bd.getHeadItem("naccountperiod");
		if (bm != null) {
			bm.setDecimalDigits(0);
		}

		SOBillCardTools.processCTBillItem(null, bd);

		// �ı����
		setListPanelByPara(bd);
		
		// ���ý��棬��������Դ
		getBillListPanel().setListData(bd);		
		
		// ����������
		initBodyComboBox();

		BillItem bmbiztype = bd.getHeadItem("cbiztype");
		if (bmbiztype != null) {
			bmbiztype.setShow(false);
			bmbiztype.setEdit(false);
		}

		BillItem cbiztypename = bd.getHeadItem("cbiztypename");
		if (cbiztypename != null) {
			cbiztypename.setEdit(false);
		}

		getBillListPanel().getChildListPanel().setTatolRowShow(true);
		
		getBillListPanel().getHeadTable().setSelectionMode(
				javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
	    // ����ӱ���ɫ
		String[] tableCodes = getBillListPanel().getBillListData().getBodyTableCodes();
		for (String tableCode : tableCodes) {
			getBillListPanel().getBodyTable(tableCode).setRowSelectionAllowed(true);
			getBillListPanel().getBodyTable(tableCode).setColumnSelectionAllowed(false);
		}

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000147")/* @res "�б�ģ����سɹ���" */);
	}

	/**
	 * ���ݲ����ı��б���棨��Ҫ�����ֶξ��ȣ���
	 * 
	 * �������ڣ�(2001-9-27 16:13:57)
	 * 
	 */
	private void setListPanelByPara(BillListData bdData) {

		try {
            //��������Ԥ�����Ƿ�ر�
			getBillListPanel().hideBodyTableCol("bbindflag");
			getBillListPanel().hideBodyTableCol("bpreorderclose");
			getBillListPanel().hideHeadTableCol("nexchangeotobrate");
			getBillListPanel().hideHeadTableCol("ccurrencytypeid");

			// ���鷢�������֯(V5.3ȥ���˲�������)
			/*if (SA_16 != null && SA_16.booleanValue()) {
				if (bdData.getBodyItem("sharecode2", "cadvisecalbody") != null)
					bdData.getBodyItem("sharecode2", "cadvisecalbody").setShow(
							true);
				else
					bdData.getBodyItem("cadvisecalbody").setShow(true);
			} else {
				if (bdData.getBodyItem("sharecode2", "cadvisecalbody") != null)
					bdData.getBodyItem("sharecode2", "cadvisecalbody").setShow(
							false);
				else
					bdData.getBodyItem("cadvisecalbody").setShow(false);
			}*/


            //���ݱ��������۱�����
			//getBillListPanel().setCurrTypeDigit();
			
			// ����С��λ��
			bdData.getBodyItem("nexchangeotobrate").setDecimalDigits(4);
			
			// �����ۿ�С��λ��
			if (bdData.getBodyItem("nitemdiscountrate") != null)
				bdData.getBodyItem("nitemdiscountrate").setDecimalDigits(6);
			if (bdData.getBodyItem("ndiscountrate") != null)
				bdData.getBodyItem("ndiscountrate").setDecimalDigits(6);
            if (bdData.getHeadItem("ndiscountrate")!= null)
            	bdData.getHeadItem("ndiscountrate").setDecimalDigits(6);            
            
			// ����
			String[] aryNum = { "nnumber", "ntotalreceivenumber",
					"ntotalinvoicenumber", "ntotalinventorynumber",
					"ntotalbalancenumber", "nquoteunitnum", "narrangescornum",
					"narrangepoapplynum", "narrangetoornum",
					"norrangetoapplynum", "narrangemonum",
					"ntotalshouldoutnum", "ntotlbalcostnum", "nrushnum",
					"ntranslossnum","ntotalreturnnumber" };

			if (BD501 != null) {
				for (int i = 0; i < aryNum.length; i++) {
					bdData.getBodyItem(aryNum[i]).setDecimalDigits(
							BD501.intValue());
				}
			}
			if (BD502 != null) {
				bdData.getBodyItem("npacknumber").setDecimalDigits(
						BD502.intValue());
				// bdData.getBodyItem("nquoteunitnum").setDecimalDigits(BD502.intValue());
			}
			// ����
			if (BD505 != null) {
				bdData.getBodyItem("noriginalcurprice").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("noriginalcurtaxprice").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("noriginalcurnetprice").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("noriginalcurtaxnetprice").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("norgqttaxprc").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("norgqtprc").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("norgqttaxnetprc").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("norgqtnetprc").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("nprice").setDecimalDigits(BD505.intValue());
				bdData.getBodyItem("ntaxprice").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("nnetprice").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("ntaxnetprice").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("nqttaxnetprc").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("nqtnetprc").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("nqttaxprc").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("nqtprc").setDecimalDigits(BD505.intValue());
				bdData.getBodyItem("nprice").setDecimalDigits(BD505.intValue());
				bdData.getBodyItem("ntaxprice").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("nnetprice").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("ntaxnetprice").setDecimalDigits(
						BD505.intValue());

				bdData.getBodyItem("norgviaprice").setDecimalDigits(
						BD505.intValue());
				bdData.getBodyItem("norgviapricetax").setDecimalDigits(
						BD505.intValue());
			}
			// ������
			if (BD503 != null) {
				bdData.getBodyItem("scalefactor").setDecimalDigits(
						BD503.intValue());
				bdData.getBodyItem("nqtscalefactor").setDecimalDigits(
						BD503.intValue());
				bdData.getHeadItem("npreceiverate").setDecimalDigits(
						BD503.intValue());
			}

		} catch (Exception e) {
			SCMEnv.out(e);
		}
		try {
			// �Զ�����
			nc.vo.bd.def.DefVO[] defs = null;

			// ��ͷ
			// ��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������
			defs = head_defs;

			if (defs != null)
				bdData.updateItemByDef(defs, "vdef", true);

			// ����
			// ��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������
			defs = body_defs;

			if (defs != null)
				bdData.updateItemByDef(defs, "vdef", false);
		} catch (Throwable ex) {
			//ex.printStackTrace();
		}

	}

	/**
	 * ��ʼ����
	 * 
	 * �������ڣ�(2001-5-12 12:04:03)
	 * 
	 */
	public void initIDs() {
		vIDs = new Vector();
		for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
			vIDs.add(getBillListPanel().getHeadBillModel().getValueAt(i,
					"csaleid"));
		}
	}

	/**
	 * �л����档
	 * 
	 * �������ڣ�(2001-10-25 12:34:10)
	 * 
	 */
	protected void switchInterface() {
		if (strShowState.equals("�б�")) { /*-=notranslate=-*/
			remove(getSplitPanelBc());
			add(getBillListPanel(), "Center");
			setCurShowPanel(getBillListPanel());
			//��������
			getBillListPanel().hideBodyTableCol("bbindflag");
		} else {
			remove(getBillListPanel());
			add(getSplitPanelBc(), "Center");
			setCurShowPanel(getSplitPanelBc());
			//����ģ��Ͳ���������ã����Һ͸��Ҿ����ɱ༭
			getBillCardTools().setBodyItemEnable(
					SOBillCardTools.getSaleOrderItems_Price_Mny_NoOriginal(),false);
			//��������
			getBillCardPanel().hideBodyTableCol("bbindflag");
		}
		
		updateUI();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-7 22:31:33)
	 * 
	 * @param newCurShowPanel
	 *            javax.swing.JPanel
	 */
	public void setCurShowPanel(JComponent newCurShowPanel) {
		curShowPanel = newCurShowPanel;
	}

	/**
	 * �����б�
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	public SOBillListPanel getBillListPanel() {
		if (ivjSaleOrderListPane == null) {
			try {
				ivjSaleOrderListPane = new SOBillListPanel(this,
						getCorpPrimaryKey(), vocache);
				ivjSaleOrderListPane.hideBodyTableCol("bbindflag");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjSaleOrderListPane;
	}

	protected abstract void onCard();

	protected SOBillQueryDlg getQueryDlg() {
		return getQueryDlg(getCorpPrimaryKey());
	}

	/**
	 * �繫˾ר��
	 * 
	 * @param pk_corp
	 * @return
	 */
	protected SOBillQueryDlg getQueryDlg(String pk_corp) {
		if (dlgQuery == null) {
			dlgQuery = new SOBillQueryDlg(this, pk_corp);
		}

		return dlgQuery;
	}

	/**
	 * ��ȡ��չ��ť����
	 */
	public ButtonObject[] getExtendBtns() {
		return null;
	}

	/**
	 * ������չ��ť���¼�
	 */
	public void onExtendBtnsClick(ButtonObject bo) {

	}

	/**
	 * ������չ��ť״̬
	 */
	public void setExtendBtnsStat(int iState) {

	}

	protected void showCustManArInfo() {
		getBillCardPanel().showCustManArInfo();
	}

	/**
	 * ���巢����˾�ı�ʱ�����ƽ���ı仯�� �������ڣ�(2004-2-9 15:10:14)
	 */
	protected void ctlUIOnCconsignCorpChg(int row) {
		String pk_corp = getBillCardTools().getHeadStringValue("pk_corp");
		if (pk_corp == null)
			return;
		String cconsigncorp = getBillCardTools().getBodyStringValue(row,
				"cconsigncorpid");

		// ���������˾�����۹�˾(����½��˾)��ͬ����Ϊ����˾���ۣ�
		// �����ջ������֯���ջ��ֿ⣬�Ƿ�ֱ�˲��ɱ༭
		if (cconsigncorp == null || pk_corp.equals(cconsigncorp)) {

			getBillCardTools().setBodyCellsEdit(
					new String[] { "creccalbody", "crecwarehouse",
							"bdericttrans" }, row, false);
			//
			getBillCardTools().setBodyCellsEdit(
					new String[] { "boosflag", "bsupplyflag" }, row, true);

		} else {
			getBillCardTools().setBodyCellsEdit(
					new String[] { "boosflag", "bsupplyflag" }, row, false);
			getBillCardTools().setBodyCellsEdit(
					new String[] { "creccalbody", "crecwarehouse",
							"bdericttrans" }, row, true);

		}
	}

	/**
	 * ������Card����ϸ��µ��ݺ󣬱����б�����ϵ�voͬʱ���£�yb��
	 * 
	 * �������ڣ�(2003-10-20 8:59:36)
	 * 
	 */
	protected void updateCacheVO() {
		if (strShowState.equals("�б�")) { /*-=notranslate=-*/
			int row = getBillListPanel().getHeadTable().getSelectedRow();
			if (row < 0)
				return;

			getBillListPanel().fillCacheName();
			getBillListPanel().fillBodyCacheName();

			String sSaleid = (String) getBillListPanel().getHeadBillModel()
					.getValueAt(row, "csaleid");
			if (sSaleid == null)
				return;
			SaleOrderVO saleorder = vocache.getSaleOrderVO(sSaleid);

			/*getBillListPanel().getHeadBillModel().setBodyRowVO(
					saleorder.getParentVO(), num);*/
			getBillListPanel().getHeadBillModel().setBodyRowVO(
					saleorder.getParentVO(), row);

			// ���ñ�ͷ����
			try {
				/*nc.ui.so.so001.panel.bom.BillTools.setMnyToModelByCurrency(
						getBillListPanel().getHeadBillModel(), saleorder
								.getParentVO(), num, getCorpPrimaryKey(),
						saleorder.getBodyVOs()[0].getCcurrencytypeid(),
						new String[] { "npreceivemny", "nreceiptcathmny" });*/
				nc.ui.so.so001.panel.bom.BillTools.setMnyToModelByCurrency(
						getBillListPanel().getHeadBillModel(), saleorder
								.getParentVO(), row, getCorpPrimaryKey(),
						saleorder.getBodyVOs()[0].getCcurrencytypeid(),
						new String[] { "npreceivemny", "nreceiptcathmny" });
			} catch (Exception e) {
				handleException(e);
			}

		} else {
			SaleOrderVO saleorder = (SaleOrderVO) getBillCardPanel()
					.getBillValueVO(SaleOrderVO.class.getName(),
							SaleorderHVO.class.getName(),
							SaleorderBVO.class.getName());

			SaleOrderVO oldvo = null;
			if (saleorder.getHeadVO().getCsaleid() != null)
				oldvo = vocache.getSaleOrderVO(saleorder.getHeadVO()
						.getCsaleid());

			SaleorderHVO hvo = (SaleorderHVO) saleorder.getParentVO();
			UIRefPane ufref = (UIRefPane) getBillCardPanel().getHeadItem(
					"ccalbodyid").getComponent();
			hvo.setAttributeValue("ccalbodyname", ufref.getUITextField()
					.getText());

			// if (boBusiType.getSelectedChildButton() != null
			// && boBusiType.getSelectedChildButton().length > 0
			// && boBusiType.getSelectedChildButton()[0] != null
			// && boBusiType.getSelectedChildButton()[0].getCode() != null) {
			// hvo.setAttributeValue("cbiztypename", boBusiType
			// .getSelectedChildButton()[0].getCode());
			//
			// } else
			if (oldvo != null) {
				hvo.setAttributeValue("cbiztypename", oldvo.getHeadVO()
						.getAttributeValue("cbiztypename"));
			}

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("ccustomerid")
					.getComponent();
			hvo.setAttributeValue("ccustomername", ufref.getUITextField()
					.getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid")
					.getComponent();
			hvo
					.setAttributeValue("cdeptname", ufref.getUITextField()
							.getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid")
					.getComponent();
			hvo.setAttributeValue("cemployeename", ufref.getUITextField()
					.getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("cfreecustid")
					.getComponent();
			hvo.setAttributeValue("cfreecustname", ufref.getUITextField()
					.getText());

			ufref = (UIRefPane) getBillCardPanel()
					.getHeadItem("creceiptcorpid").getComponent();
			hvo.setAttributeValue("creceiptcorpname", ufref.getUITextField()
					.getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem(
					"creceiptcustomerid").getComponent();
			hvo.setAttributeValue("creceiptcustomername", ufref
					.getUITextField().getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("csalecorpid")
					.getComponent();
			hvo.setAttributeValue("csalecorpname", ufref.getUITextField()
					.getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem(
					"ctermprotocolid").getComponent();
			hvo.setAttributeValue("ctermprotocolname", ufref.getUITextField()
					.getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("ctransmodeid")
					.getComponent();
			hvo.setAttributeValue("ctransmodename", ufref.getUITextField()
					.getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("cwarehouseid")
					.getComponent();
			hvo.setAttributeValue("cwarehousename", ufref.getUITextField()
					.getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			hvo.setAttributeValue("vreceiveaddress", ufref.getUITextField()
					.getText());

			ufref = (UIRefPane) getBillCardPanel().getTailItem("capproveid")
					.getComponent();
			hvo.setAttributeValue("capprovename", ufref.getUITextField()
					.getText());

			ufref = (UIRefPane) getBillCardPanel().getTailItem("coperatorid")
					.getComponent();
			hvo.setAttributeValue("coperatorname", ufref.getUITextField()
					.getText());

			vocache.updateSaleOrderVO(hvo.getCsaleid(), saleorder,
					getMustUpdateHeadNames(), getMustUpdateBodyNames());

			if ((num > -1)
					&& (num < getBillListPanel().getHeadBillModel()
							.getRowCount())) {
				hvo = vocache.getSaleOrderVO(hvo.getCsaleid()).getHeadVO();
				getBillListPanel().getHeadBillModel().setBodyRowVO(hvo, num);
			}
		}

	}

	/**
	 * ������Card����ϸ��µ��ݺ󣬱����б�����ϵ�voͬʱ���£�yb��
	 * 
	 * �������ڣ�(2003-10-20 8:59:36)
	 * 
	 */
	protected void addCacheVO() {
		if (strShowState.equals("�б�")) { /*-=notranslate=-*/

		} else {
			SaleOrderVO saleorder = (SaleOrderVO) getBillCardPanel()
					.getBillValueVO(SaleOrderVO.class.getName(),
							SaleorderHVO.class.getName(),
							SaleorderBVO.class.getName());

			UIRefPane ufref = (UIRefPane) getBillCardPanel().getHeadItem(
					"ccalbodyid").getComponent();
			saleorder.getHeadVO().setAttributeValue("ccalbodyname",
					ufref.getUITextField().getText());

//			if (boBusiType.getSelectedChildButton() != null
//					&& boBusiType.getSelectedChildButton().length > 0
//					&& boBusiType.getSelectedChildButton()[0] != null
//					&& boBusiType.getSelectedChildButton()[0].getCode() != null) {
//				saleorder.getHeadVO().setAttributeValue("cbiztypename",
//						boBusiType.getSelectedChildButton()[0].getCode());
//
//			} else {
				ufref = (UIRefPane) getBillCardPanel().getHeadItem("cbiztype")
						.getComponent();
				saleorder.getHeadVO().setAttributeValue("cbiztypename",
						ufref.getUITextField().getText());
//			}

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("ccustomerid")
					.getComponent();
			saleorder.getHeadVO().setAttributeValue("ccustomername",
					ufref.getUITextField().getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid")
					.getComponent();
			saleorder.getHeadVO().setAttributeValue("cdeptname",
					ufref.getUITextField().getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid")
					.getComponent();
			saleorder.getHeadVO().setAttributeValue("cemployeename",
					ufref.getUITextField().getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("cfreecustid")
					.getComponent();
			saleorder.getHeadVO().setAttributeValue("cfreecustname",
					ufref.getUITextField().getText());

			ufref = (UIRefPane) getBillCardPanel()
					.getHeadItem("creceiptcorpid").getComponent();
			saleorder.getHeadVO().setAttributeValue("creceiptcorpname",
					ufref.getUITextField().getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem(
					"creceiptcustomerid").getComponent();
			saleorder.getHeadVO().setAttributeValue("creceiptcustomername",
					ufref.getUITextField().getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("csalecorpid")
					.getComponent();
			saleorder.getHeadVO().setAttributeValue("csalecorpname",
					ufref.getUITextField().getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem(
					"ctermprotocolid").getComponent();
			saleorder.getHeadVO().setAttributeValue("ctermprotocolname",
					ufref.getUITextField().getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("ctransmodeid")
					.getComponent();
			saleorder.getHeadVO().setAttributeValue("ctransmodename",
					ufref.getUITextField().getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem("cwarehouseid")
					.getComponent();
			saleorder.getHeadVO().setAttributeValue("cwarehousename",
					ufref.getUITextField().getText());

			ufref = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			saleorder.getHeadVO().setAttributeValue("vreceiveaddress",
					ufref.getUITextField().getText());

			ufref = (UIRefPane) getBillCardPanel().getTailItem("capproveid")
					.getComponent();
			saleorder.getHeadVO().setAttributeValue("capprovename",
					ufref.getUITextField().getText());

			ufref = (UIRefPane) getBillCardPanel().getTailItem("coperatorid")
					.getComponent();
			saleorder.getHeadVO().setAttributeValue("coperatorname",
					ufref.getUITextField().getText());

			vocache.addSaleOrderVO(saleorder);

			getBillListPanel().getHeadBillModel().addLine();
			getBillListPanel().getHeadBillModel().setBodyRowVO(
					saleorder.getParentVO(),
					getBillListPanel().getHeadBillModel().getRowCount() - 1);
			// ���ñ�ͷ����
			int iselrow = getBillListPanel().getHeadBillModel().getRowCount() - 1;
			try {
				nc.ui.so.so001.panel.bom.BillTools.setMnyToModelByCurrency(
						getBillListPanel().getHeadBillModel(), saleorder
								.getParentVO(), iselrow, getCorpPrimaryKey(),
						saleorder.getBodyVOs()[0].getCcurrencytypeid(),
						new String[] { "npreceivemny", "nreceiptcathmny" });
			} catch (Exception e) {
				handleException(e);
			}
			getBillListPanel().getHeadTable().getSelectionModel()
					.setSelectionInterval(iselrow, iselrow);
		}
	}

	private ArrayList getMustUpdateHeadNames() {
		String[] heads = new String[] { "fstatus", "breceiptendflag",
				"boutendflag", "binvoicendflag", "ibalanceflag", "bpayendflag",
				"btransendflag", "ts", "capproveid", "dapprovedate","capprovename",
				"daudittime"
		};
		ArrayList al = new ArrayList();

		for (int i = 0; i < heads.length; i++)
			al.add(heads[i]);
		return al;
	}

	private ArrayList getMustUpdateBodyNames() {

		String[] heads = new String[] { "csaleid", "corder_bid", "frowstatus",
				"ts", "bifinvoicefinish", "bifpaybalance", "bifpayfinish",
				"bifreceivefinish", "bifinventoryfinish", "bifpaysign",
				"biftransfinish", "dlastconsigdate", "dlasttransdate",
				"dlastoutdate", "dlastinvoicedt", "dlastpaydate",
				"ntotalreturnnumber", "ntotalcarrynumber", "ntaltransnum",
				"ntranslossnum",
				"ntotalreceivenumber", "ntotalinvoicenumber",
				"ntotalbalancenumber", "ntotalpaymny", "ntotalinventorynumber",
				"ntotalsignnumber", "ntotalcostmny", "narrangescornum",
				"narrangepoapplynum", "narrangetoornum", "norrangetoapplynum",
				"ntotlbalcostnum", "barrangedflag", "carrangepersonid",
				"narrangemonum", "ts"

		};

		ArrayList al = new ArrayList();

		for (int i = 0; i < heads.length; i++)
			al.add(heads[i]);
		return al;
	}

	public BillListTools getBillListTools() {
		if (listtools == null) {
			listtools = new BillListTools(getBillListPanel().getBillListData());
		}
		return listtools;
	}
	
	protected ButtonObject getBoRefundmentDocument() {
		if (boRefundmentDocument == null) {
			boRefundmentDocument = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000026")/* @res "�ĵ�����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000026")/* @res "�ĵ�����" */, "�ĵ�����"); /*-=notranslate=-*/
			boRefundmentDocument.setTag("document");
		}
		return boRefundmentDocument;
	}

	protected ButtonObject getBoDocument() {
		if (boDocument == null) {
			boDocument = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000026")/* @res "�ĵ�����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000026")/* @res "�ĵ�����" */, "�ĵ�����"); /*-=notranslate=-*/
			boDocument.setTag("document");
		}
		return boDocument;
	}

	protected ButtonObject getBoRefundment() {
		if (boRefundment == null) {
			boRefundment = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000151")/* @res "�˻�" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000151")/* @res "�˻�" */, "�˻�"); /*-=notranslate=-*/
		}
		return boRefundment;
	}

	protected ButtonObject getBoCard() {
		if (boCard == null) {
			boCard = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UCH021")/*
			 * @res "��Ƭ"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH021")/*
			 * @res
			 * "�л������ݿ�Ƭ"
			 */, 2, "��Ƭ��ʾ"); /*-=notranslate=-*/
		}
		return boCard;
	}

	protected ButtonObject getBoBatch() {
		if (boBatch == null) {
			boBatch = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000151")/* @res "�˻�" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000151")/* @res "�˻�" */, "�˻�"); /*-=notranslate=-*/
		}
		return boBatch;
	}

	protected ButtonObject getBoPreview() {
		if (boPreview == null) {
			boPreview = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000032")/* @res "Ԥ��" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000306")/* @res "Ԥ������" */, 0, "Ԥ��"); /*-=notranslate=-*/
		}
		return boPreview;
	}

	protected ButtonObject getBoPrint() {
		if (boPrint == null) {
			boPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000007")/* @res "��ӡ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000169")/* @res "��ӡ����" */, 0, "��ӡ"); /*-=notranslate=-*/
		}
		return boPrint;
	}

	protected ButtonObject getBoSplitPrint() {
		if (boSplitPrint == null) {
			boSplitPrint = new ButtonObject("�ֵ���ӡ"/* @res "�ֵ���ӡ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000169")/* @res "��ӡ����" */, 0, "�ֵ���ӡ"); /*-=notranslate=-*/
		}
		return boSplitPrint;
	}
	
	protected ButtonObject getBoEdit() {
		if (boEdit == null) {
			boEdit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000045")/* @res "�޸�" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000291")/* @res "�޸ĵ���" */, 2, "�޸�"); /*-=notranslate=-*/
		}
		return boEdit;
	}

	protected ButtonObject getBoOrderQuery() {
		if (boOrderQuery == null) {
			boOrderQuery = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000033")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000033")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		}
		return boOrderQuery;
	}

	protected ButtonObject getBoSendAudit() {
		if (boSendAudit == null) {
			boSendAudit = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000034")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000034")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		}
		return boSendAudit;
	}

	protected ButtonObject getBoAuditFlowStatus() {
		if (boAuditFlowStatus == null) {
			boAuditFlowStatus = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000025")/* @res "������״̬" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000025")/* @res "������״̬" */, 0, "������״̬");
			boAuditFlowStatus.setTag("auditflowstatus");
		}
		return boAuditFlowStatus;
	}

	protected ButtonObject getBoCachPay() {
		if (boCachPay == null) {
			boCachPay = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000028")/* @res "�����տ�" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000028")/* @res "�����տ�" */, "�����տ�"); /*-=notranslate=-*/
		}
		return boCachPay;
	}

	protected ButtonObject getBoListDeselectAll() {
		if (boListDeselectAll == null) {
			boListDeselectAll = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000042")/* @res "ȫ��" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000042")/* @res "ȫ��ȡ��" */, 2, "ȫ��"); /*-=notranslate=-*/
		}
		return boListDeselectAll;
	}

	protected ButtonObject getBoListSelectAll() {
		if (boListSelectAll == null) {
			boListSelectAll = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000041")/* @res "ȫѡ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000041")/* @res "ȫ��ѡ��" */, 2, "ȫѡ"); /*-=notranslate=-*/
		}
		return boListSelectAll;
	}

	protected ButtonObject getBoBrowse() {
		if (boBrowse == null) {
			boBrowse = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000021")/* @res "���" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000148")/*
					 * @res "�������"
					 */, 1, "���"); /*-=notranslate=-*/
		}
		return boBrowse;
	}

	protected ButtonObject getBoFind() {
		if (boFind == null) {
			boFind = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000089")/* @res "��λ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000168")/* @res "��λ����" */, 0, "��λ"); /*-=notranslate=-*/
		}
		return boFind;
	}

	protected ButtonObject getBoQuery() {
		if (boQuery == null) {
			boQuery = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000006")/* @res "��ѯ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000034")/*
					 * @res "��ѯ����"
					 */, 0, "��ѯ"); /*-=notranslate=-*/
		}
		return boQuery;
	}

	protected ButtonObject getBoMaintain() {
		if (boMaintain == null) {
			boMaintain = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000538")/* @res "ά��" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000538")/* @res "ά��" */, "ά��");
		}
		return boMaintain;
	}

	protected ButtonObject getBoCancel() {
		if (boCancel == null) {
			boCancel = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000008")/* @res "ȡ��" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000008")/* @res "ȡ��" */, 0, "ȡ��"); /*-=notranslate=-*/
		}
		return boCancel;
	}

	protected ButtonObject getBoLine() {
		if (boLine == null) {
			boLine = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000011")/* @res "�в���" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000011")/* @res "�в���" */, 0, "�в���"); /*-=notranslate=-*/
		}
		return boLine;
	}

	protected ButtonObject getBoAddLine() {
		if (boAddLine == null) {
			boAddLine = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000012")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000012")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		}
		return boAddLine;
	}

	protected ButtonObject getBoDelLine() {
		if (boDelLine == null) {
			boDelLine = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000013")/* @res "ɾ��" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000013")/* @res "ɾ��" */, 0, "ɾ��"); /*-=notranslate=-*/
		}
		return boDelLine;
	}

	protected ButtonObject getBoInsertLine() {
		if (boInsertLine == null) {
			boInsertLine = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000016")/* @res "������" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000016")/* @res "������" */, "������"); /*-=notranslate=-*/
		}
		return boInsertLine;
	}

	protected ButtonObject getBoCopyLine() {
		if (boCopyLine == null) {
			boCopyLine = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000014")/* @res "������" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000014")/* @res "������" */, 0, "������"); /*-=notranslate=-*/
		}
		return boCopyLine;
	}
	
	protected ButtonObject getBoPasteLine() {
		if (boPasteLine == null) {
			boPasteLine = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000015")/* @res "ճ����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000015")/* @res "ճ����" */, 0, "ճ����"); /*-=notranslate=-*/
		}
		return boPasteLine;
	}
	
	protected ButtonObject getBoPasteLineToTail() {
		if (boPasteLineToTail == null) {
			boPasteLineToTail = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "SCMCOMMON000000266")/* @res "ճ���е���β" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"SCMCOMMON000000266")/* @res "ճ���е���β" */, 0, "ճ���е���β"); /*-=notranslate=-*/
		}
		return boPasteLineToTail;
	}
	
	protected ButtonObject getBoFindPrice() {
		if (boFindPrice == null) {
			boFindPrice = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060207",
							"UPT40060207-000020")/* @res "ѯ��" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060207",
							"UPT40060207-000020")/* @res "ѯ��" */, 0, "ѯ��"); /*-=notranslate=-*/
		}
		return boFindPrice;
	}
	
	protected ButtonObject getBoCardEdit() {
		if (boCardEdit == null) {
			boCardEdit = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"SCMCOMMON000000267")/* @res "��Ƭ�༭" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"SCMCOMMON000000267")/* @res "��Ƭ�༭" */, 0, "��Ƭ�༭"); /*-=notranslate=-*/
		}
		return boCardEdit;
	}
	
	protected ButtonObject getBoResortRowNo() {
		if (boResortRowNo == null) {
			boResortRowNo = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"SCMCOMMON000000283")/* @res "�����к�" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"SCMCOMMON000000283")/* @res "�����к�" */, 0, "�����к�"); /*-=notranslate=-*/
		}
		return boResortRowNo;
	}

	protected ButtonObject getBoBlankOut() {
		if (boBlankOut == null) {
			boBlankOut = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000039")/* @res "ɾ��" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000039")/* @res "ɾ��" */, 3, "ɾ��"); /*-=notranslate=-*/
			boBlankOut.setTag("SoBlankout");
		}
		return boBlankOut;
	}

	protected ButtonObject getBoCopyBill() {
		if (boCopyBill == null) {
			boCopyBill = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000043")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000043")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		}
		return boCopyBill;
	}

	protected ButtonObject getBoAction() {
		if (boAction == null) {
			boAction = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000026")/* @res "ִ��" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000026")/* @res "ִ��" */, 0, "ִ��"); /*-=notranslate=-*/
		}
		return boAction;
	}

	protected ButtonObject getBoAudit() {
		if (boAudit == null) {
			boAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000027")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000027")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
			boAudit.setTag("APPROVE");
		}
		return boAudit;
	}

	protected ButtonObject getBoCancelAudit() {
		if (boCancelAudit == null) {
			boCancelAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000028")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000214")/* @res "ȡ����������" */, 0, "����"); /*-=notranslate=-*/
			boCancelAudit.setTag("SoUnApprove");
		}
		return boCancelAudit;
	}

	protected ButtonObject getBoModification() {
		if (boModification == null) {
			boModification = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000290")/* @res "�޶�" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000159")/* @res "�޶�����" */, 3, "�޶�"); /*-=notranslate=-*/
		}
		return boModification;
	}

	protected ButtonObject getBoCancelFreeze() {
		if (boCancelFreeze == null) {
			boCancelFreeze = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000031")/* @res "�ⶳ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000156")/* @res "�ⶳ����" */, 0, "�ⶳ"); /*-=notranslate=-*/
			boCancelFreeze.setTag("OrderUnFreeze");
		}
		return boCancelFreeze;
	}

	protected ButtonObject getBoFreeze() {
		if (boFreeze == null) {
			boFreeze = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000030")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000155")/* @res "���ᵥ��" */, 0, "����"); /*-=notranslate=-*/
			boFreeze.setTag("OrderFreeze");
		}
		return boFreeze;
	}

	protected ButtonObject getBoFinish() {
		if (boFinish == null) {
			boFinish = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000119")/* @res "�ر�" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000119")/* @res "�ر�" */, 0, "�ر�"); /*-=notranslate=-*/
			boFinish.setTag("OrderFinish");
		}
		return boFinish;
	}

	protected ButtonObject getBoRefresh() {
		if (boRefresh == null) {
			boRefresh = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000009")/* @res "ˢ��" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000009")/* @res "ˢ��" */, "ˢ��");
		}
		return boRefresh;
	}

	protected ButtonObject getBoFirst() {
		if (boFirst == null) {
			boFirst = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UCH031")/* @res "��ҳ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000088")/* @res "��һ�ŵ���" */, 0, "��ҳ"); /*-=notranslate=-*/
		}
		return boFirst;
	}

	protected ButtonObject getBoPre() {
		if (boPre == null) {
			boPre = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"SCMCOMMON000000163")/* @res "��ҳ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000166")/* @res "��һ�ŵ���" */, 0, "��ҳ"); /*-=notranslate=-*/
		}
		return boPre;
	}

	protected ButtonObject getBoNext() {
		if (boNext == null) {
			boNext = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000023")/* @res "��ҳ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000167")/* @res "��һ�ŵ���" */, 0, "��ҳ"); /*-=notranslate=-*/
		}
		return boNext;
	}

	protected ButtonObject getBoLast() {
		if (boLast == null) {
			boLast = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000062")/* @res "ĩҳ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000342")/* @res "���һ�ŵ���" */, 0, "ĩҳ"); /*-=notranslate=-*/
		}
		return boLast;
	}

	protected ButtonObject getBoAssistant() {
		if (boAssistant == null) {
			boAssistant = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000160")/* @res "��������" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000160")/* @res "��������" */, "��������"); /*-=notranslate=-*/
		}
		return boAssistant;
	}

	protected ButtonObject getBoOrdBalance() {
		if (boOrdBalance == null) {
			boOrdBalance = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000030")/* @res "�տ����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000030")/* @res "�տ����" */, "�տ����"); /*-=notranslate=-*/
		}
		return boOrdBalance;
	}

	protected ButtonObject getBoStockLock() {
		if (boStockLock == null) {
			boStockLock = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC000-0001824")/* @res "���Ӳ����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC000-0001824")/* @res "���Ӳ����" */, "���Ӳ����"); /*-=notranslate=-*/
			boStockLock.setTag("StockLock");
		}
		return boStockLock;
	}
	
	protected ButtonObject getBoSendInv() {
		if (boSendInv == null) {
			boSendInv = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40060301", "UPT40060301-000571")/* @res "��������" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000571")/* @res "��������" */, "��������"); /*-=notranslate=-*/
			boSendInv.setTag(SENDINV);
		}
		return boSendInv;
	}
	
	protected ButtonObject getBoSupplyInv() {
		if (boSupplyInv == null) {
			boSupplyInv = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40060301", "UPT40060301-000572")/* @res "��������" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000572")/* @res "��������" */, "��������"); /*-=notranslate=-*/
			boSupplyInv.setTag(SUPPLYINV);
		}
		return boSupplyInv;
	}
	
	protected ButtonObject getBoDirectInv() {
		if (boDirectInv == null) {
			boDirectInv = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40060301", "UPT40060301-000573")/* @res "ֱ�˰���" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000573")/* @res "ֱ�˰���" */, "ֱ�˰���"); /*-=notranslate=-*/
			boDirectInv.setTag(DIRECTINV);
		}
		return boDirectInv;
	}

	protected ButtonObject getBoAsstntQry() {
		if (boAsstntQry == null) {
			boAsstntQry = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000056")/* @res "������ѯ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000056")/* @res "������ѯ" */, "������ѯ");
		}
		return boAsstntQry;
	}

	protected ButtonObject getBoOnHandShowHidden() {
		if (boOnHandShowHidden == null) {
			boOnHandShowHidden = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000509")/* @res "�л�" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000509")/* @res "�ִ�����ʾ/����" */, 2,
					"������ʾ/����");/*-=notranslate=-*/
		}
		return boOnHandShowHidden;
	}

	protected ButtonObject getBoCustCredit() {
		if (boCustCredit == null) {
			boCustCredit = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000035")/* @res "�ͻ�����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
							"UPT40060301-000035")/* @res "�ͻ�����" */, 0, "�ͻ�����"); /*-=notranslate=-*/
			boCustCredit.setTag("CustCredited");
		}
		return boCustCredit;
	}

	protected ButtonObject getBoOrderExecRpt() {
		if (boOrderExecRpt == null) {
			boOrderExecRpt = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060302",
							"UPT40060302-000040")/* @res "����ִ�����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060302",
							"UPT40060302-000040")/* @res "����ִ�����" */, 0,
					"����ִ�����"); /*-=notranslate=-*/
			boOrderExecRpt.setTag("OrderExecRpt");
		}
		return boOrderExecRpt;
	}

	protected ButtonObject getBoCustInfo() {
		if (boCustInfo == null) {
			boCustInfo = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060302",
							"UPT40060302-000041")/* @res "�ͻ����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060302",
							"UPT40060302-000041")/* @res "�ͻ����" */, 0, "�ͻ���Ϣ"); /*-=notranslate=-*/
			boCustInfo.setTag("CustInfo");
		}
		return boCustInfo;
	}

	protected ButtonObject getBoPrifit() {
		if (boPrifit == null) {
			boPrifit = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060302",
							"UPT40060302-000042")/* @res "ë��Ԥ��" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40060302",
							"UPT40060302-000042")/* @res "ë��Ԥ��" */, 0, "ë��Ԥ��"); /*-=notranslate=-*/
			boPrifit.setTag("Prifit");
		}
		return boPrifit;
	}

	protected ButtonObject getBoPrintMgr() {
		if (boPrntMgr == null) {
			boPrntMgr = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"SCMCOMMON000000225")/* @res "��ӡ����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"SCMCOMMON000000225")/* @res "��ӡ����" */, "��ӡ����");
		}
		return boPrntMgr;
	}

	protected ButtonObject getBoAdd() {
		if (boAdd == null) {
			boAdd = new ButtonObject(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000287")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000288")/* @res "��������" */, 1, "����"); /*-=notranslate=-*/
		}
		return boAdd;
	}

	protected ButtonObject getBoSave() {
		if (boSave == null) {
			boSave = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000001")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000008")/* @res "���浥��" */, 3, "����"); /*-=notranslate=-*/
		}
		return boSave;
	}

	protected ButtonObject getBoReturn() {
		if (boReturn == null) {
			boReturn = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("common", "UCH022")/*
			 * @res "����"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH022")/*
			 * @res
			 * "���ص����б�"
			 */, 0, "�б���ʾ"); /*-=notranslate=-*/
		}
		return boReturn;
	}

	protected ButtonObject getBoBack() {
		if (boBack == null) {
			boBack = new ButtonObject("����", "����", "����");
		}
		return boBack;
	}

}
