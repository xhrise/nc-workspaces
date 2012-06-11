package com.ufsoft.report.fmtplugin.formula;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JTextField;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.sysplugin.insertdelete.DeleteInsertDialog;
import com.ufsoft.script.AreaFmlExecutor;
import com.ufsoft.script.AreaFormulaUtil;
import com.ufsoft.script.base.AreaFormulaModel;
import com.ufsoft.script.base.FormulaVO;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.IArea;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.event.HeaderEvent;
import com.ufsoft.table.event.HeaderModelListener;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.re.CellRenderAndEditor;

/**
 * ����ʽ������ṩ��HR���á�����v56�¿�ܼ�����
 * 
 * @author zhaopq
 * @created at 2009-4-15,����09:59:50
 * @since v56
 */
public class AreaFormulaDefPlugin extends AbstractPlugin implements
		HeaderModelListener, UserActionListner {

	public static final String GROUP = "miufo1000909";//��Ԫ��ʽ

	private boolean popMenuVisible;

	private static boolean formulaRendererVisible;

	static {
		CellRenderAndEditor.getInstance().registExtSheetRenderer(
				new AreaFormulaRenderer());
		CellRenderAndEditor.getInstance().registExtSheetEditor(
				new AreaFormulaDefEditor(new JTextField()));
	}

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] { new AreaFormulaDefAction(), // ������ʽ
				new AreaFormulaToolBarAction() // ��ʽ������
		};
	}

	public boolean isPopMenuVisible() {
		return popMenuVisible;
	}

	public void setPopMenuVisible(boolean popMenuVisible) {
		this.popMenuVisible = popMenuVisible;
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void startup() {
	}

	public int getPriority() {
		return 0;
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

	private CellsModel getCellsModel() {
		return getReportDesigner().getCellsModel();
	}

	private ReportDesigner getReportDesigner() {
		if (getMainboard().getCurrentView() instanceof ReportDesigner) {
			return (ReportDesigner) getMainboard().getCurrentView();
		}
		return null;
	}

	public AreaFmlExecutor getFmlExecutor() {
		AreaFormulaModel fromulaModel = AreaFormulaModel
				.getInstance(getCellsModel());
		return fromulaModel.getAreaFmlExecutor();
	}

	public void headerPropertyChanged(PropertyChangeEvent e) {
	}

	public String isSupport(int source, EventObject e) {
		return null;
	}

	public void userActionPerformed(UserUIEvent e) {
		switch (e.getEventType()) {
		case UserUIEvent.PASTE:
			processPasteEvent(e);
			break;
		case UserUIEvent.CLEAR:
			processClearEvent(e);
			break;
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
			// fmlExecutor = null;
			// getFmlExecutor();
			break;
		default:break;
		}
	}

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
		int iPasteType = parameter.getClipType();
		pasteFormulas(areaSrc, iPasteType, true);
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
	private void pasteFormulas(IArea areaSrc, int iPasteType,boolean isInstantFml) {
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

	public static boolean isFormulaRendererVisible() {
		return formulaRendererVisible;
	}

	public static void setFormulaRendererVisible(boolean visible) {
		formulaRendererVisible = visible;
	}

}
