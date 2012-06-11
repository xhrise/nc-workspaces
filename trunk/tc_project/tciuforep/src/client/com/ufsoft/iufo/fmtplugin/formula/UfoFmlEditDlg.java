package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UIDialogListener;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.dsmanager.DatasetTree;
import nc.ui.pub.querytoolize.AbstractWizardListPanel;
import nc.ui.pub.querytoolize.WizardContainerDlg;
import nc.vo.iufo.authorization.IAuthorizeTypes;
import nc.vo.pub.dsmanager.DataSetDesignObject;

import com.ufida.dataset.Context;
import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.parser.IFuncDriver;
import com.ufsoft.iufo.util.parser.IFuncEditTypeSpecial;
import com.ufsoft.iufo.util.parser.IFuncEditTypeSpecial2;
import com.ufsoft.iufo.util.parser.UfoParseException;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.fmtplugin.formula.CommonFmlEditDlg;
import com.ufsoft.report.fmtplugin.formula.ui.FormulaEditor;
import com.ufsoft.report.fmtplugin.formula.ui.FormulaEditorArea;
import com.ufsoft.report.fmtplugin.formula.ui.FunctionPanel;
import com.ufsoft.report.fmtplugin.formula.ui.OperateSignPanel;
import com.ufsoft.report.fmtplugin.formula.ui.SearchFuncPanel;
import com.ufsoft.script.base.AbsFmlExecutor;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.script.expression.UfoCmdLet;
import com.ufsoft.script.expression.UfoExpr;
import com.ufsoft.script.extfunc.DataSetFunc;
import com.ufsoft.script.extfunc.DataSetFuncDriver;
import com.ufsoft.script.function.ExtFunc;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.script.function.UfoFuncList;
import com.ufsoft.script.util.FormulaCellExtPropertyUtil;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IArea;
import com.ufsoft.table.UFOTable;

/**
 * UFO��ʽ�༭�Ի����ඨ��
 * 
 * @author zhaopq
 * @created at 2009-2-23,����10:39:09
 */
public class UfoFmlEditDlg extends CommonFmlEditDlg implements IUfoContextKey {
	private static final long serialVersionUID = -5177844003224667242L;

	/** һ�㺯���б�:���ڣ���ѧ���ַ����� */
	public static final int[] commonlyFuncAry = { UfoFuncList.DATEFUNC,
			UfoFuncList.MATHFUNC, UfoFuncList.STRFUNC };

	/** ���ú��� */
	public static final String[] oftenUseFuncAry = { "GETDATA", "K", "ZKEY",
			"MSUM", "MSELECT", "PTOTAL", "SUBSTR", "ZDW", "ZDATE", "ZDATEQC" };

	/** IUFO���������������ڼ���ʱ���͵�IDȡ�����Զ����ɵģ����Դ˴�ȡ��һ��ĺ������ͣ��������Դ��Ƚ� */
	public static final String[] iufoFuncAry = { "uiufofunc004",// ������
			"miufopublic442",// ����ȡֵ����
			"miufopublic141",// ָ���ѯ
			"uiufofunc101", // �ؼ��ֺ���
			"uiufofunc102",// ʱ��ؼ������Ժ���
			"uiufofunc104", // ָ�꺯��
			"uiufofunc133", // �ϲ�����ָ�꺯��
			"miufo1004079", // ���ݼ�����
			"uiufodrv000003",// ���ʺ���
			"uiufofunc145", // ��¼��Ϣ����
			"uiufofunc200", // ��������
			"miufo1004080" };// Ͷ�����ݺ���

	private JPanel btnPanel;

	private JButton btnEditHr;

	private JButton btnRefCellFormula;

	private JButton btnHight;

	private JCheckBox personalNoCheckBox = null;
	
	/** ��ʽ��Ԫ�Ƿ�����༭�Ŀ��Ʊ��� */
	private int iFormulaEditType = 0;

	/** ��ʾ�Ƿ��л���ҳǩ�����ڷ����������ӱ����������ܹ�ʽ */
	private boolean hasTotalTab;

	/** ���й�ʽ�ı��༭�� */
	private FormulaEditorArea publicFormulaTextArea;

	/** ˽�й�ʽ�ı��༭�� */
	private FormulaEditorArea personalFormulaTextArea;

	/** ���ܹ�ʽ�ı��༭�� */
	private FormulaEditorArea totalFormulaTextArea;

	/** ��ʽ�߼������� */
	private UfoFmlExecutor formulaExecutor;

	private CellsPane cellsPane;

	private IContext context;

