package com.ufsoft.iufo.fmtplugin.formula;

import com.ufsoft.iufo.inputplugin.formula.FormulaInputToolBarAction;
/**
 * IUFO 公式菜单条的扩展组件，为公式菜单条加入公式定义态插件<code>FormulaPlugin</code>提供接入方式。
 * @author zhaopq
 * @created at 2009-4-9,下午03:05:08
 * @since v5.6
 */
public class FormulaDefToolBarAction extends FormulaInputToolBarAction {

	@Override
	protected FormulaDefToolBar createToolBar() {
		return new FormulaDefToolBar(getMainboard());
	}
}
