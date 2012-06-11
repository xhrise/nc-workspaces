package com.ufsoft.report.sysplugin.excel;

import java.util.List;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class ExcelImpExt extends AbsActionExt {
	

	private List<IPostProcessImpExcel> _postProcessImpExcel;

	public ExcelImpExt(List<IPostProcessImpExcel> postProcessImpExcel) {
		_postProcessImpExcel = postProcessImpExcel;
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("excelFormat"));
		uiDes.setPaths(new String[]{MultiLang.getString("file"),MultiLang.getString("import")});
		uiDes.setGroup("impAndExp");
		uiDes.setImageFile("reportcore/import.gif");
		ActionUIDes uiDes1 = (ActionUIDes) uiDes.clone();
	    uiDes1.setPaths(new String[]{});
	    uiDes1.setName(MultiLang.getString("import")+MultiLang.getString("excelFormat"));
	    uiDes1.setTooltip(MultiLang.getString("import")+MultiLang.getString("excelFormat"));
	    uiDes1.setToolBar(true);
	    uiDes1.setGroup(MultiLang.getString("file"));
		return new ActionUIDes[]{uiDes,uiDes1};
	}

	public UfoCommand getCommand() {
		return new ExcelImpCmd();
	}

	public Object[] getParams(UfoReport container) {
		return new Object[]{container,_postProcessImpExcel};
	}

}
