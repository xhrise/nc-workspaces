/*
 * �������� 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoOpenedRepBizMenuExt;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

/**
 * 
 * �������ݵ�Html��IActionExtʵ����
 *   �������������ʽ
 * @author liulp
 *
 */
public class ExportData2HtmlExt extends AbsIufoOpenedRepBizMenuExt{
    public ExportData2HtmlExt(UfoReport ufoReport) {
        super(ufoReport);
    }
    @Override
	protected String getGroup() {
		return "impAndExp";
	}
    protected String[] getPaths() {
        return doGetExportMenuPaths();
    }

    protected String getMenuName() {
        return  MultiLangInput.getString("uiufotableinput0008");//"Html��ʽ";
    }

    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new ExportData2HtmlCmd(ufoReport);
    }
    
    public Object[] getParams(UfoReport container) {
        return null;
    }
}
