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
 * ѡ����ճ��������ѡ����ĸ���
 * 
 * @author �����
 * @version Create on 2008-4-14
 */
public abstract class AbsChoosePaste {
	/** �Ƿ�ת�� */
	private boolean b_isTransfer = false;
	/** ������ */
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
	 * add by ����� 2008-3-23 ճ��ִ�з���
	 */
	public void choosePaste() {
		Clipboard m_Clipboard = getTable().getClipboard();// ��ü��а�
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
		
		if (arryCells == null) {// ���ⲿ�������excel�и��ƹ��������ݡ���֪ͨ���ճ���¼���
			pasteFromExcel(content, target);
			return;
		}
		
		arryCells = areaExtend(arryCells, parameter.getEditType());// �������չ
		
		if (isTransfer()) {// ����ת��
			arryCells = transferPosition(arryCells);
		}
		  
		
		executePaste(arryCells, content);
		 
		
	}

	/**
	 * �����ճ������������ʵ��
	 * 
	 * @param Cell
	 *            cell, int row, int column)
	 * 
	 */
	protected abstract void paste(Cell cell, int row, int column);// ����״̬��Ҫ�ķ���

	/**
	 * �û��¼���������Ҫ���Ƿ�֪ͨ���
	 * 
	 * @param Object
	 *            content, String pasteType,Cell[][] c
	 * 
	 */
	protected abstract boolean userEventAction(Object content,
			String pasteType,// ������Ҫ���Ƿ�֪ͨ���
			Cell[][] c);

	/**
	 * ճ������
	 * 
	 * @param Cell
	 *            cell, int row, int column)
	 * @return String
	 */
	protected abstract String getPasteType();

	/**
	 * �������չ
	 * 
	 * @param CellsModel
	 *            cellsModel:����ģ��,Cell[][] aryNewCells:ճ��ѡ����������, CellPosition
	 *            target:ê��, Transferable content:ճ������,Cell[][]
	 *            aryOldCells��ת��֮ǰ������
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
		 //�¼��ɷ�֮ǰ����undo�������¼�������������undo
		cellsModel.fireUndoHappened(undo);

		if (!userEventAction(content, getPasteType(), aryNewCells)) {
			return;
		}
		
		try {
			// �����ʼ����
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
			
			// ����µ�ѡ�����������趨ѡ��ģ��
			AreaPosition areaPosition = AreaPosition.getInstance(rowStart,
					colStart, aryNewCells[0].length, aryNewCells.length);
			cellsModel.getSelectModel().setSelectedArea(areaPosition);

			if (getEditType() == EditParameter.CUT) {// ճ��ʱ������ϴβ�����ִ�м��У�����Ҫֹͣ����
//				if(!isFormatState(m_rep)){//����̬ʱֻ���ֵ
					clear(getClipType(), getCopyCellPosition());// ճ��֮ǰ�������ʱѡ�����������
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
	 * �������չ�������ѡ�����кϲ���Ԫ������Ժϲ���Ԫ�����޸ģ�����������ʾ��Ϣ��ֻ��ȫ���޸�
	 * 
	 * @param Cell[][]
	 *            aryCells�����ƻ�ö�ά����
	 * @return Cell[][] ������չ��ѡ�����������
	 */
	private Cell[][] areaExtend(Cell[][] aryCells, int editType) {

		if (aryCells == null || aryCells.length == 0 || aryCells[0] == null
				|| aryCells[0].length == 0) {
			return null;
		}
		Cell[][] aryNewCells = getSource();// ���ִ��ճ��ʱ��ѡ�е�����
		if (aryNewCells == null || aryNewCells.length == 0
				|| aryNewCells[0] == null || aryNewCells[0].length == 0) {
			return null;
		}

		// ճ��(����ʽˢ)ʱ���������չճ��ʱѡ����������б����Ǹ���ʱ��������
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
		int iRow = 0;// �����������
		int cellsLength = aryNewCells.length;
		for (int i = 0; i < cellsLength; i++) {
			Cell[] cLine = aryNewCells[i];
			int iColumn = 0;// �����������
			if (cLine != null) {
				int length = cLine.length;
				for (int j = 0; j < length; j++) {
					aryNewCells[i][j] = aryCells[iRow][iColumn];
					iColumn++;
					if ((j + 1) % colCount == 0) {// ˳��aryNewCells��չ�У���֤����aryCells������ȡ��ʱ���ٴӵ�0�п�ʼȡ
						iColumn = 0;
					}
				}
			}
			iRow++;
			if ((i + 1) % rowCount == 0) {// ˳��aryNewCells��չ�У���֤����aryCells������ȡ��ʱ���ٴӵ�0�п�ʼȡ
				iRow = 0;
			}
		}
		return aryNewCells;
	}

