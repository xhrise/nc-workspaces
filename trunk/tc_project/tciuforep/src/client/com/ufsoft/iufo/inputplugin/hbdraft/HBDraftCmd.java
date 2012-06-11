package com.ufsoft.iufo.inputplugin.hbdraft;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import nc.ui.hbbb.hb.HBDraftActionUtil;
import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.hbbb.pub.HBBBUIUtils;
import nc.ui.hbbb.pub.Util;
import nc.ui.hbbb.vouch.VouchUtils;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.vo.hbbb.hb.HBDraftParamVO;
import nc.vo.hbbb.hbunit.HBUnitQueryResult;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.html.A;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.window.WindowMngPlugin;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigationPlugin;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNextPlugin;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.RepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.PluginRegister;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.sysplugin.print.PrintPlugin;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedAreaModel;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.TableStyle;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;

public class HBDraftCmd extends UfoCommand implements IUfoContextKey{
	
	

	private UfoReport _report;
	private IRepDataParam _repDataParam;

    /**
     * @param _report
     */
	HBDraftCmd(UfoReport report,IRepDataParam repDataParam) {
    	_report = report;
    	_repDataParam = repDataParam;
    }
	
	HBDraftCmd(UfoReport report) {
    	_report = report;
    }

	/**
	 * @i18n uiuforep00144=合并报表底稿不存在
	 * @i18n uiuforep00145=合并报表底稿
	 */
	@Override
	public void execute(Object[] params) {
		Map  map = HBDraftTraceExt.getSelectedCellParaMap(_report);
		Vector selMeasureVec = new Vector();
		if(map!=null){
			String selMeasurePK = (String) map.get("measurePK");
			if(selMeasurePK!=null)
			selMeasureVec.add(selMeasurePK);
		}
		if(_repDataParam == null)
			_repDataParam = getRepDataParamFromReport();
		
		CellsModel model = getHBDraftCellsModel(_repDataParam,selMeasureVec);
		
		
		if (model == null) {
			javax.swing.JOptionPane.showMessageDialog(getUfoReport(),
					MultiLang.getString("uiuforep00144"));
		}else
			QueryNavigation
		.showReport(new UfoReport(UfoReport.OPERATION_INPUT, getUfoReport().getContextVo(), model,new PluginRegister(){
			public void register() {
				getReport().addPlugIn(PrintPlugin.class.getName());        //打印相关菜单：页面设置，打印预览，打印
				getReport().addPlugIn(HBDraftTracePlugin.class.getName());   
				getReport().addPlugIn(WindowMngPlugin.class.getName());
				getReport().addPlugIn(QueryNavigationPlugin.class.getName());
				getReport().addPlugIn(QueryNextPlugin.class.getName());
			}}),
				MultiLang.getString("uiuforep00145"), true,false);
	}
	
	private IRepDataParam getRepDataParamFromReport(){
		RepDataParam repDataParam = new RepDataParam();
		
		TableInputContextVO context = (TableInputContextVO)_report.getContextVo();
		Object tableInputTransObj = context.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
	
//		TableInputTransObj obj = context.getInputTransObj();
		IRepDataParam hbParam = inputTransObj.getRepDataParam();
		
		repDataParam.setAloneID(hbParam.getAloneID());
		repDataParam.setTaskPK(hbParam.getTaskPK());
		repDataParam.setReportPK(hbParam.getReportPK());
		repDataParam.setOperUnitPK(hbParam.getOperUnitPK());
		
		return repDataParam;
	}
	

