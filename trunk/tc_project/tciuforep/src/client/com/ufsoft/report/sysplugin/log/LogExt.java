/*
 * 创建日期 2004-11-2
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.ufsoft.report.sysplugin.log;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

/**
 * 日志功能点
 * @author wupeng
 */
public class LogExt extends AbsActionExt{// implements IMainMenuExt,IToolBarExt {
	/**
	 * 日志对话框的实例.
	 */
	private LogWindow m_LogWin ;
	private UfoReport parent = null;

	public LogExt(UfoReport report) {
		parent = report;
	}
	
    LogWindow getLogWindow(){
    	if(m_LogWin == null){
    		m_LogWin = new LogWindow(parent);
    	}
    	return m_LogWin;
    }
	/* （非 Javadoc）
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new UfoCommand(){
			public void execute(Object[] params) {}			
		};
	}

	/* （非 Javadoc）
	 * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {		
		//显示日志对话框.
		getLogWindow().setVisible(true);
		return null;
	}
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    /**
	 * @i18n report00102=日志
	 * @i18n help=帮助
	 */
    public ActionUIDes[] getUIDesArr() {
    	ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setImageFile("reportcore/log.png");
        uiDes1.setName(MultiLang.getString("report00102"));
        uiDes1.setPaths(new String[]{MultiLang.getString("miufo1001692")});
        uiDes1.setGroup("formulaDefExt");
        return new ActionUIDes[]{uiDes1};
    }
}
  