/*
 * InsertColumnsExt.java
 * Created on 2004-10-19 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.insertdelete;

import java.awt.Component;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;

/**
 * 系统预制插件功能点：插入列
 * @author CaiJie
 * @since 3.1
 */
public class InsertColumnsExt extends AbsInsertDeleteExt{// imlements IMainMenuExt{
	/**
	 * 
	 * CaiJie 2004-10-19
	 * @param rep
	 */
	public InsertColumnsExt(UfoReport rep) {
		super(rep);
	}


	/* Overrding method
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new InsertColumnsCmd(getReport());
	}

	/**
	 * params[0]为插入列的数目Integer对象
	 */
	public Object[] getParams(UfoReport container) {
//		Object[] obj = null;
//		InsertDlg dlgInsert = new InsertDlg(InsertDlg.INSERT_COLUMNS, null);
//        dlgInsert.setModal(true);
//        dlgInsert.show();
//        
//        if(dlgInsert.getResult() == UfoDialog.ID_OK){  
//        	obj = new Integer[1];
//        	obj[0] = new Integer(dlgInsert.getNum());        	
//        }
//        
//        return obj;
		int insertRowNum = getReport().getCellsModel().getSelectModel().getSelectedArea().getWidth();
		return new Object[]{new Integer(insertRowNum)};
	}


    /*
     * 普通区域，和行选择情况下可用！
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return isFormatState() && StateUtil.isCPane1THeader(null,focusComp) &&
               getReport().getCellsModel().getSelectModel().getSelectedRow()==null &&
               ! getReport().getCellsModel().getSelectModel().isSelectAll() &&
               isSelectCol();
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("uiuforep0000862"));
        uiDes.setPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("uiuforep0000877")});
        uiDes.setGroup("insertAndFill");
//        return new ActionUIDes[]{uiDes};
         
        ActionUIDes uiDes2 = (ActionUIDes) uiDes.clone();
        uiDes2.setPaths(new String[]{MultiLang.getString("uiuforep0000877")});
        uiDes2.setPopup(true);
        uiDes2.setGroup("ViewFormat");
        return new ActionUIDes[]{uiDes,uiDes2}; 
    }
    
    /**
     * 是否在行头选择插入
     * @return
     */
    private boolean isSelectCol(){
    	AreaPosition areaSel = getReport().getCellsModel().getSelectModel().getSelectedArea();
    	if(areaSel == null){
    		return false;
    	}
    	return areaSel.getStart().getRow() == 0? true:false;
    	
    }
    
}
