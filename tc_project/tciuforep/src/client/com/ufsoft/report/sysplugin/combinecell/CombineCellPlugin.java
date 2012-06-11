package com.ufsoft.report.sysplugin.combinecell;

import java.util.EventObject;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.insertdelete.DeleteCmd;
import com.ufsoft.report.sysplugin.insertdelete.DeleteInsertDialog;
import com.ufsoft.report.sysplugin.insertdelete.InsertCellCmd;
import com.ufsoft.report.undo.CombinedCellUndo;
import com.ufsoft.report.undo.ReportUndo;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CombinedAreaModel;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;

/**
 * 组合单元插件
 * @author zzl 2005-5-25
 */
public class CombineCellPlugin extends AbstractPlugIn implements
		UserActionListner {

	@Override
	public void startup() {
		getReport().getEventManager().addListener(this);
	}

	public String isSupport(int source, EventObject e)
			throws ForbidedOprException {
		if (e instanceof UserUIEvent) {
			UserUIEvent ee = (UserUIEvent) e;
			if (ee.getEventType() == UserUIEvent.PASTE) {
				return isSupportPasteCombineCell(ee);// 粘贴时，对组合单元是否支持
				
			} else if (ee.getEventType() == UserUIEvent.INSERTCELL) {
				return isSupportInsertCell(ee);
				
			} else if (ee.getEventType() == UserUIEvent.DELETECELL) {
				return isSupportDeleteCell(ee);
			}

		}
		return null;
	}
	
	 private String isSupportDeleteCell(UserUIEvent ee) {
	        int deleteType = ((Integer)(ee.getOldValue())).intValue();
	        AreaPosition aimArea = (AreaPosition) ee.getNewValue();
	        AreaPosition toMoveArea = DeleteCmd.getToMoveArea(aimArea,deleteType,getCellsModel());
			CombinedCell[] combineCells = getCombinedAreaModel().getCombineCells(toMoveArea);
			if(combineCells != null && combineCells.length > 0){
			    return MultiLang.getString("uiuforep0000912");
			}
	        return null;
	    }

	    /**
		 * 是否支持插入单元事件
		 * 
		 * @param ee
		 * @return
		 */
	private String isSupportInsertCell(UserUIEvent ee) {
		int insertType = ((Integer) (ee.getOldValue())).intValue();
		AreaPosition aimArea = (AreaPosition) ee.getNewValue();
		AreaPosition toMoveArea = InsertCellCmd.getToMoveArea(aimArea,
				insertType, getCellsModel());
//		CombinedCell[] combineCells = getCombinedAreaModel().getCombineCells(
//				toMoveArea);
//		if (combineCells != null && combineCells.length > 0) {
//			return MultiLang.getString("uiuforep0000912");
//		}		
		if(isDelCombineWhenInsertCell(aimArea ,toMoveArea ,insertType))
			return MultiLang.getString("uiuforep0000912");
		return null;
	}

	/**
	 * add by wangyga 当插入单元时　，判断插入方向上是否有要删除的组合单元。
	 * @param aimArea
	 * @param toMoveArea
	 * @param insertType
	 * @return
	 */
	private boolean isDelCombineWhenInsertCell(AreaPosition aimArea , AreaPosition toMoveArea ,int insertType){
		CombinedCell[] aimAreaCombineCells = getCombinedAreaModel().getCombineCells(toMoveArea);
		if(aimAreaCombineCells == null || aimAreaCombineCells.length == 0)
			return false;
		for(CombinedCell combineCell : aimAreaCombineCells){
			AreaPosition area = combineCell.getArea();
			if(insertType == DeleteInsertDialog.CELL_MOVE_RIGHT){
				if(aimArea.getHeigth() != area.getHeigth())
					return true;
			} else if(insertType == DeleteInsertDialog.CELL_MOVE_DOWN){
				if(aimArea.getWidth() != area.getWidth())
					return true;
			}
		}		
		return false;
	}
	
	/**
	 * add by 王宇光 2008-6-4 粘贴组合单元时，不能对组合单元做部分更改
	 * 
	 * @param UserUIEvent
	 *            ee
	 * @return String
	 * @i18n miufo00122=不能对组合单元做部分更改。
	 */
	private String isSupportPasteCombineCell(UserUIEvent ee)
			throws ForbidedOprException {
		if (ee == null) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1000496"));// 输入参数不允许为空
		}
		Object object = ee.getNewValue();
		EditParameter parameter = null;
		if (object instanceof EditParameter) {
			parameter = (EditParameter) object;
		}
		AreaPosition[] areaPos = parameter.getPasteAreas();
		for(AreaPosition area: areaPos){
			if(!validateArea(area)){
				throw new ForbidedOprException(MultiLang.getString("miufo00122"));
			}
		}

		return null;
	}
	
	//交叠但不包含
	private boolean validateArea(AreaPosition area){
		CombinedCell[] cells = getCombinedAreaModel().getCombineCells();
		for(CombinedCell ex: cells){
			//交叠但不包含
			if(ex.getArea().intersection(area) && !area.contain(ex.getArea()) && !ex.getArea().contain(area)){
				return false;
			}
		}
		return true;
	}
	
	private CombinedAreaModel getCombinedAreaModel(){
		return CombinedAreaModel.getInstance(getCellsModel());
	}
	
	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
	 */
	public IPluginDescriptor createDescriptor() {
		// TODO 自动生成方法存根
		return new AbstractPlugDes(this) {

			protected IExtension[] createExtensions() {
				// TODO 自动生成方法存根
				ICommandExt extCombineCell = new CombineCellExt(getReport());//组合单元
				 //#格式菜单 与excel一致，右键不再增加组合单元操作菜单。liuyy+
				return new IExtension[0];// { extCombineCell };
			}

		};
	}

	/**
	 * add by 王宇光 2008-6-4 组合单元对粘贴事件的处理
	 * 
	 * @param UserUIEvent
	 *            e
	 * @return
	 */
	private void processPasteEvent(UserUIEvent e) {
		if (e == null)
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1000496"));// 输入参数不允许为空
		Object object = e.getNewValue();
		EditParameter parameter = null;
		if (object instanceof EditParameter) {
			parameter = (EditParameter) object;
		} else {
			return;
		}
		
		ReportUndo parentUndo =  getCellsModel().getUndoManager().getCurAddingUndo();
		
		AreaPosition[] newAreas = parameter.getPasteAreas();
				
		AreaPosition oldArea = parameter.getCopyArea();
		
		boolean b_isTransfer = parameter.isTransfer();
		
//		CombinedCell[] copyCombinedCells = getCombinedAreaModel().getCombineCells(oldArea);
		CombinedCell[] copyCombinedCells = (CombinedCell[])parameter.getAreaInfo(EditParameter.COMBINED_CELL);
		
		for(AreaPosition newArea: newAreas){
			//如果粘贴区域存在组合单元，执行清除操作。
			CombinedCell[] cellsInNewArea = getCombinedAreaModel().getCombineCells(newArea);
    		if(cellsInNewArea.length > 0){
    			for(CombinedCell cc: cellsInNewArea){
    				if(parentUndo != null){
    					CombinedCellUndo undo = new CombinedCellUndo(CombinedCellUndo.TYPE_REMOVE, cc.getArea());
    					parentUndo.addUndo(undo);
    				}
    			}
    			CombineCellCmd.delCombineCell(newArea, getReport().getTable());
    			
    		}
    		
			for(CombinedCell cc: copyCombinedCells){
				AreaPosition oldCCArea = cc.getArea();
    			AreaPosition newCCArea = (AreaPosition) oldCCArea.getMoveArea(oldArea.getStart(), newArea.getStart());
    			CellPosition startCell = newCCArea.getStart();
    			if(b_isTransfer){
    				newCCArea = AreaPosition.getInstance(startCell.getRow(), startCell.getColumn(), newCCArea.getHeigth(), newCCArea.getWidth());
    			}
    			
    			if(parameter.getEditType() == EditParameter.CUT){
    				
    				getCombinedAreaModel().removeCombinedCell(oldCCArea);
    				
    				if(parentUndo != null){
    					CombinedCellUndo undo = new CombinedCellUndo(CombinedCellUndo.TYPE_REMOVE, oldCCArea);
    					parentUndo.addUndo(undo);
    				}
    			}
    			
    			getCombinedAreaModel().combineCell(newCCArea);
    			
    			if(parentUndo != null){
					CombinedCellUndo undo = new CombinedCellUndo(CombinedCellUndo.TYPE_ADD, newCCArea, getCellsModel());
					parentUndo.addUndo(undo);
				}
    			
//    			 
//    			if(parameter.getEditType() == EditParameter.COPY || parameter.getEditType() == EditParameter.BRUSH){
//    				CombinedCell newCC = (CombinedCell) cc.clone();
//    				newCC.setArea(newCCArea);
//    				//getCellsModel().addArea(newCC);
//    				getCombinedAreaModel().addCombinedCell(newCC);
//    				 
//    			} else if(parameter.getEditType() == EditParameter.CUT){
//    				//EditPlugin已处理移动单元格业务，此处不用处理。只需改变区域。
//    				cc.setArea(newCCArea);
//    				getCombinedAreaModel().clearCache();
//    			}
			}
			
			
		}
	}

	/**
	 * 插入单元时，对组合单元的处理
	 * 1: 组合单元向插入方向移动：向右移动时，如果选择区域的height和组合单元的相同，则移动，反之则删除该单元
	 * 2：删除组合单元
	 * 
	 * @param ee
	 */
	private void processInsertCellEvent(UserUIEvent ee){
		int insertType = ((Integer)(ee.getOldValue())).intValue();
        AreaPosition aimArea = (AreaPosition) ee.getNewValue();
        if(aimArea == null)
        	return;
        AreaPosition toMoveArea = InsertCellCmd.getToMoveArea(aimArea,insertType,getCellsModel());
        CombinedCell[] combineCells = getCombinedAreaModel().getCombineCells(toMoveArea);
        if(combineCells == null || combineCells.length == 0)
        	return;
        UFOTable table = getReport().getTable();
        for(CombinedCell combineCell : combineCells){
        	AreaPosition area = combineCell.getArea();       	
        	AreaPosition newArea = null;
        	boolean isAddCombine = false;
        	if(insertType == DeleteInsertDialog.CELL_MOVE_RIGHT){
        		if(area.getHeigth() == aimArea.getHeigth())
        			isAddCombine = true;
        		newArea = (AreaPosition)area.getMoveArea(0, aimArea.getWidth());       		
        	} else if(insertType == DeleteInsertDialog.CELL_MOVE_DOWN){
        		if(area.getWidth() == aimArea.getWidth())
        			isAddCombine = true;
        		newArea = (AreaPosition)area.getMoveArea(aimArea.getHeigth(), 0);   
        	} else {
        		throw new IllegalArgumentException();
        	}
        	CombineCellCmd.delCombineCell(area, table);//删除原组合单元
        	if(isAddCombine)
        		getCombinedAreaModel().combineCell(newArea);        	
        	
        }
	}
	
	/**
	 * add by 王宇光 2008-6-4 
	 * 
	 * @param UserUIEvent
	 *            e
	 * @return
	 */
	public void userActionPerformed(UserUIEvent e) {
		// 处理事件。
		switch (e.getEventType()) {
		case UserUIEvent.PASTE:
			processPasteEvent(e);
			break;
		case UserUIEvent.INSERTCELL:
			processInsertCellEvent(e);
			break;
		case UserUIEvent.DELETECELL:
		case UserUIEvent.DELETE_ROW_OR_COL:
			getCombinedAreaModel().clearCache();
			break;

		}
	}

}
 