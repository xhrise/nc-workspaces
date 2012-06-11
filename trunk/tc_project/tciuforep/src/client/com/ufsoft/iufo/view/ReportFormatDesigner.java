package com.ufsoft.iufo.view;

import java.util.ArrayList;

import nc.pub.iufo.cache.TaskCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.datasource.DataSourceBO_Client;
import nc.ui.iufo.input.table.IufoRefData;
import nc.ui.iufo.query.datasetmanager.DataSetManagerPlugin;
import nc.ui.iufo.resmng.uitemplate.describer.ModuleProductHome;
import nc.ui.iuforeport.rep.RepToolAction;
import nc.util.iufo.resmng.IResMngConsants;
import nc.vo.iufo.authorization.IAuthorizeTypes;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iuforeport.applet.IDataSourceParam;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.dataset.Context;
import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.exception.MessageException;
import com.ufida.zior.plugin.system.RefreshViewPlugin;
import com.ufida.zior.util.UIUtilities;
import com.ufsoft.iufo.fmtplugin.BDContextKey;
import com.ufsoft.iufo.fmtplugin.businessquery.BusinessQueryPlugin;
import com.ufsoft.iufo.fmtplugin.datapreview.DataPreviewPlugin;
import com.ufsoft.iufo.fmtplugin.dataprocess.DataProcessPlugin;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaDefPlugIn;
import com.ufsoft.iufo.fmtplugin.excel.ExcelImpPlugin;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaPlugin;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.fmtplugin.key.KeyDefPlugin;
import com.ufsoft.iufo.fmtplugin.measure.MeasurePlugin;
import com.ufsoft.iufo.fmtplugin.monitor.MonitorPlugin;
import com.ufsoft.iufo.fmtplugin.rounddigit.RoundDigitPlugin;
import com.ufsoft.iufo.fmtplugin.statusshow.StatusBarPlugin;
import com.ufsoft.iufo.fmtplugin.sumfunc.SumFuncPlugin;
import com.ufsoft.iufo.inputplugin.inputcore.RefData;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportAuth;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.sysplugin.cellattr.CellAttrPlugin;
import com.ufsoft.report.sysplugin.cellpostil.CellPostilDefPlugin;
import com.ufsoft.report.sysplugin.combinecell.CombineCellPlugin;
import com.ufsoft.report.sysplugin.editplugin.RepEditPlugin;
import com.ufsoft.report.sysplugin.excel.ExcelExpPlugin2;
import com.ufsoft.report.sysplugin.fillcell.FillCellPlugin;
import com.ufsoft.report.sysplugin.findreplace.FindReplacePlugin;
import com.ufsoft.report.sysplugin.headersize.HeaderSizePlugin;
import com.ufsoft.report.sysplugin.help.HelpPlugin;
import com.ufsoft.report.sysplugin.insertdelete.InsertDeletePlugin;
import com.ufsoft.report.sysplugin.insertimg.InsertDelImgPlugin;
import com.ufsoft.report.sysplugin.location.LocationPlugin;
import com.ufsoft.report.sysplugin.pagination.PaginationPlugin;
import com.ufsoft.report.sysplugin.print.PrintPlugin;
import com.ufsoft.report.sysplugin.repheaderlock.RepHeaderLockPlugin;
import com.ufsoft.report.sysplugin.repstyle.RepStylePlugin;
import com.ufsoft.report.sysplugin.viewmanager.ViewManagerPlugin;
import com.ufsoft.report.toolbar.CellAttrToolBarPlugIn;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.undo.UndoPlugin;

/**
 * 采集表格式设计器
 * 
 * @author liuyy
 * 
 */
public class ReportFormatDesigner extends ReportDesigner {

	private static final long serialVersionUID = 6262710974919678702L;

