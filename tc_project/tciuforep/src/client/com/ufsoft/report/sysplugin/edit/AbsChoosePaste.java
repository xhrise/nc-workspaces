package com.ufsoft.report.sysplugin.edit;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.iufo.pub.tools.DeepCopyUtilities;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.sysplugin.cellpostil.CellPostilDefPlugin;
import com.ufsoft.report.sysplugin.postil.PostilVO;
import com.ufsoft.report.undo.CellUndo;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellSelection;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedAreaModel;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.TableDataModelException;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.header.HeaderModel;
import com.ufsoft.table.re.BorderPlayRender;

/**
 * <pre>
 * </pre>
 * 
 * 选择性粘贴：所有选择项的父类
 * 
 * @author 王宇光
 * @version Create on 2008-4-14
 */
public abstract class AbsChoosePaste {
	/** 是否转置 */
	private boolean b_isTransfer = false;
	/** 表格组件 */
	private UfoReport m_rep = null;
	
	private CellsModel cellsModel = null;

	private CellPosition anchorCell = null;
	
	private EditParameter editParam = null;
	
	public AbsChoosePaste(UfoReport m_rep, boolean b_isTransfer) {
		if (m_rep == null) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1004052"));
		}
		this.m_rep = m_rep;
		this.b_isTransfer = b_isTransfer;
	}

	/**
	 * add by 王宇光 2008-3-23 粘贴执行方法
	 */
	public void choosePaste() {
		Clipboard m_Clipboard = getTable().getClipboard();// 获得剪切板
		if (m_Clipboard == null) {
			return;
		}
		Transferable content = m_Clipboard.getContents(getTable());
		CellPosition target = getAnchorCell();
		if (target == null)
			return;
		EditParameter parameter = getEditParameter();
		Cell[][] arryCells = null;
//		AreaPosition copyArea = null;
		if(parameter != null){
//			copyArea = parameter.getCopyArea();
			arryCells = parameter.getCopyCells();
//			arryCells = getCells(copyArea);
		}	
		
		if (arryCells == null) {// 从外部程序比如excel中复制过来的数据。不通知插件粘贴事件。
			pasteFromExcel(content, target);
			return;
		}
		
		arryCells = areaExtend(arryCells, parameter.getEditType());// 区域的扩展
		
		if (isTransfer()) {// 处理转置
			arryCells = transferPosition(arryCells);
		}
		  
		
		executePaste(arryCells, content);
		 
		
	}

	/**
	 * 具体的粘贴方法，子类实现
	 * 
	 * @param Cell
	 *            cell, int row, int column)
	 * 
	 */
	protected abstract void paste(Cell cell, int row, int column);// 具体状态需要的方法

	/**
	 * 用户事件，根据需要，是否通知插件
	 * 
	 * @param Object
	 *            content, String pasteType,Cell[][] c
	 * 
	 */
	protected abstract boolean userEventAction(Object content,
			String pasteType,// 根据需要，是否通知插件
			Cell[][] c);

	/**
	 * 粘贴类型
	 * 
	 * @param Cell
	 *            cell, int row, int column)
	 * @return String
	 */
	protected abstract String getPasteType();

	/**
	 * 区域的扩展
	 * 
	 * @param CellsModel
	 *            cellsModel:数据模型,Cell[][] aryNewCells:粘贴选择区域数组, CellPosition
	 *            target:锚点, Transferable content:粘贴内容,Cell[][]
	 *            aryOldCells：转置之前的数组
	 * 
	 */
	private void executePaste(Cell[][] aryNewCells, Transferable content) {
		if (aryNewCells == null || aryNewCells.length == 0
				|| aryNewCells[0] == null || aryNewCells[0].length == 0) {
			return;
		}
		
	   CellsModel cellsModel = getDataModel();
		
		CellUndo undo = new CellUndo();
		if(getEditType() == EditParameter.CUT){
			AreaPosition cutArea = getEditParameter().getCopyArea();
			CellPosition[] positions = getDataModel().getSeperateCellPos(cutArea).toArray(new CellPosition[0]);
			Cell[] cutCells = getDataModel().getCells(positions);
			for (int i = 0; i < cutCells.length; i++) {
				Cell cell = cutCells[i];
				if(cell != null){
					cell = (Cell) cell.clone();
					undo.addCell(positions[i], cell, null);
				}
			}
		}
		 //事件派发之前加入undo，用以事件处理中增加子undo
		cellsModel.fireUndoHappened(undo);

		if (!userEventAction(content, getPasteType(), aryNewCells)) {
			return;
		}
		
		try {
			// 获得起始行列
			int rowStart = getAnchorCell().getStart().getRow();
			int colStart = getAnchorCell().getStart().getColumn();
			for (int i = 0; i < aryNewCells.length; i++) {
				Cell[] cLine = aryNewCells[i];
				if (cLine != null && cLine.length > 0) {
					for (int j = 0, colPos = colStart; j < cLine.length; j++, colPos++) {
						if (cLine[j] == null) {
							continue;
						}
						
						CellPosition pos = CellPosition.getInstance(rowStart + i, colPos);
						CombinedCell cc = cellsModel.getCombinedAreaModel().belongToCombinedCell(pos);
						if(cc != null && !pos.equals(cc.getArea().getStart())){
							continue;
						}
						
						Cell oldCell = getDataModel().getCell(pos);
						if(oldCell != null){
							oldCell = (Cell) oldCell.clone();
						}
						
						paste(cLine[j], rowStart + i, colPos);
						
						Cell newCell = getDataModel().getCell(pos);
						
						undo.addCell(pos, oldCell, newCell);
						 
					}
				}
			}
			
			// 获得新的选择区域，重新设定选中模型
			AreaPosition areaPosition = AreaPosition.getInstance(rowStart,
					colStart, aryNewCells[0].length, aryNewCells.length);
			cellsModel.getSelectModel().setSelectedArea(areaPosition);

			if (getEditType() == EditParameter.CUT) {// 粘贴时，如果上次操作是执行剪切，则需要停止动画
//				if(!isFormatState(m_rep)){//数据态时只清除值
					clear(getClipType(), getCopyCellPosition());// 粘贴之前清楚剪切时选择区域的内容
//				}
				BorderPlayRender.stopPlay(getTable().getCells());
			}
		}catch (Exception e) {
			AppDebug.debug(e);
		} finally {
			cellsModel.setEnableEvent(true);
		}
	}

	/**
	 * 区域的扩展，如果所选区域有合并单元：如果对合并单元部分修改，弹出错误提示信息；只能全部修改
	 * 
	 * @param Cell[][]
	 *            aryCells：复制获得二维数组
	 * @return Cell[][] 行列扩展后选中区域的数组
	 */
	private Cell[][] areaExtend(Cell[][] aryCells, int editType) {

		if (aryCells == null || aryCells.length == 0 || aryCells[0] == null
				|| aryCells[0].length == 0) {
			return null;
		}
		Cell[][] aryNewCells = getSource();// 获得执行粘贴时，选中的区域
		if (aryNewCells == null || aryNewCells.length == 0
				|| aryNewCells[0] == null || aryNewCells[0].length == 0) {
			return null;
		}

		// 粘贴(除格式刷)时，区域的扩展粘贴时选择的区域行列必须是复制时的整数倍
		if (editType != EditParameter.BRUSH
				&& !isExtendPaste(aryCells, aryNewCells)) {
			aryNewCells = aryCells;
			return aryNewCells;
		}

		if (editType == EditParameter.CUT
				|| isSwitchArea(aryNewCells, aryCells)) {
			aryNewCells = aryCells;
			return aryNewCells;
		}

		int rowCount = aryCells.length;
		int colCount = aryCells[0].length;
		int iRow = 0;// 复制区域的行
		int cellsLength = aryNewCells.length;
		for (int i = 0; i < cellsLength; i++) {
			Cell[] cLine = aryNewCells[i];
			int iColumn = 0;// 复制区域的列
			if (cLine != null) {
				int length = cLine.length;
				for (int j = 0; j < length; j++) {
					aryNewCells[i][j] = aryCells[iRow][iColumn];
					iColumn++;
					if ((j + 1) % colCount == 0) {// 顺着aryNewCells扩展列，保证数组aryCells列数据取完时，再从第0列开始取
						iColumn = 0;
					}
				}
			}
			iRow++;
			if ((i + 1) % rowCount == 0) {// 顺着aryNewCells扩展行，保证数组aryCells行数据取完时，再从第0行开始取
				iRow = 0;
			}
		}
		return aryNewCells;
	}

	/**
	 * 区域拆分
	 * @param AreaPosition areaPosition:原区域,Cell[][] cells：目标区域
	 * @return
	 */
	private AreaPosition[] getSeperateCells(AreaPosition areaPosition,
			Cell[][] cells) {

//		AreaPosition selectArea = getDataModel().getSelectModel()
//				.getSelectedArea();
//		if (selectArea == null) {
//			return null;
//		}
		ArrayList<AreaPosition> areaList = new ArrayList<AreaPosition>();
		// 获得起始行列
		int rowStart = getAnchorCell().getStart().getRow();
		int colStart = getAnchorCell().getStart().getColumn();
		int iRowCount = areaPosition.getHeigth();
		int iColumnCount = areaPosition.getWidth();
		int iCellsLength = cells.length;
		for (int i = 0; i < iCellsLength; i++) {
			Cell[] cLine = cells[i];
			if (cLine != null) {
				int ilength = cLine.length;
				for (int j = 0, colPos = colStart; j < ilength; j++, colPos++) {
					if (i % iRowCount == 0 && j % iColumnCount == 0) {
						AreaPosition area = AreaPosition.getInstance(rowStart
								+ i, colStart + j, iColumnCount, iRowCount);
						areaList.add(area);
					}
				}
			}
		}

		return areaList.toArray(new AreaPosition[0]);
	}

	protected void pasteAll(Cell cell, int row, int column) {
		if (cell == null ||row < 0 || column < 0) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1004055"));
		}
		int editType = getEditType();
		CellsModel cellsModel = getDataModel();
		if(editType == EditParameter.CUT){//剪切时是单元的移动
			CellPosition oldCellPos = CellPosition.getInstance(cell.getRow(),cell.getCol());
			AreaPosition oldAreaPos = AreaPosition.getInstance(oldCellPos, oldCellPos);	
			
			EditParameter parameter = getEditParameter();
			AreaPosition[] pasteAreas = parameter.getPasteAreas();
			if(pasteAreas == null || pasteAreas.length == 0)
				return;
			if(!pasteAreas[0].contain(oldAreaPos)){
				cellsModel.setCell(cell.getRow(), cell.getCol(), null);	
			}
			cellsModel.setCell(row, column, cell);
					
//			cellsModel.moveCells(oldAreaPos, newCellPos);	
//			getTable().getCells().changeSelectionByUser(oldAreaPos.getStart().getRow(), oldAreaPos.getStart().getColumn(), false, false, false);
		}else if(editType == EditParameter.COPY){
			Cell newCell = (Cell)cell.clone();
			cellsModel.setCell(row, column, newCell);
		}
	}
	
	/**
	 * //从外部程序比如excel中复制过来的数据。不通知插件粘贴事件。
	 * 
	 * @param Transferable
	 *            content,CellPosition target
	 * @return
	 */
	private void pasteFromExcel(Transferable content, CellPosition target) {
		UFOTable table = getTable();
		if (table == null) {
			return;
		}
		String[][] result = getAppClipCell(content);
		if (result != null) {
			for (int i = 0; i < result.length; i++) {
				String[] row = result[i];
				if (row != null) {
					for (int j = 0; j < row.length; j++) {
						// cellsModel.setCellValue(rowStart + i, colStart
						// + j, row[j]);
						table.simulateKeyBoardInput(target.getRow() + i, target
								.getColumn()
								+ j, row[j]);
					}
				}
			}
		}
	}

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
		// 检查String类型的交互信息。例如Excel在剪切板中，按照\t分割信息。
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
			// 转换为单元格式
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

	/**
	 * 粘贴格式
	 * 
	 * @param Cell
	 *            cell, int row, int column
	 * @return
	 */
	protected void pasteFormat(Cell cell, int row, int column) {
		if (cell == null) {
			return;
		}
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1004055"));
		}
		CellsModel cellsModel = getDataModel();
		Format cellFormat = cell.getFormat();
		Format cellNewFormat = null;
		if (cellFormat != null) {
			cellNewFormat = (Format) cellFormat.clone();
		}
