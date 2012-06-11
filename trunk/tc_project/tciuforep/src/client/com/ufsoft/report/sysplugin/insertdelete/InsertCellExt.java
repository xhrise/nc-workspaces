/*
 * InsertCellExt.java
 * Created on 2004-10-19 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.insertdelete;

import java.awt.Component;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.BaseDialog;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

/**
 * 系统预制插件功能点：插入单元
 * @author CaiJie
 * @since 3.1
 */
public class InsertCellExt extends AbsInsertDeleteExt{// implements IMainMenuExt{
	/**
	 * 
	 * CaiJie 2004-10-19
	 * @param rep
	 */
	public InsertCellExt(UfoReport rep) {
		super(rep);
	}

	/* Overrding method
	 * @see com.ufsoft.report.plugin.IExtension#getCommand()
	 */
	public UfoCommand getCommand() {
		return new InsertCellCmd(this.getReport());
	}

	/* Overrding method
	 * @see com.ufsoft.report.plugin.IExtension#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		DeleteInsertDialog dialog = new DeleteInsertDialog(container,true);
	    dialog.setVisible(true);
	    if(dialog.getSelectOption()== BaseDialog.OK_OPTION){
	        return new Object[]{new Integer(dialog.getOperatorMethod())};	        
	    }else{
	        return null;//cancle时返回空值.
	    }
	}

    /*
     * @see com.ufsoft.report.plugin.IMainMenuExt#getPath()
     */
    public String[] getPath() {
        return new String[]{MultiLang.getString("uiuforep0000877")};
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return StateUtil.isFormat_CPane(getReport(),focusComp);
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("uiuforep0000879"));
        uiDes.setPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("uiuforep0000877")});
        uiDes.setGroup("insertAndFill");
        uiDes.setShowDialog(true);
        ActionUIDes uiDes2 = (ActionUIDes) uiDes.clone();
        uiDes2.setPaths(new String[]{MultiLang.getString("uiuforep0000877")});
        uiDes2.setPopup(true);
        
        return new ActionUIDes[]{uiDes,uiDes2}; 
    }
}