	public UfoFmlEditDlg(CellsPane cellsPane, IContext context,
			AbsFmlExecutor fmlExecutor, boolean editable) {
		super(cellsPane, fmlExecutor);
		this.cellsPane = cellsPane;
		this.context = context;
		this.formulaExecutor = (UfoFmlExecutor) fmlExecutor;
		this.hasTotalTab = !formulaExecutor.isAnaRep();
		initialize(editable);
	}

	public void initialize(boolean editable) {
		super.initialize();
		if (editable) {
			getBtnEditHr().setVisible(false);
		} else {
			getFormulaTabbedPane().setSize(
					getFormulaTabbedPane().getWidth(),
					getFunctionTabbedPane().getHeight()
							+ getFormulaTabbedPane().getHeight());

			getBtnEditHr().setVisible(true);
		}
		
	}

	@Override
	protected JPanel getJContentPane() {
		JPanel result = super.getJContentPane();
		result.add(getTopPanel(), BorderLayout.NORTH);
		return result;
	}

	private JButton getBtnRefCellFormula() {
		if (btnRefCellFormula == null) {
			btnRefCellFormula = new UIButton();
			btnRefCellFormula.setText(StringResource
					.getStringResource("miufo1000905")); // "���õ�Ԫ��ʽ"
			addToolTipAuto(btnRefCellFormula);
			btnRefCellFormula.setVisible(false);
			btnRefCellFormula.addActionListener(this);
			btnRefCellFormula.registerKeyboardAction(this, KeyStroke
					.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
					JComponent.WHEN_FOCUSED);

		}
		return btnRefCellFormula;
	}

	/**
	 * @i18n miufohbbb00203=˽�й�ʽ��
	 */
	private JCheckBox getPersonalNoCheckBox(){
		if(personalNoCheckBox == null){
			personalNoCheckBox = new UICheckBox();
			personalNoCheckBox.setText(StringResource.getStringResource("miufohbbb00203"));
			personalNoCheckBox.addItemListener(new ItemListener(){

				public void itemStateChanged(ItemEvent e) {
					JCheckBox source = (JCheckBox)e.getSource();
					getPersonalFormulaTextArea().getFormulaEditor().setEditable(!source.isSelected());
					validateBtnEnabled(!source.isSelected());
				}
				
			});
		}
		return personalNoCheckBox;
	}
	
	/**
	 * miufofunc001=���й�ʽ miufo00661=˽�й�ʽ miufo1000910=���ܹ�ʽ
	 */
	@Override
	protected void addFormulaTab(UITabbedPane tabbedPane) {
		// @edit by wangyga at 2009-8-31,����09:23:18 ����5.6��ʽ�������޸ģ�
		//1:�¼���λ���Բ鿴�ϼ���λ�Ĺ��й�ʽ���������޸�
		//2:���ѡ���ϡ�˽�й�ʽ�ޡ� ���൱�ڴ˵�Ԫλ��û�й�ʽ
		// ��ӹ��й�ʽ�༭ҳǩ
		
		addTab(tabbedPane, getPublicFormulaTextArea(), StringResource
				.getStringResource("miufofunc001"));

		// ���˽�й�ʽ�༭ҳǩ
		addTab(tabbedPane, getPersonalFormulaTextArea(), StringResource
				.getStringResource("miufo00661"));

		if (IufoFormulalUtil.isCreateUnit(context)) {
			if (hasTotalTab) {
				// ��ӻ��ܹ�ʽ�༭ҳǩ
				addTab(tabbedPane, getTotalFormulaTextArea(), StringResource
						.getStringResource("miufo1000910"));
			}
		} else {
			if (nonPersonRight() && hasTotalTab) {
				// ��ӻ��ܹ�ʽ�༭ҳǩ
				addTab(tabbedPane, getTotalFormulaTextArea(), StringResource
						.getStringResource("miufo1000910"));
			}
		}

		tabbedPane.addChangeListener(this);
		
	}

