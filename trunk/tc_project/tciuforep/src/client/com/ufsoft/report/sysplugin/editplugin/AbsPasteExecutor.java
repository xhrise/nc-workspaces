package com.ufsoft.report.sysplugin.editplugin;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.exception.MessageException;
import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.Viewer;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.sysplugin.cellpostil.CellPostilDefPlugin;
import com.ufsoft.report.sysplugin.edit.EditPasteExt;
import com.ufsoft.report.sysplugin.postil.PostilVO;
import com.ufsoft.report.undo.CellUndo;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellSelection;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.IArea;
import com.ufsoft.table.TableDataModelException;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.header.HeaderModel;
import com.ufsoft.table.re.BorderPlayRender;

public abstract class AbsPasteExecutor {

	/** �Ƿ�ת�� */
	private boolean b_isTransfer = false;
	
    private Mainboard mainBoard = null;
    
    private CellsModel cellsModel = null;
    
    private EditParameter editParam = null;
	
	public AbsPasteExecutor(Mainboard mainBoard,boolean b_isTransfer){
		if (mainBoard == null) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1004052"));
		}
		this.mainBoard = mainBoard;
		this.b_isTransfer = b_isTransfer;
	}
	
	/**
	 * add by ����� 2008-3-23 ճ��ִ�з���
	 */
	public void choosePaste() {
		
		CellPosition target = getAnchorCell();
		if (target == null)
			return;
		EditParameter parameter = getEditParameter();
		if(parameter == null){// ���ⲿ�������excel�и��ƹ��������ݡ���֪ͨ���ճ���¼���
			pasteFromExcel(target);
			return;
		}

		if(!parameter.isCanPaste()){
			return;
		}
		
		Cell[][] arryCells = parameter.getCopyCells();
				
		arryCells = areaExtend(arryCells, parameter.getEditType());// �������չ
		
		arryCells = isTransfer() ? transferPosition(arryCells) : arryCells;
		
		executePaste(arryCells, getTransferable());
		 
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
	protected abstract boolean fireUserEvent(Object content,
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

		if (!fireUserEvent(content, getPasteType(), aryNewCells)) {
			return;
		}
		
		try {
			//���Ч�ʣ��ȹص��¼�
			cellsModel.setEnableEvent(false);
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
				getEditParameter().setCanPaste(false);
			}
		} catch (Exception e) {
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
	private void pasteFromExcel(CellPosition target) {
		UFOTable table = getTable();
		Transferable content = getTransferable();

		String[][] result = getAppClipCell(content);
		if (result != null) {
			for (int i = 0; i < result.length; i++) {
				String[] row = result[i];
				if (row != null) {
					for (int j = 0; j < row.length; j++) {
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
		if(!isFormatState()){
			iClipType = EditParameter.CELL_CONTENT;
		}
		return iClipType;
	}

	private boolean isFormatState(){
		IContext context = mainBoard.getContext();
		if(context == null){
			throw new IllegalArgumentException();
		}
		int state = new Integer("" + context.getAttribute(ReportContextKey.OPERATION_STATE));
		return state == ReportContextKey.OPERATION_FORMAT ? true : false;
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
	protected void pastePostil(int row, int column,
			CellPosition curPosition) {
		if (curPosition == null) {
			return;
		}
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1004055"));
		}
		CellsModel cellModel = getDataModel();
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
	protected void pasteNoBorder(int row, int column,
			Cell cell) {
		if (cell == null) {
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
		getDataModel().setCellFormatByAuth(row, column, format);
	}

	/**
	 * add by ����� 2008-3-23 ճ��ĳ���п�
	 * 
	 * @param CellsModel
	 *            cellModel:����ģ��,int row����, int column����, int columnWidth���ı����п�
	 * 
	 */
	protected void pasteColumnWidth(int column,
			int curColumn) {
		if (column < 0) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1004055"));
		}
		// �����ͷģ��
		HeaderModel m_ColumnHeaderModel = getDataModel().getColumnHeaderModel();
		if (m_ColumnHeaderModel == null) {
			return;
		}
		Header newHeader = m_ColumnHeaderModel.getHeader(column);
		Header curHeader = m_ColumnHeaderModel.getHeader(curColumn);
		if (newHeader == null || curHeader == null) {
			return;
		}

		m_ColumnHeaderModel.setSize(column, curHeader.getSize());
	}

	/**
	 * add by ����� 2008-3-23 ֻճ�����ָ�ʽ���ָ�ţ��ٷֺţ�С��λ�������ҷ��ţ����Ĵ�Сд��������ʾ
	 * 
	 * @param CellsModel
	 *            cellModel:����ģ��,int row����, int column����, Format curFormat����ǰ��ʽ
	 * 
	 */
	protected void pasteNumberFormat(int row, int column,
			Format curFormat, String pasteType) {
		if (curFormat == null || pasteType == null
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
			getDataModel().setCellFormatByAuth(row, column, curFormat);
		} else {
			getDataModel().setCellFormatByAuth(row, column, newFormat);
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
		parameter.setPasteType(getPasteType());
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.PASTE, content,
				parameter);
		if (!table.checkEvent(event)) {
			BorderPlayRender.stopPlay(table.getCells());
			return false;
		}

		table.fireEvent(event);
		
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
//		if(!isFormatState()){
//			CellsModel model = getDataModel();
//			model.clearArea(clipType, areas);
//		}
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
		AreaPosition area = getDataModel()
				.getSelectModel().getSelectedArea();
		if (area != null) {
			try {
				sources = getDataModel().getCells(area);
			} catch (TableDataModelException ex) {
				AppDebug.debug(ex);
			}
		}
		return sources;
	}

	/**
	 * add by ����� 2008-3-23 ճ��ֵ
	 * 
	 * @param CellsModel
	 *            cellsModel, UFOTable table, Cell cell, int row, int column
	 * 
	 */
	protected void pasteValue(Cell cell,
			int row, int column) {
		if (cell == null) {
			return;
		}
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException(MultiLang.getString("miufo1004055"));
		}
		
		CellsModel cellsModel = getDataModel();
		UFOTable table = getTable();
		
		//�������ݵļ��У���ֹ�ı���ǰ�����ݣ���cloneһ��
		Cell oldCell = cellsModel.getCell(row, column);
		cellsModel.setCell(row, column, (Cell)oldCell.clone());
		
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
		
		int editType = getEditType();
		if(editType == EditParameter.CUT){
			CellPosition oldCellPos = CellPosition.getInstance(cell.getRow(),cell.getCol());
			AreaPosition oldAreaPos = AreaPosition.getInstance(oldCellPos, oldCellPos);	
			
			EditParameter parameter = getEditParameter();
			AreaPosition[] pasteAreas = parameter.getPasteAreas();
			if(pasteAreas == null || pasteAreas.length == 0)
				return;
			if(!pasteAreas[0].contain(oldAreaPos)){
				cellsModel.clearArea(getClipType(), new IArea[]{oldCellPos});
			}
			
		}
	}
	
	private Transferable getTransferable(){
		Clipboard m_Clipboard = getTable().getClipboard();// ��ü��а�
		Transferable content = m_Clipboard.getContents(getTable());
		return content;
	}
	
	protected UFOTable getTable() {		
		return getEditor().getTable();
	}

	private ReportDesigner getEditor(){
		Viewer viewer = mainBoard.getCurrentView();
		if(!(viewer instanceof ReportDesigner))
			throw new MessageException(MessageException.TYPE_WARNING,"��ǰ��ͼ��֧��ճ��");
		return (ReportDesigner)viewer;
	}
	
	private CellPosition getAnchorCell() {
        return getDataModel().getSelectModel().getSelectedArea().getStart();
	}
	
	public CellsModel getDataModel() {
		if(cellsModel == null){
			cellsModel = getTable().getCellsModel();
		}
		return cellsModel;
	}

	/**
	 * �Ƿ�ת��
	 * @return
	 */
	protected boolean isTransfer() {
		return b_isTransfer;
	}
}
