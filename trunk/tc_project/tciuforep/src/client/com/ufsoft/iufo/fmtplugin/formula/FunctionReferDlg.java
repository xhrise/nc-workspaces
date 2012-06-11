/*
 * 创建日期 2006-6-6
 */
package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import nc.pub.iufo.accperiod.AccPeriodSchemeUtil;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.base.ICacheObject;
import nc.ui.iufo.query.measurequery.MeasureQueryFuncDriver;
import nc.ui.iufo.query.measurequery.MeasureQueryRefDlg;
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
import com.ufsoft.iufo.fmtplugin.measure.MultiSelMeasureRefDlg;
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
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.exception.MessageException;
import com.ufsoft.report.fmtplugin.formula.ui.FormulaEditor;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.script.base.AbsFmlExecutor;
import com.ufsoft.script.base.ICalcEnv;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.script.extfunc.LoginInfoFuncDriver;
import com.ufsoft.script.extfunc.MeasFuncDriver;
import com.ufsoft.script.function.ExtFunc;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.script.function.UfoFuncInfo;
import com.ufsoft.script.function.UfoFuncList;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IArea;
import com.ufsoft.table.re.timeref.CalendarDialog;

/**
 * @author ljhua 公式编辑中，函数参照对话框
 */
public class FunctionReferDlg extends AreaSelectDlg implements ActionListener,
		FocusListener, IUfoContextKey {

	private JScrollPane ivjJScrollPaneFunFormat = null;

	private JScrollPane ivjJScrollPaneFunNote = null;

	private JScrollPane ivjJScrollPaneFunNoteU8 = null;

	private JScrollPane ivjJScrollPaneFuncDes = null;

	private JTextField ivjJTextField1 = null;

	private JPanel ivjUfoDialogContentPane = null;

	private JButton ivjJBCancel = null;

	private JButton ivjJBNext = null;

	private UfoSimpleObject m_FuncName = null;

	private String m_retFunc = null;

	private JLabel ivjJLabel4 = null;

	private JLabel ivjJLabel5 = null;

	private JLabel lbFuncNote = null;

	private JLabel lbFuncNoteU8 = null;

	private JTextArea ivjJTAFuncFormat = null;

	private JPanel ivjJPanel2 = null;

	private JPanel ivjJPanelParam = null;

	private JScrollPane ivjJScrollPanelParam = null;

	private JTextField ivjJTextField2 = null;

	private IArea m_area;

	private JTextArea ivjJTAFuncDes = null;

	private JTextArea ivjJTAFuncInfo = null;

	private JTextArea ivjJTAFuncInfoU8 = null;

	private JButton ivjJBFold = null;

	private JTextField ivjJTFSelArea = null;

	private static int ParamMaxNum = 7;

	private JButton[] ivjJBFolds = new nc.ui.pub.beans.UIButton[ParamMaxNum];

	private JLabel[] ivjJLabelParams = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel[ParamMaxNum];

	private JTextField[] ivjJTFParams = new JTextField[ParamMaxNum];

	private String[] funcNameNoKAry = { "K", "ZKEY" };

	private String[] multiMeasureRef = { MeasFuncDriver.MSELECTS,
			MeasFuncDriver.HBMSELECTS };

	private JTextArea ivjJTAErrMsg = null;

	private JPanel ivjJPanel3 = null;

	private JPanel u8ParamPanel = null;

	private JTextArea ivjJTAFormulaErr = null;

	private JTextField ivjJTFFormula = null;

	private JLabel ivjJLabel51 = null;

	private boolean m_isEditClear = true;

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

	private boolean m_autoFold = false; // 标记是否是自动折行

	private int m_FocusPlace = -1;// 1 焦点在TF1 2 焦点在TF2 3 焦点在TF3

	private String m_SelFuncName = null;// 记录选中的函数名称

	private int m_ValNum = ParamMaxNum;// 记录参数个数

	private int iVerticalPixel = 42;

	// wss add 2002-10-24
	private FuncListInst m_ufoFuncList = null;

	private IContext context;

	private CellsPane cellsPane;

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
	public FunctionReferDlg(CellsPane cellsPane, IContext contextVo,
			UfoSimpleObject function, FuncListInst ufoFuncList) {
		super(cellsPane, cellsPane.getDataModel());
		this.m_ufoFuncList = ufoFuncList;
		this.cellsPane = cellsPane;
		this.context = contextVo;
		if (m_ufoFuncList == null)
			throw new IllegalArgumentException();
		setFunc(function);
		initialize();

	}

	public FunctionReferDlg(CellsPane cellsPane, IContext contextVo,
			UfoSimpleObject function) {
		this(cellsPane, contextVo, function, null);
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
					u8Input = getMQFuncParam(cellsPane, m_SelFuncName, context);
				} else {
					if (driver instanceof IFuncDriver2)
						u8Input = ((IFuncDriver2) driver).doFuncRefer(this,
								m_SelFuncName);
					else
						u8Input = driver.doFuncRefer(m_SelFuncName);
				}
				// if(driver instanceof IUFoFuncRefer)
				// u8Input
				// =((IUFoFuncRefer)driver).doFuncRefer(m_frame,m_SelFuncName);
				// else
				// u8Input = driver.doFuncRefer(m_SelFuncName);
			}
		} catch (Exception e1) {
			AppDebug.debug(e1);// @devTools e1.printStackTrace(System.out);
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

		if (m_FuncName == null) {
			showmessage(StringResource.getStringResource("miufo1000812")); // "请选择一个函数！"
			return;
		}
		showmessage("");
		m_SelFuncName = m_FuncName.getName();

		// 是否外部函数
		if (isRefFunc(m_SelFuncName)) {

			ivjJBRefer.setVisible(false);
			ivjjTFU8Input.requestFocus();

			setTitle(StringResource.getStringResource("miufo1000813")); // "函数向导（参数表）"
			ivjJBNext.setText(StringResource.getStringResource("miufo1000814")); // "完
			// 成"

			ivjJPanel2.setVisible(false);
			ivjJPanelU8.setVisible(true);
			ivjJPanel3.setVisible(true);
			createFunc();
			m_isEditClear = false;
			ivjJTFFormula.setText(m_retFunc);
			m_isEditClear = true;

			String strDriverName = m_ufoFuncList.getExtFuncDriver(m_FuncName
					.getName());
			IFuncDriver driver = null;

			try {
				driver = (IFuncDriver) m_ufoFuncList
						.getExtDriver(strDriverName);
			} catch (Exception e2) {

			}
			if (driver != null)
				ivjJTAU8Info.setText(driver.getFuncForm(m_FuncName.getName()));

			// driver和newDriver不会出现同时为空的情况
			if (driver != null && driver.hasReferDlg(m_SelFuncName)) {
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
				ivjJTFFormula.setNextFocusableComponent(ivjJBNext);

				ivjJBNext.setNextFocusableComponent(ivjJBCancel);
				ivjJBCancel.setNextFocusableComponent(ivjjTFU8Input);
				ivjjTFU8Input.setNextFocusableComponent(ivjJBRefer);
			} else {

				ivjJTFFormula.setNextFocusableComponent(ivjJBNext);
				ivjJBNext.setNextFocusableComponent(ivjJBCancel);
				ivjJBCancel.setNextFocusableComponent(ivjjTFU8Input);
				ivjjTFU8Input.setNextFocusableComponent(ivjJTFFormula);
			}
		} else {
			ivjJBRefer.setVisible(false);
			setTitle(StringResource.getStringResource("miufo1000813")); // "函数向导（参数表）"
			ivjJBNext.setText(StringResource.getStringResource("miufo1000814")); // "完
			// 成"
			setFuncVal();

			ivjJPanel2.setVisible(true);
			ivjJPanel3.setVisible(true);
			ivjJPanelU8.setVisible(false);
			createFunc();
			m_isEditClear = false;
			ivjJTFFormula.setText(m_retFunc);
			m_isEditClear = true;
		}

	}

	private void finishAction() {
		createFunc(); // 生成函数的字符串形式
		m_isEditClear = false;
		ivjJTFFormula.setText(m_retFunc);
		m_isEditClear = true;
		// 判断是否正确
		// AreaPosition area = AreaPosition.getInstance("A1");
		try {
			checkUserDefFormula(m_area);
			this.setResult(ID_OK);
			showmessage("");
			this.close();

		} catch (UfoParseException err) {
			ivjJTAFormulaErr.setText(err.getMessage());
			ivjJTFFormula.requestFocus();
			int errPos = err.getErrPos() < 1 ? 0 : err.getErrPos() - 1;
			if (ivjJTFFormula.getText() != null
					&& ivjJTFFormula.getText().length() >= (errPos))
				ivjJTFFormula.setCaretPosition(errPos);
			return;
		} catch (Exception ex) {
			AppDebug.debug(ex);// @devTools AppDebug.debug(ex);
			return;
		}
	}

	protected void checkUserDefFormula(IArea area) throws ParseException {
		getFmlExecutor().checkUserDefFormula(area, getCellFunc());
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @i18n miufo01187=时间关键字参照
	 * @i18n miufo00129=会计期间方案
	 * @i18n miufo00128=NC汇率方案
	 * @i18n miufohbbb00090=请先设置主表关键字
	 * @i18n miufohbbb00091=请先选择会计期间方案
	 * @i18n miufohbbb00092=主表关键字不是会计类的
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ivjJBCancel) {
			this.setResult(ID_CANCEL);
			return;
		} else if (e.getSource() == ivjJBRefer) {
			referAction();
		} else if (e.getSource() == ivjJBNext) {
			finishAction();
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

		// String strKeyGroupPk =
		// (String)getContextVO().getAttribute(KEYGROUP_PK);
		String strCurUserId = (String) context.getAttribute(CUR_USER_ID);
		String managerObj = (String) context.getAttribute(REP_MANAGER);
		boolean isManager = managerObj == null ? false : Boolean
				.valueOf(managerObj);
		Object repIdObj = context.getAttribute(REPORT_PK);
		String strReportPK = repIdObj != null && (repIdObj instanceof String) ? (String) repIdObj
				: null;
		CellsModel cellsModel = getCellsModel();

		try {
			for (int i = 0; i < ParamMaxNum; i++) {
				if (e.getSource() == ivjJBFolds[i]
						|| e.getSource() == ivjJTFParams[i]) {
					String paramName = ivjJLabelParams[i].getText();
					if (paramName == null || paramName.trim().length() == 0)
						continue;

					// 处理指标参照
					if (paramName.indexOf(StringResource
							.getStringResource("miufo1000172")) != -1) { // "指标"
						ReportVO[] repVOs = CacheProxy.getSingleton()
								.getReportCache().getByPks(
										new String[] { strReportPK });
						MeasureRefDlg measureRef = null;
						if (isMuliMeasureRef()) {
							measureRef = new MultiSelMeasureRefDlg(this,
									repVOs[0], null, strCurUserId, true,
									isManager, true);
						} else {
							measureRef = new MeasureRefDlg(this, repVOs[0],
									null, strCurUserId, true, isManager, true);
						}

						measureRef.setLocationRelativeTo(null);
						measureRef.setModal(true);
						measureRef.show();

						if (measureRef.getResult() == measureRef.ID_OK) {
							ivjJTFParams[i].setText(measureRef
									.getStrRefMeasure());
						}
						return;
					}
					// 处理关键字参照
					else if (paramName.indexOf(StringResource
							.getStringResource("miufopublic335")) != -1) { // "关键字"
						// 得到可以参照的关键字
						KeyVO[] keyvos = {};
						KeywordRefDlg refDialog = new KeywordRefDlg(this,
								keyvos);
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
							.getStringResource("uiufofunc024")) >= 0) { // 时间属性
						int iMeasureIndex = getMeasureInfoIndex();
						String strMeasureName = null;
						if (iMeasureIndex != -1)
							strMeasureName = ivjJTFParams[iMeasureIndex]
									.getText();
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
						String strKeyGroupPk = KeywordModel.getInstance(
								cellsModel).getMainKeyCombPK();
						KeyGroupVO keyGVO = CacheProxy.getSingleton()
								.getKeyGroupCache().getByPK(strKeyGroupPk);
						if (keyGVO == null)
							throw new MessageException(
									MessageException.TYPE_WARNING, StringResource
											.getStringResource("miufohbbb00090"));
						String strAccCode = ivjJTFParams[m_ValNum - 1]
								.getText();
						if (strAccCode == null
								|| strAccCode.trim().length() == 0) {
							throw new MessageException(
									MessageException.TYPE_WARNING, StringResource
											.getStringResource("miufohbbb00091"));
						}
						if (!keyGVO.isTTimeTypeAcc()) {
							throw new MessageException(
									MessageException.TYPE_WARNING,
									StringResource
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
						IComRefDlg comRef = ComRefFactory.getRefInstance(
								refParam, this);
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
						m_autoFold = false;
						fold(true);
						return;
					}
				}
			}
		} catch (Throwable ex) {
			AppDebug.debug(ex);
			UfoPublic.sendMessage(ex, this);
		}

		if (e.getSource() == ivjJBFold || e.getSource() == ivjJTFSelArea) {
			fold(false);
			return;
		}
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
		CellsModel cellsModel = getCellsModel();
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
	 * 函数是否调用多选指标参照
	 * 
	 * @return
	 */
	private boolean isMuliMeasureRef() {
		if (multiMeasureRef == null || multiMeasureRef.length == 0)
			return false;
		if (m_SelFuncName == null || m_SelFuncName.trim().length() == 0)
			return false;
		for (String funcName : multiMeasureRef) {
			if (funcName.equalsIgnoreCase(m_SelFuncName))
				return true;
		}
		return false;
	}

	private boolean isExistRepByRepCode(String strRepCode) {
		ReportCache reportCache = CacheProxy.getSingleton().getReportCache();
		return reportCache.isExistRepByRepCode(strRepCode, false);
	}

	/**
	 * 区域参照时，对窗口折叠或恢复。 创建日期：(2001-1-15 9:29:52)
	 * 
	 * @param FoldType
	 *            boolean
	 */
	private void fold(boolean FoldType) {
		getJTAErrMsg().setText("");// empty error message
		setFold(FoldType);
		if (FoldType) {
			if (isRefFunc(m_FuncName.getName())) {
				ivjJPanelU8.setVisible(false);
			} else {
				ivjJPanel2.setVisible(false);
			}
			for (JTextField textField : ivjJTFParams) {// 防止由于字符串过长，改变textField的大小
				textField.setPreferredSize(new Dimension(180, 21));
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
			r.height = 478 + iVerticalPixel;
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
				ivjJBCancel.setBounds(336, 406 + iVerticalPixel, 75, 22);
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
				ivjJBFolds[i].setPreferredSize(new Dimension(21, 21));
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
						.getStringResource("miufo1000814")); // 完成
				ivjJBNext.setBounds(230, 406 + iVerticalPixel, 75, 22);
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
				ivjJBRefer.setBounds(330, 250, 75, 22);
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
				ivjJLabel5.setBounds(17, 48 + iVerticalPixel, 70, 16);
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
				ivjJLabel6.setBounds(8, 50, 76, 16);
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
				ivjJLabel61.setBounds(8, 230, 76, 16);
				ivjJLabel61.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabel61;
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
				ivjJPanel2.setBounds(20, 7, 380, 300 + iVerticalPixel);
				ivjJPanel2.setVisible(false);
				ivjJPanel2.setRequestFocusEnabled(false);
				// 函数格式
				getJPanel2().add(getJLabel4(), getJLabel4().getName());
				getJPanel2().add(getJScrollPaneFuncFormat(),
						getJScrollPaneFuncFormat().getName());

				// 函数功能
				getJPanel2().add(getlbFuncNote(), getlbFuncNote().getName());
				getJPanel2().add(getJScrollPaneFuncNote(),
						getJScrollPaneFuncNote().getName());

				// 参数说明
				getJPanel2().add(getJLabel5(), getJLabel5().getName());
				getJPanel2().add(getJScrollPaneParamDes(),
						getJScrollPaneParamDes().getName());

				// 无用横条
				// getJPanel2().add(getJTextField2(),
				// getJTextField2().getName());
				// 参数表
				getJPanel2().add(getJScrollPanelParam(),
						getJScrollPanelParam().getName());

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJPanel2;
	}

	/**
	 * 返回 JPanel3 特性值。
	 * 
	 * @return javax.swing.JPanel 函数内容及出错信息面板
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getJPanel3() {
		if (ivjJPanel3 == null) {
			try {
				ivjJPanel3 = new UIPanel();
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setLayout(null);
				ivjJPanel3.setBounds(25, 304 + iVerticalPixel, 370, 86);
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
				ivjJPanelU8.setBounds(20, 4, 389, 350);
				// ivjJPanelU8.setBounds(10, 4, 389, 130);
				ivjJPanelU8.setVisible(false);
				// 函数功能
				ivjJPanelU8.add(getlbFuncNoteU8(), getlbFuncNote().getName());
				ivjJPanelU8.add(getJScrollPaneFuncNoteU8(),
						getJScrollPaneFuncNoteU8().getName());

				// 函数格式
				getJPanelU8().add(getJLabel6(), getJLabel6().getName());
				getJPanelU8().add(getJScrollPane3(),
						getJScrollPane3().getName());

				// getJPanelU8().add(getJLabel61(), getJLabel61().getName());
				// getJPanelU8().add(getjTFU8Input(),
				// getjTFU8Input().getName());
				// getJPanelU8().add(getJBRefer(), getJBRefer().getName());
				getJPanelU8().add(getU8ParamPanel());

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

	private javax.swing.JScrollPane getJScrollPane3() {
		if (ivjJScrollPane3 == null) {
			try {
				ivjJScrollPane3 = new UIScrollPane();
				ivjJScrollPane3.setName("JScrollPane3");
				ivjJScrollPane3.setBounds(88, 50, 280, 177);
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
	private javax.swing.JScrollPane getJScrollPaneParamDes() {
		if (ivjJScrollPaneFuncDes == null) {
			try {
				ivjJScrollPaneFuncDes = new UIScrollPane();
				ivjJScrollPaneFuncDes.setName("JScrollPaneFuncDes");
				ivjJScrollPaneFuncDes.setBounds(88, 48 + iVerticalPixel, 280,
						87);
				ivjJScrollPaneFuncDes.setViewportView(getJTAParamDes());
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

	private JPanel getU8ParamPanel() {
		if (u8ParamPanel == null) {
			try {
				u8ParamPanel = new UIPanel();
				u8ParamPanel.setLayout(new GridBagLayout());
				u8ParamPanel.setBounds(8, 230, 370, 100);
				u8ParamPanel
						.setBorder(new TitledBorder(null, StringResource
								.getStringResource("miufo1000824"),
								TitledBorder.LEFT, TitledBorder.TOP, new Font(
										"dialog", 0, 14), Color.BLUE));
				GridBagConstraints gridBagCon = new GridBagConstraints();
				gridBagCon.gridx = 0;
				gridBagCon.gridy = 0;
				gridBagCon.insets = new Insets(2, 2, 2, 2);
				u8ParamPanel.add(getjTFU8Input(), gridBagCon);
				gridBagCon.gridx = 1;
				gridBagCon.gridy = 0;
				u8ParamPanel.add(getJBRefer(), gridBagCon);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return u8ParamPanel;
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
				ivjJScrollPanelParam.setBounds(6, 149 + iVerticalPixel, 370,
						146);
				ivjJScrollPanelParam.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjJScrollPanelParam.setBorder(new TitledBorder(null,
						StringResource.getStringResource("miufo1000825"),
						TitledBorder.LEFT, TitledBorder.TOP, new Font("dialog",
								0, 14), Color.BLUE));

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
				ivjJTAFormulaErr.setBounds(7, 36, 361, 50);
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
	private javax.swing.JTextArea getJTAParamDes() {
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
				ivjJTAU8Info.setBackground(getUfoDialogContentPane()
						.getBackground());
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
				ivjJTextField2.setBounds(13, 142 + iVerticalPixel, 353, 2);
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
				// ivjjTFU8Input.setSingleQuoteInputEnabled(true);
				ivjjTFU8Input.setName("jTFU8Input");
				ivjjTFU8Input.setPreferredSize(new Dimension(250, 23));
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

				// getUfoDialogContentPane().add(getJTextField1(),
				// getJTextField1().getName());

				// iufo函数界面
				getUfoDialogContentPane().add(getJPanel2(),
						getJPanel2().getName());
				// 函数内容及出错信息组件
				getUfoDialogContentPane().add(getJPanel3(),
						getJPanel3().getName());

				// 区域参照界面组件
				getUfoDialogContentPane().add(getJTFSelArea(),
						getJTFSelArea().getName());
				getUfoDialogContentPane().add(getJBFold(),
						getJBFold().getName());

				// 外部函数界面，与getJPanel2互斥显示
				getUfoDialogContentPane().add(getJPanelU8(),
						getJPanelU8().getName());

				getUfoDialogContentPane().add(getJBNext(),
						getJBNext().getName());
				getUfoDialogContentPane().add(getJBCancel(),
						getJBCancel().getName());

				// user code begin {1}
				ivjUfoDialogContentPane.addFocusListener(this);
				//
				if (m_FuncName != null) {
					UfoFuncInfo m_FuncInfo = m_ufoFuncList.getFuncInfo(
							m_FuncName.getID(), m_FuncName.getName());
					ivjJTAFuncInfo.setText(m_FuncInfo.getFuncDescription());
					ivjJTAFuncInfoU8.setText(m_FuncInfo.getFuncDescription());
					ivjJTAFuncDes.setText(m_ufoFuncList.getParamDescription(
							m_FuncName.getID(), m_FuncName.getName()));
					ivjJTAFuncFormat.setText(m_ufoFuncList.getFuncFormat(
							m_FuncName.getID(), m_FuncName.getName()));
				} else {

					ivjJTAFuncInfo.setText("");
					ivjJTAFuncInfoU8.setText("");
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
			setSize(432, 478 + iVerticalPixel);
			setModal(false);
			setResizable(false);
			setContentPane(getUfoDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		setLocationRelativeTo(this);

		ivjJBCancel.setNextFocusableComponent(ivjJBNext);
		ivjJBNext.setNextFocusableComponent(ivjJBCancel);

		nextAction();

		// getJBRefer().setVisible(true);
		// 帮助
		try {
			javax.help.HelpBroker hb = ResConst.getHelpBroker();// UfoPublic.getHelpBroker(false);
			if (hb != null) {
				hb.enableHelpKey(getContentPane(), "TM_Data_Exp_Wizard1", null);
				javax.help.CSH.setHelpIDString(getJPanel2(),
						"TM_Data_Exp_Wizard2");
			}
		} catch (Exception ex) {
			// 吃掉无法加载帮助产生的异常,20040429 wupeng
			AppDebug.debug(ex);// @devTools AppDebug.debug(ex);
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

	private void myDispatchEvent(AWTEvent event) {
		if (event instanceof ActiveEvent) {
			((ActiveEvent) event).dispatch();
		} else if (event.getSource() instanceof Component) {
			((Component) event.getSource()).dispatchEvent(event);
		} else if (event.getSource() instanceof MenuComponent) {
			((MenuComponent) event.getSource()).dispatchEvent(event);
		}
	}

	public void setArea(IArea area) {
		m_area = area;
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
			if (isAddFoldBtn(str[i]))
				ivjJBFolds[i].setVisible(true);
		}
		setTextFieldFocus(num);
	}

	/**
	 * 根据参数名称判断是否添加ivjJBFolds
	 * 
	 * @param strParaName：参数名称
	 * @return
	 */
	private boolean isAddFoldBtn(String strParaName) {
		if (strParaName == null || strParaName.trim().length() == 0)
			return false;
		if (strParaName.indexOf(StringResource
				.getStringResource("miufopublic437")) >= 0
				|| strParaName.indexOf(StringResource
						.getStringResource("uiufofunc226")) >= 0)
			return false;
		else
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
			ivjJTFFormula.setNextFocusableComponent(ivjJBNext);

			ivjJBNext.setNextFocusableComponent(ivjJBCancel);
			ivjJBCancel.setNextFocusableComponent(ivjJTFParams[0]);
			ivjJTFParams[0].setNextFocusableComponent(ivjJTFParams[1]);
		} else if (num == 1) {
			ivjJTFParams[0].requestFocus();
			ivjJTFFormula.setNextFocusableComponent(ivjJBNext);

			ivjJBNext.setNextFocusableComponent(ivjJBCancel);
			ivjJBCancel.setNextFocusableComponent(ivjJTFParams[0]);
			ivjJTFParams[0].setNextFocusableComponent(ivjJTFFormula);
		} else if (num == 0) {
			ivjJBNext.requestFocus();
		}
	}

	/**
	 * 实现区域参照。 创建日期：(2001-1-4 15:01:43)
	 */
	public void show() {
		super.show();
		EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();

		while (true) {
			try {
				AWTEvent evt = eq.getNextEvent();
				if (evt.getSource() == this
						&& evt.getID() == WindowEvent.WINDOW_CLOSING) {
					myDispatchEvent(evt);
					break;
				} else if (evt.getSource() instanceof RootPaneContainer
						&& evt.getID() == WindowEvent.WINDOW_CLOSING) {
					Toolkit.getDefaultToolkit().beep();
				} else if (evt.getID() == MouseEvent.MOUSE_PRESSED) {
					MouseEvent mevt = (MouseEvent) evt;
					// 右键
					if ((mevt.getModifiers() & InputEvent.BUTTON3_MASK) == 0) {
						Point p = getPointToScreen(mevt); // 转换到相对于屏幕的坐标
						if (getBounds().contains(p)) {
							// 如果点击对话框内消息正常处理
							myDispatchEvent(mevt);
						} else if (mevt.getSource() instanceof RootPaneContainer) {
							if (ivjJBNext.getText().equals(
									StringResource
											.getStringResource("miufo1000814"))
									&& m_FocusPlace != -1) // "完 成"
							{
								// 如果步骤，则可选取区域
								Rectangle mm = super.getViewToScreenSize(mevt);
								if (mm.contains(p)) {
									if (!getIsFold()) {
										m_autoFold = true;
										fold(true);
									}
									myDispatchEvent(mevt);
									String strAreaName = getViewAreaName(mevt); // 得到视图选中区域
									ivjJTFSelArea.setText(strAreaName);
								} else
									Toolkit.getDefaultToolkit().beep();
							} else
								Toolkit.getDefaultToolkit().beep();
						}

						else if (mevt.getSource() instanceof JFrame) {
							JFrame jf = (JFrame) mevt.getSource();
							if (jf
									.getTitle()
									.indexOf(
											StringResource
													.getStringResource("miufopublic252")) > 0) // "帮助"
							{
								myDispatchEvent(mevt);
							} else
								Toolkit.getDefaultToolkit().beep();
						} else
							Toolkit.getDefaultToolkit().beep();
					} else
						Toolkit.getDefaultToolkit().beep();
				} else if (evt.getID() == MouseEvent.MOUSE_DRAGGED) {
					if (evt.getSource() instanceof RootPaneContainer) {
						myDispatchEvent(evt);

						if (ivjJBNext.getText().equals(
								StringResource
										.getStringResource("miufo1000814"))) // "完
						// 成"
						{
							String strAreaName = getViewAreaName(evt);
							ivjJTFSelArea.setText(strAreaName);
						}
					}
					if (evt.getSource() == this)
						myDispatchEvent(evt);
				} else if (evt.getID() == MouseEvent.MOUSE_RELEASED) {
					MouseEvent mevt = (MouseEvent) evt;
					// 右键
					if ((mevt.getModifiers() & InputEvent.BUTTON3_MASK) == 0) {
						// 鼠标抬起，是自动折叠则自动恢复
						if (evt.getSource() instanceof Component) {
							myDispatchEvent(evt);
							if (getIsFold() && m_autoFold)
								fold(false);
						}
					}
				} else {
					try {
						myDispatchEvent(evt);
					} catch (NullPointerException e) {
						// add by zzl 2005.8.17 这里添加例外捕获,是为了解决公式向导中输入中文字符时出现的错误.
					}
				}
			} catch (InterruptedException ie) {
			}
		}
	}

	/**
	 * 错误信息。 创建日期：(00-11-28 14:58:24)
	 */
	private void showmessage(String errs) {
		// JOptionPane.showMessageDialog(null,errs,"用友软件集团",JOptionPane.INFORMATION_MESSAGE);
		getJTAErrMsg().setText(errs);
		ivjJTFParams[0].requestFocus();
		ivjJTFParams[0].selectAll();
	}

	private void setFunc(UfoSimpleObject function) {
		m_FuncName = function;
		if (function != null)
			m_SelFuncName = function.getName();

	}

	private javax.swing.JScrollPane getJScrollPaneFuncNote() {
		if (ivjJScrollPaneFunNote == null) {
			try {
				ivjJScrollPaneFunNote = new UIScrollPane();
				ivjJScrollPaneFunNote.setName("JScrollPaneFuncFormat");
				ivjJScrollPaneFunNote.setBounds(88, 48, 280, 40);
				ivjJScrollPaneFunNote.setViewportView(getJTAFuncInfo());
				// user code begin {1}
				ivjJScrollPaneFunNote.setRequestFocusEnabled(false);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPaneFunNote;
	}

	private javax.swing.JTextArea getJTAFuncInfo() {
		if (ivjJTAFuncInfo == null) {
			try {
				ivjJTAFuncInfo = new UITextArea();
				ivjJTAFuncInfo.setName("JTAFuncInfo");

				ivjJTAFuncInfo.setBounds(88, 48, 280, 16);
				;
				ivjJTAFuncInfo.setEditable(false);
				ivjJTAFuncInfo.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjJTAFuncInfo.setBackground(ivjJPanel2.getBackground());
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

	private javax.swing.JScrollPane getJScrollPaneFuncNoteU8() {
		if (ivjJScrollPaneFunNoteU8 == null) {
			try {
				ivjJScrollPaneFunNoteU8 = new UIScrollPane();
				ivjJScrollPaneFunNoteU8.setName("JScrollPaneFuncFormat");
				ivjJScrollPaneFunNoteU8.setBounds(88, 6, 280, 40);
				ivjJScrollPaneFunNoteU8.setViewportView(getJTAFuncInfoU8());
				// user code begin {1}
				ivjJScrollPaneFunNoteU8.setRequestFocusEnabled(false);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPaneFunNoteU8;
	}

	private javax.swing.JTextArea getJTAFuncInfoU8() {
		if (ivjJTAFuncInfoU8 == null) {
			try {
				ivjJTAFuncInfoU8 = new UITextArea();
				ivjJTAFuncInfoU8.setName("JTAFuncInfoU8");

				ivjJTAFuncInfoU8.setBounds(88, 48, 280, 16);
				;
				ivjJTAFuncInfoU8.setEditable(false);
				ivjJTAFuncInfoU8.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjJTAFuncInfoU8.setBackground(ivjJPanel2.getBackground());
				ivjJTAFuncInfoU8.setLineWrap(true);
				ivjJTAFuncInfoU8.addFocusListener(this);

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTAFuncInfoU8;
	}

	private javax.swing.JLabel getlbFuncNoteU8() {
		if (lbFuncNoteU8 == null) {
			try {
				lbFuncNoteU8 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				lbFuncNoteU8.setName("JLabel6funnoteU8");
				lbFuncNoteU8.setText(StringResource
						.getStringResource("miufo1000817")); // "函数功能："
				lbFuncNoteU8.setBounds(8, 6, 76, 16);
				lbFuncNoteU8.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return lbFuncNoteU8;
	}

	private javax.swing.JLabel getlbFuncNote() {

		if (lbFuncNote == null) {
			try {
				lbFuncNote = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				lbFuncNote.setName("JLabel6funnote");
				lbFuncNote.setText(StringResource
						.getStringResource("miufo1000817")); // "函数功能："
				lbFuncNote.setBounds(17, 48, 70, 16);
				lbFuncNote.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return lbFuncNote;
	}

	public static String getMQFuncParam(Container objContainer,
			String strFuncName, IContext context) {
		try {
			MeasureQueryRefDlg dlg = new MeasureQueryRefDlg(objContainer,
					context);
			// dlg.setModal(true);
			// dlg.pack();
			// dlg.setLocationRelativeTo(null);
			dlg.showModal();

			if (dlg.getResult() == UfoDialog.ID_OK)
				if (dlg.getVO() != null)
					return dlg.getVO().getFuncString();

		} catch (NullPointerException ne) {
			com.ufsoft.report.util.UfoPublic.sendWarningMessage(StringResource
					.getStringResource("uiufodrv000014"), null);// "当前用户、当前任务下没有设置指标查询，请先设置指标查询后再使用本向导！"
			return null;
		}
		return null;
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

	private AbsFmlExecutor m_fmlExecutor = null;

	public ICalcEnv getCalcEnv() {
		if (m_fmlExecutor == null) {
			return null;
		}
		return m_fmlExecutor.getExecutorEnv();
	}

	protected AbsFmlExecutor getFmlExecutor() {
		return m_fmlExecutor;
	}

	protected void setFmlExecutor(AbsFmlExecutor fmlExecutor) {
		m_fmlExecutor = fmlExecutor;
	}

}
