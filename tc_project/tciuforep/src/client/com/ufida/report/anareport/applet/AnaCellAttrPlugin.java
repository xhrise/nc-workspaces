package com.ufida.report.anareport.applet;

import java.awt.Component;
import java.util.Hashtable;

import nc.ui.bi.query.manager.RptProvider;

import com.borland.dx.dataset.Variant;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.crosstable.CrossTableCellElement;
import com.ufida.report.crosstable.CrossTableSet;
import com.ufida.report.crosstable.DimInfo;
import com.ufida.report.crosstable.DimValueSet;
import com.ufida.report.crosstable.DimInfo.ValueInfo;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.cellattr.CellAttrPlugin;
import com.ufsoft.report.sysplugin.cellattr.SetCellAttrCmd;
import com.ufsoft.report.sysplugin.cellattr.SetCellAttrExt;
import com.ufsoft.report.sysplugin.cellattr.SetCellConditionCmd;
import com.ufsoft.report.sysplugin.cellattr.SetConditionAttrExt;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.chart.IChartModel;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.format.Format;

/**
 * 分析报表单元属性插件
 * @author wangyga
 *
 */
public class AnaCellAttrPlugin extends CellAttrPlugin{
	
	private class AnaSetCellAttrExt extends SetCellAttrExt{
		private AnaSetCellAttrCmd cmd;
		public AnaSetCellAttrExt(final UfoReport rep){
			super(rep);
		}

		@Override
		public UfoCommand getCommand() {
			if(cmd==null){
				cmd = new AnaSetCellAttrCmd(getReport());
			}
			  
			return cmd;
		}

		@Override
		public boolean isEnabled(final Component focusComp) {				
			return checkEnabled(focusComp);
		}
		
		/**
		 * @i18n miufo00231=分析报表数据态下格式设置失败
		 */
		@Override
		public Object[] getParams(UfoReport container) {
			AnaReportModel anaRepModel = getAanRepModel();
			CellPosition[] fmtCells=getSelectCells();
			if(!anaRepModel.isFormatState()){
				if(fmtCells==null){
					return null;
				}else if(fmtCells.length>1){
					Object[] ps=super.getParams(container);
					if(ps!=null&&ps.length>2){
						ps[2]=anaRepModel.getFormatPoses(getCellsModel(), getCellsModel().getSelectModel().getSelectedAreas());
					}
					
					return ps;
				}
				CellPosition fmtCellPos=null;
				CellPosition dataCellPos=fmtCells[0];
				Hashtable<CellPosition, Object> dataValueRef =null;
				
				Cell dataCell=getCellsModel().getCell(dataCellPos);
				Cell fmtCell=null;
				Format cellFmt=null;
				fmtCells =  anaRepModel.getFormatPoses(getCellsModel(), getCellsModel().getSelectModel().getSelectedAreas());
			    if(fmtCells!=null&&fmtCells.length==1){
			    	fmtCellPos=fmtCells[0];
			    	fmtCell=anaRepModel.getFormatModel().getCell(fmtCellPos);
			    	if(fmtCell.getFormat()!=null){
			    		cellFmt=fmtCell.getFormat();
			    	}else{
			    		cellFmt=new IufoFormat();
			    	}
			    	
			    	if(fmtCell!=null&&dataCell!=null){
			    		AnaRepField anaFld=(AnaRepField) fmtCell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
			    		dataValueRef=(Hashtable<CellPosition, Object>)getCellsModel().getExtProp(AnaRepField.EXKEY_DATAVALUE);
			    		
			    		if(anaFld!=null&&dataCell.getValue()!=null&&dataValueRef!=null){
			    			
			    			DimInfo dimInfo=anaFld.getDimInfo();
			    			DimValueSet[] dimvalues=null;
			    			if(dataCell.getValue() instanceof CrossTableCellElement){
			    				ExAreaCell exArea=anaRepModel.getExAreaCell(fmtCellPos);
			    				CrossTableSet crossset=null;
			    				if(exArea!=null&&exArea.getModel() instanceof AreaDataModel){
			    					AreaDataModel areaModel=(AreaDataModel)exArea.getModel();
			    					crossset=areaModel.getCrossSet().getCrossSet();		    				}
			    				    dimvalues=crossset.getAllDimValueSet((CrossTableCellElement)dataCell.getValue());
			    			}else{
			    				Object headerValue=dataValueRef.get(dataCellPos);
			    				if(headerValue!=null&&(anaFld.getField().getExtType()==RptProvider.DIMENSION||anaFld.getFieldDataType()==Variant.STRING)){
			    					dimvalues=new DimValueSet[]{new DimValueSet(new Object[]{anaFld.getField().getFldname()},new Object[]{headerValue})};
			    				}
			    				
			    			}
			    			
			    			if(dimvalues!=null&&dimInfo!=null){
			    				DimValueFormatSetDlg dlg=new DimValueFormatSetDlg(container,dimvalues,dimInfo,cellFmt);
			    				dlg.show();
			    				if(dlg.getResult()==UfoDialog.ID_OK){
			    					Format formatSet=null;
			    					ValueInfo valueInfo=null;
			    					formatSet=dlg.getCellFormat();
			    					if(formatSet!=null){
			    						anaRepModel.getFormatModel().setCellFormat(fmtCellPos.getRow(), fmtCellPos.getColumn(), formatSet);
			    					}
			    					
			    					for(int i=0;i<dimvalues.length;i++){
			    						valueInfo=dimInfo.getValueInfo(dimvalues[i]);
			    						formatSet=dlg.getDimValueFormat(dimvalues[i]);
			    						if(valueInfo==null){
			    							if(formatSet!=null)
			    							   dimInfo.setDimValueFormt(dimvalues[i], formatSet);
			    						}else{
			    						    valueInfo.setDataFormat(formatSet);
			    							
			    						}
			    					}
			    					getAanPlugin().refreshDataModel(false);	
			    				}
			    				return null;
			    			}
			    			
			    		}
			    		
			    		
			    	}
			    	
			    	
			    }
			    //不满足上面的都走的分支
			    Object[] ps=super.getParams(container);
    			if(ps!=null&&ps.length>2){
    				ps[2]=fmtCells;
    			}
				return ps;
			}
			
			return super.getParams(container);
		}
		
		
	
	}
	
