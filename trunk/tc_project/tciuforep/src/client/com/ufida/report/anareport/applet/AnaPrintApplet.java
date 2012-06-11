/**
 * 
 */
package com.ufida.report.anareport.applet;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import nc.itf.iufo.freequery.IMember;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.datasource.DataSourceBO_Client;
import nc.ui.iuforeport.rep.RepToolAction;
import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.unit.UnitInfoVO;

import com.ufida.dataset.Context;
import com.ufida.dataset.Parameter;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.anareport.model.AnaReportCondition;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaParameter;
import com.ufida.report.anareport.model.ReportParameter;
import com.ufida.report.rep.model.IBIContextKey;
import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.rep.model.BaseReportModel;
import com.ufsoft.iufo.fmtplugin.ContextFactory;
import com.ufsoft.iuforeport.batchprint.BatchPrintApplet1;
import com.ufsoft.iuforeport.batchprint.IUFODoc;
import com.ufsoft.iuforeport.batchprint.IUFOMultiDoc;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.iuforeport.freequery.FreeQueryTranceObj;
import com.ufsoft.iuforeport.tableinput.applet.DataSourceInfo;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputAppletParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.table.CellsModel;

/**
 * @author guogang
 * @created at Feb 11, 2009,3:39:30 PM
 *
 */
public class AnaPrintApplet extends BatchPrintApplet1{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Override
	protected IUFOMultiDoc initDoc(List<TableInputTransObj> datas) {

		return new AnaMultiDoc(datas,initContext());
	}

