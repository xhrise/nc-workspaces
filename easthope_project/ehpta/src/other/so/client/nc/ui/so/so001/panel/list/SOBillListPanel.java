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
 * 销售订单列表模板类
 * 
 * @author zhongwei
 *
 */
public class SOBillListPanel extends BillListPanel implements
		/*ListSelectionListener,*/ BillTableMouseListener, BillTotalListener,
		IBillRelaSortListener,BillEditListener {

	private String m_strWhere = null;
	
//	//币种监听控制类
//	private ScmCurrLocRateBizDecimalListener scmCurrTypeDigit;

	//待审批条件：objIsWaitAudit[0] -- "Y" 有待审批条件  "N" 无待审批条件
	//          objIsWaitAudit[1] -- 当前操作员ID
	private Object objIsWaitAudit = null;
	
	ClientEnvironment ce = null;

	protected BusinessCurrencyRateUtil currtype = null;

	protected ArrayList catchlist = new ArrayList();

	//公共参数
	protected UFBoolean BD302 = null; //是否主辅币核算

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
		if (ui.strShowState.equals("列表")) { /*-=notranslate=-*/
			for (int i = 0; i < getBodyBillModel().getRowCount(); i++) {
				UFBoolean blargessflag = getBodyBillModel().getValueAt(i,
						"blargessflag") == null ? new UFBoolean(false)
						: new UFBoolean(getBodyBillModel().getValueAt(i,
								"blargessflag").toString());

				// 20061026 增加缺货判断
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
	 * 表头行变换。
	 * 
	 * 多选情况下，处理选中的第一张
	 * 
	 * 创建日期：(2001-4-23 9:17:37)
	 * 
	 */
	/*public void valueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting())
			return;

		int[] selectedRows = getHeadTable().getSelectedRows();

		// //界面标记
		// BillModel bmHead = getHeadBillModel();
		// for (int i = 0, iRowCount = bmHead.getRowCount(); i < iRowCount; i++)
		// {
		// bmHead.setRowState(i, BillModel.NORMAL);
		// }
		// for (int i = 0, len = selectedRows.length; i < len; i++) {
		// bmHead.setRowState(selectedRows[i], BillModel.SELECTED);
		// }

		// 有选中表头，加载表体
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

		//获得公式解析器
		FormulaParse f = getBodyBillModel().getFormulaParse();
		f.setExpressArray(formulas);

		//获得变量名
		VarryVO[] varrys = f.getVarryArray();

		//给变量付值
		Hashtable[] hs = new Hashtable[varrys.length];

		for (int i = 0; i < varrys.length; i++) {
			VarryVO varry = varrys[i];
			Hashtable h = new Hashtable();
			if (varry.getVarry() != null) {
				for (int j = 0; j < varry.getVarry().length; j++) {
					String key = varry.getVarry()[j];
					int column = getBodyBillModel().getBodyColByKey(key);
					//设置表达式
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
		//设置变量值
		f.setDataSArray(hs);

		//设置结果
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
						//对于整数类型，应对公式的计算结果去掉小数
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
						//结束对整数类型的处理。

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
	 * 获得当前的环境信息。
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
	 * 获得查询条件。 创建日期：(2001-8-9 10:11:19)
	 *
	 * @return java.lang.String
	 */
	public String getWhere() {
		return m_strWhere;
	}

	/**
	 * 隐藏列。 
	 * 
	 * 创建日期：(2001-6-1 15:06:15)
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
		//显示单据状态
		showHeadTableCol("fstatus");
	}

	/**
	 * 加载表体数据。 
	 * 
	 * 创建日期：(2001-4-23 9:17:37)
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
				// 设置表体汇率精度
				BillTools.setExchangeRateDigitsByCurrency(getBodyBillModel(), vo
						.getChildrenVO(), "ccurrencytypeid", getCorp(), "nexchangeotobrate");

				getBodyBillModel().updateValue();
				getBodyBillModel().execLoadFormula();

				// 处理辅计量
				new UnitOfMeasureTool(getBodyBillModel()).calculateUnitOfMeasure();
				
				// 支持插件
				ui.getPluginProxy().afterSetBillVOsToListBody(vocache.getSaleOrderVO(csaleid).getBodyVOs());
				
				return;
			}
			
			getBodyBillModel().clearBodyData();

			long st = System.currentTimeMillis();
			long s = System.currentTimeMillis();
			SCMEnv.out("开始查询表体数据...");
			SaleorderBVO[] saleorderBVO = SaleOrderBO_Client
					.queryBodyAllData(csaleid);

			Arrays.sort(saleorderBVO);

			fillBodyCache(csaleid, saleorderBVO);

			SCMEnv.out("查询表体数据[共用时" + CommonConstant.BEGIN_MARK
					+ (System.currentTimeMillis() - s)
					+ CommonConstant.END_MARK + "]");

			//金额随币种精度变化
			if (saleorderBVO != null && saleorderBVO.length > 0)
				setListBodyByCurrency(saleorderBVO[0].getCcurrencytypeid());

			s = System.currentTimeMillis();
			setBodyValueVO(saleorderBVO);

			//设置表体汇率精度
			nc.ui.so.so001.panel.bom.BillTools
					.setExchangeRateDigitsByCurrency(getBodyBillModel(), saleorderBVO
							, "ccurrencytypeid", getCorp(),"nexchangeotobrate");
			
			s = System.currentTimeMillis();
			SCMEnv.out("开始执行表体公式...");

			getBodyBillModel().execLoadFormula();

			SCMEnv.out("执行表体公式[共用时" + CommonConstant.BEGIN_MARK
					+ (System.currentTimeMillis() - s)
					+ CommonConstant.END_MARK + "]");

			s = System.currentTimeMillis();
			SCMEnv.out("开始设置其他表体信息...");

			//合同存货类名称携带
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

			/** 请使用新算法工具计算* */
			// 计算原币辅计量无税单价
//			BillTools.calcViaPriceAll(getBodyBillModel(),
//					nc.vo.so.pub.SOBillType.SaleOrder);
			
			getBodyBillModel().updateValue();
			getBodyBillModel().execLoadFormula();

			// 处理辅计量
			new UnitOfMeasureTool(getBodyBillModel()).calculateUnitOfMeasure();
			
			fillBodyCacheName();

			SCMEnv.out("设置其他表体信息[共用时" + CommonConstant.BEGIN_MARK
					+ (System.currentTimeMillis() - s)
					+ CommonConstant.END_MARK + "]");
			SCMEnv.out("设置表体信息[共用时" + CommonConstant.BEGIN_MARK
					+ (System.currentTimeMillis() - st)
					+ CommonConstant.END_MARK + "]");

			// 支持插件
			ui.getPluginProxy().afterSetBillVOsToListBody(vocache.getSaleOrderVO(csaleid).getBodyVOs());
			
		} catch (Exception e) {
			SCMEnv.out("列表表体数据加载失败！");
			SCMEnv.out(e);
			ui.showErrorMessage(e.getMessage());
			return;
		}
		SCMEnv.out("列表表体数据加载成功！");
	}

	/**
	 * 加载表头数据。 
	 * 
	 * 调用时机：查询、刷新
	 * 
	 * 创建日期：(2001-4-23 9:17:37)
	 * 
	 */
	private void loadHeadData() {
		try {

			SaleorderHVO[] saleorderHVO = SaleOrderBO_Client
					.queryHeadAllData(getWhereStrWhenLoad(),getObjIsWaitAudit());

			fillHeadData(saleorderHVO);

			// 列表显示单据处理  liuping zhangcheng  v5.3
			//this.getHeadBillModel().setRowState(0, BillModel.SELECTED);
			
		} catch (Exception e) {
			MessageDialog.showHintDlg(this,
					NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/*@res "提示"*/,
					NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000310")/*@res "列表表头数据加载失败"*/);
			SCMEnv.out("列表表头数据加载失败！");
			SCMEnv.out(e);
			return;
		}
		SCMEnv.out("列表表头数据加载成功！");

	}
	
	//初始化表头vo数据（数据库中没有存而模板需要展现的）
	private void initHVO(SaleorderHVO[] saleorderHVOs){

	}

	/**
	 * 把数据加载到表头
	 * 
	 * @param saleorderHVO
	 * 
	 */
	public void fillHeadData(SaleorderHVO[] saleorderHVO) {
		ui.vocache.clearCatheData();
		
		initHVO(saleorderHVO);
		
		fillCache(saleorderHVO);

		// 设置销售公司名称为当前登陆公司
		nc.vo.so.so016.SoVoTools.setVOsValue(saleorderHVO, "salecorp",
				getClientEnvironment().getCorporation().getUnitname());

		setHeaderValueVO(saleorderHVO);

		getHeadBillModel().execLoadFormula();

		if (saleorderHVO != null && saleorderHVO.length > 0) {
			// 设置表头金额精度
			/** 模板可能已经排序，重新组织数据* */
			SaleorderHVO[] hvos = reSortHeadVOs(saleorderHVO);
			
			//设置汇率精度
			//表头
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
	 * 获得查询条件
	 * 
	 * 调用时机：刷新表头数据
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

		// 如果查询条件为空，但缓存有数据，则说明只在卡片下制单，需要刷新缓存中的数据
		StringBuffer str = new StringBuffer("so_sale.csaleid in (");
		for (int i = 0, len = hvos.length; i < len; i++) {
			str.append("'" + hvos[i].getCsaleid() + "',");
		}

		return " (" + str.substring(0, str.length() - 1) + ")) ";
	}

	/**
	 * 重新加载数据。 
	 * 
	 * 创建日期：(2001-4-23 9:17:37)
	 * 
	 */
	public void reLoadData() {
		getHeadBillModel().clearBodyData();
		getBodyBillModel().clearBodyData();

		loadHeadData();
		//getHeadTable().clearSelection();
	}

	/**
	 * 设置表体项目 
	 * 
	 * 创建日期：(01-2-26 13:29:17)
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
					"SCMCOMMON", "UPPSCMCommon-000340")/*@res "自由"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000027")/*@res "审批"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000030")/*@res "冻结"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000119")/*@res "关闭"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000005")/*@res "作废"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000128")/*@res "结束"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000320")/*@res "正在审批中"*/);
			comHeadItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000242")/*@res "审批未通过"*/);
		}
		nc.ui.pub.beans.UIComboBox comBodyItem = (nc.ui.pub.beans.UIComboBox) getBodyItem(
				"frowstatus").getComponent();
		count = comBodyItem.getItemCount();
		if (count == 0) {
			getBodyItem("frowstatus").setWithIndex(true);
			comBodyItem.addItem("");
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/*@res "自由"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000027")/*@res "审批"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000030")/*@res "冻结"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000119")/*@res "关闭"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000005")/*@res "作废"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000128")/*@res "结束"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000320")/*@res "正在审批中"*/);
			comBodyItem.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000242")/*@res "审批未通过"*/);
		}
		nc.ui.pub.beans.UIComboBox comBatch = (nc.ui.pub.beans.UIComboBox) getBodyItem(
				"fbatchstatus").getComponent();
		count = comBatch.getItemCount();
		if (count == 0) {
			getBodyItem("fbatchstatus").setWithIndex(true);
			comBatch.addItem(NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/*@res "自由"*/);
			comBatch.addItem(NCLangRes.getInstance().getStrByID(
					"40060301", "UPP40060301-000152")/*@res "批指定"*/);
			comBatch.addItem(NCLangRes.getInstance().getStrByID(
					"40060301", "UPP40060301-000153")/*@res "整批"*/);
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
			SCMEnv.out("根据币种设置小数位数失败!");
			SCMEnv.out(e);
		}
	}

//	/**
//	 * 根据币种设置折本汇率
//	 */
//	public void setCurrTypeDigit(){
//		//根据币种设置折本汇率精度
//	    scmCurrTypeDigit = new ScmCurrLocRateBizDecimalListener(getHeadBillModel(),
//		        "ccurrencytypeid",new String[]{"nexchangeotobrate"},ui.getCorpPrimaryKey());
//	}
	
	/**
	 * 设置单据数据控制。
	 * 
	 * 创建日期：(01-3-6 10:12:24)
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
	 * 设置查询条件。 
	 * 
	 * 创建日期：(2001-8-9 10:09:21)
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
	 * 鼠标双击事件 
	 * 
	 * 创建日期：(2001-6-20 17:19:03)
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
	 * 表头排序
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
	 * 表头行变换。
	 * 
	 * 多选情况下，处理选中的第一张
	 * 
	 * zhangcheng v5.3 将原先ListSelectionListener改为标准的BillEditListener
	 */
	public void bodyRowChange(BillEditEvent e) {
		
		int selectRow = e.getRow();
		loadBodyData(selectRow);

		// 有选中表头，加载表体
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
