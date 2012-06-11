/*
 * Created on 2005-4-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.common;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.event.CellEditorListener;

import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellEditor;


/**
 * 图表编辑器
 * 当在报表工具中双击图表时，如果点击在敏感区域，则对图表进行钻取等敏感操作，
 * 否则弹出当前图表的属性对话框。
 * @author caijie
 *
 */
public class ChartEditor implements SheetCellEditor{

    /* (non-Javadoc)
     * @see com.ufsoft.table.re.SheetCellEditor#getTableCellEditorComponent(com.ufsoft.table.CellsPane, java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(CellsPane table, Object value, boolean isSelected, int row, int column) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.ufsoft.table.re.SheetCellEditor#getEditorPRI()
     */
    public int getEditorPRI() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.swing.CellEditor#cancelCellEditing()
     */
    public void cancelCellEditing() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see javax.swing.CellEditor#stopCellEditing()
     */
    public boolean stopCellEditing() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.swing.CellEditor#isCellEditable(java.util.EventObject)
     */
    public boolean isCellEditable(EventObject anEvent) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.CellEditor#shouldSelectCell(java.util.EventObject)
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.CellEditor#addCellEditorListener(javax.swing.event.CellEditorListener)
     */
    public void addCellEditorListener(CellEditorListener l) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see javax.swing.CellEditor#removeCellEditorListener(javax.swing.event.CellEditorListener)
     */
    public void removeCellEditorListener(CellEditorListener l) {
        // TODO Auto-generated method stub
        
    }
//
//	/**
//	 * <code>listenerList</code> 监听器列表
//	 */
//	protected EventListenerList listenerList = new EventListenerList();
//
//	/**
//	 * <code>changeEvent</code> 编辑事件
//	 */
//	protected ChangeEvent changeEvent = null;
//	
//	/**
//	 * 鼠标点击激活编辑的次数
//	 */
//	protected int clickCountToStart = 2;
//	
//	/**
//	 * 插件
//	 */
//	ChartPlugin m_chartPlugin = null;
//
//	public ChartEditor(ChartPlugin chartPlugin) {
//	    Assert.assertNotNull(chartPlugin);
//	    m_chartPlugin = chartPlugin;
//	}
//	
//
//	//**********************************************
//	//           监听器处理
//	//************************************************
//
//	/**
//	 * 注册监听器
//	 * @param l CellEditorListener
//	 */
//	public void addCellEditorListener(CellEditorListener l) {
//		listenerList.add(CellEditorListener.class, l);
//	}
//	/**
//	 * 删除监听器.
//	 * @param l CellEditorListener
//	 */
//	public void removeCellEditorListener(CellEditorListener l) {
//		listenerList.remove(CellEditorListener.class, l);
//	}
//	/**
//	 * 取消编辑过程
//	 */
//	public void cancelCellEditing() {
//		fireEditingCanceled();
//	}
//	/**
//	 * 编辑过程被取消
//	 * @see EventListenerList
//	 */
//	protected void fireEditingCanceled() {
//		Object[] listeners = listenerList.getListenerList();
//		for (int i = listeners.length - 2; i >= 0; i -= 2) {
//			if (listeners[i] == CellEditorListener.class) {
//				if (changeEvent == null)
//					changeEvent = new ChangeEvent(this);
//				((CellEditorListener) listeners[i + 1])
//						.editingCanceled(changeEvent);
//			}
//		}
//	}
//	
//	/**
//	 * 获取编辑器的值
//	 * @return Object
//	 */
//	public Object getCellEditorValue() {
//	//	return delegate.getCellEditorValue();
//		return null;
//	}
//	
//	/**
//	 *事件是否可以激活编辑
//	 * @param anEvent
//	 * @return boolean
//	 */
//	public boolean isCellEditable(EventObject anEvent) {
//	    if (anEvent instanceof MouseEvent) {	        
//			return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
//		}
//		return false;
//	}
//
//	/** 
//	 * 返回true,表明编辑时需要选择单元.
//	 * @param anEvent EventObject
//	 * @return boolean
//	 */
//	public boolean shouldSelectCell(EventObject anEvent) {
//	    return true;
//	}
//	/**
//	 * 终止编辑
//	 * @return boolean
//	 */
//	public boolean stopCellEditing() {
//	    fireEditingStopped();	    
//		return true;
//
//	}
//	/**
//	 * 编辑过程终止
//	 * @see EventListenerList
//	 */
//	protected void fireEditingStopped() {
//		Object[] listeners = listenerList.getListenerList();
//		for (int i = listeners.length - 2; i >= 0; i -= 2) {
//			if (listeners[i] == CellEditorListener.class) {
//				if (changeEvent == null)
//					changeEvent = new ChangeEvent(this);
//				((CellEditorListener) listeners[i + 1])
//						.editingStopped(changeEvent);
//			}
//		}
//	}
//	/**
//	 * 得到编辑器
//	 * @param table 表格组件
//	 * @param value 单元的原值
//	 * @param isSelected 单元是否被选中
//	 * @param row 所处行
//	 * @param column 所处列
//	 * @return Component 编辑组件
//	 * 
//	 */
//	public Component getTableCellEditorComponent(CellsPane table, Object value,
//			boolean isSelected, int row, int column) {	
//	    CellPosition cell = CellPosition.getInstance(row, column);
//	    ChartCell chartCell = m_chartPlugin.getReportChartModel().getChartCell(cell);
//		return chartCell.getChartPanel();
//	}
//	/**
//	 *  @see com.ufsoft.table.re.SheetCellEditor#getEditorPRI()
//	 */
//	public int getEditorPRI() {
//		return 1;
//	}
//
//

	public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
		return true;
	}
}
