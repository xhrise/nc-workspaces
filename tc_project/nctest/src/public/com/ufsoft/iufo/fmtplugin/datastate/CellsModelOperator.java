package com.ufsoft.iufo.fmtplugin.datastate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import nc.bs.iufo.cache.IUFOBSCacheManager;
import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.RepFormatModelCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.base.CodeCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.pub.iufo.exception.CommonException;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.data.RepDataBO_Client;
import nc.vo.iufo.code.CodeVO;
import nc.vo.iufo.data.MeasureDataVO;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.data.RepDataVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iuforeport.rep.RepFormatModel;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.businessquery.ReportBusinessQuery;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.AreaDataProcess;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.CrossTabDef;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessDef;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.GroupLayingDef;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.formula.FormulaHandler;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaVO;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.fmtplugin.service.DataProcessSrv;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.inputplugin.formula.FormulaFmt;
import com.ufsoft.iufo.inputplugin.key.KeyFmt;
import com.ufsoft.iufo.inputplugin.measure.MeasureFmt;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.reporttool.temp.KeyDataGroup;
import com.ufsoft.iuforeport.reporttool.temp.KeyDataVO;
import com.ufsoft.iuforeport.tableinput.applet.TraceDataScope;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.table.header.HeaderModel;
import com.ufsoft.table.print.PrintSet;
import com.ufsoft.table.re.IDName;

/**
 * 1.�õ����ݻ��߸�ʽ̬��CellsModel
 * 2.װ��������ģ�����ݵ���ʽģ�ͣ��γ�����ģ�͡�
 * 3.����ģ���У��������͵�ֵ��value�зŵĶ��Ǳ���pk��ת��������ģ��ʱ����ת��ΪIDName��
 * @author zzl
 * 2005-12-29
 */
public class CellsModelOperator {
	/**
	 * ���ݱ���PK�õ���ʽCellsModel,�˸�ʽ����clone���������ȴ��벻���ٴ�clone
	 * @param reportPK
	 * @return
	 */
	public static CellsModel getFormatModelByPK(UfoContextVO context) {
		//��ԭɾ����̬��������ݴ���
		return delteteDataProcessFromFormatModel(getFormatModelByPKWithDataProcess(context));
	}

	public static CellsModel getFormatModelByPK(UfoContextVO context,
			boolean bLoadData) {
		CellsModel cellsModel = getFormatModelByPK(context);
		if (bLoadData) {
			try {
				cellsModel = loadDataFromDB(cellsModel, context, true);
			} catch (Exception e) {
				AppDebug.debug(e);
				throw new RuntimeException(e);
			}
		}

		return cellsModel;
	}

	/**
	 * �����ݿ���ȡ�����ݣ������ʽģ�ͣ��γ�����ģ�ͷ��ء�
	 * 
	 * @param formatModel
	 * @param context
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	private static CellsModel loadDataFromDB(CellsModel formatModel,
			UfoContextVO context, boolean loadDynAreaData) throws Exception {
		//		formatModel = (CellsModel) formatModel.clone();// liuyy. ����formatModel����clone����
		CellsModelOperator.initModelProperties(context, formatModel);
		MeasurePubDataVO measurePubDataVO = context.getPubDataVO();
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(formatModel);
		if (measurePubDataVO != null) {
			setMainKeyData(formatModel, measurePubDataVO);
			RepDataVO[] repDataVOs = RepDataBO_Client.loadRepData(context
					.getContextId(), context.getCurUserId(), context
					.getPubDataVO(), context.getOrgPK());
			if (repDataVOs != null && repDataVOs.length > 0
					&& repDataVOs[0].getMainPubData() != null) {
				setMainData(formatModel, repDataVOs[0], context);
				if (loadDynAreaData) {
					DynAreaCell[] dynCells = dynAreaModel.getDynAreaCells();
					for (int i = 0; i < dynCells.length; i++) {
						setDynAreaData(formatModel, dynCells[i], repDataVOs[0],
								context);
					}
				}
			}
		}
		formatModel = convertDataModelToLWModel(formatModel);
		return formatModel;
	}

	private static CellsModel delteteDataProcessFromFormatModel(
			CellsModel formatModel) {
		//��ģ�ͽ��л�ԭɾ����
		if (DynAreaModel.getInstance(formatModel).isProcessed()) {
			//			formatModel = (CellsModel) formatModel.clone();//(CellsModel) DeepCopyUtil.getDeepCopyBySerializable(formatModel);//
			DynAreaCell[] dynAreaCells = DynAreaModel.getInstance(formatModel)
					.getDynAreaCells();
			for (int i = 0; i < dynAreaCells.length; i++) {
				DataProcessSrv.delDynAreaDataProcess(formatModel,
						dynAreaCells[i]);
			}
		}
		return formatModel;
	}

	public static CellsModel getFormatModelByPKWithDataProcess(
			UfoContextVO context) {
		RepFormatModelCache repFormatModelCache = CacheProxy.getSingleton()
				.getRepFormatCache();
		CellsModel formatModel = repFormatModelCache
				.getUfoTableFormatModel(context.getContextId());
		formatModel = (CellsModel) formatModel.clone();

		//��Ӵ��жϣ�����û��ContextId�����������ָ���ѯ����鿴
		if (context.getContextId() == null || context.getContextId().equals("")) {
			return null;
		}
		ReportCache reportCache = CacheProxy.getSingleton().getReportCache();
		ReportVO[] reps = reportCache.getByPks(new String[] { context
				.getContextId() });
		if (reps != null && reps.length > 0 && reps[0] != null) {
			context.setModel(reps[0].isModel());
		}
		//        if(context.getKeyGroupPK() == null){
		//        	context.setKeyGroupPK(CellsModelOperator.getKeywordModel(formatModel).getMainKeyCombPK());
		//        }
		if (context.getKeyGroupPK() == null) {//���������ı�����û�йؼ������pk����Ҫ����KeyVO�ӻ����еõ���
			KeyVO[] mainKeyVOs = getKeywordModel(formatModel).getMainKeyVOs();
			KeyGroupVO keyGroupVO = new KeyGroupVO(mainKeyVOs);
			String mainKeyCompPK = CacheProxy.getSingleton().getKeyGroupCache()
					.getPkByKeyGroup(keyGroupVO).getKeyGroupPK();
			context.setKeyGroupPK(mainKeyCompPK);
		}
		//        initModelProperties(context,formatModel);
		//����ԭ�����������п��ܴ��ڵĶ�̬�������������Ϣ��һ��
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(formatModel);
		if (dynAreaModel != null) {
			DynAreaCell[] dynCells = dynAreaModel.getDynAreaCells();
			if (dynCells != null && dynCells.length > 0) {
				for (int i = 0; i < dynCells.length; i++) {
					//�����ݴ���ģ���з���ͽ���,���ܽ�������������
					AreaDataProcess areaDataProcess = DynAreaModel.getInstance(
							formatModel).getDataProcess(
							dynCells[i].getDynAreaPK());
					boolean canModify = false;
					if (areaDataProcess == null
							|| !areaDataProcess.isUserDefined()) {
						canModify = true;
					} else {
						boolean isGroup = areaDataProcess.getDataProcessDef() instanceof GroupLayingDef;
						boolean isCross = areaDataProcess.getDataProcessDef() instanceof CrossTabDef;
						canModify = !isGroup && !isCross;
					}
					if (canModify) {
						AreaPosition area = dynCells[i].getArea();
						if (dynCells[i].getDynAreaVO() != null)
							dynCells[i].getDynAreaVO().setOriArea(area);
					}
				}
			}
		}
        FormulaModel formulaModel = FormulaModel.getInstance(formatModel);
        formulaModel.removeErrorFormulas();

		return formatModel;
	}

	/**
	 * �Դ򿪵�ģ�ͣ��½��ĸ�ʽģ�͡�����excel��xml���ɵ�ģ�ͣ�Ҫʹ�ô˷������г�ʼ����
	 * @param contextVO
	 * @param formatModel
	 */
	public static void initModelProperties(UfoContextVO contextVO,
			CellsModel formatModel) {
		//		String strMainMeasPack = CellsModelOperator.getMeasureModel(formatModel).getMainMeasurePackPK();
		//		if (strMainMeasPack == null) {
		//			MeasureCache mCache = CacheProxy.getSingleton().getMeasureCache();
		//			strMainMeasPack = mCache.getNewMeasPackPK();
		//			CellsModelOperator.getMeasureModel(formatModel).setMainMeasurePackPK(strMainMeasPack);
		//		}
		if (contextVO.getCurUnitId() == null
				|| contextVO.getCurUnitId().trim().length() <= 0)
			return;
		getFormulaModel(formatModel).setUnitID(contextVO.getCurUnitId());
		if (contextVO.getCreateUnitId() != null) {
			getFormulaModel(formatModel).setCreateUnitID(
					contextVO.getCreateUnitId());
		}
		Hashtable<String, PrintSet> unitPrintSet = getUnitPrintSet(formatModel);
		if (unitPrintSet.get(contextVO.getCurUnitId()) != null) {
			formatModel.setPrintSet(unitPrintSet.get(contextVO.getCurUnitId()));
		}
	}

	/**
	 * ���㶯̬�����ָ�깫ʽ
	 * @param dynAreaModel
	 * @param strDynamicAreaPK
	 */
	private static void calDynAreaMeasureFm(DynAreaModel dynAreaModel,
			String strDynamicAreaPK) {
		// TODO need the programmer who is on duty of calculation,marked by liulp 2006-06-06

	}

	private static void setMainData(CellsModel formatModel, RepDataVO dataVO,
			UfoContextVO context) {
		MeasurePubDataVO measurePubDataVO = dataVO.getMainPubData();
		context.setPubDataVO(measurePubDataVO);
		setMainKeyData(formatModel, measurePubDataVO);
		setMainMeasureData(formatModel, dataVO
				.getMeasureDatas(measurePubDataVO));
	}

