package com.ufsoft.iufo.fmtplugin;
import nc.pub.iufo.cache.base.UnitCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.datasource.DataSourceBO_Client;
import nc.ui.iufo.input.table.IufoRefData;
import nc.ui.iuforeport.rep.RepToolAction;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.pub.license.LicenseValue;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iuforeport.applet.IDataSourceParam;

import com.ufida.iufo.pub.AppWorker;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.fmtplugin.formatcore.FormatCorePlugin;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.pluginregister.ReportFormatPluginRegister;
import com.ufsoft.iufo.inputplugin.inputcore.RefData;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.applet.UfoApplet;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellsModel;
/**
 * �����ʽ���ʱ������applet��
 * @author zzl 2005-11-29
 */
public class ReportFormatApplet extends  UfoApplet implements IUfoContextKey{
    /**
     * <code>serialVersionUID</code> ��ע��
     */
    private static final long serialVersionUID = -1603344197872234963L;
    /**
     * @i18n miufohbbb00080=����IUFO�������
	 */
    public void init(){ 
    	//����IUFO�������
    	new AppWorker(StringResource.getStringResource("miufohbbb00080")){
			@Override
			protected Object construct() throws Exception {
				IUFOUICacheManager.getSingleton();
				return null;
			}
    	}.start();
    	 
    	int oper = UfoReport.OPERATION_FORMAT;  
    	//�Ȼ�ȡCellsModel�ټ��ز��,�ڼ�����DynAreaDefPlugIn�����ʱ����CellsModel�л�ȡDynAreaModel
    	UfoContextVO context=getInitContext();
    	CellsModel cellsModel = CellsModelOperator.getFormatModelByPKWithDataProcess(context);
        CellsModelOperator.initModelProperties(context, cellsModel);
        cellsModel.setDirty(false);
      
        UfoReport report = new UfoReport(oper,context,cellsModel,new ReportFormatPluginRegister());
//        
////        setRootPane(report);

        setUfoReport(report);
        
        report.resetGlobalPopMenuSupport();
        
    }
        
