package com.ufsoft.report.sysplugin.excel;

import java.awt.Component;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class ExcelExpExt extends AbsActionExt {
	private IPostProcessor postProcessExpExcel;	
	public ExcelExpExt(){
	}
	
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("excelFormat"));
		uiDes.setPaths(new String[]{MultiLang.getString("file"),MultiLang.getString("export")});
		uiDes.setGroup("导入导出");
		uiDes.setImageFile("reportcore/export.gif");
		ActionUIDes uiDes1 = (ActionUIDes) uiDes.clone();
		uiDes1.setName(MultiLang.getString("export")+MultiLang.getString("excelFormat"));
		uiDes1.setTooltip(MultiLang.getString("export")+MultiLang.getString("excelFormat"));
		uiDes1.setPaths(new String[]{});	   
	    uiDes1.setToolBar(true);
	    uiDes1.setGroup(MultiLang.getString("file"));
		return new ActionUIDes[]{uiDes,uiDes1};
	}

	public UfoCommand getCommand() {
		return new ExcelExpCmd();
	}

	public Object[] getParams(UfoReport container) {
		return new Object[]{container,getPostProcessExpExcel()};
	}

	@Override
	public boolean isEnabled(Component focusComp) {
		// TODO Auto-generated method stub
		return true;
	}

	public IPostProcessor getPostProcessExpExcel() {
		return postProcessExpExcel;
	}

	public void setPostProcessExpExcel(IPostProcessor postProcessExpExcel) {
		this.postProcessExpExcel = postProcessExpExcel;
	}


}
