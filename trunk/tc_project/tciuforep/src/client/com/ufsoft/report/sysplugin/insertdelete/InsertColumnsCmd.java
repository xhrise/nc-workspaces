/*
 * InsertColumnsCmd.java
 * Created on 2004-10-19 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.insertdelete;

import javax.swing.JOptionPane;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.TableDataModelException;

/**
 * 报表工具中插入列命令
 * @author CaiJie
 * @since 3.1
 */
public class InsertColumnsCmd extends UfoCommand {
	private UfoReport m_Report;
	
	/**
	 * @param rep UfoReport - 报表
	 */
	public InsertColumnsCmd(UfoReport rep) {
		super();
		this.m_Report = rep;
	}
	/**
	 *  params[0]为插入列的数目Integer对象，参数为空时不执行；
	 *  params Object[] - 参数
	 */
	public void execute(Object[] params) {		
		if(m_Report == null) return;	
		if(params == null) return;
		
		CellsModel model = getCellsModel();
		AreaPosition[] areas = getSelectArea();		
		if (areas == null || areas.length < 1) return;
		
//        //判断所有选中的单元格是否在一列		
		int CellCol = areas[0].getStart().getColumn();		
//		for (int i  = 0; i < areas.length; i++){
//			if ((areas[i].getStart().getColumn() != CellCol)
//				||(areas[i].getEnd().getColumn() != CellCol)){
//				JOptionPane.showMessageDialog(null, MultiLang.getString("uiuforep0000857"),//请选择插入列的位置 
//				        MultiLang.getString("uiuforep0000858"),JOptionPane.ERROR_MESSAGE);//插入列错误
//				return;
//			}		
//		}
		
		//当前焦点列处插入新列，当前列以及后面的列往后面偏移
		int InsertNum = 0;
		if (params !=null){
			Integer numObj = (Integer) params[0];
			if (numObj != null)
				InsertNum = numObj.intValue();
			if(InsertNum < 0) InsertNum = 0; 
		}
		try {
            model.getColumnHeaderModel().addHeader(CellCol, InsertNum);
//		model.addHeader(new HeaderEvent(model.getColumnHeaderModel(),
//				Header.COLUMN, TableOperation.ADD,
//				CellCol, InsertNum, 0));
        } catch (TableDataModelException e) {
            JOptionPane.showMessageDialog(m_Report, MultiLang.getString("uiuforep0000857"),//请选择插入列的位置 
                    MultiLang.getString("uiuforep0000858"),JOptionPane.ERROR_MESSAGE);//插入列错误
        }
	}
	
	protected CellsModel getCellsModel(){
		return m_Report.getCellsModel();
	}
	
	protected AreaPosition[] getSelectArea(){
		return getCellsModel().getSelectModel().getSelectedAreas();	
	}

}
