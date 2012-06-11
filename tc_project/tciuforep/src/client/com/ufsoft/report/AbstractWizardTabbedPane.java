package com.ufsoft.report;


import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;

import nc.ui.pub.beans.UITabbedPane;

public abstract class AbstractWizardTabbedPane extends UITabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected AbstractWizardTabPanel[] tabPanels=null;
	protected UfoReport m_report=null;
	private ExAreaModel model;
	private ExAreaCell cell;
	
	public AbstractWizardTabbedPane(UfoReport report){
		super();
		this.m_report=report;
		initialize();
	}
    
	abstract public void initialize();
	abstract public void uninitialize();
	abstract public Object returnObject();
	
	/**
	 * TabbedPane初始化动作
	 */
	private void initWizards(){
		AbstractWizardTabPanel[] panels=getStepPanels();
		if(panels!=null){
			for(int i=0;i<panels.length;i++){
				if(panels[i]!=null){
					panels[i].initInfo();
				}
			}
		}
	}

	/**
	 * TabbedPane完成动作
	 */
	public boolean completeWizards(){
		AbstractWizardTabPanel[] panels=getStepPanels();
		boolean check=true;
		if(panels!=null){
			for(int i=0;i<panels.length;i++){
				if(panels[i]!=null){
					check=panels[i].updateInfo();
					if(!check){
						return check;
					}
				}
			}
		}
		
		return check;
	}

	/**
	 * 获得TabbedPane各步骤面板
	 */
	abstract public AbstractWizardTabPanel[] getStepPanels();
	
	private AbstractWizardTabPanel getStepPanel(int tabPlacement){
		AbstractWizardTabPanel panel=null;
		if(tabPlacement>-1&&tabPlacement<getStepPanels().length){
			panel=getStepPanels()[tabPlacement];
		}
		
		return panel;
	}
	
	abstract public void setParent(final UfoDialog dlg);
	
	public ExAreaCell getSelectedEx() {
		if (cell == null) {
			CellsModel model = m_report.getCellsModel();
			AreaPosition selCell = (AreaPosition) model.getSelectModel()
					.getSelectedArea();

			ExAreaCell[] cells = getExAreaModel().getExAreaCells();
			for (int i = 0; i < cells.length; i++) {
				AreaPosition area = cells[i].getArea();
				if (selCell.intersection(area)) {
					cell = cells[i];
					break;
				}
			}
		}
		return cell;
	}
	
	public ExAreaModel getExAreaModel() {
		if (model == null) {
			model = ExAreaModel.getInstance(m_report.getCellsModel());
		}
		return model;
	}
}
