package com.ufsoft.report.plugin;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

/**
 * 该接口描述关联一个命令的所有的信息。 包括是否在菜单、右键、工具栏、状态栏中显示，显示的图标，对应的命令执行类，子功能的描述，菜单中位置的描述。
 * 插件每个功能点实现该接口。 <title>显示菜单</title> 如果期望多个功能点在不同的菜单下，getMenuSlot返回不同的值即可。
 * 同一个插件下的功能点会与其他插件的功能点用菜单分割条分割。如果需要实现分级菜单的功能，父菜单项的getCommand方法返回为空。
 * <title>显示右键菜单</title> <title>显示工具栏</title> <title>显示状态栏</title>
 * 如果希望在状态栏显示提示内容，需要实现方法isStateBarSupported();getStatusMark();getStatusValue()。如果此时getCommand()非空，那么双击状态栏的时候，会响应该事件。
 * 创建日期：(2004-5-17 14:37:15)
 * 
 * @author：wupeng
 */
public interface ICommandExt extends IExtension {

	// /**
	// * 得到图片的文件名。该图标将显示在菜单和工具栏中。
	// * 创建日期：(2004-5-17 14:49:11)
	// * @return java.lang.String 返回的是相对于资源目录的文件名称。如果返回Null，将会显示一个空图标。
	// * @see com.ufsoft.report.resource.ResConst
	// */
	// public String getImageFile();

	// /**
	// * 得到快捷键组合。
	// * 在插件注册的时候会检查该组合是否被占用。
	// * @return int
	// */
	// public KeyStroke getAccelerator();

	/**
	 * 得到菜单对应的命令
	 * 
	 * @return UfoCommand
	 */
	public UfoCommand getCommand();

	/**
	 * 得到命令执行时候的参数。
	 * 
	 * @param container
	 *            父容器
	 * @return Object[]
	 */
	public Object[] getParams(UfoReport container);
}