//		if (getEditType() == EditParameter.BRUSH) {
			cellsModel.setCellFormat(row, column, cellNewFormat);// 格式刷改变格式时，不受表格权限的控制
//		} else {
//			cellsModel.setCellFormatByAuth(row, column, cellNewFormat);// 格式
//		}
	}

	/**
	 * 是否交换区域，如果粘贴时选择的区域小于复制的区域，则粘贴时区域的大小等于复制时的大小
	 * 
	 * @param Cell[][]
	 *            selectCells, Cell[][] pasteCells
	 * @return boolean
	 */
	private boolean isSwitchArea(Cell[][] selectCells, Cell[][] pasteCells) {
		if (selectCells.length <= pasteCells.length
				&& selectCells[0].length <= pasteCells[0].length) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isExtendPaste(Cell[][] aryCells, Cell[][] arySelectCells) {
		if (arySelectCells.length % aryCells.length == 0
				&& arySelectCells[0].length % aryCells[0].length == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得编辑类型
	 * 
	 * @param
	 * @return int
	 */
	private int getEditType() {
		int iEditType = getEditParameter().getEditType();
		return iEditType;
	}

	/**
	 * 获得业务操作类型
	 * @return
	 */
	private int getClipType() {
		int iClipType = getEditParameter().getClipType();
		if(!isFormatState(m_rep)){
			iClipType = EditParameter.CELL_CONTENT;
		}
		return iClipType;
	}

	/**
	 * 判断粘贴的区域内是否有值，公式，组合单元
	 * 
	 * @param
	 * @return boolean
	 */
	private boolean isBestrowArea(Cell[][] aryNewCells, CellPosition target) {
		if (aryNewCells == null || aryNewCells.length == 0
				|| aryNewCells[0] == null || aryNewCells[0].length == 0) {
			throw new IllegalArgumentException();
		}
		if (target == null) {
			throw new IllegalArgumentException();
		}
		int rowStart = target.getRow();
		int colStart = target.getColumn();
		CellsModel cellsModel = getDataModel();
		int cellsLength = aryNewCells.length;
		for (int i = 0; i < cellsLength; i++) {
			Cell[] cLine = aryNewCells[i];
			if (cLine != null) {
				int length = cLine.length;
				for (int j = 0, colPos = colStart; j < length; j++, colPos++) {
					Cell cell = cellsModel.getCell(rowStart + i, colPos);
					if (cell != null && cell.getValue() != null) {// 粘贴区域内有值或者公式
						return true;
					}
				}
			}
		}

		AreaPosition area = AreaPosition.getInstance(rowStart, colStart,
				aryNewCells[0].length, aryNewCells.length);
		CombinedCell[] combinedCells = CombinedAreaModel.getInstance(cellsModel).getCombineCells(area);
		if (combinedCells != null && combinedCells.length > 0) {
			return true;
		}
		return false;
	}

	/**
	 * add by 王宇光 2008-3-23 提供单元二维数组的转置
	 * 
	 * @param Cell[][]
	 *            cell
	 * 
	 * @return Cell[][]
	 */
	private Cell[][] transferPosition(Cell[][] cell) {
		if (cell == null || cell.length == 0 || cell[0].length == 0) {
			return null;
		}
		Cell[][] arryNewCells = new Cell[cell[0].length][cell.length];
		for (int i = 0; i < cell.length; i++) {
			Cell[] aryCell = cell[i];
			for (int j = 0; j < aryCell.length; j++) {
				arryNewCells[j][i] = aryCell[j];
			}
		}
		return arryNewCells;
	}

	/**
	 * add by 王宇光 2008-3-23 粘贴批注
	 * 
	 * @param CellsModel
	 *            cellModel:报表模型,int row：行, int column：列, CellPosition
	 *            curPosition：原位置
	 * 
	 */
	protected void pastePostil(CellsModel cellModel, int row, int column,
			CellPosition curPosition) {
		if (cellModel == null || curPosition == null) {
			return;
		}
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1004055"));
		}
		// 获得原批注
		PostilVO pvo = (PostilVO) cellModel.getBsFormat(curPosition,
				CellPostilDefPlugin.EXT_FMT_POSTIL);
		if (pvo == null) {
			return;
		}
		CellPosition cellPosition = CellPosition.getInstance(row, column);
		cellModel.setBsFormat(cellPosition, CellPostilDefPlugin.EXT_FMT_POSTIL, pvo);
	}

	/**
	 * add by 王宇光 2008-3-23 去除Format的边框信息
	 * 
	 * @param CellsModel
	 *            cellModel:报表模型,int row：行, int column：列, Format curFormat：当前格式
	 * 
	 */
	protected void pasteNoBorder(CellsModel cellModel, int row, int column,
			Cell cell) {
		if (cellModel == null || cell == null) {
			return;
		}
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1004055"));
		}
		IufoFormat copyFormat = (IufoFormat)cell.getFormat();
		if(copyFormat == null)
			return;
		IufoFormat format = (IufoFormat)copyFormat.clone();
		format.setLineType(Format.TOPLINE, TableConstant.UNDEFINED);
		format.setLineColorIndex(Format.TOPLINE, TableConstant.UNDEFINED);
		format.setLineType(Format.BOTTOMLINE, TableConstant.UNDEFINED);
		format.setLineColorIndex(Format.BOTTOMLINE, TableConstant.UNDEFINED);
		format.setLineType(Format.LEFTLINE, TableConstant.UNDEFINED);
		format.setLineColorIndex(Format.LEFTLINE, TableConstant.UNDEFINED);
		format.setLineType(Format.RIGHTLINE, TableConstant.UNDEFINED);
		format.setLineColorIndex(Format.RIGHTLINE, TableConstant.UNDEFINED);
		cellModel.setCellFormatByAuth(row, column, format);
	}

	/**
	 * add by 王宇光 2008-3-23 粘贴某列列宽
	 * 
	 * @param CellsModel
	 *            cellModel:报表模型,int row：行, int column：列, int columnWidth：改变后的列宽
	 * 
	 */
	protected void pasteColumnWidth(CellsModel cellModel, int column,
			int curColumn) {
		if (cellModel == null) {
			return;
		}
		if (column < 0) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1004055"));
		}
		// 获得列头模型
		HeaderModel m_ColumnHeaderModel = cellModel.getColumnHeaderModel();
		if (m_ColumnHeaderModel == null) {
			return;
		}
		Header newHeader = m_ColumnHeaderModel.getHeader(column);
		Header curHeader = m_ColumnHeaderModel.getHeader(curColumn);
		if (newHeader == null || curHeader == null) {
			return;
		}
		newHeader.setSize(curHeader.getSize());
		m_ColumnHeaderModel.setHeader(column, newHeader);
	}

	/**
	 * add by 王宇光 2008-3-23 只粘贴数字格式：分割号，百分号，小数位数，货币符号，中文大小写，负数显示
	 * 
	 * @param CellsModel
	 *            cellModel:报表模型,int row：行, int column：列, Format curFormat：当前格式
	 * 
	 */
	protected void pasteNumberFormat(CellsModel cellModel, int row, int column,
			Format curFormat, String pasteType) {
		if (cellModel == null || curFormat == null || pasteType == null
				|| pasteType.length() == 0) {
			return;
		}
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1004055"));
		}
		IufoFormat ufoFromat = (IufoFormat) curFormat.clone();
		IufoFormat newFormat = new IufoFormat();
		newFormat.setCellType(ufoFromat.getCellType());
		newFormat.setHasComma(ufoFromat.isHasComma());
		newFormat.setHasPercent(ufoFromat.isHasPercent());
		newFormat.setDecimalDigits(ufoFromat.getDecimalDigits());
		newFormat.setCurrencySymbol(ufoFromat.getChineseFormat());
		newFormat.setChineseFormat(ufoFromat.getChineseFormat());
		newFormat.setMinusFormat(ufoFromat.getMinusFormat());
		if (pasteType.equals(EditPasteExt.ALL)
				|| pasteType.equals(EditPasteExt.TRANSFER)) {
			cellModel.setCellFormatByAuth(row, column, curFormat);
		} else {
			cellModel.setCellFormatByAuth(row, column, newFormat);
		}

	}

	/**
	 * add by 王宇光 2008-3-23 用户事件 处理粘贴事件.根据测试要求，扩展数据（如公式）看作是内容。
	 * 
	 * @param Object
	 *            content：以前数据,String pasteType：粘贴类型,Cell[][] c：单元二维数组
	 * 
	 */
	protected boolean userEventAction(Object content, String pasteType,
			Cell[][] c, UFOTable table) {
		if (table == null || content == null) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1000496"));// 输入参数不允许为空
		}
		EditParameter parameter = getEditParameter();
		if(isTransfer()){
			c = transferPosition(c);
		}
		AreaPosition[] area = getSeperateCells(parameter.getCopyArea(), c);
		parameter.setTransfer(isTransfer());
		parameter.setPasteAreas(area);
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.PASTE, content,
				parameter);
		if (!table.checkEvent(event)) {
			BorderPlayRender.stopPlay(table.getCells());
			return false;
		}
