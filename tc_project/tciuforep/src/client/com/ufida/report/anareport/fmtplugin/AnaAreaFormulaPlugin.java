package com.ufida.report.anareport.fmtplugin;

import java.awt.Component;
import java.beans.PropertyChangeEvent;

import javax.swing.JTextField;

import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.dialog.AreaSelectDlg;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.fmtplugin.formula.AreaFmlEditDlg;
import com.ufsoft.report.fmtplugin.formula.AreaFormulaDefCmd;
import com.ufsoft.report.fmtplugin.formula.AreaFormulaDefExt;
import com.ufsoft.report.fmtplugin.formula.AreaFormulaPlugin;
import com.ufsoft.report.fmtplugin.formula.AreaFunctionReferDlg;
import com.ufsoft.report.fmtplugin.formula.FormulaRenderer;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.script.AreaFmlExecutor;
import com.ufsoft.script.base.AbsFmlExecutor;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.chart.IChartModel;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.re.CellRenderAndEditor;
/**
 * 分析报表格式态和数据态的公式定义插件
 * @author wangyga
 * 
 */
public class AnaAreaFormulaPlugin extends AreaFormulaPlugin{

	/**公式相关功能封装处理类*/
    private AreaFmlExecutor formulaHandler = null;
    
    
	@Override
	public void startup() {
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new FormulaRenderer(this));
		CellRenderAndEditor.getInstance().registExtSheetEditor(new AnaFormulaDefEditor(this, new JTextField()));

		//添加工具栏公式编辑栏
		getReport().getToolBarPane().add(new AnaToolBarFormulaComp(getReport()));
	}

	@Override
	protected IPluginDescriptor createDescriptor() {

		return new AbstractPlugDes(this){

			@Override
			protected IExtension[] createExtensions() {
				AnaFormulaDefExt fmlDefExt = new AnaFormulaDefExt((AnaAreaFormulaPlugin)this.getPlugin());
				return new IExtension[]{fmlDefExt};	  
			}
			
		};
	}
 
	
	@Override
	protected void processPasteEvent(UserUIEvent e) {
		if(!getAanRepModel().isFormatState())
			return;
		super.processPasteEvent(e);
	}


	private class AnaFormulaDefExt extends AreaFormulaDefExt{
		
		public AnaFormulaDefExt(AnaAreaFormulaPlugin pi){
			super(pi);
		}

		@Override
		public UfoCommand getCommand() {
			
			return new AnaAreaFormulaDefCmd();
		}

		@Override
		public boolean isEnabled(Component focusComp) {
			return checkEnabled();
		}		
	}
	
	public class AnaAreaFormulaDefCmd extends AreaFormulaDefCmd{
        
		public AnaAreaFormulaDefCmd() {
			super();
		}

		private AreaFmlEditDlg areaFmlEditDlg = null;
		
		@Override
		public void execute(Object[] params) {
			if(!checkEnabled())
				return;
			super.execute(params);
			if(areaFmlEditDlg != null && areaFmlEditDlg.getResult() == UfoDialog.ID_CANCEL)
				return;
			if(!getAanRepModel().isFormatState())
				getAanPlugin().refreshDataModel(true);	
		}

		protected CellsModel getCellsModel() {			
			return getAanRepModel().getFormatModel();
		}

		@Override
		protected CellPosition getSelectCell() {
			return getSelectedCell();
		}

		protected AreaFmlEditDlg getFmlEditDlg(UfoReport rpt,
				AbsFmlExecutor fmlExecutor) {	
			areaFmlEditDlg = new AnaFormulaEditDlg(rpt,fmlExecutor);
			return areaFmlEditDlg;
		}

		protected AreaFunctionReferDlg getFunctionReferDlg(UfoReport owner,
				UfoSimpleObject function, FuncListInst ufoFuncList) {
			return new AnaFunctionReferDlg(owner,function,ufoFuncList);
		}

		protected AreaSelectDlg getAreaSelectDlg() {
			return new AnaAreaSelectDlg(getReport());
		}			
	}
		
	private AnaReportModel getAanRepModel(){
		return getAanPlugin().getModel();
	}
	
	private AnaReportPlugin getAanPlugin(){
		return (AnaReportPlugin)getReport().getPluginManager().getPlugin(AnaReportPlugin.class.getName());
	}

	private CellPosition getSelectedCell(){
		AnaReportModel anaRepModel = getAanRepModel();
		CellsModel dataModel = getReport().getCellsModel();
		CellPosition[] selectedCells = anaRepModel.getFormatModel().getSelectModel().getSelectedCells();
		if(!anaRepModel.isFormatState()){
			selectedCells =  anaRepModel.getFormatPoses(dataModel, dataModel.getSelectModel().getSelectedAreas());
		} 
		if(selectedCells != null && selectedCells.length > 0)
			return selectedCells[0];
		return null;
	}
	
	private boolean checkEnabled(){
		CellPosition pos = getSelectedCell();
		CellsModel formatModel = getAanRepModel().getFormatModel();
		if(pos == null)
			return true;
		Cell cell = formatModel.getCellIfNullNew(pos.getRow(), pos.getColumn());
		if(cell == null)
			return true;
		if(cell.getValue() instanceof IChartModel)
			return false;
		
		ExAreaModel exAreaModel = ExAreaModel.getInstance(formatModel);
		ExAreaCell exAreaCell = exAreaModel.getExArea(pos);
		if(exAreaCell == null)
			return false;
		AreaDataModel areaDataModel = (AreaDataModel)exAreaCell.getModel();
		if(areaDataModel == null)
			return false;
		if(areaDataModel.isCross())
			return false;
		return true;
	}
	
	@Override
	public AreaFmlExecutor getFmlExecutor() {
		if(formulaHandler == null){
    		formulaHandler = new AreaFmlExecutor(getAanRepModel().getFormatModel()){

				@Override
				protected void setFormulaCellType(CellPosition cell, int value) {
					setCellProperty(cell,PropertyType.DataType,value);
				}
    			
    		};
    	}
    	return formulaHandler;
	}
	
	private void setCellProperty(CellPosition pos, int nType, int nValue) {
		// 开始保存数据
		Format format = getCellsModel().getFormat(pos);
		if (format == null) {
			format = new IufoFormat();
		}
		PropertyType.setPropertyByType(format, nType, nValue);
		getCellsModel().setCellFormat(pos.getRow(), pos.getColumn(), format);// 调用此方法激发格式改变事件。
		// 如果是折行和大字体设置时,自动设置行高.
		if (nType == PropertyType.ChangeLine || nType == PropertyType.FontSize) {
			headerPropertyChanged(new PropertyChangeEvent(getCellsModel().getRowHeaderModel(),
					Header.HEADER_AUTORESIZE, Boolean.FALSE, new Integer(pos
							.getRow())));
		}
	}
}
