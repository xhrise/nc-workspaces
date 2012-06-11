package com.ufsoft.report.fmtplugin.formula;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JTextField;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.insertdelete.DeleteInsertDialog;
import com.ufsoft.script.AreaFmlExecutor;
import com.ufsoft.script.AreaFormulaUtil;
import com.ufsoft.script.base.AreaFormulaModel;
import com.ufsoft.script.base.FormulaVO;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.IArea;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.event.HeaderEvent;
import com.ufsoft.table.event.HeaderModelListener;
import com.ufsoft.table.format.TableConstant;

/**
 * �򻯰�Ĺ�ʽ������ڿؼ���֧�ֲ��ֳ��ù�ʽ�Ķ���ͼ��㹦�ܡ� ֧�����ຯ����1����ѧ������2���ַ�������3����������
 * 
 * @author chxw 2008-4-16
 */
public class AreaFormulaPlugin extends AbstractPlugIn implements
		HeaderModelListener, UserActionListner {

//	private static DataFlavor FORMULAMODEL_FLAVOR = new DataFlavor(
//			AreaFormulaModel.class, "FormulaModel");

	/** ��ʽ��ع��ܷ�װ������ */
	private AreaFmlExecutor fmlExecutor = null;

	@Override
	public void startup() {
		getReport().getTable().getCells().registExtSheetRenderer(
				new FormulaRenderer(this));
		getReport().getTable().getCells().registExtSheetEditor(
				new FormulaDefEditor(this, new JTextField()));

		initAreaFmlExecutor();
		
		// ��ӹ�������ʽ�༭��
		getReport().getToolBarPane().add(new ToolBarFormulaComp(getReport()));
	}

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AreaFormulaDescriptor(this);
	}

	public void userActionPerformed(UserUIEvent e) {
		switch (e.getEventType()) {
		case UserUIEvent.PASTE:
			processPasteEvent(e);
			break;
		case UserUIEvent.CLEAR:
			processClearEvent(e);
			break;
//		case UserUIEvent.COPY:
//			CellSelection cellSelection = (CellSelection) e.getOldValue();
//			processCopyEvent(cellSelection);
//			break;
//		case UserUIEvent.CUT:
//			CellSelection cellsSelection = (CellSelection) e.getOldValue();
//			processCopyEvent(cellsSelection);
//			break;
		case UserUIEvent.COMBINECELL:
			AreaPosition area1 = (AreaPosition) e.getOldValue();
			processCombineCellEvent(area1);
			break;
		case UserUIEvent.UNCOMBINECELL:
			AreaPosition area2 = (AreaPosition) e.getOldValue();
			processUnCombineCellEvent(area2);
			break;
		case UserUIEvent.DELETECELL:
			processDeleteCellEvent(e);
			break;
		case UserUIEvent.INSERTCELL:
			processInsertCellEvent(e);
			break;
		case UserUIEvent.MODEL_CHANGED:
			fmlExecutor = null;
			getFmlExecutor();
			break;
		}

	}

	/**
	 * ����ճ���¼�
	 * 
	 * @param cellss
	 *            Ҫ�������к���˳Ѷ����
	 * @param content
	 */
	protected void processPasteEvent(UserUIEvent e) {
		if (e == null) {
			return;
		}
		Object object = e.getNewValue();
		EditParameter parameter = null;
		if (object instanceof EditParameter) {
			parameter = (EditParameter) object;
		}
		// ���Դ����
		IArea areaSrc = parameter.getCopyArea();
		Cell[][] cells = getCellsModel().getCells((AreaPosition) areaSrc);
		int iPasteType = parameter.getClipType();
		pasteFormulas(areaSrc, iPasteType, cells, true);
	}

	/**
	 * ��������¼�.
	 */
	private void processClearEvent(UserUIEvent e) {
		AreaPosition[] aimArea = (AreaPosition[]) e.getNewValue();
		if (aimArea == null || aimArea.length == 0) {
			return;
		}
		for (int i = 0; i < aimArea.length; i++) {
			AreaPosition area = aimArea[i];
			getFmlExecutor().clearFormula(area);
		}
	}

	/**
	 * ��formulamodel clone�󣬷ŵ����а��
	 * 
	 * @param cellSelection
	 */
