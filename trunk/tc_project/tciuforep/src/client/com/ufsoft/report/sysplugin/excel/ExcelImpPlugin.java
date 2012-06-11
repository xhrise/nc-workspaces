package com.ufsoft.report.sysplugin.excel;

import java.util.List;
import java.util.Vector;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.util.MultiLang;

public class ExcelImpPlugin extends AbstractPlugIn {

	private List<IPostProcessImpExcel> postProcessImpExcel = new Vector<IPostProcessImpExcel>();
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this){
			protected IExtension[] createExtensions() {
				return new IExtension[]{
						new AbsActionExt(){
							//µπ»Î≤Àµ•
							public UfoCommand getCommand() {
								return null;
							}
							@Override
							public Object[] getParams(UfoReport container) {
								return null;
							}
							@Override
							public ActionUIDes[] getUIDesArr() {
								ActionUIDes uiDes = new ActionUIDes();
								uiDes.setName(MultiLang.getString("import"));
								uiDes.setPaths(new String[]{MultiLang.getString("file")});
								uiDes.setDirectory(true);
								uiDes.setGroup("impAndExp");
								return new ActionUIDes[]{uiDes};
							}					
						},
						new ExcelImpExt(postProcessImpExcel)};
			}
		};
	}
	public void registerPostProcessImpExcel(IPostProcessImpExcel postProcess){
		postProcessImpExcel.add(postProcess);
	}
}