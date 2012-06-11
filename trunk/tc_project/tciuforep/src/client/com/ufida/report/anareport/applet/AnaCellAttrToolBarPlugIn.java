package com.ufida.report.anareport.applet;
import java.awt.Component;

import com.ufida.iufo.pub.tools.AppDebug;

import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.cellattr.SetCellAttrCmd;
import com.ufsoft.report.toolbar.CellAttrButtonExt;
import com.ufsoft.report.toolbar.CellAttrComboBoxExt;
import com.ufsoft.report.toolbar.CellAttrToolBarPlugIn;
import com.ufsoft.report.toolbar.CellBorderLineToolBarExt;
import com.ufsoft.report.toolbar.CombineCellToolBarCmd;
import com.ufsoft.report.toolbar.CombineCellToolBarExt;
import com.ufsoft.report.toolbar.dropdown.ColorPanel;
import com.ufsoft.report.toolbar.dropdown.SwatchPanel;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedAreaModel;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.TableDataModelException;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.format.TableConstant;
/**
 * 分析报表格式态和数据态单元属性插件:字体、字号、背景色、前景色、边框线、设置粗体、设置对齐方式、拆行、缩小字体、组合单元
 * @author wangyga
 *
 */
public class AnaCellAttrToolBarPlugIn extends CellAttrToolBarPlugIn{

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this) {
			protected IExtension[] createExtensions() {
				ICommandExt fontNameToolBar = new AnaCellAttrComboBoxExt(PropertyType.FontIndex,DefaultSetting.fontNames,getReport());//字体工具栏
				ICommandExt fontSizeToolBar = new AnaCellAttrComboBoxExt(PropertyType.FontSize,DefaultSetting.fontSizes,getReport());//字号工具栏
				ICommandExt forceColorToolBar = new AnaCellAttrComboBoxExt(PropertyType.ForeColor,new ColorPanel(),"reportcore/forcecolor.gif",getReport());
				ICommandExt backColorToolBar = new AnaCellAttrComboBoxExt(PropertyType.BackColor,new ColorPanel(),"reportcore/backcolor.gif",getReport());
				ICommandExt cellBorderLineToolBar = new AnaCellBorderLineToolBarExt(PropertyType.BorderLine,CellBorderLineToolBarExt.getSwatchPanel(),"reportcore/all_line.png",getReport());//设置单元边框线
				ICommandExt changeLineToolBar = new AnaCellAttrButtonExt(PropertyType.ChangeLine,TableConstant.TRUE,getReport());//自动换行
				ICommandExt shrinkFitToolBar = new AnaCellAttrButtonExt(PropertyType.ShrinkFit,TableConstant.TRUE,getReport());//缩小字体填充
				ICommandExt fontStyleToolBar = new AnaCellAttrButtonExt(PropertyType.FontStyle,TableConstant.FS_BOLD,getReport());
				ICommandExt horAlignLeftToolBar = new AnaCellAttrButtonExt(PropertyType.HorAlig,TableConstant.HOR_LEFT,getReport());
				ICommandExt horAlignCenterToolBar = new AnaCellAttrButtonExt(PropertyType.HorAlig,TableConstant.HOR_CENTER,getReport());
				ICommandExt horAlignRightToolBar = new AnaCellAttrButtonExt(PropertyType.HorAlig,TableConstant.HOR_RIGHT,getReport());
				ICommandExt combineCellToolBar = new AnaCombineCellToolBarExt(getReport());
				return new IExtension[] { fontNameToolBar,fontSizeToolBar,forceColorToolBar,backColorToolBar,cellBorderLineToolBar, fontStyleToolBar,horAlignLeftToolBar,horAlignCenterToolBar,horAlignRightToolBar,changeLineToolBar,shrinkFitToolBar,combineCellToolBar};
			}
			
		};
	}

	private class AnaCellAttrComboBoxExt extends CellAttrComboBoxExt{

		public AnaCellAttrComboBoxExt(int propertyname, String[] listitems,
				UfoReport rep) {
			super(propertyname, listitems, rep);
		}
		
		public AnaCellAttrComboBoxExt(int propertyname,SwatchPanel panel,String imageFile,UfoReport rep){
			super(propertyname,panel,imageFile,rep);
		}
		
		@Override
		public Object[] getCellParams(Object[] params) {
			
			return doGetParams(params);
		}

		@Override
		public UfoCommand getCommand() {
			 if(!isFireAction()){
	            	setFireAction(true);
	            	return null;
	            }
			return new AnaSetCellAttrCmd(getReport());
		}
			
		@Override
		public boolean isEnabled(Component focusComp) {
			return StateUtil.isCellsPane(getReport(), focusComp);
		}
	}
	
	private class AnaCellAttrButtonExt extends CellAttrButtonExt{

		public AnaCellAttrButtonExt(int propertyName, int propertyValue,
				UfoReport rep) {
			super(propertyName, propertyValue, rep);
		}

		@Override
		public Object[] getCellParams(Object[] params) {
			
			return doGetParams(params);
		}

		@Override
		public UfoCommand getCommand() {
			
			return new AnaSetCellAttrCmd(getReport());
		}
	
	}
	
	private class AnaCellBorderLineToolBarExt extends CellBorderLineToolBarExt{

		public AnaCellBorderLineToolBarExt(int propertyname, SwatchPanel panel,
				String imageFile, UfoReport rep) {
			super(propertyname, panel, imageFile, rep);
		}

		@Override
		public UfoCommand getCommand() {
            if(!isFireAction()){
            	setFireAction(true);
            	return null;
            }
			return new AnaSetCellAttrCmd(getReport());
		}

		@Override
		public Object[] getCellParams(Object[] params) {

			return doGetParams(params);
		}

		@Override
		public boolean isEnabled(Component focusComp) {
			return StateUtil.isCellsPane(getReport(), focusComp);
		}			
	}
	
	private class AnaSetCellAttrCmd extends SetCellAttrCmd{

		public AnaSetCellAttrCmd(UfoReport rep) {
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
	
	
	private class AnaCombineCellToolBarExt extends CombineCellToolBarExt{

		public AnaCombineCellToolBarExt(UfoReport report) {
			super(report);
		}

		@Override
		public UfoCommand getCommand() {
			return new AnaCombineCellToolBarCmd();
		}
		
		private class AnaCombineCellToolBarCmd extends CombineCellToolBarCmd{

			@Override
			public void execute(Object[] params) {

				m_rep = (UfoReport)params[0];
				m_cm = getCellsModel();
				AreaPosition[] selAreas = getSelectedAreas();
				
				if(selAreas == null)
					return;
				for(AreaPosition selArea: selAreas){
					if(selArea.isCell()){
						return;
					}
					
					if(CombinedAreaModel.getInstance(m_cm).belongToCombinedCell(selArea.getStart()) != null){
						removeCombineCell(selArea, m_rep.getTable());
						
					}else{
						doCombineCell(selArea,new AreaPosition[]{selArea});
						
					}
				}
								
				if(!getAanRepModel().isFormatState())
					getAanPlugin().refreshDataModel(false);	
			}

			private void removeCombineCell(AreaPosition area, UFOTable ufoTable){
			    CombinedAreaModel crm = CombinedAreaModel.getInstance(getCellsModel());
				CombinedCell[] ccs = crm.getCombineCells(area);
			     
		        for (CombinedCell cc: ccs) {
		        	
		        	UserUIEvent event = new UserUIEvent(ufoTable, UserUIEvent.UNCOMBINECELL,
		    				cc.getArea(), null);
		        	
		        	CellPosition anchor = cc.getArea().getStart();

		        	//插件是否允许操作.
		    		if (!ufoTable.checkEvent(event)) {
		    			continue;
		    		}
		    		crm.removeCombinedCell(cc);
		    		
		    		ufoTable.fireEvent(event);
		    		
					getCellsModel().getSelectModel().setAnchorCell(anchor); 
		        	 
		        }
		     
			}
			
			public boolean doCombineCell(AreaPosition selArea,AreaPosition[] areas){
			    //首先判断是否可以执行.
			    for(int i=0;i<areas.length;i++){
			        if(!isCanCombineCell(areas[i])){
			            return false;
			        }
			    }
			    //开始执行操作.			    
			    CombinedAreaModel crm = CombinedAreaModel.getInstance(getCellsModel());
			    try {
		            for (int i = 0; i < areas.length; i++) {
		            	crm.combineCell(areas[i]);
		            }
		            
		        } finally {
		        	CombinedAreaModel.getInstance(m_rep.getCellsModel()).clearCache();
		        	
		        }
		        return true;
			}
			
			private CellsModel getCellsModel() {

				return getAanRepModel().getFormatModel();
			}

			private AreaPosition[] getSelectedAreas() {
				AnaReportModel anaRepModel = getAanRepModel();
				CellsModel dataModel = getReport().getCellsModel();
				AreaPosition[] selectedAreas = anaRepModel.getFormatModel().getSelectModel().getSelectedAreas();
				if(!anaRepModel.isFormatState()){
					selectedAreas =  anaRepModel.getFormatAreas(dataModel, dataModel.getSelectModel().getSelectedAreas());
				}
				return selectedAreas;
			}
						
		}
		
	}
	
	private AnaReportModel getAanRepModel(){
		return getAanPlugin().getModel();
	}
	
	private AnaReportPlugin getAanPlugin(){
		return (AnaReportPlugin)getReport().getPluginManager().getPlugin(AnaReportPlugin.class.getName());
	}
	
	private Object[] doGetParams(Object[] params){
		int paramsIndex = 1;
		AnaReportModel anaRepModel = getAanRepModel();
		CellsModel dataModel = getReport().getCellsModel();
		CellPosition[] selectedCells = anaRepModel.getFormatModel().getSelectModel().getSelectedCells();
		if(!anaRepModel.isFormatState()){
			selectedCells =  anaRepModel.getFormatPoses(dataModel, dataModel.getSelectModel().getSelectedAreas());
		}
				
		if ((selectedCells != null) && (selectedCells.length > 0)) {
			params[paramsIndex++] = new Character('a');
			params[paramsIndex++] = selectedCells;
		}
		return params;
	}
	
}
