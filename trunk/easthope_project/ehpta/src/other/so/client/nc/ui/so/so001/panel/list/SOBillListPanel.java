package nc.ui.so.so001.panel.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.BillTotalListener;
import nc.ui.pub.bill.IBillRelaSortListener;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.scm.so.SaleBillType;
import nc.ui.so.pub.SoTaskManager;
import nc.ui.so.so001.SaleOrderBO_Client;
import nc.ui.so.so001.panel.UnitOfMeasureTool;
import nc.ui.so.so001.panel.bom.BillTools;
import nc.ui.so.so001.panel.card.SOBillCardTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.CommonConstant;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so001.SoVoConst;

/**
 * ���۶����б�ģ����
 * 
 * @author zhongwei
 *
 */
public class SOBillListPanel extends BillListPanel implements
		/*ListSelectionListener,*/ BillTableMouseListener, BillTotalListener,
		IBillRelaSortListener,BillEditListener {

	private String m_strWhere = null;
	
//	//���ּ���������
//	private ScmCurrLocRateBizDecimalListener scmCurrTypeDigit;

	//������������objIsWaitAudit[0] -- "Y" �д���������  "N" �޴���������
	//          objIsWaitAudit[1] -- ��ǰ����ԱID
	private Object objIsWaitAudit = null;
	
	ClientEnvironment ce = null;

	protected BusinessCurrencyRateUtil currtype = null;

	protected ArrayList catchlist = new ArrayList();

	//��������
	protected UFBoolean BD302 = null; //�Ƿ������Һ���

	protected SoTaskManager soTaskManager = null;

	private SaleBillListUI ui;

	private SaleOrderVOCache vocache;

	public SOBillListPanel() {
		super();
	}

	public SOBillListPanel(SaleBillListUI parent, String pk_corp,
			SaleOrderVOCache vocache) throws Exception {
		ui = parent;
		this.vocache = vocache;

		setName("ListPanel");
		addMouseListener(this);

		setBillType(SaleBillType.SaleOrder);
		setCorp(pk_corp);
		setOperator(getClientEnvironment().getUser().getPrimaryKey());

		getHeadTable().setSelectionMode(
				javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		BillTempletVO billtempletVO;
		// river 
		if (parent.SO_20.booleanValue()) {
//			billtempletVO = getDefaultTemplet(parent.getNodeCode(), null,
//					getOperator(), getCorp());
			billtempletVO = getDefaultTemplet("HQ010601", null,
					getOperator(), getCorp());
		} else {
//			billtempletVO = getDefaultTemplet(parent.getNodeCode(),
//					getBusiType(), getOperator(), getCorp());
			billtempletVO = getDefaultTemplet("HQ010601",
					getBusiType(), getOperator(), getCorp());
		}
		
		
		setListData(new BillListData(billtempletVO));
		
		updateUI();
	}

	public UFDouble calcurateTotal(String key) {
		UFDouble total = SoVoConst.duf0;
		if (ui.strShowState.equals("�б�")) { /*-=notranslate=-*/
			for (int i = 0; i < getBodyBillModel().getRowCount(); i++) {
				UFBoolean blargessflag = getBodyBillModel().getValueAt(i,
						"blargessflag") == null ? new UFBoolean(false)
						: new UFBoolean(getBodyBillModel().getValueAt(i,
								"blargessflag").toString());

				// 20061026 ����ȱ���ж�
				UFBoolean boosflag = getBodyBillModel().getValueAt(i,
						"boosflag") == null ? new UFBoolean(false)
						: new UFBoolean(getBodyBillModel().getValueAt(i,
								"boosflag").toString());

				if (SaleorderBVO.isPriceOrMny(key)
						&& ((blargessflag != null && blargessflag
								.booleanValue()) || (boosflag != null && boosflag
								.booleanValue())))
					continue;

				Object value = getBodyBillModel().getValueAt(i, key);
				String v = (value == null || value.toString().trim().length() <= 0) ? "0"
						: value.toString();
				total = total.add(new UFDouble(v));

			}
		} else {
			return ui.getBillCardPanel().calcurateTotal(key);
		}

		return total;
	}

	/**
	 * ��ͷ�б任��
	 * 
	 * ��ѡ����£�����ѡ�еĵ�һ��
	 * 
	 * �������ڣ�(2001-4-23 9:17:37)
	 * 
	 */
	/*public void valueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting())
			return;

		int[] selectedRows = getHeadTable().getSelectedRows();

		// //������
		// BillModel bmHead = getHeadBillModel();
		// for (int i = 0, iRowCount = bmHead.getRowCount(); i < iRowCount; i++)
		// {
		// bmHead.setRowState(i, BillModel.NORMAL);
		// }
		// for (int i = 0, len = selectedRows.length; i < len; i++) {
		// bmHead.setRowState(selectedRows[i], BillModel.SELECTED);
		// }

		// ��ѡ�б�ͷ�����ر���
		if (selectedRows.length > 0) {
			int selectRow = selectedRows[0];
			String csaleid = (String) getHeadBillModel().getValueAt(selectRow,
					"csaleid");

			SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);

			*//** it should never happen* *//*
			if (vo == null || vo.getParentVO() == null)
				return;

			if (vo.getChildrenVO() == null || vo.getChildrenVO().length <= 0) {
				loadBodyData(selectRow);
				getBodyBillModel().updateValue();
			} else {
				setListBodyByCurrency(vo.getBodyVOs()[0].getCcurrencytypeid());
				setBodyValueVO(vo.getChildrenVO());
				getBodyBillModel().updateValue();
			}

			ui.selectRow = selectRow;
		} else {
			getBodyBillModel().clearBodyData();
			getBodyTable().clearSelection();
			getBodyBillModel().updateValue();

			ui.selectRow = -1;
		}

		ui.setButtonsState();
	}*/

	private void fillCache(SaleorderHVO[] hvos) {

		if (hvos == null || hvos.length == 0 || vocache == null)
			return;

		SaleOrderVO[] saleordervos = new SaleOrderVO[hvos.length];
		for (int i = 0; i < hvos.length; i++) {
			saleordervos[i] = new SaleOrderVO();
			saleordervos[i].setParentVO(hvos[i]);
		}
		vocache.setCatheData(saleordervos);

	}

	public void fillCacheName() {

		SaleorderHVO[] hvos = (SaleorderHVO[]) getHeadBillModel()
				.getBodyValueVOs("nc.vo.so.so001.SaleorderHVO");
		if (hvos == null || hvos.length == 0 || vocache == null)
			return;

		SaleOrderVO saleordervo = null;
		String[] sNames = hvos[0].getAttributeNames();
		ArrayList alMustItems = getMustUpdateHeadNames();
		for (int i = 0; i < hvos.length; i++) {
			saleordervo = vocache.getSaleOrderVO(hvos[i].getCsaleid());
			if (saleordervo != null) {
				for (int k = 0; k < sNames.length; k++) {
					if ((alMustItems.contains(sNames[k]))
							|| ((hvos[i].getAttributeValue(sNames[k]) != null) && (saleordervo
									.getHeadVO().getAttributeValue(sNames[k]) == null
									|| saleordervo.getHeadVO()
											.getAttributeValue(sNames[k])
											.toString().trim().length() == 0 || !hvos[i]
									.getAttributeValue(sNames[k]).equals(
											saleordervo.getHeadVO()
													.getAttributeValue(
															sNames[k]))))) {
						saleordervo.getHeadVO().setAttributeValue(sNames[k],
								hvos[i].getAttributeValue(sNames[k]));
					}
				}
				vocache.setSaleOrderVO(hvos[i].getCsaleid(), saleordervo);
			}
		}

	}

	private void execBodyFormulas(String[] formulas, ArrayList lines) {
		if (formulas == null || formulas.length <= 0 || lines == null
				|| lines.size() <= 0)
			return;

		//��ù�ʽ������
		FormulaParse f = getBodyBillModel().getFormulaParse();
		f.setExpressArray(formulas);

		//��ñ�����
		VarryVO[] varrys = f.getVarryArray();

		//��������ֵ
		Hashtable[] hs = new Hashtable[varrys.length];

		for (int i = 0; i < varrys.length; i++) {
			VarryVO varry = varrys[i];
			Hashtable h = new Hashtable();
			if (varry.getVarry() != null) {
				for (int j = 0; j < varry.getVarry().length; j++) {
					String key = varry.getVarry()[j];
					int column = getBodyBillModel().getBodyColByKey(key);
					//���ñ��ʽ
					BillItem item = getBodyBillModel().getBodyItems()[column];

					String[] os = new String[lines.size()];
					for (int row = 0, count = lines.size(); row < count; row++) {

						Object o = "";
						int rowid = ((Integer) lines.get(row)).intValue();

						o = getBodyBillModel().getValueAt(rowid, key);
						String value = null;
						if (o != null && !o.equals("")) {
							if (item.getDataType() == BillItem.INTEGER
									|| item.getDataType() == BillItem.DECIMAL)
								value = o.toString();
							else
								value = "\"" + o.toString() + "\"";
						}

						os[row] = value;
					}

					h.put(key, os);
				}
			}
			hs[i] = h;
		}
		//���ñ���ֵ
		f.setDataSArray(hs);

		//���ý��
		String[][] results = f.getValueSArray();
		if (results != null) {
			for (int i = 0; i < results.length; i++) {
				String result[] = results[i];
				VarryVO varry = varrys[i];

				for (int row = 0, count = lines.size(); row < count; row++) {

					int rowid = ((Integer) lines.get(row)).intValue();

					String valueResult = result[row];
					String itemkey = varry.getFormulaName();

					if (itemkey != null
							&& !(itemkey = itemkey.trim()).equals("")) {
						//�����������ͣ�Ӧ�Թ�ʽ�ļ�����ȥ��С��
						int itemCol = getBodyBillModel().getBodyColByKey(
								itemkey);
						BillItem itemDest = getBodyBillModel().getBodyItems()[itemCol];

						if ((itemDest != null)
								&& (itemDest.getDataType() == BillItem.INTEGER)) {
							if (valueResult != null
									&& valueResult.indexOf(".") >= 0)
								valueResult = valueResult.substring(0,
										valueResult.indexOf("."));
						}
						//�������������͵Ĵ���

						getBodyBillModel().setValueAt(valueResult, rowid,
								itemkey);
					}
				}
			}
		}
	}

	public AggregatedValueObject getBillValueVO(int row, String billVOName,
			String headVOName, String bodyVOName) {

		CircularlyAccessibleValueObject hvo = getHeadBillModel()
				.getBodyValueRowVO(row, headVOName);
		CircularlyAccessibleValueObject[] bvos = getBodyBillModel()
				.getBodyValueVOs(bodyVOName);

		if (hvo == null || bvos == null || bvos.length <= 0)
			return null;

		AggregatedValueObject saleorder = null;

		try {

			saleorder = (AggregatedValueObject) Class.forName(billVOName)
					.newInstance();

		} catch (Exception e) {
			SCMEnv.out(e);
			saleorder = null;

		}

		if (saleorder == null)
			return null;

		Object otemp = getHeadBillModel().getValueAt(row, "npreceivemny");
		if (otemp != null && otemp.getClass() == String.class
				&& otemp.toString().trim().length() <= 0) {
			otemp = null;
		}

		hvo.setAttributeValue("npreceivemny", otemp);

		otemp = getHeadBillModel().getValueAt(row, "nreceiptcathmny");
		if (otemp != null && otemp.getClass() == String.class
				&& otemp.toString().trim().length() <= 0) {
			otemp = null;
		}
		hvo.setAttributeValue("nreceiptcathmny", otemp);

		saleorder.setParentVO(hvo);
		saleorder.setChildrenVO(bvos);
		
		return saleorder;
	}

	/**
	 * ��õ�ǰ�Ļ�����Ϣ��
	 *
	 * @version (00-6-13 10:51:14)
	 *
	 * @return ClientEnvironment
	 */
	protected ClientEnvironment getClientEnvironment() {
		if (ce == null) {
			ce = ClientEnvironment.getInstance();
		}
		return ce;
	}

	/**
	 * ��ò�ѯ������ �������ڣ�(2001-8-9 10:11:19)
	 *
	 * @return java.lang.String
	 */
	public String getWhere() {
		return m_strWhere;
	}

	/**
	 * �����С� 
	 * 
	 * �������ڣ�(2001-6-1 15:06:15)
	 * 
	 */
	private void hideTableCol() {
		String[] hideCols = { "ccustomerid", "cfreecustid", "cdeptid",
				"cemployeeid", "ctermprotocolid", "csalecorpid",
				"creceiptcorpid", "creceiptcustomerid", "ccalbodyid",
				"coperatorid", "capproveid", "ctransmodeid", "cwarehouseid" };

		for (int i = 0; i < hideCols.length; i++)
			hideHeadTableCol(hideCols[i]);

		hideBodyTableCol("boosflag");
		//��ʾ����״̬
		showHeadTableCol("fstatus");
	}

	/**
	 * ���ر������ݡ� 
	 * 
	 * �������ڣ�(2001-4-23 9:17:37)
	 * 
	 */
	public void loadBodyData(int row) {
		try {
			getBodyBillModel().clearBodyData();
			getBodyTable().clearSelection();
			getBodyBillModel().updateValue();

			if (row < 0)
				return;

			String csaleid = getHeadBillModel().getValueAt(row, "csaleid").toString();

			SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);

			/** it should never happen* */
			if (vo == null || vo.getParentVO() == null)
				return;

			if (vo.getChildrenVO() != null && vo.getChildrenVO().length > 0) {
				setListBodyByCurrency(vo.getBodyVOs()[0].getCcurrencytypeid());
				setBodyValueVO(vo.getChildrenVO());
				// ���ñ�����ʾ���
				BillTools.setExchangeRateDigitsByCurrency(getBodyBillModel(), vo
						.getChildrenVO(), "ccurrencytypeid", getCorp(), "nexchangeotobrate");

				getBodyBillModel().updateValue();
				getBodyBillModel().execLoadFormula();

				// ��������
				new UnitOfMeasureTool(getBodyBillModel()).calculateUnitOfMeasure();
				
				// ֧�ֲ��
				ui.getPluginProxy().afterSetBillVOsToListBody(vocache.getSaleOrderVO(csaleid).getBodyVOs());
				
				return;
			}
			
			getBodyBillModel().clearBodyData();

			long st = System.currentTimeMillis();
			long s = System.currentTimeMillis();
			SCMEnv.out("��ʼ��ѯ��������...");
			SaleorderBVO[] saleorderBVO = SaleOrderBO_Client
					.queryBodyAllData(csaleid);

			Arrays.sort(saleorderBVO);

			fillBodyCache(csaleid, saleorderBVO);

			SCMEnv.out("��ѯ��������[����ʱ" + CommonConstant.BEGIN_MARK
					+ (System.currentTimeMillis() - s)
					+ CommonConstant.END_MARK + "]");

			//�������־��ȱ仯
			if (saleorderBVO != null && saleorderBVO.length > 0)
				setListBodyByCurrency(saleorderBVO[0].getCcurrencytypeid());

			s = System.currentTimeMillis();
			setBodyValueVO(saleorderBVO);

			//���ñ�����ʾ���
			nc.ui.so.so001.panel.bom.BillTools
					.setExchangeRateDigitsByCurrency(getBodyBillModel(), saleorderBVO
							, "ccurrencytypeid", getCorp(),"nexchangeotobrate");
			
			s = System.currentTimeMillis();
			SCMEnv.out("��ʼִ�б��幫ʽ...");

			getBodyBillModel().execLoadFormula();

			SCMEnv.out("ִ�б��幫ʽ[����ʱ" + CommonConstant.BEGIN_MARK
					+ (System.currentTimeMillis() - s)
					+ CommonConstant.END_MARK + "]");

			s = System.currentTimeMillis();
			SCMEnv.out("��ʼ��������������Ϣ...");

			//��ͬ���������Я��
			ArrayList alist = new ArrayList();
			for (int i = 0; i < getBodyBillModel().getRowCount(); i++) {
				Object oCsourcebillid = getBodyBillModel().getValueAt(i,
						"csourcebillid");
				if (oCsourcebillid != null
						&& oCsourcebillid.toString().length() != 0) {
					alist.add(new Integer(i));
				}
			}//end for

			String[] bodyFormula = new String[3];
			bodyFormula[0] = "ctinvclassid->getColValue(ct_manage_b,invclid,pk_ct_manage_b,csourcebillbodyid)";
			bodyFormula[1] = "ctinvclass->getColValue(bd_invcl,invclassname,pk_invcl,ctinvclassid)";
			bodyFormula[2] = "ct_name->getColValue(ct_manage,ct_name,pk_ct_manage,ct_manageid)";

			execBodyFormulas(bodyFormula, alist);

			/** ��ʹ�����㷨���߼���* */
			// ����ԭ�Ҹ�������˰����
//			BillTools.calcViaPriceAll(getBodyBillModel(),
//					nc.vo.so.pub.SOBillType.SaleOrder);
			
			getBodyBillModel().updateValue();
			getBodyBillModel().execLoadFormula();

			// ��������
			new UnitOfMeasureTool(getBodyBillModel()).calculateUnitOfMeasure();
			
			fillBodyCacheName();

			SCMEnv.out("��������������Ϣ[����ʱ" + CommonConstant.BEGIN_MARK
					+ (System.currentTimeMillis() - s)
					+ CommonConstant.END_MARK + "]");
			SCMEnv.out("���ñ�����Ϣ[����ʱ" + CommonConstant.BEGIN_MARK
					+ (System.currentTimeMillis() - st)
					+ CommonConstant.END_MARK + "]");

			// ֧�ֲ��
			ui.getPluginProxy().afterSetBillVOsToListBody(vocache.getSaleOrderVO(csaleid).getBodyVOs());
			
		} catch (Exception e) {
			SCMEnv.out("�б�������ݼ���ʧ�ܣ�");
			SCMEnv.out(e);
			ui.showErrorMessage(e.getMessage());
			return;
		}
		SCMEnv.out("�б�������ݼ��سɹ���");
	}

	/**
	 * ���ر�ͷ���ݡ� 
	 * 
	 * ����ʱ������ѯ��ˢ��
	 * 
	 * �������ڣ�(2001-4-23 9:17:37)
	 * 
	 */
	private void loadHeadData() {
		try {

			SaleorderHVO[] saleorderHVO = SaleOrderBO_Client
					.queryHeadAllData(getWhereStrWhenLoad(),getObjIsWaitAudit());

			fillHeadData(saleorderHVO);

			// �б���ʾ���ݴ���  liuping zhangcheng  v5.3
			//this.getHeadBillModel().setRowState(0, BillModel.SELECTED);
			
		} catch (Exception e) {
			MessageDialog.showHintDlg(this,
					NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/*@res "��ʾ"*/,
					NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000310")/*@res "�б��ͷ���ݼ���ʧ��"*/);
			SCMEnv.out("�б��ͷ���ݼ���ʧ�ܣ�");
			SCMEnv.out(e);
			return;
		}
		SCMEnv.out("�б��ͷ���ݼ��سɹ���");

	}
	
	//��ʼ����ͷvo���ݣ����ݿ���û�д��ģ����Ҫչ�ֵģ�
	private void initHVO(SaleorderHVO[] saleorderHVOs){

	}

	/**
	 * �����ݼ��ص���ͷ
	 * 
	 * @param saleorderHVO
	 * 
	 */
	public void fillHeadData(SaleorderHVO[] saleorderHVO) {
		ui.vocache.clearCatheData();
		
		initHVO(saleorderHVO);
		
		fillCache(saleorderHVO);

		// �������۹�˾����Ϊ��ǰ��½��˾
		nc.vo.so.so016.SoVoTools.setVOsValue(saleorderHVO, "salecorp",
				getClientEnvironment().getCorporation().getUnitname());

		setHeaderValueVO(saleorderHVO);

		getHeadBillModel().execLoadFormula();

		if (saleorderHVO != null && saleorderHVO.length > 0) {
			// ���ñ�ͷ����
			/** ģ������Ѿ�����������֯����* */
			SaleorderHVO[] hvos = reSortHeadVOs(saleorderHVO);
			
			//���û��ʾ���
			//��ͷ
/*			nc.ui.so.so001.panel.bom.BillTools.setExchangeRateDigitsByCurrency
			(getHeadBillModel(), hvos, "ccurrencytypeid", getCorp(), "nexchangeotobrate");	*/		
			
			nc.ui.so.so001.panel.bom.BillTools.setMnyToHeadModelByCurrency(
					getHeadBillModel(), hvos, getCorp(), "ccurrencytypeid",
					new String[] { "npreceivemny", "nreceiptcathmny",
							"nheadsummny" });
		}

		fillCacheName();
	}

	private SaleorderHVO[] reSortHeadVOs(SaleorderHVO[] heads) {
		HashMap<String, Integer> index = new HashMap();
		for (int i = 0, len = heads.length; i < len; i++) {
			index.put(heads[i].getCsaleid(), i);
		}

		SaleorderHVO[] resorted = new SaleorderHVO[heads.length];
		for (int i = 0, len = getHeadBillModel().getRowCount(); i < len; i++) {
			resorted[i] = heads[index.get(getHeadBillModel().getValueAt(i,
					"csaleid").toString())];
		}

		return resorted;
	}
	
	/**
	 * ��ò�ѯ����
	 * 
	 * ����ʱ����ˢ�±�ͷ����
	 * 
	 * @param where
	 * @return
	 */
	private String getWhereStrWhenLoad() {

		if (m_strWhere != null)
			return m_strWhere;

		SaleorderHVO[] hvos = ui.vocache.getAllSaleOrderHVO();
		if (hvos.length == 0)
			return null;

		// �����ѯ����Ϊ�գ������������ݣ���˵��ֻ�ڿ�Ƭ���Ƶ�����Ҫˢ�»����е�����
		StringBuffer str = new StringBuffer("so_sale.csaleid in (");
		for (int i = 0, len = hvos.length; i < len; i++) {
			str.append("'" + hvos[i].getCsaleid() + "',");
		}

		return " (" + str.substring(0, str.length() - 1) + ")) ";
	}

	/**
	 * ���¼������ݡ� 
	 * 
	 * �������ڣ�(2001-4-23 9:17:37)
	 * 
	 */
	public void reLoadData() {
		getHeadBillModel().clearBodyData();
		getBodyBillModel().clearBodyData();

		loadHeadData();
		//getHeadTable().clearSelection();
	}

	/**
	 * ���ñ�����Ŀ 
	 * 
	 * �������ڣ�(01-2-26 13:29:17)
	 * 
	 */
	private void setBodyComboBox() {
		nc.ui.pub.beans.UIComboBox comHeadItem = (nc.ui.pub.beans.UIComboBox) getHeadItem(
				"fstatus").getComponent();
		int count = comHeadItem.getItemCount();
		if (count == 0) {
			comHeadItem.setTranslate(true);
			getHeadItem("fstatus").setWithIndex(true);
			comHeadItem.addItem("");
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/*@res "����"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000027")/*@res "����"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000030")/*@res "����"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000119")/*@res "�ر�"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000005")/*@res "����"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000128")/*@res "����"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000320")/*@res "����������"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000242")/*@res "����δͨ��"*/);
		}
		nc.ui.pub.beans.UIComboBox comBodyItem = (nc.ui.pub.beans.UIComboBox) getBodyItem(
				"frowstatus").getComponent();
		count = comBodyItem.getItemCount();
		if (count == 0) {
			getBodyItem("frowstatus").setWithIndex(true);
			comBodyItem.addItem("");
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/*@res "����"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000027")/*@res "����"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000030")/*@res "����"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000119")/*@res "�ر�"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000005")/*@res "����"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000128")/*@res "����"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000320")/*@res "����������"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000242")/*@res "����δͨ��"*/);
		}
		nc.ui.pub.beans.UIComboBox comBatch = (nc.ui.pub.beans.UIComboBox) getBodyItem(
				"fbatchstatus").getComponent();
		count = comBatch.getItemCount();
		if (count == 0) {
			getBodyItem("fbatchstatus").setWithIndex(true);
			comBatch.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/*@res "����"*/);
			comBatch.addItem(NCLangRes.getInstance().getStrByID(
					"40060301", "UPP40060301-000152")/*@res "��ָ��"*/);
			comBatch.addItem(NCLangRes.getInstance().getStrByID(
					"40060301", "UPP40060301-000153")/*@res "����"*/);
			comBatch.setSelectedIndex(0);
		}
	}

	public void setListBodyByCurrency(String ccurrencytypeid) {
		try {
			SOBillCardTools.setListPanelByCurrency(this, ccurrencytypeid,
					ClientEnvironment.getInstance().getCorporation()
							.getPk_corp(), BD302,"nexchangeotobrate",
					new String[] { "noriginalcurtaxmny", "noriginalcurmny",
							"noriginalcursummny", "noriginalcurdiscountmny" },
					new String[] { "ntaxmny", "nmny", "nsummny",
							"ndiscountmny", "ntalbalancemny" },  null, null);

		} catch (Exception e) {
			SCMEnv.out("���ݱ�������С��λ��ʧ��!");
			SCMEnv.out(e);
		}
	}

