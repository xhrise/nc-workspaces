package com.ufsoft.table.exarea;

import com.ufsoft.report.AbstractWizardTabPanel;
import com.ufsoft.report.AbstractWizardTabbedPane;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;

public class ExAreaBaseDesignWizardTabPn extends AbstractWizardTabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExAreaBaseDesignWizardTabPn(UfoReport report){
		super(report);
		
	}
	@Override
	public AbstractWizardTabPanel[] getStepPanels() {
		if(tabPanels==null){
			tabPanels=new AbstractWizardTabPanel[]{new ExAreaBaseInfoSetPanel(getExAreaModel(),getSelectedEx(),m_report)};
		}
		return tabPanels;
	}

	@Override
	public void initialize() {
		for(int i=0;i<getStepPanels().length;i++){
			this.addTab(getStepPanels()[i].getStepTitle(), getStepPanels()[i].getContentPanel());
		}

	}

	@Override
	public Object returnObject() {
		return getSelectedEx();
	}

	@Override
	public void setParent(UfoDialog dlg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void uninitialize() {
		// TODO Auto-generated method stub

	}

	
}
