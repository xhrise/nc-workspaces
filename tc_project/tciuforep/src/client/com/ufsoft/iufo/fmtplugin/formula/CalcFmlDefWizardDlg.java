package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import nc.pub.iufo.accperiod.AccPeriodSchemeUtil;
import nc.pub.iufo.cache.RepFormatModelCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.base.ICacheObject;
import nc.ui.iufo.query.measurequery.MeasureQueryFuncDriver;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.vo.bd.period.AccperiodschemeVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iuforeport.rep.ReportVO;
import nc.vo.uapbd.exratescheme.ExrateSchemeVO;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.key.DynKeywordRefDlg;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.fmtplugin.key.KeywordRefDlg;
import com.ufsoft.iufo.fmtplugin.key.NCAccSchemeRefDlg;
import com.ufsoft.iufo.fmtplugin.key.NCERateSchemeRefDlg;
import com.ufsoft.iufo.fmtplugin.measure.MeasureRefDlg;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.ref.ComRefFactory;
import com.ufsoft.iufo.inputplugin.ref.IComRefDlg;
import com.ufsoft.iufo.inputplugin.ref.RefParam;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.parser.IFuncDriver;
import com.ufsoft.iufo.util.parser.IFuncDriver2;
import com.ufsoft.iufo.util.parser.IFuncEditTypeSpecial;
import com.ufsoft.iufo.util.parser.IFuncEditTypeSpecial2;
import com.ufsoft.iufo.util.parser.UfoParseException;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.dialog.AreaSelectDlg;
import com.ufsoft.report.dialog.IFmlDefWizard;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.exception.MessageException;
import com.ufsoft.report.fmtplugin.formula.ui.FormulaEditor;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.script.UfoFormulaProxy;
import com.ufsoft.script.extfunc.LoginInfoFuncDriver;
import com.ufsoft.script.function.ExtFunc;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.script.function.UfoFuncInfo;
import com.ufsoft.script.function.UfoFuncList;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.re.timeref.CalendarDialog;

/**
 * 单元公式函数向导对话框。 原类名：CellFuncWizardDlg 创建日期：(2001-1-10 9:03:54)
 * 
 * @author：岳益民
 */