//	private void processCopyEvent(CellSelection cellSelection) {
//		cellSelection.putData(FORMULAMODEL_FLAVOR, DeepCopyUtil
//				.getDeepCopy(this.getFmlExecutor().getFormulaModel()));
//	}

	/**
	 * ɾ�����׵�Ԫ��Ĺ�ʽ��
	 * 
	 * @param area
	 */
	private void processUnCombineCellEvent(AreaPosition area) {
		processCombineCellEvent(area);
	}

	/**
	 * ɾ�����׵�Ԫ��Ĺ�ʽ��
	 * 
	 * @param area
	 */
	private void processCombineCellEvent(AreaPosition area) {
		CellPosition startPos = area.getStart();
		for (int dRow = 0; dRow < area.getHeigth(); dRow++) {
			for (int dCol = 0; dCol < area.getWidth(); dCol++) {
				CellPosition each = (CellPosition) startPos.getMoveArea(dRow,
						dCol);
				if (dRow != 0 || dCol != 0) {// ���׵�Ԫ��
					getFmlExecutor().clearFormula(each);
				}
			}
		}

		// ����׵�Ԫ�����й�ʽ���������ϵ�Ԫ���¹�ʽ����
		FormulaVO fmlVO = getFmlExecutor().getFormulaModel().getDirectFml(
				startPos);
		if (fmlVO != null) {
			getFmlExecutor().updateFormulaAreaPos(startPos, area, fmlVO, true,
					true);
		}

	}

	private void processInsertCellEvent(UserUIEvent e) {
		int insertType = (Integer) e.getOldValue();
		AreaPosition aimArea = (AreaPosition) e.getNewValue();
		if (insertType == DeleteInsertDialog.CELL_MOVE_RIGHT) {// ��Ԫ������
			getFmlExecutor().getFormulaModel().insertOrDeleteCell(true, true,
					aimArea);
		} else {// ��Ԫ������
			getFmlExecutor().getFormulaModel().insertOrDeleteCell(true, false,
					aimArea);
		}
	}

	/**
	 * userActionPerformed�д���UserUIEvent.DELETECELL�¼���ʵ�ʴ����� ����ɾ����Ԫ��ʱ�Ե�Ԫ���Ϲ�ʽ��ɾ��
	 * 
	 * @param e
	 */
	private void processDeleteCellEvent(UserUIEvent e) {
		int delType = (Integer) e.getOldValue();
		AreaPosition aimArea = (AreaPosition) e.getNewValue();
		if (delType == DeleteInsertDialog.CELL_MOVE_LEFT) {// ��Ԫ������
			getFmlExecutor().getFormulaModel().insertOrDeleteCell(false, true,
					aimArea);
		} else {// ��Ԫ������
			getFmlExecutor().getFormulaModel().insertOrDeleteCell(false, false,
					aimArea);
		}
	}

	/**
	 * ��������ʽ��ճ��
	 * 
	 * @param IArea
	 *            areaSrc��ԭ����
	 * @param int
	 *            iPasteType��ճ���Ĳ�������
	 * @param Cell[][]
	 *            cells��ԭ���Ƶĵ�Ԫ����
	 * @param boolean
	 *            isInstantFml��true:��Ԫ��ʽ;false:���ܹ�ʽ
	 * @return
	 */
	private void pasteFormulas(IArea areaSrc, int iPasteType, Cell[][] cells,
			boolean isInstantFml) {
		if (areaSrc == null) {
			return;
		}
		// �õ���ǰѡ�б�ҳ�Ľ��㵥Ԫ��
		CellPosition target = getCellsModel().getSelectModel().getAnchorCell();
		Object[][] srcFormulas = getFmlExecutor().getFormulaModel()
				.getRelatedAllFml(areaSrc);// ��ù�ʽ�͵�Ԫλ�õ�����

		// ճ����Ԫ��ʽ
		if (srcFormulas == null) {
			return;
		}
		for (int i = 0, size = srcFormulas.length; i < size; i++) {
			if (srcFormulas[i] == null || srcFormulas[i].length < 2)
				continue;
			IArea srcAreaTemp = (IArea) srcFormulas[i][0];// �������
			FormulaVO fmlTemp = (FormulaVO) srcFormulas[i][1];// ��ù�ʽ
			IArea destAreaTemp = getDestArea(srcAreaTemp, areaSrc, target);
			String strFormula = AreaFormulaUtil.modifyFormula(srcAreaTemp,
					destAreaTemp,
					fmlTemp == null ? null : fmlTemp.getContent(),
					getCellsModel().getMaxRow(), getCellsModel().getMaxCol());

			StringBuffer showErrMessage = new StringBuffer();
			try {
				getFmlExecutor().addDbDefFormula(showErrMessage, destAreaTemp,
						strFormula, null, isInstantFml);
			} catch (ParseException e1) {
				AppDebug.debug(e1);
			}
			if (iPasteType == CellsModel.CELL_CONTENT)
				pasteCellType(srcAreaTemp, destAreaTemp);

		}
	}

	/**
	 * ճ��Դ��Ԫ���͡�Ҫ��Ŀ����Դ�����Сһ��
	 * 
	 * @param srcArea
	 * @param destArea
	 */
	private void pasteCellType(IArea srcArea, IArea destArea) {
		if (srcArea == null || destArea == null)
			return;
		ArrayList<CellPosition> listCell = getCellsModel().getSeperateCellPos(
				destArea);
		if (listCell == null)
			return;
		CellPosition cell = null;
		for (int i = 0, size = listCell.size(); i < size; i++) {
			cell = listCell.get(i);
			if (cell == null)
				continue;
			IufoFormat format = (IufoFormat) getCellsModel()
					.getCellFormat(cell);
			if (format == null
					|| format.getCellType() == TableConstant.CELLTYPE_SAMPLE) {
				// ���ճ��Դ�ĵ�Ԫ������
				IufoFormat srcFormat = getSrcCellFormat(srcArea, destArea, cell);
				if (srcFormat != null
						&& srcFormat.getCellType() != TableConstant.CELLTYPE_SAMPLE)
					getCellsModel().setCellProperty(cell,
							PropertyType.DataType, srcFormat.getCellType());
			}
		}
	}

	private IufoFormat getSrcCellFormat(IArea srcArea, IArea destArea,
			CellPosition destCell) {
		CellPosition srcStartCell = srcArea.getStart();
		int iWidth = destCell.getRow() - destArea.getStart().getRow();
		int iHeight = destCell.getColumn() - destArea.getStart().getColumn();
		if (iWidth < 0 || iHeight < 0)
			return null;

		CellPosition srcCell = CellPosition.getInstance(srcStartCell.getRow()
				+ iWidth, srcStartCell.getColumn() + iHeight);
		return (IufoFormat) getCellsModel().getCellFormat(srcCell);
	}

	/**
	 * ���ճ��Ŀ������
	 * 
	 * @param areaCurrent
	 * @param srcArea
	 * @param destCell
	 * @return
	 */
	private IArea getDestArea(IArea srcAreaCurrent, IArea srcArea,
			CellPosition destCell) {
		int srcRowStart = srcArea.getStart().getRow();
		int srcColStart = srcArea.getStart().getColumn();

		int rowStart = destCell.getRow();
		int colStart = destCell.getColumn();

		int iOffRow = srcAreaCurrent.getStart().getRow() - srcRowStart;
		int iOffCol = srcAreaCurrent.getStart().getColumn() - srcColStart;

		IArea destAreaTemp = AreaPosition.getInstance(rowStart + iOffRow,
				colStart + iOffCol, srcAreaCurrent.getWidth(), srcAreaCurrent
						.getHeigth());

		return destAreaTemp;

	}

	public String isSupport(int source, EventObject e)
			throws ForbidedOprException {
		return null;
	}

	public void headerCountChanged(HeaderEvent e) {
		// 0 ��ɾ���� 1 ��ɾ���� 2�в��룬 3 �в���
		int oper;
		if (e.isRow()) {
			if (e.isHeaderAdd()) {
				oper = 2;
			} else {
				oper = 0;
			}
		} else {
			if (e.isHeaderAdd()) {
				oper = 3;
			} else {
				oper = 1;
			}
		}

		// ��ʽ�������������ʽ����
		getFmlExecutor().getFormulaModel().headerCountChanged(e);
		getFmlExecutor().updateFormulas(e.getStartIndex(), e.getCount(), oper);
	}

	public void headerPropertyChanged(PropertyChangeEvent e) {
		// TODO Auto-generated method stub

	}

	public int getPriority() {
		return 0;
	}

	public AreaFmlExecutor getFmlExecutor() {
		AreaFormulaModel fromulaModel = AreaFormulaModel.getInstance(getCellsModel());
		AreaFmlExecutor fmlExecutor = fromulaModel.getAreaFmlExecutor();
		if(fmlExecutor == null){// @edit by wangyga at 2009-6-17,����11:39:47 ���HR��ɾ������ʱ��������⣬��һ��report������������cellsModel
			fmlExecutor = new AreaFmlExecutor(getCellsModel());
			fromulaModel.setAreaFmlExecutor(fmlExecutor);
		}
		return fmlExecutor;
	}

	private void initAreaFmlExecutor(){
		AreaFormulaModel fromulaModel = AreaFormulaModel.getInstance(getCellsModel());
		if(fmlExecutor == null){
			fmlExecutor = new AreaFmlExecutor(getCellsModel());
			fromulaModel.setAreaFmlExecutor(fmlExecutor);
		}		
	}
	
	/** ��ʽ̬�¹�ʽ��չ�����Ƿ���ʾ */
	private boolean m_bFmlRendererVisible = true;

	private boolean m_bFmlDefPopMenuVisible = true;

	public boolean isFmlRendererVisible() {
		return m_bFmlRendererVisible;
	}

	public void setFmlRendererVisible(boolean fmlRendererVisible) {
		m_bFmlRendererVisible = fmlRendererVisible;
	}

	public boolean isFmlDefPopMenuVisible() {
		return m_bFmlDefPopMenuVisible;
	}

	public void setFmlDefPopMenuVisible(boolean fmlDefPopMenuVisible) {
		m_bFmlDefPopMenuVisible = fmlDefPopMenuVisible;
	}

}
