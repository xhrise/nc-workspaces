package com.ufsoft.report.fmtplugin.formula;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import nc.ui.pub.beans.UITabbedPane;

import com.ufsoft.report.fmtplugin.formula.ui.FormulaEditor;
import com.ufsoft.report.fmtplugin.formula.ui.FormulaEditorArea;
import com.ufsoft.report.fmtplugin.formula.ui.FunctionPanel;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.script.AreaFmlExecutor;
import com.ufsoft.script.base.AbsFmlExecutor;
import com.ufsoft.script.base.FormulaVO;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IArea;
import com.ufsoft.table.UFOTable;

/**
 * 区域公式编辑对话框定义
 * 
 * @author zhaopq
 * @created at 2009-2-23,上午10:39:09
 * 
 */
public class AreaFmlEditDlg extends CommonFmlEditDlg {

	private static final long serialVersionUID = 7930503452474585349L;

	/** 区域公式逻辑处理器 */
	private AreaFmlExecutor formulaExecutor;

	/** 单元公式文本编辑区 */
	private FormulaEditorArea cellFormulaTextArea;
	
	public AreaFmlEditDlg(CellsPane cellsPane,AbsFmlExecutor fmlExecutor) {
		super(cellsPane, fmlExecutor);
		formulaExecutor = (AreaFmlExecutor) fmlExecutor;
		initialize();
	}
	
	@Override
	protected JPanel getJContentPane() {
		JPanel result = super.getJContentPane();
		result.add(getTopPanel(), BorderLayout.NORTH);
		return result;
	}

	@Override
	protected void addFormulaTab(UITabbedPane tabbedPane) {
		addTab(tabbedPane, getCellFormulaTextArea(), MultiLang
				.getString("miufo1000909"));// "单元公式"
	}

	public FormulaEditorArea getCellFormulaTextArea() {
		if (cellFormulaTextArea == null) {
			cellFormulaTextArea = new FormulaEditorArea(this,
					getAllFuncName());
		}
		return cellFormulaTextArea;
	}

	@Override
	public boolean addFormula() {
		boolean result = true;
		for (IArea area : getArea()) {
			String strEditCellFormula = getCellFormulaTextArea()
					.getCheckedFormula();
			if (strEditCellFormula == null) {
				formulaExecutor.clearFormula(area);
			} else {
				try {
					StringBuffer showErrMessage = new StringBuffer();
					boolean bAddCellFml = formulaExecutor.addDbDefFormula(
							showErrMessage, area, strEditCellFormula, null,
							true);

					if (!bAddCellFml) {
						if (showErrMessage.length() > 0) {
							showErrorMessage(showErrMessage.toString());
						} else {
							showErrorMessage(MultiLang
									.getString("miufo1001713"));// 错误的单元公式
						}
						result = false;
					}
				} catch (ParseException e) {
					showErrorMessage(e.getMessage());
					result = false;
				}

			}
		}

		return result;

	}

	@Override
	public void addFormulaToTextArea() {
		if (formulaExecutor == null)
			return;
		AreaPosition fmlArea = AreaPosition.getInstance(getAreaField()
				.getText());
		FormulaVO fmlVO = formulaExecutor.getFormulaModel().getDirectFml(
				fmlArea.getStart());
		String oldFmlContent = fmlVO != null ? fmlVO.getContent() : null;
		getCellFormulaTextArea().getFormulaEditor().setText(oldFmlContent);
	}

	public void setCellUserFormula(String formula) {
		getCellFormulaTextArea().getFormulaEditor().setText(formula);
	}

	/*
	 * miufo1000177=函数
	 */
	protected void addFunctionTab(UITabbedPane tabbedPane) {
		FunctionPanel funcPanel = new FunctionPanel(this);
		tabbedPane.addTab(MultiLang.getString("miufo1000177"), null, funcPanel,
				null);
		funcPanelList.add(funcPanel);
	}

	public JPanel getBtnPanel() {
		if (btnPanel == null) {
			btnPanel = new JPanel();
			btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			btnPanel.add(getBtnOK());
			btnPanel.add(getBtnCancel());
		}
		return btnPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource() == getBtnOK()) {
			if (checkFormula() == true && addFormula() == true) {
				close();
				if (hasCheckedFormula()) {
					//如有公式，清除表样
					getCellsModel().clearArea(UFOTable.CELL_CONTENT,
							new IArea[] { getCellsModel().getSelectModel().getAnchorCell() });
				}
			}
		}
	}
	
	@Override
	protected void loadFormula(IArea anchorCell) {
		FormulaVO fmlVO = ((AreaFmlExecutor)getFmlExecutor()).getFormulaModel().getDirectFml(anchorCell);
		String cellFormula = fmlVO != null?fmlVO.getContent():null;
		FormulaEditor editor = getCellFormulaTextArea().getFormulaEditor();
		editor.setOldValue(cellFormula==null?"":cellFormula);
		editor.clear();
		editor.setText(cellFormula);
		editor.setChanged(false);
	}
}