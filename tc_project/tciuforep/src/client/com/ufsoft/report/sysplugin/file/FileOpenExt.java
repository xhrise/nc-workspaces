/*  
 * FileOpenExt.java 
 * Copyright 2004 Beijing Ufsoft LTD. All rights reserved.
 * create time 2004-10-14  13:20:42
 * @author CaiJie 
 * @since 3.1
 */
package com.ufsoft.report.sysplugin.file;


import java.awt.Component;
import java.util.EventListener;

import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

/** 
 * ϵͳԤ�Ʋ�����ܵ㣺�򿪱���
 */

public class FileOpenExt extends AbsActionExt{
	private UfoReport m_Report;
	
	/**
	 * @param rep
	 */
	public FileOpenExt(UfoReport rep){
		super();
		this.m_Report = rep;
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new FileOpenCmd(m_Report);
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {	
		//ע�⣺SysDefaultPlugin.storeĬ�ϵı�����Ŀ¼��
		//����޸Ĵ˲���ֵ����ͬʱ�޸�SysDefaultPlugin.store
		String filePath = "C:\\SeriralReport.rep";
		
		Object[] param  = {filePath};	
		return param;
	}

	/**
	 * ��ȡ�򿪵ı����·��
	 * @return String ���л������·��
	 
	private String getOpenFilePath() {
		String filePath = null;
		JFileChooser chooser = new JFileChooser();		
		chooser.setMultiSelectionEnabled(false);		
		chooser.setDialogTitle(StringResource.getStringResource("uiuforep0000891"));//�򿪱���
		chooser.setFileFilter(new RepFileFilter());
		int optionSelected = chooser.showOpenDialog(m_Report);
		if (optionSelected == JFileChooser.APPROVE_OPTION){			
			File f = chooser.getSelectedFile();
			if (!f.exists()){
				String msg = StringResource.getStringResource("uiuforep0000892");//�ļ�����Ŀ¼�����Բ���ָ�����ļ�				
				JOptionPane.showMessageDialog(null, msg, StringResource.getStringResource("uiuforep0000893"),JOptionPane.ERROR_MESSAGE);//�򿪱���			
			}	
			else{
				filePath = f.getAbsolutePath();	
			}				
		}
		return filePath;		
	}	*/
    /*
     * @see com.ufsoft.report.plugin.IMainMenuExt#getPath()
     */
    public String[] getPath() {
        return null;
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return true;
    }
    /*
     * @see com.ufsoft.report.plugin.IActionExt#getUIDes()
     */
    public ActionUIDes getUIDes() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setImageFile("reportcore/openfile.gif");
        return uiDes;
    }
  
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("uiuforep0000889"));
        uiDes.setPaths(new String[]{MultiLang.getString("file")});
        return new ActionUIDes[]{uiDes};
    }
}