	public static void setMainMeasureData(CellsModel formatModel,
			MeasureDataVO[] measureDatas) {
		HashMap htMeasureData = new HashMap();
		for (int i = 0; measureDatas != null && i < measureDatas.length; i++) {
			MeasureVO measureVO = measureDatas[i].getMeasureVO();
			String value = measureDatas[i].getDataValue();
			Object objValue = value;
			if (measureVO.getType() == MeasureVO.TYPE_NUMBER) {
				objValue = Double.valueOf(value);
			}

			htMeasureData.put(measureVO, objValue);
		}

		MeasureModel measureModel = MeasureModel.getInstance(formatModel);
		Hashtable measurePos = measureModel.getMainMeasureVOPos();
		for (Enumeration enumer = measurePos.keys(); enumer.hasMoreElements();) {
			CellPosition cellPos = (CellPosition) enumer.nextElement();
			MeasureVO measureVO = (MeasureVO) measurePos.get(cellPos);//��̬����չǰ�Ĳ��������ý�������ת����
			formatModel.setCellValue(cellPos.getRow(), cellPos.getColumn(),
					htMeasureData.get(measureVO));
		}
	}

	/**
	 * ��������ؼ���ֵ��
	 * @param formatModel
	 * @param measurePubDataVO
	 * @param dynAreaModel
	 */
	public static void setMainKeyData(CellsModel formatModel,
			MeasurePubDataVO measurePubDataVO) {
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(formatModel);
		KeyVO[] keyVOs = measurePubDataVO.getKeyGroup().getKeys();
		for (int i = 0; i < keyVOs.length; i++) {
			String value = measurePubDataVO.getKeywordByIndex(i + 1);
			Object objValue = value;
			//����ģ���У��������Ͷ��ű���ֵ����ת������ģ����ʾʱ����Ҫת����		
			CellPosition cellPos = dynAreaModel.getKeywordModel()
					.getCellPosByPK(keyVOs[i].getKeywordPK());
			if (cellPos != null) {
				formatModel.setCellValue(cellPos.getRow(), cellPos.getColumn(),
						objValue);
			} else {
				dynAreaModel.getKeywordModel().setUndisplayKeyValue(keyVOs[i],
						objValue);
			}

		}
	}

	/**
	 * ת������ģ�ͺ�����ģ�Ͳ���ʹ�ã�����ʹ��ͬһ��ģ�ͣ�
	 * ����ģ�����������ģ��ԭ�����ݣ������Ͳ���Ҫ����ģ��ת��Ϊ����ģ�͵Ĺ����ˣ�
	 * @param dataModel
	 * @return
	 */
	public static CellsModel convertDataModelToLWModel(CellsModel dataModel) {
		//������Խ���Ч���Ż���������¼������󣬵ڶ��δ��ݵ�appletǰ������Ҫ�ٴν���ת����
		//ת������ؼ���
		KeywordModel keywordModel = CellsModelOperator
				.getKeywordModel(dataModel);
		HashMap mainKeyVOs = keywordModel.getMainKeyVOPos();
		for (Iterator iterator = mainKeyVOs.keySet().iterator(); iterator
				.hasNext();) {
			KeyVO mainKeyVO = (KeyVO) iterator.next();
			CellPosition cellPos = (CellPosition) mainKeyVOs.get(mainKeyVO);
			if (cellPos != null) {//�����عؼ���
				cellPos = (CellPosition) DynAreaCell.getRealArea(cellPos,
						dataModel);
				KeyFmt keyFmt = convertKeyVO2KeyFmt(mainKeyVO, false);
				dataModel.setBsFormat(cellPos, KeyFmt.EXT_FMT_KEYINPUT, keyFmt);
				convertToIDName(dataModel, keyFmt, cellPos);
			}
		}
		//ת������ָ��		
		MeasureModel measureModel = CellsModelOperator
				.getMeasureModel(dataModel);
		Hashtable mainMeasureVOs = measureModel.getMainMeasureVOPos();
		for (Enumeration enumeration = mainMeasureVOs.keys(); enumeration
				.hasMoreElements();) {
			CellPosition cellPos = (CellPosition) enumeration.nextElement();
			MeasureVO measureVO = (MeasureVO) mainMeasureVOs.get(cellPos);
			cellPos = (CellPosition) DynAreaCell
					.getRealArea(cellPos, dataModel);
			MeasureFmt measureFmt = convertMeasureVO2MeasureFmt(measureVO);
			dataModel.setBsFormat(cellPos, MeasureFmt.EXT_FMT_MEASUREINPUT,
					measureFmt);
			convertToIDName(dataModel, measureFmt, cellPos);
		}
		//ת����̬��
		DynAreaCell[] dynAreaCells = DynAreaModel.getInstance(dataModel)
				.getDynAreaCells();
		for (int i = 0; i < dynAreaCells.length; i++) {
			DynAreaCell dynAreaCell = dynAreaCells[i];
			//���ݴ����ڸ��Ƶ�Ԫ��ʽʱת����
			if (DynAreaModel.getInstance(dataModel).isProcessed(
					dynAreaCell.getDynAreaPK())) {
				continue;
			}
			AreaPosition[] unitAreas = dynAreaCell.getUnitAreas();
			AreaPosition oriArea = dynAreaCell.getDynAreaVO().getOriArea();
			for (int dRow = 0; dRow <= oriArea.getHeigth(); dRow++) {
				for (int dCol = 0; dCol <= oriArea.getWidth(); dCol++) {
					CellPosition cellPos = (CellPosition) oriArea.getStart()
							.getMoveArea(dRow, dCol);
					String fmtType = null;
					Object fmtObj = null;
					MeasureVO measureVO = measureModel
							.getDynAreaMeasureVOByPos(dynAreaCell
									.getDynAreaVO().getDynamicAreaPK(), cellPos);
					if (measureVO != null) {
						fmtType = MeasureFmt.EXT_FMT_MEASUREINPUT;
						fmtObj = convertMeasureVO2MeasureFmt(measureVO);
					} else {
						KeyVO keyVO = keywordModel.getKeyVOByPos(cellPos);
						if (keyVO != null) {
							fmtType = KeyFmt.EXT_FMT_KEYINPUT;
							fmtObj = convertKeyVO2KeyFmt(keyVO, true);
						}
					}
					if (fmtType != null && fmtObj != null) {
						for (int j = 0; j < unitAreas.length; j++) {
							AreaPosition unitArea = unitAreas[j];
							CellPosition unitCellPos = (CellPosition) unitArea
									.getStart().getMoveArea(dRow, dCol);
							dataModel.setBsFormat(unitCellPos, fmtType, fmtObj);
							convertToIDName(dataModel, fmtObj, unitCellPos);
						}
					}
				}
			}

		}
		//ת����ʽ��
		Hashtable<IArea, FormulaVO> instanceFormulas = getFormulaModel(
				dataModel).getFormulaAllByType(true);
		Hashtable<IArea, FormulaVO> totalFormulas = getFormulaModel(dataModel)
				.getFormulaAllByType(false);

		//modify by ljhua 2007-3-16 ���ӹ�ʽ����
		String strFmlContent = null;
		for (IArea area : instanceFormulas.keySet()) {
			strFmlContent = instanceFormulas.get(area).getFormulaContent();
			area = DynAreaCell.getRealArea(area, dataModel);
			List<CellPosition> poss = dataModel.getSeperateCellPos(area);
			for (CellPosition pos : poss) {
				/**
				 * modify by chxw 2007-4-17
				 * ����¼��״̬����̬���򲻽���һ�е�Ԫ����Ҫ���ù�ʽ���ԣ�����������ҲӦ���ø�����
				 * �㷨�����ݶ�̬�����ʽ��ƵĹ�ʽ���ԣ��������һ�����ݵĹ�ʽ���ڵ�Ԫ�����ù�ʽ����
				 */
				ArrayList<IArea> extFormulaArea = null;
				DynAreaCell dynAreaCell = DynAreaModel.getInstance(dataModel)
						.getDynAreaCellByPos(pos);
				if (dynAreaCell != null) {
					extFormulaArea = getExtendFormulaPos(dataModel, pos);
				} else {
					extFormulaArea = dataModel.seperateArea(pos);
				}

				if (extFormulaArea == null || extFormulaArea.size() == 0) {
					continue;
				}

				for (IArea tmpPos : extFormulaArea) {
					CellPosition realFmlPos = null;
					FormulaFmt formulaFmt = null;
					if (area.isCell() == false) { //��������ʽ,��ʽֻ��¼���׵�Ԫ��
						if (tmpPos.equals(area.getStart())) {
							realFmlPos = area.getStart();
							formulaFmt = new FormulaFmt(strFmlContent, area
									.toString(), false);
						} else {
							realFmlPos = tmpPos.getStart();
							formulaFmt = new FormulaFmt();
						}
					} else {
						realFmlPos = tmpPos.getStart();
						formulaFmt = new FormulaFmt(strFmlContent, null, true);
					}
					//������չʵ��λ�ù�ʽ����
					dataModel.setBsFormat(realFmlPos,
							FormulaFmt.EXT_FMT_FORMULAINPUT, formulaFmt);
				}
			}
		}
		for (IArea area : totalFormulas.keySet()) {
			List<CellPosition> poss = dataModel.getSeperateCellPos(area);
			for (CellPosition pos : poss) {
				dataModel.setBsFormat(pos, FormulaFmt.EXT_FMT_FORMULAINPUT,
						new FormulaFmt());//���﹫ʽ����Ҫ��ϸ��Ϣ��
			}
		}

		//modify by chxw 2007-05-15 ��������������ģ�ͱ��
		dataModel.setLWModel(true);
		return dataModel;
	}

