package com.ufsoft.table.exarea;

import com.ufsoft.report.AbstractWizardTabPanel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.exarea.ExAreaBaseInfoPanel;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.report.util.MultiLang;


public class ExAreaBaseInfoSetPanel extends  AbstractWizardTabPanel{
    
	private ExAreaModel model;
	private ExAreaCell cell;
	private UfoReport report;
	private ExAreaBaseInfoPanel m_panel;
	
	public ExAreaBaseInfoSetPanel(ExAreaModel model,ExAreaCell cell,UfoReport parent) {
		this.model=model;
		this.cell=cell;
		this.report=parent;
	}
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @i18n miufo00200029=扩展区域基础信息设置
	 */
	public String getStepTitle() {
		return MultiLang.getString("miufo00200029");
	}

	@Override
	public void initInfo() {
		
	}

	@Override
	public boolean updateInfo() {
		
		return getContentPanel().saveData();
	}

	@Override
	public void addListener() {
		
	}
	@Override
	public void removeListener() {
		
	}

	@Override
	public ExAreaBaseInfoPanel getContentPanel() {
		if(m_panel==null){
			m_panel=new ExAreaBaseInfoPanel(model,cell,report);
		}
		return m_panel;
	}
	

}
 