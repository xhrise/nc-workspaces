package com.ufsoft.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.event.EventManager;
import com.ufida.zior.event.EventService;
import com.ufida.zior.view.IDockingContainer;
import com.ufida.zior.view.Viewer;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.undo.CellValueUndo;
import com.ufsoft.table.IVerify.VerifyType;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.header.HeaderModel;
import com.ufsoft.table.re.BorderPlayRender;
import com.ufsoft.table.re.CellRenderAndEditor;
import com.ufsoft.table.re.PriorityMouseHandle;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * <p>
 * Title: ��ʾ���Ԫ�������
 * </p>
 * <p>
 * Description: �������һ�����ؼ�����ʾ���Ԫ�Ĳ��֡�
 */

public class CellsPane extends JComponent implements Scrollable,
		CellsModelListener, CellEditorListener, SelectListener {

	private static final long serialVersionUID = 7444496238277292689L;

	// *********************����*********************//
	// ����ģ�Ͳ��֣�
	/** ��������ģ�͡���ģ���ڹ�������ʱ�򱻳�ʼ��������Ϊ�գ�����ά����������ģ�͵����� */
	private CellsModel dataModel;

	// �������Բ��֣�
	/** �����ߵ���ɫ */
	private Color gridColor;

	/** �ӿ�Scrollableʹ�ã��ṩ��������Ĵ�С */
	private Dimension preferredViewportSize;
	/** CellsPane��Ӧ��TablePane */
	private TablePane m_tablePane;// ����Ϊ������CellsPane������cp��TablePane������tp��
									// UFOTable���table��

	/**
	 * ��Ⱦ���ͱ༭���Ļ������
	 */
	private CellRenderAndEditor renderAndEditor;

	/** �Ƿ���ʾ���б�ǩ */
	private boolean showRowHeader, showColHeader;
	
	private IDockingContainer m_container = null;
	
	private UfoReport m_report = null;

	
	// ��CellsPane�ֲ�����չ�������ͱ༭��
	//v55��ʱ��������ʹ�þֲ�CellRenderAndEditor��v6�ع��� liuyy+
	private List<SheetCellRenderer> local_extSheetCellRenderer = new Vector<SheetCellRenderer>();
	private List<SheetCellEditor> local_extSheetCellEditor = new Vector<SheetCellEditor>();
		
	// @edit by ll at 2009-5-14,����10:51:43 ����˽��״̬�����ȼ����
	private int m_operationState = -1;

	/**
	 * �Ҽ��˵�
	 */
	// private CellsPanePopupMenu m_popupMenu = null;

	// *********************���캯��*******************//
	/**
	 * ����������������
	 * 
	 * @param dataModel
	 *            ����ģ��
	 * @param showRowHeader
	 *            ��ʾ�б���
	 * @param showColHeader
	 *            ��ʾ�б���
	 */
	public CellsPane(CellsModel dataModel, boolean showRowHeader,
			boolean showColHeader) {
		// ÿ��ʵ��CellsPaneʱ���ָ���ʾ����ȱʡֵ����֤��ʾ�������ò�Ӱ�������ٴ򿪵ı���liuyy. 2007-04-26
		TableStyle.setViewScale(DefaultSetting.DEFAULT_VIEWSCALE);

		this.showRowHeader = showRowHeader;
		this.showColHeader = showColHeader;
		setDataModel(dataModel);
		init();

		// m_popupMenu = new CellsPanePopupMenu();
	}

	// public CellsPanePopupMenu getPopupMenu(){
	// return m_popupMenu;
	// }

	/**
	 * ��ʼ����������������ʼ���༭������Ⱦ�������õ�ǰ�����UI
	 */
	private void init() {
		setOpaque(true);
		renderAndEditor = CellRenderAndEditor.getInstance();//new CellRenderAndEditor();
		setUI(CellsPaneUI.createUI(this));
	}

	// *********************UI����***********************************
	/**
	 * �õ���ǰ�����UI
	 * 
	 * @return ��ǰ�����UI
	 */
	public CellsPaneUI getUI() {
		return (CellsPaneUI) ui;
	}

	/**
	 * ���õ�ǰ�����UI
	 * 
	 * @param ui
	 *            ��ǰ�����UI
	 */
	public void setUI(CellsPaneUI ui) {
		if (this.ui != ui) {
			super.setUI(ui);
			repaint();
		}
	}

	/**
	 * ���õ�ǰCellsPane��Ӧ��TablePane��
	 * 
	 * @param tp
	 *            TablePane
	 */
	public void setTablePane(TablePane tp) {
		if (m_tablePane != tp) {
			m_tablePane = tp;
		}
	}

	/**
	 * �õ���CellsPane��Ӧ��TablePane��
	 * 
	 * @return TablePane
	 */
	public TablePane getTablePane() {
		return m_tablePane;
	}

	// *************************************************
	// ������ԵĲ���
	// *****************************************************
	/**
	 * ���ñ�������ģ�͡�
	 * �����ǰ�������оɵ�����ģ����ɾ��������ģ���ϵ�CellsModelListener��SelectModelListener
	 * 
	 * @param newModel
	 *            CellsModel �µı�������ģ�͡�
	 */
	public void setDataModel(CellsModel newModel) {
		if (newModel == null) {
//			throw new IllegalArgumentException();
			return;
		}
		if (newModel != this.dataModel) {
			
			//���undo������
			newModel.resetUndoManager();
			
			CellsModel oldModel = this.dataModel;
			this.dataModel = newModel;
			// ж�ؾ�ģ���ϵ����еļ���������ӵ��µ�ģ����
			if (oldModel != null) {
				oldModel.removeCellsModelListener(this);
				oldModel.getSelectModel().removeSelectModelListener(this);
			}
			newModel.addCellsModelListener(this);
			if (getSelectionModel() != null) {
				getSelectionModel().addSelectModelListener(this);
			}

		}
		 
		// ��֤������ʱ���С��������ʱ��Ϊ��ʱ���в����������ϳ��ֿհ�����
		if (this.dataModel.isInfinite()) {
			Dimension screenDim = TableStyle.getScreenDim();
			int minRowSize = screenDim.height / TableStyle.ROW_HEIGHT + 1;
			int minColSize = screenDim.width / TableStyle.COLUMN_WIDTH + 1;
			
			boolean oldDirty= this.dataModel.isDirty();
			HeaderModel rowModel = this.dataModel.getRowHeaderModel();
			HeaderModel colModel = this.dataModel.getColumnHeaderModel();
			if (minRowSize > rowModel.getTempCount()) {
				rowModel.setTempCount(minRowSize);
				this.dataModel.setDirty(oldDirty);
			}
			if (minColSize > colModel.getTempCount()) {
				colModel.setTempCount(minColSize);
				this.dataModel.setDirty(oldDirty);
			}
		}
		
		
		// Ϊ�˼򻯴��룬�Ͳ������Ըı��֪ͨ�ˡ�
		resizeAndRepaint();
	}

	/**
	 * �õ���������ģ�͡�
	 * 
	 * @return CellsModel ��������ģ�͡�
	 */
	public CellsModel getDataModel() {
		return dataModel;
	}

	// /**
	// * �����Ҽ��˵�
	// *
	// * @param pMenu
	// * @deprecated ֱ��ʹ�ø����add
	// */
	// public void addPopupMenu(JPopupMenu pMenu) {
	// if (m_popup != null) {
	// remove(m_popup);
	// }
	// this.m_popup = pMenu;
	// if (m_popup != null) {
	// add(m_popup);
	// }
	// }
	/**
	 * �Ƿ���ʾ�б���
	 * 
	 * @return boolean
	 */
	public boolean isShowRowHeader() {
		return showRowHeader;
	}

	/**
	 * �Ƿ���ʾ�б���
	 * 
	 * @return boolean
	 */
	public boolean isShowColHeader() {
		return showColHeader;
	}

	/**
	 * ���ñ�����������ɫ
	 * 
	 * @param gridColor
	 *            ������������ɫ
	 * @exception IllegalArgumentException
	 *                ��������Ϊ��
	 */
	public void setGridColor(Color gridColor) {
		checkParam(gridColor);
		if (!gridColor.equals(this.gridColor)) {
			this.gridColor = gridColor;
			repaint();
		}
	}

	/**
	 * �õ���������ɫ
	 * 
	 * @return Color ��������ɫ
	 */
	public Color getGridColor() {
		return gridColor;
	}

	/**
	 * Ϊ��ǰ��һ�����������ö�дȨ�޵�ģ�͡�
	 * 
	 * @param newAuth
	 *            ��Ԫ�Ķ�дȨ�޹���
	 */
	public void setCellsAuthorization(CellsAuthorization newAuth) {
		CellsAuthorization auth = getDataModel().getCellsAuth();
		if (newAuth == auth) {
			return;
		}
		getDataModel().setCellsAuth(newAuth);
	}

	// /**
	// * ���ĵ�ԪȨ�ޡ�����ǰӦ��ע����Ҫ���ȳ�ʼ��auth�ֶΡ�
	// *
	// * @param row
	// * @param col
	// * @param type
	// */
	// public void authorize(int row, int col, int type) {
	// CellsAuthorization auth = getDataModel().getCellsAuth();
	// if (auth == null) {
	// return;
	// }
	// auth.authorize(row, col, type);
	// //�˴���Ӵ����޸���Ӧ�ĵ�Ԫ��
	// getDataModel().freshCell(row, col);
	// }

	/**
	 * �õ��ؼ��ϱ༭������Ⱦ����
	 * 
	 * @return CellRenderAndEditor
	 */
	public CellRenderAndEditor getReanderAndEditor() {
		return renderAndEditor;
	}

	/**
	 * �õ�ѡ��ģ��
	 * 
	 * @return SelectModel
	 */
	public SelectModel getSelectionModel() {
		return dataModel.getSelectModel();
	}

	/**
	 * �õ���������ȱʡ�ߴ�
	 * 
	 * @return Dimension
	 */
	public Dimension getPreferredSize() {
		// ��������ޱ�����Ҫ��֤ȱʡ�ı�ռ�������㣬����������϶�����ô��Ҫ������ʱ�ı�ռ������㡣
		// ��������ޱ����ĳߴ�����и��п���ܺ�
		CellsModel cellsModel = getDataModel();
		if(cellsModel == null){
			return new Dimension(0,0);
		}
		HeaderModel rowM = cellsModel.getRowHeaderModel();
		HeaderModel colM = cellsModel.getColumnHeaderModel();

		return new Dimension(colM.getTotalSize(), rowM.getTotalSize());
	}

	// ***********************************************
	// ����Ϊ�����˽�з���
	// *********************************************
	/**
	 * ���������Ϊ��
	 * 
	 * @param o
	 */
	private void checkParam(Object o) {
		if (o == null) {
			throw new IllegalArgumentException();
		}
	}

	// ***********************************************
	// ���·�����Ϊ��ʹ�õı���,�������õ�����ģ�ͽ��в���
	// *********************************************
	/**
	 * �õ���Ԫ����
	 * 
	 * @param row
	 *            �к�
	 * @param col
	 *            �к�
	 * @return Cell
	 */
	public Cell getCell(int row, int col) {
		return dataModel.getCell(row, col);
	}

	/**
	 * �ж�ĳ����Ԫ�Ƿ�ѡ��
	 * 
	 * @param row
	 * @param col
	 * @return boolean
	 */
	public boolean isCellSelected(int row, int col) {
		return getSelectionModel().isSelected(row, col);
	}

	/**
	 * �ж�ĳ����Ԫ�Ƿ���ѡ��ģ���е�ê�㡣
	 * 
	 * @param row
	 * @param col
	 * @return boolean
	 */
	public boolean isCellAnchor(int row, int col) {
		return getSelectionModel().isAnchorCell(row, col);
	}

	/**
	 * �õ�ĳһ�еĿ�ȡ�
	 * 
	 * @param col
	 *            ��
	 * @return int
	 */
	public int getColumnWidth(int col) {
		HeaderModel m = getDataModel().getColumnHeaderModel();
		return m.getSize(col);
		// Header h = m.getHeader(col);
		// return h == null ? m.getPreferredSize() : h.getSize();
	}

	/**
	 * �õ��м��
	 * 
	 * @return int
	 */
	public int getRowMargin() {
		return getDataModel().getRowHeaderModel().getMargin();
	}

	/**
	 * �õ��м��
	 * 
	 * @return int
	 */
	public int getColumnMargin() {
		return getDataModel().getColumnHeaderModel().getMargin();
	}

	/**
	 * �õ�ģ���а������е�����
	 * 
	 * @return int
	 */
	public int getRowCount() {
		return dataModel.getRowNum();
	}

	/**
	 * �õ�ģ���а������е�����
	 * 
	 * @return int
	 */
	public int getColumnCount() {
		return dataModel.getColNum();
	}

	/**
	 * �õ��и�
	 * 
	 * @param index
	 * @return int
	 */
	public int getRowHeight(int index) {
		HeaderModel m = dataModel.getRowHeaderModel();
		return m.getSize(index);
		// Header h = m == null ? null : m.getHeader(index);
		// return h == null ? m.getPreferredSize() : h.getSize();
	}

	/**
	 * �õ�ĳһ�е�����
	 * 
	 * @param column
	 *            �к�
	 * @return Object ����
	 */
	public Object getColumnValue(int column) {
		Header h = getDataModel().getColumnHeaderModel().getHeader(column);
		return h == null ? null : h.getValue();
	}

	/**
	 * �õ�ĳһ�е�����
	 * 
	 * @param row
	 *            �к�
	 * @return Object ����
	 */
	public Object getRowValue(int row) {
		Header h = getDataModel().getRowHeaderModel().getHeader(row);
		return h == null ? null : h.getValue();
	}

	/**
	 * ���������жϵ�ǰλ������һ�С�Խ�緵�أ�1��
	 * 
	 * @param point
	 *            ��������
	 * @return int ���������жϵ�ǰλ������һ�С�Խ�緵�أ�1��
	 */
	public int columnAtPoint(Point point) {
		return getDataModel().getColumnHeaderModel()
				.getIndexByPosition(point.x);
	}

	/**
	 * ���������жϵ�ǰλ������һ��,Խ�緵�أ�1��ע�������ޱ������У���ʱ���в�����������ģ���У� ������Ҫ�ж���ʱ���е�λ�á�
	 * 
	 * @param point
	 *            ��������
	 * @return ���������жϵ�ǰλ������һ�С�Խ�緵�أ�1��
	 */
	public int rowAtPoint(Point point) {
		return dataModel.getRowHeaderModel().getIndexByPosition(point.y);
	} 
	
	

	/**
	 * �õ�ĳ����Ԫ������������
	 * 
	 * @param area
	 *            ��Ԫ������A3:A7��
	 * @param includeSpacing
	 *            �Ƿ��б���
	 * @return Rectangle �õ�Ԫ�ľ����������أ�
	 */
	public Rectangle getCellRect(IArea area, boolean includeSpacing) {
		return getDataModel().getCellRect(area, includeSpacing);
	}

	// ***********************************************************//
	// ����ѡ��ģ��
	// ******************************************************
	/**
	 * ѡ�����е�Ԫ
	 */
	public void selectAll() {
		// ����е�Ԫ�ڱ༭����Ҫֹͣ
		if (isEditing()) {
			removeEditor();
		}
		getSelectionModel().setSelectAll(true);
	}

	// ***********************�������ģ�ͽӿڵ�ʵ��**********************

	// *************************���ڱ༭�Ĵ���*****************************
	/**
	 * ��Ԫ�Ƿ���Ա༭.��Ҫ�жϵ�ǰ��Ԫ�Ƿ�����ϵ�Ԫ�����У�����ǣ���Ҫ�ж������ϵ�Ԫ���Ͻǡ�
	 * 
	 * @param row
	 *            �к�
	 * @param col
	 *            �к�
	 * @return boolean
	 */
	public boolean isCellEditable(int row, int col) {
		CellsModel model = getDataModel();
		CombinedCell cc = model.getCombinedAreaModel().belongToCombinedCell(row, col);
		if (cc != null) {
			row = cc.getArea().getStart().getRow();
			col = cc.getArea().getStart().getColumn();
		}

		if (isCellReadable(row, col)) {
			CellsAuthorization auth = getDataModel().getCellsAuth();
			return auth == null ? true : auth.isWritable(row, col);
		} else {
			return false;
		}
	}

	private boolean isCellReadable(int row, int col) {
		CellsAuthorization auth = getDataModel().getCellsAuth();
		return auth == null ? true : auth.isReadable(row, col);
	}

	/**
	 * �༭ĳ����Ԫ
	 * 
	 * @param row
	 *            ��
	 * @param column
	 *            ��
	 * @exception IllegalArgumentException
	 * @return boolean ����޷��༭������false��
	 */
	public boolean editCellAt(int row, int column) {
		return editCellAt(row, column, null);
	}

	/**
	 * �༭ĳ����Ԫ���Ǳ༭��������Ҫ���õķ��� ������̣���ȡ�༭��Ԫ(����Ǻϲ���Ԫ���úϲ���Ԫ����ʼ��Ԫ)�ı༭������ע�����
	 * ����prepareEditor������ȡ�õ�Ԫ�ı༭�������������༭�߽� ���ö�Ӧ��tablePane�ı༭�����༭������༭�к��е�
	 * 
	 * @param row
	 *            ��
	 * @param column
	 *            ��
	 * @param e
	 *            �༭�¼�(�絥Ԫֵ�ı䣬��Ԫ��ʽ�ı��)
	 * @exception IllegalArgumentException
	 * @return boolean ����޷��༭������false��
	 */
	public boolean editCellAt(int row, int column, EventObject e) {
		CellPosition pos = CellPosition.getInstance(row,
				column);
		
		// ��ϵ�Ԫ�������׵�Ԫ��ת��Ϊ�׵�Ԫ��
		CombinedCell cc = getDataModel().getCombinedAreaModel().belongToCombinedCell(row, column);
		if (cc != null) {
			row = cc.getArea().getStart().getRow();
			column = cc.getArea().getStart().getColumn();
		}
		// �༭���ǿգ������жϵ�ǰ�༭�ɹ���
		SheetCellEditor oldEditor = m_tablePane.getCellEditor();
		if (oldEditor != null && !oldEditor.stopCellEditing()) {
			return false;
		}
		if (!isCellEditable(row, column)) {
			return false;
		}
		SheetCellEditor editor = getCellEditor(pos);
		if (editor != null && editor.isCellEditable(e)) {
			Component editorComp = prepareEditor(editor, row, column);
			if (editorComp == null) {
				removeEditor();
				return false;
			}
			Rectangle cellRect = getCellRect(pos, false); // ���������cellsPane
			if (cc != null && !this.getParent().getBounds().contains(cellRect)) {// ��Ҫ����Ӧ��ϵ�Ԫ�����������
																					// ��
				cellRect = SwingUtilities.convertRectangle(this, cellRect,
						m_tablePane);
				cellRect = cellRect.intersection(this.getParent().getBounds());
				cellRect = SwingUtilities.convertRectangle(m_tablePane,
						cellRect, this);
			}
			// add by ����� 2008-4-2 �������߿򶯻�Ч���Ļ����������ƶ���ֹͣ
			BorderPlayRender.stopPlay(this);
			// end
			editorComp.setBounds(cellRect);
			add(editorComp);
			editorComp.validate();
			m_tablePane.setEditorComp(editorComp);
			m_tablePane.setCellEditor(editor);
			m_tablePane.setEditingRow(row);
			m_tablePane.setEditingColumn(column);
			editor.addCellEditorListener(this);
			return true;
		}
		return false;
	}

	/**
	 * �Ƿ��е�Ԫ���ڱ༭
	 * 
	 * @return boolean
	 */
	public boolean isEditing() {
		return (m_tablePane.getCellEditor() == null) ? false : true;
	}

	/**
	 * �жϵ�ǰ������ĵ�Ԫ�Ƿ��տͻ�������������¼�.
	 * 
	 * @param row
	 *            �к�
	 * @param col
	 *            �к�
	 * @param oldValue
	 *            Object ԭ�������ڵ�ǰ��Ԫ��Value
	 * @param mEvent
	 *            ����¼�
	 * @return Object �¼����������
	 */
	public Object priorityMouseEvent(int row, int col, Object oldValue,
			MouseEvent mEvent) {
		PriorityMouseHandle p = getPriorityMouseEvent();
		return p == null ? null : p.priorityMouseEvent(row, col, oldValue,
				mEvent);
	}

	/**
	 * �жϵ�ǰ����¼��Ƿ����ȴ���
	 * 
	 * @param row
	 *            ���λ��
	 * @param col
	 *            ���λ��
	 * @param e
	 *            ����¼�
	 * @return boolean
	 */
	public boolean hasPriority(int row, int col, MouseEvent e) {
		PriorityMouseHandle p = getPriorityMouseEvent();
		return p == null ? false : p.hasPriority(row, col, e);
	}

	/**
	 * �����û������������ȴ����¼�
	 * 
	 * @param newPriority
	 */
	public void setPriorityMouseEvent(PriorityMouseHandle newPriority) {
		getDataModel().setPriorityMouseEvent(newPriority);
	}

	/**
	 * �õ��û������������ȴ����¼�
	 * 
	 * @return PriorityMouseHandle
	 */
	public PriorityMouseHandle getPriorityMouseEvent() {
		return getDataModel().getPriorityMouseEvent();
	}

	//
	// **************************CellEditorListener�ӿ�ʵ��*************
	//

	/**
	 * �༭���̽�����Ĵ���������CellEditorListener���¼������� ������̣�
	 * ����setValueAt()�����õ�Ԫ���ֵΪ�༭����ֵ removeEditor()���Ƴ�tablePane�ı༭��
	 * �����Ҫ�Զ����д��������getFitHeaderSize()
	 * 
	 * @param e
	 *            ChangeEvent
	 */
	public void editingStopped(ChangeEvent e) {
		SheetCellEditor editor = m_tablePane.getCellEditor();
		if (editor == null) {
			return;
		}
		
		Object value = editor.getCellEditorValue();
		final CellPosition pos = CellPosition.getInstance(m_tablePane
				.getEditingRow(), m_tablePane.getEditingColumn());
		
		setValueAt(value, pos.getRow(), pos.getColumn());
		
		removeEditor();
		
		IufoFormat format = (IufoFormat) getDataModel().getCellFormat(pos);
		if (format != null && format.isFold()) {// �����Զ�����ʱ�ĸ߶ȡ�
			int preferRowHeight = getFitHeaderSize(true, pos.getRow());
			getDataModel().getRowHeaderModel().getHeader(pos.getRow())
					.autoSetSize(preferRowHeight);
		}
		
	}

	/**
	 * �༭ȡ����ʱ�����
	 * 
	 * @param e
	 *            ChangeEvent
	 */
	public void editingCanceled(ChangeEvent e) {
		removeEditor();
	}

	/**
	 * ���õ�Ԫֵ
	 * 
	 * @param value
	 * @param row
	 * @param col
	 */
	public void setValueAt(Object value, int row, int col) {
		CellPosition pos = CellPosition.getInstance(row, col);

		Object oldValue = dataModel.getCellValue(pos);

//		Cell oldCell = dataModel.getCell(pos);
		 
		dataModel.setCellValue(row, col, value);
		
//		Cell newCell = dataModel.getCell(pos);

		CellValueUndo ue = new CellValueUndo(pos, oldValue, value);
		
		dataModel.fireUndoHappened(ue);

	}

	// *********************�ӿ�Scrollable��ʵ��*******************//
	/**
	 * ���������ʾ���ӿڴ�С
	 * 
	 * @param size
	 *            Dimension �����ʾ���ӿڴ�С.
	 */
	public void setPreferredScrollableViewportSize(Dimension size) {
		preferredViewportSize = size;
	}

	/**
	 * �õ������ʾ���ӿڴ�С
	 * 
	 * @return �����ʾ���ӿڴ�С
	 */
	public Dimension getPreferredScrollableViewportSize() {
		return preferredViewportSize;
	}

	/**
	 * �õ�ÿ�ι���һ����λʱ�ƶ������ش�С���÷������û�ÿ������һ����λ�ƶ�ʱ����
	 * 
	 * @param visibleRect
	 *            Rectangle �ӿڵĿɼ������С
	 * @param orientation
	 *            int �ƶ�����SwingConstants.VERTICAL��SwingConstants.HORIZONTAL
	 * @param direction
	 *            int �ϡ����ƶ�Ϊ�������¡����ƶ�Ϊ����
	 * @return int �ƶ�������
	 * @see Scrollable#getScrollableUnitIncrement
	 */
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		JViewport view = (JViewport) getParent();
		if (view == null) {
			return 20;
		} else {
			Point vP = view.getViewPosition();
			// �õ���ǰ��ͼ���Ͻǵ�Ԫ�����꣬Ȼ����ݸõ�Ԫ�Ĵ�С�����ƶ��Ĵ�С
			boolean bHor = orientation == SwingConstants.HORIZONTAL;
			HeaderModel headerModel = bHor ? getDataModel()
					.getColumnHeaderModel() : getDataModel()
					.getRowHeaderModel();
			int pos = bHor ? vP.x : vP.y;
			int index = 0;
			int newPos = 0;
			if (direction > 0) {
				index = headerModel.getIndexByPosition(pos) + 1;
				newPos = headerModel.getPosition(index);
				return newPos - pos;
				
			} else {
				index = headerModel.getIndexByPosition(pos) - 1; 
				if(index < 0){
					return 0;
				}
				newPos = headerModel.getPosition(index);
				return pos - newPos;
			}
		}
	}

	/**
	 * �õ��ƶ�һ������ʱ���ƶ��ľ��롣ͨ�����ƶ�һҳ�����Է��ؿɼ�����ĸ߶Ȼ��߿��
	 * 
	 * @param visibleRect
	 * @param orientation
	 * @param direction
	 * @return int �ƶ�һҳ�ľ���
	 */
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// ���������Ҫ�޸ģ������ƶ���ʱ��Ӧ���Ǳ�֤ÿ�����ϽǶ���һ�������ĵ�Ԫ
		int n = 0;
		switch (orientation) {
		case SwingConstants.VERTICAL:
			n = visibleRect.height;
			break;
		case SwingConstants.HORIZONTAL:
			n = visibleRect.width;
			break;

		default:
			break;
		}
		return n;
	}

	/**
	 * �ӿڵĿ���Ƿ��ɱ��Ŀ�Ⱦ���������������ޱ��ʱ�򣬷���false;���ӿ������ޱ��ʱ�� ��Ҫ���ݱ�����޸ĳߴ�ʱ�Ĺ��������
	 * 
	 * @return boolean �ӿڵĿ���Ƿ��ɱ��Ŀ�Ⱦ���
	 */
	public boolean getScrollableTracksViewportWidth() {
		// ��Ҫ�޸Ĵ˴����롣
		return false;
	}

	/**
	 * �ӿڵĸ߶��Ƿ��ɱ��ĸ߶Ⱦ���������������ޱ���Ƿ񣬷���false;���ӿ������ޱ��ʱ�� ��Ҫ���ݱ�����޸ĳߴ�ʱ�Ĺ��������
	 * 
	 * @return boolean �ӿڵĸ߶��Ƿ��ɱ��ĸ߶Ⱦ���
	 */
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	/**
	 * ���²��ֻ���
	 */
	private void resizeAndRepaint() {
		revalidate();
		repaint();
	}
	
	public void requestFocus(){
//		AppDebug.debug("CellsPane.requestFocus()");
		super.requestFocus();
	}

	public void repaint() {
		// AppDebug.debug("CellsPane�ر�repaint�������á�");
		super.repaint();
	}

	/**
	 * �õ���ĳ����Ԫ���л��Ƶ���� ������̣� �����ж��Ƿ��ж���Ȩ�ޣ� Ȼ���жϸõ�Ԫ�Ƿ�ѡ��,�Ƿ���ѡ���¼�����ʼ�㣻
	 * �õ���ǰλ�õ�Ԫʹ�õ�������Ⱦ��(������Ⱦ������չ�����Ⱦ������չ��ʽ��Ⱦ������Ԫֵ���Ͷ�Ӧ����Ⱦ��)��
	 * �õ������������Ⱦ����Ӧ�Ļ������Component
	 * 
	 * @param row
	 *            the row of the cell to render, where 0 is the first row
	 * @param column
	 *            the column of the cell to render, where 0 is the first column
	 * @param bPrint
	 * @return the <code>Component</code> under the event location
	 */
	public Component[] prepareRenderer(int row, int column, boolean bPrint) {
		Cell cell = getCell(row, column);

		// if(c == null || c.getFormat() == null){
		// return null;
		// }

		Object value = cell == null ? null : cell.getValue();
		// �Ƿ����
		boolean bRead = isCellReadable(row, column);
		boolean isSelected = isCellSelected(row, column) && !bPrint;
		boolean isAnchor = isCellAnchor(row, column) && !bPrint;
		ArrayList<Component> list = new ArrayList<Component>();

		SheetCellRenderer renderer = null;
		Component com = null;

		// liuyy, ��ײ���Ʊ���ɫ��Ϊ�����չ�����������б���������, ����Ԫ�񱳾���ֵ�ֿ�����.
		com = CellRenderAndEditor.getBackgroundRenderer().getCellRendererComponent(
				this, value, isSelected, isAnchor, row, column, cell);
		list.add(com);

		boolean isHasKeyOrMeasure = false;

		//��չ������
		SheetCellRenderer[] extRenderers = getExtSheetRender();
		for (SheetCellRenderer scr : extRenderers) {
			Component eachCom = scr.getCellRendererComponent(this, value,
					isSelected, isAnchor, row, column, cell);
			if (eachCom != null) {
				list.add(eachCom);
				isHasKeyOrMeasure = true;
			}
		}
		
		// ����ڸ�ʽ���״̬���ڱ���߿������ùؼ��ֺ�ָ�겻�ܴ�ӡ��ʾ������ modify by guangang 2007-11-6
		if (cell != null) {
			if (isHasKeyOrMeasure) {
				cell.setPrint(true);
			} else {
				cell.setPrint(false);
			}
		}
		// modify end

		// ��չ��ʽ
		if (bRead && cell != null && cell.getExtFmtSize() > 0) {
			String[] extNames = cell.getExtNames();
			for (int i = 0; i < extNames.length; i++) {
				Object extFmt = cell.getExtFmt(extNames[i]);
				if (extFmt == null) {
					continue;
				}
				if (bPrint
						&& extFmt instanceof ICellVerify
						&& ((ICellVerify) extFmt).isVerify(cell,
								VerifyType.UNSHOW_WHEN_PRINT)) {
					continue;
				}
				renderer = renderAndEditor.getRender(extNames[i]);

				Component aCom = renderer.getCellRendererComponent(this, cell
						.getExtFmt(extNames[i]), isSelected, isAnchor, row,
						column, cell);
				if (aCom != null) {
					list.add(aCom);
				}
			}
		}

		// liuyy, ����Ԫ���ֵ�����ơ�
		if (bRead) {
			// add by guogang 2007-7-3 ��Ԫ���������������ʽ����Double Render
			Format format = getDataModel().getFormat(
					CellPosition.getInstance(row, column));
			if (format != null && format.isCondition()) {
				renderer = renderAndEditor.getRender(Double.class);
			} else {
				renderer = renderAndEditor.getRender(value == null ? null
						: value.getClass());
			}
		} else {
			renderer = CellRenderAndEditor.getUnreadableRenderer();
		}
		com = renderer.getCellRendererComponent(this, value, isSelected,
				isAnchor, row, column, cell);
		list.add(com);

		return (Component[]) list.toArray(new Component[0]);
	}

	/**
	 * �õ�ĳ���ϲ���ϵ�Ԫ�Ļ��ƿؼ�. ���˱߿���Ķ�����
	 * 
	 * @param cc
	 *            CombinedCell
	 * @param bPrint
	 * @return Component[]
	 */
	public Component[] prepareRenderer(AreaCell cc, boolean bPrint) {
		// TODO ��Ҫ��ϸ�޸ģ�
		if (cc instanceof CombinedCell) {
			int row = cc.getArea().getStart().getRow();
			int column = cc.getArea().getStart().getColumn();
			return prepareRenderer(row, column, bPrint);
		} else {
			return null;
		}

	}

	// public Component prepareRenderer(CombinedCell areaData) {
	// CellPosition cellStart = areaData.getArea().getStart();
	// boolean bRead = auth.isReadable(cellStart.getRow(),
	// cellStart.getColumn());
	// SheetCellRenderer renderer = bRead ?
	// renderAndEditor.getRender(areaData.getValue()) :
	// renderAndEditor.getUnreadableRenderer();
	// boolean isSelected = isCellSelected(cellStart.getRow(),
	// cellStart.getColumn());
	// boolean IsAnchor = isCellAnchor(cellStart.getRow(),
	// cellStart.getColumn());
	// return renderer.getCellRendererComponent(this, areaData.getValue(),
	// isSelected, IsAnchor,
	// cellStart.getRow(),
	// cellStart.getColumn());
	// }

	
	/**
	 * �õ�ĳ��λ�õı༭��
	 */
	public SheetCellEditor getCellEditor(CellPosition pos) {
		
		SheetCellEditor editor = CellRenderAndEditor.getInstance().getEditor(
				getDataModel(), pos);
		
		if(getOperationState() == ReportContextKey.OPERATION_INPUT){
			return editor;
		}
		SheetCellEditor[] arrExtSheetEditors = getExtSheetEditor();
		for (int i = 0; i < arrExtSheetEditors.length; i++) {
			SheetCellEditor aEditor = arrExtSheetEditors[i];
			if (aEditor != null
					&& editor.getEditorPRI() < aEditor.getEditorPRI()
					&& aEditor.isEnabled(getDataModel(), pos)) {
				editor = aEditor;
			}
		}
		return editor;
	}

	/**
	 * Ϊĳ����Ԫ׼���༭���
	 * 
	 * @param editor
	 *            SheetCellEditor �༭��
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 * @return Component �༭���
	 */
	public Component prepareEditor(SheetCellEditor editor, int row, int column) {
		Cell c = getCell(row, column);
		Object value = c == null ? null : c.getValue();
		boolean isSelected = isCellSelected(row, column);
		Component comp = editor.getTableCellEditorComponent(this, value,
				isSelected, row, column);
		if (comp instanceof JComponent) {
			JComponent jComp = (JComponent) comp;
			if (jComp.getNextFocusableComponent() == null) {
				jComp.setNextFocusableComponent(this);
			}
		}
		if (!isCellEditable(row, column)) {
			comp.setEnabled(false);
		}
		return comp;
	}

	/**
	 * ɾ��tablePane�ı༭�����������༭������༭�к���.�༭���̽��������
	 */
	public void removeEditor() {
		SheetCellEditor editor = m_tablePane.getCellEditor();
		Component editorComp = m_tablePane.getEditorComp();
		if (editor != null) {
			editor.removeCellEditorListener(this);
			if (editorComp != null) {
				remove(editorComp);
			}
			int editingRow = m_tablePane.getEditingRow(), editingCol = m_tablePane
					.getEditingColumn();
			m_tablePane.setCellEditor(null);
			m_tablePane.setEditingColumn(-1);
			m_tablePane.setEditingRow(-1);
			m_tablePane.setEditorComp(null);
			repaint(getCellRect(CellPosition
					.getInstance(editingRow, editingCol), false));
			this.requestFocus();
		}
	}

	/**
	 * 
	 * @param row
	 * @param col
	 */
	private void repaintCell(int row, int col) {
		if (row < 0)
			row = 0;

		if (col < 0)
			col = 0;

		repaint(CellPosition.getInstance(row, col), true);
	}

	// *********************������ע��*******************//
	// *********************�ӿ�SelectListener��ʵ��*******************//
	/**
	 * ѡ��ı䣨���ê��ı䣬ѡ��Ԫ�ı䣩�Ĵ���������SelectListener���¼������� ֻ��������޸����򣬲������ƶ���ͼ��
	 */
	public void selectedChanged(SelectEvent e) {
		if (e == null) {
			return;
		}
		
		//�����¿�� liuyy+
		EventManager eventManager = getEventManager();
		if(eventManager != null){//hr����Ԥ���Ӧ��eventManager����Ϊnull
			eventManager.dispatch(e);
		}
		
		// System.out.println(DateUtil.getCurTimeWithMillisecond() + ": " +
		// e.getProperty());
		if (e.getProperty().equals(SelectEvent.ANCHOR_CHANGED)) {
			JViewport viewPort = (JViewport) getParent();
			if (viewPort == null) { // ��֤��ͼ�п��Կ�����ǰ�Ľ���.
				return;
			}

			repaintCell(e.getOldAnchor().getRow(), e.getOldAnchor().getColumn());
			
			// repaintCell(e.getNewAnchorRow(),e.getNewAnchorCol());
			// viewPort.repaint();
			// end

		} else if (e.getProperty().equals(SelectEvent.SELECTED_CHANGE)) {
			//			
			// //�õ����ѡ��������뽹��Խǵĵĵ�Ԫ��λ�á�
			// CellPosition anchorCell = getSelectionModel().getAnchorCell();
			// AreaPosition dragSelectArea =
			// getSelectionModel().getDragSelectedArea();
			// if (dragSelectArea != null) {
			// CellPosition start = dragSelectArea.getStart();
			// CellPosition end = dragSelectArea.getEnd();
			// int row = start.getRow() == anchorCell.getRow() ? end
			// .getRow() : start.getRow();
			// int col = start.getColumn() == anchorCell.getColumn() ? end
			// .getColumn() : start.getColumn();
			// moveViewToDisplayRect(CellPosition.getInstance(row, col),true);
			// }

			
			
			AreaPosition[] changedArea = e.getChangedArea();
			if (changedArea == null) {
				return;
			}
			
			for (AreaPosition area : changedArea) {


				repaint(area, true);
			}
		}

	}

	/**
	 * �ı���ͼλ�ã�ʹ����ʾ���򣨲�������λ��
	 * 
	 * @param area
	 *            ����
	 * @param includeSpacing
	 *            �Ƿ�����߿�
	 */
	public void moveViewToDisplayRect(IArea area, boolean includeSpacing) {
		Rectangle rect = getCellRect(area, includeSpacing);
		moveViewToDisplayRect(rect);
		 
		
	}

	public void repaint(IArea[] areas, boolean includeSpacing) {
		for (IArea a : areas) {
			repaint(a, includeSpacing);
		}
	}

	/**
	 * ���»�������ķ���,��ֹ����CellsPane.repaint()����,��Ҫ��Խ�紦��
	 * 
	 * @param area
	 *            ����
	 * @param includeSpacing
	 *            �Ƿ�����߿�
	 */
	public void repaint(IArea area, boolean includeSpacing) {

		// ��ִ�л��ơ�liuyy+
		if (!getDataModel().isEnableEvent()) {
			AppDebug.debug("CellsModel��ֹ���ƣ�" + area);
			return;
		}
		
		// ��֤������ʱ���С�
		if (getDataModel().isInfinite()) {
			int row = area.getEnd().getRow(), col = area.getEnd().getColumn();
			HeaderModel rowModel = getDataModel().getRowHeaderModel();
			HeaderModel colModel = getDataModel().getColumnHeaderModel();
			if (row >= rowModel.getTempCount()) {
				rowModel.setTempCount(row + 1);
			}
			if (col >= colModel.getTempCount()) {
				colModel.setTempCount(col + 1);
			}
		}
		

		// ��ʼ���ơ�
		Graphics g1 = this.getGraphics();
		if (g1 == null) {
			return;
		}
		Rectangle paintRect = null;
		Rectangle viewRect = getViewRect();
		Rectangle rect = getCellRect(area, includeSpacing);

		// add by guogang 2008-4-21 Ϊ��֤������Ƶ�ʱ���ܹ�����ϡ�����ߣ�Ҫ����������򵽰������ϵĵ�Ԫ
		rect.x -= 1;
		rect.y -= 1;
		rect.width += 1;
		rect.height += 1;
 
		if (viewRect != null) {
			paintRect = rect.intersection(viewRect);
		} else {
			paintRect = rect;
		}
		
//		AppDebug.debug("��������" + area + ";��ֵ����" + paintRect);
		
		// ȷ�����������������ֻ������ȷ����
		if (paintRect.width > 0 && paintRect.height > 0) {
			g1.setClip(paintRect);
			getUI().paint(g1, this);
		}

	}
	/**
	 * ��ȡ��������
	 * @return
	 */
	private Rectangle getViewRect(){
		JViewport view = null;
		Rectangle viewRect = null;
		if (this.getParent() instanceof JViewport) {
			view = (JViewport) this.getParent();
		}
		if (view != null) {
			viewRect = view.getViewRect();
		}
		return viewRect;
		
	}

	/**
	 * �ı���ͼλ�ã�ʹ����ʾ���Σ���������λ�á� ���Σ��������������ͼ�������ϽǶ�����ʾ�� modify by guogang
	 * 2007-12-13 ��CellsPane����ǰ����SelectModel.setAnchorCell()����selectChange�¼�
	 * �����𲻱�Ҫ�����¶�λ���⣬�繫ʽ׷�ٵ�ָ�궨λ����
	 * 
	 * @param rect
	 *            Rectangle
	 */
	private void moveViewToDisplayRect(Rectangle rect) {
		JViewport view = (JViewport) this.getParent();
		SeperateLockSet lockset = getDataModel().getSeperateLockSet();

		Rectangle viewRect = view.getViewRect();
		if (viewRect.contains(rect)) {
			return;
		}
		// add by guogang
		if (!lockset.isFreezing()
				&& (viewRect.width == 0 || viewRect.height == 0)) {
			view.setViewPosition(new Point(rect.x, rect.y));
		} else {
			TablePane pane = (TablePane) view.getParent();
			view=pane.getRightDownView();
			if (view==null)
				view=pane.getDownView();
			if (view==null)
				view=pane.getRightView();
			if (view==null)
				view=pane.getMainView();
			viewRect = view.getViewRect();
			Rectangle unionRect = rect.union(viewRect);
			int dx = (viewRect.x != unionRect.x) ? (unionRect.x - viewRect.x)
					: (unionRect.width - viewRect.width);
			int dy = (viewRect.y != unionRect.y) ? (unionRect.y - viewRect.y)
					: (unionRect.height - viewRect.height);
			Point p = view.getViewPosition();
			
			if (pane != null
					&& pane.isFreezing()
					&& (pane.getSeperateRow() != 0 || pane.getSeperateCol() != 0)) {
				// �ƶ���ͼλ�á�
				int newX = Math.max(p.x + dx, pane.getSeperateX());
				int newY = Math.max(p.y + dy, pane.getSeperateY());
				if (view == pane.getMainView()) {
					// ��ǰ��ͼ������ͼʱ�ƶ����Ҫ�ӷ������������
					if (pane.getRightView() == null
							&& pane.getDownView() == null) {// δ����
						view.setViewPosition(new Point(newX, newY));
					} else if (pane.getRightView() != null
							&& pane.getDownView() == null) {// ���ҷ���
						view.setViewPosition(new Point(0, newY));
					} else if (pane.getRightView() == null
							&& pane.getDownView() != null) {// ���·���
						view.setViewPosition(new Point(newX, 0));
					} else {// �������Ҷ�������
						view.setViewPosition(new Point(0, 0));
					}
				}
				if (view == pane.getRightView()) {
					if (pane.getDownView() != null) {
						view.setViewPosition(new Point(newX, 0));
					} else {
						view.setViewPosition(new Point(newX, newY));
					}
				}
				if (view == pane.getDownView()) {
					if (pane.getRightView() != null) {
						view.setViewPosition(new Point(0, newY));
					} else {
						view.setViewPosition(new Point(newX, newY));
					}
				}
				if (view == pane.getRightDownView()) {
					view.setViewPosition(new Point(newX, newY));
				}
				// ȷ��������Խ��seperate��ʱת�����������
				// rect = SwingUtilities.convertRectangle(this,rect,pane);
				// rect =
				//SwingUtilities.convertRectangle(pane,rect,pane.getMainView());
				newX = rect.x;
				newY = rect.y;
				if (newX >= pane.getSeperateX() && pane.getRightView() != null) { // ����Ӧ��������ͼ����������ͼ
					if (newY >= pane.getSeperateY()
							&& pane.getDownView() != null) {
						// ��Ϊÿһ����ͼ�����յ�selectedChange�¼�������ֻ�н������Լ�ʱ����Ӧ��
						if (this == pane.getRightDownView().getView()
								&& !this.isEditing()) {
							this.requestFocus();
						}
					} else {
						if (this == pane.getRightView().getView()
								&& !this.isEditing()) {
							this.requestFocus();
						}
					}
				} else { // ����Ӧ��������ͼ��������ͼ
					if (newY >= pane.getSeperateY()
							&& pane.getDownView() != null) {
						if (this == pane.getDownView().getView()
								&& !this.isEditing()) {
							this.requestFocus();
						}
					} else {
						if (this == pane.getMainView().getView()
								&& !this.isEditing()) {
							this.requestFocus();
						}
					}
				}
			} else {
				view.setViewPosition(new Point(p.x + dx, p.y + dy));
			}

		}
		

		paginalData();
		
		// if(pane.getDownView() != null){
		// pane.getDownView().requestFocus();
		// }
		// if(pane.getRightView() != null){
		// pane.getRightView().requestFocus();
		// }
		// if(pane.getColumnHeader2() != null){
		// pane.getColumnHeader2().requestFocus();
		// }
		//				
		// view.requestFocus();
	}

	// *********************�ӿ�CellsModelListener��ʵ��*******************//

	/*
	 * ��Ԫ��ı���¼����������ӿ�CellsModelListener��ʵ��
	 * @
	 * @param event CellsEvent
	 * 
	 * @see
	 * com.ufsoft.table.CellsModelListener#cellsChanged(com.ufsoft.table.CellsEvent
	 * )
	 * modify by guogang 2009-3-30 ����5.6���¼�����
	 */
	public void cellsChanged(CellsEvent event) {
		
		if (dataModel.isEnableEvent() == false) {
			return;
		}
		//���������ɷ�
		EventManager eventManager = getEventManager();
		if(eventManager != null){//hr����Ԥ���Ӧ��eventManager����Ϊnull
			eventManager.dispatch(event);
		}
			
		SheetCellEditor oldEditor =null;
		if(m_tablePane!=null){
			oldEditor= m_tablePane.getCellEditor();
		}
		
		if (oldEditor != null && event != null
				&& event.getMessage() == CellsEvent.SIZE_CHANGED) {
			oldEditor.stopCellEditing();
		}
		if (oldEditor != null && event != null
				&& event.getMessage() == CellsEvent.MARGIN_CHANGED) {
			oldEditor.stopCellEditing();
		}
		int type = event.getMessage();
		setSize(getPreferredSize());
		if (type == CellsEvent.VALUE_CHANGED) {
			IArea area = event.getArea();
			if (area != null) {
				// modify by guogang 2008-4-30 �����������뵥Ԫ���ʱ����߻��Ƶ�����
				Rectangle rect = getCellRect(area, true);
				rect.x--;
				rect.y--;
				rect.width++;
				rect.height++;
				repaint(rect);
			}
		} else {// if (type == CellsEvent.SIZE_CHANGED || type ==
				// CellsEvent.MARGIN_CHANGED) {
			resizeAndRepaint();
		}

	}

	// *********************�ӿ�CellEditorListener��ʵ��*******************//
	// *********************˽�з���*******************//

	/**
	 * �û�UI����Ķ������ı����ѡ��ģ�͡�������ģ�ͷ�����¼�����������ʽҪ��ȫѡ���µ�ѡ��ı䣩�� �����ƶ���ͼ����������ơ�
	 * 
	 * @param row
	 *            ��
	 * @param col
	 *            ��
	 * @param isControlDown
	 *            Control���Ƿ���
	 * @param isShiftDown
	 *            Shift���Ƿ���
	 * @param bDrag
	 *            �Ƿ����϶�
	 */
	public void changeSelectionByUser(int row, int col, boolean isControlDown,
			boolean isShiftDown, boolean bDrag) {

		getSelectionModel().changeSelectionByUser(row, col, isControlDown,
				isShiftDown, bDrag);

		moveViewToDisplayRect(CellPosition.getInstance(row, col), true);

	}
	
