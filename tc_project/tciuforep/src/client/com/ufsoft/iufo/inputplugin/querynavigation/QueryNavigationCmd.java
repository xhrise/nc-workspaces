package com.ufsoft.iufo.inputplugin.querynavigation;

import com.ufsoft.report.command.UfoCommand;

public class QueryNavigationCmd  extends UfoCommand {

	
	public void execute(Object[] params) {

		QueryNaviMenu menu = (QueryNaviMenu) params[0];
		
		menu.toFront();
	}

}
