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
 * 系统预制插件功能点：打印预览
 */
public class PrintPreViewExt extends AbsActionExt{// implements IMainMenuExt,IToolBarExt {	
	protected UfoReport m_report;
	
	/**
	 * 构造函数
	 * @param rep - 报表
	 */
	public PrintPreViewExt(UfoReport rep){
		m_report = rep;
	}
    /* (non-Javadoc)
     * @see com.ufsoft.report.menu.ICommandExt#getName()
     */
    public String getName() {
        // TODO Auto-generated method stub
        return MultiLang.getString("PrintPreview");//打印预览
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



    /* (non-Javadoc)
     * @see com.ufsoft.report.menu.ICommandExt#getImageFile()
     */
    public String getImageFile() {
        // TODO Auto-generated method stub
        return "preview.gif";
    }

   

    /* (non-Javadoc)
     * @see com.ufsoft.report.menu.ICommandExt#getAccelerator()
     */
    public KeyStroke getAccelerator() {
    	return null;
    }

    /* (non-Javadoc)
     * @see com.ufsoft.report.menu.ICommandExt#getCommand()
     */
    public UfoCommand getCommand() {       
        return new PrintPreViewCmd(m_report);
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
        uiDes1.setImageFile("reportcore/preview.gif");
        uiDes1.setName(MultiLang.getString("PrintPreview"));
        uiDes1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK));
        uiDes1.setMnemonic('V');
        uiDes1.setShowDialog(true);
        uiDes1.setPaths(new String[]{MultiLang.getString("file")});
        ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
        uiDes2.setTooltip(MultiLang.getString("PrintPreview"));
        uiDes2.setToolBar(true);
        uiDes2.setPaths(new String[]{});
        return new ActionUIDes[]{uiDes1,uiDes2};
    }
}