	/**
	 * ������
	 * @param AreaPosition areaPosition:ԭ����,Cell[][] cells��Ŀ������
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
		// �����ʼ����
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
		if(editType == EditParameter.CUT){//����ʱ�ǵ�Ԫ���ƶ�
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
	 * //���ⲿ�������excel�и��ƹ��������ݡ���֪ͨ���ճ���¼���
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
		// ���String���͵Ľ�����Ϣ������Excel�ڼ��а��У�����\t�ָ���Ϣ��
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
			// ת��Ϊ��Ԫ��ʽ
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
	 * ճ����ʽ
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
			cellsModel.setCellFormat(row, column, cellNewFormat);// ��ʽˢ�ı��ʽʱ�����ܱ��Ȩ�޵Ŀ���
//		} else {
//			cellsModel.setCellFormatByAuth(row, column, cellNewFormat);// ��ʽ
//		}
	}

	/**
	 * �Ƿ񽻻��������ճ��ʱѡ�������С�ڸ��Ƶ�������ճ��ʱ����Ĵ�С���ڸ���ʱ�Ĵ�С
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
	 * ��ñ༭����
	 * 
	 * @param
	 * @return int
	 */
	private int getEditType() {
		int iEditType = getEditParameter().getEditType();
		return iEditType;
	}

	/**
	 * ���ҵ���������
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
	 * �ж�ճ�����������Ƿ���ֵ����ʽ����ϵ�Ԫ
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
					if (cell != null && cell.getValue() != null) {// ճ����������ֵ���߹�ʽ
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
	 * add by ����� 2008-3-23 �ṩ��Ԫ��ά�����ת��
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
	 * add by ����� 2008-3-23 ճ����ע
	 * 
	 * @param CellsModel
	 *            cellModel:����ģ��,int row����, int column����, CellPosition
	 *            curPosition��ԭλ��
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
		// ���ԭ��ע
		PostilVO pvo = (PostilVO) cellModel.getBsFormat(curPosition,
				CellPostilDefPlugin.EXT_FMT_POSTIL);
		if (pvo == null) {
			return;
		}
		CellPosition cellPosition = CellPosition.getInstance(row, column);
		cellModel.setBsFormat(cellPosition, CellPostilDefPlugin.EXT_FMT_POSTIL, pvo);
	}

	/**
	 * add by ����� 2008-3-23 ȥ��Format�ı߿���Ϣ
	 * 
	 * @param CellsModel
	 *            cellModel:����ģ��,int row����, int column����, Format curFormat����ǰ��ʽ
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
	 * add by ����� 2008-3-23 ճ��ĳ���п�
	 * 
	 * @param CellsModel
	 *            cellModel:����ģ��,int row����, int column����, int columnWidth���ı����п�
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
		// �����ͷģ��
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
	 * add by ����� 2008-3-23 ֻճ�����ָ�ʽ���ָ�ţ��ٷֺţ�С��λ�������ҷ��ţ����Ĵ�Сд��������ʾ
	 * 
	 * @param CellsModel
	 *            cellModel:����ģ��,int row����, int column����, Format curFormat����ǰ��ʽ
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
	 * add by ����� 2008-3-23 �û��¼� ����ճ���¼�.���ݲ���Ҫ����չ���ݣ��繫ʽ�����������ݡ�
	 * 
	 * @param Object
	 *            content����ǰ����,String pasteType��ճ������,Cell[][] c����Ԫ��ά����
	 * 
	 */
	protected boolean userEventAction(Object content, String pasteType,
			Cell[][] c, UFOTable table) {
		if (table == null || content == null) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1000496"));// �������������Ϊ��
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
//				&& isBestrowArea(c, getAnchorCell())) {// �Ƿ񸲸Ǵ�ճ������
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
	 * add by ����� 2008-3-23 ��ø���ʱ������λ��
	 * 
	 * @param Cell[][]
	 *            aryCells������ʱ������Ԫ����
	 * 
	 */
	private AreaPosition[] getCopyCellPosition() {
		AreaPosition area = getEditParameter().getCopyArea();
		AreaPosition[] aryArea = { area };
		return aryArea;
	}

	/**
	 * �жϱ���״̬
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
		Clipboard m_Clipboard = getTable().getClipboard();// ��ü��а�
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
	 * ���ָ������ĵ�Ԫ�е�����
	 * 
	 * @param clipType
	 * @param dispatch
	 * @param areas����Ҫ���������
	 */
	private void clear(int clipType, boolean dispatch, AreaPosition[] areas) {
		UFOTable table = getTable();
		if (areas == null || areas.length == 0)
			return;
		// ��װ�����¼�.
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.CLEAR, null,
				areas);
		if (dispatch && table.checkEvent(event)) {
			table.fireEvent(event);
		}
		// ɾ�����ݺ͸�ʽ ��鲻�ָ�ʽ�����ݡ�Ĭ��ʱȫ��
		if(!isFormatState(getReport())){
			CellsModel model = table.getCellsModel();
			model.clearArea(clipType, areas);
		}
	}