	private CellsModel getHBDraftCellsModel(IRepDataParam hbParam,Vector selMeasureVec){
		
		HBDraftParamVO param = new HBDraftParamVO();

		String taskId = hbParam.getTaskPK();
		String aloneId = hbParam.getAloneID();
		if(aloneId == null)
			return null;
		MeasurePubDataVO pubdata = getHBMeasurePubdata(aloneId, taskId);
		if(pubdata!=null)
			aloneId = pubdata.getAloneID();
		if(aloneId == null)
			return null;
		String repId = hbParam.getReportPK();
		String curUnitId = hbParam.getOperUnitPK();
		String accTime = getAccTime(pubdata, taskId);
		
		param.setAccTime(accTime);
		param.setAloneId(aloneId);
		param.setDraftType(HBDraftParamVO.QUERY_TYPE_DRAFT_GENERAL);
		param.setRepID(repId);
		param.setUnitCode(curUnitId);
		param.setTaskID(taskId);
		CellsModel model = null;
		try {
			Object[][] datas = HBDraftActionUtil.genDraftData(param, new Vector(), null);
		int rowSize = datas.length;
		int colSize = datas[0].length;
		int titleSize = 3;
		int keywordSize = 1;
		int headSize = 1;
		Cell[][] cells = new Cell[rowSize+titleSize+keywordSize+headSize+2][colSize];
		//标题
		Cell cellTitle = new Cell();
		cellTitle.setRow(0);
		cellTitle.setCol(0);
		
		cellTitle.setValue(getTitle(repId));
		IufoFormat format = new IufoFormat();
	      format.setFontSize(30);
	      format.setHalign(TableConstant.HOR_CENTER);
	      format.setFold(true);
	      cellTitle.setFormat(format);
		cells[0][0] = cellTitle;
		Object[] heads = HBDraftActionUtil.getTableHeads(param, new Vector(), null);
		
		IufoFormat defaultFmt = getContentFormat();
		//关键字
		Cell keywordCell = new Cell();
		keywordCell.setRow(titleSize);
		keywordCell.setCol(0);
		keywordCell.setValue(getKeyword(aloneId));
		keywordCell.setFormat(defaultFmt);
		cells[titleSize][0] = keywordCell;
		
		keywordCell = new Cell();
		keywordCell.setRow(titleSize+1);
		keywordCell.setCol(0);
		keywordCell.setValue(getKeyword(aloneId));
		keywordCell.setFormat(defaultFmt);
		cells[titleSize+1][0] = keywordCell;
		//单位，抵销借贷方
		for(int i=0;i<colSize;i++){
			String value = null;
			Cell cell = new Cell();
			cell.setCol(i);
			cell.setRow(titleSize+3);
			if(heads[i]  instanceof A){
				value = ((A)heads[i]).getValue();
			}else
				value = heads[i].toString();
			cell.setValue(value);
			cell.setFormat(getHeadFormat());
			
			cells[titleSize+2][i] = cell;
		}
		int IMaxLen = getCellWidth(datas);
		
		//内容
		for(int i=0;i<rowSize;i++){
			for(int j=0;j<colSize;j++){
				
				String value = null;
				Cell cell = new Cell();
				cell.setRow(i+titleSize+keywordSize+headSize+1);
				cell.setCol(j);

				if(datas[i][j]  instanceof A){
					value = ((A)datas[i][j]).getValue();
				}else
					value = datas[i][j].toString();
				cell.setValue(value);
				if(j==0)
					cell.setFormat(getMeasureNameFormat());
				else
					cell.setFormat(defaultFmt);

				cells[i+titleSize+keywordSize+headSize+1][j] = cell;
			}
		}
		
		AreaPosition aPos = AreaPosition.getInstance(0, 0, colSize, 3);
		
		model = CellsModel.getInstance(cells, true);
		CombinedAreaModel combinedModel = CombinedAreaModel.getInstance(model);
		
	      CombinedCell cc = new CombinedCell(aPos,null , "");
//	      cc.setValue("合并报表");
		
		combinedModel.addCombinedCell(cc);
		
		
		AreaPosition bPos = AreaPosition.getInstance(titleSize+1, 0, colSize, 0);
		CombinedCell cc2 = new CombinedCell(bPos,null , "");
		combinedModel.addCombinedCell(cc2);
		//得到合并范围内的单位pk
		String[] hbUnitPKs = HBDraftActionUtil.getHBUnitPKs(param);
		HBUnitQueryResult queryResult = Util.getHBUnit(param.getTaskID(), param.getUnitCode(),param.getAccTime(), true, false);
		HashMap mapOfDataVer = Util.getDirectHBUnitRepVer(queryResult);
		int nVer = HBBBSysParaUtil.VER_SEPARATE;
		//在下级单位的列中增加数据追踪功能
		MeasureVO[] measures=HBDraftActionUtil.getSortHBMeasures(repId,curUnitId,HBDraftParamVO.HBMEAS_ALL,true);
		for(int i=titleSize+keywordSize+headSize;i<rowSize+titleSize+keywordSize+headSize+2;i++){
			String measurePK = null;
			if(i > titleSize+keywordSize+headSize)
				measurePK = measures[i-titleSize-keywordSize-headSize-1].getCode();
			//合并范围的单位列
			for(int j=0;j<hbUnitPKs.length;j++){
					CellPosition pos =CellPosition.getInstance(i, j+1);
					Map map = new HashMap();
					map.put("colType", HBDraftTraceExt.DRAFT_UNITCOL);
					map.put("unitPK", hbUnitPKs[j]);
					map.put("measurePK", measurePK);
					if(mapOfDataVer.containsKey(hbUnitPKs[j])){
						nVer = ((Integer)mapOfDataVer.get(hbUnitPKs[j])).intValue();
					}
					
					if (!curUnitId.equals(hbUnitPKs[j])	&& nVer == HBBBSysParaUtil.VER_HBBB) {
						map.put("ver", "hb");
						if (i == titleSize + 2) {
							pos = CellPosition.getInstance(i, j + 1);
							model.getCell(pos).setFormat(getHBUnitFormat());
						}
					}
					
					model.setBsFormat(pos, HBDraftTraceExt.HBDRAFT_TRACE_FMT, map);
			}
			//抵销分录列
			if(i > titleSize+keywordSize+headSize){
				CellPosition pos =CellPosition.getInstance(i, colSize-3);
				Map map = new HashMap();
				map.put("measurePK", measurePK);
				map.put("colType", HBDraftTraceExt.DRAFT_VOUCHCOL);
				map.put("unitPK", curUnitId);
				model.setBsFormat(pos, HBDraftTraceExt.HBDRAFT_TRACE_FMT, map);
				
				pos =CellPosition.getInstance(i, colSize-2);
				
				model.setBsFormat(pos, HBDraftTraceExt.HBDRAFT_TRACE_FMT, map);
			}
			//合并数列
			if(measurePK!=null && selMeasureVec.contains(measurePK)){
				CellPosition pos =CellPosition.getInstance(i, colSize-1);
				model.getCell(pos).setFormat(getSelMesureFormat());
			}
				
		}
		

		model.getColumnHeaderModel().setSize(0, IMaxLen*10);
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		return model;
	}

private MeasurePubDataVO  getHBMeasurePubdata(String aloneId,String taskId){
	MeasurePubDataVO pubdata = null;
	try{
	pubdata = MeasurePubDataBO_Client.findByAloneID(aloneId);
	pubdata.setVer(HBBBSysParaUtil.VER_HBBB);
	if(Util.isHBRepDataRelatingWithTask())
		pubdata.setFormulaID(taskId);
	pubdata = MeasurePubDataBO_Client.findByKeywords(pubdata);
	
	}catch(Exception e){
		AppDebug.debug(e);
	}
	return pubdata;
}
private String getAccTime(MeasurePubDataVO pubdata,String taskId){
	return pubdata.getTTimeKeyValue();
//	return pubdata.getKeywordByPK(timeKeyword);
}

/**
 * @i18n uiuforep00146=工作底稿
 */
private String getTitle(String repId){
	return IUFOUICacheManager.getSingleton().getReportCache().getByPK(repId).getName()+MultiLang.getString("uiuforep00146");
}
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

private IufoFormat getHBUnitFormat(){
	int [] linePos = {Format.TOPLINE,Format.BOTTOMLINE,Format.LEFTLINE,Format.RIGHTLINE};
	IufoFormat format = new IufoFormat();
	for (int i = 0; i < linePos.length; i++) {
        format.setLineType(linePos[i],TableConstant.L_SOLID1);
        format.setLineColor(linePos[i],Color.black);
      }
	format.setHalign(TableConstant.HOR_CENTER);
	format.setBackgroundColor(Color.RED);
	return format;
}

private IufoFormat getSelMesureFormat(){
	int [] linePos = {Format.TOPLINE,Format.BOTTOMLINE,Format.LEFTLINE,Format.RIGHTLINE};
	IufoFormat format = new IufoFormat();
	for (int i = 0; i < linePos.length; i++) {
        format.setLineType(linePos[i],TableConstant.L_SOLID1);
        format.setLineColor(linePos[i],Color.black);
      }
	format.setHalign(TableConstant.HOR_RIGHT);
	format.setBackgroundColor(TableStyle.SELECTION_BACKGROUND);
	return format;
} 

private IufoFormat getMeasureNameFormat(){
	int [] linePos = {Format.TOPLINE,Format.BOTTOMLINE,Format.LEFTLINE,Format.RIGHTLINE};
	IufoFormat format = new IufoFormat();
	for (int i = 0; i < linePos.length; i++) {
        format.setLineType(linePos[i],TableConstant.L_SOLID1);
        format.setLineColor(linePos[i],Color.black);
      }
	format.setHalign(TableConstant.HOR_CENTER);
	return format;
} 

private IufoFormat getHeadFormat(){
	int [] linePos = {Format.TOPLINE,Format.BOTTOMLINE,Format.LEFTLINE,Format.RIGHTLINE};
	IufoFormat format = new IufoFormat();
	for (int i = 0; i < linePos.length; i++) {
        format.setLineType(linePos[i],TableConstant.L_SOLID1);
        format.setLineColor(linePos[i],Color.black);
      }
	format.setHalign(TableConstant.HOR_CENTER);
	format.setBackgroundColor(Color.GRAY);
	return format;
}


private int getCellWidth(Object[][] datas){
	int iMaxLen = 12;
	String measureName = null;
	for(int i=0;i<datas.length;i++){
		if(datas[i][0] instanceof WebLabel)
			measureName =((WebLabel)datas[i][0]).getValue();
		else if (datas[i][0] instanceof String)
			measureName = datas[i][0].toString();
		if(iMaxLen < measureName.getBytes().length)
			iMaxLen = measureName.getBytes().length;
	}
	return iMaxLen;
}
/**
 * @i18n uiuforep00055=会计期间
 */
private String getKeyword(String aloneId){
	String keyword = "";
	MeasurePubDataVO pubdata= null;
	try {
		pubdata = MeasurePubDataBO_Client.findByAloneID(aloneId);
	} catch (Exception e) {
		AppDebug.debug(e);
	}
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

protected UfoReport getUfoReport() {
	return _report;
}

}
 