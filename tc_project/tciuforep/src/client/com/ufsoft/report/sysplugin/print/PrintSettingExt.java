package com.ufsoft.report.sysplugin.print;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
/**
 * 打印设置功能点
 * @author guogang
 *
 */
public class PrintSettingExt extends AbsActionExt{ 
	private UfoReport m_report;
	 
	public PrintSettingExt(UfoReport rep){
		m_report = rep;
	} 
    public UfoCommand getCommand() {
        return new PrintSettingCmd(m_report);
    }
 
    public Object[] getParams(UfoReport container) {
        return null;
    } 
    
    /**
	 * @i18n miufo00008=打印设置
	 */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setGroup(MultiLang.getString("printToolBar"));
        uiDes1.setImageFile("reportcore/pageset.gif");
        uiDes1.setName(MultiLang.getString("miufo00008"));//MultiLang.getString("PagePrintSet") + 
        uiDes1.setPaths(new String[]{MultiLang.getString("file")});
        uiDes1.setShowDialog(true);    
        ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
        uiDes2.setTooltip(MultiLang.getString("miufo00008"));
        uiDes2.setPaths(new String[]{});
        uiDes2.setToolBar(true);
        return new ActionUIDes[]{uiDes1,uiDes2};
    }
}
 