//	/**
//	 * ���ݱ��������۱�����
//	 */
//	public void setCurrTypeDigit(){
//		//���ݱ��������۱����ʾ���
//	    scmCurrTypeDigit = new ScmCurrLocRateBizDecimalListener(getHeadBillModel(),
//		        "ccurrencytypeid",new String[]{"nexchangeotobrate"},ui.getCorpPrimaryKey());
//	}
	
	/**
	 * ���õ������ݿ��ơ�
	 * 
	 * �������ڣ�(01-3-6 10:12:24)
	 * 
	 * @param bdData
	 *            ufbill.BillData
	 * 
	 */
	public void setListData(BillListData billTempletData) {
		super.setListData(billTempletData);

		hideTableCol();
		setBodyComboBox();

	}

	/**
	 * ���ò�ѯ������ 
	 * 
	 * �������ڣ�(2001-8-9 10:09:21)
	 *
	 * @param newWhere
	 *            java.lang.String
	 *            
	 */
	public void setWhere(String newWhere) {
		m_strWhere = newWhere;
	}

	protected SoTaskManager getSoTaskMagager() {
		if (soTaskManager == null) {
			soTaskManager = new SoTaskManager();
		}
		return soTaskManager;
	}

	public void fillBodyCache(String csaleid, SaleorderBVO[] bvos) {

		if (bvos == null || bvos.length == 0 || vocache == null)
			return;

		SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);
		if (vo == null)
			return;

		vo.getHeadVO().setAttributeValue("nexchangeotobrate", bvos[0].getNexchangeotobrate());
		
		vo.setChildrenVO(bvos);

	}

	public void fillBodyCacheName() {

		SaleorderBVO[] hvos = (SaleorderBVO[]) getBodyBillModel()
				.getBodyValueVOs("nc.vo.so.so001.SaleorderBVO");
		if (hvos == null || hvos.length == 0 || vocache == null)
			return;

		Hashtable ht = new Hashtable();

		for (int i = 0; i < hvos.length; i++)
			ht.put(hvos[i].getCorder_bid(), hvos[i]);

		SaleOrderVO saleordervo = vocache.getSaleOrderVO(hvos[0].getCsaleid());

		String[] sNames = hvos[0].getAttributeNames();

		SaleorderBVO bvo = null;
		ArrayList alMustItems = getMustUpdateBodyNames();
		for (int i = 0; i < saleordervo.getBodyVOs().length; i++) {

			if (ht.containsKey(saleordervo.getBodyVOs()[i].getCorder_bid())) {
				bvo = (SaleorderBVO) ht.get(saleordervo.getBodyVOs()[i]
						.getCorder_bid());

				for (int k = 0; k < sNames.length; k++) {
					if ((alMustItems.contains(sNames[k]))
							|| ((bvo.getAttributeValue(sNames[k]) != null) && (saleordervo
									.getBodyVOs()[i]
									.getAttributeValue(sNames[k]) == null
									|| saleordervo.getBodyVOs()[i]
											.getAttributeValue(sNames[k])
											.toString().trim().length() == 0 || !bvo
									.getAttributeValue(sNames[k])
									.equals(
											saleordervo.getBodyVOs()[i]
													.getAttributeValue(sNames[k]))))) {
						saleordervo.getBodyVOs()[i].setAttributeValue(
								sNames[k], bvo.getAttributeValue(sNames[k]));
					}
				}
			}
		}
		vocache.setSaleOrderVO(hvos[0].getCsaleid(), saleordervo);

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

	private ArrayList getMustUpdateHeadNames() {
		String[] heads = new String[] { "fstatus", "breceiptendflag",
				"boutendflag", "binvoicendflag", "ibalanceflag", "bpayendflag",
				"btransendflag", "ts", "capproveid", "dapprovedate"

		};
		ArrayList al = new ArrayList();

		for (int i = 0; i < heads.length; i++)
			al.add(heads[i]);
		return al;
	}
//
//	private BillListTools getBillListTools() {
//		return ui.getBillListTools();
//	}

	/**
	 * ���˫���¼� 
	 * 
	 * �������ڣ�(2001-6-20 17:19:03)
	 * 
	 */
	public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
		if (e.getPos() == BillItem.HEAD) {
			ui.selectRow = e.getRow();
			ui.onCard();
			ui.setButtonsState();
		}
	}

	/**
	 * ��ͷ����
	 * 
	 */
	public List getRelaSortObject() {
		ArrayList aldata = new ArrayList();

		CircularlyAccessibleValueObject[] obj = getHeadBillModel()
				.getBodyValueVOs(SaleorderHVO.class.getName());
		if (obj != null && obj.length > 0)
			for (int i = 0; i < obj.length; i++)
				aldata.add(obj[i]);
		return aldata;
	}

	/*public void addListSelectionLstn() {
		getHeadTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		getHeadTable().getSelectionModel().addListSelectionListener(this);
	}*/

	public void addBodyTotalLstn() {
		getBodyBillModel().addTotalListener(this);
	}

	public void addHeadSortRelaLstn() {
		getHeadBillModel().addSortRelaObjectListener(this);
	}

	public void afterEdit(BillEditEvent e) {
	}

	/**
	 * ��ͷ�б任��
	 * 
	 * ��ѡ����£�����ѡ�еĵ�һ��
	 * 
	 * zhangcheng v5.3 ��ԭ��ListSelectionListener��Ϊ��׼��BillEditListener
	 */
	public void bodyRowChange(BillEditEvent e) {
		
		int selectRow = e.getRow();
		loadBodyData(selectRow);

		// ��ѡ�б�ͷ�����ر���
		if (selectRow >= 0) {
			ui.selectRow = selectRow;
		} else {
			ui.selectRow = -1;
		}

		ui.setButtonsState();
	}

	public Object getObjIsWaitAudit() {
		return objIsWaitAudit;
	}

	public void setObjIsWaitAudit(Object objIsWaitAudit) {
		this.objIsWaitAudit = objIsWaitAudit;
	}

}
