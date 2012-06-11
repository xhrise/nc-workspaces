package com.ufsoft.iufo.fmtplugin.dataprocess;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.util.MultiLang;

public class DataProcessDescripter extends AbstractPlugDes {

	public DataProcessDescripter(IPlugIn plugin) {
		super(plugin);
	}

	protected IExtension[] createExtensions() {
		return new IExtension[]{
				new AbsActionExt(){
					public ActionUIDes[] getUIDesArr() {
						ActionUIDes uiDes = new ActionUIDes();
						uiDes.setName(StringResource.getStringResource("uiuforelease00028"));
						uiDes.setPaths(new String[]{MultiLang.getString("data")});
						uiDes.setDirectory(true);
						uiDes.setGroup("dataProcess");
						
//						ActionUIDes uiDesRightBtnDirectory = new ActionUIDes();
//						uiDesRightBtnDirectory.setDirectory(true);
//						uiDesRightBtnDirectory.setName(StringResource.getStringResource("uiuforelease00028"));
//						uiDesRightBtnDirectory.setPopup(true);
//						uiDesRightBtnDirectory.setGroup("qqwqw");
						return new ActionUIDes[]{uiDes};
					}
					public UfoCommand getCommand() {return null;}

					public Object[] getParams(UfoReport container) {return null;}					
				},
				new FilterDataProcessExt(getReport()),
				new SortDataProcessExt(getReport()),
				new GroupDataProcessExt(getReport()),
				new GroupStatDataProcessExt(getReport()),
				new CrossTableDataProcessExt(getReport()),
				new DelDataProcessExt(getReport())
		};
	}

}
