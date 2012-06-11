package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import java.util.Hashtable;

import nc.vo.iufo.unit.UnitInfoVO;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.inputplugin.MeasTraceInfo;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.biz.WindowNavUtil;
import com.ufsoft.iufo.inputplugin.biz.file.GeneralQueryUtil;
import com.ufsoft.iuforeport.tableinput.applet.ReportViewType;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.report.util.MultiLang;

public class MeasureTraceBizOper extends InputBizOper implements IUfoContextKey{
	private TableInputTransObj m_oMeasTraceTransObj = null;
	private MeasTraceInfo m_oMeasTraceInfo = null;
	public MeasureTraceBizOper(UfoReport ufoReport) {
		super(ufoReport);
	}
	
	public MeasureTraceBizOper(UfoReport ufoReport,TableInputTransObj measTraceTransObj,MeasTraceInfo measTraceInfo) {
		super(ufoReport);
		this.m_oMeasTraceTransObj = measTraceTransObj;
		this.m_oMeasTraceInfo = measTraceInfo;
	}
    protected TableInputTransObj getTransObj(){   
    	if(m_oMeasTraceTransObj != null){
    		return m_oMeasTraceTransObj;
    	}
        return doGetTransObj(getUfoReport());
    }
    protected CellsModel getCellsModel(){
    	if(getUfoReport() != null){
    		return getUfoReport().getCellsModel();
    	}
    	return null;
    }
    
    /**
     * 
     * @param transReturnObj
     * @param nMenuType
     * @return 返回给html的提示信息
     * @i18n uiuforep00103=自由查询->指标追踪
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
        if(results.length ==3 && nMenuType == ITableInputMenuType.BIZ_TYPE_MEASURE_TRACE){
            firstTransReturnObj = new Object[]{results[0],results[1]};
            Object transContextObj = results[0];
            Object transCoreReturnObj = results[1];
            
            Object[] otherRetResults = (Object[])results[2];
            Object transTableInputObj = null;
            if(otherRetResults != null && otherRetResults.length > 0){
            	transTableInputObj = otherRetResults[0];            	
            }else{
            	transTableInputObj = getTransObj();
            }
            //#指标追踪
            //获取窗口UfoReport
        	CellsModel cellsModel = (CellsModel)transCoreReturnObj;        	
        	TableInputContextVO newInputContextVO = new TableInputContextVO((UfoContextVO)transContextObj,(TableInputTransObj)transTableInputObj); 
        		//geneNewInputContextVO(getUfoReport(),(UfoContextVO)transContextObj);;
        	newInputContextVO.setAttribute(SHOW_REP_TREE, true);//TODO
        	newInputContextVO.setAttribute(GENRAL_QUERY, true);//TODO 
        	Hashtable<String,Object> params = WindowNavUtil.getParams(newInputContextVO);
            UfoReport ufoReport = WindowNavUtil.getReportInstance(newInputContextVO,cellsModel, UfoReport.OPERATION_INPUT, params,ReportViewType.VIEW_REPORT,MultiLang.getString("uiuforep00103"));//TODO 标题名称
            
            TableInputContextVO inputContextVO = (TableInputContextVO)ufoReport.getContextVo();
            
            //计算获得公式位置信息 
            String strMeasurePK = m_oMeasTraceInfo.getStrMeasurePK();
            String[] strKeyVals = m_oMeasTraceInfo.getStrKeyVals(); 
            Object repIdObj = inputContextVO.getAttribute(REPORT_PK);
    		String strReportPK = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null;
    		
            IArea[] curTracedPos = WindowNavUtil.calMeasureTracedPos(strReportPK,cellsModel,strMeasurePK,strKeyVals);
            
            //追踪相关状态设置:
            WindowNavUtil.setTraceInfo(ufoReport,curTracedPos);

            //设置导航树及查询面板缺省条件
//			String strOperUnitPK = inputContextVO.getInputTransObj().getRepDataParam().getOperUnitPK();
//			String strOperRepPK = inputContextVO.getInputTransObj().getRepDataParam().getReportPK();
//			UnitInfoVO unitInfo = GeneralQueryUtil.findUnitInfoByPK(strOperUnitPK);
//			if(unitInfo != null){
//				GeneralQueryUtil.getChooseRepPanel(ufoReport).setSelNodeOfNavTree(strOperRepPK, unitInfo.getCode());
//				GeneralQueryUtil.getChooseCordPanel(ufoReport).setSelItemOfCordPanel(unitInfo.getCode(), strOperRepPK);
//			}
            
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
 