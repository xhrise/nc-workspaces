/*
 * 创建日期 2005-3-1
 *
 */
package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import nc.ui.bd.def.DefaultDefdocRefModel;
import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.querymodel.AdvParamEditor;
import nc.ui.pub.querymodel.IAdvParamDlg;
import nc.ui.pub.querymodel.QERefModel;
import nc.vo.bi.query.manager.BIDatasetUtil;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.dbbase.ClientDataSet;
import nc.vo.pub.querymodel.IEnvParam;
import nc.vo.pub.querymodel.ParamConst;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.QueryConst;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * @author yangxiong
 * 
 * 参数设置面板
 */
public class ParamSetPanel extends UIPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//  定义数据源
	private String m_defDsName = null;

	//表格面板数组
	private UITablePane[] m_tablePns = null;

	//单元编辑器数组
	private TableCellEditor[][] m_refEditorsArray = null;

	//多个参数数组
	private ParamVO[][] m_paramsArray = null;

	//数据源名称
	//private String m_dsName = null;
	//参数参照依赖数组（页签序号，依赖参数序号-被依赖参数序号）
	private int[][][] m_iRefDepends = null;

	private UIPanel ivjPnNorth = null;

	private UITabbedPane ivjTabbedPn = null;

	/**
	 * ParamSetPanel 构造子注解。
	 */
	public ParamSetPanel() {
		initialize();
	}

	public String getDefDsName() {
		return m_defDsName;
	}

	public void setDefDsName(String dsName) {
		m_defDsName = dsName;
	}

	/**
	 * 停止编辑后，设置参照结果和显示值 创建日期：(2001-7-31 13:02:41)
	 * 
	 * @param value
	 *            java.lang.Object
	 * @param row
	 *            int
	 * @param col
	 *            int
	 */
	protected void afterEdit(int iIndex, TableCellEditor editor, int row,
			int col) {

		Object value = editor.getCellEditorValue();
		getTable(iIndex).setValueAt(value, row, col); //11-20

		if ((editor instanceof UIRefCellEditor)) {
			Object temp = ((UIRefCellEditor) editor).getComponent();
			if (temp instanceof UIRefPane) {
				UIRefPane pane = (UIRefPane) temp;
				//数据类型
				Integer iDataType = m_paramsArray[iIndex][row].getDataType();
				//参照返回值类型
				int iReturnType = 0;
				if (iDataType != null)
					if (iDataType.intValue() == ParamConst.REF_CODE)
						iReturnType = 1;
					else if (iDataType.intValue() == ParamConst.REF_NAME)
						iReturnType = 2;
				//设置输入值
				String strOpr = getTM(iIndex).getValueAt(row, 2).toString()
						.trim();
				if (strOpr.equals("like")) {
					String strResult = getRefResult(pane, iReturnType);
					if (strResult != null && !strResult.equals(""))
						getTable(iIndex).setValueAt(strResult + "%", row, col);
				} else if (strOpr.equals("in")) {
					String strResult = getRefResult_in(pane, iReturnType);
					if (strResult != null && !strResult.equals(""))
						getTable(iIndex).setValueAt(strResult, row, col);
				} else {
					String strResult = getRefResult(pane, iReturnType);
					if (strResult != null && !strResult.equals(""))
						getTable(iIndex).setValueAt(strResult, row, col);
				}
			}
		}
	}

	public void stopTableEditing() {
		//      终止编辑态
		for (int i = 0; i < m_tablePns.length; i++)
			if (getTable(i).getCellEditor() != null)
				getTable(i).getCellEditor().stopCellEditing();
	}

	/**
	 * 多页签同名参数快捷录入
	 */
	public void resemble() {
		stopTableEditing();
		if (m_paramsArray == null || m_paramsArray.length == 1) {
			return;
		}

		String hint = nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
				"UPP10241201-001166")/*
									  * @res
									  * "是否将第一页签中参数的取值类推到其它页签的同名参数？(Y-是并确定，N-否并确定)"
									  */;
		int iResult = MessageDialog.showYesNoCancelDlg(this,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000099")/* @res "查询引擎" */, hint);
		if (iResult == UIDialog.ID_CANCEL) {
			return;
		}

		int iTabCount = getTabbedPn().getTabCount();
		//获得当前第一页签的参数名-参数值对应关系
		int iLen = getTM(0).getRowCount();
		Hashtable<String, String> hashCodeValue = new Hashtable<String, String>();
		for (int i = 0; i < iLen; i++) {
			//获得参数名
			String paramCode = getTM(0).getValueAt(i, 0).toString();
			//获得参数取值
			Object objValue = getTM(0).getValueAt(i, 3);
			String paramValue = (objValue == null) ? "" : objValue.toString();
			//加入哈希表
			hashCodeValue.put(paramCode, paramValue);
		}

		if (iResult == UIDialog.ID_YES) {
			for (int i = 1; i < iTabCount; i++) {
				iLen = getTM(i).getRowCount();
				for (int j = 0; j < iLen; j++) {
					//获得参数名
					String paramCode = getTM(i).getValueAt(j, 0).toString();
					if (hashCodeValue.containsKey(paramCode)) {
						//类推设置参数取值（仅setValueAt）
						Object objValue = hashCodeValue.get(paramCode);
						getTM(i).setValueAt(objValue, j, 3);
					}
				}
			}
		}
	}

	/**
	 * 改变特定字符串为环境变量值 创建日期：(2003-11-7 15:40:43)
	 * 
	 * @return java.lang.String
	 * @param value
	 *            java.lang.String
	 */
	private String changeEnvValue(String value) {
		if (value != null
				&& BIModelUtil.isElement(value, QueryConst.BASIC_LOGIN_ENVS)) {
			//获得环境变量
			String[] envs = BIModelUtil.getEnvInfo();
			String account = null;
			String corp = null;
			String user = null;
			String date = null;
			String yearmonth = null;
			String year = null;
			String month = null;
			if (envs != null) {
				account = envs[0];
				corp = envs[1];
				user = envs[2];
				date = envs[3];
				yearmonth = envs[4];
				year = envs[5];
				month = envs[6];
			}
			if (value.equals(QueryConst.LOGIN_ACCOUNT_ENV))
				value = account;
			else if (value.equals(QueryConst.LOGIN_CORP_ENV))
				value = corp;
			else if (value.equals(QueryConst.LOGIN_USER_ENV))
				value = user;
			else if (value.equals(QueryConst.LOGIN_DATE_ENV))
				value = date;
			else if (value.equals(QueryConst.LOGIN_YEARMONTH_ENV))
				value = yearmonth;
			else if (value.equals(QueryConst.LOGIN_YEAR_ENV))
				value = year;
			else if (value.equals(QueryConst.LOGIN_MONTH_ENV))
				value = month;
		}
		return value;
	}

	/**
	 * 合法性校验 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public String check() {
		Object obj = null;
		for (int j = 0; j < m_tablePns.length; j++) {
			int iLen = getTM(j).getRowCount();
			for (int i = 0; i < iLen; i++) {
				//参数值
				obj = getTM(j).getValueAt(i, 3);
				if (m_paramsArray[j][i].getIfMust() != null
						&& m_paramsArray[j][i].getIfMust().booleanValue())
					if (obj == null || obj.toString().trim().equals(""))
						return nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-001167", null,
								new String[] { "" + (j + 1), "" + (i + 1) })/*
																			 * @res
																			 * "第{0}页签的第{1}行取值为必输项 "
																			 */;
			}
		}
		return null;
	}

	/**
	 * 处理参照依赖 创建日期：(2004-4-28 15:55:07)
	 */
	private void doRefDepend(int iIndex, TableCellEditor editor, int row,
			int column) {

		//获得依赖关系
		String strDepend = m_paramsArray[iIndex][row].getRefDepend();
		if (strDepend == null || strDepend.trim().equals(""))
			return;

		//根据参数依赖动态替换过滤条件
		int[][] iRefs = m_iRefDepends[iIndex];
		int iLen = (iRefs == null) ? 0 : iRefs.length;
		for (int i = 0; i < iLen; i++) {
			if (iRefs[i][0] == row) {
				//获得依赖行
				int iDependedRow = iRefs[i][1];
				//获得依赖值
				Object obj = getTM(iIndex).getValueAt(iDependedRow, 3);
				String strDependValue = (obj == null) ? "" : obj.toString();
				if (strDependValue.equals(""))
					continue;
				String strDependParam = getTM(iIndex).getValueAt(iDependedRow,
						0).toString();
				//根据被依赖参数类型判断是否加引号（不处理，因为'[param1]%'比较难办）
				//int iDependedType =
				//m_paramsArray[iIndex][iDependedRow].getDataType().intValue();
				//if (iDependedType != ParamConst.DECIMAL
				//&& iDependedType != ParamConst.COMBO_DECIMAL)
				//strDependValue = "'" + strDependValue + "'";
				//替换依赖关系
				strDepend = StringUtil.replaceAllString(strDepend, "["
						+ strDependParam + "]", strDependValue);
			}
		}
		//更新参照模型
		UIRefPane refPn = (UIRefPane) ((UIRefCellEditor) editor).getComponent();
		AbstractRefModel rm = refPn.getRefModel();
		AppDebug.debug("替换后条件：" + strDepend);//@devTools System.out.println("替换后条件：" + strDepend);
		String[] subConds = strDepend.split(";");//数组长度至少为1
		if (subConds[0].trim().length() > 0) {
			rm.setWherePart(subConds[0]);
		}
		//树参照有可能需要调用setClassWherePart条件
		if (subConds.length > 1 && subConds[1].trim().length() > 0
				&& rm instanceof AbstractRefTreeModel) {
			((AbstractRefTreeModel) rm).setClassWherePart(subConds[1]);
		}
		//rm.setUseDataPower(false);
		//rm.setCacheEnabled(false);
		//rm.setWherePart(rm.getWherePart() + " and " + strDepend);

		//强制刷新（难道没有方法？）
		rm.reloadData();
	}

	/**
	 * 获得表格面板 创建日期：(2003-9-17 10:49:22)
	 * 
	 * @return UITablePane
	 */
	@SuppressWarnings("serial")
	private UITablePane getNewTablePn(final int iIndex) {
		UITable table = new UITable() {
			public Component prepareEditor(TableCellEditor editor, int row,
					int column) {
				Component comp = super.prepareEditor(editor, row, column);
				if (editor instanceof UIRefCellEditor) {
					//处理参照依赖
					doRefDepend(iIndex, editor, row, column);
				}
				return comp;
			}

			public void editingStopped(ChangeEvent e) {
				int row = editingRow;
				int col = editingColumn;
				TableCellEditor editor = getCellEditor();
				//结束编辑状态
				if (editor != null) {
					removeEditor();
					afterEdit(iIndex, editor, row, col);
				}
			}
		};
		UITablePane tablePn = new UITablePane();
		tablePn.setTable(table);
		return tablePn;
	}

	/**
	 * 获得多个参数VO数组 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public ParamVO[][] getParamsArray() {
		stopTableEditing();
		//ParamVO[][] paramsArray = new ParamVO[m_tablePns.length][];
		for (int i = 0; i < m_paramsArray.length; i++) {
			//paramsArray[i] = getParamVOs(i);
			getParamVOs(i);
		}
		return m_paramsArray;
	}

	/**
	 * 获得参数VO数组 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	private ParamVO[] getParamVOs(int iIndex) {
		ParamVO[] params = m_paramsArray[iIndex];
		int iLen = getTM(iIndex).getRowCount();
		if (iLen != 0) {
			//params = new ParamVO[iLen];
			for (int i = 0; i < iLen; i++) {
				//params[i] = new ParamVO();
				//获得属性
				params[i].setParamCode(getTM(iIndex).getValueAt(i, 0)
						.toString());
				params[i].setParamName(getTM(iIndex).getValueAt(i, 1)
						.toString());
				params[i].setOperaCode(getTM(iIndex).getValueAt(i, 2)
						.toString());
				//取值
				Object objValue = getTM(iIndex).getValueAt(i, 3);
				String value = (objValue == null) ? "" : objValue.toString();
				value = changeEnvValue(value);

				//int iDataType =
				// m_paramsArray[iIndex][i].getDataType().intValue();
				//if (iDataType == ParamConst.REF_ID
				//|| iDataType == ParamConst.REF_CODE
				//|| iDataType == ParamConst.REF_NAME)
				//if (!value.equals(""))
				//value = "'" + value + "'";
				params[i].setDataType(m_paramsArray[iIndex][i].getDataType());
				params[i].setValue(value);
			}
		}
		return params;
	}

	/**
	 * 返回 PnNorth 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnNorth() {
		if (ivjPnNorth == null) {
			try {
				ivjPnNorth = new UIPanel();
				ivjPnNorth.setName("PnNorth");
				ivjPnNorth.setPreferredSize(new Dimension(10, 5));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnNorth;
	}

	/**
	 * 根据返回值类型获得参照返回值 创建日期：(02-3-19 17:29:56)
	 * 
	 * @return java.lang.String
	 * @param refPn
	 *            UIRefPane
	 * @param iReturnType
	 *            int
	 */
	private String getRefResult(UIRefPane refPn, int iReturnType) {
		String strResult = null;
		switch (iReturnType) {
		case 0: {
			strResult = refPn.getRefPK();
			break;
		}
		case 1: {
			strResult = refPn.getRefCode();
			break;
		}
		case 2: {
			strResult = refPn.getRefName();
			break;
		}
		}
		return strResult;
	}

	/**
	 * 根据返回值类型获得多选参照返回值（用于in） 创建日期：(02-3-19 17:29:56)
	 * 
	 * @return java.lang.String
	 * @param refPn
	 *            UIRefPane
	 * @param iReturnType
	 *            int
	 */
	private String getRefResult_in(UIRefPane refPn, int iReturnType) {
		String[] strResults = null;
		switch (iReturnType) {
		case 0: {
			strResults = refPn.getRefPKs();
			break;
		}
		case 1: {
			strResults = refPn.getRefCodes();
			break;
		}
		case 2: {
			strResults = refPn.getRefNames();
			break;
		}
		}
		String strInResult = null;
		if (strResults != null) {
			strInResult = "(";
			for (int i = 0; i < strResults.length; i++) {
				strInResult += "'" + strResults[i] + "'";
				if (i < strResults.length - 1)
					strInResult += ",";
			}
			strInResult += ")";
		}
		return strInResult;
	}

	/**
	 * 返回 TabbedPn 特性值。
	 * 
	 * @return UITabbedPane
	 */
	/* 警告：此方法将重新生成。 */
	private UITabbedPane getTabbedPn() {
		if (ivjTabbedPn == null) {
			try {
				ivjTabbedPn = new UITabbedPane();
				ivjTabbedPn.setName("TabbedPn");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTabbedPn;
	}

	/**
	 * 获得表格 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private UITable getTable(int iIndex) {
		return (UITable) m_tablePns[iIndex].getTable();
	}

	/**
	 * 获得表模型 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private DefaultTableModel getTM(int iIndex) {
		return (DefaultTableModel) getTable(iIndex).getModel();
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		AppDebug.debug("--------- 未捕捉到的异常 ---------");//@devTools System.out.println("--------- 未捕捉到的异常 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			setName("UIDialogContentPane");
			setLayout(new BorderLayout());
			add(getPnNorth(), "North");
			add(getTabbedPn(), "Center");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * 初始化值的CellEditor 创建日期：(2001-7-17 13:18:15)
	 * 
	 * @param iRow
	 *            int
	 * @param iCol
	 *            int
	 */
	private void initRefValue(int iIndex, ParamVO[] params) {

		int iRowCount = getTM(iIndex).getRowCount();
		m_refEditorsArray[iIndex] = new TableCellEditor[iRowCount];
		for (int i = 0; i < iRowCount; i++) {
			//数据类型
			int iDataType = m_paramsArray[iIndex][i].getDataType().intValue();
			switch (iDataType) {
			case ParamConst.REF_ID:
			case ParamConst.REF_CODE:
			case ParamConst.REF_NAME: {
				//枚举项
				String strConsult = params[i].getConsultCode();
				//比较符
				String strOpr = getTM(iIndex).getValueAt(i, 2).toString()
						.trim();
				//数据源
				String dsName = m_paramsArray[iIndex][i].getDsName();
				//处理参照
				m_refEditorsArray[iIndex][i] = procRef(strConsult, strOpr,
						dsName);
				break;
			}
			case ParamConst.COMBO_STRING:
			case ParamConst.COMBO_DECIMAL: {
				//枚举项
				String strConsult = params[i].getConsultCode();
				//数据源
				String dsName = m_paramsArray[iIndex][i].getDsName();
				//解析
				String[] strItems = BIModelUtil.delimString(strConsult, "@");
				//处理下拉框
				m_refEditorsArray[iIndex][i] = procCombo(strItems, dsName);
				break;
			}
			case ParamConst.STRING:
			case ParamConst.DECIMAL: {
				//枚举项
				String strConsult = params[i].getConsultCode();
				//数据源
				String dsName = m_paramsArray[iIndex][i].getDsName();
				//高级参数参照
				if (strConsult.toLowerCase().startsWith("<html>"))
					break;
				int iBeginIndex = strConsult.indexOf("<");
				int iEndIndex = strConsult.indexOf(">");
				if (iBeginIndex != -1 && iEndIndex != -1) {
					//获得类名
					String className = strConsult.substring(iBeginIndex + 1,
							iEndIndex);
					//处理高级参数参照框
					m_refEditorsArray[iIndex][i] = procAdvParam(className,
							dsName);
				}
				break;
			}
			}
		}
		return;
	}

	/**
	 * 初始化表格 创建日期：(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	private void initTable(final int iIndex) {
		//表模型
		DefaultTableModel tm = new DefaultTableModel(new Object[] {
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000974")/* @res "参数名" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000893")/* @res "显示名" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000298")/* @res "操作符" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001040") /* @res "参数值" */}, 0) {
			public int getColumnCount() {
				return 4;
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				return Object.class;
			}

			public boolean isCellEditable(int iRow, int iCol) {
				if (iCol < 3)
					return false;
				else {
					//取值参照
					getTable(iIndex).getColumn(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10241201", "UPP10241201-001040")/*
																	  * @res
																	  * "参数值"
																	  */).setCellEditor(m_refEditorsArray[iIndex][iRow]);
					return true;
				}
			}
		};
		getTable(iIndex).setModel(tm);
		//设置表属性
		getTable(iIndex).setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable(iIndex).setColumnWidth(new int[] { 0, 140, 88, 352 });
		getTable(iIndex).sizeColumnsToFit(-1);
		getTable(iIndex).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable(iIndex).getTableHeader().setBackground(
				QueryConst.HEADER_BACK_COLOR);
		getTable(iIndex).getTableHeader().setForeground(
				QueryConst.HEADER_FORE_COLOR);
	}

	/**
	 * 处理高级参数参照框 创建日期：(2003-9-17 15:24:54)
	 */
	private AdvParamEditor procAdvParam(String className, String dsName) {

		AdvParamEditor refEditor = new AdvParamEditor();
		refEditor.setDsName(dsName);
		try {
			//反射（使用能找到父组件的构造方式）
			Constructor constructor = Class.forName(className).getConstructor(
					new Class[] { Container.class });
			Object objClass = constructor.newInstance(new Object[] { this });
			//一般自定义参照
			IAdvParamDlg iDlg = (IAdvParamDlg) objClass;
			refEditor.setAdvParamDlg(iDlg);
		} catch (Exception e) {
			AppDebug.debug(e);
			MessageDialog.showWarningDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-001169")/* @res "参照设置有误" */);
		}
		return refEditor;
	}

	/**
	 * 处理下拉框 创建日期：(2003-9-17 15:24:54)
	 */
	private UIRefCellEditor procCombo(String[] strItems, String dsName) {

		UIRefCellEditor refEditor = null;
		int ilen = (strItems == null) ? 0 : strItems.length;
		if (ilen != 0) {
			JComboBox cbb = new UIComboBox();
			if (ilen == 1 && strItems[0].startsWith("select ")) {
				//缺省环境变量（帐套、公司、用户、日期）
				Hashtable hashEnv = BIModelUtil.addEnvInfo(null);
				//添加自定义环境参数
				//Object[] objEnvParams = new Object[] { m_iEnvParam, dsName,
				//		new EnvInfo(true) };
				//hashEnv.put(QueryConst.ENV_PARAM_KEY, objEnvParams);
				//替换SQL中的环境变量
				String sql = BIModelUtil.replaceHandSql(strItems[0], hashEnv);
				//查询枚举
				ClientDataSet dataSet = new ClientDataSet();
				dataSet.setSQLString(sql);
				dataSet.setDsName(dsName);
				dataSet.open();
				//删除ROWID列
				int iColCount = dataSet.getColumnCount();
				if (iColCount > 0) {
					com.borland.dx.dataset.Column col = dataSet.getColumn(0);
					if (col.isRowId())
						dataSet.dropColumn(col);
				}
				//
				Vector vec = BIDatasetUtil.getObjArrayByDataset(dataSet);
				int iSize = (vec == null) ? 0 : vec.size();
				for (int i = 0; i < iSize; i++) {
					Object[] objRows = (Object[]) vec.elementAt(i);
					cbb.addItem(objRows[0]);
				}
			} else {
				//直接枚举
				for (int i = 0; i < ilen; i++)
					cbb.addItem(strItems[i]);
			}
			refEditor = new UIRefCellEditor(cbb);
		}
		return refEditor;
	}

	/**
	 * 处理参照 创建日期：(2003-9-17 15:24:54)
	 */
	private UIRefCellEditor procRef(String strConsult, String strOpr,
			String dsName) {

		UIRefCellEditor refEditor = null;
		if (strConsult != null && !strConsult.equals("")
				&& !strConsult.toLowerCase().startsWith("<html>"))
			try {
				UIRefPane refPn = new UIRefPane();
				refPn.getUITextField().setSingleQuoteInputEnabled(true);
				int iBeginIndex = strConsult.indexOf("<");
				int iEndIndex = strConsult.indexOf(">");
				if (iBeginIndex != -1 && iEndIndex != -1) {
					//自定义参照
					String className = strConsult.substring(iBeginIndex + 1,
							iEndIndex);
					//处理
					int iLen = strConsult.length();
					if (iEndIndex != iLen - 1) {
						AppDebug.debug("自定义项参照");//@devTools System.out.println("自定义项参照");
						//自定义项参照
						String pkDefDef = strConsult.substring(iEndIndex + 1,
								iLen);
						DefaultDefdocRefModel drm = (DefaultDefdocRefModel) Class
								.forName(className).newInstance();
						drm.setPkdefdef(pkDefDef);
						refPn.setRefModel(drm);
					} else {
						AppDebug.debug("一般自定义参照");//@devTools System.out.println("一般自定义参照");
						//一般自定义参照
						AbstractRefModel rm = (AbstractRefModel) Class.forName(
								className).newInstance();
						//非末级不可选（绕过宋学军的陷阱）
						refPn.setNotLeafSelectedEnabled(rm
								.isNotLeafSelectedEnabled());
						refPn.setRefModel(rm);
					}
				} else {
					iBeginIndex = strConsult.indexOf("[");
					iEndIndex = strConsult.indexOf("]");
					if (iBeginIndex != -1 && iEndIndex != -1) {
						//查询制导参照
						String info = strConsult.substring(iBeginIndex + 1,
								iEndIndex);
						//取出查询ID，参照名称，主键序号，编码序号，名称序号
						StringTokenizer st = new StringTokenizer(info, ",");
						String queryId = st.nextToken();
						String refTitle = st.nextToken();
						String idIndex = st.nextToken();
						String codeIndex = st.nextToken();
						String nameIndex = st.nextToken();
						//
						String[] strRefInfos = new String[] { queryId,
								m_defDsName, refTitle };
						int[] iFldIndices = new int[] {
								Integer.parseInt(idIndex),
								Integer.parseInt(codeIndex),
								Integer.parseInt(nameIndex) };
						//设置查询制导参照模型
						QERefModel qerm = new QERefModel();
						qerm.setRefInfo(strRefInfos, iFldIndices);
						qerm.setUseDataPower(false);
						refPn.setRefModel(qerm);
					} else {
						//系统参照
						refPn.setRefNodeName(strConsult);
						//临时
						//setWherePart必须放在setRefNodeName后面
						//String originWhere =
						// refPn.getRefModel().getWherePart();
						//refPn.getRefModel().setWherePart(originWhere + " and
						// 120=120");
					}
				}
				//多选
				refPn.setMultiSelectedEnabled(strOpr.equals("in"));
				//自检
				refPn.setAutoCheck(!strOpr.equals("like")
						&& !strOpr.equals("in"));
				//不能篡改已扩大权限（like不必控制，因为初始值%就能查出全部）
				refPn.getUITextField().setEditable(!strOpr.equals("in"));
				//最大长度
				refPn.setMaxLength(200);
				//获得当前节点所连的数据源
				refPn.getRefModel().setDataSource(dsName);
				//编辑器
				refEditor = new UIRefCellEditor(refPn);
			} catch (Exception e) {
				AppDebug.debug(e);
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "查询引擎" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-001169")/* @res "参照设置有误" */);
			}
		return refEditor;
	}

	/**
	 * 设置多个参数VO数组 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public void setParamsArray(ParamVO[][] paramsArray, String[] ids) {
		int iLen = (paramsArray == null) ? 0 : paramsArray.length;
		//克隆
		m_paramsArray = new ParamVO[iLen][];
		for (int i = 0; i < iLen; i++) {
			Vector<ParamVO> vec = new Vector<ParamVO>();
			int subLen = paramsArray[i] == null ? 0 : paramsArray[i].length;
			for (int j = 0; j < subLen; j++) {
				ParamVO visibleParam = ((paramsArray[i][j]) == null) ? null
						: (ParamVO) paramsArray[i][j].clone();
				//不加入隐形参数
				if (visibleParam == null || !visibleParam.isInvisible())
					vec.addElement(visibleParam);
			}
			m_paramsArray[i] = new ParamVO[vec.size()];
			vec.copyInto(m_paramsArray[i]);
		}
		m_tablePns = new UITablePane[iLen];
		m_refEditorsArray = new TableCellEditor[iLen][];
		m_iRefDepends = new int[iLen][][];

		getTabbedPn().removeAll();
		for (int i = 0; i < iLen; i++) {
			m_tablePns[i] = getNewTablePn(i);
			m_tablePns[i].setName(ids[i]);
			//初始化表格
			initTable(i);
			//设置参数值
			setParamVOs(m_paramsArray[i], i);
			//添加页签
			getTabbedPn().add(m_tablePns[i], ids[i]);
			//初始化表单元编辑器
			initRefValue(i, m_paramsArray[i]);
			//计算参照依赖
			int[][] iRefDepends = BIModelUtil
					.getParamRefDepends(m_paramsArray[i]);
			m_iRefDepends[i] = iRefDepends;
			//隐藏参数名列
			getTable(i).removeColumn(
					getTable(i).getColumn(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10241201", "UPP10241201-000974")/*
																	  * @res
																	  * "参数名"
																	  */));
		}
	}

	/**
	 * 设置参数VO数组 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	private void setParamVOs(ParamVO[] params, int iIndex) {
		getTM(iIndex).setNumRows(0);
		int iLen = (params == null) ? 0 : params.length;
		for (int i = 0; i < iLen; i++) {
			//增行
			String value = changeEnvValue(params[i].getValue());
			Object[] objrows = new Object[] { params[i].getParamCode(),
					params[i].getParamName(), params[i].getOperaCode(), value };
			getTM(iIndex).addRow(objrows);
		}
	}

	/**
	 * 设置自定义环境参数接口 创建日期：(2004-12-2 17:09:38)
	 * 
	 * @param newM_iEnvParam
	 *            nc.vo.pub.querymodel.IEnvParam
	 */
	public void setIEnvParam(IEnvParam newIEnvParam) {
	}
}  