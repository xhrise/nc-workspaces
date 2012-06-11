/*
 * PrintPageSetCmd.java
 * Created on 2004-10-14 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.print;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

/**
 * @author CaiJie
 * @since 3.1
 */
public class PrintPageSetCmd extends UfoCommand{

	private UfoReport m_Rep = null;
	/**
	 * @param rep UfoReport - ±¨±í
	 */
	public PrintPageSetCmd(UfoReport rep) {
		super();
		this.m_Rep = rep;
	}

	/* 
	 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
	 */
	public void execute(Object[] params) {
		m_Rep.getTable().pageFromat();
	}


}
