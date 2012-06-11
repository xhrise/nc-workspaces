/*
 * FileCloseCmd.java
 * Created on 2004-10-19 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.file;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

/**
 * 报表工具中关闭报表命令
 * @author CaiJie
 * @since 3.1
 */
public class FileCloseCmd extends UfoCommand{
    //报表工具
	private UfoReport m_RepTool = null;
	/**
	 * @param  rep UfoReport - 报表工具
	 * CaiJie 2004-10-19
	 */
	public FileCloseCmd(UfoReport rep) {
		super();
		this.m_RepTool = rep;
	}

	/* Overrding method
	 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
	 */
	public void execute(Object[] params) {		
	    if(m_RepTool.exit())	    
			System.exit(0);
	}

}