	/**
	 * ����̬������ģ��ת��Ϊ������ģ��
	 * ע�⣺�� convertDataModelToLWModel ��������ʱ�����ݴ����ڸ��Ƶ�Ԫ��ʽʱת����isConvertDataProcessΪfalse
	 * @param dataModel
	 * @param isConvertDataProcess �Ƿ�ת�����ݴ���
	 * @return
	 */
	public static CellsModel convertDynAreaModelToLWModel(CellsModel dataModel,
			boolean isConvertDataProcess) {
		KeywordModel keywordModel = CellsModelOperator
				.getKeywordModel(dataModel);
		MeasureModel measureModel = CellsModelOperator
				.getMeasureModel(dataModel);
		DynAreaCell[] dynAreaCells = DynAreaModel.getInstance(dataModel)
				.getDynAreaCells();
		for (int i = 0; i < dynAreaCells.length; i++) {
			DynAreaCell dynAreaCell = dynAreaCells[i];
			if (!isConvertDataProcess
					&& DynAreaModel.getInstance(dataModel).isProcessed(
							dynAreaCell.getDynAreaPK())) {
				continue;
			}
			AreaPosition[] unitAreas = dynAreaCell.getUnitAreas();
			AreaPosition oriArea = dynAreaCell.getDynAreaVO().getOriArea();
			for (int dRow = 0; dRow <= oriArea.getHeigth(); dRow++) {
				for (int dCol = 0; dCol <= oriArea.getWidth(); dCol++) {
					CellPosition cellPos = (CellPosition) oriArea.getStart()
							.getMoveArea(dRow, dCol);
					String fmtType = null;
					Object fmtObj = null;
					MeasureVO measureVO = measureModel
							.getDynAreaMeasureVOByPos(dynAreaCell
									.getDynAreaVO().getDynamicAreaPK(), cellPos);
					if (measureVO != null) {
						fmtType = MeasureFmt.EXT_FMT_MEASUREINPUT;
						fmtObj = convertMeasureVO2MeasureFmt(measureVO);
					} else {
						KeyVO keyVO = keywordModel.getKeyVOByPos(cellPos);
						if (keyVO != null) {
							fmtType = KeyFmt.EXT_FMT_KEYINPUT;
							fmtObj = convertKeyVO2KeyFmt(keyVO, true);
						}
					}
					if (fmtType != null && fmtObj != null) {
						for (int j = 0; j < unitAreas.length; j++) {
							AreaPosition unitArea = unitAreas[j];
							CellPosition unitCellPos = (CellPosition) unitArea
									.getStart().getMoveArea(dRow, dCol);
							dataModel.setBsFormat(unitCellPos, fmtType, fmtObj);
							convertToIDName(dataModel, fmtObj, unitCellPos);
						}
					}
				}
			}

		}
		return dataModel;
	}

	private static ArrayList<IArea> getExtendFormulaPos(CellsModel dataModel,
			CellPosition pos) {
		DynAreaCell dynAreaCell = DynAreaModel.getInstance(dataModel)
				.getDynAreaCellByPos(pos);
		if (dynAreaCell == null) {
			return null;
		}

		AreaPosition oriArea = dynAreaCell.getDynAreaVO().getOriArea();
		AreaPosition[] unitAreas = dynAreaCell.getUnitAreas();
		if (unitAreas == null || !oriArea.contain(pos)
				|| !unitAreas[0].contain(pos)) {
			return null;
		}

		int row = pos.getRow() - oriArea.getStart().getRow();
		int col = pos.getColumn() - oriArea.getStart().getColumn();
		ArrayList<IArea> extFormulaPos = new ArrayList<IArea>();
		for (int i = 0; i < unitAreas.length; i++) {
			CellPosition cellPos = null;
			if (dynAreaCell.getDirection() == DynAreaVO.DIRECTION_COL) {
				cellPos = CellPosition.getInstance(pos.getRow(), unitAreas[i]
						.getStart().getColumn()
						+ col);
			} else {
				cellPos = CellPosition.getInstance(unitAreas[i].getStart()
						.getRow()
						+ row, pos.getColumn());
			}
			extFormulaPos.add(cellPos);
		}
		return extFormulaPos;
	}

	private static void convertToIDName(CellsModel dataModel, Object fmtObj,
			CellPosition cellPos) {
		Object value = dataModel.getCellValue(cellPos);
		if (value == null || value.toString() == null
				|| value.toString().equals("")) {
			return;
		}
		String codePK = null;
		if (fmtObj instanceof KeyFmt) {
			codePK = ((KeyFmt) fmtObj).getRefCodePK();
		} else if (fmtObj instanceof MeasureFmt) {
			codePK = ((MeasureFmt) fmtObj).getRefCodePK();
		} else {
			throw new RuntimeException();
		}
		try {
			if (fmtObj instanceof KeyFmt && KeyFmt.isUnitKey((KeyFmt) fmtObj)) {
				String unitPK = (String) value;
				UnitCache cache = CacheProxy.getSingleton().getUnitCache();
				UnitInfoVO infoVO = cache.getUnitInfoByPK(unitPK);
				if (infoVO == null) {
					infoVO = cache.getUnitInfoByCode(unitPK);
				}
				dataModel.setCellValue(cellPos, new IDName(infoVO.getCode(),
						infoVO.getUnitName()));
			} else {
				if (codePK == null) {
					return;
				}
				CodeCache cache = IUFOBSCacheManager.getSingleton()
						.getCodeCache();
				CodeVO code = cache.findCodeByID(codePK);
				String name = cache.findCodeInfoByID(code, value.toString())
						.getContent();
				dataModel.setCellValue(cellPos, new IDName(value.toString(),
						name));
			}
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace();
		}
	}

	public static MeasureFmt convertMeasureVO2MeasureFmt(MeasureVO measureVO) {
		int nMeasFmtType = toExtMeasFmtType(measureVO.getType());
		//		CellPosition formatCellPos = getFormatCellPos(cellPropertyEx);
		return new MeasureFmt(measureVO.getCode(), nMeasFmtType, measureVO
				.getRefPK(), -1, -1,//formatCellPos.getRow(), formatCellPos.getColumn(),
				measureVO.getLen());
	}

	/**
	 * ת��Ϊ���ؼ���չ��ʽָ�������
	 * 
	 * @param nCellPropertyMeasType
	 * @return
	 */
	private static int toExtMeasFmtType(int nCellPropertyMeasType) {
		int nMeasFmtType;//OPTIMIZE:change to give "default" value
		switch (nCellPropertyMeasType) {
		case MeasureVO.TYPE_CHAR:
			nMeasFmtType = MeasureFmt.TYPE_CHAR;
			break;
		case MeasureVO.TYPE_CODE:
			nMeasFmtType = MeasureFmt.TYPE_REF_CODE;
			break;
		case MeasureVO.TYPE_NUMBER:
			nMeasFmtType = MeasureFmt.TYPE_NUMBER;
			break;
		case MeasureVO.TYPE_DATE:
			nMeasFmtType = MeasureFmt.TYPE_REF_DATE;
			break;
		default:
			nMeasFmtType = MeasureFmt.TYPE_CHAR;
			break;
		}
		return nMeasFmtType;
	}

	public static KeyFmt convertKeyVO2KeyFmt(KeyVO keyVO, boolean bInDynArea) {
		int nKeyFmtType = toExtKeyFmtType(keyVO.getType(), KeyVO
				.isUnitKeyVO(keyVO), KeyVO.isDicUnitKeyVO(keyVO));
		String strKeyName = keyVO.getName();
		//ʱ��ؼ���ʱ����ҪתΪ�����ڡ�����
		if (nKeyFmtType == KeyFmt.TYPE_TIME) {
			strKeyName = StringResource.getStringResource("miufo1003135");//����
		}
		//		CellPosition formatCellPos = getFormatCellPos(cellPropertyEx);//������Ҫ��Щ���ԡ�
		return new KeyFmt(keyVO.getKeywordPK(), strKeyName, nKeyFmtType, keyVO
				.getRef(), -1,//formatCellPos.getRow(),
				-1,//formatCellPos.getColumn(),
				bInDynArea, keyVO.getLen(), keyVO.getTimeProperty());

	}

	/**
	 * ת��Ϊ���ؼ���չ��ʽ�ؼ��ֵ�����
	 * 
	 * @param nCellPropertyKeyType
	 * @param bUnitKey �Ƿ��ǵ�λ�ؼ���
	 * @return
	 */
	private static int toExtKeyFmtType(int nCellPropertyKeyType,
			boolean bUnitKey, boolean bDicUnitKey) {
		if (bUnitKey)
			return KeyFmt.TYPE_UNIT;
		if (bDicUnitKey)
			return KeyFmt.TYPE_DIC_UNIT;

		switch (nCellPropertyKeyType) {
		case KeyVO.TYPE_CHAR:
			return KeyFmt.TYPE_CHAR;
		case KeyVO.TYPE_REF:
			return KeyFmt.TYPE_CODE;
		case KeyVO.TYPE_TIME:
			return KeyFmt.TYPE_TIME;
		default:
			return KeyFmt.TYPE_CHAR;
		}
	}

	/**
	 * ������ģ���б������ݵ����ݿ⡣
	 * @param dataModel
	 * @param context
	 * @throws Exception 
	 */
	public static void saveDataToDB(CellsModel dataModel, UfoContextVO context,
			Hashtable<String, Vector<String>> hashDynAloneID) throws Exception {
		String strReportPK = context.getContextId();
		String strKeyGPk = context.getKeyGroupPK();
		RepDataVO repData = new RepDataVO(strReportPK, strKeyGPk);
		//�õ��������ݴ洢��Ϣ
		MeasurePubDataVO mainPubData = context.getPubDataVO();
		MeasureDataVO[] mainData = getMainMeasureDatas(dataModel, context);
		//�õ������ӱ�Ĵ洢��Ϣ
		Hashtable dynData = generateDynDatas(dataModel, context);
		ArrayList listPubdata = new ArrayList();
		ArrayList listData = new ArrayList();
		if (dynData != null && dynData.size() > 0) {
			listPubdata.addAll(dynData.keySet());
			Collection col = dynData.values();
			for (Iterator iter = col.iterator(); iter.hasNext();) {
				MeasureDataVO[] eachMeasureDataVOs = (MeasureDataVO[]) iter
						.next();
				listData.addAll(Arrays.asList(eachMeasureDataVOs));
			}
		}
		if (mainPubData != null) {
			listPubdata.add(mainPubData);
		}
		if (mainData != null) {
			listData.addAll(Arrays.asList(mainData));
		}
		//
		int len = listPubdata.size();
		MeasurePubDataVO[] mpdatas = new MeasurePubDataVO[len];
		listPubdata.toArray(mpdatas);

		len = listData.size();
		MeasureDataVO[] mdatas = new MeasureDataVO[len];
		listData.toArray(mdatas);

		repData.setDatas(mpdatas, mdatas);
		repData.setUserID(context.getCurUserId());
		RepDataBO_Client.createRepData(repData, context.getOrgPK(),
				hashDynAloneID);
	}

