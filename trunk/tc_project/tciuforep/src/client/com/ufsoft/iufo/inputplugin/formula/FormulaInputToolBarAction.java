package com.ufsoft.iufo.inputplugin.formula;

import com.ufsoft.report.fmtplugin.formula.AreaFormulaToolBarAction;

/**
 * IUFO 公式菜单条的扩展组件，为公式菜单条加入数据录入态公式插件<code>FormulaInputPlugin</code>提供接入方式。
 * 
 * @author zhaopq
 * @created at 2009-4-15,下午03:05:08
 * @since v5.6
 */
public class FormulaInputToolBarAction extends AreaFormulaToolBarAction {
	@Override
	protected FormulaInputToolBar createToolBar() {
		return new FormulaInputToolBar(getMainboard());
	}
}