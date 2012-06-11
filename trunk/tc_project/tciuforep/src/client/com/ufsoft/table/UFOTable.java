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
 * Title: 表格控件，支持多表页
 * </p>
 * <p>
 * Description: 这是对外提供使用的表格控件，其中允许容纳多个表页并对它们管理，当选择表页转换时，
 * 将组件中包含的数据模型包装的到Sheet中，用新的Sheet中包含的数据设置到组件中。其中可以 容纳有限表和无限表，但是所有的表页必须是一种类型。
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

	//********************************私有属性*****************************//

	/** 是否无限表 */
	private boolean m_bInfinite;

	/** 当前程序的剪切板 */
	private Clipboard m_Clipboard = null;
	
	/** 报表风格 */
	private ReportStyle m_reportStyle = new ReportStyle();

	/** 用户事件的监听器 */
	private ArrayList<UserActionListner> listeners = new ArrayList<UserActionListner>();
    /** 用于复制、剪切、清除的静态常量,包括单元的内容和格式*/
	public static final int CELL_ALL = CellsModel.CELL_ALL;
	 /** 用于复制、剪切、清除的静态常量,只包括单元的内容*/
	public static final int CELL_CONTENT = CellsModel.CELL_CONTENT;
	 /** 用于复制、剪切、清除的静态常量,只包括单元的格式*/
	public static final int CELL_FORMAT = CellsModel.CELL_FORMAT;


	//××××××××××××××××××××××构造方法，以及一些Factory方法×××××××××××××××××××××××//
	/**
	 * 
	 * @create by wangyga at 2009-3-2,下午05:14:18
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
		
		//在格式设计状态下设置分栏信息 add by guogang 2008-3-12
		if(getCells().getDataModel()!=null){
		   initSeperator(getCells().getDataModel().getSeperateLockSet());
		}
	}
	
	/**
	 * Factory方法。创建一个无限表，对于无限表的滚动策略，统一设置为永远显示。
	 * 
	 * @param showPagemark
	 *            boolean 是否显示页签
	 * @param showRowHeader
	 *            boolean 是否显示行标题
	 * @param showColHeader
	 *            boolean 是否显示列标题
	 * @return UFOTable
	 * @see createTableImpl()
	 */
	public static UFOTable createInfiniteTable(
			boolean showRowHeader, boolean showColHeader) {
		//FIXME 整理需要添加是否显示页签的功能
		Sheet sheet = createEmptySheet(true, 0, 0);
		return createTableImpl(sheet.getModel(), showRowHeader,
				showColHeader);
	}

	/**
	 * Factory方法。创建空的有限表，指定它的表尺寸
	 * 
	 * @param rowSize
	 *            int 表尺寸：行的数量
	 * @param colSize
	 *            int 表尺寸：列的数量
	 * @return UFOTable
	 * @see createTableImpl()
	 */
	public static UFOTable createFiniteTable(int rowSize, int colSize) {
		Sheet sheet = createEmptySheet(false, rowSize, colSize);
		return createTableImpl(sheet.getModel(), true, true);

	}
    /**
     * 由数据模型创建表
     * @param cellsModel
     * @return
     * @see createTableImpl()
     */
	public static UFOTable createTableByCellsModel(CellsModel cellsModel) {
		//modify chxw 2008-04-03 由数据模型创建表控件时，设置行头对数据模型的监听
		cellsModel.getRowHeaderModel().setCellsModel(cellsModel);
		cellsModel.getColumnHeaderModel().setCellsModel(cellsModel);
		return createTableImpl(cellsModel, true, true);
	}
    /**
     * 创建表的实际实现
     * @param sheet 表格sheet
     * @param showPagemark  是否显示页签
     * @param showRowHeader 是否显示行标题
     * @param showColHeader 是否显示列标题
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
	 * 得到渲染起和编辑器的管理器
	 * 
	 * @return CellRenderAndEditor
	 */
	public CellRenderAndEditor getReanderAndEditor() {
		return getCells().getReanderAndEditor();
	}
	/**
	 * 是否是无限表
	 * 
	 * @return boolean
	 */
	public boolean isInfinite() {
		return m_bInfinite;
	}

	/**
	 * 当一个表页失去选择状态的时候，将组件的当前数据信息保存。需要保存的数据包括数据模型；
	 * 选择模型，树的数据模型，视图拆分窗口的信息。另外需要解除视图和数据模型之间关联的监听器。
	 * 
	 * @param sheet
	 */
	private void suspend(Sheet sheet) {
		sheet.setColTreeModel(getTreeModel(getColTreeView()));
		sheet.setRowTreeModel(getTreeModel(getRowTreeView()));
		//得到分栏的信息。
		sheet.setFreezing(isFreezing());
		sheet.setSepRow(getSeperateRow());
		sheet.setSepCol(getSeperateCol());
		sheet.setSepX(getSeperateX());
		sheet.setSepY(getSeperateY());
		sheet.setViewPoint(getViewPoint(getMainView()), Sheet.LEFT_TOP);
		sheet.setViewPoint(getViewPoint(getRightView()), Sheet.RIGHT_TOP);
		sheet.setViewPoint(getViewPoint(getDownView()), Sheet.LEFT_DOWN);
		sheet.setViewPoint(getViewPoint(getRightDownView()), Sheet.RIGHT_DOWM);
		//这里需要解除监听器之间的关联。
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
	 * 将Sheet中的信息设置到组件中。
	 * 
	 * @param sheet
	 */
	private void active(Sheet sheet) {
		//这其中需要一些优化的处理，因为设置数据模型的时候会联动其他的修改，在这里应该减少这种联动。
		//    需要监听器的添加。
		JViewport view = getMainView();
		CellsPane cellsPane = ((CellsPane) view.getView());
		cellsPane.setDataModel(sheet.getModel());
		((TableHeader) getRowHeader().getView()).setModel(sheet.getModel()
				.getRowHeaderModel());
		((TableHeader) getColumnHeader().getView()).setModel(sheet.getModel()
				.getColumnHeaderModel());
		//分栏的信息。
		cancelSeperate();
		setSeperatePos(sheet.getSepRow(), sheet.getSepCol());
		setFreezing(sheet.isFreezing());
		setViewPoint(getMainView(), sheet.getViewPoint(Sheet.LEFT_TOP));
		setViewPoint(getRightView(), sheet.getViewPoint(Sheet.RIGHT_TOP));
		setViewPoint(getDownView(), sheet.getViewPoint(Sheet.LEFT_DOWN));
		setViewPoint(getRightDownView(), sheet.getViewPoint(Sheet.RIGHT_DOWM));

	}

	//******************关联编辑的方法
//	/** 得到选择区域的单元信息 */
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
	 * 添加监听器
	 * 
	 * @param lis
	 */
	public void addUserActionListener(UserActionListner lis) {
		listeners.add(lis);
	}

	/**
	 * 删除监听器
	 * 
	 * @param lis
	 */
	public void removeUserActionListener(UserActionListner lis) {
		listeners.remove(lis);
	}

	/**
	 * 向所有UserActionListner监听器类对象广播UserUIEvent事件
	 * 
	 * @param event UserUIEvent
	 */
	public void fireEvent(UserUIEvent event) {
		for (UserActionListner listener: listeners) {
			listener.userActionPerformed(event);
		}
	}

	/**
	 * 处理事件之前,检查所有注册的监听器是否允许该事件发生.
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

	/**modify by 2008-4-29 王宇光 ，关掉此方法：剪切，复制，粘贴等行为由各个业务插件完成
	 * 将指定的区域拷贝到应用程序的剪切板中。
	 * 
	 * @param clipType
	 */
//	public void copy(int clipType) {
//		copy(clipType, clipType == UFOTable.CELL_ALL
//				|| clipType == UFOTable.CELL_CONTENT);
//	}

	/**
	 * 清除指定区域的单元中的数据
	 * 
	 * @param clipType，参考UFOTable的CELL_ALL、CELL_CONTENT、CELL_FORMAT。
	 */
	public void clear(int clipType) {
		clear(clipType, clipType == UFOTable.CELL_ALL
				|| clipType == UFOTable.CELL_CONTENT);
	}

	/**modify by 2008-4-29 王宇光 ，关掉此方法：剪切，复制，粘贴等行为由各个业务插件完成
	 * 事件是否向注册器派发。
	 * 
	 * @param dispatch
	 *            当cut调用的时候为false
	 */
//	private void copy(int clipType, boolean dispatch) {
//		Cell[][] sources = getSource();
//		if (sources == null)
//			return;
//		CellSelection sel = new CellSelection(clipType, sources);
//		//包装拷贝事件.
//		UserUIEvent event = new UserUIEvent(this, UserUIEvent.COPY, sel,
//				sources);
//		//处理拷贝事件.
//		if (dispatch && checkEvent(event)) {
//			fireEvent(event);
//		}
//		m_Clipboard.setContents(sel, null);
//	}
    /**
     * 清除指定区域的单元中的数据
     * @param clipType
     * @param dispatch
     */
	private void clear(int clipType, boolean dispatch) {
		AreaPosition[] areas = getCellsModel().getSelectModel()
				.getSelectedAreas();
		if (areas == null)
			return;
		//包装拷贝事件.
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.CLEAR, null,
				areas);
		if (dispatch && checkEvent(event)) {
			fireEvent(event);
		}
		//删除数据和格式
		CellsModel model =this.getCellsModel();
		model.clearArea(clipType, areas);

	}

	/**modify by 2008-4-29 王宇光 ，关掉此方法：剪切，复制，粘贴等行为由各个业务插件完成
	 * 剪切操作. 控件执行剪切操作,并且向外派发剪切事件.
	 * 
	 * @param clipType
	 */
