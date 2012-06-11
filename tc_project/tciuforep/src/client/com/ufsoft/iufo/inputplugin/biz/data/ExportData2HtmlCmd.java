/*
 * 创建日期 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.report.UfoReport;
/**
 * 导出数据到HTML页的UfoCommand
 *   包含包含报表格式
 * 
 * @author liulp
 *
 */
public class ExportData2HtmlCmd extends AbsIufoBizCmd{

    protected ExportData2HtmlCmd(UfoReport ufoReport) {
        super(ufoReport);
    }

    protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
        IInputBizOper inputMenuOper = new InputBizOper(ufoReport);
        inputMenuOper.performBizTask(ITableInputMenuType.MENU_TYPE_EXPORT_HTML);         
    }

}