	public static MeasureDataVO[] getMainMeasureDatas(CellsModel dataModel,
			UfoContextVO context) {
		MeasurePubDataVO pubData = context.getPubDataVO();
		MeasureModel measureModel = CellsModelOperator
				.getMeasureModel(dataModel);
		Hashtable mainMeasurePosVO = measureModel.getMainMeasureVOPos();
		MeasureDataVO[] mainMeasureDataVOs = new MeasureDataVO[mainMeasurePosVO
				.size()];
		int i = 0;
		for (Enumeration enumeration = mainMeasurePosVO.keys(); enumeration
				.hasMoreElements(); i++) {
			CellPosition cellPos = (CellPosition) enumeration.nextElement();
			MeasureVO measureVO = (MeasureVO) mainMeasurePosVO.get(cellPos);
			MeasureDataVO each = mainMeasureDataVOs[i] = new MeasureDataVO();
			each.setAloneID(pubData.getAloneID());

			each.setMeasureVO(measureVO);
			if (pubData.getKeyGroup() != null) {
				each.setPrvtKeyGroupPK(pubData.getKeyGroup().getKeyGroupPK());
			}
			cellPos = (CellPosition) DynAreaCell
					.getRealArea(cellPos, dataModel);
			Object value = dataModel.getCellValue(cellPos);
			if (value != null) {
				each.setDataValue(value.toString());
			}
		}
		return mainMeasureDataVOs;
	}

	private static Hashtable generateDynDatas(CellsModel dataModel,
			UfoContextVO context) throws Exception{
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(dataModel);
		DynAreaCell[] dynAreaCells = dynAreaModel.getDynAreaCells();
		Hashtable rtn = new Hashtable();
		for (int i = 0; i < dynAreaCells.length; i++) {
			Hashtable hashMdatas = generateDynData(dataModel, context,
					dynAreaModel, dynAreaCells[i], true);
			for (Enumeration enumeration = hashMdatas.keys(); enumeration
					.hasMoreElements();) {
				Object keyObj = enumeration.nextElement();
				if (rtn.get(keyObj) == null) {
					rtn.put(keyObj, hashMdatas.get(keyObj));
				} else {
					MeasureDataVO[] values1 = (MeasureDataVO[]) rtn.get(keyObj);
					MeasureDataVO[] values2 = (MeasureDataVO[]) hashMdatas
							.get(keyObj);
					MeasureDataVO[] valuesTotal = new MeasureDataVO[values1.length
							+ values2.length];
					System
							.arraycopy(values1, 0, valuesTotal, 0,
									values1.length);
					System.arraycopy(values2, 0, valuesTotal, values1.length,
							values2.length);
					rtn.put(keyObj, valuesTotal);
				}
			}

		}
		return rtn;
	}

	private static Hashtable generateDynData(CellsModel dataModel,
			UfoContextVO context, DynAreaModel dynAreaModel,
			DynAreaCell dynAreaCell, boolean bContainMeasData) throws Exception{
		try {
			MeasurePubDataVO mainPubData = context.getPubDataVO();
			//�õ���̬����Ĺؼ����������
			String strKeyCombPK = dynAreaModel.getKeyCompPK(dynAreaCell
					.getDynAreaVO());

			Hashtable hashRepMeasure = new Hashtable();

			Hashtable hashMeasureDatas = getDynMeasureData(dataModel,
					dynAreaCell, dynAreaModel, bContainMeasData);
			if (hashMeasureDatas == null) {
				hashMeasureDatas = new Hashtable();
			}

			//�жϵ�ǰ��̬����Ĺؼ�������
			KeyGroupCache kgCache = CacheProxy.getSingleton()
					.getKeyGroupCache();
			KeyGroupVO keyGroupVO = kgCache.getByPK(strKeyCombPK);
			if (keyGroupVO == null) {
				return null;
			}
			KeyVO[] prvKeyVOs = keyGroupVO.getPrivatekey();
			if (prvKeyVOs != null && prvKeyVOs.length > 0) {
				//��ǰ��̬����Ϊ˽�йؼ�������
				Enumeration keys = hashMeasureDatas.keys();
				while (keys.hasMoreElements()) {
					KeyDataGroup keyDGroup = (KeyDataGroup) keys.nextElement();
					KeyDataVO[] keyDatas = keyDGroup.getKeyDatas();
					Vector vecNewMDataVOs = (Vector) hashMeasureDatas
							.get(keyDGroup);
					if (vecNewMDataVOs != null) {
						boolean bHasKeyVal = true;
						for (int i = 0; i < vecNewMDataVOs.size() && bHasKeyVal; i++) {
							for (int j = 0; j < prvKeyVOs.length && bHasKeyVal; j++) {
								for (int m = 0; m < keyDatas.length; m++) {
									if (prvKeyVOs[j].getName().equals(
											keyDatas[m].getKey().getName())) {
										String strKeyVal = keyDatas[m]
												.getValue();
										if (strKeyVal == null
												|| strKeyVal.trim().length() <= 0) {
											bHasKeyVal = false;
											break;
										}
										MeasureDataVO mDataVO = (MeasureDataVO) vecNewMDataVOs
												.get(i);
										mDataVO.setKeyValueByIndex(j + 1,
												strKeyVal);
										break;
									}
								}
							}
						}
						if (!bHasKeyVal)
							continue;

						Vector vecMdatas = null;
						MeasureDataVO[] mDataVOs = (MeasureDataVO[]) hashRepMeasure
								.get(mainPubData);
						if (mDataVOs != null && mDataVOs.length > 0) {
							vecMdatas = new Vector(Arrays.asList(mDataVOs));
						}
						if (vecMdatas == null) {
							vecMdatas = new Vector();
						}
						for (int i = 0; i < vecNewMDataVOs.size(); i++) {
							MeasureDataVO md = (MeasureDataVO) vecNewMDataVOs
									.get(i);
							md.setAloneID(mainPubData.getAloneID());
							vecMdatas.add(md);
						}
						hashRepMeasure.put(mainPubData, vecMdatas
								.toArray(new MeasureDataVO[0]));
					}
				}
			} else {
				//��ǰ��̬����Ϊ���йؼ�������
				Enumeration keys = hashMeasureDatas.keys();
				while (keys.hasMoreElements()) {
					MeasurePubDataVO subPubData = new MeasurePubDataVO();
					subPubData.setVer(mainPubData.getVer());
					subPubData.setKeyGroup(keyGroupVO);
					subPubData.setKType(keyGroupVO.getKeyGroupPK());
					subPubData.setFormulaID(mainPubData.getFormulaID());
					//������ؼ���ֵ���õ���̬������
					KeyVO[] subKeys = keyGroupVO.getKeys();
					if (keys != null) {
						for (int i = 0; i < subKeys.length; i++) {
							subPubData.setKeywordByName(subKeys[i].getName(),
									mainPubData.getKeywordByName(subKeys[i]
											.getName()));
						}
					}
					KeyDataGroup keyDGroup = (KeyDataGroup) keys.nextElement();
					KeyDataVO[] keyDatas = keyDGroup.getKeyDatas();
					//�Ƚ�����Ĺؼ���ֵ����ؼ�����Ϣ��
					KeyVO[] keyVOs = subPubData.getKeyGroup().getKeys();
					boolean bHasKeyVal = true;
					if (keyVOs != null) {
						for (int m = 0; m < keyDatas.length; m++) {
							String strKeyVal = keyDatas[m].getValue();
							if (strKeyVal == null
									|| strKeyVal.trim().length() <= 0) {
								bHasKeyVal = false;
								break;
							}

							if (keyDatas[m].getKey().getKeywordPK().equals(
									KeyVO.CORP_PK)
									|| keyDatas[m].getKey().getKeywordPK()
											.equals(KeyVO.DIC_CORP_PK)) {
								UnitInfoVO unitInfo = IUFOUICacheManager
										.getSingleton().getUnitCache()
										.getUnitInfoByCode(strKeyVal);
								if (unitInfo != null)
									strKeyVal = unitInfo.getPK();
							}
							subPubData.setKeywordByName(keyDatas[m].getKey()
									.getName(), strKeyVal);
						}
					}
					if (!bHasKeyVal)
						continue;

					subPubData.setAloneID(MeasurePubDataBO_Client
							.getAloneID(subPubData));

					MeasureDataVO[] mDataVOs = (MeasureDataVO[]) ((Vector) hashMeasureDatas
							.get(keyDGroup)).toArray(new MeasureDataVO[0]);
					for (int i = 0; i < mDataVOs.length; i++) {
						mDataVOs[i].setAloneID(subPubData.getAloneID());
					}
					hashRepMeasure.put(subPubData, mDataVOs);
				}
			}
			return hashRepMeasure;
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools         e.printStackTrace();
			throw e;
		}
	}

