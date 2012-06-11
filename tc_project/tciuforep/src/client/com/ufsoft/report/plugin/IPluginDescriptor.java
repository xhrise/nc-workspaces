//Source file: F:\\workspace\\reportTool\\src\\com\\ufsoft\\report\\plugin\\IPluginDescriptor.java

package com.ufsoft.report.plugin;

/**
 * 对于插件信息的描述。
 * 
 * @author wupeng 2004-7-31
 */

public interface IPluginDescriptor {
	/**
	 * 插件名称
	 * 
	 * @return String
	 */
	public String getName();

	/**
	 * 得到插件的说明信息
	 * 
	 * @return String
	 */
	public String getNote();

	/**
	 * 得到当前插件运行需要其他插件的信息。
	 * 
	 * @return String[] 返回值是插件完整类名称的数组。
	 */
	public String[] getPluginPrerequisites();

	/**
	 * 得到插件挂接的功能点
	 * 
	 * @return ICommandExt[]
	 */
	public IExtension[] getExtensions();

	/**
	 * 得到帮助文件的节点
	 * 
	 * @return String
	 */
	public String getHelpNode();

}