	/**
	 * context的公共属性
	 * @create by guogang at Feb 20, 2009,3:02:09 PM
	 *
	 * @return
	 */
	private Context initContext(){
		String userId=getParameter(ITableInputAppletParam.PARAM_OPER_USERPK);
		String unitId=getParameter(ITableInputAppletParam.PARAM_OPER_UNITPK);
		String orgPK=getParameter(RepToolAction.PARAMETER_ORGPK);
        
		BIContextVO contextVO = new BIContextVO();
		contextVO.setLang(getParameter("localCode"));//语种信息
		contextVO.setCurUserID(userId);
		contextVO.setOrgID(orgPK);
		contextVO.setLoginUnitID(unitId);
		//add by guogang 2009-2-21
		String pageDims=getParameter("PageDim");
        String areaParams=getParameter("AreaParam");
		contextVO.setAttribute("PageDim", pageDims);
		contextVO.setAttribute("AreaParam", areaParams);
		
		UnitInfoVO unitInfo = IUFOUICacheManager.getSingleton().getUnitCache().getUnitInfoByPK(unitId);
		if (unitInfo != null) {
			String unitValue = unitInfo.getPropValue(orgPK);
			contextVO.setUnitValue(unitValue);
		}
		//数据源信息
		DataSourceInfo curDSInfo = getCurDataSourceInfo();
		contextVO.setAttribute(FreeQueryContextVO.DATA_SOURCEINFO, curDSInfo);
		
	    try {
			String dataSourceID = curDSInfo.getDSID();
			if (dataSourceID != null) {
				DataSourceVO dataSourceVO = DataSourceBO_Client
						.loadDataSByID(dataSourceID);
				if(dataSourceVO != null){
					dataSourceVO.setLoginUnit(curDSInfo.getDSUnitPK());
					dataSourceVO.setLoginName(curDSInfo.getDSUserPK());
					dataSourceVO.setUnitId(curDSInfo.getDSUnitPK());
					String dsPassword = nc.bs.iufo.toolkit.Encrypt.decode(
							curDSInfo.getDSPwd()
							, dataSourceID);
					dataSourceVO.setLoginPassw(dsPassword);
					dataSourceVO.setLoginDate(curDSInfo.getDSDate());
					contextVO.setAttribute(IBIContextKey.DATA_SOURCE, dataSourceVO);
				}
				ContextFactory.addReplaceParam2Context(contextVO);
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		

		return contextVO;
		
	}
	
	private DataSourceInfo getCurDataSourceInfo() {
		String strDSID = getParameter(ITableInputAppletParam.DS_DEFAULT);
		String strDSUnitPK = getParameter(ITableInputAppletParam.DS_UNIT);
		String strDSUserPK = getParameter(ITableInputAppletParam.DS_USER);
		//				String strDSPwd = nc.bs.iufo.toolkit.Encrypt.decode(getParameter(IDataSourceParam.DS_PASSWORD), strDSID);
		String strNotEncodedDSPwd =getParameter(ITableInputAppletParam.DS_PASSWORD);
		String strDSDate = getParameter(ITableInputAppletParam.DS_DATE);
		DataSourceInfo curDSInfo = new DataSourceInfo(strDSID, strDSUnitPK,
				strDSUserPK, strNotEncodedDSPwd, strDSDate);
		return curDSInfo;
	}
	private class AnaMultiDoc extends IUFOMultiDoc {
        
		private AnaRenderEditorPlugin render=new AnaRenderEditorPlugin();
		private Context biContext;
		private ReportSrv srv;
		/**
		 * @create by guogang at Feb 16, 2009,2:14:11 PM
		 * 
		 * @param transObj
		 */
		public AnaMultiDoc(List<TableInputTransObj> transObj,Context biContext) {
			super(transObj);
			this.biContext=biContext;
			srv=new ReportSrv();
		}

		@Override
		protected void printBeforeProcessor(IUFODoc doc) throws IOException {
			if(doc.getPrintData()!=null){
				render.register(doc.getPrintData().getReanderAndEditor());
			}
		}
        
		
		@Override
		protected CellsModel loadCellsModel(TableInputTransObj inputObj) {

			String[] strRepPks = new String[] { inputObj.getPrintParam()
					.getRepID() };
			CellsModel data=null;
			try {
				
				
				ReportVO[] vos = (ReportVO[])srv.getByIDs(strRepPks);
		    	if(vos == null && vos.length == 0)
		    		return data;
		    	biContext.setAttribute(IBIContextKey.REPORT_PK, vos[0].getID());
		    	biContext.setAttribute(IBIContextKey.REPORT_CODE, vos[0].getReportcode());
		    	BaseReportModel model = (BaseReportModel)vos[0].getDefinition();
				if(model instanceof AnaReportModel){
					((AnaReportModel)model).setContextVO(biContext);
					 processPageDim((AnaReportModel)model);
					 processAreaParam(((AnaReportModel)model).getReportParams(true));
					 ((AnaReportModel)model).loadAllData(true);//加载数据
					 data=model.getCellsModel();
				}
				return data;
			} catch (Exception e) {
				AppDebug.debug(e);
				throw new IllegalArgumentException(e);
			}

		}
        /**
         * 设置要打印的纬度
         * @create by guogang at Feb 21, 2009,2:58:19 PM
         *
         * @param pageDimFields
         */
		private void processPageDim(AnaReportModel model){
			String pageDims=(String)biContext.getAttribute("PageDim");
			String[] pageDimValues=null;
			if(pageDims==null||pageDims.length()<1){
				return ;
			}else{
				pageDimValues=pageDims.split("@");
			}
			AnaReportCondition[] pageDimFields=model.getReportCondition().toArray(new AnaReportCondition[0]);
			
			if(pageDimFields.length>=pageDimValues.length){
				for(int i=0;i<pageDimValues.length;i++){
					
					IMember[] allMembers =  pageDimFields[i].getAllValues();
					if(allMembers==null){
						pageDimFields[i].setReportModel(model);
						allMembers =  pageDimFields[i].getAllValues();
					}
					if(allMembers != null){
						if( pageDimFields[i].getSelectedValue().getMemcode().equals(pageDimValues[i]) == false ){
			    			for(int k = 0; k < allMembers.length; k++){
			    				if(allMembers[k].getMemcode().equalsIgnoreCase(pageDimValues[i])){
			    					pageDimFields[i].setSelectedValue(allMembers[k]);
			    					break;
			    				}
			    			}
						}
					}
				}
			}
			
		}
		
		/**
		 * 设置要打印的区域参数
		 * @create by guogang at Feb 21, 2009,3:11:35 PM
		 *
		 * @param repParams
		 */
		private void processAreaParam(ReportParameter repParams){
			String areaParamStr=(String)biContext.getAttribute("AreaParam");
			String[] areaParamValues=null;
			if(areaParamStr==null||areaParamStr.length()<1){
				return ;
			}else{
				areaParamValues=areaParamStr.split(",");
			}
			Hashtable<String,String> paramValues=new Hashtable<String,String>();
			for(int i=0;i<areaParamValues.length;i++){
				String[] strVals=areaParamValues[i].split("@");
				paramValues.put(strVals[0], strVals[1]);
			}
			String paramName="";
			String paramValue=null;
			if(repParams.getSize()>0){
				AreaParameter[] areaParams = repParams.getAllParams();
				for (int i = 0; i < areaParams.length; i++) {
					AreaParameter aPara = areaParams[i];
					if(aPara!=null){
						Parameter[] ps=aPara.getParams();
						for(int j=0;j<ps.length;j++){
							paramName=aPara.getAreaPK()+"."+ps[j].getName();
							paramValue=paramValues.get(paramName);
							if(paramValue!=null){
								ps[j].setValue(paramValue);
							}else{
								ps[j].setValue("");
							}
						}
						
					}
					
				}
			}
		}
	}
	

}