	/**
	 * ����KeyDataGroup��Vector(MeasureDataVO),����KeyDataGroup����������Ĺؼ��֡�
	 * @param dataModel
	 * @param dynAreaCell 
	 * @param dynAreaModel
	 * @return
	 */
	public static Hashtable<KeyDataGroup, Vector<MeasureDataVO>> getDynMeasureData(
			CellsModel dataModel, DynAreaCell dynAreaCell,
			DynAreaModel dynAreaModel, boolean bContainMeasData) {
		boolean privateKey = isPrivateKey(dataModel, dynAreaCell);
		AreaPosition oriArea = dynAreaCell.getDynAreaVO().getOriArea();
		AreaPosition[] unitAreas = dynAreaCell.getUnitAreas();
		KeywordModel keywordModel = KeywordModel.getInstance(dataModel);
		String keyCombPK = keywordModel.getDynAreaKeyCombPK(dynAreaCell
				.getDynAreaVO().getDynamicAreaPK());
		Hashtable rtn = new Hashtable();
		unitUnitArea: for (int i = 0; i < unitAreas.length; i++) {
			Vector keyDataVOs = new Vector();
			Vector measureDataVOs = new Vector();
			AreaPosition eachUnitArea = unitAreas[i];
			for (int dRow = 0; dRow < oriArea.getHeigth(); dRow++) {
				for (int dCol = 0; dCol < oriArea.getWidth(); dCol++) {
					CellPosition oriPos = (CellPosition) oriArea.getStart()
							.getMoveArea(dRow, dCol);
					CellPosition thePos = (CellPosition) eachUnitArea
							.getStart().getMoveArea(dRow, dCol);
					MeasureVO measureVO = dynAreaModel.getMeasureModel()
							.getMeasureVOByPos(oriPos);
					if (measureVO != null) {
						if (bContainMeasData) {
							MeasureDataVO measureDataVO = new MeasureDataVO();
							measureDataVO.setRowNo(privateKey ? i + 1 : 0);
							measureDataVO.setMeasureVO(measureVO);

							Object value = dataModel.getCellValue(thePos);
							measureDataVO.setDataValue(value == null ? null
									: value.toString());
							measureDataVO.setPrvtKeyGroupPK(keyCombPK);
							measureDataVOs.add(measureDataVO);
						}
					} else {
						KeyVO keyVO = dynAreaModel.getKeywordModel()
								.getKeyVOByPos(oriPos);
						if (keyVO != null) {
							KeyDataVO keyDataVO = new KeyDataVO();
							keyDataVO.setKey(keyVO);
							Object value = dataModel.getCellValue(thePos);
							if (value == null || "".equals(""+value)) {
								continue unitUnitArea;
							}
							
//							if (keyVO.getKeywordPK().equals(KeyVO.CORP_PK) || keyVO.getKeywordPK().equals(KeyVO.DIC_CORP_PK)){
//								String strValue=value.toString();
//								UnitInfoVO unitInfo=IUFOUICacheManager.getSingleton().getUnitCache().getUnitInfoByCode(strValue);
//								if (unitInfo!=null)
//									value=unitInfo.getPK();
//							}
							
							keyDataVO.setValue(value.toString());
							keyDataVOs.add(keyDataVO);
						}
					}
				}
			}
			KeyDataGroup keyDataGroup = new KeyDataGroup();
			keyDataGroup.setKeyDatas((KeyDataVO[]) keyDataVOs
					.toArray(new KeyDataVO[0]));
			rtn.put(keyDataGroup, measureDataVOs);
		}

		return rtn;
	}

