package com.ufsoft.iufo.inputplugin.hbdraft;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import nc.ui.hbbb.hb.HBDraftActionUtil;
import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.hbbb.pub.HBBBUIUtils;
import nc.ui.hbbb.pub.Util;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.vo.hbbb.hb.HBDraftParamVO;
import nc.vo.hbbb.hbunit.HBUnitQueryResult;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.html.A;
import com.ufida.zior.console.ActionHandler;
import com.ufsoft.iufo.inputplugin.ufobiz.UfoExcelExpPlugin;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.ReportAuthReadOnly;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedAreaModel;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;

public class HBDraftViewer extends ReportDesigner{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3401350237796729372L;
	public static final String COLUMN_REPDATA = "repdata";
	public static final String COLUMN_VOUCHDATA = "vouchdata";
	
	public static String HBDRAFT_TRACE_FMT = "hbdraft_tracefmt";

	@Override
	protected boolean save() {
		return false;
	}

	@Override
	public String[] createPluginList() {
		return new String[]{UfoHBDraftTraceSrcPlugin.class.getName(),UfoExcelExpPlugin.class.getName()};
	}

	@Override
	protected void shutdown() {
		
	}
	
	public void initCellsModel(RepDataEditor repDataEditor) throws Exception {
		HBDraftParamVO param = new HBDraftParamVO();

		String strTaskId = repDataEditor.getTaskPK();
		String strAloneId = repDataEditor.getAloneID();
		String strRepId = repDataEditor.getRepPK();
		MeasurePubDataVO pubdata = repDataEditor.getPubData();
		String strUnitId = pubdata.getUnitPK();
		String strAccPeriod = pubdata.getInputDate();

		param.setAccTime(strAccPeriod);
		param.setAloneId(strAloneId);
		param.setDraftType(HBDraftParamVO.QUERY_TYPE_DRAFT_GENERAL);
		param.setRepID(strRepId);
		param.setUnitCode(strUnitId);
		param.setTaskID(strTaskId);
		CellsModel model = null;
		
		
		List<Cell[]> lstAll = new ArrayList<Cell[]>();
		Object[] heads = (Object[])ActionHandler.exec("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "getHBDraftHeads",new Object[]{param}); 
			//HBDraftActionUtil.getTableHeads(param, new Vector(), null);
		int iColSize = heads.length;
		//标题所占行数
		int iTitleRow = 1;
		//标题cell
		Cell titleCell = getTitleCell(strRepId);
		lstAll.add(new Cell[]{titleCell});
		//关键字信息
		lstAll.add(new Cell[]{getKeywordCell(iTitleRow, pubdata, iColSize)});

		//头信息
		Cell[] headCells = getHeadCell(iTitleRow + 1, heads);
		lstAll.add(headCells);
		
		//生成底稿数据	
		Object[][] datas = (Object[][])ActionHandler.exec("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "getHBDraftDatas",new Object[]{param});
//			HBDraftActionUtil.genDraftData(param,new Vector(), null);
		Cell[][] contents = genDataCell(iTitleRow + 2,datas);
		lstAll.addAll(Arrays.asList(contents));
		
		model = CellsModel.getInstance(lstAll.toArray(new Object[0][0]), true);
		
		//组合标题单元格
		CombinedAreaModel combinedModel = CombinedAreaModel.getInstance(model);
		AreaPosition aPos = AreaPosition.getInstance(0, 0, iColSize, 1);
		CombinedCell cc = new CombinedCell(aPos,null , "");
		combinedModel.addCombinedCell(cc);
		
		//组合查询关键字单元格
		AreaPosition bPos = AreaPosition.getInstance(1, 0, iColSize, 1);
		CombinedCell cc2 = new CombinedCell(bPos,null , "");
		combinedModel.addCombinedCell(cc2);
		//设置标题的高度
		model.getRowHeaderModel().getHeader(0).setSize(100);
		model.getColumnHeaderModel().getHeader(0).setSize(getMeaColWidth(datas));
		
		
		//设置各列的业务属性，以便于再次追踪：指标数据追踪到报表数据，抵销列追踪到调整凭证
		
		String[] aryHBUnitIds = HBDraftActionUtil.getHBUnitPKs(param);
		HBUnitQueryResult queryResult = Util.getHBUnit(strTaskId, strUnitId,strAccPeriod, true, false);
		HashMap mapOfDataVer = Util.getDirectHBUnitRepVer(queryResult);
		
		MeasureVO[] measures=HBDraftActionUtil.getSortHBMeasures(strRepId,strUnitId,HBDraftParamVO.HBMEAS_ALL,true);
		for(int i=0;i<measures.length;i++){
			String strMeasurePK = measures[i].getCode();
			
			for(int j=0;j<aryHBUnitIds.length;j++){
				CellPosition pos =CellPosition.getInstance(i+3, j+1);
				HBDraftTraceVO trace = new HBDraftTraceVO();
				trace.setColType(COLUMN_REPDATA);
				trace.setUnitId(aryHBUnitIds[j]);
				trace.setMeasurePK(strMeasurePK);
				trace.setTaskId(strTaskId);
				trace.setReportId(strRepId);
				trace.setAloneId(strAloneId);
				if(strUnitId.equals(aryHBUnitIds[j]))
					trace.setVer(HBBBSysParaUtil.VER_SEPARATE);
				else
					trace.setVer(Integer.parseInt(""+mapOfDataVer.get(aryHBUnitIds[j])));
				model.setBsFormat(pos, HBDRAFT_TRACE_FMT, trace);
			}
			
			for(int j= iColSize-3 ; j < iColSize -1;j++){
				CellPosition pos =CellPosition.getInstance(i+3, j);
				HBDraftTraceVO trace = new HBDraftTraceVO();
				trace.setColType(COLUMN_VOUCHDATA);
				trace.setMeasurePK(strMeasurePK);
				trace.setTaskId(strTaskId);
				trace.setAloneId(strAloneId);
				model.setBsFormat(pos, HBDraftTraceExt.HBDRAFT_TRACE_FMT, trace);
			}
		}
		
		model.setDirty(false);
		setCellsModel(model);
		
		
		
	}

	private Cell getTitleCell(String repId) {
		Cell titleCell = new Cell();
		titleCell.setRow(0);
		titleCell.setCol(0);
		titleCell.setValue(getTitle(repId));
		titleCell.setFormat(getTitleFormat());
		return titleCell;
	}
	/**
	 * 得到关键字cell
	 * @param iStartRow
	 * @param pubdata
	 * @param iColSize
	 * @return
	 */
	private Cell getKeywordCell(int iStartRow,MeasurePubDataVO pubdata,int iColSize){
		Cell cell = new Cell();
		cell.setValue(getKeyword(pubdata));
		cell.setRow(iStartRow);
		cell.setCol(0);
		return cell;
		
	}
	/**
	 * 得到头信息cells
	 * @param iStartRow
	 * @param heads
	 * @return
	 */
	private Cell[] getHeadCell(int iStartRow,Object[] heads){
		Cell[] cells = new Cell[heads.length];
		IufoFormat defaultFmt = getHeadFormat(false);
		IufoFormat hbVerFmt = getHeadFormat(true);
		for(int i=0;i<heads.length;i++){
			String value = null;
			Cell cell = new Cell();
			cell.setCol(i);
			cell.setRow(iStartRow);
			if(heads[i]  instanceof A){
				value = ((A)heads[i]).getValue();
				cell.setFormat(hbVerFmt);
			}else{
				value = heads[i].toString();
				cell.setFormat(defaultFmt);
			}
			cell.setValue(value);
			cells[i] = cell;
		}
		return cells;
	}
	/**
	 * 根据底稿数据得到cells
	 * @param datas
	 * @return
	 */
	private Cell[][] genDataCell(int iStartRow,Object[][] datas){
		int rowSize = datas.length;
		int colSize = datas[0].length;
		Cell[][] cells = new Cell[rowSize][colSize];
		IufoFormat dataFmt = getContentFormat();
		IufoFormat measureFmt = getMeasureFormat();
		for(int i = 0;i < rowSize; i++){
			for(int j = 0;j < colSize;j++){
				Cell cell = new Cell();
				cell.setRow(i+iStartRow);
				cell.setCol(j);

				if(datas[i][j]  instanceof A){
					cell.setValue(((A)datas[i][j]).getValue());
				}else
					cell.setValue( datas[i][j].toString());
				if(j == 0)
					cell.setFormat(measureFmt);
				else
					cell.setFormat(dataFmt);
				cells[i][j] = cell;
			}
		}
		return cells;
	}
	/**
	 * 得到指标列的宽度
	 * @param datas
	 * @return
	 */
	private int getMeaColWidth(Object[][] datas){
		int iMaxLen = 0;
		for(int i=0;i<datas.length;i++){
			String strMeasureName = datas[i][0].toString();
			if (iMaxLen<strMeasureName.getBytes().length)
				iMaxLen=strMeasureName.getBytes().length;
		}
		return (int)(iMaxLen*7.5);
	}
	
	private String getTitle(String repId){
		return IUFOUICacheManager.getSingleton().getReportCache().getByPK(repId).getName()+MultiLang.getString("uiuforep00146");
	}
	/**
	 *第一行单元格的格式
	 * @return
	 */
	private IufoFormat getHeadFormat(boolean bHBVer){
		int [] linePos = {Format.TOPLINE,Format.BOTTOMLINE,Format.LEFTLINE,Format.RIGHTLINE};
		IufoFormat format = new IufoFormat();
		for (int i = 0; i < linePos.length; i++) {
	        format.setLineType(linePos[i],TableConstant.L_SOLID1);
	        format.setLineColor(linePos[i],Color.black);
	      }
		format.setHalign(TableConstant.HOR_CENTER);
		if(bHBVer)
			format.setBackgroundColor(Color.RED);
		else
			format.setBackgroundColor(Color.LIGHT_GRAY);
		return format;
	}
	/**
	 * 内容单元格的格式
	 * @return
	 */
	private IufoFormat getContentFormat(){
		int [] linePos = {Format.TOPLINE,Format.BOTTOMLINE,Format.LEFTLINE,Format.RIGHTLINE};
		IufoFormat format = new IufoFormat();
		for (int i = 0; i < linePos.length; i++) {
	        format.setLineType(linePos[i],TableConstant.L_SOLID1);
	        format.setLineColor(linePos[i],Color.black);
	      }
		format.setHalign(TableConstant.HOR_RIGHT);
		return format;
	}
	private IufoFormat getMeasureFormat(){
		int [] linePos = {Format.TOPLINE,Format.BOTTOMLINE,Format.LEFTLINE,Format.RIGHTLINE};
		IufoFormat format = new IufoFormat();
		for (int i = 0; i < linePos.length; i++) {
	        format.setLineType(linePos[i],TableConstant.L_SOLID1);
	        format.setLineColor(linePos[i],Color.black);
	      }
		format.setHalign(TableConstant.HOR_CENTER);
		return format;
	}
	private IufoFormat getTitleFormat(){
		IufoFormat format = new IufoFormat();
		format.setFontSize(30);
		format.setHalign(TableConstant.HOR_CENTER);
		format.setFold(true);
		return format;
	}
	/**
	 * 得到关键字的值
	 * @param aloneId
	 * @return
	 */
	private String getKeyword(MeasurePubDataVO pubdata){
		String keyword = "";
		KeyVO[] keys = pubdata.getKeyGroup().getKeys();
		String name = null;
		String keywordPK = null;
		String value = null;
		for(int i=0;i<keys.length;i++){
			name = keys[i].getName();
			keywordPK = keys[i].getKeywordPK();
			value = pubdata.getKeywordByPK(keywordPK);
			if(keys[i].getType()==KeyVO.TYPE_TIME)
				name = MultiLang.getString("uiuforep00055");
			if(keywordPK.equals(KeyVO.CORP_PK))
				value = HBBBUIUtils.getUnitCodeNameByPK(value);
			keyword = keyword+ name+": "+value+"    ";
		}
		return keyword;
	}

}
