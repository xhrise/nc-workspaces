package com.ufsoft.iufo.fmtplugin.dynarea;

import java.awt.Component;
import java.util.EventObject;

import com.ufida.zior.comp.KCheckBoxPopMenuItem;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.RowNumber;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.menu.UFCheckBoxPopMenuItem;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.ToggleMenuUIDes;
import com.ufsoft.report.util.AreaCommonOpr;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsEvent;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsModelListener;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;

public class RowNumberExt extends AbsActionExt{

	private UfoReport _report;

	RowNumberExt(UfoReport report){
		_report = report;
	}
	
	/**
	 * @i18n uiiufofmt00003=自动编号
	 */
	public ActionUIDes[] getUIDesArr() {
		ToggleMenuUIDes uiDes = new ToggleMenuUIDes();
	        uiDes.setName(StringResource.getStringResource("uiiufofmt00003"));
	        uiDes.setPopup(true);
	        uiDes.setCheckBox(true);
	        uiDes.setGroup(DynAreaDefPlugIn.MENU_GROUP);
	        return new ActionUIDes[]{uiDes};
	}

	/**
	 * @i18n uiiufofmt00003=动态区只能设置一个自动编号字段！
	 */
	public UfoCommand getCommand() {
		return new UfoCommand(){
			public void execute(Object[] params) {
				CellPosition anchorPos = getCellsModel().getSelectModel().getAnchorCell();
				Object value = getCellsModel().getCellValue(anchorPos);
				if(value == null || !(value instanceof RowNumber)){
					if(isHasRowNumberFld(anchorPos)){
						UfoPublic.sendErrorMessage(
								StringResource.getStringResource("miufo1004037"), _report, null);
						return;
					}
					
					getCellsModel().setCellValue(anchorPos,RowNumber.getInstance());
				} else{
					getCellsModel().setCellValue(anchorPos,null);
				}
			}			
		};
	}

	public Object[] getParams(UfoReport container) {
		return null;
	}

    public boolean isEnabled(Component focusComp) {
    	if(getCellsModel() == null){
    		return true;
    	}
        AreaPosition selArea = getCellsModel().getSelectModel().getSelectedArea();
        if(!selArea.isCell()){
        	return false;
        }
        
        DynAreaModel dynAreaModel = DynAreaModel.getInstance(getCellsModel());
        CellPosition anchorPos = getCellsModel().getSelectModel().getAnchorCell();
        if(dynAreaModel.isInDynArea(anchorPos) && !isHasBusinessData(anchorPos)){
        	return true;        	
        } else{
            return false;
        }
    }
    
    private boolean isHasBusinessData(CellPosition anchorPos) {
		MeasureModel measureModel = MeasureModel.getInstance(getCellsModel());
		if(measureModel.getMeasureVOByPos(anchorPos) != null){
			return true;
		}
		KeywordModel keywordModel = KeywordModel.getInstance(getCellsModel());
		if(keywordModel.getKeyVOByPos(anchorPos) != null){
			return true;
		}
		FormulaModel formulaModel = FormulaModel.getInstance(getCellsModel());
		if(formulaModel.getRelatedFmlVO(anchorPos,true)[0] != null ||
				formulaModel.getRelatedFmlVO(anchorPos,false)[0] != null	){
			return true;
		}
		return false;
	}
    
    private boolean isHasRowNumberFld(CellPosition anchorPos){
    	DynAreaModel dynAreaModel = DynAreaModel.getInstance(getCellsModel());
    	if(dynAreaModel.isInDynArea(anchorPos)){
    		DynAreaCell dynCell = dynAreaModel.getDynAreaCellByFmtPos(anchorPos);
    		CellPosition[] cellPosList = AreaCommonOpr.getCellsPositions(dynCell.getArea());
    		for(int i=0; i<cellPosList.length;i++){
    			Object value = getCellsModel().getCellValue(cellPosList[i]);
				if(value != null && value instanceof RowNumber){
					return true;
				}
    		}
    	}
    	return false;
    }
    
    private SelectListener sl;
    
    private CellsModelListener cml;
    
    public void initListenerByComp(final Component stateChangeComp){
    	if(sl==null){
    		sl = new SelectListener(){
        		public void selectedChanged(SelectEvent e) {
        			if(isEnabled(stateChangeComp)  && e.getProperty() == SelectEvent.ANCHOR_CHANGED){
        				CellPosition anchorPos = getCellsModel().getSelectModel().getAnchorCell();
        				setSelectedByPos(anchorPos, (KCheckBoxPopMenuItem)stateChangeComp);
        			}
        		}    		
        	};
        	_report.getCellsModel().getSelectModel().addSelectModelListener(sl);
    	}
    	

    	if(cml!=null){
    		cml = new CellsModelListener(){
        		public void cellsChanged(CellsEvent e){
        			if(stateChangeComp instanceof UFCheckBoxPopMenuItem && 
        					((UFCheckBoxPopMenuItem)stateChangeComp).getName().equals(StringResource.getStringResource("uiiufofmt00003"))){
        				if(isEnabled(stateChangeComp)){
        					CellPosition anchorPos = getCellsModel().getSelectModel().getAnchorCell();
        					setSelectedByPos(anchorPos, (KCheckBoxPopMenuItem)stateChangeComp);
        				}
        			}
        		}

        		public String isSupport(int source, EventObject e)
        			throws ForbidedOprException {
        			return null;
        		}
        	};
        	_report.getCellsModel().addCellsModelListener(cml);
    	}
    }

    private void setSelectedByPos(CellPosition changedPos, KCheckBoxPopMenuItem stateChangeComp){
    	Object value = getCellsModel().getCellValue(changedPos);
    	if(value != null && value instanceof RowNumber){
    		stateChangeComp.setSelected(true);
    	} else{
    		stateChangeComp.setSelected(false);
    	}
    }
	
    private CellsModel getCellsModel(){
    	return _report.getCellsModel();
    }
}
 