public class CalcFmlDefWizardDlg extends AreaSelectDlg implements
		IFmlDefWizard, ActionListener, FocusListener, ListSelectionListener,
		IUfoContextKey {

	private static final long serialVersionUID = 8723441785023264199L;

	private JLabel ivjJLabel1 = null;

	private JLabel ivjJLabel2 = null;

	private JLabel ivjJLabel3 = null;

	private JLabel ivjJLabelFuncName = null;

	private JList ivjJListFuncName = null;

	private JList ivjJListFuncType = null;

	private JPanel ivjJPanel1 = null;

	private JScrollPane ivjJScrollPane1 = null;

	private JScrollPane ivjJScrollPane2 = null;

	private JScrollPane ivjJScrollPaneFunFormat = null;

	private JScrollPane ivjJScrollPaneFuncDes = null;

	private JTextField ivjJTextField1 = null;

	private JPanel ivjUfoDialogContentPane = null;

	private JButton ivjJBCancel = null;

	private JButton ivjJBNext = null;

	private JButton ivjJBPre = null;

	private UfoSimpleObject m_FuncCategory = null;

	private UfoSimpleObject m_FuncName = null;

	private JLabel ivjJLabel11 = null;

	private DefaultListModel model = new DefaultListModel();

	private String m_retFunc = null;

	private JLabel ivjJLabel4 = null;

	private JLabel ivjJLabel5 = null;

	private JTextArea ivjJTAFuncFormat = null;

	private JPanel ivjJPanel2 = null;

	private JPanel ivjJPanelParam = null;

	private JScrollPane ivjJScrollPanelParam = null;

	private JTextField ivjJTextField2 = null;

	private JTextArea ivjJTAFuncDes = null;

	private JTextArea ivjJTAFuncInfo = null;

	private JButton ivjJBFold = null;

	private JTextField ivjJTFSelArea = null;

	private static int ParamMaxNum = 7;

	private JButton[] ivjJBFolds = new nc.ui.pub.beans.UIButton[ParamMaxNum];

	private JLabel[] ivjJLabelParams = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel[ParamMaxNum];

	private JTextField[] ivjJTFParams = new JTextField[ParamMaxNum];

	private String[] funcNameNoKAry = { "K", "ZKEY" };

	private JTextArea ivjJTAErrMsg = null;

	private JPanel ivjJPanel3 = null;

	private JTextArea ivjJTAFormulaErr = null;

	private JTextField ivjJTFFormula = null;

	private JLabel ivjJLabel51 = null;

	private boolean m_isEditClear = true;

	// liuyy+, 更改选择区域连动方式
	private SelectListener m_selectListener = null;

	public class EditDocument extends javax.swing.text.PlainDocument {
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			if (m_isEditClear)
				return;
			else
				super.insertString(offs, str, a);
		}

		public void remove(int offs, int len) throws BadLocationException {
			if (m_isEditClear)
				return;
			else
				super.remove(offs, len);
		}

	}

	private JButton ivjJBRefer = null;

	private JLabel ivjJLabel6 = null;

	private JLabel ivjJLabel61 = null;

	private JPanel ivjJPanelU8 = null;

	private JScrollPane ivjJScrollPane3 = null;

	private JTextArea ivjJTAU8Info = null;

	private JTextField ivjjTFU8Input = null;

	private int m_FocusPlace = -1;// 1 焦点在TF1 2 焦点在TF2 3 焦点在TF3

	// private UfoReport m_frame;

	private String m_SelFuncName = null;// 记录选中的函数名称

	private int m_ValNum = ParamMaxNum;// 记录参数个数

	private String unitId;// 私有报表标记

	// wss add 2002-10-24
	private FuncListInst m_ufoFuncList = null;

	/** 记录执行代理。通常从UfoTable中获得，但是为预算添加的构建器不许UfoTable，所以单独记录 */
	private UfoFormulaProxy m_formProxy = null;

	private CellsModel cellsModel;
	
	private IContext context;

	public class ValueChangeDocument extends javax.swing.text.PlainDocument {
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			super.insertString(offs, str, a);
			createFunc();// 生成函数的字符串形式
			m_isEditClear = false;
			ivjJTFFormula.setText(m_retFunc);
			m_isEditClear = true;
		}

		public void remove(int offs, int len) throws BadLocationException {
			super.remove(offs, len);
			createFunc();// 生成函数的字符串形式
			m_isEditClear = false;
			ivjJTFFormula.setText(m_retFunc);
			m_isEditClear = true;
		}
	}

	/**
	 * Hr公式编辑区域。 Hr公式返回字符信息是二进制流的形式，所以希望对于HR的函数只是在显示的时候做一个修改，
	 * 显示统一的内容。如此，避免多语言问题，并且显示更加友好。 wupeng2005-4-11修改，原因是HR需求。
	 * 
	 * @author wupeng
	 * @version 3.1
	 */
	public class HrFmlField extends JTextField {
		/**
		 * 是否Hr函数。
		 */
		private boolean b_HrFml = false;

		private String str_HRFml = null;

		public void setText(String text) {
			if (text != null) {
				b_HrFml = text.startsWith(FormulaEditor.HRFORMULA);
			} else {
				b_HrFml = false;
			}
			setEditable(!b_HrFml);// HR函数不允许编辑。
			if (b_HrFml) {
				str_HRFml = text;
				text = FormulaEditor.HRFORMULASHOW;
			}
			super.setText(text);
		}

		public String getText() {
			if (b_HrFml) {
				return str_HRFml;
			}
			return super.getText();
		}
	}
	

	/**
	 * 此处插入方法描述。 创建日期：(2002-4-22 10:38:31)
	 * 
	 * @param owner
	 *            java.awt.Container
	 */
	public CalcFmlDefWizardDlg(Container owner, CellsModel cellsModel,IContext context) {
		super(owner, cellsModel);
		this.cellsModel = cellsModel;
		this.context = context;
		// m_frame = owner;
		if (cellsModel != null){
			m_ufoFuncList = getUfoExecutor().getFuncListInst();
		}
		if (m_ufoFuncList == null)
			return;
		initialize();

	}

	public CalcFmlDefWizardDlg(UfoFormulaProxy executor) {
		super();
		if (executor == null)
			throw new IllegalArgumentException();
		m_formProxy = executor;
		m_ufoFuncList = m_formProxy.getEnv().loadFuncListInst();
		if (m_ufoFuncList == null)
			return;
		initialize();
	}

	private void referAction() {
		String strDriverName = m_ufoFuncList.getExtFuncDriver(m_FuncName
				.getName());
		String u8Input = null;

		try {
			IFuncDriver driver = null;
			driver = (IFuncDriver) m_ufoFuncList.getExtDriver(strDriverName);
			if (driver != null
					&& driver instanceof IFuncEditTypeSpecial
					&& ((IFuncEditTypeSpecial) driver)
							.getFuncEditType(m_SelFuncName) == IFuncEditTypeSpecial.CANNTEDITMANUAL) {
				if (getjTFU8Input().getText() != null
						&& getjTFU8Input().getText().length() > 0) {
					if (driver instanceof IFuncEditTypeSpecial2)
						u8Input = ((IFuncEditTypeSpecial2) driver).dealFunc(
								this, m_SelFuncName, getjTFU8Input().getText(),
								true);
					else
						u8Input = ((IFuncEditTypeSpecial) driver).dealFunc(
								m_SelFuncName, getjTFU8Input().getText(), true);
					if (u8Input == null || u8Input.trim().equals("")) { // 取消被按下
					} else { // 确定被按下
						ivjjTFU8Input.setText(u8Input);

					}
					return;
				} else {
					if (driver instanceof IFuncDriver2)
						u8Input = ((IFuncDriver2) driver).doFuncRefer(this,
								m_SelFuncName);
					else
						u8Input = driver.doFuncRefer(m_SelFuncName);
				}
			} else {
				if (driver instanceof MeasureQueryFuncDriver) {
					FunctionReferDlg.getMQFuncParam(getParent(), m_SelFuncName,context);
				} else {
					if (driver instanceof IFuncDriver2)
						u8Input = ((IFuncDriver2) driver).doFuncRefer(this,
								m_SelFuncName);
					else
						u8Input = driver.doFuncRefer(m_SelFuncName);
				}

			}
		} catch (Exception e1) {
			e1.printStackTrace(System.out);
			IFuncDriver newDriver = (IFuncDriver) m_ufoFuncList
					.getExtDriver(strDriverName);
			if (newDriver instanceof IFuncDriver2)
				u8Input = ((IFuncDriver2) newDriver).doFuncRefer(this,
						m_SelFuncName);
			else
				u8Input = newDriver.doFuncRefer(m_SelFuncName);
		}

		if (u8Input == null || u8Input.trim().equals("")) { // 取消被按下
		} else { // 确定被按下
			String str = ivjjTFU8Input.getText();
			if (str == null || str.equals(""))
				ivjjTFU8Input.setText(u8Input);
			else
				ivjjTFU8Input.setText(str + u8Input);

		}
	}

	private void nextAction() {
		if (ivjJBNext.getText().equals(
				StringResource.getStringResource("miufopublic261"))) { // "下一步"
			if (ivjJListFuncName.isSelectionEmpty()) {
				showmessage(StringResource.getStringResource("miufo1000812")); // "请选择一个函数！"
				return;
			}
			showmessage("");
			m_SelFuncName = m_FuncName.getName();

			if (isRefFunc(m_SelFuncName)) {

				ivjJBRefer.setVisible(false);
				ivjjTFU8Input.requestFocus();

				setTitle(StringResource.getStringResource("miufo1000813")); // "函数向导（参数表）"
				ivjJBNext.setText(StringResource
						.getStringResource("miufo1000814")); // "完 成"
				ivjJBPre.setEnabled(true);
				ivjJPanel1.setVisible(false);
				ivjJPanel2.setVisible(false);
				ivjJPanelU8.setVisible(true);
				ivjJPanel3.setVisible(true);
				createFunc();
				m_isEditClear = false;
				ivjJTFFormula.setText(m_retFunc);
				m_isEditClear = true;

				String strDriverName = m_ufoFuncList
						.getExtFuncDriver(m_FuncName.getName());
				IFuncDriver driver = null;
				IFuncDriver newDriver = null;
				try {
					driver = (IFuncDriver) m_ufoFuncList
							.getExtDriver(strDriverName);
				} catch (Exception e2) {
					newDriver = (IFuncDriver) m_ufoFuncList
							.getExtDriver(strDriverName);
				}
				// driver和newDriver不会出现同时为空的情况
				if ((driver != null && driver.hasReferDlg(m_SelFuncName))
						|| (newDriver != null && newDriver
								.hasReferDlg(m_SelFuncName))) {
					// 有参照对话框
					if (driver != null
							&& driver instanceof IFuncEditTypeSpecial
							&& ((IFuncEditTypeSpecial) driver)
									.getFuncEditType(m_SelFuncName) == IFuncEditTypeSpecial.CANNTEDITMANUAL) {
						ivjjTFU8Input.setEditable(false);

					} else {
						ivjjTFU8Input.setEditable(true);
					}
					ivjJBRefer.setVisible(true);
					ivjJBRefer.setNextFocusableComponent(ivjJTFFormula);
					ivjJTFFormula.setNextFocusableComponent(ivjJBPre);
					ivjJBPre.setNextFocusableComponent(ivjJBNext);
					ivjJBNext.setNextFocusableComponent(ivjJBCancel);
					ivjJBCancel.setNextFocusableComponent(ivjjTFU8Input);
					ivjjTFU8Input.setNextFocusableComponent(ivjJBRefer);
				} else {
					ivjJTFFormula.setNextFocusableComponent(ivjJBPre);
					ivjJBPre.setNextFocusableComponent(ivjJBNext);
					ivjJBNext.setNextFocusableComponent(ivjJBCancel);
					ivjJBCancel.setNextFocusableComponent(ivjjTFU8Input);
					ivjjTFU8Input.setNextFocusableComponent(ivjJTFFormula);
				}
			} else {
				ivjJBRefer.setVisible(false);
				setTitle(StringResource.getStringResource("miufo1000813")); // "函数向导（参数表）"
				ivjJBNext.setText(StringResource
						.getStringResource("miufo1000814")); // "完 成"
				setFuncVal();
				ivjJBPre.setEnabled(true);
				ivjJPanel1.setVisible(false);
				ivjJPanel2.setVisible(true);
				ivjJPanel3.setVisible(true);
				ivjJPanelU8.setVisible(false);
				createFunc();
				m_isEditClear = false;
				ivjJTFFormula.setText(m_retFunc);
				m_isEditClear = true;
			}
		}
	}

	private void finishAction() {
		createFunc(); // 生成函数的字符串形式
		m_isEditClear = false;
		ivjJTFFormula.setText(m_retFunc);
		m_isEditClear = true;
		// 判断是否正确
		try {
			if (cellsModel != null) {
				AreaPosition area = AreaPosition.getInstance("A1");
				getUfoExecutor().checkUserDefFormula(area, m_retFunc);
			} else if (m_formProxy != null) {

				m_formProxy.parseUserDefExpr(m_retFunc);
			}
			showmessage("");
			this.setResult(this.ID_OK);
			this.close();

		} catch (UfoParseException err) {
			ivjJTAFormulaErr.setText(err.getMessage());
			ivjJTFFormula.requestFocus();
			if (ivjJTFFormula.getText() != null
					&& ivjJTFFormula.getText().length() >= (err.getErrPos() - 1))
				ivjJTFFormula.setCaretPosition(err.getErrPos() - 1);
			return;
		} catch (Exception ex) {
			AppDebug.debug(ex);
			return;
		}
	}

	private void previousAction() {
		showmessage("");
		ivjJBPre.setEnabled(false);
		ivjJBNext.setText(StringResource.getStringResource("miufopublic261")); // "下一步"
		setTitle(StringResource.getStringResource("miufo1000815")); // "函数向导（函数名）"
		ivjJPanel1.setVisible(true);
		ivjJPanel2.setVisible(false);
		ivjJPanelU8.setVisible(false);
		ivjJPanel3.setVisible(false);
		ivjJBNext.requestFocus();
		ivjJBCancel.setNextFocusableComponent(ivjJListFuncName);
		ivjJListFuncName.setNextFocusableComponent(ivjJListFuncType);
		ivjJListFuncType.setNextFocusableComponent(ivjJBNext);
		ivjJBNext.setNextFocusableComponent(ivjJBCancel);
	}

	/**
	 * Invoked when an action occurs.
	 * @i18n miufohbbb00090=请先设置主表关键字
	 * @i18n miufohbbb00091=请先选择会计期间方案
	 * @i18n miufohbbb00092=主表关键字不是会计类的
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ivjJBCancel) {
			this.setResult(ID_CANCEL);

			removeSelectedModelListener();

			return;
		} else if (e.getSource() == ivjJBRefer) {
			referAction();
		} else if (e.getSource() == ivjJBNext) {
			if (ivjJBNext.getText().equals(
					StringResource.getStringResource("miufopublic261"))) { // "下一步")
				nextAction();
				return;
			} else if (ivjJBNext.getText().equals(
					StringResource.getStringResource("miufo1000814"))) { // "完
				// 成"
				finishAction();

				removeSelectedModelListener();
				return;
			}
		}

		else if (e.getSource() == ivjJBPre) {
			previousAction();
			return;
		}

		if (e.getSource() == ivjJBFolds[0]) {
			// 处理curdate参数参照
			if (LoginInfoFuncDriver.GETLOGINDATE.equalsIgnoreCase(m_FuncName
					.getName())) {
				CurDateParamRefDlg dlg = new CurDateParamRefDlg(this,
						ivjJTFParams[0].getText());
				dlg.setModal(true);
				dlg.setLocationRelativeTo(this);
				dlg.show();
				if (dlg.getResult() == UfoDialog.ID_OK) {
					ivjJTFParams[0].setText(dlg.getSelectedTime());
				}
				return;
			}
		}
		Object keyGroupPkObj = getContextVO().getAttribute(KEYGROUP_PK);
		// String strKeyGroupPk = keyGroupPkObj != null
		// && (keyGroupPkObj instanceof String) ? keyGroupPkObj.toString()
		// : null;

		Object curUserIdObj = getContextVO().getAttribute(CUR_USER_ID);
		String strCurUserId = curUserIdObj != null
				&& (curUserIdObj instanceof String) ? curUserIdObj.toString()
				: null;

		Object repIdObj = getContextVO().getAttribute(REPORT_PK);
		String strReportPK = repIdObj != null && (repIdObj instanceof String) ? repIdObj
				.toString()
				: null;
		// CellsModel cellsModel = cellsModel;

		for (int i = 0; i < ParamMaxNum; i++) {
			if (e.getSource() == ivjJBFolds[i]
					|| e.getSource() == ivjJTFParams[i]) {
				String paramName = ivjJLabelParams[i].getText();
				// 处理指标参照
				if (paramName != null
						&& paramName.indexOf(StringResource
								.getStringResource("miufo1000172")) != -1) { // "指标"
					ReportVO[] repVOs = CacheProxy.getSingleton()
							.getReportCache().getByPks(
									new String[] { strReportPK });
					// KeyGroupVO keyGVO = CacheProxy.getSingleton()
					// .getKeyGroupCache().getByPK(strKeyGroupPk);
					String managerObj = (String)getContextVO()
							.getAttribute(REP_MANAGER);
					
					boolean isManager = managerObj == null ? false
							: Boolean.valueOf(managerObj);
					MeasureRefDlg measureRef = new MeasureRefDlg(this,
							repVOs[0], null, strCurUserId, true, isManager,
							true);
					measureRef.setLocationRelativeTo(null);
					measureRef.setModal(true);
					measureRef.show();

					if (measureRef.getResult() == measureRef.ID_OK) {
						ivjJTFParams[i].setText(measureRef.getStrRefMeasure());
					}
					return;
				}
				// 处理关键字参照
				else if (paramName != null
						&& paramName.indexOf(StringResource
								.getStringResource("miufopublic335")) != -1) { // "关键字"
					// 得到可以参照的关键字
					KeyVO[] keyvos = {};
					KeywordRefDlg refDialog = new KeywordRefDlg(this, keyvos);
					refDialog.setLocationRelativeTo(this);
					refDialog.setModal(true);
					refDialog.show();
					KeyVO refvo = refDialog.getRefVO();
					if (refvo != null) {
						if (isAddKFunc())
							ivjJTFParams[i].setText("K(" + "\'"
									+ refvo.getName() + "\'" + ")=");
						else
							ivjJTFParams[i].setText("\'" + refvo.getName()
									+ "\'");
					}
					return;
				} else if (paramName.indexOf(StringResource
						.getStringResource("uiufofunc024")) >= 0) { // 时间属性 //
					// @edit by
					// wangyga
					// at
					// 2009-2-18,下午03:46:17
					int iMeasureIndex = getMeasureInfoIndex();
					String strMeasureName = null;
					if (iMeasureIndex != -1)
						strMeasureName = ivjJTFParams[iMeasureIndex].getText();
					DynKeywordRefDlg keyDlg = getTimeKeyRefDlg(strMeasureName);
					if (keyDlg == null)
						return;
					keyDlg.setTitle(StringResource
							.getStringResource("miufo01187"));
					if (keyDlg.isInitOver) {
						keyDlg.setLocationRelativeTo(this);
						keyDlg.setModal(true);
						keyDlg.show();
						KeyVO refvo = keyDlg.getRefVO();
						if (refvo != null) {
							ivjJTFParams[i].setText("\'" + refvo.getName()
									+ "\'");
						}
					}
					return;
				} else if (paramName.indexOf(StringResource
						.getStringResource("uiufodrv000013")) >= 0) {
					CalendarDialog dlg=new CalendarDialog(ivjJTFParams[0], null);
					dlg.setVisible(true);
					String strTime = ivjJTFParams[0].getText();
					if (strTime != null && strTime.trim().length() > 0) {
						ivjJTFParams[i].setText("\'" + strTime + "\'");
					}
					return;
				} else if (paramName.indexOf(StringResource
						.getStringResource("uiufodrv000011")) >= 0
						|| paramName.indexOf(StringResource
								.getStringResource("uiufodrv000012")) >= 0) {
					IComRefDlg comRef = ComRefFactory.getRefInstance(
							new RefParam(RefParam.COIN_REF_CODE), this);
					comRef.showModel();
					Object selectValueObj = comRef.getSelectedValue();
					if (selectValueObj != null) {
						ivjJTFParams[i].setText("\'"
								+ selectValueObj.toString() + "\'");
					}
					return;
				} else if (paramName.indexOf(StringResource
						.getStringResource("uiufodrv000017")) >= 0
						&& i == 0) {
					String strKeyGroupPk = KeywordModel.getInstance(cellsModel)
							.getMainKeyCombPK();
					KeyGroupVO keyGVO = CacheProxy.getSingleton()
							.getKeyGroupCache().getByPK(strKeyGroupPk);
					if (keyGVO == null)
						throw new MessageException(
								MessageException.TYPE_WARNING, StringResource
										.getStringResource("miufohbbb00090"));
					String strAccCode = ivjJTFParams[m_ValNum - 1].getText();
					if (strAccCode == null || strAccCode.trim().length() == 0) {
						throw new MessageException(
								MessageException.TYPE_WARNING, StringResource
										.getStringResource("miufohbbb00091"));
					}
					if (!keyGVO.isTTimeTypeAcc()) {
						throw new MessageException(
								MessageException.TYPE_WARNING, StringResource
										.getStringResource("miufohbbb00092"));
					}

					String strAccPk = AccPeriodSchemeUtil.getInstance()
							.getPeriodSchemePKByCode(
									toDelQuotes(strAccCode),
									AccPeriodSchemeUtil.getInstance()
											.getAccountID());
					RefParam refParam = new RefParam(RefParam.ACC_REF_CODE);
					refParam.setAttribute(RefParam.ACC_PREIOD_PK, strAccPk);
					refParam.setAttribute(RefParam.ACC_PERIOD_TYPE, keyGVO
							.getAccKey().getKeywordPK());
					IComRefDlg comRef = ComRefFactory.getRefInstance(refParam,
							this);
					comRef.showModel();
					Object selectValueObj = comRef.getSelectedValue();
					if (selectValueObj != null) {
						ivjJTFParams[i].setText("\'"
								+ selectValueObj.toString() + "\'");
					}
					return;
				} else if (paramName.indexOf(StringResource
						.getStringResource("miufo00129")) >= 0) { // 会计期间方案
					NCAccSchemeRefDlg refDialog = new NCAccSchemeRefDlg();
					refDialog.setLocationRelativeTo(this);
					refDialog.setModal(true);
					refDialog.show();
					AccperiodschemeVO refvo = refDialog.getRefVO();
					if (refvo != null) {
						ivjJTFParams[i].setText("\'"
								+ refvo.getAccperiodschemecode() + "\'");
					}
					return;
				} else if (paramName.indexOf(StringResource
						.getStringResource("miufo00128")) >= 0) { // NC汇率方案
					NCERateSchemeRefDlg refDialog = new NCERateSchemeRefDlg();
					refDialog.setLocationRelativeTo(this);
					refDialog.setModal(true);
					refDialog.show();
					ExrateSchemeVO refvo = refDialog.getRefVO();
					if (refvo != null) {
						ivjJTFParams[i].setText("\'"
								+ refvo.getExrateschemecode() + "\'");
					}
					return;
				} else {
					m_FocusPlace = i;
					fold(true);
					return;
				}
			}
		}
		if (e.getSource() == ivjJBFold || e.getSource() == ivjJTFSelArea) {
			fold(false);
			return;
		}
	}

	/**
	 * “ESC”键按下之后，退出对话框所作处理。 创建日期：(2000-11-30 15:51:01)
	 * 
	 * @return int
	 */
	public void closeDiolog() {
		removeSelectedModelListener();
		setResult(ID_CANCEL);
	}

	/**
	 * 根据输入生成函数字符串。 创建日期：(00-11-28 14:58:24)
	 */
	private void createFunc() {
		m_retFunc = m_SelFuncName;
		if (isRefFunc(m_FuncName.getName())) {
			m_retFunc += "(" + ivjjTFU8Input.getText() + ")";
		} else {
			String[] parmValues = new String[ParamMaxNum];
			for (int i = 0; i < ParamMaxNum; i++) {
				parmValues[i] = ivjJTFParams[i].getText();
			}
			m_retFunc += "(";
			if (m_ValNum > ParamMaxNum)
				m_ValNum = ParamMaxNum;
			for (int i = 0; i < m_ValNum; i++) {
				if (parmValues[i] != null)
					m_retFunc += parmValues[i] + ",";
				else
					m_retFunc += ",";
			}
			if (m_ValNum > 0)
				m_retFunc = m_retFunc.substring(0, m_retFunc.length() - 1);
			while (m_retFunc.indexOf(',', m_retFunc.length() - 1) != -1) {
				m_retFunc = m_retFunc.substring(0, m_retFunc.length() - 1);
			}
			m_retFunc += ")";
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2000-12-27 12:48:01)
	 * 
	 * @param evt
	 *            java.awt.event.FocusEvent
	 */
	public void focusGained(FocusEvent evt) {
		if (ivjJBFold.isVisible())
			return;
		for (int i = 0; i < ParamMaxNum; i++) {
			if (evt.getSource().equals(ivjJTFParams[i])
					|| evt.getSource().equals(ivjJBFolds[i])) {
				m_FocusPlace = i;
				return;
			}
		}
		m_FocusPlace = -1;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2000-12-27 12:47:25)
	 * 
	 * @param evt
	 *            java.awt.event.FocusEvent
	 */
	public void focusLost(FocusEvent evt) {
	}

	/**
	 * 区域参照时，对窗口折叠或恢复。 创建日期：(2001-1-15 9:29:52)
	 * 
	 * @param FoldType
	 *            boolean
	 */
	private void fold(boolean FoldType) {
		ivjJTAErrMsg.setText("");// empty error message
		setFold(FoldType);
		if (FoldType) {
			if (isRefFunc(m_FuncName.getName())) {
				ivjJPanelU8.setVisible(false);
			} else {
				ivjJPanel2.setVisible(false);
			}
			ivjJTFSelArea.setVisible(true);
			ivjJBFold.setVisible(true);
			Rectangle r = getBounds();
			r.height = 70;
			setBounds(r);
			if (m_FocusPlace >= 0 && m_FocusPlace < ParamMaxNum) {
				ivjJTFSelArea.setText(ivjJTFParams[m_FocusPlace].getText());
			}
		} else {
			if (isRefFunc(m_FuncName.getName())) {
				ivjJPanelU8.setVisible(true);
			} else {
				ivjJPanel2.setVisible(true);
			}
			ivjJTFSelArea.setVisible(false);
			ivjJBFold.setVisible(false);
			Rectangle r = getBounds();
			r.height = 468;
			setBounds(r);
			if (m_FocusPlace >= 0 && m_FocusPlace < ParamMaxNum) {
				ivjJTFParams[m_FocusPlace].setText(ivjJTFSelArea.getText());
				ivjJTFParams[m_FocusPlace].requestFocus();
			}
			createFunc();
			m_isEditClear = false;
			ivjJTFFormula.setText(m_retFunc);
			m_isEditClear = true;
			ivjJTAFormulaErr.setText("");
		}
	}

	/**
	 * 返回输入的函数字符串。 创建日期：(2001-1-10 13:51:29)
	 * 
	 * @return java.lang.String
	 */
	public String getCellFunc() {
		return m_retFunc;
	}

	/**
	 * 此处插入方法描述。 创建日期：(2003-9-19 10:47:03)
	 * 
	 * @return com.ufsoft.iuforeport.reporttool.temp.ContextVO
	 */
	public IContext getContextVO() {
		return context;
	}

	/**
	 * 返回 JBCancel 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJBCancel() {
		if (ivjJBCancel == null) {
			try {
				ivjJBCancel = new nc.ui.pub.beans.UIButton();
				ivjJBCancel.setName("JBCancel");
				ivjJBCancel.setText(StringResource
						.getStringResource("miufo1000274")); // "取 消"
				ivjJBCancel.setBounds(336, 396, 75, 22);
				ivjJBCancel.addFocusListener(this);
				ivjJBCancel.addActionListener(this);
				ivjJBCancel.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJBCancel;
	}

	/**
	 * 返回 JBFold 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJBFold() {
		if (ivjJBFold == null) {
			try {
				ivjJBFold = new nc.ui.pub.beans.UIButton();
				ivjJBFold.setName("JBFold");
				ivjJBFold.setText("");
				ivjJBFold.setBounds(394, 2, 18, 17);
				ivjJBFold.setVisible(false);
				ivjJBFold.addFocusListener(this);
				ivjJBFold.addActionListener(this);
				ivjJBFold.registerKeyboardAction(this, KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);

				ivjJBFold.setIcon(ResConst.getImageIcon("reportcore/down.gif")); // UfoPublic.getImageIcon("down.gif"));

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJBFold;
	}

	/**
	 * 返回 JBConFold 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJBFold(int i) {
		if (ivjJBFolds[i] == null) {
			try {
				ivjJBFolds[i] = new nc.ui.pub.beans.UIButton();
				ivjJBFolds[i].setName("JBFolds" + Integer.toString(i));
				ivjJBFolds[i].setText("");
				ivjJBFolds[i].setVisible(false);
				ivjJBFolds[i].addFocusListener(this);
				ivjJBFolds[i].addActionListener(this);
				ivjJTFParams[i].setPreferredSize(new Dimension(180, 21));
				ivjJBFolds[i].setIcon(ResConst
						.getImageIcon("reportcore/up.gif"));// UfoPublic.getImageIcon("up.gif"));
				ivjJBFolds[i].registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJBFolds[i];
	}

	/**
	 * 返回 JBNext 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJBNext() {
		if (ivjJBNext == null) {
			try {
				ivjJBNext = new nc.ui.pub.beans.UIButton();
				ivjJBNext.setName("JBNext");
				ivjJBNext.setText(StringResource
						.getStringResource("miufopublic261")); // "下一步"
				ivjJBNext.setBounds(245, 396, 75, 22);
				ivjJBNext.addFocusListener(this);
				ivjJBNext.addActionListener(this);
				ivjJBNext.registerKeyboardAction(this, KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJBNext;
	}

	/**
	 * 返回 JBPre 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJBPre() {
		if (ivjJBPre == null) {
			try {
				ivjJBPre = new nc.ui.pub.beans.UIButton();
				ivjJBPre.setName("JBPre");
				ivjJBPre.setText(StringResource
						.getStringResource("miufopublic260")); // "上一步"
				ivjJBPre.setBounds(140, 396, 75, 22);
				addToolTipAuto(ivjJBPre);
				ivjJBPre.setEnabled(false);
				ivjJBPre.addFocusListener(this);
				ivjJBPre.addActionListener(this);
				ivjJBPre.registerKeyboardAction(this, KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJBPre;
	}

	/**
	 * 返回 JBRefer 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJBRefer() {
		if (ivjJBRefer == null) {
			try {
				ivjJBRefer = new nc.ui.pub.beans.UIButton();
				ivjJBRefer.setName("JBRefer");
				ivjJBRefer.setText(StringResource
						.getStringResource("miufopublic283"));
				ivjJBRefer.setBounds(325, 230, 75, 22);
				ivjJBRefer.setVisible(false);
				ivjJBRefer.addFocusListener(this);
				ivjJBRefer.addActionListener(this);
				ivjJBRefer.registerKeyboardAction(this, KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJBRefer;
	}

	/**
	 * 返回 JLabel1 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLabel1() {
		if (ivjJLabel1 == null) {
			try {
				ivjJLabel1 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLabel1.setName("JLabel1");
				ivjJLabel1.setText(StringResource
						.getStringResource("miufo1000816")); // "选中函数："
				ivjJLabel1.setBounds(15, 8, 100, 16);
				ivjJLabel1.setForeground(java.awt.Color.black);
				ivjJLabel1.setRequestFocusEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel1;
	}

	/**
	 * 返回 JLabel11 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLabel11() {
		if (ivjJLabel11 == null) {
			try {
				ivjJLabel11 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLabel11.setName("JLabel11");
				ivjJLabel11.setText(StringResource
						.getStringResource("miufo1000817")); // "函数功能："
				ivjJLabel11.setBounds(15, 32, 100, 16);
				ivjJLabel11.setForeground(java.awt.Color.black);
				ivjJLabel11.setRequestFocusEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel11;
	}

	/**
	 * 返回 JLabel2 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLabel2() {
		if (ivjJLabel2 == null) {
			try {
				ivjJLabel2 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLabel2.setName("JLabel2");
				ivjJLabel2.setText(StringResource
						.getStringResource("miufo1000818")); // "函数类别："
				ivjJLabel2.setBounds(15, 96, 100, 16);
				ivjJLabel2.setForeground(java.awt.Color.black);
				ivjJLabel2.setRequestFocusEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel2;
	}

	/**
	 * 返回 JLabel3 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLabel3() {
		if (ivjJLabel3 == null) {
			try {
				ivjJLabel3 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLabel3.setName("JLabel3");
				ivjJLabel3.setText(StringResource
						.getStringResource("miufo1000819")); // "函数名称："
				ivjJLabel3.setBounds(194, 96, 100, 16);
				ivjJLabel3.setForeground(java.awt.Color.black);
				ivjJLabel3.setRequestFocusEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel3;
	}

	/**
	 * 返回 JLabel4 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLabel4() {
		if (ivjJLabel4 == null) {
			try {
				ivjJLabel4 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLabel4.setName("JLabel4");
				ivjJLabel4.setText(StringResource
						.getStringResource("miufo1000820")); // "函数格式："
				ivjJLabel4.setBounds(17, 8, 70, 16);
				ivjJLabel4.setForeground(java.awt.Color.black);
				ivjJLabel4.setRequestFocusEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel4;
	}

	/**
	 * 返回 JLabel5 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLabel5() {
		if (ivjJLabel5 == null) {
			try {
				ivjJLabel5 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLabel5.setName("JLabel5");
				ivjJLabel5.setText(StringResource
						.getStringResource("miufo1000821")); // "参数说明："
				ivjJLabel5.setBounds(17, 48, 70, 16);
				ivjJLabel5.setForeground(java.awt.Color.black);
				ivjJLabel5.setRequestFocusEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel5;
	}

	/**
	 * 返回 JLabel51 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLabel51() {
		if (ivjJLabel51 == null) {
			try {
				ivjJLabel51 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLabel51.setName("JLabel51");
				ivjJLabel51.setText(StringResource
						.getStringResource("miufo1000822")); // "函数内容："
				ivjJLabel51.setBounds(7, 9, 70, 16);
				ivjJLabel51.setForeground(java.awt.Color.black);
				ivjJLabel51.setRequestFocusEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel51;
	}

	/**
	 * 返回 JLabel6 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLabel6() {
		if (ivjJLabel6 == null) {
			try {
				ivjJLabel6 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLabel6.setName("JLabel6");
				ivjJLabel6.setText(StringResource
						.getStringResource("miufo1000823")); // "函数说明："
				ivjJLabel6.setBounds(8, 6, 76, 16);
				ivjJLabel6.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel6;
	}

	/**
	 * 返回 JLabel61 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLabel61() {
		if (ivjJLabel61 == null) {
			try {
				ivjJLabel61 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLabel61.setName("JLabel61");
				ivjJLabel61.setText(StringResource
						.getStringResource("miufo1000824")); // "参数输入："
				ivjJLabel61.setBounds(8, 203, 76, 16);
				ivjJLabel61.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel61;
	}

	/**
	 * 返回 JLabelFuncName 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLabelFuncName() {
		if (ivjJLabelFuncName == null) {
			try {
				ivjJLabelFuncName = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLabelFuncName.setName("JLabelFuncName");
				ivjJLabelFuncName.setText("");
				ivjJLabelFuncName.setBounds(120, 7, 265, 16);
				ivjJLabelFuncName.setForeground(java.awt.Color.black);
				ivjJLabelFuncName.setRequestFocusEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabelFuncName;
	}

	/**
	 * 返回 JLabel6 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLabelParam(int i) {
		if (ivjJLabelParams[i] == null) {
			try {
				ivjJLabelParams[i] = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLabelParams[i].setName("JLabel" + Integer.toString(i));
				ivjJLabelParams[i].setText("");
				ivjJLabelParams[i].setVisible(false);
				ivjJLabelParams[i].setForeground(java.awt.Color.black);
				ivjJLabelParams[i].setRequestFocusEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabelParams[i];
	}

	/**
	 * 返回 JListFuncName 特性值。
	 * 
	 * @return javax.swing.JList
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JList getJListFuncName() {
		if (ivjJListFuncName == null) {
			try {
				ivjJListFuncName = new UIList();
				ivjJListFuncName.setName("JListFuncName");
				ivjJListFuncName.setBounds(0, 0, 157, 193);
				ivjJListFuncName
						.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
				ivjJListFuncName.setModel(model);

				ivjJListFuncName.addListSelectionListener(this);
				ivjJListFuncName.addFocusListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJListFuncName;
	}

	/**
	 * 返回 JListFuncType 特性值。
	 * 
	 * @return javax.swing.JList
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JList getJListFuncType() {
		if (ivjJListFuncType == null) {
			try {
				ivjJListFuncType = new UIList();
				ivjJListFuncType.setName("JListFuncType");
				ivjJListFuncType.setBounds(0, 0, 157, 128);
				ivjJListFuncType
						.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
				ivjJListFuncType.setListData(m_ufoFuncList.getCatList());
				ivjJListFuncType.setSelectedIndex(0);
				ivjJListFuncType.addListSelectionListener(this);
				ivjJListFuncType.addFocusListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJListFuncType;
	}

	/**
	 * 返回 JPanel1 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getJPanel1() {
		if (ivjJPanel1 == null) {
			try {
				ivjJPanel1 = new UIPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setBounds(20, 7, 380, 372);
				ivjJPanel1.setVisible(true);
				ivjJPanel1.setRequestFocusEnabled(false);
				getJPanel1()
						.add(getJScrollPane1(), getJScrollPane1().getName());
				getJPanel1().add(getJLabel1(), getJLabel1().getName());
				getJPanel1().add(getJLabelFuncName(),
						getJLabelFuncName().getName());
				getJPanel1().add(getJLabel2(), getJLabel2().getName());
				getJPanel1().add(getJLabel3(), getJLabel3().getName());
				getJPanel1()
						.add(getJScrollPane2(), getJScrollPane2().getName());
				getJPanel1().add(getJLabel11(), getJLabel11().getName());
				getJPanel1().add(getJTAFuncInfo(), getJTAFuncInfo().getName());
				getJPanel1().add(getJTAErrMsg(), getJTAErrMsg().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * 返回 JPanel2 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getJPanel2() {
		if (ivjJPanel2 == null) {
			try {
				ivjJPanel2 = new UIPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setLayout(null);
				ivjJPanel2.setBounds(20, 7, 380, 300);
				ivjJPanel2.setVisible(false);
				ivjJPanel2.setRequestFocusEnabled(false);
				getJPanel2().add(getJLabel4(), getJLabel4().getName());
				getJPanel2().add(getJScrollPaneFuncFormat(),
						getJScrollPaneFuncFormat().getName());
				getJPanel2().add(getJLabel5(), getJLabel5().getName());
				getJPanel2().add(getJTextField2(), getJTextField2().getName());
				getJPanel2().add(getJScrollPanelParam(),
						getJScrollPanelParam().getName());
				getJPanel2().add(getJScrollPaneFuncDes(),
						getJScrollPaneFuncDes().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJPanel2;
	}

	/**
	 * 返回 JPanel3 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getJPanel3() {
		if (ivjJPanel3 == null) {
			try {
				ivjJPanel3 = new UIPanel();
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setLayout(null);
				ivjJPanel3.setBounds(25, 304, 370, 76);
				ivjJPanel3.setVisible(false);
				getJPanel3().add(getJTFFormula(), getJTFFormula().getName());
				getJPanel3().add(getJTAFormulaErr(),
						getJTAFormulaErr().getName());
				getJPanel3().add(getJLabel51(), getJLabel51().getName());
				ivjJPanel3.setRequestFocusEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJPanel3;
	}

	/**
	 * 返回 JPanelParam 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getJPanelParam() {
		if (ivjJPanelParam == null) {
			try {
				ivjJPanelParam = new UIPanel();
				ivjJPanelParam.setName("JPanelParam");
				ivjJPanelParam.setLayout(new GridBagLayout());
				ivjJPanelParam.setRequestFocusEnabled(false);
				GridBagConstraints gridBagCon = new GridBagConstraints();

				for (int i = 0; i < m_ValNum; i++) {
					gridBagCon.gridx = 0;
					gridBagCon.gridy = i;
					gridBagCon.insets = new Insets(2, 2, 2, 2);
					getJPanelParam().add(getJLabelParam(i), gridBagCon);
					gridBagCon.gridx = 1;
					gridBagCon.gridy = i;
					getJPanelParam().add(getJTFParam(i), gridBagCon);
					gridBagCon.gridx = 2;
					gridBagCon.gridy = i;
					getJPanelParam().add(getJBFold(i), gridBagCon);
				}
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJPanelParam;
	}

	/**
	 * 返回 JPanelU8 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getJPanelU8() {
		if (ivjJPanelU8 == null) {
			try {
				ivjJPanelU8 = new UIPanel();
				ivjJPanelU8.setName("JPanelU8");
				ivjJPanelU8.setLayout(null);
				ivjJPanelU8.setBounds(20, 4, 389, 254);
				ivjJPanelU8.setVisible(false);
				getJPanelU8().add(getJLabel6(), getJLabel6().getName());
				getJPanelU8().add(getJLabel61(), getJLabel61().getName());
				getJPanelU8().add(getjTFU8Input(), getjTFU8Input().getName());
				getJPanelU8().add(getJBRefer(), getJBRefer().getName());
				getJPanelU8().add(getJScrollPane3(),
						getJScrollPane3().getName());
				// user code begin {1}
				ivjJPanelU8.setRequestFocusEnabled(false);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJPanelU8;
	}

	/**
	 * 返回 JScrollPane1 特性值。
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JScrollPane getJScrollPane1() {
		if (ivjJScrollPane1 == null) {
			try {
				ivjJScrollPane1 = new UIScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setBounds(15, 117, 160, 204);
				ivjJScrollPane1.setRequestFocusEnabled(false);
				getJScrollPane1().setViewportView(getJListFuncType());
				// user code begin {1}

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * 返回 JScrollPane2 特性值。
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JScrollPane getJScrollPane2() {
		if (ivjJScrollPane2 == null) {
			try {
				ivjJScrollPane2 = new UIScrollPane();
				ivjJScrollPane2.setName("JScrollPane2");
				ivjJScrollPane2.setBounds(194, 117, 160, 204);
				ivjJScrollPane2.setRequestFocusEnabled(false);
				getJScrollPane2().setViewportView(getJListFuncName());
				// user code begin {1}

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPane2;
	}

	/**
	 * 返回 JScrollPane3 特性值。
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JScrollPane getJScrollPane3() {
		if (ivjJScrollPane3 == null) {
			try {
				ivjJScrollPane3 = new UIScrollPane();
				ivjJScrollPane3.setName("JScrollPane3");
				ivjJScrollPane3.setBounds(8, 22, 375, 177);
				getJScrollPane3().setViewportView(getJTAU8Info());
				// user code begin {1}
				ivjJScrollPane3.setRequestFocusEnabled(false);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPane3;
	}

	/**
	 * 返回 JScrollPane3 特性值。
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JScrollPane getJScrollPaneFuncDes() {
		if (ivjJScrollPaneFuncDes == null) {
			try {
				ivjJScrollPaneFuncDes = new UIScrollPane();
				ivjJScrollPaneFuncDes.setName("JScrollPaneFuncDes");
				ivjJScrollPaneFuncDes.setBounds(88, 48, 280, 87);
				ivjJScrollPaneFuncDes.setViewportView(getJTAFuncDes());
				// user code begin {1}
				ivjJScrollPaneFuncDes.setRequestFocusEnabled(false);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPaneFuncDes;
	}

	/**
	 * 返回 JScrollPane3 特性值。
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JScrollPane getJScrollPaneFuncFormat() {
		if (ivjJScrollPaneFunFormat == null) {
			try {
				ivjJScrollPaneFunFormat = new UIScrollPane();
				ivjJScrollPaneFunFormat.setName("JScrollPaneFuncFormat");
				ivjJScrollPaneFunFormat.setBounds(88, 7, 280, 40);
				ivjJScrollPaneFunFormat.setViewportView(getJTAFuncFormat());
				// user code begin {1}
				ivjJScrollPaneFunFormat.setRequestFocusEnabled(false);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPaneFunFormat;
	}

	/**
	 * 返回 JPanelParam 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JScrollPane getJScrollPanelParam() {
		if (ivjJScrollPanelParam == null) {
			try {
				ivjJScrollPanelParam = new UIScrollPane(getJPanelParam());
				ivjJScrollPanelParam.setName("JScrollPanelParam");
				ivjJScrollPanelParam.setBounds(6, 149, 370, 146);
				ivjJScrollPanelParam.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjJScrollPanelParam.setBorder(new TitledBorder(null,
						StringResource.getStringResource("miufo1000825"),
						TitledBorder.LEFT, TitledBorder.TOP, new Font("dialog",
								0, 14), Color.black));

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPanelParam;
	}

	/**
	 * 返回 JTAErrMsg 特性值。
	 * 
	 * @return javax.swing.JTextArea
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextArea getJTAErrMsg() {
		if (ivjJTAErrMsg == null) {
			try {
				ivjJTAErrMsg = new UITextArea();
				ivjJTAErrMsg.setName("JTAErrMsg");
				ivjJTAErrMsg.setBounds(15, 332, 338, 24);
				ivjJTAErrMsg.setEditable(false);
				ivjJTAErrMsg.setForeground(java.awt.Color.red);
				ivjJTAErrMsg.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjJTAErrMsg.setBackground(ivjUfoDialogContentPane
						.getBackground());
				ivjJTAErrMsg.setLineWrap(true);
				ivjJTAErrMsg.addFocusListener(this);

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTAErrMsg;
	}

	/**
	 * 返回 JTAFormulaErr 特性值。
	 * 
	 * @return javax.swing.JTextArea
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextArea getJTAFormulaErr() {
		if (ivjJTAFormulaErr == null) {
			try {
				ivjJTAFormulaErr = new UITextArea();
				ivjJTAFormulaErr.setName("JTAFormulaErr");
				ivjJTAFormulaErr.setBounds(7, 36, 361, 44);
				ivjJTAFormulaErr.setEditable(false);
				ivjJTAFormulaErr.setForeground(java.awt.Color.red);
				ivjJTAFormulaErr.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjJTAFormulaErr.setBackground(ivjUfoDialogContentPane
						.getBackground());
				ivjJTAFormulaErr.setLineWrap(true);
				ivjJTAFormulaErr.addFocusListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTAFormulaErr;
	}

	/**
	 * 返回 JTAFuncDes 特性值。
	 * 
	 * @return javax.swing.JTextArea
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextArea getJTAFuncDes() {
		if (ivjJTAFuncDes == null) {
			try {
				ivjJTAFuncDes = new UITextArea();
				ivjJTAFuncDes.setName("JTAFuncDes");
				ivjJTAFuncDes.setBounds(88, 32, 280, 87);
				ivjJTAFuncDes.setEditable(false);
				ivjJTAFuncDes.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjJTAFuncDes.setBackground(ivjJPanel2.getBackground());
				ivjJTAFuncDes.setLineWrap(true);
				ivjJTAFuncDes.addFocusListener(this);

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTAFuncDes;
	}

	/**
	 * 返回 JLabelFuncFormat 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextArea getJTAFuncFormat() {
		if (ivjJTAFuncFormat == null) {
			try {
				ivjJTAFuncFormat = new UITextArea();
				ivjJTAFuncFormat.setName("JLabelFuncFormat");
				ivjJTAFuncFormat.setBounds(88, 7, 280, 16);
				ivjJTAFuncFormat.setForeground(java.awt.Color.black);
				ivjJTAFuncFormat.setEditable(false);
				ivjJTAFuncFormat.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjJTAFuncFormat.setBackground(ivjJPanel2.getBackground());
				ivjJTAFuncFormat.setLineWrap(true);
				ivjJTAFuncFormat.addFocusListener(this);

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTAFuncFormat;
	}

	/**
	 * 返回 JTAFuncInfo 特性值。
	 * 
	 * @return javax.swing.JTextArea
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextArea getJTAFuncInfo() {
		if (ivjJTAFuncInfo == null) {
			try {
				ivjJTAFuncInfo = new UITextArea();
				ivjJTAFuncInfo.setName("JTAFuncInfo");
				ivjJTAFuncInfo.setBounds(120, 32, 250, 55);
				ivjJTAFuncInfo.setEditable(false);
				ivjJTAFuncInfo.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjJTAFuncInfo.setBackground(ivjJPanel1.getBackground());
				ivjJTAFuncInfo.setLineWrap(true);
				ivjJTAFuncInfo.addFocusListener(this);

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTAFuncInfo;
	}

	/**
	 * 返回 JTAU8Info 特性值。
	 * 
	 * @return javax.swing.JTextArea
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextArea getJTAU8Info() {
		if (ivjJTAU8Info == null) {
			try {
				ivjJTAU8Info = new UITextArea();
				ivjJTAU8Info.setName("JTAU8Info");
				ivjJTAU8Info.setBounds(0, 0, 284, 147);
				// user code begin {1}
				ivjJTAU8Info.setRequestFocusEnabled(false);
				ivjJTAU8Info.setBackground(ivjJPanel1.getBackground());
				ivjJTAU8Info.setLineWrap(true);
				ivjJTAU8Info.addFocusListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTAU8Info;
	}

	/**
	 * 返回 JTextField1 特性值。
	 * 
	 * @return javax.swing.JTextField
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextField getJTextField1() {
		if (ivjJTextField1 == null) {
			try {
				ivjJTextField1 = new JTextField();
				ivjJTextField1.setName("JTextField1");
				ivjJTextField1.setBounds(13, 387, 396, 2);
				ivjJTextField1.setRequestFocusEnabled(false);
				// user code begin {1}

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTextField1;
	}

	/**
	 * 返回 JTextField2 特性值。
	 * 
	 * @return javax.swing.JTextField
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextField getJTextField2() {
		if (ivjJTextField2 == null) {
			try {
				ivjJTextField2 = new JTextField();
				ivjJTextField2.setName("JTextField2");
				ivjJTextField2.setBounds(13, 142, 353, 2);
				ivjJTextField2.setRequestFocusEnabled(false);
				// user code begin {1}

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTextField2;
	}

	/**
	 * 返回 JTFFormula 特性值。
	 * 
	 * @return javax.swing.JTextField
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextField getJTFFormula() {
		if (ivjJTFFormula == null) {
			try {
				ivjJTFFormula = new HrFmlField();
				ivjJTFFormula.setName("JTFFormula");
				ivjJTFFormula.setBounds(73, 7, 295, 20);
				ivjJTFFormula.setVisible(true);
				ivjJTFFormula.setEditable(true);
				ivjJTFFormula.setRequestFocusEnabled(true);
				// user code begin {1}
				EditDocument document = new EditDocument();
				ivjJTFFormula.setDocument(document);
				ivjJTFFormula.addFocusListener(this);
				ivjJTFFormula.setBackground(ivjUfoDialogContentPane
						.getBackground());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTFFormula;
	}

	/**
	 * 返回 JTFArea 特性值。
	 * 
	 * @return javax.swing.JTextField
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextField getJTFParam(int i) {
		if (ivjJTFParams[i] == null) {
			try {
				ivjJTFParams[i] = new JTextField();
				ivjJTFParams[i].setName("JTFParam" + Integer.toString(i));
				ivjJTFParams[i].setVisible(false);
				// user code begin {1}
				ivjJTFParams[i].addFocusListener(this);
				ivjJTFParams[i].addActionListener(this);
				ivjJTFParams[i].setPreferredSize(new Dimension(180, 21));
				ValueChangeDocument document = new ValueChangeDocument();
				ivjJTFParams[i].setDocument(document);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTFParams[i];
	}

	/**
	 * 返回 JTFSelArea 特性值。
	 * 
	 * @return javax.swing.JTextField
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextField getJTFSelArea() {
		if (ivjJTFSelArea == null) {
			try {
				ivjJTFSelArea = new JTextField();
				ivjJTFSelArea.setName("JTFSelArea");
				ivjJTFSelArea.setBounds(2, 1, 392, 21);
				ivjJTFSelArea.setVisible(false);
				// user code begin {1}
				ivjJTFSelArea.addFocusListener(this);
				ivjJTFSelArea.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTFSelArea;
	}

	/**
	 * 返回 jTFU8Input 特性值。
	 * 
	 * @return javax.swing.JTextField
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextField getjTFU8Input() {
		if (ivjjTFU8Input == null) {
			try {
				ivjjTFU8Input = new JTextField();
				ivjjTFU8Input.setName("jTFU8Input");
				ivjjTFU8Input.setBounds(8, 230, 312, 23);
				// user code begin {1}
				ivjjTFU8Input.addFocusListener(this);
				ValueChangeDocument document = new ValueChangeDocument();
				ivjjTFU8Input.setDocument(document);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjjTFU8Input;
	}

	/**
	 * 返回 UfoDialogContentPane 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getUfoDialogContentPane() {
		if (ivjUfoDialogContentPane == null) {
			try {
				ivjUfoDialogContentPane = new UIPanel();
				ivjUfoDialogContentPane.setName("UfoDialogContentPane");
				ivjUfoDialogContentPane.setLayout(null);
				ivjUfoDialogContentPane.setRequestFocusEnabled(false);
				getUfoDialogContentPane().add(getJPanel1(),
						getJPanel1().getName());
				getUfoDialogContentPane().add(getJTextField1(),
						getJTextField1().getName());
				getUfoDialogContentPane().add(getJBPre(), getJBPre().getName());
				getUfoDialogContentPane().add(getJBNext(),
						getJBNext().getName());
				getUfoDialogContentPane().add(getJBCancel(),
						getJBCancel().getName());
				getUfoDialogContentPane().add(getJPanel2(),
						getJPanel2().getName());
				getUfoDialogContentPane().add(getJTFSelArea(),
						getJTFSelArea().getName());
				getUfoDialogContentPane().add(getJBFold(),
						getJBFold().getName());
				getUfoDialogContentPane().add(getJPanelU8(),
						getJPanelU8().getName());
				getUfoDialogContentPane().add(getJPanel3(),
						getJPanel3().getName());
				// user code begin {1}
				ivjUfoDialogContentPane.addFocusListener(this);
				m_FuncCategory = (UfoSimpleObject) ivjJListFuncType
						.getSelectedValue();
				if (m_FuncCategory != null) {
					UfoSimpleObject[] m_FuncNameList = m_ufoFuncList
							.getFuncList(m_FuncCategory.getID());
					ivjJLabelFuncName.setText(m_FuncCategory.getName());
					model.removeAllElements();
					if (m_FuncNameList.length > 0) {
						for (int i = 0; i < m_FuncNameList.length; i++)
							model.addElement(m_FuncNameList[i]);
						ivjJListFuncName.setSelectedIndex(0);

						m_FuncName = (UfoSimpleObject) ivjJListFuncName
								.getSelectedValue();
						if (m_FuncName != null) {
							ivjJLabelFuncName.setText(m_FuncCategory.getName()
									+ StringResource
											.getStringResource("miufo1000826")
									+ m_FuncName.getName()); // "——"
							UfoFuncInfo m_FuncInfo = m_ufoFuncList
									.getFuncInfo(m_FuncName.getID());
							ivjJTAFuncInfo.setText(m_FuncInfo
									.getFuncDescription());
							ivjJTAFuncDes.setText(m_ufoFuncList
									.getParamDescription(m_FuncName.getID(),
											m_FuncName.getName()));
							ivjJTAFuncFormat.setText(m_ufoFuncList
									.getFuncFormat(m_FuncName.getID(),
											m_FuncName.getName()));
						}
					}
				} else {
					model.removeAllElements();
					ivjJLabelFuncName.setText("");
					ivjJTAFuncInfo.setText("");
				}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUfoDialogContentPane;
	}

	public String getunitID() {
		return unitId;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		AppDebug.debug(exception);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("CellFuncWizardDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setTitle(StringResource.getStringResource("miufo1000815")); // "函数向导（函数名）"
			setSize(426, 468);
			setModal(false);
			setResizable(false);
			setContentPane(getUfoDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		setLocationRelativeTo(this);

		addSelectedModelListener();

		ivjJBCancel.setNextFocusableComponent(ivjJListFuncType);
		ivjJListFuncType.setNextFocusableComponent(ivjJListFuncName);
		ivjJListFuncName.setNextFocusableComponent(ivjJBNext);
		ivjJBNext.setNextFocusableComponent(ivjJBCancel);

		// 帮助
		try {
			javax.help.HelpBroker hb = ResConst.getHelpBroker();// UfoPublic.getHelpBroker(false);
			if (hb == null) {
				return;
			}
			hb.enableHelpKey(getContentPane(), "TM_Data_Exp_Wizard1", null);
			javax.help.CSH.setHelpIDString(getJPanel1(), "TM_Data_Exp_Wizard1");
			javax.help.CSH.setHelpIDString(getJPanel2(), "TM_Data_Exp_Wizard2");
		} catch (Exception ex) {
			// 吃掉无法加载帮助产生的异常,20040429 wupeng
			AppDebug.debug(ex);
		}

		// user code end
	}

	/**
	 * 根据函数名称判断该函数是否具有弹出参照界面的功能。 创建日期：(2003-12-18 15:11:56)
	 * 
	 * @author：岳益民
	 * @return boolean
	 * @param strFunName
	 *            java.lang.String
	 */
	private boolean isRefFunc(String strFunName) {
		/**
		 * 如果是指标查询函数，则返回true 如果是外部注册函数，但又不是IUFO2FuncDrive，则返回true 其余返回false
		 */

		if (m_ufoFuncList.getFuncID(strFunName) != UfoFuncList.EXTFUNCID) {
			// 如果不是外部注册函数，则返回false
			return false;
		}
		Object driver = m_ufoFuncList.getExtDriver(m_ufoFuncList
				.getExtFuncDriver(strFunName));
		if (driver != null) {
			if (ExtFunc.isIUFO2FuncDriver(driver)
					&& !(driver instanceof nc.ui.iufo.query.measurequery.MeasureQueryFuncDriver)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 此处插入方法描述。 创建日期：(2003-9-19 10:47:03)
	 * 
	 * @param newContextVO
	 *            com.ufsoft.iuforeport.reporttool.temp.ContextVO
	 */
	public void setContextVO(IContext newContextVO) {
		this.context = newContextVO;
	}

	/**
	 * 根据函数种类的不同，设置不同的参数输入界面。 创建日期：(00-11-28 14:58:24)
	 */
	private void setFuncVal() {
		for (int i = 0; i < ParamMaxNum; i++) {
			ivjJLabelParams[i].setVisible(false);
			ivjJTFParams[i].setVisible(false);
			ivjJBFolds[i].setVisible(false);
		}
		String[] Inputs;

		UfoFuncInfo nFuncInfo = m_ufoFuncList.getFuncInfo(m_FuncName.getID(),
				m_FuncName.getName());
		byte[] typelist = nFuncInfo.m_ptypelist;
		if (typelist != null) {
			int length = typelist.length;
			Inputs = new String[length];
			m_ValNum = length;
			for (int i = 0; i < length; i++) {
				Inputs[i] = m_ufoFuncList.getParaName(m_FuncName.getName(), i)
						+ StringResource.getStringResource("miufopublic100"); // "："
			}
			setInputVal(length, Inputs);
		} else {
			Inputs = null;
			m_ValNum = 0;
			setInputVal(0, Inputs);
		}
	}

	/**
	 * 根据参数个数，决定显示参数输入框的数目。 创建日期：(00-11-28 14:58:24)
	 */
	private void setInputVal(int num, String[] str) {
		if (num > ParamMaxNum)
			num = ParamMaxNum;
		for (int i = 0; i < num; i++) {
			ivjJLabelParams[i].setText(str[i]);
			ivjJLabelParams[i].setVisible(true);
			ivjJTFParams[i].setVisible(true);
			ivjJBFolds[i].setVisible(true);
		}
		setTextFieldFocus(num);
	}

	/**
	 * 是否Ｋ类型的函数
	 * 
	 * @return
	 */
	private boolean isAddKFunc() {
		if (funcNameNoKAry == null || funcNameNoKAry.length == 0)
			return true;
		if (m_SelFuncName == null || m_SelFuncName.trim().length() == 0)
			return true;
		for (String funcName : funcNameNoKAry) {
			if (funcName.equalsIgnoreCase(m_SelFuncName))
				return false;
		}
		return true;
	}

	/**
	 * 根据参数个数，决定按“tab”时焦点的顺序。 创建日期：(00-11-28 14:58:24)
	 */
	private void setTextFieldFocus(int num) {
		if (num > 1) {
			if (num > ParamMaxNum)
				num = ParamMaxNum;

			ivjJTFParams[0].requestFocus();
			for (int i = 1; i < num - 1; i++) {
				ivjJTFParams[i].setNextFocusableComponent(ivjJTFParams[i + 1]);
			}
			ivjJTFParams[num - 1].setNextFocusableComponent(ivjJTFFormula);
			ivjJTFFormula.setNextFocusableComponent(ivjJBPre);
			ivjJBPre.setNextFocusableComponent(ivjJBNext);
			ivjJBNext.setNextFocusableComponent(ivjJBCancel);
			ivjJBCancel.setNextFocusableComponent(ivjJTFParams[0]);
			ivjJTFParams[0].setNextFocusableComponent(ivjJTFParams[1]);
		} else if (num == 1) {
			ivjJTFParams[0].requestFocus();
			ivjJTFFormula.setNextFocusableComponent(ivjJBPre);
			ivjJBPre.setNextFocusableComponent(ivjJBNext);
			ivjJBNext.setNextFocusableComponent(ivjJBCancel);
			ivjJBCancel.setNextFocusableComponent(ivjJTFParams[0]);
			ivjJTFParams[0].setNextFocusableComponent(ivjJTFFormula);
		} else if (num == 0) {
			ivjJBPre.requestFocus();
		}
	}

	public void setUnitID(String unitId) {
		this.unitId = unitId;
	}

	private void addSelectedModelListener() {
		if (cellsModel == null) {
			return;
		}
		if (m_selectListener == null) {
			m_selectListener = new SelectListener() {
				public void selectedChanged(SelectEvent e) {
					AreaPosition area = cellsModel.getSelectModel()
							.getSelectedArea();
					ivjJTFSelArea.setText(area.toString());

				}
			};
		}
		cellsModel.getSelectModel().addSelectModelListener(m_selectListener);
	}

	private void removeSelectedModelListener() {
		if (cellsModel == null) {
			return;
		}

		if (m_selectListener != null) {
			cellsModel.getSelectModel().removeSelectModelListener(
					m_selectListener);
		}
	}

	/**
	 * 错误信息。 创建日期：(00-11-28 14:58:24)
	 */
	private void showmessage(String errs) {
		ivjJTAErrMsg.setText(errs);
		ivjJTFParams[0].requestFocus();
		ivjJTFParams[0].selectAll();
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == ivjJListFuncType) {
			m_FuncCategory = (UfoSimpleObject) ivjJListFuncType
					.getSelectedValue();
			if (m_FuncCategory != null) {
				UfoSimpleObject[] funcNameList = m_ufoFuncList
						.getFuncList(m_FuncCategory.getID());
				model.removeAllElements();
				ivjJScrollPane2.validate();
				if (funcNameList != null) {
					for (int i = 0; i < funcNameList.length; i++)
						model.addElement(funcNameList[i]);

					ivjJLabelFuncName.setText(m_FuncCategory.getName());

					ivjJListFuncName.setSelectedIndex(0);
					m_FuncName = (UfoSimpleObject) ivjJListFuncName
							.getSelectedValue();
					if (m_FuncName != null) {// 显示选中函数的相关信息
						if (isRefFunc(m_FuncName.getName())) {
							ivjJLabelFuncName.setText(m_FuncCategory.getName()
									+ StringResource
											.getStringResource("miufo1000826")
									+ m_FuncName.getName()); // "——"
							String strDriverName = m_ufoFuncList
									.getExtFuncDriver(m_FuncName.getName());

							try {
								IFuncDriver driver = null;
								driver = (IFuncDriver) m_ufoFuncList
										.getExtDriver(strDriverName);
								ivjJTAFuncInfo.setText(driver
										.getFuncDesc(m_FuncName.getName()));
								ivjJTAU8Info.setText(driver
										.getFuncForm(m_FuncName.getName()));
							} catch (Exception ex) {
								ex.printStackTrace(System.out);
							}
						} else {
							ivjJLabelFuncName.setText(m_FuncCategory.getName()
									+ StringResource
											.getStringResource("miufo1000826")
									+ m_FuncName.getName()); // "——"
							UfoFuncInfo nFuncInfo = m_ufoFuncList.getFuncInfo(
									m_FuncName.getID(), m_FuncName.getName());
							ivjJTAFuncInfo.setText(nFuncInfo
									.getFuncDescription());
							ivjJTAFuncDes.setText(m_ufoFuncList
									.getParamDescription(m_FuncName.getID(),
											m_FuncName.getName()));
							ivjJTAFuncFormat.setText(m_ufoFuncList
									.getFuncFormat(m_FuncName.getID(),
											m_FuncName.getName()));
						}
					}
				}
				ivjJTAErrMsg.setText("");// empty error message
				ivjJTAFormulaErr.setText("");
				for (int i = 0; i < ParamMaxNum; i++) {
					ivjJTFParams[i].setText("");
				}
				ivjjTFU8Input.setText("");
			} else {// 清除函数种类的选择
				model.removeAllElements();
				ivjJLabelFuncName.setText("");
				ivjJTAFuncInfo.setText("");
			}
		}
		if (e.getSource() == ivjJListFuncName) {
			m_FuncName = (UfoSimpleObject) ivjJListFuncName.getSelectedValue();
			if (m_FuncName != null) {// 显示选中函数的信息
				if (isRefFunc(m_FuncName.getName())) {
					ivjJLabelFuncName.setText(m_FuncCategory.getName()
							+ StringResource.getStringResource("miufo1000826")
							+ m_FuncName.getName()); // "——"
					String strDriverName = m_ufoFuncList
							.getExtFuncDriver(m_FuncName.getName());
					IFuncDriver driver = null;
					try {
						driver = (IFuncDriver) m_ufoFuncList
								.getExtDriver(strDriverName);
						ivjJTAFuncInfo.setText(driver.getFuncDesc(m_FuncName
								.getName()));
						ivjJTAU8Info.setText(driver.getFuncForm(m_FuncName
								.getName()));
					} catch (Exception ex) {
					}
				} else {
					ivjJLabelFuncName.setText(m_FuncCategory.getName()
							+ StringResource.getStringResource("miufo1000826")
							+ m_FuncName.getName()); // "——"

					UfoFuncInfo nFuncInfo = m_ufoFuncList.getFuncInfo(
							m_FuncName.getID(), m_FuncName.getName());

					ivjJTAFuncInfo.setText(nFuncInfo.getFuncDescription());
					ivjJTAFuncDes.setText(m_ufoFuncList.getParamDescription(
							m_FuncName.getID(), m_FuncName.getName()));
					ivjJTAFuncFormat.setText(m_ufoFuncList.getFuncFormat(
							m_FuncName.getID(), m_FuncName.getName()));
				}
				ivjJTAErrMsg.setText("");// empty error message
				ivjJTAFormulaErr.setText("");
				for (int i = 0; i < ParamMaxNum; i++) {
					ivjJTFParams[i].setText("");
				}
				ivjjTFU8Input.setText("");
			} else {// 清除函数的选择
				ivjJLabelFuncName.setText(m_FuncCategory.getName());
				ivjJTAFuncInfo.setText("");
			}
		}
	}

	private UfoFmlExecutor getUfoExecutor() {
		FormulaModel formulaModel = FormulaModel.getInstance(cellsModel);
		return formulaModel.getUfoFmlExecutor();
	}

	/**
	 * 去多余的引号
	 * 
	 * @param str
	 * @return
	 */
	private String toDelQuotes(String str) {
		if (str == null || str.length() == 0 || str.equals("''")) {
			return "";
		}
		if (str.charAt(0) == '\'' && str.charAt(str.length() - 1) == '\'') {
			return str.substring(1, str.length() - 1);
		} else {
			return str;
		}
	}

	/**
	 * 如果函数中有指标名称，则返回指标名称在参数中的位置，以便获得指标名称，从中取出指标所属报表code
	 * 
	 * @return
	 */
	private int getMeasureInfoIndex() {
		UfoFuncInfo nFuncInfo = m_ufoFuncList.getFuncInfo(m_FuncName.getID(),
				m_FuncName.getName());
		byte[] typelist = nFuncInfo.m_ptypelist;
		if (typelist != null) {
			int length = typelist.length;

			for (int i = 0; i < length; i++) {
				String strParaName = m_ufoFuncList.getParaName(m_FuncName
						.getName(), i)
						+ StringResource.getStringResource("miufopublic100"); // "："
				if (strParaName != null
						&& strParaName.indexOf(StringResource
								.getStringResource("miufo1000172")) != -1) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 获得时间关键字参照
	 * 
	 * @param strMeasureName：指标的名称，如果为空，则显示自然和会计类
	 * @return
	 * @i18n miufo01188=语法错误：指标所属的报表编码不存在
	 */
	private DynKeywordRefDlg getTimeKeyRefDlg(String strMeasureName) {
		String strRepCode = null;
		if (strMeasureName != null && strMeasureName.trim().length() > 0) {
			int index = strMeasureName.indexOf("->");
			if (index >= 0)
				strRepCode = strMeasureName.substring(1, index);
		}
		if (getMeasureInfoIndex() != -1 && strRepCode != null) {// 函数有指标名称参数
			if (!isExistRepByRepCode(strRepCode)) {
				getJTAFormulaErr().setText(
						StringResource.getStringResource("miufo01188"));
				return null;
			}
		}

		KeyVO timeKeyVo = getTimeKeyVo(strRepCode);
		ICacheObject[] cacheVos = CacheProxy.getSingleton().getKeywordCache()
				.getAll();
		Vector<KeyVO> keyVoVector = new Vector<KeyVO>();
		for (ICacheObject cacheObj : cacheVos) {
			KeyVO keyVo = (KeyVO) cacheObj;
			if (keyVo != null && keyVo.isTTimeKeyVO()) {// 过滤掉非时间关键字
				keyVoVector.add(keyVo);
			}
		}
		cacheVos = keyVoVector.toArray(new KeyVO[0]);

		DynKeywordRefDlg dlgKR = new DynKeywordRefDlg(this, null, timeKeyVo,
				cacheVos);
		return dlgKR;
	}

	/**
	 * 根据报表的code获得报表的时间关键字
	 * 
	 * @param strRepCode
	 * @return
	 */
	private KeyVO getTimeKeyVo(String strRepCode) {
		if (strRepCode == null || strRepCode.trim().length() == 0)
			return null;
		CellsModel cellsModel = getCellsModel(strRepCode);
		KeywordModel keyWordModel = KeywordModel.getInstance(cellsModel);
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		DynAreaCell[] dynAreaCells = dynAreaModel.getDynAreaCells();
		HashMap<KeyVO, CellPosition> mainTableKeyVOs = keyWordModel
				.getMainKeyVOPos();
		if (mainTableKeyVOs != null && mainTableKeyVOs.size() > 0) {
			for (Iterator iter = mainTableKeyVOs.keySet().iterator(); iter
					.hasNext();) {
				KeyVO each = (KeyVO) iter.next();
				if (each.isTTimeKeyVO()) {
					return each;
				}
			}
		}

		if (dynAreaCells != null && dynAreaCells.length > 0) {
			int iLength = dynAreaCells.length;
			for (int i = 0; i < iLength; i++) {
				Collection keys = keyWordModel.getKeyVOByArea(
						dynAreaCells[i].getArea()).values();
				for (Iterator iter = keys.iterator(); iter.hasNext();) {
					KeyVO each = (KeyVO) iter.next();
					if (each.isTTimeKeyVO()) {
						return each;
					}
				}
			}
		}
		return null;
	}

	private boolean isExistRepByRepCode(String strRepCode) {
		ReportCache reportCache = CacheProxy.getSingleton().getReportCache();
		return reportCache.isExistRepByRepCode(strRepCode, false);
	}

	/**
	 * 根据报表code从缓存中获得报表的格式模型
	 * 
	 * @param strRepCode
	 * @return
	 */
	private CellsModel getCellsModel(String strRepCode) {
		ReportCache reportCache = CacheProxy.getSingleton().getReportCache();
		String strRepPk = reportCache.getRepPKByCode(strRepCode, false);
		RepFormatModelCache repFormatModelCache = CacheProxy.getSingleton()
				.getRepFormatCache();
		CellsModel formatModel = repFormatModelCache
				.getUfoTableFormatModel(strRepPk);
		return formatModel;
	}

}
