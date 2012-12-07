package nc.ui.so.so001.panel.card;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.table.DefaultTableCellRenderer;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.ui.arap.badacc.OnDeleteButtonHandler;
import nc.ui.ic.pub.QueryOnHandInfoPanel;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.service.IQueryOnHandInfoPanel;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.bill.BillActionListener;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.action.BillTableLineAction;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.scm.extend.IFuncExtend;
import nc.ui.scm.freecust.UFRefGridUI;
import nc.ui.scm.plugin.InvokeEventProxy;
import nc.ui.scm.pub.InvoInfoBYFormula;
import nc.ui.scm.pub.ctrl.BillLineInfoListener;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.ref.prm.CustAddrRefModel;
import nc.ui.scm.so.SaleBillType;
import nc.ui.so.pub.ColoredTableCellRenderer;
import nc.ui.so.pub.InvAttrCellRenderer;
import nc.ui.so.pub.UITimeTextField;
import nc.ui.so.so001.SaleOrderBO_Client;
import nc.ui.so.so001.panel.bom.BillTools;
import nc.ui.so.so001.panel.bom.SaleBillBomUI;
import nc.ui.so.so001.panel.list.SaleOrderVOCache;
import nc.vo.bd.def.DefVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.OnHandRefreshVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.pub.SOCurrencyRateUtil;
import nc.vo.so.so001.SORowData;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so001.SoVoConst;

/**
 * ���۶���UI�˳������(��Ҫ����Ƭģ�����)
 * 
 * @author zhongwei
 * 
 */
