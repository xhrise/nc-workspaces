/**
 * 
 */
package com.ufsoft.report.sysplugin.excel;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.util.MultiLang;

/**
 * @author guogang
 *
 */
public class ExcelExpPlugin extends AbstractPlugIn {
	private ExcelExpExt excelExpExt;
	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.AbstractPlugIn#createDescriptor()
	 */
	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this){

			@Override
			protected IExtension[] createExtensions() {
				if(excelExpExt==null)
					excelExpExt=new ExcelExpExt();
				return new IExtension[]{
						new AbsActionExt(){
							@Override
							public UfoCommand getCommand() {
								return null;
							}
							@Override
							public Object[] getParams(UfoReport container) {
								return null;
							}
							//导出菜单
							public ActionUIDes[] getUIDesArr() {
								ActionUIDes uiDes = new ActionUIDes();
								uiDes.setName(MultiLang.getString("export"));
								uiDes.setPaths(new String[]{MultiLang.getString("file")});
								uiDes.setDirectory(true);
								uiDes.setGroup("导入导出");
								return new ActionUIDes[]{uiDes};
							}					
						},
						excelExpExt
				};
			}
			
		};
	}
	public void registerPostProcessExpExcel(IPostProcessor postProcess){
		excelExpExt.setPostProcessExpExcel(postProcess);
	}
}
