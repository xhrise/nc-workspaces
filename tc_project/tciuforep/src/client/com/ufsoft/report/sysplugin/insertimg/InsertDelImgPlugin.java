package com.ufsoft.report.sysplugin.insertimg;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;


/**
 * ��Ԫ�����/ɾ��ͼƬ���
 * @author liuyy
 *
 */
public class InsertDelImgPlugin extends AbstractPlugIn {

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(InsertDelImgPlugin.this){

			protected IExtension[] createExtensions() {
        		ICommandExt extInsertImg = new InsertImgExt(getReport()); 	         		
        		ICommandExt extDelete = new DeleteImgExt(getReport());//ɾ��
				return new IExtension[]{
                        extInsertImg,extDelete
                             };
			}
			
		};
	}

}