	private static boolean isPrivateKey(CellsModel dataModel,
			DynAreaCell dynAreaCell) {
		KeywordModel keywordModel = KeywordModel.getInstance(dataModel);
		String dynAreaPK = dynAreaCell.getDynAreaPK();
		KeyVO[] dynKeys = keywordModel.getDynKeyVOs(dynAreaPK);
		if (dynKeys.length > 0 && dynKeys[0].isPrivate()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �ô����ʽ��ϢnewFormatModel���滻����ĵ�λstrUnitId���Ի���Ϣ��
	 * �������ڣ�(2004-4-26 16:35:51)
	 * @author������
	 * @param strUnitId
	 * @param formatModel
	 * @param findFormatModel
	 */
	public static void replaceUnitPersonalSet(String strUnitId,
			CellsModel fromModel, CellsModel toModel) {
		if (fromModel == null)
			return;
		FormulaModel fromFormulaModel = getFormulaModel(fromModel);
		FormulaModel toFormulaModel = getFormulaModel(toModel);
		toFormulaModel.setUnitPersonalFormula(strUnitId, fromFormulaModel
				.getUnitPersonalFormula(strUnitId));
		Hashtable fromPrintSetHT = getUnitPrintSet(fromModel);
		Hashtable toPrintSetHT = getUnitPrintSet(toModel);
		if (fromPrintSetHT.get(strUnitId) != null) {
			toPrintSetHT.put(strUnitId, fromPrintSetHT.get(strUnitId));
		}
	}

	/**
	 * �ӱ������ģ��(CellsModel)���ָ�����ģ��(MeasureModel)��
	 * @param �������ģ��CellsModel
	 * @return ָ�����ģ��MeasureModel
	 */
	public static MeasureModel getMeasureModel(CellsModel cellsModel) {
		return MeasureModel.getInstance(cellsModel);
	}

	private static CellPosition getFixAreaMeasurePos(CellsModel cellsModel,
			String measurePK) {
		DynAreaModel dynAreaModel = getDynAreaModel(cellsModel);
		MeasureModel measureModel = getMeasureModel(cellsModel);

		CellPosition measCellPos = measureModel.getMeasurePosByPK(measurePK);
		if (dynAreaModel == null || dynAreaModel.getDynAreaSize() <= 0)
			return measCellPos;

		if (!cellsModel.isLWModel()) {
			return DynAreaCell.getRealArea(
					AreaPosition.getInstance(measCellPos, measCellPos),
					cellsModel).getStart();
		}

		int iDirect = dynAreaModel.getDynAreaDirection();
		int iMaxRow = 0;
		int iMaxCol = 0;

		if (iDirect == DynAreaVO.DIRECTION_ROW) {
			iMaxRow = cellsModel.getMaxRow();
			iMaxCol = cellsModel.getMaxCol();
		} else {
			iMaxCol = cellsModel.getMaxRow();
			iMaxRow = cellsModel.getMaxCol();
		}

		for (int iRow = 0; iRow <= iMaxRow + 1; iRow++) {
			for (int iCol = 0; iCol <= iMaxCol + 1; iCol++) {
				CellPosition viewCell = null;

				if (iDirect == DynAreaVO.DIRECTION_ROW) {
					viewCell = CellPosition.getInstance(iRow, iCol);
				} else {
					viewCell = CellPosition.getInstance(iCol, iRow);
				}

				DynAreaCell[] dynCells = dynAreaModel.getDynAreaCells();
				int k = 0;
				for (k = 0; k < dynCells.length; k++) {
					DynAreaCell dynCell = dynCells[k];
					if (dynCell.getArea().contain(viewCell)) {
						break;
					}
				}
				if (k < dynCells.length)
					break;

				Cell cell = cellsModel.getCell(viewCell);
				MeasureFmt measFmt = (MeasureFmt) cell
						.getExtFmt(MeasureFmt.EXT_FMT_MEASUREINPUT);
				if (measFmt != null && measFmt.getCode().equals(measurePK)) {
					return viewCell;
				}
			}
		}

		return null;
	}

	/**
	 * ��ȡ����ģ����ָ������λ�á���������ָ��Ͷ�̬��ָ�ꡣ
	 * 2007-10-25
	 * @param cellsModel  ����̬����ģ��
	 * @param measurePK   ָ��PK
	 * @param kg   �ؼ�ֵ֮
	 * @return
	 */
	public static CellPosition getMeasureDataPos(String strReportPK,
			CellsModel cellsModel, String measurePK, String[] keyvalues) {
		MeasureModel measureModel = getMeasureModel(cellsModel);
		String strDynPK = measureModel.getDynPKByMeasurePK(measurePK);

		//����
		if (strDynPK == null || MeasureModel.MAINTABLE_DYNPK.equals(strDynPK)) {
			return getFixAreaMeasurePos(cellsModel, measurePK);
		}

		ReportVO report = IUFOUICacheManager.getSingleton().getReportCache()
				.getByPK(strReportPK);
		KeyGroupVO mainKeyGroup = IUFOUICacheManager.getSingleton()
				.getKeyGroupCache().getByPK(report.getKeyCombPK());

		MeasureVO measure = IUFOUICacheManager.getSingleton().getMeasureCache()
				.getMeasure(measurePK);
		KeyVO[] measKeys = IUFOUICacheManager.getSingleton().getKeyGroupCache()
				.getByPK(measure.getKeyCombPK()).getKeys();

		Vector<KeyVO> vKeyVO = new Vector<KeyVO>();
		for (int i = 0; i < measKeys.length; i++) {
			if (mainKeyGroup.containsKey(measKeys[i]) == false)
				vKeyVO.add(measKeys[i]);
		}

		//��̬��
		DynAreaModel dynAreaModel = getDynAreaModel(cellsModel);
		DynAreaCell dynCell = dynAreaModel.getDynAreaCellByPK(strDynPK);

		AreaPosition[] areas = dynCell.getUnitAreas();

		AreaPosition areaPosition = null;
		for (int i = 0; i < areas.length; i++) {
			areaPosition = areas[i];
			ArrayList<CellPosition> listCellPos = cellsModel
					.getSeperateCellPos(areaPosition);

			CellPosition pos = null;
			boolean justit = true;
			for (int ii = 0; ii < listCellPos.size(); ii++) {
				CellPosition eachPos = (CellPosition) listCellPos.get(ii);
				CellPosition formatPos = (CellPosition) DynAreaCell
						.getFormatArea(eachPos, cellsModel);
				MeasureVO measureVO = dynAreaModel.getMeasureModel()
						.getMeasureVOByPos(formatPos);
				KeyVO key = dynAreaModel.getKeywordModel().getKeyVOByPos(
						formatPos);

				if (measureVO != null && pos == null) {
					if (measurePK.equals(measureVO.getCode())) {
						pos = (CellPosition) listCellPos.get(ii);
					}
				} else if (key != null) {
					Object value = null;
					Cell cell = cellsModel.getCell(eachPos);
					if (cell != null) {
						value = cell.getValue();
					}
					if (value != null && value instanceof IDName) {
						value = ((IDName) value).getID();
					}
					if (keyvalues[vKeyVO.indexOf(key)] != null && !keyvalues[vKeyVO.indexOf(key)].equals(value)) {
						justit = false;
						break;
					}
				}
			}
			if (justit == true) {
				return pos;
			}
		}

		return null;
	}

	public static DynAreaModel getDynAreaModel(CellsModel cellsModel) {
		return DynAreaModel.getInstance(cellsModel);
	}

	/**
	 * ��CellsModel�л��KeywordModel��
	 * @param cellsModel
	 * @return 
	 */
	public static KeywordModel getKeywordModel(CellsModel cellsModel) {
		return KeywordModel.getInstance(cellsModel);
	}

	/**
	 * ��CellsModel�л��FormulaModel��
	 * @param cellsModel
	 * @return
	 */
	public static FormulaModel getFormulaModel(CellsModel cellsModel) {
		return FormulaModel.getInstance(cellsModel);
	}

	/**
	 * ��CellsModel�л��߸��Ի���ӡ����Hashtable(unitID-PrintSet)
	 * @param cellsModel
	 * @return
	 */
	public static Hashtable getUnitPrintSet(CellsModel cellsModel) {
		Hashtable htPrintSet = (Hashtable) cellsModel
				.getExtProp(PrintSet.class);
		if (htPrintSet == null) {
			htPrintSet = new Hashtable();
			cellsModel.putExtProp(PrintSet.class, htPrintSet);
		}
		return htPrintSet;
	}

	/**
	 * ����CellsModel����ʱ��ӡ����Ϊָ����λ�ĸ��Ի���ӡ������
	 * @param cellsModel
	 * @param strUnitID
	 */
	public static void setPrintSetup(CellsModel cellsModel, String strUnitID) {
		PrintSet printSet = cellsModel.getPrintSet();
		if (strUnitID != null && printSet != null)
			CellsModelOperator.getUnitPrintSet(cellsModel).put(strUnitID,
					printSet);
	}

	/**
	 * �õ������е�ҵ���ѯ
	 * @param cellsModel
	 * @return
	 */
	public static ReportBusinessQuery getReportBusinessQuery(
			CellsModel cellsModel) {
		return (ReportBusinessQuery) cellsModel
				.getExtProp(ReportBusinessQuery.class);
	}

	/**
	 * ���ñ����е�ҵ���ѯ
	 * @param cellsModel
	 * @param query
	 */
	public static void setReportBusinessQuery(CellsModel cellsModel,
			ReportBusinessQuery query) {
		cellsModel.putExtProp(ReportBusinessQuery.class, query);
	}

	/**
	 * ���汨���ʽ��
	 * @param contextVO
	 * @param formatModel
	 */
	public static boolean saveReportFormat(UfoContextVO context,
			CellsModel cellsModel) {
		return saveReportFormatImpl(context, cellsModel);
	}

	private static boolean saveReportFormatImpl(UfoContextVO context,
			CellsModel cellsModel) {
		try {
			//������ʱ��ӡ������Ϣ�����Ի���ӡ������
			setPrintSetup(cellsModel, context.getCurUnitId());
			//�ж��Ƿ��ж�̬����ؼ���Ϊ��
			CellsModel formatModel = cellsModel;
			KeywordModel keyModel = getKeywordModel(formatModel);
			KeyVO[] mainKeys = keyModel.getMainKeyVOs();
			KeyVO vo = null;
			boolean isYearKeyVO = true;
			for (int n = 0; n < mainKeys.length; n++) {
				vo = mainKeys[n];
				if (!vo.getTimeProperty().equals(
						nc.vo.iufo.pub.date.UFODate.NONE_PERIOD)) {
					if (vo.getKeywordPK().equals(KeyVO.YEAR_PK))
						isYearKeyVO = true;
					else
						isYearKeyVO = false;
				}
			}

			DynAreaModel dynAreaModel = DynAreaModel.getInstance(formatModel);
			DynAreaVO[] dynAreaVOs = dynAreaModel.getDynAreaVOs();
			for (int i = 0; i < dynAreaVOs.length; i++) {
				DynAreaVO dynAreaVO = dynAreaVOs[i];
				KeyVO[] dynAreaKeyVOs = keyModel.getDynKeyVOs(dynAreaVO
						.getDynamicAreaPK());
				if (dynAreaKeyVOs.length == 0) {
					//					 UfoPublic.setPercentBar(100);
					//					 UfoPublic.setPercentBar(-1);
					throw new RuntimeException(StringResource
							.getStringResource("miufo1000737")); //"��̬����Ĺؼ��ֲ���Ϊ��"
				}
				for (int j = 0; j < dynAreaKeyVOs.length; j++) {
					KeyVO keyVO = dynAreaKeyVOs[j];
					if (keyVO.getKeywordPK().equals(KeyVO.WEEK_PK)
							&& !isYearKeyVO) {
						throw new RuntimeException(StringResource
								.getStringResource("miufo1000738")); //"��̬����ʱ��ؼ���Ϊ��ʱ������ʱ��ؼ��ֱ���Ϊ��"
					}
				}
			}
			saveReportFormatImpl2(context, cellsModel);
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace();				
			throw new CommonException("miufo1000739", new String[] { e
					.getMessage() }); //"���������" + e.getMessage()
		} finally {
			//			 UfoPublic.setPercentBar(100);
			//			 UfoPublic.setPercentBar(-1);
		}
		return true;
	}

	/**
	 * ���������еĸ�ʽ��Ϣ����Ϊiufo2.0��ʽ����TableFormatVO[]��
	 * �������ڣ�(2002-3-7 14:31:55)
	 * @param uTable com.ufsoft.iuforeport.reporttool.table.UfoTable
	 */
	private static void saveReportFormatImpl2(UfoContextVO context,
			CellsModel formatModel) throws Exception {

		ReportCache reportCache = CacheProxy.getSingleton().getReportCache();
		ReportVO[] repVOs = reportCache.getByPks(new String[] { context
				.getContextId() });
		if (repVOs != null && repVOs.length > 0) {
			if (repVOs[0] == null) {
				throw new RuntimeException(StringResource
						.getStringResource("miufo1000747")); //"�����޷����档���ܱ����Ѿ���ɾ������ȡ������"
			}
		}

		if (repVOs == null || repVOs.length < 1) {
			return;
		}

		RepFormatModelCache formatCache = CacheProxy.getSingleton()
				.getRepFormatCache();
		if (context.getFormatRight() == UfoContextVO.RIGHT_FORMAT_WRITE) {
			//��ȫ����        	           
			MeasureModel measureModel = getMeasureModel(formatModel);
			KeywordModel keywordModel = getKeywordModel(formatModel);
			//liuyy. 2005-1-17 formatModel.setMainMeasurePackPK(context.getM_strMainMeasurePackPK());

			RepFormatModel repFmtModel = new RepFormatModel();
			repFmtModel.setFormatModel(formatModel);
			repFmtModel.setReportPK(context.getContextId());

			repFmtModel.setMainKeyCombPK(context.getKeyGroupPK());
			Hashtable hashReturn = formatCache.updateRepFormat(repFmtModel, "");

			//add by ljhua 2006-9-5 ���applet��ָ��ģ�ͼ��ؼ���ģ���е���ʱ����
			measureModel.clearMeasureVOsLocal();
			keywordModel.clearKeyVOsLocal();

			if (hashReturn == null) {
				return;
			}

			final String KEY_COMB_PK = "KeyCombPK";
			final String DB_TABLE_NAMES = "DBTableNames";
			final String DB_TABLE = "DBTable";
			final String DB_COLUMN = "DBColumn";
			//��������ؼ������
			context.setKeyGroupPK((String) hashReturn.get(KEY_COMB_PK));
			keywordModel.setMainKeyCombPK((String) hashReturn.get(KEY_COMB_PK));
			//��������ָ�����ݱ���
			measureModel.setMainDbTableNames((Vector) hashReturn
					.get(DB_TABLE_NAMES));

			//��������ָ����¼�¼
			Collection vecMainMeasures = measureModel.getMainMeasureVOPos()
					.values();
			if (vecMainMeasures != null && vecMainMeasures.size() > 0) {
				for (Iterator iterator = vecMainMeasures.iterator(); iterator
						.hasNext();) {
					MeasureVO mvo = (MeasureVO) iterator.next();
					if (mvo == null || mvo.getDbtable() != null) {
						continue;
					}
					String strmpk = mvo.getCode();
					Hashtable hashDBInfo = (Hashtable) hashReturn.get(strmpk);
					if (hashDBInfo == null) {
						continue;
					}
					mvo.setDbtable((String) hashDBInfo.get(DB_TABLE));
					mvo.setDbcolumn((String) hashDBInfo.get(DB_COLUMN));
				}
			}

			//���ö�̬������¼�¼
			DynAreaModel dynAreaModel = getDynAreaModel(formatModel);
			KeywordModel keyModel = getKeywordModel(formatModel);
			DynAreaVO[] vecDyns = dynAreaModel.getDynAreaVOs();
			if (vecDyns != null && vecDyns.length > 0) {
				int nLen = vecDyns.length;
				for (int i = 0; i < nLen; i++) {
					DynAreaVO dynAreaVO = (DynAreaVO) vecDyns[i];
					if (dynAreaVO == null) {
						continue;
					}
					String strDynPK = dynAreaVO.getDynamicAreaPK();
					Hashtable hashDynInfo = (Hashtable) hashReturn
							.get(strDynPK);
					//���ö�̬����ؼ����������
					keyModel.setDynAreaKeyCombPK(dynAreaVO.getDynamicAreaPK(),
							(String) hashDynInfo.get(KEY_COMB_PK));
					//�����ӱ��ָ�����ݱ���
					measureModel.setDynAreaDBTableNames(dynAreaVO
							.getDynamicAreaPK(), (Vector) hashDynInfo
							.get(DB_TABLE_NAMES));
					//�����ӱ�ָ����¼�¼
					Collection vecDynMeasures = measureModel
							.getDynAreaMeasureVOPos(
									dynAreaVO.getDynamicAreaPK()).values();
					for (Iterator iterator = vecDynMeasures.iterator(); iterator
							.hasNext();) {
						MeasureVO mvo = (MeasureVO) iterator.next();
						if (mvo == null || mvo.getDbtable() != null) {
							continue;
						}
						String strmpk = mvo.getCode();
						Hashtable hashDBInfo = (Hashtable) hashReturn
								.get(strmpk);
						if (hashDBInfo == null) {
							continue;
						}
						mvo.setDbtable((String) hashDBInfo.get(DB_TABLE));
						mvo.setDbcolumn((String) hashDBInfo.get(DB_COLUMN));
					}
				}
			}

		} else if (context.getFormatRight() == UfoContextVO.RIGHT_FORMAT_PERSONAL) {
			//���Ի�����
			RepFormatModel personalFormat = formatCache.getFormatByPk(context
					.getContextId());
			CellsModel toFormatModel = personalFormat.getFormatModel();
			replaceUnitPersonalSet(context.getCurUnitId(), formatModel,
					toFormatModel);

			String curUnit = context.getCurUnitId();
			if (context.isAnaRep()) {
				//�������ʱ��Ϊ�˲����и��Ի�����,��ʱ����ǰ��½��λ������������λ
				//liuyy. 2005-3-21  ��ʱ����������λ���óɱ���ǰ��λ//                
				context.setCreateUnitId(curUnit);
			}
			formatCache.updatePersonalRep(curUnit, personalFormat);
		} else {
			//û�б���Ȩ��.
			throw new CommonException(StringResource.getStringResource(
					"miufo1001707", new String[] { context.getReportcode() }));
		}

		if (!context.isOnServer()) {
			IUFOUICacheManager.getSingleton().saveToLocalFiles();
			IUFOUICacheManager.getSingleton().startAllCache();
		}
	}

	public static void copyPersonalPrintset(CellsModel cellsModel,
			String strFromUnitID, String strToUnitID) {
		Hashtable printSets = getUnitPrintSet(cellsModel);
		Object from = printSets.get(strFromUnitID);
		if (from != null) {
			printSets.put(strToUnitID, from);
		} else {
			printSets.remove(strToUnitID);
		}
	}

	public static void removePersonalPrintSet(CellsModel cellsModel,
			String unitID) {
		getUnitPrintSet(cellsModel).remove(unitID);
	}

	public static void setDynAreaData(CellsModel formatModel,
			DynAreaCell dynCell, RepDataVO dataVO, UfoContextVO context) {
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(formatModel);
		DynAreaVO dynVO = dynCell.getDynAreaVO();
		MeasureDataVO[][] mDataVOs = dataVO
				.getMeasureDatasAsMatrix(dynAreaModel.getKeyCompPK(dynVO));
		if (mDataVOs == null || mDataVOs.length <= 0 || mDataVOs[0].length <= 0) {
			return;
		}
		KeyGroupVO kg = IUFOUICacheManager.getSingleton().getKeyGroupCache()
				.getByPK(dynAreaModel.getKeyCompPK(dynVO));
		KeyVO[] privKeys = kg.getPrivatekey();
		ArrayList vPrivKeyPK = new ArrayList();
		if (privKeys != null && privKeys.length > 0) {
			for (int i = 0; i < privKeys.length; i++)
				vPrivKeyPK.add(privKeys[i].getKeywordPK());
		}

		int dataCount = mDataVOs.length;
		/*
		 * modify by guogang 2007-11-9
		 * mDataVOs�Ǹ��ݶ�̬���ؼ����������ȡ�����ݣ���ζ����̬���ؼ�����ͬ����Ȼҵ���߼���û������ģ������ͻ�ȡ��������ͬ�ؼ��ֵĶ�̬����������
		 * ��������䵥����̬�����������������
		 */

		for (int i = 0; i < dataCount; i++) {//��ÿһ��ָ������
			MeasureDataVO[] rowMeasureDataVOs = mDataVOs[i];
			if (rowMeasureDataVOs == null || rowMeasureDataVOs.length <= 0
					|| rowMeasureDataVOs[0] == null)
				continue;

			/*
			 * modify by chxw 2007-07-19
			 * ���ݼ���������չ��������������ټ�鱨�������Ƿ񳬹���ʾģ������������ƣ�
			 * Ϊ�˽�����ݴ���ֻ�Բ���������������⣬��������ʱ��ȫ�����ݼ��ص�����ģ���У�
			 * ���Ѿ������㡢ɸѡ������ģ�͸���������е�����ת������ʾģ��;
			 */
			boolean isRow = dynCell.getDynAreaVO().isRowDirection();
			HeaderModel headerModel = isRow ? formatModel.getRowHeaderModel()
					: formatModel.getColumnHeaderModel();
			if (headerModel.getCount() == headerModel.getMaxNum()) {
				headerModel.setMaxNum(headerModel.getMaxNum() + 1);
			}

			//�Ӹ�ʽ����ȡ��ָ��ؼ�����Ϣ���õ���Ӧ������ֵ��Ȼ������¼�����
			IArea fmtArea = dynCell.getFmtAreaAfterFillData(formatModel);
			IArea addArea = i == 0 ? fmtArea : dynCell.addNewUnit(formatModel);
			ArrayList fmtCellPos = formatModel.getSeperateCellPos(fmtArea);
			ArrayList addCellPos = formatModel.getSeperateCellPos(addArea);
			for (int ii = 0; ii < fmtCellPos.size(); ii++) {
				CellPosition eachPos = (CellPosition) fmtCellPos.get(ii);
				eachPos = (CellPosition) DynAreaCell.getFormatArea(eachPos,
						formatModel);
				MeasureVO measureVO = dynAreaModel.getMeasureModel()
						.getMeasureVOByPos(eachPos);
				if (measureVO != null) {
					Object measureValue = null;
					for (int k = 0; k < rowMeasureDataVOs.length; k++) {
						if (rowMeasureDataVOs[k] != null
								&& measureVO.equals(rowMeasureDataVOs[k]
										.getMeasureVO())) {
							measureValue = rowMeasureDataVOs[k].getDataValue();
							if (measureVO.getType() == MeasureVO.TYPE_NUMBER) {
								measureValue = new Double(measureValue
										.toString());
							}
						}
					}
					formatModel.setCellValue((CellPosition) addCellPos.get(ii),
							measureValue);
				} else {
					KeyVO keyVO = dynAreaModel.getKeywordModel()
							.getDynAreaKeyVOByPos(dynVO.getDynamicAreaPK(),
									eachPos);
					if (keyVO != null) {
						String keyValue = null;
						if (keyVO.isPrivate()) {
							int keyIndex = vPrivKeyPK.indexOf(keyVO
									.getKeywordPK());
							keyValue = rowMeasureDataVOs[0]
									.getKeyValueByIndex(keyIndex + 1);
						} else {
							MeasurePubDataVO tempPubData = dataVO
									.getPubData(rowMeasureDataVOs[0]
											.getAloneID());
							keyValue = tempPubData.getKeywordByName(keyVO
									.getName());
						}
						formatModel.setCellValue((CellPosition) addCellPos
								.get(ii), keyValue);
					}
				}
			}

		}
	}

	public static void applyNewFormatModel2DataModel(CellsModel formatModel,
			CellsModel dataModel) {
		int nRow = dataModel.getRowNum();
		int nCol = dataModel.getColNum();
		for (int r = 0; r < nRow; r++) {
			for (int c = 0; c < nCol; c++) {
				CellPosition cellPos = CellPosition.getInstance(r, c);
				Cell cell = dataModel.getCell(cellPos);
				CellPosition formatPos = (CellPosition) DynAreaCell
						.getFormatArea(cellPos, dataModel);
				Cell fmtCell = formatModel.getCell(formatPos);

				if (fmtCell == null) {
					cell = null;
					continue;
				} else if (fmtCell != null && cell == null) {
					cell = new Cell();
					cell.setRow(r);
					cell.setCol(c);
					dataModel.setCell(r, c, cell);
					cell.setFormat(fmtCell.getFormat());
				}

				if (fmtCell.getExtFmt(MeasureFmt.EXT_FMT_MEASUREINPUT) != null
						&& //ָ��
						cell.getExtFmt(MeasureFmt.EXT_FMT_MEASUREINPUT) == null) {
					cell.setFormat(fmtCell.getFormat());
					cell.setExtFmt(MeasureFmt.EXT_FMT_MEASUREINPUT, fmtCell
							.getExtFmt(MeasureFmt.EXT_FMT_MEASUREINPUT));
				} else if (fmtCell.getExtFmt(MeasureFmt.EXT_FMT_MEASUREINPUT) == null
						&& cell.getExtFmt(MeasureFmt.EXT_FMT_MEASUREINPUT) != null) {
					cell.removeExtFmt(MeasureFmt.EXT_FMT_MEASUREINPUT);
					cell.setValue(null);
				} else if (fmtCell.getExtFmt(FormulaFmt.EXT_FMT_FORMULAINPUT) != null
						&& //��ʽ
						cell.getExtFmt(FormulaFmt.EXT_FMT_FORMULAINPUT) == null) {
					cell.setExtFmt(FormulaFmt.EXT_FMT_FORMULAINPUT, fmtCell
							.getExtFmt(FormulaFmt.EXT_FMT_FORMULAINPUT));
				} else if (fmtCell.getExtFmt(FormulaFmt.EXT_FMT_FORMULAINPUT) == null
						&& cell.getExtFmt(FormulaFmt.EXT_FMT_FORMULAINPUT) != null) {
					cell.removeExtFmt(FormulaFmt.EXT_FMT_FORMULAINPUT);
					cell.setValue(null);
				} else if (fmtCell.getExtFmt(KeyFmt.EXT_FMT_KEYINPUT) != null && //�ؼ���
						cell.getExtFmt(KeyFmt.EXT_FMT_KEYINPUT) == null) {
					cell.setExtFmt(KeyFmt.EXT_FMT_KEYINPUT, fmtCell
							.getExtFmt(KeyFmt.EXT_FMT_KEYINPUT));
				} else if (fmtCell.getExtFmt(KeyFmt.EXT_FMT_KEYINPUT) == null
						&& cell.getExtFmt(KeyFmt.EXT_FMT_KEYINPUT) != null) {
					cell.removeExtFmt(KeyFmt.EXT_FMT_KEYINPUT);
					cell.setValue(null);
				}
			}
		}
	}

	/**
	 * ������ʾģ���з����ݴ���̬������
	 * @param showModel ��ʾģ��
	 * @param dataModel ����ģ��
	 * @param dynAreaCell �����ݴ���̬��
	 * modify by guogang 2007-11-26 �����ǰ�Ķ�̬����ǰһ����̬�������ݴ����ر���ɸѡ��ʱ��dataModel���������ݵĶ�̬��ģ�ͣ�showModel�Ǿ���ǰһ�����ݴ�����޸ĺ�Ķ�̬��ģ��
	 * ��Ӧ����showModel�еĶ�̬��ģ��
	 * @return
	 */
	public static void setDynAreaNotDPData(CellsModel showModel,
			CellsModel dataModel, DynAreaCell dynAreaCell) {
		if (showModel == null || dataModel == null || dynAreaCell == null) {
			return;
		}

		//�޸���ʾģ���е�ǰ��̬��ʵ��λ��
		DynAreaCell dataDynArea = DynAreaModel.getInstance(dataModel)
				.getDynAreaCellByPK(dynAreaCell.getDynAreaPK());
		DynAreaCell showDynArea = DynAreaModel.getInstance(showModel)
				.getDynAreaCellByPK(dynAreaCell.getDynAreaPK());
		//modify by guogang ������ô�޸Ķ�̬��ģ��
		//		showDynArea.setArea(dataDynArea.getArea());
		//��ȡ��̬������ģ�ͺ���ʾģ�͵Ĳ���,���������µĶ�̬��ģ��,����Ҫ���Ǹ�������ָ��ģ��

		DynAreaVO defDynAreaVo = showDynArea.getDynAreaVO();
		//��¼����ģ�Ͷ�̬����Ӧ����ʾģ�Ͷ�̬����ƫ����
		int warp = 0;
		//��¼����ģ���ж�̬������������
		int dataRow = 0;

		AreaPosition dynArea = showDynArea.getArea();
		//����ģ���е�ÿ�����ݵ�����,��ʼ��Ϊ���ݵ�ͷ��
		AreaPosition dataRowArea = null;
		//������ʾģ���еĶ�̬�����ݣ�getArea()�����µ�û��������ݵ���ʼ����
		if (defDynAreaVo.getDirection() == DynAreaVO.DIRECTION_ROW) {
			warp = dataDynArea.getArea().getStart().getRow()
					- showDynArea.getArea().getStart().getRow();
			dataRow = dataDynArea.getArea().getHeigth();
			dataRowArea = AreaPosition.getInstance(dataDynArea.getArea()
					.getStart().getRow(), dataDynArea.getArea().getStart()
					.getColumn(), dataDynArea.getArea().getWidth(), 1);
		} else {
			warp = dataDynArea.getArea().getStart().getColumn()
					- showDynArea.getArea().getStart().getColumn();
			dataRow = dataDynArea.getArea().getWidth();
			dataRowArea = AreaPosition.getInstance(dataDynArea.getArea()
					.getStart().getRow(), dataDynArea.getArea().getStart()
					.getColumn(), 1, dataDynArea.getArea().getHeigth());
		}
		//��������ģ�ͣ���ʾģ���ڷָ̬����ʱ����ܲ���ȷ����������

		for (int j = 0; j < dataRow; j++) {
			//������ģ�͵�ÿ��������չ��ʾģ�͵Ķ�̬����������ݣ�����ָ��Ҳ����Ӧ�ƶ���
			if (j > 0) {
				showDynArea.addNewUnit(showModel);
			}
			//��ȡ����ģ�͵ĸö�̬������ÿ����Ԫ���������䵽��Ӧ����ʾģ����
			ArrayList dynCellPos = dataModel.seperateArea(dataRowArea);
			CellPosition showCellPos = null;
			CellPosition dataCellPos = null;
			for (int i = 0; i < dynCellPos.size(); i++) {
				if (dynCellPos.get(i) instanceof CellPosition) {
					dataCellPos = (CellPosition) dynCellPos.get(i);
				}
				if (dynCellPos.get(i) instanceof AreaPosition) {
					dataCellPos = ((AreaPosition) dynCellPos.get(i)).getStart();
				}
				//modify by guogang showModel�Ķ�̬����Ԫλ�ú�dataModel�еĶ�̬����Ԫλ�ò�ͬ
				if (defDynAreaVo.getDirection() == DynAreaVO.DIRECTION_ROW) {
					showCellPos = CellPosition.getInstance(dataCellPos.getRow()
							- warp, dataCellPos.getColumn());
				} else {
					showCellPos = CellPosition.getInstance(
							dataCellPos.getRow(), dataCellPos.getColumn()
									- warp);
				}
				Cell dataCell = dataModel.getCell(dataCellPos);
				//modify end guogang
				Cell showCell = showModel.getCell(showCellPos);
				if (dataCell != null && showCell == null) {
					showModel.setCell(dataCell.getRow(), dataCell.getCol(),
							dataCell);
					showCell = showModel.getCell(showCellPos);
				}

				if (dataCell != null && dataCell.getValue() != null
						&& showCell != null) {
					showCell.setValue(dataCell.getValue());
				}
			}
			//��ȡ�µ�������
			if (defDynAreaVo.getDirection() == DynAreaVO.DIRECTION_ROW) {
				dataRowArea = (AreaPosition) dataRowArea.getMoveArea(1, 0);
			} else {
				dataRowArea = (AreaPosition) dataRowArea.getMoveArea(0, 1);
			}
		}

	}

	public static List<String> getHasFilterAndCanInputDynAreaPKs(
			CellsModel dataModel) {
		DynAreaModel dynModel = DynAreaModel.getInstance(dataModel);
		KeyGroupCache kgCache = IUFOUICacheManager.getSingleton()
				.getKeyGroupCache();

		DynAreaVO[] dynAreas = null;
		if (dynModel != null)
			dynAreas = dynModel.getDynAreaVOs();

		if (dynAreas == null || dynAreas.length <= 0)
			return null;

		List<String> vRetDynAreaPK = new ArrayList<String>();
		for (int i = 0; i < dynAreas.length; i++) {
			String strDynPK = dynAreas[i].getDynamicAreaPK();

			String strKGPK = dynModel.getKeyCompPK(dynAreas[i]);
			KeyGroupVO keyGroup = kgCache.getByPK(strKGPK);
			if (keyGroup == null)
				continue;

			KeyVO[] privKeys = keyGroup.getPrivatekey();
			if (privKeys != null && privKeys.length > 0)
				continue;

			if (dynModel.isProcessedAndNotSortFilter(strDynPK))
				return null;

			AreaDataProcess process = dynModel.getDataProcess(strDynPK);
			if (process == null)
				continue;

			DataProcessDef def = process.getDataProcessDef();
			if (def != null
					&& def.getDPFilterCond() != null
					&& def.getDPFilterCond().getFilterCond() != null
					&& def.getDPFilterCond().getFilterCond().trim().length() > 0)
				vRetDynAreaPK.add(strDynPK);
		}
		return vRetDynAreaPK;
	}

	/**
	 * ������ģ���б������ݵ����ݿ⡣
	 * @param dataModel
	 * @param context
	 * @throws Exception 
	 */
	public static Hashtable<String, Vector<String>> loadDynDataAloneIDs(
			CellsModel dataModel, UfoContextVO context) throws Exception {
		List<String> vDynPK = getHasFilterAndCanInputDynAreaPKs(dataModel);
		if (vDynPK == null || vDynPK.size() <= 0)
			return null;

		DynAreaModel dynAreaModel = DynAreaModel.getInstance(dataModel);
		DynAreaCell[] dynAreaCells = dynAreaModel.getDynAreaCells();

		Hashtable<String, Vector<String>> hashRetAloneID = new Hashtable<String, Vector<String>>();
		for (int i = 0; i < dynAreaCells.length; i++) {
			if (dynAreaCells[i] == null
					|| !vDynPK.contains(dynAreaCells[i].getDynAreaPK()))
				continue;

			String strKeyCombPK = dynAreaModel.getKeyCompPK(dynAreaModel
					.getDynAreaVOByPK(dynAreaCells[i].getDynAreaPK()));
			Vector<String> vAloneID = null;
			if (hashRetAloneID.get(strKeyCombPK) == null) {
				vAloneID = new Vector<String>();
				hashRetAloneID.put(strKeyCombPK, vAloneID);
			}

			Hashtable hash = generateDynData(dataModel, context, dynAreaModel,
					dynAreaCells[i], false);

			Enumeration enumPubData = hash.keys();
			while (enumPubData.hasMoreElements()) {
				MeasurePubDataVO pubData = (MeasurePubDataVO) enumPubData
						.nextElement();
				if (vAloneID.contains(pubData.getAloneID()) == false)
					vAloneID.add(pubData.getAloneID());
			}
		}

		return hashRetAloneID;
	}

	public static TraceDataScope getTraceDataScope(CellsModel cellsModel,
			CellPosition cell) {
		TraceDataScope scope = new TraceDataScope();
		scope.setCell(cell);

		Cell objCell = cellsModel.getCell(cell);
		if (objCell == null)
			return null;

		MeasureFmt meas = (MeasureFmt) objCell
				.getExtFmt(MeasureFmt.EXT_FMT_MEASUREINPUT);
		if (meas != null)
			scope.setMeasID(meas.getCode());

		//ѡ��Ԫ���ڶ�̬����
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		if (!dynAreaModel.isInDynArea(cell)) {
			return scope;
		}

		//���ҵ�Ԫ��λ�ڶ�̬����������
		DynAreaCell dynAreaCell = dynAreaModel.getDynAreaCellByPos(cell);
		AreaPosition unitArea = dynAreaCell.getOwnerUnitArea(cell);

		KeyVO[] dynKeyVOs = dynAreaModel.getKeywordModel().getDynKeyVOs(
				dynAreaCell.getDynAreaPK());
		Arrays.sort(dynKeyVOs);

		//���Ҷ�̬���ؼ��ֵ�ֵ
		Vector<String> dynKeyVals = new Vector<String>();
		ArrayList<CellPosition> list = cellsModel.getSeperateCellPos(unitArea);
		for (KeyVO key : dynKeyVOs) {
			for (CellPosition cellPos : list) {
				KeyFmt keyFmt = (KeyFmt) cellsModel.getCell(cellPos).getExtFmt(
						KeyFmt.EXT_FMT_KEYINPUT);
				if (keyFmt != null
						&& keyFmt.getKeyPK().equals(key.getKeywordPK())) {
					Object objValue = cellsModel.getCellValue(cellPos);
					dynKeyVals.add(objValue == null ? null : objValue
							.toString());
					break;
				}
			}

		}
		scope.setDynKeyVals(dynKeyVals.toArray(new String[0]));

		return scope;
	}

	public static FormulaHandler getFormulaHandler(UfoContextVO contextVO,
			CellsModel cellsModel) {
		return new FormulaHandler(contextVO, cellsModel, true);
	}
}
