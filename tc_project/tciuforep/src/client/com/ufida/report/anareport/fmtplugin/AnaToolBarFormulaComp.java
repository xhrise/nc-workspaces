package com.ufida.report.anareport.fmtplugin;

import java.math.BigDecimal;

import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.crosstable.CrossTableCellElement;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.fmtplugin.formula.AreaFormulaPlugin;
import com.ufsoft.report.fmtplugin.formula.ToolBarFormulaComp;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
/**
 * 
 * @author wangyga
 *
 */
public class AnaToolBarFormulaComp extends ToolBarFormulaComp{
	private static final long serialVersionUID = -6734352065349669452L;

	public AnaToolBarFormulaComp(UfoReport ufoReport) {
		super(ufoReport);
	}

//	@Override
//	protected AreaFmlExecutor getFormulaHandler() {
//		
//		return getFormulaPlugin().getFmlExecutor();
//	}

	private AreaFormulaPlugin getFormulaPlugin(){
		AreaFormulaPlugin _formulaDefPlugin = (AreaFormulaPlugin) getReport()
		.getPluginManager()
		.getPlugin(AnaAreaFormulaPlugin.class.getName());
		return _formulaDefPlugin;
	}
	
	@Override
	protected void setCellValue(String cellText, int row, int column) {
		CellPosition pos = CellPosition.getInstance(row, column);
		Cell cell = getAanRepModel().getFormatCell(getAanRepModel().getCellsModel(), pos);
		if(cell == null)
			return;
		if(cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) != null){
			getAanRepModel().getDataModel().setCellValue(pos, cellText);
		} else{
			getAanRepModel().getFormatModel().setCellValue(cell.getRow(), cell.getCol(), cellText);
		}		
		if(!isFormat()){
			getAanPlugin().refreshDataModel(false);
		}	
	}

	
	@Override
	protected String getFormatValue(CellPosition cell) {
		if(cell == null)
			return null;
		if(getAanRepModel() == null)
			return  null;
		Object value = getAanRepModel().getCellsModel().getCellValue(cell);
		if(value instanceof CrossTableCellElement){
			value = ((CrossTableCellElement)value).getData();
		}
		if(value instanceof BigDecimal){
			value = ((BigDecimal)value).doubleValue();
		}
		// add by 王宇光 2008-5-4 修改单元数据为double类型，且为8位以上时，工具栏上不用科学计数法		
		if (value instanceof Double) {
			IufoFormat format = (IufoFormat) getAanRepModel().getCellsModel().getCellIfNullNew(
					cell.getRow(),cell.getColumn()).getFormat();
			if (format != null) {				
				value = format.getString((Double) value);
			}
		}
		return value != null ? value.toString() : "";
	}

	@Override
	protected void executeFormulaDefCmd() {		
		((AnaAreaFormulaPlugin)getFormulaPlugin()).new AnaAreaFormulaDefCmd().execute(new Object[]{getReport()});
	}

	private AnaReportModel getAanRepModel(){
		if(getAanPlugin() == null)
			return null;
		return getAanPlugin().getModel();
	}
	
	private AnaReportPlugin getAanPlugin(){
		return (AnaReportPlugin)getReport().getPluginManager().getPlugin(AnaReportPlugin.class.getName());
	}
	
	private boolean isFormat(){
		if(getAanRepModel() == null)
			return true;
		return getAanRepModel().isFormatState();
	}
	
}