	/**
	 * ���ָ������ĵ�Ԫ�е�����
	 * 
	 * @param
	 * clipType���ο�UFOTable��CELL_ALL��CELL_CONTENT��CELL_FORMAT��areas:��Ҫ���������
	 */
	private void clear(int clipType, AreaPosition[] areas) {
		clear(clipType, clipType == UFOTable.CELL_ALL
				|| clipType == UFOTable.CELL_CONTENT, areas);
	}

	/** �õ�ѡ������ĵ�Ԫ��Ϣ */
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
	 * ������
	 * 
	 * @param area
	 * @return Cell[][]
	 * @throws TableDataModelException
	 */
	static Cell[][] getCells(AreaPosition area,CellsModel cellsModel) throws TableDataModelException {
		if(area == null || cellsModel == null)
			throw new IllegalArgumentException();

		// �����ж������Ƿ���Ч
		CellPosition cellStart = area.getStart();
		CellPosition cellEnd = area.getEnd();
		// �ж����䷶Χ�Ƿ����
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
				cell = cellsModel.getCellIfNullNew(cp.getRow(), cp.getColumn());// ���Ƶ�Ԫ��ʽʱ����Ҫ֪��λ����Ϣ��
				areaDatas[i][j] = cell;
				if (cell != null && cell instanceof CombinedCell) { // ����Ƿ��Ǻϲ���Ԫ
					CombinedCell cc = (CombinedCell) cell;
					hs.addAll(cellsModel.getSeperateCellPos(cc.getArea()));
				}

			}
		}
		return areaDatas;
	}

	/**
	 * add by ����� 2008-3-23 ճ��ֵ
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
		if(value instanceof Cloneable){//�������󣬱���ͼ��
			cellsModel.setCellValueByAuth(row, column, DeepCopyUtilities.getDeepCopy(value));
		} else {
			if (cell.getValue() != null) {
				// ճ����Ԫ������ʱ�����������ݲ�����toString����(���ݽϴ�ʱ�����ص��ǿ�ѧ�����ַ���)
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
	 * �ṩ��cellsModel�����ã���ʱ����̬ʱҲ��Ҫ����ʽ����
	 * @param cellsModel
	 */
	public void setDataModel(CellsModel cellsModel){
		this.cellsModel = cellsModel;
	}
	
	public CellPosition getAnchorCell() {
		if(this.anchorCell == null){//�˴���Ҫȥ���ê��
			return getReport().getCellsModel().getSelectModel().getSelectedArea().getStart();
		}
        return this.anchorCell;
	}

	/**
	 * ����ê�㣺��ʱ����̬ʱҲ��Ҫ����ʽ����
	 * @param anchorCell
	 */
	public void setAnchorCell(CellPosition anchorCell){
		this.anchorCell = anchorCell;
	}
	
	/**
	 * �Ƿ�ת��
	 * @return
	 */
	public boolean isTransfer() {
		return b_isTransfer;
	}
}
