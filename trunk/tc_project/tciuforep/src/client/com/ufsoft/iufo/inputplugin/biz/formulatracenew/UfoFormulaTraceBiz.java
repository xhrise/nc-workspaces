package com.ufsoft.iufo.inputplugin.biz.formulatracenew;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import nc.ui.sm.login.NCAppletStub;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.iufo.measure.MeasureVO;

import com.ufida.dataset.Context;
import com.ufida.dataset.DataSet;
import com.ufida.dataset.metadata.Field;
import com.ufida.dataset.tracedata.TraceDataParam;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaHandler;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaVO;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaTraceBiz;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.reporttool.temp.KeyDataGroup;
import com.ufsoft.iuforeport.tableinput.FormulaTraceUtil;
import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedData;
import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaTraceValueItem;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.util.UfoPublic1;
import com.ufsoft.script.base.ICalcEnv;
import com.ufsoft.script.base.UfoArray;
import com.ufsoft.script.base.UfoEElement;
import com.ufsoft.script.base.UfoVal;
import com.ufsoft.script.datachannel.IDynAreaDataParam;
import com.ufsoft.script.datachannel.IUFODynAreaDataParam;
import com.ufsoft.script.datachannel.IUFOTableData;
import com.ufsoft.script.exception.CmdException;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.script.expression.UfoCmdLet;
import com.ufsoft.script.expression.UfoExpr;
import com.ufsoft.script.expression.UfoFullArea;
import com.ufsoft.script.extfunc.DataSetFunc;
import com.ufsoft.script.extfunc.DataSetFuncCalcUtil;
import com.ufsoft.script.extfunc.MeasFunc;
import com.ufsoft.script.extfunc.MeasFuncDriver;
import com.ufsoft.script.extfunc.MeasOperand;
import com.ufsoft.script.extfunc.MeasPercentFunc;
import com.ufsoft.script.extfunc.MultiMeasOperand;
import com.ufsoft.script.function.UfoFunc;
import com.ufsoft.script.spreadsheet.ReportDynCalcEnv;
import com.ufsoft.script.spreadsheet.UfoCalcEnv;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.header.Header;

/**
 * �ͻ��˵Ĺ�ʽ׷��ҵ��ӿ�ʵ����
 * 
 * @author liulp
 * 
 */
public class UfoFormulaTraceBiz implements IFormulaTraceBiz {
	private static IFormulaTraceBiz s_oFormulaTraceBiz = new UfoFormulaTraceBiz();

	private UfoFormulaTraceBiz() {
	}

	public static IFormulaTraceBiz getInstance() {
		return s_oFormulaTraceBiz;
	}

	/**
	 * ���ָ����Ԫ���Ƿ����й�ʽ
	 * 
	 * @param cellsModel
	 * @param area
	 * @return boolean
	 */
	public boolean existFormula(CellsModel cellsModel, CellPosition cell) {
		return isExistFormula(cellsModel, cell);

	}

	public FormulaParsedData parseFormula(Context contextVO, CellsModel cellsModel, CellPosition cell) {
		// ȡ����ǰѡ��������Ĺ�ʽ����
		if (!isExistFormula(cellsModel, cell)) {
			return null;
		}

		UfoFmlExecutor ufoFmlExecutor = UfoFormulaTraceBizHelper.getFormulaHandler(contextVO,
				cellsModel);
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		FormulaModel formulaModel = dynAreaModel.getFormulaModel();

		// ����ù�ʽ��δ���������ȶԹ�ʽ���н���
		IArea selArea = cellsModel.getArea(cell);
		IArea fmtFmlArea = DynAreaCell.getFormatArea(selArea, cellsModel);
		fmtFmlArea = formulaModel.getRelatedFmlArea(fmtFmlArea, true);
		FormulaVO formulaVO = formulaModel.getDirectFml(fmtFmlArea, true);

		try {
			if (formulaVO.getLet() == null) {
				formulaVO.setLet(ufoFmlExecutor.parseExpr(formulaVO.getFormulaContent()));
			}

		} catch (ParseException e1) {
			AppDebug.debug(e1);
		}

		// �����ʽ�޷����������ٽ��м���
		// (�繫ʽ���������繫ʽHBMSELECT('���01��->�����ʽ�','�ϲ���������','��',-1)�ȹ�ʽ)
		fmtFmlArea = DynAreaCell.getRealArea(fmtFmlArea, cellsModel);
		if (formulaVO.getLet() == null) {
			return setNotParsedFormulaData(cell, fmtFmlArea, formulaVO);
		} else {
			// �����HR���������ٽ��н�����׷��
			if (UfoFormulaTraceBizHelper.isExtHRFunc(formulaVO.getLet(), ufoFmlExecutor.getCalcEnv())) {
				return null;
			}
			return setParsedFormulaData(cell, fmtFmlArea, cellsModel, ufoFmlExecutor, formulaVO);
		}

	}

	/**
	 * ���㹫ʽ���λ��
	 * 
	 * @param cell
	 * @param realFmlArea
	 * @return CellPosition
	 */
	private CellPosition getRelaFmlArea(CellPosition cell, IArea realFmlArea) {
		int nRow = cell.getRow() - realFmlArea.getStart().getRow();
		int nCol = cell.getColumn() - realFmlArea.getStart().getColumn();
		return CellPosition.getInstance(nRow, nCol);
	}

	/**
	 * ���ع�ʽ��������(�����ڿͻ��˽��н����Ĺ�ʽ����MSUMA()��MSELECTA()��)
	 * 
	 * @param cell
	 * @param formulaVO
	 * @return FormulaParsedData
	 */
	private FormulaParsedData setNotParsedFormulaData(CellPosition cell, IArea realFmlArea, FormulaVO formulaVO) {
		FormulaParsedData formulaParsedData = new FormulaParsedData();
		formulaParsedData.setFormulaDisContent(formulaVO.getFormulaContent());
		formulaParsedData.setTracePos(cell);

		FormulaParsedDataItem[] formulaParsedDataItems = new FormulaParsedDataItem[1];
		formulaParsedData.setFormulaParsedItems(formulaParsedDataItems);
		FormulaParsedDataItem formulaParsedDataItem = new FormulaParsedDataItem();
		formulaParsedDataItems[0] = formulaParsedDataItem;
		formulaParsedDataItem.setDisContent(formulaVO.getFormulaContent());
		formulaParsedDataItem.setNeedToCal(false);
		formulaParsedDataItem.setCanTrace(false);
		formulaParsedDataItem.setTraceSelf(false);
		formulaParsedDataItem.setTracedPos(null);
		formulaParsedDataItem.setFormulaValue("");
		formulaParsedDataItem.setTraceMultiValues(true);
		formulaParsedDataItem.setAreaFunc(!realFmlArea.isCell());
		return formulaParsedData;

	}