	/*
	 * miufo00662=���ú��� miufo00663=һ�㺯�� miufo00664=IUFO���� ubiquery0050=ҵ����
	 * miufo00665=�������� miufo1003324=����
	 */
	@SuppressWarnings("serial")
	@Override
	public void addFunctionTab(UITabbedPane tabbedPane) {
		tabbedPane.addChangeListener(this);
		// ��ӳ��ú������
		FunctionPanel cyFuncPanel = new FunctionPanel(this) {
			@Override
			protected void initialize() {
				this.setLayout(new BorderLayout());
				this.add(getFuncNamePane(), BorderLayout.WEST);
				this.add(getFuncDescPane(), BorderLayout.CENTER);
			}

			@Override
			protected UfoSimpleObject[] getFuncs(UfoSimpleObject category) {
				List<UfoSimpleObject> result = new ArrayList<UfoSimpleObject>();
				FuncListInst funcList = formulaExecutor.getFuncListInst();
				if (funcList != null) {
					UfoSimpleObject[] simpleObj = funcList.getCatList();
					for (UfoSimpleObject funcTypeobj : simpleObj) {
						if (funcTypeobj == null)
							continue;
						UfoSimpleObject[] funcObjAry = funcList
								.getFuncList(funcTypeobj.getID());
						if (funcObjAry == null)
							continue;
						for (UfoSimpleObject funcObj : funcObjAry) {
							for (String strFuncName : oftenUseFuncAry) {
								if (funcObj != null
										&& strFuncName != null
										&& strFuncName
												.equals(funcObj.getName())) {
									result.add(funcObj);
								}
							}
						}
					}
				}
				return result.toArray(new UfoSimpleObject[0]);
			}

			@Override
			public UfoSimpleObject getSelectedFuncCategory() {
				return null;
			}

		};
		tabbedPane.addTab(StringResource.getStringResource("miufo00662"), null,
				cyFuncPanel, null);
		funcPanelList.add(cyFuncPanel);
		// ���һ�㺯�����
		FunctionPanel ybFuncPanel = new FunctionPanel(this) {

			@Override
			protected UfoSimpleObject[] getFuncCategory() {
				return getCommonlyFunc();
			}

		};
		tabbedPane.addTab(StringResource.getStringResource("miufo00663"), null,
				ybFuncPanel, null);
		funcPanelList.add(ybFuncPanel);
		// ���IUFO�������
		FunctionPanel iufoFuncPanel = new FunctionPanel(this) {

			@Override
			protected UfoSimpleObject[] getFuncCategory() {
				return getIufoFunc();
			}

		};
		tabbedPane.addTab(StringResource.getStringResource("miufo00664"), null,
				iufoFuncPanel, null);
		funcPanelList.add(iufoFuncPanel);
		// ���ҵ�������
		FunctionPanel ywFuncPanel = new FunctionPanel(this) {

			@Override
			protected UfoSimpleObject[] getFuncCategory() {
				return getOutFunc();
			}
            //�����û�ϰ�ߵ�˳�򣬲�����
			@Override
			protected UfoSimpleObject[] sortArray(UfoSimpleObject[] array) {
				return array;
			}
		};
		tabbedPane.addTab(StringResource.getStringResource("ubiquery0050"),
				null, ywFuncPanel, null);
		funcPanelList.add(ywFuncPanel);
		// ��Ӳ��������
		OperateSignPanel czfPanel = new OperateSignPanel(this);
		tabbedPane.addTab(StringResource.getStringResource("miufo00665"), null,
				czfPanel, null);
		funcPanelList.add(czfPanel);
		// ����������
		SearchFuncPanel searchPanel = new SearchFuncPanel(this);
		tabbedPane.addTab(StringResource.getStringResource("miufo1003324"),
				null, searchPanel, null);
		funcPanelList.add(searchPanel);
		
		if(!IufoFormulalUtil.isCreateUnit(context)){
			getFormulaTabbedPane().setSelectedIndex(1);
		}
	}

