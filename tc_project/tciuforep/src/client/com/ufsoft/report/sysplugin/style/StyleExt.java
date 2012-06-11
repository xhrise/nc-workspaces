/*
 * 创建日期 2004-11-2
 */
package com.ufsoft.report.sysplugin.style;

import java.awt.Component;

import com.ufsoft.report.ReportStyle;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.BaseDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

/**
 * 界面风格.
 * @author wupeng
 * @version 3.1
 */
public class StyleExt extends AbsActionExt{// IMainMenuExt {
	private StylePanelDialog m_dialog ;
	private StyleCommand command = null;


	/* （非 Javadoc）
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		if(command ==null){
			command = new StyleCommand();
		}
		return command;
	}

	/* （非 Javadoc）
	 * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		//首先得到当前页面显示的设置参数
		ReportStyle style = container.getReportStyle();
		if(style==null){
			style = new ReportStyle();
			container.setReportStyle(style);
		}
		//利用参数构建对话框
		if(m_dialog==null){
			m_dialog = new StylePanelDialog(container);
		}
		m_dialog.setReportStyle(style);
		m_dialog.setVisible(true);
		if(m_dialog.getSelectOption()==BaseDialog.OK_OPTION){
			style = m_dialog.getReportStyle();
			return new Object[]{container ,style};
		}else {
			return null;
		}		
		
	}
	class StyleCommand extends UfoCommand{
		public void execute(Object[] params) {
			if(params!=null){
				UfoReport report = (UfoReport) params[0];
				ReportStyle style =(ReportStyle)params[1];
				report.setReportStyle(style);
			}			
		}		
	}

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return true;
    }

    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    /**
	 * @i18n miufo1001189=显示风格
	 * @i18n format=格式
	 */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("miufo1001189"));
        uiDes.setPaths(new String[]{MultiLang.getString("format")});
        uiDes.setImageFile("reportcore/set_group.gif");
        uiDes.setGroup(MultiLang.getString("miufo1001189"));
        uiDes.setShowDialog(true);
        ActionUIDes uiDes1 = (ActionUIDes)uiDes.clone();
        uiDes1.setToolBar(true);
        uiDes1.setPaths(null);
        return new ActionUIDes[]{uiDes,uiDes1};
    }
	

}
  