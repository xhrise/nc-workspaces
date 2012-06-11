/*
 * InsertRowsCmd.java
 * Created on 2004-10-19 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.insertdelete;

import javax.swing.JOptionPane;

import com.sun.corba.se.pept.protocol.MessageMediator;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.exception.MessageException;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;

/**
 * 报表工具中插入行命令
 * @author CaiJie
 * @since 3.1
 */
public class InsertRowsCmd extends UfoCommand {
	private UfoReport m_Report;

	/**
	 * @param rep UfoReport - 报表
	 */
	public InsertRowsCmd(UfoReport rep) {
		super();
		this.m_Report = rep;
	}

	/**
	 *  params[0]为插入行的数目Integer对象，参数为空时不执行；
	 *  params Object[] - 参数
	 */
	public void execute(Object[] params) {
		if (m_Report == null)
			return;
		if (params == null)
			return;

		CellsModel model = getCellsModel();
		AreaPosition[] areas = getSelectArea();
		if (areas == null || areas.length < 1)
			return;

		//判断所有选中的单元格是否在一行		
		int CellRowNo = areas[0].getStart().getRow();
		//		for (int i  = 0; i < areas.length; i++){
		//			if ((areas[i].getStart().getRow() != CellRowNo)
		//				||(areas[i].getEnd().getRow() != CellRowNo)){
		//				JOptionPane.showMessageDialog(null, MultiLang.getString("uiuforep0000851"), //请选择插入行的位置
		//				        MultiLang.getString("uiuforep0000852"),JOptionPane.ERROR_MESSAGE);//插入行错误
		//				return;
		//			}		
		//		}

		//当前焦点列处插入新行，当前行以及后面的行往下偏移
		int InsertNum = 0;
		if (params != null) {
			Integer numObj = (Integer) params[0];
			if (numObj != null)
				InsertNum = numObj.intValue();
			if (InsertNum < 0)
				InsertNum = 0;
		}
		try {
			//		HeaderModel rowModel = model.getRowHeaderModel();
			model.getRowNum();
			model.getRowHeaderModel().addHeader(CellRowNo, InsertNum);
			model.getRowNum();
			//		model.addHeader(new HeaderEvent(model.getColumnHeaderModel(),
			//				Header.ROW, TableOperation.ADD,
			//				CellRowNo, InsertNum, 0));
		} catch (MessageException e) {
			throw e;
		} catch (Exception e) {
			AppDebug.debug(e);
			JOptionPane.showMessageDialog(m_Report,
					MultiLang.getString("uiuforep0000852"), //请选择插入行的位置
					MultiLang.getString("uiuforep0000852"),
					JOptionPane.ERROR_MESSAGE);//插入行错误
		}

	}

	protected CellsModel getCellsModel() {
		return m_Report.getCellsModel();
	}

	protected AreaPosition[] getSelectArea() {
		return getCellsModel().getSelectModel().getSelectedAreas();
	}

}