	private class AnaSetCellAttrCmd extends SetCellAttrCmd{
		
		public AnaSetCellAttrCmd(UfoReport rep){
			super(rep);
		}

		@Override
		protected CellsModel getCellsModel() {
			return getAanRepModel().getFormatModel();
		}

		@Override
		public void execute(Object[] params) {
			if(params==null){
				return ;
			}
			CellPosition[] fmtCells=null;
			if(params[2]!=null){
				fmtCells=(CellPosition[])params[2];
			}
            if(getAanRepModel().isFormatState()){
            	super.execute(params);
            }else{
            	if(fmtCells!=null){
            		super.execute(params);
            	}
            	getAanPlugin().refreshDataModel(false);	
            }
			
			
				
		}

		@Override
		protected void setCellsProperty(CellPosition[] cells, int nType,
				int nValue) {//重写此方法，在设置单元数据类型时，不清除单元的值
			 if(cells == null) return;
		        int cellIndex = 0;    	
		        while (cellIndex < cells.length) {            
		            setCellProperty(cells[cellIndex], nType, nValue);  
		            cellIndex++;
		        }
		}
		
	}
		
	private class AnaSetConditionAttrExt extends SetConditionAttrExt{
		
		public AnaSetConditionAttrExt(UfoReport rep){
			super(rep);
		}
		
		@Override
		public UfoCommand getCommand() {
			AnaSetCellConditionCmd cmd = new AnaSetCellConditionCmd(getReport());
			return cmd;
		}

		@Override
		public Object[] getCellParams(Object[] params) {

			return doGetParams(params);
		}

		@Override
		protected CellPosition[] getSelectCells() {
//			AnaReportModel anaRepModel = getAanRepModel();
//			CellsModel dataModel = getReport().getCellsModel();
//			CellPosition[] selectedCells = anaRepModel.getFormatModel().getSelectModel().getSelectedCells();
//			if(!anaRepModel.isFormatState()){
//				selectedCells =  anaRepModel.getFormatPoses(dataModel, dataModel.getSelectModel().getSelectedAreas());
//			} 		
			return getSelectedCells();
		}

		@Override
		public boolean isEnabled(final Component focusComp) {
			return checkEnabled(focusComp);
		}

		@Override
		protected CellsModel getCellsModel() {			
			return getAanRepModel().getFormatModel();
		}
	}
	
	private class AnaSetCellConditionCmd extends SetCellConditionCmd{
		
		public AnaSetCellConditionCmd(UfoReport rep){
			super(rep);
		}

		@Override
		public void execute(Object[] params) {

			super.execute(params);
			if(!getAanRepModel().isFormatState())
				getAanPlugin().refreshDataModel(false);	
		}

		@Override
		protected CellsModel getCellsModel() {				
			return getAanRepModel().getFormatModel();
		}
					
	}
		
	private AnaReportModel getAanRepModel(){
		return getAanPlugin().getModel();
	}
	
	private AnaReportPlugin getAanPlugin(){
		return (AnaReportPlugin)getReport().getPluginManager().getPlugin(AnaReportPlugin.class.getName());
	}
	
	private CellPosition[] getSelectedCells(){
		AnaReportModel anaRepModel = getAanRepModel();
		CellsModel dataModel = getReport().getCellsModel();
		CellPosition[] selectedCells = anaRepModel.getFormatModel().getSelectModel().getSelectedCells();
		if(!anaRepModel.isFormatState()){
			selectedCells =  anaRepModel.getFormatPoses(dataModel, dataModel.getSelectModel().getSelectedAreas());
		} 
		return selectedCells;
	}
	
	private Object[] doGetParams(Object[] params){
		int paramsIndex = 1;
		CellPosition[] selectedCells = getSelectedCells();		
		if ((selectedCells != null) && (selectedCells.length > 0)) {
			params[paramsIndex++] = new Character('a');
			params[paramsIndex++] = selectedCells;
		}
		return params;
	}
	
	private boolean checkEnabled(final Component focusComp) {
		CellPosition[] pos = getSelectedCells();
		if(pos == null || pos.length == 0)
			return false;
		CellsModel formatModel = getAanRepModel().getFormatModel();
		if(pos == null)
			return true;
		Cell cell = formatModel.getCell(pos[0]);
		if(cell == null)
			return true;
		if(cell.getValue() instanceof IChartModel)
			return false;
		return StateUtil.isCPane1THeader(getReport(), focusComp);
	}
	
	/*
     * @see com.ufsoft.report.plugin.AbstractPlugIn#createDescriptor()
     */
    protected IPluginDescriptor createDescriptor() {
        return new AbstractPlugDes(this){
            protected IExtension[] createExtensions() {       		
        		ICommandExt extConCellAttr = new AnaSetConditionAttrExt(getReport());
        		ICommandExt extSetCellAttr = new AnaSetCellAttrExt(getReport());   
        		return new IExtension[]{extConCellAttr,extSetCellAttr};
            }
            
        };
    }
}
 