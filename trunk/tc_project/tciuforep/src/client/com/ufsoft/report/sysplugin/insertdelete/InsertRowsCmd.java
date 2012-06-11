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
 * �������в���������
 * @author CaiJie
 * @since 3.1
 */
public class InsertRowsCmd extends UfoCommand {
	private UfoReport m_Report;

	/**
	 * @param rep UfoReport - ����
	 */
	public InsertRowsCmd(UfoReport rep) {
		super();
		this.m_Report = rep;
	}

	/**
	 *  params[0]Ϊ�����е���ĿInteger���󣬲���Ϊ��ʱ��ִ�У�
	 *  params Object[] - ����
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

		//�ж�����ѡ�еĵ�Ԫ���Ƿ���һ��		
		int CellRowNo = areas[0].getStart().getRow();
		//		for (int i  = 0; i < areas.length; i++){
		//			if ((areas[i].getStart().getRow() != CellRowNo)
		//				||(areas[i].getEnd().getRow() != CellRowNo)){
		//				JOptionPane.showMessageDialog(null, MultiLang.getString("uiuforep0000851"), //��ѡ������е�λ��
		//				        MultiLang.getString("uiuforep0000852"),JOptionPane.ERROR_MESSAGE);//�����д���
		//				return;
		//			}		
		//		}

		//��ǰ�����д��������У���ǰ���Լ������������ƫ��
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
					MultiLang.getString("uiuforep0000852"), //��ѡ������е�λ��
					MultiLang.getString("uiuforep0000852"),
					JOptionPane.ERROR_MESSAGE);//�����д���
		}

	}

	protected CellsModel getCellsModel() {
		return m_Report.getCellsModel();
	}

	protected AreaPosition[] getSelectArea() {
		return getCellsModel().getSelectModel().getSelectedAreas();
	}

}