	@Override
	public boolean isOuterFunc(FuncListInst funcList, String strFunName) {
		/**
		 * �����ָ���ѯ�������򷵻�true ������ⲿע�ắ�������ֲ���IUFO2FuncDrive���򷵻�true ���෵��false
		 */

		if (funcList.getFuncID(strFunName) != UfoFuncList.EXTFUNCID) {
			// ��������ⲿע�ắ�����򷵻�false
			return false;
		}
		Object driver = funcList.getExtDriver(funcList
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

	@Override
	public String getExtFuncInfo(FuncListInst funcList, String strFuncName) {
		String strDriverName = funcList.getExtFuncDriver(strFuncName);
		IFuncDriver driver = null;

		try {
			driver = (IFuncDriver) funcList.getExtDriver(strDriverName);
		} catch (Exception e2) {

		}
		if (driver != null)
			return driver.getFuncForm(strFuncName);
		return "";
	}

	/*
	 * miufopublic368=�߼�
	 */
	public JButton getBtnHeight() {
		if (btnHight == null) {
			btnHight = new UIButton(StringResource
					.getStringResource("miufopublic368"));
			btnHight.addActionListener(this);
		}
		return btnHight;
	}

	@Override
	public JPanel getTopPanel() {
		JPanel topPanel = super.getTopPanel();
		topPanel.add(getBtnRefCellFormula());
		topPanel.add(getBtnEditHr());
		return topPanel;
	}

	public FormulaEditorArea getPublicFormulaTextArea() {
		if (publicFormulaTextArea == null) {
			publicFormulaTextArea = new FormulaEditorArea(this,
					getAllFuncName());
		}
		return publicFormulaTextArea;
	}

	public FormulaEditorArea getPersonalFormulaTextArea() {
		if (personalFormulaTextArea == null) {
			personalFormulaTextArea = new FormulaEditorArea(this,
					getAllFuncName()){
						private static final long serialVersionUID = 1L;
						@Override
						protected void initialize() {
							super.initialize();
							JPanel personalCheckBoxPanel = new UIPanel();
							personalCheckBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
							personalCheckBoxPanel.add(getPersonalNoCheckBox());
							add(personalCheckBoxPanel,BorderLayout.NORTH);
						}
				
			};
		}
		return personalFormulaTextArea;
	}

	public FormulaEditorArea getTotalFormulaTextArea() {
		if (totalFormulaTextArea == null) {
			totalFormulaTextArea = new FormulaEditorArea(this,
					getAllFuncName());
		}
		return totalFormulaTextArea;
	}

	private JButton getBtnEditHr() {
		if (btnEditHr == null) {
			btnEditHr = new UIButton();
			btnEditHr.setText(StringResource.getStringResource("miufo1000904")); // "�޸�..."
			btnEditHr.addActionListener(this);
			btnEditHr.registerKeyboardAction(this, KeyStroke.getKeyStroke(
					KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
		}
		return btnEditHr;
	}

	private boolean nonPersonRight() {
		return (Integer) context.getAttribute(FORMAT_RIGHT) >= IAuthorizeTypes.AU_REPDIR_TYPE_MODIFY;
	}

	private boolean getPersionalNoValue(){
		return getPersonalNoCheckBox().isSelected();
	}
	
	@Override
	public boolean addFormula() {
		boolean bSuccess = true;
		String strEditCellFormulaForPublic = null;
		if (IufoFormulalUtil.isCreateUnit(context)) {
			strEditCellFormulaForPublic = getPublicFormulaTextArea()
					.getCheckedFormula();
		}
		String strEditCellFormulaForPerson = getPersonalFormulaTextArea()
				.getCheckedFormula();
		String strEditTotalFormula = getTotalFormulaTextArea()
				.getCheckedFormula();

		for (IArea area : getArea()) {
			formulaExecutor.clearFormula(area, true);
			boolean bAddCellFml = false;
			try {
				StringBuffer showErrMessage = new StringBuffer();
				if (strEditCellFormulaForPublic != null) {
					bAddCellFml = formulaExecutor.addDbDefFormula(
							showErrMessage, area, strEditCellFormulaForPublic,
							null, true, true, false);
				}

				if (strEditCellFormulaForPerson != null) {
					bAddCellFml = formulaExecutor.addDbDefFormula(
							showErrMessage, area, strEditCellFormulaForPerson,
							null, true, false, false);
				}

				if ((strEditCellFormulaForPublic != null || strEditCellFormulaForPerson != null)
						&& bAddCellFml == false) {
					if (showErrMessage.length() > 0) {
						showErrorMessage(showErrMessage.toString());
					} else {
						showErrorMessage(StringResource
								.getStringResource("miufo1001713"));
					}
					bSuccess = false;
				}

			} catch (ParseException e) {
				Logger.error(
						StringResource.getStringResource("uiiufofmt00010"), e);
				showErrorMessage(e.getMessage());
				bSuccess = false;
			}

			if (strEditTotalFormula == null)
				formulaExecutor.clearFormula(area, false);
			else {
				boolean bAddCellTotalFml = false;
				try {
					StringBuffer showErrMessage = new StringBuffer();
					bAddCellTotalFml = formulaExecutor.addDbDefFormula(
							showErrMessage, area, strEditTotalFormula, null,
							false, false);
					if (bAddCellTotalFml == false) {
						showErrorMessage(StringResource
								.getStringResource("miufo1001714"));
						bSuccess = false;
					}

				} catch (ParseException e) {
					Logger.error(StringResource
							.getStringResource("uiiufofmt00011"), e);
					showErrorMessage(e.getMessage());
					bSuccess = false;
				}
			}
		}
		return bSuccess;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource() == getBtnOK()) {
			if (checkFormula() == true && addFormula() == true) {
				close();
				if (hasCheckedFormula()) {
					IArea selArea = getCellsModel().getSelectModel()
							.getSelectedArea();
					dealFormulaCellExt(selArea);
					// ���й�ʽ���������
					getCellsModel().clearArea(UFOTable.CELL_CONTENT,
							new IArea[] { selArea });
				}
			}
		} else if (e.getSource() == getBtnRefCellFormula()) {
			getTotalFormulaTextArea().getFormulaEditor().setText(
					getPublicFormulaTextArea().getFormulaEditor().getText());
		} else if (e.getSource() == getBtnEditHr()) {
			pos = getCurrentFmlEditArea().getFormulaEditor().getCaretPosition();
			setVisible(false);
			// hr������
			IFuncEditTypeSpecial funedit = ((UfoFmlExecutor) getFmlExecutor())
					.getEditTypeDriver();
			int funNameIndex = 0;
			String funName = null;
			int lastIndex = 0;
			String partam = null;
			String strPubCellFormula = getPublicFormulaTextArea().getFormulaEditor().getText();
			String strPerCellFormula = getPersonalFormulaTextArea().getFormulaEditor().getText();
			if (strPubCellFormula != null) {
				funNameIndex = strPubCellFormula.indexOf("(");
				funName = strPubCellFormula.substring(0, funNameIndex);
				lastIndex = strPubCellFormula.lastIndexOf(")");
				partam = strPubCellFormula.substring(funNameIndex + 1,
						lastIndex);
			} else if (strPerCellFormula != null) {
				funNameIndex = strPerCellFormula.indexOf("(");
				funName = strPerCellFormula.substring(0, funNameIndex);
				lastIndex = strPerCellFormula.lastIndexOf(")");
				partam = strPerCellFormula.substring(funNameIndex + 1,
						lastIndex);
			}
			String contents = null;
			try {
				if (funedit instanceof IFuncEditTypeSpecial2)
					contents = ((IFuncEditTypeSpecial2) funedit).dealFunc(this,
							funName, partam, false);
				else
					contents = funedit.dealFunc(funName, partam, false);
			} catch (UfoParseException ue) {
				AppDebug.debug(ue);
			}
			if (contents != null && contents.length() > 0) {
				String strFormula = funName + "(" + contents + ")";
				getCurrentFmlEditArea().getFormulaEditor().setText(strFormula);
			}
			setVisible(true);
		} else if (e.getSource() == getBtnHeight()) {
			FuncHightLevelDlg funcHeightDlg = new FuncHightLevelDlg(
					getCellsPane());
			funcHeightDlg.show();
			if (funcHeightDlg.getResult() == UfoDialog.ID_OK) {
				setFormulaEditType(funcHeightDlg.getFormulaEditType());
			}
		}
	}

	/**
	 * add by ����� 2008-7-2 ����ʽ��Ԫ��������չ����
	 * 1����ʽ��Ԫ�Ƿ��������
	 * 2��˽�й�ʽ��
	 * @see com.ufsoft.iufo.report.propertyoperate.PropertyOperate
	 */
	private void dealFormulaCellExt(IArea area) {
		if (area == null)
			return;
		CellsModel cellsModel = getCellsModel();
		ArrayList<CellPosition> cellPosList = cellsModel
				.getSeperateCellPos(area);
		if (cellPosList == null || cellPosList.size() == 0)
			return;
		
		for (int i = 0, n = cellPosList.size(); i < n; i++) {
			CellPosition cellPos = cellPosList.get(i);
			//��ʽ��Ԫ�Ƿ������������
			if (getFormulaEditType() == FormulaCellExtPropertyUtil.FORMULA_EDIT_NO_CONTROL) {
				FormulaCellExtPropertyUtil.removeFormulaCellExt(cellsModel, cellPos,
						FormulaCellExtPropertyUtil.EXT_FMT_FORMULA_EDIT);
			}
			FormulaCellExtPropertyUtil.setFormulaCellExt(cellsModel, cellPos, String
					.valueOf(getFormulaEditType()),
					FormulaCellExtPropertyUtil.EXT_FMT_FORMULA_EDIT);
		}
		
		FormulaVO publicCellFormula = getFormulaModel().getPublicDirectFml(area);
		FormulaVO personCellFormula = getFormulaModel().getPersonalDirectFml(area);
		if(publicCellFormula != null && IufoFormulalUtil.isCreateUnit(context)){
			publicCellFormula.setFormulaNo(getPersionalNoValue());
		}
		if(personCellFormula != null){
			personCellFormula.setFormulaNo(getPersionalNoValue());
		}
	}

	public String getRetCellFormulaForPerson() {
		return getPersonalFormulaTextArea().getFormulaEditor().getText();
	}

	public String getRetCellFormulaForPublic() {
		return publicFormulaTextArea.getFormulaEditor().getText();
	}

	public String getRetTotalFormula() {
		return totalFormulaTextArea.getFormulaEditor().getText();
	}

	public void stateChanged(ChangeEvent ce) {
		UITabbedPane tabbedPane = (UITabbedPane) ce.getSource();
		// ȡ����ѡ���������
		int m_nTab = tabbedPane.getSelectedIndex();
		if (tabbedPane == getFormulaTabbedPane()) {
			switch (m_nTab) {
			case 0:
				getBtnRefCellFormula().setVisible(false);
				requestCompFocus(getPublicFormulaTextArea().getFormulaEditor());
				validateBtnEnabled(IufoFormulalUtil.isCreateUnit(context));
				break;
			case 1:
				getBtnRefCellFormula().setVisible(false);
				requestCompFocus(getPersonalFormulaTextArea().getFormulaEditor());
				getPersonalFormulaTextArea().getFormulaEditor().setEditable(!getPersonalNoCheckBox().isSelected());
				validateBtnEnabled(!getPersonalNoCheckBox().isSelected());
				break;
			case 2:
				getBtnRefCellFormula().setVisible(true);
				requestCompFocus(getTotalFormulaTextArea().getFormulaEditor());
				validateBtnEnabled(true);
				break;
			default:
				break;
			}
		}
	}

	
	private void validateBtnEnabled(boolean isEnabled){
		getBtnValidate().setEnabled(isEnabled);
		for(int i=0,n= funcPanelList.size() ; i<n-1;i++){
			funcPanelList.get(i).setEnabled(isEnabled);
		}
	}
	
	private void requestCompFocus(final Component comp){
		if(comp == null){
			return;
		}
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				comp.requestFocus();
			}
		});
	}
	
