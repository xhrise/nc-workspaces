/*  
 * FileCloseExt.java 
 * Copyright 2004 Beijing Ufsoft LTD. All rights reserved.
 * create time 2004-10-14  13:26:39
 * @author CaiJie 
 * @since 3.1
 */
package com.ufsoft.report.sysplugin.file;

import java.awt.Component;

import javax.swing.KeyStroke;

import com.ufsoft.report.ReportMenuBar;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

/** 
 * 系统预制插件功能点：关闭报表
 */
public class FileCloseExt extends AbsActionExt{// implements IMainMenuExt{
	private UfoReport m_Report;
	
	/**
	 * @param rep
	 */
	public FileCloseExt(UfoReport rep){
		super();
		this.m_Report = rep;
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getName()
	 */
	public String getName() {
		return MultiLang.getString("uiuforep0000894");//关闭
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getHint()
	 */
	public String getHint() {
		return MultiLang.getString("uiuforep0000895");//关闭报表
	}

//	/* (non-Javadoc)
//	 * @see com.ufsoft.report.menu.ICommandExt#getMenuSlot()
//	 */
//	public int getMenuSlot() {
//		return ReportMenuBar.FILE_END;
//	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getImageFile()
	 */
	public String getImageFile() {
		// TODO 添加文件关闭图标，现无相应的图表资源
		return null;
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getAccelerator()
	 */
	public KeyStroke getAccelerator() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new FileCloseCmd(m_Report);
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		return null;
	}
    /*
     * @see com.ufsoft.report.plugin.IMainMenuExt#getPath()
     */
    public String[] getPath() {
        return null;
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return true;
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("uiuforep0000894"));
        uiDes.setPaths(new String[]{MultiLang.getString("file")});
        return new ActionUIDes[]{uiDes};
    }
}	

