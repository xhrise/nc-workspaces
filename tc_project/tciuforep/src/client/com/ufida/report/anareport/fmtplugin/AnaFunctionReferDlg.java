package com.ufida.report.anareport.fmtplugin;

import java.awt.AWTEvent;

import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.fmtplugin.formula.AreaFunctionReferDlg;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
/**
 * 分析报表公式参数对话框
 * @author wangyga
 *
 */
public class AnaFunctionReferDlg extends AreaFunctionReferDlg{
	private static final long serialVersionUID = -5244653392826178342L;
	private UfoReport rep = null;
	public AnaFunctionReferDlg(UfoReport owner,UfoSimpleObject function, FuncListInst ufoFuncList) {
		super(owner.getTable().getCells(), function,ufoFuncList);
		this.rep = owner;
	}

	@Override
	protected String getViewAreaName(AWTEvent evt) {
        String strArea = super.getViewAreaName(evt);
        if(strArea == null)
        	return "";
        AnaReportModel anaRepModel = getAanRepModel();
        if(anaRepModel.isFormatState())
        	return strArea;
		CellsModel dataModel = getReport().getCellsModel();
		CellPosition[] selectedCells =  anaRepModel.getFormatPoses(dataModel, new AreaPosition[]{AreaPosition.getInstance(strArea)});
		if(selectedCells == null || selectedCells.length == 0)
			return "";
		strArea = AreaPosition.getInstance(selectedCells[0], selectedCells[selectedCells.length -1]).toString();
		
		return strArea;
	}

	private AnaReportModel getAanRepModel(){
		return getAanPlugin().getModel();
	}
	
	private AnaReportPlugin getAanPlugin(){
		return (AnaReportPlugin)getReport().getPluginManager().getPlugin(AnaReportPlugin.class.getName());
	}
	
	private UfoReport getReport(){
		return this.rep;
	}
	
}
