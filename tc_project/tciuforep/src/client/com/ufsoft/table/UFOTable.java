package com.ufsoft.table;

import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JViewport;

import nc.ui.pub.beans.util.NCOptionPane;

import com.ufida.zior.exception.MessageException;
import com.ufida.zior.util.UIUtilities;
import com.ufsoft.report.ReportStyle;
import com.ufsoft.report.sysplugin.insertdelete.DeleteCmd;
import com.ufsoft.report.sysplugin.insertdelete.DeleteInsertDialog;
import com.ufsoft.report.sysplugin.insertdelete.InsertCellCmd;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.event.HeaderModelListener;
import com.ufsoft.table.header.HeaderModel;
import com.ufsoft.table.header.HeaderTree;
import com.ufsoft.table.header.TableHeader;
import com.ufsoft.table.header.TreeModel;
import com.ufsoft.table.re.BorderPlayRender;
import com.ufsoft.table.re.CellRenderAndEditor;

/**
 * <p>
 * Title: ���ؼ���֧�ֶ��ҳ
 * </p>
 * <p>
 * Description: ���Ƕ����ṩʹ�õı��ؼ��������������ɶ����ҳ�������ǹ�����ѡ���ҳת��ʱ��
 * ������а���������ģ�Ͱ�װ�ĵ�Sheet�У����µ�Sheet�а������������õ�����С����п��� �������ޱ�����ޱ��������еı�ҳ������һ�����͡�
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0.0.1
 */

public class UFOTable extends TablePane {
	 
	private static final long serialVersionUID = -9024099000772954528L;

	//********************************˽������*****************************//

	/** �Ƿ����ޱ� */
	private boolean m_bInfinite;

	/** ��ǰ����ļ��а� */
	private Clipboard m_Clipboard = null;
	
	/** ������ */
	private ReportStyle m_reportStyle = new ReportStyle();

	/** �û��¼��ļ����� */
	private ArrayList<UserActionListner> listeners = new ArrayList<UserActionListner>();
    /** ���ڸ��ơ����С�����ľ�̬����,������Ԫ�����ݺ͸�ʽ*/
	public static final int CELL_ALL = CellsModel.CELL_ALL;
	 /** ���ڸ��ơ����С�����ľ�̬����,ֻ������Ԫ������*/
	public static final int CELL_CONTENT = CellsModel.CELL_CONTENT;
	 /** ���ڸ��ơ����С�����ľ�̬����,ֻ������Ԫ�ĸ�ʽ*/
	public static final int CELL_FORMAT = CellsModel.CELL_FORMAT;


	//�����������������������������������������������췽�����Լ�һЩFactory��������������������������������������������������//
	/**
	 * 
	 * @create by wangyga at 2009-3-2,����05:14:18
	 *
	 * @param horPolicy
	 * @param verPolicy
	 * @param pane
	 */
	protected UFOTable(int horPolicy, int verPolicy, CellsPane pane){
		super(pane, verPolicy, horPolicy, pane.getDataModel() != null ? pane
				.getDataModel().isInfinite() : true);
		
		initialize();
		
	}

	private void initialize(){
		SecurityManager security = System.getSecurityManager();
		try {
			if (security != null) {
				security.checkSystemClipboardAccess();
			}
			m_Clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		} catch (Exception ex) {
			m_Clipboard = new Clipboard("local");
		}
		
		//�ڸ�ʽ���״̬�����÷�����Ϣ add by guogang 2008-3-12
		if(getCells().getDataModel()!=null){
		   initSeperator(getCells().getDataModel().getSeperateLockSet());
		}
	}
	
	/**
	 * Factory����������һ�����ޱ��������ޱ�Ĺ������ԣ�ͳһ����Ϊ��Զ��ʾ��
	 * 
	 * @param showPagemark
	 *            boolean �Ƿ���ʾҳǩ
	 * @param showRowHeader
	 *            boolean �Ƿ���ʾ�б���
	 * @param showColHeader
	 *            boolean �Ƿ���ʾ�б���
	 * @return UFOTable
	 * @see createTableImpl()
	 */
	public static UFOTable createInfiniteTable(
			boolean showRowHeader, boolean showColHeader) {
		//FIXME ������Ҫ����Ƿ���ʾҳǩ�Ĺ���
		Sheet sheet = createEmptySheet(true, 0, 0);
		return createTableImpl(sheet.getModel(), showRowHeader,
				showColHeader);
	}

