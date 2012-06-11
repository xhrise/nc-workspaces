package com.ufsoft.report.sysplugin.insertimg;

import java.awt.Component;

import javax.swing.ImageIcon;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.sysplugin.insertdelete.AbsInsertDeleteExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectModel;

public class DeleteImgExt  extends AbsInsertDeleteExt{

	private UfoReport m_report;
	protected DeleteImgExt(UfoReport report) {
		super(report);
		m_report = report;
	}

	@Override
	public UfoCommand getCommand() {
		return new DeleteImgCmd(this.getReport());
	}

	@Override
	public Object[] getParams(UfoReport container) { 
		
		return null;
	}
	
    public boolean isEnabled(Component focusComp) {
	    
	    CellsModel cellsModel = m_report.getCellsModel();
	    if(cellsModel == null){
	    	return false;
	    }
		SelectModel selectModel = cellsModel.getSelectModel();
	    CellPosition[] arrPos = selectModel.getSelectedCells();
	    if(arrPos == null || arrPos.length < 1){
	    	return false;
	    }
	    for (int i = 0; i < arrPos.length; i++) {
	    	CellPosition pos = arrPos[i];
			Object value = cellsModel.getCellValue(pos);
	    	if(value != null && value instanceof ImageIcon){
		    	return true;
	    	}			
		}
	    
	    return false;
    }
    
    

	/**
	 * @i18n miufo00002=É¾³ýÍ¼Æ¬
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("miufo00002"));
        uiDes.setPaths(new String[]{MultiLang.getString("edit")});
        uiDes.setGroup("insertGroup");
//        ActionUIDes uiDes2 = (ActionUIDes) uiDes.clone();
//        uiDes2.setPaths(new String[]{});
//        uiDes2.setPopup(true);
        return new ActionUIDes[]{uiDes}; 
	}

}
