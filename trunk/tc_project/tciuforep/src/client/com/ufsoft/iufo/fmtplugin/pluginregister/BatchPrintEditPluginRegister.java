package com.ufsoft.iufo.fmtplugin.pluginregister;

import com.ufsoft.report.PluginRegister;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.sysplugin.cellattr.CellAttrPlugin;
import com.ufsoft.report.sysplugin.combinecell.CombineCellPlugin;
import com.ufsoft.report.sysplugin.dnd.DndPlugin;
import com.ufsoft.report.sysplugin.edit.EditPlugin;
import com.ufsoft.report.sysplugin.findreplace.FindReplacePlugin;
import com.ufsoft.report.sysplugin.headerlock.HeaderLockPlugin;
import com.ufsoft.report.sysplugin.headersize.HeaderSizePlugin;
import com.ufsoft.report.sysplugin.help.HelpPlugin;
import com.ufsoft.report.sysplugin.insertdelete.InsertDeletePlugin;
import com.ufsoft.report.sysplugin.insertimg.InsertDelImgPlugin;
import com.ufsoft.report.sysplugin.location.LocationPlugin;
import com.ufsoft.report.sysplugin.log.LogPlugin;
import com.ufsoft.report.sysplugin.pagination.PaginationPlugin;
import com.ufsoft.report.sysplugin.postil.PostilPlugin;
import com.ufsoft.report.sysplugin.print.PrintPlugin;
import com.ufsoft.report.sysplugin.repname.RepNamePlugin;
import com.ufsoft.report.sysplugin.style.StylePlugin;
import com.ufsoft.report.toolbar.CellAttrToolBarPlugIn;

public class BatchPrintEditPluginRegister extends PluginRegister {

	@Override
	public void register() {
		UfoReport report = getReport();
		
	    report.addPlugIn(HeaderLockPlugin.class.getName());
	    report.addPlugIn(HelpPlugin.class.getName());
	    report.addPlugIn(CellAttrPlugin.class.getName());
	    report.addPlugIn(HeaderSizePlugin.class.getName());
	    report.addPlugIn(LocationPlugin.class.getName());
	    report.addPlugIn(LogPlugin.class.getName());
	    report.addPlugIn(CellAttrToolBarPlugIn.class.getName());
	    report.addPlugIn(PrintPlugin.class.getName());
	    report.addPlugIn(StylePlugin.class.getName());
	    
	    report.addPlugIn(PaginationPlugin.class.getName());
	    report.addPlugIn(FindReplacePlugin.class.getName());

	    report.addPlugIn(RepNamePlugin.class.getName());
	    
	    report.addPlugIn(InsertDeletePlugin.class.getName());
	    report.addPlugIn(InsertDelImgPlugin.class.getName());

	}

}
