/* 
 * create time 2004-10-14  11:13:42
 * @author CaiJie 
 * @since 3.1
 */
package com.ufsoft.report.sysplugin.print;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

/**
 * 系统预制插件功能点：打印
 */
public class PrintExt extends AbsActionExt{// implements IMainMenuExt,IToolBarExt {
	protected UfoReport m_report;
	
	/**
	 * 构造函数
	 * @param rep - 报表
	 */
	public PrintExt(UfoReport rep){
		m_report = rep;
	}
    /* (non-Javadoc)
     * @see com.ufsoft.report.menu.ICommandExt#getName()
     */
    public String getName() {
        // TODO Auto-generated method stub
        return MultiLang.getString("print");//打印
    }

    /* (non-Javadoc)
     * @see com.ufsoft.report.menu.ICommandExt#getHint()
     */
    public String getHint() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.ufsoft.report.menu.ICommandExt#getToolBarGroup()
     */
    public String getToolBarGroup() {
        // TODO Auto-generated method stub
        return null;
    }

//    /* (non-Javadoc)
//     * @see com.ufsoft.report.menu.ICommandExt#getMenuSlot()
//     */
//    public int getMenuSlot() {
//        // TODO Auto-generated method stub
//        return ReportMenuBar.FILE_END;
//    }

    /* (non-Javadoc)
     * @see com.ufsoft.report.menu.ICommandExt#getImageFile()
     */
    public String getImageFile() {
        // TODO Auto-generated method stub
        return "print.gif";
    }

    /* (non-Javadoc)
     * @see com.ufsoft.report.menu.ICommandExt#getAccelerator()
     */
    public KeyStroke getAccelerator() {
    	return KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_MASK);
    }

    /* (non-Javadoc)
     * @see com.ufsoft.report.menu.ICommandExt#getCommand()
     */
    public UfoCommand getCommand() {
        // TODO Auto-generated method stub
        return new PrintCmd(m_report);
    }

    /* (non-Javadoc)
     * @see com.ufsoft.report.menu.ICommandExt#getParams(com.ufsoft.report.UfoReport)
     */
    public Object[] getParams(UfoReport container) {
        // TODO Auto-generated method stub
        return null;
    }
    /*
     * @see com.ufsoft.report.plugin.IMainMenuExt#getPath()
     */
    public String[] getPath() {
        return null;
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setGroup(MultiLang.getString("printToolBar"));
        uiDes1.setImageFile("reportcore/print.gif");
        uiDes1.setName(MultiLang.getString("print"));
        uiDes1.setPaths(new String[]{MultiLang.getString("file")});
        uiDes1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
        uiDes1.setMnemonic('P');
        uiDes1.setShowDialog(true);
        ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
        uiDes2.setTooltip(MultiLang.getString("print"));
        uiDes2.setPaths(new String[]{});
        uiDes2.setToolBar(true);
        return new ActionUIDes[]{uiDes1,uiDes2};
    }
}

