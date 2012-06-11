package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.iuforeport.reporttool.temp.TXTFileFilter;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.fmtplugin.formula.ui.FormulaEditor;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.script.base.IParsed;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.table.CellsModel;

/**
 * 原类名：RepCheckMngDlg
 * 
 * @author zzl 2005-12-21
 */
public class CheckFmlMngDlg extends UfoDialog implements ActionListener,
		ListSelectionListener, MouseListener, TableModelListener,
		IUfoContextKey {

	private JButton ivjJBtnCancel = null;

	private JButton complexBtnAdd = null;

	private JButton simpleBtnAdd = null;

	private JButton complexBtnDelete = null;

	private JButton simpleBtnDelete = null;

	private JButton complexBtnUpdate = null;

	private JButton complexBtnExport = null;

	private JButton complexBtnImport = null;

	private JButton simpleBtnExport = null;

	private JButton simpleBtnImport = null;

	private JScrollPane complexScrollPane1 = null;

	private JScrollPane simpleJScrollPane1 = null;

	private JScrollPane simpErrJScrollPane1 = null;

	private JTextArea complexBatchEditPane = null;

	private JCheckBox complexBatchChk = null;

	private JTable complexTableCheck = null;

	private JTable simpleJTableCheck = null;

	private JPanel complePanel = null;

	private JPanel simplePanel = null;

	private JPanel ivjUfoDialogContentPane = null;

	private nc.ui.pub.beans.UITabbedPane jTabbedPane = null;

	private JLabel lbFormName = null;

	private JLabel lbForm = null;

	private JLabel lbErr = null;

	private JTextField ivjTxtName = null;

	private JTextField ivjTxtForm = null;

	private JTextArea ivjTxtErr = null;

	private JButton ivjBtnRefFunc = null;// 函数参照

	private JButton ivjJBtnOK = null;

	// private UfoReport m_container = null;

	public static int ID_ADD_COMPLEX = 4;

	public static int ID_UPDATE_COMPLEX = 5;

	public static int SIMPLE_CHECK_TAB_INDEX = 0;

	public static int COMPLEX_CHECK_TAB_INDEX = 1;

	public static int ID_EXPORT = 6;

	// private static String SIMPLE_EXORT_TAG_NAME="name";
	// private static String SIMPLE_EXORT_TAG_FML="formula";
	// private static String SIMPLE_EXORT_TAG_ERR="error";

	/**
	 * 审核表格模型，3.1增加
	 */
	private ComplexCheckMngTbModel m_complexCheckModel = null;

	private SimpleCheckMngTbModel m_simplecheckModel = null;

	private UfoFmlExecutor m_fmlExecutor = null;

	private IContext m_contextVO = null;

//	private UfoReport m_report = null;

	private int[] m_nSelectRows = null;

	private static final int iOffset = 20;
	
	private CellsModel cellsModel;

	public CheckFmlMngDlg(Container parent,CellsModel cellsModel, Vector vecComplexCheckFmls, 
			Vector vecSimpleCheckFmls, UfoFmlExecutor fmlExecutor,
			int iTabIndex, IContext contextVO) {
		super(parent);
		this.cellsModel = cellsModel;
//		m_report = parent;
		m_contextVO = contextVO;
		m_fmlExecutor = fmlExecutor;
		m_complexCheckModel = new ComplexCheckMngTbModel(vecComplexCheckFmls,
				m_fmlExecutor);
		m_simplecheckModel = new SimpleCheckMngTbModel(vecSimpleCheckFmls,
				m_fmlExecutor);
		initialize(iTabIndex);
	}

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ivjJBtnOK) {
			if (getComplexBatchChk().isSelected())
				if (!getComplexBatch())
					return;
			setResult(ID_OK);
			close();
		} else if (e.getSource() == ivjJBtnCancel) {
			setResult(ID_CANCEL);
			close();
		} else if (e.getSource() == complexBtnAdd) {
			setResult(ID_ADD_COMPLEX);
			close();
		} else if (e.getSource() == complexBtnDelete) {
			m_complexCheckModel.deleteRows(m_nSelectRows);
		} else if (e.getSource() == complexBtnUpdate) {
			setResult(ID_UPDATE_COMPLEX);
			close();
		} else if (e.getSource() == complexBtnExport) {
			doExport(false);
		} else if (e.getSource() == complexBatchChk) {
			// 批量编辑复选框
			if (getComplexBatchChk().isSelected() == true) {
				getComplexBatchPane().setText(getComplexCheckAsText());
				getComplexScrollPane1().setViewportView(getComplexBatchPane());

				getComplexBtnUpdate().setVisible(false);
				getComplexBtnAdd().setVisible(false);
				getComplexBtnDelete().setVisible(false);
				getComplexBtnExport().setVisible(false);
				getComplexBtnImport().setVisible(false);
			} else {
				getComplexScrollPane1().setViewportView(getComplexTableCheck());
				getComplexBtnUpdate().setVisible(true);
				getComplexBtnAdd().setVisible(true);
				getComplexBtnDelete().setVisible(true);
				getComplexBtnExport().setVisible(true);
				getComplexBtnImport().setVisible(true);

				getComplexBatch();
			}
		} else if (e.getSource() == complexBtnImport) {
			doComplexImport();
		} else if (e.getSource() == ivjBtnRefFunc) {
			// 简单审核中函数参照
			doRefFunc();
		} else if (e.getSource() == simpleBtnAdd) {
			// 简单审核中增加
			doSimpleAdd();
		} else if (e.getSource() == simpleBtnDelete) {
			m_simplecheckModel.deleteRows(simpleJTableCheck.getSelectedRows());
			setSimpleBtnExportEnable();
		} else if (e.getSource() == simpleBtnExport) {
			doExport(true);
		} else if (e.getSource() == simpleBtnImport) {
			// 简单审核中导入
			doSimpleImport();
		}
	}

	@Override
	public void close() {
		super.close();
		cellsModel = null;
	}

	// 将批量编辑的内容置回到高级审核的表格模型中
	private boolean getComplexBatch() {
		String strComplexBatch = getComplexBatchPane().getText().trim();

		Object[] objs = parseComplexCheck(strComplexBatch);
		String strErrMsg = (String) objs[1];
		if (strErrMsg != null && strErrMsg.length() > 0) {
			UfoPublic.sendWarningMessage(strErrMsg, null);
			return false;
		} else {
			m_complexCheckModel.updateDatas((Vector) objs[0]);
			return true;
		}

	}

	private void doComplexImport() {
		StringBuffer strBufImport = new StringBuffer();
		ArrayList list = getImportContent();
		if (list != null && list.size() > 0) {
			for (int i = 0, size = list.size(); i < size; i++) {
				strBufImport.append((String) list.get(i));
				strBufImport.append("\r\n");
			}
		}

		Object[] objs = parseComplexCheck(strBufImport.toString());
		String strErrMsg = (String) objs[1];
		if (strErrMsg != null && strErrMsg.length() > 0) {
			UfoPublic.sendWarningMessage(strErrMsg, null);
		}
		m_complexCheckModel.addRows((Vector) objs[0]);
		setTableRowHeight(false);
	}

	private ArrayList getImportContent() {
		javax.swing.JFileChooser fc = new UIFileChooser();
		fc.setApproveButtonText(StringResource
				.getStringResource("miufopublic156")); // "导入"
		fc.setDialogTitle(StringResource.getStringResource("miufopublic156")); // "导入"
		fc.setApproveButtonToolTipText(StringResource
				.getStringResource("miufopublic156")); // "导入"
		TXTFileFilter xf = new TXTFileFilter();
		fc.setFileFilter(xf);
		fc.setMultiSelectionEnabled(false);
		int returnVal = fc.showOpenDialog(this);

		ArrayList<String> listReturn = null;
		if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
			java.io.File file = fc.getSelectedFile();
			String pathname = file.getPath();
			if (pathname.indexOf(".txt") == -1
					&& pathname.indexOf(".TXT") == -1) {
				UfoPublic.sendWarningMessage(StringResource
						.getStringResource("miufo1000968"), null); // "请选择TXT文本文件！"
				return null;
			}

			try {
				// 得到文件,获得输入
				File newFile = new File(pathname);
				BufferedReader in = new BufferedReader(new FileReader(newFile));

				String readedLine = null;
				listReturn = new ArrayList<String>();
				while ((readedLine = in.readLine()) != null) {
					readedLine = readedLine.trim();
					if (readedLine.endsWith(";")) {
						readedLine = readedLine.substring(0, readedLine
								.length() - 1);
					}
					listReturn.add(readedLine);
				}
				in.close();
			} catch (java.io.IOException ex) {
				AppDebug.debug(ex);
			}
		}
		return listReturn;
	}

	/**
	 * 检查表内审核公式
	 * 
	 * @param strCheckFormulas
	 * @return object[0] 合格的复杂审核公式；object[1]错误信息
	 */
	private Object[] parseComplexCheck(String strCheckFormulas) {

		Object[] objReturns = new Object[2];
		if (strCheckFormulas == null || strCheckFormulas.length() == 0) {
			objReturns[1] = "";
			return objReturns;
		}

		StringBuffer bufErr = new StringBuffer();

		RepCheckFormulaChecker checker = new RepCheckFormulaChecker(
				m_fmlExecutor);

		StringTokenizer tokenizer = new StringTokenizer(strCheckFormulas, "}");
		Vector vec = new Vector();

		while (tokenizer.hasMoreTokens()) {
			String str = tokenizer.nextToken();
			if (str != null) {
				str = str != null ? str.trim() : null;
				// 获得审核公式名称、内容
				int iEqual = str.indexOf("=");
				if (iEqual < 0)
					continue;
				String strName = str.substring(0, iEqual);
				strName = strName != null ? strName.trim() : null;
				int iLastReturn = strName.lastIndexOf("\r\n");
				if (iLastReturn >= 0)
					strName = strName.substring(iLastReturn + "\r\n".length(),
							strName.length());

				int iFomulaStart1 = str.indexOf("{\r\n");
				int iFomulaStart2 = -1;
				if (iFomulaStart1 < 0) {
					iFomulaStart2 = str.indexOf("{\n");
				}
				String strFormula = str
						.substring((iFomulaStart1 < 0 ? iFomulaStart2
								: iFomulaStart1)
								+ (iFomulaStart1 < 0 ? "{\n".length() : "{\r\n"
										.length()));

				// 检查
				String strNameErr = checker.checkName(strName);
				StringBuffer bufFormulaErr = new StringBuffer();
				String strValidFormula = checker.checkCheckFormula(strFormula,
						bufFormulaErr);

				if (strNameErr == null && strValidFormula != null) {
					// 新建审核VO
					RepCheckVO repCheckVO = new RepCheckVO();
					repCheckVO.setID(RepCheckVO.getValidID());

					repCheckVO.setName(strName);
					repCheckVO.setFormula(strValidFormula);
					vec.add(repCheckVO);
				} else {
					// 添加错误信息
					if (strNameErr != null) {
						bufErr.append(StringResource.getStringResource(
								"miuforep014", new String[] { strName }));
						bufErr.append(strNameErr);
						bufErr.append("\r\n");
					}
					if (bufFormulaErr.length() > 0) {
						bufErr.append(StringResource.getStringResource(
								"miuforep012", new String[] { strName }));
						bufErr.append(bufFormulaErr.toString());
						bufErr.append("\r\n");
					}
				}
			}
		}
		// 加入新审核公式
		if (bufErr.length() == 0) {
			objReturns[0] = vec;
		}
		objReturns[1] = bufErr.toString();

		return objReturns;

	}

	private void doSimpleImport() {
		ArrayList listImport = getImportContent();
		if (listImport != null && listImport.size() > 0) {
			String strTemp = null;
			Vector vecImports = new Vector();
			SimpleCheckFmlVO simpleVO = null;
			StringTokenizer tokenizer = null;

			for (int i = 0, size = listImport.size(); i < size; i++) {
				strTemp = (String) listImport.get(i);

				tokenizer = new StringTokenizer(strTemp, ";");
				int iStart = 0;
				String strName = null;
				String strFml = null;
				String strErr = null;

				if (tokenizer.hasMoreTokens()) {
					strName = tokenizer.nextToken().trim();
				}
				if (tokenizer.hasMoreTokens()) {
					strFml = tokenizer.nextToken().trim();
				}
				if (tokenizer.hasMoreTokens()) {
					strErr = tokenizer.nextToken().trim();
				}

				// TODO 将未导入公式增加到日志信息
				simpleVO = getNewSimpleCheckVO(strName, strFml, strErr, false);
				if (simpleVO != null) {
					vecImports.add(simpleVO);
				}
				// if(strTemp.startsWith(SIMPLE_EXORT_TAG_NAME)==false)
				// continue;
				// int iPosFml=strTemp.indexOf(SIMPLE_EXORT_TAG_FML);
				// int iPosErr=strTemp.indexOf(SIMPLE_EXORT_TAG_ERR,iPosFml);
				// if(iPosFml<1 || iPosErr<iPosFml)
				// continue;
				// int iFirstEq=strTemp.indexOf("=");
				// int iSecondEq=strTemp.indexOf("=",iPosFml);
				// int iThirdEq=strTemp.indexOf("=",iPosErr);
				// if(iThirdEq>iSecondEq && iSecondEq>iFirstEq && iFirstEq>0){
				// int
				// iCommaTemp=strTemp.substring(iFirstEq,iSecondEq).lastIndexOf(",");
				// if(iCommaTemp<0)
				// continue;
				// int iFirstComma=iCommaTemp+iFirstEq;
				// iCommaTemp=strTemp.substring(iSecondEq,iThirdEq).lastIndexOf(",");
				// if(iCommaTemp<0)
				// continue;
				// int iSecondComma=iSecondEq+iCommaTemp;
				// if(iSecondComma< iPosErr && iSecondComma>iFirstComma &&
				// iFirstComma>0){
				//					
				// String
				// strName=strTemp.substring(iFirstEq+1,iFirstComma).trim();
				// String
				// strFml=strTemp.substring(iSecondEq+1,iSecondComma).trim();
				// String strErr=strTemp.substring(iThirdEq+1).trim();
				// simpleVO=getNewSimpleCheckVO(strName,strFml,strErr,false);
				// if(simpleVO!=null){
				// vecImports.add(simpleVO);
				// }
				// }
				// }
			}
			m_simplecheckModel.addRows(vecImports);
		}

	}

	private void setSimpleBtnExportEnable() {
		if (m_simplecheckModel.getActualRowCount() > 0)
			getSimpleBtnExport().setEnabled(true);
		else
			getSimpleBtnExport().setEnabled(false);
	}

	private void doSimpleAdd() {
		String strName = getJTxtName().getText();

		// 公式内容
		String strFmlUser = getJTxtForm().getText();

		// 错误信息
		String strErrMsg = getJTxtErr().getText();

		SimpleCheckFmlVO checkVO = getNewSimpleCheckVO(strName, strFmlUser,
				strErrMsg, true);
		if (checkVO != null) {
			int iSelectRow = getSimpleTableCheck().getSelectedRow();
			if (iSelectRow < 0
					|| iSelectRow == m_simplecheckModel.getActualRowCount()) {
				m_simplecheckModel.addRow(checkVO);
				getSimpleTableCheck().getSelectionModel().clearSelection();
				getJTxtName().setText("");
				getJTxtForm().setText("");
				getJTxtErr().setText("");
			} else {
				m_simplecheckModel.updateRow(checkVO, iSelectRow);
			}
		}

		setSimpleBtnExportEnable();

	}

	/**
	 * @i18n uiiufofmt00022=公式内容错误
	 * @i18n uiiufofmt00023=出错信息不能为空
	 */
	private SimpleCheckFmlVO getNewSimpleCheckVO(String strName,
			String strFmlUser, String strErrMsg, boolean bDisplayErrMsg) {

		String strMsg = ComplexCheckFmlDlg.checkName(strName);
		if (strMsg != null) {
			if (bDisplayErrMsg == true) {
				showmessage(strMsg);
				getJTxtName().setFocusable(true);
			}
			return null;
		}
		// 公式内容

		if (strFmlUser.trim().equals("")) {
			if (bDisplayErrMsg == true) {
				getJTxtForm().requestFocus();
				showmessage(StringResource.getStringResource("miufo1000916")); // "审核公式不能为空"
			}
			return null;
		}

		IParsed expr = null;
		try {
			expr = m_fmlExecutor.parseLogicExpr(strFmlUser.trim(), true);

		} catch (Exception e) {
			AppDebug.debug(e);
			if (bDisplayErrMsg == true) {
				showmessage(StringResource.getStringResource("uiiufofmt00022")
						+ ":" + e.getMessage());
				getJTxtForm().requestFocus();
			}
			return null;
		}
		String strFmlDB = m_fmlExecutor.getStrFmlByLet(expr, false);
		// 错误信息

		if (strErrMsg.trim().equals("")) {
			if (bDisplayErrMsg == true)
				showmessage(StringResource.getStringResource("uiiufofmt00023"));
			return null;
		}
		SimpleCheckFmlVO checkVO = new SimpleCheckFmlVO();
		checkVO.setID(SimpleCheckFmlVO.getValidID());
		checkVO.setErrMsg(strErrMsg);
		checkVO.setFmlName(strName);
		checkVO.setCheckCond(strFmlDB);
		checkVO.setParsedExpr(expr);

		return checkVO;
	}

	private void showmessage(String errs) {
		JOptionPane.showMessageDialog(this, errs, StringResource
				.getStringResource("miufo1000761"),
				JOptionPane.INFORMATION_MESSAGE); // "用友软件集团"
	}

	private void doRefFunc() {
		String unitID = null;
		boolean isPrivate = m_contextVO.getAttribute(PRIVATE) == null ? false
				: Boolean.parseBoolean(m_contextVO.getAttribute(PRIVATE)
						.toString());
		if (isPrivate) {
			unitID = (String) m_contextVO.getAttribute(CUR_UNIT_ID);
		}
		// @edit by wangyga at 2009-1-12,下午01:34:19
		CalcFmlDefWizardDlg dlgCellFuncWizard = new IufoCalcFmlDefWizardDlg(getParent(),cellsModel,m_contextVO);
		dlgCellFuncWizard.setUnitID(unitID);
		dlgCellFuncWizard.setContextVO(m_contextVO);

		setVisible(false);

		dlgCellFuncWizard.show();

		if (dlgCellFuncWizard.getResult() == UfoDialog.ID_OK) {
			String strWizard = dlgCellFuncWizard.getCellFunc();
			StringBuffer strBuf = new StringBuffer(getJTxtForm().getText());
			strBuf.append(strWizard);
			getJTxtForm().setText(strBuf.toString());
		}
		setVisible(true);

	}

	/**
	 * Return the JBtnAdd property value.
	 * 
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getComplexBtnAdd() {
		if (complexBtnAdd == null) {
			try {
				complexBtnAdd = new nc.ui.pub.beans.UIButton();
				complexBtnAdd.setName("JBtnAdd");

				complexBtnAdd.setText(StringResource
						.getStringResource("miufopublic242")); // "新建"
				complexBtnAdd.setBounds(85, 290, 75, 22);
				// user code begin {1}
				complexBtnAdd.addActionListener(this);
				complexBtnAdd.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return complexBtnAdd;
	}

	/**
	 * @i18n mbiadhoc00006=保存
	 */
	private javax.swing.JButton getSimpleBtnAdd() {
		if (simpleBtnAdd == null) {
			try {
				simpleBtnAdd = new nc.ui.pub.beans.UIButton();
				simpleBtnAdd.setName("simpleBtnAdd");

				simpleBtnAdd.setText(StringResource
						.getStringResource("mbiadhoc00006")); // TODO 保存
				simpleBtnAdd.setBounds(285, 5, 75, 22);
				// user code begin {1}
				simpleBtnAdd.addActionListener(this);
				simpleBtnAdd.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return simpleBtnAdd;
	}

	/**
	 * Return the JBtnCancel property value.
	 * 
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getJBtnCancel() {
		if (ivjJBtnCancel == null) {
			try {
				ivjJBtnCancel = new nc.ui.pub.beans.UIButton();
				ivjJBtnCancel.setName("JBtnCancel");
				// ivjJBtnCancel.setFont(new java.awt.Font("dialog", 0, 14));
				ivjJBtnCancel.setText(StringResource
						.getStringResource("miufopublic247")); // "取消"
				ivjJBtnCancel.setBounds(522, 380, 75, 22);
				// user code begin {1}
				ivjJBtnCancel.addActionListener(this);
				ivjJBtnCancel.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJBtnCancel;
	}

	/**
	 * Return the JBtnDelete property value.
	 * 
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getComplexBtnDelete() {
		if (complexBtnDelete == null) {
			try {
				complexBtnDelete = new nc.ui.pub.beans.UIButton();
				complexBtnDelete.setName("complexBtnDelete");
				// ivjJBtnDelete.setFont(new java.awt.Font("dialog", 0, 14));
				complexBtnDelete.setText(StringResource
						.getStringResource("miufopublic243")); // "删除"
				complexBtnDelete.setBounds(243, 290, 75, 22);
				// user code begin {1}
				complexBtnDelete.setEnabled(false);
				complexBtnDelete.addActionListener(this);
				complexBtnDelete.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return complexBtnDelete;
	}

	private javax.swing.JButton getSimpleBtnDelete() {
		if (simpleBtnDelete == null) {
			try {
				simpleBtnDelete = new nc.ui.pub.beans.UIButton();
				simpleBtnDelete.setName("simpleBtnDelete");

				simpleBtnDelete.setText(StringResource
						.getStringResource("miufopublic243")); // "删除"
				simpleBtnDelete.setBounds(373, 5, 75, 22);
				// user code begin {1}
				simpleBtnDelete.setEnabled(false);
				simpleBtnDelete.addActionListener(this);
				simpleBtnDelete.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return simpleBtnDelete;
	}

	private javax.swing.JButton getComplexBtnExport() {
		if (complexBtnExport == null) {
			try {
				complexBtnExport = new nc.ui.pub.beans.UIButton();
				complexBtnExport.setName("JBtnDelete");
				// ivjJBtnExport.setFont(new java.awt.Font("dialog", 0, 14));
				complexBtnExport.setText(StringResource
						.getStringResource("miufopublic157")); // "导出"
				complexBtnExport.setBounds(401, 290, 75, 22);
				// user code begin {1}
				complexBtnExport.setEnabled(getComplexCheckNum() > 0 ? true
						: false);
				complexBtnExport.addActionListener(this);
				complexBtnExport.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return complexBtnExport;
	}

	private javax.swing.JButton getComplexBtnImport() {
		if (complexBtnImport == null) {
			try {
				complexBtnImport = new nc.ui.pub.beans.UIButton();
				complexBtnImport.setName("complexBtnImport");
				complexBtnImport.setText(StringResource
						.getStringResource("miufo1000959")); // "导入"
				complexBtnImport.setBounds(322, 290, 75, 22);

				complexBtnImport.addActionListener(this);
				complexBtnImport.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return complexBtnImport;
	}

	private javax.swing.JButton getSimpleBtnExport() {
		if (simpleBtnExport == null) {
			try {
				simpleBtnExport = new nc.ui.pub.beans.UIButton();
				simpleBtnExport.setName("simpleBtnExport");
				// ivjJBtnExport.setFont(new java.awt.Font("dialog", 0, 14));
				simpleBtnExport.setText(StringResource
						.getStringResource("miufopublic157")); // "导出"
				simpleBtnExport.setBounds(258, 290, 75, 22);
				// user code begin {1}

				simpleBtnExport.addActionListener(this);
				simpleBtnExport.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);

				setSimpleBtnExportEnable();
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return simpleBtnExport;
	}

	private javax.swing.JButton getSimpleBtnImport() {
		if (simpleBtnImport == null) {
			try {
				simpleBtnImport = new nc.ui.pub.beans.UIButton();
				simpleBtnImport.setName("simpleBtnImport");
				simpleBtnImport.setText(StringResource
						.getStringResource("miufo1000959")); // "导入"
				simpleBtnImport.setBounds(170, 290, 75, 22);
				// user code begin {1}
				simpleBtnImport.addActionListener(this);
				simpleBtnImport.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return simpleBtnImport;
	}

	/**
	 * Return the JBtnOK property value.
	 * 
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getJBtnOK() {
		if (ivjJBtnOK == null) {
			try {
				ivjJBtnOK = new nc.ui.pub.beans.UIButton();
				ivjJBtnOK.setName("JBtnOK");
				// ivjJBtnOK.setFont(new java.awt.Font("dialog", 0, 14));
				ivjJBtnOK.setText(StringResource
						.getStringResource("miufo1000064")); // "完成"
				ivjJBtnOK.setBounds(422, 380, 75, 22);
				addToolTipAuto(ivjJBtnOK);
				// user code begin {1}
				ivjJBtnOK.addActionListener(this);
				ivjJBtnOK.registerKeyboardAction(this, KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJBtnOK;
	}

	/**
	 * Return the JBtnUpdate property value.
	 * 
	 * @return javax.swing.JButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JButton getComplexBtnUpdate() {
		if (complexBtnUpdate == null) {
			try {
				complexBtnUpdate = new nc.ui.pub.beans.UIButton();
				complexBtnUpdate.setName("complexBtnUpdate");
				// ivjJBtnUpdate.setFont(new java.awt.Font("dialog", 0, 14));
				complexBtnUpdate.setText(StringResource
						.getStringResource("miufopublic244")); // "修改"
				complexBtnUpdate.setBounds(164, 290, 75, 22);
				// user code begin {1}
				complexBtnUpdate.setEnabled(false);
				complexBtnUpdate.addActionListener(this);
				complexBtnUpdate.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return complexBtnUpdate;
	}

	/**
	 * Return the JList1 property value.
	 * 
	 * @return javax.swing.JList
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JTable getComplexTableCheck() {
		if (complexTableCheck == null) {
			try {
				complexTableCheck = new nc.ui.pub.beans.UITable(
						m_complexCheckModel, getComplexTblColModel());
				complexTableCheck.setName("JTableChecks");
				complexTableCheck.setBounds(0, 0, 580, 280);
				// user code begin {1}
				complexTableCheck.getTableHeader().setReorderingAllowed(false);
				complexTableCheck.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				setTableRowHeight(false);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return complexTableCheck;
	}

	private javax.swing.JTable getSimpleTableCheck() {
		if (simpleJTableCheck == null) {
			try {
				simpleJTableCheck = new nc.ui.pub.beans.UITable(
						m_simplecheckModel, getSimpleTblColModel());
				simpleJTableCheck.setName("simpleTableChecks");
				simpleJTableCheck.setBounds(0, 0, 580, 213);
				// user code begin {1}
				simpleJTableCheck.getTableHeader().setReorderingAllowed(false);
				simpleJTableCheck.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				simpleJTableCheck.setRowSelectionAllowed(true);
				// simpleJTableCheck.getSelectionModel().setLeadSelectionIndex(m_simplecheckModel.getRowCount()-1);
				setTableRowHeight(true);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return simpleJTableCheck;
	}

	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JScrollPane getComplexScrollPane1() {
		if (complexScrollPane1 == null) {
			try {
				complexScrollPane1 = new UIScrollPane();
				complexScrollPane1.setName("JScrollPane1");
				complexScrollPane1.setBounds(0, 0, 580, 280);
				getComplexScrollPane1().setViewportView(getComplexTableCheck());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return complexScrollPane1;
	}

	private javax.swing.JScrollPane getSimpleScrollPane1() {
		if (simpleJScrollPane1 == null) {
			try {
				simpleJScrollPane1 = new UIScrollPane();
				simpleJScrollPane1.setName("simpleScrollPane1");
				simpleJScrollPane1.setBounds(0, 75 + iOffset, 580,
						213 - iOffset);
				simpleJScrollPane1.setViewportView(getSimpleTableCheck());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return simpleJScrollPane1;
	}

	/**
	 * Return the UfoDialogContentPane property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getUfoDialogContentPane() {
		if (ivjUfoDialogContentPane == null) {
			try {
				ivjUfoDialogContentPane = new UIPanel();
				ivjUfoDialogContentPane.setName("UfoDialogContentPane");
				ivjUfoDialogContentPane.setLayout(null);
				getUfoDialogContentPane().add(getJTabbedPane(),
						getJTabbedPane().getName());
				// getUfoDialogContentPane().add(getComplexBtnAdd(),
				// getComplexBtnAdd().getName());
				// getUfoDialogContentPane().add(getJBtnUpdate(),
				// getJBtnUpdate().getName());
				// getUfoDialogContentPane().add(getJBtnDelete(),
				// getJBtnDelete().getName());
				getUfoDialogContentPane().add(getJBtnCancel(),
						getJBtnCancel().getName());
				getUfoDialogContentPane().add(getJBtnOK(),
						getJBtnOK().getName());
				// getUfoDialogContentPane().add(getComplexBtnExport(),
				// getComplexBtnExport().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUfoDialogContentPane;
	}

	private javax.swing.JPanel getComplexPane() {
		if (complePanel == null) {
			try {
				complePanel = new UIPanel();
				complePanel.setName("complePanel");
				complePanel.setLayout(null);
				complePanel.setBounds(0, 0, 580, 350);
				complePanel.add(getComplexScrollPane1(),
						getComplexScrollPane1().getName());
				complePanel.add(getComplexBatchChk(), getComplexBatchChk()
						.getName());
				complePanel.add(getComplexBtnAdd(), getComplexBtnAdd()
						.getName());
				complePanel.add(getComplexBtnUpdate(), getComplexBtnUpdate()
						.getName());
				complePanel.add(getComplexBtnDelete(), getComplexBtnDelete()
						.getName());
				complePanel.add(getComplexBtnImport(), getComplexBtnImport()
						.getName());
				complePanel.add(getComplexBtnExport(), getComplexBtnExport()
						.getName());

				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return complePanel;
	}

	private javax.swing.JPanel getSimplePane() {
		if (simplePanel == null) {
			try {
				simplePanel = new UIPanel();
				simplePanel.setName("simplePanel");
				simplePanel.setLayout(null);
				simplePanel.setBounds(0, 0, 180, 350);
				simplePanel.add(getLbFormName(), getLbFormName().getName());
				simplePanel.add(getJTxtName(), getJTxtName().getName());
				simplePanel.add(getLbForm(), getLbForm().getName());
				simplePanel.add(getJTxtForm(), getJTxtForm().getName());
				simplePanel.add(getJBtnRefFunc(), getJBtnRefFunc().getName());
				simplePanel.add(getLbErr(), getLbErr().getName());
				// simplePanel.add(getJTxtErr(),getJTxtErr().getName());
				simplePanel.add(getScrollTxtErr(), getScrollTxtErr().getName());

				simplePanel.add(getSimpleScrollPane1(), getSimpleScrollPane1()
						.getName());

				simplePanel.add(getSimpleBtnAdd(), getSimpleBtnAdd().getName());
				simplePanel.add(getSimpleBtnDelete(), getSimpleBtnDelete()
						.getName());
				simplePanel.add(getSimpleBtnImport(), getSimpleBtnImport()
						.getName());
				simplePanel.add(getSimpleBtnExport(), getSimpleBtnExport()
						.getName());

				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return simplePanel;
	}

	/**
	 * @i18n uiiufofmt00024=一般审核
	 * @i18n uiiufofmt00025=高级审核
	 */
	private nc.ui.pub.beans.UITabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new nc.ui.pub.beans.UITabbedPane();
			jTabbedPane.setName("TabbedPane");
			jTabbedPane.setTabPlacement(nc.ui.pub.beans.UITabbedPane.TOP);
			jTabbedPane.setEnabled(true);
			jTabbedPane.setDebugGraphicsOptions(0);
			jTabbedPane.setDoubleBuffered(false);
			jTabbedPane.setRequestFocusEnabled(true);
			jTabbedPane.setToolTipText("");
			jTabbedPane.setVerifyInputWhenFocusTarget(true);
			jTabbedPane.setBounds(20, 20, 580, 350);

			jTabbedPane.add(getSimplePane(), StringResource
					.getStringResource("uiiufofmt00024"),
					SIMPLE_CHECK_TAB_INDEX); // TODO Stringresource 一般审核
			jTabbedPane.add(getComplexPane(), StringResource
					.getStringResource("uiiufofmt00025"),
					COMPLEX_CHECK_TAB_INDEX); // TODO Stringresource 高级审核
		}
		return jTabbedPane;
	}

	/**
	 * 返回审核公式列表。 创建日期：(2001-1-9 16:50:50)
	 * 
	 * @return java.lang.String
	 */
	public Vector getComplexChecks() {
		if (m_complexCheckModel != null) {
			return m_complexCheckModel.getComplexCheckFms();
		} else {
			return null;
		}
	}

	public Vector getSimpleChecks() {
		if (m_simplecheckModel != null) {
			return m_simplecheckModel.getSimpleCheckFms();
		} else {
			return null;
		}
	}

	private int getComplexCheckNum() {

		if (m_complexCheckModel != null) {
			return m_complexCheckModel.getComplexCheckFms().size();
		} else {
			return 0;
		}
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		AppDebug.debug(exception);// @devTools
									// exception.printStackTrace(System.out);
	}

	/**
	 * @i18n uiufochk002=公式名称
	 */
	private javax.swing.JLabel getLbFormName() {
		if (lbFormName == null) {
			try {
				lbFormName = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				lbFormName.setName("lbFormName");
				// ivjLbForm.setFont(new java.awt.Font("dialog", 0, 14));
				lbFormName.setText(StringResource
						.getStringResource("uiufochk002")); // TODO 公式名称
				lbFormName.setBounds(5, 5, 70, 20);
				lbFormName.setForeground(java.awt.Color.black);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return lbFormName;
	}

	private javax.swing.JLabel getLbForm() {
		if (lbForm == null) {
			try {
				lbForm = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				lbForm.setName("lbForm");
				// ivjLbForm.setFont(new java.awt.Font("dialog", 0, 14));
				lbForm.setText(StringResource
						.getStringResource("miufopublic467"));
				lbForm.setBounds(5, 28, 70, 20);
				lbForm.setForeground(java.awt.Color.black);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return lbForm;
	}

	/**
	 * @i18n uiiufofmt00026=出错信息
	 */
	private javax.swing.JLabel getLbErr() {
		if (lbErr == null) {
			try {
				lbErr = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				lbErr.setName("lbErr");
				// ivjLbForm.setFont(new java.awt.Font("dialog", 0, 14));
				lbErr.setText(StringResource
						.getStringResource("uiiufofmt00026"));// TODO 出错信息
				lbErr.setBounds(5, 51, 70, 20);
				lbErr.setForeground(java.awt.Color.black);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return lbErr;
	}

	private javax.swing.JTextField getJTxtName() {
		if (ivjTxtName == null) {
			try {
				ivjTxtName = new JTextField();
				ivjTxtName.setName("txtName");
				// ivjTxtName.setFont(new java.awt.Font("dialog", 0, 14));
				ivjTxtName.setBounds(80, 5, 200, 20);
				ivjTxtName.setForeground(java.awt.Color.black);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTxtName;
	}

	javax.swing.JTextField getJTxtForm() {
		if (ivjTxtForm == null) {
			try {
				ivjTxtForm = new JTextField();
				ivjTxtForm.setName("ivjTxtForm");
				// ivjTxtName.setFont(new java.awt.Font("dialog", 0, 14));
				ivjTxtForm.setBounds(80, 28, 455, 20);
				ivjTxtForm.setForeground(java.awt.Color.black);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTxtForm;
	}

	private JScrollPane getScrollTxtErr() {
		if (simpErrJScrollPane1 == null) {
			simpErrJScrollPane1 = new UIScrollPane();
			simpErrJScrollPane1.setName("simpErrJScrollPane1");
			simpErrJScrollPane1.setBounds(80, 51, 490, 20 + iOffset);
			simpErrJScrollPane1.setViewportView(getJTxtErr());
		}
		return simpErrJScrollPane1;
	}

	private javax.swing.JTextArea getJTxtErr() {
		if (ivjTxtErr == null) {
			try {
				ivjTxtErr = new JTextArea();
				ivjTxtErr.setName("ivjTxtErr");
				// ivjTxtName.setFont(new java.awt.Font("dialog", 0, 14));
				// ivjTxtErr.setBounds(80, 51, 490, 20+iOffset);
				ivjTxtErr.setForeground(java.awt.Color.black);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTxtErr;
	}

	private javax.swing.JButton getJBtnRefFunc() {
		if (ivjBtnRefFunc == null) {
			try {
				ivjBtnRefFunc = new nc.ui.pub.beans.UIButton();
				ivjBtnRefFunc.setName("ivjBtnRefFunc");
				// ivjBtnRefFunc.setText("Fx"); //"函数参照"
				ivjBtnRefFunc.setIcon(ResConst
						.getImageIcon("reportcore/ref_down.gif"));
				ivjBtnRefFunc.setBounds(540, 28, 30, 20);
				ivjBtnRefFunc.setForeground(java.awt.Color.black);
				// user code begin {1}
				ivjBtnRefFunc.addActionListener(this);
				ivjBtnRefFunc.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnRefFunc;
	}

	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize(int iTabIndex) {
		try {
			// user code begin {1}
			// user code end
			setName("RepCheckMngDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(640, 440);
			setTitle(StringResource.getStringResource("miufo1001133")); // "表内审核公式管理"
			setContentPane(getUfoDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		getJTabbedPane().setSelectedIndex(iTabIndex);

		setLocationRelativeTo(this);
		complexBtnAdd.setNextFocusableComponent(complexBtnUpdate);
		complexBtnUpdate.setNextFocusableComponent(complexBtnDelete);
		complexBtnDelete.setNextFocusableComponent(ivjJBtnCancel);

		ivjJBtnCancel.setNextFocusableComponent(complexTableCheck);
		getComplexTableCheck().setNextFocusableComponent(complexBtnAdd);

		// 选择事件监听
		getComplexTableCheck().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		getComplexTableCheck().getSelectionModel().addListSelectionListener(
				this);
		getComplexTableCheck().getModel().addTableModelListener(this);
		// 列鼠标事件监听，处理列排序
		getComplexTableCheck().getTableHeader().addMouseListener(this);

		getSimpleTableCheck().getSelectionModel()
				.addListSelectionListener(this);
		getSimpleTableCheck().getModel().addTableModelListener(this);
		getSimpleTableCheck().getTableHeader().addMouseListener(this);
	}

	private FuncListInst getFuncList() {
		FormulaModel formulaModel = FormulaModel.getInstance(cellsModel);
		return formulaModel.getUfoFmlExecutor().getFuncListInst();
	}

	/**
	 * 获得所有函数的名称
	 * 
	 * @return
	 */
	public String[] getAllFuncName() {
		FuncListInst funcList = getFuncList();
		if (funcList == null)
			return null;
		UfoSimpleObject[] allModules = funcList.getCatList();
		Vector<String> funcNameVector = new Vector<String>();
		for (UfoSimpleObject module : allModules) {
			if (module == null)
				continue;
			UfoSimpleObject[] m_FuncNameList = funcList.getFuncList(module
					.getID());
			if (m_FuncNameList == null || m_FuncNameList.length == 0)
				continue;
			for (UfoSimpleObject simpleObj : m_FuncNameList) {
				if (simpleObj != null) {
					funcNameVector.addElement(simpleObj.getName());
				}
			}
		}
		return funcNameVector.toArray(new String[0]);
	}

	/**
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == complexTableCheck.getSelectionModel()) {
			m_nSelectRows = complexTableCheck.getSelectedRows();

			if (m_nSelectRows != null && m_nSelectRows.length > 0) {
				complexBtnDelete.setEnabled(true);
				complexBtnUpdate.setEnabled(true);
				complexBtnUpdate.requestFocus();
			} else {
				complexBtnDelete.setEnabled(false);
				complexBtnUpdate.setEnabled(false);
			}
		} else if (e.getSource() == getSimpleTableCheck().getSelectionModel()) {
			int iSelectRow = getSimpleTableCheck().getSelectedRow();
			if (iSelectRow == m_simplecheckModel.getActualRowCount()) {
				simpleBtnDelete.setEnabled(false);
				getJTxtName().setText("");
				getJTxtForm().setText("");
				getJTxtErr().setText("");
			} else if (iSelectRow >= 0) {
				simpleBtnDelete.setEnabled(true);
				SimpleCheckFmlVO simpleVO = m_simplecheckModel
						.getSimpleCheckVOByRow(iSelectRow);
				if (simpleVO != null) {
					getJTxtName().setText(simpleVO.getFmlName());
					getJTxtForm().setText(getCheckUserCond(simpleVO));
					getJTxtErr().setText(simpleVO.getErrMsg());

				}
			} else {
				simpleBtnDelete.setEnabled(false);
			}

		}
	}

	private String getCheckUserCond(SimpleCheckFmlVO repCheckVO) {
		if (repCheckVO == null)
			return null;
		String strFormula = null;
		if (repCheckVO.getParsedExpr() != null) {
			strFormula = m_fmlExecutor.getStrFmlByLet(repCheckVO
					.getParsedExpr(), true);
		} else {
			try {
				IParsed parsed = m_fmlExecutor.parseLogicExpr(repCheckVO
						.getCheckCond(), false);
				repCheckVO.setParsedExpr(parsed);
				strFormula = m_fmlExecutor.getStrFmlByLet(parsed, true);
			} catch (ParseException e) {
				AppDebug.debug(e);
				strFormula = repCheckVO.getCheckCond();
			}
		}
		return strFormula;
	}

	public RepCheckVO getSelectedVO() {
		if (m_nSelectRows != null)
			return (RepCheckVO) getComplexChecks().get(m_nSelectRows[0]);
		return null;
	}

	public int getSelectedIndex() {
		if (m_nSelectRows != null) {
			return m_nSelectRows[0];
		}
		return -1;
	}

	/**
	 * 得到JTable列模型
	 * 
	 * @return
	 */
	private DefaultTableColumnModel getComplexTblColModel() {
		int[] nWidths = {/* 80, */180, 400, 2 };
		DefaultTableColumnModel columnModel = new DefaultTableColumnModel();
		int nColumns = m_complexCheckModel.getColumnCount();
		for (int i = 0; i < nColumns; i++) {
			TableColumn tableColumn = new TableColumn(i);
			tableColumn.setHeaderValue(m_complexCheckModel.getColumnName(i));
			tableColumn.setMinWidth(nWidths[i]);

			if (i == 2)
				tableColumn.setMaxWidth(nWidths[i]);

			if (i == nColumns - 1) {
				tableColumn.setCellRenderer(new RepCheckFormulaRender());
			}
			columnModel.addColumn(tableColumn);
		}
		return columnModel;
	}

	/**
	 * @i18n uiufocheck00002=批量编辑
	 */
	private JCheckBox getComplexBatchChk() {
		if (complexBatchChk == null) {
			complexBatchChk = new UICheckBox(StringResource
					.getStringResource("uiufocheck00002"));// TODO 批量编辑
			complexBatchChk.setBounds(5, 290, 77, 27);
			complexBatchChk.setName("complexBatchChk");
			complexBatchChk.addActionListener(this);
		}
		return complexBatchChk;
	}

	private JTextArea getComplexBatchPane() {
		if (complexBatchEditPane == null) {
			complexBatchEditPane = new FormulaEditor(getAllFuncName());
			complexBatchEditPane.setText(getComplexCheckAsText());
			complexBatchEditPane.addKeyListener(this);
		}

		return complexBatchEditPane;
	}

	private String getComplexCheckAsText() {
		Vector vecCheck = m_complexCheckModel.getDatas();
		StringBuffer buffer = new StringBuffer();

		if (vecCheck != null && vecCheck.size() > 0) {
			RepCheckVO repCheckVO = null;
			for (int i = 0, size = vecCheck.size(); i < size; i++) {
				repCheckVO = (RepCheckVO) vecCheck.get(i);

				String strFormula = repCheckVO.getFormula();
				try {
					strFormula = m_fmlExecutor.parseRepCheckFormula(strFormula,
							false);

				} catch (Exception e) {
					AppDebug.debug(e);// @devTools
										// e.printStackTrace(System.out);
				}
				buffer.append(repCheckVO.getName());
				buffer.append("={\r\n");
				buffer.append(strFormula);
				buffer.append("}\r\n\r\n");

			}
		}
		return buffer.toString();
	}

	private DefaultTableColumnModel getSimpleTblColModel() {
		int[] nWidths = { 10, 10, 50 };
		DefaultTableColumnModel columnModel = new DefaultTableColumnModel();
		int nColumns = m_simplecheckModel.getColumnCount();
		for (int i = 0; i < nColumns; i++) {
			TableColumn tableColumn = new TableColumn(i);
			tableColumn.setHeaderValue(m_simplecheckModel.getColumnName(i));
			tableColumn.setMinWidth(nWidths[i]);
			// if(i ==1){
			// tableColumn.setCellRenderer(new RepCheckFormulaRender());
			// }
			columnModel.addColumn(tableColumn);
		}
		return columnModel;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == complexTableCheck.getTableHeader()) {
			TableColumnModel tcm = complexTableCheck.getColumnModel();
			int vc = tcm.getColumnIndexAtX(e.getX());
			if (vc == 0) {// 名称列
				m_complexCheckModel.sort();
			}
		} else if (e.getSource() == getSimpleTableCheck().getTableHeader()) {
			TableColumnModel tcm = getSimpleTableCheck().getColumnModel();
			int vc = tcm.getColumnIndexAtX(e.getX());
			if (vc == 0) {// 名称列
				m_simplecheckModel.sort();
			}
		}
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	/**
	 * 导出审核公式
	 * 
	 */
	private void doExport(boolean bSimple) {
		javax.swing.JFileChooser fc = new UIFileChooser();
		fc.setDialogTitle(StringResource.getStringResource("miufopublic157")); // "导出"
		fc.setApproveButtonToolTipText(StringResource
				.getStringResource("miufopublic157")); // "导出"
		TXTFileFilter xf = new TXTFileFilter();
		fc.setFileFilter(xf);
		fc.setMultiSelectionEnabled(false);
		int returnVal = fc.showSaveDialog(null);
		if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
			java.io.File file = fc.getSelectedFile();
			String pathname = file.getPath();
			if (pathname.indexOf(".txt") == -1
					&& pathname.indexOf(".TXT") == -1) {
				pathname += ".txt";
			}
			try {
				File newFile = new File(pathname);
				// 如果文件已经存在，提示用户是否覆盖
				if (newFile.exists()) {
					int nOption = JOptionPane.showConfirmDialog(this,
							StringResource.getStringResource("miufochk005"),
							StringResource.getStringResource("miufochk006"),
							JOptionPane.OK_CANCEL_OPTION);
					if (nOption == JOptionPane.CANCEL_OPTION) {
						return;
					}
				}
				BufferedWriter out = new BufferedWriter(new FileWriter(newFile));
				if (bSimple == false) {
					// 得到审核公式
					int nSize = m_complexCheckModel.getRowCount();
					for (int i = 0; i < nSize; i++) {
						out
								.write((String) m_complexCheckModel.getValueAt(
										i, 0));
						out.write("={\r\n");
						// 将公式转换为用户定义的形式
						String strFormula = (String) m_complexCheckModel
								.getValueAt(i, 1);
						out.write(strFormula);
						out.write("}\r\n\r\n");
					}
				} else {
					for (int i = 0, size = m_simplecheckModel
							.getActualRowCount(); i < size; i++) {
						// out.write(SIMPLE_EXORT_TAG_NAME);
						// out.write("=");
						out.write((String) m_simplecheckModel.getValueAt(i, 0));
						out.write(";");
						// out.write(SIMPLE_EXORT_TAG_FML);
						// out.write("=");
						out.write((String) m_simplecheckModel.getValueAt(i, 1));
						out.write(";");
						// out.write(SIMPLE_EXORT_TAG_ERR);
						// out.write("=");
						out.write((String) m_simplecheckModel.getValueAt(i, 2));
						out.write("\r\n");
					}
				}
				out.close();
			} catch (java.io.IOException ex) {
				AppDebug.debug(ex);// @devTools AppDebug.debug(ex);
				JOptionPane.showMessageDialog(this, StringResource
						.getStringResource("miufochk007"));
			}
		}
	}

	/**
	 * JTable模型改变
	 */
	public void tableChanged(TableModelEvent e) {
		if (e.getSource() == complexTableCheck.getModel()) {
			setTableRowHeight(false);
			getComplexBtnExport().setEnabled(
					getComplexCheckNum() > 0 ? true : false);
		} else if (e.getSource() == simpleJTableCheck.getModel()) {
			setTableRowHeight(true);
			setSimpleBtnExportEnable();
		}
	}

	/**
	 * 设置JTable行高
	 * 
	 */
	private void setTableRowHeight(boolean bSimple) {
		Vector vecChecks = bSimple == true ? getSimpleChecks()
				: getComplexChecks();
		String strFormula = null;
		for (int i = 0; i < vecChecks.size(); i++) {
			if (bSimple == true)
				strFormula = ((SimpleCheckFmlVO) vecChecks.get(i))
						.getCheckCond();
			else
				strFormula = ((RepCheckVO) vecChecks.get(i)).getFormula();
			StringTokenizer tokener = new StringTokenizer(strFormula, "\r\n");
			int nRows = tokener.countTokens();
			if (bSimple)
				simpleJTableCheck.setRowHeight(i, nRows * 20);
			else
				complexTableCheck.setRowHeight(i, nRows * 20);
		}
	}

	class RepCheckFormulaRender extends JTextArea implements TableCellRenderer {
		public RepCheckFormulaRender() {
			super();
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean arg2, boolean arg3, int arg4, int arg5) {
			if (value instanceof String) {
				this.setText((String) value);
				if (arg2) {
					this.setBackground(table.getSelectionBackground());
				} else {
					this.setBackground(table.getBackground());
				}
				return this;
			}
			return null;
		}

	}
}