	/**
	 * Factory�����������յ����ޱ�ָ�����ı�ߴ�
	 * 
	 * @param rowSize
	 *            int ��ߴ磺�е�����
	 * @param colSize
	 *            int ��ߴ磺�е�����
	 * @return UFOTable
	 * @see createTableImpl()
	 */
	public static UFOTable createFiniteTable(int rowSize, int colSize) {
		Sheet sheet = createEmptySheet(false, rowSize, colSize);
		return createTableImpl(sheet.getModel(), true, true);

	}
    /**
     * ������ģ�ʹ�����
     * @param cellsModel
     * @return
     * @see createTableImpl()
     */
	public static UFOTable createTableByCellsModel(CellsModel cellsModel) {
		//modify chxw 2008-04-03 ������ģ�ʹ�����ؼ�ʱ��������ͷ������ģ�͵ļ���
		cellsModel.getRowHeaderModel().setCellsModel(cellsModel);
		cellsModel.getColumnHeaderModel().setCellsModel(cellsModel);
		return createTableImpl(cellsModel, true, true);
	}
    /**
     * �������ʵ��ʵ��
     * @param sheet ���sheet
     * @param showPagemark  �Ƿ���ʾҳǩ
     * @param showRowHeader �Ƿ���ʾ�б���
     * @param showColHeader �Ƿ���ʾ�б���
     * @return
     */
	private static UFOTable createTableImpl(CellsModel model,
			boolean showRowHeader, boolean showColHeader) {
		CellsPane pane = new CellsPane(model, showRowHeader,
				showColHeader);
		UFOTable table = new UFOTable(TableConstants.HORIZONTAL_SCROLLBAR_ALWAYS,
				TableConstants.VERTICAL_SCROLLBAR_ALWAYS,pane);
		return table;
	}

	/**
	 * �õ���Ⱦ��ͱ༭���Ĺ�����
	 * 
	 * @return CellRenderAndEditor
	 */
	public CellRenderAndEditor getReanderAndEditor() {
		return getCells().getReanderAndEditor();
	}
	/**
	 * �Ƿ������ޱ�
	 * 
	 * @return boolean
	 */
	public boolean isInfinite() {
		return m_bInfinite;
	}

	/**
	 * ��һ����ҳʧȥѡ��״̬��ʱ�򣬽�����ĵ�ǰ������Ϣ���档��Ҫ��������ݰ�������ģ�ͣ�
	 * ѡ��ģ�ͣ���������ģ�ͣ���ͼ��ִ��ڵ���Ϣ��������Ҫ�����ͼ������ģ��֮������ļ�������
	 * 
	 * @param sheet
	 */
	private void suspend(Sheet sheet) {
		sheet.setColTreeModel(getTreeModel(getColTreeView()));
		sheet.setRowTreeModel(getTreeModel(getRowTreeView()));
		//�õ���������Ϣ��
		sheet.setFreezing(isFreezing());
		sheet.setSepRow(getSeperateRow());
		sheet.setSepCol(getSeperateCol());
		sheet.setSepX(getSeperateX());
		sheet.setSepY(getSeperateY());
		sheet.setViewPoint(getViewPoint(getMainView()), Sheet.LEFT_TOP);
		sheet.setViewPoint(getViewPoint(getRightView()), Sheet.RIGHT_TOP);
		sheet.setViewPoint(getViewPoint(getDownView()), Sheet.LEFT_DOWN);
		sheet.setViewPoint(getViewPoint(getRightDownView()), Sheet.RIGHT_DOWM);
		//������Ҫ���������֮��Ĺ�����
	}
    
	private TreeModel getTreeModel(JViewport view) {
		if (view != null) {
			HeaderTree tree = (HeaderTree) view.getView();
			if (tree != null) {
				return tree.getTreeModel();
			}
		}
		return null;
	}

	private Point getViewPoint(JViewport view) {
		if (view != null) {
			return view.getViewPosition();
		} else {
			return null;
		}
	}

	private void setViewPoint(JViewport view, Point p) {
		if (view != null && view.isEnabled()) {
			if (p == null) {
				p = new Point(0, 0);
			}
			view.setViewPosition(p);
		}
	}

	/**
	 * ��Sheet�е���Ϣ���õ�����С�
	 * 
	 * @param sheet
	 */
	private void active(Sheet sheet) {
		//��������ҪһЩ�Ż��Ĵ�����Ϊ��������ģ�͵�ʱ��������������޸ģ�������Ӧ�ü�������������
		//    ��Ҫ����������ӡ�
		JViewport view = getMainView();
		CellsPane cellsPane = ((CellsPane) view.getView());
		cellsPane.setDataModel(sheet.getModel());
		((TableHeader) getRowHeader().getView()).setModel(sheet.getModel()
				.getRowHeaderModel());
		((TableHeader) getColumnHeader().getView()).setModel(sheet.getModel()
				.getColumnHeaderModel());
		//��������Ϣ��
		cancelSeperate();
		setSeperatePos(sheet.getSepRow(), sheet.getSepCol());
		setFreezing(sheet.isFreezing());
		setViewPoint(getMainView(), sheet.getViewPoint(Sheet.LEFT_TOP));
		setViewPoint(getRightView(), sheet.getViewPoint(Sheet.RIGHT_TOP));
		setViewPoint(getDownView(), sheet.getViewPoint(Sheet.LEFT_DOWN));
		setViewPoint(getRightDownView(), sheet.getViewPoint(Sheet.RIGHT_DOWM));

	}

