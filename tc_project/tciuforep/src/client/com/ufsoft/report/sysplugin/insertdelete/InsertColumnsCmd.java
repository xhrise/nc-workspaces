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
 * �������в���������
 * @author CaiJie
 * @since 3.1
 */
public class InsertColumnsCmd extends UfoCommand {
	private UfoReport m_Report;
	
	/**
	 * @param rep UfoReport - ����
	 */
	public InsertColumnsCmd(UfoReport rep) {
		super();
		this.m_Report = rep;
	}
	/**
	 *  params[0]Ϊ�����е���ĿInteger���󣬲���Ϊ��ʱ��ִ�У�
	 *  params Object[] - ����
	 */
	public void execute(Object[] params) {		
		if(m_Report == null) return;	
		if(params == null) return;
		
		CellsModel model = getCellsModel();
		AreaPosition[] areas = getSelectArea();		
		if (areas == null || areas.length < 1) return;
		
//        //�ж�����ѡ�еĵ�Ԫ���Ƿ���һ��		
		int CellCol = areas[0].getStart().getColumn();		
//		for (int i  = 0; i < areas.length; i++){
//			if ((areas[i].getStart().getColumn() != CellCol)
//				||(areas[i].getEnd().getColumn() != CellCol)){
//				JOptionPane.showMessageDialog(null, MultiLang.getString("uiuforep0000857"),//��ѡ������е�λ�� 
//				        MultiLang.getString("uiuforep0000858"),JOptionPane.ERROR_MESSAGE);//�����д���
//				return;
//			}		
//		}
		
		//��ǰ�����д��������У���ǰ���Լ��������������ƫ��
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
            JOptionPane.showMessageDialog(m_Report, MultiLang.getString("uiuforep0000857"),//��ѡ������е�λ�� 
                    MultiLang.getString("uiuforep0000858"),JOptionPane.ERROR_MESSAGE);//�����д���
        }
	}
	
	protected CellsModel getCellsModel(){
		return m_Report.getCellsModel();
	}
	
	protected AreaPosition[] getSelectArea(){
		return getCellsModel().getSelectModel().getSelectedAreas();	
	}

}