	@Override
	public String[] createPluginList() {
		ArrayList<String> pluginList = new ArrayList<String>();
		pluginList.add(RefreshViewPlugin.class.getName());
		pluginList.add(ExcelImpPlugin.class.getName());
		pluginList.add(ExcelExpPlugin2.class.getName());
		pluginList.add(DataPreviewPlugin.class.getName());
		pluginList.add(PrintPlugin.class.getName());
		pluginList.add(UndoPlugin.class.getName());
		pluginList.add(RepEditPlugin.class.getName());
		pluginList.add(InsertDeletePlugin.class.getName());
		pluginList.add(LocationPlugin.class.getName());
		pluginList.add(InsertDelImgPlugin.class.getName());
		pluginList.add(FindReplacePlugin.class.getName());
		pluginList.add(RepStylePlugin.class.getName());
		pluginList.add(FillCellPlugin.class.getName());
		pluginList.add(SumFuncPlugin.class.getName());
		pluginList.add(CombineCellPlugin.class.getName());
		pluginList.add(CellAttrToolBarPlugIn.class.getName());
		pluginList.add(CellAttrPlugin.class.getName());
		pluginList.add(KeyDefPlugin.class.getName());
		pluginList.add(MeasurePlugin.class.getName());
		pluginList.add(FormulaPlugin.class.getName());
		pluginList.add(RoundDigitPlugin.class.getName());
		pluginList.add(CellPostilDefPlugin.class.getName());
		pluginList.add(PaginationPlugin.class.getName());
		pluginList.add(RepHeaderLockPlugin.class.getName());
		pluginList.add(HeaderSizePlugin.class.getName());
		pluginList.add(DynAreaDefPlugIn.class.getName());
		pluginList.add(BusinessQueryPlugin.class.getName());
		pluginList.add(DataProcessPlugin.class.getName());
		if(!isAnaRep()){
			pluginList.add(DataSetManagerPlugin.class.getName());
		}
		pluginList.add(MonitorPlugin.class.getName());
		pluginList.add(HelpPlugin.class.getName());
		pluginList.add(StatusBarPlugin.class.getName());
		pluginList.add(ViewManagerPlugin.class.getName());
		return pluginList.toArray(new String[0]);
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
	}

	@Override
	public void startup() {

		super.startup();
		
		//xlm 2009-08-12 初始化参照数据访问代理,解决参照显示错误问题
		//准备代码参照录入的环境
        RefData.setProxy(new IufoRefData());   
		CellsModel cellsModel = CellsModelOperator
				.getFormatModelByPKWithDataProcess(getContext());
		CellsModelOperator.initModelProperties(getContext(), cellsModel);
		cellsModel.setDirty(false);

		setCellsModel(cellsModel);
		cellsModel.setCellsAuth(new ReportAuth(getCellsPane(), getContext()));
		ReportVO repVO = (ReportVO) IUFOUICacheManager.getSingleton()
				.getReportCache().get(getReportId());
		String strRepType = (String) getContext().getAttribute(
				ReportContextKey.REPORT_TYPE);

		setTitle(getTitle(Integer.parseInt(strRepType),repVO));

		// 初始化公式执行器
		FormulaModel formulaModel = FormulaModel.getInstance(getCellsModel());
		formulaModel.setUfoFmlExecutor(UfoFmlExecutor.getInstance(
				(Context) getContext(), getCellsModel(), true));

	}

	/**
	 * 此context包含applet参数信息，其他的参数需要在此添加
	 * 
	 * @create by wangyga at 2009-3-17,下午08:25:06
	 * 
	 */
	protected void initContext() {

		try {
			String reportPK=getReportId();
			getContext()
					.setAttribute(ReportContextKey.REPORT_PK, reportPK);
			// 设置报表创建者所属单位
			ReportVO repVO = (ReportVO) IUFOUICacheManager.getSingleton()
					.getReportCache().get(reportPK);
			if (repVO == null) {
				throw new IllegalArgumentException("repVO is null");
			}

			String strModelId = (String) getContext().getAttribute(IUfoContextKey.MODEL_ID);
			
			String strCreateUnitID = RepToolAction.getCreateUnitID(repVO,
					strModelId);
			getContext().setAttribute(IUfoContextKey.CREATE_UNIT_ID,
					strCreateUnitID);

			getContext().setAttribute(ReportContextKey.OPERATION_STATE,
					ReportContextKey.OPERATION_FORMAT);

			
			getContext().setAttribute(IUfoContextKey.IN_TASK, isInTask(reportPK));

			getContext().setAttribute(IUfoContextKey.ANA_REP, isAnaRep());
			
			getContext().setAttribute(IUfoContextKey.DATA_SOURCE, getDataSourceVO());
			
			String strUserId = (String) getContext().getAttribute(BDContextKey.CUR_USER_ID);
			String strLoginUnitId = (String) getContext().getAttribute(BDContextKey.LOGIN_UNIT_ID);
			if (strLoginUnitId != null) {
				UnitInfoVO unitInfo = IUFOUICacheManager.getSingleton()
						.getUnitCache().getUnitInfoByPK(strLoginUnitId);
				String orgPK=(String) getContext().getAttribute(BDContextKey.ORG_PK);
				if (unitInfo != null&&orgPK!=null) {
					String unitValue = unitInfo.getPropValue(orgPK);
					getContext().setAttribute(
							BDContextKey.LOGIN_UNIT_LEVELCODE, unitValue);
				}
			}
			int iFormatAuth = getFormatAuth(strUserId,strLoginUnitId,repVO,strModelId);
			getContext().setAttribute(IUfoContextKey.FORMAT_RIGHT,
					iFormatAuth);

		} catch (MessageException e) {
			UIUtilities.sendMessage(e, this);
		} catch (Throwable e) {
			AppDebug.debug(e);
		}
	}

