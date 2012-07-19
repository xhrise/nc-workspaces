package nc.ui.so.so001.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;

import nc.bs.framework.common.NCLocator;
import nc.itf.scm.so.pub.IARBusiness;
import nc.itf.uap.pf.IPFMetaModel;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.ui.bd.b21.CurrParamQuery;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.msg.PfLinkData;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.message.Message;
import nc.ui.scm.plugin.InvokeEventProxy;
import nc.ui.scm.print.BillPrintTool;
import nc.ui.scm.print.IFreshTsListener;
import nc.ui.scm.print.PrintLogClient;
import nc.ui.scm.print.SalePubPrintDS;
import nc.ui.scm.print.SplitParams;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.InvoInfoBYFormula;
import nc.ui.scm.pub.ScmPubHelper;
import nc.ui.scm.pub.query.ConvertQueryCondition;
import nc.ui.scm.pub.redunmulti.IUseSupplyTrans;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.pub.report.LocateDialog;
import nc.ui.scm.ref.prm.CustAddrRefModel;
import nc.ui.scm.so.RedunVOTool;
import nc.ui.scm.so.SaleBillType;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.ui.so.pub.DataPowerUtils;
import nc.ui.so.pub.InvAttrCellRenderer;
import nc.ui.so.pub.SoTaskManager;
import nc.ui.so.pub.plugin.SOPluginUI;
import nc.ui.so.so001.SaleOrderBO_Client;
import nc.ui.so.so001.panel.bom.BillTools;
import nc.ui.so.so001.panel.bom.BomorderBO_Client;
import nc.ui.so.so001.panel.card.SOBillCardTools;
import nc.ui.so.so001.panel.list.SaleBillListUI;
import nc.ui.so.so003.BillToBillUI;
import nc.ui.so.so016.OrderBalanceCardUI;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pfflow04.MessagedriveVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.bd.SmartVODataUtils;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.exp.ATPNotEnoughException;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.redun.ISourceRedunVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.so.pub.BusiUtil;
import nc.vo.so.so001.AtpCheckException;
import nc.vo.so.so001.AtpSetException;
import nc.vo.so.so001.BomorderHeaderVO;
import nc.vo.so.so001.BomorderItemVO;
import nc.vo.so.so001.BomorderVO;
import nc.vo.so.so001.ISaleOrderAction;
import nc.vo.so.so001.SOSaleBusinessPara;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so016.SaleReceiveBVO;
import nc.vo.so.so016.SaleReceiveHVO;
import nc.vo.so.so016.SaleReceiveVO;
import nc.vo.so.so016.SoVoTools;
import nc.vo.so.so120.CreditNotEnoughException;
import nc.vo.so.so120.PeriodNotEnoughException;
import nc.vo.so.sodispart.SaleDispartVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 * ���۵��ݻ��ࡣ
 * 
 * �������ڣ�(2001-4-13 15:26:05)
 * 
 * @author���ν�
 * 
 * �޸�ʱ�䣺2002-10-07
 * 
 * 2003-04-21 ������������
 * 
 * �޸����ڣ�2003-09-27 �޸��ˣ����� �޸����ݣ���˰���ۺ���˰��λ����˰���ۺ���˰�����㷨�޸�
 * 
 * �޸����ڣ�2003-10-15 �޸��ˣ����� �޸����ݣ��㷨�Ľ�
 * 
 * �޸����ڣ�2003-10-16 �޸��ˣ����� �޸����ݣ����ӹ�˾������SO21��Ԥ�����ȣ�
 * 
 * �޸����ڣ�2003-10-27 �޸��ˣ����� �޸����ݣ���˾������SO21���߼��͸�Ϊ�ַ��ͣ�ȡֵΪ���������͡���
 * 
 * @modified V5.01 zhongwei �޶�ʱ�ſ��������Ŀ���ջ���λ���ջ���ַ���ջ��ص�����
 * 
 * 
 * 
 * @refactored V5.1 ���۶���UI�˳�����࣬����������ҵ���߼��Ͳ��ֹ�������(����ť����) zhongwei
 * 
 * �޸����ڣ�2008-06-25 �޸��ˣ��ܳ�ʤ �޸����ݣ�v5.5���۶����޶���ͷ�����޶��汾�ź��޶����ڡ��޶��˵Ĵ洢
 */
public abstract class SaleBillUI extends SaleBillListUI implements IFreshTsListener, ILinkAdd,
		ILinkMaintain, ILinkApprove, ILinkQuery, IUseSupplyTrans {

	protected ButtonObject boClose = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"SCMCOMMON", "UPPSCMCommon-000119")/* @res "�ر�" */, NCLangRes.getInstance()
			.getStrByID("SCMCOMMON", "UPPSCMCommon-000120")/* @res "�رյ���" */, 0, "�ر�"); /*-=notranslate=-*/

	protected ButtonObject boOpen = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"SCMCOMMON", "UPPSCMCommon-000060")/* @res "��" */, NCLangRes.getInstance()
			.getStrByID("40060301", "UPP40060301-000157")/* @res "�򿪵���" */, 0, "��"); /*-=notranslate=-*/

	protected ButtonObject boATP = new ButtonObject("ATP", "ATP", 0);

	protected ButtonObject boAfterAction;

	protected ButtonObject boConsignment = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000161")/* @res "����" */, NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000161")/*
												 * @res "����"
												 */, 0, "����");

	protected ButtonObject boOutStore = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000162")/* @res "����" */, NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000162")/*
												 * @res "����"
												 */, 0, "����");

	protected ButtonObject boMakeInvoice = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000163")/* @res "��Ʊ" */, NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000163")/*
												 * @res "��Ʊ"
												 */, 0, "��Ʊ");

	protected ButtonObject boGathering = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000164")/* @res "�տ�" */, NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000164")/*
												 * @res "�տ�"
												 */, 0, "�տ�");

	protected ButtonObject boRequestPurchase = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000165")/* @res "�빺" */, NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000165")/*
												 * @res "�빺"
												 */, 0, "�빺");

	// �������۰�ť��
	protected ButtonObject[] bomButtonGroup;

	// ����Ի���
	SourceBillFlowDlg soureDlg = null;

	private boolean isSaveStart = true;

	protected boolean binitOnNewByOther = false;

	private String strUISource;

	protected boolean bInMsgPanel = false;

	protected String headwarehouseRefWhereSql = null;

	// ����ֱ��UI
	protected BillToBillUI billToBillUI = null;

	protected OrderBalanceCardUI orderBalanceUI = null;

	protected SoTaskManager soTaskManager = null;

	protected PrintLogClient printLogClient = null;

	protected Vector vRowATPStatus_bak = null;

	protected ArrayList alInvs_bak = null;

	protected HashMap hsMapSendAudit = new HashMap();

	// �б���ŵ��ݴ�ӡbyzxj
	// ��ʼ����ӡ�ӿ�
	protected SaleOrderPrintDataInterface m_dataSource;

	protected SalePubPrintDS m_spdatasource;

	protected PrintEntry m_print;

	// �ֵ���ӡ�Ի���
	private SaleOrderSplitDLG spDLG = null;

	// �Ƿ��Ѿ����й���ѯ�������ж�ˢ�°�ť״̬ʹ�ã�
	private boolean b_query;

	ProccDlg proccdlg;

	WorkThread work;

	// ����ʱ�Ĵ�����Ϣ��
	private ArrayList<Integer> rowList = new ArrayList<Integer>();

	protected static final String SELFBILL = "makeflag";

	protected String coBusiType = null;

	protected String coPoOrderId = null;

	protected String coPoTs = null;

	// ���滺���ʼ��VO:��������
	protected AggregatedValueObject initAvo = null;

	// ���ο������
	private InvokeEventProxy pluginproxy;

	private java.util.Map<String, java.util.ArrayList<UIMenuItem>> m_bodyMenuItems;

	public SaleBillUI() {
		super();
	}

	public SaleBillUI(String pk_corp, String billtype, String busitype, String operator, String id) {
		super(pk_corp, billtype, busitype, operator, id);
	}

	public InvokeEventProxy getPluginProxy() {
		if (pluginproxy == null) {
			try {
				pluginproxy = new InvokeEventProxy("SO", "30", getClientUI(), new SOPluginUI(this,
						"30"));
			} catch (BusinessException e) {
				SCMEnv.out(e);
			}
		}

		return pluginproxy;
	}

	/**
	 * ���ʵ���������
	 * 
	 * @return
	 */
	protected abstract String getClientUI();

	protected void loadAllBtns() {
		super.loadAllBtns();
		getBoAfterAction();
	}

	/**
	 * ���붩��VO��ͨ���Զ�������VO�Ľ�����ͳ�� ���ٳ���ȱ������Ʒ���������Ȼ�����Ԥ�տ�����ͽ�
	 * 
	 * �������ڣ�(2003-11-07 13:22:27)
	 * 
	 * @param saleOrder
	 *            nc.vo.so.so001.SaleOrderVO
	 * @author:����
	 * @description:
	 */
	private boolean calculatePreceive(SaleOrderVO saleorder) {
		BillItem bi = getBillCardPanel().getHeadItem("npreceiverate");
		if (bi == null) {
			SCMEnv.out("����Ԥ�տ������ģ���в�����!");
			return true;
		}
		bi = getBillCardPanel().getHeadItem("npreceivemny");
		if (bi == null) {
			SCMEnv.out("����Ԥ�տ�����ģ���в�����!");
			return true;
		}

		// ����Ԥ�տ������Ԥ�տ���
		UFDouble npreceiverate = null;
		UFDouble npreceivemny = null;
		// ԭ�Ҽ�˰�ϼ�
		UFDouble noriginalcursummny = new UFDouble(0);

		SaleorderHVO ordhvo = saleorder.getHeadVO();
		SaleorderBVO[] ordbvos = saleorder.getBodyVOs();
		for (int i = 0, loop = ordbvos.length; i < loop; i++) {
			// ȱ������
			UFBoolean boosflag = ordbvos[i].getBoosflag() == null ? new UFBoolean(false)
					: ordbvos[i].getBoosflag();
			// ��Ʒ����
			UFBoolean blargessflag = ordbvos[i].getBlargessflag() == null ? new UFBoolean(false)
					: ordbvos[i].getBlargessflag();

			// ����������

			if (ordbvos[i].getCinventoryid() == null
					|| ordbvos[i].getCinventoryid().trim().length() <= 0)
				continue;
			if (!boosflag.booleanValue() && !blargessflag.booleanValue()
			/* && !isappendant.booleanValue() */) {
				UFDouble dobj = ordbvos[i].getNoriginalcursummny();
				if (dobj == null)
					dobj = new UFDouble(0);
				noriginalcursummny = noriginalcursummny.add(dobj);
			}
		}
		npreceiverate = ordhvo.getNpreceiverate();
		npreceivemny = ordhvo.getNpreceivemny();

		if (npreceiverate == null && npreceivemny == null) {
			return true;
		}

		if (npreceiverate != null && npreceiverate.doubleValue() == 0 && npreceivemny != null
				&& npreceivemny.doubleValue() == 0) {
			return true;
		}

		UFDouble temp = npreceivemny;
		if (temp == null && npreceiverate != null && "����".equals(SO_21)) {
			temp = noriginalcursummny.multiply(npreceiverate).div(100);
		}

		/** �������ݲ����Ƚ�* */
		if (ordhvo.getCsaleid() != null && ordhvo.getCsaleid().length() > 0) {
			if ((ordhvo.getNreceiptcathmny() != null && temp != null)
					&& (temp.sub(ordhvo.getNreceiptcathmny()).doubleValue() < 0)) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000544")/*
												 * @res "Ԥ�տ����С���տ���"
												 */);
				return false;
			}

		}

		if (noriginalcursummny.doubleValue() < 0) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000174")/*
																									 * @res
																									 * "������˰�ϼ�С��0ʱ����������Ԥ�տ����Ԥ�տ����"
																									 */);
			return false;
		}

		if (noriginalcursummny.doubleValue() == 0) {
			ordhvo.setNpreceiverate(null);
			ordhvo.setNpreceivemny(null);
			getBillCardPanel().setHeadItem("npreceiverate", null);
			getBillCardPanel().setHeadItem("npreceivemny", null);
			return true;
		}

		// ����Ԥ�ۿ����Ϊ�յ����
		if (npreceiverate == null) {
			if (npreceivemny.doubleValue() < 0
					|| npreceivemny.doubleValue() > noriginalcursummny.doubleValue()) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000175")/*
												 * @res "Ԥ�տ���ܴ��ڶ�����˰�ϼƻ�С��0"
												 */);
				return false;
			}
			npreceiverate = npreceivemny.div(noriginalcursummny).multiply(100);
			ordhvo.setNpreceiverate(npreceiverate);
			getBillCardPanel().setHeadItem("npreceiverate", npreceiverate);
			getBillCardPanel().setHeadItem("npreceivemny", npreceivemny);
			return true;

		}

		// ����Ԥ�տ���Ϊ�յ����
		if (npreceivemny == null) {
			if (npreceiverate.doubleValue() < 0 || npreceiverate.doubleValue() > 100) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000176")/*
												 * @res "Ԥ�տ�������ܴ���100��С��0"
												 */);
				return false;
			}
			npreceivemny = noriginalcursummny.multiply(npreceiverate).div(100);
			ordhvo.setNpreceivemny(npreceivemny);
			getBillCardPanel().setHeadItem("npreceiverate", npreceiverate);
			getBillCardPanel().setHeadItem("npreceivemny", npreceivemny);
			return true;

		}

		if ("����".equals(SO_21)) {/*-=notranslate=-*/
			if (npreceiverate.doubleValue() < 0 || npreceiverate.doubleValue() > 100) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000176")/*
												 * @res "Ԥ�տ�������ܴ���100��С��0"
												 */);
				return false;
			}
			npreceivemny = noriginalcursummny.multiply(npreceiverate).div(100);
		} else if ("���".equals(SO_21)) {/*-=notranslate=-*/
			if (npreceivemny.doubleValue() < 0
					|| npreceivemny.doubleValue() > noriginalcursummny.doubleValue()) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000178")/*
												 * @res "Ԥ�տ����С��0����ڶ������"
												 */);
				return false;
			}
			npreceiverate = npreceivemny.div(noriginalcursummny).multiply(100);
		}

		if (npreceiverate.doubleValue() > 100) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000179")/*
																									 * @res
																									 * "Ԥ�տ�������ܴ���100"
																									 */);
			return false;
		}

		ordhvo.setNpreceiverate(npreceiverate);
		ordhvo.setNpreceivemny(npreceivemny);
		getBillCardPanel().setHeadItem("npreceiverate", npreceiverate);
		getBillCardPanel().setHeadItem("npreceivemny", npreceivemny);
		return true;
	}

	/**
	 * �����Դ�����Ƿ���Ժϲ��� �������ڣ�(2003-8-4 10:28:45)
	 */
	private boolean checkSourceComb(AggregatedValueObject[] voSource) {
		SaleorderBVO[] saleBs = (SaleorderBVO[]) voSource[0].getChildrenVO();
		if (saleBs == null || saleBs.length == 0)
			return true;
		String creceipttype = saleBs[0].getCreceipttype();
		if (creceipttype == null || creceipttype.length() == 0) {
			return true;
		}
		// wsy 20060329 ����Լ��
		boolean bCustomer = false;// ��Ҫ�ͻ�Ψһ��
		boolean bcurr = false;// ��Ҫ����Ψһ��
		boolean bsaleorg = false;// ��Ҫ�ͻ�Ψһ��
		if (creceipttype.equals(SaleBillType.SoContract)
				|| creceipttype.equals(SaleBillType.SoInitContract)) {
			// ��ͬ �ͻ�+����
			bCustomer = true;
			bcurr = true;
		}
		// ���۵�
		else if (creceipttype.equals(SaleBillType.SaleQuotation)) {
			// ���۱��۵��γ����۶���ʱ��Ҫ����Լ������
			// �ͻ�+����+������֯����Ψһ��Լ��
			bCustomer = true;
			bcurr = true;
			bsaleorg = true;
		} else if (creceipttype.equals("4H")) {
			// ������γ����۶���ʱ��Ҫ����Լ��������
			// �� ���գ��ͻ�����Ψһ��Լ��
			bCustomer = true;
		}

		// ���պ�ͬ
		if (voSource.length > 1) {
			// �ͻ���׼ֵ
			if (bCustomer) {
				String ccustomerid0 = ((SaleorderHVO) voSource[0].getParentVO()).getCcustomerid();
				if (ccustomerid0 != null) {
					for (int i = 1; i < voSource.length; i++) {
						String ccustomerid = ((SaleorderHVO) voSource[i].getParentVO())
								.getCcustomerid();
						if (ccustomerid != null) {
							if (!ccustomerid.trim().equals(ccustomerid0.trim())) {
								showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
										"UPP40060301-000182")/*
																 * @res
																 * "��ѡ��ͬһ�ͻ������ݺϳɶ���"
																 */);
								return false;
							}
						}
					}
				}
			}
			// ���ֻ�׼ֵ
			if (bcurr) {
				SaleorderBVO[] salebodyVO = (SaleorderBVO[]) voSource[0].getChildrenVO();
				String currencyid0 = salebodyVO[0].getCcurrencytypeid();
				if (currencyid0 != null) {
					for (int i = 1; i < voSource.length; i++) {
						salebodyVO = (SaleorderBVO[]) voSource[i].getChildrenVO();
						String currencyid = salebodyVO[0].getCcurrencytypeid();
						if (!currencyid.trim().equals(currencyid0.trim())) {
							showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
									"UPP40060301-000183")/*
															 * @res
															 * "��ѡ��ͬһ���ֵ����ݺϳɶ���"
															 */);
							return false;
						}
					}
				}
			}
			// ������֯��׼ֵ
			if (bsaleorg) {
				String ccustomerid0 = ((SaleorderHVO) voSource[0].getParentVO()).getCsalecorpid();
				if (ccustomerid0 != null) {
					for (int i = 1; i < voSource.length; i++) {
						String ccustomerid = ((SaleorderHVO) voSource[i].getParentVO())
								.getCsalecorpid();
						if (ccustomerid != null) {
							if (!ccustomerid.trim().equals(ccustomerid0.trim())) {
								showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
										"UPP40060301-000513")/*
																 * @res
																 * "��ѡ��ͬһ������֯�����ݺϳɶ���"
																 */);
								return false;
							}
						}
					}
				}

			}

		}

		return true;
	}

	/**
	 * ��ø�����Ҫ������ �������ڣ�(2001-11-23 9:03:31)
	 * 
	 * @return java.lang.String
	 * @param bo
	 *            nc.ui.pub.ButtonObject
	 */
	private Object getAssistantPara(ButtonObject bo) throws ValidationException {

		int row = getBillListPanel().getHeadTable().getSelectedRow();

		Object o = null;
		// �������(����ID)
		if (bo.getTag().equals("OrderAlterRpt") || bo.getTag().equals("OrderExecRpt")) {
			if (strShowState.equals("�б�")) { /*-=notranslate=-*/
				o = getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");
			} else {
				o = getBillCardPanel().getHeadItem("csaleid").getValue();
			}
		}
		// ATP
		if (bo.getTag().equals("ATP")) {
			o = getVO(true);
		}
		// �ͻ���Ϣ
		if (bo.getTag().equals("CustInfo")) {
			if (strShowState.equals("�б�")) { /*-=notranslate=-*/
				o = getBillListPanel().getHeadBillModel().getValueAt(num, "ccustomerid");
			} else {
				o = getBillCardPanel().getHeadItem("ccustomerid").getValue();
			}
		}
		// �ͻ�����
		if (bo.getTag().equals("CustCredited")) {
			String sCust = null;
			String sBiztype = null;
			String sProductLine = null;
			if (strShowState.equals("�б�")) { /*-=notranslate=-*/
				Object oTemp = getBillListPanel().getHeadBillModel().getValueAt(num, "ccustomerid");
				sCust = oTemp == null ? null : oTemp.toString();

				oTemp = getBillListPanel().getHeadBillModel().getValueAt(num, "cbiztype");
				sBiztype = oTemp == null ? null : oTemp.toString();

				oTemp = getBillListPanel().getBodyBillModel().getValueAt(0, "cprolineid");
				sProductLine = oTemp == null ? null : oTemp.toString();
			} else {
				sCust = (String) getBillCardPanel().getHeadItem("ccustomerid").getValueObject();
				sBiztype = (String) getBillCardPanel().getHeadItem("cbiztype").getValueObject();
				sProductLine = (String) getBillCardPanel().getBodyValueAt(0, "cprolineid");
			}

			nc.vo.so.pub.CustCreditVO voCredit = new nc.vo.so.pub.CustCreditVO();
			voCredit.setPk_cumandoc(sCust);
			voCredit.setCbiztype(sBiztype);
			// ������ϸ��ղ�Ʒ�ߣ���null�����򣬴���һ�б���
			if (SO27 == null || !SO27.booleanValue())
				voCredit.setCproductline(null);
			else
				voCredit.setCproductline(sProductLine);

			o = voCredit;
		}
		// �������
		if (bo.getTag().equals("FlowState")) {
			java.util.ArrayList alTemp = new java.util.ArrayList();
			alTemp.add(getBillType());
			if (strShowState.equals("�б�")) { /*-=notranslate=-*/
				alTemp.add(getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid"));
			} else {
				alTemp.add(getBillCardPanel().getHeadItem("csaleid").getValueObject());
			}
			o = alTemp;
		}
		// ë��
		if (bo.getTag().equals("Prifit")) {
			if (strShowState.equals("�б�")) { /*-=notranslate=-*/
				nc.vo.so.so006.ProfitHeaderVO headVO = new nc.vo.so.so006.ProfitHeaderVO();
				// ��˾
				headVO.setPkcorp(getCorpPrimaryKey());
				// �����֯ID
				headVO.setCcalbodyid((String) getBillListPanel().getHeadBillModel().getValueAt(row,
						"ccalbodyid"));
				// ��������
				headVO.setBilltype(getBillType());
				// ����
				if (getBillListPanel().getBodyTable().getRowCount() > 0) {
					String curID = (String) (getBillListPanel().getBodyBillModel().getValueAt(0,
							"ccurrencytypeid"));
					headVO.setCurrencyid(curID);
				}
				// �����֯����
				headVO.setCcalbodyname((String) getBillListPanel().getHeadBillModel().getValueAt(
						row, "ccalbodyname"));
				nc.vo.so.so006.ProfitItemVO[] bodyVOs = new nc.vo.so.so006.ProfitItemVO[getBillListPanel()
						.getBodyBillModel().getRowCount()];
				for (int i = 0; i < bodyVOs.length; i++) {
					nc.vo.so.so006.ProfitItemVO bodyVO = new nc.vo.so.so006.ProfitItemVO();

					String creccalbodyid = (String) getBillListPanel().getBodyBillModel()
							.getValueAt(i, "creccalbodyid");
					if (creccalbodyid != null && creccalbodyid.trim().length() > 0) {
						// yb edit 2003-11-05
						bodyVO.setCbodycalbodyid((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "creccalbodyid"));
						bodyVO.setCbodycalbodyname((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "creccalbody"));
						bodyVO.setCbodywarehouseid((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "crecwareid"));
						bodyVO.setCbodywarehousename((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "crecwarehouse"));
					} else {
						// yb edit 2003-11-05
						bodyVO.setCbodycalbodyid((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "cadvisecalbodyid"));
						bodyVO.setCbodycalbodyname((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "cadvisecalbody"));
						bodyVO.setCbodywarehouseid((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "cbodywarehouseid"));
						bodyVO.setCbodywarehousename((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "cbodywarehousename"));
					}

					// ���ID
					bodyVO.setCinventoryid((String) getBillListPanel().getBodyBillModel()
							.getValueAt(i, "cinventoryid"));
					// �������
					bodyVO.setCode((String) getBillListPanel().getBodyBillModel().getValueAt(i,
							"cinventorycode"));
					// �������
					bodyVO.setName((String) getBillListPanel().getBodyBillModel().getValueAt(i,
							"cinventoryname"));
					// ����ͺ�
					bodyVO.setSize((String) getBillListPanel().getBodyBillModel().getValueAt(i,
							"GGXX"));
					// ����
					bodyVO.setCbatchid((String) getBillListPanel().getBodyBillModel().getValueAt(i,
							"cbatchid"));
					// ����
					bodyVO.setNumber((UFDouble) getBillListPanel().getBodyBillModel().getValueAt(i,
							"nnumber"));
					// ����
					bodyVO.setNnetprice((UFDouble) getBillListPanel().getBodyBillModel()
							.getValueAt(i, "nnetprice") == null ? new UFDouble(0)
							: (UFDouble) getBillListPanel().getBodyBillModel().getValueAt(i,
									"nnetprice"));
					bodyVO.setNmny((UFDouble) getBillListPanel().getBodyBillModel().getValueAt(i,
							"nmny") == null ? new UFDouble(0) : (UFDouble) getBillListPanel()
							.getBodyBillModel().getValueAt(i, "nmny"));

					bodyVOs[i] = bodyVO;
					if (getBillListPanel().getBodyBillModel().getValueAt(i, "blargessflag") != null
							&& getBillListPanel().getBodyBillModel().getValueAt(i, "blargessflag")
									.toString().equals("false"))
						bodyVO.m_blargessflag = new UFBoolean(false);
					else
						bodyVO.m_blargessflag = new UFBoolean(true);
				}
				nc.vo.so.so006.ProfitVO profit = new nc.vo.so.so006.ProfitVO();
				profit.setParentVO(headVO);
				profit.setChildrenVO(bodyVOs);
				profit.validate();
				o = profit;
			}// end list handle
			else {
				o = getProfitVOonCardPanel();
			}// end card handle
		}
		return o;
	}

	/**
	 * �ӿ�Ƭ�����ϻ�ȡ�����������
	 * 
	 * @return
	 * @throws Exception
	 */
	private Object getProfitVOonCardPanel() throws ValidationException {
		nc.vo.so.so006.ProfitHeaderVO headVO = new nc.vo.so.so006.ProfitHeaderVO();
		// ��˾
		headVO.setPkcorp(getCorpPrimaryKey());
		// �����֯ID
		headVO.setCcalbodyid(getBillCardPanel().getHeadItem("ccalbodyid").getValue());
		// �����֯����
		UIRefPane calBodyName = (UIRefPane) getBillCardPanel().getHeadItem("ccalbodyid")
				.getComponent();
		headVO.setCcalbodyname(calBodyName.getRefName());
		// ��������
		headVO.setBilltype(getBillType());
		// ����
		headVO.setCurrencyid((String) getBillCardPanel().getHeadItem("ccurrencytypeid")
				.getValueObject());
		nc.vo.so.so006.ProfitItemVO[] bodyVOs = new nc.vo.so.so006.ProfitItemVO[getBillCardPanel()
				.getRowCount()];
		for (int i = 0; i < bodyVOs.length; i++) {
			nc.vo.so.so006.ProfitItemVO bodyVO = new nc.vo.so.so006.ProfitItemVO();

			String creccalbodyid = (String) getBillCardPanel().getBodyValueAt(i, "creccalbodyid");
			if (creccalbodyid != null && creccalbodyid.trim().length() > 0) {
				// yb edit 2003-11-05
				bodyVO.setCbodycalbodyid((String) getBillCardPanel().getBodyValueAt(i,
						"creccalbodyid"));
				bodyVO.setCbodycalbodyname((String) getBillCardPanel().getBodyValueAt(i,
						"creccalbody"));
				bodyVO.setCbodywarehouseid((String) getBillCardPanel().getBodyValueAt(i,
						"crecwareid"));
				bodyVO.setCbodywarehousename((String) getBillCardPanel().getBodyValueAt(i,
						"crecwarehouse"));
			} else {
				// yb edit 2003-11-05
				bodyVO.setCbodycalbodyid((String) getBillCardPanel().getBodyValueAt(i,
						"cadvisecalbodyid"));
				bodyVO.setCbodycalbodyname((String) getBillCardPanel().getBodyValueAt(i,
						"cadvisecalbody"));
				bodyVO.setCbodywarehouseid((String) getBillCardPanel().getBodyValueAt(i,
						"cbodywarehouseid"));
				bodyVO.setCbodywarehousename((String) getBillCardPanel().getBodyValueAt(i,
						"cbodywarehousename"));
			}

			// ���ID
			bodyVO.setCinventoryid((String) getBillCardPanel().getBodyValueAt(i, "cinventoryid"));
			// �������
			bodyVO.setCode((String) getBillCardPanel().getBodyValueAt(i, "cinventorycode"));
			// �������
			bodyVO.setName((String) getBillCardPanel().getBodyValueAt(i, "cinventoryname"));
			// ����ͺ�
			bodyVO.setSize((String) getBillCardPanel().getBodyValueAt(i, "GGXX"));
			// ����
			bodyVO.setCbatchid((String) getBillCardPanel().getBodyValueAt(i, "cbatchid"));
			// ����
			bodyVO.setNumber((UFDouble) getBillCardPanel().getBodyValueAt(i, "nnumber"));
			// ����
			Object value = getBillCardPanel().getBodyValueAt(i, "nnetprice");
			if (value == null || value.toString().toString().equals(""))
				value = new UFDouble(0);
			bodyVO.setNnetprice((UFDouble) value);
			if (getBillCardPanel().getBodyValueAt(i, "blargessflag") != null
					&& getBillCardPanel().getBodyValueAt(i, "blargessflag").toString().equals(
							"false"))
				bodyVO.m_blargessflag = new UFBoolean(false);
			else
				bodyVO.m_blargessflag = new UFBoolean(true);
			value = getBillCardPanel().getBodyValueAt(i, "nmny");
			if (value == null || value.toString().toString().equals(""))
				value = new UFDouble(0);
			bodyVO.setNmny((UFDouble) value);
			bodyVOs[i] = bodyVO;
		}
		nc.vo.so.so006.ProfitVO profit = new nc.vo.so.so006.ProfitVO();
		profit.setParentVO(headVO);
		profit.setChildrenVO(bodyVOs);
		profit.validate();

		return profit;
	}

	/**
	 * ��ð�ť������ �������ڣ�(2001-11-15 8:56:16)
	 * 
	 * @return java.lang.String
	 * @param bo
	 *            nc.ui.pub.ButtonObject
	 */
	public abstract String getBillButtonAction(ButtonObject bo);

	/**
	 * ��õ��ݰ�ť״̬�顣 �������ڣ�(2001-11-15 8:48:20)
	 * 
	 * @return java.lang.String
	 */
	public abstract String getBillButtonState();

	/**
	 * ��ð�ť���顣 �������ڣ�(2001-11-15 8:58:51)
	 * 
	 * @return nc.ui.pub.ButtonObject[]
	 */
	public abstract ButtonObject[] getBillButtons();

	/**
	 * ���ص�ǰ������ID�� �������ڣ�(2001-11-15 9:10:05)
	 * 
	 * @return java.lang.String
	 */
	public abstract String getBillID();

	/**
	 * ���ص�ǰ������ID�� �������ڣ�(2001-11-15 9:10:05)
	 * 
	 * @return java.lang.String
	 */
	private String getBillIDSource() {
		if (getBillListPanel().isShowing()) {
			num = getBillListPanel().getHeadTable().getSelectedRow();
		}

		if (num >= 0 && num < getBillListPanel().getHeadBillModel().getRowCount())
			return (String) getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");
		else
			/** ȡ��Ƭ�ϵ�ֵ��������ֱ�ӿ�Ƭ�����б���û��ֵ�����* */
			return (String) getBillCardPanel().getHeadItem("csaleid").getValueObject();
	}

	/**
	 * ��õ������͡� �������ڣ�(2001-11-15 8:52:43)
	 * 
	 * @return java.lang.String
	 */
	public String getBillType() {
		return SaleBillType.SaleOrder;
	}

	/**
	 * ����к� ���к�Ϊ��������¼��Ӧ����Ȼ�������к�,Ϊ��Ǹ��еĿ�������׼����
	 * 
	 * �������ڣ�(2002-10-11 15:28:57)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 * @param voSource
	 *            nc.vo.so.so001.SaleOrderVO
	 */
	private SaleOrderVO getLineNumber(SaleOrderVO voSource, SaleOrderVO voChange) {
		SaleorderBVO[] voBodys = (SaleorderBVO[]) voSource.getChildrenVO();
		Vector vec = new Vector();
		// ��ñ仯��¼���к�
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			if (getBillCardPanel().getBillModel().getRowState(i) != BillModel.NORMAL) {
				voBodys[i].setNlinenumber(new Integer(i + 1));
				vec.addElement(voBodys[i]);
			}
		}
		// ���ɾ�����м�¼
		SaleorderBVO[] voChangeBodys = (SaleorderBVO[]) voChange.getChildrenVO();
		for (int i = 0; i < voChangeBodys.length; i++) {
			if (voChangeBodys[i].getStatus() == nc.vo.pub.VOStatus.DELETED) {
				vec.addElement(voChangeBodys[i]);
			}
		}
		SaleorderBVO[] retBodys = new SaleorderBVO[vec.size()];
		if (vec.size() > 0) {
			vec.copyInto(retBodys);
		}

		SaleOrderVO copyvo = new SaleOrderVO();
		copyvo.setParentVO(voSource.getParentVO());
		copyvo.setChildrenVO(retBodys);
		// voSource.setChildrenVO(retBodys);
		return copyvo;
	}

	/**
	 * �ν������������޸ġ� �������ڣ�(2003-10-15 10:05:48)
	 * 
	 * @return int[]
	 */
	private int[] getNotCheckFreeItemLine() {
		if (getBillCardPanel().alInvs == null || getBillCardPanel().alInvs.size() <= 0)
			return null;
		ArrayList indexlist = new ArrayList();
		for (int i = 0; i < getBillCardPanel().alInvs.size(); i++) {
			InvVO vo = (InvVO) getBillCardPanel().alInvs.get(i);
			if (vo == null)
				continue;
			if (vo.getFreeItemVO() != null) {
				if ((vo.getFreeItemVO().getVfreeid1() == null || vo.getFreeItemVO().getVfreeid1()
						.length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid2() == null || vo.getFreeItemVO()
								.getVfreeid2().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid3() == null || vo.getFreeItemVO()
								.getVfreeid3().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid4() == null || vo.getFreeItemVO()
								.getVfreeid4().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid5() == null || vo.getFreeItemVO()
								.getVfreeid5().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid6() == null || vo.getFreeItemVO()
								.getVfreeid6().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid7() == null || vo.getFreeItemVO()
								.getVfreeid7().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid8() == null || vo.getFreeItemVO()
								.getVfreeid8().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid9() == null || vo.getFreeItemVO()
								.getVfreeid9().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid10() == null || vo.getFreeItemVO()
								.getVfreeid10().length() <= 0)) {
					indexlist.add(new Integer(i));
				}
			} else
				indexlist.add(new Integer(i));

		}

		if (indexlist.size() <= 0)
			return null;

		int[] ret = new int[indexlist.size()];
		for (int i = 0; i < indexlist.size(); i++) {
			ret[i] = ((Integer) indexlist.get(i)).intValue();
		}
		return ret;
	}

	/**
	 * ��õ������͡�
	 * 
	 * �������ڣ�(2001-11-15 8:52:43)
	 * 
	 * @return java.lang.String
	 */
	public nc.ui.scm.sourcebill.SourceBillFlowDlg getSourceDlg() {

		soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(this, getBillType(),/* ��ǰ�������� */
		getBillIDSource(),/* ��ǰ����ID */
		getBillCardPanel().getBusiType(),/* ��ǰҵ������ */
		getBillCardPanel().getOperator(),/* ��ǰ�û�ID */
		((SaleOrderVO) getVo()).getHeadVO().getVreceiptcode()/* ���ݺ� */
		);

		return soureDlg;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public abstract String getTitle();

	/**
	 * ���沿���׳��쳣ʱ������
	 * 
	 * @param ex
	 *            java.lang.Throwable
	 */
	protected void handleSaveException(java.lang.Throwable ex) {
		showWarningMessage(ex.getMessage());
		showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000185")/*
																							 * @res
																							 * "����ʧ�ܣ�"
																							 */);
	}

	public void handleException(Exception e, String msg) {
		MessageDialog.showWarningDlg(this, "����", msg + e.getMessage());
		SCMEnv.out(e.getMessage());
	}

	/**
	 * ��ʼ��ִ�а�ť
	 */
	protected void retElseBtn(String billbuttonstate, String billbuttonaction) {

		// ����ִ��
		if (billbuttonaction.equals("Order001")) {
			boAction.removeAllChildren();

			// ����
			if (billbuttonstate.equals("Order001")) {
				// boAudit.setTag("APPROVE");
				// boAction.addChildButton(boAudit);
			}

			// ������Ƭ
			else if (billbuttonstate.equals("Order002") || billbuttonstate.equals("Order003")) {

				boAction.addChildButton(boSendAudit);

				boAction.addChildButton(boCancelAudit);
				boAction.addChildButton(boFreeze);
				boAction.addChildButton(boCancelFreeze);
				boAction.addChildButton(boFinish);

			}
		}

		// ��������
		else if (billbuttonaction.equals("Order003")) {

			boAssistant.removeAllChildren();

			// // ����
			if (billbuttonstate.equals("Order001")) {

				boAssistant.addChildButton(boCustCredit);
				boAssistant.addChildButton(boCustInfo);
				boAssistant.addChildButton(boPrifit);
				boAssistant.addChildButton(boOnHandShowHidden);

			}

			// // ������Ƭ
			else if (billbuttonstate.equals("Order002") || billbuttonstate.equals("Order003")) {

				boAssistant.addChildButton(boRefundment);
				boAssistant.addChildButton(boCachPay);
				boAssistant.addChildButton(boOrdBalance);
				boAssistant.addChildButton(boStockLock);
				boAssistant.addChildButton(boSendInv);
				boAssistant.addChildButton(boSupplyInv);
				boAssistant.addChildButton(boDirectInv);
				boAssistant.addChildButton(boDocument);
				boAssistant.addChildButton(boBom);

			}

		}

		// ����ִ��
		else if (billbuttonaction.equals("BomOrder001")) {
			boAction.removeAllChildren();

			// ����
			if (billbuttonstate.equals("BomOrder001")) {
				boAction.addChildButton(boAudit);
			}

			// ����
			else if (billbuttonstate.equals("BomOrder002")) {
				boAction.addChildButton(boAudit);
				boAction.addChildButton(boCancelAudit);

			}

			// ��Ƭ
			else if (billbuttonstate.equals("BomOrder003")) {
				boAction.addChildButton(boAudit);
			}

		}
		// ���ƺ���
		else if (billbuttonaction.equals("BomOrder002")) {

		}

	}

	/**
	 * ��ʼ����ť
	 * 
	 * �������ڣ�(01-2-26 13:29:17)
	 * 
	 * @comment ���าд�˷���ʱ������մ˴�����
	 * 
	 */
	protected void initButtons() {

		// ҵ������
		PfUtilClient.retBusinessBtn(boBusiType, getCorpPrimaryKey(), getBillType());

		// onBusiType(boBusiType.getChildButtonGroup()[0]);

		if (strState.equals("BOM")) {
			setButtons(bomButtonGroup);
		} else {

			initLineButton();

			if (boBusiType.getChildButtonGroup() != null
					&& boBusiType.getChildButtonGroup().length > 0) {

				ButtonObject[] bo = boBusiType.getChildButtonGroup();
				for (int i = 0; i < bo.length; i++) {
					bo[i].setName(bo[i].getName());
				}
				boBusiType.setChildButtonGroup(bo);

				boBusiType.setTag(boBusiType.getChildButtonGroup()[0].getTag());
				boBusiType.getChildButtonGroup()[0].setSelected(true);
				boBusiType.setCheckboxGroup(true);

				// ����
				PfUtilClient.retAddBtn(boAdd, getCorpPrimaryKey(), getBillType(), boBusiType
						.getChildButtonGroup()[0]);

				bo = boAdd.getChildButtonGroup();
				for (int i = 0; i < bo.length; i++) {
					bo[i].setName(bo[i].getName());
				}
				boAdd.setChildButtonGroup(bo);

				retElseBtn(getBillButtonState(), getBillButtonAction(boAction));
				retElseBtn(getBillButtonState(), getBillButtonAction(boAssistant));
			} else {
				SCMEnv.out("û�г�ʼ��ҵ������!");
			}

			setButtons(getBillButtons());
		}
	}

	private void initInvList() {

		getBillCardPanel().alInvs = new java.util.ArrayList();
		if (getBillCardPanel().getRowCount() <= 0)
			return;

		try {
			// ������ȡ�����Ϣ
			InvVO[] invvos = new InvVO[getBillCardPanel().getRowCount()];
			for (int i = 0; i < invvos.length; i++) {
				invvos[i] = new InvVO();
				invvos[i].setCinventoryid((String) getBillCardPanel().getBodyValueAt(i,
						"cinventoryid"));
			}

			InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
			invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

			for (int i = 0; i < invvos.length; i++)
				getBillCardPanel().alInvs.add(invvos[i]);

			if (getBillCardPanel().alInvs != null) {
				for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
					InvVO voInv = (InvVO) getBillCardPanel().alInvs.get(i);
					getBillCardPanel().setBodyFreeValue(i, voInv);
				}
			}

			this.alInvs = getBillCardPanel().alInvs;

		} catch (Exception ex) {
			SCMEnv.out("����������ʧ��!");
		}
	}

	/**
	 * ��ʼ���б༭��Ŧ��
	 * 
	 * �������ڣ�(2001-11-27 14:53:22)
	 * 
	 */
	protected void initLineButton() {
		boLine.removeAllChildren();
		boLine.addChildButton(boAddLine);
		boLine.addChildButton(boDelLine);
		boLine.addChildButton(boCopyLine);
		boLine.addChildButton(boPasteLine);
		boLine.addChildButton(boPasteLineToTail);
		boLine.addChildButton(boFindPrice);
		boLine.addChildButton(boCardEdit);
		boLine.addChildButton(boResortRowNo);
	}

	/**
	 * ���ؿ�Ƭ���ݡ�
	 * 
	 * �������ڣ�(2001-11-15 9:02:22)
	 * 
	 */
	public void loadCardData() {
		try {
			long s = System.currentTimeMillis();
			boolean bisCalculate = getBillCardPanel().getBillModel().isNeedCalculate();
			getBillCardPanel().getBillModel().setNeedCalculate(false);

			SaleOrderVO saleorderVO = (SaleOrderVO) SaleOrderBO_Client.queryData(getBillID());
			// ��ѯ���ս��
			if (saleorderVO != null && saleorderVO.getHeadVO().getCsaleid() != null) {
				saleorderVO.getHeadVO().setNreceiptcathmny(
						SaleOrderBO_Client
								.queryCachPayByOrdId(saleorderVO.getHeadVO().getCsaleid()));
			}
			SCMEnv.out("���ݶ�ȡ[����ʱ" + (System.currentTimeMillis() - s) + "]");
			s = System.currentTimeMillis();
			// ������Я������
			getBillCardPanel().setPanelByCurrency(
					((SaleorderBVO) saleorderVO.getChildrenVO()[0]).getCcurrencytypeid());

			// �ջ���ַ����
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			vreceiveaddress.setAutoCheck(false);

			// ��������
			getBillCardPanel().setBillValueVO(saleorderVO);
			// ִ�м��ع�ʽ
			getBillCardPanel().getBillModel().execLoadFormula();

			// �����Ƿ��ӡ�ϼ�
			copyIstotal();
			SCMEnv.out("��������[����ʱ" + (System.currentTimeMillis() - s) + "]");
			long s1 = System.currentTimeMillis();

			getBillCardPanel().getBillModel().execLoadFormula();

			SCMEnv.out("ִ�й�ʽ[����ʱ" + (System.currentTimeMillis() - s1) + "]");

			s1 = System.currentTimeMillis();

			// �ջ���ַ����
			if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null)
				((CustAddrRefModel) vreceiveaddress.getRefModel())
						.setCustId((String) getBillCardPanel().getHeadItem("creceiptcustomerid")
								.getValueObject());

			Object temp = saleorderVO.getParentVO().getAttributeValue("vreceiveaddress");
			// ��������ID
			String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
			getBillCardPanel().execHeadFormula(formula);

			// �ջ���ַ
			vreceiveaddress.getUITextField().setText(temp == null ? "" : temp.toString());

			// ���������ۿ�
			UFDouble ndiscountrate = getBillCardTools().getHeadUFDoubleValue("ndiscountrate");
			if (ndiscountrate == null) {
				ndiscountrate = getBillCardTools().getBodyUFDoubleValue(0, "ndiscountrate");
			}
			if (ndiscountrate == null)
				ndiscountrate = new UFDouble(100);

			getBillCardPanel().setHeadItem("ndiscountrate", ndiscountrate);

			// ����������
			getBillCardPanel().initFreeItem();

			// ���ر�ͷ��ĿЯ��������
			setHeadDefaultData();

			// ������
			getBillCardPanel().initUnit();

			SCMEnv.out("���ر�ͷ[����ʱ" + (System.currentTimeMillis() - s1) + "]");

			s1 = System.currentTimeMillis();

			// ����״̬��ʽ,BOM��־ //yb �Ż�����
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				String[] formulas = {
						"wholemanaflag->getColValue(bd_invmandoc,wholemanaflag,pk_invmandoc,cinventoryid)",
						"isconfigable->getColValue(bd_invmandoc,isconfigable,pk_invmandoc,cinventoryid)",
						"isspecialty->getColValue(bd_invmandoc,isspecialty,pk_invmandoc,cinventoryid)" };
				getBillCardPanel().getBillModel().execFormulas(formulas);
			}
			// ����Ʒ��־����
			String[] appendFormula = { "isappendant->getColValue(bd_invmandoc,isappendant,pk_invmandoc,cinventoryid)" };
			getBillCardPanel().getBillModel().execFormulas(appendFormula);

			ArrayList alist = new ArrayList();
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				// ������λ

				/** ��ʹ�����㷨���߼���* */
				// // ���㸨������˰����,��������˰����
				// BillTools.calcViaPrice1(i, getBillCardPanel().getBillModel(),
				// getBillType());
				// ��ͬ���������Я��
				Object oCsourcebillid = getBillCardPanel().getBillModel().getValueAt(i,
						"csourcebillid");
				if (oCsourcebillid != null && oCsourcebillid.toString().length() != 0) {
					alist.add(new Integer(i));
				}
			}

			String[] bodyFormula = new String[2];
			bodyFormula[0] = "ctinvclassid->getColValue(ct_manage_b,invclid,pk_ct_manage_b,csourcebillbodyid)";
			bodyFormula[1] = "ctinvclass->getColValue(bd_invcl,invclassname,pk_invcl,ctinvclassid)";
			getBillCardTools().execBodyFormulas(bodyFormula, alist);

			// ���۹�˾
			getBillCardPanel().setHeadItem("salecorp",
					getClientEnvironment().getCorporation().getUnitname());

			SCMEnv.out("���ر���[����ʱ" + (System.currentTimeMillis() - s1) + "]");

			getBillCardPanel().updateValue();

			getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
			if (getBillCardPanel().getHeadItem("nheadsummny") != null)
				getBillCardPanel().getHeadItem("nheadsummny").setValue(
						getBillCardPanel().getTotalValue("noriginalcursummny"));

			setButtonsState();
			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000186",
					null, new String[] { (System.currentTimeMillis() - s) / 1000 + "" }));

		} catch (ValidationException e) {
			showErrorMessage(e.getMessage());
		} catch (Exception e) {
			showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256")/*
																									 * @res
																									 * "���ݼ���ʧ�ܣ�"
																									 */);
			// e.printStackTrace(System.out);
		}
	}

	private void copyIstotal() {
		int iRow = getBillCardPanel().getRowCount();
		for (int i = 0; i < iRow; i++) {
			Object largess = getBillCardPanel().getBodyValueAt(i, "blargessflag");
			if (largess != null && new UFBoolean(largess.toString()).booleanValue()) {
				getBillCardPanel().setBodyValueAt("1", i, "is_total");
			}
		}
	}

	/**
	 * ���ؿ�Ƭ���ݡ�
	 * 
	 * ybadd �������ڣ�(2003-8-26 9:02:22)
	 * 
	 */
	public void loadCardData(SaleOrderVO billvo) {
		if (billvo == null) {
			getBillCardPanel().addNew();
			return;
		}

		try {

			long s = System.currentTimeMillis();

			// ������Я������
			getBillCardPanel().setPanelByCurrency(
					((SaleorderBVO) billvo.getChildrenVO()[0]).getCcurrencytypeid());
			// �ջ���ַ����
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			vreceiveaddress.setAutoCheck(false);

			// ���ñ�ͷ���ʾ���
			getBillCardTools().setHeadItemDigits(
					"nexchangeotobrate",
					getSOCurrencyRateUtil().getExchangeRateDigits(
							billvo.getHeadVO().getCcurrencytypeid()));

			// ��������
			getBillCardPanel().setBillValueVO(billvo);

			// ���ñ�����ʾ���
			nc.ui.so.so001.panel.bom.BillTools.setExchangeRateDigitsByCurrency(getBillCardPanel()
					.getBillModel(), billvo.getChildrenVO(), "ccurrencytypeid",
					billvo.getPk_corp(), "nexchangeotobrate");

			// ִ�м��ع�ʽ
			getBillCardPanel().execHeadLoadFormulas();
			getBillCardPanel().getBillModel().execLoadFormula();

			// �����Ƿ��ӡ�ϼ�
			copyIstotal();

			long s1 = System.currentTimeMillis();

			SCMEnv.out("ִ�й�ʽ[����ʱ" + (System.currentTimeMillis() - s1) + "]");
			if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null)
				((CustAddrRefModel) vreceiveaddress.getRefModel())
						.setCustId((String) getBillCardPanel().getHeadItem("creceiptcustomerid")
								.getValueObject());
			Object temp = billvo.getParentVO().getAttributeValue("vreceiveaddress");
			// ��������ID
			String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
			// String pk_cubasdoc = (String)
			getBillCardPanel().execHeadFormula(formula);

			// �ջ���ַ
			vreceiveaddress.getUITextField().setText(temp == null ? "" : temp.toString());

			// ����������
			getBillCardPanel().initFreeItem();

			// ���ر�ͷ��ĿЯ��������
			/** ������أ���ͷȥ����* */
			setHeadDefaultData();

			// ������
			getBillCardPanel().initUnit();

			SCMEnv.out("���ر�ͷ����bo[����ʱ" + (System.currentTimeMillis() - s) + "]");
			s = System.currentTimeMillis();

			// �������Լ���
			String[] formulas = {
					"wholemanaflag->getColValue(bd_invmandoc,wholemanaflag,pk_invmandoc,cinventoryid)",
					"isconfigable->getColValue(bd_invmandoc,isconfigable,pk_invmandoc,cinventoryid)",
					"isspecialty->getColValue(bd_invmandoc,isspecialty,pk_invmandoc,cinventoryid)",
					"isappendant->getColValue(bd_invmandoc,isappendant,pk_invmandoc,cinventoryid)" };

			// ����Ʒ��־����
			getBillCardPanel().getBillModel().execFormulas(formulas);

			ArrayList alist = new ArrayList();
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				// ������λ

				/** ��ʹ�����㷨���߼���* */
				// ���㸨������˰����,��������˰����
				// BillTools.calcViaPrice1(i, getBillCardPanel().getBillModel(),
				// getBillCardPanel().getBillType());
				// ��ͬ���������Я��
				Object oCsourcebillid = getBillCardPanel().getBillModel().getValueAt(i,
						"csourcebillid");
				if (oCsourcebillid != null && oCsourcebillid.toString().length() != 0) {

					alist.add(new Integer(i));

				}
			}

			String[] bodyFormula = new String[2];
			bodyFormula[0] = "ctinvclassid->getColValue(ct_manage_b,invclid,pk_ct_manage_b,csourcebillbodyid)";
			bodyFormula[1] = "ctinvclass->getColValue(bd_invcl,invclassname,pk_invcl,ctinvclassid)";
			getBillCardTools().execBodyFormulas(bodyFormula, alist);

			getBillCardTools().setBodyInventory1(0, getBillCardPanel().getRowCount());

			// ���������ۿ�
			UFDouble ndiscountrate = getBillCardTools().getHeadUFDoubleValue("ndiscountrate");
			if (ndiscountrate == null) {
				ndiscountrate = getBillCardTools().getBodyUFDoubleValue(0, "ndiscountrate");
			}
			if (ndiscountrate == null)
				ndiscountrate = new UFDouble(100);

			getBillCardPanel().setHeadItem("ndiscountrate", ndiscountrate);

			// ���۹�˾
			getBillCardPanel().setHeadItem("salecorp",
					getClientEnvironment().getCorporation().getUnitname());

			getBillCardPanel().updateValue();
			getBillCardPanel().getBillModel().reCalcurateAll();
			if (getBillCardPanel().getHeadItem("nheadsummny") != null)
				getBillCardPanel().getHeadItem("nheadsummny").setValue(
						getBillCardPanel().getTotalValue("noriginalcursummny"));

			// ���֧��
			getPluginProxy().afterSetBillVOToCard(billvo);

			SCMEnv.out("���ر�������bo[����ʱ" + (System.currentTimeMillis() - s) + "]");

		} catch (Exception e) {
			showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256")/*
																									 * @res
																									 * "���ݼ���ʧ�ܣ�"
																									 */);
			// e.printStackTrace(System.out);
		}
	}

	/**
	 * ��������ID��
	 * 
	 * �������ڣ�(2001-11-15 9:02:22)
	 * 
	 */
	private void loadIDafterADD(ArrayList listID) {
		try {
			removeOOSLine();
			// ���¼��ر�ͷ

			if (listID == null)
				return;
			getBillCardPanel().setHeadItem("csaleid", (String) listID.get(0));
			getBillCardPanel().setHeadItem("vreceiptcode", (String) listID.get(1));

			Object ots = listID.get(listID.size() - 2);
			SaleOrderVO retVO = (SaleOrderVO) listID.get(listID.size() - 1);
			SaleorderBVO[] retbvos = retVO.getBodyVOs();
			String[] keys = new String[] { "noriginalcursummny", "noriginalcurmny",
					"noriginalcurtaxmny", "ntaxmny", "nmny", "nsummny" };

			for (int i = 2; i < listID.size() - 2; i++) {
				getBillCardPanel().setBodyValueAt((String) listID.get(0), i - 2, "csaleid");
				getBillCardPanel().setBodyValueAt((String) listID.get(i), i - 2, "corder_bid");

				for (String key : keys)
					getBillCardPanel().setBodyValueAt(retbvos[i - 2].getAttributeValue(key), i - 2,
							key);

				getBillCardPanel().setBodyValueAt(ots, i - 2, "ts");
				getBillCardPanel().setBodyValueAt(ots, i - 2, "exets");

			}

			getBillCardPanel().setHeadItem("ts", ots);

		} catch (Exception e) {
			showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256")/*
																									 * @res
																									 * "���ݼ���ʧ�ܣ�"
																									 */);
			// e.printStackTrace(System.out);
		}
	}

	/**
	 * ��������ID��
	 * 
	 * �������ڣ�(2001-11-15 9:02:22)
	 * 
	 */
	private void loadIDafterEDIT(ArrayList listID) {
		try {
			removeOOSLine();
			// ���¼��ر�ͷTS
			String formula[] = { "vreceiptcode->getColValue(so_sale,vreceiptcode,csaleid,csaleid)" };
			getBillCardPanel().execHeadFormulas(formula);

			if (listID == null)
				return;
			int i = 1;
			Object ots = listID.get(listID.size() - 2);
			for (int j = 0; j < getBillCardPanel().getRowCount(); j++) {
				if (getBillCardPanel().getBillModel().getRowState(j) == BillModel.ADD) {
					// �鵽��IDΪ�յģ�����ID����
					getBillCardPanel().setBodyValueAt(
							getBillCardPanel().getHeadItem("csaleid").getValueObject(), j,
							"csaleid");
					getBillCardPanel().setBodyValueAt((String) listID.get(i), j, "corder_bid");

					getBillCardPanel().setBodyValueAt(ots, j, "ts");
					getBillCardPanel().setBodyValueAt(ots, j, "exets");

					i++;
				}
				if (getBillCardPanel().getBillModel().getRowState(j) == BillModel.MODIFICATION) {
					getBillCardPanel().setBodyValueAt(ots, j, "ts");
					getBillCardPanel().setBodyValueAt(ots, j, "exets");
				}
			}
			getBillCardPanel().setHeadItem("ts", ots);// listID.get(listID.size()-2));
		} catch (Exception e) {
			showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256")/*
																									 * @res
																									 * "���ݼ���ʧ�ܣ�"
																									 */);
			// e.printStackTrace(System.out);
		}
	}

	/**
	 * ��Ǵ���еĿ�����״̬��
	 * 
	 * �������ڣ�(2002-10-11 15:52:18)
	 * 
	 * @param sMessage
	 *            java.lang.String
	 * 
	 */
	private void markRow(String message) {
		Vector vecTemp = new Vector();
		int pos0 = 0;
		if (message != null) {
			do {
				int start = message.indexOf("<-", pos0);
				if (start < 0)
					break;
				int end = message.indexOf("->", start);
				String temp = message.substring(start + 2, end);
				vecTemp.add(temp);
				SCMEnv.out(temp);
				pos0 = end;
			} while (true);
		}
		vRowATPStatus = new Vector();
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			vRowATPStatus.addElement(new UFBoolean(false));
		}
		// ���û�п���������Ĵ��������տ�����״̬����
		if (vecTemp != null && vecTemp.size() != 0) {
			for (int i = 0; i < vecTemp.size(); i++) {
				int iRow = new Integer(vecTemp.elementAt(i).toString()).intValue();
				vRowATPStatus.setElementAt(new UFBoolean(true), iRow - 1);
			}
		}
		updateUI();
	}

	/**
	 * �жϵ�ǰҵ�������У����۶����ǿ繫˾ֱ����ʽ���ɵ�������
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	private boolean ifPushSave205A5D(SaleOrderVO vo) throws BusinessException {
		boolean flag = false;

		// **********���˵�������˾�Ͷ�����˾��ͬ�Ķ�����**********
		String pk_corp = vo.getPk_corp();
		SaleorderBVO[] bvos = vo.getBodyVOs();
		String consigncorpid;
		ArrayList<SaleorderBVO> al_to = new ArrayList<SaleorderBVO>();
		for (int i = 0, len = bvos.length; i < len; i++) {
			consigncorpid = bvos[i].getCconsigncorpid();
			if (consigncorpid == null || consigncorpid.trim().length() <= 0
					|| consigncorpid.equals(pk_corp))
				continue;
			al_to.add(bvos[i]);
		}// end for
		if (al_to.size() == 0)
			return flag;
		// **********���˵�������˾�Ͷ�����˾��ͬ�Ķ�����**********

		// ��ȡ��Ϣ������Ϣ
		IPFMetaModel bo = (IPFMetaModel) NCLocator.getInstance().lookup(
				IPFMetaModel.class.getName());

		MessagedriveVO message[] = bo.queryAllMsgdrvVOs(vo.getPk_corp(), SaleBillType.SaleOrder, vo
				.getBizTypeid(), "APPROVE");

		// ��˾���̲鲻�����鼯��
		if (message == null || message.length <= 0) {
			message = bo.queryAllMsgdrvVOs("@@@@", SaleBillType.SaleOrder, vo.getBizTypeid(),
					"APPROVE");
		}

		// �жϵ�ǰҵ�������У����۶����Ƿ���ʽ���ɵ�������
		if (message != null && message.length != 0) {
			for (int j = 0; j < message.length; j++) {
				if (message[j].getActiontype().toString().equals("PushSave205A5D")) {
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	protected void onAction(ButtonObject bo) {

		long s_init = System.currentTimeMillis();

		if (bo == boClose) {
			return;
		}

		// ɾ��ǰ��ʾ
		if (bo.getTag().equals("SoBlankout")) {
			// ������Ч����
			if ((strShowState.equals("�б�") && getBillListPanel().getHeadTable()
					.getSelectedRowCount() >= 1)
					|| strShowState.equals("��Ƭ")) {
				// ��ʾ
				if (showYesNoMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000540")/*
												 * @res "�Ƿ�ȷʵҪɾ��?"
												 */) != MessageDialog.ID_YES) {
					return;
				}
			}
		}

		// �б�����µ�������
		if (strShowState.equals("�б�") /*-=notranslate=-*/
				&& getBillListPanel().getHeadTable().getSelectedRowCount() > 1) {
			if (bo.getTag().equals("APPROVE")) {
				onApprove(bo);
			} else if (bo.getTag().equals("SoUnApprove")) {
				onUnApprove(bo);
			} else if (bo.getTag().equals("SoBlankout")) {
				onBlankout(bo);
			}

			return;

		}

		SaleOrderVO saleorder = (SaleOrderVO) getVO(false);

		Integer fstatus = saleorder.getHeadVO().getFstatus();
		if (fstatus != null && fstatus.intValue() == BillStatus.BLANKOUT) {
			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000500")/*
																								 * @res
																								 * "���������ϣ�"
																								 */);
			return;
		}

		long s1 = 0;
		try {
			// long s = System.currentTimeMillis();

			if (saleorder != null)
				saleorder.validate();

			SaleorderBVO[] ordbvo = saleorder.getBodyVOs();

			if (bo.getTag().equals("SoBlankout")) {
				saleorder.setIAction(ISaleOrderAction.A_BLANKOUT);
				for (int i = 0, loop = ordbvo.length; i < loop; i++) {
					// ����atp���¶������Ա����ATP
					ordbvo[i].setIAction(ISaleOrderAction.A_OUTEND);
					ordbvo[i].setStatus(VOStatus.DELETED);

				}
			} else if (bo.getTag().equals("SoUnApprove")) {
				saleorder.setIAction(ISaleOrderAction.A_UNAUDIT);
				for (int i = 0, loop = ordbvo.length; i < loop; i++) {
					// ����atp���¶������Ա����ATP
					ordbvo[i].setIAction(ISaleOrderAction.A_UNAUDIT);
				}
			} else if (bo.getTag().equals("APPROVE")) {
				saleorder.setIAction(ISaleOrderAction.A_AUDIT);
				for (int i = 0, loop = ordbvo.length; i < loop; i++) {
					// ����atp���¶������Ա����ATP
					ordbvo[i].setIAction(ISaleOrderAction.A_AUDIT);
				}
			} else if (bo.getTag().equals("OrderFinish")) {
				saleorder.setIAction(ISaleOrderAction.A_CLOSE);
				for (int i = 0, loop = ordbvo.length; i < loop; i++) {
					// ����atp���¶������Ա����ATP
					ordbvo[i].setIAction(ISaleOrderAction.A_OUTEND);
				}
			} else if (bo.getTag().equals("OrderFreeze")) {
				saleorder.setIAction(ISaleOrderAction.A_FREEZE);
				for (int i = 0, loop = ordbvo.length; i < loop; i++) {
					// //����atp���¶������Ա����ATP
					ordbvo[i].setIAction(ISaleOrderAction.A_DEFAULT);
				}
			} else if (bo.getTag().equals("OrderUnFreeze")) {
				saleorder.setIAction(ISaleOrderAction.A_UNFREEZE);
				for (int i = 0, loop = ordbvo.length; i < loop; i++) {
					// //����atp���¶������Ա����ATP
					ordbvo[i].setIAction(ISaleOrderAction.A_DEFAULT);
				}
			}

			if (bo.getTag().equals("APPROVE")) {
				// �����ж� liuhui zhangcheng v5.3 Ϊ��֧�ֿ繫˾���� ��˾ȡ���ݱ�ͷ��˾����У��
				new BusiUtil().isInvoiceFirstNewFrom501(saleorder.getPk_corp(), saleorder
						.getBizTypeid());

				onApproveCheck(saleorder);
				onApproveCheckWorkflow(saleorder);
				nc.vo.scm.pub.SCMEnv.out("��˼��ͨ��");
			}
			if (bo.getTag().equals("SoUnApprove")) {
				onUnApproveCheck(saleorder);
			}

			nc.vo.scm.pub.SCMEnv.out("<====== ���۶���:[" + bo.getName() + "]���� ## ���ýű�ǰ׼��ʱ�乲��ʱ["
					+ (System.currentTimeMillis() - s_init) / 1000 + "."
					+ (System.currentTimeMillis() - s_init) % 1000 + "��]==============>");

			nc.vo.scm.pub.session.ClientLink clientLink = new nc.vo.scm.pub.session.ClientLink(
					getClientEnvironment());
			saleorder.setClientLink(clientLink);
			saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
			saleorder.setDcurdate(getClientEnvironment().getDate());
			saleorder.setCusername(getClientEnvironment().getUser().getUserName());
			saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());
			saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000504")
			/* @res "ӆ�ι���" */);

			s1 = System.currentTimeMillis();

			if (bo.getTag().equals("APPROVE")) {

				getBillCardTools().getPkarrivecorp(saleorder);
				saleorder.getHeadVO().setCapproveid(clientLink.getUser());

				saleorder.processVOForTrans();

				Object otemp = null;
				boolean bContinue = true;
				while (bContinue) {
					try {

						// ����ѹ�� V51 ��������
						ObjectUtils.objectReference(saleorder);

						otemp = PfUtilClient.processActionFlow(this, bo.getTag(), getBillType(),
								getClientEnvironment().getDate().toString(), saleorder, null);
						bContinue = false;
					} catch (Exception ex) {
						bContinue = doException(saleorder, null, ex);

					}
				}

				if (otemp != null) {
					String ErrMsg = otemp.toString();
					if (ErrMsg != null && ErrMsg.startsWith("ERR")) {
						showWarningMessage(ErrMsg.substring(3));
					}
				}
			} else if (bo.getTag().equals("SoUnApprove")) {

				saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
				saleorder.processVOForTrans();
				boolean bContinue = true;
				while (bContinue) {

					try {
						// ����������
						PfUtilClient.processActionFlow(this, bo.getTag()
								+ getClientEnvironment().getUser().getPrimaryKey(),
								SaleBillType.SaleOrder,
								getClientEnvironment().getDate().toString(), saleorder, null);
						bContinue = false;

						if (ifPushSave205A5D(saleorder))
							MessageDialog.showHintDlg(this, "������ʾ", "���ݺ�Ϊ��"
									+ saleorder.getHeadVO().getVreceiptcode()
									+ "�����۶������Ѿ��Զ�ɾ�����ε�������/��������");

					} catch (Exception ex) {
						bContinue = doException(saleorder, null, ex);

					}
				}

			}
			// ɾ��
			else {

				if (bo.getTag().equals("SoBlankout")) {
					saleorder.getParentVO().setStatus(VOStatus.UNCHANGED);
					((SaleorderHVO) saleorder.getParentVO())
							.setVoldreceiptcode(((SaleorderHVO) saleorder.getParentVO())
									.getVreceiptcode());

					// ���� ���򲻴�����д
					saleorder.setFirstTime(true);

					// ���֧��
					getPluginProxy().beforeAction(Action.DELETE, new SaleOrderVO[] { saleorder });

				}

				saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

				saleorder.processVOForTrans();

				boolean bContinue = true;
				while (bContinue) {
					try {

						PfUtilClient.processActionNoSendMessage(this, bo.getTag(),
								SaleBillType.SaleOrder,
								getClientEnvironment().getDate().toString(), saleorder, null, null,
								null);
						bContinue = false;
						// ɾ��ʱ����ǲ��ղɹ����������۶�������Ҫ��дԴ�ɹ�����
						if (saleorder.getHeadVO().isCoopped()) {

							String sourceId = (String) saleorder.getBodyVOs()[0].getCsourcebillid();
							reWritePO(new String[] { sourceId }, false);

						}
					} catch (Exception ex) {
						bContinue = doException(saleorder, null, ex);

					}
				}

			}

			nc.vo.scm.pub.SCMEnv.out("<====== ���۶���:[" + bo.getName() + "]���� ## ���ýű����̹���ʱ["
					+ (System.currentTimeMillis() - s1) / 1000 + "."
					+ (System.currentTimeMillis() - s1) % 1000 + "��]==============>");

			if (PfUtilClient.isSuccess()) {

				s1 = System.currentTimeMillis();

				BillTools.reLoadBillState(this, getClientEnvironment());
				showCustManArInfo();

				showMessageWhenAction(bo);

				if (strShowState.equals("�б�")) { /*-=notranslate=-*/
					if (bo.getTag().equals("OrderFinish")) {
						// ��ѯ���ս��
						UFDouble dtempmny = SaleOrderBO_Client.queryCachPayByOrdId(saleorder
								.getHeadVO().getCsaleid());
						getBillListPanel().getHeadBillModel().setValueAt(dtempmny,
								getBillListPanel().getHeadTable().getSelectedRow(),
								"nreceiptcathmny");
					}
				}
				if (bo.getTag().equals("SoBlankout")) {
					vocache.deleteByID(saleorder.getHeadVO().getCsaleid());

					int[] irows = new int[] { getBillListPanel().getHeadTable().getSelectedRow() };
					if (strShowState.equals("�б�")) {
						getBillListPanel().getHeadBillModel().delLine(irows);
						getBillListPanel().getHeadBillModel().updateValue();

						getBillListPanel().getBodyBillModel().clearBodyData();
						getBillListPanel().getBodyBillModel().updateValue();
					} else {
						getBillCardPanel().addNew();
						getBillCardPanel().setHeadItem("vreceiptcode", null);
						getBillCardPanel().getBillModel().clearBodyData();
						getBillCardPanel().updateValue();

						// //ͬʱˢ���б����
						if (irows[0] > -1) {
							getBillListPanel().getHeadBillModel().delLine(irows);
							getBillListPanel().getHeadTable().clearSelection();
							getBillListPanel().getHeadBillModel().updateValue();

							getBillListPanel().getBodyBillModel().clearBodyData();
							getBillListPanel().getBodyBillModel().updateValue();
						}

						if (vocache.getCacheSize() == 0)
							getBillListPanel().getHeadBillModel().clearBodyData();
					}

				} else {

					updateCacheVO();
				}

				if (!strShowState.equals("�б�")) {
					getBillCardPanel().updateValue();
					nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel());
				}

				nc.vo.scm.pub.SCMEnv.out("<====== ���۶���:[" + bo.getName() + "]���� ## ���ýű���ˢ�½��湲��ʱ["
						+ (System.currentTimeMillis() - s1) / 1000 + "."
						+ (System.currentTimeMillis() - s1) % 1000 + "��]==============>");

			} else {
				showHintMessage(bo.getName()
						+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000339")/*
																								 * @res
																								 * "ȡ����"
																								 */);
			}
		} catch (BusinessException e) {
			showWarningMessage(e.getMessage() + bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134")/*
																							 * @res
																							 * "ʧ�ܣ�"
																							 */);
		} catch (Exception e) {
			showWarningMessage(e.getMessage() + " " + bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134")/*
																							 * @res
																							 * "ʧ�ܣ�"
																							 */);
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}

		nc.vo.scm.pub.SCMEnv.out("<====== ���۶���:[" + bo.getName() + "]���� ## ����ʱ["
				+ (System.currentTimeMillis() - s_init) / 1000 + "."
				+ (System.currentTimeMillis() - s_init) % 1000 + "��]==============>");

	}

	private void showMessageWhenAction(ButtonObject bo) {
		int hit = 0;
		String msg = "";
		if ("APPROVE".equals(bo.getTag())) {
			if (strShowState.equals("�б�"))
				msg = getBillListPanel().getHeadBillModel().getValueAt(
						getBillListPanel().getHeadTable().getSelectedRow(), "fstatus").toString();
			else {
				hit = Integer.parseInt(getHeadItemValue("fstatus").toString());
				msg = nc.vo.so.pub.ConstVO.billStatus[hit - 1];
			}
			showHintMessage(bo.getName() + "��������۶���״̬Ϊ--[" + msg + "]");
		} else
			showHintMessage(bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000132")/*
																							 * @res
																							 * "�ɹ���"
																							 */);
	}

	protected void onApprove(ButtonObject bo) {
		try {
			// �����ж�
			new BusiUtil().isInvoiceFirstNewFrom501(getCorpPrimaryKey(), getBillCardPanel()
					.getBusiType());
		} catch (BusinessException e) {
			showErrorMessage(e.getMessage());
			showHintMessage(bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134")/*
																							 * @res
																							 * "ʧ�ܣ�"
																							 */);
			return;
		}

		proccdlg = new ProccDlg(this, bo.getName());
		work = new WorkThread(bo.getTag(), this);

		proccdlg.showModal();
		updateUI();

	}

	public int onApprove(SaleOrderVO vo, String tag) throws Exception {

		if (vo == null)
			return 0;

		SaleOrderVO saleorder = vo;

		if (vo.getChildrenVO() != null) {
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				vo.getChildrenVO()[i].setStatus(VOStatus.UNCHANGED);
			}
		}
		saleorder.setIAction(ISaleOrderAction.A_AUDIT);

		boolean bContinue = true;

		while (bContinue) {

			try {

				onApproveCheck(saleorder);

				onApproveCheckWorkflow(saleorder);

				nc.vo.scm.pub.session.ClientLink clientLink = new nc.vo.scm.pub.session.ClientLink(
						getClientEnvironment());

				saleorder.setClientLink(clientLink);

				getBillCardTools().getPkarrivecorp(saleorder);

				saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
				saleorder.setDcurdate(getClientEnvironment().getDate());
				saleorder.setCusername(getClientEnvironment().getUser().getUserName());
				saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());
				saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000504")
				/* @res "ӆ�ι���" */);
				vo.getHeadVO().setAttributeValue("daudittime", ClientEnvironment.getServerTime());

				Object otemp = PfUtilClient.processActionFlow(this, tag, getBillType(),
						getClientEnvironment().getDate().toString(), saleorder, null);

				if (otemp != null) {
					String ErrMsg = otemp.toString();
					if (ErrMsg != null && ErrMsg.startsWith("ERR")) {
						ShowToolsInThread.showMessage(proccdlg, ErrMsg.substring(3));
						// showWarningMessage(ErrMsg.substring(3));
					}
				}

				if (PfUtilClient.isSuccess()) {
					return 1;
				} else {
					return 0;
				}

			} catch (Exception ex) {
				bContinue = doException(saleorder, null, ex);
			}

		}// end while

		return 0;

	}

	protected void onUnApprove(ButtonObject bo) {

		proccdlg = new ProccDlg(this, bo.getName());
		work = new WorkThread(bo.getTag(), this);

		proccdlg.showModal();
		updateUI();

	}

	public int onUnApprove(SaleOrderVO vo, String tag) throws Exception {

		if (vo == null)
			return 0;

		SaleOrderVO saleorder = vo;
		if (vo.getChildrenVO() != null) {
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				vo.getChildrenVO()[i].setStatus(VOStatus.UNCHANGED);
			}
		}

		saleorder.setIAction(ISaleOrderAction.A_UNAUDIT);

		try {

			onUnApproveCheck(saleorder);

			ClientLink clientLink = new ClientLink(getClientEnvironment());
			saleorder.setClientLink(clientLink);
			saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
			saleorder.setDcurdate(getClientEnvironment().getDate());
			saleorder.setCusername(getClientEnvironment().getUser().getUserName());
			saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());
			saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000504")
			/* @res "ӆ�ι���" */);

			PfUtilClient.processAction(tag, SaleBillType.SaleOrder, getClientEnvironment()
					.getDate().toString(), saleorder);

			if (PfUtilClient.isSuccess()) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw e;
		}

	}

	protected void onBlankout(ButtonObject bo) {

		proccdlg = new ProccDlg(this, bo.getName());
		work = new WorkThread(bo.getTag(), this);

		proccdlg.showModal();
		updateUI();

		showHintMessage(bo.getName()
				+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000132")/*
																						 * @res
																						 * "�ɹ���"
																						 */);

	}

	public int onBlankout(SaleOrderVO vo, String tag) throws Exception {

		if (vo == null)
			return 0;

		SaleOrderVO saleorder = vo;
		if (vo.getChildrenVO() != null) {
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				vo.getChildrenVO()[i].setStatus(VOStatus.UNCHANGED);
			}
		}

		// ���ö��� Ϊ��дԤ������׼��
		saleorder.setIAction(ISaleOrderAction.A_BLANKOUT);

		try {

			((SaleorderHVO) saleorder.getParentVO()).setVoldreceiptcode(((SaleorderHVO) saleorder
					.getParentVO()).getVreceiptcode());

			saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

			saleorder.processVOForTrans();

			PfUtilClient.processActionNoSendMessage(this, tag, SaleBillType.SaleOrder,
					getClientEnvironment().getDate().toString(), saleorder, null, null, null);
			// ɾ��ʱ����ǲ��ղɹ����������۶�������Ҫ��дԴ�ɹ�����
			if (saleorder.getHeadVO().isCoopped()) {

				String sourceId = (String) saleorder.getBodyVOs()[0].getCsourcebillid();
				reWritePO(new String[] { sourceId }, false);

			}

			if (PfUtilClient.isSuccess()) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * ����������ݺϷ��ԡ�
	 * 
	 * �������ڣ�(2001-8-8 10:09:01)
	 * 
	 * @return boolean
	 */
	private void onUnApproveCheck(SaleOrderVO saleorder) throws nc.vo.pub.ValidationException {
		if (saleorder.getHeadVO().getAttributeValue("bcooptopo") != null
				&& ((UFBoolean) saleorder.getHeadVO().getAttributeValue("bcooptopo"))
						.booleanValue()) {
			throw new ValidationException("�ö����Ѿ�Эͬ���ɲɹ������޷�����");
		}

		SaleorderBVO[] oldbodyVOs = saleorder.getBodyVOs();
		// �������
		for (int i = 0; i < oldbodyVOs.length; i++) {

			if (oldbodyVOs[i].getNtotalinventorynumber() != null
					&& oldbodyVOs[i].getNtotalinventorynumber().doubleValue() != 0)
				throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000294")/*
												 * @res "��ǰ�����ѳ��⣬��������!"
												 */);

			if (oldbodyVOs[i].getBifinventoryfinish() != null
					&& oldbodyVOs[i].getBifinventoryfinish().booleanValue())
				throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000418")/*
												 * @res "��ǰ���ݴ��ڳ���������У���������!"
												 */);

			if (oldbodyVOs[i].getBifinvoicefinish() != null
					&& oldbodyVOs[i].getBifinvoicefinish().booleanValue())
				throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000419")/*
												 * @res "��ǰ���ݴ��ڿ�Ʊ�������У���������!"
												 */);

			// if (oldbodyVOs[i].getBifpayfinish() != null
			// && oldbodyVOs[i].getBifpayfinish().booleanValue())
			// throw new ValidationException(NCLangRes.getInstance()
			// .getStrByID("40060301", "UPP40060301-000502")/*
			// * @res
			// * "��ǰ���ݴ����տ�������У���������!"
			// */);

			if (oldbodyVOs[i].getNarrangescornum() != null
					&& oldbodyVOs[i].getNarrangescornum().doubleValue() != 0)
				throw new ValidationException(NCLangRes.getInstance().getStrByID("sopub",
						"UPPsopub-000303")/*
											 * @res "�Ѿ�����(ȫ��)����ί�ⶩ��,����ȡ��"
											 */);

			if (oldbodyVOs[i].getNarrangemonum() != null
					&& oldbodyVOs[i].getNarrangemonum().doubleValue() != 0)
				throw new ValidationException(NCLangRes.getInstance().getStrByID("sopub",
						"UPPsopub-000304")/*
											 * @res "�Ѿ�����(ȫ��)������������,����ȡ��"
											 */);

			if (oldbodyVOs[i].getNarrangepoapplynum() != null
					&& oldbodyVOs[i].getNarrangepoapplynum().doubleValue() != 0)
				throw new ValidationException(NCLangRes.getInstance().getStrByID("sopub",
						"UPPsopub-000322")/*
											 * @res "�Ѿ�����(ȫ��)�����빺��,����ȡ��"
											 */);

			/*
			 * if (oldbodyVOs[i].getNarrangetoornum() != null &&
			 * oldbodyVOs[i].getNarrangetoornum().doubleValue() != 0) throw new
			 * ValidationException(NCLangRes.getInstance().getStrByID("sopub",
			 * "UPPsopub-000036") @res "�Ѿ�����(ȫ��)���ɵ������뵥���������,����ȡ��" );
			 */

			// /** �޶����Ķ������������� V502 yangchunlin jdm zhongwei* */
			// if (oldbodyVOs[i].getBeditflag() != null &&
			// oldbodyVOs[i].getBeditflag().booleanValue()) {
			// throw new
			// ValidationException(NCLangRes.getInstance().getStrByID("40060301",
			// "UPP40060301-000545")/*
			// * @res
			// * "���ݽ��й��޶�������������"
			// */);
			// }
		}// end for

		boCancelAudit.setEnabled(false);
		updateButton(boCancelAudit);
		nc.vo.scm.pub.SCMEnv.out("������ͨ��1");
	}

	/**
	 * �쳣������
	 * 
	 * @param vo
	 * @param oldvo
	 * @param e
	 *            ֻ������֪��ҵ���쳣��������׳��쳣
	 * @return �Ƿ�����ϣ������Ƿ���������
	 * @throws Exception
	 * 
	 * @comment �쳣��ǰ̨У��ʹ����������ҵ�����Ҫ��
	 */
	protected boolean doException(SaleOrderVO vo, SaleOrderVO oldvo, Exception e) throws Exception {
		e = nc.vo.so.pub.ExceptionUtils.marshException(e);

		// �����ͨ��,���쳣
		if (e instanceof ATPNotEnoughException && ((ATPNotEnoughException) e).getHint() == null) {
			throw e;
		}

		/** ƽ̨�쳣�������ǵ��ݶ�����麯���׳� v5.5 */
		if (e instanceof nc.vo.uap.pf.PFBusinessException) {
			// ������Ϣ���кŵ�����λ��
			String rowNo_s = "�к�Ϊ";
			int rowStringIndex = e.getMessage().indexOf(rowNo_s);

			// ������Ϣ�к�
			String rowNo = e.getMessage().substring(rowStringIndex + rowNo_s.length(),
					rowStringIndex + rowNo_s.length() + 2);

			ArrayList<Integer> rowList = new ArrayList<Integer>();
			if (!strShowState.equals("�б�")) {
				for (int row = 0; row < getBillCardPanel().getRowCount(); row++) {
					if (getBillCardTools().getBodyStringValue(row, "crowno").equals(rowNo))
						rowList.add(new Integer(row));
				}
				nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillCardPanel(), rowList);
			} else {
				for (int row = 0; row < getBillListPanel().getBodyTable().getRowCount(); row++) {
					if (getBillListPanel().getBodyBillModel().getValueAt(row, "crowno").equals(
							rowNo))
						rowList.add(new Integer(row));
				}
				nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillListPanel().getBodyTable(),
						rowList);
			}
		}
		/** ƽ̨�쳣�������ǵ��ݶ�����麯���׳� v5.5 */

		if (!(e instanceof ATPNotEnoughException || e instanceof CreditNotEnoughException
				|| e instanceof PeriodNotEnoughException || e instanceof nc.vo.scm.pub.SaveHintException))
			throw e;

		String sMsg = e.getMessage();

		sMsg += NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000230");/*
																						 * @res "
																						 * ,�Ƿ������"
																						 */
		if (showYesNoMessage(sMsg) != MessageDialog.ID_YES)
			return false;

		if (e instanceof ATPNotEnoughException) {
			vo.setBCheckATP(false);
			if (oldvo != null)
				oldvo.setBCheckATP(false);
		} else if (e instanceof CreditNotEnoughException) {
			vo.setCheckCredit(false);
			if (oldvo != null)
				oldvo.setCheckCredit(false);
		} else if (e instanceof PeriodNotEnoughException) {
			vo.setCheckPeriod(false);
			if (oldvo != null)
				oldvo.setCheckPeriod(false);
		} else if (e instanceof nc.vo.scm.pub.SaveHintException) {
			vo.setFirstTime(false);
			if (oldvo != null)
				oldvo.setFirstTime(false);
		}

		return true;
	}

	/**
	 * ����������
	 * 
	 * �������ڣ�(2001-6-1 13:12:36)
	 * 
	 */
	protected void onAfterAction(ButtonObject bo) {
		try {
			PfUtilClient.processAction(bo.getTag(), getBillType(), getClientEnvironment().getDate()
					.toString(), getVO(false));
			showHintMessage(bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000132")/*
																							 * @res
																							 * "�ɹ���"
																							 */);
		} catch (Exception e) {
			showWarningMessage(bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134")/*
																							 * @res
																							 * "ʧ�ܣ�"
																							 */);
			// e.printStackTrace();
		}
	}

	/**
	 * ����������ݺϷ��ԡ�
	 * 
	 * �������ڣ�(2001-8-8 10:09:01)
	 * 
	 * @return boolean
	 * 
	 */
	private void onApproveCheckWorkflow(SaleOrderVO saleorder) throws nc.vo.pub.ValidationException {
		try {
			boolean isExist = false;
			isExist = nc.ui.pub.pf.PfUtilBO_Client.isExistWorkFlow(getBillType(), saleorder
					.getBizTypeid(), getClientEnvironment().getCorporation().getPk_corp(),
					getClientEnvironment().getUser().getPrimaryKey());
			String pkOperator = ((SaleorderHVO) saleorder.getParentVO()).getCoperatorid().trim();
			if (isExist == true
					&& pkOperator.equals(getClientEnvironment().getUser().getPrimaryKey().trim())) {
				int iWorkflowstate = 0;
				iWorkflowstate = nc.ui.pub.pf.PfUtilClient.queryWorkFlowStatus(saleorder
						.getBizTypeid(), getBillType(), saleorder.getParentVO().getPrimaryKey());

				if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.BILL_NOT_IN_WORKFLOW) {
					throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID(
							"40060301", "UPP40060301-000188")/*
																 * @res
																 * "�������������˹����������õ���û���ڹ������С�"
																 */);
				}
			}
		} catch (Throwable e) {
			SCMEnv.out(e);
			// e.printStackTrace();
			throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000188")/*
											 * @res "�������������˹����������õ���û���ڹ������С�"
											 */);
		}
	}

	/**
	 * ����ִ�С�
	 * 
	 * �������ڣ�(2001-6-1 13:12:36)
	 * 
	 */
	protected void onAssistant(ButtonObject bo) {

		if (strShowState.equals("�б�")) {
			num = getBillListPanel().getHeadTable().getSelectedRow();

			if (num == -1)
				return;
		}

		// ������������ֱ�˰���
		if (bo.getTag().equals(SENDINV)) {
			onBoSendInv();
		} else if (bo.getTag().equals(SUPPLYINV)) {
			onBoSupplyDirectInv(bo);
		} else if (bo.getTag().equals(DIRECTINV)) {
			onBoSupplyDirectInv(bo);
		} else {
			try {
				PfUtilClient.processActionNoSendMessage(this, bo.getTag(), getBillType(),
						getClientEnvironment().getDate().toString(), getVO(false),
						getAssistantPara(bo), null, null);
			} catch (Exception e) {
				showErrorMessage(e.getMessage());
			}
		}

		// ��������ִ�к�ˢ�½���
		BillTools.reLoadBillState(this, getClientEnvironment());
		// ˢ�»���TS
		refreshVOcacheTS();
	}

	private void refreshVOcacheTS() {
		SaleOrderVO svo = vocache.getSaleOrderVO(getHeadItemValue("csaleid").toString());
		if (svo != null) {
			svo.getHeadVO().setTs(SmartVODataUtils.getUFDateTime(getHeadItemValue("ts")));
			for (int i = 0; i < svo.getBodyVOs().length; i++) {
				SaleorderBVO bvo = svo.getBodyVOs()[i];
				bvo.setTs(SmartVODataUtils.getUFDateTime(getBillCardTools().getBodyValue(i, "ts")));
			}
		}
	}

	/**
	 * ��������
	 */
	private void onBoSendInv() {
		try {
			// 1 �����رա����ֶ����������ۿ��У�������������
			ArrayList<SaleorderBVO> list = new ArrayList<SaleorderBVO>();
			SaleOrderVO svo = (SaleOrderVO) getVo();
			SaleorderBVO[] bvos = svo.getBodyVOs();

			boolean bsendendflag = false;
			boolean bretractflag = false;
			boolean bLaborDiscountflag = false;
			for (int i = 0; i < bvos.length; i++) {
				bLaborDiscountflag = bvos[i].getLaborflag().booleanValue() 
				                            || bvos[i].getDiscountflag().booleanValue();
				bsendendflag = bvos[i].getBifreceivefinish().booleanValue();
				bretractflag = bLaborDiscountflag 
				                 || bvos[i].getNnumber().compareTo(new UFDouble(0)) < 0 ? true : false;
				if (!bsendendflag
						&& !bretractflag
						&& (bvos[i].getBdericttrans() == null || !bvos[i].getBdericttrans()
								.booleanValue())
						&& !bLaborDiscountflag) {
					list.add(bvos[i]);
				}
			}
			if (list.size() == 0) {
				throw new BusinessException("�����رա����ֶ����������ۿ��У����ܽ��з������ţ�");
			}

			SaleorderBVO[] newItems = new SaleorderBVO[list.size()];
			newItems = (SaleorderBVO[]) list.toArray(newItems);

			SaleOrderVO newVO = new SaleOrderVO();
			newVO.setParentVO(svo.getParentVO());
			newVO.setChildrenVO(newItems);

			// VO����
			nc.vo.so.soreceive.SaleReceiveVO sendVO;

			sendVO = (nc.vo.so.soreceive.SaleReceiveVO) PfChangeBO_Client.pfChangeBillToBill(newVO,
					"30", "4331");

			PfLinkData linkData = new PfLinkData();
			linkData.setUserObject(sendVO);

			SFClientUtil.openLinkedADDDialog("40060401", this, linkData);

		} catch (BusinessException e) {
			showWarningMessage(e.getMessage());
		}
	}

	/**
	 * ����ֱ�˰���
	 */
	private void onBoSupplyDirectInv(ButtonObject bo) {
		try {
			// 1.����ֱ�˹رգ�����������ֱ��
			ArrayList<SaleorderBVO> list = new ArrayList<SaleorderBVO>();
			SaleOrderVO svo = (SaleOrderVO) getVo();
			SaleorderBVO[] bvos = svo.getBodyVOs();
			for (int i = 0; i < bvos.length; i++) {
				boolean bsendendflag = bvos[i].getBarrangedflag().booleanValue();
				if (!bsendendflag 
						&& (!bvos[i].getLaborflag().booleanValue() && !bvos[i].getDiscountflag().booleanValue()) 
						&& bvos[i].getNnumber().doubleValue() > 0) {
					list.add(bvos[i]);
				}
			}
			if (list.size() == 0) {
				throw new BusinessException("���ֶ������߱������Ѳ���ֱ�˰��Źرգ����ܽ��в���ֱ�˰��ţ�");
			}

			SaleorderBVO[] newItems = new SaleorderBVO[list.size()];
			newItems = (SaleorderBVO[]) list.toArray(newItems);

			SaleOrderVO newVO = new SaleOrderVO();
			newVO.setParentVO(svo.getParentVO());
			newVO.setChildrenVO(newItems);

			// ���˲���ֱ�˱���VO
			SaleOrderVO realVo = null;
			ISourceRedunVO sourceRedunVO = null;
			if (bo.getTag().equals(SUPPLYINV)) {
				sourceRedunVO = new ISourceRedunVO(null, ISourceRedunVO.INDIRECT);
				realVo = ((SaleOrderVO) newVO).getBillRedunVO(sourceRedunVO);
				;
			} else if (bo.getTag().equals(DIRECTINV)) {
				sourceRedunVO = new ISourceRedunVO(null, ISourceRedunVO.DIRECT);
				realVo = ((SaleOrderVO) newVO).getBillRedunVO(sourceRedunVO);
				;
			}

			// 2.��ʼ������ֱ�˽���
			if (getCurShowPanel() == null)
				remove(getBillListPanel());
			else
				remove(getCurShowPanel());
			add(getBillToBillUI(realVo), "Center");
			setTitleText(getBillToBillUI().getTitle());
			getBillToBillUI().setVisible(true);

			ButtonObject[] btns = getBillToBillUI().setButtenForUse("30");
			setButtons(btns, "40060402");
			updateButtons();
			updateUI();

		} catch (BusinessException e) {
			showWarningMessage(e.getMessage());
		}
	}

	/**
	 * �ɲ���ֱ��UI�������۶���UI
	 */
	public void returnToMainUI() {

		// ���³�ʼ�����۶���UI
		remove(getBillToBillUI());
		if (getCurShowPanel() == null)
			add(getBillListPanel(), "Center");
		else
			add(getCurShowPanel(), "Center");
		setTitleText(getTitle());
		setButtons();
		setButtonsState();

		// ˢ�½���ͻ���
		BillTools.reLoadBillState(this, getClientEnvironment());
		updateCacheVO();
		if (!strShowState.equals("�б�"))
			getBillCardPanel().updateValue();

		updateUI();
	}

	/**
	 * ÿ�β���һ���µ�ʵ��
	 * 
	 * @param newVO
	 * @return
	 */
	public BillToBillUI getBillToBillUI(SaleOrderVO newVO) {
		// if (billToBillUI == null)
		billToBillUI = new BillToBillUI(this, "30", new SaleOrderVO[] { newVO });
		return billToBillUI;
	}

	public BillToBillUI getBillToBillUI() {
		return billToBillUI;
	}

	/**
	 * ���۶����������ӵ��ݸ��ƹ��ܡ�(���Ƶ�����ͬ����������)
	 * 
	 * ��������:���ݸ��ƹ��ܲ˵��¼�����
	 * 
	 * �������:��
	 * 
	 * ����ֵ:��
	 * 
	 * �쳣����:��
	 * 
	 * ����:2003-08-25
	 * 
	 */
	protected void onCopyBill() throws Exception {

		// �ڵ�ǰҵ�����͵ĵ�����Դ���������Ƶ���ʱ�����ܽ��и���
		// ���Ƶĵ��ݾ�Ϊ���Ƶ��ݡ��ڴ˽����ж�
		PfUtilClient.retAddBtn(boAdd, getCorpPrimaryKey(), getBillType(), boBusiType);
		int ccount = boAdd.getChildCount();
		ButtonObject[] bos = boAdd.getChildButtonGroup();
		boolean bCanCopyFlag = false;
		for (int i = 0; i < ccount; i++) {
			if (bos[i].getTag().startsWith(SELFBILL)) {
				bCanCopyFlag = true;
				break;
			}
		}

		if (!bCanCopyFlag) {
			showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000289")/*
																									 * @res
																									 * "��ǰ��ҵ�����������Ƶ��ݣ����ܸ��Ƶ���"
																									 */);
			return;
		}
		if (strShowState.equals("�б�")) {
			selectRow = getBillListPanel().getHeadTable().getSelectedRow();
			num = selectRow;
			getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(selectRow,
					selectRow);
			onCard();
		}
		if (getBillCardPanel().getHeadItem("bretinvflag") != null
				&& getBillCardPanel().getHeadItem("bretinvflag").getValueObject() != null
				&& (new UFBoolean(getBillCardPanel().getHeadItem("bretinvflag").getValue()))
						.booleanValue()) {
			showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000290")/*
																									 * @res
																									 * "���ܸ����˻���������"
																									 */);
			return;

		}
		// head null items
		String[] headitems = new String[] { "bretinvflag", "binvoicendflag", "boutendflag",
				"breceiptendflag", "bpayendflag", "btransendflag", "boverdate", "capproveid",
				"dapprovedate", "vreceiptcode", "csaleid", "veditreason", "nreceiptcathmny",
				"boverdate", "iprintcount", "dbilltime", "daudittime", "dmoditime", "editionnum",
				"editdate", "editauthor" };
		for (int i = 0; i < headitems.length; i++) {
			if (getBillCardPanel().getHeadItem(headitems[i]) != null)
				getBillCardPanel().getHeadItem(headitems[i]).setValue(null);
		}
		String[] tails = new String[] { "capproveid", "dapprovedate", "iprintcount", "dbilltime",
				"daudittime", "dmoditime" };
		for (int i = 0; i < tails.length; i++) {
			if (getBillCardPanel().getTailItem(tails[i]) != null)
				getBillCardPanel().getTailItem(tails[i]).setValue(null);
		}

		if (getBillCardPanel().getHeadItem("bcodechanged") != null)
			getBillCardPanel().getHeadItem("bcodechanged").setValue(new UFBoolean(false));
		if (getBillCardPanel().getHeadItem("fstatus") != null)
			getBillCardPanel().getHeadItem("fstatus").setValue(new Integer(BillStatus.FREE));
		if (getBillCardPanel().getHeadItem("dbilldate") != null)
			getBillCardPanel().getHeadItem("dbilldate").setValue(getClientEnvironment().getDate());

		if (getBillCardPanel().getHeadItem("pk_corp") != null)
			getBillCardPanel().getHeadItem("pk_corp").setValue(
					getClientEnvironment().getCorporation().getPk_corp());

		// n30add
		String[] keys = getNeedSetNullItemsWhenCopy();

		String[] keys1 = getBillCardPanel().getNeedSetNullSourceItems();

		getBillCardTools().setHeadRefLimit(strState);

		// ���������ݵķ�ʽ���븴�Ƶ���
		getBillCardPanel().setEnabled(true);

		// �����ſɱ༭
		vRowATPStatus = new Vector();
		if (alInvs == null) {
			initInvList();
		}
		UFBoolean wholemanaflag = null, isDiscount = null, isLabor = null;
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			for (int j = 0; j < keys.length; j++) {
				getBillCardPanel().setBodyValueAt(null, i, keys[j]);
			}
			for (int j = 0; j < keys1.length; j++) {
				getBillCardPanel().setBodyValueAt(null, i, keys1[j]);
			}
			getBillCardPanel().setBodyValueAt(null, i, "csaleid");

			vRowATPStatus.addElement(new UFBoolean(false));
			getBillCardPanel().getBillModel().setRowState(i, BillModel.ADD);

			// ���ƶ๫˾�еı༭״̬
			ctlUIOnCconsignCorpChg(i);

			// ����
			getBillCardPanel().setAssistUnit(i);
			getBillCardPanel().setScaleEditableByRow(i);

			// ����
			wholemanaflag = getBillCardTools().getBodyUFBooleanValue(i, "wholemanaflag");
			getBillCardPanel().setCellEditable(i, "fbatchstatus",
					wholemanaflag == null ? false : wholemanaflag.booleanValue());
			getBillCardPanel().setCellEditable(i, "cbatchid",
					wholemanaflag == null ? false : wholemanaflag.booleanValue());

			isDiscount = getBillCardTools().getBodyUFBooleanValue(i, "discountflag");
			isLabor = getBillCardTools().getBodyUFBooleanValue(i, "laborflag");

			if ((isDiscount != null && isDiscount.booleanValue())
					|| (isLabor != null && isLabor.booleanValue())) {

				getBillCardTools().setBodyCellsEdit(
						new String[] { "cconsigncorp", "creccalbody", "crecwarehouse",
								"bdericttrans", "boosflag", "bsupplyflag" }, i, false);
			}
		}
		setDefaultData(false);

		// ������ű���գ���ָ���ԭֵ
		if (getBillCardPanel().getHeadItem("cdeptid").getValueObject() == null) {
			getBillCardPanel().getHeadItem("cdeptid").resumeValue();
		}

		// �����ջ���ַ�ָ�ԭֵ
		// ��������Ѿ�������������ܻ�������
		SaleOrderVO saleorder = vocache.getSaleOrderVO(getBillCardPanel().getHeadItem("csaleid")
				.getValueCache().toString());
		SaleorderBVO[] items = saleorder.getBodyVOs();
		for (int i = 0, len = getBillCardPanel().getRowCount(); i < len; i++) {
			getBillCardPanel().setBodyValueAt(items[i].getVreceiveaddress(), i, "vreceiveaddress");
		}

		// �ָ�ģ����ĳ�ʼ�༭״̬
		getBillCardTools().resumeBillItemEditToInit();

		// ctlCurrencyEdit();
		// ������ֻ����б仯���򴥷������¼������¼���
		Object oldvalue = getBillCardPanel().getHeadItem("nexchangeotobrate").getValueCache();
		Object newvalue = getBillCardPanel().getHeadItem("nexchangeotobrate").getValueObject();
		if (((oldvalue != null) && !oldvalue.equals(newvalue))
				|| (oldvalue == null && newvalue != null)) {
			getBillCardPanel().afterChangeotobrateEdit(null);
			getBillCardPanel().freshBodyLargess(2);
		}

		if (((oldvalue != null) && !oldvalue.equals(newvalue))
				|| (oldvalue == null && newvalue != null)) {
			getBillCardPanel().freshBodyLargess(2);
		}
		// ������ֻ����б仯���򴥷������¼������¼���

		/** v5.3ȥ�������ֶ� ** */
		/*
		 * if (getBillCardPanel().getHeadItem("naccountperiod").getValueObject() !=
		 * null && (new UFDouble((String) getBillCardPanel().getHeadItem(
		 * "naccountperiod").getValueObject())).doubleValue() < 0.0)
		 * getBillCardPanel().setHeadItem("naccountperiod", null);
		 */

		String formulas[] = {
		// �ͻ���������
				"ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)",
				// ɢ����־
				"bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,ccustbasid)" };

		getBillCardPanel().execHeadFormulas(formulas);

		// ���¼����ͷ��˰�ϼƣ������˰�ϼ��п��ܱ����¼��㣬��Ҫͬ�����£�
		getBillCardPanel().getHeadItem("nheadsummny").setValue(
				getBillCardPanel().getTotalValue("noriginalcursummny"));

		showCustManArInfo();

		// ���������ɫ����
		InvVO[] invvos = new InvVO[getBillCardPanel().getRowCount()];
		for (int i = 0; i < invvos.length; i++) {
			invvos[i] = new InvVO();
			invvos[i]
					.setCinventoryid((String) getBillCardPanel().getBodyValueAt(i, "cinventoryid"));
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

		strState = "����"; /*-=notranslate=-*/
		setButtonsState();

		if (saleorder.isCoopped()) {
			// Эͬʱ����������
			setCoopDefaultDataForCopy();

		}

		// ���۶������ƺ����ø��ֶοɱ༭��
		getBillCardPanel().setEditEnabledForCopy();

		// getBillCardTools().setBodyItemsEdit(new String[]{"scalefactor"},
		// true);
	}

	/**
	 * ����ʱЭͬ����ֶ����
	 * 
	 * @throws Exception
	 */
	private void setCoopDefaultDataForCopy() throws Exception {
		getBillCardPanel().setHeadItem("bcooptopo", new UFBoolean(false));
		getBillCardPanel().setHeadItem("bpocooptome", new UFBoolean(false));
		getBillCardPanel().setHeadItem("ccooppohid", null);

	}

	protected void onAuditFlowStatus() {
		SaleOrderVO hvo = null;
		hvo = (SaleOrderVO) getVO(false);
		if (hvo == null || hvo.getParentVO() == null) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000199")/*
																									 * @res
																									 * "��ѡ�񵥾�"
																									 */);
		} else {
			SaleorderHVO header = (SaleorderHVO) hvo.getParentVO();
			String pk = header.getCsaleid();
			String biztype = header.getCbiztype();

			if (pk == null) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000067")/*
												 * @res "���ݺ�Ϊ��"
												 */);
			} else {
				nc.ui.pub.workflownote.FlowStateDlg approvestatedlg = new nc.ui.pub.workflownote.FlowStateDlg(
						this, "30", biztype, pk);
				approvestatedlg.showModal();
			}
		}
	}

	/**
	 * �˻�����
	 * 
	 */
	protected void onBatch() {
		long s = System.currentTimeMillis();
		SaleOrderVO saleorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
				SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
				SaleorderBVO.class.getName());
		try {
			onCheck(saleorder);
			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000278")/*
																								 * @res
																								 * "��ʼ�˻�...."
																								 */);

			// ���ñ�ͷ����״̬
			saleorder.setStatus(nc.vo.pub.VOStatus.NEW);
			saleorder.getParentVO().setStatus(nc.vo.pub.VOStatus.NEW);

			// ���ø���VO����
			saleorder.setIAction(ISaleOrderAction.A_ADD);

			saleorder.setDcurdate(getClientEnvironment().getDate());
			saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

			// �ջ���ַ
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();

			saleorder.getHeadVO().setVreceiveaddress(vreceiveaddress.getUITextField().getText());

			for (int i = 0; i < saleorder.getChildrenVO().length; i++) {
				saleorder.getChildrenVO()[i].setStatus(nc.vo.pub.VOStatus.NEW);
			}

			// ����Ԥ�տ������Ԥ�տ���
			if (calculatePreceive(saleorder) == false)
				return;

			java.util.ArrayList alistID = null;

			boolean bContinue = true;
			boolean bRight = true;
			while (bContinue) {
				try {
					saleorder.processVOForTrans();

					UFDateTime ud = ClientEnvironment.getServerTime();
					saleorder.getHeadVO().setAttributeValue("dbilltime", ud);

					alistID = (java.util.ArrayList) PfUtilClient.processActionNoSendMessage(this,
							"PreKeep", getBillType(), getClientEnvironment().getDate().toString(),
							saleorder, null, null, null);

					bContinue = false;
					bRight = true;
				} catch (Exception ex) {
					bContinue = doException(saleorder, null, ex);
					bRight = false;
				}
			}

			if (!bRight) {
				showHintMessage(NCLangRes.getInstance()
						.getStrByID("40060301", "UPP40060301-000185")/*
																		 * @res
																		 * "����ʧ�ܣ�"
																		 */);
				return;
			}

			vIDs.add((String) alistID.get(0));
			this.id = (String) alistID.get(0);
			num = vIDs.size() - 1;
			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000279",
					null, new String[] { (System.currentTimeMillis() - s) / 1000 + "" }));

			// ���¼�������
			loadCardData();

			strState = "����";

			getBillCardTools().setHeadRefLimit(strState);

			setCardButtonsState();

			getBillCardPanel().showCustManArInfo();

			// ���������ɫ����
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), null);

			// �ָ���������ɫ
			nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel());

		} catch (ValidationException e) {
			nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillCardPanel(), rowList);
			showErrorMessage(e.getMessage()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000280")/*
																							 * @res
																							 * "�˻�ʧ�ܣ�"
																							 */);
		} catch (Exception e) {
			showWarningMessage(e.getMessage());
			showHintMessage(e.getMessage()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000280")/*
																							 * @res
																							 * "�˻�ʧ�ܣ�"
																							 */);
			// nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}
	}

	/**
	 * ���ÿ�Ƭ��ť״̬�� �������ڣ�(2001-3-17 9:00:09)
	 */
	private void setCardButtonsState() {
		boFirst.setEnabled(true);
		boLast.setEnabled(true);
		getBoOnHandShowHidden().setEnabled(true);

		// yb modify
		// �ڵ�ǰҵ�����͵ĵ�����Դ���������Ƶ���ʱ�����ܽ��и���
		// ���Ƶĵ��ݾ�Ϊ���Ƶ��ݡ��ڴ˽����ж�
		PfUtilClient.retAddBtn(boAdd, getCorpPrimaryKey(), getBillType(), boBusiType);

		String csaleid = (String) getBillCardPanel().getHeadItem("csaleid").getValueObject();

		// û�е��ݼ��ص����״̬
		if ((csaleid == null || csaleid.length() == 0) && (strState.equals("����"))) {
			boBusiType.setEnabled(true);
			boAdd.setEnabled(true);
			boSave.setEnabled(false);
			boMaintain.setEnabled(false);
			boLine.setEnabled(false);
			boAudit.setEnabled(false);
			boAction.setEnabled(false);
			boQuery.setEnabled(true);
			boBrowse.setEnabled(false);
			boReturn.setEnabled(true);
			boPrntMgr.setEnabled(false);
			boAssistant.setEnabled(true);
			boAsstntQry.setEnabled(false);
		} else if (strState.equals("����")) { /*-=notranslate=-*/
			boBusiType.setEnabled(true);
			int ccount = boAdd.getChildCount();
			ButtonObject[] bos = boAdd.getChildButtonGroup();
			boolean bCanCopyFlag = false;
			for (int i = 0; i < ccount; i++) {
				if (bos[i].getTag().startsWith(SELFBILL)) {
					bCanCopyFlag = true;
					break;
				}
			}
			boAdd.setEnabled(true);
			boBatch.setEnabled(false);
			boEdit.setEnabled(true);
			boCancel.setEnabled(false);
			boSave.setEnabled(false);
			boMaintain.setEnabled(true);
			boBlankOut.setEnabled(true);
			setLineButtonStatus(false);
			boAudit.setEnabled(false);
			boClose.setEnabled(true);
			boFreeze.setEnabled(false);
			boDocument.setEnabled(true);
			boOrderQuery.setEnabled(true);
			boReturn.setEnabled(true);
			boAction.setEnabled(true);
			boPrntMgr.setEnabled(true);
			boAssistant.setEnabled(true);
			boAsstntQry.setEnabled(true);
			boQuery.setEnabled(true);
			boBrowse.setEnabled(true);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(true);
			boBom.setEnabled(false);
			boPrint.setEnabled(true);
			boPreview.setEnabled(true);
			boSplitPrint.setEnabled(true);
			boPre.setEnabled(true);
			boNext.setEnabled(true);

			boSendAudit.setEnabled(true);
			boAuditFlowStatus.setEnabled(true);

			if (num == 0)
				boPre.setEnabled(false);
			if (num == vIDs.size() - 1)
				boNext.setEnabled(false);

			boCopyBill.setEnabled(bCanCopyFlag);

			getBillCardPanel().setEnabled(false);

			// ����״̬
			int iStatus = (Integer) getBillCardPanel().getHeadItem("fstatus").getValueObject();
			// ���ݵ���״̬���õ���
			setBtnsByBillState(iStatus);

		} else if (strState.equals("����")) { /*-=notranslate=-*/
			boBusiType.setEnabled(false);
			boAdd.setEnabled(false);
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boFinish.setEnabled(false);
			boQuery.setEnabled(false);
			boPrntMgr.setEnabled(false);
			boAssistant.setEnabled(true);
			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boAsstntQry.setEnabled(true);
			boAction.setEnabled(true);
			boBrowse.setEnabled(false);
			boPrint.setEnabled(false);
			boPreview.setEnabled(false);
			boSplitPrint.setEnabled(false);
			boEdit.setEnabled(false);
			boCancel.setEnabled(true);
			boSave.setEnabled(true);
			boMaintain.setEnabled(true);
			boBlankOut.setEnabled(false);
			setLineButtonStatus(true);
			boFirst.setEnabled(false);
			boLast.setEnabled(false);
			boPre.setEnabled(false);
			boNext.setEnabled(false);
			boDocument.setEnabled(false);
			boOrderQuery.setEnabled(false);
			boReturn.setEnabled(false);
			boAfterAction.setEnabled(false);
			boRefundment.setEnabled(false);
			// �༭״̬��"��������"���� dongwei zhongwei
			boBom.setEnabled(true);

			// yb add
			boCopyBill.setEnabled(false);

			boSendAudit.setEnabled(true);
			boAuditFlowStatus.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			setLineButtonsState();
		} else if (strState.equals("�޸�")) { /*-=notranslate=-*/
			boBusiType.setEnabled(false);
			boAdd.setEnabled(false);
			boAudit.setEnabled(false);
			boQuery.setEnabled(false);
			boPrntMgr.setEnabled(false);
			boAssistant.setEnabled(true);
			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boAsstntQry.setEnabled(true);
			boAction.setEnabled(true);
			boBrowse.setEnabled(false);
			boPrint.setEnabled(false);
			boPreview.setEnabled(false);
			boSplitPrint.setEnabled(false);
			boEdit.setEnabled(false);
			boCancel.setEnabled(true);
			boSave.setEnabled(true);
			boBlankOut.setEnabled(false);
			setLineButtonStatus(true);
			boFirst.setEnabled(false);
			boLast.setEnabled(false);
			boPre.setEnabled(false);
			boNext.setEnabled(false);
			boClose.setEnabled(false);
			boDocument.setEnabled(false);
			boOrderQuery.setEnabled(false);
			boReturn.setEnabled(false);
			boAfterAction.setEnabled(false);
			boRefundment.setEnabled(false);

			// �༭״̬��"��������"���� dongwei zhongwei
			boBom.setEnabled(true);
			int selrow = getBillCardPanel().getBillTable().getSelectedRow();
			if (selrow >= 0) {
				UFBoolean isInvBom = getBillCardTools().getBodyUFBooleanValue(selrow,
						"isconfigable");
				if (isInvBom != null && isInvBom.booleanValue())
					boBom.setEnabled(true);
				else
					boBom.setEnabled(false);
			}

			// yb add
			boCopyBill.setEnabled(false);

			// �޸�״̬�����󲻿��ã�ln zc v5.5
			boSendAudit.setEnabled(false);

			boAuditFlowStatus.setEnabled(true);

			setLineButtonsState();
		} else if (strState.equals("�޶�")) { /*-=notranslate=-*/
			boBusiType.setEnabled(false);
			boAdd.setEnabled(false);
			boAudit.setEnabled(false);
			boQuery.setEnabled(false);
			boPrntMgr.setEnabled(false);
			boAssistant.setEnabled(true);
			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boAsstntQry.setEnabled(true);
			boAction.setEnabled(false);
			boBrowse.setEnabled(false);
			boPrint.setEnabled(false);
			boPreview.setEnabled(false);
			boSplitPrint.setEnabled(false);
			boEdit.setEnabled(false);
			boCancel.setEnabled(true);
			boSave.setEnabled(true);
			boBlankOut.setEnabled(false);
			setLineButtonStatus(true);
			boFirst.setEnabled(false);
			boLast.setEnabled(false);
			boPre.setEnabled(false);
			boNext.setEnabled(false);
			boClose.setEnabled(false);
			boDocument.setEnabled(false);
			boOrderQuery.setEnabled(false);
			boReturn.setEnabled(false);
			boAfterAction.setEnabled(false);
			boRefundment.setEnabled(false);
			boBom.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boModification.setEnabled(false);

		}
		if (strState.equals("�˻�")) { /*-=notranslate=-*/
			boBatch.setEnabled(true);
			boRefundmentDocument.setEnabled(true);
			boOrderQuery.setEnabled(true);
			boReturn.setEnabled(true);
			boRefundment.setEnabled(false);
			boBom.setEnabled(false);
			setLineButtonStatus(true);

			// yb add
			boCopyBill.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(false);
		}

		boRefundment.setEnabled(false);

		getBillCardPanel().setBodyMenuShow(boLine.isEnabled());
		if (strState.equals("�˻�")) { /*-=notranslate=-*/
			getBillCardPanel().getAddLineMenuItem().setEnabled(false);
			getBillCardPanel().getDelLineMenuItem().setEnabled(boDelLine.isEnabled());
			getBillCardPanel().getPasteLineMenuItem().setEnabled(false);
			getBillCardPanel().getCopyLineMenuItem().setEnabled(false);
			getBillCardPanel().getInsertLineMenuItem().setEnabled(false);
		}

		int iStatus = -1;
		if (getBillCardPanel().getHeadItem("fstatus") != null
				&& getBillCardPanel().getHeadItem("fstatus").getValueObject() != null)
			iStatus = (Integer) getBillCardPanel().getHeadItem("fstatus").getValueObject();
		setBtnsByBillState(iStatus);

		if (!strState.equals("�޶�")) { /*-=notranslate=-*/
			getBillCardPanel().hideBodyTableCol("veditreason");
			getBillCardPanel().getBodyItem("veditreason").setEnabled(false);
		}

		boRefresh.setEnabled(b_query);

		/** �ڿ�Ƭ�²������õİ�ť* */
		boFind.setEnabled(false);
		boListSelectAll.setEnabled(false);
		boListDeselectAll.setEnabled(false);
		/** �ڿ�Ƭ�²������õİ�ť* */

		setBodyMenuStatus();

		updateButtons();
	}

	/**
	 * ��������
	 */
	protected void onBom() {
		int row;

		if (strShowState.equals("�б�")) {
			row = getBillListPanel().getBodyTable().getSelectedRow();

			onCard();
		} else {
			row = getBillCardPanel().getBillTable().getSelectedRow();
		}

		if (row < 0) {
			showWarningMessage("����ѡ������У�");
			return;
		}

		orderrow = row;
		Object nnumber = getBillCardPanel().getBodyValueAt(row, "nnumber");
		if (nnumber != null && nnumber.toString().length() != 0) {
			remove(getSplitPanelBc());
			add(getBillTreeCardPanel(), "Center");
			String saleID = (String) getBillCardPanel().getHeadItem("csaleid").getValueObject();
			String custID = (String) getBillCardPanel().getHeadItem("ccustomerid").getValueObject();
			String currID = (String) getBillCardPanel().getHeadItem("ccurrencytypeid")
					.getValueObject();
			//
			String invID = getBillCardPanel().getBodyValueAt(row, "cinventoryid").toString();
			//
			String invbaseID = getBillCardPanel().getBodyValueAt(row, "cinvbasdocid").toString();
			//
			String invname = getBillCardPanel().getBodyValueAt(row, "cinventoryname").toString();
			// isspecialty
			Object tempo = getBillCardPanel().getBodyValueAt(row, "isspecialty");
			String isspecialty = tempo == null ? "N"
					: (tempo.toString().equals("true") ? "Y" : "N");
			// �۸�
			Object price = null;
			if (SA_02.booleanValue()) {
				price = getBillCardPanel().getBodyValueAt(row, "noriginalcurtaxprice");
			} else {
				price = getBillCardPanel().getBodyValueAt(row, "noriginalcurprice");
			}
			String nprice = (price == null ? null : price.toString());
			// �������辫��
			setPanelBomByCurrency(currID);
			// csaleid
			if (saleID != null && saleID.length() != 0) {
				getBillTreeCardPanel().setHeadItem("csaleid", saleID);
			}
			// ���Ƿ���������
			Object bomCurrID = getBillCardPanel().getBodyValueAt(row, "cbomorderid");
			if (bomCurrID == null && (!strState.equals("����") && !strState.equals("�޸�"))) {
				strBomState = "FREE";
				getBillTreeCardPanel().setEnabled(false);
			} else if (bomCurrID == null) {
				getBillTreeCardPanel().addNew();
				// �ͻ�
				getBillTreeCardPanel().setHeadItem("ccustomerid", custID);
				// ����
				getBillTreeCardPanel().setHeadItem("ccurrencytypeid", currID);
				// ���
				getBillTreeCardPanel().setHeadItem("cinventoryid", invID);
				// ��������
				getBillTreeCardPanel().setHeadItem("nrequirenumber", nnumber.toString());
				// ���ۼ۸�
				getBillTreeCardPanel().setHeadItem("nsaleprice", nprice);
				// �Ƶ�����
				getBillTreeCardPanel().setTailItem("dmakedate",
						getClientEnvironment().getDate().toString());
				// �Ƶ���
				getBillTreeCardPanel().setTailItem("coperatorid",
						getClientEnvironment().getUser().getPrimaryKey());
				strBomState = "ADD";
				getBillTreeCardPanel().setEnabled(true);
			} else {
				strBomState = "FREE";
				loadBomData(row);
				getBillTreeCardPanel().setHeadItem("ccurrencytypeid", currID);
			}
			initBomTree(invbaseID, invname, invID, isspecialty);
			getBillTreeCardPanel().getBillModel().execLoadFormula();
			getBillTreeCardPanel().getBodyItem("nprice").setEdit(SA_15.booleanValue());
			getBillTreeCardPanel().getCustTree().setEnabled(true);
			getBillTreeCardPanel().getCustTree().setEditable(false);
			isSaveStart = true;
			strOldState = strState;
			strState = "BOM";
			// setButtons(getBillButtons());
			initButtons();
			setButtonsState();
			setTitleText(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000190")/*
																								 * @res
																								 * "��Ʒ���õ�"
																								 */);
			// �û��޸ļ�
			boBomEdit.setEnabled(false);
			updateButtons();
			updateUI();
		} else {
			showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000191")/*
																									 * @res
																									 * "����������������"
																									 */);
		}
	}

	/**
	 * ����ִ�С�
	 * 
	 * �������ڣ�(2001-6-1 13:12:36)
	 * 
	 */
	protected void onBomAction(ButtonObject bo) {
		try {
			PfUtilClient.processAction(bo.getTag(), SaleBillType.BomOrder, getClientEnvironment()
					.getDate().toString(), getBomVO());
			if (PfUtilClient.isSuccess()) {
				loadBomCurrData();
				// setButtonsState();
				showHintMessage(bo.getName()
						+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000132")/*
																								 * @res
																								 * "�ɹ���"
																								 */);
			} else {
				showHintMessage(bo.getName()
						+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000339")/*
																								 * @res
																								 * "ȡ����"
																								 */);
			}
		} catch (BusinessException e) {
			showErrorMessage(e.getMessage());
			showHintMessage(bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134")/*
																							 * @res
																							 * "ʧ�ܣ�"
																							 */);
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			showHintMessage(bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134")/*
																							 * @res
																							 * "ʧ�ܣ�"
																							 */);
			// e.printStackTrace();
		}
	}

	protected void onBomCancel() {
		if (strBomState.equals("ADD")) {
			getBillTreeCardPanel().getCustTree().setEnabled(true);
			// getBillTreeCardPanel().getBillModel().clearBodyData();
			strBomState = "CANCEL";
		}

		if (strBomState.equals("EDIT")) {
			getBillTreeCardPanel().resumeValue();
			getBillTreeCardPanel().getCustTree().setEnabled(true);
			// getBillTreeCardPanel().getBillModel().clearBodyData();
			strBomState = "FREE";
		}

		getBillTreeCardPanel().getCustTree().setEditable(false);
		setBomButtonsState();
	}

	protected void onBomEdit() {
		strBomState = "EDIT";
		getBillTreeCardPanel().setEnabled(true);
		setBomButtonsState();
		getBillTreeCardPanel().getCustTree().setEnabled(false);
		getBillTreeCardPanel().getCustTree().setEditable(false);
		setPzdRowSelected();

	}

	protected void onBomPrint() {
		// �˹���ȥ������ϸ���ѯ��ǰ�汾
	}

	protected void onBomReturn() {
		try {
			// ������BOM�۸�
			String idBom = (String) getBillTreeCardPanel().getHeadItem("cbomorderid")
					.getValueObject();
			if (idBom != null) {
				UFDouble dPrice = null;
				String idRoot = (String) getBillTreeCardPanel().getHeadItem("cinventoryid")
						.getValueObject();
				if (SO_14.booleanValue()) {
					if (idRoot != null && idRoot.length() != 0)
						dPrice = BomorderBO_Client.getBomPriceUnify(getCorpPrimaryKey(), idBom,
								idRoot);
				} else
					dPrice = BomorderBO_Client.getBomPrice(idBom);
				String sTmp = (String) getBillTreeCardPanel().getHeadItem("bomorderfee")
						.getValueObject();
				UFDouble dBomorderfee = (sTmp == null ? new UFDouble(0) : new UFDouble(sTmp.trim()));
				// ������
				dPrice = (dPrice == null ? new UFDouble(0) : dPrice).add(dBomorderfee);
				if (dPrice != null && dPrice.doubleValue() != 0) {
					if (SA_02.booleanValue())
						getBillCardPanel().setBodyValueAt(dPrice, orderrow, "noriginalcurtaxprice");
					else
						getBillCardPanel().setBodyValueAt(dPrice, orderrow, "noriginalcurprice");
				}

				// �������ú����޸Ĵ����ֻ��ɾ���ô��
				getBillCardPanel().setCellEditable(orderrow, "cinventorycode", false);

				getBillCardPanel().setBodyRowState(orderrow);
			}
			if (SA_02.booleanValue())
				getBillCardPanel().calculateNumber(orderrow, "noriginalcurtaxprice");
			else
				getBillCardPanel().calculateNumber(orderrow, "noriginalcurprice");

			// ���¼����ͷ��˰�ϼ�
			getBillCardPanel().getHeadItem("nheadsummny").setValue(
					getBillCardPanel().getTotalValue("noriginalcursummny"));

			remove(getBillTreeCardPanel());
			add(getSplitPanelBc(), "Center");
			long s1 = System.currentTimeMillis();
			getBillCardPanel().getBillModel().execLoadFormula();
			SCMEnv.out("ִ�й�ʽ[����ʱ" + (System.currentTimeMillis() - s1) + "]");
			getBillCardPanel().getBillTable().clearSelection();
			strState = strOldState;
			initButtons();
			setButtonsState();
			setTitleText(getBillCardPanel().getTitle());
			// ��ǰBOM��ID
			bomID = null;
			updateUI();
		} catch (Throwable ex) {
			handleException(ex);
		}
	}

	protected void onBomSave() {
		long s = System.currentTimeMillis();
		BomorderVO bomorder = null;
		try {
			if (strBomState.equals("ADD")) {
				bomorder = (BomorderVO) getBillTreeCardPanel().getBillValueVO(
						BomorderVO.class.getName(), BomorderHeaderVO.class.getName(),
						BomorderItemVO.class.getName());
				// ��˾����
				((BomorderHeaderVO) bomorder.getParentVO()).setPk_corp(getCorpPrimaryKey());
				// ��������
				((BomorderHeaderVO) bomorder.getParentVO()).setCreceipttype(SaleBillType.BomOrder);
				// ����״̬
				((BomorderHeaderVO) bomorder.getParentVO()).setFstatus(new Integer(
						nc.ui.pub.bill.BillStatus.FREE));
				bomorder.setStatus(nc.vo.pub.VOStatus.NEW);
			}
			if (strBomState.equals("EDIT")) {
				bomorder = (BomorderVO) getBillTreeCardPanel().getBillValueVO(
						BomorderVO.class.getName(), BomorderHeaderVO.class.getName(),
						BomorderItemVO.class.getName());
				// ��˾����
				((BomorderHeaderVO) bomorder.getParentVO()).setPk_corp(getCorpPrimaryKey());
				// ��������
				((BomorderHeaderVO) bomorder.getParentVO()).setCreceipttype(SaleBillType.BomOrder);
				// ����״̬
				((BomorderHeaderVO) bomorder.getParentVO()).setFstatus(new Integer(
						nc.ui.pub.bill.BillStatus.FREE));
				for (int i = 0; i < bomorder.getChildrenVO().length; i++) {
					((BomorderItemVO[]) bomorder.getChildrenVO())[i]
							.setStatus(nc.vo.pub.VOStatus.UPDATED);
				}
				bomorder.setStatus(nc.vo.pub.VOStatus.UPDATED);
			}
			bomorder.validate();
			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000136")/*
																								 * @res
																								 * "��ʼ��������...."
																								 */);
			SCMEnv.out("��ʼ���棺" + System.currentTimeMillis());
			if (strBomState.equals("ADD")) {
				if (isSaveStart) {
					bomID = BomorderBO_Client.insert(bomorder);
					int row = getBillCardPanel().getBillTable().getSelectedRow();
					getBillCardPanel().setBodyValueAt(bomID, row, "cbomorderid");
					getBillCardPanel().getBillModel().execLoadFormula(row);
					// loadBomCurrData();
				} else {
					BomorderBO_Client.insertItems((BomorderItemVO[]) bomorder.getChildrenVO(),
							bomID, getCorpPrimaryKey());
				}
				loadBomCurrData();
				isSaveStart = false;
			}
			if (strBomState.equals("EDIT")) {
				if (addData) {
					BomorderBO_Client.insertItems((BomorderItemVO[]) bomorder.getChildrenVO(),
							bomID, getCorpPrimaryKey());
					addData = false;
				} else {
					bomorder.setPrimaryKey(bomID);
					BomorderBO_Client.update(bomorder);
				}
			}
			getBillTreeCardPanel().updateValue();
			showHintMessage(Message.getSaveSuccessMessage(s));
			if (strBomState.equals("ADD")) {
				getBillTreeCardPanel().getCustTree().setEnabled(true);
				int yesno = showYesNoMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000396")/*
												 * @res "������������������! "
												 */);
				if (yesno != UIDialog.ID_YES) {
					strBomState = "SAVE";
					getBillTreeCardPanel().setEnabled(false);
					setPzdSelectedEnabled(false);
					addData = false;
				} else {
					strBomState = "EDIT";
					addData = false;
				}
			} else if (strBomState.equals("EDIT")) {
				getBillTreeCardPanel().getCustTree().setEnabled(true);
				int yesno = showYesNoMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000138")/*
												 * @res "�����޸�����������!"
												 */);
				if (yesno != UIDialog.ID_YES) {
					strBomState = "SAVE";
				}
			}
			setBomButtonsState();
			// getBillTreeCardPanel().getCustTree().setEnabled(true);
			SCMEnv.out("�������" + System.currentTimeMillis());
			showHintMessage(Message.getSaveSuccessMessage(s));
		} catch (ValidationException e) {
			showErrorMessage(e.getMessage());
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			SCMEnv.out("���ݱ���ʧ�ܣ�");
			// e.printStackTrace(System.out);
		}
	}

	/**
	 * ҵ�����ͱ仯��
	 * 
	 * �������ڣ�(2001-9-14 9:41:00)
	 * 
	 * @param bo
	 *            nc.ui.pub.ButtonObject
	 * 
	 */
	protected void onBusiType(ButtonObject bo) {
		bo.setSelected(true);

		// ����ҵ������
		boBusiType.setTag(bo.getTag());
		getBillCardPanel().setBusiType(bo.getTag());
		getBillListPanel().setBusiType(bo.getTag());

		// �仯��ť
		setButtonsState();
		setButtons(getBillButtons());

		/** �ı�ҵ�����Ͳ�Ӱ��ģ������� V51* */
		// // ����ģ��
		// if (!SO_20.booleanValue()) {
		// billtempletVO = null;
		// loadListTemplet();
		// loadCardTemplet();
		// setTitleText(getBillCardPanel().getTitle());
		// }
		/** �ı�ҵ�����Ͳ�Ӱ��ģ������� V51* */
	}

	/**
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
	 * 
	 * @version (00-6-1 10:32:59)
	 * 
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(ButtonObject bo) {

		// ���ο�����չ
		try {
			getPluginProxy().beforeButtonClicked(bo);

			// ֧�ֶ��ο�����չ
			if (strShowState.equals("�б�") && getFuncExtend() != null) {/*-=notranslate=-*/

				getFuncExtend().doAction(bo, this, getBillCardPanel(), getBillListPanel(),
						nc.ui.scm.extend.IFuncExtend.LIST);

			} else if (strShowState.equals("��Ƭ") && getFuncExtend() != null) {/*-=notranslate=-*/

				getFuncExtend().doAction(bo, this, getBillCardPanel(), getBillListPanel(),
						nc.ui.scm.extend.IFuncExtend.CARD);

			}
			onExtendBtnsClick(bo);

		} catch (Throwable exx) {
			showErrorMessage(exx.getMessage());
			// //���쳣ֱ�ӷ���
			return;
		}

		// δѡ���д���
		if (strShowState.equals("�б�") /*-=notranslate=-*/
				&& getBillListPanel().getHeadTable().getSelectedRowCount() <= 0
				&& bo.getParent() != boBusiType && bo.getParent() != boBrowse && bo != boDocument
				&& bo != boRefundmentDocument && bo != boListDeselectAll && bo != boListSelectAll
				&& bo != boCoRefPo && bo != boCoPushPo && bo != boCard && bo != boQuery
				&& bo.getParent() != boAdd && bo.getParent() != boBusiType) {
			showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000281")/*
																									 * @res
																									 * "��ѡ��һ������"
																									 */);
			return;
		}

		if (bo == boListSelectAll)
			onSelectAll();
		else if (bo == boListDeselectAll)
			onDeSelectAll();

		// �б�ѡ���д���
		else if ((strShowState.equals("�б�") /*-=notranslate=-*/
		&& getBillListPanel().getHeadTable().getSelectedRowCount() > 1) && bo != boSendAudit
				&& bo != boCard) {
			if (bo == boPrint || bo == boPreview || bo == boSplitPrint || bo == boQuery
					|| bo.getParent() == boBusiType || bo.getParent() == boBrowse
					|| (bo.getTag() != null && bo.getTag().equals("APPROVE"))
					|| (bo.getTag() != null && bo.getTag().equals("SoUnApprove"))
					|| (bo.getTag() != null && bo.getTag().equals("SoBlankout"))) {

				if (bo.getTag() != null && bo.getTag().equals("APPROVE")) {

					int[] selrow = getBillListPanel().getHeadTable().getSelectedRows();
					if (selrow != null && selrow.length > 0) {
						for (int i = 0; i < selrow.length; i++) {
							int iStatus = Integer.parseInt(getBillListPanel()
									.getHeadItem("fstatus").converType(
											getBillListPanel().getHeadBillModel().getValueAt(
													selrow[i], "fstatus")).toString());
							try {
								if (isExistWorkflow(selrow[i])) {
									showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
											"UPP40060301-000282", null,
											new String[] { (selrow[i] + 1) + "" }));

									return;
								}
							} catch (Exception e) {
								showErrorMessage(e.getMessage());
								return;
							}
							if (iStatus != BillStatus.FREE) {
								showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
										"UPP40060301-000284", null,
										new String[] { (selrow[i] + 1) + "" }));

								return;
							}
						}
					}

				} else if (bo.getTag() != null && bo.getTag().equals("SoUnApprove")) {

					int[] selrow = getBillListPanel().getHeadTable().getSelectedRows();
					for (int i = 0; i < selrow.length; i++) {

						int iStatus = Integer.parseInt(getBillListPanel().getHeadItem("fstatus")
								.converType(
										getBillListPanel().getHeadBillModel().getValueAt(selrow[i],
												"fstatus")).toString());
						try {
							if (isExistWorkflow(selrow[i])) {
								showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
										"UPP40060301-000286", null,
										new String[] { (selrow[i] + 1) + "" }));
								// showErrorMessage("��ѡ����[" + (selrow[i] +
								// 1)
								// + "]�Ķ��������˹����������ܶ�����������");
								return;
							}
						} catch (Exception e) {
							showErrorMessage(e.getMessage());
							return;
						}
						if (iStatus != BillStatus.AUDIT) {
							showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
									"UPP40060301-000287", null,
									new String[] { (selrow[i] + 1) + "" }));
							// showErrorMessage("��ѡ�����[" + (selrow[i] + 1)
							// + "]�����������Ķ�������������");
							return;
						}

					}
				} else if (bo.getTag() != null && bo.getTag().equals("SoBlankout")) {

					int[] selrow = getBillListPanel().getHeadTable().getSelectedRows();
					for (int i = 0; i < selrow.length; i++) {

						int iStatus = Integer.parseInt(getBillListPanel().getHeadItem("fstatus")
								.converType(
										getBillListPanel().getHeadBillModel().getValueAt(selrow[i],
												"fstatus")).toString());
						if (iStatus != BillStatus.FREE) {
							showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
									"UPP40060301-000527", null,
									new String[] { (selrow[i] + 1) + "" }));
							// showErrorMessage("��ѡ�����[" + (selrow[i] + 1)
							// + "]�����������Ķ�������������");
							return;
						}

					}
				}

			} else {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000288")/*
												 * @res "��ѡ����"
												 */);
				return;
			}
		}

		getBillCardPanel().stopEditing();

		// �������޸� ��ʾ
		showHintMessage("");
		try {
			if (bo == boAddLine)
				onAddLine();
			else if (bo == boDelLine)
				onDelLine();
			else if (bo == boInsertLine)
				onInsertLine();
			else if (bo == boCopyLine)
				onCopyLine();
			else if (bo == boPasteLine)
				onPasteLine();
			else if (bo == boPasteLineToTail)
				onPasteLineToTail();
			else if (bo == boFindPrice)
				onFindPrice();
			else if (bo == boResortRowNo)
				onResortRowNo();
			else if (bo == boCardEdit)
				onCardEdit();
			else if (bo == boSave)
				onSave();
			else if (bo == boCancel)
				onCancel();
			else if (bo == boEdit)
				onEdit();
			else if (bo == boCard)
				onCard();
			else if (bo == boPreview)
				onPrint(true);
			else if (bo == boPrint)
				onPrint(false);
			else if (bo == boSplitPrint)
				onSplitPrint();
			else if (bo == boFind)
				onFind();
			else if (bo == boFirst)
				onFrist();
			else if (bo == boLast)
				onLast();
			else if (bo == boPre)
				onPre();
			else if (bo == boNext)
				onNext();
			else if (bo == boDocument || "document".equals(bo.getTag()))
				onDocument();
			else if (bo == boReturn)
				onReturn();
			else if (bo == boBack)
				onBack();
			else if (bo == boQuery)
				onQuery();
			else if (bo == boRefresh)
				onRefresh();
			else if (bo == boRefundment)
				onRefundment();
			else if (bo == boBatch)
				onBatch();
			else if (bo == boOrderQuery)
				// ����
				onOrderQuery();
			else if (bo == boBom) {
				onBom();
				return;
			} else if (bo == boBomSave)
				onBomSave();
			else if (bo == boBomEdit)
				onBomEdit();
			else if (bo == boBomCancel)
				onBomCancel();
			else if (bo == boAuditFlowStatus || "auditflowstatus".equals(bo.getTag()))
				onAuditFlowStatus();
			else if (bo == boSendAudit)
				onSendAudit();
			else if (bo == boBomPrint)
				onBomPrint();
			else if (bo == boBomReturn)
				onBomReturn();
			else if (bo == boModification)
				onModification();
			else if (bo.getParent() == boBusiType) {
				// ҵ������
				onBusiType(bo);
			} else if (bo.getParent() == boAdd) {
				// ����
				onNew(bo);
			} else if (bo == boCoRefPo) {
				// ����Эͬ�ɹ�����
				onCoRefPo(bo);
			} else if (bo == boCoPushPo) {
				// Эͬ�Ʋɹ�����
				onCoPushPo();
			} else if (bo == boCopyBill) {
				onCopyBill();
			} else if (bo.getParent() == boAction || "APPROVE".equals(bo.getTag())
					|| "SoUnApprove".equals(bo.getTag()) || "SoBlankout".equals(bo.getTag())) {
				// ִ��
				onAction(bo);
			} else if (bo == boCachPay) {
				// �����տ�
				onCachPay();
				return;
			} else if (bo == boOrdBalance) {
				// ��������
				onOrderBalance();
				return;
			} else if (bo == boOnHandShowHidden) {
				// ������ʾ/����
				onOnHandShowHidden();
			} else if (bo == boCustCredit) {
				// �ͻ�����
				onAssistant(bo);
			} else if (bo == boOrderExecRpt) {
				// ����ִ�����
				onAssistant(bo);
			} else if (bo == boCustInfo) {
				// �ͻ���Ϣ
				onAssistant(bo);
			} else if (bo == boPrifit) {
				// ë��Ԥ��
				onAssistant(bo);
			} else if (bo.getParent() == boAssistant) {
				// ����
				onAssistant(bo);
			} else if (bo.getParent() == boAfterAction) {
				// ��������
				onAfterAction(bo);
			} else {
				ButtonObject[] btns = getOrderBalanceUI().getButtons();
				if (btns != null && btns.length > 0) {
					for (int i = 0, loop = btns.length; i < loop; i++) {
						if (btns[i] == bo || btns[i] == bo.getParent()) {
							getOrderBalanceUI().onButtonClicked(bo);
							updateButtons();
							updateUI();
							return;
						}
					}
				}

				if (getBillToBillUI() != null) {
					btns = getBillToBillUI().setButtenForUse(SaleBillType.SaleOrder);
					if (btns != null && btns.length > 0) {
						for (int i = 0, loop = btns.length; i < loop; i++) {
							if (btns[i] == bo || btns[i] == bo.getParent()) {
								getBillToBillUI().onButtonClicked(bo);
								updateButtons();
								updateUI();
								return;
							}
						}
					}
				}

			}

			// ���ο�����չ
			getPluginProxy().afterButtonClicked(bo);

		} catch (Exception e) {
			handleException(e);
			showErrorMessage(e.getMessage());
		}

		setButtonsState();
		if (bo.getParent() == boAdd || bo == boCopyBill || bo == boRefundment || bo == boEdit) {
			// ����궨λ�ڵ�һ���ɱ༭��
			getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
		}

	}

	protected void onSelectAll() {
		try {
			int maxindex = getBillListPanel().getHeadBillModel().getRowCount();
			if (maxindex > 0)
				getBillListPanel().getHeadTable().setRowSelectionInterval(0, maxindex - 1);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}

	}

	protected void onDeSelectAll() {
		try {
			int maxindex = getBillListPanel().getHeadBillModel().getRowCount();
			if (maxindex > 0)
				getBillListPanel().getHeadTable().clearSelection();
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}
	}

	/**
	 * ����������ݺϷ��ԡ�
	 * 
	 * �������ڣ�(2001-8-8 10:09:01)
	 * 
	 * @return boolean
	 */
	private boolean isExistWorkflow(int row) throws ValidationException {
		boolean isExist = false;
		try {
			isExist = nc.ui.pub.pf.PfUtilBO_Client.isExistWorkFlow(getBillType(), boBusiType
					.getTag(), getClientEnvironment().getCorporation().getPk_corp(),
					(String) getBillListPanel().getHeadBillModel().getValueAt(row, "coperatorid"));

		} catch (Throwable e) {
			throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000276")/*
											 * @res "�жϵ����Ƿ��ڹ�����ʧ��"
											 */);
		}
		return isExist;
	}

	/**
	 * �������롣
	 * 
	 * �������ڣ�(2001-4-21 10:36:57)
	 * 
	 */
	public void onCancel() {

		// // �ָ�ģ����ĳ�ʼ�༭״̬
		// getBillCardTools().resumeBillItemEditToInit();

		getBillCardPanel().setEnabled(false);

		strState = "����";

		getBillCardPanel().resumeValue();

		if (SO41.booleanValue()) {
			if (getBillCardTools().getOldsaleordervo() != null)
				getBillCardPanel().setTailItem("coperatorid",
						getBillCardTools().getOldsaleordervo().getHeadVO().getCoperatorid());
		}

		getBillCardTools().setHeadRefLimit(strState);

		String csaleid = (String) getBillCardPanel().getHeadItem("csaleid").getValueObject();

		SaleOrderVO saleorder = vocache.getSaleOrderVO(csaleid);
		loadCardData(saleorder);
		setButtonsState();
		getBillCardPanel().showCustManArInfo();
		updateUI();

		showHintMessage("");

		// ���������ɫ����
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), null);

		nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel());
	}

	/**
	 * �������б�״̬���Ŀ�Ƭ״̬��
	 * 
	 * �������ڣ�(2001-3-17 9:00:09)
	 * 
	 */
	protected void onCard() {
		strShowState = "��Ƭ"; /*-=notranslate=-*/
		strState = "����"; /*-=notranslate=-*/
		switchInterface();
		num = getBillListPanel().getHeadTable().getSelectedRow();

		String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");

		if (csaleid != null) {
			SaleOrderVO saleorder = vocache.getSaleOrderVO(csaleid);
			loadCardData(saleorder);
			
			// ���ñ�ͷ��ͬ���������б��������ʾ
			// add by river for 2012-07-18
			// start ..
			if(getBillCardPanel().getHeadItem("contracttype") != null) {
				if(saleorder != null && saleorder.getParentVO() != null) {
					if((Integer) saleorder.getParentVO().getAttributeValue("contracttype") == 10)
						((UIComboBox) getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("�ֻ���ͬ");
					else if((Integer) saleorder.getParentVO().getAttributeValue("contracttype") == 20)
						((UIComboBox) getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("������ͬ");
				}
			}
			
			// .. end
		} else {
			loadCardData(null);
		}

		setButtons(getBillButtons());
		setBodyMenuItem();
		setButtonsState();

		showCustManArInfo();
		updateUI();
	}

	/**
	 * ��ⵥ�ݺϷ��ԡ�
	 * 
	 * �������ڣ�(2001-8-8 10:09:01)
	 * 
	 * @return boolean
	 * 
	 */
	protected void onCheck(SaleOrderVO saleorder) throws ValidationException, BusinessException {

		// ��մ�������Ϣ
		rowList.clear();

		// VO���
		saleorder.validate();

		saleorder.checkSummny();

		// long s = System.currentTimeMillis();

		// ����ͷ������֯������Ϊĩ��
		/** ��Ը��Ƶ��ݣ�������Ϣ�Զ����������* */
		getBillCardPanel().checkSaleCorp();

		// ������
		if (getBillCardPanel().getRowCount() == 0)
			throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000073")/* @res "�����岻��Ϊ��!" */);

		/** v5.3ȥ�������ֶ� * */
		/*
		 * Object oaccountperiod = getBillCardPanel()
		 * .getHeadItem("naccountperiod").getValueObject(); if (oaccountperiod !=
		 * null && oaccountperiod.toString().trim().length() > 0) { UFDouble
		 * naccountperiod = null; try { naccountperiod = new
		 * UFDouble(oaccountperiod.toString().trim()); } catch (Exception e) {
		 * throw new ValidationException(NCLangRes.getInstance()
		 * .getStrByID("40060301", "UPP40060301-000397") @res "����ӦΪ��������! " ); }
		 * if (naccountperiod.doubleValue() < 0) throw new
		 * ValidationException(NCLangRes.getInstance() .getStrByID("40060301",
		 * "UPP40060301-000397") @res "����ӦΪ��������! " ); }
		 */

		//
		int iOOSLine = 0;

		// ��鶩�������Ƿ�����������ŵ���
		int iallMinusMnyrow = 0;
		int iallMinusNumrow = 0;
		SaleorderBVO[] bodyVOs = (SaleorderBVO[]) saleorder.getChildrenVO();

		UFBoolean bretflag = saleorder.getHeadVO().getBretinvflag();

		for (int i = 0; i < saleorder.getChildrenVO().length; i++) {

			if (bodyVOs[i].getNexchangeotobrate() == null
					|| bodyVOs[i].getNexchangeotobrate().compareTo(UFDouble.ZERO_DBL) <= 0) {
				rowList.add(new Integer(i));
				throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000563", null, new String[] { (i + 1) + "" }));
			}

			if (bodyVOs[i].getCconsigncorpid() != null
					&& bodyVOs[i].getCconsigncorpid().trim().length() > 0
					&& !getCorpPrimaryKey().equals(bodyVOs[i].getCconsigncorpid())) {
				if (bodyVOs[i].getCinventoryid1() == null
						|| bodyVOs[i].getCinventoryid1().trim().length() <= 0) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000193", null, new String[] { (i + 1) + "" }));
				}
			}
			if (bodyVOs[i].getNoriginalcursummny() != null
					&& bodyVOs[i].getNoriginalcursummny().doubleValue() < 0) {
				iallMinusMnyrow++;
			}

			if ((bodyVOs[i].getNoriginalcurtaxprice() != null && bodyVOs[i]
					.getNoriginalcurtaxprice().doubleValue() < 0)
					|| (bodyVOs[i].getNoriginalcurtaxnetprice() != null && bodyVOs[i]
							.getNoriginalcurtaxnetprice().doubleValue() < 0)
					|| (bodyVOs[i].getNoriginalcurprice() != null && bodyVOs[i]
							.getNoriginalcurprice().doubleValue() < 0)
					|| (bodyVOs[i].getNoriginalcurnetprice() != null && bodyVOs[i]
							.getNoriginalcurnetprice().doubleValue() < 0)) {
				rowList.add(new Integer(i));
				throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000420", null, new String[] { (i + 1) + "" }));
				// throw new ValidationException("��[" + (i + 1)
				// + "]�ļ۸񣨺�˰���ۣ�����˰���ۣ���˰���ۣ�����˰���ۣ�����С��0! ");

			}

			if (bodyVOs[i].getNnumber() != null && bodyVOs[i].getNnumber().doubleValue() >= 0) {
				if (bretflag != null && bretflag.booleanValue()) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000195", null, new String[] { (i + 1) + "" }));
					// throw new ValidationException("�����˻���������[" + (i + 1)
					// + "]���������ܴ��ڵ���0! ");
				}
			}

			// ����<0���
			if (bodyVOs[i].getNnumber() != null && bodyVOs[i].getNnumber().doubleValue() < 0) {
				if (!SO45.booleanValue() && (bretflag == null || !bretflag.booleanValue())) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000194", null, new String[] { (i + 1) + "" }));
					// throw new ValidationException("��[" + (i + 1)
					// + "]����������С��0! ");
				}
				// �繫˾���۹�˾�ĺ����б�����ֱ��
				else {
					if (bodyVOs[i].getCconsigncorpid() != null
							&& !bodyVOs[i].getCconsigncorpid().equals(bodyVOs[i].getPkcorp())
							&& (bodyVOs[i].getBdericttrans() == null || !bodyVOs[i]
									.getBdericttrans().booleanValue())) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPT40060301-000548", null,
								new String[] { (i + 1) + "" }));
					}
					// throw new ValidationException("��[" + (i + 1)
					// + "]�Ĵ��������ֱ��! ");
				}
				iallMinusNumrow++;
			}

			if (bodyVOs[i].getBoosflag() != null && bodyVOs[i].getBoosflag().booleanValue()) {

				iOOSLine++;
			} else {
				if (bodyVOs[i].getBoosflag() == null)
					bodyVOs[i].setBoosflag(new UFBoolean(false));
			}
		}
		if (iOOSLine == bodyVOs.length) {
			throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000398")/*
											 * @res "�����в���ȫѡ��ȱ��! "
											 */);
		}
		// if (iallMinusNumrow > 0 && iallMinusNumrow < bodyVOs.length) {
		// throw new ValidationException("�����е��������ܲ���Ϊ��! ");
		// }
		// if (iallMinusMnyrow > 0 && iallMinusMnyrow < bodyVOs.length) {
		// throw new ValidationException("�����еļ�˰�ϼƲ��ܲ���Ϊ���Ľ��! ");
		// }

		// ��������
		if (SO_01 != null) {
			if (SO_01.intValue() != 0) {
				if (SO_01.intValue() < getBillCardPanel().getRowCount()) {
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000171", null, new String[] { SO_01.intValue() + "" }));
					// throw new ValidationException("������"
					// + CommonConstant.BEGIN_MARK + SO_01.intValue()
					// + CommonConstant.END_MARK + "�С�");
				}
			}
		}
		//
		if (getBillType().equals(SaleBillType.SaleOrder)) {
			// �۱�����
			if (getBillCardPanel().getHeadItem("nexchangeotobrate").getValueObject() == null
					|| getBillCardPanel().getHeadItem("nexchangeotobrate").getValueObject().equals(
							"")) {
				throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000310")/* @res "�۱����ʲ���Ϊ��!" */);
			}

		}

		boolean bischeckaddr = false;
		BillItem biaddr = getBillCardPanel().getHeadItem("vreceiveaddress");
		try {

			if (biaddr != null) {
				bischeckaddr = biaddr.isNull();
				biaddr.setNull(false);
			}

			// �û��������÷ǿ����� yb modify 2003-10-15 �޸�������ǿռ������
			// getBillCardPanel().dataNotNullValidate();
			int[] notcheckline = getNotCheckFreeItemLine();
			if (notcheckline == null)
				getBillCardPanel().getBillData().dataNotNullValidate();
			else {
				BillItem item = getBillCardPanel().getBodyItem("vfree0");
				if (item == null)
					getBillCardPanel().getBillData().dataNotNullValidate();
				else {
					Hashtable ht = new Hashtable();
					ht.put(item, notcheckline);
					getBillCardPanel().getBillData().dataNotNullValidate(ht);
				}
			}

			if (biaddr != null) {
				if (bischeckaddr) {
					String saddr = ((UIRefPane) biaddr.getComponent()).getUITextField().getText();
					if (saddr == null || saddr.trim().length() <= 0) {
						throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000083")
								+ biaddr.getName()/* @res "�����ֶβ���Ϊ�գ�" */
						);
						// "��ͷ"+biaddr.getName()+"����Ϊ��!");
						// showErrorMessage();
						// return
					}
				}
			}

		} finally {
			biaddr.setNull(bischeckaddr);
		}

		boolean retflag = false;
		UFBoolean bret = new UFBoolean(getBillCardPanel().getHeadItem("bretinvflag").getValue());
		if (bret != null && bret.toString().equals("Y"))
			retflag = true;

		// �����߼��
		if (SO27 != null && SO27.booleanValue())
			getBillCardTools().checkProdLineForOne();

		// �������
		boolean isOtherRow = false;

		HashMap hp = new HashMap();
		if (strState.equals("�޶�")) {
			SaleOrderVO svo = getBillCardTools().getOldsaleordervo();
			for (int i = 0; i < svo.getChildrenVO().length; i++) {
				if (svo.getChildrenVO()[i].getPrimaryKey() != null) {
					hp.put(svo.getChildrenVO()[i].getPrimaryKey(), svo.getChildrenVO()[i]);
				}
			}
		}
		for (int i = 0; i < saleorder.getChildrenVO().length; i++) {

			SaleorderBVO oldbodyVO = (SaleorderBVO) saleorder.getChildrenVO()[i];

			if (oldbodyVO.getCconsigncorpid() != null
					&& !oldbodyVO.getCconsigncorpid().equals(getCorpPrimaryKey())) {

				isOtherRow = true;

			} else {

				isOtherRow = false;

			}

			// �ջ������֯�ǿռ���
			if (isOtherRow) {

				if (oldbodyVO.getCreccalbodyid() == null
						|| oldbodyVO.getCreccalbodyid().trim().length() <= 0
				// || oldbodyVO.getCreccalbody() == null
				// || oldbodyVO.getCreccalbody().trim().length() <= 0
				) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000202")/*
													 * @res
													 * "�����ֶβ���Ϊ�գ�������˾�Ǳ����۹�˾�е��ջ������֯"
													 */);
				}
				if (oldbodyVO.getBdericttrans() == null
						|| !oldbodyVO.getBdericttrans().booleanValue()) {
					if (oldbodyVO.getCrecwareid() == null
							|| oldbodyVO.getCrecwareid().trim().length() <= 0) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000203")/*
																	 * @res
																	 * "�����ֶβ���Ϊ�գ���ֱ�˶����е��ջ��ֿ�"
																	 */);
					}

				}

				if (oldbodyVO.getLaborflag() != null && oldbodyVO.getLaborflag().booleanValue()) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000204")/*
													 * @res "�����������ܴ�������˾����"
													 */);
				}

				if (oldbodyVO.getDiscountflag() != null
						&& oldbodyVO.getDiscountflag().booleanValue()) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000205")/*
													 * @res "�ۿ��������ܴ�������˾����"
													 */);
				}

			}
			// �����֯�ǿռ���
			// if (SA_16 != null && SA_16.booleanValue()) {
			if (!isOtherRow) {
				// if((oldbodyVO.getLaborflag()==null ||
				// !oldbodyVO.getLaborflag().booleanValue())
				// && (oldbodyVO.getDiscountflag()==null ||
				// !oldbodyVO.getDiscountflag().booleanValue())){
				if (oldbodyVO.getCadvisecalbodyid() == null
						|| oldbodyVO.getCadvisecalbodyid().equals("")) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000206")/*
													 * @res "�����ֶβ���Ϊ�գ����������֯"
													 */);
				}
				// }
			}
			// }
			// ��������
			if (oldbodyVO.getStatus() == VOStatus.NEW) {
				oldbodyVO.setPrimaryKey(null);
				oldbodyVO.setCsaleid(null);
			}
			if (oldbodyVO.getDiscountflag() == null || !oldbodyVO.getDiscountflag().booleanValue()) {
				// ���ۿ�����
				if (oldbodyVO.getNnumber() == null || oldbodyVO.getNnumber().doubleValue() == 0) {

					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000207")/*
													 * @res "��������Ϊ��!"
													 */);
				}
				if (oldbodyVO.getNquoteunitnum() == null
						|| oldbodyVO.getNquoteunitnum().doubleValue() == 0) {

					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000208")/*
													 * @res "������������Ϊ��!"
													 */);
				}// continue;
			}
			// �ӱ�ͷЯ����������
			if (oldbodyVO.getNexchangeotobrate() == null) {
				if (getBillCardPanel().getHeadItem("nexchangeotobrate").getValueObject() == null
						|| getBillCardPanel().getHeadItem("nexchangeotobrate").getValueObject()
								.equals("")) {
				} else {
					oldbodyVO.setNexchangeotobrate(new UFDouble(getBillCardPanel().getHeadItem(
							"nexchangeotobrate").getValue()));
				}
			}

			// ���ۿ�����
			if (oldbodyVO.getDiscountflag() == null || !oldbodyVO.getDiscountflag().booleanValue()) {
				// ������Ʒʱ��ⵥ��
				if (!(oldbodyVO.getBlargessflag() == null || oldbodyVO.getBlargessflag()
						.booleanValue())
				// && !(oldbodyVO.getIsappendant() == null ||
				// oldbodyVO.getIsappendant().booleanValue())
				) {

					// ����
					if ((oldbodyVO.getNoriginalcurmny() != null && oldbodyVO.getNoriginalcurmny()
							.doubleValue() < 0)
							|| (oldbodyVO.getNoriginalcurtaxmny() != null && oldbodyVO
									.getNoriginalcurtaxmny().doubleValue() < 0)
							|| (oldbodyVO.getNoriginalcursummny() != null && oldbodyVO
									.getNoriginalcursummny().doubleValue() < 0))
						if (oldbodyVO.getNnumber().doubleValue() > 0) {
							rowList.add(new Integer(i));
							throw new ValidationException(NCLangRes.getInstance().getStrByID(
									"40060301", "UPP40060301-000209")/*
																		 * @res
																		 * "��ǰ���������������!"
																		 */);
						}
					// ����������˰�ϼ�
					if (oldbodyVO.getNoriginalcursummny() == null
					/** �����˰�ϼ�Ϊ0���� V55 jindongmei zhongwei* */
					// || oldbodyVO.getNoriginalcursummny().doubleValue() == 0
					) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000210", null,
								new String[] { (i + 1) + "" })/*
																 * @res
																 * "���м�˰�ϼƲ���Ϊ��!"
																 */);
					}
				}
			} else {
				// ����������˰�ϼ�
				if (oldbodyVO.getNoriginalcursummny() == null) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000210", null, new String[] { (i + 1) + "" })/*
																						 * @res
																						 * "���м�˰�ϼƲ���Ϊ��!"
																						 */);
				}

				// ����������˰�ϼ�
				if (oldbodyVO.getNnumber() == null && oldbodyVO.getNoriginalcurmny() == null
						&& oldbodyVO.getNoriginalcursummny() == null) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000261")/*
													 * @res "����������˰�ϼƲ���ͬʱΪ��!"
													 */);
				}
			}
			// ��װ��λ(�Ƿ���ø�����)
			if (oldbodyVO.getAssistunit() != null && oldbodyVO.getAssistunit().booleanValue()) {
				if (oldbodyVO.getDiscountflag() != null
						&& !oldbodyVO.getDiscountflag().booleanValue()) {
					if (oldbodyVO.getCpackunitid() == null
							|| oldbodyVO.getCpackunitid().trim().length() == 0) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000211", null,
								new String[] { (i + 1) + "" }));
						// throw new ValidationException("��" + (i + 1)
						// + "�и���λ����Ϊ��!");
					}
					if (oldbodyVO.getNpacknumber() == null) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000212", null,
								new String[] { (i + 1) + "" }));
						// throw new ValidationException("��" + (i + 1)
						// + "�и���������Ϊ��!");
					}
					if (oldbodyVO.getScalefactor() == null) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000548", null,
								new String[] { (i + 1) + "" }));
						// throw new ValidationException("��" + (i + 1)
						// + "�л����ʲ���Ϊ��!");
					} else {
						if (oldbodyVO.getNpacknumber().doubleValue()
								* oldbodyVO.getNnumber().doubleValue() < 0) {
							rowList.add(new Integer(i));
							throw new ValidationException(NCLangRes.getInstance().getStrByID(
									"40060301", "UPP40060301-000213", null,
									new String[] { (i + 1) + "" }));
							// throw new ValidationException("��" + (i + 1)
							// + "�������͸��������ű�����ͬ!");
						}
					}
				}
			}
			// �����ķ�������С�ڶ�������
			UFDate dbilldate = new UFDate(getBillCardPanel().getHeadItem("dbilldate").getValue());
			UFDate dconsigndate = oldbodyVO.getDconsigndate();
			UFDate deliverdate = oldbodyVO.getDdeliverdate();
			if (dconsigndate == null || dconsigndate.toString().length() == 0
					|| deliverdate == null || deliverdate.toString().length() == 0) {
				rowList.add(new Integer(i));
				throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000214")/*
												 * @res "�����ķ������ڡ��������ڲ���Ϊ��!"
												 */);
			}
			if (!retflag && dconsigndate != null && dbilldate != null) {
				if (dbilldate.after(dconsigndate) && dbilldate != dconsigndate) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000033")/*
													 * @res "��������Ӧ���ڵ��ڵ�������!"
													 */);
				}
			}
			if (dconsigndate != null && deliverdate != null) {
				if (dconsigndate.after(deliverdate) && deliverdate != dconsigndate) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000034")/*
													 * @res "�����Ľ�������Ӧ���ڵ��ڷ�������!""
													 */);
				}
			}
			if (!isOtherRow) {
				if ((oldbodyVO.getFbatchstatus() != null && oldbodyVO.getFbatchstatus().intValue() == 1)
						&& (oldbodyVO.getCbatchid() == null || oldbodyVO.getCbatchid().trim()
								.length() == 0)) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000215")/*
													 * @res "��ָ���������!"
													 */);
				}
			}
			// ���ݲ������
			String oldinventoryid = oldbodyVO.getCinventoryid(); // ���
			// String oldbodywarehouseid = oldbodyVO.getCbodywarehouseid(); //�ֿ�
			// String oldbatchid = oldbodyVO.getCbatchid(); //��������
			// Integer oldinvclass = null; //���Ｖ��
			if (oldbodyVO.getBlargessflag() != null && !oldbodyVO.getBlargessflag().booleanValue()) {
				for (int j = i + 1; j < saleorder.getChildrenVO().length; j++) {
					SaleorderBVO newbodyVO = (SaleorderBVO) saleorder.getChildrenVO()[j];
					// SO_03 ��ͬ����ɷ񹲴�һ������Ʒ���⣩
					if (!SO_03.booleanValue()) {
						String newinventoryid = newbodyVO.getCinventoryid(); // ���
						if (newbodyVO.getBlargessflag() != null
								&& !newbodyVO.getBlargessflag().booleanValue()) {
							if (oldinventoryid.equals(newinventoryid)) {
								rowList.add(new Integer(i));
								throw new ValidationException(NCLangRes.getInstance().getStrByID(
										"40060301", "UPP40060301-000216", null,
										new String[] { (i + 1) + "", (j + 1) + "" }));
								// throw new nc.vo.pub.ValidationException("��"
								// + CommonConstant.BEGIN_MARK + (i + 1)
								// + CommonConstant.END_MARK + "�к͵�"
								// + CommonConstant.BEGIN_MARK + (j + 1)
								// + CommonConstant.END_MARK + "�д����ͬ��");
							}
						}
					}
				}
			}
			// �޶����
			if (strState.equals("�޶�")) {/*-=notranslate=-*/

				UFDouble nnumber = oldbodyVO.getNnumber();
				CircularlyAccessibleValueObject coriVO = (CircularlyAccessibleValueObject) hp
						.get(oldbodyVO.getPrimaryKey());
				if (coriVO != null
						&& coriVO.getAttributeValue("nnumber") != null
						&& oldbodyVO.getNnumber() != null
						&& (((UFDouble) coriVO.getAttributeValue("nnumber")).multiply(oldbodyVO
								.getNnumber())).doubleValue() < 0) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("sopub",
							"UPPsopub-000007"));
				}
				// �ʲ��ж�
				checkAllowDiffer(oldbodyVO, i);

				// +narrangescornum
				UFDouble ntotalconvertnum = SoVoTools.getMnyAdd(SoVoConst.duf0, oldbodyVO
						.getNarrangescornum());
				// +narrangepoapplynum
				ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, oldbodyVO
						.getNarrangepoapplynum());
				// +narrangetoornum
				ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, oldbodyVO
						.getNarrangetoornum());
				// +norrangetoapplynum
				ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, oldbodyVO
						.getNorrangetoapplynum());
				// +narrangemonum
				ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, oldbodyVO
						.getNarrangemonum());
				nnumber = oldbodyVO.getNnumber();
				if (nnumber != null && nnumber.doubleValue() < 0) {
					nnumber = nnumber.multiply(-1);
					ntotalconvertnum = ntotalconvertnum == null ? new UFDouble(0)
							: ntotalconvertnum.multiply(-1);
				}
				if (ntotalconvertnum != null
						&& nnumber != null
						&& ntotalconvertnum.abs().compareTo(nnumber.abs().multiply(SO43.add(1.0))) > 0) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000501", null, new String[] { (i + 1) + "" }));
					// UPP40060301-000501=��{0}����ӆ��������С��DՈُ�Σ�ί��ӆ�Σ����bӆ�Σ��{��ӆ�Σ��{����Ո�Δ���֮�͡�
				}
			}
			// �˻����
			if (strState.equals("�˻�")) {/*-=notranslate=-*/
				UFDouble nnumber = oldbodyVO.getNnumber();
				UFDouble ntotalreceiptnumber = oldbodyVO.getNtotalreceivenumber();
				if (nnumber != null)
					if (nnumber.compareTo(new UFDouble(0)) >= 0) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000225", null,
								new String[] { (i + 1) + "" }));
					}
				// throw new nc.vo.pub.ValidationException("��"
				// + CommonConstant.BEGIN_MARK + (i + 1)
				// + CommonConstant.END_MARK + "���˻��������ܴ��ڻ�����㡣");
				if (nnumber != null && ntotalreceiptnumber != null)
					if (nnumber.compareTo(ntotalreceiptnumber) == 1) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000226", null,
								new String[] { (i + 1) + "" }));
					}

				UFDouble ntotalinvoicenumber = oldbodyVO.getNtotalinvoicenumber();
				if (nnumber != null && ntotalinvoicenumber != null)
					if (nnumber.compareTo(ntotalinvoicenumber) == 1) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000228", null,
								new String[] { (i + 1) + "" }));
					}

				UFDouble ntotalinventorynumber = oldbodyVO.getNtotalinventorynumber();
				if (nnumber != null && ntotalinventorynumber != null)
					if (nnumber.compareTo(ntotalinventorynumber) == 1) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000229", null,
								new String[] { (i + 1) + "" }));
					}

			}
		}
		// System.out.println("�Ϸ��Լ����ʱ��" + (System.currentTimeMillis() - s) +
		// "����");

		// ȫͨ���Ժ����β���
	}

	/**
	 * ����Ƿ񳬳��ʲ� wsy 2005-9-13
	 */
	private void checkAllowDiffer(SaleorderBVO oldbodyVO, int i) throws ValidationException {

		SaleOrderVO vo = vocache.getSaleOrderVO(oldbodyVO.getCsaleid());
		SaleorderBVO bvo = null;
		if (vo != null && vo.getBodyVOs() != null)
			for (int k = 0; k < vo.getBodyVOs().length; k++) {
				if (vo.getBodyVOs()[k].getCorder_bid().equals(oldbodyVO.getCorder_bid())) {
					bvo = vo.getBodyVOs()[k];
				}

			}
		if (bvo == null)
			bvo = oldbodyVO;

		UFDouble nnumber = oldbodyVO.getNnumber() == null ? new UFDouble(0) : oldbodyVO
				.getNnumber();
		boolean bisNeg = false;
		if (nnumber.doubleValue() < 0) {
			bisNeg = true;
			nnumber = nnumber.multiply(-1);

		}

		UFDouble ntotalreceiptnumber = bvo.getNtotalreceivenumber() == null ? new UFDouble(0) : bvo
				.getNtotalreceivenumber();
		UFDouble ntotalinvoicenumber = bvo.getNtotalinvoicenumber() == null ? new UFDouble(0) : bvo
				.getNtotalinvoicenumber();
		UFDouble ntotalinventorynumber = (bvo.getNtotalinventorynumber() == null ? new UFDouble(0)
				: bvo.getNtotalinventorynumber())
				.add(bvo.getNtotalshouldoutnum() == null ? new UFDouble(0) : bvo
						.getNtotalshouldoutnum());
		UFDouble ntranslossnum = bvo.getNtranslossnum() == null ? new UFDouble(0) : bvo
				.getNtranslossnum();

		ntotalinventorynumber = ntotalinventorynumber.sub(ntranslossnum);

		if (bisNeg) {
			ntotalreceiptnumber = ntotalreceiptnumber.multiply(-1);
			ntotalinvoicenumber = ntotalinvoicenumber.multiply(-1);
			ntotalinventorynumber = ntotalinventorynumber.multiply(-1);
		}

		UFDouble postiveNum = null;

		// ��{0}���޶��������ܳ���������������
		String FHMSG = NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000218", null,
				new String[] { (i + 1) + "" });
		String CKMSG = NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000222", null,
				new String[] { (i + 1) + "" });
		String KPMSG = NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000221", null,
				new String[] { (i + 1) + "" });

		ArrayList al = new ArrayList();

		postiveNum = nnumber;

		if (postiveNum.compareTo(ntotalreceiptnumber) < 0) {
			al.add(FHMSG);
		}

		if (SO25.booleanValue())
			postiveNum = nnumber.multiply(1.0 + SO26.doubleValue() / 100.);
		else
			postiveNum = nnumber;

		if (postiveNum.compareTo(ntotalinvoicenumber) < 0) {
			al.add(KPMSG);
		}

		postiveNum = nnumber;

		if (postiveNum.compareTo(ntotalinventorynumber) < 0) {
			al.add(CKMSG + "\n" + NCLangRes.getInstance().getStrByID("common", "UC000-0003145")/*
																								 * @res
																								 * "�ۼƳ�������"
																								 */
					+ ":" + ntotalinventorynumber);
		}

		// ��[]���޶�����{}����[]����{}���ʲΧ[]

		if (al.size() > 0) {
			String sScoremsg = "";
			for (int j = 0; j < al.size(); j++) {
				sScoremsg += (String) al.get(j) + "\n";
			}
			throw new ValidationException(sScoremsg);

		}

	}

	/**
	 * �ĵ�����
	 * 
	 * �������ڣ�(2001-4-24 9:55:56)
	 * 
	 */
	protected void onDocument() {
		String id = null;
		if (strShowState.equals("�б�")) { /*-=notranslate=-*/
			id = getBillListPanel().getHeadBillModel().getValueAt(
					getBillListPanel().getHeadTable().getSelectedRow(), "csaleid").toString();
		} else {
			id = getBillCardPanel().getHeadItem("csaleid").getValue();
		}

		DocumentManager.showDM(this, SaleBillType.SaleOrder, id);
	}

	/**
	 * �޸ġ�
	 * 
	 * �������ڣ�(2001-3-17 9:00:09)
	 * 
	 */
	protected void onEdit() {
		SaleOrderVO saleorder = null;

		if (strShowState.equals("�б�")) { /*-=notranslate=-*/
			// ����״̬
			strShowState = "��Ƭ"; /*-=notranslate=-*/
			switchInterface();
			num = getBillListPanel().getHeadTable().getSelectedRow();

			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num,
					"csaleid");

			saleorder = vocache.getSaleOrderVO(csaleid);
			loadCardData(saleorder);

			setButtons(getBillButtons());
			setBodyMenuItem();
		} else {
			saleorder = vocache.getSaleOrderVO(getCurrentOrderId());
		}

		strState = "�޸�"; /*-=notranslate=-*/
		setButtonsState();
		getBillCardPanel().setEnabled(true);

		// �ָ�����ģ��ĳ�ʼ�༭״̬
		getBillCardTools().resumeBillItemEditToInit();

		vRowATPStatus = new Vector();
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			vRowATPStatus.addElement(new UFBoolean(false));

			// �๫˾�еı༭��
			ctlUIOnCconsignCorpChg(i);
		}

		if (getBillCardPanel().alInvs == null || getBillCardPanel().alInvs.size() == 0) {
			initInvList();
		}

		setNoEditItem();
		// ���ݺ�
		// getBillCardPanel().getHeadItem("vreceiptcode").setEnabled(false);

		// �ջ���ַ
		UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem("vreceiveaddress")
				.getComponent();
		if (vreceiveaddress != null) {
			if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null) {
				// �ջ���ַ����
				((CustAddrRefModel) vreceiveaddress.getRefModel()).setCustId(getBillCardPanel()
						.getHeadItem("creceiptcustomerid").getValue());
			}
		}
		// ����δͨ��������״̬Ϊ�޸�
		int iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus").getValue());
		if (iStatus == BillStatus.NOPASS) {
			for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
				getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
			}
		}

		// by zxj
		// getBillCardPanel().getHeadItem("vreceiptcode").setEnabled(true);
		// �����޸�ǰ���ݺ�
		SaleOrderVO hvo = (SaleOrderVO) getVO(false);
		SaleorderHVO header = (SaleorderHVO) hvo.getParentVO();
		m_oldreceipt = header.getVreceiptcode();

		/** v5.3ȥ�������ֶ� * */
		/*
		 * if (hvo.getHeadVO().getNaccountperiod() != null &&
		 * hvo.getHeadVO().getNaccountperiod().doubleValue() < 0.0)
		 * getBillCardPanel().setHeadItem("naccountperiod", null);
		 */

		if (SO41.booleanValue()) {
			getBillCardPanel().setTailItem("coperatorid",
					getClientEnvironment().getUser().getPrimaryKey());
		}

		// ������ʼ��VO
		getBillCardTools().setOldsaleordervo(hvo);

		try {

			this.alInvs_bak = (ArrayList) nc.vo.scm.pub.smart.ObjectUtils
					.serializableClone(getBillCardPanel().alInvs);
			this.vRowATPStatus_bak = (Vector) nc.vo.scm.pub.smart.ObjectUtils
					.serializableClone(this.vRowATPStatus);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			this.vRowATPStatus_bak = this.vRowATPStatus;
			this.alInvs_bak = getBillCardPanel().alInvs;
			initInvList();

		}
		// this.alInvs_bak = (ArrayList)this.alInvs.clone();
		// this.vRowATPStatus_bak = (Vector)this.vRowATPStatus.clone();

		showCustManArInfo();

		// �����������
		getBillCardTools().clearCatheData();

		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH027"));

		// ���������ɫ����
		try {
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), getBillCardPanel().alInvs);
		} catch (Exception e) {

		}

		// �޸�ʱԭ���ֹ��޸Ĺ��۸���б�ɫ
		nc.ui.scm.pub.panel.SetColor.setErrorRowColor(getBillCardPanel(),
				getChangePriceRowFromCardUI());

		setCoopEditableByVo(saleorder);

	}

	/**
	 * ���ؿ�Ƭ������,�ֹ��޸Ĺ��ļ۸��е���š�
	 */
	public ArrayList<Integer> getChangePriceRowFromCardUI() {
		// ��ǰ��Ƭ��������
		int count = getBillCardPanel().getRowCount();
		ArrayList<Integer> aryrow = new ArrayList<Integer>();
		// У��ÿһ���Ƿ��ֹ��޸Ĺ��۸񣬴���һ���޸Ĺ��ļ�����
		for (int i = 0; i < count; i++) {
			if (getBillCardPanel().ifModifyPrice(i)) {
				aryrow.add(i);
			}
		}
		return aryrow;
	}

	/**
	 * ��λ��
	 * 
	 * �������ڣ�(2001-12-4 10:56:17)
	 * 
	 */
	protected void onFind() {

		num = getBillListPanel().getHeadTable().getSelectedRow();
		LocateDialog dlg = new LocateDialog(this, getBillListPanel().getHeadTable());
		dlg.showModal();
		int newnum = getBillListPanel().getHeadTable().getSelectedRow();
		if (newnum >= 0 && newnum < getBillListPanel().getHeadBillModel().getRowCount()) {
			BillEditEvent e = new BillEditEvent(this, num, newnum);
			bodyRowChange(e);
			num = getBillListPanel().getHeadTable().getSelectedRow();
		} else {
			getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(
					getBillListPanel().getHeadTable().getSelectedRow(),
					getBillListPanel().getHeadTable().getSelectedRow());
		}

	}

	/**
	 * �޶����ݡ�
	 * 
	 * �������ڣ�(2001-4-25 10:48:26) �޸����ڣ���2003-08-26�� �޸����ݣ��޶�ʱ����۸�����ֶ������û��޸�
	 * 
	 * �޸��ˣ�����
	 * 
	 */
	protected void onModification() {

		if (strShowState.equals("�б�")) { /*-=notranslate=-*/
			strShowState = "��Ƭ"; /*-=notranslate=-*/
			strState = "����"; /*-=notranslate=-*/
			switchInterface();
			num = getBillListPanel().getHeadTable().getSelectedRow();

			// ��������

			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num,
					"csaleid");

			SaleOrderVO saleorder = vocache.getSaleOrderVO(csaleid);
			loadCardData(saleorder);

			updateCacheVO();

			setButtons(getBillButtons());
			setBodyMenuItem();

		} else {
			updateCacheVO();
		}

		getBillCardPanel().setEnabled(true);

		// ���ݲ����ָ�ģ��ı༭״̬
		getBillCardTools().resumeBillItemEditToInit();

		strState = "�޶�"; /*-=notranslate=-*/

		getBillCardTools().setHeadRefLimit(strState);

		vRowATPStatus = new Vector();
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			vRowATPStatus.addElement(new UFBoolean(false));

			// ���ƶ๫˾�еı༭״̬
			ctlUIOnCconsignCorpChg(i);
		}

		if (alInvs == null) {
			initInvList();
		}

		setNoEditItem();

		// �����޶�ʱ���ÿɱ༭��
		setEditWhenModification();

		getBillCardTools().setCardPanelCellEditableByLargess(SO59.booleanValue());

		setNotReviseItems();

		// //��ʾ�޶�����
		// getBillCardPanel().showBodyTableCol("veditreason");
		// getBillCardPanel().getBodyItem("veditreason").setEnabled(true);

		SaleOrderVO ordoldvo = (SaleOrderVO) getVO(false);

		/** v5.3ȥ�������ֶ� * */
		/*
		 * if (ordoldvo.getHeadVO().getNaccountperiod() != null &&
		 * ordoldvo.getHeadVO().getNaccountperiod().doubleValue() < 0.0)
		 * getBillCardPanel().setHeadItem("naccountperiod", null);
		 */

		// ��Ʊ�ͻ�
		/** �п�Ʊ�������������޸� V502 yangcl zhongwei* */
		Object ntotalinvoicenumber;
		for (int i = 0, len = getBillCardPanel().getRowCount(); i < len; i++) {
			ntotalinvoicenumber = getBillCardPanel().getBodyValueAt(i, "ntotalinvoicenumber");
			if (ntotalinvoicenumber != null && ((UFDouble) ntotalinvoicenumber).doubleValue() > 0) {
				getBillCardPanel().getHeadItem("creceiptcorpid").setEnabled(false);
				break;
			}
		}

		// ������ʼ��VO
		getBillCardTools().setOldsaleordervo(ordoldvo);

		try {

			this.alInvs_bak = (ArrayList) nc.vo.scm.pub.smart.ObjectUtils.serializableClone(alInvs);
			this.vRowATPStatus_bak = (Vector) nc.vo.scm.pub.smart.ObjectUtils
					.serializableClone(this.vRowATPStatus);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			this.vRowATPStatus_bak = this.vRowATPStatus;
			this.alInvs_bak = this.alInvs;
			initInvList();

		}

		// �����������
		getBillCardTools().clearCatheData();

		showCustManArInfo();

		// ���������ɫ����
		try {
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), this.alInvs);
		} catch (Exception e) {

		}

		setButtonsState();

		updateUI();

	}

	/**
	 * �����޶�ʱ���ÿɱ༭��(2004-12-01 23:25:18)
	 * 
	 * @param
	 */
	private void setEditWhenModification() {

		// 1���������в��ɱ༭
		Object otemp = getBillCardPanel().getHeadItem("fstatus").getValue();
		if (otemp == null)
			otemp = "";

		getBillCardTools().setHeadEnable(false);

		getBillCardTools().resumeHeadItemsEditToInit(new String[] { "vreceiptcode", "dbilldate" });
		UFDouble dcathpay = getBillCardTools().getHeadUFDoubleValue("nreceiptcathmny");
		if ((dcathpay == null || dcathpay.doubleValue() == 0) && strState.equals("�޶�")
				&& isHeadCustCanbeModified()) {/*-=notranslate=-*/
			getBillCardTools().resumeHeadItemsEditToInit(new String[] { "ccustomerid" });
		}

		// Integer fstatus = (Integer)
		// getBillCardPanel().getHeadItem("fstatus").converType(otemp);�ܳ�ʤ�޸�
		Integer fstatus = SmartVODataUtils.getInteger(otemp);
		UFBoolean binvoicendflag = getBillCardTools().getHeadUFBooleanValue("binvoicendflag");

		// 2�� �����޶���Լ�������۶����н��� OR ���۶����н������ OR ���۶����п�Ʊ����
		if ((fstatus != null && fstatus.intValue() == BillStatus.FINISH)
				|| (binvoicendflag != null && binvoicendflag.booleanValue())) {
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				getBillCardTools().setRowEnable(i, false);
			}
		}

		String[] bodyItems = { "cinventorycode", "nitemdiscountrate", "ntaxrate",
				"noriginalcurtaxprice", "noriginalcurprice", "noriginalcurnetprice",
				"noriginalcurtaxnetprice", "noriginalcurmny", "noriginalcursummny",
				"noriginalcurtaxmny", "noriginalcurdiscountmny", "blargessflag", "norgqttaxprc",
				"norgqtprc", "norgqttaxnetprc", "norgqtnetprc", "cpriceitem",
				"cpriceitemtablename", "cpricepolicy", "cconsigncorp", "creccalbody",
				"crecwarehouse", "bdericttrans", "crowno", "cadvisecalbody", "cbodywarehousename",
				"ctransmodeid", "cprojectname", "cprojectphasename", "creceiptcorpname",
				"vreceiveaddress", "crecaddrnodename" };

		String[] bodyNoModify = { "cbatchid", "vfree0" };
		// getBillCardTools().setBodyItemEnable(bodyItems,true);
		getBillCardTools().resumeBillBodyItemsEdit(bodyItems);

		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			if (isHasBackwardDoc(i))
				getBillCardTools().setBodyCellsEdit(bodyNoModify, i, false);

			getBillCardTools().setBodyCellsEdit(bodyItems, i, true);
		}

		// ����
		boolean bedit_ccurrencytypeid_h = false;
		// ��Ʊ��λ
		boolean bedit_creceiptcorpid_h = false;
		// �ջ���λ
		boolean bedit_creceiptcustomerid_h = false;
		// �ջ���ַ
		boolean bedit_vreceiveaddress_h = false;
		// ��ע
		boolean bedit_vnote_h = false;
		// �Զ�����
		boolean bedit_vdef_h = false;

		boolean bedit_nexchangeotobrate_h = false;

		// 2�� �������ϵ������������״̬�����۶��������Խ����޶��������޶������ݣ�
		//
		// 1) ���۶���δ�տ� AND δ��Ʊ AND δ���������µ��޶���
		//
		// a) ��ͷ�����޶����ԣ����֡��ջ���λ���ջ��ص㡢�ջ���ַ����Ʊ��λ����ע���Զ����
		//
		// b) ��������޶����ԣ����ۡ��������������ڡ�����ʱ�䡢�ջ����ڡ��ջ�ʱ�䡢�б�ע����Ŀ��Ϣ���ջ���λ���ջ��������ջ���ַ���ջ��ص㡢
		// �Ƿ�۱����Ƿ������б�ע����Ŀ��Ϣ���Զ�����
		//
		// 2) ���۶������տ� OR �ѿ�ƱOR�ѽ��������µ��޶���
		//
		// a) ��ͷ�����޶����ԣ��ջ���λ���ջ��ص㡢�ջ���ַ����Ʊ��λ����ע���Զ����
		//
		// b) ��������޶����ԣ����ۡ��������������ڡ�����ʱ�䡢�ջ����ڡ��ջ�ʱ�䡢�б�ע����Ŀ��Ϣ���ջ���λ���ջ��������ջ���ַ���ջ��ص㡢
		// �Ƿ�۱����Ƿ������б�ע����Ŀ��Ϣ���Զ�����

		boolean ispay = false;
		UFDouble nreceiptcathmny = getBillCardTools().getHeadUFDoubleValue("nreceiptcathmny");
		if (nreceiptcathmny != null && nreceiptcathmny.doubleValue() > 0) {
			ispay = true;
		}
		UFDouble ntotalinvoicenumber = null;
		UFDouble ntotalbalancenumber = null;
		UFDouble totalreceiptnumber = null;
		UFBoolean bifinvoicefinish = null;

		UFDouble ntotalinventorynumber = null;

		UFDouble ntalbalancemny = null;
		UFDouble nnumber = null;

		UFDouble nsummny = null;

		String cpackunitid = null;

		String[] keybodys = new String[] {
				// ����
				"nnumber",
				// ��������
				"dconsigndate",
				// ����ʱ��
				"tconsigntime",
				// �ջ�����
				"ddeliverdate",
				// �ջ�ʱ��
				"tdelivertime",
				// �б�ע
				"frownote",
				// ��Ŀ
				"cprojectname",
				// ��Ŀ�׶�
				"cprojectphasename",
				// �ջ���λ
				"creceiptcorpname",
				// �ջ�����
				"creceiptareaname",
				// �ջ���ַ
				"vreceiveaddress",
				// �ջ��ص�
				"crecaddrnodename",
				// �Ƿ�۱�
				"bsafeprice",
				// �Ƿ���
				"breturnprofit",
				// �Զ�����
				"vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9",
				"vdef10", "vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17",
				"vdef18", "vdef19", "vdef20",
				// ��˰���
				"noriginalcurmny",
				// ��˰�ϼ�
				"noriginalcursummny",
				// �ۿ۶�
				"noriginaldiscountmny",
				// ˰��
				"ntaxrate"

		};

		String[] sNoEditItem = new String[] { "GGXX", "ct_code", "ctinvclass", "cpackunitname",
				"cunitname", "cquoteunit", "cconsigncorp", "cadvisecalbody", "cbodywarehousename",
				"cbomordercode", "creceiptareaname", "boosflag", "bsupplyflag", "cbatchid",
				"ccurrencytypename", "isappendant", "crecwarehouse", "bdericttrans", "creccalbody",
				"ntotalshouldoutnum", "ntotalreceivenumber", "ntotalinvoicenumber",
				"ntotalinventorynumber", "ntotalbalancenumber", "ntotalcostmny",
				"bifreceivefinish", "bifinventoryfinish", "frowstatus", "ntalplconsigmny",
				"ntaltransnum", "ntalbalancemny", "dlastconsigdate", "dlastoutdate",
				"dlastinvoicedt", "dlastpaydate", "cinvspec", "cinvtype", "narrangescornum",
				"narrangepoapplynum", "narrangetoornum", "norrangetoapplynum", "narrangemonum",
				"ntotlbalcostnum", "carrangepersonid", "nrushnum" };

		getBillCardTools().setBodyItemEnable(
				new String[] { "norgqttaxprc", "noriginalcurtaxprice", "norgqttaxnetprc",
						"noriginalcurtaxnetprice", "norgqtprc", "norginalcurprice", "norgqtnetprc",
						"noriginalcurnetprice", "npacknumber", "nquoteunitnum", "veditreason" },
				true);
		getBillCardTools().setBodyItemEnable(sNoEditItem, false);
		getBillCardTools().setBodyItemEnable(keybodys, true);

		boolean beditaddr = false;
		String[] keybodyfields = {
		// ��������
				"dconsigndate",
				// ����ʱ��
				"tconsigntime",
				// �ջ�����
				// "ddeliverdate",
				// �ջ�ʱ��
				// "tdelivertime",
				// �ջ���λ
				"creceiptcorpname",
				// �ջ�����
				"creceiptareaname",
				// �ջ���ַ
				"vreceiveaddress",
				// �ջ��ص�
				"crecaddrnodename",

		};

		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {

			getBillCardPanel().getBillModel().setCellEditable(i, "veditreason", true);

			// ����nnumber
			nnumber = getBillCardTools().getBodyUFDoubleValue(i, "nnumber");
			nnumber = nnumber == null ? SoVoConst.duf0 : nnumber;

			// �ۼƿ�Ʊ��
			ntotalinvoicenumber = getBillCardTools().getBodyUFDoubleValue(i, "ntotalinvoicenumber");
			ntotalinvoicenumber = ntotalinvoicenumber == null ? SoVoConst.duf0
					: ntotalinvoicenumber;

			// �ۼƿ�Ʊ����
			bifinvoicefinish = getBillCardTools().getBodyUFBooleanValue(i, "bifinvoicefinish");
			bifinvoicefinish = bifinvoicefinish == null ? SoVoConst.buffalse : bifinvoicefinish;

			// �ۼƽ�������ntotalbalancenumber
			ntotalbalancenumber = getBillCardTools().getBodyUFDoubleValue(i, "ntotalbalancenumber");
			ntotalbalancenumber = ntotalbalancenumber == null ? SoVoConst.duf0
					: ntotalbalancenumber;

			// �ۼƷ�����
			totalreceiptnumber = getBillCardTools().getBodyUFDoubleValue(i, "ntotalreceiptnumber");
			totalreceiptnumber = totalreceiptnumber == null ? SoVoConst.duf0 : totalreceiptnumber;

			// �ۼƳ�������
			ntotalinventorynumber = getBillCardTools().getBodyUFDoubleValue(i,
					"ntotalinventorynumber");
			ntotalinventorynumber = ntotalinventorynumber == null ? SoVoConst.duf0
					: ntotalinventorynumber;

			if (nnumber.doubleValue() != 0 && totalreceiptnumber.doubleValue() == 0
					&& ntotalinventorynumber.doubleValue() == 0 && !beditaddr) {
				beditaddr = true;
			}

			// �ۼƽ�����
			ntalbalancemny = getBillCardTools().getBodyUFDoubleValue(i, "ntalbalancemny");
			ntalbalancemny = ntalbalancemny == null ? SoVoConst.duf0 : ntalbalancemny;

			// b���� ��˰�ϼ�
			nsummny = getBillCardTools().getBodyUFDoubleValue(i, "nsummny");
			nsummny = nsummny == null ? SoVoConst.duf0 : nsummny;

			cpackunitid = getBillCardTools().getBodyStringValue(i, "cpackunitid");

			// 1�� �����޶���Լ�������۶����н��� OR ���۶����н������ OR ���۶����п�Ʊ����
			if ((nnumber.doubleValue() != 0 && ntotalinvoicenumber.abs().compareTo(nnumber.abs()) >= 0)
					|| bifinvoicefinish.booleanValue()
					|| (nnumber.doubleValue() != 0 && ntotalbalancenumber.abs().compareTo(
							nnumber.abs()) >= 0)
					|| (ntalbalancemny.doubleValue() != 0 && ntalbalancemny.abs().compareTo(
							nsummny.abs()) >= 0)) {
				getBillCardTools().setRowEnable(i, false);

				// ���۶���δ�տ� ANDδ��Ʊ AND δ���������£�
			} else if (!ispay && ntotalinvoicenumber.compareTo(SoVoConst.duf0) == 0
					&& ntotalbalancenumber.compareTo(SoVoConst.duf0) == 0
					&& ntalbalancemny.compareTo(SoVoConst.duf0) == 0) {
				// ��ͷ�����޶����ԣ����֡��ջ���λ���ջ��ص㡢�ջ���ַ����Ʊ��λ����ע���Զ����
				if (!bedit_ccurrencytypeid_h)
					bedit_ccurrencytypeid_h = true;
				if (!bedit_nexchangeotobrate_h)
					bedit_nexchangeotobrate_h = true;

				// ��Ʊ��λ
				if (!bedit_creceiptcorpid_h)
					bedit_creceiptcorpid_h = true;
				// �ջ���λ
				if (!bedit_creceiptcustomerid_h)
					bedit_creceiptcustomerid_h = true;
				// �ջ���ַ
				if (!bedit_vreceiveaddress_h)
					bedit_vreceiveaddress_h = true;
				// ��ע
				if (!bedit_vnote_h)
					bedit_vreceiveaddress_h = true;
				// �Զ�����
				if (!bedit_vdef_h)
					bedit_vdef_h = true;
				// ��������޶����ԣ����ۡ��������������ڡ�����ʱ�䡢�ջ����ڡ��ջ�ʱ�䡢�б�ע����Ŀ��Ϣ���ջ���λ��
				// �ջ��������ջ���ַ���ջ��ص㡢�Ƿ�۱����Ƿ������б�ע����Ŀ��Ϣ���Զ�����

				if (SA_02.booleanValue()) {

					if (getBillCardPanel().getBodyItem("norgqttaxprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurtaxprice", true);

					if (getBillCardPanel().getBodyItem("norgqttaxnetprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxnetprc",
								true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurtaxnetprice", true);

				} else {

					if (getBillCardPanel().getBodyItem("norgqtprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqtprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i, "norginalcurprice",
								true);

					if (getBillCardPanel().getBodyItem("norgqtnetprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqtnetprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurnetprice", true);

				}
				if (SoVoTools.isEmptyString(cpackunitid))
					getBillCardPanel().getBillModel().setCellEditable(i, "npacknumber", false);
				else
					getBillCardPanel().getBillModel().setCellEditable(i, "npacknumber", true);
				getBillCardTools().setRowEnable(i, keybodys, true);

				if (getBillCardPanel().getBodyItem("nquoteunitnum").isShow()) {
					getBillCardPanel().getBillModel().setCellEditable(i, "nquoteunitnum", true);
					getBillCardPanel().getBillModel().setCellEditable(i, "nnumber", false);
				} else {
					getBillCardPanel().getBillModel().setCellEditable(i, "nquoteunitnum", false);
					getBillCardPanel().getBillModel().setCellEditable(i, "nnumber", true);
				}

			} else if (ispay || ntotalinvoicenumber.compareTo(SoVoConst.duf0) != 0
					|| ntotalbalancenumber.compareTo(SoVoConst.duf0) != 0
					|| ntalbalancemny.compareTo(SoVoConst.duf0) != 0) {
				// ���۶������տ� OR �ѿ�ƱOR�ѽ��������µ��޶���

				// ��ͷ�����޶����ԣ��ջ���λ���ջ��ص㡢�ջ���ַ����Ʊ��λ����ע���Զ����
				// ��Ʊ��λ
				if (!bedit_creceiptcorpid_h)
					bedit_creceiptcorpid_h = true;
				// �ջ���λ
				if (!bedit_creceiptcustomerid_h)
					bedit_creceiptcustomerid_h = true;
				// �ջ���ַ
				if (!bedit_vreceiveaddress_h)
					bedit_vreceiveaddress_h = true;
				// ��ע
				if (!bedit_vnote_h)
					bedit_vreceiveaddress_h = true;
				// �Զ�����
				if (!bedit_vdef_h)
					bedit_vdef_h = true;

				// ����
				// ���ۡ��������������ڡ�����ʱ�䡢�ջ����ڡ��ջ�ʱ�䡢�б�ע����Ŀ��Ϣ���ջ���λ��
				// �ջ��������ջ���ַ���ջ��ص㡢�Ƿ�۱����Ƿ������б�ע����Ŀ��Ϣ���Զ�����
				if (SA_02.booleanValue()) {

					if (getBillCardPanel().getBodyItem("norgqttaxprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurtaxprice", true);

					if (getBillCardPanel().getBodyItem("norgqttaxnetprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxnetprc",
								true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurtaxnetprice", true);

				} else {

					if (getBillCardPanel().getBodyItem("norgqtprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqtprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i, "norginalcurprice",
								true);

					if (getBillCardPanel().getBodyItem("norgqtnetprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqtnetprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurnetprice", true);

				}

				if (SoVoTools.isEmptyString(cpackunitid))
					getBillCardPanel().getBillModel().setCellEditable(i, "npacknumber", false);
				else
					getBillCardPanel().getBillModel().setCellEditable(i, "npacknumber", true);
				getBillCardTools().setRowEnable(i, keybodys, true);

				if (getBillCardPanel().getBodyItem("nquoteunitnum").isShow()) {
					getBillCardPanel().getBillModel().setCellEditable(i, "nquoteunitnum", true);
					getBillCardPanel().getBillModel().setCellEditable(i, "nnumber", false);
				} else {
					getBillCardPanel().getBillModel().setCellEditable(i, "nquoteunitnum", false);
					getBillCardPanel().getBillModel().setCellEditable(i, "nnumber", true);
				}
			}

			getBillCardTools().setRowEnable(i, keybodyfields, beditaddr);
			getBillCardTools().setRowEnable(i, sNoEditItem, false);

		}

		// ��ͷ

		getBillCardPanel().getHeadItem("ccurrencytypeid").setEnabled(bedit_ccurrencytypeid_h);
		getBillCardPanel().getHeadItem("ccurrencytypeid").setEdit(bedit_ccurrencytypeid_h);

		getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(bedit_nexchangeotobrate_h);
		getBillCardPanel().getHeadItem("nexchangeotobrate").setEdit(bedit_nexchangeotobrate_h);

		getBillCardPanel().getHeadItem("creceiptcorpid").setEnabled(bedit_creceiptcorpid_h);
		getBillCardPanel().getHeadItem("creceiptcorpid").setEdit(bedit_creceiptcorpid_h);

		getBillCardPanel().getHeadItem("creceiptcustomerid").setEnabled(bedit_creceiptcustomerid_h);
		getBillCardPanel().getHeadItem("creceiptcustomerid").setEdit(bedit_creceiptcustomerid_h);

		getBillCardPanel().getHeadItem("vreceiveaddress").setEnabled(bedit_vreceiveaddress_h);
		getBillCardPanel().getHeadItem("vreceiveaddress").setEdit(bedit_vreceiveaddress_h);

		getBillCardPanel().getHeadItem("vnote").setEnabled(bedit_vnote_h);
		getBillCardPanel().getHeadItem("vnote").setEdit(bedit_vnote_h);

		getBillCardPanel().getHeadItem("creceiptcustomerid").setEnabled(beditaddr);
		getBillCardPanel().getHeadItem("creceiptcustomerid").setEdit(beditaddr);

		getBillCardPanel().getHeadItem("vreceiveaddress").setEnabled(beditaddr);
		getBillCardPanel().getHeadItem("vreceiveaddress").setEdit(beditaddr);

		String[] keysh = new String[] { "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6",
				"vdef7", "vdef8", "vdef9", "vdef10", "vdef11", "vdef12", "vdef13", "vdef14",
				"vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20" };
		getBillCardTools().setHeadEnable(keysh, bedit_vdef_h);

		// ��ʾ�޶�����
		getBillCardPanel().showBodyTableCol("veditreason");
		getBillCardPanel().getBodyItem("veditreason").setEnabled(true);

		// �ۿ۶�
		getBillCardTools().setBodyItemEnable(
				new String[] { "noriginalcurdiscountmny", "ndiscountmny" }, false);

	}

	/**
	 * ������������ģ���в����޶�����
	 */
	private void setNotReviseItems() {

		BillItem[] bHeaditems = getBillCardPanel().getHeadItems();

		// �����޶�����Ϊ���ܱ༭״̬
		for (int i = 0; i < bHeaditems.length; i++) {

			if (!bHeaditems[i].isM_bReviseFlag())
				bHeaditems[i].setEnabled(false);
			else if (bHeaditems[i].getKey() != null && bHeaditems[i].getKey().startsWith("vdef")) {
				bHeaditems[i].setEnabled(true);
			}

		}
		BillItem[] bBodyitems = getBillCardPanel().getBodyItems();
		for (int i = 0; i < bBodyitems.length; i++) {
			if (!bBodyitems[i].isM_bReviseFlag()) {
				for (int j = 0, jLen = getBillCardPanel().getRowCount(); j < jLen; j++) {
					getBillCardPanel().setCellEditable(j, bBodyitems[i].getKey(), false);
				}

			} else if (bBodyitems[i].getKey().startsWith("vdef")) {
				for (int j = 0, jLen = getBillCardPanel().getRowCount(); j < jLen; j++) {
					getBillCardPanel().setCellEditable(j, bBodyitems[i].getKey(), true);
				}
			}
		}

	}

	/**
	 * ���۶�����ʽЭͬ�ɹ�����
	 * 
	 * @param bo
	 * @throws Exception
	 */
	protected void onCoRefPo(ButtonObject bo) throws Exception {
		// SaleOrderBO_Client.setSoRefferedByPO(new
		// String[]{"1001D41000000000181P"},false);
		switchToCardPanel();
		PfUtilClient.childButtonClicked(bo, getCorpPrimaryKey(), getNodeCode(),
				getClientEnvironment().getUser().getPrimaryKey(), getBillType(), this);
		vRowATPStatus = new Vector();
		boolean bisCalculate = getBillCardPanel().getBillModel().isNeedCalculate();
		// AggregatedValueObject[] pursueVOs=null;
		SaleOrderVO[] saleOrderVOs = null;
		try {
			if (PfUtilClient.isCloseOK()) {

				saleOrderVOs = ((PfUtilClient.getRetVos() == null) || (((Object[]) PfUtilClient
						.getRetVos()).length > 0 && !(((Object[]) PfUtilClient.getRetVos())[0] instanceof SaleOrderVO))) ? null
						: (SaleOrderVO[]) PfUtilClient.getRetVos();

			} else {
				return;

			}

			if (saleOrderVOs != null && saleOrderVOs.length > 0) {
				coBusiType = saleOrderVOs[0].getBizTypeid();
				coPoOrderId = saleOrderVOs[0].getHeadVO().getCoopPoId();
				coPoTs = saleOrderVOs[0].getHeadVO().getCoopPoTs();
				// CHG21TO30 voChanger=new CHG21TO30();
				// saleOrderVOs=new SaleOrderVO[saleOrderVOs.length];
				// saleOrderVOs=(SaleOrderVO[])voChanger.retChangeBusiVOs(saleOrderVOs,
				// saleOrderVOs);
				binitOnNewByOther = true;
				// saleOrderVOs[0].getHeadVO().getAttributeValue("ccooppohid");
				onNewByOther(saleOrderVOs);

				for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
					vRowATPStatus.add(new UFBoolean(false));

				setCoopEditableByVo(saleOrderVOs[0]);
			} else {
				return;
			}

			getBillCardPanel().showCustManArInfo();
			getBillCardPanel().setHeadItem("cbiztype", coBusiType);
			getBillCardPanel().setBusiType(coBusiType);

			// ���������ɫ����
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), this.getBillCardPanel().alInvs);
		} catch (Exception e) {
			throw e;
		} finally {
			getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
			if (bisCalculate)
				getBillCardPanel().getBillModel().reCalcurateAll();
			binitOnNewByOther = false;

		}

		// �����������
		getBillCardTools().clearCatheData();

	}

	/**
	 * ����Эͬ���ݵĿɲ�����
	 * 
	 * @param saleorder
	 */
	private void setCoopEditableByVo(SaleOrderVO saleorder) {
		if (saleorder.getHeadVO().getAttributeValue("ccooppohid") != null) {
			getBillCardPanel().getHeadItem("ccustomerid").setEdit(false);
		}
	}

	/**
	 * @return ��Ƭ���б�������Ƿ���ѡ����
	 */
	private boolean ifHasData() {
		boolean flag = true;
		if (strShowState.equals("�б�")) {
			if (getBillListPanel().getHeadTable().getSelectedRow() == -1)
				flag = false;
		} else
			flag = getBillCardPanel().getHeadItem("csaleid").getValueObject() != null;
		return flag;
	}

	private String getCurrentOrderId() {
		String csaleid = null;
		if (strShowState.equals("�б�")) {
			csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(selectRow,
					"csaleid");
		} else {
			csaleid = getBillCardPanel().getHeadItem("csaleid").getValue();
		}
		return csaleid;

	}

	private SaleOrderVO getCurrentSaleOrderVO() {
		return vocache.getSaleOrderVO(getCurrentOrderId());

	}

	/**
	 * ���۶����Ʋɹ�����
	 */
	protected void onCoPushPo() {
		try {
			String sSaleid = getCurrentOrderId();
			if (sSaleid != null) {
				SaleOrderVO saleorder = vocache.getSaleOrderVO(sSaleid);
				if (saleorder.getHeadVO().getAttributeValue("bcooptopo") != null
						&& ((UFBoolean) saleorder.getHeadVO().getAttributeValue("bcooptopo"))
								.booleanValue()) {
					showErrorMessage("�����۶�����Эͬ���ɲɹ�������");
					return;
				}
				if (saleorder.isCoopped()) {
					showErrorMessage("�����۶�������Эͬ�ɹ�������");
					return;

				}

				if (BillStatus.AUDIT != saleorder.getHeadVO().getFstatus()
						|| saleorder.hasRowClosed()) {
					showErrorMessage("ֻ����������δ���йرյ����۶������ܽ��д˲�����");
					return;

				}

				PfUtilClient.processActionNoSendMessage(this, "SALETOPO", "21",
						getClientEnvironment().getDate().toString(), saleorder, null, null, null);
				// SaleOrderBO_Client.setSoRefferedByPO(new String[] { sSaleid
				// }, true);
				// saleorder.getHeadVO().setAttributeValue("bcooptopo", new
				// UFBoolean(true));
				// getBillCardPanel().setHeadItem("bcooptopo", new
				// UFBoolean(true));
				BillTools.reLoadBillState(this, getClient());
				getCurrentSaleOrderVO().getHeadVO().setAttributeValue("bcooptopo",
						new UFBoolean(true));
				getCurrentSaleOrderVO().getHeadVO().setAttributeValue("ccooppohid",
						getBillCardPanel().getHeadItem("ccooppohid").getValue());
				setBtnsByBillState(BillStatus.AUDIT);

				// updateCacheVO();
				showHintMessage("�����۶����ѳɹ�Эͬ���ɲɹ�����");
			} else {
				showErrorMessage("��ѡ���¼���ٲ�����");
				return;
			}
		} catch (Exception e) {
			SCMEnv.error(e);
			showErrorMessage(e.getMessage());
		}

	}

	/**
	 * 
	 * ��������ÿ�Ƭ�����±����е��һ��˵���Ŀ�ļ���
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @return �һ��˵�����
	 *         <p>
	 * @author duy
	 * @time 2008-12-11 ����09:42:55
	 */
	private java.util.Map<String, java.util.ArrayList<UIMenuItem>> getBodyMenuItems() {
		if (m_bodyMenuItems == null && boLine != null) {
			String[] tabCodes = getBillCardPanel().getBillData().getTableCodes(BillItem.BODY);
			m_bodyMenuItems = new java.util.HashMap<String, java.util.ArrayList<UIMenuItem>>();
			ButtonObject[] lineButtons = boLine.getChildButtonGroup();

			for (String tabCode : tabCodes) {
				java.util.ArrayList<UIMenuItem> items = new java.util.ArrayList<UIMenuItem>(
						lineButtons.length);
				for (ButtonObject lineButton : lineButtons) {
					SoUIMenuItem menuItem = new SoUIMenuItem(lineButton);
					items.add(menuItem);
				}
				m_bodyMenuItems.put(tabCode, items);
			}
		}
		return m_bodyMenuItems;
	}

	/**
	 * 
	 * ���������ñ���˵���
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author duy
	 * @time 2008-12-11 ����11:28:33
	 */
	private void setBodyMenuItem() {
		String[] tabCodes = getBillCardPanel().getBillData().getTableCodes(BillItem.BODY);
		java.util.Map<String, java.util.ArrayList<UIMenuItem>> menuItems = getBodyMenuItems();
		for (String tabCode : tabCodes) {
			java.util.ArrayList<UIMenuItem> items = menuItems.get(tabCode);
			UIMenuItem[] menus = items.toArray(new UIMenuItem[items.size()]);
			getBillCardPanel().setBodyMenu(tabCode, menus);
			getBillCardPanel().addMenuListener(menus);
		}
	}

	/**
	 * 
	 * ���������ñ���˵����״̬
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author duy
	 * @time 2008-12-11 ����10:32:27
	 */
	private void setBodyMenuStatus() {
		String[] tabCodes = getBillCardPanel().getBillData().getTableCodes(BillItem.BODY);
		for (String tabCode : tabCodes)
			getBillCardPanel().setBodyMenuShow(tabCode, boLine.isEnabled());

		if (!boLine.isEnabled())
			return;

		for (String tabCode : tabCodes) {
			UIMenuItem[] menuItems = getBillCardPanel().getBodyMenuItems(tabCode);
			if (menuItems != null) {
				for (UIMenuItem menuItem : menuItems) {
					if (menuItem instanceof SoUIMenuItem) {
						((SoUIMenuItem) menuItem).setEnabled(((SoUIMenuItem) menuItem)
								.getButtonObject().isEnabled());
						menuItem.updateUI();
					}
				}
			}
		}

		getBillCardPanel().getAddLineMenuItem().setEnabled(boAddLine.isEnabled());
		getBillCardPanel().getDelLineMenuItem().setEnabled(boDelLine.isEnabled());
		getBillCardPanel().getPasteLineMenuItem().setEnabled(boPasteLine.isEnabled());
		getBillCardPanel().getCopyLineMenuItem().setEnabled(boCopyLine.isEnabled());
		getBillCardPanel().getInsertLineMenuItem().setEnabled(boAddLine.isEnabled());
		getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(boPasteLine.isEnabled());
	}

	protected void switchToCardPanel() {
		// �л�����Ƭ
		if (strShowState.equals("�б�")) {
			strShowState = "��Ƭ";
			strState = "����";
			switchInterface();
			setButtons(getBillButtons());
			setBodyMenuItem();
			showCustManArInfo();
			updateUI();
		}

	}

	/**
	 * �������ݡ�
	 * 
	 * �������ڣ�(2001-4-21 14:11:14)
	 * 
	 */
	protected void onNew(ButtonObject bo) throws Exception {

		// �л�����Ƭ
		if (strShowState.equals("�б�")) {
			strShowState = "��Ƭ";
			strState = "����";
			switchInterface();
			setButtons(getBillButtons());
			showCustManArInfo();
			updateUI();
		}

		// ���۲���ѯ�ۣ�SA15--N,�ָ�ģ���Ĭ��ֵ
		if (!SA_15.booleanValue())
			getBillCardTools().resumeBillBodyItemsEdit(
					getBillCardTools().getSaleOrderItems_Price_Original());

		PfUtilClient.childButtonClicked(bo, getCorpPrimaryKey(), getNodeCode(),
				getClientEnvironment().getUser().getPrimaryKey(), getBillType(), this);
		vRowATPStatus = new Vector();

		boolean bisCalculate = getBillCardPanel().getBillModel().isNeedCalculate();
		getBillCardPanel().getBillModel().setNeedCalculate(false);

		try {
			if (nc.ui.pub.pf.PfUtilClient.makeFlag) {
				onNewBySelf();
			} else {
				if (PfUtilClient.isCloseOK()) {
					binitOnNewByOther = true;
					onNewByOther(PfUtilClient.getRetVos());
					// ������״̬
					for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
						vRowATPStatus.add(new UFBoolean(false));
				}
			}
			getBillCardPanel().showCustManArInfo();

			// ���������ɫ����
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), this.getBillCardPanel().alInvs);

		} catch (Exception e) {
			throw e;
		} finally {
			getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
			if (bisCalculate)
				getBillCardPanel().getBillModel().reCalcurateAll();
			binitOnNewByOther = false;

		}

		// ���ñ���˵�
		setBodyMenuItem();

		// �����������
		getBillCardTools().clearCatheData();
		
		// ���ñ�ͷ��ͬ���������б��������ʾ
		// add by river for 2012-07-18
		// start ..
		if(getBillCardPanel().getHeadItem("contracttype") != null) {
			if(PfUtilClient.getRetVos() != null && PfUtilClient.getRetVos().length > 0) {
				if((Integer) PfUtilClient.getRetVos()[0].getParentVO().getAttributeValue("contracttype") == 10)
					((UIComboBox) getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("�ֻ���ͬ");
				else if((Integer) PfUtilClient.getRetVos()[0].getParentVO().getAttributeValue("contracttype") == 20)
					((UIComboBox) getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("������ͬ");
			}
		}
		
		// .. end
	}

	/**
	 * ͨ�����������������ݡ�
	 * 
	 * �������ڣ�(2001-4-21 14:11:14)
	 * 
	 */
	protected void onNewByOther(AggregatedValueObject[] saleVOs) throws Exception {
		if (saleVOs == null || saleVOs.length == 0)
			return;

		// ���ϲ�������
		if (!checkSourceComb(saleVOs))
			return;
		// ����ϲ�����Դҵ��VO
		// long s = System.currentTimeMillis();
		// SCMEnv.out("onNewByOther1��ʼ...");

		SaleOrderVO saleVO = new SaleOrderVO();
		saleVO.setParentVO(saleVOs[0].getParentVO());

		saleVO.getHeadVO().setCapproveid(null);

		Vector vecTemp = new Vector();
		for (int i = 0; i < saleVOs.length; i++) {
			for (int j = 0; j < saleVOs[i].getChildrenVO().length; j++) {
				vecTemp.addElement(saleVOs[i].getChildrenVO()[j]);
			}
		}
		SaleorderBVO[] aryChildren = new SaleorderBVO[vecTemp.size()];
		if (vecTemp.size() > 0)
			vecTemp.copyInto(aryChildren);
		saleVO.setChildrenVO(aryChildren);
		// ������������
		strState = "����";
		getBillCardPanel().addNew();
		getBillCardPanel().setEnabled(true);

		getBillCardTools().setHeadRefLimit(strState);

		// �������־��ȱ仯
		Object oCurrency = saleVO.getChildrenVO()[0].getAttributeValue("ccurrencytypeid");
		try {
			if (oCurrency == null || oCurrency.toString().length() == 0) {
				for (int i = 0; i < saleVO.getChildrenVO().length; i++) {
					saleVO.getChildrenVO()[i].setAttributeValue("ccurrencytypeid", CurrParamQuery
							.getInstance().getLocalCurrPK(getCorpPrimaryKey()));
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}
		oCurrency = saleVO.getChildrenVO()[0].getAttributeValue("ccurrencytypeid");
		if (oCurrency != null && oCurrency.toString().length() != 0) {
			getBillCardPanel().setPanelByCurrency(oCurrency.toString());
		}

		// ���ӵ����к�
		nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(saleVO, SaleBillType.SaleOrder,
				getBillCardPanel().getRowNoItemKey());

		// ���滺���ʼ��VO
		this.initAvo = saleVO;

		// ��������
		getBillCardPanel().setBillValueVO(saleVO);

		// ���ع�ʽ
		BillItem[] bms = getBillCardPanel().getBillModel().getBodyItems();
		for (int i = 0; i < bms.length; i++) {
			if (bms[i].getLoadFormula() != null && bms[i].getLoadFormula().length > 0) {
				getBillCardPanel().getBillModel().execLoadFormulasByKey(bms[i].getKey());
			}
		}
		// getBillCardPanel().getBillModel().execLoadFormula();

		setDefaultData(false);
		
		// ����Ĭ������
		ArrayList formulaslist = new ArrayList();
		// ��Ʊ��λ
		String creceiptcorpid = getBillCardPanel().getHeadItem("creceiptcorpid").getValue();
		if (creceiptcorpid == null || creceiptcorpid.trim().length() <= 0)
			formulaslist
					.add("creceiptcorpid->getColValue(bd_cumandoc,pk_cusmandoc2,pk_cumandoc,ccustomerid)");
		// �ջ���λ
		String creceiptcustomerid = getBillCardPanel().getHeadItem("creceiptcustomerid") == null ? null
				: getBillCardPanel().getHeadItem("creceiptcustomerid").getValue();
		if (creceiptcustomerid == null || creceiptcustomerid.trim().length() <= 0)
			formulaslist
					.add("creceiptcustomerid->getColValue(bd_cumandoc,pk_cusmandoc3,pk_cumandoc,ccustomerid)");
		// ����
		String cdeptid = getBillCardPanel().getHeadItem("cdeptid").getValue();
		if (cdeptid == null || cdeptid.trim().length() <= 0)
			formulaslist
					.add("cdeptid->getColValue(bd_cumandoc,pk_respdept1,pk_cumandoc,ccustomerid)");
		// ҵ��Ա
		String cemployeeid = getBillCardPanel().getHeadItem("cemployeeid").getValue();
		if (cemployeeid == null || cemployeeid.trim().length() <= 0)
			formulaslist
					.add("cemployeeid->getColValue(bd_cumandoc,pk_resppsn1,pk_cumandoc,ccustomerid)");
		// ��������
		UFDouble ndiscountrate = getBillCardTools().getHeadUFDoubleValue("ndiscountrate");
		if (ndiscountrate == null)
			formulaslist
					.add("ndiscountrate->getColValue(bd_cumandoc,discountrate,pk_cumandoc,ccustomerid)");

		// String ctransmodeid =
		// getBillCardPanel().getHeadItem("ctransmodeid").getValue();
		// if (ctransmodeid == null || ctransmodeid.trim().length() <= 0)
		// formulaslist.add("ctransmodeid->getColValue(bd_cumandoc,pk_sendtype,pk_cumandoc,ccustomerid)");

		// Ĭ�Ͻ��ױ���
		String ccurrencytypeid = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
		if (ccurrencytypeid == null || ccurrencytypeid.trim().length() <= 0)
			formulaslist
					.add("ccurrencytypeid->getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,ccustomerid)");
		// �ո���Э��
		String ctermprotocolid = getBillCardPanel().getHeadItem("ctermprotocolid").getValue();
		if (ctermprotocolid == null || ctermprotocolid.trim().length() <= 0)
			formulaslist
					.add("ctermprotocolid->getColValue(bd_cumandoc,pk_payterm,pk_cumandoc,ccustomerid)");
		// �����֯
		String ccalbodyid = getBillCardPanel().getHeadItem("ccalbodyid").getValue();
		if (ccalbodyid == null || ccalbodyid.trim().length() <= 0)
			formulaslist
					.add("ccalbodyid->getColValue(bd_cumandoc,pk_calbody,pk_cumandoc,ccustomerid)");
		// ������֯
		String csalecorpid = getBillCardPanel().getHeadItem("csalecorpid").getValue();
		if (csalecorpid == null || csalecorpid.trim().length() <= 0)
			formulaslist
					.add("csalecorpid->getColValue(bd_cumandoc,pk_salestru,pk_cumandoc,ccustomerid)");

		/** v5.3ȥ�������ֶ� * */
		/*
		 * formulaslist
		 * .add("naccountperiod->getColValue(bd_cumandoc,acclimit,pk_cumandoc,ccustomerid)");
		 */

		// �ͻ���������
		String ccustbasid = getBillCardPanel().getHeadItem("ccustbasid").getValue();
		if (ccustbasid == null || ccustbasid.trim().length() <= 0)
			formulaslist
					.add("ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)");

		// ɢ����־
		UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue("bfreecustflag");
		if (bfreecustflag == null)
			formulaslist
					.add("bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,ccustbasid)");

		// Ԥ�տ����
		Object prepaidratio = getBillCardPanel().getHeadItem("npreceiverate").getValueObject();
		if (prepaidratio == null || prepaidratio.toString().trim().length() == 0) {
			formulaslist
					.add("npreceiverate->getColValue(bd_cumandoc,prepaidratio,pk_cumandoc,ccustomerid)");
		}

		if (formulaslist.size() > 0)
			getBillCardPanel().getBillData().execHeadFormulas(
					(String[]) formulaslist.toArray(new String[formulaslist.size()]));

		// ��δ�����ջ���λ����ȡ�ͻ�
		if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null
				&& (getBillCardPanel().getHeadItem("creceiptcustomerid").getValue() == null || getBillCardPanel()
						.getHeadItem("creceiptcustomerid").getValue().length() <= 0)) {
			getBillCardPanel().getHeadItem("creceiptcustomerid").setValue(
					getBillCardPanel().getHeadItem("ccustomerid").getValue());
		}
		// ��δ���ÿ�Ʊ��λ����ȡ�ͻ�
		if (getBillCardPanel().getHeadItem("creceiptcorpid").getValue() == null
				|| getBillCardPanel().getHeadItem("creceiptcorpid").getValue().length() <= 0) {
			getBillCardPanel().getHeadItem("creceiptcorpid").setValue(
					getBillCardPanel().getHeadItem("ccustomerid").getValue());
		}

		String[] bodyformulas = {
				"cinvbasdocid->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)",
				"laborflag->getColValue(bd_invbasdoc,laborflag,pk_invbasdoc,cinvbasdocid)",
				"discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)",
				"cprolineid->getColValue(bd_invbasdoc,pk_prodline,pk_invbasdoc,cinvbasdocid)",
				"cprolinename->getColValue(bd_prodline,prodlinename,pk_prodline,cprolineid)",
				"nreturntaxrate->getColValue(bd_invmandoc,expaybacktax,pk_invmandoc,cinventoryid)",
				"ct_name->getColValue(ct_manage,ct_name,pk_ct_manage,csourcebillid)" };

		getBillCardPanel().getBillModel().execFormulas(bodyformulas);

		getBillCardTools().setDefaultValueByTemplate(0, -1);

		if (getSouceBillType().equals(SaleBillType.SoContract)
				|| getSouceBillType().equals(SaleBillType.SoInitContract)) {

			if (getBillCardPanel().getHeadItem("creceiptcorpid").getValue() == null
					|| getBillCardPanel().getHeadItem("creceiptcorpid").getValue().length() <= 0) {
				getBillCardPanel().getHeadItem("creceiptcorpid").setValue(
						getBillCardPanel().getHeadItem("ccustomerid").getValue());
			}

			// ��ͬ���������
			String[] bodyFormula = new String[1];
			bodyFormula[0] = "ctinvclass->getColValue(bd_invcl,invclassname,pk_invcl,ctinvclassid)";

			getBillCardPanel().getBillModel().execFormulas(bodyFormula);

			//
			getBillCardPanel().afterCcalbodyidEdit(null);
			getBillCardPanel().afterCreceiptcorpEdit(null);
		}
		// ���۵�
		else if (getSouceBillType().equals(SaleBillType.SaleQuotation)) {

			if (getBillCardPanel().getHeadItem("creceiptcorpid").getValue() == null
					|| getBillCardPanel().getHeadItem("creceiptcorpid").getValue().length() <= 0) {
				getBillCardPanel().getHeadItem("creceiptcorpid").setValue(
						getBillCardPanel().getHeadItem("ccustomerid").getValue());
			}

			// �ָ����۵��ϵĲ���
			if (saleVO.getHeadVO().getCdeptid() != null)
				getBillCardPanel().getHeadItem("cdeptid").setValue(saleVO.getHeadVO().getCdeptid());

			getBillCardPanel().afterCcalbodyidEdit(null);
			getBillCardPanel().afterCreceiptcorpEdit(null);
			// ���㸨������˰����,��������˰����
			// BillTools.calcViaPriceAll(getBillCardPanel().getBillModel(),getBillCardPanel().getBillType());
		}
		// ���ת����
		else if (getSouceBillType().equals("4H") || getSouceBillType().equals("42")) {
			//
			getBillCardPanel().getHeadItem("ccalbodyid").setEnabled(false);
			getBillCardPanel().getBodyItem("cadvisecalbody").setEnabled(false);
			getBillCardPanel().getBodyItem("cbodywarehousename").setEnabled(false);

			if (getBillCardPanel().getHeadItem("creceiptcorpid").getValue() == null
					|| getBillCardPanel().getHeadItem("creceiptcorpid").getValue().length() <= 0) {
				getBillCardPanel().getHeadItem("creceiptcorpid").setValue(
						getBillCardPanel().getHeadItem("ccustomerid").getValue());
			}

			// ��Ʒ��˰��
			String[] formulas = new String[] {
					"ctaxitemid->getColValue(bd_invbasdoc,pk_taxitems,pk_invbasdoc,cinvbasdocid)",
					"ntaxrate->getColValue(bd_taxitems,taxratio,pk_taxitems,ctaxitemid)" };

			getBillCardPanel().getBillModel().execFormulas(formulas);

			// �����ۿ�
			getBillCardPanel().setHeadItem("ndiscountrate", new UFDouble(100));
			getBillCardPanel().afterDiscountrateEdit(null);

			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				// ��Ʒ�ۿ�
				getBillCardPanel().setBodyValueAt(new UFDouble(100), i, "nitemdiscountrate");
			}

		} else if (getBillType().equals(SaleBillType.SaleOrder)) {
			// �ջ���ַ����
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null)
				((CustAddrRefModel) vreceiveaddress.getRefModel()).setCustId(getBillCardPanel()
						.getHeadItem("creceiptcustomerid").getValue());
			vreceiveaddress.setValue((String) saleVO.getParentVO().getAttributeValue(
					"vreceiveaddress"));

		} else if (getSouceBillType().equals(SaleBillType.PurchaseOrder)) {

		} else if (getSouceBillType().equals("38") || getSouceBillType().equals("3B")) {// Ԥ����

			SaleorderBVO[] ordbvos = saleVO.getBodyVOs();

			String ccalbodyid_h = null;
			for (int i = 0, loop = ordbvos.length; i < loop; i++) {
				if (ccalbodyid_h == null) {
					if (ordbvos[i].getCconsigncorpid() == null
							|| ordbvos[i].getCconsigncorpid().equals(ordbvos[i].getPkcorp())) {
						ccalbodyid_h = ordbvos[i].getCadvisecalbodyid();
					}
				}
			}

			String oldcalbodyid = getBillCardPanel().getHeadItem("ccalbodyid").getValue();
			getBillCardPanel().getHeadItem("ccalbodyid").setValue(ccalbodyid_h);
			if (SoVoTools.isEmptyString(getBillCardPanel().getHeadItem("ccalbodyid").getValue())) {
				getBillCardPanel().getHeadItem("ccalbodyid").setValue(oldcalbodyid);
			}

		}

		setNoEditItem();

		/** �������Զ�������Ҫ��pk��ֵ����value��* */
		// ��ͷ
		if (head_defs != null) {
			for (int i = 1; i <= 20; i++) {
				// �����Զ�������ģ���޷���pk_def%��ֵ���⣩
				// ��ʱ�������
				getBillCardPanel().getHeadItem("pk_defdoc" + i).setValue(
						saleVO.getHeadVO().getAttributeValue("pk_defdoc" + i));

				/*
				 * if (head_defs[i - 1] != null && head_defs[i -
				 * 1].getType().equals("ͳ��")) {
				 * getBillCardPanel().getHeadItem("vdef" + i).setValue(
				 * saleVO.getHeadVO().getAttributeValue( "pk_defdoc" + i)); }
				 */
			}// end for head
		}

		// ����
		/*
		 * if (body_defs != null) { int rowcount = saleVO.getBodyVOs().length;
		 * for (int i = 1; i <= 20; i++) { if (body_defs[i - 1] != null &&
		 * body_defs[i - 1].getType().equals("ͳ��")) { for (int j = 0; j <
		 * rowcount; j++) { getBillCardPanel().setBodyValueAt(
		 * getBillCardPanel().getBodyValueAt(j, "pk_defdoc" + i), j, "vdef" +
		 * i); } } }// end for body }
		 */
		/** �������Զ�������Ҫ��pk��ֵ����value��* */

		// �������ε��ݵ��۱�����
		// ��������Դ�ں�ͬ
		UFDouble nexchangeotobrate = saleVO.getBodyVOs()[0].getNexchangeotobrate();
		if (nexchangeotobrate != null
				&& nexchangeotobrate.toDouble().compareTo(
						Double.parseDouble(getBillCardPanel().getHeadItem("nexchangeotobrate")
								.getValueObject().toString())) != 0) {
			getBillCardPanel().getHeadItem("nexchangeotobrate").setValue(nexchangeotobrate);

			for (int i = 0, len = getBillCardPanel().getRowCount(); i < len; i++) {
				getBillCardPanel().setBodyValueAt(nexchangeotobrate, i, "nexchangeotobrate");
				getBillCardPanel().calculateNumber(i, "noriginalcursummny");
			}
		}

		// ��Դ�ں�ͬ\Ԥ����,����������ջ���λ\�ջ���ַ\�ջ�����\�ջ��ص�
		if ("Z4".equals(getSouceBillType()) || "38".equals(getSouceBillType())) {
			SaleorderBVO[] items = saleVO.getBodyVOs();
			SaleorderBVO item;
			Object obj;
			for (int i = 0, len = items.length; i < len; i++) {
				item = items[i];

				// �ջ���λ
				// CustmandocDefaultRefModel
				if (item.getCreceiptcorpid() != null) {
					getBillCardPanel()
							.setBodyValueAt(item.getCreceiptcorpid(), i, "creceiptcorpid");
					getBillCardPanel().setBodyValueAt(
							((Object[]) CacheTool.getCellValue("bd_cubasdoc", "pk_cubasdoc",
									"custname", (String) ((Object[]) CacheTool.getCellValue(
											"bd_cumandoc", "pk_cumandoc", "pk_cubasdoc", item
													.getCreceiptcorpid()))[0]))[0], i,
							"creceiptcorpname");
				}

				// �ջ�����
				// AreaclDefaultRefModel
				if (item.getCreceiptareaid() != null) {
					getBillCardPanel()
							.setBodyValueAt(item.getCreceiptareaid(), i, "creceiptareaid");
					obj = CacheTool.getCellValue("bd_areacl", "pk_areacl", "areaclname", item
							.getCreceiptareaid());
					getBillCardPanel().setBodyValueAt(obj == null ? null : ((Object[]) obj)[0], i,
							"creceiptareaname");
				}

				// �ջ��ص�
				// AddressDefaultRefModel
				if (item.getCrecaddrnode() != null) {
					getBillCardPanel().setBodyValueAt(item.getCrecaddrnode(), i, "crecaddrnode");
					obj = CacheTool.getCellValue("bd_address", "pk_address", "addrname", item
							.getCrecaddrnode());
					getBillCardPanel().setBodyValueAt(obj == null ? null : ((Object[]) obj)[0], i,
							"crecaddrnodename");
				}

				// �ջ���ַ
				getBillCardPanel().setBodyValueAt(item.getVreceiveaddress(), i, "vreceiveaddress");

			}// end for
		}// end if

	}

	/**
	 * �����������ݡ�
	 * 
	 * �������ڣ�(2001-4-21 14:11:14)
	 * 
	 */
	protected void onNewBySelf() {
		strState = "����";
		getBillCardPanel().addNew();
		getBillCardPanel().setEnabled(true);
		getBillCardTools().setHeadRefLimit(strState);
		getBillCardPanel().initFreeItem();
		setDefaultData(true);
		setNoEditItem();

		// �Զ����������
		addLine();

	}

	/**
	 * ��һ���ݡ�
	 * 
	 * �������ڣ�(2001-4-24 9:55:56)
	 * 
	 */
	protected void onNext() {
		if (num < vIDs.size()) {
			num++;

			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num,
					"csaleid");

			SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);
			if (vo != null) {
				// ���ر���
				if (vo.getChildrenVO() == null) {
					getBillListPanel().loadBodyData(num);
					vo = vocache.getSaleOrderVO(csaleid);
				}

				loadCardData(vo);
			} else {
				loadCardData();
				updateCacheVO();
			}
			setButtonsState();
			showCustManArInfo();
		}
	}

	/**
	 * ���һ�š�
	 * 
	 * �������ڣ�(2001-4-24 9:55:56)
	 * 
	 */
	protected void onLast() {
		num = vIDs.size() - 1;

		String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");

		SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);

		if (vo != null) {
			// ���ر���
			if (vo.getChildrenVO() == null) {
				getBillListPanel().loadBodyData(num);
				vo = vocache.getSaleOrderVO(csaleid);
			}

			loadCardData(vo);
		} else {
			loadCardData();
			updateCacheVO();
		}
		setButtonsState();
		showCustManArInfo();
	}

	/**
	 * ��һ���ݡ�
	 * 
	 * �������ڣ�(2001-4-24 9:55:56)
	 * 
	 */
	protected void onFrist() {
		num = 0;

		String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");

		SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);

		if (vo != null) {
			// ���ر���
			if (vo.getChildrenVO() == null) {
				getBillListPanel().loadBodyData(num);
				vo = vocache.getSaleOrderVO(csaleid);
			}

			loadCardData(vo);
		} else {
			loadCardData();
			updateCacheVO();
		}
		setButtonsState();
		showCustManArInfo();
	}

	/**
	 * ǰһ���ݡ�
	 * 
	 * �������ڣ�(2001-4-24 9:55:42)
	 * 
	 */
	protected void onPre() {
		if (num > 0) {
			num--;

			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num,
					"csaleid");

			SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);

			if (vo != null) {
				// ���ر���
				if (vo.getChildrenVO() == null) {
					getBillListPanel().loadBodyData(num);
					vo = vocache.getSaleOrderVO(csaleid);
				}

				loadCardData(vo);
			} else {
				loadCardData();
				updateCacheVO();
			}
			setButtonsState();
			showCustManArInfo();
		}
	}

	/**
	 * ����
	 */
	protected void onOrderQuery() {
		getSourceDlg().showModal();
	}

	/**
	 * �ֵ���ӡ
	 */
	protected void onSplitPrint() {
		if (getSpDLG().showModal() == QueryConditionClient.ID_CANCEL)
			return;

		// ȡ�ֵ�����
		SaleDispartVO[] receSplitVOs = (SaleDispartVO[]) getSpDLG().getSplitPanel()
				.getReceiveSplitPanel().getBillModel().getBodyValueVOs(
						SaleDispartVO.class.getName());

		// �ֵ���ӡ
		try {
			// ����ӡVO
			ArrayList<AggregatedValueObject> prlistvo = new ArrayList<AggregatedValueObject>();
			// �б�
			if (strShowState.equals("�б�")) {
				int[] selRows = getBillListPanel().getHeadTable().getSelectedRows();
				if (selRows.length > 0) {
					for (int i = 0; i < selRows.length; i++) {
						num = selRows[i];
						// ��������
						loadCardData();
						prlistvo.add(getPrintVO());
					}
				}
			} else
				// ��Ƭ
				prlistvo.add(getPrintVO());

			// �����ӡ����
			BillPrintTool printTool = new BillPrintTool(this, getNodeCode(), prlistvo,
					getBillCardPanel().getBillData(), null, getCorpPrimaryKey(),
					getClientEnvironment().getUser().getPrimaryKey(), "vreceiptcode", "csaleid");

			// �ֵ���ӡ
			if (strShowState.equals("�б�"))
				printTool.onBatchSplitPrintPreview(getBillListPanel(), SaleBillType.SaleOrder,
						transDispartVO(receSplitVOs));
			else
				printTool.onSplitCardPrintPreview(getBillCardPanel(), getBillListPanel(),
						SaleBillType.SaleOrder, transDispartVO(receSplitVOs));

		} catch (Exception e) {
			handleException(e, "���۶����ֵ���ӡ�����쳣! ");
		}
	}

	/**
	 * �����۵ķֵ�VOת��Ϊ�����ķֵ�VO
	 * 
	 * @param svos
	 *            --- ���۵ķֵ�VO
	 */
	private SplitParams[] transDispartVO(SaleDispartVO[] svos) {
		if (svos == null || svos.length <= 0)
			return null;
		SplitParams[] paramvos = null;

		int type = SplitParams.NoInput;
		Object value = null;
		ArrayList<SplitParams> list = new ArrayList<SplitParams>();
		for (int i = 0; i < svos.length; i++) {
			if (svos[i].getBdefault().booleanValue()) {
				// ���÷�������
				if (svos[i].getDispartkey().equals("dconsigndate")) {
					type = SplitParams.NeedInputInt;
					value = svos[i].getValue() == null ? 1 : svos[i].getValue().intValue();
				} else {
					type = SplitParams.NoInput;
					value = null;
				}

				list.add(new SplitParams(svos[i].getDispartkey(), svos[i].getDispartname(), type,
						value, svos[i].getBdefault().booleanValue()));
			}
		}

		if (list.size() > 0) {
			paramvos = list.toArray(new SplitParams[list.size()]);
		}

		return paramvos;
	}

	/**
	 * ��ӡ��
	 * 
	 * �������ڣ�(2001-4-25 10:48:26)
	 * 
	 * @throws Exception
	 * 
	 */
	protected void onPrint(boolean needPreview) throws Exception {
		if (!strShowState.equals("�б�")) { /*-=notranslate=-*/
			onPrintCard(needPreview);
		} else {
			onPrintListVos(needPreview);

		}
	}

	/**
	 * ��ӡ��
	 * 
	 * �����б�ʽ�޸� ����wsy��Ŀ�����޸�
	 * 
	 * �������ڣ�(2001-4-25 10:48:26)
	 * 
	 */
	protected void onPrintCard(boolean needPreview) {

		// ��ӡģ��
		if (getPrintEntry().selectTemplate() < 0)
			return;

		SaleOrderVO saleorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
				SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
				SaleorderBVO.class.getName());

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		showHintMessage(plc.getBeforePrintMsg(needPreview, false));
		plc.addFreshTsListener(this);
		plc.setPrintEntry(getPrintEntry());
		plc.setPrintInfo(saleorder.getHeadVO().getScmPrintlogVO());

		// ��ӡģ�������ô�ӡ����
		getPrintEntry().setPrintListener(plc);

		// ��ӡ����Դ
		SaleOrderPrintDataInterface prds = getDataSource();
		ArrayList prlistvo = new ArrayList();
		prlistvo.add(getPrintVO());
		prds.setListVOs(prlistvo);
		prds.setIsNeedSpaceRowInOneVO(false);// ����ӡ����
		prds.setTotalLinesInOnePage(m_print.getBreakPos());

		// ��ӡģ�������ô�ӡ����Դ
		getPrintEntry().setDataSource(prds);

		// ��ʼ��ӡ
		if (needPreview) {
			getPrintEntry().preview();
		} else {
			getPrintEntry().print(true);
		}

		showHintMessage(plc.getPrintResultMsg(needPreview));

	}

	/**
	 * @return 2007-8-21wsy �õ���ӡVO���ⷽ��Ϊ��ӭ����ǰ���÷�����˴����ܶ಻�㡣
	 */
	private AggregatedValueObject getPrintVO() {
		getBillCardPanel().execHeadLoadFormulas();
		getBillCardPanel().getBillModel().execLoadFormula();

		AggregatedValueObject printHvo = getBillCardPanel().getBillValueVO("nc.vo.dm.pub.DMVO",
				"nc.vo.dm.pub.DMDataVO", "nc.vo.dm.pub.DMDataVO");

		/** ��������* */
		// �ջ���ַ
		printHvo.getParentVO().setAttributeValue(
				"vreceiveaddress",
				((UIRefPane) getBillCardPanel().getHeadItem("vreceiveaddress").getComponent())
						.getUITextField().getText());

		return printHvo;

	}

	/**
	 * �������е�vo��Ϣ��ȫ,Ϊ�б��ӡ��׼��
	 * 
	 * @throws Exception
	 */
	private void supplyVocacheForPrintList() throws Exception {
		SaleorderHVO[] hvos = vocache.getHeadVoWithOutBody();
		SaleorderBVO[] bvos = null;
		if (hvos!=null && hvos.length>0){
	     	bvos = SaleOrderBO_Client.queryBodyAllDataByIDs(SoVoTools.getVOsOnlyValues(hvos, "csaleid"));
	     	SaleOrderVO[] svos = (SaleOrderVO[])RedunVOTool.getBillVos(SaleOrderVO.class.getName()
	     			, "csaleid", hvos, bvos);
	     	for (SaleOrderVO svo : svos)
	     		vocache.setSaleOrderVO(svo.getPrimaryKey(), svo);
		}
	}

	/**
	 * �б���ŵ��ݴ�ӡ��
	 * 
	 * �޸�Ϊ������ӡ V502
	 * 
	 * �������ڣ�(2003-11-5 9:37:56)
	 * 
	 * ��ӡ��ر���ÿ�ξ����³�ʼ������֤ÿ�ο����ڶ�ģ��ʱ����ѡ��
	 * 
	 * ͳһ��ӡ����Դ����Ϊ��Ƭ��ʽ
	 * 
	 * @throws Exception
	 * 
	 */
	protected void onPrintListVos(boolean needPreview) throws Exception {

		// ���Ԥ������ֻ��ӡ��ӡ��һ�ŵ��ݣ���ͬ�ڿ�Ƭ��ӡ
		if (needPreview) {
			num = getBillListPanel().getHeadTable().getSelectedRow();

			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num,
					"csaleid");

			SaleOrderVO saleorder = vocache.getSaleOrderVO(csaleid);
			loadCardData(saleorder);

			onPrintCard(true);

			return;
		}

		/** �б�����ʽ����Ϊ����* */

		java.util.ArrayList alvos = new java.util.ArrayList();

		int selectRows[] = getBillListPanel().getHeadTable().getSelectedRows();

		SaleorderHVO[] hvos = new SaleorderHVO[selectRows.length];
		for (int i = 0, loop = selectRows.length; i < loop; i++) {
			hvos[i] = (SaleorderHVO) getBillListPanel().getHeadBillModel().getBodyValueRowVO(
					selectRows[i], "nc.vo.so.so001.SaleorderHVO");
		}

		// �������е�vo��Ϣ��ȫ
		supplyVocacheForPrintList();

		for (int i = 0; i < selectRows.length; i++) {
			num = selectRows[i];

			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num,
					"csaleid");

			SaleOrderVO saleorder = vocache.getSaleOrderVO(csaleid);
			loadCardData(saleorder);

			alvos.add(getPrintVO());

			if (needPreview)
				break;
		}

		if (null == alvos || alvos.size() == 0) {
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */, NCLangRes.getInstance()
					.getStrByID("SCMCOMMON", "UPPSCMCommon-000153")/*
																	 * @res
																	 * "û�л���б����ݣ�"
																	 */);
			return;
		}

		if (getPrintEntry().selectTemplate() < 0)
			return;

		showHintMessage(PrintLogClient.getBeforePrintMsg(needPreview, true));

		if (needPreview) {
			// �����ӡ��־
			getPrintLogClient().setPrintEntry(m_print);
			printLogClient.setPrintInfo(hvos[0].getScmPrintlogVO());
			m_print.setPrintListener(printLogClient);// ���ô�ӡ����

			ArrayList prlistvo = new ArrayList();
			SaleOrderPrintDataInterface prds = getDataSource();
			prlistvo.add(alvos.get(0));
			prds.setListVOs(prlistvo);
			prds.setIsNeedSpaceRowInOneVO(false);// ����ӡ����
			prds.setTotalLinesInOnePage(m_print.getBreakPos());

			// ���ӡ��������Դ�����д�ӡ
			m_print.setDataSource(prds);
			m_print.preview();
		} else {
			// �����ӡ��־
			getPrintLogClient().setPrintEntry(m_print);

			m_print.beginBatchPrint();
			SaleOrderPrintDataInterface prds = null;
			ArrayList prlistvo = null;
			printLogClient.setBatchPrint(true);// ����������
			m_print.setPrintListener(printLogClient);// ���ô�ӡ����
			for (int i = 0, loop = hvos.length; i < loop; i++) {
				// �����ӡ��־
				printLogClient.setPrintInfo(hvos[i].getScmPrintlogVO());

				if (printLogClient.check()) {// ���ͨ����ִ�д�ӡ��
					// ��������
					prds = getDataSource();
					prlistvo = new ArrayList();
					prlistvo.add(alvos.get(i));
					prds.setListVOs(prlistvo);
					prds.setIsNeedSpaceRowInOneVO(false);// ����ӡ����
					prds.setTotalLinesInOnePage(m_print.getBreakPos());

					// ���ӡ��������Դ�����д�ӡ
					m_print.setDataSource(prds);

				}
			}

			m_print.endBatchPrint();

		}

		showHintMessage(printLogClient.getPrintResultMsg(needPreview));
	}

	protected PrintEntry getPrintEntry() {
		if (null == m_print) {
			m_print = new nc.ui.pub.print.PrintEntry(null, null);
			m_print.setTemplateID(getClientEnvironment().getCorporation().getPk_corp(),
					getNodeCode(), getClientEnvironment().getUser().getPrimaryKey(), null);
		}
		return m_print;
	}

	/**
	 * �����ߣ�������
	 * 
	 * ���ܣ������� ���أ�
	 * 
	 * ���⣺ ���ڣ�(2001-10-30 15:06:35)
	 * 
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	protected SaleOrderPrintDataInterface getDataSource() {
		// if (m_dataSource == null) {
		m_dataSource = new SaleOrderPrintDataInterface();
		// }

		m_dataSource.setBillData(getBillCardPanel().getBillData());
		m_dataSource.setModuleName(getNodeCode());
		m_dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());

		return m_dataSource;
	}

	/**
	 * ��ѯ���ݡ�
	 * 
	 * �������ڣ�(2001-6-1 13:12:36)
	 * 
	 */
	protected void onQuery() {

		// ��ʾ��ѯ�Ի���
		if (getQueryDlg().showModal() == QueryConditionClient.ID_CANCEL)
			return;

		onRefresh();

	}

	/**
	 * ˢ�²���
	 * 
	 */
	protected void onRefresh() {
		ConditionVO[] voCondition = getQueryDlg().getConditionVO();

		// ������̹������Զ�����
		// ����������
		boolean queryallflag = false;
		int index = -1;
		if (voCondition != null) {
			for (int i = 0; i < voCondition.length; i++) {
				if (voCondition[i].getTableNameForMultiTable() != null
						&& voCondition[i].getTableNameForMultiTable().equals("bd_cumandoc")
						&& voCondition[i].getTableCodeForMultiTable().indexOf("def") >= 0) {
					voCondition[i].setTableCode(voCondition[i].getTableName() + "."
							+ voCondition[i].getTableCodeForMultiTable());
					voCondition[i].setFieldCode("ccustomerid");
				} else if ("so_sale.bretinvflag".equals(voCondition[i].getFieldCode())) {
					if (voCondition[i].getValue().equals(
							NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000547")/*
																								 * @res
																								 * "ȫ��"
																								 */)) {
						queryallflag = true;
						index = i;
					} else if (voCondition[i].getValue().equals(
							NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000546")/*
																								 * @res
																								 * "���˻�"
																								 */)) {
						voCondition[i].setValue("N");
					} else if (voCondition[i].getValue().equals(
							NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000151")/*
																								 * @res
																								 * "�˻�"
																								 */)) {
						voCondition[i].setValue("Y");
					}
				}

			}// end for
		}

		// ����������
		// ȫ����ѯȥ��������
		if (queryallflag) {
			ConditionVO[] conds1 = new ConditionVO[voCondition.length - 1];
			System.arraycopy(voCondition, 0, conds1, 0, index);
			System.arraycopy(voCondition, index + 1, conds1, index, conds1.length - index);
			voCondition = conds1;
		}

		// ���ţ������¼�����
		// ������֯�������¼�������֯
		try {
			ConditionVO[] newvo;
			newvo = ScmPubHelper.getTotalSubPkVOs(voCondition, getCorpPrimaryKey());
			voCondition = newvo;
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}

		ArrayList al = new ArrayList();
		ArrayList<ConditionVO> qualified = new ArrayList();
		for (int i = 0, len = voCondition.length; i < len; i++) {
			// �ж�����Ȩ������
			if (!(voCondition[i].getOperaCode().trim().equalsIgnoreCase("IS")
					&& voCondition[i].getValue().trim().equalsIgnoreCase("NULL") || voCondition[i]
					.getValue().trim().indexOf(
					// "(select distinct power.resource_data_id"
							"(select ") >= 0)) {
				if ("�ͻ���������".equals(voCondition[i].getFieldName())) {
					// ��ѯģ����TableCodeΪareaclname��value���ò��յ�nameֵ
					voCondition[i].setValue(voCondition[i].getRefResult().getRefName());
				}
				al.add(voCondition[i]);
			} else {
				qualified.add(voCondition[i]);
			}
		}// end for
		ConditionVO[] normals = new ConditionVO[al.size()];
		al.toArray(normals);

		// ��͸��ѯ
		normals = ConvertQueryCondition.getConvertedVO(normals, getCorpPrimaryKey());

		// �����������
		for (int i = 0, len = normals.length; i < len; i++) {
			voCondition[i] = normals[i];
		}
		for (int i = 0, len = qualified.size(); i < len; i++) {
			voCondition[i + normals.length] = qualified.get(i);
		}

		boolean bfstatus = false;
		boolean bvreceiptcode = false;
		for (int i = 0; i < voCondition.length; i++) {
			if (voCondition[i].getFieldCode().equals("so_sale.fstatus")) {
				if (voCondition[i].getValue().equals("Y")) {
					Object objIsWaitAudit = new Object[2];
					((Object[]) objIsWaitAudit)[0] = "Y";
					((Object[]) objIsWaitAudit)[1] = ClientEnvironment.getInstance().getUser()
							.getPrimaryKey();
					getBillListPanel().setObjIsWaitAudit(objIsWaitAudit);
					voCondition[i].setValue(String.valueOf(BillStatus.FREE));
				} else if (voCondition[i].getValue().equals("N")) {
					voCondition[i].setFieldCode("so_sale.dr");
					voCondition[i].setValue("0");
				}
				bfstatus = true;
			}
			if (voCondition[i].getFieldCode().equals("so_sale.vreceiptcode")
					&& voCondition[i].getOperaCode().equals("like")
					&& !voCondition[i].getValue().endsWith("%")) {
				voCondition[i].setValue(voCondition[i].getValue() + "%");
				bvreceiptcode = true;
			}
			if (bfstatus && bvreceiptcode)
				break;
		}

		// ��������Ȩ������ת��
		DataPowerUtils.trnsDPCndFromAreaToCust(voCondition, "ccustomerid");

		String tablename = null;
		if (getBillType().equals(SaleBillType.SaleOrder))
			tablename = "so_sale";
		else
			tablename = "so_salereceipt";

		StringBuffer strWhere = new StringBuffer();
		strWhere.append(tablename + ".pk_corp = '" + getCorpPrimaryKey() + "' ");
		/** ������ѯֻ�������е�ҵ���������ƣ����ܰ�ťҵ������ȡֵ���� V51* */
		if (!getBillType().equals(SaleBillType.SaleOrder))
			strWhere.append(" AND " + tablename + ".cbiztype = '" + boBusiType.getTag() + "' ");
		/** ������ѯֻ�������е�ҵ���������ƣ����ܰ�ťҵ������ȡֵ���� V51* */
		strWhere.append(" AND " + tablename + ".dr=0 ");

		if (getBillType().equals(SaleBillType.SaleOrder))
			strWhere.append(" AND " + tablename + ".creceipttype = '30' ");
		else
			strWhere.append(" AND " + tablename + ".creceipttype = '31' ");

		// ����״̬����
		if (getQueryDlg().m_rdoAll.isSelected()) {
			strWhere.append(" and " + tablename + ".fstatus != " + BillStatus.BLANKOUT);
		}
		// ������������objIsWaitAudit[0] -- "Y" �д��������� "N" �޴���������
		// objIsWaitAudit[1] -- ��ǰ����ԱID
		// else if (getQueryDlg().m_rdoIsWaitAudit.isSelected()) {
		// Object objIsWaitAudit = new Object[2];
		// ((Object[]) objIsWaitAudit)[0] = "Y";
		// ((Object[]) objIsWaitAudit)[1] =
		// ClientEnvironment.getInstance().getUser().getPrimaryKey();
		// getBillListPanel().setObjIsWaitAudit(objIsWaitAudit);
		// strWhere.append(" and " + tablename + ".fstatus in (" +
		// String.valueOf(BillStatus.FREE) + ")");
		// }
		// ��ȫ��״̬
		else {
			String fstatus = "";
			if (getQueryDlg().m_rdoFree.isSelected()) {
				fstatus += fstatus.equals("") ? String.valueOf(BillStatus.FREE) : ","
						+ String.valueOf(BillStatus.FREE);
			}
			if (getQueryDlg().m_rdoAudited.isSelected()) {
				fstatus += fstatus.equals("") ? String.valueOf(BillStatus.AUDIT) : ","
						+ String.valueOf(BillStatus.AUDIT);
			}
			if (getQueryDlg().m_rdoBlankOut.isSelected()) {
				fstatus += fstatus.equals("") ? String.valueOf(BillStatus.BLANKOUT) : ","
						+ String.valueOf(BillStatus.BLANKOUT);
			}
			if (getQueryDlg().m_rdoFreeze.isSelected()) {
				fstatus += fstatus.equals("") ? String.valueOf(BillStatus.FREEZE) : ","
						+ String.valueOf(BillStatus.FREEZE);
			}
			if (getQueryDlg().m_rdoAuditing.isSelected()) {
				fstatus += fstatus.equals("") ? String.valueOf(BillStatus.AUDITING) : ","
						+ String.valueOf(BillStatus.AUDITING);
			}
			if (getQueryDlg().m_rdoClosed.isSelected()) {
				fstatus += fstatus.equals("") ? String.valueOf(BillStatus.FINISH) : ","
						+ String.valueOf(BillStatus.FINISH);
			}

			if (fstatus.equals(""))
				strWhere.append(" and " + tablename + ".fstatus != " + BillStatus.BLANKOUT);
			else
				strWhere.append(" and " + tablename + ".fstatus in (" + fstatus + ")");
		}

		// if (getBillType().equals(SaleBillType.SaleOrder)) {
		// if (getQueryDlg().m_rdoBatch.isSelected())
		// strWhere.append(" and " + tablename + ".bretinvflag = 'Y'");
		// else
		// strWhere.append(" and " + tablename + ".bretinvflag = 'N'");
		// }

		if (getQueryDlg().getWhereSQL(voCondition) == null
				|| getQueryDlg().getWhereSQL(voCondition).length() == 0) {
			getBillListPanel().setWhere(strWhere.toString());
		} else {
			getBillListPanel().setWhere(
					strWhere + " AND (" + getQueryDlg().getWhereSQL(voCondition) + ") ");
		}

		setBtnsByBillState(-1);

		getBillListPanel().reLoadData();
		fillCacheByListPanel();
		selectRow = -1;
		initIDs();

		// ��Ƭ�²�ѯ(bodyRowChange+onCard)
		if (strShowState.equals("��Ƭ")) {

			selectRow = 0;
			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(selectRow,
					"csaleid");

			SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);

			if (vo == null || vo.getParentVO() == null) {
				num = -1;
				getBillCardPanel().addNew();
				getBillCardPanel().getHeadItem("vreceiptcode").setValue(null);
			} else {
				getBillListPanel().loadBodyData(selectRow);

				strState = "����";
				num = 0;

				loadCardData(vocache.getSaleOrderVO(csaleid));
				
				// ���ñ�ͷ��ͬ���������б��������ʾ
				// add by river for 2012-07-18
				// start ..
				SaleOrderVO saleorder = vocache.getSaleOrderVO(csaleid);
				if(getBillCardPanel().getHeadItem("contracttype") != null) {
					if(saleorder != null && saleorder.getParentVO() != null) {
						if((Integer) saleorder.getParentVO().getAttributeValue("contracttype") == 10)
							((UIComboBox) getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("�ֻ���ͬ");
						else if((Integer) saleorder.getParentVO().getAttributeValue("contracttype") == 20)
							((UIComboBox) getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("������ͬ");
					}
				}
				
				// .. end
			}

			showCustManArInfo();
			updateUI();
		}

		showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000542", null,
				new String[] { "" + getBillListPanel().getHeadBillModel().getRowCount() })/* ����ѯ��{0}������ */);

		b_query = true;
		setButtonsState();

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-16 11:37:56)
	 */
	private void fillCacheByListPanel() {

		if (getBillListPanel().getHeadBillModel().getRowCount() <= 0)
			return;

		SaleorderHVO[] hvos = (SaleorderHVO[]) getBillListPanel().getHeadBillModel()
				.getBodyValueVOs("nc.vo.so.so001.SaleorderHVO");
		// ȡ��ͷ��������
		try {
			BillTools.getMnyByCurrencyFromModel(getBillListPanel().getHeadBillModel(), hvos,
					getCorpPrimaryKey(), "ccurrencytypeid", new String[] { "npreceivemny",
							"nreceiptcathmny" });
		} catch (Exception e) {
			handleException(e);
		}

	}

	/**
	 * �˻�����
	 * 
	 * �������ڣ�(2001-4-24 9:55:56)
	 * 
	 */
	protected void onRefundment() {

		// �л�����
		if (strShowState.equals("�б�")) {
			onCard();
		}
		// switchInterface();

		SaleOrderVO saleorderVO = (SaleOrderVO) getVO(false);

		strUISource = strShowState;
		// ����״̬
		strShowState = "�˻�"; /*-=notranslate=-*/
		strState = "�˻�"; /*-=notranslate=-*/

		getBillCardTools().setHeadRefLimit(strState);

		// ��������
		try {
			num = getBillListPanel().getHeadTable().getSelectedRow();
			long s = System.currentTimeMillis();
			/*
			 * SaleOrderVO saleorderVO =
			 * SaleOrderBO_Client.queryData(getBillID());
			 * nc.vo.scm.pub.SCMEnv.out( "���ݶ�ȡ[����ʱ" + CommonConstant.BEGIN_MARK +
			 * (System.currentTimeMillis() - s) + CommonConstant.END_MARK +
			 * "]");
			 */
			// SaleOrderVO saleorderVO=vocache.getSaleOrderVO(num);
			// ������ʼ��VO
			getBillCardTools().setOldsaleordervo(saleorderVO);
			String csaleid = saleorderVO.getHeadVO().getCsaleid();

			// ��������
			saleorderVO.getHeadVO().setBretinvflag(new UFBoolean(true));
			saleorderVO.getHeadVO().setDbilldate(getClientEnvironment().getDate());
			saleorderVO.getHeadVO().setCsaleid(null);

			saleorderVO.getHeadVO().setVreceiptcode(null);
			saleorderVO.getHeadVO().setCapproveid(null);
			saleorderVO.getHeadVO().setDapprovedate(null);
			saleorderVO.getHeadVO().setBinvoicendflag(null);
			saleorderVO.getHeadVO().setBoutendflag(null);
			saleorderVO.getHeadVO().setBreceiptendflag(null);
			saleorderVO.getHeadVO().setBpayendflag(null);

			saleorderVO.getHeadVO().setFstatus(new Integer(BillStatus.FREE));
			saleorderVO.getHeadVO().setPrimaryKey(null);
			saleorderVO.getHeadVO().setTs(null);
			saleorderVO.getHeadVO().setDmakedate(getClientEnvironment().getDate());

			saleorderVO.getHeadVO().setDbilltime(null);
			saleorderVO.getHeadVO().setDmoditime(null);
			saleorderVO.getHeadVO().setDaudittime(null);

			// n30
			saleorderVO.getHeadVO().setNpreceivemny(null);
			saleorderVO.getHeadVO().setNpreceiverate(null);
			saleorderVO.getHeadVO().setNreceiptcathmny(null);
			saleorderVO.getHeadVO().setBoverdate(null);
			saleorderVO.getHeadVO().setBtransendflag(null);

			// n30add
			String[] keys = new String[] {
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
					// �ۼư���ί�ⶩ������
					"narrangescornum",
					// �ۼư����빺������
					"narrangepoapplynum",
					// �ۼư��ŵ�����������
					"narrangetoornum",
					// �ۼư��ŵ�����������
					"norrangetoapplynum",
					// �Ƿ��Դ�������
					"barrangedflag",
					// ����Դ������
					"carrangepersonid",
					// ����Դ����ʱ��
					"tlastarrangetime",

					// �ۼư���������������
					"narrangemonum", "ntotalshouldoutnum"

			};

			for (int i = 0; i < saleorderVO.getBodyVOs().length; i++) {
				// ��¼�˻�����Դͷ
				saleorderVO.getBodyVOs()[i].setCsourcebillid(csaleid);
				saleorderVO.getBodyVOs()[i].setCsourcebillbodyid(saleorderVO.getBodyVOs()[i]
						.getCorder_bid());

				saleorderVO.getBodyVOs()[i].setCreceipttype(SaleBillType.SaleOrder);
				saleorderVO.getBodyVOs()[i].setPrimaryKey(null);
				saleorderVO.getBodyVOs()[i].setFrowstatus(new Integer(BillStatus.FREE));

				saleorderVO.getBodyVOs()[i].setBifinventoryfinish(null);
				saleorderVO.getBodyVOs()[i].setBifinvoicefinish(null);
				saleorderVO.getBodyVOs()[i].setBifpayfinish(null);
				saleorderVO.getBodyVOs()[i].setBifreceivefinish(null);

				saleorderVO.getBodyVOs()[i].setNtotalbalancenumber(null);
				saleorderVO.getBodyVOs()[i].setNtotalcostmny(null);
				saleorderVO.getBodyVOs()[i].setNtotalinventorynumber(null);
				saleorderVO.getBodyVOs()[i].setNtotalinvoicenumber(null);
				saleorderVO.getBodyVOs()[i].setNtotalinvoicemny(null);
				saleorderVO.getBodyVOs()[i].setNtotalpaymny(null);
				saleorderVO.getBodyVOs()[i].setNtotalreceivenumber(null);
				saleorderVO.getBodyVOs()[i].setNtotalsignnumber(null);
				saleorderVO.getBodyVOs()[i].setCfreezeid(null);
				saleorderVO.getBodyVOs()[i].setTs(null);
				saleorderVO.getBodyVOs()[i].setBdericttrans(null);
				saleorderVO.getBodyVOs()[i].setCconsigncorp(null);
				saleorderVO.getBodyVOs()[i].setCconsigncorpid(null);
				saleorderVO.getBodyVOs()[i].setCadvisecalbodyid(null);
				saleorderVO.getBodyVOs()[i].setCbodywarehouseid(null);
				saleorderVO.getBodyVOs()[i].setCreccalbody(null);
				saleorderVO.getBodyVOs()[i].setCreccalbodyid(null);
				saleorderVO.getBodyVOs()[i].setCrecwarehouse(null);
				saleorderVO.getBodyVOs()[i].setCrecwareid(null);
				saleorderVO.getBodyVOs()[i].setNtotlbalcostnum(null);

				// nc30add
				nc.vo.so.so016.SoVoTools.setVOValueForOne(saleorderVO.getBodyVOs()[i], keys, null);

			}

			s = System.currentTimeMillis();
			getBillCardPanel().setBillValueVO(saleorderVO);
			nc.vo.scm.pub.SCMEnv.out("��������[����ʱ" + (System.currentTimeMillis() - s) + "]");

			long s1 = System.currentTimeMillis();
			// getBillCardPanel().getBillModel().execLoadFormula();
			BillItem[] bms = getBillCardPanel().getBillModel().getBodyItems();
			for (int i = 0; i < bms.length; i++) {
				if (bms[i].getLoadFormula() != null && bms[i].getLoadFormula().length > 0) {
					getBillCardPanel().getBillModel().execLoadFormulasByKey(bms[i].getKey());
				}
			}
			nc.vo.scm.pub.SCMEnv.out("ִ�й�ʽ[����ʱ" + (System.currentTimeMillis() - s1) + "]");

			// ����������˰�ϼ�
			if (getBillCardPanel().getHeadItem("nheadsummny") != null)
				getBillCardPanel().getHeadItem("nheadsummny").setValue(
						getBillCardPanel().getTotalValue("noriginalcursummny"));

			// �ջ���ַ����
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();

			/** ������ͷ�ջ���ַ V502 qurui zhongwei* */
			// ((CustAddrRefModel) vreceiveaddress.getRefModel())
			// .setCustId(getBillCardPanel().getHeadItem(
			// "creceiptcustomerid").getValue());
			// // ��������ID
			// String formula =
			// "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
			// String pk_cubasdoc = (String) getBillCardPanel().execHeadFormula(
			// formula);
			// String strvreceiveaddress = BillTools.getColValue2("bd_custaddr",
			// "pk_custaddr", "defaddrflag", "Y", "pk_cubasdoc",
			// pk_cubasdoc);
			// vreceiveaddress.setPK(strvreceiveaddress);
			/** ������ͷ�ջ���ַ V502 qurui zhongwei* */

			vreceiveaddress.getUITextField().setText(saleorderVO.getHeadVO().getVreceiveaddress());

			getBillCardPanel().initFreeItem();
			setHeadDefaultData();
			// ����ɢ��״̬
			if (getBillCardPanel().getHeadItem("bfreecustflag").getValue() == null
					|| getBillCardPanel().getHeadItem("bfreecustflag").getValue().equals("false")) {
				getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
			} else {
				getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
			}
			// ������λ
			getBillCardPanel().initUnit();

			// ��ձ��巢�������֯�������ֿ�
			UIRefPane cadvisecalbody = (UIRefPane) getBillCardPanel().getBodyItem("cadvisecalbody")
					.getComponent();
			cadvisecalbody.setPK(null);

			UIRefPane cbodywarehousename = (UIRefPane) getBillCardPanel().getBodyItem(
					"cbodywarehousename").getComponent();
			cbodywarehousename.setPK(null);

			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {

				getBillCardPanel().setAssistUnit(i);

				/** ���������֯���ֿ� V502 jindongmei jiangchunxia zhongwei* */
				// getBillCardPanel().setBodyValueAt(null, i,
				// "cadvisecalbodyid");
				// getBillCardPanel().setBodyValueAt(null, i, "cadvisecalbody");
				// getBillCardPanel().setBodyValueAt(null, i,
				// "cbodywarehouseid");
				// getBillCardPanel().setBodyValueAt(null, i,
				// "cbodywarehousename");
				/** ���������֯���ֿ� V502 jindongmei jiangchunxia zhongwei* */

				/** ȥ�����ͬ�Ĺ��� V502 dongwei zhongwei* */
				getBillCardPanel().setBodyValueAt(null, i, "ct_manageid");
				getBillCardPanel().setBodyValueAt(null, i, "ct_name");
				getBillCardPanel().setBodyValueAt(null, i, "ct_code");
				getBillCardPanel().setBodyValueAt(null, i, "ctinvclassid");
				getBillCardPanel().setBodyValueAt(null, i, "ctinvclass");
				/** ȥ�����ͬ�Ĺ��� V502 dongwei zhongwei* */

				// ������ۺ�˰
				if (SA_02.booleanValue()) {
					if (getBillCardPanel().getBodyItem("norgqttaxprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurtaxprice", true);
					if (getBillCardPanel().getBodyItem("norgqttaxnetprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxnetprc",
								true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurtaxnetprice", true);

					getBillCardPanel().getBillModel()
							.setCellEditable(i, "noriginalcurtaxmny", true);
					getBillCardPanel().getBillModel()
							.setCellEditable(i, "noriginalcursummny", true);
					getBillCardPanel().getBillModel().setCellEditable(i, "noriginaldiscountmny",
							true);
				} else {
					if (getBillCardPanel().getBodyItem("norgqtprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqtprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i, "norginalcurprice",
								true);
					if (getBillCardPanel().getBodyItem("norgqtnetprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqtnetprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurnetprice", true);

					getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcurmny", true);
					getBillCardPanel().getBillModel()
							.setCellEditable(i, "noriginalcursummny", true);
					getBillCardPanel().getBillModel().setCellEditable(i, "noriginaldiscountmny",
							true);
				}

			}// end for

			/** �༭ǰ����* */
			// // ���ú�ͬ���Ʋ��ɱ༭
			// getBillCardPanel().getBodyItem("ct_name").setEdit(false);
			getBillCardPanel().updateValue();
			getBillCardPanel().getBillModel().reCalcurateAll();
			getBillCardPanel().setEnabled(true);
			// �����ſɱ༭
			// getBillCardPanel().getHeadItem("vreceiptcode").setEnabled(true);
			// �������ɱ༭
			int rowcount = getBillCardPanel().getRowCount();
			for (int i = 0; i < rowcount; i++)
				getBillCardPanel().setCellEditable(i, "cinventorycode", false);

			// �����������
			getBillCardTools().clearCatheData();

			/** v5.3ȥ�������ֶ� * */
			/*
			 * if (saleorderVO.getHeadVO().getNaccountperiod() != null &&
			 * saleorderVO.getHeadVO().getNaccountperiod() .doubleValue() < 0.0)
			 * getBillCardPanel().setHeadItem("naccountperiod", null);
			 */

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("���ݼ���ʧ�ܣ�");
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}

		// ���浥��ģ��ı༭״̬
		getBillCardTools().resumeBillItemEditToInit();

		// ���ð�Ŧ״̬
		setButtons();
		setButtonsState();
		setNoEditItem();
		showCustManArInfo();

		// ���������ɫ����
		try {
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), this.alInvs);
		} catch (Exception e) {

		}
		updateUI();

	}

	/**
	 * �����б�
	 * 
	 * �������ڣ�(2001-4-25 10:48:26)
	 * 
	 */
	protected void onReturn() {

		// ��Ƭ��������
		boolean bcardHasData = false;
		if (getBillCardTools().getHeadValue("csaleid") != null
				&& getBillCardTools().getHeadValue("csaleid").toString().trim().length() != 0)
			bcardHasData = true;

		strShowState = "�б�"; /*-=notranslate=-*/
		switchInterface();

		// selectRow = -1;
		// num = -1;

		getBillListPanel().getHeadTable().clearSelection();
		BillModel bmHead = getBillListPanel().getHeadBillModel();
		for (int i = 0, iRowCount = bmHead.getRowCount(); i < iRowCount; i++) {
			bmHead.setRowState(i, BillModel.NORMAL);
		}
		getBillListPanel().getBodyBillModel().clearBodyData();

		// ��տ�Ƭ��Ϣ
		getBillCardPanel().addNew();
		getBillCardPanel().getHeadItem("vreceiptcode").setValue(null);

		initIDs();

		setButtons(getBillButtons());

		updateUI();

		// ԭ�ȿ�Ƭ��������
		if (bcardHasData) {
			// ģ���ͷ�б任�¼���ʹ�л����б�󽹵���ԭ���б�ѡ������
			getBillListPanel().bodyRowChange(
					new BillEditEvent(getBillListPanel().getHeadTable(), -1, selectRow));
			UITable table = getBillListPanel().getHeadTable();
			table.getSelectionModel().setSelectionInterval(selectRow, selectRow);
			table.scrollRectToVisible(table.getCellRect(selectRow, 0, true));
		}

		// ��������
		new UnitOfMeasureTool(getBillListPanel().getBodyBillModel()).calculateUnitOfMeasure();

		// �ı䰴ť״̬
		setButtonsState();
	}

	/**
	 * �˻������µķ���
	 * 
	 */
	protected void onBack() {

		// ��տ�Ƭ������Ϣ
		getBillCardPanel().addNew();
		getBillCardPanel().setEnabled(false);

		// ������һ������
		if (strUISource.equals("�б�"))
			onReturn();
		else
			onCard();

		// �ָ���������ɫ
		nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel());
	}
	
	protected boolean beforeSaveValid() {
		
		return true;
	}

	/**
	 * ���浥�ݡ�
	 * 
	 * �������ڣ�(2001-4-21 10:21:54)
	 * 
	 * �޸����ڣ�2003-10-31 �޸��ˣ����� �޸����ݣ�����Ԥ�տ�����ͽ��ļ���
	 * 
	 */
	protected boolean onSave() {

		// ����ʱ�жϱ������ϵļ۸��Ƿ��ֹ��޸ģ���ѡ��Ϊ�ǣ����ص��ݽ���
		if (getChangePriceRowFromCardUI().size() > 0
				&& !"true".equals(getBillCardPanel().getHeadItem("bpocooptome").getValue())) {
			if (showYesNoMessage(NCLangRes.getInstance().getStrByID("40060301",
					"UPT40060301-000576") /* @res"�ֹ��޸ĵĴ���۸��Ƿ�����ѯ�ۣ�ֱ�ӱ��棿" */) != nc.ui.pub.beans.UIDialog.ID_YES) {
				showHintMessage(NCLangRes.getInstance().getStrByID("scmcommon",
						"UPPSCMCommon-000291")/* @res "�޸ĵ���" */);
				return true;
			}
			// �ָ���������ɫ
			nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel());
		}
		
		// PTA���۶��� �� ��Ӻ�ͬ����ʱ�������֤
		// add by river for 2012-07-18
		// start ..
		
		if(!beforeSaveValid()) {
			showWarningMessage("<<< ������֤��ʾ >>>");
		}
		
		// .. end

		// ����ͨ���������ӵĶ������棨�����ĺ�������ͬ��
		if ("����".equals(strState)) { /*-=notranslate=-*/
			onSaveCopyBill();
			return false;
		}

		try {
			getBillCardPanel().cleanNullLine();

			if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), getRowNoItemKey())) {
				showWarningMessage(NCLangRes.getInstance().getStrByID(
						"common",
						"MC1",
						null,
						new String[] { NCLangRes.getInstance()
								.getStrByID("common", "UC000-0003389") }));
				return false;
			}

			SaleOrderVO saleorder = (SaleOrderVO) getBillCardPanel().getBillValueChangeVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());
			SaleOrderVO oldsaleorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());

			// ��������
			((SaleorderHVO) saleorder.getParentVO()).setCreceipttype(SaleBillType.SaleOrder);

			// yt add 2003-10-31
			// ����Ԥ�տ������Ԥ�տ���
			if (!calculatePreceive(oldsaleorder))
				return false;

			SaleorderBVO[] bvos = saleorder.getBodyVOs();
			if (bvos != null) {
				for (int i = 0, iLen = bvos.length; i < iLen; i++) {
					if (bvos[i].getBoosflag() == null)
						bvos[i].setBoosflag(new UFBoolean(false));
				}
			}

			// ���ݼ��
			onCheck(oldsaleorder);
			// ����к�
			saleorder = getLineNumber(oldsaleorder, saleorder);

			// �����к�
			if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(getBillCardPanel(),
					getRowNoItemKey()))
				return false;

			// ��˾����
			((SaleorderHVO) saleorder.getParentVO()).setPk_corp(getCorpPrimaryKey());
			// ���Ƿ��޸�
			((SaleorderHVO) saleorder.getParentVO()).setBcodechanged(m_isCodeChanged);
			// if (m_isCodeChanged == true) {

			saleorder.getHeadVO().setVoldreceiptcode(
					getBillCardTools().getOldsaleordervo() == null ? null : getBillCardTools()
							.getOldsaleordervo().getHeadVO().getVreceiptcode());
			// �ջ���ַ
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			saleorder.getHeadVO().setVreceiveaddress(vreceiveaddress.getUITextField().getText());

			oldsaleorder.getHeadVO().setVreceiveaddress(vreceiveaddress.getUITextField().getText());
			if (coPoTs != null && saleorder.isCoopped()) {
				// ЭͬtsУ��
				((SaleorderHVO) saleorder.getParentVO()).setCoopPoTs(coPoTs);

			}

			if (saleorder.getChildrenVO() != null) {
				for (int i = 0; i < saleorder.getChildrenVO().length; i++) {
					// ��˾����
					((SaleorderBVO) saleorder.getChildrenVO()[i]).setPkcorp(getCorpPrimaryKey());
					if (strState.equals("�޶�") /*-=notranslate=-*/
							&& saleorder.getChildrenVO()[i].getStatus() == VOStatus.UPDATED)
						saleorder.getChildrenVO()[i]
								.setStatus(nc.vo.pub.bill.BillVOStatus.MODIFICATION);
					/** v5.3 MM ��΢�� ��������Ҫ�� */
					else if (strState.equals("�޶�") /*-=notranslate=-*/
							&& saleorder.getChildrenVO()[i].getStatus() == VOStatus.NEW)
						((SaleorderBVO) saleorder.getChildrenVO()[i])
								.setFrowstatus(BillStatus.AUDIT);
					/** v5.3 MM ��΢�� ��������Ҫ�� */
				}
			}
			if (strState.equals("�޸�") || strState.equals("�޶�")) { /*-=notranslate=-*/
				saleorder.setStatus(VOStatus.UPDATED);
			}

			saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
			saleorder.setDcurdate(getClientEnvironment().getDate());
			saleorder.setCusername(getClientEnvironment().getUser().getUserName());
			saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());
			oldsaleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
			oldsaleorder.setDcurdate(getClientEnvironment().getDate());
			saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000504")
			/* @res "ӆ�ι���" */);

			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000136")/*
																								 * @res
																								 * "��ʼ��������...."
																								 */);
			// // ���۶�����ʽЭͬ�ɹ���������д�ɹ�������־����������ccooppohid
			if (saleorder.getHeadVO().isCoopped()) {
				saleorder.getHeadVO().setCoopPoId(coPoOrderId);
			}
			nc.vo.scm.pub.SCMEnv.out("��ʼ���棺" + System.currentTimeMillis());

			// ��һ�α����ͬ�����д
			saleorder.setFirstTime(true);
			boolean bContinue = true;
			boolean bRight = true;
			java.util.ArrayList listID = null;
			HashMap reths = null;

			fillDataBeforeSave(saleorder, oldsaleorder);

			nc.vo.scm.pub.session.ClientLink clientLink = new nc.vo.scm.pub.session.ClientLink(
					getClientEnvironment());
			saleorder.setClientLink(clientLink);

			// ���֧��
			getPluginProxy().beforeAction(Action.SAVE, new SaleOrderVO[] { saleorder });

			while (bContinue) {
				try {
					if (strState.equals("�޸�")) { /*-=notranslate=-*/
						// ����״̬
						int iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus")
								.getValue());
						UFDateTime ud = ClientEnvironment.getServerTime();
						saleorder.getHeadVO().setAttributeValue("dmoditime", ud);
						if (iStatus == BillStatus.NOPASS) {
							// ����VO״̬
							listID = (java.util.ArrayList) PfUtilClient.processActionNoSendMessage(
									this, "SpecialSave", SaleBillType.SaleOrder,
									getClientEnvironment().getDate().toString(), oldsaleorder,
									null, null, null);
							Integer istatus = new Integer(BillStatus.FREE);
							getBillCardPanel().setHeadItem("fstatus", istatus);
							for (int i = 0, loop = getBillCardPanel().getRowCount(); i < loop; i++) {
								getBillCardPanel().setBodyValueAt(istatus, i, "frowstatus");
							}

						} else {
							/**
							 * �༭�������ۿۻ��߼�˰�ϼƺ�����ж�Ӧ�ø���
							 * oldsaleorder��bvo�е�״̬�Ѿ���fillDataBeforeSave�����ù�
							 */
							SaleOrderVO oldvo = getBillCardTools().getOldsaleordervo();// ��ȡ�༭ǰ��VO
							if (oldvo.getHeadVO().getNdiscountrate().compareTo(
									oldsaleorder.getHeadVO().getNdiscountrate()) != 0) {
								saleorder.setChildrenVO(oldsaleorder.getChildrenVO());
							}
							listID = (java.util.ArrayList) PfUtilClient.processActionNoSendMessage(
									this, "PreModify", SaleBillType.SaleOrder,
									getClientEnvironment().getDate().toString(), saleorder, null,
									null, null);

						}
						getBillCardPanel().setTailItem("dmoditime", ud);
						// ���ظ���ID
						loadIDafterEDIT(listID);
						if (listID != null)
							reths = (HashMap) listID.get(listID.size() - 1);
					}
					if (strState.equals("�޶�")) { /*-=notranslate=-*/
						UFDateTime ud = ClientEnvironment.getServerTime();
						saleorder.getHeadVO().setAttributeValue("dmoditime", ud);

						listID = (java.util.ArrayList) PfUtilClient.processActionNoSendMessage(
								this, "OrderAlter", SaleBillType.SaleOrder, getClientEnvironment()
										.getDate().toString(), saleorder, null, null, null);

						if (listID != null) {
							reths = (HashMap) listID.get(listID.size() - 1);
						}

						getBillCardPanel().setTailItem("dmoditime", ud);
						// ���¼�������
						loadCardData();

					}
					// ��ͬ��д�Ƿ����ڶ���
					bContinue = false;
					bRight = true;
				} catch (Exception ex) {
					bRight = false;
					bContinue = doException(saleorder, oldsaleorder, ex);
				}
			}
			if (!bRight) {
				showHintMessage(NCLangRes.getInstance()
						.getStrByID("40060301", "UPP40060301-000185")/*
																		 * @res
																		 * "����ʧ�ܣ�"
																		 */);
				return false;
			}

			nc.vo.scm.pub.SCMEnv.out("���������" + System.currentTimeMillis());
			showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH005"));

			if (!strState.equals("�˻�")) /*-=notranslate=-*/
				strState = "����"; /*-=notranslate=-*/

			//
			// String sourceId = (String) saleorder.getHeadVO().getCoopPoId();
			// reWritePO(new String[] { sourceId }, true);
			//
			// }

			// getBillCardTools().setHeadRefLimit(strState);

			// ����ʱ���
			// reLoadTS();
			getBillCardTools().reLoadConsignCorpAndCalbody(reths);

			vRowATPStatus = new Vector();
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				vRowATPStatus.addElement(new UFBoolean(false));
			}

			// ��ѯ���ս��
			if (reths != null) {
				getBillCardPanel().setHeadItem("nreceiptcathmny", reths.get("queryCachPayByOrdId"));
			}

			updateCacheVO();

			// ��Ʊ���Ƿ��޸�
			m_isCodeChanged = false;

			showCustManArInfo(reths);

			getBillCardPanel().updateValue();

			// ���������ɫ����
			InvVO[] invvos = new InvVO[getBillCardPanel().getRowCount()];
			for (int i = 0; i < invvos.length; i++) {
				invvos[i] = new InvVO();
				invvos[i].setCinventoryid((String) getBillCardPanel().getBodyValueAt(i,
						"cinventoryid"));
			}

			InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
			invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

			ArrayList al_invs = new ArrayList();
			for (InvVO invvo : invvos) {
				al_invs.add(invvo);
			}

			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), al_invs);

			// ���֧��
			getPluginProxy().afterAction(Action.SAVE, new SaleOrderVO[] { saleorder });

			updateUI();

			return true;
		} catch (AtpSetException atpsetex) {

			showWarningMessage(atpsetex.getMessage());
			getBillCardTools().processAtpSetException(atpsetex.getMessage());
			return false;
		} catch (Exception e) {

			if (e instanceof ValidationException) // ��������ɫ���
				nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillCardPanel(), rowList);

			// ��ʶ����
			if (e.getClass() != AtpCheckException.class
					|| (e.getClass() == AtpCheckException.class))/*-=notranslate=-*/

				showWarningMessage(e.getMessage());
			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000185")/*
																								 * @res
																								 * "����ʧ�ܣ�"
																								 */);
			markRow(e.getMessage());
			SCMEnv.out(e);
			return false;
		}
	}

	/**
	 * ɾ��ʱ��д�ɹ�������
	 * 
	 * @param poID
	 * @param isReferred
	 * @throws Exception
	 */
	private void reWritePO(String[] poID, boolean isReferred) throws Exception {

		nc.itf.po.IOrder bo = (nc.itf.po.IOrder) nc.bs.framework.common.NCLocator.getInstance()
				.lookup(nc.itf.po.IOrder.class.getName());
		bo.updateCoopFlag(isReferred, poID, null, null, getClient().getUser().getPrimaryKey());
		// bo.updateCoopFlag(isReferred, poID);
	}

	/**
	 * ���Ӹ��ƹ��ܣ����ӶԸ��Ƶ��ݵı���֧�֡�
	 * 
	 * �������ڣ�(2003-8-25 10:21:54)
	 * 
	 */
	protected void onSaveCopyBill() {
		if (!onSaveExt())
			return;
		// �����ĸ��Ƶ��ݱ���ɹ��󣬽�����صĺ�������
		SaleOrderVO oldsaleorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
				SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
				SaleorderBVO.class.getName());
		if (oldsaleorder != null) {

			vIDs.add(((SaleorderHVO) oldsaleorder.getParentVO()).getCsaleid());
			num = vIDs.size() - 1;
			addCacheVO();

		}
		getBillCardPanel().updateValue();
	}

	/**
	 * ����ǰ�������
	 */
	private void fillDataBeforeSave(SaleOrderVO saleorder, SaleOrderVO oldsaleorder)
			throws Exception {

		if (strState.equals("�޸�")) { /*-=notranslate=-*/
			saleorder.setPrimaryKey(getBillID());
			// ����״̬
			int iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus").getValue());

			if (iStatus == BillStatus.NOPASS) {
				// ����VO״̬
				if (saleorder != null && saleorder.getBodyVOs() != null) {
					ArrayList dellinelist = new ArrayList();
					for (int i = 0, loop = saleorder.getBodyVOs().length; i < loop; i++) {
						if (saleorder.getBodyVOs()[i].getStatus() == VOStatus.DELETED) {
							dellinelist.add(saleorder.getBodyVOs()[i]);
						}
					}
					if (dellinelist.size() > 0) {
						SaleorderBVO[] newbvos = new SaleorderBVO[oldsaleorder.getBodyVOs().length
								+ dellinelist.size()];
						System.arraycopy(oldsaleorder.getBodyVOs(), 0, newbvos, 0, oldsaleorder
								.getBodyVOs().length);
						int pos = 0;
						for (int i = oldsaleorder.getBodyVOs().length, loop = newbvos.length; i < loop; i++) {
							newbvos[i] = (SaleorderBVO) dellinelist.get(pos);
							pos++;
						}
						oldsaleorder.setChildrenVO(newbvos);
					}
				}
				oldsaleorder.setStatus(VOStatus.UPDATED);
				oldsaleorder.getParentVO().setStatus(VOStatus.UPDATED);
				((SaleorderHVO) oldsaleorder.getParentVO())
						.setFstatus(new Integer(BillStatus.FREE));
				// �ӱ�VO״̬
				SaleorderBVO[] voBodys = (SaleorderBVO[]) oldsaleorder.getChildrenVO();
				for (int i = 0; i < voBodys.length; i++) {
					if (voBodys[i].getStatus() == VOStatus.UNCHANGED)
						voBodys[i].setStatus(VOStatus.UPDATED);
					voBodys[i].setFrowstatus(new Integer(BillStatus.FREE));
					// ����ATP���·�ʽ
					voBodys[i].setIAction(ISaleOrderAction.A_DEFAULT);
				}

				// ��ȡ�༭ǰ��VO
				SaleOrderVO oldvo = getBillCardTools().getOldsaleordervo();
				for (int i = 0, loop = oldvo.getBodyVOs().length; i < loop; i++) {
					oldvo.getBodyVOs()[i].setIAction(ISaleOrderAction.A_DEFAULT);
				}

				oldsaleorder.setOldSaleOrderVO(oldvo);
				// ���ø���VO����
				oldsaleorder.setIAction(ISaleOrderAction.A_SPECIALADD);

				// �����޸ĺ������VO
				saleorder.setAllSaleOrderVO((SaleOrderVO) getBillCardPanel().getBillValueChangeVO(
						SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
						SaleorderBVO.class.getName()));

				oldsaleorder.processVOForTrans();

			} else {

				// ɾ�������е�ȱ�������
				saleorder = remarkOOSLine(saleorder);
				oldsaleorder = remarkOOSLine(oldsaleorder);

				oldsaleorder.setStatus(VOStatus.UPDATED);

				// ��ȡ�༭ǰ��VO
				SaleOrderVO oldvo = getBillCardTools().getOldsaleordervo();
				if (oldvo.getHeadVO().getNheadsummny().compareTo(
						oldsaleorder.getHeadVO().getNheadsummny()) != 0
						|| oldvo.getHeadVO().getNdiscountrate().compareTo(
								oldsaleorder.getHeadVO().getNdiscountrate()) != 0) {
					// �ӱ�VO״̬
					SaleorderBVO[] voBodys = (SaleorderBVO[]) oldsaleorder.getChildrenVO();
					for (int i = 0; i < voBodys.length; i++)
						voBodys[i].setStatus(VOStatus.UPDATED);
				}

				// ��ȡ�༭ǰ��VO
				saleorder.setOldSaleOrderVO(getBillCardTools().getOldsaleordervo());
				// ���ø���VO����
				saleorder.setIAction(ISaleOrderAction.A_EDIT);
				// �����޸ĺ������VO
				saleorder.setAllSaleOrderVO(oldsaleorder);

				saleorder.processVOForTrans();

			}

			saleorder.getHeadVO().setAttributeValue("dmoditime", ClientEnvironment.getServerTime());

		} else if (strState.equals("�޶�")) { /*-=notranslate=-*/

			saleorder.setPrimaryKey(getBillID());
			// actionname = "OrderAlter";
			// saleorderch = saleorder;

			// �汾���Լ��޶��˵�
			doEditBeforeModify(saleorder);

			// ɾ�������е�ȱ�������
			oldsaleorder = remarkOOSLine(oldsaleorder);
			oldsaleorder.setStatus(VOStatus.UPDATED);

			// ��ȡ�༭ǰ��VO
			saleorder.setOldSaleOrderVO(getBillCardTools().getOldsaleordervo());
			// ���ø���VO����
			saleorder.setIAction(ISaleOrderAction.A_MODIFY);
			// �����޸ĺ������VO
			saleorder.setAllSaleOrderVO(oldsaleorder);

			nc.vo.scm.pub.session.ClientLink clientLink = new nc.vo.scm.pub.session.ClientLink(
					getClientEnvironment());
			saleorder.setClientLink(clientLink);

			SaleorderBVO[] ordbvos = saleorder.getBodyVOs();
			if (ordbvos != null && ordbvos.length > 0) {
				UFDouble ntotalconvertnum = null;
				for (int i = 0, loop = ordbvos.length; i < loop; i++) {
					ntotalconvertnum = SoVoConst.duf0;
					// +narrangescornum
					ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i]
							.getNarrangescornum());
					// +narrangepoapplynum
					ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i]
							.getNarrangepoapplynum());
					// +narrangetoornum
					ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i]
							.getNarrangetoornum());
					// +norrangetoapplynum
					ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i]
							.getNorrangetoapplynum());
					// +narrangemonum
					ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i]
							.getNarrangemonum());
					if (ntotalconvertnum != null && ordbvos[i].getNnumber() != null
							&& ntotalconvertnum.abs().compareTo(ordbvos[i].getNnumber().abs()) >= 0) {
						ordbvos[i].setBarrangedflag(SoVoConst.buftrue);
					}
				}
			}

			saleorder.processVOForTrans();
		}

		// ״̬
		SaleorderBVO[] ordbvos = saleorder.getBodyVOs();
		for (SaleorderBVO ordbvo : ordbvos) {
			if (ordbvo.getStatus() == VOStatus.UPDATED
					&& (ordbvo.getCorder_bid() == null || ordbvo.getCorder_bid().length() == 0))
				ordbvo.setStatus(VOStatus.NEW);
		}
	}

	/**
	 * �������ݣ����ӶԶ�����������и����������ݱ����֧�֣��򶩵� �����Ĵ������׼����¼�벻ͬ����
	 * 
	 * ������ �� ����ֵ��boolean,�ɹ�����true
	 * 
	 * �������ڣ�(2003-8-25 10:21:54)
	 * 
	 */
	protected boolean onSaveExt() {

		boolean bret = false;
		String actionname = null;
		if (strState.equals("����")) {/*-=notranslate=-*/
			actionname = "PreKeep";
		} else if (strState.equals("�޸�")) {/*-=notranslate=-*/
			actionname = "PreModify";
		}
		SaleOrderVO saleorder = null;

		try {
			long s = System.currentTimeMillis();
			getBillCardPanel().cleanNullLine();
			saleorder = (SaleOrderVO) getBillCardPanel().getBillValueChangeVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());
			SaleOrderVO saleoldorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());

			// ��������
			((SaleorderHVO) saleorder.getParentVO()).setCreceipttype(getBillType());
			((SaleorderHVO) saleoldorder.getParentVO()).setCreceipttype(getBillType());
			getBillCardPanel().setHeadItem("creceipttype", getBillType());
			// ҵ������(����Эͬʱ��������)
			if (saleorder.getHeadVO().getAttributeValue("ccooppohid") != null) {
				getBillCardPanel().setBusiType(coBusiType);
				((SaleorderHVO) saleorder.getParentVO()).setCbiztype(coBusiType);
				getBillCardPanel().setHeadItem("cbiztype", coBusiType);

			} else {
				((SaleorderHVO) saleorder.getParentVO()).setCbiztype(getBillCardPanel()
						.getBusiType());
				getBillCardPanel().setHeadItem("cbiztype", getBillCardPanel().getBusiType());
			}

			// ���¼����ͷ��˰�ϼ�
			/** ȥ��ȱ����Ӱ��* */
			saleorder.reCalHeadSummny();

			// yt add 2003-11-07
			if (calculatePreceive(saleoldorder) == false)
				return false;
			// ���
			if (strState.equals("����"))/*-=notranslate=-*/
				onCheck(saleorder);
			else
				onCheck(saleoldorder);
			// ����к�
			saleorder = getLineNumber(saleoldorder, saleorder);
			// �����к�
			if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), getBillCardPanel()
					.getRowNoItemKey()))
				return bret;
			// ����״̬
			((SaleorderHVO) saleorder.getParentVO()).setFstatus(new Integer(BillStatus.FREE));
			// ��˾����
			((SaleorderHVO) saleorder.getParentVO()).setPk_corp(getCorpPrimaryKey());
			// �ջ���ַ
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			saleorder.getHeadVO().setVreceiveaddress(vreceiveaddress.getUITextField().getText());

			if (saleorder.getChildrenVO() != null) {
				for (int i = 0; i < saleorder.getChildrenVO().length; i++) {
					SaleorderBVO bodyVO = (SaleorderBVO) saleorder.getChildrenVO()[i];
					// ��˾����
					bodyVO.setPkcorp(getCorpPrimaryKey());
					if (bodyVO.getStatus() == VOStatus.NEW)
						bodyVO.setPrimaryKey(null);
				}
			}

			saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
			saleorder.setDcurdate(getClientEnvironment().getDate());
			saleorder.setCusername(getClientEnvironment().getUser().getUserName());
			saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());

			saleoldorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
			saleoldorder.setDcurdate(getClientEnvironment().getDate());

			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000136")/*
																								 * @res
																								 * "��ʼ��������...."
																								 */);
			if (saleorder.getHeadVO().isCoopped()) {
				saleorder.getHeadVO().setCoopPoId(coPoOrderId);
			}

			if (coPoTs != null && saleorder.isCoopped()) {
				// ЭͬtsУ��
				((SaleorderHVO) saleorder.getParentVO()).setCoopPoTs(coPoTs);

			}

			SCMEnv.out("��ʼ���棺" + System.currentTimeMillis());
			// ��һ�α����ͬ�����д
			saleorder.setFirstTime(true);
			boolean bContinue = true;
			// HashMap reths = null;
			// ���ø���VO����
			saleorder.setIAction(ISaleOrderAction.A_ADD);

			saleorder.setStatus(nc.vo.pub.VOStatus.NEW);
			saleorder.processVOForTrans();
			nc.vo.scm.pub.session.ClientLink clientLink = new nc.vo.scm.pub.session.ClientLink(
					getClientEnvironment());
			saleorder.setClientLink(clientLink);

			// ���֧��
			getPluginProxy().beforeAction(Action.SAVE, new SaleOrderVO[] { saleorder });

			while (bContinue) {
				try {
					if (strState.equals("����")) {/*-=notranslate=-*/
						UFDateTime ud = ClientEnvironment.getServerTime();
						saleorder.getHeadVO().setAttributeValue("dbilltime", ud);
						// �ܳ�ʤ
						String editionNum = saleorder.getHeadVO().getEditionNum();
						int editionNumNew = new UFDouble(editionNum == null ? "1.0" : editionNum
								.trim()).intValue();
						editionNum = String.valueOf(editionNumNew) + ".0";
						saleorder.getHeadVO().setEditionNum(editionNum);
						getBillCardPanel().setHeadItem("editionnum", editionNum);
						// �ܳ�ʤ
						ArrayList alistID = (ArrayList) PfUtilClient.processActionNoSendMessage(
								this, "PreKeep", getBillType(), getClientEnvironment().getDate()
										.toString(), saleorder, null, null, null);
						getBillCardPanel().setTailItem("dbilltime", ud);
						id = (String) alistID.get(0);
						loadIDafterADD(alistID);
					}

					bContinue = false;
				} catch (Exception ex) {
					bContinue = doException(saleorder, null, ex);
					if (!bContinue)
						return false;
				}
			}
			SCMEnv.out("���������" + System.currentTimeMillis());

			// if (saleorder.getHeadVO().isCoopped() ) {
			//
			// String sourceId = (String) saleorder.getHeadVO().getCoopPoId();
			// reWritePO(new String[] { sourceId }, true);
			//
			// }

			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000232",
					null, new String[] { (System.currentTimeMillis() - s) / 1000 + "" }));
			// ����״̬
			strState = "����";
			bret = true;

			// getBillCardTools().setHeadRefLimit(strState);

			vRowATPStatus = new Vector();
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				vRowATPStatus.addElement(new UFBoolean(false));
			}
			getBillCardPanel().updateValue();

			// ���������ɫ����
			try {
				InvAttrCellRenderer ficr = new InvAttrCellRenderer();
				ficr.setFreeItemRenderer(getBillCardPanel(), null);
			} catch (Exception einv) {

			}

			// ���֧��
			getPluginProxy().afterAction(Action.SAVE, new SaleOrderVO[] { saleorder });

		} catch (AtpSetException atpsetex) {

			showWarningMessage(atpsetex.getMessage());
			getBillCardTools().processAtpSetException(atpsetex.getMessage());
		} catch (Exception ex) {

			if (ex instanceof ValidationException) // ��������ɫ���
				nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillCardPanel(), rowList);

			// ��ʶ����
			handleSaveException(getBillType(), getBillCardPanel().getBusiType(), actionname,
					getBillCardPanel().getCorp(), getBillCardPanel().getOperator(), saleorder, ex);
			markRow(ex.getMessage());
			// ex.printStackTrace();
			return bret;
		}
		return bret;
	}

	protected void onSendAudit() {
		// ����״̬
		if (!"����".equals(strState)) {/*-=notranslate=-*/
			onSave();
		}
		if (!"����".equals(strState)) {/*-=notranslate=-*/
			return;
		}
		SaleOrderVO saleorder = (SaleOrderVO) getVO(false);
		if (saleorder == null || saleorder.getParentVO() == null) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000199")/*
																									 * @res
																									 * "��ѡ�񵥾�"
																									 */);
		} else {
			if (!getClientEnvironment().getUser().getPrimaryKey().equals(
					saleorder.getHeadVO().getCoperatorid())) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000505")/*
												 * @res "�������Ƶ�������"
												 */);
				return;
			}
			try {
				boolean isExist = false;
				isExist = nc.ui.pub.pf.PfUtilBO_Client.isExistWorkFlow(getBillType(), saleorder
						.getBizTypeid(), getClientEnvironment().getCorporation().getPk_corp(),
						getClientEnvironment().getUser().getPrimaryKey());

				if (isExist == false) {
					showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000111")/*
													 * @res "�ò���Աû������������"
													 */);
					return;
				}

				saleorder.validate();

				int iWorkflowstate = 0;
				iWorkflowstate = nc.ui.pub.pf.PfUtilClient.queryWorkFlowStatus(saleorder
						.getBizTypeid(), getBillType(), saleorder.getParentVO().getPrimaryKey());

				// ���ݵ�������״̬Ӧ��Ϊ���ڹ������л���δ��ʼ����������δͨ��
				if (iWorkflowstate != nc.vo.pub.pf.IWorkFlowStatus.NOT_STARTED_IN_WORKFLOW
						&& iWorkflowstate != nc.vo.pub.pf.IWorkFlowStatus.BILL_NOT_IN_WORKFLOW
						&& iWorkflowstate != nc.vo.pub.pf.IWorkFlowStatus.NOT_APPROVED_IN_WORKFLOW) {

					if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.WORKFLOW_FINISHED) {
						showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000078")/*
														 * @res "�������������"
														 */);
						return;
					} else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.WORKFLOW_ON_PROCESS) {
						showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000079")/*
														 * @res "��������������"
														 */);
						return;
					} else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.NOT_APPROVED_IN_WORKFLOW) {
						showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000071")/*
														 * @res "��������δͨ��"
														 */);
						return;
					} else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.BILLTYPE_NO_WORKFLOW) {
						showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000112")/*
														 * @res "�õ�������û�����ù�����"
														 */);
						return;
					} else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.BILL_NOT_IN_WORKFLOW) {
						showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000070")/*
														 * @res
														 * "�������������˹����������õ���û���ڹ�������"
														 */);
						return;
					}
				}
				nc.vo.scm.pub.session.ClientLink clientLink = new nc.vo.scm.pub.session.ClientLink(
						getClientEnvironment());
				saleorder.setClientLink(clientLink);

				saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
				saleorder.setDcurdate(getClientEnvironment().getDate());
				saleorder.setCusername(getClientEnvironment().getUser().getUserName());
				saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());

				saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000504")
				/* @res "ӆ�ι���" */);

				saleorder.setIAction(ISaleOrderAction.A_SENDAUDIT);
				String ErrMsg = null;

				// v5.5��������(�����õ�key��NULL)
				/*
				 * AggregatedValueObject transVO =
				 * TransferVOProcessUtil.transferProcessBySetNull(saleorder,
				 * nc.vo.so.pub.TransferVOProcessField.closeOPDown_HeadKeys_4331,
				 * nc.vo.so.pub.TransferVOProcessField.closeOPDown_BodyKeys_4331,
				 * SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
				 * SaleorderBVO.class.getName());
				 */

				ArrayList al = (ArrayList) PfUtilClient.processAction(this, "SAVE", getBillType(),
						getClientEnvironment().getDate().toString(), saleorder, null);

				ErrMsg = (String) al.get(1);
				if (ErrMsg != null && ErrMsg.startsWith("ERR")) {
					ErrMsg = ErrMsg.substring(3);
					if (ErrMsg != null && ErrMsg.trim().length() > 0) {
						showWarningMessage(ErrMsg);
						if (ErrMsg.indexOf("����") > 0)
							getBillCardPanel().setHeadItem("boverdate", new UFBoolean(true));
					}
				}

				if (PfUtilClient.isSuccess()) {
					BillTools.reLoadBillState(this, getClientEnvironment());

					setButtonsState();
					showHintMessage(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000291")/*
													 * @res "����ɹ�!"
													 */);
				} else {
					showHintMessage(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000292")/*
													 * @res "��������Ѿ����û�ȡ����"
													 */);
				}
				reLoadTS();
				updateCacheVO();
			} catch (BusinessException e) {
				showWarningMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000293")/*
												 * @res "����ʧ��:"
												 */
						+ e.getMessage());
			} catch (Exception e) {
				showWarningMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000293")/*
												 * @res "����ʧ��:"
												 */
						+ e.getMessage());
			}
		}
	}

	/**
	 * ���¼���TS��
	 * 
	 * �������ڣ�(2002-7-11 9:51:36)
	 * 
	 */
	private void reLoadTS() {
		try {
			if (getBillListPanel().isShowing())
				num = getBillListPanel().getHeadTable().getSelectedRow();
			int temp = num;
			String sBillID = getBillID();

			// ���¼��ر�ͷTS
			if (sBillID != null) {
				if (strShowState.equals("�б�")) { /*-=notranslate=-*/

					String sql = "select so_sale.csaleid,"// 0
							+ "so_sale.vreceiptcode," + "so_sale.fstatus,"
							+ "so_sale.ts,"
							+ "so_sale.capproveid "// 4
							+ "from so_sale " + "where so_sale.csaleid='" + sBillID.trim() + "'";

					nc.vo.so.so001.SORowData[] rows = null;

					rows = nc.ui.so.so016.SOToolsBO_Client.getSORows(sql);

					if (rows != null && rows.length > 0) {

						Integer fstatus = rows[0].getInteger(2);

						String value = SaleOrderVO.getBillState(fstatus);
						// ���ı�ͷ����
						getBillListPanel().getHeadBillModel().setValueAt(rows[0].getUFDateTime(3),
								temp, "ts");
						getBillListPanel().getHeadBillModel().setValueAt(value, temp, "fstatus");

						if (fstatus.intValue() == BillStatus.AUDIT
								|| fstatus.intValue() == BillStatus.FINISH
								|| fstatus.intValue() == BillStatus.FREEZE) {
							getBillListPanel().getHeadBillModel().setValueAt(
									getClientEnvironment().getUser().getPrimaryKey().toString(),
									temp, "capproveid");
							getBillListPanel().getHeadBillModel().setValueAt(
									getClientEnvironment().getUser().getUserName().toString(),
									temp, "capprovename");
							getBillListPanel().getHeadBillModel().setValueAt(
									getClientEnvironment().getDate().toString(), temp,
									"dapprovedate");
						} else {
							getBillListPanel().getHeadBillModel().setValueAt(null, temp,
									"capproveid");
							getBillListPanel().getHeadBillModel().setValueAt(null, temp,
									"capprovename");
							getBillListPanel().getHeadBillModel().setValueAt(null, temp,
									"dapprovedate");
						}
						// ���ı�������
						if (getBillListPanel().getBodyBillModel() != null) {
							if (getBillListPanel().getBodyTable().getRowCount() > 0) {
								for (int i = 0; i < getBillListPanel().getBodyBillModel()
										.getRowCount(); i++) {
									getBillListPanel().getBodyBillModel().setValueAt(value, i,
											"frowstatus");
								}
							}
						}
					}
				} else {
					String formula[] = { "ts->getColValue(so_sale,ts,csaleid,csaleid)",

					"vreceiptcode->getColValue(so_sale,vreceiptcode,csaleid,csaleid)" };

					getBillCardPanel().execHeadFormulas(formula);

				}
			}
			updateUI();
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("���¼��ر�ͷTSʧ��.");
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}
	}

	/**
	 * ɾ�������е�ȱ������С�
	 * 
	 * �������ڣ�(2001-4-13 16:38:18)
	 * 
	 */
	private SaleOrderVO remarkOOSLine(SaleOrderVO voSource) {
		// ѡ��Ϊȱ���Ĵ�����Ϊɾ��״̬
		SaleorderBVO[] voBodys = (SaleorderBVO[]) voSource.getChildrenVO();
		for (int i = 0; i < voBodys.length; i++) {
			if (voBodys[i].getBoosflag() != null && voBodys[i].getBoosflag().booleanValue()) {
				if (voSource.getChildrenVO()[i].getStatus() == VOStatus.UPDATED) {
					voSource.getChildrenVO()[i].setStatus(VOStatus.DELETED);
					// Ϊȱ���Ǽ��޸ļ�¼���е�Լ��
					((SaleorderBVO[]) voSource.getChildrenVO())[i].setBsupplyflag(new UFBoolean(
							true));
				}
			}
		}
		return voSource;
	}

	/**
	 * ɾ�������е�ȱ������С�
	 * 
	 * �������ڣ�(2001-4-13 16:38:18)
	 * 
	 */
	private void removeOOSLine() {
		int iRowCount = getBillCardPanel().getBillTable().getRowCount();
		Vector vecOOSLine = new Vector();
		for (int i = iRowCount - 1; i >= 0; i--) {
			UFBoolean boosflag = getBillCardPanel().getBodyValueAt(i, "boosflag") == null ? new UFBoolean(
					false)
					: new UFBoolean(getBillCardPanel().getBodyValueAt(i, "boosflag").toString());
			if (boosflag.booleanValue()) {
				if (getBillCardPanel().alInvs != null && i < getBillCardPanel().alInvs.size()) {
					getBillCardPanel().alInvs.remove(i);
				}
				if (vRowATPStatus != null && vRowATPStatus.size() > 0 && i < vRowATPStatus.size()) {
					vRowATPStatus.remove(i);
				}
				vecOOSLine.addElement(i + "");
			}
		}
		int[] aryRow = new int[vecOOSLine.size()];
		for (int i = 0; i < vecOOSLine.size(); i++) {
			aryRow[i] = new Integer(vecOOSLine.elementAt(i).toString()).intValue();
		}

		getBillCardPanel().getBillModel().delLine(aryRow);
	}

	/**
	 * ���ñ��巢���͵�������Ĭ�����ݡ�
	 * 
	 * �������ڣ�(2001-8-27 10:05:59)
	 * 
	 */
	private void setBodyDate(int row) {
		// ��������
		UFDate dbilldate = new UFDate(getBillCardPanel().getHeadItem("dbilldate").getValue());
		// ��������
		getBillCardPanel().setBodyValueAt(dbilldate.toString(), row, "dconsigndate");
		// ��������
		getBillCardPanel().afterBodyDateEdit(row, true);
		String sDateDeliver = getBillCardPanel().getBodyValueAt(row, "ddeliverdate") == null ? null
				: getBillCardPanel().getBodyValueAt(row, "ddeliverdate").toString().trim();
		if (sDateDeliver == null || sDateDeliver.length() == 0) {
			getBillCardPanel().setBodyValueAt(dbilldate.toString(), row, "ddeliverdate");
		}
	}

	/**
	 * ���ð�ť״̬�� �������ڣ�(2001-3-17 9:00:09)
	 */
	private void setBomButtonsState() {
		// ��ֻ֤�ܶ����޸�ʱ�ſɽ��в���
		if (strOldState.equals("����") || strOldState.equals("�޸�")) {/*-=notranslate=-*/
			if (strBomState.equals("ADD")) {
				boBomSave.setEnabled(true);
				boBomEdit.setEnabled(false);
				boBomCancel.setEnabled(true);
				boBomPrint.setEnabled(false);
				boBomReturn.setEnabled(false);
			}
			if (strBomState.equals("FREE")) {
				boBomSave.setEnabled(false);
				boBomEdit.setEnabled(true);
				boBomCancel.setEnabled(false);
				boBomPrint.setEnabled(true);
				boBomReturn.setEnabled(true);
				getBillTreeCardPanel().setEnabled(false);
			}
			if (strBomState.equals("SAVE")) {
				boBomSave.setEnabled(false);
				boBomEdit.setEnabled(true);
				boBomCancel.setEnabled(false);
				boBomPrint.setEnabled(true);
				boBomReturn.setEnabled(true);
				getBillTreeCardPanel().setEnabled(false);
			}
			if (strBomState.equals("EDIT")) {
				boBomSave.setEnabled(true);
				boBomEdit.setEnabled(false);
				boBomCancel.setEnabled(true);
				boBomPrint.setEnabled(false);
				boBomReturn.setEnabled(false);
			}
			if (strBomState.equals("CANCEL")) {
				boBomSave.setEnabled(false);
				boBomEdit.setEnabled(false);
				boBomCancel.setEnabled(true);
				boBomPrint.setEnabled(false);
				boBomReturn.setEnabled(true);
			}
			setBomPrice();
		} else {
			boBomSave.setEnabled(false);
			boBomEdit.setEnabled(false);
			boBomCancel.setEnabled(false);
			boBomPrint.setEnabled(true);
			boBomReturn.setEnabled(true);
			getBillTreeCardPanel().setEnabled(false);
		}
		updateButtons();
	}

	/**
	 * ����״̬���ð�ť �������ڣ�(01-2-26 13:29:17)
	 */
	private void setButtons() {

		if (strShowState.equals("�б�")) { /*-=notranslate=-*/
			setButtons(aryListButtonGroup, "40060301");
		} else if (strShowState.equals("��Ƭ")) { /*-=notranslate=-*/
			setButtons(aryButtonGroup, "40060301");
		} else {
			boLine.removeAllChildren();
			boLine.addChildButton(boDelLine);
			setButtons(aryBatchButtonGroup, "40060301");
		}
	}

	/**
	 * ���ð�ť״̬��
	 * 
	 * �������ڣ�(2001-3-17 9:00:09)
	 * 
	 */
	public void setButtonsState() {

		if (bInMsgPanel)
			return;

		if (strState.equals("BOM"))
			setBomButtonsState();
		else if (strShowState.equals("�б�")) {
			setListButtonsState();
		} else {
			setCardButtonsState();
		}

		// ���ο�����չ
		getPluginProxy().setButtonStatus();

	}

	/**
	 * �����б�ť״̬��
	 * 
	 * �������ڣ�(2001-3-17 9:00:09)
	 * 
	 */
	private void setListButtonsState() {

		// �ڵ�ǰҵ�����͵ĵ�����Դ���������Ƶ���ʱ�����ܽ��и���
		// ���Ƶĵ��ݾ�Ϊ���Ƶ��ݡ��ڴ˽����ж�
		PfUtilClient.retAddBtn(boAdd, getCorpPrimaryKey(), getBillType(), boBusiType);

		boBusiType.setEnabled(true);
		boAdd.setEnabled(true);

		boBrowse.setEnabled(true);

		int selectRowCount = getBillListPanel().getHeadTable().getSelectedRowCount();
		if (selectRowCount > 1) {
			boMaintain.setEnabled(true);
			boBlankOut.setEnabled(true);
			boAudit.setEnabled(true);
			boAction.setEnabled(true);
			boSendAudit.setEnabled(true);
			boCancelAudit.setEnabled(true);
			boSendAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boRefundment.setEnabled(false);
			boQuery.setEnabled(true);
			boCard.setEnabled(true);
			boPrntMgr.setEnabled(true);
			boPreview.setEnabled(true);
			boPrint.setEnabled(true);
			boSplitPrint.setEnabled(true);
			boAssistant.setEnabled(true);
			boAsstntQry.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);
		} else if (selectRowCount == 1) {
			boMaintain.setEnabled(true);
			boEdit.setEnabled(true);
			boAction.setEnabled(true);
			boPrntMgr.setEnabled(true);
			boPrint.setEnabled(true);
			boSplitPrint.setEnabled(true);
			boPreview.setEnabled(true);
			boAssistant.setEnabled(true);
			boAsstntQry.setEnabled(true);
			boAfterAction.setEnabled(true);
			boBatch.setEnabled(false);
			boRefundment.setEnabled(true);
			boQuery.setEnabled(true);
			boCard.setEnabled(true);
			boDocument.setEnabled(true);
			boOrderQuery.setEnabled(true);

			int ccount = boAdd.getChildCount();
			ButtonObject[] bos = boAdd.getChildButtonGroup();
			boolean bCanCopyFlag = false;
			for (int i = 0; i < ccount; i++) {
				if (bos[i].getTag().startsWith(SELFBILL)) {
					bCanCopyFlag = true;
					break;
				}
			}
			boCopyBill.setEnabled(bCanCopyFlag);

			boSendAudit.setEnabled(true);
			boAuditFlowStatus.setEnabled(true);
		} else {
			boMaintain.setEnabled(false);
			boEdit.setEnabled(false);
			boAudit.setEnabled(false);
			boAction.setEnabled(false);
			boPrntMgr.setEnabled(false);
			boPrint.setEnabled(false);
			boSplitPrint.setEnabled(false);
			boPreview.setEnabled(false);
			boAssistant.setEnabled(true);
			boAsstntQry.setEnabled(false);
			boBatch.setEnabled(false);
			boQuery.setEnabled(true);
			boCard.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boDocument.setEnabled(false);
			boOrderQuery.setEnabled(false);
			boCopyBill.setEnabled(false);
			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(false);
			boCoPushPo.setEnabled(false);

		}

		if (selectRow > -1
				&& getBillListPanel().getHeadBillModel().getValueAt(selectRow, "fstatus") != null) {
			// ����״̬
			int iStatus = Integer.parseInt(getBillListPanel().getHeadItem("fstatus").converType(
					getBillListPanel().getHeadBillModel().getValueAt(selectRow, "fstatus"))
					.toString());
			setBtnsByBillState(iStatus);
			// �˻����
			Object retinvflag = getBillListPanel().getHeadBillModel().getValueAt(selectRow,
					"bretinvflag");
			if (retinvflag != null && retinvflag.toString().equals("true")) {
				// ���ݵ���״̬���õ���
				boRefundment.setEnabled(false);
			}
		}

		if (getBillListPanel() != null && getBillListPanel().getHeadTable() != null) {
			if (getBillListPanel().getHeadTable().getRowCount() > 0) {
				boFind.setEnabled(true);
				getBoListDeselectAll().setEnabled(true);
				getBoListSelectAll().setEnabled(true);
			} else {
				boFind.setEnabled(false);
				getBoListDeselectAll().setEnabled(false);
				getBoListSelectAll().setEnabled(false);
			}
		} else {
			boFind.setEnabled(false);
		}

		boRefresh.setEnabled(b_query);

		/** ���б��²������õİ�ť* */
		boSave.setEnabled(false);
		boCancel.setEnabled(false);
		boLine.setEnabled(false);
		boFirst.setEnabled(false);
		boPre.setEnabled(false);
		boNext.setEnabled(false);
		boLast.setEnabled(false);
		getBoOnHandShowHidden().setEnabled(false);
		/** ���б��²������õİ�ť* */

		updateButtons();
	}

	protected final ButtonObject getBoAfterAction() {
		if (boAfterAction == null) {
			boAfterAction = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000128")/*
											 * @res "����ҵ��"
											 */, NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000128")/*
																						 * @res
																						 * "����ҵ��"
																						 */, 0, "����ҵ��"); /*-=notranslate=-*/
		}
		return boAfterAction;
	}

	/**
	 * ��������ʱ���ñ�ͷĬ�����ݡ�
	 * 
	 * �������ڣ�(2001-8-27 10:05:59)
	 * 
	 */
	private void setDefaultData(boolean isfree) {

		/** v5.3ȥ�������ֶ� * */
		/*
		 * // ���� if (isfree) getBillCardPanel().setHeadItem("naccountperiod",
		 * null);
		 */

		// ���۹�˾
		getBillCardPanel().setHeadItem("salecorp",
				getClientEnvironment().getCorporation().getUnitname());

		// ��˾
		getBillCardPanel().setHeadItem("pk_corp", getCorpPrimaryKey());

		// ҵ������
		getBillCardPanel().setHeadItem("cbiztype", getBillCardPanel().getBusiType());

		// �ͻ�
		getBillCardPanel().getHeadItem("ccustomerid").setEnabled(true);

		// ɢ��
		getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);

		// ���ݺ�
		getBillCardPanel().setHeadItem("vreceiptcode", null);

		// ��������
		getBillCardPanel().setHeadItem("dbilldate", getClientEnvironment().getDate().toString());

		// ����״̬
		getBillCardPanel().setHeadItem("fstatus", "1");

		// ҵ��Ա
		if (getBillCardPanel().getHeadItem("cemployeeid").getValueObject() == null) {
			String cemployeeid = getCemployeeId();
			getBillCardPanel().setHeadItem("cemployeeid", cemployeeid);
			((UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent())
					.setPK(cemployeeid);

			// ��ҵ��Ա��������
			getBillCardPanel().afterEmployeeEdit(null);
		}

		// �Ƶ�����
		getBillCardPanel().setTailItem("dmakedate", getClientEnvironment().getDate().toString());
		// �Ƶ���
		getBillCardPanel().setTailItem("coperatorid",
				getClientEnvironment().getUser().getPrimaryKey());
		// �������
		getBillCardPanel().setTailItem("capproveid", null);
		// �����
		getBillCardPanel().setTailItem("dapprovedate", null);

		// ����
		if (isfree) {
			// �����ۿ�
			getBillCardPanel().setHeadItem("ndiscountrate", 100.00 + "");
			// Ԥ���˷�
			getBillCardPanel().setHeadItem("nevaluatecarriage", 0.00 + "");
			// ���ݺ�
			((UIRefPane) getBillCardPanel().getHeadItem("vreceiptcode").getComponent())
					.getUITextField().setDelStr("+");
		} else {
			
			getBillCardPanel().initFreeItem();
			getBillCardPanel().initUnit();
			initCTTypeVO();

			getBillCardTools().setBodyInventory1(0, getBillCardPanel().getRowCount());

			// ȡ��������
			getBillCardTools().setBodyCchantypeid(0, getBillCardPanel().getRowCount());
			
			// ɢ��
			UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue("bfreecustflag");
			if (bfreecustflag == null || !bfreecustflag.booleanValue()) {
				getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
			} else {
				getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
			}
			// ����
			UIRefPane ccurrencytypeid = (UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent();
			if (getBillCardPanel().getRowCount() > 0) {
				Object oTemp = getBillCardPanel().getBodyValueAt(0, "ccurrencytypeid");
				if (oTemp != null && ((String) oTemp).trim().length() > 0) {

					ccurrencytypeid.setPK(getBillCardPanel().getBodyValueAt(0, "ccurrencytypeid"));
					// ����
					getBillCardPanel().setHeadItem("nexchangeotobrate",
							getBillCardPanel().getBodyValueAt(0, "nexchangeotobrate"));
					
					if (getBillCardTools().getBodyUFDoubleValue(0, "nexchangeotobrate") == null) {
						getBillCardPanel().afterCurrencyChange();
					} else {
						ctlCurrencyEdit();
					}
				} else {
					// Ĭ�Ͻ��ױ���
					String[] formulas = { "ccurrencytypeid->getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,ccustomerid)" };
					getBillCardPanel().getBillData().execHeadFormulas(formulas);
					// ����Ĭ��ֵ
					try {
						if (ccurrencytypeid.getRefPK() == null)
							ccurrencytypeid.setPK(CurrParamQuery.getInstance().getLocalCurrPK(getCorpPrimaryKey()));
						getBillCardPanel().afterCurrencyChange();
					} catch (java.lang.Exception e1) { }
				}
				// ���������ۿ�
				getBillCardPanel().setHeadItem("ndiscountrate",
						getBillCardTools().getBodyUFDoubleValue(0, "ndiscountrate"));
			}

			String headccurrencytypeid = getBillCardPanel().getHeadItem("ccurrencytypeid")
					.getValue();
			String headccurrencytypename = ccurrencytypeid.getRefName();
			// SCMEnv.out("::��ͷ����Ϊheadccurrencytypeid��"+headccurrencytypeid);
			// ����
			String headBrate = getBillCardPanel().getHeadItem("nexchangeotobrate").getValue();

			// ����״̬��ʽ,BOM��־
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				String[] formulas = {
						"wholemanaflag->getColValue(bd_invmandoc,wholemanaflag,pk_invmandoc,cinventoryid)",
						"isconfigable->getColValue(bd_invmandoc,isconfigable,pk_invmandoc,cinventoryid)",
						"isspecialty->getColValue(bd_invmandoc,isspecialty,pk_invmandoc,cinventoryid)" };
				getBillCardPanel().getBillModel().execFormulas(formulas);
			}

			// �ջ���λ�ȴ������
			getBillCardPanel().afterCreceiptcorpEdit();

			String[] keys = new String[] { "cconsigncorpid", // ������˾id
					"cconsigncorp", // ������˾
					"creccalbody", "creccalbodyid", "crecwarehouse", "crecwareid", "bdericttrans" };

			Object bodydate;
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {

				// ���ƶ๫˾�еı༭״̬
				getBillCardPanel().ctlUIOnCconsignCorpChg(i);

				// ����������
				String sDateBody = getBillCardPanel().getBodyValueAt(i, "dconsigndate") == null ? null
						: getBillCardPanel().getBodyValueAt(i, "dconsigndate").toString().trim();
				if (sDateBody == null || sDateBody.length() == 0) {
					setBodyDate(i);
				}

				/** ������ǰ�����ڵ���½����* */
				// ��������
				bodydate = getBillCardPanel().getBodyValueAt(i, "dconsigndate");
				if (bodydate == null
						|| bodydate.toString().compareTo(getClient().getDate().toString()) < 0) {
					getBillCardPanel().setBodyValueAt(getClient().getDate(), i, "dconsigndate");
				}

				// ��������
				bodydate = getBillCardPanel().getBodyValueAt(i, "ddeliverdate");
				if (bodydate == null
						|| bodydate.toString().compareTo(getClient().getDate().toString()) < 0) {
					getBillCardPanel().setBodyValueAt(getClient().getDate(), i, "ddeliverdate");
				}
				/** ������ǰ�����ڵ���½����* */

				// ����״̬
				getBillCardPanel().setBodyValueAt(new Integer(BillStatus.FREE), i, "frowstatus");

				// ��Ʒ
				if (getBillCardPanel().getBodyValueAt(i, "blargessflag") == null)
					getBillCardPanel().setBodyValueAt(new Boolean(false), i, "blargessflag");
				// ����
				getBillCardPanel().setBodyValueAt(headccurrencytypeid, i, "ccurrencytypeid");

				getBillCardPanel().setBodyValueAt(headccurrencytypename, i, "ccurrencytypename");

				// ��ͷ���ʴ������
				getBillCardPanel().setBodyValueAt(headBrate, i, "nexchangeotobrate");

				// ���ת����
				if (getSouceBillType().equals("4H") || getSouceBillType().equals("42")) {
					getBillCardTools().setBodyCellsEdit(keys, i, false);

					// �������ʣ�����������=0�����Ӧ�����ϵĸ�����Ӧ��null
					if (((UFDouble) getBillCardPanel().getBodyValueAt(i, "npacknumber"))
							.doubleValue() == 0)
						getBillCardPanel().setBodyValueAt(null, i, "npacknumber");
				}

			}

			// �۸���
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				// ��ͬ������������϶�����Ѱ�۴���
				if (getSouceBillType().equals(SaleBillType.SoContract)
						|| getSouceBillType().equals(SaleBillType.SoInitContract)
						|| getSouceBillType().equals("4H") || getSouceBillType().equals("42")
						|| getSouceBillType().equals("3B")) {
					boolean is3B = getSouceBillType().equals("3B");
					boolean isContract = false;
					if (getSouceBillType().equals(SaleBillType.SoContract)
							|| getSouceBillType().equals(SaleBillType.SoInitContract))
						isContract = true;
					int[] rows = new int[getBillCardPanel().getBillModel().getRowCount()];
					for (int i = 0, loop = getBillCardPanel().getBillModel().getRowCount(); i < loop; i++) {

						rows[i] = i;
						if (!is3B || !getSouceBillType().equals(SaleBillType.SoContract)) {
							// ����ձ����������ѱ�����v5.3���۽���㷨ʹ��
							// getBillCardPanel().setBodyValueAt(null,
							// i,"nquoteunitnum");

							/** ��ʹ�����㷨���߼���* */
							// ���㱨�۵���
							// BillTools.calcQPrice(i,
							// getBillCardPanel().getBillModel(),
							// SaleBillType.SaleOrder);
						}

						if (isContract) {
							if (SA_02.booleanValue()) {
								getBillCardPanel().calculateNumber(i, "norgqttaxprc");
							} else {
								getBillCardPanel().calculateNumber(i, "norgqtprc");
							}
						}

						// ��Ϊ��VO����ʱ��������һ����Թ���
						if (getBillCardPanel().getBodyValueAt(i, "assistunit") == null
								|| getBillCardPanel().getBodyValueAt(i, "cpackunitid") == null) {
							getBillCardPanel().setBodyValueAt(null, i, "npacknumber");
							getBillCardPanel().setBodyValueAt(null, i, "scalefactor");
						}
					}

					getBillCardPanel().afterNumberEdit(rows, "nnumber", null, false, true);

				}

				// ��ͷ��˰�ϼƼ���
				getBillCardPanel().getHeadItem("nheadsummny").setValue(
						getBillCardPanel().getTotalValue("noriginalcursummny"));
			}
			// /�۸���

		}

		// ���棬�ѱ��༭�����ۿ�ʱʹ��
		getBillCardPanel().ndiscountrate = getBillCardTools().getHeadValue("ndiscountrate");
	}

	/**
	 * �ɲ����˻��ҵ��Ա
	 * 
	 * @return
	 */
	private String getCemployeeId() {
		try {
			IUserManageQuery query = (IUserManageQuery) NCLocator.getInstance().lookup(
					"nc.itf.uap.rbac.IUserManageQuery");
			PsndocVO psn = query.getPsndocByUserid(getCorpPrimaryKey(), getClientEnvironment()
					.getUser().getPrimaryKey());

			if (psn != null)
				return psn.getPk_psndoc();
			else
				return null;
		} catch (BusinessException e) {
			SCMEnv.out(e);
			return null;
		}
	}

	/**
	 * �����в�����ť״̬��
	 * 
	 * �������ڣ�(2001-3-17 9:00:09)
	 * 
	 */
	private void setLineButtonsState() {
		// �ޱ�����
		if (getBillCardPanel().getRowCount() == 0) {
			boDelLine.setEnabled(false);
			boCopyLine.setEnabled(false);
			boPasteLine.setEnabled(false);
			boPasteLineToTail.setEnabled(false);
			boFindPrice.setEnabled(false);
			boCardEdit.setEnabled(false);
			boResortRowNo.setEnabled(false);
		} else {
			boDelLine.setEnabled(true);
			boCopyLine.setEnabled(true);
			boPasteLine.setEnabled(true);
			boPasteLineToTail.setEnabled(true);
			boFindPrice.setEnabled(true);
			boCardEdit.setEnabled(true);
			boResortRowNo.setEnabled(true);
		}
	}

	/**
	 * ���ò����޸��
	 * 
	 * �������ڣ�(2001-8-27 10:05:59)
	 * 
	 */
	private void setNoEditItem() {
		try {

			// �ȴ����ͷ
			// ���ֿ���
			UFDouble dcathpay = getBillCardTools().getHeadUFDoubleValue("nreceiptcathmny");
			if ((dcathpay != null && dcathpay.doubleValue() != 0) || strState.equals("�޶�")) {/*-=notranslate=-*/
				if (!isHeadCustCanbeModified())
					getBillCardPanel().getHeadItem("ccustomerid").setEnabled(false);
			}

			if (!strState.equals("�޶�")) {/*-=notranslate=-*/
				String formulas[] = {
				// �ͻ���������
						"ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)",
						// ɢ����־
						"bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,ccustbasid)" };
				getBillCardPanel().execHeadFormulas(formulas);

				UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue("bfreecustflag");
				if (bfreecustflag != null && bfreecustflag.booleanValue()) {
					getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
				} else {
					getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
				}
			}

			// getBillCardPanel().getHeadItem("dbilldate").setEnabled(false);
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				// ���ֲ��ɱ༭
				// getBillCardPanel().getHeadItem("ccurrencytypeid").setEnabled(false);
				String curID = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
				if (curID != null && curID.length() != 0) {
					if (CurrParamQuery.getInstance().isLocalCurrType(getCorpPrimaryKey(), curID)) {
						getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);
					} else
						getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);
				}
				if (strState.equals("�޶�")) {/*-=notranslate=-*/
					getBillCardPanel().getHeadItem("ccalbodyid").setEnabled(false);
					getBillCardPanel().getHeadItem("cwarehouseid").setEnabled(false);
				}
			}
			// �����ۿ�
			((UIRefPane) getBillCardPanel().getHeadItem("ndiscountrate").getComponent())
					.setMinValue(0.0);
			// v30dd
			String[] bodyItems = { "nqttaxnetprc", "nqtnetprc", "nqttaxprc", "nqtprc" };
			for (int k = 0; k < bodyItems.length; k++) {
				if (getBillCardPanel().getBodyItem(bodyItems[k]) != null)
					getBillCardPanel().getBodyItem(bodyItems[k]).setEnabled(false);

			}

			// �ɲ���������
			if (DRP04 != null && DRP04.booleanValue()) {
				if (getSouceBillType().equals(SaleBillType.SoDRP)) {
					getBillCardPanel().getBodyItem("nnumber").setEnabled(false);
					getBillCardPanel().getBodyItem("nquoteunitnum").setEnabled(false);
					getBillCardPanel().getBodyItem("npacknumber").setEnabled(false);
				}
			}

			// �ϸ�ִ�к�ͬ�۸��Ҳ�ѯ�ۣ���ͬ�۱��������������޸�
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {

				getBillCardPanel().setAssistUnit(i);
				getBillCardPanel().setCtItemEditable(i);
				getBillCardPanel().setScaleEditableByRow(i);
			}

			// ������Ѱ��
			if (getSouceBillType().equals(SaleBillType.SoDRP)) {
				for (int row = 0; row < getBillCardPanel().getRowCount(); row++) {
					if (getBillCardPanel().getBodyValueAt(row, "nnumber") != null) {
						getBillCardPanel().afterNumberEdit(new int[] { row }, "nnumber", null,
								false, true);
					}
				}
			}
			// �ɶ������ɷ�����,���¼��㸨����
			if (getSouceBillType().equals(SaleBillType.SaleOrder)) {
				for (int row = 0; row < getBillCardPanel().getRowCount(); row++) {
					if (getBillCardPanel().getBodyValueAt(row, "fixedflag") != null
							&& getBillCardPanel().getBodyValueAt(row, "fixedflag").equals(
									new Boolean(true))) {
						/*
						 * String[] formula = {
						 * "npacknumber->iif(nnumber=NULL,0,iif(nnumber=\"\",0,nnumber))/scalefactor" };
						 * getBillCardPanel().getBillModel().execFormula(row,
						 * formula);
						 */

						BillModel bm = getBillCardPanel().getBillModel();
						getBillCardPanel().setBodyValueAt(
								BillTools.calc(row, BillTools.value(row, "nnumber",
										new UFDouble(0), bm), "scalefactor", BillTools.div, bm),
								row, "npacknumber");
					}
				}
			}
			// ɢ��
			if (getBillCardPanel().getHeadItem("bfreecustflag").getValue() == null
					|| getBillCardPanel().getHeadItem("bfreecustflag").getValue().equals("false")) {
				getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
			} else {
				getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
			}
			// ����
			UFBoolean isDiscount = null;
			UFBoolean isLabor = null;
			for (int i = 0, iLen = getBillCardPanel().getRowCount(); i < iLen; i++) {
				// setAssistUnit(i);
				if (getBillType().equals(SaleBillType.SaleOrder)) {
					// ����
					Object temp = getBillCardPanel().getBodyValueAt(i, "wholemanaflag");
					boolean wholemanaflag = (temp == null ? false : new UFBoolean(temp.toString())
							.booleanValue());
					getBillCardPanel().setCellEditable(i, "fbatchstatus", wholemanaflag);
					getBillCardPanel().setCellEditable(i, "cbatchid", wholemanaflag);
				}

				isDiscount = getBillCardTools().getBodyUFBooleanValue(i, "discountflag");
				isLabor = getBillCardTools().getBodyUFBooleanValue(i, "laborflag");

				if ((isDiscount != null && isDiscount.booleanValue())
						|| (isLabor != null && isLabor.booleanValue())) {

					getBillCardTools().setBodyCellsEdit(
							new String[] { "cconsigncorp", "creccalbody", "crecwarehouse",
									"bdericttrans", "boosflag", "bsupplyflag" }, i, false);
				}

				/** SA_07�Ѳ�ʹ�� v5.3 fangchan zhangcheng */
				/*
				 * if (SA_15 != null && SA_15.booleanValue() && SA_07 != null &&
				 * !SA_07.booleanValue()) { getBillCardTools().setBodyCellsEdit(
				 * SOBillCardTools.getSaleItems_Price(), i, false); }
				 */

			}
			// ������Ʒ�б༭��
			getBillCardTools().setCardPanelCellEditableByLargess(SO59.booleanValue());

			// v30add������ս�����0�����ñ��ֲ��ɱ༭
			UFDouble nreceiptcathmny = getBillCardTools().getHeadUFDoubleValue("nreceiptcathmny");
			if (nreceiptcathmny != null && nreceiptcathmny.doubleValue() > 0) {
				getBillCardPanel().getHeadItem("ccurrencytypeid").setEnabled(false);
				getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);

			}

			// v30add v5.3ȥ��
			// �������������ʾ�������������ɱ༭
			/*
			 * if (getBillCardPanel().getBodyItem("nquoteunitnum").isShow()) {
			 * getBillCardPanel().getBodyItem("nnumber").setEnabled(false); }
			 */

			if ("�˻�".equals(strState)) {/*-=notranslate=-*/
				getBillCardPanel().getHeadItem("npreceiverate").setEnabled(false);
				getBillCardPanel().getHeadItem("npreceivemny").setEnabled(false);

				/** v5.3ȥ�������ֶ� * */
				/*
				 * getBillCardPanel().getHeadItem("naccountperiod").setEnabled(
				 * false);
				 */
			}

		} catch (Throwable ex) {
			handleException(ex);
		}

	}

	/**
	 * ���ݱ������û��ʺͽ��С��λ����
	 * 
	 * �������ڣ�(2002-6-24 11:15:06)
	 * 
	 */
	private void setPanelBomByCurrency(String ccurrencytypeid) {
		try {
			if (ccurrencytypeid == null || ccurrencytypeid.length() == 0)
				return;
			nc.vo.bd.b21.CurrinfoVO currVO = currtype.getCurrinfoVO(ccurrencytypeid, null);

			// v30ȡҵ�񾫶�
			int digit = currVO.getCurrdigit() == null ? 4 : currVO.getCurrdigit().intValue();

			// BOM������
			getBillTreeCardPanel().getBillData().getHeadItem("nsaleprice").setDecimalDigits(digit);
			getBillTreeCardPanel().getBillData().getHeadItem("bomorderfee").setDecimalDigits(digit);
			getBillTreeCardPanel().getBillData().getBodyItem("nprice").setDecimalDigits(digit);
			String name = getBillTreeCardPanel().getBodyItem("nprice").getName();
			if (getBillTreeCardPanel().getBodyPanel().hasShowCol(name))
				getBillTreeCardPanel().getBodyPanel().resetTableCellRenderer(name);
		} catch (Exception e) {
			SCMEnv.out("���ݱ�������С��λ��ʧ��!");
			// e.printStackTrace();
		}
	}

	/**
	 * ������ѡ��״̬����Ϊ�����ٱ༭״̬ �������ڣ�(2002-6-13 16:55:52)
	 */
	private void setPzdSelectedEnabled(boolean isEnable) {
		for (int i = 0; i < getBillTreeCardPanel().getBillModel().getRowCount(); i++) {
			getBillTreeCardPanel().setCellEditable(i, "bselect", isEnable);
		}
	}

	/**
	 * ���õ���״̬��
	 * 
	 * �������ڣ�(2001-6-13 15:17:39)
	 * 
	 * @param iState
	 *            int
	 * 
	 */
	private void setBtnsByBillState(int iState) {

		if (strState.equals("�޶�") || strState.equals("�޸�") || strState.equals("����")
				|| strState.equals("�˻�")) {
			boCoPushPo.setEnabled(false);
			return;
		}

		switch (iState) {

		case -1: {
			boBlankOut.setEnabled(false);
			boEdit.setEnabled(false);
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boModification.setEnabled(false);
			boBatch.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(false);

			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boCoPushPo.setEnabled(false);

			break;
		}
		case BillStatus.FREE: {
			boBlankOut.setEnabled(true);
			boEdit.setEnabled(true);
			boAudit.setEnabled(true);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boBatch.setEnabled(false);
			boModification.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(true);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(true);
			boOrdBalance.setEnabled(true);
			boCoPushPo.setEnabled(false);

			break;
		}
		case BillStatus.AUDIT: {
			boBlankOut.setEnabled(false);
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(true);
			boFreeze.setEnabled(true);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(true);
			boOpen.setEnabled(false);
			boFinish.setEnabled(true);
			boModification.setEnabled(true);
			boEdit.setEnabled(false);
			boBatch.setEnabled(true);
			boRefundment.setEnabled(true);
			boAfterAction.setEnabled(true);
			boStockLock.setEnabled(true);

			boSendInv.setEnabled(true);
			boSupplyInv.setEnabled(true);
			boDirectInv.setEnabled(true);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(true);
			boOrdBalance.setEnabled(true);
			if (getCurrentOrderId() != null
					&& !vocache.getSaleOrderVO(getCurrentOrderId()).isCoopped()) {
				boCoPushPo.setEnabled(true);

			} else {
				boCoPushPo.setEnabled(false);
			}

			if (ifHasData()) {
				setBoSendInvEnabled();
				setBoSupplyInvEnabled();
				setBoDirectInvEnabled();
			}

			setImageType(IMAGE_AUDIT);

			break;
		}
		case BillStatus.AUDITING: {
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boBatch.setEnabled(false);
			boModification.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(true);
			boOrdBalance.setEnabled(true);
			boCoPushPo.setEnabled(false);

			// v5.5 ����������״̬���������˵ĵ��ݿ����޸ģ��������˾Ͳ������޸�
			// v5.5 ����������״̬����ǰ�����˿������꣬�����˲�������
			Object auditUser = null;
			Object ordercorp = null;
			if (!strShowState.equals("�б�")) {
				auditUser = getBillCardTools().getTailValue("capproveid");
				ordercorp = getBillCardTools().getHeadValue("pk_corp");
			} else {
				auditUser = getBillListPanel().getHeadBillModel().getValueAt(
						getBillListPanel().getHeadTable().getSelectedRow(), "capproveid");
				ordercorp = getBillListPanel().getHeadBillModel().getValueAt(
						getBillListPanel().getHeadTable().getSelectedRow(), "pk_corp");
			}

			String curuserid = getClientEnvironment().getUser().getPrimaryKey();
			String curlogcorp = getClientEnvironment().getCorporation().getPk_corp();
			boCancelAudit.setEnabled(auditUser == null ? true
					: ((curuserid.equals(auditUser)) ? true : false));
			boAudit.setEnabled(true);
			if (curlogcorp.equals(ordercorp)) {
				boEdit.setEnabled(auditUser == null ? true : false);
				boBlankOut.setEnabled(auditUser == null ? true : false);
			} else {
				boEdit.setEnabled(false);
				boBlankOut.setEnabled(false);
			}

			break;
		}
		case BillStatus.NOPASS: {
			boBlankOut.setEnabled(false);
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boModification.setEnabled(false);
			boEdit.setEnabled(true);
			boBatch.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boCoPushPo.setEnabled(false);

			break;
		}
		case BillStatus.FREEZE: {
			boEdit.setEnabled(false);
			boCancel.setEnabled(false);
			boBlankOut.setEnabled(false);
			boModification.setEnabled(false);
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(true);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boBatch.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boCoPushPo.setEnabled(false);

			break;
		}
		case BillStatus.CLOSE: {
			boAudit.setEnabled(true);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(true);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(true);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boBatch.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boEdit.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boCoPushPo.setEnabled(false);

			break;
		}
		case BillStatus.FINISH: {
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boModification.setEnabled(false);
			boBatch.setEnabled(true);
			boRefundment.setEnabled(true);
			boAfterAction.setEnabled(false);
			boBlankOut.setEnabled(false);
			boEdit.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boCoPushPo.setEnabled(false);

			setBoSendInvEnabled();
			setBoSupplyInvEnabled();
			setBoDirectInvEnabled();

			break;
		}
		case BillStatus.BLANKOUT: {
			boBlankOut.setEnabled(false);
			boEdit.setEnabled(false);
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boModification.setEnabled(false);
			boBatch.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boAssistant.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boCoPushPo.setEnabled(false);

			break;
		}
		}
		if (getBillListPanel().getHeadTable().getRowCount() <= 0)
			setExtendBtnsStat(iState);
		updateUI();
	}

	/**
	 * �������Ű�ť������
	 */
	private void setBoSendInvEnabled() {
		boolean isUse = true;

		String cbiztype = getHeadItemValue("cbiztype").toString();
		String breceiptendflag = SmartVODataUtils.getUFBoolean(getHeadItemValue("breceiptendflag"))
				.toString();
		SaleOrderVO svo = (SaleOrderVO) getVo();

		if (breceiptendflag.equals("Y"))
			isUse = false;
		else if (!getBillCardTools().isSendInvEnable(svo))
			isUse = false;
		else if (!getBillCardTools().isSendInvBusiness(cbiztype))
			isUse = false;

		boSendInv.setEnabled(isUse);
	}

	/**
	 * �������Ű�ť������
	 */
	private void setBoSupplyInvEnabled() {
		boolean isUse = true;

		String cbiztype = getHeadItemValue("cbiztype").toString();

		if (!getBillCardTools().isArrangend((SaleOrderVO) getVo()))
			isUse = false;
		else if (!getBillCardTools().isBodyAllDirect((SaleOrderVO) getVo()))
			isUse = false;
		else if (getBillCardTools().isZVerifyRule(cbiztype))
			isUse = false;

		boSupplyInv.setEnabled(isUse);
	}

	/**
	 * ֱ�˰��Ű�ť������
	 */
	private void setBoDirectInvEnabled() {
		boolean isUse = true;

		String cbiztype = getHeadItemValue("cbiztype").toString();
		if (!getBillCardTools().isArrangend((SaleOrderVO) getVo()))
			isUse = false;
		else if (!getBillCardTools().isBodyDirect((SaleOrderVO) getVo())
				&& !getBillCardTools().isZVerifyRule(cbiztype))
			isUse = false;

		boDirectInv.setEnabled(isUse);
	}

	public AggregatedValueObject getVo() {
		if (!strShowState.equals("�б�")) {
			return getVO(false);
		} else {
			int selrow = getBillListPanel().getHeadTable().getSelectedRow();
			if (selrow < 0)
				selrow = selectRow;
			return getBillListPanel().getBillValueVO(selrow, SaleOrderVO.class.getName(),
					SaleorderHVO.class.getName(), SaleorderBVO.class.getName());
		}
	}

	protected ATPUIQryDelegate atpQry = null;

	// nc240 new add
	protected SOBillCardTools soBillCardTools = null;

	/**
	 * ���沿���׳��쳣ʱ������
	 * 
	 * @param ex
	 *            java.lang.Throwable
	 */
	protected void handleSaveException(String billType, String businessType, String actionName,
			String corpId, String operator, SaleOrderVO vo, Exception ex) {
		String err = ex.getMessage();
		if (ex.getClass() != AtpCheckException.class || (ex.getClass() == AtpCheckException.class)) {
			// �������������е��쳣
			if (ex instanceof PFBusinessException) {
				String errMsg = vo.getHeadVO().getErrMsg();
				if (errMsg != null && errMsg.startsWith("FUNC:lessSaleMinPrice")) {
					err += "/n" + errMsg.substring(errMsg.indexOf("$") + 1);
				}
			}
			showWarningMessage(err);
		}

		showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000185")/*
																							 * @res
																							 * "����ʧ�ܣ�"
																							 */);
	}

	/**
	 * ��ȡ��ͬ��Ϣ��
	 * 
	 * �������ڣ�(2001-10-9 13:05:04)
	 * 
	 */
	private void initCTTypeVO() {

		HashMap ht = new HashMap();

		if (getBillCardPanel().getBillModel().getRowCount() > 0) {
			if (getSouceBillType().equals(SaleBillType.SoContract)
					|| getSouceBillType().equals(SaleBillType.SoInitContract)) {
				for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
					String ct_manageid = (String) getBillCardPanel().getBodyValueAt(i,
							"ct_manageid");
					if (ct_manageid != null && ct_manageid.length() != 0)
						ht.put(ct_manageid, ct_manageid);
				}

			}
		}
		if (ht.size() <= 0)
			return;
		String[] ids = (String[]) ht.keySet().toArray(new String[0]);

		try {
			getBillCardPanel().hCTTypeVO = SaleOrderBO_Client.getAllContractType(ids);

		} catch (Exception ex) {
			SCMEnv.out("��ȡ��ͬ��Ϣʧ��!");
		}
	}

	/**
	 * ���ֱ༭���¼����� �������ڣ�(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void ctlCurrencyEdit() {
		UIRefPane ccurrencytypeid = (UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid")
				.getComponent();

		try {
			if (BD302 == null || !BD302.booleanValue()) {
				// ������ֵ��ڱ�λ�������۱����ʲ����޸ģ�����Ӧ�õ���1
				if (CurrParamQuery.getInstance().isLocalCurrType(getCorpPrimaryKey(),
						ccurrencytypeid.getRefPK())) {
					getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);
				} else
					getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);

			} else {
				// ������ֵ��ڸ����������۸������ʲ����޸�,�Ա��һ��ʿ����޸�
				if (CurrParamQuery.getInstance().isFracCurrType(getCorpPrimaryKey(),
						ccurrencytypeid.getRefPK())) {

					getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);
				} else {
					if (CurrParamQuery.getInstance().isLocalCurrType(getCorpPrimaryKey(),
							ccurrencytypeid.getRefPK())) {
						// ������ֵ��ڱ��������۸������ʣ����һ��ʲ������޸�
						getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);

					} else {
						getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);

					}
				}
			}
		} catch (java.lang.Exception e1) {
			SCMEnv.out("��û���ʧ�ܣ�");
			// e1.printStackTrace();
		}
	}

	public OrderBalanceCardUI getOrderBalanceUI() {
		if (orderBalanceUI == null)
			orderBalanceUI = new OrderBalanceCardUI(this);
		return orderBalanceUI;
	}

	/**
	 * ����������ݺϷ��ԡ�
	 * 
	 * �������ڣ�(2001-8-8 10:09:01)
	 * 
	 * @return boolean
	 * 
	 */
	private void onApproveCheck(SaleOrderVO saleorder) throws nc.vo.pub.ValidationException {

		String salecorp = null;

		SaleorderBVO[] bodyVOs = saleorder.getBodyVOs();

		salecorp = saleorder.getHeadVO().getPk_corp();

		if (salecorp == null || salecorp.trim().length() <= 0)
			salecorp = getCorpPrimaryKey();

		boolean isBomOrder = false;

		// �������ڲ���С���Ƶ�����
		if (saleorder.getHeadVO().getDmakedate().compareTo(getClientEnvironment().getDate()) > 0) {
			throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000541")/*
											 * @res "�������ڲ���С���Ƶ�����"
											 */);
		}

		for (int i = 0, loop = bodyVOs.length; i < loop; i++) {
			SaleorderBVO oldbodyVO = bodyVOs[i];

			if (!SoVoTools.isEmptyString(oldbodyVO.getCbomorderid()) && !isBomOrder)
				isBomOrder = true;

			// �ջ������֯�ǿռ���
			if (oldbodyVO.getCconsigncorpid() != null
					&& !oldbodyVO.getCconsigncorpid().equals(salecorp)) {

				if (oldbodyVO.getCreccalbodyid() == null
						|| oldbodyVO.getCreccalbodyid().trim().length() <= 0) {
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000202")/*
													 * @res
													 * "�����ֶβ���Ϊ�գ�������˾�Ǳ����۹�˾�е��ջ������֯"
													 */);
				}
				if (oldbodyVO.getBdericttrans() == null
						|| !oldbodyVO.getBdericttrans().booleanValue()) {
					if (oldbodyVO.getCrecwareid() == null
							|| oldbodyVO.getCrecwareid().trim().length() <= 0)
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000203")/*
																	 * @res
																	 * "�����ֶβ���Ϊ�գ���ֱ�˶����е��ջ��ֿ�"
																	 */);
				}

				if (oldbodyVO.getLaborflag() != null && oldbodyVO.getLaborflag().booleanValue())
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000204")/*
													 * @res "�����������ܴ�������˾����"
													 */);

				if (oldbodyVO.getDiscountflag() != null
						&& oldbodyVO.getDiscountflag().booleanValue())
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000205")/*
													 * @res "�ۿ��������ܴ�������˾����"
													 */);
			}
		}

		SCMEnv.out("��ˣ�" + getSouceBillType());
		SCMEnv.out("��ˣ�" + SaleBillType.SoDRP);

		if (!getSouceBillType().equals(SaleBillType.SoDRP))
			return;

		try {
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				// ��Ʒ���õ��������
				if (isBomOrder && !SaleOrderBO_Client.isBomApproved(saleorder.getPrimaryKey())) {
					throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID(
							"40060301", "UPP40060301-000240")/*
																 * @res
																 * "��Ʒ���õ�û����ȫ����!"
																 */);
				}
				// �۱�����
				if (getBillCardPanel().getHeadItem("nexchangeotobrate").getValue() == null
						|| getBillCardPanel().getHeadItem("nexchangeotobrate").getValue()
								.equals("")) {
					throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000310")/*
																 * @res
																 * "�۱����ʲ���Ϊ��!"
																 */);
				}

			}

			// VO���
			saleorder.validate();

			// �������
			for (int i = 0; i < saleorder.getChildrenVO().length; i++) {
				SaleorderBVO oldbodyVO = (SaleorderBVO) saleorder.getChildrenVO()[i];

				// �ջ������֯�ǿռ���
				if (oldbodyVO.getCconsigncorpid() != null
						&& !oldbodyVO.getCconsigncorpid().equals(salecorp)) {
					if (oldbodyVO.getCreccalbodyid() == null
							|| oldbodyVO.getCreccalbodyid().trim().length() <= 0) {
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000202")/*
																	 * @res
																	 * "�����ֶβ���Ϊ�գ�������˾�Ǳ����۹�˾�е��ջ������֯"
																	 */
								+ "\n�кţ�" + oldbodyVO.getCrowno());
					}
					if (oldbodyVO.getBdericttrans() == null
							|| !oldbodyVO.getBdericttrans().booleanValue()) {
						if (oldbodyVO.getCrecwareid() == null
								|| oldbodyVO.getCrecwareid().trim().length() <= 0)
							throw new ValidationException(NCLangRes.getInstance().getStrByID(
									"40060301", "UPP40060301-000203")/*
																		 * @res
																		 * "�����ֶβ���Ϊ�գ���ֱ�˶����е��ջ��ֿ�"
																		 */);
					}

					if (oldbodyVO.getDiscountflag() != null
							&& oldbodyVO.getDiscountflag().booleanValue())
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000205")/*
																	 * @res
																	 * "�ۿ��������ܴ�������˾����"
																	 */);
				}

				// ���ۿ�����
				if (oldbodyVO.getDiscountflag() != null
						&& !oldbodyVO.getDiscountflag().booleanValue()) {
					// ����
					if (oldbodyVO.getNoriginalcurmny().doubleValue() < 0
							|| oldbodyVO.getNoriginalcurtaxmny().doubleValue() < 0
							|| oldbodyVO.getNoriginalcursummny().doubleValue() < 0)
						if (oldbodyVO.getNnumber().doubleValue() > 0)
							throw new ValidationException(NCLangRes.getInstance().getStrByID(
									"40060301", "UPP40060301-000209")/*
																		 * @res
																		 * "��ǰ���������������!"
																		 */);
					// ����
					if (oldbodyVO.getNoriginalcurprice().doubleValue() < 0)
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000063")/*
																	 * @res
																	 * "���۱�����ڵ�����!"
																	 */);
				} else {
					// ����������˰�ϼ�
					if (oldbodyVO.getNnumber() == null && oldbodyVO.getNoriginalcurmny() == null
							&& oldbodyVO.getNoriginalcursummny() == null)
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000261")/*
																	 * @res
																	 * "����������˰�ϼƲ���ͬʱΪ��!"
																	 */);
				}
				// ��װ��λ(�Ƿ���ø�����)
				if (oldbodyVO.getAssistunit() != null && oldbodyVO.getAssistunit().booleanValue()) {
					if (oldbodyVO.getDiscountflag() != null
							&& !oldbodyVO.getDiscountflag().booleanValue()) {
						if (oldbodyVO.getCpackunitid() == null) {
							throw new ValidationException(NCLangRes.getInstance().getStrByID(
									"40060301", "UPP40060301-000241")/*
																		 * @res
																		 * "����λ����Ϊ��!"
																		 */);
						}
						if (oldbodyVO.getNpacknumber() == null) {
							throw new ValidationException(NCLangRes.getInstance().getStrByID(
									"40060301", "UPP40060301-000242")/*
																		 * @res
																		 * "����������Ϊ��!"
																		 */);
						} else {
							if (oldbodyVO.getNpacknumber().doubleValue()
									* oldbodyVO.getNnumber().doubleValue() < 0) {
								throw new ValidationException(NCLangRes.getInstance().getStrByID(
										"40060301", "UPP40060301-000243")/*
																			 * @res
																			 * "�����͸��������ű�����ͬ!"
																			 */);
							}
						}
					}
				}

				// �����ķ�������С�ڶ�������
				UFDate dbilldate = new UFDate(getBillCardPanel().getHeadItem("dbilldate")
						.getValue());
				UFDate dconsigndate = oldbodyVO.getDconsigndate();
				if (dconsigndate != null && dbilldate != null) {
					if (dbilldate.after(dconsigndate) && dbilldate != dconsigndate)
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000244")/*
																	 * @res
																	 * "�����ķ�������Ӧ���ڵ��ڶ�������!"
																	 */);
				}

				// ���ݲ������
				String oldinventoryid = oldbodyVO.getCinventoryid(); // ���

				if (oldbodyVO.getBlargessflag() != null
						&& !oldbodyVO.getBlargessflag().booleanValue()) {
					for (int j = i + 1; j < saleorder.getChildrenVO().length; j++) {
						SaleorderBVO newbodyVO = (SaleorderBVO) saleorder.getChildrenVO()[j];
						// SO_03 ��ͬ����ɷ񹲴�һ������Ʒ���⣩
						if (!SO_03.booleanValue()) {
							String newinventoryid = newbodyVO.getCinventoryid(); // ���
							if (newbodyVO.getBlargessflag() != null
									&& !newbodyVO.getBlargessflag().booleanValue()) {
								if (oldinventoryid.equals(newinventoryid)) {
									throw new ValidationException(NCLangRes.getInstance()
											.getStrByID("40060301", "UPP40060301-000216", null,
													new String[] { (i + 1) + "", (j + 1) + "" }));
								}
							}
						}
					}
				}
			}

			// �����տ�>=������˰�ϼƶ����տ�>=����Ԥ�տ� by zxj
			SaleorderHVO header = (SaleorderHVO) saleorder.getParentVO();
			for (int i = 0; i < saleorder.getChildrenVO().length; i++) {
				SaleorderBVO saleorderitem = (SaleorderBVO) saleorder.getChildrenVO()[i];
				if (saleorderitem != null && saleorderitem.getNtotalpaymny() != null) {
					if (saleorderitem.getNsummny() != null
							&& saleorderitem.getNtotalpaymny().doubleValue() < saleorderitem
									.getNsummny().doubleValue()) {
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000245")/*
																	 * @res
																	 * "�����տ�Ӧ>=������˰�ϼ�!"
																	 */);
					}
					if (header.getNsubscription() != null
							&& saleorderitem.getNtotalpaymny().doubleValue() < header
									.getNsubscription().doubleValue()) {
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000246")/*
																	 * @res
																	 * "�����տ�Ӧ>=����Ԥ�տ�!"
																	 */);
					}
				}
			}
			//
			SCMEnv.out("��˼��ͨ��1");
		} catch (Throwable e) {
			throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000247")/*
											 * @res "�˵����ɲ������������ݲ����������޸Ĳ��롣"
											 */);
		}
	}

	private Object getHeadItemValue(String key) {
		Object obj = null;
		if (strShowState.equals("�б�")) {
			int selrow = getBillListPanel().getHeadTable().getSelectedRow();
			if (selrow >= 0 && selrow < getBillListPanel().getHeadBillModel().getRowCount()) {
				obj = getBillListPanel().getHeadBillModel().getValueAt(selrow, key);
			} else
				obj = getBillListPanel().getHeadBillModel().getValueAt(selectRow, key);
		} else
			obj = getBillCardTools().getHeadValue(key);
		return obj;
	}

	/**
	 * �����տ�
	 */
	protected void onCachPay() {
		try {
			SaleOrderVO ordvo = (SaleOrderVO) getVO(false);
			if (ordvo == null)
				return;

			SaleorderBVO[] bodyVOs = ordvo.getBodysNoInludeOOSLine();

			if (bodyVOs.length <= 0) {
				return;
			}

			// SO72-���ֶ����Ƿ���Զ����տ�տ����
			UFDouble noriginalcursummny = SOBillCardTools.getCurSumMny(ordvo);
			if (noriginalcursummny == null || noriginalcursummny.doubleValue() == 0
					|| (!SO72.booleanValue() && noriginalcursummny.doubleValue() < 0)) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000250")/*
												 * @res "��˰�ϼ�С�ڵ���0"
												 */);
				return;
			}

			// ��ѯ���ս��
			UFDouble nreceiptcathmny = SaleOrderBO_Client.queryCachPayByOrdId(getHeadItemValue(
					"csaleid").toString());
			getBillListPanel().getHeadBillModel().setValueAt(nreceiptcathmny,
					getBillListPanel().getHeadTable().getSelectedRow(), "nreceiptcathmny");
			getBillCardPanel().setHeadItem("nreceiptcathmny", nreceiptcathmny);
			// ͬ�����»�������
			SaleOrderVO svo = vocache.getSaleOrderVO(getHeadItemValue("csaleid").toString());
			if (svo != null) {
				svo.getHeadVO().setNreceiptcathmny((UFDouble) nreceiptcathmny);
			}

			// Ԥ�տ���
			UFDouble npreceivemny = ordvo.getHeadVO().getNpreceivemny();
			if (npreceivemny != null) {
				if (nreceiptcathmny != null
						&& nreceiptcathmny.abs().doubleValue() >= npreceivemny.abs().doubleValue()) {
					showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000251")/*
													 * @res "����Ԥ�տ������"
													 */);
					return;
				}
			}

			if (nreceiptcathmny != null) {
				if (nreceiptcathmny.abs().doubleValue() >= noriginalcursummny.abs().doubleValue()) {
					showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000252")/*
													 * @res "�������տ����"
													 */);
					return;
				}
			}

			/** v5.5 ����ֱ�Ӵ��տ�ڵ�******************************************* */
			SaleReceiveVO srvo = get3FVoFrom30Vo(ordvo);
			/** Ӧ�ղ�Ʒ�����ж�* */
			if (ifProductUse(nc.vo.pub.ProductCode.PROD_AR)) {
				IARBusiness arbusiness = (IARBusiness) NCLocator.getInstance().lookup(
						IARBusiness.class.getName());
				String[] dest = arbusiness.queryARBusiness(getCorpPrimaryKey(), "30", srvo
						.getHead().getCbiztypeid(), "CachPay", new SOSaleBusinessPara());
				srvo.getHead().setCbiztypeid(dest[0]);

				// vo������3FtoD2
				DJZBVO djzbvo = (DJZBVO) PfChangeBO_Client.pfChangeBillToBill(srvo, "3F", dest[1]);

				PfLinkData linkData = new PfLinkData();
				linkData.setUserObject(new Object[] { new DJZBVO[] { djzbvo } });
				// �տ�ڵ��
				String nodecode = PfUIDataCache.getBillType(dest[1]).getNodecode();
				// ���տ�ڵ�
				SFClientUtil.openLinkedADDDialog(nodecode, this, linkData);

			} else {
				MessageDialog.showWarningDlg(this, "���棡", "Ӧ�ղ�Ʒδ���ã�");
				return;
			}
			/** v5.5 ����ֱ�Ӵ��տ�ڵ�******************************************* */

		} catch (PFBusinessException pfe) {
			showErrorMessage(pfe.getMessage());
			return;
		} catch (Exception e) {
			SCMEnv.out(e);
		}
	}

	/**
	 * ��30�õ�3FVO
	 */
	public SaleReceiveVO get3FVoFrom30Vo(nc.vo.so.so001.SaleOrderVO ordVO) {

		SaleReceiveVO vo = getSaleReceiveVO(ordVO);
		SaleReceiveHVO hvo = (SaleReceiveHVO) vo.getParentVO();

		String csaleid = (String) ordVO.getParentVO().getAttributeValue("csaleid");
		// set ts
		vo.getParentVO().setAttributeValue("ts", ordVO.getHeadVO().getTs());
		// set csaleid
		vo.getParentVO().setAttributeValue("csaleid", csaleid);
		SoVoTools.setVOsValue(vo.getChildrenVO(), "csaleid", csaleid);

		// �ͻ�(��Ӧ�����Ŀͻ�)
		hvo.setCcustomerid(ordVO.getHeadVO().getCcustomerid());
		// ��Ʊ��λ
		hvo.setCreceiptcorpid(ordVO.getHeadVO().getCreceiptcorpid());
		SoVoTools
				.execFormulas(
						new String[] {
								"ccustomerbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)",
								"creceiptcorpbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)" },
						new SaleReceiveHVO[] { hvo });

		// ���ý��㷽ʽ
		hvo.setCbaltypeid(ordVO.getHeadVO().getCbaltypeid());

		return vo;
	}

	public SaleReceiveVO getSaleReceiveVO(SaleOrderVO ordVO) {

		if (ordVO == null)
			return null;
		SaleorderHVO ordhvo = (SaleorderHVO) ordVO.getParentVO();
		SaleorderBVO[] ordbvos = (SaleorderBVO[]) ordVO.getChildrenVO();

		// // ������Ʒ��/////////////////////////////////////////////////
		java.util.ArrayList ordbvolist = new java.util.ArrayList();
		for (int i = 0, loop = ordbvos.length; i < loop; i++) {
			if (ordbvos[i].getBlargessflag() != null && ordbvos[i].getBlargessflag().booleanValue())
				continue;
			ordbvolist.add(ordbvos[i]);
		}
		ordbvos = (SaleorderBVO[]) ordbvolist.toArray(new SaleorderBVO[ordbvolist.size()]);
		if (ordbvos == null || ordbvos.length <= 0)
			return null;

		// // voֵ����/////////////////////////////////////////////////
		SaleReceiveHVO hvo = (SaleReceiveHVO) SoVoTools.getVOByVO("nc.vo.so.so016.SaleReceiveHVO",
				ordhvo,

				/* src */new String[] { "csaleid", "cbiztype", "ccustomerid", "npreceivemny",
						"coperatorid", "cemployeeid", "cdeptid", "cfreecustid", "cfreecustname" },

				/* dest */new String[] { "csaleid", "cbiztypeid", "ccustomerid", "npreceivemny",
						"coperatorid", "cemployeeid", "cdeptid", "cfreecustid", "cfreecustname" });

		hvo.setCemployeeid(ordhvo.getCemployeeid());
		if (ordhvo.getCreceiptcorpid() != null && ordhvo.getCreceiptcorpid().trim().length() > 0)
			hvo.setCcustomerid(ordhvo.getCreceiptcorpid());

		// �����ͻ�
		hvo.setCcustomerid(ordhvo.getCcustomerid());
		// ���ý��㷽ʽ
		hvo.setCbaltypeid(ordhvo.getCbaltypeid());

		SoVoTools.copyVOByVO(hvo, new String[] { "ccurrencytypeid", "nexchangeotobrate", },
				ordbvos[0], new String[] { "ccurrencytypeid", "nexchangeotobrate", });

		hvo.setCPkCorp(ordVO.getPk_corp());
		hvo.setDbilldate(getClientEnvironment().getDate());
		hvo.setNpreceivemny(ordhvo.getNpreceivemny());

		// ����Ԥ�ձ�־
		if (hvo.getNpreceivemny() != null && hvo.getNpreceivemny().doubleValue() > 0)
			hvo.setBpriorrecptflag(new nc.vo.pub.lang.UFBoolean(true));
		else
			hvo.setBpriorrecptflag(new nc.vo.pub.lang.UFBoolean(false));

		boolean bbyprodline = (SO27 == null ? false : SO27.booleanValue());
		SaleReceiveBVO bvo = new SaleReceiveBVO();
		bvo.setCsaleid(ordhvo.getCsaleid());
		bvo.setCbillordercode(ordhvo.getVreceiptcode());
		bvo.setCtermprotocolid(ordhvo.getCtermprotocolid());

		bvo.setNexchangeotobrate(ordbvos[0].getNexchangeotobrate());
		bvo.setCdeptid(ordhvo.getCdeptid());
		bvo.setCemployeeid(ordhvo.getCemployeeid());

		if (bbyprodline) {
			bvo.setCproductlineid(ordbvos[0].getCprolineid());
		} else {
			String[] prodlineids = SoVoTools.getVOsOnlyValues(ordbvos, "cprolineid");
			if (prodlineids != null && prodlineids.length == 1)
				bvo.setCproductlineid(prodlineids[0]);
			else
				bvo.setCproductlineid(null);
		}

		// Ԥ�տ���!=null && >0
		if (hvo.getNpreceivemny() != null && hvo.getNpreceivemny().doubleValue() > 0) {
			// ԭ�Ҽ�˰�ϼơ���˰���
			bvo.setAttributeValue("noriginalcursummny", SoVoTools.getMnySub(hvo.getNpreceivemny(),
					ordhvo.getNreceiptcathmny()));
		} else {
			/** ====ԭ�Ҽ�˰�ϼ� ==== */
			bvo.setAttributeValue("noriginalcursummny", SoVoTools.getMnySub(SoVoTools.getTotalMny(
					ordbvos, "noriginalcursummny"), ordhvo.getNreceiptcathmny()));
		}

		try {
			// ���㱾�Ҽ�˰�ϼ�
			UFDouble nsummny = null;

			// û�����ս����Ҵ�����ȡ
			if (ordhvo.getNreceiptcathmny() == null
					|| ordhvo.getNreceiptcathmny().compareTo(UFDouble.ZERO_DBL) == 0) {
				nsummny = getBillListPanel().calcurateTotal("nsummny");
				bvo.setAttributeValue("nsummny", nsummny);
			} else {
				nsummny = currtype.getAmountByOpp(hvo.getCcurrencytypeid(), CurrParamQuery
						.getInstance().getLocalCurrPK(hvo.getcPkCorp()), SmartVODataUtils
						.getUFDouble(bvo.getAttributeValue("noriginalcursummny")), hvo
						.getNexchangeotobrate(), getClientEnvironment().getDate().toString());
				bvo.setAttributeValue("nsummny", nsummny);
			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("calcMny:������ʧ��");
		}

		// �����տ��˰
		bvo.setAttributeValue("nmny", bvo.getAttributeValue("nsummny"));
		bvo.setAttributeValue("noriginalcurmny", bvo.getAttributeValue("noriginalcursummny"));

		SaleReceiveVO oldSaleReceiveVO = new SaleReceiveVO();
		oldSaleReceiveVO.setParentVO(hvo);
		oldSaleReceiveVO.setChildrenVO(new SaleReceiveBVO[] { bvo });

		hvo.setAttributeValue("nmny", bvo.getAttributeValue("nsummny"));
		hvo.setAttributeValue("noriginalcurmny", bvo.getAttributeValue("noriginalcursummny"));

		return oldSaleReceiveVO;
	}

	protected void onOrderBalance() {
		try {
			// ���������ڵ��Ƿ�����
			if (!nc.ui.so.pub.BillTools.ifNodeCodeUse("40060308")) {
				showWarningMessage("���������ڵ�δ���ã��ð�ť�����ã�");
				return;
			}

			SaleOrderVO ordvo = (SaleOrderVO) getVO(false);
			if (ordvo == null)
				return;

			// ��鶩�������Ƿ�����������ŵ���
			int iallMinusMnyrow = 0;
			int iallMinusNumrow = 0;
			SaleorderBVO[] bodyVOs = ordvo.getBodysNoInludeOOSLine();

			if (bodyVOs.length <= 0) {
				return;
			}

			for (int i = 0, loop = bodyVOs.length; i < loop; i++) {

				if (bodyVOs[i].getNoriginalcursummny() != null
						&& bodyVOs[i].getNoriginalcursummny().doubleValue() < 0) {
					iallMinusMnyrow++;
				}
				if (bodyVOs[i].getNnumber() != null && bodyVOs[i].getNnumber().doubleValue() < 0) {
					iallMinusNumrow++;
				}
			}

			ordvo.setChildrenVO(bodyVOs);

			// SO72-���ֶ����Ƿ���Զ����տ�տ����
			UFDouble noriginalcursummny = SOBillCardTools.getCurSumMny(ordvo);
			if (noriginalcursummny == null || noriginalcursummny.doubleValue() == 0
					|| (!SO72.booleanValue() && noriginalcursummny.doubleValue() < 0)) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000250")/*
												 * @res "��˰�ϼ�С�ڵ���0"
												 */);
				return;
			}

			// ���ÿͻ�����Ʊ��λ�Ļ�������id
			SoVoTools
					.execFormulas(
							new String[] {
									"ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)",
									"creceiptcorpbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)" },
							new SaleorderHVO[] { ordvo.getHeadVO() });

			getOrderBalanceUI().setOrdVO(ordvo);
			if (getCurShowPanel() == null)
				remove(getBillListPanel());
			else
				remove(getCurShowPanel());
			add(getOrderBalanceUI(), "Center");
			getOrderBalanceUI().initUI();
			setTitleText(getOrderBalanceUI().getTitle());

			showHintMessage("");

			ButtonObject[] btns = getOrderBalanceUI().getButtons();

			// �������ð�ť
			for (int i = 0, len = btns.length; i < len; i++) {
				if (btns[i].getCode().equals("�б���ʾ") || btns[i].getCode().equals("���")
						|| btns[i].getCode().equals("��ӡ����")) {
					btns[i].setVisible(false);
				}
			}// end for

			setButtons(btns);

			updateUI();

		} catch (Exception e) {
			returnFormOrderBalanceUI();
			orderBalanceUI = null;
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}
	}

	public void returnFormOrderBalanceUI() {
		remove(getOrderBalanceUI());
		if (getCurShowPanel() == null)
			add(getBillListPanel(), "Center");
		else
			add(getCurShowPanel(), "Center");
		setTitleText(getTitle());
		setButtons();
		setButtonsState();
		getBillCardPanel().setHeadItem("nreceiptcathmny", getOrderBalanceUI().getNbalmny());

		int selrow = getBillListPanel().getHeadTable().getSelectedRow();
		if (selrow >= 0) {
			getBillListPanel().getHeadBillModel().setValueAt(getOrderBalanceUI().getNbalmny(),
					selrow, "nreceiptcathmny");
			// ͬ�����»�������
			SaleOrderVO ordvo = vocache.getSaleOrderVO((String) getBillListPanel()
					.getHeadBillModel().getValueAt(selrow, "csaleid"));
			if (ordvo != null) {
				ordvo.getHeadVO().setNreceiptcathmny(getOrderBalanceUI().getNbalmny());
			}
		}
		updateUI();
	}

	public JComponent getCurShowPanel() {
		return curShowPanel;
	}

	/**
	 * ���ݴ�ӡ��ˢ��ts(2004-12-01 23:25:18)
	 * 
	 * @param
	 */
	public void freshTs(String sBillID, String sTS, Integer iPrintCount) {
		if (sTS == null || sTS.trim().length() <= 0)
			return;
		if (strShowState.equals("�б�")) { /*-=notranslate=-*/
			if (sBillID == null || sBillID.trim().length() <= 0)
				return;
			String csaleid = null;
			for (int i = 0, loop = getBillListPanel().getHeadTable().getRowCount(); i < loop; i++) {
				csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid");
				if (sBillID.equals(csaleid)) {
					getBillListPanel().getHeadBillModel().setValueAt(sTS, i, "ts");
					getBillListPanel().getHeadBillModel().setValueAt(iPrintCount, i, "iprintcount");
					break;
				}
			}
		} else {
			getBillCardPanel().setHeadItem("ts", sTS);
			getBillCardPanel().setTailItem("iprintcount", iPrintCount);
			String csaleid = null;
			for (int i = 0, loop = getBillListPanel().getHeadTable().getRowCount(); i < loop; i++) {
				csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid");
				if (sBillID.equals(csaleid)) {
					getBillListPanel().getHeadBillModel().setValueAt(sTS, i, "ts");
					getBillListPanel().getHeadBillModel().setValueAt(iPrintCount, i, "iprintcount");
					break;
				}
			}
		}
		SaleOrderVO ordvo = vocache.getSaleOrderVO(sBillID);
		if (ordvo != null) {
			ordvo.getHeadVO().setIprintcount(iPrintCount);
			ordvo.getHeadVO().setTs(new UFDateTime(sTS.trim()));
		}
	}

	/**
	 * ��ʼ����ӡ��־����(2004-12-01 23:25:18)
	 * 
	 * @param
	 */
	protected PrintLogClient getPrintLogClient() {
		// if (printLogClient == null) {
		printLogClient = new PrintLogClient();
		printLogClient.addFreshTsListener(this);
		// }
		return printLogClient;
	}

	/**
	 * ��ʾ���̹�����Ӧ�������Ϣ��
	 * 
	 * �������ڣ�(2003-12-26 12:52:43)
	 * 
	 */
	private void showCustManArInfo(HashMap reths) {

		if (reths == null)
			return;

		UFDouble[] dvalues = (UFDouble[]) reths.get("showCustManArInfo");
		if (dvalues == null || dvalues.length < 5)
			return;

		BillItem[] bis = new BillItem[5];
		// ����Ӧ��accawmny
		bis[0] = getBillCardPanel().getTailItem("accawmny");
		// ҵ��Ӧ��busawmny
		bis[1] = getBillCardPanel().getTailItem("busawmny");
		// ����Ӧ��ordawmny
		bis[2] = getBillCardPanel().getTailItem("ordawmny");
		// ���ö��creditmny
		bis[3] = getBillCardPanel().getTailItem("creditmny");
		// ���ñ�֤��creditmoney
		bis[4] = getBillCardPanel().getTailItem("creditmoney");

		// �������ý���
		int digit = getBillCardPanel().getBillData().getBodyItem("noriginalcursummny")
				.getDecimalDigits();
		for (int i = 0; i < bis.length; i++) {
			if (bis[i] != null) {
				bis[i].setDecimalDigits(digit);
			}
		}

		for (int i = 0; i < bis.length; i++) {
			if (bis[i] != null) {
				bis[i].setValue(dvalues[i]);
			}
		}

	}

	/**
	 * 1. �����۶����޶�ʱ�ṩ�ͻ��޸ĵĹ��ܣ�ֻ�������۶���û�з���/����/��Ʊ/�����������޶����۶����Ŀͻ��� 2.
	 * ���ѽ����˻�Դ���ŵ�����¿��Խ��пͻ��޶����޶���Ӱ��������ݵĿͻ���Ϣ�� 3 �ͻ��޶���Ӧ����ѯ�ۣ�ͬʱ�޸�����Ӧ�����ݡ�
	 * 
	 * @param i
	 * @return
	 */
	private boolean isHeadCustCanbeModified() {
		int irowcount = getBillCardPanel().getRowCount();
		if (irowcount <= 0)
			return true;
		for (int irow = 0; irow < irowcount; irow++) {
			String sPk = (String) getBillCardPanel().getBodyValueAt(irow, "csaleid");
			String sBodyPk = (String) getBillCardPanel().getBodyValueAt(irow, "corder_bid");
			if (sPk == null || sPk.trim().length() == 0 || sBodyPk == null
					|| sBodyPk.trim().length() == 0)
				// ����
				continue;

			SaleOrderVO svo = vocache.getSaleOrderVO(sPk);

			if (svo == null)
				continue;

			SaleorderBVO[] bvos = svo.getBodyVOs();
			if (bvos == null || bvos.length == 0)
				continue;
			for (int i = 0; i < bvos.length; i++) {
				if (bvos[i].getCorder_bid().equals(sBodyPk)) {
					String[] sNames = { "ntotalreceivenumber",// �ۼƷ�������
							"ntotalinvoicenumber",// �ۼƿ�Ʊ����
							"ntotalinventorynumber",// �ۼƳ�������
							"ntotalshouldoutnum",// ����Ӧ��
							"ntotalbalancenumber",// �ۼƽ�������
							"ntotalreturnnumber",// �ۼ��˻�����
					// "narrangescornum",//�ۼư���ί�ⶩ������
					// "narrangepoapplynum",//�ۼư����빺������
					// "narrangetoornum",//�ۼư��ŵ�����������
					// "norrangetoapplynum",//�ۼư��ŵ�����������
					// "narrangemonum"//�ۼư���������������
					};
					for (int j = 0; j < sNames.length; j++) {
						if (bvos[i].getAttributeValue(sNames[j]) != null
								&& ((UFDouble) bvos[i].getAttributeValue(sNames[j])).doubleValue() != 0)
							return false;
					}
					break;
				}
			}

		}
		return true;

	}

	/**
	 * ��������:������ѯ
	 * 
	 * ����:
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 */
	protected void onOnHandShowHidden() {
		// ������ѯ����ֻ�ڿ�Ƭ������ʾ
		if (strShowState.equals("�б�")) {
			onCard();

			// ѡ�еı���
			int selectedrow = getBillListPanel().getBodyTable().getSelectedRow();
			if (selectedrow > -1) {
				getBillCardPanel().getBillTable().getSelectionModel().setSelectionInterval(
						selectedrow, selectedrow + 1);
			}
		}

		// ������ѯ����ֻ�ڿ�Ƭ������ʾ

		m_bOnhandShowHidden = !m_bOnhandShowHidden;

		show(true, m_bOnhandShowHidden);
		if (m_bOnhandShowHidden) {
			freshOnhandnum(getBillCardPanel().getBillTable().getSelectedRow());
		}
		updateUI();
	}

	/**
	 * �������ڣ�(2004-2-10 18:30:34)
	 * 
	 * ���ߣ����˾� ������ ���أ� ˵������ʾ/����panel
	 * 
	 */
	private void show(boolean bCardShow, boolean bSouthPanelShow) {
		getBillCardPanel().setVisible(bCardShow);
		getSplitPanelBc().setVisible(bCardShow);
		if (bCardShow) {
			if (bSouthPanelShow) {
				if (getSplitPanelBc().getBottomComponent() == null) {
					getSplitPanelBc().add(getPnlSouth(this), nc.ui.pub.beans.UISplitPane.BOTTOM);
				}
				if (getSplitPanelBc().getTopComponent() == null) {
					getSplitPanelBc().add(getBillCardPanel(), nc.ui.pub.beans.UISplitPane.TOP);
				}
				getSplitPanelBc().setDividerLocation((int) (getSplitPanelBc().getHeight() * 0.68));

			} else {
				if (getSplitPanelBc().getTopComponent() == null)
					getSplitPanelBc().add(getBillCardPanel(), nc.ui.pub.beans.UISplitPane.TOP);
				if (getSplitPanelBc().getBottomComponent() != null)
					getSplitPanelBc().remove(getPnlSouth(this));
				getSplitPanelBc().setDividerLocation((int) (getSplitPanelBc().getHeight() * 0.95));

			}
		}

	}

	/**
	 * �رմ��ڵĿͻ��˽ӿڡ����ڱ���������ɴ��ڹر�ǰ�Ĺ�����
	 * 
	 * @return boolean ����ֵΪtrue��ʾ�����ڹرգ�����ֵΪfalse��ʾ�������ڹرա�
	 * 
	 * �������ڣ�(2001-8-8 13:52:37)
	 */
	public boolean onClosing() {

		boolean closeFlag = true;

		if (strState.equals("�޸�") || strState.equals("�޶�") || strState.equals("����")) {
			int ireturn = MessageDialog.showYesNoCancelDlg(this, NCLangRes.getInstance()
					.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
																	 * @res "��ʾ"
																	 */, NCLangRes.getInstance().getStrByID("common", "UCH001")/*
																		 * @res
																		 * "�Ƿ񱣴����޸ĵ����ݣ���"
																		 */);
			if (ireturn == MessageDialog.ID_YES) {
				if (onSave())
					closeFlag = true;
				else
					closeFlag = false;
			} else if (ireturn == MessageDialog.ID_CANCEL)
				closeFlag = false;
		}

		if (closeFlag)
			processMemoryAfterClose();

		return closeFlag;
	}

	/**
	 * �رսڵ����ղ���Ҫ�Ķ������ã����ͷ��ڴ�
	 */
	private void processMemoryAfterClose() {
		// �Ҽ��˵�
		if (m_bodyMenuItems != null) {
			m_bodyMenuItems.clear();
			m_bodyMenuItems = null;
		}
	}

	private void setLineButtonStatus(boolean bstatus) {
		boLine.setEnabled(bstatus);
		boAddLine.setEnabled(bstatus);
		boDelLine.setEnabled(bstatus);
		boCopyLine.setEnabled(bstatus);
		boPasteLine.setEnabled(bstatus);
		boPasteLineToTail.setEnabled(bstatus);
		boFindPrice.setEnabled(bstatus);
		boCardEdit.setEnabled(bstatus);
		boResortRowNo.setEnabled(bstatus);
		setBodyMenuStatus();
	}

	/**
	 * UI��������-����
	 * 
	 * @author leijun 2006-5-24
	 * 
	 * @see ��ʽ������Ϣ��زο���Ʊ--SaleInvoiceUI.java
	 * 
	 */
	public void doAddAction(ILinkAddData adddata) {
		try {
			if (PfUtilClient.isCloseOK()) {
				binitOnNewByOther = true;

				onNewByOther(PfUtilClient.getRetVos());
				// ������״̬
				for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
					vRowATPStatus.add(new UFBoolean(false));
			}
			getBillCardPanel().showCustManArInfo();

			// ���������ɫ����
			try {
				InvAttrCellRenderer ficr = new InvAttrCellRenderer();
				ficr.setFreeItemRenderer(getBillCardPanel(), this.getBillCardPanel().alInvs);
			} catch (Exception e) {

			}
			setBusitype();
		} catch (Exception e) {
			handleException(e);
		} finally {
			binitOnNewByOther = false;
		}

	}

	/**
	 * UI��������-����
	 * 
	 * @author cch 2006-5-9-11:04:16
	 * 
	 */
	public void doApproveAction(ILinkApproveData approvedata) {

		if (strShowState == "�б�") {
			onCard();
		}

		id = approvedata.getBillID();
		loadCardDataByID(id);
		setCardButtonsState();

		setButtons(getBillButtons());

		// �������ݵ��б������
		SaleOrderVO vo = vocache.getSaleOrderVO(id);
		if (vo != null) {
			getBillListPanel().fillHeadData(new SaleorderHVO[] { vo.getHeadVO() });
			fillCacheByListPanel();
			selectRow = -1;
			initIDs();
		}
	}

	/**
	 * UI��������-ά��
	 * 
	 * @author leijun 2006-5-24
	 */
	public void doMaintainAction(ILinkMaintainData maintaindata) {

		if (strShowState == "�б�") {
			onCard();
		}

		id = maintaindata.getBillID();
		loadCardDataByID(id);
		setCardButtonsState();

		// ��ȡ��ǰ���ݵĹ�˾ID
		Object pk_corp = getBillCardPanel().getHeadItem("pk_corp").getValueObject();
		if (getCorpPrimaryKey().equals(pk_corp))
			setButtons(getBillButtons());
		else
			setButtons(new ButtonObject[0]);

		// �������ݵ��б������
		SaleOrderVO vo = vocache.getSaleOrderVO(id);
		if (vo != null) {
			getBillListPanel().fillHeadData(new SaleorderHVO[] { vo.getHeadVO() });
			fillCacheByListPanel();
			selectRow = -1;
			initIDs();
		}

	}

	/**
	 * UI��������-��ѯ
	 * 
	 * @author leijun 2006-5-24
	 * 
	 * @modified v51 ���������Ϊ��ǿ
	 * 
	 */
	public void doQueryAction(ILinkQueryData querydata) {

		// ֱ����ʾ��Ƭ����
		if (strShowState == "�б�") {
			onCard();
		}

		id = querydata.getBillID();
		loadCardDataByID(id, false);
		setCardButtonsState();

		// ��ȡ��ǰ���ݵĹ�˾ID
		Object pk_corp = getBillCardPanel().getHeadItem("pk_corp").getValueObject();
		if (getCorpPrimaryKey().equals(pk_corp))
			setButtons(getBillButtons());
		else
			setButtons(new ButtonObject[0]);

		// �������ݵ��б������
		SaleOrderVO vo = vocache.getSaleOrderVO(id);
		if (vo != null) {
			getBillListPanel().fillHeadData(new SaleorderHVO[] { vo.getHeadVO() });
			fillCacheByListPanel();
			vocache.getSaleOrderVO(id).setChildrenVO(vo.getChildrenVO());
			selectRow = -1;
			initIDs();
		}

	}

	private void setBusitype() {

		String sBusitype = getBillCardPanel().getHeadItem("cbiztype").getValue();

		// ����ҵ������
		boBusiType.setTag(sBusitype);
		getBillCardPanel().setBusiType(sBusitype);

	}

	ClientEnvironment getClient() {
		return getClientEnvironment();
	}

	/**
	 * �õ�����VO��
	 * 
	 * �������ڣ�(2001-6-23 9:47:36)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 * 
	 */
	public AggregatedValueObject getVO(boolean needRemove) {
		SaleOrderVO saleorder = null;
		if (strShowState.equals("�б�")) { /*-=notranslate=-*/
			int row = getBillListPanel().getHeadTable().getSelectedRow();

			if (row < 0)
				return null;

			saleorder = (SaleOrderVO) getBillListPanel().getBillValueVO(row,
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());

		} else {

			saleorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());
			// ��������
			((SaleorderHVO) saleorder.getParentVO()).setCreceipttype(SaleBillType.SaleOrder);
			// ����
			((SaleorderHVO) saleorder.getParentVO()).setPrimaryKey(getBillID());
			// �ջ���ַ
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			((SaleorderHVO) saleorder.getParentVO()).setVreceiveaddress(vreceiveaddress
					.getUITextField().getText());
		}
		if (needRemove) {
			Vector vTemp = new Vector();
			SaleorderBVO[] itemVOs = (SaleorderBVO[]) saleorder.getChildrenVO();
			int indexSelected = -1;
			if (strShowState.equals("�б�")) { /*-=notranslate=-*/
				indexSelected = getBillListPanel().getBodyTable().getSelectedRow();
			} else {
				indexSelected = getBillCardPanel().getBillTable().getSelectedRow();
			}
			for (int i = 0; i < itemVOs.length; i++) {
				// ��������������
				boolean notLabor = itemVOs[i].getLaborflag() == null
						|| !itemVOs[i].getLaborflag().booleanValue();
				// �������ۿ�����
				boolean notDiscount = itemVOs[i].getDiscountflag() == null
						|| !itemVOs[i].getDiscountflag().booleanValue();

				if (notLabor && notDiscount) {
					if (indexSelected > -1 && i == indexSelected) {
						vTemp.addElement(itemVOs[i]);
					}
				}
			}
			SaleorderBVO[] itemsNew = new SaleorderBVO[vTemp.size()];
			vTemp.copyInto(itemsNew);
			saleorder.setChildrenVO(itemsNew);
		}

		// ĳЩ����������޹�˾
		SaleorderBVO[] itemVOs = (SaleorderBVO[]) saleorder.getChildrenVO();
		for (int i = 0, len = itemVOs.length; i < len; i++) {
			if (itemVOs[i].getPkcorp() == null) {
				itemVOs[i].setPkcorp(getCorpPrimaryKey());
			}
		}

		// ������
		((SaleorderHVO) saleorder.getParentVO()).setCapproveid(getClientEnvironment().getUser()
				.getPrimaryKey());

		return saleorder;
	}

	/**
	 * ���б任 �������ڣ�(01-2-26 13:29:17)
	 */
	public void bodyRowChange(BillEditEvent e) {

		if (e.getRow() == -1)
			return;
		super.bodyRowChange(e);
		if (getFuncExtend() != null) {
			// ֧�ֹ�����չ
			try {
				getFuncExtend().rowchange(this, getBillCardPanel(), getBillListPanel(),
						nc.ui.scm.extend.IFuncExtend.LIST, nc.ui.pub.bill.BillItem.HEAD);
			} catch (Throwable ee) {
				nc.vo.scm.pub.SCMEnv.out(ee.getMessage());
			}
		}

		if (strState.equals("�޶�")) { /*-=notranslate=-*/

			if (getBillCardPanel().getBillModel().getRowState(e.getRow()) == BillModel.ADD
					|| !isHasBackwardDoc(e.getRow())) {
				boDelLine.setEnabled(true);
				getBillCardPanel().getDelLineMenuItem().setEnabled(true);

			} else {
				boDelLine.setEnabled(false);
				getBillCardPanel().getDelLineMenuItem().setEnabled(false);

			}
			updateButton(boDelLine);
		}
		if (strShowState.equals("�б�")) { /*-=notranslate=-*/
			selectRow = e.getRow();
			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(selectRow,
					"csaleid");

			SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);

			if (vo == null || vo.getParentVO() == null)
				return;

			if (vo.getChildrenVO() == null || vo.getChildrenVO().length <= 0) {
				getBillListPanel().loadBodyData(selectRow);
				getBillListPanel().getBodyBillModel().updateValue();
			} else {
				getBillListPanel().setListBodyByCurrency(vo.getBodyVOs()[0].getCcurrencytypeid());
				getBillListPanel().setBodyValueVO(vo.getChildrenVO());
				getBillListPanel().getBodyBillModel().updateValue();
			}

			setButtonsState();

		} else if (strShowState.equals("��Ƭ")) { /*-=notranslate=-*/
			// wsy ������ʾ
			freshOnhandnum(e.getRow());

		}

	}

	public void reLoadListData() {
		if (strShowState.equals("�б�")) { /*-=notranslate=-*/
			setBtnsByBillState(-1);
			getBillListPanel().reLoadData();
			fillCacheByListPanel();
			selectRow = -1;
			initIDs();
		}
	}

	/**
	 * 
	 * �������������������޶��汾�ţ��޶��ˣ��޶�����
	 * 
	 * @author �ܳ�ʤ
	 * @time 2008-8-14 ����04:38:06
	 */
	public void doEditBeforeModify(SaleOrderVO saleorder) {
		// v5.5�޶��汾�ţ����۶��������汾��1.0��֮��ÿ���޶���1
		String editionNum = saleorder.getHeadVO().getEditionNum();
		int editionNumNew = new UFDouble(editionNum == null ? "1.0" : editionNum.trim()).intValue() + 1;
		editionNum = String.valueOf(editionNumNew) + ".0";
		saleorder.getHeadVO().setEditionNum(editionNum);
		// �޶�����ȡ��ǰ�޶���ϵͳ����
		saleorder.getHeadVO().setEditDate(getClientEnvironment().getDate().toString());
		// �޶���ȡ��ǰ��½��
		saleorder.getHeadVO().setEditAuthor(
				getClientEnvironment().getUser().getUserName().toString());
	}

	/**
	 * �����Դ�������͡�
	 * 
	 * �������ڣ�(2001-11-16 13:24:23)
	 * 
	 * @return java.lang.String
	 */
	protected String getSouceBillType() {
		String creceipttype = null;
		if (strShowState.equals("��Ƭ")) { /*-=notranslate=-*/
			if (getBillCardPanel().getRowCount() > 0) {
				creceipttype = (String) getBillCardPanel().getBodyValueAt(0, "creceipttype");
			}
		} else {
			if (getBillListPanel().getBodyBillModel().getRowCount() > 0) {
				creceipttype = (String) getBillListPanel().getBodyBillModel().getValueAt(0,
						"creceipttype");
			}
		}
		if (creceipttype == null || creceipttype.trim().equals(""))
			creceipttype = "NO";
		return creceipttype;
	}

	public SaleOrderSplitDLG getSpDLG() {
		if (spDLG == null) {
			spDLG = new SaleOrderSplitDLG(this);
		}
		return spDLG;
	}

	/**
	 * @param productCode
	 * @return ĳ��Ʒ�Ƿ�����
	 */
	public boolean ifProductUse(String productCode) throws BusinessException {
		nc.itf.uap.sf.ICreateCorpQueryService icorp = (nc.itf.uap.sf.ICreateCorpQueryService) nc.bs.framework.common.NCLocator
				.getInstance().lookup(nc.itf.uap.sf.ICreateCorpQueryService.class.getName());
		java.util.Hashtable pEnabled = icorp.queryProductEnabled(getClient().getCorporation()
				.getPrimaryKey(), new String[] { productCode });

		return ((nc.vo.pub.lang.UFBoolean) pEnabled.get(productCode)).booleanValue();
	}
}