//	public void cut(int clipType) {
//		//		Cell[][] sources = getSource();
//		//		if (sources == null)
//		//			return;
//		//		//包装拷贝事件.
//		//		UserUIEvent event = new
//		// UserUIEvent(this,UserUIEvent.CUT,null,sources);
//		//		//处理拷贝事件.
//		//		if (checkEvent(event)) {
//		//			fireEvent(event);
//		//可以没有cut事件的派发。cut事件由copy和clear两个事件组成。
//		copy(clipType);
//		clear(clipType);
//		//		}
//	}

	/**
	 * 合并单元.组合最后选择区域的单元.
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
		//插件是否允许操作.
		if (!checkEvent(event)) {
			return;
		}
		//插件操作放在模型操作前,对模型操作进行预处理.
		fireEvent(event);
		//模型操作.
		CellsModel cm =this.getCellsModel();
		ArrayList areaDatas = cm.getAreaDatas();
		if (areaDatas != null) {
			Iterator iter = areaDatas.iterator();
			ArrayList listRemove = new ArrayList();
			//检查组合区域是否交叉，是否容纳旧的区域；
			while (iter.hasNext()) {
				IAreaAtt att = (IAreaAtt) iter.next();
				if (att instanceof CombinedCell) {
					//如果区域相交但是不相含，抛出异常。
					if (att.getArea().intersection(area)) {
						if (area.contain(att.getArea())) { //新区域包含旧区域，记录需要删除的旧区域。
							listRemove.add(att);
						} else {
							throw new TableDataModelException();
						}
					}
				}
			}
			areaDatas.removeAll(listRemove);
		}
		//得到当前合并单元首单元的数据。
		cm.combineCell(area);
	}

//	/**
//	 * 解除合并单元。 将组合单元的信息设置在组合单元的首单元上，并且删除组合单元信息。 要求AreaPosition正好是一个组合单元对应的区域.
//	 * 
//	 * @param area
//	 */
//	public void separateCombinedCell(AreaPosition area) {
//		if (area == null || area.isCell()) {
//			return;
//		}
//		UserUIEvent event = new UserUIEvent(this, UserUIEvent.UNCOMBINECELL,
//				area, null);
//		//插件是否允许操作.
//		if (!checkEvent(event)) {
//			return;
//		}
//		//模型操作.
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
////								cCell.getExtFmtAll());//组合单元数据放到首单元中.
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
//						//插件操作放在模型操作后,进行模型操作的后处理.
//						fireEvent(event);
//						return;
//					}
//				}
//			}
//		}
//
//	}

	/**modify by 2008-4-29 王宇光 ，关掉此方法：剪切，复制，粘贴等行为由各个业务插件完成
	 * 将剪切板中的内容复制到选中的位置。
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
//		// 得到当前选中表页的焦点单元。
//		CellsModel cellsModel = getSelectedSheet().getModel();
//		int rowStart = target.getRow();
//		int colStart = target.getColumn();
//		if (c != null) {
//			for (int i = 0; i < c.length; i++) {
//				Cell[] cLine = c[i];
//				if (cLine != null) {
//					for (int j = 0, colPos = colStart; j < cLine.length; j++, colPos++) {
//						if (cLine[j] instanceof CombinedCell) {
//							// 检查当前覆盖的区域是否包含组合单元，如果包括，抛出异常。
//							// CombinedCell cc = (CombinedCell) cLine[j];
//							// AreaPosition area = cc.getArea();
//							// CombineCellCmd.combineCell(area,cellsModel);
//							// 组合单元插件应该放到table项目里吧!然后用事件通知组合事件.
//							// 或者复制粘贴等事件全部在reporttool项目中用插件实现.否则这里无法知道业务是否同意组合.
//						} else {
//							if (cLine[j] == null) {
//								cLine[j] = new Cell();
//							}
//							// 全部默认设置数据和格式,扩展数据是否复制由插件自己决定.
//							if (clipType == CELL_ALL || clipType == CELL_CONTENT) {
//								// cellsModel.setCellValueByAuth(rowStart + i,
//								// colPos, cLine[j].getValue());
//								if (cLine[j].getValue() != null){
//									//modify by chxw 2007-05-17 粘贴单元格内容时，浮点型数据不能用toString方法(数据较大时，返回的是科学计数字符串)
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
//			// 处理粘贴事件.根据测试要求，扩展数据（如公式）看作是内容。
//			if (clipType == CELL_FORMAT || !checkEvent(event)) {
//				return;
//			}
//			fireEvent(event);
//		} else {//从外部程序比如excel中复制过来的数据。不通知插件粘贴事件。
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
	 * 得到其他应用程序中拷贝的内容。
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
		//检查String类型的交互信息。例如Excel在剪切板中，按照\t分割信息。
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
			//转换为单元格式
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
	//	 * 得到所有的右键菜单。如果右键菜单为空，该方法负责构建一个返回，并且添加到对应的组件上。
	//	 * @param nCmpName
	//	 * @return 如果没有右键菜单信息，返回空数组
	//	 */
	//	public JPopupMenu[] getPopupMenu(int nCmpName) {
	//		//todo 还需要添加由于分栏窗口而产生组件上的右键菜单。
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

	//**********************私有方法**********************
    /**
     * 创建空的表格sheet
     * @param infinite 是否是无限表
     * @param rowSize 表的行数
     * @param colSize 表的列数
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

	//**************************打印的代码××××××××××××××

	//**************************事件通知**************************
	//@toList :暂时全表重新绘制，以后添加一个监听器来通知需要绘制的内容
	private void fireSelectChanged() {
		revalidate();
		repaint();
	}

	/**
	 * 设置当前表页的数据模型。
	 * 
	 * @param cellsModel
	 */
	public void setCurCellsModel(CellsModel cellsModel) {

		//add by wangyga 2008-12-17 切换报表时如果有动画，停止动画效果
		BorderPlayRender.stopPlay(getCells());
		
		//记录旧的模型，为转移监听器作准备		
		CellsModel oldCellsModel = getCellsModel();
		if (cellsModel == null||oldCellsModel==cellsModel)
			return;
		/** ****只考虑单表页的情况********* */

		boolean dispatch = true;
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.MODEL_CHANGED, oldCellsModel,
				cellsModel);
		boolean fireEvent = false;
		if (dispatch && checkEvent(event)) {
			fireEvent = true;
		}
		
		//表格模型和选择模型
		this.getCells().setDataModel(cellsModel);
		setCompDataModel(cellsModel);

		//行列模型
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
		
		//TODO 此处设置树模型

		/**转移旧的模型上的其他监听器*///(目前未处理HeaderModel实现了的TreeModelListener)
		
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
		
		//模型操作需要，保留旧模型的header监听
			
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
 