	//******************�����༭�ķ���
//	/** �õ�ѡ������ĵ�Ԫ��Ϣ */
//	private Cell[][] getSource() {
//		Cell[][] sources = null;
//		AreaPosition area = getSelectedSheet().getModel().getSelectModel()
//				.getSelectedArea();
//		if (area != null) {
//			try {
//				sources = getSelectedSheet().getModel().getCells(area);
//			} catch (TableDataModelException ex) {
//				AppDebug.debug(ex);
//			}
//		}
//		return sources;
//	}

	/**
	 * ��Ӽ�����
	 * 
	 * @param lis
	 */
	public void addUserActionListener(UserActionListner lis) {
		listeners.add(lis);
	}

	/**
	 * ɾ��������
	 * 
	 * @param lis
	 */
	public void removeUserActionListener(UserActionListner lis) {
		listeners.remove(lis);
	}

	/**
	 * ������UserActionListner�����������㲥UserUIEvent�¼�
	 * 
	 * @param event UserUIEvent
	 */
	public void fireEvent(UserUIEvent event) {
		for (UserActionListner listener: listeners) {
			listener.userActionPerformed(event);
		}
	}

	/**
	 * �����¼�֮ǰ,�������ע��ļ������Ƿ�������¼�����.
	 * 
	 * @param e
	 * @return
	 * @throws ForbidedOprException
	 */
	public boolean checkEvent(UserUIEvent e) {
//		return TableUtilities.checkEvent(listeners, e, Examination.USER_DEFINE);
		boolean result = false;
		try {
			String strResult = null;
			for (Examination listener: listeners) {
				String strInfo = listener.isSupport(Examination.USER_DEFINE, e);
				if (strInfo != null) {
					if (strResult == null) {
						strResult = strInfo;
					} else {
						strResult += strInfo;
					}
				}
			}
			if (strResult != null) {
				String title = MultiLang.getString("report00407");
				int value = NCOptionPane.showConfirmDialog(null, strResult,
						title, NCOptionPane.YES_NO_OPTION);
				result = value == NCOptionPane.YES_OPTION;
			} else {
				result = true;
			}
		} catch (MessageException e1) {
			Component parent = null;
			if (e.getSource() instanceof Component){
				parent = (Component) e.getSource();
			} 
			UIUtilities.sendMessage(e1, parent);
			result = false;
//			 JOptionPane.showMessageDialog(parent, e1.getMessage(),"",JOptionPane.WARNING_MESSAGE);
//			throw e1;
			 
		}  
		
		return result;
		
		
	}

	/**modify by 2008-4-29 ����� ���ص��˷��������У����ƣ�ճ������Ϊ�ɸ���ҵ�������
	 * ��ָ�������򿽱���Ӧ�ó���ļ��а��С�
	 * 
	 * @param clipType
	 */
//	public void copy(int clipType) {
//		copy(clipType, clipType == UFOTable.CELL_ALL
//				|| clipType == UFOTable.CELL_CONTENT);
//	}

	/**
	 * ���ָ������ĵ�Ԫ�е�����
	 * 
	 * @param clipType���ο�UFOTable��CELL_ALL��CELL_CONTENT��CELL_FORMAT��
	 */
	public void clear(int clipType) {
		clear(clipType, clipType == UFOTable.CELL_ALL
				|| clipType == UFOTable.CELL_CONTENT);
	}

	/**modify by 2008-4-29 ����� ���ص��˷��������У����ƣ�ճ������Ϊ�ɸ���ҵ�������
	 * �¼��Ƿ���ע�����ɷ���
	 * 
	 * @param dispatch
	 *            ��cut���õ�ʱ��Ϊfalse
	 */
//	private void copy(int clipType, boolean dispatch) {
//		Cell[][] sources = getSource();
//		if (sources == null)
//			return;
//		CellSelection sel = new CellSelection(clipType, sources);
//		//��װ�����¼�.
//		UserUIEvent event = new UserUIEvent(this, UserUIEvent.COPY, sel,
//				sources);
//		//�������¼�.
//		if (dispatch && checkEvent(event)) {
//			fireEvent(event);
//		}
//		m_Clipboard.setContents(sel, null);
//	}
    /**
     * ���ָ������ĵ�Ԫ�е�����
     * @param clipType
     * @param dispatch
     */
	private void clear(int clipType, boolean dispatch) {
		AreaPosition[] areas = getCellsModel().getSelectModel()
				.getSelectedAreas();
		if (areas == null)
			return;
		//��װ�����¼�.
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.CLEAR, null,
				areas);
		if (dispatch && checkEvent(event)) {
			fireEvent(event);
		}
		//ɾ�����ݺ͸�ʽ
		CellsModel model =this.getCellsModel();
		model.clearArea(clipType, areas);

	}

	/**modify by 2008-4-29 ����� ���ص��˷��������У����ƣ�ճ������Ϊ�ɸ���ҵ�������
	 * ���в���. �ؼ�ִ�м��в���,���������ɷ������¼�.
	 * 
	 * @param clipType
	 */
