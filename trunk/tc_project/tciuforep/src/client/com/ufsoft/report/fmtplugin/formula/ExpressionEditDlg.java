package com.ufsoft.report.fmtplugin.formula;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import nc.ui.pub.beans.UITabbedPane;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.util.parser.UfoParseException;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.fmtplugin.formula.ui.FormulaEditorArea;
import com.ufsoft.report.fmtplugin.formula.ui.FunctionPanel;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.script.base.AbsFmlExecutor;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.table.CellsPane;

/**
 * 表达式编辑面板
 * 
 * @author zhaopq
 * @created at 2009-2-23,下午02:44:22
 * 
 */
public class ExpressionEditDlg extends CommonFmlEditDlg {

	private static final long serialVersionUID = 4702657444829509426L;

	/** 表达式文本编辑区 */
	private FormulaEditorArea exprFormulaTextArea;

	public ExpressionEditDlg(CellsPane cellsPane, AbsFmlExecutor fmlExecutor) {
		super(cellsPane, fmlExecutor,MultiLang.getString("miufo00029"));// miufo00029=定义向导
		initialize();
	}

	private FormulaEditorArea getExprlFormulaTextArea() {
		if (exprFormulaTextArea == null) {
			exprFormulaTextArea = new FormulaEditorArea(this,
					getAllFuncName());
		}
		return exprFormulaTextArea;
	}

	@Override
	public boolean addFormula() {
		return true;
	}

	@Override
	protected void addFormulaTab(UITabbedPane tabbedPane) {
		addTab(tabbedPane, getExprlFormulaTextArea(), MultiLang
				.getString("miufo00125"));// miufo00125=表达式
	}

	/*
	 * miufo1000177=函数
	 */
	@Override
	protected void addFunctionTab(UITabbedPane tabbedPane) {
		FunctionPanel funcPanel = new FunctionPanel(this);
		tabbedPane.addTab(MultiLang.getString("miufo1000177"), null, funcPanel,
				null);
		funcPanelList.add(funcPanel);
	}

	@Override
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
				setResult(UfoDialog.ID_OK);
				close();
			}
		}
	}

	@Override
	public boolean checkFormula() {
		String strContent = getCurrentFmlEditArea().getFormulaEditor().getText();
		if (strContent != null && !strContent.trim().equals("") && getFmlExecutor().getFormulaProxy() != null) {
			try {
				getFmlExecutor().getFormulaProxy().parseExpr(strContent, true);
				showMessage(MultiLang.getString("miufo00200064"));
			} catch (UfoParseException err) {
				AppDebug.debug(err);
				showErrorMessage(err.getMessage());
				if (getCurrentFmlEditArea().getFormulaEditor().getText().length() > err.getErrPos())
					getCurrentFmlEditArea().getFormulaEditor().setCaretPosition(err.getErrPos());
                return false;
			} catch (ParseException err) {
				AppDebug.debug(err);
				showErrorMessage(err.getMessage());
				return false;
			}
		} 
		return true;
	}
	
	

}
