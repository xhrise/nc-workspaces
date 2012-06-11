package com.ufida.report.anareport.applet;

import java.awt.Component;

import nc.ui.bi.query.manager.RptProvider;

import com.borland.dx.dataset.Variant;
import com.ufida.dataset.descriptor.DescriptorType;
import com.ufida.report.anareport.model.AnaDataSetTool;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.iufo.resource.StringResource;


public class AnaFixFieldSetExt extends AbsActionExt {

	private AnaReportPlugin m_plugin = null;
	
	public AnaFixFieldSetExt(AnaReportPlugin plugin){
		this.m_plugin=plugin;
	}
	
	@Override
	public UfoCommand getCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public boolean isEnabled(Component focusComp) {
		if(!StateUtil.isAreaSel(m_plugin.getReport(), focusComp))
			return false;
		AnaReportModel model = m_plugin.getModel();
		CellPosition anchorCellPos = model.getCellsModel().getSelectModel().getAnchorCell();
		if(!model.isFormatState()){
			CellPosition[] dataStateCells = model.getFormatPoses(model.getDataModel(), new AreaPosition[]{AreaPosition.getInstance(anchorCellPos, anchorCellPos)});
			if(dataStateCells != null && dataStateCells.length > 0)
				anchorCellPos = dataStateCells[0];
		}
			
		Cell cell = model.getFormatModel().getCell(anchorCellPos);
		if(cell == null)
			return false;
		AnaRepField anaField = (AnaRepField)cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
		if( anaField != null && anaField.getFieldCountDef() == null){
			String dsPK = anaField.getDSPK();
			AnaDataSetTool dsTool=model.getDataSetTool(dsPK);
			if(dsTool!=null&&!dsTool.isSupport(DescriptorType.FilterDescriptor)){
				//对于不支持筛选的数据集，此功能不可用
				return false;
			}
				
			return true;
		}
        else
			return false;	
	}

	/**
	 * @i18n miufo00374=只能在单个字段上设置固定成员
	 * @i18n miufo00375=不能在统计字段上设置固定成员
	 * @i18n miufo00376=只能在字段上设置固定成员
	 */
	@Override
	public Object[] getParams(UfoReport container) {
		CellsModel model = m_plugin.getModel().getFormatModel();
		CellPosition[] cells=getSelectedCell();
		if (cells == null||cells.length==0)
			return null;
		
		if(cells.length>1){
			UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo00374"), container);
			return null;
		}else{
			CellPosition pos = cells[0];//modify by wangyga 2008-9-7
			ExAreaModel areaModel = ExAreaModel.getInstance(model);
			
			if (areaModel.getExArea(pos) !=null&&model.getCell(pos) != null && model.getCell(pos).getExtFmt(AnaRepField.EXKEY_FIELDINFO) != null){
				AnaRepField fld=(AnaRepField)(model.getCell(pos).getExtFmt(AnaRepField.EXKEY_FIELDINFO));
				if(fld.getField()!=null&&(fld.getField().getDataType()==Variant.STRING||fld.getField().getExtType()==RptProvider.DIMENSION)){
				String areaPk = areaModel.getExArea(pos).getExAreaPK();
				AreaDataModel aModel = m_plugin.getModel().getAreaData(areaPk);
				
				AnaFixFieldSetDlg dlg=new AnaFixFieldSetDlg(container, aModel,fld);
				
				dlg.show();
				if(dlg.getResult()==UfoDialog.ID_OK){
					
					if(!m_plugin.getModel().getFormatModel().isDirty()){
						m_plugin.getModel().getFormatModel().setDirty(true);
					}
					
					if(!m_plugin.getModel().isFormatState())
						m_plugin.refreshDataModel(true);
				}
				}else{
					UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo00375"), container);
					return null;
				}
			}else{
				UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo00376"), container);
				return null;
			}
			
		
		}
		return null;
	}

	private CellPosition[] getSelectedCell(){
		AnaReportModel model = m_plugin.getModel();
		CellsModel formatModel = model.getFormatModel();
		CellsModel dataModel = model.getDataModel();
		CellPosition[] selectCells = formatModel.getSelectModel().getSelectedCells();
		if (!model.isFormatState()){
			selectCells = model.getFormatPoses(dataModel, dataModel.getSelectModel().getSelectedAreas());
		}	
		
		return selectCells;
	}
	
	/**
	 * @i18n miufo00178=字段设置
	 * @i18n miufo00377=固定成员设置
	 * @i18n miufo00239=固定成员
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setToolBar(true);
		uiDes.setGroup(StringResource.getStringResource("miufo00178"));
		uiDes.setPaths(new String[] {});
		uiDes.setName(StringResource.getStringResource("miufo00377"));
		uiDes.setTooltip(StringResource.getStringResource("miufo00239"));
		uiDes.setImageFile("reportcore/fix_field.gif");
		return new ActionUIDes[] {uiDes};
	}

}
 