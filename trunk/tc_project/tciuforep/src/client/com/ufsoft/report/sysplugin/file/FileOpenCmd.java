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
 * 报表工具中打开序列化存储文件。
 * @author CaiJie
 * 2004-10-18
 */
public class FileOpenCmd extends UfoCommand {
	//报表工具
	private UfoReport m_RepTool = null;	

	/**
	 * @param  rep UfoReport - 报表工具
	 */
	public FileOpenCmd(UfoReport rep) {
		super();
		this.m_RepTool = rep;
	}
	
	/**
	 * 打开序列化存储报表。
	 * @see com.ufsoft.iufo.reporttool.command.UfoCommand#execute(java.lang.Object[])
	 */
	public void execute(Object[] params) {	
		openSerialReport((String)params[0]);
	}
	/**
	 * 打开序列化报表
	 * @param filePath -序列化报表路径
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
			JOptionPane.showMessageDialog(m_RepTool, MultiLang.getString("uiuforep0000853"),//打开报表失败！
			        MultiLang.getString("uiuforep0000854"), JOptionPane.ERROR_MESSAGE);//打开报表时出错，打开失败
		}
	}
	}

