package com.ufsoft.report.plugin;

import java.awt.Component;
import java.util.EventListener;

/**
 * 动作扩展接口
 * @author zzl 2005-6-28
 */
public interface IActionExt extends ICommandExt {
	public ActionUIDes[] getUIDesArr();

	/**
	 * 当前状态扩展对应的组件是否可用。
	 * 
	 * @param focusComp
	 * @return boolean
	 */
	public boolean isEnabled(Component focusComp);

	/**
	 * 这里的监听器主要是修改菜单、工具条等的显示状态。 如锁定解锁的名称切换，舍位区域的是否选中状态。
	 * 
	 * @param stateChangeComp
	 * @return EventListener[]
	 * @deprecated 请使用initListenerByComp.
	 */
	public EventListener getListener(Component stateChangeComp);

	/**
	 * 对组件进行处理,可以构造监听器加到适当的对象上,也可以直接修改组件name,image等.
	 * 
	 * @param stateChangeComp
	 *            void
	 */
	public void initListenerByComp(Component stateChangeComp);
}
