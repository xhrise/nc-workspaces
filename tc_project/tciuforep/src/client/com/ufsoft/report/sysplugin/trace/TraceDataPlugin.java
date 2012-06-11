package com.ufsoft.report.sysplugin.trace;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;


/**
 * ����׷�ٲ��
 * ���ڻ���DataSet������׷��
 * @author liuyy
 *
 */
public class TraceDataPlugin  extends AbstractPlugIn {

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this) {
			protected IExtension[] createExtensions() {
				return new IExtension[] {
						new TraceDataExt((TraceDataPlugin) getPlugin())
				};
			}
		};
	}

}
