/*
 * 创建日期 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoOpenedRepBizMenuExt;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
/**
 * 
 * 导出数据到Excel的IActionExt实现类
 *   包含包含报表格式
 * 
 * @author liulp
 *
 */
public class ExportData2ExcelExt extends AbsIufoOpenedRepBizMenuExt{
    public ExportData2ExcelExt(UfoReport ufoReport) {
        super(ufoReport);
    }

    protected String[] getPaths() {
        return doGetExportMenuPaths();
    }

    protected String getMenuName() {
        return  MultiLangInput.getString("uiufotableinput0007");//Excel格式";
    }

    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new ExportData2ExcelCmd(ufoReport);
    }
    
    public Object[] getParams(UfoReport container) {
        return null;
    }
}