public abstract class SaleBillCardUI extends SaleBillBomUI implements
		BillLineInfoListener, ICheckRetVO ,BillActionListener{

	protected SOBillCardPanel cardpanel = null;

	public SaleOrderVOCache vocache = new SaleOrderVOCache();

	public String id = ""; // ��ǰID

	// �۸����
	public UFBoolean SA_02 = null; // ���ۺ�˰

	public UFBoolean SA_50 = null; // �����Ƿ�ѯ��
	
	public UFBoolean SO78 = null; // ���۶����޶��Ƿ�ѯ��

	protected UFBoolean SO_17 = null; // �Ƿ�ִ�к�ͬ�۸�
	
	public UFBoolean SA_39 = null; // ѯ�������Ƿ������޸ļ۸�

	protected UFBoolean SA_40 = null; // ѯ�����Ƿ������޸ļ۸�

	protected UFBoolean SA_41 = null; // �Ƿ������޸��ۿ�

	// ���ݲ���
	protected Integer SO_01 = null; // ��������

	protected UFBoolean SO_03 = null; // ͬһ�����Ƿ���ж���

	protected UFBoolean SO_14 = null;

	public UFBoolean SO_20 = new UFBoolean("Y"); // ���۹����Ƿ�ʹ��ͨ��ģ��

	//public UFBoolean SA_16 = null; // ���鷢��

	protected UFBoolean IC003 = null; // �Ƿ񳬶�������

	protected UFDouble IC004 = null; // �����������ݲ�

	protected UFBoolean SO25 = null; // �Ƿ񳬶���(���ⵥ)��Ʊ

	protected UFDouble SO26 = null; // ��������Ʊ�ݲ�

	protected UFBoolean SO23 = null; // �Ƿ񳬶�������

	protected UFDouble SO24 = null; // �������۶�������%

	protected UFDouble SO29 = null; // �����ر�����%

	protected UFDouble SO43 = null; // ؛Դ�����ݲ����
	
	protected String SO40 = null; // �����ۻ��ǵ���Ʒ�ۿۣ�Ĭ�ϵ��ۿۣ�
	
	protected UFBoolean SO72 = null;//���ֶ����Ƿ���Զ����տ�տ����

	// yt add 2003-10-16
	// yt update 2003-10-27
	protected String SO_21 = null; // Ԥ������
	

	// ��������
	protected UFBoolean BD302 = null; // �Ƿ������Һ���

	public UFBoolean SO59 = null; // ���۶���/���۷�Ʊ�ϵ���Ʒ���Ƿ�������/��,Ĭ��Ϊ���ǡ�20070104

	public Integer BD502 = null; // ����������С��λ��

	public Integer BD503 = null; // ������

	public Integer BD505 = null; // ����С��λ��

	// DRP����
	protected UFBoolean DRP04 = null; // �������Ƿ��ϸ�ִ��

	public BusinessCurrencyRateUtil currtype = null; // ����

	// �޸�ǰ���ݺ�
	protected String m_oldreceipt = null;

	protected BillTempletVO billtempletVO = null;

	// �����Զ������ͷ
	protected DefVO[] head_defs = null;

	// �����Զ��������
	protected DefVO[] body_defs = null;

	protected Hashtable hsparas = null;

	// �Ƿ���������ѯ��
	protected UFBoolean SA34 = null;

	// �Ƿ��ϸ��ղ�Ʒ�߽���Ӧ�յĳ��
	protected UFBoolean SO27 = null;

	// ����ATP��������Ĵ���ʽ(v5.3ȥ��)
	//protected String SO28 = null;

	protected UFDouble SO34 = null;

	// ���۶��������Ƿ�������
	protected UFBoolean SO45 = null;

	// �޸Ķ����Ƿ��޸��Ƶ���
	protected UFBoolean SO41 = null;

	protected boolean binitFuncExtend = false;

	protected UISplitPane pnlCardAndBc = null;

	private SOBillCardTools cardtools;
	
	private SOCurrencyRateUtil socrutil;

	// ��������
	private int iCopyRowCount = 0;

	// �����м�������
	private Object[] oCopy = null;

	// ��ʾ���������ִ������
	protected boolean m_bOnhandShowHidden = false;

	// ������״̬
	public Vector vRowATPStatus = new Vector();

	protected UIPanel m_pnlOnHand = null;

	protected IFuncExtend m_funcExtend = null;

	// ���л�ʱ�ִ�����ʾ��ڲ���
	protected OnHandRefreshVO m_voLineOnHand = new OnHandRefreshVO();

	// �������۰�ť
	public ButtonObject boBom = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
					"UPT40060301-000031")/* @res "��������" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
					"UPT40060301-000031")/* @res "��������" */, 0, "��������"); /*-=notranslate=-*/

	public ButtonObject boStockLock;
	
	//��������
	public ButtonObject boSendInv;
	public static String SENDINV = "SendInv";
	
	//��������
	public ButtonObject boSupplyInv;
	public static String SUPPLYINV = "SupplyInv";
	
	//ֱ�˰���
	public ButtonObject boDirectInv;
	public static String DIRECTINV = "DirectInv";

	protected ButtonObject boAddLine;

	protected ButtonObject boDelLine;

	protected ButtonObject boInsertLine;

	protected ButtonObject boCopyLine;

	protected ButtonObject boPasteLine;
	
	protected ButtonObject boPasteLineToTail;
	
	protected ButtonObject boFindPrice;
	
	protected ButtonObject boResortRowNo;
	
	protected ButtonObject boCardEdit;

	public ButtonObject boBusiType = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC001-0000003")/* @res "ҵ������" */, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000293")/* @res "ѡ��ҵ������" */, 1,
			"ҵ������"); /*-=notranslate=-*/

	// ԭ����ɫ
	private Color initColor = null;

	class myCellRenderer extends ColoredTableCellRenderer {
		public Component getTableCellRendererComponent(
				javax.swing.JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			Component comp = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			if (initColor == null) {
				initColor = getForeground();
			}
			if (vRowATPStatus == null || vRowATPStatus.size() == 0
					|| row >= vRowATPStatus.size()) {
				setForeground(initColor);
			} else if (vRowATPStatus.elementAt(row) == null ? false
					: ((UFBoolean) vRowATPStatus.elementAt(row)).booleanValue()) {
				setForeground(java.awt.Color.red);
			} else {
				setForeground(initColor);
			}
			Color background = getBackGround(row, column);
			if (background != null && table.getModel() instanceof BillModel) {
				setBackground(background);
			}
			else {
				setBackground(null);
			}
			return comp;
		}
	}

	public SaleBillCardUI() {
		// initialize();
	}

	public SaleBillCardUI(String pk_corp, String billtype, String busitype,
			String operator, String id) {
		initialize(pk_corp, billtype, busitype, operator, id);
	}

	@Override
	protected void postInit() {
		initialize();
	}

	/**
	 * ��ʼ���ࡣ
	 */
	protected void initialize() {
		long st = System.currentTimeMillis();

		// ���ý���
		try {
			setName("SaleOrder");
			setSize(774, 419);
			add(getSplitPanelBc(), "Center");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}

		initVars(null);

		initCurrency();

		// ���Ի���ť
		initButtons();

		loadCardTemplet();

		strState = "����";

		// ����Ĭ��ҵ������
		getBillCardPanel().setBusiType(boBusiType.getTag());

		SCMEnv
				.out(" initialize cost time "
						+ (System.currentTimeMillis() - st));

	}

	protected UISplitPane getSplitPanelBc() {
		if (pnlCardAndBc == null) {

			pnlCardAndBc = new nc.ui.pub.beans.UISplitPane(
					nc.ui.pub.beans.UISplitPane.VERTICAL_SPLIT);
			pnlCardAndBc.setName("card2");
			pnlCardAndBc.add(getBillCardPanel(),
					nc.ui.pub.beans.UISplitPane.TOP);
		}

		return pnlCardAndBc;

	}

	/**
	 * ���Ի����ݱ�����ͨ��һ��Զ�̵������
	 * 
	 * �������ڣ�(2001-11-15 9:02:22)
	 * 
	 */
	private void initVars(String pkcorp) {

		String salecorp = null;

		if (pkcorp == null || pkcorp.trim().length() <= 0)
			salecorp = getClientEnvironment().getCorporation().getPrimaryKey();
		else
			salecorp = pkcorp;

		// ������ȡ��ʼ����
		String syParas[] = { "SA02", "SA15", "SO01", "SO03", "BD302",
				"BD501", "BD502", "BD503", "BD505", "SO14", "SO17", "SO20",
				"SO21", "SO27", "SO21", "SA34", "SA13", "SO34", "SO45",
				"SO41", "IC090", "SO25", "SO26", "SO23", "SO24",
				"SO43", "SO29", "SO59","SO40","SA39","SA40","SA41","SA50","SO72","SO78" };

		// ������ȡ
		try {
	
			hsparas = SysInitBO_Client.queryBatchParaValues(salecorp, syParas);

			if (hsparas != null)
				hsparas.put("SA09", SysInitBO_Client.getParaBoolean("0001", "SA09"));

			head_defs = DefSetTool.getDefHead(salecorp, getBillType());

			body_defs = DefSetTool.getDefBody(salecorp, getBillType());

		} catch (Exception e) {
			SCMEnv.out(e);
			// e.printStackTrace();
		}

		// ���ز���
		getSystemPara();

	}

	/**
	 * ���ϵͳ������
	 * 
	 * �������ڣ�(2001-8-8 9:26:11)
	 * 
	 * �޸����ڣ�2003-10-16 �޸��ˣ����� �޸����ݣ�����SO_21��ȡֵ��Ԥ�����ȣ�Y-������N-��
	 * 
	 * �޸����ڣ�2003-10-27 �޸��ˣ����� �޸����ݣ�SO_21��ȡֵ�����������͡�����
	 * 
	 */
	private void getSystemPara() {
		try {
			String para[] = { "SA02", "SA15", "SO01", "SO03", "SO14",
					"SO17", "SO20", "SO21", "SO22", "SO23", "SO24", "SO25",
					"SO26", "SO27", "SO29", "SO34", "SO41", "SO43", "SO45",
					"IC090", "BD302", "BD501", "BD502",
					"BD503", "BD505", "SA34", "SO59","SO40","SA39","SA40","SA41","SA50","SO72","SO78" };

			String para0001[] = { "SA13" };
			java.util.Hashtable h = hsparas;
			Hashtable h0001 = new Hashtable();
			String sCurrPkCorp = ClientEnvironment.getInstance()
					.getCorporation().getPk_corp();
			if (h == null) {
				h = SysInitBO_Client.queryBatchParaValues(sCurrPkCorp, para);
			}
			h0001 = SysInitBO_Client.queryBatchParaValues("0001", para0001);

			SA_39 = getParaBoolean(h, "SA39");
			SA_40 = getParaBoolean(h, "SA40");
			SA_41 = getParaBoolean(h, "SA41");
			
			SA_15 = getParaBoolean(h, "SA15");
			if (SA_15 == null)
				SA_15 = UFBoolean.FALSE;
			SA_50 = getParaBoolean(h, "SA50");
			if (SA_50 == null)
				SA_50 = UFBoolean.FALSE;
			
			SO72 = getParaBoolean(h, "SO72");
			if (SO72==null)
				SO72 = new UFBoolean(false);

			SO78 = getParaBoolean(h, "SO78");
			if (SO78==null)
				SO78 = new UFBoolean(false);
			
			//��	V5.3ȥ����
			/*if (h.get("SO46") == null)
				SA_16 = SoVoConst.buftrue;
			else
				SA_16 = getParaBoolean(h, "SO46");// "SA16");*/			
			SO27 = getParaBoolean(h, "SO27");
			SO_01 = getParaInt(h, "SO01");
			SO_03 = getParaBoolean(h, "SO03");
			SO_14 = getParaBoolean(h, "SO14");
			SO_17 = getParaBoolean(h, "SO17");

			//SO28 = (String) h.get("IC090");//��V5.3ȥ����

			BD302 = getParaBoolean(h, "BD302");
			BD501 = getParaInt(h, "BD501");
			BD502 = getParaInt(h, "BD502");
			BD503 = getParaInt(h, "BD503");
			BD505 = getParaInt(h, "BD505");
			DRP04 = getParaBoolean(h, "DRP04");
			SO_20 = getParaBoolean(h, "SO20");

			if (SO_20 == null)
				SO_20 = new UFBoolean(true);

			// yt update 2003-10-27
			SO_21 = (String) h.get("SO21");
			if (SO_21 == null)
				SO_21 = "����";/*-=notranslate=-*/

			// ���ۺ�˰ͬʱ���Ǽ��ź͹�˾
			String SA13 = (String) h0001.get("SA13");
			if ("���Ŷ���".equals(SA13)) {/*-=notranslate=-*/
				SA_02 = getParaBoolean(h, "SA09");
			} else {
				SA_02 = getParaBoolean(h, "SA02");
			}

			if (SA_02 == null) {
				SA_02 = getParaBoolean(h, "SA02");
			}
			if (SA_02 == null)
				SA_02 = UFBoolean.FALSE;

			// ѯ���������ã�ѡ��Ϊ�������͡��ͻ���Ĭ��Ϊ��������
			String stemp = (String) h.get("SA34");
			if ("��������".equals(stemp))/*-=notranslate=-*/
				SA34 = new UFBoolean(true);
			else
				SA34 = new UFBoolean(false);

			String svalue = (String) h.get("SO34");
			if (svalue != null && svalue.trim().length() > 0) {
				SO34 = new UFDouble(svalue.trim());
			} else {
				SO34 = new UFDouble(1.0);
			}

			SO45 = getParaBoolean(h, "SO45");
			if (SO45 == null)
				SO45 = SoVoConst.buffalse;

			SO41 = getParaBoolean(h, "SO41");
			if (SO41 == null)
				SO41 = SoVoConst.buffalse;

			SO25 = getParaBoolean(h, "SO25");
			svalue = (String) h.get("SO26");
			if (svalue != null && svalue.trim().length() > 0) {
				SO26 = new UFDouble(svalue.trim());
			} else {
				SO26 = new UFDouble(0);
			}
			svalue = (String) h.get("SO43");
			if (svalue != null && svalue.trim().length() > 0) {
				SO43 = new UFDouble(svalue.trim());
			} else {
				SO43 = new UFDouble(0);
			}

			SO23 = getParaBoolean(h, "SO23");
			IC003 = SO23;
			svalue = (String) h.get("SO24");
			if (svalue != null && svalue.trim().length() > 0) {
				SO24 = new UFDouble(svalue.trim());
			} else {
				SO24 = new UFDouble(0);
			}
			IC004 = SO24;
			svalue = (String) h.get("SO29");
			if (svalue != null && svalue.trim().length() > 0) {
				SO29 = new UFDouble(svalue.trim());
			} else {
				SO29 = new UFDouble(0);
			}
			SO59 = getParaBoolean(h, "SO59");
			if (SO59 == null)
				SO59 = SoVoConst.buftrue;
			
			SO40 = (String) h.get("SO40");

		} catch (Exception e) {
			SCMEnv.out("ϵͳ������ȡʧ��!");
			handleException(e);
		}
	}

	/**
	 * ��ò������Ͳ����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private UFBoolean getParaBoolean(java.util.Hashtable h, String key) {
		return nc.vo.scm.bd.SmartVODataUtils.getUFBoolean(h.get(key));
	}

	/**
	 * ����������Ͳ����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private Integer getParaInt(java.util.Hashtable h, String key) {
		return nc.vo.scm.bd.SmartVODataUtils.getInteger(h.get(key));
	}

	/**
	 * ��ʼ�����֡�
	 * 
	 * �������ڣ�(2001-11-1 13:25:16)
	 * 
	 */
	private void initCurrency() {
		try {
			currtype = new BusinessCurrencyRateUtil(getCorpPrimaryKey());
		} catch (Exception exp) {
			SCMEnv.out(exp);
			throw new BusinessRuntimeException(exp.getMessage());
		}
	}

	protected abstract void initButtons();

	public abstract void setButtonsState();

	/**
	 * ��ýڵ�š� �������ڣ�(2001-11-27 13:51:07)
	 * 
	 * @return java.lang.String
	 */
	public abstract String getNodeCode();

	/**
	 * ���ؿ�Ƭģ�塣
	 * 
	 * �������ڣ�(2001-11-15 9:03:35)
	 * 
	 */
	private void loadCardTemplet() {
		// ����ҵ������
		for (int i = 0; i < boBusiType.getChildCount(); i++) {
			if (boBusiType.getChildButtonGroup()[i].isSelected()) {
				getBillCardPanel().setBusiType(
						boBusiType.getChildButtonGroup()[i].getTag());
				break;
			}
		}

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000136")/* @res "��ʼ����ģ��...." */);

		// modify by river for 2012-12-05
		// �޸�ģ��ΪPTA����ģ�壺���۶���(PTA) ����ѯ�� pub_systemplate �� 
		// �޸��˽ڵ�ţ���һ�������������ڶ��ο�����������ӹ��ܽڵ�Ĭ��ģ��
		billtempletVO = getBillCardPanel().getTempletData("HQ010601",
				SO_20.booleanValue() ? null : getBillCardPanel().getBusiType(),
				getClientEnvironment().getUser().getPrimaryKey(),
				getCorpPrimaryKey());
		
		// river
//		if(billtempletVO != null) {
//			billtempletVO.getParentVO().setAttributeValue("strBillTempletName", value);
//		}

		SOBillCardTools.addExeTs(billtempletVO);
		BillData bd = new BillData(billtempletVO);

		billtempletVO = null;

		BillItem bm = bd.getHeadItem("naccountperiod");
		if (bm != null) {
			bm.setDecimalDigits(0);
		}

		SOBillCardTools.processCTBillItem(bd, null);

		// �ı����
		setCardPanel(bd);

		// ���ý��棬��������Դ		
		getBillCardPanel().setBillData(bd);	
		
		getBillCardPanel().addSortLstn();
		getBillCardPanel().addSortRelaLstn();
		getBillCardPanel().addEditLstn();
		getBillCardPanel().addEditLstn2();
		getBillCardPanel().addTotalLstn();
		getBillCardPanel().addSortPrepareListener();

		getBillCardPanel().getBodyPanel().setAutoAddLine(false);
		getBillCardPanel().getBodyPanel().getTable().addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void keyReleased(KeyEvent e) {
				/** ���༭���һ�д���󣬰������¡������ظ������ж�����ɾ��һ���ֲ�* */
				if ((e.getKeyCode() == KeyEvent.VK_DOWN) && (getBillCardPanel().binvedit)) {
					int row = getBillCardPanel().getRowCount() - 2;
					int col = getBillCardPanel().getBodyColByKey("cinventorycode");
					col = getBillCardPanel().getBillTable("table").convertColumnIndexToView(col);

					onDelLine(new int[] { row + 1 });
					getBillCardPanel().binvedit = false;

					getBillCardPanel().getBillTable().setRowSelectionInterval(row, row);
					getBillCardPanel().getBillTable().setColumnSelectionInterval(col, col);

					// getBillCardPanel().transferFocusTo(IBillItem.BODY);
				}// end if

			}

			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		// ����ҵ�����Ͳ���
		UIRefPane biztypeRef = (UIRefPane) getBillCardPanel().getHeadItem(
				"cbiztype").getComponent();
		String sWherePart = biztypeRef.getRefModel().getWherePart();
		sWherePart += " AND pk_busitype IN (SELECT pk_businesstype FROM pub_billbusiness WHERE coalesce(dr,0) = 0";
		sWherePart += " AND (pk_corp ='" + getCorpPrimaryKey()
				+ "' or pk_corp ='@@@@') ";
		sWherePart += " AND pk_billtype ='30')";
		biztypeRef.getRefModel().setWherePart(sWherePart);

		//v5.3ȥ��������
		// ��ѯ�ۣ��Ҳ��ɸļ۸�ʱ�����м۸�����޸� 20060427
		/*if (SA_15 != null && SA_15.booleanValue() && SA_07 != null
				&& !SA_07.booleanValue()) {
			getBillCardTools().setBodyItemsEdit(
					SOBillCardTools.getSaleItems_Price(), false);
		}*/

		// ��ʼ����ʽ
		BillTools.initItemKeys();

		// �������볤��
		getBillCardPanel().setInputLimit();

		// ����������
		initBodyComboBox();

		// ���Ի�״̬
		initState();

		((UIRefPane) getBillCardPanel().getBodyItem("cbatchid").getComponent())
				.setReturnCode(true);

		BillItem crownoitem = bd.getBodyItem("crowno");

		javax.swing.table.TableColumn tal = null;

		if (crownoitem != null) {
			try {
				tal = getBillCardPanel().getBodyPanel().getTable().getColumn(
						crownoitem.getName());
				if (tal != null) {
					tal.setCellRenderer(new myCellRenderer());
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}

		}

		// ����ʱ��༭��
		String[] tabCodes = getBillCardPanel().getBillData().getBodyTableCodes();
		for (String tabCode : tabCodes) {
			BillItem tconsigntimeitem = bd.getBodyItem("tconsigntime");
			if (tconsigntimeitem != null ) {
				// ����ʱ��༭��
				try {
					//�ƻ�����ʱ��ֻ��"sharecode"
					tal = getBillCardPanel().getBodyPanel(tabCode).getTable().getColumn(tconsigntimeitem.getName());
					if (tal != null) {
						BillCellEditor timecelledit = new BillCellEditor(
								new UITimeTextField());
						tal.setCellEditor(timecelledit);
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
	
			BillItem tdelivertimeitem = bd.getBodyItem("tdelivertime");
			if (tdelivertimeitem != null ) {
				try {
					// ����ʱ��༭��
					//Ҫ�󵽻�ʱ��ֻ��"sharecode"
					tal = getBillCardPanel().getBodyPanel(tabCode).getTable().getColumn(tdelivertimeitem.getName());
					if (tal != null) {
						BillCellEditor timecelledit = new BillCellEditor(
								new UITimeTextField());
						tal.setCellEditor(timecelledit);
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}

		BillItem bmbiztypename = bd.getHeadItem("cbiztypename");
		if (bmbiztypename != null) {
			bmbiztypename.setShow(false);
			bmbiztypename.setEdit(false);
		}

		// BillItem bmbiztype = bd.getHeadItem("cbiztype");
		// if (bmbiztype != null) {
		// bmbiztype.setEdit(false);
		// }

		// ��ͷҵ��Ա����
		/** ֻ������Ϊ���ۻ��߲ɹ������۲��������Ա V502 jiangzhe * */
		UIRefPane refPsn = (UIRefPane) getBillCardPanel().getHeadItem(
				"cemployeeid").getComponent();
		if (refPsn != null)
			refPsn
					.setWhereString(" (bd_deptdoc.deptattr = '3' or bd_deptdoc.deptattr= '4') and bd_psndoc.pk_corp='"
							+ getCorpPrimaryKey() + "'");
//		else
//			refPsn.setWhereString(null);

		// �޸ı���������
		UIRefPane invRef = (UIRefPane) getBillCardPanel().getBodyItem(
				"cinventorycode").getComponent();

		if (invRef != null) {
				invRef.setTreeGridNodeMultiSelected(true);
				invRef.setMultiSelectedEnabled(true);
		}

		// �к�
		BillRowNo.loadRowNoItem(getBillCardPanel(), getBillCardPanel()
				.getRowNoItemKey());

		getBillCardTools().reload(getBillCardPanel(), getClientEnvironment());

		getBillCardPanel().setTatolRowShow(true);
		
		getBillCardTools().getInitBillItemEidtState();

		// ������ק���Գ�ʼ��
		initBodyFillStatus(bd);
		
	    // ����ӱ���ɫ
		String[] tableCodes = getBillCardPanel().getBillData().getBodyTableCodes();
		for (String tableCode : tableCodes) {
			getBillCardPanel().getBillTable(tableCode).setRowSelectionAllowed(true);
			getBillCardPanel().getBillTable(tableCode).setColumnSelectionAllowed(false);
		}
		
		//����˵�����
		String[] tabcodes = getBillCardPanel().getBillData().getTableCodes(BillItem.BODY);
		for (String tabCode : tabcodes) {
			getBillCardPanel().addActionListener(tabCode, this);
		}
		
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000176")/* @res "ģ����سɹ���" */);
	}

	private void initBodyFillStatus(BillData bd) {
		HashSet<String> hs_key = new HashSet();

		// ���֡����ʡ���Ʒ�ۿ�
		// ������˾�����������֯�������ֿ⡢�������ڡ�����ʱ�䡢�ջ���λ���ջ��������ջ��ص㡢�ջ���ַ���������ڡ�����ʱ�䡢�ջ������֯��
		// ��Ŀ����Ŀ�׶Ρ���ע�������Զ�����1-20

		String[] skeys = new String[] {
				//
				"ccurrencytypename", "nexchangeotobrate", "nitemdiscountrate",
				//
				"cconsigncorp", "cadvisecalbody", "cbodywarehousename", "dconsigndate", "tconsigntime",
				//
				"creceiptcorpname", "creceiptareaname", "crecaddrnodename", "vreceiveaddress", "ddeliverdate",
				"tdelivertime", "creccalbody",
				//
				"cprojectname", "cprojectphasename", "frownote",
				//		
				"vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10",
				//
				"vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20", };
		hs_key.addAll(Arrays.asList(skeys));

		BillItem[] bis = bd.getBodyItems();
		for (BillItem bi : bis) {
			if (hs_key.contains(bi.getKey()) && bi.isEdit()) {
				bi.setFillEnabled(true);
			} else {
				bi.setFillEnabled(false);
			}
		}

	}

	/**
	 * ����������Ŀ
	 * 
	 * �������ڣ�(01-2-26 13:29:17)
	 * 
	 */
	protected void initBodyComboBox() {
		UIComboBox comHeadItem = (UIComboBox) getBillCardPanel().getHeadItem(
				"fstatus").getComponent();
		int count = comHeadItem.getItemCount();
		if (count == 0) {
			comHeadItem.setTranslate(true);
			getBillCardPanel().getHeadItem("fstatus").setWithIndex(true);
			comHeadItem.addItem("");
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/* @res "����" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000027")/* @res "����" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000030")/* @res "����" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000119")/* @res "�ر�" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000005")/* @res "����" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000128")/* @res "����" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000320")/* @res "����������" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000242")/* @res "����δͨ��" */);
		}
		UIComboBox comBodyItem = (UIComboBox) getBillCardPanel().getBodyItem(
				"frowstatus").getComponent();
		count = comBodyItem.getItemCount();
		if (count == 0) {
			getBillCardPanel().getBodyItem("frowstatus").setWithIndex(true);
			comBodyItem.addItem("");
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/* @res "����" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000027")/* @res "����" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000030")/* @res "����" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000119")/* @res "�ر�" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000005")/* @res "����" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000128")/* @res "����" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000320")/* @res "����������" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000242")/* @res "����δͨ��" */);
		}
		UIComboBox comBatch = (UIComboBox) getBillCardPanel().getBodyItem(
				"fbatchstatus").getComponent();
		count = comBatch.getItemCount();
		if (count == 0) {
			getBillCardPanel().getBodyItem("fbatchstatus").setWithIndex(true);
			comBatch.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/* @res "����" */);
			comBatch.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"40060301", "UPP40060301-000152")/* @res "��ָ��" */);
			comBatch.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"40060301", "UPP40060301-000153")/* @res "����" */);
		}
	}

	/**
	 * ���Ի�����״̬
	 * 
	 * �������ڣ�(2001-11-15 9:02:22)
	 * 
	 */
	private void initState() {

		// ���Ի�
		id = "";
		strState = "����"/*-=notranslate=-*/;

		getBillCardPanel().setEnabled(false);

		setButtonsState();
	}

	/**
	 * ���ؿ�Ƭģ�塣
	 * 
	 * �������ڣ�(2001-11-15 9:03:35)
	 * 
	 */
	private void loadCardTemplet(String billType, String busiType,
			String userid, String corpid) {

		BillData bd = null;

		billtempletVO = null;

		if (SO_20.booleanValue()) {

			if (billtempletVO == null) {
//				billtempletVO = getBillCardPanel().getTempletData(
//						"40060302".equals(getNodeCode()) ? getNodeCode()
//								: getBillType(), null,
//						getClientEnvironment().getUser().getPrimaryKey(),
//						getCorpPrimaryKey());
				
				billtempletVO = getBillCardPanel().getTempletData(
						"HQ010601", null,
						getClientEnvironment().getUser().getPrimaryKey(),
						getCorpPrimaryKey());
			}
			
			SOBillCardTools.addExeTs(billtempletVO);
			bd = new BillData(billtempletVO);

		} else {
			if (billtempletVO == null) {
				billtempletVO = getBillCardPanel().getTempletData(
						// "40060302".equals(getNodeCode()) ? getNodeCode() : getBillType()
						"HQ010601"
								, busiType,
						getClientEnvironment().getUser().getPrimaryKey(),
						getCorpPrimaryKey());
			}
			
			
			SOBillCardTools.addExeTs(billtempletVO);
			bd = new BillData(billtempletVO);
		}

		// �������۹�˾Ϊ���ɱ༭��
		if (bd != null) {
			BillItem salecorpbm = bd.getHeadItem("salecorp");
			if (salecorpbm != null) {
				salecorpbm.setEdit(false);
				salecorpbm.setEnabled(false);
			}

			BillItem bm = bd.getHeadItem("naccountperiod");
			if (bm != null) {
				bm.setDecimalDigits(0);
			}

		}

		SOBillCardTools.processCTBillItem(bd, null);

		// �ı����
		setCardPanel(bd);

		// ���ý��棬��������Դ
		getBillCardPanel().setBillData(bd);

		// ��ʼ����ʽ
		BillTools.initItemKeys();

		// ����������
		initBodyComboBox();

		// ���Ի�״̬
		initState();

		// ���ñ�ͷ���յĹ�˾
		BillItem[] bms = bd.getHeadItems();
		nc.ui.bd.ref.AbstractRefModel refmodel = null;

		try {

			for (int i = 0, loop = bms.length; i < loop; i++) {
				if (bms[i].getDataType() == BillItem.UFREF) {
					refmodel = ((UIRefPane) bms[i].getComponent())
							.getRefModel();
					if (refmodel != null)
						refmodel.setPk_corp(corpid);
				}
			}

		} catch (Exception e) {
			SCMEnv.out("���ò��յĹ�˾Լ��ʧ�ܣ�");
		}

		BillItem bmbiztypename = bd.getHeadItem("cbiztypename");
		if (bmbiztypename != null) {
			bmbiztypename.setShow(false);
			bmbiztypename.setEdit(false);
		}

		BillItem bmbiztype = bd.getHeadItem("cbiztype");
		if (bmbiztype != null) {
			bmbiztype.setEdit(false);
		}

		// �޸ı���������
		UIRefPane invRef = (UIRefPane) getBillCardPanel().getBodyItem(
				"cinventorycode").getComponent();

		if (invRef != null) {
				invRef.setTreeGridNodeMultiSelected(true);
				invRef.setMultiSelectedEnabled(true);
		}

		// �к�
		BillRowNo.loadRowNoItem(getBillCardPanel(), getBillCardPanel()
				.getRowNoItemKey());

		getBillCardPanel().setTatolRowShow(true);

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000176")/* @res "ģ����سɹ���" */);

	}

	/**
	 * ��ʼ��ǰ�ı���档
	 * 
	 * �������ڣ�(2001-11-15 9:20:13)
	 * 
	 * @param bdData
	 *            nc.ui.pub.bill.BillData
	 * 
	 */
	private void setCardPanel(BillData bdData) {
		setCardPanelByPara(bdData);
		setCardPanelByOther(bdData);
	}

	/**
	 * ���ݲ����ı����(��Ҫ�Ǹ��ֶξ��ȿ���)��
	 * 
	 * �������ڣ�(2001-9-27 16:13:57)
	 * 
	 */
	private void setCardPanelByPara(BillData bdData) {
		try {
			
			/** V53 �������󷢻������֯�ı༭����ʾ������SO46������Ӱ��
			 *  SA_16����SO46�ı���ı�
			 **/
			/*if (getBillType().equals(SaleBillType.SaleOrder)
					|| getBillType().equals(SaleBillType.SaleInitOrder)) {
				// ���鷢����֯
				if (SA_16.booleanValue()) {
					if (bdData.getBodyItem("sharecode2", "cadvisecalbody") != null)
						bdData.getBodyItem("sharecode2", "cadvisecalbody")
								.setShow(true);
					else
						bdData.getBodyItem("cadvisecalbody").setShow(true);
				} else {
					if (bdData.getBodyItem("sharecode2", "cadvisecalbody") != null)
						bdData.getBodyItem("sharecode2", "cadvisecalbody")
								.setShow(false);
					else
						bdData.getBodyItem("cadvisecalbody").setShow(false);
				}
				// bdData.getBodyItem("table", "cadvisecalbody").setShow(false);
			}*/

			/** V502 ��Ŀ�����Ͻ�ͨ��zhanghaiyan zhongwei* */
			// // �����Ƿ�˰
			// if (SA_02 != null && SA_02.booleanValue()) {
			// // ��˰���
			// if (bdData.getBodyItem("noriginalcurmny") != null)
			// bdData.getBodyItem("noriginalcurmny").setEdit(false);
			// else
			// System.out
			// .println(" bdData.getBodyItem(noriginalcurmny) is null ");
			// // ��˰�ϼ�
			// if (bdData.getBodyItem("noriginalcursummny") != null)
			// getBillCardTools().resumeBillBodyItemEdit(
			// bdData.getBodyItem("noriginalcursummny"));
			// // bdData.getBodyItem("noriginalcursummny").setEdit(true);
			// else
			// System.out
			// .println(" bdData.getBodyItem(noriginalcursummny) is null ");
			// } else {
			// // ��˰���
			// if (bdData.getBodyItem("noriginalcurmny") != null)
			// // bdData.getBodyItem("noriginalcurmny").setEdit(true);
			// getBillCardTools().resumeBillBodyItemEdit(
			// bdData.getBodyItem("noriginalcurmny"));
			//
			// else
			// System.out
			// .println(" bdData.getBodyItem(noriginalcurmny) is null ");
			// // ��˰�ϼ�
			// if (bdData.getBodyItem("noriginalcursummny") != null)
			// bdData.getBodyItem("noriginalcursummny").setEdit(false);
			// else
			// System.out
			// .println(" bdData.getBodyItem(noriginalcursummny) is null ");
			// }
			/** V502 ��Ŀ�����Ͻ�ͨ��zhanghaiyan zhongwei* */

			// �����ۿ�С��λ��
			if (bdData.getBodyItem("nitemdiscountrate") != null)
				bdData.getBodyItem("nitemdiscountrate").setDecimalDigits(6);
			if (bdData.getBodyItem("ndiscountrate") != null)
				bdData.getBodyItem("ndiscountrate").setDecimalDigits(6);
            if(bdData.getHeadItem("ndiscountrate")!= null)
            	bdData.getHeadItem("ndiscountrate").setDecimalDigits(6);
            bdData.getHeadItem("ndiscountrate").setLength(21);
            bdData.getBodyItem("ndiscountrate").setLength(21);
            bdData.getBodyItem("nitemdiscountrate").setLength(21);          
            
			// ���ֻ���
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				if (bdData.getHeadItem("nexchangeotobrate") != null)
					bdData.getHeadItem("nexchangeotobrate").setDecimalDigits(4);
			}
			if (bdData.getBodyItem("nexchangeotobrate") != null)
				bdData.getBodyItem("nexchangeotobrate").setDecimalDigits(4);

			if (BD501 != null) {
				String[] aryNum = SOBillCardTools.getSaleItems_Num();
				for (int i = 0; i < aryNum.length; i++) {
					if (bdData.getBodyItem(aryNum[i]) != null)
						bdData.getBodyItem(aryNum[i]).setDecimalDigits(
								BD501.intValue());
					else
						SCMEnv.out(" bdData.getBodyItem(" + aryNum[i]
								+ ") is null ");
				}
			}

			SCMEnv.out(" into setCardPanelByPara 4 ");

			if (BD502 != null) {
				bdData.getBodyItem("npacknumber").setDecimalDigits(
						BD502.intValue());
				// bdData.getBodyItem("nquoteunitnum").setDecimalDigits(BD502.intValue());
			}

			// ����
			if (BD505 != null) {
				for (int i = 0; i < SOBillCardTools.getSaleItems_Price().length; i++) {
					bdData.getBodyItem(SOBillCardTools.getSaleItems_Price()[i])
							.setDecimalDigits(BD505.intValue());
				}
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

			// ������۵�λ��˰��/���ۣ����۵�λ��˰��/���ۿɼ�����������˰��/���ۣ���˰��/���۵Ȳ��ɱ༭
			/*if (bdData.getBodyItem("norgqtprc").isShow()
					|| bdData.getBodyItem("norgqttaxprc").isShow()
					|| bdData.getBodyItem("norgqtnetprc").isShow()
					|| bdData.getBodyItem("norgqttaxnetprc").isShow()) {
				bdData.getBodyItem("noriginalcurprice").setEdit(false);
				bdData.getBodyItem("noriginalcurtaxprice").setEdit(false);
				bdData.getBodyItem("noriginalcurtaxnetprice").setEdit(false);
				bdData.getBodyItem("noriginalcurnetprice").setEdit(false);
			}*/

			// �������������ʾ�������������ɱ༭
			/*if (bdData.getBodyItem("nquoteunitnum").isShow()) {
				bdData.getBodyItem("nnumber").setEdit(false);
			}*/
			
			//����ģ��Ͳ���������ã����Һ͸��Ҿ����ɱ༭
			getBillCardTools().setBodyItemEnable(
					SOBillCardTools.getSaleOrderItems_Price_Mny_NoOriginal(),false);
			
			getBillCardTools().setBodyItemEnable(
					SOBillCardTools.getSaleOrderCurrTypeDigit(), false);
			
			//�ۿ۶�
			getBillCardTools().setBodyItemEnable(new String[]{"noriginalcurdiscountmny","ndiscountmny"}, false);
			
		} catch (Exception e) {
			SCMEnv.out(e);
			// e.printStackTrace();
		}

	}

	/**
	 * ������ԭ��ı���档
	 * 
	 * �������ڣ�(2001-11-15 9:18:13)
	 * 
	 * @param bdData
	 *            nc.ui.pub.bill.BillData
	 * 
	 */
	private void setCardPanelByOther(BillData bdData) {

		try {
			//��������Ԥ�����Ƿ�ر�
			getBillCardPanel().hideBodyTableCol("bbindflag");
			getBillCardPanel().hideBodyTableCol("bpreorderclose");
			// �������
			UIRefPane refInv = (UIRefPane) bdData.getBodyItem("cinventorycode")
					.getComponent();
			refInv.getRefModel().addWherePart(" AND bd_invmandoc.sealflag='N'");
			// ���κ�
			bdData.getBodyItem("cbatchid").setComponent(
					getBillCardPanel().getLotNumbRefPane());
			bdData.getBodyItem("cbatchid").setDataType(BillItem.STRING);

			// ɢ��
			UIRefPane ref = (UIRefPane) bdData.getHeadItem("cfreecustid")
					.getComponent();
			ref.getRef().setRefUI(new UFRefGridUI());
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				// ��ͷ�ջ���ַ
				UIRefPane refHeadAddress = (UIRefPane) bdData.getHeadItem(
						"vreceiveaddress").getComponent();
				refHeadAddress.setAutoCheck(false);
				refHeadAddress.setReturnCode(true);
				// �����ջ���ַ
				UIRefPane vreceiveaddress = (UIRefPane) bdData.getBodyItem(
						"vreceiveaddress").getComponent();
				vreceiveaddress.setAutoCheck(false);
				vreceiveaddress.setReturnCode(true);
				bdData.getBodyItem("vreceiveaddress").setDataType(
						BillItem.USERDEF);
				// �ջ�����
				UIRefPane creceiptareaid = (UIRefPane) bdData.getBodyItem(
						"creceiptareaid").getComponent();
				creceiptareaid.setReturnCode(false);
				// �ջ���λ
				UIRefPane creceiptcorpid = (UIRefPane) bdData.getBodyItem(
						"creceiptcorpid").getComponent();
				creceiptcorpid.setReturnCode(false);
				// ���鷢�������֯
				UIRefPane cadvisecalbody = (UIRefPane) bdData.getBodyItem(
						"cadvisecalbody").getComponent();
				cadvisecalbody.setReturnCode(false);
			}

			// // ��Ŀ
			// ref = (UIRefPane)
			// bdData.getBodyItem("cprojectname").getComponent();
			// ref.setRefType(nc.ui.bd.ref.IBusiType.GRIDTREE);
			// ref.setRefModel(new nc.ui.bd.b39.JobRefTreeModel("0001",
			// getCorpPrimaryKey(), null));

			// �ı��²��յĳ���
			getBillCardPanel().getFreeItemRefPane().setMaxLength(1000);

			// �����µĲ��գ�Ҫ��ָ����Ӧ���ֶ���
			// ����,������
			bdData.getBodyItem("vfree0").setComponent(
					getBillCardPanel().getFreeItemRefPane());
			// �����Զ�����
			DefVO[] defs = null;
			// ��ͷ
			// ��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������
			defs = head_defs;

			if (defs != null) {
				bdData.updateItemByDef(defs, "vdef", true);
			}

			// ����
			// ��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������
			defs = body_defs;

			if (defs != null) {
				bdData.updateItemByDef(defs, "vdef", false);
			}

			/** ͳ���͵��Զ�������Ա༭¼�� �������* */
			for (int i = 1; i <= 20; i++) {
				nc.ui.pub.bill.BillItem item = bdData.getBodyItem("vdef" + i);
				if (item != null && item.getDataType() == nc.ui.pub.bill.BillItem.USERDEF) {
					((nc.ui.pub.beans.UIRefPane) item.getComponent()).setAutoCheck(true);
				}
				if (item != null && item.getComponent() != null)
					((nc.ui.pub.beans.UIRefPane) item.getComponent()).setEditable(item.isEdit());
			}
			/** ͳ���͵��Զ�������Ա༭¼�� �������* */
			
		} catch (Exception e) {
			SCMEnv.out(e);
			// e.printStackTrace();
		}
	}

	/**
	 * ��ʼ���ࡣ
	 */
	protected void initialize(String pk_corp, String billtype, String busitype,
			String operator, String id) {
		// ���ý���

		String cbilltype = billtype;
		if (cbilltype == null || cbilltype.trim().length() <= 0)
			cbilltype = "30";
		String csalecorp = pk_corp;
		String cbusitype = busitype;
		String cuserid = operator;
		String cbillid = id;

		try {
			setName("SaleOrder");
			setSize(774, 419);
			add(getBillCardPanel(), "Center");

			String sql = "select pk_corp,vreceiptcode,cbiztype,coperatorid from so_sale "
					+ "where so_sale.csaleid='" + cbillid.trim() + "'";
			SORowData[] rows = null;
			rows = nc.ui.so.so016.SOToolsBO_Client.getSORows(sql);
			if (rows != null && rows.length > 0 && rows[0] != null) {
				csalecorp = rows[0].getString(0);
				cbusitype = rows[0].getString(2);
				cuserid = rows[0].getString(3);
			}

		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// �õ�ǰ��½��˾��ʼ������
		initVars(getCorpPrimaryKey());

		// ���Ի�����
		initCurrency();

		// ����ģ��
		loadCardTemplet(cbilltype, cbusitype, cuserid, csalecorp);

		strState = "����";

		// �к�
		BillRowNo.loadRowNoItem(getBillCardPanel(), getBillCardPanel()
				.getRowNoItemKey());

		// ��������
		loadCardDataByID(id);
	}

	protected void loadCardDataByID(String sID) {
		loadCardDataByID(sID, false);
	}

	/**
	 * ���ݶ���ID���ؿ�Ƭ���ݡ�
	 * 
	 * �������ڣ�(2001-11-15 9:02:22)
	 * 
	 */
	protected void loadCardDataByID(String sID, boolean b_datapower) {
		try {
			long s = System.currentTimeMillis();
			SaleOrderVO saleorderVO = (SaleOrderVO) SaleOrderBO_Client
					.queryData(sID);

			// ������Ȩ�޹���
			if (b_datapower) {

				getQueryDlg(saleorderVO.getPk_corp()).initConditions();
				ConditionVO[] voCondition = getQueryDlg().getConditionVO();
				java.util.Set<String> datapowerConditions = new java.util.HashSet<String>();
				for (ConditionVO c : voCondition) {
					if (c!=null){
						if (c.getValue().trim().indexOf("(select distinct power.resource_data_id") > 0) {
							datapowerConditions.add(c.getFieldCode());
						}
					}
				}

				// ��������
				ArrayList<ConditionVO> filted = new ArrayList<ConditionVO>();
				for (int i = 0, len = voCondition.length; i < len; i++) {
					if (voCondition[i]!=null){
						// // �ж�����Ȩ������
						if (datapowerConditions.contains(voCondition[i].getFieldCode())) {
							filted.add(voCondition[i]);
						}
					}
				}
				if (filted.size()>0)
					voCondition = filted.toArray(new ConditionVO[0]);

//				// ��������Ȩ������ת��
//				DataPowerUtils.trnsDPCndFromAreaToCust(voCondition,
//						"ccustomerid");

				if (voCondition != null && voCondition.length > 0) {
					ConditionVO[] conditions = new ConditionVO[voCondition.length + 1];

					System.arraycopy(voCondition, 0, conditions, 0,
							voCondition.length);

					ConditionVO con_id = new ConditionVO();
					con_id.setFieldCode("so_sale.csaleid");
					con_id.setOperaCode("=");
					con_id.setValue(sID);

					conditions[voCondition.length] = con_id;

					SaleorderHVO[] hvo = SaleOrderBO_Client
							.queryHeadAllData(getQueryDlg().getWhereSQL(
									conditions));
					if (hvo == null || hvo.length == 0) {
						saleorderVO = null;
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40060301", "UPP40060301-000539")/*
																				 * @res
																				 * "û�в鿴���ݵ�Ȩ��"
																				 */);
						return;
					}
				}

			}// end if datapower

			// ��ѯ���ս��
			if (saleorderVO != null
					&& saleorderVO.getHeadVO().getCsaleid() != null) {
				saleorderVO.getHeadVO().setNreceiptcathmny(
						SaleOrderBO_Client.queryCachPayByOrdId(saleorderVO
								.getHeadVO().getCsaleid()));
			}

			// ���ñ�ͷ���յĹ�˾
			BillItem[] bms = getBillCardPanel().getHeadItems();
			nc.ui.bd.ref.AbstractRefModel refmodel = null;

			for (int i = 0, loop = bms.length; i < loop; i++) {
				if (bms[i].getDataType() == BillItem.UFREF) {
					refmodel = ((UIRefPane) bms[i].getComponent())
							.getRefModel();
					if (refmodel != null) {
						try {
							refmodel.setPk_corp(saleorderVO.getHeadVO()
									.getPk_corp());
							refmodel.setUseDataPower(false);
						} catch (Exception e) {
							SCMEnv.out("���ò��գ�" + bms[i].getName() + "�Ĺ�˾Լ��ʧ�ܣ�");
						}
					}
				}
			}

			// �ջ���ַ����
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel()
					.getHeadItem("vreceiveaddress").getComponent();

			SCMEnv.out("���ݶ�ȡ[����ʱ" + (System.currentTimeMillis() - s) + "]");
			s = System.currentTimeMillis();

			// ������Я������
			getBillCardPanel().setPanelByCurrency(
					((SaleorderBVO) saleorderVO.getChildrenVO()[0])
							.getCcurrencytypeid());

			// �������þ��� 2007-11-27 xhq
			if (!getCorpPrimaryKey().equals(
					saleorderVO.getHeadVO().getPk_corp())) {
				initVars(saleorderVO.getHeadVO().getPk_corp());
				nc.ui.pub.bill.BillData bd = getBillCardPanel().getBillData();
				setCardPanelByPara(bd);
			}		
			
			// ��������
			getBillCardPanel().setBillValueVO(saleorderVO);
			
			SCMEnv.out("��������[����ʱ" + (System.currentTimeMillis() - s) + "]");
			long s1 = System.currentTimeMillis();
			
			getBillCardPanel().getBillModel().execLoadFormula();
			SCMEnv.out("ִ�й�ʽ[����ʱ" + (System.currentTimeMillis() - s1) + "]");

			// �ջ���ַ����
			if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null)
				((CustAddrRefModel) vreceiveaddress.getRefModel())
						.setCustId(getBillCardPanel().getHeadItem(
								"creceiptcustomerid").getValue());

			Object temp = saleorderVO.getParentVO().getAttributeValue(
					"vreceiveaddress");
			// ��������ID
			String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
			String pk_cubasdoc = (String) getBillCardPanel().execHeadFormula(
					formula);

			// �ջ���ַ
			vreceiveaddress.setAutoCheck(false);
			String strvreceiveaddress = BillTools.getColValue2("bd_custaddr",
					"pk_custaddr", "defaddrflag", "Y", "pk_cubasdoc",
					pk_cubasdoc);
			if (temp != null) {
				vreceiveaddress.getUITextField().setText(temp.toString());
			} else if (strvreceiveaddress != null
					&& strvreceiveaddress.trim().length() > 0) {
				vreceiveaddress.setPK(strvreceiveaddress);
			}

			// �������Լ���
			Object cpackunitid, fixedflag, value;
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				// ����Ʒ��־����
				String[] appendFormula = { "isappendant->getColValue(bd_invmandoc,isappendant,pk_invmandoc,cinventoryid)" };
				getBillCardPanel().execBodyFormulas(i, appendFormula);

				// �ǹ̶����㸨����
				cpackunitid = getBillCardPanel().getBodyValueAt(i,
						"cpackunitid");
				if (cpackunitid != null) {
					fixedflag = BillTools.getColValue2("bd_convert",
							"fixedflag", "pk_invbasdoc",
							(String) getBillCardPanel().getBodyValueAt(i,
									"cinvbasdocid"), "pk_measdoc",
							(String) cpackunitid);

					if (fixedflag != null && "N".equals(fixedflag.toString())) {
						value = ((UFDouble) getBillCardPanel().getBodyValueAt(
								i, "nnumber"))
								.div((UFDouble) getBillCardPanel()
										.getBodyValueAt(i, "npacknumber"));
						getBillCardPanel().setBodyValueAt(value, i,
								"scalefactor");
					}

				}

			}// end for

			setHeadDefaultData();
			getBillCardPanel().initFreeItem();

			// ���������ۿ�
			UFDouble ndiscountrate = getBillCardTools().getHeadUFDoubleValue(
					"ndiscountrate");
			if (ndiscountrate == null) {
				ndiscountrate = getBillCardTools().getBodyUFDoubleValue(0,
						"ndiscountrate");
			}
			if (ndiscountrate == null)
				ndiscountrate = new UFDouble(100);

			getBillCardPanel().setHeadItem("ndiscountrate", ndiscountrate);

			formula = "salecorp->getColValue(bd_corp,unitname,pk_corp,pk_corp)";
			// ���۹�˾
			getBillCardPanel().execHeadFormula(formula);

			getBillCardPanel().updateValue();
			getBillCardPanel().getBillModel().reCalcurateAll();

			showHintMessage(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID(
							"40060301",
							"UPP40060301-000186",
							null,
							new String[] { (System.currentTimeMillis() - s)
									/ 1000 + "" }));

			// ����������
			vocache.setCatheData(new SaleOrderVO[] { saleorderVO });

		} catch (ValidationException e) {
			showErrorMessage(e.getMessage());
		} catch (Exception e) {
			SCMEnv.out(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000256")/* @res "���ݼ���ʧ�ܣ�" */);
			//e.printStackTrace();
		}
	}

	/**
	 * �޸ĵ���ʱ���ñ�ͷĬ�����ݡ�
	 * 
	 * �������ڣ�(2001-8-27 10:05:59)
	 * 
	 */
	protected void setHeadDefaultData() {
		if (getBillCardPanel().getRowCount() > 0) {
			// ����
			UIRefPane ccurrencytypeid = (UIRefPane) getBillCardPanel()
					.getHeadItem("ccurrencytypeid").getComponent();
			ccurrencytypeid.setPK(getBillCardPanel().getBodyValueAt(0,
					"ccurrencytypeid"));

			// �۱�����
			UIRefPane nexchangeotobrate = (UIRefPane) getBillCardPanel()
					.getHeadItem("nexchangeotobrate").getComponent();
			if (getBillCardPanel().getBodyValueAt(0, "nexchangeotobrate") != null)
				nexchangeotobrate.setValue(getBillCardPanel().getBodyValueAt(0,
						"nexchangeotobrate").toString());
		}
	}

	public SOBillCardPanel getBillCardPanel() {
		if (cardpanel == null) {
			try {
				cardpanel = new SOBillCardPanel(this, "BillCardPanel",
						getBillType(), getCorpPrimaryKey(),
						getClientEnvironment().getUser().getPrimaryKey());
				//�����Ƿ�����
				cardpanel.hideBodyTableCol("bbindflag");
				// ���ñ�ͷ�༭��ʽ�Զ�ִ��
				cardpanel.setAutoExecHeadEditFormula(true);
				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return cardpanel;
	}

	public SOBillCardTools getBillCardTools() {
		if (cardtools == null) {
			cardtools = new SOBillCardTools(getBillCardPanel(),
					getClientEnvironment());
			cardtools.SA34 = this.SA34;
		}
		return cardtools;
	}
	
	/**
	 * ���ֻ��㹤��
	 */
	public SOCurrencyRateUtil getSOCurrencyRateUtil() {
		if (socrutil == null) {
			socrutil = new SOCurrencyRateUtil(getClientEnvironment().getCorporation().getPrimaryKey());
		}
		return socrutil;
	}

	public abstract String getBillType();

	/**
	 * �����к�
	 */
	public void onResortRowNo(){
		nc.ui.scm.pub.report.BillRowNo.addNewRowNo(getBillCardPanel(),
				SaleBillType.SaleOrder, "crowno");
	}
	
	/**
	 * ��Ƭ�༭
	 */
	public void onCardEdit(){
		try{	        
	        //getBillCardPanel().addActionListener(this);
	        getBillCardPanel().startRowCardEdit();
	      }finally{
	       // getBillCardPanel().addActionListener(this);
	      }
	}
	
	/**
	 * �����С� �������ڣ�(2001-6-23 14:41:56)
	 */
	public void onInsertLine() {
		int row = getBillCardPanel().getBillTable().getSelectedRow();
		onInsertLine(row);
		//����ɫΪԭ�б���ɫ
		getBillCardPanel().resetErrorRowColor(row);
	}

	/**
	 * �����С� �������ڣ�(2001-6-23 14:41:56)
	 */
	public void onInsertLine(int row) {

		getBillCardPanel().alInvs.add(row, null);
		vRowATPStatus.insertElementAt(new UFBoolean(false), row);
		getBillCardPanel().insertLine();
		BillRowNo.insertLineRowNo(getBillCardPanel(), getBillType(),
				getRowNoItemKey());
		getBillCardPanel().setBodyDefaultData(row, row + 1);

		// ����������������ʾ
		// ������ȡ�����Ϣ
		InvVO[] invvos = new InvVO[getBillCardPanel().getRowCount()];
		for (int i = 0; i < invvos.length; i++) {
			invvos[i] = new InvVO();
			invvos[i].setCinventoryid((String) getBillCardPanel()
					.getBodyValueAt(i, "cinventoryid"));
		}

		InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
		invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

		ArrayList al_invs = new ArrayList();
		for (InvVO invvo : invvos) {
			al_invs.add(invvo);
		}

		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), al_invs);

		updateUI();
	}

	/**
	 * �����С�
	 * 
	 * �������ڣ�(2001-4-13 16:37:56)
	 * 
	 */
	public void onAddLine() {
		if (checkAddLine()) {
			addLine();
		}
	}

	/**
	 * �������в�����û�������жϣ�
	 * 
	 */
	public void addLine() {
		BillActionListener  bal = getBillCardPanel().getBodyPanel().getBillActionListener(); 
		getBillCardPanel().getBodyPanel().removeBillActionListener();
		getBillCardPanel().addLine();
		getBillCardPanel().getBodyPanel().addBillActionListener(bal);

		// ���
		if (getBillCardPanel().alInvs != null)
			getBillCardPanel().alInvs.add(null);
		// ������״̬
		if (vRowATPStatus != null)
			vRowATPStatus.add(new UFBoolean(false));

		// Ĭ������
		int row = getBillCardPanel().getRowCount() - 1;

		// ���㲢���������к�
		BillRowNo.addLineRowNo(getBillCardPanel(), getBillType(),
				getRowNoItemKey());
		getBillCardPanel().setBodyDefaultData(row, row + 1);

		// yb add 03-09-16
		setDefaultCellEditable(row);

		/** ��ָ��ѡ���У���꽹�㶨λ��������* */
		// getBillCardPanel().getBillTable().getSelectionModel()
		// .setSelectionInterval(row, row + 1);
		/** ��ָ��ѡ���У���꽹�㶨λ��������* */
		
		//����ɫΪԭ�б���ɫ
		getBillCardPanel().resetErrorRowColor(row);
	}

	/**
	 * ������ʱ��⡣
	 * 
	 * �������ڣ�(2001-6-23 14:06:13)
	 * 
	 * @return boolean
	 * 
	 */
	private boolean checkAddLine() {

		// �ޱ�����
		if (getBillCardPanel().getRowCount() == 0) {
			String strCustomerID = getBillCardPanel()
					.getHeadItem("ccustomerid").getValue();
			if (strCustomerID == null) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40060301", "UPP40060301-000180")/* @res "�ͻ�����Ϊ�ա�" */);
				return false;
			}
			String strCurrTypeID = getBillCardPanel().getHeadItem(
					"ccurrencytypeid").getValue();
			if (strCurrTypeID == null) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40060301", "UPP40060301-000181")/* @res "ԭ�Ҳ���Ϊ�ա�" */);
				return false;
			}
		}
		// ��������
		if (SO_01 != null) {
			if (SO_01.intValue() != 0) {
				if (SO_01.intValue() < getBillCardPanel().getRowCount() + 1) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("40060301", "UPP40060301-000171", null,
									new String[] { SO_01.intValue() + "" }));
					// ������SO_01.intValue()��
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * �ڱ�������ʱ���ñ�����Ŀɱ༭�ԡ�
	 * 
	 * yb �������ڣ�(2003-9-16 10:20:12)
	 * 
	 */
	protected void setDefaultCellEditable(int irow) {
		if (irow < 0
				|| irow > getBillCardPanel().getBillModel().getRowCount() - 1)
			return;
		// ���ø���λ���ɱ༭
		getBillCardPanel().setCellEditable(irow, "cpackunitname", false);
		// �������β��ɱ༭
		getBillCardPanel().setCellEditable(irow, "cbatchid", false);
		// ��������״̬���ɱ༭
		getBillCardPanel().setCellEditable(irow, "fbatchstatus", false);
		// �����������Ͳ��ɱ༭
		getBillCardPanel().setCellEditable(irow, "cchantype", false);

	}
	
	/**
	 * �ֹ�ѯ�� v5.5 
	 */
	public void onFindPrice(){
		
		int[] selRows = getBillCardPanel().getBillTable().getSelectedRows();
		
		getBillCardPanel().findPrice(selRows, null,	false);
		
		ArrayList rowList  = new ArrayList();
		for (int i = 0;i<selRows.length;i++)
			rowList.add(new Integer(selRows[i]));
		
		//���¼����ͷ��˰�ϼ�
		getBillCardPanel().getHeadItem("nheadsummny").setValue(
				getBillCardPanel().getTotalValue("noriginalcursummny"));
		
		nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel(), rowList); 
	}

	/**
	 * ɾ���С� �������ڣ�(2001-4-13 16:38:18)
	 */
	public void onDelLine() {
		int[] irows = getBillCardPanel().getBillTable().getSelectedRows();
		if (irows == null || irows.length == 0)
			return;
		for (int i = 0; i < irows.length; i++) {
			if (isHasBackwardDoc(irows[i]))
				return;
		}
		// ���
		int[] aryRows = getBillCardPanel().getBillTable().getSelectedRows();
		int[] inewdelline = getBillCardPanel()
				.setBlargebindLineWhenDelLine(aryRows,1);
		int[] aryRows1 = aryRows;
		if (inewdelline != null && inewdelline.length > 0) {
			aryRows = new int[aryRows1.length + inewdelline.length];
			for (int i = 0; i < aryRows1.length; i++) {
				aryRows[i] = aryRows1[i];
			}
			for (int i = aryRows1.length; i < aryRows1.length
					+ inewdelline.length; i++) {
				aryRows[i] = inewdelline[i - aryRows1.length];
			}
		}
		onDelLine(aryRows);

	}

	/**
	 * ɾ���С� �������ڣ�(2001-4-13 16:38:18)
	 */
	public void onDelLine(int[] aryRows) {

		if (aryRows == null || aryRows.length == 0)
			return;

		boolean bisCalculate = getBillCardPanel().getBillModel()
				.isNeedCalculate();
		getBillCardPanel().getBillModel().setNeedCalculate(false);

		// ���
		ArrayList dellist = new ArrayList();
		ArrayList dellistrow = new ArrayList();

		ArrayList invidlist = new ArrayList();
		String invid = null;
		for (int i = aryRows.length - 1; i >= 0; i--) {
			if (vRowATPStatus != null && vRowATPStatus.size() > aryRows[i]
					&& vRowATPStatus.size() > 0) {
				dellistrow.add(vRowATPStatus.get(aryRows[i]));
			}
			if (getBillCardPanel().alInvs != null
					&& aryRows[i] < getBillCardPanel().alInvs.size()
					&& getBillCardPanel().alInvs.size() > 0) {

				if (getBillCardPanel().alInvs.get(aryRows[i]) == null) {
					InvVO delinvvo = new InvVO();
					getBillCardPanel().alInvs.set(aryRows[i], delinvvo);
					dellist.add(delinvvo);
				} else {
					dellist.add(getBillCardPanel().alInvs.get(aryRows[i]));
				}
			}
			invid = getBillCardTools().getBodyStringValue(aryRows[i],
					"cinventoryid");
			if (invid != null && invid.trim().length() > 0) {
				invidlist.add(invid);
			}
		}
		for (int i = 0; i < dellist.size(); i++)
			getBillCardPanel().alInvs.remove(dellist.get(i));
		for (int i = 0; i < dellistrow.size(); i++)
			vRowATPStatus.remove(dellistrow.get(i));

		try {
			BillActionListener  bal = getBillCardPanel().getBodyPanel().getBillActionListener(); 
			getBillCardPanel().getBodyPanel().removeBillActionListener();
			getBillCardPanel().getBillModel().delLine(aryRows);
			getBillCardPanel().getBodyPanel().addBillActionListener(bal);
		} catch (Exception e) {
			SCMEnv.out(e);
			// e.printStackTrace();
		}

		// Ѱ�۴���
		if (invidlist.size() > 0 && getBillCardPanel().getRowCount() > 0) {

			ArrayList rowlist = new ArrayList();
			for (int i = 0, loop = invidlist.size(); i < loop; i++) {
				for (int j = 0, loopj = getBillCardPanel().getRowCount(); j < loopj; j++) {
					if (invidlist.get(i).equals(
							getBillCardTools().getBodyStringValue(j,
									"cinventoryid"))) {
						rowlist.add(new Integer(j));
						break;
					}
				}
			}
			if (rowlist.size() > 0) {
				int[] findpricerows = new int[rowlist.size()];
				for (int i = 0, loop = rowlist.size(); i < loop; i++)
					findpricerows[i] = ((Integer) rowlist.get(i)).intValue();
				// getBillCardPanel().afterNumberEdit(findpricerows, "nnumber",
				// null, false, true);
			}
		}
		getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
		if (getBillCardPanel().getHeadItem("nheadsummny") != null)
			getBillCardPanel().getHeadItem("nheadsummny").setValue(
					getBillCardPanel().getTotalValue("noriginalcursummny"));

	}

	/**
	 * ��鵱ǰ���Ƿ��������ε���
	 * 
	 * @param i
	 * @return
	 */
	protected boolean isHasBackwardDoc(int irow) {
		String sPk = (String) getBillCardPanel()
				.getBodyValueAt(irow, "csaleid");
		String sBodyPk = (String) getBillCardPanel().getBodyValueAt(irow,
				"corder_bid");
		if (sPk == null || sPk.trim().length() == 0 || sBodyPk == null
				|| sBodyPk.trim().length() == 0)
			return false;

		SaleOrderVO svo = vocache.getSaleOrderVO(sPk);

		if (svo == null)
			return false;

		SaleorderBVO[] bvos = svo.getBodyVOs();
		if (bvos == null || bvos.length == 0)
			return false;
		for (int i = 0; i < bvos.length; i++) {
			if (bvos[i].getCorder_bid().equals(sBodyPk)) {
				String[] sNames = { "ntotalreceivenumber",// �ۼƷ�������
						"ntotalinvoicenumber",// �ۼƿ�Ʊ����
						"ntotalinventorynumber",// �ۼƳ�������
						"ntotalshouldoutnum",// ����Ӧ��
						"ntotalbalancenumber",// �ۼƽ�������
						"ntotalreturnnumber",// �ۼ��˻�����
						"narrangescornum",// �ۼư���ί�ⶩ������
						"narrangepoapplynum",// �ۼư����빺������
						"narrangetoornum",// �ۼư��ŵ�����������
						"norrangetoapplynum",// �ۼư��ŵ�����������
						"narrangemonum"// �ۼư���������������
				};
				for (int j = 0; j < sNames.length; j++) {
					if (bvos[i].getAttributeValue(sNames[j]) != null
							&& ((UFDouble) bvos[i].getAttributeValue(sNames[j]))
									.doubleValue() != 0)
						return true;
				}
				break;
			}
		}
		return false;

	}

	  public void setSortEnable(boolean isenable) {
		    if(!isenable){
		      getBillCardPanel().getBillTable().setSortEnabled(false);
		      getBillCardPanel().getBillTable().removeSortListener();
		    }else{
		      getBillCardPanel().getBillTable().setSortEnabled(true);
		      getBillCardPanel().getBillTable().addSortListener();
		    }
	  }
	
	  /**
	   * 
	   * ���ݱ���˵���������.
	   * @param e ufbill.BillEditEvent
	   */
	  public boolean onEditAction(int action){
	    
	    boolean isSort = getBillCardPanel().getBillTable().isSortEnabled();
	    BillActionListener  bal = getBillCardPanel().getBodyPanel().getBillActionListener(); 
	    boolean isdo = false;
	    try {
	      if(bal!=null)
	        getBillCardPanel().getBodyPanel().removeBillActionListener();
	      if (isSort)
	        setSortEnable(false);
	      getBillCardPanel().getBillModel().setNeedCalculate(false);
	    
	      switch(action){
	        case BillTableLineAction.ADDLINE:
	          onAddLine();
	          // �˴����ܷ���true���������true��UAP���Զ�����һ����
	          break;
	        case BillTableLineAction.DELLINE:
	          onDelLine();
	          isdo = true;
	          break;
	        /*
	         * ĿǰUAP�Ŀ�Ƭ�༭ֻ֧�����к�ɾ�У��������ֻ��Ҫ����ADDLINE��DELLINE
	         * Ϊ�˱��������ѭ������addLine()��delLine()��������removeListener�����
	         * ����պ���Ҫ֧������ķ���������Ҫ�ڸ�����Ӧ�ķ�����ȥ����removeListener�����
	        case BillTableLineAction.INSERTLINE:
		          onInsertLine();
		          break;  
	        case BillTableLineAction.COPYLINE:
	          onCopyLine();
	          break;
	        case BillTableLineAction.PASTELINE:
	          onPasteLine();
	          break;
	        case BillTableLineAction.PASTELINETOTAIL:
	          onPasteLineToTail();
	          break;
	        case BillTableLineAction.EDITLINE:
	          onCardEdit();
	          break;
			*/
	        default:
	        	isdo = true;
	      }
	    
	    } finally {
	      if(bal!=null)
	        getBillCardPanel().getBodyPanel().addBillActionListener(bal);
	      if (isSort)
	        setSortEnable(true);
	      getBillCardPanel().getBillModel().setNeedCalculate(true);
	    }
	    
	    return isdo;
	  }
	
	/**
	 * �����С� �������ڣ�(2001-4-13 16:37:56)
	 */
	public void onCopyLine() {
		getBillCardPanel().copyLine();
		iCopyRowCount = 0;
		if (getBillCardPanel().getBillTable().getSelectedRow() > -1) {
			int[] indexRows = getBillCardPanel().getBillTable()
					.getSelectedRows();
			oCopy = new Object[indexRows.length];
			if (getBillCardPanel().alInvs != null) {
				for (int i = 0; i < oCopy.length; i++) {
					oCopy[i] = getBillCardPanel().alInvs.get(indexRows[i]);
				}
			}
			iCopyRowCount = getBillCardPanel().getBillTable()
					.getSelectedRowCount();
		}
	}

	/**
	 * ճ���С� �������ڣ�(2001-4-13 16:37:56)
	 */
	public void onPasteLine() {
		if (checkPasteLine()) {
			// ȡ��ճ��ǰ����
			int iBefore = getBillCardPanel().getRowCount();
			int rowOld = getBillCardPanel().getBillTable().getSelectedRow();
			if (rowOld < 0)
				return;
			getBillCardPanel().pasteLine();

			int rowNew = getBillCardPanel().getBillTable().getSelectedRow();
			boolean boriginalcurprice = false;
			boolean boriginalcurtaxprice = false;
			try {
				int colCount = getBillCardPanel().getBillTable().getColumnCount();
				String originalcurpriceName = getBillCardPanel().getBodyItem("table", "noriginalcurprice").getName();
				String originalcurtaxpriceName = getBillCardPanel().getBodyItem("table", "noriginalcurtaxprice").getName();
				for (int i = 0; i < colCount; i++) {
					if (getBillCardPanel().getBillTable().getColumnName(i).equals(originalcurpriceName)) {
						boriginalcurprice = getBillCardPanel().getBillTable("table").isCellEditable(rowNew, i);
					}
					else if (getBillCardPanel().getBillTable().getColumnName(i).equals(originalcurtaxpriceName)) {
						boriginalcurtaxprice = getBillCardPanel().getBillTable("table").isCellEditable(rowNew, i);
					}
				}
			}
			catch (Exception e) {
			}
			
			 //    ճ���е�ʱ���ֹ��޸Ĺ��۸���б�ɫ 
		      ArrayList<Integer> aryrow = new ArrayList<Integer>();
			for (int i = rowOld; i < rowNew; i++) {
				getBillCardPanel().setCellEditable(i, "noriginalcurprice", boriginalcurprice);
				getBillCardPanel().setCellEditable(i, "noriginalcurtaxprice", boriginalcurtaxprice);
				
				// �����Դ��
				setItemsValue(getNeedSetNullItemsWhenCopy(), i, null);
				vRowATPStatus.insertElementAt(new UFBoolean(false), i);
				// ������ճ��
				if (getBillCardPanel().alInvs != null) {
					int indexCopy = i - rowOld;
					getBillCardPanel().alInvs.add(i, oCopy[indexCopy]);
				}
				
				if(getBillCardPanel().ifModifyPrice(i))
		            aryrow.add(i);
			}
			nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillCardPanel(), aryrow);
			
			if (vRowATPStatus.size() != getBillCardPanel().getRowCount()) {
				SCMEnv.out(getBillCardPanel().getRowCount() + "");
			}
			// ������λ�༭״̬����
			setAssistUnit(rowOld, rowNew);
			// ����
			setBatch(rowOld, rowNew);
			// ȡ��ճ��������
			int iAfter = getBillCardPanel().getRowCount();
			// ����ճ������
			int iRow = iAfter - iBefore;
			if (iBefore > 0 && iAfter > 0 && iRow > 0) {
				BillRowNo.pasteLineRowNo(getBillCardPanel(), getBillType(),
						getRowNoItemKey(), iRow);
			}
			boolean bisCalculate = getBillCardPanel().getBillModel()
					.isNeedCalculate();
			getBillCardPanel().getBillModel().setNeedCalculate(false);

			// ���������������
			getBillCardPanel().setBindAndLargeWhenPaste(rowOld, rowNew);
			getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
			if (getBillCardPanel().getHeadItem("nheadsummny") != null)
				getBillCardPanel().getHeadItem("nheadsummny").setValue(
						getBillCardPanel().getTotalValue("noriginalcursummny"));

		}
	}

	/**
	 * ճ������ʱ��⡣
	 * 
	 * �������ڣ�(2001-6-23 14:06:13)
	 * 
	 * @return boolean
	 * 
	 */
	private boolean checkPasteLine() {
		// �ޱ�����
		if (getBillCardPanel().getRowCount() == 0) {
			String strCustomerID = getBillCardPanel()
					.getHeadItem("ccustomerid").getValue();
			if (strCustomerID == null) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40060301", "UPP40060301-000180")/* @res "�ͻ�����Ϊ�ա�" */);
				return false;
			}
			String strCurrTypeID = getBillCardPanel().getHeadItem(
					"ccurrencytypeid").getValue();
			if (strCurrTypeID == null) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40060301", "UPP40060301-000181")/* @res "ԭ�Ҳ���Ϊ�ա�" */);
				return false;
			}
		}
		// ��������
		if (SO_01 != null) {
			if (SO_01.intValue() != 0) {
				if (SO_01.intValue() < getBillCardPanel().getRowCount()
						+ iCopyRowCount) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("40060301", "UPP40060301-000171", null,
									new String[] { SO_01.intValue() + "" }));
					// showErrorMessage("������" + CommonConstant.BEGIN_MARK
					// + SO_01.intValue() + CommonConstant.END_MARK + "�С�");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * �õ�������ʱ����Ҫ��յ���
	 * 
	 * wsy 2005-10-10
	 * 
	 */
	protected String[] getNeedSetNullItemsWhenCopy() {

		return new String[] {

				// v30 so_saleorder_b
				"tconsigntime", // ����ʱ��
				"tdelivertime",// ����ʱ��
				"ntaldcnum", // �Ѳ���۱�������
				"nasttaldcnum",// �Ѳ���۱�������
				"ntaldcmny", // �۱����
				"nretprofnum", // �Ѳ��뷵��������
				"nastretprofnum",// �Ѳ��뷵��������
				"nretprofmny", // �������
				"natp", // ATP

				// v30 so_saleexecute
				"ntalplconsigmny", // �Ѽƻ������������
				"ntaltransnum",// �ۼ���������
				"ntalbalancemny",// �ۼƽ�����
				"ntranslossnum",// �����������
				"biftransfinish",// ���������־
				"dlastconsigdate",// ���һ�η�������
				"dlasttransdate",// ���һ����������
				"dlastoutdate",// ���һ�γ�������
				"dlastinvoicedt",// ���һ�ο�Ʊ����
				"dlastpaydate",// ���һ�λؿ�����

				"bifinventoryfinish", "bifinvoicefinish", "bifpaybalance",
				"bifreceivefinish", "bifpaybalance", "bifpayfinish",
				"bifpaysign", "biftransfinish",

				"barrangedflag", "narrangemonum", "narrangepoapplynum",
				"narrangescornum", "narrangetoornum", "norrangetoapplynum",
				"narrangepoordernum",

				"carrangepersonid", "tlastarrangetime",

				"cfreezeid", "corder_bid",

				"ntotalbalancenumber", "ntotalinventorynumber",
				"ntotalinvoicenumber", "ntotalreceivenumber",
				"ntotalsignnumber","narrangetotolenum","arrangenum",

				"ntotalcostmny", "ntotalinvoicemny", "ntotalpaymny",
				"ntotalshouldoutnum", "ntotlbalcostnum", "nrushnum",

				"ts", "bindpricetype" };

	}

	private void setItemsValue(String[] sItemNames, int irow, Object value) {
		for (int i = 0; i < sItemNames.length; i++) {
			getBillCardPanel().setBodyValueAt(value, irow, sItemNames[i]);
		}
	}

	/**
	 * �Ƿ񸨼����� �������ڣ�(2001-11-30 15:20:14)
	 * 
	 * @param row
	 *            int
	 */
	private void setAssistUnit(int rowOld, int rowNew) {
		for (int row = rowOld; row < rowNew; row++) {
			getBillCardPanel().setAssistUnit(row);
		}
	}

	/**
	 * ���ο���
	 * 
	 * �������ڣ�(2001-11-30 15:20:14)
	 * 
	 * @param row
	 *            int
	 * 
	 */
	private void setBatch(int rowOld, int rowNew) {
		for (int row = rowOld; row < rowNew; row++) {
			// ���ο���:�ĵ�������������Ŀ��wholemanaflag ����
			Object temp = getBillCardPanel().getBodyValueAt(row,
					"wholemanaflag");
			boolean wholemanaflag = (temp == null ? false : new UFBoolean(temp
					.toString()).booleanValue());
			getBillCardPanel().setCellEditable(row, "fbatchstatus",
					wholemanaflag);
			getBillCardPanel().setCellEditable(row, "cbatchid", wholemanaflag);
		}
	}

	/**
	 * ճ���С� �������ڣ�(2001-4-13 16:37:56)
	 */
	public void onPasteLineToTail() {
		if (iCopyRowCount <= 0)
			return;

		if (checkPasteLine()) {
			// ȡ��ճ��ǰ����
			int iBefore = getBillCardPanel().getRowCount();
			getBillCardPanel().pasteLineToTail();

			// ȡ��ճ��������
			int iAfter = getBillCardPanel().getRowCount();

			ArrayList pricelist = new ArrayList();

			UFBoolean discountflag = null;
			
			 //    ճ���е�ʱ���ֹ��޸Ĺ��۸���б�ɫ 
		      ArrayList<Integer> aryrow = new ArrayList<Integer>();
			for (int i = iBefore; i < iAfter; i++) {

				setItemsValue(getNeedSetNullItemsWhenCopy(), i, null);

				vRowATPStatus.insertElementAt(new UFBoolean(false), i);
				// ������ճ��
				if (getBillCardPanel().alInvs != null) {
					int indexCopy = i - iBefore;
					getBillCardPanel().alInvs.add(i, oCopy[indexCopy]);
				}

				discountflag = getBillCardTools().getBodyUFBooleanValue(i,
						"discountflag");
				if (discountflag == null || !discountflag.booleanValue())
					pricelist.add(new Integer(i));
				
				if(getBillCardPanel().ifModifyPrice(i))
		            aryrow.add(i);
			}
			nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillCardPanel(), aryrow);

			if (vRowATPStatus.size() != getBillCardPanel().getRowCount()) {
				SCMEnv.out(getBillCardPanel().getRowCount() + "");
			}
			// ������λ�༭״̬����
			setAssistUnit(iBefore, iAfter);
			// ����
			setBatch(iBefore, iAfter);

			// ����ճ������
			int iRow = iAfter - iBefore;
			if (iBefore > 0 && iAfter > 0 && iRow > 0) {

				// ���㲢���������к�
				BillRowNo.addLineRowNos(getBillCardPanel(), getBillType(),
						getRowNoItemKey(), iRow);

			}
			boolean bisCalculate = getBillCardPanel().getBillModel()
					.isNeedCalculate();
			getBillCardPanel().getBillModel().setNeedCalculate(false);

			if (pricelist.size() > 0) {

				int[] findpricerows = new int[pricelist.size()];

				for (int i = 0, loop = pricelist.size(); i < loop; i++) {
					findpricerows[i] = ((Integer) pricelist.get(i)).intValue();
				}

				// Ѱ�۴���
				// getBillCardPanel().afterNumberEdit(findpricerows, "nnumber",
				// null, false, true);
			}
			// ���������������
			getBillCardPanel().setBindAndLargeWhenPaste(iBefore, iAfter);
			getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
			if (getBillCardPanel().getHeadItem("nheadsummny") != null)
				getBillCardPanel().getHeadItem("nheadsummny").setValue(
						getBillCardPanel().getTotalValue("noriginalcursummny"));

		}
	}

	public void updateButton(ButtonObject bo) {
		super.updateButton(bo);
	}

	public void freshOnhandnum(int row) {
		if (!m_bOnhandShowHidden)
			return;
		if (row < 0) {
			((IQueryOnHandInfoPanel) getPnlSouth(this)).showData(null);
			return;
		}
		OnHandRefreshVO onhandnumvo = getSelectedItemHandInfo();

		// �����ִ�����ѯ���������������֯������������������Ρ������ֿ�
		((IQueryOnHandInfoPanel) getPnlSouth(this)).showData(onhandnumvo);
	}

	protected UIPanel getPnlSouth(BillLineInfoListener listener) {

		if (m_pnlOnHand == null) {

			Object obj = null;
			try {
				obj = new QueryOnHandInfoPanel(listener, false);
			} catch (Exception e) {
				// ����ӿڵĳ������
				// e.printStackTrace();
				SCMEnv.out(e);
			}

			if (obj != null) {
				try {
					m_pnlOnHand = (UIPanel) ((IQueryOnHandInfoPanel) obj);
				} catch (Exception e) {
					SCMEnv.out(e);
					// e.printStackTrace();
				}
			}

		}
		return m_pnlOnHand;
	}

	public OnHandRefreshVO getSelectedItemHandInfo() {

		int row = getBillCardPanel().getBillTable().getSelectedRow();
		SaleOrderVO billVO = (SaleOrderVO) getVo();
		if (billVO == null || billVO.getChildrenVO() == null
				|| billVO.getChildrenVO().length == 0)
			return null;
		if (row < 0 || row >= billVO.getChildrenVO().length)
			return null;
		SaleorderBVO billitem = (billVO.getBodyVOs())[row];

		String[] saParam = new String[] { "ccorpid", "ccalbodyid", "cwhid",
				"cinvbasdoc", "cinvid", "castunitid", "invcode", "cspaceid",
				"vfree1", "vfree2", "vfree3", "vfree4", "vfree5" };
		String[] saOrg = new String[] { "pk_corp", "cadvisecalbodyid",
				"cbodywarehouseid", "cinvbasdocid", "cinventoryid",
				"cpackunitid", "cinventorycode", "cbatchid", "vfree1",
				"vfree2", "vfree3", "vfree4", "vfree5" };
		// �Բ��������ж�
		if ((billitem.getAttributeValue("pk_corp") == null || billitem
				.getAttributeValue("pk_corp").toString().trim().length() == 0)
				|| ((billitem.getAttributeValue("cinventoryid") == null || billitem
						.getAttributeValue("cinventoryid").toString().trim()
						.length() == 0))) {
			return null;
		}
		for (int i = 0; i < saParam.length; i++) {
			m_voLineOnHand.setAttributeValue(saParam[i], billitem
					.getAttributeValue(saOrg[i]));
		}

		return m_voLineOnHand;
	}

	public IFuncExtend getFuncExtend() {
		if (m_funcExtend == null && !binitFuncExtend) {
			m_funcExtend = nc.ui.scm.extend.FuncExtendInfo
					.getFuncExtendInstance("30");
			binitFuncExtend = true;
		}
		return m_funcExtend;
	}

	public boolean addNullLine(int istartrow, int count) {

		// if (checkAddLine(count)) {
		if (count <= 0)
			return false;
		for (int i = 1; i <= count; i++) {
			getBillCardPanel().addLine();
			// ���
			getBillCardPanel().alInvs.add(null);
			// ������״̬
			vRowATPStatus.add(new UFBoolean(false));
			// yb add 03-09-16
			setDefaultCellEditable(istartrow + i);
		}
		// Ĭ������
		// ���㲢���������к�
		BillRowNo.addLineRowNos(getBillCardPanel(), getBillType(),
				getRowNoItemKey(), count);

		getBillCardPanel().setBodyDefaultData(istartrow + 1,
				istartrow + 1 + count);

		return true;

	}

	public boolean insertNullLine(int istartrow, int count) {

		if (count <= 0)
			return true;
		if (istartrow >= getBillCardPanel().getRowCount() - 1)
			return false;
		int no = istartrow + 1;
		getBillCardPanel().getBillTable().getSelectionModel()
				.setSelectionInterval(no, no);

		for (int i = 1; i <= count; i++) {
			getBillCardPanel().insertLine();
			// ���
			if (no <= getBillCardPanel().alInvs.size())
				getBillCardPanel().alInvs.add(no, null);
			// ������״̬
			vRowATPStatus.insertElementAt(new UFBoolean(false), no);
			
			//TODO ��Ӧ��ȥ����ԭ�е�Ĭ�Ͽɱ༭��  v5.5 ȥ�� 
			// yb add 03-09-16
			//setDefaultCellEditable(no - 1);
			
			no++;
			getBillCardPanel().getBillTable().getSelectionModel()
					.setSelectionInterval(no, no);
		}

		// Ĭ������
		// ���㲢���������к�
		getBillCardPanel().getBillTable().getSelectionModel()
				.setSelectionInterval(no, no);
		BillRowNo.insertLineRowNos(getBillCardPanel(), getBillType(),
				getRowNoItemKey(), no, count);

		getBillCardPanel().setBodyDefaultData(istartrow + 1,
				istartrow + 1 + count);
		getBillCardPanel().getBillTable().getSelectionModel()
				.setSelectionInterval(istartrow, istartrow);

		return true;

	}

	/**
	 * �õ�����VO�� �������ڣ�(2001-6-23 9:47:36)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public AggregatedValueObject getVo() {
		return getVO(false);
	}

	/**
	 * �õ�����VO�� �������ڣ�(2001-6-23 9:47:36)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public AggregatedValueObject getVO(boolean isNeedRemoveDiscountInv) {
		SaleOrderVO saleorder = null;
		saleorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
				SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
				SaleorderBVO.class.getName());
		if (isNeedRemoveDiscountInv) {
			Vector vTemp = new Vector();
			SaleorderBVO[] itemVOs = (SaleorderBVO[]) saleorder.getChildrenVO();
			int indexSelected = getBillCardPanel().getBillTable()
					.getSelectedRow();

			for (int i = 0; i < itemVOs.length; i++) {
				// ��������������
				boolean notLabor = itemVOs[i].getLaborflag() == null
						|| !itemVOs[i].getLaborflag().booleanValue();
				// �������ۿ�����
				boolean notDiscount = itemVOs[i].getDiscountflag() == null
						|| !itemVOs[i].getDiscountflag().booleanValue();

				if (notLabor && notDiscount) {
					if (indexSelected > -1 && i == indexSelected) {
						// ���
						vTemp.addElement(itemVOs[i]);
					}
				}
			}
			SaleorderBVO[] itemsNew = new SaleorderBVO[vTemp.size()];
			vTemp.copyInto(itemsNew);
			saleorder.setChildrenVO(itemsNew);
		}
		// ��λ����
		if (saleorder.getHeadVO().getPk_corp() == null
				|| saleorder.getHeadVO().getPk_corp().trim().length() <= 0)
			((SaleorderHVO) saleorder.getParentVO())
					.setPk_corp(getCorpPrimaryKey());
		// ��������
		((SaleorderHVO) saleorder.getParentVO())
				.setCreceipttype(SaleBillType.SaleOrder);
		// ������
		((SaleorderHVO) saleorder.getParentVO())
				.setCapproveid(getClientEnvironment().getUser().getPrimaryKey());
		// �ջ���ַ
		UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
				"vreceiveaddress").getComponent();
		((SaleorderHVO) saleorder.getParentVO())
				.setVreceiveaddress(vreceiveaddress.getUITextField().getText());

		return saleorder;
	}

	protected String getRowNoItemKey() {
		return getBillCardPanel().getRowNoItemKey();
	}

	// ��ò�ѯ�Ի���ؼ�
	protected abstract SOBillQueryDlg getQueryDlg();

	// ��ò�ѯ�Ի���ؼ� �繫˾ר��
	protected abstract SOBillQueryDlg getQueryDlg(String pk_corp);

	public abstract InvokeEventProxy getPluginProxy();

}