	public int getFormulaEditType() {
		return iFormulaEditType;
	}

	public void setFormulaEditType(int iFormulaEditType) {
		this.iFormulaEditType = iFormulaEditType;
	}

	/**
	 * ���һ�㺯���б�
	 * 
	 * @return
	 */
	private UfoSimpleObject[] getCommonlyFunc() {
		FuncListInst funcList = formulaExecutor.getFuncListInst();
		if (funcList == null)
			return null;
		UfoSimpleObject[] simpleObj = funcList.getCatList();
		Vector<UfoSimpleObject> vector = new Vector<UfoSimpleObject>();
		for (UfoSimpleObject obj : simpleObj) {
			for (int type : commonlyFuncAry) {
				if (obj != null && obj.getID() == type) {
					vector.add(obj);
				}
			}
		}
		return vector.toArray(new UfoSimpleObject[0]);
	}

	/**
	 * ���IUFO����
	 * 
	 * @return
	 */
	private UfoSimpleObject[] getIufoFunc() {
		FuncListInst funcList = formulaExecutor.getFuncListInst();
		if (funcList == null)
			return null;
		UfoSimpleObject[] simpleObj = funcList.getCatList();
		List<UfoSimpleObject> list = new ArrayList<UfoSimpleObject>();
		for (UfoSimpleObject obj : simpleObj) {
			for (String strNameCode : iufoFuncAry) {
				String strFuncName = StringResource
						.getStringResource(strNameCode);
				if (obj != null && strFuncName != null
						&& strFuncName.equals(obj.getName())) {
					list.add(obj);
				}
			}
		}
		return list.toArray(new UfoSimpleObject[0]);
	}

