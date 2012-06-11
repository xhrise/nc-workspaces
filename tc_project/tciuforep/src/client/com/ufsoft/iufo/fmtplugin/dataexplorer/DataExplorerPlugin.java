package com.ufsoft.iufo.fmtplugin.dataexplorer;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
/**
 * ��ʽ���ʱ������̬���
 * @author zzl
 */
public class DataExplorerPlugin extends AbstractPlugIn {

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(null){
			protected IExtension[] createExtensions() {
				return new IExtension[]{new DataExplorerExt()};
			}			
		};
	}

}
