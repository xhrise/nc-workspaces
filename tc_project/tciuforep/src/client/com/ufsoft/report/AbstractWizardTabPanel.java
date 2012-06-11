package com.ufsoft.report;

import javax.swing.JPanel;



public abstract class AbstractWizardTabPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AbstractWizardTabbedPane m_tabbedPane;
	private int m_iLastSelTabIndex = -1;

	abstract public void initInfo();
	abstract public String getStepTitle();
	abstract public boolean updateInfo();
	abstract public void addListener();
	abstract public void removeListener();

	/**
	 * @return ·µ»Ø iLastSelTabIndex¡£
	 */
	public int getLastSelTabIndex() {
		return m_iLastSelTabIndex;
	}

	/**
	 * @param iLastSelTabIndex
	 */
	public void setLastSelTabIndex(int iLastSelTabIndex) {
		m_iLastSelTabIndex = iLastSelTabIndex;
	}

	public AbstractWizardTabbedPane getParentPane() {
		return m_tabbedPane;
	}

	public void setParentPane(AbstractWizardTabbedPane parent) {
		this.m_tabbedPane = parent;
	}
    abstract public JPanel getContentPanel();
    
}