	/**
	 * ��������Դ���ü��ص��ⲿ����
	 * 
	 * @return
	 */
	private UfoSimpleObject[] getOutFunc() {
		FuncListInst funcList = formulaExecutor.getFuncListInst();
		if (funcList == null)
			return null;

		List<UfoSimpleObject> list = new ArrayList<UfoSimpleObject>();
		for (UfoSimpleObject obj : funcList.getCatList()) {
			boolean isHas = false;
			for (UfoSimpleObject iufoObj : getIufoFunc()) {
				if (obj != null && iufoObj != null
						&& obj.getID() == iufoObj.getID()) {
					isHas = true;
					break;
				}

			}
			if (isHas)
				continue;
			list.add(obj);

		}

		List<UfoSimpleObject> filterList = new ArrayList<UfoSimpleObject>();
		for (UfoSimpleObject obj : list) {
			boolean isHas = false;
			for (UfoSimpleObject commonlyObj : getCommonlyFunc()) {
				if (obj != null && commonlyObj != null
						&& obj.getID() == commonlyObj.getID()) {
					isHas = true;
					break;
				}
			}
			if (isHas)
				continue;
			filterList.add(obj);
		}
		return filterList.toArray(new UfoSimpleObject[0]);
	}

	@Override
	public JPanel getBtnPanel() {
		if (btnPanel == null) {
			btnPanel = new JPanel();
			btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			btnPanel.add(getBtnOK());
			btnPanel.add(getBtnHeight());
			btnPanel.add(getBtnCancel());
		}
		return btnPanel;
	}

