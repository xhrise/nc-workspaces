/**
 * 
 */
package com.ufsoft.iufo.fmtplugin.window;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.sysplugin.view.ViewMngExt;

/**
 * 窗口功能描述类
 * @author guogang
 *
 */
public class WindowMngPluginDes extends AbstractPlugDes {

	public WindowMngPluginDes(IPlugIn plugin) {
        super(plugin);
    }
	protected IExtension[] createExtensions() {
		UfoReport report=getReport();
		return new IExtension[]{new ViewMngExt(report),new NavPaneMngExt(report)};

	}

}
