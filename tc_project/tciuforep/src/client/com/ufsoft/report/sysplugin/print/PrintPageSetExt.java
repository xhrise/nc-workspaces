/*
 * create time 2004-10-14 11:05:49
 * @author CaiJie
 * @since 3.1
 */
package com.ufsoft.report.sysplugin.print;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

/**
 * 系统预制插件功能点：页面设置
 */
public class PrintPageSetExt extends AbsActionExt{// implements IMainMenuExt,IToolBarExt {
	private UfoReport m_report;
	
	/**
	 * 构造函数
	 * @param rep - 报表
	 */
	public PrintPageSetExt(UfoReport rep){
		m_report = rep;
	}
    /*
     * (non-Javadoc)
     * 
     * @see com.ufsoft.report.menu.ICommandExt#getCommand()
     */
    public UfoCommand getCommand() {
        return new PrintPageSetCmd(m_report);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ufsoft.report.menu.ICommandExt#getParams(com.ufsoft.report.UfoReport)
     */
    public Object[] getParams(UfoReport container) {
        return null;
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setGroup(MultiLang.getString("printToolBar"));
        uiDes1.setImageFile("reportcore/pageset.gif");
        uiDes1.setName(MultiLang.getString("PagePrintSet"));
        uiDes1.setPaths(new String[]{MultiLang.getString("file")});
        ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
        uiDes2.setTooltip(MultiLang.getString("PagePrintSet"));
        uiDes2.setPaths(new String[]{});
        uiDes2.setToolBar(true);
        return new ActionUIDes[]{uiDes1,uiDes2};
    }
}

