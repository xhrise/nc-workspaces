package com.ufsoft.table.demo;


import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.applet.UfoApplet;
import com.ufsoft.report.sysplugin.cellattr.CellAttrPlugin;
import com.ufsoft.report.sysplugin.combinecell.CombineCellPlugin;
import com.ufsoft.report.sysplugin.dnd.DndPlugin;
import com.ufsoft.report.sysplugin.edit.EditPlugin;
import com.ufsoft.report.sysplugin.excel.ExcelImpPlugin;
import com.ufsoft.report.sysplugin.excel.ExcelExpPlugin;
import com.ufsoft.report.sysplugin.file.FilePlugin;
import com.ufsoft.report.sysplugin.fill.FillPlugin;
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
import com.ufsoft.report.sysplugin.toolbarmng.ToolBarMngPlugin;
import com.ufsoft.report.sysplugin.view.ViewMngExt;
import com.ufsoft.report.sysplugin.xml.XMLImpExpPlugin;
import com.ufsoft.report.toolbar.CellAttrToolBarPlugIn;

/**
 * ReportCore中运行的applet。
 * @author zzl 2005-5-12
 */
public class ReportApplet  extends UfoApplet{//nc.ui.pub.beans.UIApplet{
 
	private static final long serialVersionUID = -8382302990867341715L;

	public void init(){
	    int oper = 0;	
		UfoReport report = new UfoReport(oper,getInitContext());
	    initPlugins(report);
	    
//        setRootPane(report);  
        
        setUfoReport(report);
        
        String p = getParameter("DEBUG");
        if(p != null && !"".equals(p)){
        	System.setProperty("DEBUG", p);
        }
         
//        report.getCellsModel().getSelectModel().clearAnchor();
    }
	
	private void initPlugins(UfoReport report){
	    //添加插件。
	    report.addPlugIn(FilePlugin.class.getName());
	    report.addPlugIn(EditPlugin.class.getName());
	    report.addPlugIn(DndPlugin.class.getName());
	    report.addPlugIn(HeaderLockPlugin.class.getName());
	    report.addPlugIn(HelpPlugin.class.getName());
	    report.addPlugIn(CombineCellPlugin.class.getName());
	    report.addPlugIn(CellAttrPlugin.class.getName());
	    report.addPlugIn(HeaderSizePlugin.class.getName());
	    report.addPlugIn(LocationPlugin.class.getName());
	    report.addPlugIn(LogPlugin.class.getName());
	    report.addPlugIn(CellAttrToolBarPlugIn.class.getName());
	    report.addPlugIn(PrintPlugin.class.getName());
	    report.addPlugIn(StylePlugin.class.getName());
	    
	    report.addPlugIn(PostilPlugin.class.getName());
	    report.addPlugIn(PaginationPlugin.class.getName());
	    report.addPlugIn(FindReplacePlugin.class.getName());
	    report.addPlugIn(ToolBarMngPlugin.class.getName());
	    report.addPlugIn(XMLImpExpPlugin.class.getName());
	    report.addPlugIn(ExcelImpPlugin.class.getName());
	    report.addPlugIn(ExcelExpPlugin.class.getName());	    
	    report.addPlugIn(RepNamePlugin.class.getName());
	    
	    report.addPlugIn(InsertDeletePlugin.class.getName());
	    report.addPlugIn(InsertDelImgPlugin.class.getName());
	    
	    report.addPlugIn(FillPlugin.class.getName());
	    report.addActionExt(new ViewMngExt(report));

	}
	
	private ContextVO getInitContext(){
	    ContextVO context=new ContextVO();		
	    return context;
	}
}

