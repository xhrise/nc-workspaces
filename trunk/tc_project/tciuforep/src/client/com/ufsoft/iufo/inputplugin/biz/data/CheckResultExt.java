package com.ufsoft.iufo.inputplugin.biz.data;


import javax.swing.JPanel;

import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractNavExt;
import com.ufsoft.report.util.MultiLang;

public class CheckResultExt extends AbstractNavExt {

	private UfoReport m_ufoReport = null;

	public CheckResultExt(UfoReport ufoReport) {
		m_ufoReport = ufoReport;
	}

	/**
	 * @i18n uiuforep00115=ÉóºË½á¹û
	 */
	public String getName() {
		return MultiLang.getString("uiuforep00115");
	}

	public int getNavPanelPos() {
		return ReportNavPanel.SOUTH_NAV;
	}

	public CheckResultPanel getResultPanel(){
		return (CheckResultPanel)getPanel();
	}

	@Override
	protected JPanel createPanel() {
		// TODO Auto-generated method stub
		return new CheckResultPanel(m_ufoReport,getName());
	}

}
 