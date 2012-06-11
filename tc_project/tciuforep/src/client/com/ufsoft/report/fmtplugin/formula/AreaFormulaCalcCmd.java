package com.ufsoft.report.fmtplugin.formula;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.script.AreaFormulaCalUtil;
import com.ufsoft.script.exception.CmdException;
import com.ufsoft.table.CellsModel;

/**
 * 简化单元公式计算
 * 
 * @author chxw
 * 2008-4-16
 */
public class AreaFormulaCalcCmd extends UfoCommand {

	@Override
	public void execute(Object[] params) {
		UfoReport ufoReport = (UfoReport) params[0];
		CellsModel cellsModel = ufoReport.getCellsModel();
		
		try {
			AreaFormulaCalUtil formulaCalUtil = new AreaFormulaCalUtil(cellsModel);
			formulaCalUtil.calcAllFormula();
		} catch (CmdException e) {
			AppDebug.debug(e);
		}		
	}

}