//	public void cut(int clipType) {
//		//		Cell[][] sources = getSource();
//		//		if (sources == null)
//		//			return;
//		//		//��װ�����¼�.
//		//		UserUIEvent event = new
//		// UserUIEvent(this,UserUIEvent.CUT,null,sources);
//		//		//�������¼�.
//		//		if (checkEvent(event)) {
//		//			fireEvent(event);
//		//����û��cut�¼����ɷ���cut�¼���copy��clear�����¼���ɡ�
//		copy(clipType);
//		clear(clipType);
//		//		}
//	}

	/**
	 * �ϲ���Ԫ.������ѡ������ĵ�Ԫ.
	 * 
	 * @param area
	 * @throws TableDataModelException
	 */
	public void combineCell(AreaPosition area) throws TableDataModelException {
		if (area == null || area.isCell()) {
			return;
		}
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.COMBINECELL,
				area, null);
		//����Ƿ��������.
		if (!checkEvent(event)) {
			return;
		}
		//�����������ģ�Ͳ���ǰ,��ģ�Ͳ�������Ԥ����.
		fireEvent(event);
		//ģ�Ͳ���.
		CellsModel cm =this.getCellsModel();
		ArrayList areaDatas = cm.getAreaDatas();
		if (areaDatas != null) {
			Iterator iter = areaDatas.iterator();
			ArrayList listRemove = new ArrayList();
			//�����������Ƿ񽻲棬�Ƿ����ɾɵ�����
			while (iter.hasNext()) {
				IAreaAtt att = (IAreaAtt) iter.next();
				if (att instanceof CombinedCell) {
					//��������ཻ���ǲ��ຬ���׳��쳣��
					if (att.getArea().intersection(area)) {
						if (area.contain(att.getArea())) { //��������������򣬼�¼��Ҫɾ���ľ�����
							listRemove.add(att);
						} else {
							throw new TableDataModelException();
						}
					}
				}
			}
			areaDatas.removeAll(listRemove);
		}
		//�õ���ǰ�ϲ���Ԫ�׵�Ԫ�����ݡ�
		cm.combineCell(area);
	}

//	/**
//	 * ����ϲ���Ԫ�� ����ϵ�Ԫ����Ϣ��������ϵ�Ԫ���׵�Ԫ�ϣ�����ɾ����ϵ�Ԫ��Ϣ�� Ҫ��AreaPosition������һ����ϵ�Ԫ��Ӧ������.
//	 * 
//	 * @param area
//	 */
//	public void separateCombinedCell(AreaPosition area) {
//		if (area == null || area.isCell()) {
//			return;
//		}
//		UserUIEvent event = new UserUIEvent(this, UserUIEvent.UNCOMBINECELL,
//				area, null);
//		//����Ƿ��������.
//		if (!checkEvent(event)) {
//			return;
//		}
//		//ģ�Ͳ���.
//		CellsModel cm = getSelectedSheet().getModel();
//		ArrayList areaDatas = cm.getAreaDatas();
//		if (areaDatas != null) {
//			Iterator iter = areaDatas.iterator();
//			AreaPosition ap;
//			while (iter.hasNext()) {
//				IAreaAtt areaAtt = (IAreaAtt) iter.next();
//				if (areaAtt instanceof CombinedCell) {
//					CombinedCell cCell = (CombinedCell) areaAtt;
//					ap = cCell.getArea();
//					if (ap.equals(area)) {
////						Cell c = new Cell(cCell.getFormat(), cCell.getValue(),
////								cCell.getExtFmtAll());//��ϵ�Ԫ���ݷŵ��׵�Ԫ��.
//						areaDatas.remove(cCell);
////						cm.setCell(ap.getStart().getRow(), ap.getStart()
////								.getColumn(), c);
//						ArrayList listCellPos = cm.getSeperateCellPos(area);
//						for (Iterator iterator = listCellPos.iterator(); iterator
//								.hasNext();) {
//							CellPosition cellPos = (CellPosition) iterator
//									.next();
//							Format format = cCell.getFormat();
//							cm.setCellFormat(cellPos.getRow(), cellPos
//									.getColumn(), format == null ? null
//									: (Format) format.clone());
//						}
//						//�����������ģ�Ͳ�����,����ģ�Ͳ����ĺ���.
//						fireEvent(event);
//						return;
//					}
//				}
//			}
//		}
//
//	}

	/**modify by 2008-4-29 ����� ���ص��˷��������У����ƣ�ճ������Ϊ�ɸ���ҵ�������
	 * �����а��е����ݸ��Ƶ�ѡ�е�λ�á�
	 */
