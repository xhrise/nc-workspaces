package com.ufsoft.report.plugin;

import java.util.EventObject;


/**
 * 插件事件描述。 该类描述插件状态的改变。它会通知报表工具和其他实现插件接口的类发生改变。 通常应该将插件作为参数传入，便于其他处理者获得插件的状态。
 * 另外添加几个通用事件类型，报表工具处理插件事件的时候，会根据事件类型来决定刷新的区域，是否扩展行列等。
 * 使用者应该尽可能传入事件影响区域，如此可以便于其他监听器实现者需要减少处理的区域。
 * 
 * @author wupeng 2004-7-30
 */
public class PlugEvent extends EventObject {
	/**
	 * <code>ADD</code>事件类型标记：添加数据
	 */
	public final static String ADD = "ADD";
	/**
	 * <code>MODIFY</code>事件类型标记：修改数据
	 */
	public final static String MODIFY = "MODIFY";
	/**
	 * <code>REMOVE</code>事件类型标记：删除数据
	 */
	public final static String REMOVE = "REMOVE";

	/**
	 * 构造函数
	 * 
	 * @param source
	 *            事件源
	 */
	public PlugEvent(Object source) {
		super(source);
	}

}
