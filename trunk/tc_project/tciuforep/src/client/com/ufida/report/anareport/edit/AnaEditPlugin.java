package com.ufida.report.anareport.edit;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.edit.AbsChoosePaste;
import com.ufsoft.report.sysplugin.edit.EditExt;
import com.ufsoft.report.sysplugin.edit.EditPasteFormat;
import com.ufsoft.report.sysplugin.edit.EditPlugin;
import com.ufsoft.report.sysplugin.edit.FormatBrushExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.table.re.BorderPlayRender;
/**
 * 
 * @author wangyga
 *
 */
public class AnaEditPlugin extends EditPlugin {
	private class AnaEditExt extends EditExt {
		public AnaEditExt(UfoReport report, int editType, int clipType) {
			super(report, editType, clipType);
		}
				
		@Override
		public boolean isEnabled(Component focusComp) {
			AnaReportModel model = getAanRepModel();
			if (model == null)
				return true;
			CellsModel formatModel = model.getFormatModel();
			AreaPosition[] selectedAreas = getSelectedAreas();
			if (selectedAreas != null && selectedAreas.length > 0) {
				ExAreaCell ex = ExAreaModel.getInstance(formatModel).getExArea(
						selectedAreas[0]);
				if (ex != null && (ex.getModel() instanceof AreaDataModel)) {
					AreaDataModel areaDataModel = (AreaDataModel) ex.getModel();
					if (areaDataModel != null && areaDataModel.isCross()) {
						if (!model.isFormatState()) {//数据态不允许复制交叉区域
							return false;
						}
						if (!ex.getArea().equals(selectedAreas[0]))//格式态必须复制整个扩展区域
							return false;
					}

				}
			}
			return isExtEnabled(focusComp);
		}

		@Override
		protected AreaPosition[] getSelectedAreas() {
			return getSelectAreas();
		}
		
		@Override
		protected CellsModel getCellsModel() {
			return getAanRepModel().getFormatModel();
		}	
	}

	private class AnaFormatBrushExt extends FormatBrushExt {
		public AnaFormatBrushExt(UfoReport report) {
			super(report);
		}

		public boolean isEnabled(Component focusComp) {
			return isExtEnabled(focusComp);
		}

		@Override
		protected AreaPosition[] getSelectedAreas() {
			return getSelectAreas();
		}
		
		@Override
		protected MouseListener createMouseListener() {
			return new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					if (isSeries()) {// 双击连续操作时，格式刷状态是：可以持续操作
						setUseBrush(true);
					}
					if (isUseBrush()) {				
						getChoosePaste().choosePaste();//粘贴格式
						if(!getAanRepModel().isFormatState())
							getAanPlugin().refreshDataModel(true);						
						setUseBrush(false);// 执行一次后，使格式刷不可用：如果是连续(双击格式刷按钮)操作时，再变成可用，可以继续操作
						if (!isSeries()) {// 执行一次时，停止动画，连续执行时，不需要停止
							BorderPlayRender.stopPlay(getReport().getTable().getCells());
						}
					}
					
				}

			};
		}

		private AbsChoosePaste getChoosePaste() {
			AbsChoosePaste choosePaste = null;
			if (choosePaste == null) {
				choosePaste = new EditPasteFormat(getReport(), false);
			}	
			choosePaste.setDataModel(getAanRepModel().getFormatModel());
			choosePaste.setAnchorCell(getStartCell());
			return choosePaste;
		}
		
		private CellPosition getStartCell(){
			AnaReportModel anaRepModel = getAanRepModel();
			CellsModel dataModel = getReport().getCellsModel();
			AreaPosition[] selectedAreas = anaRepModel.getFormatModel().getSelectModel().getSelectedAreas();
			if(!anaRepModel.isFormatState())//数据态时，需要把数据态的位置转化为格式态的对应字段位置上
				selectedAreas =  anaRepModel.getFormatAreas(dataModel, dataModel.getSelectModel().getSelectedAreas());
			if(selectedAreas != null && selectedAreas.length >0)
				return selectedAreas[0].getStart();
			return null;
		}
	}

	private boolean isExtEnabled(Component focusComp) {
		return StateUtil.isAreaSel(getReport(), focusComp);
	}

	
	private AnaReportModel getAanRepModel(){
		if(getAanPlugin() == null)
			return null;
		return getAanPlugin().getModel();
	}
	
	private AnaReportPlugin getAanPlugin(){
		return (AnaReportPlugin)getReport().getPluginManager().getPlugin(AnaReportPlugin.class.getName());
	}
	
	private AreaPosition[] getSelectAreas(){
		AnaReportModel anaRepModel = getAanRepModel();
		CellsModel dataModel = getReport().getCellsModel();
		AreaPosition[] selectedAreas = anaRepModel.getFormatModel().getSelectModel().getSelectedAreas();
		if(!anaRepModel.isFormatState())//数据态时，需要把数据态的位置转化为格式态的对应字段位置上
			selectedAreas =  anaRepModel.getFormatAreas(dataModel, dataModel.getSelectModel().getSelectedAreas());
		return selectedAreas;
	}
	
	public IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this) {
			protected IExtension[] createExtensions() {
				ICommandExt extCutAll = new AnaEditExt(getReport(), EditParameter.CUT, EditParameter.CELL_ALL);
				ICommandExt extCutContent = new AnaEditExt(getReport(), EditParameter.CUT, EditParameter.CELL_CONTENT);
				ICommandExt extCutFormat = new AnaEditExt(getReport(), EditParameter.CUT, EditParameter.CELL_FORMAT);
				
				ICommandExt extCopyAll = new AnaEditExt(getReport(), EditParameter.COPY, EditParameter.CELL_ALL);
				ICommandExt extCopyContent = new AnaEditExt(getReport(), EditParameter.COPY, EditParameter.CELL_CONTENT);
				ICommandExt extCopyFormat = new AnaEditExt(getReport(), EditParameter.COPY, EditParameter.CELL_FORMAT);

				ICommandExt extPaste = new AnaEditPasteExt(getReport());
				
				
				ICommandExt extClearAll = new AnaEditClearAllExt(getReport());
				ICommandExt extClearContent = new AnaEditClearContentExt(getReport());
				ICommandExt extClearFormat = new AnaEditClearFormatExt(getReport());
				
				ICommandExt formatBrushExt = new AnaFormatBrushExt(getReport());
                return new IExtension[] { 
                		extCutAll, extCutContent, extCutFormat,
                		extCopyAll, extCopyContent,extCopyFormat, 
						extPaste,
						extClearAll, extClearContent, extClearFormat,
						formatBrushExt };
			}

		};

	}
}