	private DataSourceVO getDataSourceVO() throws Exception {
		String dataSourceID = (String) getContext().getAttribute(
				IUfoContextKey.DATA_SOURCE_ID);
		DataSourceVO dataSourceVO = null;
		if (dataSourceID != null) {
			dataSourceVO = DataSourceBO_Client.loadDataSByID(dataSourceID);
			dataSourceVO.setLoginName((String) getContext().getAttribute(IDataSourceParam.DS_USER));
			dataSourceVO.setLoginUnit((String) getContext().getAttribute(IDataSourceParam.DS_UNIT));
			String pwd = nc.bs.iufo.toolkit.Encrypt.decode((String)getContext().getAttribute(IDataSourceParam.DS_PASSWORD),
					dataSourceID);
			dataSourceVO.setLoginPassw(pwd);
			
			dataSourceVO.setLoginDate((String) getContext().getAttribute(IDataSourceParam.DS_DATE));
		}
		return dataSourceVO;

	}

	private String getReportId() {
		String strViewId = getId();
		if (strViewId.indexOf("_") < 0) {
			return strViewId;
		}
		return strViewId.substring(strViewId.indexOf("_") + 1);
	}

	private String getTitle(int type, ReportVO repVO){
		switch (type) {
		case ReportContextKey.REPORT_TYPE_ANALYSIS:
			return repVO.getName();// 分析表的code是随即生成的，所以不在标题中显示code
		case ReportContextKey.REPORT_TYPE_GATHER:
			return repVO.getNameWithCode();
		case ReportContextKey.REPORT_TYPE_MODEL:
			return repVO.getNameWithCode();
		default:
			break;
		}
		return null;
	}
	
	private boolean isAnaRep(){
		int iRepType = -1;
		Object obj = getContext().getAttribute(ReportContextKey.REPORT_TYPE);
		if (obj == null) {
			return false;
		}
		try {
			iRepType = Integer.parseInt(obj.toString());
		} catch (Throwable e) {
			AppDebug.debug(e);
			return false;
		}
		
		return iRepType == ReportContextKey.REPORT_TYPE_ANALYSIS ? true : false;
	}
	
	private boolean isInTask(String strRepID){
		TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
        String[] strRefTaskIds = taskCache.getTaskIdsByReportId(strRepID);
        boolean bRefTask = false;
        if (strRefTaskIds != null && strRefTaskIds.length > 0) {
            bRefTask = true;
        }
        return bRefTask;
	}
	
	/**
	 * @i18n miufohbbb00122=获得报表权限出错：
	 */
	private int getFormatAuth(String strUserId, String strUnitId, ReportVO repVO, String strModuleName){
		 int nFormatAuth = -1;
		 if(strModuleName != null && IResMngConsants.MODULE_ANALYSIS_REPORT.equals(strModuleName)){
			 return IAuthorizeTypes.AU_REPDIR_TYPE_MODIFY;
		 }
		 try {
			 if (repVO==null) {
		            nFormatAuth=IAuthorizeTypes.AU_REPDIR_TYPE_VIEW;
			 }else {
				 if (strModuleName != null) {
					boolean bAuthNoModel = ModuleProductHome.getInstance()
							.isAuthNoModule(strModuleName);
					if (!bAuthNoModel) {
						nFormatAuth = RepToolAction.getRepAuthType(repVO.getReportPK(), strUserId, strUnitId,strModuleName);
						if( nFormatAuth > IAuthorizeTypes.AU_REPDIR_TYPE_MODIFY){
    	                    nFormatAuth = IAuthorizeTypes.AU_REPDIR_TYPE_MODIFY;
    	                }
					} else {
						if(repVO.isModel() && repVO.isBuiltIn()){
							nFormatAuth=IAuthorizeTypes.AU_REPDIR_TYPE_VIEW;
						} else {
							nFormatAuth = IAuthorizeTypes.AU_REPDIR_TYPE_MODIFY;
						}
					}
				} else {
					//缺省值为最新权限类型：查看
		        	nFormatAuth=IAuthorizeTypes.AU_REPDIR_TYPE_VIEW;
				}
			}
		} catch (Throwable e) {
			AppDebug.debug(e);
			throw new MessageException(StringResource.getStringResource("miufohbbb00122")+e);
		}
		 
		return nFormatAuth;	 
	}
	
	@Override
	protected boolean save() {
		IContext context = getContext();
		if((Integer)context.getAttribute(IUfoContextKey.FORMAT_RIGHT) == IUfoContextKey.RIGHT_FORMAT_READ){
			throw new MessageException(StringResource.getStringResource("uiiufofmt00014"));
		}
		return CellsModelOperator.saveReportFormat(context,
				getCellsModel());
	}

} 