//		int iUserSel = 0;
//		if (getEditType() != EditParameter.BRUSH
//				&& isBestrowArea(c, getAnchorCell())) {// 是否覆盖待粘贴区域
//			iUserSel = JOptionPane.showConfirmDialog(m_rep, MultiLang.getString("miufo1004054"), "",
//					JOptionPane.YES_NO_OPTION);
//			if (iUserSel == JOptionPane.NO_OPTION) {
//				return false;
//			}
//		}
//		if(iUserSel == JOptionPane.YES_OPTION){
			table.fireEvent(event);
//		}
		
		return true;
	}

	/**
	 * add by 王宇光 2008-3-23 获得复制时的区域位置
	 * 
	 * @param Cell[][]
	 *            aryCells：复制时的区域单元数组
	 * 
	 */
	private AreaPosition[] getCopyCellPosition() {
		AreaPosition area = getEditParameter().getCopyArea();
		AreaPosition[] aryArea = { area };
		return aryArea;
	}

	/**
	 * 判断报表状态
	 * @param report
	 * @return
	 */
	private boolean isFormatState(UfoReport report){
		return report.getOperationState() == UfoReport.OPERATION_FORMAT;
	}
	
	private EditParameter getEditParameter() {
		if(editParam != null){
			return editParam;
		}
		Clipboard m_Clipboard = getTable().getClipboard();// 获得剪切板
		if (m_Clipboard == null) {
			return null;
		}
		Transferable transferable = m_Clipboard.getContents(getTable());
		if(transferable == null){
			return null;
		}
		EditParameter parameter = null;
		try {
			if(transferable.isDataFlavorSupported(CellSelection.EDIT_PARAMETER)){
				parameter = (EditParameter) transferable
				.getTransferData(CellSelection.EDIT_PARAMETER);
			}
			
		} catch (UnsupportedFlavorException e) {			
			AppDebug.debug(e);
		} catch (IOException e) {		
			AppDebug.debug(e);
		}
		editParam = parameter;
		return parameter;
	}

	/**
	 * 清除指定区域的单元中的数据
	 * 
	 * @param clipType
	 * @param dispatch
	 * @param areas：需要清楚的区域
	 */
	private void clear(int clipType, boolean dispatch, AreaPosition[] areas) {
		UFOTable table = getTable();
		if (areas == null || areas.length == 0)
			return;
		// 包装拷贝事件.
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.CLEAR, null,
				areas);
		if (dispatch && table.checkEvent(event)) {
			table.fireEvent(event);
		}
		// 删除数据和格式 简介不分格式、内容、默认时全部
		if(!isFormatState(getReport())){
			CellsModel model = table.getCellsModel();
			model.clearArea(clipType, areas);
		}
	}

	/**
	 * 清除指定区域的单元中的数据
	 * 
	 * @param
	 * clipType，参考UFOTable的CELL_ALL、CELL_CONTENT、CELL_FORMAT。areas:需要清楚的区域
	 */
	private void clear(int clipType, AreaPosition[] areas) {
		clear(clipType, clipType == UFOTable.CELL_ALL
				|| clipType == UFOTable.CELL_CONTENT, areas);
	}

	/** 得到选择区域的单元信息 */
	private Cell[][] getSource() {
		Cell[][] sources = null;
		AreaPosition area = getTable().getCellsModel()
				.getSelectModel().getSelectedArea();
		if (area != null) {
			try {
				sources = getCells(area,getDataModel());
			} catch (TableDataModelException ex) {
				AppDebug.debug(ex);
			}
		}
		return sources;
	}

	/**
	 * 读区域
	 * 
	 * @param area
	 * @return Cell[][]
	 * @throws TableDataModelException
	 */
	static Cell[][] getCells(AreaPosition area,CellsModel cellsModel) throws TableDataModelException {
		if(area == null || cellsModel == null)
			throw new IllegalArgumentException();

		// 首先判断区域是否有效
		CellPosition cellStart = area.getStart();
		CellPosition cellEnd = area.getEnd();
		// 判断区间范围是否相等
		int startRow = cellStart.getRow();
		int startCol = cellStart.getColumn();
		int endRow = cellEnd.getRow();
		int endCol = cellEnd.getColumn();
		int heigth = endRow - startRow + 1;
		int width = endCol - startCol + 1;
		Cell[][] areaDatas = null;
		if (cellStart.equals(cellEnd)) {
			CombinedCell combinedCell = cellsModel.belongToCombinedCell(
					cellStart.getRow(), cellStart.getColumn());
			if (combinedCell != null) {
				AreaPosition areaPosition = combinedCell.getArea();
				areaDatas = new Cell[areaPosition.getHeigth()][areaPosition
						.getWidth()];
			} else {
				areaDatas = new Cell[heigth][width];
			}
		} else {
			areaDatas = new Cell[heigth][width];
		}
		Cell cell = null;
		HashSet hs = new HashSet();
		for (int i = 0; i < areaDatas.length; i++) {
			for (int j = 0; j < areaDatas[i].length; j++) {
				CellPosition cp = CellPosition.getInstance(startRow + i,
						startCol + j);
				if (hs.contains(cp)) {
					continue;
				}
				cell = cellsModel.getCellIfNullNew(cp.getRow(), cp.getColumn());// 复制单元格公式时，需要知道位置信息。
				areaDatas[i][j] = cell;
				if (cell != null && cell instanceof CombinedCell) { // 检查是否是合并单元
					CombinedCell cc = (CombinedCell) cell;
					hs.addAll(cellsModel.getSeperateCellPos(cc.getArea()));
				}

			}
		}
		return areaDatas;
	}

	/**
	 * add by 王宇光 2008-3-23 粘贴值
	 * 
	 * @param CellsModel
	 *            cellsModel, UFOTable table, Cell cell, int row, int column
	 * 
	 */
	protected void pasteValue(CellsModel cellsModel, UFOTable table, Cell cell,
			int row, int column) {
		if (cellsModel == null || cell == null) {
			return;
		}
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1004055"));
		}
		
		Object value = cell.getValue();
		if(value == null){
			return;
		}
		if(value instanceof Cloneable){//处理大对象，比如图表
			cellsModel.setCellValueByAuth(row, column, DeepCopyUtilities.getDeepCopy(value));
		} else {
			if (cell.getValue() != null) {
				// 粘贴单元格内容时，浮点型数据不能用toString方法(数据较大时，返回的是科学计数字符串)
				String inputText = cell.getValue().toString();
				if (cell.getValue() instanceof Double) {
					double dValue = ((Double) cell.getValue()).doubleValue();
					IufoFormat format = (IufoFormat) cellsModel
							.getFormatIfNullNew(CellPosition.getInstance(cell
									.getRow(), cell.getCol()));
					inputText = format.getString(dValue);
				}
				table.simulateKeyBoardInput(row, column, inputText);
			} else {
				table.simulateKeyBoardInput(row, column, "");
			}
		}
	}
	
	protected UFOTable getTable() {
		return m_rep.getTable();
	}

	protected UfoReport getReport(){
		return m_rep;
	}
	
	public CellsModel getDataModel() {
		if(this.cellsModel == null){
			return m_rep.getCellsModel();
		}
		return this.cellsModel;
	}

	/**
	 * 提供对cellsModel的设置：有时数据态时也需要做格式设置
	 * @param cellsModel
	 */
	public void setDataModel(CellsModel cellsModel){
		this.cellsModel = cellsModel;
	}
	
	public CellPosition getAnchorCell() {
		if(this.anchorCell == null){//此处不要去获得锚点
			return getReport().getCellsModel().getSelectModel().getSelectedArea().getStart();
		}
        return this.anchorCell;
	}

	/**
	 * 设置锚点：有时数据态时也需要做格式设置
	 * @param anchorCell
	 */
	public void setAnchorCell(CellPosition anchorCell){
		this.anchorCell = anchorCell;
	}
	
	/**
	 * 是否转置
	 * @return
	 */
	public boolean isTransfer() {
		return b_isTransfer;
	}
}