//		//分栏锁定的设置。
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
		
		//分栏锁定的设置。
		SeperateLockSet set = cellsModel.getSeperateLockSet();
		if(set == null){
			setFreezing(false);
			cancelSeperate();			
		}else{
			
//	 5.02升级5.5之后，如果设置分栏后，在数据录入界面会引发绘制缓慢问题，解决方式是先cnacel再set
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
     * 设置相关组件的数据模型
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
     * 如果监听器是旧的模型，对应成新模型
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
     * 表头改变的事件处理
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
	 * 插入单元格.
	 * 
	 * @param aimArea
	 * @param insertType
	 *            见InsertCellDlg:MOVE_RIGHT_WHEN_INSERT_CELL,MOVE_DOWN_WHEN_INSERT_CELL
	 */
	public void insertCells(final AreaPosition aimArea, final int insertType) {
		if (aimArea == null)
			return;
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.INSERTCELL,
				new Integer(insertType), aimArea);
		if (checkEvent(event)) {//检查动态区域,组合单元
			fireEvent(event);//删除组合单元
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
	 * 删除单元格
	 * 处理过程: 1.向所有实现UserActionListner的监听器类对象广播UserUIEvent.DELETECELL事件(如删除该单元格上的合并单元格,关键字,指标,公式)
	 *          2.清除要删除区域上的数据和格式信息(主要是CellsModel没有处理UserUIEvent.DELETECELL事件)
	 *          3.移动区域(先获取要移动区域单元格,清除所有要移动的单元格上的信息,重新设置移动后的单元格的信息)
	 * @param aimArea 要删除的区域
	 * @param deleteType 删除后其他单元的移动类型(DeleteDialog中定义)
	 */
	public void deleteCells(AreaPosition aimArea, int deleteType) {
		if (aimArea == null)
			return;
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.DELETECELL,
				new Integer(deleteType), aimArea);
		if (checkEvent(event)) {//检查动态区域,组合单元
			fireEvent(event);//删除组合单元
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

//			// 5.02升级5.5之后，如果设置分栏后，在数据录入界面会引发绘制缓慢问题，解决方式是先cnacel再set
//			cancelSeperate();

			setSeperatePos(slSet.getSeperateRow(), slSet.getSeperateCol());
		}
	}

	public Clipboard getClipboard() {
		return m_Clipboard;
	}
	
	/**
	 * 获得表页显示风格
	 * @create by wangyga at 2009-9-3,上午11:10:21
	 *
	 * @return
	 */
	public ReportStyle getStyle() {
		return m_reportStyle;
	}

	/**
	 * 设置表页显示风格
	 * @create by wangyga at 2009-9-3,上午11:10:27
	 *
	 * @param reportStyle
	 */
	public void setStyle(ReportStyle reportStyle) {
		m_reportStyle = reportStyle;
		// 修改页面显示的风格.
		setRowHeaderVisible(m_reportStyle.isShowRowHeader());
		setColHeaderVisible(m_reportStyle.isShowColHeader());
		getCells().setGridColor(m_reportStyle.getGrid());
		// 显示比例
		setViewScale((m_reportStyle.getPercent() * 0.01));
		// 为0是否显示.
		repaint();
	}
}  