//	public void paste() {
//		CellPosition target = getSelectedSheet().getModel().getSelectModel()
//				.getAnchorCell();
//		if (target == null)
//			return;
//		Transferable content = m_Clipboard.getContents(this);
//		int clipType = CELL_ALL;
//		Cell[][] c = null;
//		try {
//			Integer IntClipType = (Integer) content
//					.getTransferData(CellSelection.CLIPTYPE_FLAVOR);
//			if (IntClipType != null) {
//				clipType = IntClipType.intValue();
//			}
//			c = (Cell[][]) content.getTransferData(CellSelection.CELL_FLAVOR);
//		} catch (Exception ex) {
//		}
//		if(content == null) return;
//		// �õ���ǰѡ�б�ҳ�Ľ��㵥Ԫ��
//		CellsModel cellsModel = getSelectedSheet().getModel();
//		int rowStart = target.getRow();
//		int colStart = target.getColumn();
//		if (c != null) {
//			for (int i = 0; i < c.length; i++) {
//				Cell[] cLine = c[i];
//				if (cLine != null) {
//					for (int j = 0, colPos = colStart; j < cLine.length; j++, colPos++) {
//						if (cLine[j] instanceof CombinedCell) {
//							// ��鵱ǰ���ǵ������Ƿ������ϵ�Ԫ������������׳��쳣��
//							// CombinedCell cc = (CombinedCell) cLine[j];
//							// AreaPosition area = cc.getArea();
//							// CombineCellCmd.combineCell(area,cellsModel);
//							// ��ϵ�Ԫ���Ӧ�÷ŵ�table��Ŀ���!Ȼ�����¼�֪ͨ����¼�.
//							// ���߸���ճ�����¼�ȫ����reporttool��Ŀ���ò��ʵ��.���������޷�֪��ҵ���Ƿ�ͬ�����.
//						} else {
//							if (cLine[j] == null) {
//								cLine[j] = new Cell();
//							}
//							// ȫ��Ĭ���������ݺ͸�ʽ,��չ�����Ƿ����ɲ���Լ�����.
//							if (clipType == CELL_ALL || clipType == CELL_CONTENT) {
//								// cellsModel.setCellValueByAuth(rowStart + i,
//								// colPos, cLine[j].getValue());
//								if (cLine[j].getValue() != null){
//									//modify by chxw 2007-05-17 ճ����Ԫ������ʱ�����������ݲ�����toString����(���ݽϴ�ʱ�����ص��ǿ�ѧ�����ַ���)
//									String inputText = cLine[j].getValue().toString();
//									if(cLine[j].getValue() instanceof Double){
//										double dValue = ((Double)cLine[j].getValue()).doubleValue();
//										IufoFormat format = (IufoFormat)cellsModel.getFormatIfNullNew(CellPosition.getInstance(cLine[j].getRow(), cLine[j].getCol()));
//										inputText = format.getString(dValue);
//									} 
//									simulateKeyBoardInput(rowStart + i, colPos, inputText);
//								}
//							}
//							if (clipType == CELL_ALL || clipType == CELL_FORMAT) {
//								cellsModel.setCellFormatByAuth(rowStart + i, colPos, cLine[j].getFormat());
//							}
//						}
//					}
//				}
//			}
//			UserUIEvent event = new UserUIEvent(this, UserUIEvent.PASTE, content, c);
//			// ����ճ���¼�.���ݲ���Ҫ����չ���ݣ��繫ʽ�����������ݡ�
//			if (clipType == CELL_FORMAT || !checkEvent(event)) {
//				return;
//			}
//			fireEvent(event);
//		} else {//���ⲿ�������excel�и��ƹ��������ݡ���֪ͨ���ճ���¼���
//			String[][] result = getAppClipCell(content);
//			if (result != null) {
//				for (int i = 0; i < result.length; i++) {
//					String[] row = result[i];
//					if (row != null) {
//						for (int j = 0; j < row.length; j++) {
//							// cellsModel.setCellValue(rowStart + i, colStart
//							// + j, row[j]);
//							simulateKeyBoardInput(rowStart + i, colStart + j, row[j]);
//						}
//					}
//				}
//			}
//		}
//	}

	/**
	 * �õ�����Ӧ�ó����п��������ݡ�
	 * 
	 * @param content
	 *            Transferable
	 * @return String[][]
	 */
	private String[][] getAppClipCell(Transferable content) {
		String strOtherApplication = null;
		String[][] result = null;
		try {
			strOtherApplication = (String) content
					.getTransferData(DataFlavor.stringFlavor);
		} catch (Exception ex) {
			return null;
		}
		//���String���͵Ľ�����Ϣ������Excel�ڼ��а��У�����\t�ָ���Ϣ��
		if (strOtherApplication != null) {
			ArrayList allLine = new ArrayList();
			String[] sepLines = strOtherApplication.split("\n");
			for (int i = 0; i < sepLines.length; i++) {
				String line = sepLines[i];
				if (line == null || line.equals("")) {
					allLine.add(null);
				} else {
					String[] lineElem = line.split("\t");
					ArrayList allCell = new ArrayList();
					for (int j = 0; j < lineElem.length; j++) {
						allCell.add(lineElem[j]);
					}
					allLine.add(allCell);
				}
			}
			//ת��Ϊ��Ԫ��ʽ
			int rowNum = allLine.size();
			result = new String[rowNum][];
			for (int i = 0; i < rowNum; i++) {
				ArrayList row = (ArrayList) allLine.get(i);
				if (row != null) {
					int lineNum = row.size();
					String[] line = new String[lineNum];
					row.toArray(line);
					result[i] = line;
				}
			}
		}
		return result;
	}

	//	/**
	//	 * �õ����е��Ҽ��˵�������Ҽ��˵�Ϊ�գ��÷������𹹽�һ�����أ�������ӵ���Ӧ������ϡ�
	//	 * @param nCmpName
	//	 * @return ���û���Ҽ��˵���Ϣ�����ؿ�����
	//	 */
	//	public JPopupMenu[] getPopupMenu(int nCmpName) {
	//		//todo ����Ҫ������ڷ������ڶ���������ϵ��Ҽ��˵���
	//		JPopupMenu[] pops = {};
	//		if(nCmpName>0) {
	//			ArrayList list = new ArrayList();
	//			if((nCmpName&POPUP_CELLSPANEL)>0){
	//				if(m_popupOnCellsPane==null){;
	//					addPopMenu(getCells(),m_popupOnCellsPane = new UIPopupMenu());
	//				}
	//				list.add(m_popupOnCellsPane);
	//			}
	//			if((nCmpName&POPUP_ROWHEADER)>0){
	//				if(m_popupOnRHeader==null){
	//					addPopMenu((JComponent)getRowHeader().getView(),m_popupOnRHeader = new
	// JPopupMenu());
	//				}
	//				list.add(m_popupOnRHeader);
	//			}
	//			if((nCmpName&POPUP_COLHEADER)>0){
	//				if(m_popupOnCHeader==null){
	//					addPopMenu((JComponent)getColumnHeader().getView(),m_popupOnCHeader = new
	// JPopupMenu());
	//				}
	//				list.add(m_popupOnCHeader);
	//			}
	//			if((nCmpName&POPUP_HORSCROLL)>0){
	//				if(m_popupOnHScroll==null){
	//					addPopMenu(getHScrollBar(),m_popupOnHScroll = new UIPopupMenu());
	//				}
	//				list.add(m_popupOnHScroll);
	//			}
	//			if((nCmpName&POPUP_VERSCROLL)>0){
	//				if(m_popupOnVScroll==null){
	//					addPopMenu(getVScrollBar(),m_popupOnVScroll = new UIPopupMenu());
	//				}
	//				list.add(m_popupOnVScroll);
	//			}
	//			int size = list.size();
	//			pops = new UIPopupMenu[size];
	//			list.toArray(pops);
	//		}
	//		return pops;
	//	}

