package com.ufida.report.anareport.fmtplugin;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.fmtplugin.formula.AreaFmlEditDlg;
import com.ufsoft.script.base.AbsFmlExecutor;
/**
 * 分析报表公式编辑对话框
 * @author wangyga
 *
 */
public class AnaFormulaEditDlg extends AreaFmlEditDlg{
	private static final long serialVersionUID = -360946634353384322L;
	
	public AnaFormulaEditDlg(UfoReport rpt, AbsFmlExecutor fmlExecutor) {
		super(rpt.getTable().getCells(), fmlExecutor);
	}

//	@Override
//	protected AreaFormulaPlugin getFormulaPlugin() {
//		return (AreaFormulaPlugin) getReport().getPluginManager().getPlugin(
//				AnaAreaFormulaPlugin.class.getName());
//	}

	

}
