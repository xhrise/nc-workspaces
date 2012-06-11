package com.ufsoft.report.plugin;

import javax.swing.JPanel;

/**
 * INavigationExtƒ¨»œ µœ÷
 * 
 * @author zzl 2005-6-7
 */
public abstract class AbstractNavExt implements INavigationExt {

	private JPanel m_panel;
    private boolean isShow;
	/*
	 * @see com.ufsoft.report.plugin.INavigationExt#getPanel()
	 */
	public JPanel getPanel() {
		if (m_panel == null) {
			m_panel = createPanel();
			m_panel.setBorder(null);
		}
		return m_panel;
	}

	/**
	 * @return JPanel
	 */
	protected abstract JPanel createPanel();

	/*
	 * @see com.ufsoft.report.plugin.IExtension#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		getPanel().setVisible(enabled);
	}

	public boolean isShow() {
		// TODO Auto-generated method stub
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public void setPanel(JPanel m_panel) {
		this.m_panel = m_panel;
	}

}
