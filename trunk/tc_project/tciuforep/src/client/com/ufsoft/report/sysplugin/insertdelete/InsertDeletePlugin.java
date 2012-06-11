package com.ufsoft.report.sysplugin.insertdelete;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.util.MultiLang;

public class InsertDeletePlugin extends AbstractPlugIn {

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(InsertDeletePlugin.this){

			protected IExtension[] createExtensions() {
				ICommandExt extFillExt = new AbsActionExt(){
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
						uiDes.setName(MultiLang.getString("uiuforep0000877"));
						uiDes.setPaths(new String[]{MultiLang.getString("edit")});
						uiDes.setDirectory(true);
						uiDes.setGroup("insertAndFill");
						return new ActionUIDes[]{uiDes};
					}					
				};
//        		ICommandExt extInsertCell = new InsertCellExt(getReport());//插入单元格		
//        		ICommandExt extInsertRows = new InsertRowsExt(getReport());//插入行
//        		ICommandExt extInsertCols = new InsertColumnsExt(getReport());//插入列    
				ICommandExt extInsert = new InsertExt(getReport());
        		ICommandExt extDelete = new DeleteExt(getReport());//删除
				return new IExtension[]{
						extFillExt,
//						extInsertCell,extInsertRows,extInsertCols, 
						extInsert,
						extDelete};
			}
			
		};
	}

}
