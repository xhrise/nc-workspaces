package com.ufsoft.report.fmtplugin.formula;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITextArea;
import nc.util.iufo.pub.UFOString;

import com.ufida.dataset.IContext;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.fmtplugin.formula.ui.FormulaEditorArea;
import com.ufsoft.report.fmtplugin.formula.ui.FunctionPanel;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.script.base.AbsFmlExecutor;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.script.function.UfoFuncInfo;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IArea;
import com.ufsoft.table.TableUtilities;

/**
 * 公式编辑对话框基类定义
 * 
 * @author zhaopq
 * @created at 2009-2-23,上午10:39:09
 */
public abstract class CommonFmlEditDlg extends UfoDialog implements
		ActionListener, ChangeListener {
	private JTextField areaField;

	private JPanel jContentPane;

	/** 公式编辑页签 */
	private UITabbedPane formulaTabbedPane;

	/** 函数列表页签 */
	private UITabbedPane functionTabbedPane;

	protected JPanel topPanel;

	protected JPanel btnPanel;

	private IArea[] areas;

	/** 公式验证信息区 */
	private JTextArea frmlChckInfoTextArea;

	private AbsFmlExecutor m_fmlExecutor;

	/** 按钮组:确定 */
	private JButton btnOK;

	/** 按钮组:取消 */
	private JButton btnCancel;

	/** 按钮组:高级 */
	private JButton btnHight;

	/** 按钮组:公式验证 */
	private JButton btnValidate;

	private JButton btnFold;

	protected int pos = -1;// 返回光标所在的位置

	/** 存放公式编辑区对象 */
	protected List<FormulaEditorArea> formulaEditorList = new ArrayList<FormulaEditorArea>();

	/** 存放函数操作面板 */
	protected List<FunctionPanel> funcPanelList = new ArrayList<FunctionPanel>();

	private CellsPane cellsPane;

	public CommonFmlEditDlg(CellsPane cellsPane, AbsFmlExecutor fmlExecutor) {
		this(cellsPane, fmlExecutor, MultiLang.getString("miufo1002179"));// 公式向导;
	}

	public CommonFmlEditDlg(CellsPane cellsPane, AbsFmlExecutor fmlExecutor,
			String title) {
		super(cellsPane, title);
		this.cellsPane = cellsPane;
		m_fmlExecutor = fmlExecutor;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				if (formulaEditorList.size() > 0) {
					formulaEditorList.get(0).requestFocus();
				}
			}
		});

		getRootPane().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		getRootPane().registerKeyboardAction(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						showClassInfo();
					}
				},
				KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK
						+ InputEvent.SHIFT_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	private void showClassInfo(){
		JOptionPane.showMessageDialog(this, this.getClass().getName());
	}

	protected void initialize() {
		this.setSize(625, 530);
		this.setContentPane(getJContentPane());
		setLocationRelativeTo(this);
	}

	protected javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.setSize(605, 570);

			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new GridBagLayout());
			GridBagConstraints gridBagCon = new GridBagConstraints();
			gridBagCon.gridx = 0;
			gridBagCon.gridy = 0;
			gridBagCon.weightx = 1;
			gridBagCon.weighty = 1;
			gridBagCon.anchor = GridBagConstraints.CENTER;
			gridBagCon.fill = GridBagConstraints.BOTH;
			gridBagCon.insets = new Insets(1, 1, 1, 1);
			centerPanel.add(getFormulaTabbedPane(), gridBagCon);
			gridBagCon.gridx = 0;
			gridBagCon.gridy = 1;
			centerPanel.add(getFormulaCheckPanel(), gridBagCon);
			gridBagCon.gridx = 0;
			gridBagCon.gridy = 2;
			centerPanel.add(getFunctionTabbedPane(), gridBagCon);

			jContentPane.add(centerPanel, BorderLayout.CENTER);
			jContentPane.add(getBtnPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	public UITabbedPane getFormulaTabbedPane() {
		if (formulaTabbedPane == null) {
			formulaTabbedPane = new UITabbedPane();
			formulaTabbedPane.setPreferredSize(new Dimension(577, 180));
			formulaTabbedPane.setBounds(19, 35, 572, 151);
			addFormulaTab(formulaTabbedPane);
		}
		return formulaTabbedPane;
	}

	protected abstract void addFormulaTab(UITabbedPane tabbedPane);

	/**
	 * 添加公式编辑页签
	 * 
	 * @param formulaTabbedPane
	 *            页签所在的面板
	 */
	protected void addTab(UITabbedPane formulaTabbedPane,
			FormulaEditorArea textEditor, String text) {
		UIScrollPane spane = new UIScrollPane();
		spane.setViewportView(textEditor);
		formulaTabbedPane.addTab(text, null, spane, null);
		formulaEditorList.add(textEditor);
	}

	protected UITabbedPane getFunctionTabbedPane() {
		if (functionTabbedPane == null) {
			functionTabbedPane = new UITabbedPane();
			functionTabbedPane.setPreferredSize(new Dimension(577, 210));
			addFunctionTab(functionTabbedPane);
		}
		return functionTabbedPane;
	}

	/** 添加函数面板页签 */
	protected abstract void addFunctionTab(UITabbedPane tabbedPane);

	protected JPanel getTopPanel() {
		if (topPanel == null) {
			topPanel = new JPanel();
			topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			topPanel.add(getAreaField());
			topPanel.add(getBtnFold());
			topPanel.add(new UILabel("="));
		}
		return topPanel;
	}

	protected JTextField getAreaField() {
		if (areaField == null) {
			areaField = new JTextField();
			areaField.setPreferredSize(new Dimension(80, 21));
			areaField.setHorizontalAlignment(JTextField.CENTER);
			areaField.registerKeyboardAction(new ActionListener() {
				/**
				 * miufo00020=对不起,区域位置无效,请重新输入 miufo00021=函数内容发生变化,是否先保存再继续?
				 */
				public void actionPerformed(ActionEvent e) {
					AreaPosition area = TableUtilities
							.getAreaPosByString(getAreaField().getText());
					if (area == null) {
						UfoPublic.sendWarningMessage(MultiLang
								.getString("miufo00020"), getParent());
						areaField.requestFocus();
						return;
					}
					if (isFormulaValueChanged()) {
						int iUserSel = UfoPublic.showConfirmDialog(getParent(),
								MultiLang.getString("miufo00021"), "",
								JOptionPane.YES_NO_OPTION);
						setFormulaValueChanged(false);
						if (iUserSel == JOptionPane.YES_OPTION) {
							if (checkFormula())
								addFormula();
							else
								return;
						}
					}
					addFormulaToTextArea();
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
					JComponent.WHEN_FOCUSED);

		}
		return areaField;
	}

	public abstract JPanel getBtnPanel();

	protected JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton();
			btnOK.setBounds(220, 465, 75, 22);
			btnOK.setText(MultiLang.getString("miufo1000790")); // "确 定"
			btnOK.addActionListener(this);
			btnOK.registerKeyboardAction(this, KeyStroke.getKeyStroke(
					KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
		}
		return btnOK;
	}

	protected JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel.setBounds(350, 465, 75, 22);
			btnCancel.setText(MultiLang.getString("miufo1000274")); // "取 消"
			btnCancel.addActionListener(this);
			btnCancel.registerKeyboardAction(this, KeyStroke.getKeyStroke(
					KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
		}
		return btnCancel;
	}

	private JButton getBtnFold() {
		if (btnFold == null) {
			btnFold = new UIButton();
			btnFold.setIcon(ResConst.getImageIcon("reportcore/up.gif"));
			btnFold.setPreferredSize(new Dimension(21, 21));
			btnFold.addActionListener(new ActionListener() {
				/**
				 * miufo00021=函数内容发生变化,是否先保存再继续?
				 */
				public void actionPerformed(ActionEvent e) {
					if (isFormulaValueChanged()) {
						int iUserSel = UfoPublic.showConfirmDialog(getParent(),
								MultiLang.getString("miufo00021"), "",
								JOptionPane.YES_NO_OPTION);
						setFormulaValueChanged(false);
						if (iUserSel == JOptionPane.YES_OPTION) {
							if (checkFormula())
								addFormula();
							else
								return;
						}
					}
					setVisible(false);
					FuncAreaSelectDlg areaSelectDlg = new FuncAreaSelectDlg(
							getParent(), getCellsModel());
					areaSelectDlg.setModal(false);
					areaSelectDlg.show();
					if (areaSelectDlg.getResult() == CommonFmlEditDlg.ID_OK) {
						String strSelectArea = areaSelectDlg.getSelectArea();
						if (!UFOString.isEmpty(strSelectArea)) {
							IArea selArea = AreaPosition
									.getInstance(strSelectArea);
							IArea[] selAreas = new AreaPosition[] { (AreaPosition) selArea };
							setArea(selAreas);
							loadFormula(selAreas[0]);
						}
					}
					setVisible(true);
				}
			});
		}
		return btnFold;
	}

	/**
	 * 切换区域时重新加载新区域的公式
	 * 
	 * @param anchorCell
	 *            新区域开始位置
	 */
	protected void loadFormula(IArea anchorCell){
	}

	/**
	 * @i18n miufo00022=高级
	 */
	public JButton getBtnHeight() {
		if (btnHight == null) {
			btnHight = new UIButton(MultiLang.getString("miufo00022"));
			btnHight.addActionListener(this);
		}
		return btnHight;
	}

	public JPanel getFormulaCheckPanel() {
		JPanel funcErrorPanel = new JPanel();
		funcErrorPanel.setPreferredSize(new Dimension(577, 26));
		funcErrorPanel.setLayout(new BorderLayout());
		funcErrorPanel.add(getFrmlChckInfoTextArea(), BorderLayout.CENTER);
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel1.add(getBtnValidate());
		funcErrorPanel.add(panel1, BorderLayout.EAST);
		return funcErrorPanel;
	}

	/**
	 * miufopublic384=公式验证 miufo00023=公式验证
	 */
	public JButton getBtnValidate() {
		if (btnValidate == null) {
			btnValidate = new JButton(MultiLang.getString("miufo00023"));
			btnValidate.setPreferredSize(new Dimension(80, 22));
			btnValidate.addActionListener(this);
		}
		return btnValidate;
	}

	public JTextArea getFrmlChckInfoTextArea() {
		if (frmlChckInfoTextArea == null) {
			frmlChckInfoTextArea = new UITextArea();
			frmlChckInfoTextArea.setEditable(false);
			frmlChckInfoTextArea.setForeground(java.awt.Color.red);
		}
		return frmlChckInfoTextArea;
	}

	/** 在公式验证信息栏中显示错误信息 */
	public void showErrorMessage(String errs) {
		getFrmlChckInfoTextArea().setForeground(Color.RED);
		getFrmlChckInfoTextArea().setText(errs);
		getCurrentFmlEditArea().requestFocus();
		getCurrentFmlEditArea().getFormulaEditor().selectAll();
	}

	/** 在公式验证信息栏中显示提示信息 */
	public void showMessage(String msg) {
		getFrmlChckInfoTextArea().setForeground(Color.BLACK);
		getFrmlChckInfoTextArea().setText(msg);
	}

	public int getCurrentFuncTabPanel() {
		return getFunctionTabbedPane().getSelectedIndex();
	}

	public void setCurrentFuncTabPanel(int currentFuncTabPanel) {
		getFunctionTabbedPane().setSelectedIndex(currentFuncTabPanel);
	}

	public int getCurrentFormulaTabPanel() {
		return getFormulaTabbedPane().getSelectedIndex();
	}

	public void setCurrentFormulaTabPanel(int currentFormulaTabPanel) {
		getFormulaTabbedPane().setSelectedIndex(currentFormulaTabPanel);
	}

	public int getCaretPos() {
		return pos;
	}

	public void setCaretPos(int i) {
		this.pos = i;
	} 

	/**
	 * 设置区域信息
	 * 
	 * @param area
	 *            选中的区域
	 */
	public void setArea(IArea[] areas) {
		if (areas != null && areas.length > 0) {
			this.areas = areas;
			getAreaField().setText(areas[0].toString());
		}
	}

	/**
	 * 返回公式定义区域
	 * 
	 * @return
	 */
	public IArea[] getArea() {
		if (areas == null)
			return areas = new IArea[0];
		return areas;
	}

	public AbsFmlExecutor getFmlExecutor() {
		return m_fmlExecutor;
	}

	public void setSelectedFunc(UfoSimpleObject category, UfoSimpleObject func) {
		getCurrentFunctionPanel().setSelectedFuncCategory(category);
		getCurrentFunctionPanel().setSelectedFunc(func);
	}

	public FunctionPanel getCurrentFunctionPanel() {
		int page = getFunctionTabbedPane().getSelectedIndex();
		return funcPanelList.get(page);
	}

	public UfoSimpleObject getSelectedCategory() {
		return getCurrentFunctionPanel().getSelectedFuncCategory();
	}

	public UfoSimpleObject getSelectedFunc() {
		return getCurrentFunctionPanel().getSelectedFunc();
	}

	// public UfoReport getReport() {
	// return m_ufoReport;
	// }

	/**
	 * 获取当前公式编辑JTextArea
	 */
	public FormulaEditorArea getCurrentFmlEditArea() {
		int page = getFormulaTabbedPane().getSelectedIndex();
		return formulaEditorList.get(page);
	}

	public boolean checkFormula() {
		for (FormulaEditorArea editor : formulaEditorList) {
			if (!editor.checkFormula(getAreaField().getText())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 添加公式到模型及公式链中
	 */
	public abstract boolean addFormula();

	/**
	 * 检查函数是否外部函数
	 * 
	 * @param funcList
	 * @param strFunName
	 * @return
	 */
	public boolean isOuterFunc(FuncListInst funcList, String strFunName) {
		return false;
	}

	/**
	 * 返回外部函数信息
	 * 
	 * @param funcList
	 * @param strFuncName
	 * @return
	 */
	public String getExtFuncInfo(FuncListInst funcList, String strFuncName) {
		return null;
	}

	/**
	 * 根据选择的区域向公式编辑框中添加公式内容
	 */
	public void addFormulaToTextArea() {

	}

	/**
	 * 处理公式编辑对话框按钮操作
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnCancel()) {
			setResult(ID_CANCEL);
			close();
		} else if (e.getSource() == getBtnValidate()) {
			String formula = getCurrentFmlEditArea().getFormulaEditor().getText();
			if (!UFOString.isEmpty(formula)) {
				checkFormula();
			} else {
				showErrorMessage(MultiLang.getString("miufo00200065"));
			}
		}
	}

	public void stateChanged(ChangeEvent e) {
	}

	public static void addString(UfoFuncInfo funcInfo, JTextArea areaDest) {
		if (funcInfo != null) {

			int insertPos = areaDest.getCaretPosition();
			StringBuffer buf = new StringBuffer();
			buf.append(funcInfo.getFuncName());
			buf.append('(');
			int iRealtiveCaretPos = funcInfo.getParamLen() > 0 ? buf.length()
					: buf.length() + 1;
			for (int i = 0, size = funcInfo.getParamLen() - 1; i < size; i++)
				buf.append(',');
			buf.append(')');
			try {
				areaDest.insert(buf.toString(), insertPos);
			} catch (Exception e1) {
				areaDest.append(buf.toString());
			}
			areaDest.setCaretPosition(insertPos + iRealtiveCaretPos);
		}
	}

	/**
	 * 获得所有函数的名称
	 * 
	 * @return
	 */
	public String[] getAllFuncName() {
		FuncListInst funcList = m_fmlExecutor.getFuncListInst();
		if (funcList == null)
			return null;
		UfoSimpleObject[] allModules = funcList.getCatList();
		List<String> funcNameList = new ArrayList<String>();
		for (UfoSimpleObject module : allModules) {
			if (module == null)
				continue;
			UfoSimpleObject[] m_FuncNameList = funcList.getFuncList(module
					.getID());
			if (m_FuncNameList == null || m_FuncNameList.length == 0)
				continue;
			for (UfoSimpleObject simpleObj : m_FuncNameList) {
				if (simpleObj != null) {
					funcNameList.add(simpleObj.getName());
				}
			}
		}
		return funcNameList.toArray(new String[funcNameList.size()]);
	}

	/** 公式编辑器内容是否改变 */
	protected boolean isFormulaValueChanged() {
		for (FormulaEditorArea editor : formulaEditorList) {
			if (editor.getFormulaEditor().isChanged()) {
				return true;
			}
		}
		return false;
	}

	protected void setFormulaValueChanged(boolean changed) {
		for (FormulaEditorArea editor : formulaEditorList) {
			editor.getFormulaEditor().setChanged(false);
		}
	}

	public String getFormulaFromCurrentFmlEditArea() {
		return getCurrentFmlEditArea().getFormulaEditor().getText();
	}

	/** 是否有通过验证的公式 */
	public boolean hasCheckedFormula() {
		for (FormulaEditorArea editor : formulaEditorList) {
			if (editor.hasCheckedFormula()) {
				return true;
			}
		}
		return false;
	}

	public List<FormulaEditorArea> getFormulaEditorList() {
		return formulaEditorList;
	}

	public void setFormulaEditorList(
			List<FormulaEditorArea> formulaEditorList) {
		this.formulaEditorList = formulaEditorList;
	}

	public CellsModel getCellsModel() {
		return cellsPane.getDataModel();
	}

	protected void updateFormulaContent(String strWizard) {
		String formula = getCurrentFmlEditArea().getFormulaEditor().getText();
		// 得到单元公式,再光标处插入
		int nCaretPos = getCaretPos();
		if (nCaretPos < 0 || nCaretPos >= formula.length()) {
			nCaretPos = formula.length();
		}
		StringBuilder newFormula = new StringBuilder(formula);
		newFormula.insert(nCaretPos, strWizard);

		getCurrentFmlEditArea().getFormulaEditor().setText(newFormula.toString());
	}

	/**
	 * 打开函数编辑向导，并将函数向导的最后结果拼接到公式面板，该方法仅当双击某函数时被调用。
	 * 
	 * @param function
	 *            函数
	 * @param fmlArea
	 *            公式所在的单元格区
	 * @see com.ufsoft.report.fmtplugin.formula.ui.FunctionPanel
	 */
	public void openFunctionWizard(UfoSimpleObject function, IArea fmlArea) {
		AreaFunctionReferDlg funcWizardDlg = new AreaFunctionReferDlg(getCellsPane(), function, getFmlExecutor().getFuncListInst());
		funcWizardDlg.setModal(isRefDlg());
		funcWizardDlg.setArea(fmlArea);
		funcWizardDlg.setFmlExecutor(getFmlExecutor());
		funcWizardDlg.show();
		if (funcWizardDlg.getResult() == CommonFmlEditDlg.ID_OK) {
			updateFormulaContent(funcWizardDlg.getCellFunc());
		}
	}
	
	public boolean isRefDlg(){
		Object refObj = getCellsPane().getContext().getAttribute(ReportContextKey.AREA_FORMULA_DIALOG_REF);
		if(refObj == null){
			return false;
		}
		return Boolean.parseBoolean(refObj.toString());
	}
	
	public CellsPane getCellsPane() {
		return cellsPane;
	}

	public void setCellsPane(CellsPane cellsPane) {
		this.cellsPane = cellsPane;
	}
	
	public IContext getContext(){
		return getCellsPane() != null ? getCellsPane().getContext() : null;
	}
}
