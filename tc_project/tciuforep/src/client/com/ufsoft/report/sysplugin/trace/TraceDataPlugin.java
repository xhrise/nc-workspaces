package com.ufsoft.report.sysplugin.trace;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;


/**
 * 数据追踪插件
 * 用于基于DataSet的数据追踪
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
