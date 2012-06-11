package com.ufsoft.report.plugin;

import java.awt.Component;
import java.util.EventListener;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

/**
 * 动作扩展接口默认实现
 * @author zzl 2005-6-29
 */
public abstract class AbsActionExt implements IActionExt {

	/*
	 * @see com.ufsoft.report.plugin.IActionExt#getUIDesArr()
	 */
	public abstract ActionUIDes[] getUIDesArr();

	/*
	 * @see com.ufsoft.report.plugin.IActionExt#isEnabled(java.awt.Component)
	 */
	public boolean isEnabled(Component focusComp) {
		return true;
	}

	/*
	 * @see com.ufsoft.report.plugin.IActionExt#getListeners(java.awt.Component)
	 */
	public EventListener getListener(Component stateChangeComp) {
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IActionExt#changeCompState(java.awt.Component)
	 */
	public void initListenerByComp(Component stateChangeComp) {
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public abstract UfoCommand getCommand();

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public abstract Object[] getParams(UfoReport container);

}
