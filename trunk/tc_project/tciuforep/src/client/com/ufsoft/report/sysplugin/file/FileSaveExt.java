/*  
 * FileSaveExt.java 
 * Copyright 2004 Beijing Ufsoft LTD. All rights reserved.
 * create time 2004-10-14  13:26:39
 * @author CaiJie 
 * @since 3.1
 */
package com.ufsoft.report.sysplugin.file;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;

/** 
 * 系统预制插件功能点：保存报表
 */
public class FileSaveExt extends AbsActionExt{// implements IMainMenuExt,IToolBarExt{
	private UfoReport m_Report;
	
	/**
	 * @param rep
	 */
	public FileSaveExt(UfoReport rep){
		super();
		this.m_Report = rep;
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new FileSaveCmd(m_Report);
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		return null;
	}
	
	@Override
	public void initListenerByComp(Component stateChangeComp) {
		m_Report.getCellsModel().getSelectModel().addSelectModelListener(new SelectListener(){

			public void selectedChanged(SelectEvent e) {
				// TODO Auto-generated method stub
				m_Report.getStatusBar().repaint();
			}
			
		});
	}
	
	/**
	 * 获取报表保存的路径
	 * @return String 文件保存的路径
	
	private String getSavePath() {
		String filePath = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);		
		chooser.setDialogTitle(StringResource.getStringResource("uiuforep0000886"));//保存报表
		chooser.setFileFilter(new RepFileFilter());
		int optionSelected = chooser.showSaveDialog(m_Report);
		if (optionSelected == JFileChooser.APPROVE_OPTION){
			File f = chooser.getSelectedFile();			
			if (f.exists()){
				String msg = StringResource.getStringResource("uiuforep0000887");//文件已经存在，要替换它吗？
				int result = 0;
				result = JOptionPane.showConfirmDialog(chooser, msg, StringResource.getStringResource("uiuforep0000888"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);//保存报表
				if (result == JOptionPane.OK_OPTION)
					filePath = f.getAbsolutePath();				
			}else{//检查文件后缀是否为".rep"
				filePath = f.getAbsolutePath();
				if (!f.getName().toLowerCase().endsWith(".rep"))
					filePath = filePath + ".rep";	
			}
		}
		return filePath;
	}	 */

    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setImageFile("reportcore/savefile.gif");
        uiDes1.setName(MultiLang.getString("uiuforep0000884"));
        uiDes1.setPaths(new String[]{MultiLang.getString("file")});
        uiDes1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
        uiDes2.setPaths(new String[]{});
        uiDes2.setToolBar(true);
        uiDes2.setGroup(MultiLang.getString("file"));
        return new ActionUIDes[]{uiDes1,uiDes2};
    }
    public UfoReport getReport(){
    	return m_Report;
    }
}	

