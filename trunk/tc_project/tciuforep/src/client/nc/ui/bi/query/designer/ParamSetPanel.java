/*
 * �������� 2005-3-1
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
 * �����������
 */
public class ParamSetPanel extends UIPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//  ��������Դ
	private String m_defDsName = null;

	//����������
	private UITablePane[] m_tablePns = null;

	//��Ԫ�༭������
	private TableCellEditor[][] m_refEditorsArray = null;

	//�����������
	private ParamVO[][] m_paramsArray = null;

	//����Դ����
	//private String m_dsName = null;
	//���������������飨ҳǩ��ţ������������-������������ţ�
	private int[][][] m_iRefDepends = null;

	private UIPanel ivjPnNorth = null;

	private UITabbedPane ivjTabbedPn = null;

	/**
	 * ParamSetPanel ������ע�⡣
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
	 * ֹͣ�༭�����ò��ս������ʾֵ �������ڣ�(2001-7-31 13:02:41)
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
				//��������
				Integer iDataType = m_paramsArray[iIndex][row].getDataType();
				//���շ���ֵ����
				int iReturnType = 0;
				if (iDataType != null)
					if (iDataType.intValue() == ParamConst.REF_CODE)
						iReturnType = 1;
					else if (iDataType.intValue() == ParamConst.REF_NAME)
						iReturnType = 2;
				//��������ֵ
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
		//      ��ֹ�༭̬
		for (int i = 0; i < m_tablePns.length; i++)
			if (getTable(i).getCellEditor() != null)
				getTable(i).getCellEditor().stopCellEditing();
	}

	/**
	 * ��ҳǩͬ���������¼��
	 */
	public void resemble() {
		stopTableEditing();
		if (m_paramsArray == null || m_paramsArray.length == 1) {
			return;
		}

		String hint = nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
				"UPP10241201-001166")/*
									  * @res
									  * "�Ƿ񽫵�һҳǩ�в�����ȡֵ���Ƶ�����ҳǩ��ͬ��������(Y-�ǲ�ȷ����N-��ȷ��)"
									  */;
		int iResult = MessageDialog.showYesNoCancelDlg(this,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000099")/* @res "��ѯ����" */, hint);
		if (iResult == UIDialog.ID_CANCEL) {
			return;
		}

		int iTabCount = getTabbedPn().getTabCount();
		//��õ�ǰ��һҳǩ�Ĳ�����-����ֵ��Ӧ��ϵ
		int iLen = getTM(0).getRowCount();
		Hashtable<String, String> hashCodeValue = new Hashtable<String, String>();
		for (int i = 0; i < iLen; i++) {
			//��ò�����
			String paramCode = getTM(0).getValueAt(i, 0).toString();
			//��ò���ȡֵ
			Object objValue = getTM(0).getValueAt(i, 3);
			String paramValue = (objValue == null) ? "" : objValue.toString();
			//�����ϣ��
			hashCodeValue.put(paramCode, paramValue);
		}

		if (iResult == UIDialog.ID_YES) {
			for (int i = 1; i < iTabCount; i++) {
				iLen = getTM(i).getRowCount();
				for (int j = 0; j < iLen; j++) {
					//��ò�����
					String paramCode = getTM(i).getValueAt(j, 0).toString();
					if (hashCodeValue.containsKey(paramCode)) {
						//�������ò���ȡֵ����setValueAt��
						Object objValue = hashCodeValue.get(paramCode);
						getTM(i).setValueAt(objValue, j, 3);
					}
				}
			}
		}
	}

	/**
	 * �ı��ض��ַ���Ϊ��������ֵ �������ڣ�(2003-11-7 15:40:43)
	 * 
	 * @return java.lang.String
	 * @param value
	 *            java.lang.String
	 */
	private String changeEnvValue(String value) {
		if (value != null
				&& BIModelUtil.isElement(value, QueryConst.BASIC_LOGIN_ENVS)) {
			//��û�������
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
	 * �Ϸ���У�� �������ڣ�(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public String check() {
		Object obj = null;
		for (int j = 0; j < m_tablePns.length; j++) {
			int iLen = getTM(j).getRowCount();
			for (int i = 0; i < iLen; i++) {
				//����ֵ
				obj = getTM(j).getValueAt(i, 3);
				if (m_paramsArray[j][i].getIfMust() != null
						&& m_paramsArray[j][i].getIfMust().booleanValue())
					if (obj == null || obj.toString().trim().equals(""))
						return nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-001167", null,
								new String[] { "" + (j + 1), "" + (i + 1) })/*
																			 * @res
																			 * "��{0}ҳǩ�ĵ�{1}��ȡֵΪ������ "
																			 */;
			}
		}
		return null;
	}

	/**
	 * ����������� �������ڣ�(2004-4-28 15:55:07)
	 */
	private void doRefDepend(int iIndex, TableCellEditor editor, int row,
			int column) {

		//���������ϵ
		String strDepend = m_paramsArray[iIndex][row].getRefDepend();
		if (strDepend == null || strDepend.trim().equals(""))
			return;

		//���ݲ���������̬�滻��������
		int[][] iRefs = m_iRefDepends[iIndex];
		int iLen = (iRefs == null) ? 0 : iRefs.length;
		for (int i = 0; i < iLen; i++) {
			if (iRefs[i][0] == row) {
				//���������
				int iDependedRow = iRefs[i][1];
				//�������ֵ
				Object obj = getTM(iIndex).getValueAt(iDependedRow, 3);
				String strDependValue = (obj == null) ? "" : obj.toString();
				if (strDependValue.equals(""))
					continue;
				String strDependParam = getTM(iIndex).getValueAt(iDependedRow,
						0).toString();
				//���ݱ��������������ж��Ƿ�����ţ���������Ϊ'[param1]%'�Ƚ��Ѱ죩
				//int iDependedType =
				//m_paramsArray[iIndex][iDependedRow].getDataType().intValue();
				//if (iDependedType != ParamConst.DECIMAL
				//&& iDependedType != ParamConst.COMBO_DECIMAL)
				//strDependValue = "'" + strDependValue + "'";
				//�滻������ϵ
				strDepend = StringUtil.replaceAllString(strDepend, "["
						+ strDependParam + "]", strDependValue);
			}
		}
		//���²���ģ��
		UIRefPane refPn = (UIRefPane) ((UIRefCellEditor) editor).getComponent();
		AbstractRefModel rm = refPn.getRefModel();
		AppDebug.debug("�滻��������" + strDepend);//@devTools System.out.println("�滻��������" + strDepend);
		String[] subConds = strDepend.split(";");//���鳤������Ϊ1
		if (subConds[0].trim().length() > 0) {
			rm.setWherePart(subConds[0]);
		}
		//�������п�����Ҫ����setClassWherePart����
		if (subConds.length > 1 && subConds[1].trim().length() > 0
				&& rm instanceof AbstractRefTreeModel) {
			((AbstractRefTreeModel) rm).setClassWherePart(subConds[1]);
		}
		//rm.setUseDataPower(false);
		//rm.setCacheEnabled(false);
		//rm.setWherePart(rm.getWherePart() + " and " + strDepend);

		//ǿ��ˢ�£��ѵ�û�з�������
		rm.reloadData();
	}

	/**
	 * ��ñ����� �������ڣ�(2003-9-17 10:49:22)
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
					//�����������
					doRefDepend(iIndex, editor, row, column);
				}
				return comp;
			}

			public void editingStopped(ChangeEvent e) {
				int row = editingRow;
				int col = editingColumn;
				TableCellEditor editor = getCellEditor();
				//�����༭״̬
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
	 * ��ö������VO���� �������ڣ�(2003-8-8 14:57:25)
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
	 * ��ò���VO���� �������ڣ�(2003-8-8 14:57:25)
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
				//�������
				params[i].setParamCode(getTM(iIndex).getValueAt(i, 0)
						.toString());
				params[i].setParamName(getTM(iIndex).getValueAt(i, 1)
						.toString());
				params[i].setOperaCode(getTM(iIndex).getValueAt(i, 2)
						.toString());
				//ȡֵ
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
	 * ���� PnNorth ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���ݷ���ֵ���ͻ�ò��շ���ֵ �������ڣ�(02-3-19 17:29:56)
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
	 * ���ݷ���ֵ���ͻ�ö�ѡ���շ���ֵ������in�� �������ڣ�(02-3-19 17:29:56)
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
	 * ���� TabbedPn ����ֵ��
	 * 
	 * @return UITabbedPane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private UITable getTable(int iIndex) {
		return (UITable) m_tablePns[iIndex].getTable();
	}

	/**
	 * ��ñ�ģ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private DefaultTableModel getTM(int iIndex) {
		return (DefaultTableModel) getTable(iIndex).getModel();
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		AppDebug.debug("--------- δ��׽�����쳣 ---------");//@devTools System.out.println("--------- δ��׽�����쳣 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ʼ��ֵ��CellEditor �������ڣ�(2001-7-17 13:18:15)
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
			//��������
			int iDataType = m_paramsArray[iIndex][i].getDataType().intValue();
			switch (iDataType) {
			case ParamConst.REF_ID:
			case ParamConst.REF_CODE:
			case ParamConst.REF_NAME: {
				//ö����
				String strConsult = params[i].getConsultCode();
				//�ȽϷ�
				String strOpr = getTM(iIndex).getValueAt(i, 2).toString()
						.trim();
				//����Դ
				String dsName = m_paramsArray[iIndex][i].getDsName();
				//�������
				m_refEditorsArray[iIndex][i] = procRef(strConsult, strOpr,
						dsName);
				break;
			}
			case ParamConst.COMBO_STRING:
			case ParamConst.COMBO_DECIMAL: {
				//ö����
				String strConsult = params[i].getConsultCode();
				//����Դ
				String dsName = m_paramsArray[iIndex][i].getDsName();
				//����
				String[] strItems = BIModelUtil.delimString(strConsult, "@");
				//����������
				m_refEditorsArray[iIndex][i] = procCombo(strItems, dsName);
				break;
			}
			case ParamConst.STRING:
			case ParamConst.DECIMAL: {
				//ö����
				String strConsult = params[i].getConsultCode();
				//����Դ
				String dsName = m_paramsArray[iIndex][i].getDsName();
				//�߼���������
				if (strConsult.toLowerCase().startsWith("<html>"))
					break;
				int iBeginIndex = strConsult.indexOf("<");
				int iEndIndex = strConsult.indexOf(">");
				if (iBeginIndex != -1 && iEndIndex != -1) {
					//�������
					String className = strConsult.substring(iBeginIndex + 1,
							iEndIndex);
					//����߼��������տ�
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
	 * ��ʼ����� �������ڣ�(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	private void initTable(final int iIndex) {
		//��ģ��
		DefaultTableModel tm = new DefaultTableModel(new Object[] {
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000974")/* @res "������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000893")/* @res "��ʾ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000298")/* @res "������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001040") /* @res "����ֵ" */}, 0) {
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
					//ȡֵ����
					getTable(iIndex).getColumn(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10241201", "UPP10241201-001040")/*
																	  * @res
																	  * "����ֵ"
																	  */).setCellEditor(m_refEditorsArray[iIndex][iRow]);
					return true;
				}
			}
		};
		getTable(iIndex).setModel(tm);
		//���ñ�����
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
	 * ����߼��������տ� �������ڣ�(2003-9-17 15:24:54)
	 */
	private AdvParamEditor procAdvParam(String className, String dsName) {

		AdvParamEditor refEditor = new AdvParamEditor();
		refEditor.setDsName(dsName);
		try {
			//���䣨ʹ�����ҵ�������Ĺ��췽ʽ��
			Constructor constructor = Class.forName(className).getConstructor(
					new Class[] { Container.class });
			Object objClass = constructor.newInstance(new Object[] { this });
			//һ���Զ������
			IAdvParamDlg iDlg = (IAdvParamDlg) objClass;
			refEditor.setAdvParamDlg(iDlg);
		} catch (Exception e) {
			AppDebug.debug(e);
			MessageDialog.showWarningDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "��ѯ����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-001169")/* @res "������������" */);
		}
		return refEditor;
	}

	/**
	 * ���������� �������ڣ�(2003-9-17 15:24:54)
	 */
	private UIRefCellEditor procCombo(String[] strItems, String dsName) {

		UIRefCellEditor refEditor = null;
		int ilen = (strItems == null) ? 0 : strItems.length;
		if (ilen != 0) {
			JComboBox cbb = new UIComboBox();
			if (ilen == 1 && strItems[0].startsWith("select ")) {
				//ȱʡ�������������ס���˾���û������ڣ�
				Hashtable hashEnv = BIModelUtil.addEnvInfo(null);
				//����Զ��廷������
				//Object[] objEnvParams = new Object[] { m_iEnvParam, dsName,
				//		new EnvInfo(true) };
				//hashEnv.put(QueryConst.ENV_PARAM_KEY, objEnvParams);
				//�滻SQL�еĻ�������
				String sql = BIModelUtil.replaceHandSql(strItems[0], hashEnv);
				//��ѯö��
				ClientDataSet dataSet = new ClientDataSet();
				dataSet.setSQLString(sql);
				dataSet.setDsName(dsName);
				dataSet.open();
				//ɾ��ROWID��
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
				//ֱ��ö��
				for (int i = 0; i < ilen; i++)
					cbb.addItem(strItems[i]);
			}
			refEditor = new UIRefCellEditor(cbb);
		}
		return refEditor;
	}

	/**
	 * ������� �������ڣ�(2003-9-17 15:24:54)
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
					//�Զ������
					String className = strConsult.substring(iBeginIndex + 1,
							iEndIndex);
					//����
					int iLen = strConsult.length();
					if (iEndIndex != iLen - 1) {
						AppDebug.debug("�Զ��������");//@devTools System.out.println("�Զ��������");
						//�Զ��������
						String pkDefDef = strConsult.substring(iEndIndex + 1,
								iLen);
						DefaultDefdocRefModel drm = (DefaultDefdocRefModel) Class
								.forName(className).newInstance();
						drm.setPkdefdef(pkDefDef);
						refPn.setRefModel(drm);
					} else {
						AppDebug.debug("һ���Զ������");//@devTools System.out.println("һ���Զ������");
						//һ���Զ������
						AbstractRefModel rm = (AbstractRefModel) Class.forName(
								className).newInstance();
						//��ĩ������ѡ���ƹ���ѧ�������壩
						refPn.setNotLeafSelectedEnabled(rm
								.isNotLeafSelectedEnabled());
						refPn.setRefModel(rm);
					}
				} else {
					iBeginIndex = strConsult.indexOf("[");
					iEndIndex = strConsult.indexOf("]");
					if (iBeginIndex != -1 && iEndIndex != -1) {
						//��ѯ�Ƶ�����
						String info = strConsult.substring(iBeginIndex + 1,
								iEndIndex);
						//ȡ����ѯID���������ƣ�������ţ�������ţ��������
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
						//���ò�ѯ�Ƶ�����ģ��
						QERefModel qerm = new QERefModel();
						qerm.setRefInfo(strRefInfos, iFldIndices);
						qerm.setUseDataPower(false);
						refPn.setRefModel(qerm);
					} else {
						//ϵͳ����
						refPn.setRefNodeName(strConsult);
						//��ʱ
						//setWherePart�������setRefNodeName����
						//String originWhere =
						// refPn.getRefModel().getWherePart();
						//refPn.getRefModel().setWherePart(originWhere + " and
						// 120=120");
					}
				}
				//��ѡ
				refPn.setMultiSelectedEnabled(strOpr.equals("in"));
				//�Լ�
				refPn.setAutoCheck(!strOpr.equals("like")
						&& !strOpr.equals("in"));
				//���ܴ۸�������Ȩ�ޣ�like���ؿ��ƣ���Ϊ��ʼֵ%���ܲ��ȫ����
				refPn.getUITextField().setEditable(!strOpr.equals("in"));
				//��󳤶�
				refPn.setMaxLength(200);
				//��õ�ǰ�ڵ�����������Դ
				refPn.getRefModel().setDataSource(dsName);
				//�༭��
				refEditor = new UIRefCellEditor(refPn);
			} catch (Exception e) {
				AppDebug.debug(e);
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "��ѯ����" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-001169")/* @res "������������" */);
			}
		return refEditor;
	}

	/**
	 * ���ö������VO���� �������ڣ�(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public void setParamsArray(ParamVO[][] paramsArray, String[] ids) {
		int iLen = (paramsArray == null) ? 0 : paramsArray.length;
		//��¡
		m_paramsArray = new ParamVO[iLen][];
		for (int i = 0; i < iLen; i++) {
			Vector<ParamVO> vec = new Vector<ParamVO>();
			int subLen = paramsArray[i] == null ? 0 : paramsArray[i].length;
			for (int j = 0; j < subLen; j++) {
				ParamVO visibleParam = ((paramsArray[i][j]) == null) ? null
						: (ParamVO) paramsArray[i][j].clone();
				//���������β���
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
			//��ʼ�����
			initTable(i);
			//���ò���ֵ
			setParamVOs(m_paramsArray[i], i);
			//���ҳǩ
			getTabbedPn().add(m_tablePns[i], ids[i]);
			//��ʼ����Ԫ�༭��
			initRefValue(i, m_paramsArray[i]);
			//�����������
			int[][] iRefDepends = BIModelUtil
					.getParamRefDepends(m_paramsArray[i]);
			m_iRefDepends[i] = iRefDepends;
			//���ز�������
			getTable(i).removeColumn(
					getTable(i).getColumn(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10241201", "UPP10241201-000974")/*
																	  * @res
																	  * "������"
																	  */));
		}
	}

	/**
	 * ���ò���VO���� �������ڣ�(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	private void setParamVOs(ParamVO[] params, int iIndex) {
		getTM(iIndex).setNumRows(0);
		int iLen = (params == null) ? 0 : params.length;
		for (int i = 0; i < iLen; i++) {
			//����
			String value = changeEnvValue(params[i].getValue());
			Object[] objrows = new Object[] { params[i].getParamCode(),
					params[i].getParamName(), params[i].getOperaCode(), value };
			getTM(iIndex).addRow(objrows);
		}
	}

	/**
	 * �����Զ��廷�������ӿ� �������ڣ�(2004-12-2 17:09:38)
	 * 
	 * @param newM_iEnvParam
	 *            nc.vo.pub.querymodel.IEnvParam
	 */
	public void setIEnvParam(IEnvParam newIEnvParam) {
	}
}  