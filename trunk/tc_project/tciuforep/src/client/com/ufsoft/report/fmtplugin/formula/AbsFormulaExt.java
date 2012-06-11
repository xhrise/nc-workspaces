package com.ufsoft.report.fmtplugin.formula;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;

/**
 * 公式节点描述
 * 
 * @author chxw 2008-4-16
 */
public abstract class AbsFormulaExt extends AbsActionExt {
	/**
	 * 公式插件的实例
	 */ 
	private AreaFormulaPlugin m_formulaPlugin = null;

	public AbsFormulaExt() {
	}

	public AbsFormulaExt(AreaFormulaPlugin formulaPlugin) {
		m_formulaPlugin = formulaPlugin;
	}
 
	public abstract String getName();

	public abstract String[] getPath();

	public Object[] getParams(UfoReport container) {
		return new Object[] { container, m_formulaPlugin };
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(getName());
		uiDes.setPaths(getPath());
		uiDes.setGroup("formulaExt");
		return new ActionUIDes[] { uiDes };
	}

	public AreaFormulaPlugin getFormulaPlugin() {
		return m_formulaPlugin;
	}

}