	@Override
	public void addFormulaToTextArea() {
		AreaPosition fmlArea = AreaPosition.getInstance(getAreaField()
				.getText());

		FormulaModel formulaModel = FormulaModel.getInstance(cellsPane
				.getDataModel());

		// ���ѡ�������Ӧ��ʽ��Ϣ
		FormulaVO publicCellFormula = fmlArea == null ? null : formulaModel
				.getPublicDirectFml(fmlArea);
		FormulaVO personCellFormula = fmlArea == null ? null : formulaModel
				.getPersonalDirectFml(fmlArea);
		FormulaVO totalFormula = fmlArea == null ? null : formulaModel
				.getDirectFml(fmlArea, false);

		String strDynPK = getDynPKByFmlArea(fmlArea);
		String pubCellFormula = publicCellFormula == null ? null
				: formulaExecutor.getUserDefFmlContent(publicCellFormula,
						fmlArea, strDynPK);
		String perCellFormula = personCellFormula == null ? null
				: formulaExecutor.getUserDefFmlContent(personCellFormula,
						fmlArea, strDynPK);
		String strTotalFormula = totalFormula == null ? null : formulaExecutor
				.getUserDefFmlContent(totalFormula, fmlArea, strDynPK);

		if (pubCellFormula != null && pubCellFormula.trim().length() > 0) {
			getPublicFormulaTextArea().getFormulaEditor().setText(pubCellFormula);
		}
		if (perCellFormula != null && perCellFormula.trim().length() > 0) {
			getPersonalFormulaTextArea().getFormulaEditor().setText(perCellFormula);
		}
		if (strTotalFormula != null
				&& strTotalFormula.trim().trim().length() > 0) {
			getTotalFormulaTextArea().getFormulaEditor().setText(strTotalFormula);
		}
	}

	private String getDynPKByFmlArea(IArea fmlArea) {
		DynAreaCell[] dynCellsTemp = DynAreaModel.getInstance(
				cellsPane.getDataModel()).getDynAreaCellByArea(fmlArea);
		String strDynPK = null;
		if (dynCellsTemp != null && dynCellsTemp.length > 0)
			strDynPK = dynCellsTemp[0].getDynAreaVO().getDynamicAreaPK();
		return strDynPK;
	}

