/**
 * 
 */
package com.ufsoft.report.fmtplugin.formula.ui;

import java.awt.BorderLayout;

import nc.ui.pub.beans.UIPanel;
import nc.util.iufo.pub.UFOString;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.util.parser.UfoParseException;
import com.ufsoft.report.fmtplugin.formula.CommonFmlEditDlg;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.script.base.IParsed;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.table.AreaPosition;

/**
 * @author wangyga
 * 公式编辑器的panel基类,包含公式编译器
 * 只负责公式的校验和保存
 * @created at 2009-9-1,下午02:22:38
 *
 */
public class FormulaEditorArea extends UIPanel{

	private static final long serialVersionUID = 1L;

	private FormulaEditor editor = null;
	
	String[] functionNames = null;
	
	private CommonFmlEditDlg fmlEditDlg = null;
	
	private String checkedFormula;
	
	public FormulaEditorArea(CommonFmlEditDlg fmlEditDlg,
			String[] functionNames){
		this.functionNames = functionNames;
		this.fmlEditDlg = fmlEditDlg;
		initialize();
	}
	
	protected void initialize() {
		setLayout(new BorderLayout());
		add(getFormulaEditor(),BorderLayout.CENTER);
	}
	
	public boolean checkFormula(String area){
		String formula = getFormulaEditor().getText();
		if (UFOString.isEmpty(formula)) {
			checkedFormula = null;
			return true;
		}
		try {
			AreaPosition areaPostition = AreaPosition.getInstance(area);
			IParsed objUserLet = fmlEditDlg.getFmlExecutor()
					.parseUserDefFormula(areaPostition, formula);
			checkedFormula = objUserLet.toString(fmlEditDlg.getFmlExecutor()
					.getExecutorEnv());
			fmlEditDlg.showMessage(MultiLang.getString("miufo00200064"));
		} catch (ParseException e) {
			UfoParseException err = (UfoParseException) e;
			AppDebug.debug(err);
			fmlEditDlg.showErrorMessage(err.getMessage());
			int index = fmlEditDlg.getFormulaEditorList().indexOf(this);
			fmlEditDlg.getFormulaTabbedPane().setSelectedIndex(index);
			 getFormulaEditor().setCaretPosition(err.getErrPos());
			checkedFormula = null;
			return false;
		}
		return true;
	}
	
	public void  saveFormula(){
		
	}
	
	public FormulaEditor getFormulaEditor(){
		if(editor == null){
			editor = new FormulaEditor(functionNames);
		}
		return editor;
	}
	
	/** 是否有通过验证的公式 */
	public boolean hasCheckedFormula() {
		return !UFOString.isEmpty(checkedFormula);
	}

	public String getCheckedFormula() {
		return checkedFormula;
	}
}
