
package com.ufsoft.table;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.CellEditor;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import com.ufida.zior.event.EventManager;
import com.ufsoft.table.event.MouseEventAdapter;


/**
 *  CellsPaneUI的鼠标监听器
 *  @author caijie
 */
public class CellsPaneUIMouseActionHandler  implements MouseInputListener{//,MouseWheelListener{

	// 在处理鼠标事件是响应鼠标的组件，可能并不是设置为编辑器的组件（是他的子组件）
	private Component dispatchComponent;
	private boolean selectedOnPress;

	CellsPane m_cellsPane = null;
	
	//记录鼠标最后所在单元格，用于事件处理。
	private transient CellPosition m_lastPosition = null;
	
	public CellsPaneUIMouseActionHandler(CellsPane cellsPane){
	    m_cellsPane = cellsPane;
	}
	
	protected CellsPane getCellsPane(){
		return m_cellsPane;
	}
	/**
	 * @param e 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		EventManager eventManager = m_cellsPane.getEventManager();
		if(eventManager != null){
			eventManager.dispatch(new MouseEventAdapter(e));
		}		
	}

	/**
	 * 设置处理当前鼠标事件的组件.
	 * 
	 * @param e
	 */
	private void setDispatchComponent(MouseEvent e) {
		//当前编辑器组件
		Component editorComponent = getCellsPane().getTablePane().getEditorComp();
		//单元面板上的鼠标事件位置.
		Point p = e.getPoint();
		//转换为编辑器组件上的坐标.
		Point p2 = SwingUtilities.convertPoint(getCellsPane(), p, editorComponent);
		//设置处理鼠标事件的组件.
		dispatchComponent = SwingUtilities.getDeepestComponentAt(
				editorComponent, p2.x, p2.y);
	}

	//重新向需要派发鼠标事件的组件派发鼠标事件.
	private boolean repostEvent(MouseEvent e) {
		if (dispatchComponent == null || !getCellsPane().isEditing()) {
			return false;
		}
		MouseEvent e2 = SwingUtilities.convertMouseEvent(getCellsPane(), e,
				dispatchComponent);
		dispatchComponent.dispatchEvent(e2);
		return true;
	}

	//是否忽略当前的鼠标事件
	private boolean shouldIgnore(MouseEvent e) {
		return e.isConsumed() || !getCellsPane().isEnabled();
	}
	/**
	 * @param e 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		
		if (e.isConsumed()) {
			selectedOnPress = false;
		} else {
			selectedOnPress = true;
			
//			CellsModel cm = getCellsPane().getDataModel();
//			try {
//				cm.setEnableEvent(false);
			//wangyga+派发cellsPane鼠标事件，解决v56新框架中插件和cellsPane一对多问题
			EventManager eventManager = m_cellsPane.getEventManager();
			if(eventManager != null){
				eventManager.dispatch(new MouseEventAdapter(e));
			}
			
			
			adjustFocusAndSelection(e);
//				
//			} finally {
//				cm.setEnableEvent(true);
//			}
		}
		
	}

	/**
	 * 调整组件的焦点和选中模型中的选择内容.
	 * 
	 * @param e
	 */
	protected void adjustFocusAndSelection(MouseEvent e) { //调整当前选择的焦点
		if (shouldIgnore(e)) {
			return;
		}
        // @edit by wangyga at 2009-2-24,下午08:00:52
        CellsModel cellsModel = getCellsPane().getDataModel();
        if(cellsModel == null){
        	return;
        }
		Point p = e.getPoint();
		int row = getCellsPane().rowAtPoint(p);
		int column = getCellsPane().columnAtPoint(p);
		if ((column == -1) || (row == -1)) {
			return;
		}
		if(SwingUtilities.isRightMouseButton(e)){
			if(cellsModel.getSelectModel().isSelected(row, column)){
				return;
			};
			
		}  
		
		
		//结束上次编辑

		//优先执行客户定义的鼠标响应.
		if (getCellsPane().hasPriority(row, column, e)) {
			Cell c = cellsModel.getCell(row, column);
			Object oldObj = c == null ? null : c.getValue();
			Object obj = getCellsPane().priorityMouseEvent(row, column, oldObj, e);
			if (obj != oldObj) {
				c.setValue(obj);
				//调用该方法目的是触发事件响应
				cellsModel.setCell(row, column, c);
			}
			//        return;
		}
		//判断当前是否有单元编辑,如果有,将鼠标事件传递到编辑组件
		else if (getCellsPane().editCellAt(row, column, e)) {
			setDispatchComponent(e);
			repostEvent(e);
		}
//		//表格控件得到焦点.
		else if (getCellsPane().isRequestFocusEnabled()) {
			getCellsPane().requestFocus();
		}
		CellEditor editor = getCellsPane().getTablePane().getCellEditor();
		if (editor == null || editor.shouldSelectCell(e)) {
			getCellsPane().changeSelectionByUser(row, column, e.isControlDown(), e.isShiftDown(), false);
		}
	}

