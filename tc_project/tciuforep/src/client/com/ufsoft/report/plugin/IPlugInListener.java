
package com.ufsoft.report.plugin;

/**
 * 插件的事件监听器
 */
public interface IPlugInListener {
	/**
	 * 对于插件事件的处理。
	 * 
	 * @param e
	 *            PlugEvent
	 */
	void pluginAction(PlugEvent e);
}