/**
 * ��ҳ�������
 * liuyy
 */
	public void paginalData(){
		 
		int state = getOperationState();
		if(state == ReportContextKey.OPERATION_FORMAT){
			return;
		}
		
		CellsModel dataModel = getDataModel();
		if(dataModel == null){// @edit by wangyga at 2009-2-24,����08:30:42
			return;
		}
		Rectangle viewRect =  getVisibleRect(); 
		
		int rMin = dataModel.getRowHeaderModel().getIndexByPosition(viewRect.y);
		int rMax = dataModel.getRowHeaderModel().getIndexByPosition(viewRect.y + viewRect.height);
  		
		if(rMin < 0){
			rMin = 0;
		}
		if(rMax < rMin){
			rMax = rMin;
		}
		AreaPosition area = AreaPosition.getInstance(CellPosition.getInstance(
				rMin, 0), CellPosition.getInstance(rMax, 255));
		 
		ExAreaModel eaModel = ExAreaModel.getInstance(dataModel);

		eaModel.paginalData(area);
		
		
	}

	/**
	 * ���ﷵ��һ���͵�ǰ�������ģ��,ѡ��ģ��,����ģ����ȫ��ͬ�����
	 * 
	 * @return Object
	 */
	public Object clone() {
		CellsPane cp = new CellsPane(dataModel, showRowHeader, showColHeader);
		cp.setRenderAndEditor(renderAndEditor);
		cp.setTablePane(getTablePane());
		cp.setActionMap(getActionMap());
		cp.setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT));
		cp.setPrivateOperationState(this.m_operationState);
		return cp;
	}

	/**
	 * ���ظ���ļ��̴����������ȵ�ǰ���������������¼��Ƿ��Ѿ��󶨣�����ǰ��ո���ķ�������
	 * ��˿��Դ�����InputMap�ж���ļ������롣����������һ���ַ����룬��ʱ��Ҫ���ǰ��Ԫ�ı༭����
	 * ����������¼��������ı༭�������Ŀ������ĳ����Ԫ�û��ַ�ʱ������༭����
	 * �÷�����Jdk1.3�ϲ���Ч��������汾֮��super.processKeyBinding���ɼ���
	 * 
	 * @param ks
	 * @param e
	 * @param condition
	 * @param pressed
	 * @return boolean
	 */
	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
			int condition, boolean pressed) {
		// ���ȵ���ȱʡ�ķ���,ֻ�Ǽ��Keymap���Ƿ�����Ӧ�ļ����¼��İ�.
		// �������ݿ��Ը��ݵ�ǰ��Jdk�汾�л�.
		boolean retValue = super.processKeyBinding(ks, e, condition, pressed);// ����ֻ������KeyPress��WHEN_ANCESTOR_OF_FOCUSED_COMPONENT�����
																				// ��
		// �������¼�û�б����ദ��,���Ҳ��ǵ�ǰ�����ý��������¼�,�����洦��.
		// ��Ҫ�ǵõ���ǰ��Anchor,�����ڸ�λ����ӱ༭���.

		// add at 2006-9-21,�����ڵ�Ԫ���ݱ༭״̬�����Ⱥŵ�����ʽ���������
		if (!retValue && condition == WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
				&& m_tablePane != null && m_tablePane.getEditorComp() != null
				&& m_tablePane.getEditorComp().hasFocus()
				&& e.getKeyChar() == '=')
			return true;

		if (!retValue
				&& // ������û�д���
				condition == WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
				&& // �����㽹�������
				hasFocus()
				&& // �����μ���SHIFT����û�����μ�
				(e.getModifiers() == InputEvent.SHIFT_MASK || e.getModifiers() == 0)
				&& // ������ActionKey
				!e.isActionKey() && e.getKeyChar() != KeyEvent.VK_DELETE
				&& e.getKeyChar() != KeyEvent.VK_ENTER// ������DELETE��.
				&& e.getKeyChar() != KeyEvent.VK_ESCAPE//������ESC��
		) {
			Component editorComp = m_tablePane.getEditorComp();
			if (e == null || e.getID() != KeyEvent.KEY_TYPED) {
				return false;
			}
			int code = e.getKeyCode();
			if (code == KeyEvent.VK_SHIFT || code == KeyEvent.VK_CONTROL
					|| code == KeyEvent.VK_ALT) {
				return false;
			}
			
			if (editorComp == null) {
				CellPosition anchorCell = getSelectionModel().getAnchorCell();
				int anchorRow = anchorCell.getRow();
				int anchorColumn = anchorCell.getColumn();
				if (anchorRow != -1 && anchorColumn != -1 && !isEditing()) {
					if (!editCellAt(anchorRow, anchorColumn)) {
						return false;
					}
				}
				editorComp = m_tablePane.getEditorComp();
				if (editorComp == null) {
					return false;
				}
			}
			// ����༭�����JComponent, �������¼������������.
			if (editorComp instanceof JComponent) {
//				if (editorComp instanceof JTextComponent) {
//					((JTextComponent) editorComp).setText("");
//				}
				editorComp.requestFocus();
				getToolkit().getSystemEventQueue()
						.postEvent(
								new KeyEvent(editorComp, e.getID(),
										e.getWhen(), e.getModifiers(), e
												.getKeyCode(), e.getKeyChar()));
				return true;
			}
		}
		return retValue;
	}

	/**
	 * �˴����뷽�������� �������ڣ�(2004-5-19 9:04:21)
	 * 
	 * @param re
	 *            com.ufsoft.table.re.CellRenderAndEditor
	 */
	public void setRenderAndEditor(CellRenderAndEditor re) {
		renderAndEditor = re;
	}

	/**
	 * �����ڲ��������ǰ���ı�ʶ��
	 * 
	 * @return String
	 */
	public String getIdentifier() {
		TablePane tablePane = (TablePane) this.getParent().getParent();
		if (this.getParent() == tablePane.getMainView()) {
			return "MainView";
		} else if (this.getParent() == tablePane.getRightView()) {
			return "RightView";
		} else if (this.getParent() == tablePane.getDownView()) {
			return "DownView";
		} else if (this.getParent() == tablePane.getRightDownView()) {
			return "RightDownView";
		} else {
			return "UnknowView";
		}
	}

	/**
	 * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
	 * modify by guogang 2009-3-30 ����5.6���¼�����
	 */
	public String isSupport(int source, EventObject e)
			throws ForbidedOprException {
		return TableUtilities.newCheckEvent(source, e);
	}

	// /**
	// * ������������
	// * @param e void
	// */
	// public void processMouseWheelMoved(MouseWheelEvent e) {
	// Container parent = getParent();
	// if(parent != null && parent instanceof JViewport){
	// JViewport view = (JViewport) parent;
	// int distance = getScrollableUnitIncrement(view.getViewRect(),
	// SwingConstants.VERTICAL,e.getWheelRotation());
	// Point p = view.getViewPosition();
	// p.translate(0,distance*e.getWheelRotation());
	// if(p.y < view.getHeight()){
	// view.setViewPosition(p);
	// }
	// }
	// }
	/**
	 * ��δʹ��
	 */
	public boolean isPermitViewPos(Point viewPos, boolean isRow) {
		if (!(getDataModel().isInfinite())) {
			if (getParent() != null && getParent() instanceof JViewport) {
				JViewport viewPort = (JViewport) getParent();
				Point p = (Point) viewPos.clone();
				p.translate(viewPort.getWidth() - 1, viewPort.getHeight() - 1);
				if ((isRow && rowAtPoint(p) >= getRowCount())
						|| (!isRow && columnAtPoint(p) >= getColumnCount())) {
					return false;
				}
			}

		}
		return true;
	}

	public void registExtSheetRenderer(SheetCellRenderer renderer) {
		if (renderer == null) {
			return;
		}
		if(!local_extSheetCellRenderer.contains(renderer)){
			local_extSheetCellRenderer.add(renderer);
		} 
	}


	/**
	 * ��ȡȫ����չ������
	 * ����ȫ�ֺ;ֲ�
	 * @return
	 */
	private SheetCellRenderer[] getExtSheetRender() {
		List<SheetCellRenderer> renders = new ArrayList<SheetCellRenderer>();
		renders.addAll(Arrays.asList(CellRenderAndEditor.getInstance().getExtSheetRender()));
		renders.addAll(local_extSheetCellRenderer);
		return renders.toArray(new SheetCellRenderer[0]);
	}

	public void registExtSheetEditor(SheetCellEditor editor) {
		if (editor == null) {
			return;
		}
		if(!local_extSheetCellEditor.contains(editor)){
			local_extSheetCellEditor.add(editor);
		}
	}

	/**
	 * ��ȡȫ����չ�༭��
	 * ����ȫ�ֺ;ֲ�
	 * @return
	 */
	private SheetCellEditor[] getExtSheetEditor() {
		List<SheetCellEditor> editors = new ArrayList<SheetCellEditor>();
		editors.addAll(Arrays.asList(CellRenderAndEditor.getInstance().getExtSheetEditor()));
		editors.addAll(local_extSheetCellEditor);
		return editors.toArray(new SheetCellEditor[0]);
	}

	/**
	 * ������ʵ��л��и�
	 * 
	 * @param isRow
	 *            �Ƿ�����
	 * @param index
	 *            �к�
	 * @return int �µ��и�,���isRowΪtrue�򷵻ص�ǰ�е����е�Ԫ������иߣ����򷵻������е�����и�
	 */
	public int getFitHeaderSize(boolean isRow, int index) {
		int fitHeaderSize = -1;
		if (isRow) {
			int colNum = getDataModel().getColNum();
			for (int col = 0; col < colNum; col++) {
				CellPosition pos = CellPosition.getInstance(index, col);
				Cell cell = getDataModel().getCell(pos);
				Object value = cell == null ? null : cell.getValue();
				if (value != null) {
					JComponent comp = getValueComp(value, index, col, cell);
					int preferSize = comp.getPreferredSize().height;
					fitHeaderSize = Math.max(fitHeaderSize, preferSize);
					remove(comp);
				}
			}
			fitHeaderSize = (int) (fitHeaderSize * 1.13);// ��������������и�ƫС�����⡣
		} else {
			int rowNum = getDataModel().getRowNum();
			for (int row = 0; row < rowNum; row++) {
				CellPosition pos = CellPosition.getInstance(row, index);
				Cell cell = getDataModel().getCell(pos);
				Object value = cell == null ? null : cell.getValue();
				if (value != null) {
					JComponent comp = getValueComp(value, row, index, cell);
					int preferSize = comp.getPreferredSize().width;
					fitHeaderSize = Math.max(fitHeaderSize, preferSize);
					remove(comp);
				}
			}
		}
		
		if(fitHeaderSize > 800){
			fitHeaderSize = 800;
		}
		return fitHeaderSize;
	}

	/**
	 * ��ö�Ӧ��Ԫ��ֵ�����������͵ı༭���
	 * 
	 * @param value
	 *            ֵ
	 * @param row
	 *            ��Ӧ��Ԫ����
	 * @param col
	 *            ��Ӧ��Ԫ����
	 * @return JComponent �༭���
	 */
	private JComponent getValueComp(Object value, int row, int col, Cell cell) {
		SheetCellRenderer render = getReanderAndEditor().getRender(
				value.getClass());
		JComponent valueComp = (JComponent) render.getCellRendererComponent(
				this, value, false, false, row, col, cell);
		Rectangle rect = getCellRect(CellPosition.getInstance(row, col), true);
		valueComp.setBounds(rect);
		add(valueComp);
		return valueComp;
	}

	@Override
	/*
	 * ΪTablePane�Ļ������� modify by guogang 2008-1-24
	 */
	public Graphics getComponentGraphics(Graphics g) {
		return super.getComponentGraphics(g);
	}
	
	
	
	public int getOperationState(){
		// @edit by ll at 2009-5-14,����10:53:21
		if(m_operationState != -1)
			return m_operationState;
		
		IDockingContainer container =getContainer();
		if(container == null){
			return ReportContextKey.OPERATION_FORMAT;
		}
		if(container instanceof UfoReport){
			return ((UfoReport) container).getOperationState();
		}

		IContext context = getContext();
		if(context == null){
			throw new IllegalArgumentException();
		}
		
		if(context.getAttribute(ReportContextKey.OPERATION_STATE) == null){
			throw new IllegalArgumentException("OPERATION_STATE is NUll");
		}
		
		int state = new Integer("" + context.getAttribute(ReportContextKey.OPERATION_STATE));
		
		return state;
	}

	public IContext getContext(){
		//���ݾ�ģ��
		Viewer vr = (Viewer)SwingUtilities.getAncestorOfClass(Viewer.class, this);
		if(vr != null){
			return vr.getContext();
		}
		if(getContainer() == null){
			throw new IllegalArgumentException("Container is null.");
		}
		return getContainer().getContext();
	}
	
	public EventManager getEventManager(){
		EventManager em = EventService.getEventManager();
		if(em != null){
			return em;
		}
		IDockingContainer container = getContainer();
		if(container == null){
			return null;
		}
		return container.getEventManager();
	}
	
	private IDockingContainer getContainer(){
		if(m_container != null){
			return m_container;
		}
		m_container = (IDockingContainer)SwingUtilities.getAncestorOfClass(IDockingContainer.class, this);
		
		return m_container;
	}
	
	//�˷���Ԥ���Ǳߵ���
	public UfoReport getReport() {
		if (m_report == null) {
			m_report = (UfoReport)SwingUtilities.getAncestorOfClass(UfoReport.class, this);		
		}
		return m_report;
	}
	public void setPrivateOperationState(int state){
		m_operationState = state;
	}
}