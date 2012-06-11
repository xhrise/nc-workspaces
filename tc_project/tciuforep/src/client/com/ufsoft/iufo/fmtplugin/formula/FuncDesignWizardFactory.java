package com.ufsoft.iufo.fmtplugin.formula;

import nc.ui.pub.dsmanager.BasicWizardStepPanel;
import nc.ui.pub.querytoolize.AbstractWizardListPanel;
import nc.ui.pub.querytoolize.AbstractWizardTabPn;

import com.ufsoft.table.CellsPane;

/*
 * 函数工具化向导工厂.
 * Creation date: (2008-06-24 15:39:08)
 * @author: chxw
 */
public class FuncDesignWizardFactory {
	
	/**
	 * 数据集函数设计向导步骤类
	 */
	public static BasicWizardStepPanel[] createDataSetFuncSteps(
			String ownerID, AbstractWizardListPanel awlp, DataSetFuncDesignObject dsdo,CellsPane cellsPane) {
		DataSetFuncProviderPanel datasetTreePanel = new DataSetFuncProviderPanel(dsdo, awlp);
		datasetTreePanel.setOwnerID(ownerID);
		BasicWizardStepPanel[] stepPanels = new BasicWizardStepPanel[] {
				datasetTreePanel,
				new DataSetFuncMetaDataPanel(dsdo, awlp), 
				new DataSetFuncParameterPanel(dsdo, awlp,cellsPane)};
		return stepPanels;
	}
	
	/**
	 * 数据集函数设计向导主类（页签）
	 */
	public static AbstractWizardTabPn createDataSetFuncTab(
			DataSetFuncDesignObject dsdo, AbstractWizardListPanel listPn) {
		AbstractWizardTabPn tabPn = new DataSetFuncDesignWizardTabPn(dsdo, listPn);
		return tabPn;
	}
	
}
