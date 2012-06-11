
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
 *  CellsPaneUI����������
 *  @author caijie
 */
public class CellsPaneUIMouseActionHandler  implements MouseInputListener{//,MouseWheelListener{

	// �ڴ�������¼�����Ӧ������������ܲ���������Ϊ�༭����������������������
	private Component dispatchComponent;
	private boolean selectedOnPress;

	CellsPane m_cellsPane = null;
	
	//��¼���������ڵ�Ԫ�������¼�����
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
	 * ���ô���ǰ����¼������.
	 * 
	 * @param e
	 */
	private void setDispatchComponent(MouseEvent e) {
		//��ǰ�༭�����
		Component editorComponent = getCellsPane().getTablePane().getEditorComp();
		//��Ԫ����ϵ�����¼�λ��.
		Point p = e.getPoint();
		//ת��Ϊ�༭������ϵ�����.
		Point p2 = SwingUtilities.convertPoint(getCellsPane(), p, editorComponent);
		//���ô�������¼������.
		dispatchComponent = SwingUtilities.getDeepestComponentAt(
				editorComponent, p2.x, p2.y);
	}

	//��������Ҫ�ɷ�����¼�������ɷ�����¼�.
	private boolean repostEvent(MouseEvent e) {
		if (dispatchComponent == null || !getCellsPane().isEditing()) {
			return false;
		}
		MouseEvent e2 = SwingUtilities.convertMouseEvent(getCellsPane(), e,
				dispatchComponent);
		dispatchComponent.dispatchEvent(e2);
		return true;
	}

	//�Ƿ���Ե�ǰ������¼�
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
			//wangyga+�ɷ�cellsPane����¼������v56�¿���в����cellsPaneһ�Զ�����
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
	 * ��������Ľ����ѡ��ģ���е�ѡ������.
	 * 
	 * @param e
	 */
	protected void adjustFocusAndSelection(MouseEvent e) { //������ǰѡ��Ľ���
		if (shouldIgnore(e)) {
			return;
		}
        // @edit by wangyga at 2009-2-24,����08:00:52
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
		
		
		//�����ϴα༭

		//����ִ�пͻ�����������Ӧ.
		if (getCellsPane().hasPriority(row, column, e)) {
			Cell c = cellsModel.getCell(row, column);
			Object oldObj = c == null ? null : c.getValue();
			Object obj = getCellsPane().priorityMouseEvent(row, column, oldObj, e);
			if (obj != oldObj) {
				c.setValue(obj);
				//���ø÷���Ŀ���Ǵ����¼���Ӧ
				cellsModel.setCell(row, column, c);
			}
			//        return;
		}
		//�жϵ�ǰ�Ƿ��е�Ԫ�༭,�����,������¼����ݵ��༭���
		else if (getCellsPane().editCellAt(row, column, e)) {
			setDispatchComponent(e);
			repostEvent(e);
		}
//		//���ؼ��õ�����.
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
			
			//wangyga+�ɷ�cellsPane����¼������v56�¿���в����cellsPaneһ�Զ�����
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

	//  ����ƶ����¼�.
	public void mouseMoved(MouseEvent e) {	
		//wangyga+�ɷ�cellsPane����¼������v56�¿���в����cellsPaneһ�Զ�����
		EventManager eventManager = m_cellsPane.getEventManager();
		if(eventManager != null){
			eventManager.dispatch(new MouseEventAdapter(e));
		}		
	}

	/**
	 * ����϶����¼�,Ŀǰ��������ѡ��.
	 * 
	 * @param e
	 */
	public void mouseDragged(MouseEvent e) {
	    if(!SwingUtilities.isLeftMouseButton(e)){
	        return;
	    }
	    // @edit by wangyga at 2009-2-24,����08:01:04 cellsModel�п���Ϊnull
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

		//�������ޱ���ק�������򲻵û�����ʱ����
		if(!cellsModel.isInfinite()){
			if(row > cellsModel.getRowNum() - 1){
				row = cellsModel.getRowNum() - 1;
			}
			if(column > cellsModel.getColNum() - 1){
				column = cellsModel.getColNum() - 1;
			}
		}
		 
 		// liuyy+, �����ͬһ��Ԫ��ֱ�ӷ��ء�
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
		
		//����µ�������ϴ��϶�������ͬ�����µ�������룬���ϴε�����ɾ����
		getCellsPane().changeSelectionByUser(row, column, e.isControlDown(), e.isShiftDown(), true);
	}
	
	

}
