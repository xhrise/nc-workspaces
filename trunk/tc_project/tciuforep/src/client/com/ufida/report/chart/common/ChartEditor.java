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
 * ͼ��༭��
 * ���ڱ�������˫��ͼ��ʱ���������������������ͼ�������ȡ�����в�����
 * ���򵯳���ǰͼ������ԶԻ���
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
//	 * <code>listenerList</code> �������б�
//	 */
//	protected EventListenerList listenerList = new EventListenerList();
//
//	/**
//	 * <code>changeEvent</code> �༭�¼�
//	 */
//	protected ChangeEvent changeEvent = null;
//	
//	/**
//	 * ���������༭�Ĵ���
//	 */
//	protected int clickCountToStart = 2;
//	
//	/**
//	 * ���
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
//	//           ����������
//	//************************************************
//
//	/**
//	 * ע�������
//	 * @param l CellEditorListener
//	 */
//	public void addCellEditorListener(CellEditorListener l) {
//		listenerList.add(CellEditorListener.class, l);
//	}
//	/**
//	 * ɾ��������.
//	 * @param l CellEditorListener
//	 */
//	public void removeCellEditorListener(CellEditorListener l) {
//		listenerList.remove(CellEditorListener.class, l);
//	}
//	/**
//	 * ȡ���༭����
//	 */
//	public void cancelCellEditing() {
//		fireEditingCanceled();
//	}
//	/**
//	 * �༭���̱�ȡ��
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
//	 * ��ȡ�༭����ֵ
//	 * @return Object
//	 */
//	public Object getCellEditorValue() {
//	//	return delegate.getCellEditorValue();
//		return null;
//	}
//	
//	/**
//	 *�¼��Ƿ���Լ���༭
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
//	 * ����true,�����༭ʱ��Ҫѡ��Ԫ.
//	 * @param anEvent EventObject
//	 * @return boolean
//	 */
//	public boolean shouldSelectCell(EventObject anEvent) {
//	    return true;
//	}
//	/**
//	 * ��ֹ�༭
//	 * @return boolean
//	 */
//	public boolean stopCellEditing() {
//	    fireEditingStopped();	    
//		return true;
//
//	}
//	/**
//	 * �༭������ֹ
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
//	 * �õ��༭��
//	 * @param table ������
//	 * @param value ��Ԫ��ԭֵ
//	 * @param isSelected ��Ԫ�Ƿ�ѡ��
//	 * @param row ������
//	 * @param column ������
//	 * @return Component �༭���
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
