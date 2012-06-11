package com.ufsoft.iufo.inputplugin.biz;

import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceExt;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceNavExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;
/**
 * 公式追踪的插件描述
 * @author liulp
 *
 */
public class FormulaTracePlugDes  extends AbstractPlugDes{

	public FormulaTracePlugDes(IPlugIn plugin) {
		super(plugin);
	}

	@Override
	protected IExtension[] createExtensions() {
		//公式追踪菜单的扩展
		IExtension formulaTraceExt = new FormulaTraceExt(getReport());
		//公式追踪导航面板的扩展
		IExtension formulaTraceNavExt = new FormulaTraceNavExt(getReport());
		
		return new IExtension[]{formulaTraceExt,formulaTraceNavExt};
	}

}
