/*
 * FileSaveCmd.java
 * Created on 2004-10-18 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.file;


import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.component.ProcessController;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

/**
 * �����������л��洢�ļ����洢�ļ���׺Ϊ*.rep
 * @author CaiJie
 * @since 3.1
 */
public class FileSaveCmd extends UfoCommand {
	//������
	private UfoReport m_RepTool = null;

	/**	
	 * @param  rep UfoReport - ������
	 */
	public FileSaveCmd(UfoReport rep) {
		super();
		this.m_RepTool = rep;
	}
//	/**
//	 * @return
//	 * @see com.ufsoft.iufo.reporttool.command.UfoCommand#isPermit()
//	 */
//	 public boolean isPermit(){	 	
//        return m_RepTool.isDirty();
//    }
	/**
	 * ִ�д洢���л�����
	 * @param params
	 * @see com.ufsoft.iufo.reporttool.command.UfoCommand#execute(java.lang.Object[])
	 */
	public void execute(Object[] params) {
		ProcessController controller = new ProcessController(true);
		controller.setRunnable(new Runnable() {
			public void run() {
				m_RepTool.store();;							
			}
		});
		controller.setString(MultiLang.getString("save"));
		m_RepTool.getStatusBar().setProcessDisplay(controller);
		m_RepTool.getStatusBar().setHintMessage(StringResource.getStringResource("miufopublic391"));
	}
}

