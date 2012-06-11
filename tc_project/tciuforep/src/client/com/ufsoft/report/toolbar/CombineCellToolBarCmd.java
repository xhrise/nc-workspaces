package com.ufsoft.report.toolbar;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.sysplugin.combinecell.CombineCellCmd;
import com.ufsoft.report.undo.CombinedCellUndo;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CombinedCell;

public class CombineCellToolBarCmd extends CombineCellCmd {

	public void execute(Object[] params) {
		m_rep = (UfoReport) params[0];
		m_cm = m_rep.getCellsModel();
		// AreaPosition[] selAreas = m_cm.getSelectModel().getSelectedAreas();
		AreaPosition selArea = m_cm.getSelectModel().getSelectedArea();

		// for(AreaPosition selArea: selAreas){
		if (selArea.isCell()) {
			return;
		}
		
		CombinedCellUndo undo = null;
		
		CombinedCell[] ccs = m_cm.getCombinedAreaModel().getCombineCells(
				selArea);
		if (ccs != null && ccs.length > 0) {
			// if(CombinedAreaModel.getInstance(m_cm).belongToCombinedCell(selArea.getStart())
			// != null){

			undo = new CombinedCellUndo(CombinedCellUndo.TYPE_REMOVE, selArea);
			delCombineCell(selArea, m_rep.getTable());
			

		} else {
			undo = new CombinedCellUndo(CombinedCellUndo.TYPE_ADD, selArea, m_cm);
			if(!doCombineCell(selArea, new AreaPosition[] { selArea }))
				return;
		}
		
		if(undo != null){
			m_rep.getCellsModel().fireUndoHappened(undo);			
		}
		
		m_cm.getSelectModel().setSelectedArea(selArea);
		 
	}
}
