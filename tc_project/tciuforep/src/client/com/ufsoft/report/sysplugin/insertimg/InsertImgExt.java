package com.ufsoft.report.sysplugin.insertimg;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.sysplugin.insertdelete.AbsInsertDeleteExt;
import com.ufsoft.report.util.MultiLang;

public class InsertImgExt  extends AbsInsertDeleteExt{

	protected InsertImgExt(UfoReport report) {
		super(report); 
	}

	@Override
	public UfoCommand getCommand() {
		return new InsertImgCmd(this.getReport());
	}

	@Override
	public Object[] getParams(UfoReport container) { 
		
		return null;
	}

	/**
	 * @i18n report00045=≤Â»ÎÕº∆¨
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("report00045"));
        uiDes.setPaths(new String[]{MultiLang.getString("edit"),});
        uiDes.setGroup("insertGroup");
        uiDes.setShowDialog(true);
//        ActionUIDes uiDes2 = (ActionUIDes) uiDes.clone();
//        uiDes2.setPaths(new String[]{});
//        uiDes2.setPopup(true);
        return new ActionUIDes[]{uiDes}; 
	}

}