	/**
	 * @param e 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
				
		m_lastPosition = null;
		
//		
//		if(e.isPopupTrigger()){
//			getCellsPane().getPopupMenu().show(e);
//		}
//		
		if (selectedOnPress) {
			if (shouldIgnore(e)) {
				return;
			}
			
			//wangyga+派发cellsPane鼠标事件，解决v56新框架中插件和cellsPane一对多问题
			EventManager eventManager = m_cellsPane.getEventManager();
			if(eventManager != null){
				eventManager.dispatch(new MouseEventAdapter(e));
			}
			
			repostEvent(e);
			dispatchComponent = null;
		} else {
			adjustFocusAndSelection(e);
		}
	}

	/**
	 * @param e 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * @param e 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	//  鼠标移动的事件.
	public void mouseMoved(MouseEvent e) {	
		//wangyga+派发cellsPane鼠标事件，解决v56新框架中插件和cellsPane一对多问题
		EventManager eventManager = m_cellsPane.getEventManager();
		if(eventManager != null){
			eventManager.dispatch(new MouseEventAdapter(e));
		}		
	}

	/**
	 * 鼠标拖动的事件,目前处理区域选择.
	 * 
	 * @param e
	 */
	public void mouseDragged(MouseEvent e) {
	    if(!SwingUtilities.isLeftMouseButton(e)){
	        return;
	    }
	    // @edit by wangyga at 2009-2-24,下午08:01:04 cellsModel有可能为null
	    CellsModel cellsModel = getCellsPane().getDataModel();
	    if(cellsModel == null){
	    	return;
	    }
		Point p = e.getPoint();
		int row = getCellsPane().rowAtPoint(p);
		int column = getCellsPane().columnAtPoint(p);
		if ((column == -1) || (row == -1)) {
			return;
		}

		//处理有限表，拖拽超出区域不得绘制临时区域
		if(!cellsModel.isInfinite()){
			if(row > cellsModel.getRowNum() - 1){
				row = cellsModel.getRowNum() - 1;
			}
			if(column > cellsModel.getColNum() - 1){
				column = cellsModel.getColNum() - 1;
			}
		}
		 
 		// liuyy+, 鼠标在同一单元格，直接返回。
		CellPosition pos = CellPosition.getInstance(row, column);
		if(pos.equals(m_lastPosition)){
			return;
		}
		CombinedAreaModel cm = cellsModel.getCombinedAreaModel();
		if(m_lastPosition != null){
			CombinedCell c1 = cm.belongToCombinedCell(m_lastPosition);
			if(c1 != null && c1 == cm.belongToCombinedCell(pos)){
				return;
			}
		}

		m_lastPosition = pos;
		
		//如果新的区域和上次拖动的区域不同，将新的区域放入，将上次的区域删除。
		getCellsPane().changeSelectionByUser(row, column, e.isControlDown(), e.isShiftDown(), true);
	}
	
	

}
