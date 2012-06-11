/*
 * GotoExt.java
 * Created on 2004-10-19 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.location;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.BaseDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IStatusBarExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.event.SelectEvent;

/**
 * 系统预制插件功能点:定位
 * @author CaiJie
 * @since 3.1
 */
public class GotoExt extends AbsActionExt implements IStatusBarExt{ // IMainMenuExt,IStatusBarExt{

	private UfoReport m_Report;

	/**
	 * 
	 * CaiJie 2004-10-19
	 * @param rep
	 */
	public GotoExt(UfoReport rep) {
		super();
		m_Report = rep;
	}
	
	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new GotoCommand(m_Report);
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {		
	    GotoDialog gotoDialog = new GotoDialog(m_Report);		
	    gotoDialog.show();	   
	    if(gotoDialog.getSelectOption() == BaseDialog.OK_OPTION){
	        String value = gotoDialog.getText(null);
			Object[] values = null;
			if(value!=null){
				values = new Object[]{value};				
			}
			gotoDialog.dispose();
			//add by guogang 2007-11-29 解决定位不在显示区域的单元格时由于移动屏幕只绘制新的区域引起对话框所在区域由于对话框关闭没有绘制的问题
			m_Report.getTable().getCells().repaint();
			return values;
	    }else{
	        gotoDialog.dispose();
	        return null; 
	    }	       
	}


    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    /**
	 * @i18n edit=编辑
	 */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("uiuforep0000881"));
        uiDes.setPaths(new String[]{MultiLang.getString("edit")});
        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
				KeyEvent.CTRL_MASK));
        uiDes.setGroup("locationGroup");
        uiDes.setMnemonic('G');
        uiDes.setShowDialog(true);
        return new ActionUIDes[]{uiDes};
    }

	public void initListenerByComp(final Component stateChangeComp) {
		SelectModel selectModel = m_Report.getCellsModel().getSelectModel();
		if(stateChangeComp instanceof JLabel){
			selectModel.addSelectModelListener(new SelectListener(){
				public void selectedChanged(SelectEvent e) {
					if(e.getProperty() == SelectEvent.ANCHOR_CHANGED){
						String newAnchorPos = m_Report.getCellsModel().getSelectModel().getSelectedArea().toString();
						((JLabel)stateChangeComp).setText(MultiLang.getString("uiuforep0000883")+":"+newAnchorPos);
					}
				}
				
			});
		}
	}

}
  