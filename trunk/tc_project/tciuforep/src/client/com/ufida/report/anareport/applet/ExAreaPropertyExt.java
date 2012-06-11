package com.ufida.report.anareport.applet;

import java.util.ArrayList;

import nc.ui.iufo.analysisrep.AnaRepMngUI;

import com.ufida.dataset.metadata.Field;
import com.ufida.report.adhoc.model.IFldCountType;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.AreaFields;
import com.ufida.report.anareport.model.FieldCountDef;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.iufo.resource.StringResource;
/**
 * 可扩展区域数据属性
 * @author wangyga
 *
 */
public class ExAreaPropertyExt extends AbsActionExt{

	private AnaReportPlugin m_plugin = null;
	
	public ExAreaPropertyExt(AnaReportPlugin plugin){
		super();
		this.m_plugin = plugin;
	}
	
	@Override
	public UfoCommand getCommand() {
		return null;
	}

	private ExAreaCell getSelectedExAreaCell(){
		AnaReportModel model = getAnaRepModel();
		CellsModel formatModel = model.getFormatModel();
		CellsModel dataModel = model.getDataModel();
		AreaPosition[] areas = formatModel.getSelectModel().getSelectedAreas();
		if(!model.isFormatState()){
			areas = model.getFormatAreas(dataModel, dataModel.getSelectModel().getSelectedAreas());
		}
		if(areas == null || areas.length == 0)
			return null;
		ExAreaModel exAreaModel = ExAreaModel.getInstance(formatModel);
		ExAreaCell[] exAreaCells = exAreaModel.getIntersectionExCells(areas[0]);
		if(exAreaCells != null && exAreaCells.length >0)
			return exAreaCells[0];
		return null;
	}
	
	private boolean isAcrossArea(){
		AreaDataModel areaDataModel = getSelectedAreaDataModel();
		if(areaDataModel == null)
			return false;
		if(areaDataModel.isCross())
			return true;
		return false;
	}
	
	private String[][] getGroupData(){
		AreaDataModel areaDataModel = getSelectedAreaDataModel();
		if(areaDataModel == null)
			return null;
		AreaFields areaFields = areaDataModel.getAreaFields();
		if(areaFields == null)
			return null;
		String[] groupFieldNamesAry = areaFields.getGroupFldNames();
		FieldCountDef[] countFieldsAry = areaFields.getAggrFlds();
		if(groupFieldNamesAry == null)
			return null;
		
		ArrayList<String[]> groupFieldInfo = new ArrayList<String[]>();
		String[] rowFieldInfo = null;
		for (String strFieldName : groupFieldNamesAry) {
			rowFieldInfo = new String[6];
			Field oldField = getAnaRepModel().getDataSetTool(areaDataModel.getDSPK()).getField(strFieldName);
			rowFieldInfo[0] = oldField == null ? "" : oldField.getCaption();
			if(countFieldsAry != null && countFieldsAry.length >0){
				for(FieldCountDef countDef : countFieldsAry){
					if(strFieldName.equalsIgnoreCase(countDef.getRangeFld())){
						if(countDef.getCountType() == IFldCountType.TYPE_SUM)
							rowFieldInfo[1] = countDef.getToolTipText();
						else if(countDef.getCountType() == IFldCountType.TYPE_COUNT)
							rowFieldInfo[2] = countDef.getToolTipText();
						else if(countDef.getCountType() == IFldCountType.TYPE_AVAGE)
							rowFieldInfo[3] = countDef.getToolTipText();
						else if(countDef.getCountType() == IFldCountType.TYPE_MAX)
							rowFieldInfo[4] = countDef.getToolTipText();
						else if(countDef.getCountType() == IFldCountType.TYPE_MIN)
							rowFieldInfo[5] = countDef.getToolTipText();
					}
				}
			}
			groupFieldInfo.add(rowFieldInfo);			
		}
		
		return groupFieldInfo.toArray(new String[0][0]);
	}
	
	private AreaDataModel getSelectedAreaDataModel(){
		ExAreaCell exAreaCell = getSelectedExAreaCell();
		if(exAreaCell == null)
			return null;
		AreaDataModel areaDataModel = (AreaDataModel)exAreaCell.getModel();
		return areaDataModel;
	}
	
	private AnaReportModel getAnaRepModel(){
		return m_plugin.getModel();
	}
	
	@Override
	public Object[] getParams(UfoReport container) {
		ExAreaPropertyDlg propertyDlg = new ExAreaPropertyDlg(getGroupData(),m_plugin.getReport());
		propertyDlg.show();
		
		return null;
	}

	/**
	 * @i18n miufo00325=区域属性
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(StringResource.getStringResource("miufo00325"));
        uiDes.setPaths(new String[]{MultiLang.getString("data")});
        uiDes.setGroup("qqq");
		return new ActionUIDes[]{uiDes};
	}

}
 