	@Override
	public void openFunctionWizard(UfoSimpleObject function, IArea fmlArea) {

		if (function.getName().equals(DataSetFuncDriver.GETDATA)) {
			// ׼�����ݼ�������ݽṹ
			DataSetFuncDesignObject dsdo = new DataSetFuncDesignObject();
			DataSetFunc objDataSetFunc = parseEditedDataSetFormula(fmlArea,
					getCurrentFmlEditArea().getFormulaEditor().getText());
			dsdo.setEditedDataSetFunc(objDataSetFunc);
			dsdo
					.setStatus(objDataSetFunc == null ? DataSetDesignObject.STATUS_CREATE
							: DataSetDesignObject.STATUS_UPDATE);

			dsdo.setContext((Context) context);
			// ���ݼ��������б����
			AbstractWizardListPanel listPn = new DataSetFuncDesignWizardListPn(
					dsdo, ((UfoFmlExecutor) getFmlExecutor()).getCalcEnv(),
					cellsPane);

			WizardContainerDlg dlg = new WizardContainerDlg(getParent());
			dlg.addUIDialogListener(new UIDialogListener(){
//				@Override
				public void dialogClosed(UIDialogEvent event) {
					// @edit by wangyga at 2009-11-28,����02:45:25 �ͷ�
					DatasetTree.clearInstance();
				}
			});
			dlg.setListPn(listPn);
			dlg.setTabPn(listPn.getWizardTabPn());

			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == CommonFmlEditDlg.ID_OK) {
				updateFormulaContent(dsdo.toString());
			}
		} else {
			FunctionReferDlg funcWizardDlg = new FunctionReferDlg(getCellsPane(), context, function,
					getFmlExecutor().getFuncListInst());
			funcWizardDlg.setModal(false);
			funcWizardDlg.setArea(fmlArea);
			funcWizardDlg.setFmlExecutor(getFmlExecutor());
			funcWizardDlg.show();
			if (funcWizardDlg.getResult() == CommonFmlEditDlg.ID_OK) {
				updateFormulaContent(funcWizardDlg.getCellFunc());
			}
		}

	}
	
	/**
	 * ����GETDATA�����������ݼ���������NULL.
	 * 
	 * @param fmlArea
	 * @param formula
	 * @return
	 */
	private DataSetFunc parseEditedDataSetFormula(IArea fmlArea, String formula) {
		if (fmlArea == null || formula == null || formula.trim().length() == 0
				|| !formula.trim().startsWith("GETDATA")) {
			return null;
		}

		try {
			UfoCmdLet objLet = ((UfoFmlExecutor) getFmlExecutor())
					.getFormulaExecutor().parseUserDefFormula(fmlArea, formula);
			UfoExpr[] allExpr = objLet.getExprs();
			if (allExpr != null
					&& allExpr.length == 1
					&& allExpr[0].getElementLength() == 1
					&& allExpr[0].getElementObjByIndex(0) instanceof DataSetFunc) {
				return (DataSetFunc) allExpr[0].getElementObjByIndex(0);
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}

		return null;
	}

	@Override
	protected void loadFormula(IArea selArea) {

		CellsModel cellsModel = getCellsModel();

		IArea fmlArea = getFormulaModel().getRelatedFmlArea(selArea, true);
		if (fmlArea == null) {
			fmlArea = getFormulaModel().getRelatedFmlArea(selArea, false);
		}
		if (fmlArea != null && !fmlArea.isCell()) {
			selArea = fmlArea;
			cellsModel.getSelectModel().setSelectedArea((AreaPosition) fmlArea);
		}

		FormulaVO publicCellFormula = fmlArea == null ? null
				: getFormulaModel().getPublicDirectFml(selArea);
		FormulaVO personCellFormula = fmlArea == null ? null
				: getFormulaModel().getPersonalDirectFml(selArea);
		FormulaVO totalFormula = selArea == null ? null : getFormulaModel()
				.getDirectFml(selArea, false);

		String strDynPK = getDynPKByFmlArea(selArea);
		String strPubCellFormula = publicCellFormula == null ? null
				: ((UfoFmlExecutor) getFmlExecutor()).getUserDefFmlContent(
						publicCellFormula, selArea, strDynPK);
		String strPerCellFormula = personCellFormula == null ? null
				: ((UfoFmlExecutor) getFmlExecutor()).getUserDefFmlContent(
						personCellFormula, selArea, strDynPK);
		String strTotalFormula = totalFormula == null ? null
				: ((UfoFmlExecutor) getFmlExecutor()).getUserDefFmlContent(
						totalFormula, selArea, strDynPK);

		FormulaEditor publicEditor = getPublicFormulaTextArea().getFormulaEditor();
		publicEditor.setOldValue(
				strPubCellFormula == null ? "" : strPubCellFormula);
		publicEditor.clear();
		publicEditor.setText(strPubCellFormula);
		boolean isHRFunction = strPubCellFormula != null && strPubCellFormula.startsWith(FormulaEditor.HRFORMULA);
		publicEditor.setEditable(IufoFormulalUtil.isCreateUnit(getContext())&&!isHRFunction);
		publicEditor.setChanged(false);

		FormulaEditor persionalEditor = getPersonalFormulaTextArea().getFormulaEditor();
		persionalEditor.setOldValue(
				strPerCellFormula == null ? "" : strPerCellFormula);
		persionalEditor.clear();
		persionalEditor.setText(strPerCellFormula);
		boolean isHR = strPerCellFormula != null && strPerCellFormula.startsWith(FormulaEditor.HRFORMULA);
		Object cellExt = getCellsModel().getBsFormat(selArea.getStart(), FormulaCellExtPropertyUtil.EXT_PERSIONAL_FORMULA);
		persionalEditor.setEditable(!(cellExt != null) && !isHR);
		persionalEditor.setChanged(false);

		FormulaEditor totalEditor = getTotalFormulaTextArea().getFormulaEditor();
		totalEditor.setOldValue(
				strTotalFormula == null ? "" : strTotalFormula);
		totalEditor.clear();
		totalEditor.setText(strTotalFormula);
		totalEditor.setChanged(false);
		
		getPersonalNoCheckBox()
		.setSelected((publicCellFormula != null
				&& IufoFormulalUtil.isCreateUnit(context) && publicCellFormula
				.isFormulaNo())
				|| (personCellFormula != null && personCellFormula
						.isFormulaNo()) ? true : false);

	}

	private FormulaModel getFormulaModel() {
		return FormulaModel.getInstance(getCellsModel());
	}
}
 