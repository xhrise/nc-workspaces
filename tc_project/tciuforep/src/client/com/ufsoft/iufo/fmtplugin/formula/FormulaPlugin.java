package com.ufsoft.iufo.fmtplugin.formula;

import java.beans.PropertyChangeEvent;
import java.util.EventObject;

import javax.swing.JTextField;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.sysplugin.fill.FillCmd;
import com.ufsoft.report.sysplugin.insertdelete.DeleteInsertDialog;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.script.AreaFormulaUtil;
import com.ufsoft.script.base.CommonExprCalcEnv;
import com.ufsoft.script.base.IParsed;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.IArea;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.event.CellsModelSelectedListener;
import com.ufsoft.table.event.HeaderEvent;
import com.ufsoft.table.event.HeaderModelListener;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.re.CellRenderAndEditor;

/**
 * 格式态的公式插件,取代V55之前的<code>FormulaDefPlugin</code>
 * 
 * @author zhaopq
 * @created at 2009-3-19,下午03:05:08
 * @since v5.6
 */
public class FormulaPlugin extends AbstractPlugin implements
		HeaderModelListener, UserActionListner, CellsModelSelectedListener {

	public static final String GROUP = "formula";

	/** 删除公式区域时，避免清除触发选中区域公式的动作 * */
	private AreaPosition delFormulaArea = null;

	static {
		CellRenderAndEditor.getInstance().registExtSheetRenderer(
				new FormulaDefRenderer(true));
		CellRenderAndEditor.getInstance().registExtSheetRenderer(
				new FormulaDefRenderer(false));
		CellRenderAndEditor.getInstance().registExtSheetEditor(
				new FormulaDefEditor(new JTextField()));
	}

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] { new FormulaEditAction(), // 单元公式
				new FormulaTransAction(),// 公式套用
				new AuditFormulaAction(),// 审核公式
				new BatchFormulaAction(), // 批量公式
				new FormulaDefToolBarAction(), // 公式工具条
				new FormulaRendererAction() // 公式扩展属性是否渲染
		};
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void startup() {
		// wangyga+
		getEventManager().addListener(this);
	}

	public int getPriority() {
		return HeaderModelListener.MIN_PRIORITY;
	}

	public void headerCountChanged(HeaderEvent e) {
		// 0 行删除， 1 列删除； 2行插入， 3 列插入
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

		getFormulaModel().headerCountChanged(e);

		// 公式链处理及相对区域公式更改。此操作应该在公式左侧位置模型修改完毕。
		// TODO 此处需要优化。dynPKs应该是操作影响的动态区
		String[] dynPKs = getDynAreaPKsAll();
		getFmlExecutor().updateFormulas(e.getStartIndex(), e.getCount(), oper,
				dynPKs, true);
		getFmlExecutor().updateFormulas(e.getStartIndex(), e.getCount(), oper,
				dynPKs, false);

	}

	private UfoFmlExecutor getFmlExecutor() {
		return getFormulaModel().getUfoFmlExecutor();
	}

	private FormulaModel getFormulaModel() {
		FormulaModel formulaModel = CellsModelOperator
				.getFormulaModel(getCellsModel());
		return formulaModel;
	}

	private ReportDesigner getReportDesigner() {
		if (getMainboard().getCurrentView() instanceof ReportDesigner) {
			return (ReportDesigner) getMainboard().getCurrentView();
		}
		return null;
	}

	private CellsModel getCellsModel() {
		return getReportDesigner().getCellsModel();
	}

	private String[] getDynAreaPKsAll() {
		DynAreaVO[] dynAreaVOs = DynAreaModel.getInstance(getCellsModel())
				.getDynAreaVOs();
		String[] dynPKs = new String[dynAreaVOs.length];
		for (int i = 0; i < dynAreaVOs.length; i++) {
			dynPKs[i] = dynAreaVOs[i].getDynamicAreaPK();
		}
		return dynPKs;
	}

	public void headerPropertyChanged(PropertyChangeEvent e) {

	}

	public String isSupport(int source, EventObject e) {
		return null;
	}

	public void userActionPerformed(UserUIEvent e) {
		// 处理事件。
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
		case UserUIEvent.FILL:
			AreaPosition area = (AreaPosition) e.getNewValue();
			int fillTo = ((Integer) e.getOldValue()).intValue();
			processFillEvent(area, fillTo);
			break;
		case UserUIEvent.DELETECELL:
			processDeleteCellEvent(e);
			break;
		case UserUIEvent.INSERTCELL:
			processInsertCellEvent(e);
			break;
		case UserUIEvent.MODEL_CHANGED:
			// liuyy+, 切换模型效率优化，关闭。
			// getFormulaHandler();//通知事件时还没有实际切换模型。
			break;
		}
	}

	private void processInsertCellEvent(UserUIEvent e) {
		int insertType = (Integer) e.getOldValue();
		AreaPosition aimArea = (AreaPosition) e.getNewValue();
		if (insertType == DeleteInsertDialog.CELL_MOVE_RIGHT) {// 单元格右移
			getFormulaModel().insertOrDeleteCell(true, true, aimArea);
		} else {// 单元格下移
			getFormulaModel().insertOrDeleteCell(true, false, aimArea);
		}
		int operation = insertType == DeleteInsertDialog.CELL_MOVE_RIGHT ? 6
				: 7;
		String[] dynAreaPKs = getDynAreaPKsAll();
		getFmlExecutor().updateFormulas(aimArea.getStart().getRow(),
				aimArea.getStart().getColumn(), operation, dynAreaPKs, true);
		getFmlExecutor().updateFormulas(aimArea.getStart().getRow(),
				aimArea.getStart().getColumn(), operation, dynAreaPKs, false);
	}

	/**
	 * userActionPerformed中处理UserUIEvent.DELETECELL事件的实际处理方法 处理删除单元格时对单元格上公式的删除
	 * 
	 * @param e
	 */
	private void processDeleteCellEvent(UserUIEvent e) {
		int delType = (Integer) e.getOldValue();
		AreaPosition aimArea = (AreaPosition) e.getNewValue();
		delFormulaArea = aimArea;
		if (delType == DeleteInsertDialog.CELL_MOVE_LEFT) {// 单元格左移
			getFormulaModel().insertOrDeleteCell(false, true, aimArea);
		} else {// 单元格上移
			getFormulaModel().insertOrDeleteCell(false, false, aimArea);
		}
		int operation = delType == DeleteInsertDialog.CELL_MOVE_LEFT ? 8 : 9;
		String[] dynAreaPKs = getDynAreaPKsAll();
		getFmlExecutor().updateFormulas(aimArea.getStart().getRow(),
				aimArea.getStart().getColumn(), operation, dynAreaPKs, true);
		getFmlExecutor().updateFormulas(aimArea.getStart().getRow(),
				aimArea.getStart().getColumn(), operation, dynAreaPKs, false);
	}

	private void processFillEvent(AreaPosition area, int fillTo) {
		int startRow = area.getStart().getRow();
		int startCol = area.getStart().getColumn();
		int endRow = area.getEnd().getRow();
		int endCol = area.getEnd().getColumn();
		CellPosition firPos;// 填充区域中的一条区域的首单元格。
		switch (fillTo) {
		case FillCmd.FillToUp:
			for (int i = 0; i < 2; i++) {
				int iStep = 0;
				for (int dCol = 0; dCol < area.getWidth(); dCol += iStep) {
					firPos = CellPosition.getInstance(endRow, startCol + dCol);
					iStep = fillExcuteOnePiece(area, firPos, FillCmd.FillToUp,
							i == 0 ? true : false);
					if (iStep < 1)
						iStep = 1;
				}
			}

			break;
		case FillCmd.FillToDown:

			for (int i = 0; i < 2; i++) {
				int iStep = 0;
				for (int dCol = 0; dCol < area.getWidth(); dCol += iStep) {
					firPos = CellPosition
							.getInstance(startRow, startCol + dCol);
					iStep = fillExcuteOnePiece(area, firPos,
							FillCmd.FillToDown, i == 0 ? true : false);
					if (iStep < 1)
						iStep = 1;
				}
			}

			break;
		case FillCmd.FillToLeft:

			for (int i = 0; i < 2; i++) {
				int iStep = 0;
				for (int dRow = 0; dRow < area.getHeigth(); dRow += iStep) {
					firPos = CellPosition.getInstance(startRow + dRow, endCol);
					iStep = fillExcuteOnePiece(area, firPos,
							FillCmd.FillToLeft, i == 0 ? true : false);
					if (iStep < 1)
						iStep = 1;
				}
			}

			break;
		case FillCmd.FillToRight:
			for (int i = 0; i < 2; i++) {
				int iStep = 0;
				for (int dRow = 0; dRow < area.getHeigth(); dRow += iStep) {
					firPos = CellPosition
							.getInstance(startRow + dRow, startCol);
					iStep = fillExcuteOnePiece(area, firPos,
							FillCmd.FillToRight, i == 0 ? true : false);
					if (iStep < 1)
						iStep = 1;
				}
			}
			break;
		}
	}

	private boolean isCreateUnit() {
		return IufoFormulalUtil.isCreateUnit(getReportDesigner().getContext());
	}

	/**
	 * 单元公式填充到区域公式里时，删除区域公式。 区域公式往外填充时，不进行填充。
	 * 
	 * @param area
	 *            填充区域
	 * @param firPos
	 *            源公式单元
	 * @param dRow
	 * @param j
	 * @return int 移动步长
	 */
	private int fillExcuteOnePiece(AreaPosition area, CellPosition firPos,
			int fillTo, boolean bCellFormula) {
		// 填充区域需要考虑区域公式情况
		Object[] objs = getFormulaModel().getRelatedFmlVO(firPos, bCellFormula);
		FormulaVO fmlVOGeneral = null;
		IArea fmlArea = null;
		if (objs != null && objs.length > 1) {
			fmlArea = (IArea) objs[0];
			fmlVOGeneral = (FormulaVO) objs[1];
		}
		if (fmlArea == null || fmlVOGeneral == null
				|| area.contain(fmlArea) == false)
			return 1;

		// 判断源区域是否为公有公式
		boolean bPublic = true;
		if (bCellFormula == true) {
			if (isCreateUnit() == true) {
				FormulaVO fmlPerson = getFormulaModel().getPersonalDirectFml(
						(IArea) objs[0]);
				if (fmlPerson != null)
					bPublic = false;
			} else
				bPublic = false;
		} else
			bPublic = false;

		int iRowStep = fmlArea.getWidth();
		int iColStep = fmlArea.getHeigth();

		// 计算下一个公式区域与上一个公式区域首单元的行、列差值
		int iRowDiff = 0;
		int iColDiff = 0;
		if (fillTo == FillCmd.FillToUp) {
			iRowDiff = -iColStep;
		} else if (fillTo == FillCmd.FillToDown) {
			iRowDiff = iColStep;

		} else if (fillTo == FillCmd.FillToLeft) {
			iColDiff = -iRowStep;
		} else if (fillTo == FillCmd.FillToRight) {
			iColDiff = iRowStep;
		}

		try {
			IArea areaTemp = fmlArea;
			StringBuffer showErrMessage = new StringBuffer();
			while (true) {
				areaTemp = AreaPosition.getInstance(areaTemp.getStart()
						.getRow()
						+ iRowDiff, areaTemp.getStart().getColumn() + iColDiff,
						iRowStep, iColStep);

				if (!area.contain(areaTemp)) {
					break;
				}
				// modify by ljhua 2007-3-19 解决相对区域公式填充问题
				String strFormula = AreaFormulaUtil.modifyFormula(fmlArea,
						areaTemp, fmlVOGeneral == null ? null : fmlVOGeneral
								.getFormulaContent(), getCellsModel()
								.getMaxRow(), getCellsModel().getMaxCol());

				getFmlExecutor().addDbDefFormula(showErrMessage, areaTemp,
						strFormula, null, bCellFormula, bPublic);
			}
		} catch (ParseException e) {
			AppDebug.debug(e);
		}
		int iStep = 1;
		if (fillTo == FillCmd.FillToUp || fillTo == FillCmd.FillToDown) {
			iStep = iRowStep;
		} else if (fillTo == FillCmd.FillToLeft || fillTo == FillCmd.FillToLeft)
			iStep = iColStep;

		return iStep;

	}

	/**
	 * 删除非首单元格的公式。
	 * 
	 * @param area
	 */
	private void processUnCombineCellEvent(AreaPosition area) {
		processCombineCellEvent(area);
	}

	/**
	 * 删除非首单元格的公式。
	 * 
	 * @param area
	 */
	private void processCombineCellEvent(AreaPosition area) {
		CellPosition startPos = area.getStart();
		for (int dRow = 0; dRow < area.getHeigth(); dRow++) {
			for (int dCol = 0; dCol < area.getWidth(); dCol++) {
				CellPosition each = (CellPosition) startPos.getMoveArea(dRow,
						dCol);
				if (dRow != 0 || dCol != 0) {// 非首单元格
					getFmlExecutor().clearFormula(each, true);
				}
			}
		}

		// 如果首单元格定义有公式，则根据组合单元更新公式定义
		FormulaVO fmlVO = getFormulaModel().getDirectFml(startPos, true);
		if (fmlVO != null) {
			// 判断源区域是否为公有公式
			boolean bPublic = false;
			if (isCreateUnit() == true) {
				FormulaVO fmlPublic = getFormulaModel().getPublicDirectFml(
						startPos);
				if (fmlPublic != null)
					bPublic = true;
			}
			getFmlExecutor().updateFormulaAreaPos(startPos, area.getStart(),
					fmlVO, true, bPublic);
		}

	}

	/**
	 * 处理清除事件.
	 */
	private void processClearEvent(UserUIEvent e) {
		AreaPosition[] aimArea = (AreaPosition[]) e.getNewValue();
		// 如果是公式删除动作，则不执行选中单元格清除，解决避免相邻单元格被清除的问题
		if (delFormulaArea != null && aimArea != null && aimArea.length > 0
				&& delFormulaArea == aimArea[0]) {
			delFormulaArea = null;
			return;
		}

		if (aimArea == null || aimArea.length == 0) {
			return;
		}
		for (int i = 0; i < aimArea.length; i++) {
			AreaPosition area = aimArea[i];
			if (getFmlExecutor() == null) {
				return;
			}
			getFmlExecutor().clearFormula(area, true);
			getFmlExecutor().clearFormula(area, false);
		}
	}

	/**
	 * 处理粘贴事件.modify by 2008-4-29 王宇光 处理公式转置
	 * 
	 * @param cellss
	 *            要求按照先行后列顺讯排列
	 * @param content
	 */
	private void processPasteEvent(UserUIEvent e) {
		if (e == null) {
			return;
		}

		Object object = e.getNewValue();
		EditParameter parameter = null;
		if (object instanceof EditParameter) {
			parameter = (EditParameter) object;
		}
		boolean b_isTransfer = parameter.isTransfer();
		// 获得源区域
		IArea areaSrc = parameter.getCopyArea();
		FormulaModel oldFormulaModel = getFormulaModel();
		Cell[][] cells = getCellsModel().getCells((AreaPosition) areaSrc);
		int iPasteType = parameter.getClipType();
		// 粘贴单元公式
		pasteFormulas(oldFormulaModel, areaSrc, iPasteType, b_isTransfer,
				cells, true);
	}

	/**
	 * add by 王宇光 2008-4-29 公式的粘贴(单元公式和汇总公式),以及粘贴时对转置的处理
	 * 
	 * @param AreaFormulaModel
	 *            oldFormulaModel:粘贴之前的公式模型
	 * @param IArea
	 *            areaSrc：原区域
	 * @param int
	 *            iPasteType：粘贴的操作类型
	 * @param boolean
	 *            b_isTransfer：是否转置
	 * @param Cell[][]
	 *            cells：原复制的单元数组
	 * @param boolean
	 *            isInstantFml：true:单元公式;false:汇总公式
	 * @return
	 */
	private void pasteFormulas(FormulaModel oldFormulaModel, IArea areaSrc,
			int iPasteType, boolean b_isTransfer, Cell[][] cells,
			boolean isInstantFml) {
		if (oldFormulaModel == null || areaSrc == null) {
			return;
		}
		// 得到当前选中表页的焦点单元。
		CellPosition target = getCellsModel().getSelectModel()
				.getSelectedArea().getStart();
		Object[][] srcFormulas = oldFormulaModel.getRelatedAllFmls(areaSrc,
				isInstantFml);// 获得公式和单元位置的数组

		if (b_isTransfer) {
			areaSrc = transferPosition(cells);// 转置
		}
		// 粘贴单元公式
		if (srcFormulas == null) {
			return;
		}
		int iMaxRow = getCellsModel().getMaxRow();
		int iMaxCol = getCellsModel().getMaxCol();
		UfoFmlExecutor fmlExecutor = getFmlExecutor();
		CommonExprCalcEnv calcEnv = fmlExecutor.getExecutorEnv();

		for (int i = 0, size = srcFormulas.length; i < size; i++) {
			if (srcFormulas[i] == null || srcFormulas[i].length < 3)
				continue;
			IArea srcAreaTemp = (IArea) srcFormulas[i][0];// 获得区域
			FormulaVO publicTemp = (FormulaVO) srcFormulas[i][1];// 获得公式
			FormulaVO personTemp = (FormulaVO) srcFormulas[i][2];
			FormulaVO totalTemp = (FormulaVO) srcFormulas[i][3];
			if (publicTemp == null && personTemp == null && totalTemp == null)
				continue;
			IArea destAreaTemp = null;
			// add by 王宇光 2008-4-21判断是否需要转置
			if (b_isTransfer) {
				IArea area = transferPosition(srcAreaTemp);
				destAreaTemp = getDestArea(area, areaSrc, target);// 获得目标单元
			} else {
				destAreaTemp = getDestArea(srcAreaTemp, areaSrc, target);
			}
			if (destAreaTemp == null)
				return;

			String strDynPK = getDynPKByFmlArea(srcAreaTemp);
			StringBuffer showErrMessage = new StringBuffer();
			try {

				String strPubUserDefContent = fmlExecutor.getUserDefFmlContent(
						publicTemp, srcAreaTemp, strDynPK);
				String strPerUserDefContent = fmlExecutor.getUserDefFmlContent(
						personTemp, srcAreaTemp, strDynPK);
				String strTotalUserDefContent = fmlExecutor
						.getUserDefFmlContent(totalTemp, srcAreaTemp, strDynPK);

				String strpublic = AreaFormulaUtil.modifyFormula(srcAreaTemp,
						destAreaTemp, strPubUserDefContent, iMaxRow, iMaxCol);

				String strPerson = AreaFormulaUtil.modifyFormula(srcAreaTemp,
						destAreaTemp, strPerUserDefContent, iMaxRow, iMaxCol);

				String strTotal = AreaFormulaUtil.modifyFormula(srcAreaTemp,
						destAreaTemp, strTotalUserDefContent, iMaxRow, iMaxCol);

				fmlExecutor.clearFormula(destAreaTemp, true);
				fmlExecutor.clearFormula(destAreaTemp, false);
				if (strpublic != null && strpublic.length() != 0) {
					IParsed objUserLet = fmlExecutor.parseUserDefFormula(
							destAreaTemp, strpublic);
					fmlExecutor.addDbDefFormula(showErrMessage, destAreaTemp,
							objUserLet.toString(calcEnv), null, true, true,
							false);
				}

				if (strPerson != null && strPerson.length() != 0) {
					IParsed objUserLet = fmlExecutor.parseUserDefFormula(
							destAreaTemp, strPerson);
					fmlExecutor.addDbDefFormula(showErrMessage, destAreaTemp,
							objUserLet.toString(calcEnv), null, true, false,
							false);
				}

				if (strTotal != null && strTotal.length() != 0) {
					IParsed objUserLet = fmlExecutor.parseUserDefFormula(
							destAreaTemp, strTotal);
					fmlExecutor.addDbDefFormula(showErrMessage, destAreaTemp,
							objUserLet.toString(calcEnv), null, false, false);
				}

			} catch (Throwable e1) {
				AppDebug.debug(e1);
				if (i == 0) {
					UfoPublic.sendWarningMessage(e1.getMessage(),
							getReportDesigner().getCellsPane());
				}
			}

		}
	}

	private String getDynPKByFmlArea(IArea fmlArea) {
		DynAreaCell[] dynCellsTemp = DynAreaModel.getInstance(
				getReportDesigner().getCellsModel()).getDynAreaCellByArea(
				fmlArea);
		String strDynPK = null;
		if (dynCellsTemp != null && dynCellsTemp.length > 0)
			strDynPK = dynCellsTemp[0].getDynAreaVO().getDynamicAreaPK();
		return strDynPK;
	}

	/**
	 * 获得粘贴目标区域
	 * 
	 * @param areaCurrent
	 * @param srcArea
	 * @param destCell
	 * @return
	 */
	private IArea getDestArea(IArea srcAreaCurrent, IArea srcArea,
			CellPosition destCell) {

		int iOffRow = destCell.getRow() - srcArea.getStart().getRow();
		int iOffCol = destCell.getColumn() - srcArea.getStart().getColumn();

		int iStartRow = srcAreaCurrent.getStart().getRow() + iOffRow;
		int iStartCol = srcAreaCurrent.getStart().getColumn() + iOffCol;

		if (iStartRow < 0 || iStartCol < 0)
			return null;
		IArea destAreaTemp = AreaPosition.getInstance(iStartRow, iStartCol,
				srcAreaCurrent.getWidth(), srcAreaCurrent.getHeigth());

		return destAreaTemp;

	}

	/**
	 * add by 王宇光 2008-4-21 实现单元的转置
	 * 
	 * @param Cell[][]
	 *            cell 原单元二维数组
	 * @return Cell[][] 转置后的二维数组
	 */
	private IArea transferPosition(Cell[][] cell) {
		if (cell == null || cell.length == 0 || cell[0].length == 0) {
			return null;
		}
		Cell[][] arryNewCells = new Cell[cell[0].length][cell.length];
		for (int i = 0; i < cell.length; i++) {
			Cell[] aryCell = cell[i];
			for (int j = 0; j < aryCell.length; j++) {
				Cell curCell = aryCell[j];
				Cell c = (Cell) curCell.clone();
				c.setRow(curCell.getCol());
				c.setCol(curCell.getRow());
				arryNewCells[j][i] = c;
			}
		}
		IArea newArea = AreaPosition.getInstance(arryNewCells[0][0].getRow(),
				arryNewCells[0][0].getCol(), arryNewCells[0].length,
				arryNewCells.length);
		return newArea;
	}

	private IArea transferPosition(IArea area) {
		if (area == null) {
			return null;
		}
		AreaPosition areaPosition = AreaPosition.getInstance(area.getStart(),
				area.getEnd());
		Cell[][] cells = getReportDesigner().getCellsModel().getCells(
				areaPosition);
		if (cells == null || cells.length == 0 || cells[0].length == 0) {
			return null;
		}
		Cell[][] arryNewCells = new Cell[cells[0].length][cells.length];
		for (int i = 0; i < cells.length; i++) {
			Cell[] aryCell = cells[i];
			for (int j = 0; j < aryCell.length; j++) {
				Cell curCell = aryCell[j];
				Cell c = (Cell) curCell.clone();
				c.setRow(curCell.getCol());
				c.setCol(curCell.getRow());
				arryNewCells[j][i] = c;
			}
		}
		IArea newArea = AreaPosition.getInstance(arryNewCells[0][0].getRow(),
				arryNewCells[0][0].getCol(), arryNewCells[0].length,
				arryNewCells.length);
		return newArea;
	}

	/*
	 * 当前区域中有区域类型的单元公式或汇总公式时,实现全选的效果.
	 * 
	 */
	public void selectedChanged(SelectEvent e) {

	}
	/**
	 * 当前区域中有区域类型的单元公式或汇总公式时,实现全选的效果.
	 * 
	 */
	public void anchorChanged(CellsModel model, CellPosition oldAnchor,
			CellPosition newAnchor) {
		SelectModel sm = getCellsModel().getSelectModel();
		// 判断区域中是否有区域类型的公式.只考虑最后选择的区域.
		AreaPosition selArea = sm.getSelectedArea();
		if (selArea != null) {
			CellPosition[] poss = (CellPosition[]) getCellsModel()
					.getSeperateCellPos(selArea).toArray(new CellPosition[0]);
			AreaPosition newSelArea = selArea;
			for (int i = 0; i < poss.length; i++) {
				// 由于是焦点变化事件激发,一般情况应该是只有一次循环.
				IArea area1 = getFormulaModel()
						.getRelatedFmlArea(poss[i], true);
				IArea area2 = getFormulaModel().getRelatedFmlArea(poss[i],
						false);
				if (area1 != null) {
					newSelArea = newSelArea.getInstanceUnionWith(area1);
				}
				if (area2 != null) {
					newSelArea = newSelArea.getInstanceUnionWith(area2);
				}
			}
			if (!newSelArea.equals(selArea)) {// 防止本身的更新动作又引发操作，引起死循环。
				sm.setSelectedArea(newSelArea);
			}
		}

	}

	
	public void selectedChanged(CellsModel cellsModel,
			AreaPosition[] changedArea) {

		// 实现前一版本的既能区域全选,又能部分选的功能.
		// if (e.getProperty() == SelectEvent.ANCHOR_CHANGED
		// && sm.getSelectedArea() != null) {

		// }

	}

}
