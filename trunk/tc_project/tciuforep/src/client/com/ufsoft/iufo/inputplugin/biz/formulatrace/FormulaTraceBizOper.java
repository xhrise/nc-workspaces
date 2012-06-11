package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import java.util.Hashtable;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.biz.WindowNavUtil;
import com.ufsoft.iuforeport.tableinput.applet.ReportViewType;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.report.util.MultiLang;

public class FormulaTraceBizOper extends InputBizOper implements IUfoContextKey{

	public FormulaTraceBizOper(UfoReport ufoReport) {
		super(ufoReport);
	}

	
    /**
     * 
     * @param transReturnObj
     * @param nMenuType
     * @return 返回给html的提示信息
     * @i18n uiuforep00095=综合查询->公式追踪
     */
    protected String dealTransReturnObj(Object transReturnObj,int nMenuType) {
        StringBuffer sbErrorRetObjs = new StringBuffer();
        if(checkReturnObjs(transReturnObj,sbErrorRetObjs)){
            return sbErrorRetObjs.toString();
        }
        Object[] results = null;
        if(transReturnObj instanceof Object[]){
            results = (Object[])transReturnObj;
        }else{
            return null;
        }
        Object firstTransReturnObj = transReturnObj;
        if(results.length ==3 && nMenuType == ITableInputMenuType.BIZ_TYPE_FORMULATRACE_TRACE){
            firstTransReturnObj = new Object[]{results[0],results[1]};
            Object transContextObj = results[0];
            Object transCoreReturnObj = results[1];
            //#公式追踪-联查值 
            //获取窗口UfoReport
        	CellsModel cellsModel = (CellsModel)transCoreReturnObj;        	
        	TableInputContextVO newInputContextVO = geneNewInputContextVO(getUfoReport(),(UfoContextVO)transContextObj);;
        	newInputContextVO.setAttribute(SHOW_REP_TREE, true);//TODO chxw...
        	newInputContextVO.setAttribute(GENRAL_QUERY, true);//TODO chxw...
        	Hashtable<String,Object> params = WindowNavUtil.getParams(newInputContextVO);
            
        	UfoReport ufoReport = WindowNavUtil.getReportInstance(newInputContextVO,cellsModel, UfoReport.OPERATION_INPUT, params,ReportViewType.VIEW_REPORT,MultiLang.getString("uiuforep00095"));
            //公式追踪相关状态设置:
            Object[] otherRetResults = (Object[])results[2];
            if(otherRetResults != null && otherRetResults.length > 0){
            	IArea[] curTracedPos = (IArea[])otherRetResults[0]; 
            	WindowNavUtil.setTraceInfo(ufoReport,curTracedPos);
            }
 
//        	//TODO DEBUGING measure trace... 
//        	String strReportPK = newInputContextVO.getInputTransObj().getRepDataParam().getReportPK();
//        	String strAloneID = "o4508bkp6j4q3p034jbl";//newInputContextVO.getInputTransObj().getRepDataParam().getAloneID();
//        	MeasTraceInfo measTraceInfo = new MeasTraceInfo("",new String[]{"",""});
//        	WindowNavUtil.traceMeasure(getUfoReport(),strReportPK,strAloneID,measTraceInfo);
        	
        	String strDebugReturn = "true";
        	return strDebugReturn;
        	
        	
        }else{
            firstTransReturnObj = transReturnObj;
        }
        
//        if(firstTransReturnObj != null &&  results.length >=2 && results[1] instanceof CellsModel){
        if(results.length >=2){
        	return super.dealTransReturnObj(firstTransReturnObj,nMenuType);
        }else {
            return null;
        }
    }
    
    
}
 