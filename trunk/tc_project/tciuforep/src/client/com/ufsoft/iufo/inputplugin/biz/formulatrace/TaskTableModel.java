package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import javax.swing.table.AbstractTableModel;

import nc.vo.iufo.task.TaskVO;
import com.ufsoft.report.util.MultiLang;

public class TaskTableModel extends AbstractTableModel{
	
	private TaskVO[] m_tasks=null;
	
	public TaskTableModel(TaskVO[] taskVOs){
		this.m_tasks=taskVOs;
		
	}
	public int getColumnCount() {
		return 1;
	}
    
	/**
	 * @i18n uiuforep00133=ÈÎÎñÃû³Æ
	 */
	public String getColumnName(int column) {
		return MultiLang.getString("uiuforep00133");
	}
	public int getRowCount() {
		if(m_tasks!=null){
			return m_tasks.length;
		}
		return 0;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(m_tasks==null||rowIndex <0 || rowIndex >= m_tasks.length){
			return null;
		}
		TaskVO returnVO=m_tasks[rowIndex];
		
		return returnVO.getName();
	}

}
 