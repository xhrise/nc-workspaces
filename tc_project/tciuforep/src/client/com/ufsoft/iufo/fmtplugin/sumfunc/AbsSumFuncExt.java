package com.ufsoft.iufo.fmtplugin.sumfunc;

import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectModel;

abstract class AbsSumFuncExt extends AbsActionExt {

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setToolBar(true);
		uiDes.setImageFile(getIconFile());
		uiDes.setTooltip(getToolTip());
		uiDes.setGroup(MultiLang.getString("sumFunc"));
		return new ActionUIDes[]{uiDes};
	}

	public UfoCommand getCommand() {
		return new SumFuncCmd(isDownwardNotRightward());
	}

	public Object[] getParams(UfoReport container) {
		CellsModel cellsModel = container.getCellsModel();
		SelectModel selectModel = cellsModel.getSelectModel();
		AreaPosition areaPos = null;
		if(!selectModel.isSelectAll() && selectModel.getSelectedRow()==null && selectModel.getSelectedCol()==null){
			areaPos = selectModel.getSelectedArea();
		}		
//		FormulaDefPlugin formulaPlugin = (FormulaDefPlugin) container.getPluginManager().getPlugin(FormulaDefPlugin.class.getName());
		UfoFmlExecutor formulaHandler = FormulaModel.getInstance(cellsModel).getUfoFmlExecutor();
		return new Object[]{cellsModel,areaPos,formulaHandler};
	}
	abstract protected boolean isDownwardNotRightward();
	private String getIconFile(){
		return isDownwardNotRightward() ? 
				"iufoplugin/downsum.gif" :
				"iufoplugin/rightsum.gif";
	}
	private String getToolTip(){
		return isDownwardNotRightward() ? 
				StringResource.getStringResource("miufo1001335") :
				StringResource.getStringResource("miufo1001336");
	}
}
