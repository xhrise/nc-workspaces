package com.ufida.report.anareport.edit;

import javax.swing.JComboBox;

import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.sysplugin.edit.AbsChoosePaste;
import com.ufsoft.report.sysplugin.edit.EditPasteAll;
import com.ufsoft.report.sysplugin.edit.EditPasteExt;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

public class AnaEditPasteExt extends EditPasteExt{

	public AnaEditPasteExt(UfoReport report) {
		super(report);

	}

	@Override
	public Object[] getParams(UfoReport container) {
		Object[] aryParams = new Object[2];// 对象数组
		AbsChoosePaste choosePaste = null;
		JComboBox comboBox = getItem();// 获得选择性粘贴控件的引用
		if (comboBox == null) {// 粘贴快捷键的处理：默认是粘贴全部
			comboBox = new JComboBox();
		}

		Object selectValueObj = comboBox.getSelectedItem();// 获得选择性粘贴的值
		if (selectValueObj == null) {// 如果此值为NULL，则是用快捷键粘贴
			choosePaste = new EditPasteAll(getReport(), false);	
		}
		if (selectValueObj != null) {// 获得由下拉菜单选择返回的状态对象
			choosePaste = getSelectPaste(selectValueObj.toString());
		}
		if(choosePaste != null){
			choosePaste.setDataModel(getAanRepModel().getFormatModel());
			choosePaste.setAnchorCell(getStartCell());
		}
		comboBox.getModel().setSelectedItem(null);
		aryParams[0] = container;
		aryParams[1] = choosePaste;// 选择时的状态对象实例
		return aryParams;
	}
	
	
	@Override
	public UfoCommand getCommand() {
		return new UfoCommand(){
			public void execute(Object[] params) {
				if(!isEnabled(null))
					return;
				if (params == null || params.length < 2) {
					return;
				}
				AbsChoosePaste choosePaste = (AbsChoosePaste) params[1];// 获得状态对象

				if (choosePaste == null) {
					return;
				}
				choosePaste.choosePaste();// 执行粘贴
				if(!getAanRepModel().isFormatState())
					getAanPlugin().refreshDataModel(true);	
			}
			
		};
	}

	private CellPosition getStartCell(){
		AnaReportModel anaRepModel = getAanRepModel();
		CellsModel dataModel = getReport().getCellsModel();
		AreaPosition[] selectedAreas = anaRepModel.getFormatModel().getSelectModel().getSelectedAreas();
		if(!anaRepModel.isFormatState())//数据态时，需要把数据态的位置转化为格式态的对应字段位置上
			selectedAreas =  anaRepModel.getFormatAreas(dataModel, dataModel.getSelectModel().getSelectedAreas());
		if(selectedAreas != null && selectedAreas.length >0)
			return selectedAreas[selectedAreas.length -1].getStart();
		return null;
	}
	
	private AnaReportModel getAanRepModel(){
		return getAanPlugin().getModel();
	}
	
	private AnaReportPlugin getAanPlugin(){
		return (AnaReportPlugin)getReport().getPluginManager().getPlugin(AnaReportPlugin.class.getName());
	}

}
