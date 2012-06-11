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
 * 客户端的公式追踪业务接口实现类
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
	 * 检查指定单元格是否定义有公式
	 * 
	 * @param cellsModel
	 * @param area
	 * @return boolean
	 */
	public boolean existFormula(CellsModel cellsModel, CellPosition cell) {
		return isExistFormula(cellsModel, cell);

	}

	public FormulaParsedData parseFormula(Context contextVO, CellsModel cellsModel, CellPosition cell) {
		// 取出当前选中区域定义的公式对象
		if (!isExistFormula(cellsModel, cell)) {
			return null;
		}

		UfoFmlExecutor ufoFmlExecutor = UfoFormulaTraceBizHelper.getFormulaHandler(contextVO,
				cellsModel);
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		FormulaModel formulaModel = dynAreaModel.getFormulaModel();

		// 如果该公式尚未解析，首先对公式进行解析
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

		// 如果公式无法解析，则不再进行计算
		// (如公式定义有误，如公式HBMSELECT('会合01表->货币资金','合并报表任务','月',-1)等公式)
		fmtFmlArea = DynAreaCell.getRealArea(fmtFmlArea, cellsModel);
		if (formulaVO.getLet() == null) {
			return setNotParsedFormulaData(cell, fmtFmlArea, formulaVO);
		} else {
			// 如果是HR函数，则不再进行解析及追踪
			if (UfoFormulaTraceBizHelper.isExtHRFunc(formulaVO.getLet(), ufoFmlExecutor.getCalcEnv())) {
				return null;
			}
			return setParsedFormulaData(cell, fmtFmlArea, cellsModel, ufoFmlExecutor, formulaVO);
		}

	}

	/**
	 * 计算公式相对位置
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
	 * 返回公式解析参数(不能在客户端进行解析的公式，如MSUMA()、MSELECTA()等)
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
	 * 返回公式解析参数(能在客户端进行解析的公式，包括非取他表的指标公式)
	 * 
	 * @param cell
	 * @param cellsModel
	 * @param ufoFmlExecutor
	 * @param formulaVO
	 * @return FormulaParsedData
	 */
	private FormulaParsedData setParsedFormulaData(CellPosition cell, IArea fmtFmlArea, CellsModel cellsModel,
			UfoFmlExecutor ufoFmlExecutor, FormulaVO formulaVO) {
		// 实际的公式区域(对于动态区上定义的公式为公式随动态区扩展后的区域，
		IArea realFmlExtArea = fmtFmlArea;
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		ReportDynCalcEnv dynCalcEnv = ufoFmlExecutor.getCalcEnv();

		// 如果公式在动态区中，则设置动态区计算环境
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
			// 设置分组计算环境参数
			setDynAreaCalcEnv(ufoFmlExecutor, dynAreaCell, stepRow, stepCol, unitAreaNum);
		} else {
			dynCalcEnv.setDynAreaInfo(null, null);
			dynCalcEnv.setDynAllKeyDatas(null);
			dynCalcEnv.setDynField2Values(null);
		}

		// 构建公式解析数据对象，为公式追踪准备数据
		FormulaParsedData formulaParsedData = new FormulaParsedData();
		formulaParsedData.setFormulaDisContent(formulaVO.getLet().toUserDefString(ufoFmlExecutor.getCalcEnv()));
		formulaParsedData.setTracePos(cell);

		// 计算每个细节表达式，分析公式并为公式追踪准备细节数据
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
			boolean isDataSetOut = false;//数据集函数超出列数，不用计算和联查
			boolean hasAvgField = false;
			boolean isHasNCFuncInIFFFunc = false;
			boolean isInEnvRepAndNonRefRep = true;
			boolean isHasDataSetFunc = false;
			String strUserDis = expr.toUserDefString(ufoFmlExecutor.getCalcEnv());
			// 对于业务函数(如总帐或HR等)或者IFF函数中不包含总帐函数，跳过计算
			boolean isNCFunc = UfoFormulaTraceBizHelper.isExtNCFunc(expr, ufoFmlExecutor.getCalcEnv());

			try {
				// 对于区域公式，设置公式相对计算位置
				if (!realFmlExtArea.isCell()) {
					CellPosition relaFmlPos = getRelaFmlArea(cell, realFmlExtArea);
					formulaParsedDataItem.setRelaCell(relaFmlPos);
					formulaParsedDataItem.setAbsCell(cell);

					// 按照先行后列计算
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
				// 对于IFF函数，检查是否包含总帐等业务函数
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
					// 因为总帐NCAppletStub为唯一实例，客户端调用后服务器端再调用该接口会抛出异常，因此在此做一次总帐环境的清除
					clearNCStubEnv();
				} else if (isDataSetFunc && realFmlExtArea.contain(cell)) {
					// 设置数据集追踪参数
					DataSetFunc func = getDataSetFunc(expr);
					Field[] flds = func.getDataSetFieldsVal();
					if (flds == null
							|| (formulaParsedDataItem.getRelaCell() != null && ((isInDynArea && flds.length <= formulaParsedDataItem
									.getRelaCellInAreaFml()) || (!isInDynArea
									&& flds.length <= formulaParsedDataItem.getRelaCell().getColumn())))) {// add by ll, 对于超出列数的单元不用计算
						formulaParsedDataItem.setFormulaValue("");
						isDataSetOut = true;
					} else {
						DataSetDefVO datasetDef = func.getDataSetVal();
						DataSet dataSet = datasetDef.getDataSetDef();
						TraceDataParam traceDataParam = new TraceDataParam(dataSet);
						formulaParsedDataItem.setTraceDataParam(traceDataParam);
//						@edit by ll at 2008-12-24 上午10:05:41, 为了效率这里不作处理，改为在“计算”和“联查”的时候才去执行取数
						setExtTraceParam(cell, realFmlExtArea, ufoFmlExecutor, expr, func, traceDataParam, cellsModel);
						hasAvgField = dataSet.getMetaData().hasAvgField();//有聚集字段不可追踪
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
					// @edit by wangyga at 2009-2-23,下午03:38:38 MeasureQuery函数不显示子项值
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

			// 将公式区域转换为实际区域，为后面公式追踪焦点绘制准备数据
			IArea[] traceCellPosList = getTracedPos(expr, ufoFmlExecutor.getCalcEnv(), cellsModel);
			traceCellPosList = getRealExprAreaByOriArea(ufoFmlExecutor, cellsModel, dynAreaModel, unitAreaNum, stepRow,
					stepCol, traceCellPosList);

			// 由区域公式追踪相对位置，设置相对追踪区域
			if (formulaParsedDataItem.isAreaFunc()
					&& traceCellPosList.length > formulaParsedDataItem.getRelaCellInAreaFml()
					&& formulaParsedDataItem.getRelaCellInAreaFml() > -1) {
				int relaCell = formulaParsedDataItem.getRelaCellInAreaFml();
				traceCellPosList = new IArea[] { traceCellPosList[relaCell] };
			}

			// 设置公式追踪子项细节信息
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
			// @edit by wangyga at 2009-10-16,上午11:30:14 nc函数，带区域不是计算本表
			formulaParsedDataItem.setTraceSelf(isTraceSelf(expr, ufoFmlExecutor.getCalcEnv(), isIFFFunc) && !isNCFunc);
			formulaParsedDataItem.setTracedPos(traceCellPosList);
			formulaParsedDataItem.setTraceMultiValues(isTraceMultiValues(expr, ufoFmlExecutor.getCalcEnv()));
		}
		clearNCStubEnv();
		clearDynAreaCalcEnv(ufoFmlExecutor);
		return formulaParsedData;

	}

	
	/**
	 * 清除总帐NCAppletStub环境，NCAppletStub为唯一实例，客户端调用后服务器端再调用该接口会抛出异常
	 */
	private void clearNCStubEnv() {
		NCAppletStub ncStub = NCAppletStub.getInstance();
		ncStub.setParameter("ACCOUNT_ID", "");
	}

	/**
	 * 检查指定单元格是否存在公式 公式可能是单元公式，也可能是区域公式，需要根据正确的选择区域计算正确的公式区域， 而后检查是否定义公式
	 * 
	 * @param cellsModel
	 * @param cell
	 * @return
	 */
	private boolean isExistFormula(CellsModel cellsModel, CellPosition selPos) {
		// 指定的单元格返回CellPosition类型，如果选择的是组合单元，则返回正确的选择区域 而
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

	//判断所有表达式中是否函数数据集函数
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
	 * 设置动态区计算环境参数
	 * 
	 * @param ufoFmlExecutor
	 * @param dynAreaCell
	 * @param stepRow
	 * @param stepCol
	 * @param unitAreaNum
	 */
	static void setDynAreaCalcEnv(UfoFmlExecutor ufoFmlExecutor, DynAreaCell dynAreaCell, int stepRow, int stepCol,
			int unitAreaNum) {
		// 设置分组计算环境参数
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
	 * 清除动态区的计算环境参数
	 * 
	 * @param ufoFmlExecutor
	 */
	static void clearDynAreaCalcEnv(UfoFmlExecutor ufoFmlExecutor) {
		ufoFmlExecutor.getCalcEnv().setDynAreaInfo(null, null);
	}

	/**
	 * 将公式定义区域转换为实际显示区域
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
			if (dynAreaCell == null) {// 子项定义区域不在动态区
				realArea = getRealAreaPos(null, cellsModel, area);
			} else if (dynAreaDataParam != null && // 子项定义区域在其他动态区，暂时不支持的情况
					!dynAreaDataParam.getDynAreaPK().equals(dynAreaCell.getDynAreaPK())) {
				realArea = getRealAreaPos(null, cellsModel, area);
			} else {// 子项定义区域在当前动态区
				realArea = getRealAreaPos(dynAreaDataParam, cellsModel, area);
			}
			vctRealArea.addAll(Arrays.asList(realArea));
		}

		return vctRealArea.toArray(new IArea[0]);
	}

	/**
	 * 根据动态区分组计算参数计算公式区域实际位置
	 * 
	 * @param dynAreaDataParam
	 * @param cellsModel
	 * @param oriExprArea
	 * @return
	 */
	private IArea[] getRealAreaPos(IDynAreaDataParam dynAreaDataParam, CellsModel cellsModel, IArea oriExprArea) {
		IArea realExprArea = oriExprArea;
		if (dynAreaDataParam == null) {
			// 公式定义在固定区,同时公式子项不在动态区，则直接计算实际位置
			IArea realArea = DynAreaCell.getRealArea(realExprArea, cellsModel);
			DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
			boolean isExprInDynArea = dynAreaModel.isInDynArea(realArea.getStart());
			if (!isExprInDynArea) {
				return new IArea[] { realArea };
			} else {// 公式定义在固定区，同时公式子项在动态区，则计算扩展区域(如PTotal函数)
				DynAreaCell dynAreaCell = dynAreaModel.getDynAreaCellByPos(realArea.getStart());
				return dynAreaCell.getUnitAreasByRealArea(realArea);
			}
		}

		// 公式定义在动态区，同时子项区域oriExprArea也在动态区，则根据当前环境计算正确的计算位置
		boolean direction = dynAreaDataParam.getDirection() == Header.ROW;
		int dRow = direction ? (dynAreaDataParam.getStepRow() * dynAreaDataParam.getUnitDataRowIndex())
				: dynAreaDataParam.getStepRow();
		int dCol = direction ? dynAreaDataParam.getStepCol() : (dynAreaDataParam.getStepCol() * dynAreaDataParam
				.getUnitDataRowIndex());
		realExprArea = oriExprArea.getMoveArea(dRow, dCol);
		return new IArea[] { realExprArea };
	}

	/**
	 * 设置数据集表达式追踪参数
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
	 * modify by wangyga 通过单元的格式信息交验设置的值 设置公式计算值，对于区域公式需要返回相对计算结果
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
	 * 将计算出来的的数据进行转换
	 * @create by wangyga at 2009-8-13,下午04:34:47
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
	 * add by wangyga 交验单元的值
	 * 
	 * @param cellPos
	 * @param format
	 * @param value
	 * @return
	 * @i18n miufohbbb00105=单元的数值为无穷大，设置失败
	 * @i18n miufohbbb00106=单元的数值为无效数，设置失败
	 */
	private static Object getValidValue(CellPosition cellPos, Format format, Object value) {
		if (format == null || value == null)
			return value;

		Object retValue = value;
		// 数值
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
		// 根据公式解析后的数据IFormulaParsedDataItem，准备往服务器请求单值追踪的数据对象IFormulaTraceValueItem
		// IFormulaTraceValueItem formulaTraceValueItem = new
		// FormulaTraceValueItem();
		// //// //取出当前选中区域定义的公式对象
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
		// //如果公式在动态区中，则设置动态区计算环境
		// int unitAreaNum = -1, stepRow = -1, stepCol = -1;
		// boolean isInDynArea = dynAreaModel.isInDynArea(cell);
		// if(isInDynArea){
		// DynAreaCell dynAreaCell = dynAreaModel.getDynAreaCellByPos(cell);
		// stepRow = dynAreaCell.getDynAreaVO().isRowDirection() ?
		// dynAreaCell.getDynAreaVO().getOriArea().getHeigth() : 0;
		// stepCol = dynAreaCell.getDynAreaVO().isRowDirection() ?
		// 0 : dynAreaCell.getDynAreaVO().getOriArea().getWidth();
		// unitAreaNum = dynAreaCell.getOwnerUnitAreaNum(cell);
		// //设置分组计算环境参数
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
		// //设置标识，此计算环境为指标公式追踪
		// env.setMeasureTraceVOs(tracevos);
		//
		// UfoFormulaProxy.preCalcExtFunc(new UfoExpr[]{expr},null, env, 3);
		//			
		// tracevos = env.getMeasureTraceVOs();
		// tracevo = tracevos[0];
		//			
		// // String aloneID = tracevo.getAloneID();
		// // AppDebug.debug("指标追踪关键字值：" + tracevo.getKeyvalues());
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
	 * 追踪单元位置 modify by 王宇光 此方法改为公有接口，在定位中用到
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
	 * 是否本表追踪，只有引用本表区域的非指标函数才可以追踪本表
	 * 
	 * @param isHasNCFuncInIFFFunc
	 * @param isIFFFunc
	 * @return
	 */
	private boolean isTraceSelf(UfoExpr expr, UfoCalcEnv env, boolean isIFFFunc) {
		if (isIFFFunc)
			return false;
		if (expr.isRelaWithArea(env, 3))// 区域公式
			return true;
		return (isTracebleMeasureFunc(expr, env, true) && isInEnvRepAndNonRefRep(expr, env));// 不带条件的本表mselect(a,
																								// s)
	}

	/**
	 * 指标函数是否计算本表数据(如果是本表应无条件设置，如果是他表则直接返回false)
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
	 * 检查表达式子项是否需要计算
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
	 * 检查表达式子项是否需要追踪，只有常量类型、未设置区域的简单函数才不追踪，即检查：
	 * 1、所有参数是否除UfoFullArea类型外，还检查指标参数是否与区域关联，如是返回真。
	 * nFlag=3,则除了检查UfoFullArea外检查指标函数中第二个参数以后的指标与区域关联也返回真.
	 * 指标与区域相关表示指标所属当前计算环境报表 2、除MPercentFunc以外所有指标函数需要追踪；
	 * 
	 * @param expr
	 * @param env
	 * @param isHasNCFuncInIFFFunc
	 * @param isIFFFunc
	 * @return
	 */
	private boolean canTrace(UfoExpr expr, UfoCalcEnv env, boolean isDataSetFunc, boolean isIFFFunc,
			boolean isHasNCFuncInIFFFunc) {
		if (isIFFFunc)// IFF函数
			return false;
		if (UfoFormulaTraceBizHelper.isExtNCFunc(expr, env)) {// 业务函数
			return true;
		}
		if (isDataSetFunc) {// 数据集函数
			return true;
		}
		// 指标函数
		if (isMeasFunc(expr, env)) {
			if (isTracebleMeasureFunc(expr, env, false))
				return true;

			return false;
		}
		// 区域公式可联查
		return expr.isRelaWithArea(env, 3);
	}

	/**
	 * 是否多值追踪
	 * 
	 * @return
	 */
	private boolean isTraceMultiValues(UfoExpr expr, ICalcEnv env) {
		return isMultiValues(expr, env);
	}

	/**
	 * 是否多值
	 * 
	 * @return
	 */
	private boolean isMultiValues(UfoExpr expr, ICalcEnv env) {
		return expr.getValueNum(env) > 1;
	}

	/**
	 * 检查公式子项是否指标函数
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
	 * 检查公式子项是否MeasPercentFunc函数
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
	 * 检查指标函数是否是支持追踪的指标函数（只有mSelect(a,s)和mSum(a,s)两种） boolean onlySelect:
	 * true:mSelect系列; false:mSum系列
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
	 * 返回公式表达式中的数据集函数
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
	 * 检查公式子项是否指标函数并且引用他表指标
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
			// 只取本表数据的指标函数为引用本表，及指标函数条件为空
			if (MeasFuncDriver.MSELECT.equalsIgnoreCase(measFunc.getFuncName())
					&& getMeasFuncParams(measFunc.getParams()) > 1) {
				return true;
			}
			// 对于MSELECTA函数在本表并且不能有其他条件
			if (MeasFuncDriver.MSELECTA.equalsIgnoreCase(measFunc.getFuncName())) {
				// 是否本表指标
				ArrayList alPara = measFunc.getParams();
				int nParaSize = alPara.size();
				UfoFullArea fullArea = (UfoFullArea) alPara.get(0);
				if (fullArea.hasReport() == true) {
					return true;
				}
				if (fullArea.hasReport() == false) {
					// 判断mselecta的条件是否为空
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
	 * 返回指标函数的参数个数
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
	 * 返回指标函数区域参数
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
	 * 检查是否非指标函数的Ufo函数
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
	 * 返回非指标函数的Ufo函数的区域参数
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
 