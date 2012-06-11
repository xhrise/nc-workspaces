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
 * ��ϵ�Ԫ���
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
				return isSupportPasteCombineCell(ee);// ճ��ʱ������ϵ�Ԫ�Ƿ�֧��
				
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
		 * �Ƿ�֧�ֲ��뵥Ԫ�¼�
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
	 * add by wangyga �����뵥Ԫʱ�����жϲ��뷽�����Ƿ���Ҫɾ������ϵ�Ԫ��
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
	 * add by ����� 2008-6-4 ճ����ϵ�Ԫʱ�����ܶ���ϵ�Ԫ�����ָ���
	 * 
	 * @param UserUIEvent
	 *            ee
	 * @return String
	 * @i18n miufo00122=���ܶ���ϵ�Ԫ�����ָ��ġ�
	 */
	private String isSupportPasteCombineCell(UserUIEvent ee)
			throws ForbidedOprException {
		if (ee == null) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1000496"));// �������������Ϊ��
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
	
	//������������
	private boolean validateArea(AreaPosition area){
		CombinedCell[] cells = getCombinedAreaModel().getCombineCells();
		for(CombinedCell ex: cells){
			//������������
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
		// TODO �Զ����ɷ������
		return new AbstractPlugDes(this) {

			protected IExtension[] createExtensions() {
				// TODO �Զ����ɷ������
				ICommandExt extCombineCell = new CombineCellExt(getReport());//��ϵ�Ԫ
				 //#��ʽ�˵� ��excelһ�£��Ҽ�����������ϵ�Ԫ�����˵���liuyy+
				return new IExtension[0];// { extCombineCell };
			}

		};
	}

	/**
	 * add by ����� 2008-6-4 ��ϵ�Ԫ��ճ���¼��Ĵ���
	 * 
	 * @param UserUIEvent
	 *            e
	 * @return
	 */
	private void processPasteEvent(UserUIEvent e) {
		if (e == null)
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1000496"));// �������������Ϊ��
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
			//���ճ�����������ϵ�Ԫ��ִ�����������
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
//    				//EditPlugin�Ѵ����ƶ���Ԫ��ҵ�񣬴˴����ô���ֻ��ı�����
//    				cc.setArea(newCCArea);
//    				getCombinedAreaModel().clearCache();
//    			}
			}
			
			
		}
	}

	/**
	 * ���뵥Ԫʱ������ϵ�Ԫ�Ĵ���
	 * 1: ��ϵ�Ԫ����뷽���ƶ��������ƶ�ʱ�����ѡ�������height����ϵ�Ԫ����ͬ�����ƶ�����֮��ɾ���õ�Ԫ
	 * 2��ɾ����ϵ�Ԫ
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
        	CombineCellCmd.delCombineCell(area, table);//ɾ��ԭ��ϵ�Ԫ
        	if(isAddCombine)
        		getCombinedAreaModel().combineCell(newArea);        	
        	
        }
	}
	
	/**
	 * add by ����� 2008-6-4 
	 * 
	 * @param UserUIEvent
	 *            e
	 * @return
	 */
	public void userActionPerformed(UserUIEvent e) {
		// �����¼���
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
 