//	private void addPopMenu(JComponent container, JPopupMenu menu) {
//		container.add(menu);
//		container.addMouseListener(new PopupListener(menu));
//	}

	//**********************˽�з���**********************
    /**
     * �����յı��sheet
     * @param infinite �Ƿ������ޱ�
     * @param rowSize �������
     * @param colSize �������
     * @return
     */
	private static Sheet createEmptySheet(boolean infinite, int rowSize,
			int colSize) {
		if (infinite) {
			return Sheet.createInfiniteSheet(null);
		} else {
			if (rowSize < 1 || colSize < 1) {
				throw new IllegalArgumentException();
			}
			return Sheet.createFiniteSheet(new Object[rowSize][colSize]);
		}
	}

	//**************************��ӡ�Ĵ������������������������������

	//**************************�¼�֪ͨ**************************
	//@toList :��ʱȫ�����»��ƣ��Ժ����һ����������֪ͨ��Ҫ���Ƶ�����
	private void fireSelectChanged() {
		revalidate();
		repaint();
	}

	/**
	 * ���õ�ǰ��ҳ������ģ�͡�
	 * 
	 * @param cellsModel
	 */
	public void setCurCellsModel(CellsModel cellsModel) {

		//add by wangyga 2008-12-17 �л�����ʱ����ж�����ֹͣ����Ч��
		BorderPlayRender.stopPlay(getCells());
		
		//��¼�ɵ�ģ�ͣ�Ϊת�Ƽ�������׼��		
		CellsModel oldCellsModel = getCellsModel();
		if (cellsModel == null||oldCellsModel==cellsModel)
			return;
		/** ****ֻ���ǵ���ҳ�����********* */

		boolean dispatch = true;
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.MODEL_CHANGED, oldCellsModel,
				cellsModel);
		boolean fireEvent = false;
		if (dispatch && checkEvent(event)) {
			fireEvent = true;
		}
		
		//���ģ�ͺ�ѡ��ģ��
		this.getCells().setDataModel(cellsModel);
		setCompDataModel(cellsModel);

		//����ģ��
		((TableHeader) getRowHeader().getView()).setModel(cellsModel
				.getRowHeaderModel());
		((TableHeader) getColumnHeader().getView()).setModel(cellsModel
				.getColumnHeaderModel());
		
		if(getRowHeader2() != null){
			((TableHeader)getRowHeader2().getView()).setModel(cellsModel.getRowHeaderModel());
		}
		if(getColumnHeader2() != null){
			((TableHeader)getColumnHeader2().getView()).setModel(cellsModel.getColumnHeaderModel());
		}
		
		//TODO �˴�������ģ��

		/**ת�ƾɵ�ģ���ϵ�����������*///(Ŀǰδ����HeaderModelʵ���˵�TreeModelListener)
		
		//CellsModel
		CellsModelListener[] oldCellsListener = oldCellsModel.getListeners();
		cellsModel.clearListeners();
		for (CellsModelListener l: oldCellsListener) {
			Object newListner = getNewListner(l, oldCellsModel,
					cellsModel);
			if (newListner != null){
				cellsModel.addCellsModelListener((CellsModelListener) newListner);
			}
		}
		oldCellsModel.clearListeners();
		cellsModel.addCellsModelListener(getCells());

		//HeaderModel
		HeaderModel oldRowHeader = oldCellsModel.getRowHeaderModel();
		HeaderModel newRowHeader = cellsModel.getRowHeaderModel();
		changeHeaderListner(oldRowHeader, newRowHeader, oldCellsModel,
				cellsModel);

		HeaderModel oldColumnHeader = oldCellsModel.getColumnHeaderModel();
		HeaderModel newColumnHeader = cellsModel.getColumnHeaderModel();
		changeHeaderListner(oldColumnHeader, newColumnHeader, oldCellsModel,
				cellsModel);
		
		//ģ�Ͳ�����Ҫ��������ģ�͵�header����
			
			oldCellsModel.addCellsModelListener(oldCellsModel.getRowHeaderModel());
			oldCellsModel.addCellsModelListener(oldCellsModel.getColumnHeaderModel());
			oldCellsModel.getRowHeaderModel().addHeaderModelListener(oldCellsModel);
			oldCellsModel.getColumnHeaderModel().addHeaderModelListener(oldCellsModel);
		
		

