package nc.ui.iufo.input;

import nc.vo.iufo.repdataright.RepDataRightVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.service.CellDataValue;
import com.ufsoft.iufo.fmtplugin.service.ReportFormatSrv;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 报表录入界面Action
 * @author weixl,Created on 2006-2-28
 */
public class InputNewAction extends AbstractRepInputAction {

	protected ReportVO[] getReports(CSomeParam param) throws Exception{
		String strUnitID=param.getUnitId();
		String strTaskID=param.getTaskId();
		if (strTaskID==null)
			return null;
		
        boolean bViewTask = param.getOperType()==CSomeParam.VIEW_TASK;
        ReportVO[] reports = InputUtil.getReports(strTaskID,strUnitID,getCurUserInfo(),isNeedFilterByDataRight(),bViewTask,param.getAloneId(),isFilterInnerTrade(),getCurOrgPK());      		
	    return reports;
	}
	
	protected String doCheckDynArea(ReportFormatSrv repFormatSrv,CellDataValue[][] cells){
		return InputUtil.doCheckDynArea(repFormatSrv,cells,StringResource.getStringResource("miufo1002873"));
	}
	
	protected UfoTableUtil doDataRightTreat(int iRepDataRight,String unitId, String repId, UfoTableUtil tabUtil) throws Exception {
    	if (isNeedFilterByDataRight()==false)
    		return tabUtil;
    	
        if (repId!=null && unitId!=null && iRepDataRight<RepDataRightVO.RIGHT_TYPE_VIEW)
        	return null;
        
        return tabUtil;
	}

	protected boolean isNeedFilterByDataRight() {
		return true;
	}

	protected boolean isSuppertU8Decent() {
		return true;
	}

	protected boolean isSupportTraceback(IRepGridForm form) {
		int iOperType=form.getParam().getOperType();
		if (iOperType==CSomeParam.INPUT)
			return false;
		return true;
	}
	
	protected String getImportExcelActionClass() {
		return ImportExcelDataAction.class.getName();
	}

	protected String getInputKeywordActionClass() {
		return AutoInputKeywordsAction.class.getName();
	}

	protected String getInputUIClass() {
		return InputNewUI.class.getName();
	}

	protected int getReportType(CSomeParam param,int iRepDataRight) throws Exception{
		return InputActionUtil.getReportType(this,param,getCurUserInfo(),isNeedFilterByDataRight(),iRepDataRight);
	}

	protected UfoTableUtil getUfoTableUtil() {
		return new UfoTableUtil(this);
	}

	protected boolean isCanInput(IRepGridForm form,int iRepDataRight) throws Exception{
		int iReportType=getReportType(form.getParam(),iRepDataRight);
		return iReportType>=CSomeParam.TYPE_REP_INPUT;
	}

	protected boolean isCanImportU8UFOData(CSomeParam param) throws Exception{
        if(param != null){
            return InputActionUtil.isCanImportU8UFOData(param.getTaskId());
        }
        return false;
	}

	protected boolean isHaveCanModifyRepPKs(CSomeParam param,int iDataRight) throws Exception{
		String strRepID=param.getRepId();
		if (strRepID==null || strRepID.trim().length()<=0 || strRepID.equalsIgnoreCase("null"))
			strRepID=null;
		
		return InputActionUtil.isHaveCanModifyRepPKs(strRepID,isNeedFilterByDataRight(),iDataRight,param.getAloneId(),param.getTaskId(),param.getUnitId(),getCurUserInfo(),getCurOrgPK());
	}
	
    /**
     * 报表树中是否需要过滤掉内部交易报表
     * @return
     */
	protected boolean isFilterInnerTrade(){
		return false;
	}
    
	protected boolean isSupportMultiStyle(IRepMenuForm form) {
		int operType=form.getParam().getOperType();
		if (form.isHaveKeys() && form.isSWData()==false && (operType==CSomeParam.INPUT || operType==CSomeParam.MODIFY_REP ||operType==CSomeParam.MODIFY_TASK))
			return true;
	
		return false;
	}
}