    private UfoContextVO getInitContext(){
    	UfoContextVO context=new UfoContextVO();
        context.setContextId(getParameter(REPORT_PK));

        context.setAttribute(MODEL, "1".equals(getParameter(REPORT_TYPE)));
        context.setAttribute(ON_SERVER, false);
        context.setAttribute(CUR_USER_ID, getParameter(CUR_USER_ID));

        String unitId=getParameter(CUR_UNIT_ID);
        getAppletContext();
        String orgPk=getParameter(ORG_PK);
        context.setAttribute(ORG_PK, orgPk);
        context.setAttribute(LOGIN_UNIT_ID, unitId);
        UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
		UnitInfoVO unitInfo = unitCache.getUnitInfoByPK(unitId);
		if(unitInfo!=null&&orgPk!=null){
			String unitPK=unitInfo.getPropValue(orgPk);
			if(unitPK!=null){
				context.setAttribute(LOGIN_UNIT_LEVELCODE, unitPK);
			}
		}
        //edit by wangyga at 2008-12-23 ����04:49:39 �еĵط���ȡCUR_UNIT_ID�ڴ˴���LOGIN_UNIT_ID��ͬ
        context.setAttribute(CUR_UNIT_ID, getParameter(CUR_UNIT_ID));
        context.setAttribute(CREATE_UNIT_ID, getParameter(CREATE_UNIT_ID));
        context.setReportcode(getParameter(REPORT_CODE));
        context.setAttribute(REP_MANAGER, Boolean.valueOf(getParameter(REP_MANAGER)).booleanValue());
        context.setAttribute(ANA_REP, "2".equals(getParameter(REPORT_TYPE)));//Boolean.valueOf(getParameter("isAnalisisReport")).booleanValue());
        context.setName(getParameter(REPORT_NAME));      
        context.setAttribute(TYPE, UfoContextVO.REPORT_TABLE);
        context.setAttribute(PRIVATE, Boolean.valueOf(getParameter("isPrivateReport")).booleanValue());
        context.setFormatRight(Integer.parseInt(getParameter(FORMAT_RIGHT)));
       
        try {
			String dataSourceID = getParameter("defaultDS");
			if (dataSourceID != null) {
				DataSourceVO dataSourceVO = DataSourceBO_Client
						.loadDataSByID(dataSourceID);
				if(dataSourceVO != null){
					dataSourceVO.setLoginUnit(getParameter("DSunit"));
					dataSourceVO.setLoginName(getParameter("DSuser"));
					dataSourceVO.setUnitId(getParameter("DSunit"));
					String dsPassword = nc.bs.iufo.toolkit.Encrypt.decode(
							getParameter("DSpassword")
							, dataSourceID);
					dataSourceVO.setLoginPassw(dsPassword);
					dataSourceVO.setLoginDate(getParameter(IDataSourceParam.DS_DATE));
					context.setAttribute(DATA_SOURCE, dataSourceVO);
				}
				
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		
		context.setAttribute(CURRENT_LANG, getParameter("localCode"));
		String strInTask=getParameter("inTask");
		boolean bInTask=false;
		if(strInTask!=null)
			bInTask=Boolean.parseBoolean(strInTask);
		context.setAttribute(IN_TASK, bInTask);
		context.setAttribute(CUR_UNIT_CODE, CacheProxy.getSingleton().getUnitCache().getUnitInfoByPK((String)context.getAttribute(CREATE_UNIT_ID)).getCode());
		
		String strIsHrReport = getParameter(RepToolAction.PARAMETER_HRLICENCE);
		String strIsNetReport = getParameter(RepToolAction.PARAMETER_NETLICENCE);
		String strIsHBBBReport = getParameter(RepToolAction.PARAMETER_HBBBLICENCE);
		LicenseValue.setHRReport(Boolean.valueOf(strIsHrReport));
		LicenseValue.setNetReport(Boolean.valueOf(strIsNetReport));
		LicenseValue.setHBBB(Boolean.valueOf(strIsHBBBReport));
		
        return context;
}
  
    /**
     * ��ie���á�
     * @return
     */
    public boolean isDirty(){
    	return getReport().isDirty() || getReport().getCellsModel().getPrintSet().isDirty();
    }
    /**
     *��ie���á�
     * @i18n uiiufofmt00014=���Ե�ǰ����ֻ�в鿴Ȩ�ޣ����ܱ��汨��
     */
    public String save(){
    	try {
    		UfoContextVO contextVO = (UfoContextVO) getReport().getContextVo();
    		if((Integer)contextVO.getAttribute(FORMAT_RIGHT) == RIGHT_FORMAT_READ){
    			String errMsg = StringResource.getStringResource("uiiufofmt00014");
    			UfoPublic.showConfirmDialog(this, errMsg);
    			getReport().setDirty(false);
    			return "";
    		}
//    		if(contextVO.getFormatRight() == UfoContextVO.RIGHT_FORMAT_PERSONAL){
//    			UfoPublic.showConfirmDialog(this, "��ʾ�����Ե�ǰ������޸�ֻ�и��Ի���ʽ���Ա���ɹ���");
//    		}
    		UfoReport report = getReport();
    		FormatCorePlugin formatCorePlugin = (FormatCorePlugin)getReport().getPluginManager().getPlugin(FormatCorePlugin.class.getName());
            if(!formatCorePlugin.verifyBeforeSave()){
                return "";
            }
    		if(isDirty()){
    			UfoContextVO context = (UfoContextVO)report.getContextVo();
    			report.setDirty(!CellsModelOperator.saveReportFormat(context, report.getCellsModel()));
    		}
        	
		} catch (Exception e) {
			return e.getMessage();
		}	
		return "";
    }
    @Override
    public void start() {
    	UfoReport report = getReport();
    	report.getTable().getCells().requestFocusInWindow();
    	report.setFocusComp(report.getTable().getCells());
    	
    	// @edit by wangyga at 2009-1-5,����09:28:18
    	//׼���������¼��Ļ���,
        RefData.setProxy(new IufoRefData());      
    }
    private UfoReport getReport(){
    	return getUfoReport(); 
    }
}
   