	/**
	 * ���ع�ʽ��������(���ڿͻ��˽��н����Ĺ�ʽ��������ȡ�����ָ�깫ʽ)
	 * 
	 * @param cell
	 * @param cellsModel
	 * @param ufoFmlExecutor
	 * @param formulaVO
	 * @return FormulaParsedData
	 */
	private FormulaParsedData setParsedFormulaData(CellPosition cell, IArea fmtFmlArea, CellsModel cellsModel,
			UfoFmlExecutor ufoFmlExecutor, FormulaVO formulaVO) {
		// ʵ�ʵĹ�ʽ����(���ڶ�̬���϶���Ĺ�ʽΪ��ʽ�涯̬����չ�������
		IArea realFmlExtArea = fmtFmlArea;
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		ReportDynCalcEnv dynCalcEnv = ufoFmlExecutor.getCalcEnv();

		// �����ʽ�ڶ�̬���У������ö�̬�����㻷��
		int unitAreaNum = -1, stepRow = -1, stepCol = -1;
		boolean isInDynArea = dynAreaModel.isInDynArea(cell);
		DynAreaCell dynAreaCell = null;
		if (isInDynArea) {
			dynAreaCell = dynAreaModel.getDynAreaCellByPos(cell);
			stepRow = dynAreaCell.getDynAreaVO().isRowDirection() ? dynAreaCell.getDynAreaVO().getOriArea().getHeigth()
					: 0;
			stepCol = dynAreaCell.getDynAreaVO().isRowDirection() ? 0 : dynAreaCell.getDynAreaVO().getOriArea()
					.getWidth();
			unitAreaNum = dynAreaCell.getOwnerUnitAreaNum(cell);
			realFmlExtArea = realFmlExtArea.getMoveArea(stepRow * unitAreaNum, stepCol * unitAreaNum);
			// ���÷�����㻷������
			setDynAreaCalcEnv(ufoFmlExecutor, dynAreaCell, stepRow, stepCol, unitAreaNum);
		} else {
			dynCalcEnv.setDynAreaInfo(null, null);
			dynCalcEnv.setDynAllKeyDatas(null);
			dynCalcEnv.setDynField2Values(null);
		}

		// ������ʽ�������ݶ���Ϊ��ʽ׷��׼������
		FormulaParsedData formulaParsedData = new FormulaParsedData();
		formulaParsedData.setFormulaDisContent(formulaVO.getLet().toUserDefString(ufoFmlExecutor.getCalcEnv()));
		formulaParsedData.setTracePos(cell);

		// ����ÿ��ϸ�ڱ��ʽ��������ʽ��Ϊ��ʽ׷��׼��ϸ������
		List allExpr = new ArrayList();
		if (formulaVO.getLet() instanceof UfoCmdLet) {
			((UfoCmdLet) formulaVO.getLet()).getAllExprsAndNoMeasCord(allExpr);
		} else {
			formulaVO.getLet().getAllExprs(allExpr);
		}
		List<UfoExpr> listAllExpr = FormulaHandler.getCalaElemFromExpr(allExpr, ufoFmlExecutor.getCalcEnv());
		
		FormulaParsedDataItem[] formulaParsedDataItems = new FormulaParsedDataItem[listAllExpr.size()];
		formulaParsedData.setFormulaParsedItems(formulaParsedDataItems);
		for (int index = 0; index < listAllExpr.size(); index++) {
			UfoExpr expr = listAllExpr.get(index);
			FormulaParsedDataItem formulaParsedDataItem = new FormulaParsedDataItem();
			formulaParsedDataItems[index] = formulaParsedDataItem;
			formulaParsedDataItem.setTracedExpr(expr);

			boolean isIFFFunc = false;
			boolean isDataSetFunc = false;
			boolean isDataSetOut = false;//���ݼ������������������ü��������
			boolean hasAvgField = false;
			boolean isHasNCFuncInIFFFunc = false;
			boolean isInEnvRepAndNonRefRep = true;
			boolean isHasDataSetFunc = false;
			String strUserDis = expr.toUserDefString(ufoFmlExecutor.getCalcEnv());
			// ����ҵ����(�����ʻ�HR��)����IFF�����в��������ʺ�������������
			boolean isNCFunc = UfoFormulaTraceBizHelper.isExtNCFunc(expr, ufoFmlExecutor.getCalcEnv());

			try {
				// ��������ʽ�����ù�ʽ��Լ���λ��
				if (!realFmlExtArea.isCell()) {
					CellPosition relaFmlPos = getRelaFmlArea(cell, realFmlExtArea);
					formulaParsedDataItem.setRelaCell(relaFmlPos);
					formulaParsedDataItem.setAbsCell(cell);

					// �������к��м���
					int relaPosIndex = relaFmlPos.getRow() * fmtFmlArea.getWidth() + relaFmlPos.getColumn();
					formulaParsedDataItem.setRelaCellInAreaFml(relaPosIndex);
					formulaParsedDataItem.setAreaFunc(true);
					if (isInDynArea) {
						IUFOTableData dataChannel = (IUFOTableData) (ufoFmlExecutor.getCalcEnv().getDataChannel());
						IDynAreaDataParam dynAreaParam = dataChannel.getDynAreaCalcParam();
						if (dynAreaParam != null) {
							dynAreaParam.setUnitDataColIndex(new int[] { relaPosIndex });
						}
					}
				}
				// ����IFF����������Ƿ�������ʵ�ҵ����
				isIFFFunc = expr.isIffFuncExpr(ufoFmlExecutor.getCalcEnv());
				if (isIFFFunc) {
					if (UfoExpr.getIffFuncCalcExpr(expr, ufoFmlExecutor.getCalcEnv()) != null) {
						isHasNCFuncInIFFFunc = true;
					}
					isHasDataSetFunc = isHasDataSetFunc(listAllExpr);
				}
				
				isDataSetFunc = expr.isDataSetFuncExpr(ufoFmlExecutor.getCalcEnv());
				isInEnvRepAndNonRefRep = isInEnvRepAndNonRefRep(expr, ufoFmlExecutor.getCalcEnv());
				if (isNCFunc) {
					formulaParsedDataItem.setFormulaValue("");
					formulaParsedDataItem.setNCFunc(true);
					UfoFormulaTraceBizHelper.getTraceStrOfNCFunc(formulaParsedDataItem, cellsModel, ufoFmlExecutor, cell);
					formulaParsedDataItem.setTracedExpr(expr);
					// ��Ϊ����NCAppletStubΪΨһʵ�����ͻ��˵��ú���������ٵ��øýӿڻ��׳��쳣������ڴ���һ�����ʻ��������
					clearNCStubEnv();
				} else if (isDataSetFunc && realFmlExtArea.contain(cell)) {
					// �������ݼ�׷�ٲ���
					DataSetFunc func = getDataSetFunc(expr);
					Field[] flds = func.getDataSetFieldsVal();
					if (flds == null
							|| (formulaParsedDataItem.getRelaCell() != null && ((isInDynArea && flds.length <= formulaParsedDataItem
									.getRelaCellInAreaFml()) || (!isInDynArea
									&& flds.length <= formulaParsedDataItem.getRelaCell().getColumn())))) {// add by ll, ���ڳ��������ĵ�Ԫ���ü���
						formulaParsedDataItem.setFormulaValue("");
						isDataSetOut = true;
					} else {
						DataSetDefVO datasetDef = func.getDataSetVal();
						DataSet dataSet = datasetDef.getDataSetDef();
						TraceDataParam traceDataParam = new TraceDataParam(dataSet);
						formulaParsedDataItem.setTraceDataParam(traceDataParam);
//						@edit by ll at 2008-12-24 ����10:05:41, Ϊ��Ч�����ﲻ��������Ϊ�ڡ����㡱�͡����顱��ʱ���ȥִ��ȡ��
						setExtTraceParam(cell, realFmlExtArea, ufoFmlExecutor, expr, func, traceDataParam, cellsModel);
						hasAvgField = dataSet.getMetaData().hasAvgField();//�оۼ��ֶβ���׷��
//						if (traceDataParam.getValue() != null) {
//							formulaParsedDataItem.setFormulaValue(traceDataParam.getValue());
							formulaParsedDataItem.setFormulaValue("");
							expr.clearData();
//						}
					}
				} else if (!isInEnvRepAndNonRefRep || (isIFFFunc && isHasNCFuncInIFFFunc)) {
					formulaParsedDataItem.setFormulaValue("");
					formulaParsedDataItem.setNCFunc(false);
				} else {
					UfoVal[] ufoVals = expr.getValue(ufoFmlExecutor.getCalcEnv());
					formulaParsedDataItem.setNCFunc(false);	
					// @edit by wangyga at 2009-2-23,����03:38:38 MeasureQuery��������ʾ����ֵ
					if(!expr.isMeasureQueryFuncExpr(ufoFmlExecutor.getCalcEnv())){
						setFormulaValue(cellsModel, formulaParsedDataItem, cell, ufoVals);						
					}else{
						formulaParsedDataItem.setFormulaValue("");
					}					
					expr.clearData();
				}
			} catch (Exception e) {
				AppDebug.debug(e);
				formulaParsedDataItem.setFormulaValue("");
			}

			// ����ʽ����ת��Ϊʵ������Ϊ���湫ʽ׷�ٽ������׼������
			IArea[] traceCellPosList = getTracedPos(expr, ufoFmlExecutor.getCalcEnv(), cellsModel);
			traceCellPosList = getRealExprAreaByOriArea(ufoFmlExecutor, cellsModel, dynAreaModel, unitAreaNum, stepRow,
					stepCol, traceCellPosList);

			// ������ʽ׷�����λ�ã��������׷������
			if (formulaParsedDataItem.isAreaFunc()
					&& traceCellPosList.length > formulaParsedDataItem.getRelaCellInAreaFml()
					&& formulaParsedDataItem.getRelaCellInAreaFml() > -1) {
				int relaCell = formulaParsedDataItem.getRelaCellInAreaFml();
				traceCellPosList = new IArea[] { traceCellPosList[relaCell] };
			}

			// ���ù�ʽ׷������ϸ����Ϣ
			formulaParsedDataItem.setUnitDataNum(unitAreaNum);// index .
																// liuyy,
																// 2008-01-08.
			formulaParsedDataItem.setInDynArea(isInDynArea);
			formulaParsedDataItem.setDynAreaPK(dynAreaCell == null ? null : dynAreaCell.getDynAreaPK());
			formulaParsedDataItem.setDisContent(strUserDis);
			formulaParsedDataItem.setNeedToCal(isNeedToCal(formulaParsedDataItem, isInEnvRepAndNonRefRep, isIFFFunc,
					isHasNCFuncInIFFFunc || isHasDataSetFunc, isDataSetFunc && !isDataSetOut));
			formulaParsedDataItem.setCanTrace(canTrace(expr, ufoFmlExecutor.getCalcEnv(), isDataSetFunc && !isDataSetOut && !hasAvgField, isIFFFunc,
					isHasNCFuncInIFFFunc));
			// @edit by wangyga at 2009-10-16,����11:30:14 nc�������������Ǽ��㱾��
			formulaParsedDataItem.setTraceSelf(isTraceSelf(expr, ufoFmlExecutor.getCalcEnv(), isIFFFunc) && !isNCFunc);
			formulaParsedDataItem.setTracedPos(traceCellPosList);
			formulaParsedDataItem.setTraceMultiValues(isTraceMultiValues(expr, ufoFmlExecutor.getCalcEnv()));
		}
		clearNCStubEnv();
		clearDynAreaCalcEnv(ufoFmlExecutor);
		return formulaParsedData;

	}

	
	/**
	 * �������NCAppletStub������NCAppletStubΪΨһʵ�����ͻ��˵��ú���������ٵ��øýӿڻ��׳��쳣
	 */
	private void clearNCStubEnv() {
		NCAppletStub ncStub = NCAppletStub.getInstance();
		ncStub.setParameter("ACCOUNT_ID", "");
	}

