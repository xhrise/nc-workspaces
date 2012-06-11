package com.ufsoft.report.fmtplugin.formula;

import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.AreaSelectDlg;
import com.ufsoft.script.AreaFmlExecutor;
import com.ufsoft.script.base.AbsFmlExecutor;
import com.ufsoft.script.base.AreaFormulaModel;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IArea;

/**
 * �򻯵�Ԫ��ʽ����
 * modify by wangyga 2008-9-8 ��ȡһЩ����������������д
 * @author chxw
 * 2008-4-16
 */
public class AreaFormulaDefCmd extends UfoCommand {
	
	private UfoReport m_ufoReport = null;

	@Override
	public void execute(Object[] params) {
		m_ufoReport = (UfoReport)params[0];

		AreaFormulaModel areaFormulaModel = AreaFormulaModel
				.getInstance(getCellsModel());
		if (areaFormulaModel.getAreaFmlExecutor() == null) {

			// ��ʼ����ʽִ����
			AreaFormulaModel formulaModel = AreaFormulaModel
					.getInstance(getCellsModel());
			formulaModel.setAreaFmlExecutor(new AreaFmlExecutor(getCellsModel()));
		}
		// ���ѡ����Ԫ����ĵ�Ԫ��ʽ��ʽ����
		IArea anchorCell = getSelectCell();
		if (anchorCell == null)
			return;
		AreaFmlEditDlg fmlEditDlg = new AreaFmlEditDlg(getCellsPane(),
				areaFormulaModel.getAreaFmlExecutor());

		fmlEditDlg.setArea(new IArea[] { anchorCell });
		fmlEditDlg.loadFormula(anchorCell);
		fmlEditDlg.show();
	}
	
	protected CellsModel getCellsModel(){
		return m_ufoReport.getCellsModel();
	}
	
	protected CellPosition getSelectCell(){
		return getCellsModel().getSelectModel().getAnchorCell();
	}
	
	protected AreaFmlEditDlg getFmlEditDlg(UfoReport rpt, AbsFmlExecutor fmlExecutor){
		return new AreaFmlEditDlg(rpt.getTable().getCells(), fmlExecutor); 
	}
	
	protected AreaFunctionReferDlg getFunctionReferDlg(UfoReport owner,UfoSimpleObject function, FuncListInst ufoFuncList){
		return new AreaFunctionReferDlg(getCellsPane(), function, ufoFuncList);
	}
	
	protected AreaSelectDlg getAreaSelectDlg(){
		return new FuncAreaSelectDlg(getCellsPane(),getCellsModel());
	}

	private CellsPane getCellsPane(){
		return m_ufoReport.getTable().getCells();
	}
}