package com.ufsoft.iufo.fmtplugin.formula;

import nc.ui.pub.dsmanager.BasicWizardStepPanel;
import nc.ui.pub.querytoolize.AbstractWizardListPanel;
import nc.ui.pub.querytoolize.AbstractWizardTabPn;

import com.ufsoft.table.CellsPane;

/*
 * �������߻��򵼹���.
 * Creation date: (2008-06-24 15:39:08)
 * @author: chxw
 */
public class FuncDesignWizardFactory {
	
	/**
	 * ���ݼ���������򵼲�����
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
	 * ���ݼ�������������ࣨҳǩ��
	 */
	public static AbstractWizardTabPn createDataSetFuncTab(
			DataSetFuncDesignObject dsdo, AbstractWizardListPanel listPn) {
		AbstractWizardTabPn tabPn = new DataSetFuncDesignWizardTabPn(dsdo, listPn);
		return tabPn;
	}
	
}
