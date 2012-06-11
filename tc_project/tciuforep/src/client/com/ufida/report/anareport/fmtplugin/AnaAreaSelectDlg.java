package com.ufida.report.anareport.fmtplugin;

import java.awt.AWTEvent;

import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.fmtplugin.formula.FuncAreaSelectDlg;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
/**
 * 
 * @author wangyga
 *
 */
public class AnaAreaSelectDlg extends FuncAreaSelectDlg{
	private static final long serialVersionUID = 6339613795821999249L;
	private UfoReport m_report = null;
	
	public AnaAreaSelectDlg(UfoReport owner) {
		super(owner,owner.getCellsModel());
		this.m_report = owner;
	}

	@Override
	protected String getViewAreaName(AWTEvent evt) {
        String strArea = super.getViewAreaName(evt);
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
		return this.m_report;
	}
}
