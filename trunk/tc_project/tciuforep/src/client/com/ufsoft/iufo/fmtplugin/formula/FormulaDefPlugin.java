package com.ufsoft.iufo.fmtplugin.formula;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JTextField;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaDefPlugIn;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.measure.MeasureDefPlugIn;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
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
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.IArea;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.event.HeaderEvent;
import com.ufsoft.table.event.HeaderModelListener;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.re.CellRenderAndEditor;

/**
 * ��ʽ���,ͬʱ���򹤾������ToolBarFormulaComp()���
 * @author wupeng
 * 2004-8-27
 */
public class FormulaDefPlugin extends AbstractPlugIn implements
		HeaderModelListener, UserActionListner, SelectListener {

//	private static DataFlavor FORMULAMODEL_FLAVOR = new DataFlavor(
//			FormulaModel.class, "FormulaModel");
	/**��ʽ��ع��ܷ�װ����*/
	private UfoFmlExecutor fmlExecutor = null;
	/**ɾ����ʽ����ʱ�������������ѡ������ʽ�Ķ��� **/
	private AreaPosition delFormulaArea = null;
	
    // @edit by wangyga at 2009-3-3,����09:43:14
	static{
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new FormulaDefRenderer(true));
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new FormulaDefRenderer(false));
		CellRenderAndEditor.getInstance().registExtSheetEditor(new FormulaDefEditor(new JTextField()));		
	}
	
	public void startup() {	
		getReport().getEventManager().addListener(this);
//		getReport().getTable().getCells().registExtSheetRenderer(
//				new FormulaDefRenderer(this, true));
//		getReport().getTable().getCells().registExtSheetRenderer(
//				new FormulaDefRenderer(this, false));
//		getReport().getTable().getCells().registExtSheetEditor(
//				new FormulaDefEditor(this, new JTextField()));

		//��ʽ��ʼ��
		//liuyy+
//		AppWorker aw = new AppWorker("��ʽ����ʼ��"){
//			protected Object construct() throws Exception {
//				getFormulaHandler();
//				return null;
//			}
//			
//		};
//		aw.start(5 * 1000);//�ӳ�5���ִ�С�
		
		//���غ�����������ʼ����ʽ��()
//		getFmlExecutor();
		initFmlExecutor();
		
		//��ӹ�����ֱ�ӹ�ʽ�༭��
		getReport().getToolBarPane().add(new ToolBarFormulaComp(getReport()));
//		CellSelection.registerFlavor(FORMULAMODEL_FLAVOR);
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
	 */
	public IPluginDescriptor createDescriptor() {
		return new FormulaDescriptor(this);
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
	 */
	public void userActionPerformed(UserUIEvent e) {
		//�����¼���
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
			fmlExecutor = null;
			//liuyy+, �л�ģ��Ч���Ż����رա�
			//getFormulaHandler();//֪ͨ�¼�ʱ��û��ʵ���л�ģ�͡�
			break;
		}
	}

	private void processInsertCellEvent(UserUIEvent e) {
		int insertType = (Integer) e.getOldValue();
		AreaPosition aimArea = (AreaPosition) e.getNewValue();
		if (insertType == DeleteInsertDialog.CELL_MOVE_RIGHT) {//��Ԫ������
			getFormulaModel().insertOrDeleteCell(true, true, aimArea);
		} else {//��Ԫ������
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
	 * userActionPerformed�д���UserUIEvent.DELETECELL�¼���ʵ�ʴ�����
	 * ����ɾ����Ԫ��ʱ�Ե�Ԫ���Ϲ�ʽ��ɾ��
	 * @param e
	 */
	private void processDeleteCellEvent(UserUIEvent e) {
		int delType = (Integer) e.getOldValue();
		AreaPosition aimArea = (AreaPosition) e.getNewValue();
		delFormulaArea = aimArea;
		if (delType == DeleteInsertDialog.CELL_MOVE_LEFT) {//��Ԫ������
			getFormulaModel().insertOrDeleteCell(false, true, aimArea);
		} else {//��Ԫ������
			getFormulaModel().insertOrDeleteCell(false, false, aimArea);
		}
		int operation = delType == DeleteInsertDialog.CELL_MOVE_LEFT ? 8 : 9;
		String[] dynAreaPKs = getDynAreaPKsAll();
		getFmlExecutor().updateFormulas(aimArea.getStart().getRow(),
				aimArea.getStart().getColumn(), operation, dynAreaPKs, true);
		getFmlExecutor().updateFormulas(aimArea.getStart().getRow(),
				aimArea.getStart().getColumn(), operation, dynAreaPKs, false);
	}

	/**
	 * ��formulamodel clone�󣬷ŵ����а��
	 * @param cellSelection
	 */
//	private void processCopyEvent(CellSelection cellSelection) {
//		cellSelection.putData(FORMULAMODEL_FLAVOR, DeepCopyUtil
//				.getDeepCopy(getFormulaModel()));
//	}

	/**
	 * @param area
	 * @param fillType
	 */
	private void processFillEvent(AreaPosition area, int fillTo) {
		int startRow = area.getStart().getRow();
		int startCol = area.getStart().getColumn();
		int endRow = area.getEnd().getRow();
		int endCol = area.getEnd().getColumn();
		CellPosition firPos;//��������е�һ��������׵�Ԫ��
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

	/**
	 * ��Ԫ��ʽ��䵽����ʽ��ʱ��ɾ������ʽ��
	 * ����ʽ�������ʱ����������䡣
	 * @param area �������
	 * @param firPos Դ��ʽ��Ԫ
	 * @param dRow 
	 * @param j
	 * @return int �ƶ�����
	 */
	private int fillExcuteOnePiece(AreaPosition area, CellPosition firPos,
			int fillTo, boolean bCellFormula) {
		//���������Ҫ��������ʽ���
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

		//�ж�Դ�����Ƿ�Ϊ���й�ʽ
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

		//������һ����ʽ��������һ����ʽ�����׵�Ԫ���С��в�ֵ
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
				//modify by ljhua 2007-3-19 ����������ʽ�������
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
	 * ɾ�����׵�Ԫ��Ĺ�ʽ��
	 * @param area
	 */
	private void processUnCombineCellEvent(AreaPosition area) {
		processCombineCellEvent(area);
	}

	/**
	 * ɾ�����׵�Ԫ��Ĺ�ʽ��
	 * @param area
	 */
	private void processCombineCellEvent(AreaPosition area) {
		CellPosition startPos = area.getStart();
		for (int dRow = 0; dRow < area.getHeigth(); dRow++) {
			for (int dCol = 0; dCol < area.getWidth(); dCol++) {
				CellPosition each = (CellPosition) startPos.getMoveArea(dRow,
						dCol);
				if (dRow != 0 || dCol != 0) {//���׵�Ԫ��
					getFmlExecutor().clearFormula(each, true);
				}
			}
		}

		//����׵�Ԫ�����й�ʽ���������ϵ�Ԫ���¹�ʽ����
		FormulaVO fmlVO = getFormulaModel().getDirectFml(startPos, true);
		if (fmlVO != null) {
			//�ж�Դ�����Ƿ�Ϊ���й�ʽ
			boolean bPublic = false;
			if (isCreateUnit() == true) {
				FormulaVO fmlPublic = getFormulaModel().getPublicDirectFml(
						startPos);
				if (fmlPublic != null)
					bPublic = true;
			}
			getFmlExecutor().updateFormulaAreaPos(startPos, area.getStart(), fmlVO,
					true, bPublic);
		}

	}

	/**
	 * ��������¼�.
	 */
	private void processClearEvent(UserUIEvent e) {
		AreaPosition[] aimArea = (AreaPosition[]) e.getNewValue();
		//����ǹ�ʽɾ����������ִ��ѡ�е�Ԫ�����������������ڵ�Ԫ�����������
		if (delFormulaArea != null && aimArea != null && aimArea.length > 0
				&& delFormulaArea == aimArea[0]) {
			delFormulaArea = null;
			return;
		}

		//�õ���ǰѡ�еĵ�Ԫ��modify by 2008-4-23����� �����в�������ճ��ʱ�������ʱѡ���������ݣ������Ǵ�selectModel�л��
		//        AreaPosition selArea = getCellsModel().getSelectModel().getSelectedArea();
		//        if(selArea == null){
		//            return;
		//        }
		//        getFmlExecutor().clearFormula(selArea,true);
		//        getFmlExecutor().clearFormula(selArea,false);

		if (aimArea == null || aimArea.length == 0) {
			return;
		}
		for (int i = 0; i < aimArea.length; i++) {
			AreaPosition area = aimArea[i];
			getFmlExecutor().clearFormula(area, true);
			getFmlExecutor().clearFormula(area, false);
		}
	}

	private boolean isCreateUnit() {
		return ((UfoContextVO) getReport().getContextVo()).isCreateUnit();
	}

	/**
	 * ���ճ��Ŀ������
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
		
		if(iStartRow < 0 || iStartCol <0)
			return null;
		IArea destAreaTemp = AreaPosition.getInstance(iStartRow,
				iStartCol, srcAreaCurrent.getWidth(), srcAreaCurrent
						.getHeigth());
		

		return destAreaTemp;

	}

	/**
	 * ����ճ���¼�.modify by 2008-4-29 ����� ����ʽת��
	 * @param cellss Ҫ�������к���˳Ѷ����
	 * @param content 
	 */
	private void processPasteEvent(UserUIEvent e) {
		if (e == null) {
			return;
		}
		
		Object object = e.getNewValue();
		EditParameter parameter = null;
		if(object instanceof EditParameter){
			parameter = (EditParameter)object;
		}
		boolean b_isTransfer = parameter.isTransfer();
		//���Դ����       	
		IArea areaSrc = parameter.getCopyArea();
		FormulaModel oldFormulaModel = getFormulaModel();
		Cell[][] cells = getCellsModel().getCells((AreaPosition)areaSrc);
		int iPasteType = parameter.getClipType();
        //ճ����Ԫ��ʽ
		pasteFormulas(oldFormulaModel,areaSrc,iPasteType,b_isTransfer,cells,true);		   
	}

	/**
	 * add by ����� 2008-4-29 ��ʽ��ճ��(��Ԫ��ʽ�ͻ��ܹ�ʽ),�Լ�ճ��ʱ��ת�õĴ���
	 * 
	 * @param AreaFormulaModel oldFormulaModel:ճ��֮ǰ�Ĺ�ʽģ��
	 * @param IArea areaSrc��ԭ����
	 * @param int iPasteType��ճ���Ĳ�������
	 * @param boolean b_isTransfer���Ƿ�ת��
	 * @param Cell[][] cells��ԭ���Ƶĵ�Ԫ����
	 * @param boolean isInstantFml��true:��Ԫ��ʽ;false:���ܹ�ʽ
	 * @return
	 */
	private void pasteFormulas(FormulaModel oldFormulaModel, IArea areaSrc,
			int iPasteType, boolean b_isTransfer, Cell[][] cells,
			boolean isInstantFml) {
		if (oldFormulaModel == null || areaSrc == null) {
			return;
		}
		// �õ���ǰѡ�б�ҳ�Ľ��㵥Ԫ��
		CellPosition target = getCellsModel().getSelectModel()
				.getSelectedArea().getStart();
		Object[][] srcFormulas = oldFormulaModel.getRelatedAllFmls(areaSrc,
				isInstantFml);// ��ù�ʽ�͵�Ԫλ�õ�����

		if (b_isTransfer) {
			areaSrc = transferPosition(cells);// ת��
		}
		// ճ����Ԫ��ʽ
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
			IArea srcAreaTemp = (IArea) srcFormulas[i][0];// �������
			FormulaVO publicTemp = (FormulaVO) srcFormulas[i][1];// ��ù�ʽ
			FormulaVO personTemp = (FormulaVO) srcFormulas[i][2];
			FormulaVO totalTemp = (FormulaVO) srcFormulas[i][3];
			if (publicTemp == null && personTemp == null && totalTemp == null)
				continue;
			IArea destAreaTemp = null;
			// add by ����� 2008-4-21�ж��Ƿ���Ҫת��
			if (b_isTransfer) {
				IArea area = transferPosition(srcAreaTemp);
				destAreaTemp = getDestArea(area, areaSrc, target);// ���Ŀ�굥Ԫ
			} else {
				destAreaTemp = getDestArea(srcAreaTemp, areaSrc, target);
			}
			if (destAreaTemp == null)
				return;

			String strDynPK = getDynPKByFmlArea(srcAreaTemp);
			StringBuffer showErrMessage = new StringBuffer();
			try {

				String strPubUserDefContent = fmlExecutor
						.getUserDefFmlContent(publicTemp, srcAreaTemp, strDynPK);
				String strPerUserDefContent = fmlExecutor
						.getUserDefFmlContent(personTemp, srcAreaTemp, strDynPK);
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
					fmlExecutor.addDbDefFormula(showErrMessage,
							destAreaTemp, objUserLet.toString(calcEnv), null, true,
							true, false);
				}

				if (strPerson != null && strPerson.length() != 0) {
					IParsed objUserLet = fmlExecutor.parseUserDefFormula(
							destAreaTemp, strPerson);
					fmlExecutor.addDbDefFormula(showErrMessage,
							destAreaTemp, objUserLet.toString(calcEnv), null, true,
							false, false);
				}

				if (strTotal != null && strTotal.length() != 0) {
					IParsed objUserLet = fmlExecutor.parseUserDefFormula(
							destAreaTemp, strTotal);
					fmlExecutor.addDbDefFormula(showErrMessage,
							destAreaTemp, objUserLet.toString(calcEnv), null, false,
							false);
				}

			} catch (Throwable e1) {
				AppDebug.debug(e1);
				if(i == 0){
					UfoPublic.sendWarningMessage(e1.getMessage(), getReport());
				}				
				// throw new
				// IllegalArgumentException(showErrMessage.toString());
			}

		}
	}

	/*
	 * ��ǰ���������������͵ĵ�Ԫ��ʽ����ܹ�ʽʱ,ʵ��ȫѡ��Ч��.
	 * 
	 * @see com.ufsoft.table.SelectListener#selectedChanged(com.ufsoft.table.SelectEvent)
	 */
	public void selectedChanged(SelectEvent e) {
		SelectModel sm = getCellsModel().getSelectModel();
		//ʵ��ǰһ�汾�ļ�������ȫѡ,���ܲ���ѡ�Ĺ���.
		if (e.getProperty() == SelectEvent.ANCHOR_CHANGED
				&& sm.getSelectedArea() != null) {
			//�ж��������Ƿ����������͵Ĺ�ʽ.ֻ�������ѡ�������.
			AreaPosition selArea = sm.getSelectedArea();
			CellPosition[] poss = (CellPosition[]) getCellsModel()
					.getSeperateCellPos(selArea).toArray(new CellPosition[0]);
			AreaPosition newSelArea = selArea;
			for (int i = 0; i < poss.length; i++) {
				//�����ǽ���仯�¼�����,һ�����Ӧ����ֻ��һ��ѭ��.
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
			if (!newSelArea.equals(selArea)) {//��ֹ����ĸ��¶���������������������ѭ����
				sm.setSelectedArea(newSelArea);
			}
		}

	}

	/** add by ����� 2008-4-21
	 * ʵ�ֵ�Ԫ��ת��
	 * @param Cell[][] cell ԭ��Ԫ��ά����
	 * @return Cell[][] ת�ú�Ķ�ά����
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
		Cell[][] cells = getReport().getCellsModel().getCells(areaPosition);
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

	private UfoContextVO getContextVO() {
		return (UfoContextVO) getReport().getContextVo();
	}

	private FormulaModel getFormulaModel() {
		FormulaModel formulaModel = CellsModelOperator
				.getFormulaModel(getCellsModel());
		return formulaModel;
	}

	private UfoFmlExecutor getFmlExecutor() {
		return getFormulaModel().getUfoFmlExecutor();
	}
	
	private void initFmlExecutor(){
		if(getCellsModel() == null)
			return;
		FormulaModel formulaMoel =  FormulaModel.getInstance(getCellsModel());
		//���غ�����������ʼ����ʽ��
		if(fmlExecutor == null){
			fmlExecutor = UfoFmlExecutor.getInstance(getContextVO(), getCellsModel(), true);
			formulaMoel.setUfoFmlExecutor(fmlExecutor);
		}		
	}

	/**
	 * �޸Ĺ�ʽ������
	 */
	public void headerCountChanged(HeaderEvent e) {
		//0 ��ɾ���� 1 ��ɾ���� 2�в��룬 3 �в���
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

		//��ʽ�������������ʽ���ġ��˲���Ӧ���ڹ�ʽ���λ��ģ���޸���ϡ�
		//TODO �˴���Ҫ�Ż���dynPKsӦ���ǲ���Ӱ��Ķ�̬��
		String[] dynPKs = getDynAreaPKsAll();
		getFmlExecutor().updateFormulas(e.getStartIndex(), e.getCount(),
				oper, dynPKs, true);
		getFmlExecutor().updateFormulas(e.getStartIndex(), e.getCount(),
				oper, dynPKs, false);

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

	public String isSupport(int source, EventObject e)
			throws ForbidedOprException {
		return null;
	}

	public int getPriority() {
		return HeaderModelListener.MIN_PRIORITY;
	}

//	private MeasureDefPlugIn getMeasurePI() {
//		return (MeasureDefPlugIn) getReport().getPluginManager().getPlugin(
//				MeasureDefPlugIn.class.getName());
//	}

	/**
	 * ճ��Դ��Ԫ���͡�Ҫ��Ŀ����Դ�����Сһ��
	 * @param srcArea
	 * @param destArea
	 */
//	private void pasteCellType(IArea srcArea, IArea destArea) {
//		if (srcArea == null || destArea == null)
//			return;
//		ArrayList<CellPosition> listCell = getCellsModel().getSeperateCellPos(
//				destArea);
//		if (listCell == null)
//			return;
//		CellPosition cell = null;
//		for (int i = 0, size = listCell.size(); i < size; i++) {
//			cell = listCell.get(i);
//			if (cell == null)
//				continue;
//			IufoFormat format = (IufoFormat) getCellsModel()
//					.getCellFormat(cell);
//			if (format == null
//					|| format.getCellType() == TableConstant.CELLTYPE_SAMPLE
//					&& (getMeasurePI() == null || getMeasurePI()
//							.getMeasureModel().getMeasureVOByPos(cell) == null)) {
//				//���ճ��Դ�ĵ�Ԫ������
//				IufoFormat srcFormat = getSrcCellFormat(srcArea, destArea, cell);
//				if (srcFormat != null
//						&& srcFormat.getCellType() != TableConstant.CELLTYPE_SAMPLE)
//					getCellsModel().setCellProperty(cell,
//							PropertyType.DataType, srcFormat.getCellType());
//			}
//		}
//	}

//	private IufoFormat getSrcCellFormat(IArea srcArea, IArea destArea,
//			CellPosition destCell) {
//		CellPosition srcStartCell = srcArea.getStart();
//		int iWidth = destCell.getRow() - destArea.getStart().getRow();
//		int iHeight = destCell.getColumn() - destArea.getStart().getColumn();
//		if (iWidth < 0 || iHeight < 0)
//			return null;
//
//		CellPosition srcCell = CellPosition.getInstance(srcStartCell.getRow()
//				+ iWidth, srcStartCell.getColumn() + iHeight);
//		return (IufoFormat) getCellsModel().getCellFormat(srcCell);
//	}

	private String getDynPKByFmlArea(IArea fmlArea) {
		DynAreaCell[] dynCellsTemp =getDynAreaPI()==null?null: getDynAreaPI().getDynAreaModel()
				.getDynAreaCellByArea(fmlArea);
		String strDynPK = null;
		if (dynCellsTemp != null && dynCellsTemp.length > 0)
			strDynPK = dynCellsTemp[0].getDynAreaVO().getDynamicAreaPK();
		return strDynPK;
	}
	
	private DynAreaDefPlugIn getDynAreaPI() {
		return (DynAreaDefPlugIn) getReport().getPluginManager().getPlugin(
				DynAreaDefPlugIn.class.getName());
	}
	
	//	private Clipboard _clipboard = null;
	//	private Clipboard getClipboard(){
	//		if(_clipboard == null){		
	//			SecurityManager security = System.getSecurityManager();	
	//			if (security != null) {
	//				security.checkSystemClipboardAccess();
	//			}
	//			_clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();	
	//		}
	//		return _clipboard;
	//	}
}
