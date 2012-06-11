package com.ufsoft.iufo.inputplugin.hbdraft;

import java.util.Map;

import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.vo.iufo.data.MeasurePubDataVO;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.MeasTraceInfo;
import com.ufsoft.iufo.inputplugin.biz.WindowNavUtil;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

public class HBDraftTraceCmd  extends UfoCommand implements IUfoContextKey{

	
	private UfoReport _report;

    /**
     * @param _report
     */
	public HBDraftTraceCmd(UfoReport report) {
    	_report = report;
    }

	
	@Override
	public void execute(Object[] params) {
		Map map =  HBDraftTraceExt.getSelectedCellParaMap(_report);
		if(map == null)
			return ;
		String strUnitPK = (String)map.get("unitPK");
		String measurePK = (String)map.get("measurePK");
		TableInputContextVO context = (TableInputContextVO)_report.getContextVo();
		
		Object tableInputTransObj = context.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
	
//		TableInputTransObj obj = context.getInputTransObj();
		IRepDataParam hbParam = inputTransObj.getRepDataParam();
		
		String aloneId = hbParam.getAloneID();
		String taskId = hbParam.getTaskPK();
		String reportPK = hbParam.getReportPK();
		String retAloneId = getSepAloneId(aloneId, strUnitPK, taskId, reportPK);
		Object showRepTreeObj = context.getAttribute(SHOW_REP_TREE);
		boolean isShowRepTree = showRepTreeObj == null ? false : Boolean.parseBoolean(showRepTreeObj.toString());
		
		Object genralQueryObj = context.getAttribute(GENRAL_QUERY);
		boolean isgenralQuery = genralQueryObj == null ? false : Boolean.parseBoolean(genralQueryObj.toString());
		
		MeasurePubDataVO pubdata = context.getPubDataVO();
		
		MeasTraceInfo measTraceInfo = new MeasTraceInfo(measurePK,pubdata.getKeywords(),hbParam.getReportPK(),retAloneId,false);
		measTraceInfo.setGeneryQuery(isgenralQuery);
		measTraceInfo.setShowRepTree(isShowRepTree);
		measTraceInfo.setStrTaskId(taskId);
		TableInputContextVO inputContextVO = null;
		if (_report.getContextVo() instanceof TableInputContextVO) {
			inputContextVO = (TableInputContextVO) _report.getContextVo();
			}
		String strLoginUnitPK = inputContextVO.getAttribute(LOGIN_UNIT_ID) == null ? null : (String)inputContextVO.getAttribute(LOGIN_UNIT_ID);

		measTraceInfo.setStrLoginUnitID(strLoginUnitPK);
		String strOrgPK = inputContextVO.getAttribute(ORG_PK) == null ? null : (String)inputContextVO.getAttribute(ORG_PK);

		measTraceInfo.setStrOrgPK(strOrgPK);
		if(inputTransObj!=null){
			measTraceInfo.setStrLangCode(inputTransObj.getLangCode());
			if(inputTransObj.getRepDataParam()!=null)
				measTraceInfo.setStrOperUserPK(inputTransObj.getRepDataParam().getOperUserPK());
			    measTraceInfo.setDataSource(inputTransObj.getRepDataParam().getDSInfo());
		}
		WindowNavUtil.traceMeasure(measTraceInfo);
		
	}

	private String getSepAloneId(String aloneId,String strUnitPK,String taskId,String reportPK){
		String retAloneId = HBDraftTraceExt.getAloneIdByUnitId(aloneId, strUnitPK,HBBBSysParaUtil.VER_SEPARATE_ADJUST);
		if(retAloneId==null){
			retAloneId = HBDraftTraceExt.getAloneIdByUnitId(aloneId, strUnitPK,HBBBSysParaUtil.VER_SEPARATE);
		}else			
			if(!HBBBSysParaUtil.hasRepData(retAloneId, new String[]{reportPK}))
				retAloneId = HBDraftTraceExt.getAloneIdByUnitId(aloneId, strUnitPK,HBBBSysParaUtil.VER_SEPARATE);
		return retAloneId;
	}
	
}
