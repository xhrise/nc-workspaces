package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import javax.swing.JPanel;

import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractNavExt;
import com.ufsoft.report.util.MultiLang;
/**
 * 公式追踪导航面板的扩展
 * @author liulp
 *
 */
public class FormulaTraceNavExt extends AbstractNavExt{
	private UfoReport m_ufoReport = null;
	
	public FormulaTraceNavExt(UfoReport ufoReport) {
		m_ufoReport = ufoReport;
	}
	/**
	 * @i18n uiuforep00123=公式追踪
	 */
	public String getName() {
		// TODO to add multi language resoure ids
		return MultiLang.getString("uiuforep00123");
	}

	public int getNavPanelPos() {
		return ReportNavPanel.SOUTH_NAV;
	}

	public FormulaTraceNavPanel getFormulaTraceNavPanel(){
		return (FormulaTraceNavPanel)getPanel();
	}

	@Override
	protected JPanel createPanel() {
		JPanel t_panel=new FormulaTraceNavPanel(m_ufoReport);
		t_panel.setVisible(false);
		return t_panel;
	}
	

}
 