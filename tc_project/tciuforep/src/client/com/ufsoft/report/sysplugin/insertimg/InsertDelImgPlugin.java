package com.ufsoft.report.sysplugin.insertimg;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;


/**
 * 单元格插入/删除图片插件
 * @author liuyy
 *
 */
public class InsertDelImgPlugin extends AbstractPlugIn {

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(InsertDelImgPlugin.this){

			protected IExtension[] createExtensions() {
        		ICommandExt extInsertImg = new InsertImgExt(getReport()); 	         		
        		ICommandExt extDelete = new DeleteImgExt(getReport());//删除
				return new IExtension[]{
                        extInsertImg,extDelete
                             };
			}
			
		};
	}

}
