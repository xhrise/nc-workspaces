/*
 * FileOpenCmd.java
 * Created on 2004-10-18 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.file;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.JOptionPane;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;


/**
 * �������д����л��洢�ļ���
 * @author CaiJie
 * 2004-10-18
 */
public class FileOpenCmd extends UfoCommand {
	//������
	private UfoReport m_RepTool = null;	

	/**
	 * @param  rep UfoReport - ������
	 */
	public FileOpenCmd(UfoReport rep) {
		super();
		this.m_RepTool = rep;
	}
	
	/**
	 * �����л��洢����
	 * @see com.ufsoft.iufo.reporttool.command.UfoCommand#execute(java.lang.Object[])
	 */
	public void execute(Object[] params) {	
		openSerialReport((String)params[0]);
	}
	/**
	 * �����л�����
	 * @param filePath -���л�����·��
	 * CaiJie 2004-10-19
	 */
	private void openSerialReport(String filePath){
		if (filePath == null)
			return;
		try{
			CellsModel cellsModel = null;
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
			cellsModel = (CellsModel)in.readObject();
			in.close();
			m_RepTool.getTable().setCurCellsModel(cellsModel);
		}catch(Exception e){
			AppDebug.debug(e);
			JOptionPane.showMessageDialog(m_RepTool, MultiLang.getString("uiuforep0000853"),//�򿪱���ʧ�ܣ�
			        MultiLang.getString("uiuforep0000854"), JOptionPane.ERROR_MESSAGE);//�򿪱���ʱ������ʧ��
		}
	}
	}

