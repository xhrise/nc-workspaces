/*
 * PrintPreViewCmd.java
 * Created on 2004-10-14 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */

package com.ufsoft.report.sysplugin.print;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

/**
 * 打印预览实现
 * @author caijie
 * @since 3.1
 */
public class PrintPreViewCmd extends UfoCommand {
	
	private UfoReport m_Rep = null;
	/**
	 * @param rep UfoReport - 报表
	 */
	public PrintPreViewCmd(UfoReport rep) {
		super();
		this.m_Rep = rep;
	}

	/* 
	 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
	 */
	public void execute(Object[] params) {
		m_Rep.getTable().printPreview(m_Rep);

	}

}