	/**
	 * ���ָ����Ԫ���Ƿ���ڹ�ʽ ��ʽ�����ǵ�Ԫ��ʽ��Ҳ����������ʽ����Ҫ������ȷ��ѡ�����������ȷ�Ĺ�ʽ���� �������Ƿ��幫ʽ
	 * 
	 * @param cellsModel
	 * @param cell
	 * @return
	 */
	private boolean isExistFormula(CellsModel cellsModel, CellPosition selPos) {
		// ָ���ĵ�Ԫ�񷵻�CellPosition���ͣ����ѡ�������ϵ�Ԫ���򷵻���ȷ��ѡ������ ��
		IArea selArea = cellsModel.getArea(selPos);
		IArea fmtFmlArea = DynAreaCell.getFormatArea(selArea, cellsModel);
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		FormulaModel formulaModel = dynAreaModel.getFormulaModel();
		IArea realFmlArea = formulaModel.getRelatedFmlArea(fmtFmlArea, true);
		if (realFmlArea == null) {
			return false;
		}

		FormulaVO formulaVO = formulaModel.getDirectFml(realFmlArea, true);
		return (formulaVO != null) ? true : false;
	}

	//�ж����б��ʽ���Ƿ������ݼ�����
	private boolean isHasDataSetFunc(List<UfoExpr> listAllExpr){
		if(listAllExpr == null || listAllExpr.size() == 0){
			return false;
		}
		for(UfoExpr ex : listAllExpr){
			try {
				if(ex.isDataSetFuncExpr(null)){
					return true;
				}
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}
		return false;
	}
	
	/**
	 * ���ö�̬�����㻷������
	 * 
	 * @param ufoFmlExecutor
	 * @param dynAreaCell
	 * @param stepRow
	 * @param stepCol
	 * @param unitAreaNum
	 */
	static void setDynAreaCalcEnv(UfoFmlExecutor ufoFmlExecutor, DynAreaCell dynAreaCell, int stepRow, int stepCol,
			int unitAreaNum) {
		// ���÷�����㻷������
		String strDynPK = dynAreaCell.getDynAreaVO().getDynamicAreaPK();
		KeyDataGroup[] objKeydatas = ufoFmlExecutor.getDynKeyDataGroups(strDynPK);
		if (objKeydatas == null || objKeydatas.length < unitAreaNum + 1) {
			return;
		}
		ufoFmlExecutor.getCalcEnv().setDynAllKeyDatas(objKeydatas);
		ufoFmlExecutor.getCalcEnv().setDynAreaInfo(strDynPK, objKeydatas[unitAreaNum]);
		Hashtable<Integer, Hashtable<String, Object>> mapField2Values = ufoFmlExecutor.getDynAreaField2KeyValues(
				dynAreaCell, true);
		ufoFmlExecutor.getCalcEnv().setDynField2Values(mapField2Values);
		IUFOTableData dataChannel = (IUFOTableData) (ufoFmlExecutor.getCalcEnv().getDataChannel());
		dataChannel.setDynAreaCalcParam(new IUFODynAreaDataParam(stepRow, stepCol, unitAreaNum, null, dynAreaCell
				.getDirection(), strDynPK));
	}

	/**
	 * �����̬���ļ��㻷������
	 * 
	 * @param ufoFmlExecutor
	 */
	static void clearDynAreaCalcEnv(UfoFmlExecutor ufoFmlExecutor) {
		ufoFmlExecutor.getCalcEnv().setDynAreaInfo(null, null);
	}

	/**
	 * ����ʽ��������ת��Ϊʵ����ʾ����
	 * 
	 * @param ufoFmlExecutor
	 * @param cellsModel
	 * @param area
	 * @param dynAreaModel
	 * @param unitAreaNum
	 * @param stepRow
	 * @param stepCol
	 * @param traceCellPosList
	 */
	private IArea[] getRealExprAreaByOriArea(UfoFmlExecutor ufoFmlExecutor, CellsModel cellsModel,
			DynAreaModel dynAreaModel, int unitAreaNum, int stepRow, int stepCol, IArea[] traceCellPosList) {
		if (traceCellPosList == null || traceCellPosList.length == 0) {
			return new IArea[0];
		}

		IArea realArea[] = null;
		Vector<IArea> vctRealArea = new Vector<IArea>();
		IUFOTableData dataChannel = ((IUFOTableData) ufoFmlExecutor.getCalcEnv().getDataChannel());
		IDynAreaDataParam dynAreaDataParam = dataChannel.getDynAreaCalcParam();
		for (int i = 0; i < traceCellPosList.length; i++) {
			IArea area = traceCellPosList[i];
			DynAreaCell dynAreaCell = dynAreaModel.getDynAreaCellByFmtPos(area.getStart());
			if (dynAreaCell == null) {// ����������ڶ�̬��
				realArea = getRealAreaPos(null, cellsModel, area);
			} else if (dynAreaDataParam != null && // �����������������̬������ʱ��֧�ֵ����
					!dynAreaDataParam.getDynAreaPK().equals(dynAreaCell.getDynAreaPK())) {
				realArea = getRealAreaPos(null, cellsModel, area);
			} else {// ����������ڵ�ǰ��̬��
				realArea = getRealAreaPos(dynAreaDataParam, cellsModel, area);
			}
			vctRealArea.addAll(Arrays.asList(realArea));
		}

		return vctRealArea.toArray(new IArea[0]);
	}

	/**
	 * ���ݶ�̬���������������㹫ʽ����ʵ��λ��
	 * 
	 * @param dynAreaDataParam
	 * @param cellsModel
	 * @param oriExprArea
	 * @return
	 */
	private IArea[] getRealAreaPos(IDynAreaDataParam dynAreaDataParam, CellsModel cellsModel, IArea oriExprArea) {
		IArea realExprArea = oriExprArea;
		if (dynAreaDataParam == null) {
			// ��ʽ�����ڹ̶���,ͬʱ��ʽ����ڶ�̬������ֱ�Ӽ���ʵ��λ��
			IArea realArea = DynAreaCell.getRealArea(realExprArea, cellsModel);
			DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
			boolean isExprInDynArea = dynAreaModel.isInDynArea(realArea.getStart());
			if (!isExprInDynArea) {
				return new IArea[] { realArea };
			} else {// ��ʽ�����ڹ̶�����ͬʱ��ʽ�����ڶ�̬�����������չ����(��PTotal����)
				DynAreaCell dynAreaCell = dynAreaModel.getDynAreaCellByPos(realArea.getStart());
				return dynAreaCell.getUnitAreasByRealArea(realArea);
			}
		}

		// ��ʽ�����ڶ�̬����ͬʱ��������oriExprAreaҲ�ڶ�̬��������ݵ�ǰ����������ȷ�ļ���λ��
		boolean direction = dynAreaDataParam.getDirection() == Header.ROW;
		int dRow = direction ? (dynAreaDataParam.getStepRow() * dynAreaDataParam.getUnitDataRowIndex())
				: dynAreaDataParam.getStepRow();
		int dCol = direction ? dynAreaDataParam.getStepCol() : (dynAreaDataParam.getStepCol() * dynAreaDataParam
				.getUnitDataRowIndex());
		realExprArea = oriExprArea.getMoveArea(dRow, dCol);
		return new IArea[] { realExprArea };
	}

	/**
	 * �������ݼ����ʽ׷�ٲ���
	 * 
	 * @param anchorCell
	 * @param realFormulaArea
	 * @param ufoFmlExecutor
	 * @param expr
	 * @param func
	 * @param traceDataParam
	 * @param cellsModel
	 * @throws CmdException
	 */
	private void setExtTraceParam(CellPosition anchorCell, IArea realFormulaArea, UfoFmlExecutor ufoFmlExecutor,
			UfoExpr expr, DataSetFunc func, TraceDataParam traceDataParam, CellsModel cellsModel) throws CmdException {
		CellPosition startFmlArea = realFormulaArea.getStart();
		int traceField = anchorCell.getColumn() - startFmlArea.getColumn();
		int traceRow = anchorCell.getRow() - startFmlArea.getRow();
		IUFOTableData dataChannel = (IUFOTableData) (ufoFmlExecutor.getCalcEnv().getDataChannel());
		if (dataChannel.getDynAreaCalcParam() != null) {
			traceRow = dataChannel.getDynAreaCalcParam().getUnitDataRowIndex();
			traceField = anchorCell.getColumn() - startFmlArea.getColumn() + realFormulaArea.getWidth()
					* (anchorCell.getRow() - startFmlArea.getRow());
		}
		Field[] fields = func.getDataSetFieldsVal();
		Field field = null;
		if (traceField != -1 && traceField < fields.length) {
			field = fields[traceField];
			traceDataParam.setFieldName(field.getFldname());
		}
	
		DataSetDefVO dataSetDefVO = func.getDataSetVal();
		String[] fieldNames = dataSetDefVO.getDataSetDef().getMetaData().getFieldNames();
		int i = 0;
		for (String f : fieldNames) {
			if (field != null && f.equals(field.getFldname())) {
				traceField = i;
			}
			i++;
		}
		traceDataParam.addParam(DataSetFuncCalcUtil.EXT_FMT_TRACEFIELD, traceField);
		Integer rowNumFmt = (Integer) cellsModel.getCell(anchorCell).getExtFmt(DataSetFuncCalcUtil.EXT_FMT_ROWNUM);
		traceDataParam.addParam(DataSetFuncCalcUtil.EXT_FMT_ROWNUM, rowNumFmt);
		/*
		ufoFmlExecutor.getCalcEnv().setMeasureTraceVOs(new MeasureTraceVO[] {});
		UfoVal[] ufoVals = expr.getValue(ufoFmlExecutor.getCalcEnv());
		if (ufoVals != null && ufoVals.length > 0) {
			if (rowNumFmt != null) {
				traceRow = rowNumFmt.intValue();
			}
			if (ufoVals.length == 1) {
				traceRow = 0;
			}
			if (traceRow >= ufoVals.length)
				return;
			UfoArray rowData = (UfoArray) ufoVals[traceRow];
			if (rowData == null) {
				return;
			}
			Object[] objDatas = (Object[]) rowData.getValue();
			if (traceField > objDatas.length) {
				return;
			}
			traceDataParam.setValue(objDatas[traceField]);
			int rowNum = ((Integer) objDatas[objDatas.length - 1]).intValue();
			traceDataParam.setRow(rowNum);
			traceDataParam.setRowData(objDatas);
		} else if (ufoVals == null) {
			traceDataParam.setRow(-1);
		}
		*/
	}

	/**
	 * modify by wangyga ͨ����Ԫ�ĸ�ʽ��Ϣ�������õ�ֵ ���ù�ʽ����ֵ����������ʽ��Ҫ������Լ�����
	 * 
	 * @param formulaParsedDataItem
	 * @param ufoVals
	 */
	public static void setFormulaValue(CellsModel cellsModel, FormulaParsedDataItem formulaParsedDataItem,
			CellPosition cell, UfoVal[] ufoVals) {
		if (cellsModel == null || formulaParsedDataItem == null || ufoVals == null || ufoVals.length == 0)
			return;
		Format format = cellsModel.getCellFormat(cell);
		int index = formulaParsedDataItem.getRelaCellInAreaFml();
		ufoVals = transformData(ufoVals);
		if (ufoVals == null || ufoVals.length == 0) {
			return;
		} else if (index == -1) {
			formulaParsedDataItem.setFormulaValue(FormulaTraceUtil.getFormatValueStr(getValidValue(cell, format,
					ufoVals[0].getValue())));
		} else {
			if (index < ufoVals.length) {
				formulaParsedDataItem.setFormulaValue(FormulaTraceUtil.getFormatValueStr(getValidValue(cell,
						format, ufoVals[index].getValue())));
			} else {
				formulaParsedDataItem.setFormulaValue(FormulaTraceUtil.getFormatValueStr(getValidValue(cell,
						format, ufoVals[0].getValue())));
			}
		}
	}

	/**
	 * ����������ĵ����ݽ���ת��
	 * @create by wangyga at 2009-8-13,����04:34:47
	 *
	 * @param values
	 * @return
	 */
	public static UfoVal[] transformData(UfoVal[] values){
		UfoVal[] newUfoVals = null;
		UfoArray arrayVal = null;
		if(values != null){
			for(UfoVal val:values){
				if(val != null && val.isArray() && val.getDimens()==1){
					arrayVal = (UfoArray)val;
					break;
				}
			}
		}
		if (values != null && values.length>0 && values[0] != null && values[0].isArray() && arrayVal == null) {
			UfoVal[] newvalTemp = values[0].makeArrayVals();
			int height = 1;
			int dimens = ((Integer) newvalTemp[0].getValue()).intValue();
			for (int i = 1; i < dimens; i++) {
				height *= ((Integer) newvalTemp[i].getValue()).intValue();
			}
			newUfoVals = new UfoVal[newvalTemp.length - dimens - 1];
			System.arraycopy(newvalTemp, dimens + 1, newUfoVals, 0,
					newvalTemp.length - dimens - 1);
		} else
			newUfoVals = values;
		
		return newUfoVals;
	}
	
	/**
	 * add by wangyga ���鵥Ԫ��ֵ
	 * 
	 * @param cellPos
	 * @param format
	 * @param value
	 * @return
	 * @i18n miufohbbb00105=��Ԫ����ֵΪ���������ʧ��
	 * @i18n miufohbbb00106=��Ԫ����ֵΪ��Ч��������ʧ��
	 */
	private static Object getValidValue(CellPosition cellPos, Format format, Object value) {
		if (format == null || value == null)
			return value;

		Object retValue = value;
		// ��ֵ
		if (format.getCellType() == TableConstant.CELLTYPE_NUMBER) {
			if (value instanceof Double) {
				if (((Double) value).isInfinite()) {
					AppDebug.debug(StringResource.getStringResource("miufohbbb00105"));
					retValue = null;
				} else if (((Double) value).isNaN()) {
					AppDebug.debug(StringResource.getStringResource("miufohbbb00106"));
					retValue = null;
				} else if (Math.abs(((Double) value).doubleValue()) <= 1.0e-20) {
					retValue = new Double("0");
				} else {
					int digits = ((IufoFormat) format).getDecimalDigits();
					if (digits == TableConstant.UNDEFINED)
						digits = DefaultSetting.DEFAULT_DECIMALDIGITS;
					if (((IufoFormat) format).isHasPercent() == TableConstant.TRUE)
						digits += 2;
					retValue = new Double(UfoPublic1.roundDouble(((Double) retValue).doubleValue(), digits));
				}

			} else if (value instanceof Integer) {
				retValue = Double.valueOf(value.toString());
			}
		} else {
			if (value instanceof BigDecimal) {
				retValue = Double.valueOf(((BigDecimal) value).doubleValue());
			}
		}

		return retValue;
	}

	public IFormulaTraceValueItem calFormulaTraceValueItem(ContextVO contextVO, CellsModel cellsModel,
			IFormulaParsedDataItem formulaParsedDataItem, CellPosition cell) {
		// // TODO
		// ���ݹ�ʽ�����������IFormulaParsedDataItem��׼��������������ֵ׷�ٵ����ݶ���IFormulaTraceValueItem
		// IFormulaTraceValueItem formulaTraceValueItem = new
		// FormulaTraceValueItem();
		// //// //ȡ����ǰѡ��������Ĺ�ʽ����
		// if(!isExistFormula(cellsModel, cell)){
		// return null;
		// }
		// //
		// UfoFmlExecutor ufoFmlExecutor =
		// getFormulaHandler((TableInputContextVO)contextVO, cellsModel);
		//		
		// ReportDynCalcEnv dynCalcEnv = ufoFmlExecutor.getCalcEnv();
		//		
		// DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		// 
		// //�����ʽ�ڶ�̬���У������ö�̬�����㻷��
		// int unitAreaNum = -1, stepRow = -1, stepCol = -1;
		// boolean isInDynArea = dynAreaModel.isInDynArea(cell);
		// if(isInDynArea){
		// DynAreaCell dynAreaCell = dynAreaModel.getDynAreaCellByPos(cell);
		// stepRow = dynAreaCell.getDynAreaVO().isRowDirection() ?
		// dynAreaCell.getDynAreaVO().getOriArea().getHeigth() : 0;
		// stepCol = dynAreaCell.getDynAreaVO().isRowDirection() ?
		// 0 : dynAreaCell.getDynAreaVO().getOriArea().getWidth();
		// unitAreaNum = dynAreaCell.getOwnerUnitAreaNum(cell);
		// //���÷�����㻷������
		// setDynAreaCalcEnv(ufoFmlExecutor, dynAreaCell, stepRow, stepCol,
		// unitAreaNum);
		//
		// } else{
		// dynCalcEnv.setDynAreaInfo(null, null);
		// }
		//		
		// UfoCalcEnv env = ufoFmlExecutor.getCalcEnv();
		//		
		// UfoExpr expr = formulaParsedDataItem.getTracedExpr();
		//
		// MeasureTraceVO[] tracevos = new MeasureTraceVO[1];
		// MeasureTraceVO tracevo = new MeasureTraceVO();
		// tracevos[0] = tracevo;
		//		
		// try{
		//			 
		// MeasFunc func = (MeasFunc) expr.getElements()[0].getObj();
		// MeasureVO[] measurevos = func.getMeasures(env);
		//            
		// String measpk = measurevos[0].getCode();
		// String reportpk = measurevos[0].getReportPK();
		//			
		// //���ñ�ʶ���˼��㻷��Ϊָ�깫ʽ׷��
		// env.setMeasureTraceVOs(tracevos);
		//
		// UfoFormulaProxy.preCalcExtFunc(new UfoExpr[]{expr},null, env, 3);
		//			
		// tracevos = env.getMeasureTraceVOs();
		// tracevo = tracevos[0];
		//			
		// // String aloneID = tracevo.getAloneID();
		// // AppDebug.debug("ָ��׷�ٹؼ���ֵ��" + tracevo.getKeyvalues());
		//			
		// tracevo.setMeasurePK(measpk);
		// tracevo.setReportpk(reportpk);
		//			
		// // UfoVal[] vals = expr.calcExpr(env);
		//				 
		// }catch(Throwable e){
		// AppDebug.debug(e);//@devTools AppDebug.debug(e);
		// throw new RuntimeException(e.getMessage());
		//				 
		// } finally {
		// UfoFormulaProxy.clearPreCalcValues(new UfoExpr[]{expr},env);
		//			 
		// }
		// 
		// clearDynAreaCalcEnv(ufoFmlExecutor);
		//		 
		// formulaTraceValueItem.setTracevo(tracevo);
		//		
		// return formulaTraceValueItem;

		return null;
	}

	/**
	 * ׷�ٵ�Ԫλ�� modify by ����� �˷�����Ϊ���нӿڣ��ڶ�λ���õ�
	 * 
	 * @return
	 */
	public IArea[] getTracedPos(UfoExpr expr, ICalcEnv env, CellsModel cellModel) {
		if (isUfoFunc(expr, env)) {
			ArrayList<IArea> funcAreas = getUfoFuncArea(expr, cellModel);
			return (funcAreas != null && funcAreas.size() > 0) ? funcAreas.toArray(new IArea[0]) : null;
		} else if (isMeasFunc(expr, env)) {
			ArrayList<CellPosition> measAreas = getMeasArea(expr, cellModel);
			return (measAreas != null && measAreas.size() > 0) ? measAreas.toArray(new IArea[0]) : null;
		}
		return null;
	}

	/**
	 * �Ƿ񱾱�׷�٣�ֻ�����ñ�������ķ�ָ�꺯���ſ���׷�ٱ���
	 * 
	 * @param isHasNCFuncInIFFFunc
	 * @param isIFFFunc
	 * @return
	 */
	private boolean isTraceSelf(UfoExpr expr, UfoCalcEnv env, boolean isIFFFunc) {
		if (isIFFFunc)
			return false;
		if (expr.isRelaWithArea(env, 3))// ����ʽ
			return true;
		return (isTracebleMeasureFunc(expr, env, true) && isInEnvRepAndNonRefRep(expr, env));// ���������ı���mselect(a,
																								// s)
	}

	/**
	 * ָ�꺯���Ƿ���㱾������(����Ǳ���Ӧ���������ã������������ֱ�ӷ���false)
	 * 
	 * @param expr
	 * @param env
	 * @return
	 */
	private boolean isInEnvRepAndNonRefRep(UfoExpr expr, UfoCalcEnv env) {
		Object objFunc = expr.getElementObjByIndex(0);
		if (objFunc != null && objFunc instanceof MeasFunc) {
			MeasFunc measFunc = (MeasFunc) objFunc;
			boolean isInEnvRep = measFunc.isInEnvRep(env);
			if (!isInEnvRep) {
				return false;
			}

			List params = measFunc.getParams();
			for (int i = 0; i < params.size(); i++) {
				if (i > 0 && params.get(i) != null) {
					return false;
				}
			}
		}
		return true;

	}

	/**
	 * �����ʽ�����Ƿ���Ҫ����
	 * 
	 * @param isHasNCFuncInIFFFunc
	 * @param isIFFFunc
	 * @return
	 */
	private boolean isNeedToCal(FormulaParsedDataItem parsedDataItem, boolean isInEnvRepAndNonRefRep,
			boolean isIFFFunc, boolean isHasNCFuncInIFFFunc, boolean isDataSetFunc) {
		return parsedDataItem.getFormulaValue() == null || !isInEnvRepAndNonRefRep || parsedDataItem.isNCFunc()
				|| (isIFFFunc && isHasNCFuncInIFFFunc)
		 || isDataSetFunc;
	}

	/**
	 * �����ʽ�����Ƿ���Ҫ׷�٣�ֻ�г������͡�δ��������ļ򵥺����Ų�׷�٣�����飺
	 * 1�����в����Ƿ��UfoFullArea�����⣬�����ָ������Ƿ���������������Ƿ����档
	 * nFlag=3,����˼��UfoFullArea����ָ�꺯���еڶ��������Ժ��ָ�����������Ҳ������.
	 * ָ����������ر�ʾָ��������ǰ���㻷������ 2����MPercentFunc��������ָ�꺯����Ҫ׷�٣�
	 * 
	 * @param expr
	 * @param env
	 * @param isHasNCFuncInIFFFunc
	 * @param isIFFFunc
	 * @return
	 */
	private boolean canTrace(UfoExpr expr, UfoCalcEnv env, boolean isDataSetFunc, boolean isIFFFunc,
			boolean isHasNCFuncInIFFFunc) {
		if (isIFFFunc)// IFF����
			return false;
		if (UfoFormulaTraceBizHelper.isExtNCFunc(expr, env)) {// ҵ����
			return true;
		}
		if (isDataSetFunc) {// ���ݼ�����
			return true;
		}
		// ָ�꺯��
		if (isMeasFunc(expr, env)) {
			if (isTracebleMeasureFunc(expr, env, false))
				return true;

			return false;
		}
		// ����ʽ������
		return expr.isRelaWithArea(env, 3);
	}

	/**
	 * �Ƿ��ֵ׷��
	 * 
	 * @return
	 */
	private boolean isTraceMultiValues(UfoExpr expr, ICalcEnv env) {
		return isMultiValues(expr, env);
	}

	/**
	 * �Ƿ��ֵ
	 * 
	 * @return
	 */
	private boolean isMultiValues(UfoExpr expr, ICalcEnv env) {
		return expr.getValueNum(env) > 1;
	}

	/**
	 * ��鹫ʽ�����Ƿ�ָ�꺯��
	 * 
	 * @return
	 */
	private boolean isMeasFunc(UfoExpr expr, ICalcEnv env) {
		Object objFunc = expr.getElementObjByIndex(0);
		if (objFunc != null && (objFunc instanceof MeasFunc || objFunc instanceof MeasPercentFunc)) {
			return true;
		}
		return false;

	}

	/**
	 * ��鹫ʽ�����Ƿ�MeasPercentFunc����
	 * 
	 * @param expr
	 * @param env
	 * @return
	 */
	private boolean isMPercentFunc(UfoExpr expr, ICalcEnv env) {
		Object objFunc = expr.getElementObjByIndex(0);
		if (objFunc != null && objFunc instanceof MeasPercentFunc) {
			return true;
		}
		return false;
	}

	/**
	 * ���ָ�꺯���Ƿ���֧��׷�ٵ�ָ�꺯����ֻ��mSelect(a,s)��mSum(a,s)���֣� boolean onlySelect:
	 * true:mSelectϵ��; false:mSumϵ��
	 * 
	 * @return
	 */
	private boolean isTracebleMeasureFunc(UfoExpr expr, ICalcEnv env, boolean onlySelect) {
		Object objFunc = expr.getElementObjByIndex(0);
		if (objFunc != null && objFunc instanceof MeasFunc) {
			MeasFunc measFunc = (MeasFunc) objFunc;
			// if(measFunc.getFid() == MeasFuncDriver.FMSELECT ||
			// measFunc.getFid() == MeasFuncDriver.FMSELECTA ||
			// measFunc.getFid() == MeasFuncDriver.FMSELECTS)
			// return true;
			// if(!onlySelect){
			// if(measFunc.getFid() == MeasFuncDriver.FMSUM || measFunc.getFid()
			// == MeasFuncDriver.FMSUMA )
			// return true;
			// }
			String funcName = measFunc.getFuncName();
			if (funcName.equalsIgnoreCase(MeasFuncDriver.MSELECT) || funcName.equalsIgnoreCase(MeasFuncDriver.MSELECTA)
					|| funcName.equalsIgnoreCase(MeasFuncDriver.MSELECTS))
				return true;
			if (!onlySelect) {
				if (funcName.equalsIgnoreCase(MeasFuncDriver.MSUM) || funcName.equalsIgnoreCase(MeasFuncDriver.MSUMA))
					return true;
			}
		}
		return false;
	}

	/**
	 * ���ع�ʽ���ʽ�е����ݼ�����
	 * 
	 * @param expr
	 * @return
	 */
	private DataSetFunc getDataSetFunc(UfoExpr expr) {
		Object objFunc = expr.getElementObjByIndex(0);
		if (objFunc != null && objFunc instanceof DataSetFunc) {
			DataSetFunc func = (DataSetFunc) objFunc;
			return func;
		}
		return null;
	}

	/**
	 * ��鹫ʽ�����Ƿ�ָ�꺯��������������ָ��
	 * 
	 * @deprecated
	 * @return
	 */
	private boolean isMeasFuncAndRefRep(UfoExpr expr, UfoCalcEnv env) {
		Object objFunc = expr.getElementObjByIndex(0);
		if (objFunc == null) {
			return false;
		}

		if (objFunc instanceof MeasFunc) {
			MeasFunc measFunc = (MeasFunc) objFunc;
			// ֻȡ�������ݵ�ָ�꺯��Ϊ���ñ�����ָ�꺯������Ϊ��
			if (MeasFuncDriver.MSELECT.equalsIgnoreCase(measFunc.getFuncName())
					&& getMeasFuncParams(measFunc.getParams()) > 1) {
				return true;
			}
			// ����MSELECTA�����ڱ����Ҳ�������������
			if (MeasFuncDriver.MSELECTA.equalsIgnoreCase(measFunc.getFuncName())) {
				// �Ƿ񱾱�ָ��
				ArrayList alPara = measFunc.getParams();
				int nParaSize = alPara.size();
				UfoFullArea fullArea = (UfoFullArea) alPara.get(0);
				if (fullArea.hasReport() == true) {
					return true;
				}
				if (fullArea.hasReport() == false) {
					// �ж�mselecta�������Ƿ�Ϊ��
					boolean hasOtherParam = false;
					for (int k = 1; k < nParaSize; k++) {
						if (alPara.get(k) != null) {
							hasOtherParam = true;
						}
					}
					return hasOtherParam ? true : false;
				}
			}

			return !measFunc.isInEnvRep(env);
		} else if (objFunc instanceof MeasPercentFunc) {
			MeasPercentFunc measPercFunc = (MeasPercentFunc) objFunc;
			MeasOperand objOperand = (MeasOperand) measPercFunc.getParams().get(0);
			MeasureVO measure = objOperand.getMeasureVO();
			return !(env.getRepPK()).equals(measure.getReportPK()) ? true : false;
		}
		return false;

	}

	/**
	 * ����ָ�꺯���Ĳ�������
	 * 
	 * @param params
	 * @return
	 */
	private int getMeasFuncParams(ArrayList params) {
		if (params == null) {
			return 0;
		}

		int count = 0;
		for (int index = 0; index < params.size(); index++) {
			if (params.get(index) != null) {
				count++;
			}
		}
		return count;

	}

	/**
	 * ����ָ�꺯���������
	 * 
	 * @param elem
	 * @return
	 */
	private ArrayList<CellPosition> getMeasArea(UfoExpr expr, CellsModel cellsModel) {
		Object objFunc = expr.getElementObjByIndex(0);
		if (objFunc != null && objFunc instanceof MeasFunc) {
			MeasFunc measFunc = (MeasFunc) objFunc;
			ArrayList measCellPosList = new ArrayList();
			ArrayList params = measFunc.getParams();

			for (int index = 0; index < params.size(); index++) {
				if (params.get(index) instanceof MeasOperand) {
					MeasureVO measure = ((MeasOperand) params.get(index)).getMeasureVO();
					CellPosition cellPos = MeasureModel.getInstance(cellsModel).getMeasurePosByPK(measure.getCode());
					if (cellPos != null)
						measCellPosList.add(cellPos);
				} else if ((params.get(index) instanceof MultiMeasOperand)) {
					MeasureVO[] measures = ((MultiMeasOperand) params.get(index)).getMeasList();
					for (MeasureVO measure : measures) {
						CellPosition cellPos = MeasureModel.getInstance(cellsModel)
								.getMeasurePosByPK(measure.getCode());
						if (cellPos != null)
							measCellPosList.add(cellPos);
					}
				} else if (params.get(index) instanceof UfoFullArea) {
					UfoFullArea fullArea = (UfoFullArea) params.get(index);
					measCellPosList.addAll(cellsModel.seperateArea(fullArea.getArea()));
				}
			}
			return measCellPosList;
		}
		return null;
	}

	/**
	 * ����Ƿ��ָ�꺯����Ufo����
	 * 
	 * @return
	 */
	private boolean isUfoFunc(UfoExpr expr, ICalcEnv env) {
		Object objFunc = expr.getElementObjByIndex(0);
		if ((objFunc != null && objFunc instanceof UfoFullArea)
				|| (objFunc != null && objFunc instanceof UfoFunc && !(objFunc instanceof MeasFunc))) {
			return true;
		}
		return false;

	}

	/**
	 * ���ط�ָ�꺯����Ufo�������������
	 * 
	 * @param elem
	 * @return
	 */
	private ArrayList<IArea> getUfoFuncArea(UfoExpr expr, CellsModel cellsModel) {
		Object objFunc = expr.getElementObjByIndex(0);
		ArrayList<IArea> exprAreaList = new ArrayList<IArea>();
		if (objFunc != null && objFunc instanceof UfoFullArea) {
			UfoEElement[] elems = expr.getElements();
			for (UfoEElement elem : elems) {
				if (elem.getObj() != null && elem.getObj() instanceof UfoFullArea) {
					UfoFullArea fullArea = (UfoFullArea) elem.getObj();
					exprAreaList.add(fullArea.getArea());
				}
			}
		} else if (objFunc != null && objFunc instanceof UfoFunc) {
			UfoFunc ufoFunc = (UfoFunc) objFunc;
			ArrayList funcParas = ufoFunc.getParams();
			if (funcParas == null) {
				return null;
			}
			for (int i = 0; i < funcParas.size(); i++) {
				Object param = funcParas.get(i);
				if (param instanceof UfoFullArea) {
					UfoFullArea fullArea = (UfoFullArea) param;
					exprAreaList.add(fullArea.getArea());
				}
				if (param instanceof UfoExpr) {
					UfoEElement[] elems = ((UfoExpr) param).getElements();
					for (UfoEElement elem : elems) {
						if (elem.getObj() != null && elem.getObj() instanceof UfoFullArea) {
							UfoFullArea fullArea = (UfoFullArea) elem.getObj();
							exprAreaList.add(fullArea.getArea());
						}
					}
				}
			}
		}

		return exprAreaList;
	}

}
 