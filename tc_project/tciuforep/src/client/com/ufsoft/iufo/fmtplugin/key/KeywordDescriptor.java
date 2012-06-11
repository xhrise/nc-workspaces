package com.ufsoft.iufo.fmtplugin.key;





import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.iufo.resource.StringResource;
/**
* <pre>关键字插件
* </pre>
* @author zzl
* @version 5.0
* Create on 2004-9-20
*/

public class KeywordDescriptor extends AbstractPlugDes {
	
	/**
	 * @param plugin 插件
	 */
	public KeywordDescriptor(KeyDefPlugin plugin) {
		super(plugin);
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.IPluginDescriptor#getName()
	 */
	/**
	 * @i18n miuforepquery0019=关键字
	 */
	public String getName() {
		return StringResource.getStringResource("miuforepquery0019");
	}


	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.IPluginDescriptor#getPluginPrerequisites()
	 */
	public String[] getPluginPrerequisites() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.IPluginDescriptor#getExtensions()
	 */
	public IExtension[] createExtensions() {
		return new ICommandExt[]{new KeywordSetExt(getReport()),new DynAreaKeyMngExt(getReport())};
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.IPluginDescriptor#getHelpNode()
	 */
	public String getHelpNode() {
		return null;
	}
}
 