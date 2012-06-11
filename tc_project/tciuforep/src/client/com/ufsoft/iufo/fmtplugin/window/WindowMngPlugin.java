/**
 * 
 */
package com.ufsoft.iufo.fmtplugin.window;

import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;

/**
 * @author guogang
 *
 */
public class WindowMngPlugin extends AbstractPlugIn {

	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.AbstractPlugIn#createDescriptor()
	 */
	@Override
	protected IPluginDescriptor createDescriptor() {
		// TODO Auto-generated method stub
		return new WindowMngPluginDes(this);
	}

}
