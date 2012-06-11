package com.ufsoft.iufo.fmtplugin.formula;

import nc.ui.pub.querytoolize.AbstractWizardListPanel;
import nc.ui.pub.querytoolize.AbstractWizardStepPanel;
import nc.ui.pub.querytoolize.AbstractWizardTabPn;

/*
 * 数据集函数设计全程向导页签.
 * Creation date: (2008-06-24 15:39:08)
 * @author: chxw
 */
public class DataSetFuncDesignWizardTabPn extends AbstractWizardTabPn {
	private static final long serialVersionUID = 1L;

	//数据集函数设计数据结构
	protected DataSetFuncDesignObject m_dsdo = null;

	// 向导列表面板实例
	protected AbstractWizardListPanel m_listPn = null;

	/**
	 * DataSetFuncDesignWizardTabPn 构造子注解。
	 */
	public DataSetFuncDesignWizardTabPn(DataSetFuncDesignObject dsdo,
			AbstractWizardListPanel listPn) {
		super();
		m_dsdo = dsdo;
		m_listPn = listPn;
		setContainerPanel(listPn.getContainerPanel());
		setLastSelTabIndex(listPn.getLastSelListIndex());
	}

	public String getWizardTitle() {
		return m_listPn.getWizardTitle();
	}

	public String getWizardDescription() {
		return m_listPn.getWizardDescription();
	}

	public void initWizard() {
		m_listPn.initWizard();
	}

	public boolean completeWizard() {
		return m_listPn.completeWizard();
	}

	public AbstractWizardStepPanel[] getStepPanels() {
		return m_listPn.getStepPanels();
	}

}