//		liuyy+  2008-05-22
//		SelectModel
		SelectModel oldSelect = oldCellsModel.getSelectModel();
		SelectModel newSelect = cellsModel.getSelectModel();
		if (oldSelect != null && newSelect != null) {
			newSelect.clearListeners();
			SelectListener[] selectList = oldSelect.getListenerList();
			for (int i = 0; i < selectList.length; i++) {
				Object newListner = getNewListner(selectList[i], oldCellsModel,
						cellsModel);
				if (newListner != null)
					newSelect.addSelectModelListener((SelectListener) newListner);
			}
			
			oldSelect.clearListeners();
		}
		newSelect.addSelectModelListener(getCells());
 
//		//�������������á�
//		if(cellsModel.getSeperateLockSet() == null){
//
//			setFreezing(false);
//			cancelSeperate();			
//		}else{
//			SeperateLockSet set = cellsModel.getSeperateLockSet();
//			if(set.getSeperateRow()==0 && set.getSeperateCol()==0){
//				cancelSeperate();
//				
//			}else{
//				setSeperatePos(set.getSeperateRow(), set.getSeperateCol());
//			}
//			setFreezing(set.isFreezing());
//		}
		
		//�������������á�
		SeperateLockSet set = cellsModel.getSeperateLockSet();
		if(set == null){
			setFreezing(false);
			cancelSeperate();			
		}else{
			
//	 5.02����5.5֮��������÷�����������¼�������������ƻ������⣬�����ʽ����cnacel��set
			int sptRow = set.getSeperateRow();
			int sptCol = set.getSeperateCol();
			boolean freezing = set.isFreezing();

			cancelSeperate();
			
			setSeperatePos(sptRow, sptCol);
			
			setFreezing(freezing);
		}

		initSeperator(cellsModel.getSeperateLockSet());
		
		if(fireEvent){
			fireEvent(event);
		}
	
	}
    /**
     * ����������������ģ��
     * @param cellsModel
     */
    private void setCompDataModel(CellsModel cellsModel) {
        getCells().setDataModel(cellsModel);
        if(getRightView()!=null){
            ((CellsPane)(getRightView().getView())).setDataModel(cellsModel);
        }
        if(getDownView()!=null){
            ((CellsPane)(getDownView().getView())).setDataModel(cellsModel);
        }
        if(getRightDownView()!=null){
            ((CellsPane)(getRightDownView().getView())).setDataModel(cellsModel);
        }        
    }

    /**
     * ����������Ǿɵ�ģ�ͣ���Ӧ����ģ��
     * @param oldListener
     * @param oldCells
     * @param newCells
     * @return
     */
	private Object getNewListner(Object oldListener, CellsModel oldCells,
			CellsModel newCells) {

		if (oldListener == oldCells)
			return newCells;
		HeaderModel oldRowHeader = oldCells.getRowHeaderModel();
		if (oldRowHeader != null && oldListener == oldRowHeader) {
			return newCells.getRowHeaderModel();
		}
		HeaderModel oldColumnHeader = oldCells.getColumnHeaderModel();
		if (oldColumnHeader != null && oldListener == oldColumnHeader) {
			return newCells.getColumnHeaderModel();
		}
		return oldListener;
	}
    /**
     * ��ͷ�ı���¼�����
     * @param oldHeaderModel
     * @param newHeaderModel
     * @param oldCells
     * @param newCells
     */
	private void changeHeaderListner(HeaderModel oldHeaderModel,
			HeaderModel newHeaderModel, CellsModel oldCells, CellsModel newCells) {
		HeaderModelListener[] headerList = oldHeaderModel.getListenerList();
		for (Object l: headerList) {
			Object newListner = getNewListner(l, oldCells,
					newCells);
			if (newListner != null)
				newHeaderModel
						.addHeaderModelListener((HeaderModelListener) newListner);
		}
		oldHeaderModel.clearListeners();

	}

	/**
	 * ���뵥Ԫ��.
	 * 
	 * @param aimArea
	 * @param insertType
	 *            ��InsertCellDlg:MOVE_RIGHT_WHEN_INSERT_CELL,MOVE_DOWN_WHEN_INSERT_CELL
	 */
	public void insertCells(final AreaPosition aimArea, final int insertType) {
		if (aimArea == null)
			return;
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.INSERTCELL,
				new Integer(insertType), aimArea);
		if (checkEvent(event)) {//��鶯̬����,��ϵ�Ԫ
			fireEvent(event);//ɾ����ϵ�Ԫ
			CellsModel cellsModel = this.getCellsModel();
			CellPosition newAnchorPos;
			CellPosition startCellPos = aimArea.getStart();
			if (insertType == DeleteInsertDialog.CELL_MOVE_DOWN) {
				newAnchorPos = (CellPosition) startCellPos.getMoveArea(aimArea.getHeigth(), 0);
			} else if (insertType == DeleteInsertDialog.CELL_MOVE_RIGHT) {
				newAnchorPos = (CellPosition) startCellPos.getMoveArea(0, aimArea.getWidth());
			} else {
				throw new IllegalArgumentException();
			}
			AreaPosition toMoveArea = InsertCellCmd.getToMoveArea(aimArea,
					insertType, cellsModel);
			cellsModel.moveCells(toMoveArea, newAnchorPos);
		}
	}

	/**
	 * ɾ����Ԫ��
	 * �������: 1.������ʵ��UserActionListner�ļ����������㲥UserUIEvent.DELETECELL�¼�(��ɾ���õ�Ԫ���ϵĺϲ���Ԫ��,�ؼ���,ָ��,��ʽ)
	 *          2.���Ҫɾ�������ϵ����ݺ͸�ʽ��Ϣ(��Ҫ��CellsModelû�д���UserUIEvent.DELETECELL�¼�)
	 *          3.�ƶ�����(�Ȼ�ȡҪ�ƶ�����Ԫ��,�������Ҫ�ƶ��ĵ�Ԫ���ϵ���Ϣ,���������ƶ���ĵ�Ԫ�����Ϣ)
	 * @param aimArea Ҫɾ��������
	 * @param deleteType ɾ����������Ԫ���ƶ�����(DeleteDialog�ж���)
	 */
	public void deleteCells(AreaPosition aimArea, int deleteType) {
		if (aimArea == null)
			return;
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.DELETECELL,
				new Integer(deleteType), aimArea);
		if (checkEvent(event)) {//��鶯̬����,��ϵ�Ԫ
			fireEvent(event);//ɾ����ϵ�Ԫ
			CellsModel cellsModel = this.getCellsModel();
			clear(UFOTable.CELL_ALL);
			CellPosition newAnchorPos = aimArea.getStart();
			AreaPosition toMoveArea = DeleteCmd.getToMoveArea(aimArea,
					deleteType, cellsModel);
			cellsModel.moveCells(toMoveArea, newAnchorPos);
		}
	}
	
	public void initSeperator(SeperateLockSet slSet) {
		if (slSet != null
				&& (slSet.getSeperateRow() != 0 || slSet.getSeperateCol() != 0)) {

//			// 5.02����5.5֮��������÷�����������¼�������������ƻ������⣬�����ʽ����cnacel��set
//			cancelSeperate();

			setSeperatePos(slSet.getSeperateRow(), slSet.getSeperateCol());
		}
	}

	public Clipboard getClipboard() {
		return m_Clipboard;
	}
	
	/**
	 * ��ñ�ҳ��ʾ���
	 * @create by wangyga at 2009-9-3,����11:10:21
	 *
	 * @return
	 */
	public ReportStyle getStyle() {
		return m_reportStyle;
	}

	/**
	 * ���ñ�ҳ��ʾ���
	 * @create by wangyga at 2009-9-3,����11:10:27
	 *
	 * @param reportStyle
	 */
	public void setStyle(ReportStyle reportStyle) {
		m_reportStyle = reportStyle;
		// �޸�ҳ����ʾ�ķ��.
		setRowHeaderVisible(m_reportStyle.isShowRowHeader());
		setColHeaderVisible(m_reportStyle.isShowColHeader());
		getCells().setGridColor(m_reportStyle.getGrid());
		// ��ʾ����
		setViewScale((m_reportStyle.getPercent() * 0.01));
		// Ϊ0�Ƿ���ʾ.
		repaint();
	}
}  