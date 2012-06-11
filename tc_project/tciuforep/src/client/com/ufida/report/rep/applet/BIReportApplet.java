package com.ufida.report.rep.applet;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import nc.pub.iufo.cache.TaskCache;
import nc.ui.bi.query.designer.BIDesignApplet;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.datasource.DataSourceBO_Client;
import nc.ui.pub.dsmanager.DatasetTree;
import nc.us.bi.report.manager.BIReportSrv;
import nc.vo.bi.report.manager.ReportResource;
import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.pub.DataManageObjectIufo;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iufo.unit.UnitPropVO;
import nc.vo.pub.ValueObject;

import com.ufida.bi.base.BIException;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.adhoc.applet.AdhocPlugin;
import com.ufida.report.anareport.applet.AnaReportPluginRegister;
import com.ufida.report.anareport.model.AnaRepDataSource;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.chart.applet.DimChartPlugin;
import com.ufida.report.complexrep.applet.ComplexRepPlugin;
import com.ufida.report.free.applet.FreeQueryDesignePlugin;
import com.ufida.report.multidimension.applet.DimensionPlugin;
import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.rep.model.BaseReportModel;
import com.ufida.report.rep.model.IBIContextKey;
import com.ufida.report.spreedsheet.applet.SpreadCellAttrPlugin;
import com.ufida.report.spreedsheet.applet.SpreadSheetPlugin;
import com.ufsoft.iufo.fmtplugin.ContextFactory;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNaviMenu;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.iuforeport.freequery.FreeQueryTranceObj;
import com.ufsoft.iuforeport.tableinput.applet.DataSourceInfo;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputAppletParam;
import com.ufsoft.iuforeport.tableinput.applet.ReportViewType;
import com.ufsoft.report.ReportStyle;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.sysplugin.cellattr.CellAttrPlugin;
import com.ufsoft.report.sysplugin.combinecell.CombineCellPlugin;
import com.ufsoft.report.sysplugin.edit.EditPlugin;
import com.ufsoft.report.sysplugin.excel.ExcelExpPlugin;
import com.ufsoft.report.sysplugin.fill.FillPlugin;
import com.ufsoft.report.sysplugin.headerlock.HeaderLockPlugin;
import com.ufsoft.report.sysplugin.headersize.HeaderSizePlugin;
import com.ufsoft.report.sysplugin.insertdelete.InsertDeletePlugin;
import com.ufsoft.report.sysplugin.print.PrintPlugin;

/**
 * BI中运行的applet。
 * 
 * @author zzl 2005-5-12
 */
public class BIReportApplet extends BIDesignApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	   /**
	    * applet对应的综合查询导航菜单
	    * liuyy
	    */
		private QueryNaviMenu m_querymenu = null;
		
		private transient static String reportCode=null;
		
		private boolean isNeedSave = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.bi.query.designer.BIDesignApplet#initUI()
	 */
	/**
	 * @i18n mbirep00020=开始加载!
	 */
	public void initUI() {
		AppDebug.debug("开始加载!");// @devTools
																		// System.out.println("开始加载!");
		int reportType = ReportResource.INT_REPORT_ADHOC;
		if (getParameter(ReportResource.REPORT_TYPE) != null) {
			reportType = Integer.parseInt(getParameter(ReportResource.REPORT_TYPE));
		}

		int operation = UfoReport.OPERATION_FORMAT;// 初始状态，默认为格式状态
		if (getParameter(ReportResource.OPERATE_TYPE) != null) {
			try {
				operation = Integer.parseInt(getParameter(ReportResource.OPERATE_TYPE));
			} catch (Exception e) {
				operation = UfoReport.OPERATION_FORMAT;
			}
		}

		BIContextVO contextVO = getBIContextVO(this);
		contextVO.setReportType(reportType);
		isNeedSave = (operation == UfoReport.OPERATION_FORMAT);//标记是否可以修改报表格式
		setAppletRootPane(getUfoReport(this, operation, reportType, contextVO));
	}

	public void setAppletRootPane(UfoReport report) {

		setUfoReport(report);
		
    	report.getTable().getCells().requestFocusInWindow();
    	report.setFocusComp(report.getTable().getCells());

	}

	/**
	 * @i18n mbirep00021=开始加载initPlugins!
	 * @i18n mbirep00022=开始加载setContentPane
	 */
	public static UfoReport getUfoReport(BIReportApplet applet, int operation, int reportType, BIContextVO contextVO) {
		UfoReport report = new UfoReport(operation, contextVO);
		AppDebug.debug("开始加载initPlugins!");// @devTools
		// System.out.println("开始加载initPlugins!");

		initPlugins(applet, report, reportType);
		AppDebug.debug("开始加载setContentPane");// @devTools
		// System.out.println("开始加载setContentPane");
		return report;
	}

	/**
	 * @i18n mbirep00020=开始加载!
	 * @i18n mbirep00023=Applet参数ReportResource.OPERATE_TYPE：
	 * @i18n mbirep00024=Applet参数ReportResource.REPORT_ID：
	 * @i18n mbirep00025=Applet参数USER：
	 * @i18n miufo00303=Applet参数UNITID：
	 */
	public static BIContextVO getBIContextVO(BIReportApplet applet) {
		AppDebug.debug("开始加载!");

		String pk_report = applet.getParameter(ReportResource.REPORT_ID);
		String user = applet.getParameter(ReportResource.USER_ID) == null ? ReportResource.DEFAULT_USER_ID : applet
				.getParameter(ReportResource.USER_ID);
		String unitID = applet.getParameter(ReportResource.UNIT_ID) == null ? ReportResource.DEFAULT_UNIT_ID : applet
				.getParameter(ReportResource.UNIT_ID);
		String orgPk = applet.getParameter(ReportResource.ORG_PK) == null ? UnitPropVO.BASEORGPK: applet
				.getParameter(ReportResource.ORG_PK);
		

		String loadFromSessionStr = applet.getParameter(ReportResource.LOAD_FROM_SESSION);
		loadFromSessionStr = loadFromSessionStr == null ? "false" : loadFromSessionStr;
		boolean loadedFromSession = Boolean.parseBoolean(loadFromSessionStr);
		String strSessionID = applet.getParameter(ReportResource.SESSION_ID);

		AppDebug.debug("Applet参数ReportResource.OPERATE_TYPE：" + applet.getParameter(ReportResource.OPERATE_TYPE));
		AppDebug.debug("Applet参数ReportResource.REPORT_ID：" + pk_report);
		AppDebug.debug("Applet参数USER：" + user);
		AppDebug.debug("Applet参数UNITID：" + unitID);

		BIContextVO contextVO = new BIContextVO();
		contextVO.setLang(applet.getParameter("localCode"));//语种信息
		contextVO.setAttribute("SERVER_PORT", applet.getParameter("SERVER_PORT"));
		BaseReportModel model = getBaseReportModel(pk_report, loadedFromSession, strSessionID);
		contextVO.setBaseReportModel(model);
		contextVO.setReportPK(pk_report);
		contextVO.setLoadedFromSession(loadedFromSession);

		String strTaskPK = applet.getParameter(ReportResource.TASK_ID);
		contextVO.setTaskID(strTaskPK);
		TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
		TaskVO taskVO = taskCache.getTaskVO(strTaskPK);

		if (taskVO != null) {
			contextVO.setKeyGroupID(taskVO.getKeyGroupId());
		}

		// add by ljhua 解决电子表格中公式插件需要报表编码的问题
		if(reportCode==null){
			reportCode= getReportCode(pk_report);
		}
 
		contextVO.setReportcode(reportCode);
		contextVO.setCurUserID(user);
		contextVO.setOrgID(orgPk);
		contextVO.setLoginUnitID(unitID);
		UnitInfoVO unitInfo = IUFOUICacheManager.getSingleton().getUnitCache().getUnitInfoByPK(unitID);
		if (unitInfo != null) {
			String unitValue = unitInfo.getPropValue(orgPk);
			contextVO.setUnitValue(unitValue);
		}
		//数据源信息
		DataSourceInfo curDSInfo = getCurDataSourceInfo(applet);
		contextVO.setAttribute(FreeQueryContextVO.DATA_SOURCEINFO,curDSInfo);
		
		//@edit by liuyy at 2008-12-25 下午08:49:04 添加datasource
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
				//@add by:yza :-> 加入NC宏变量
				ContextFactory.addReplaceParam2Context(contextVO);
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		

		return contextVO;
	}

	/**
	 * 根据参数中传来的'业务日期''单位''用户'信息初始化客户端环境。 创建日期：(00-7-19 10:35:05)
	 */
	protected void initClientEnvironment() {
		String strDisplayZero = getParameter(ITableInputAppletParam.PARAM_TI_DISPLAYZERO);
		boolean bDisplayZero = "true".equalsIgnoreCase(strDisplayZero) ? true
				: false;
		ReportStyle.setShowZero(bDisplayZero);
	}

	public static BaseReportModel getBaseReportModel(String reportPk) {
		BaseReportModel model = getBaseReportModel(reportPk, false, null);
		if (model != null)// 回写报表主键(数据权限需要)
			model.setPK(reportPk);
		return model;
	}

	
	public static BaseReportModel getBaseReportModel(String reportPk, boolean loadFromSession, String strSessionID) {
		boolean local = false; // 调试的时候可从本地直接打开
		try {
			if (local) {// 从本地打开

				boolean newReport = false; // 是否建立新的报表

				if (newReport) {
					return null;
				}
				ObjectInputStream in;
				BaseReportModel model;
				try {
					String filePath = "C:\\BIReport.rep";
					in = new ObjectInputStream(new FileInputStream(filePath));
					model = (BaseReportModel) in.readObject();
				} catch (Exception e1) {
					return null;
				}
				in.close();
				return model;
			} else {
				// 如果是从Session中加载的方式，则首先查找Session
				if (loadFromSession){
					Object resFromSession =  doLinkServletTask(reportPk, strSessionID);
					if(resFromSession instanceof BaseReportModel)
						return (BaseReportModel)resFromSession;
				}

				// 调用服务器后台服务加载
				ReportSrv reportSrv = new ReportSrv();
				ValueObject[] vos = reportSrv.getByIDs(new String[] { reportPk });

				if (vos == null || vos.length == 0)
					return null;

				if (vos[0] == null || !(vos[0] instanceof ReportVO))
					return null;

				ReportVO reportVO = (ReportVO) vos[0];
				reportCode=reportVO.getReportcode();
				return (BaseReportModel) reportVO.getDefinition();
			}
		} catch (Exception e) {
			AppDebug.debug(e);// @devTools AppDebug.debug(e);
		}

		return null;
	}
	public static void initPlugins(BIReportApplet applet, final UfoReport report, int reportType) {
		initPlugins(report, reportType);
		if(reportType == ReportResource.INT_REPORT_FREE && applet != null)
				initWindowNav(applet, report);
		
	}
	// 初始化Adhoc报表所用的插件集合。
	public static void initPlugins(final UfoReport report, int reportType) {
		// 删除除系统插件以外的所有插件。

		// 添加插件。
		if (reportType == ReportResource.INT_REPORT_ADHOC) {
			report.addPlugIn(PrintPlugin.class.getName());
			report.addPlugIn(AdhocPlugin.class.getName());
			report.addPlugIn(ExcelExpPlugin.class.getName());
		} else if (reportType == ReportResource.INT_REPORT_MULTI) {
			report.addPlugIn(PrintPlugin.class.getName());
			report.addPlugIn(HeaderSizePlugin.class.getName());
			report.addPlugIn(DimensionPlugin.class.getName());
		} else if (reportType == ReportResource.INT_REPORT_SPREADSHT) {
			report.addPlugIn(EditPlugin.class.getName());
			report.addPlugIn(InsertDeletePlugin.class.getName());
			report.addPlugIn(CombineCellPlugin.class.getName());
			report.addPlugIn(FillPlugin.class.getName());
			report.addPlugIn(HeaderSizePlugin.class.getName());
			report.addPlugIn(PrintPlugin.class.getName());

			report.addPlugIn(SpreadCellAttrPlugin.class.getName());
			report.addPlugIn(SpreadSheetPlugin.class.getName());

			// TODO 需要添加公式插件
			// report.addPlugIn(FormulaDefPlugin.class.getName(),new
			// SpreadFormulaPlugin());

		} else if (reportType == ReportResource.INT_REPORT_COMPLEX) {
			report.addPlugIn(CellAttrPlugin.class.getName());
			report.addPlugIn(ComplexRepPlugin.class.getName());
		} else if (reportType == ReportResource.INT_REPORT_CHART) {
			report.addPlugIn(CellAttrPlugin.class.getName());
			report.addPlugIn(DimChartPlugin.class.getName());
		}else if (reportType == ReportResource.INT_REPORT_FREE) {
			report.addPlugIn(PrintPlugin.class.getName());
			report.addPlugIn(AdhocPlugin.class.getName());
			report.addPlugIn(FreeQueryDesignePlugin.class.getName());
			report.addPlugIn(HeaderLockPlugin.class.getName());
		}else if (reportType == ReportResource.INT_REPORT_ANALYZE) {
			new AnaReportPluginRegister(report).registerPlugIn();
		}

	}
	//see TableInputApplet
	private static void initWindowNav(final BIReportApplet applet, UfoReport report){
   
		QueryNaviMenu menu = new QueryNaviMenu(applet, ReportViewType.VIEW_FREEQUERY);
		applet.m_querymenu = menu;
		menu.add(report, "");
		QueryNavigation.getSingleton().addMenu(menu);
		
		QueryNavigation.getSingleton().setCurWindow(applet.m_querymenu);
		QueryNavigation.refreshMenu(report);
		
		applet.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
				QueryNaviMenu curMenu = QueryNavigation.getSingleton().getCurWindow();
				if(curMenu != null && curMenu == applet.m_querymenu){
					return;
				}
				QueryNavigation.getSingleton().focusMenu(applet.m_querymenu);
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseReleased(MouseEvent e) {
			}
		});
		
//  	this.addFocusListener(new FocusListener() {
//			public void focusGained(FocusEvent e) {
//				QueryNavigation.getSingleton().focusMenu(m_querymenu);
//			}
//			public void focusLost(FocusEvent e) {
//			}
//		});
  	
  	//********end. liuyy  *****//
	}
	public static String getReportCode(String strRepPK) {
		if (strRepPK == null)
			return null;

		try {
			ReportVO rep = BIReportSrv.getInstance().getByID(strRepPK);
			if (rep != null)
				return rep.getReportcode();
		} catch (Exception e) {
			AppDebug.debug(e);
		}

		return null;
	}

	/**
	 * Applet方式下，重新加载插件和报表模型
	 * 
	 */

	public static void drillThrough(BaseReportModel newModel, UfoReport report, int operationState) throws BIException {

		// 加载目标报表
		if (newModel == null) {
			throw new BIException(StringResource.getStringResource("uibiadhoc00005"));
		}

		// 设置环境信息
		BIContextVO contextVO = (BIContextVO) report.getContextVo();
		contextVO.setReportPK(newModel.getPK());
		// add by ljhua 解决电子表格中公式插件需要报表编码的问题
		String strRepCode = getReportCode(newModel.getPK());
		contextVO.setReportcode(strRepCode);

		contextVO.setBaseReportModel(newModel);

		// 替换AppletRootPane
		BIReportApplet applet = (BIReportApplet) report.getParent();

		// 加载新的插件，并设置格式/浏览状态
		UfoReport newReport = BIReportApplet.getUfoReport(applet, operationState, newModel.getReportType(), contextVO);
		// newReport.setOperationState(operationState);

		applet.setAppletRootPane(newReport);

		applet.validate();
		applet.invalidate();
		applet.repaint();
	}

	/**
	 * 执行连接Servlet的操作
	 * 
	 * @param reportPk
	 * @param strSessionID
	 * @return
	 */
	public static Object doLinkServletTask(String reportPk, String strSessionID) {
		Object returnObj = null;
		returnObj = BILinkServletUtil.linkBIReportOperServlet(reportPk, strSessionID);
		return returnObj;
	}
	/**
	 * 获得数据源信息，复制于TableInputApplet
	 * @return
	 */
	public static DataSourceInfo getCurDataSourceInfo(BIReportApplet applet) {
		String strDSID = applet.getParameter(ITableInputAppletParam.DS_DEFAULT);
		String strDSUnitPK = applet.getParameter(ITableInputAppletParam.DS_UNIT);
		String strDSUserPK = applet.getParameter(ITableInputAppletParam.DS_USER);
		//				String strDSPwd = nc.bs.iufo.toolkit.Encrypt.decode(getParameter(IDataSourceParam.DS_PASSWORD), strDSID);
		String strNotEncodedDSPwd = applet.getParameter(ITableInputAppletParam.DS_PASSWORD);
		String strDSDate = applet.getParameter(ITableInputAppletParam.DS_DATE);
		DataSourceInfo curDSInfo = new DataSourceInfo(strDSID, strDSUnitPK,
				strDSUserPK, strNotEncodedDSPwd, strDSDate);
		return curDSInfo;
	}

	@Override
	public boolean needSave() {
		return isNeedSave;
	}
	/**
	 * 查询自由报表引用的数据集中是否存在指定类型数据集
	 * @param reportPK
	 * @param types
	 * @return
	 */
	public static boolean isDataSetInTypes(String reportPK, String[] kinds){
		BaseReportModel report = getBaseReportModel(reportPK);
		if(report == null || !(report instanceof AnaReportModel))
			return false;
		AnaRepDataSource dataSource =((AnaReportModel)report).getDataSource();
		if(dataSource == null)
			return false;
		String[] dsPKs = dataSource.getAllDSPKs();
		if(dsPKs == null || dsPKs.length==0)
			return false;
		//noted by csli:此处传入的kinds是exclude kind，即加载除kinds外的数据集类型。
		DatasetTree dsTree = (DatasetTree)DatasetTree.getInstance(DataManageObjectIufo.IUFO_DATASOURCE, reportPK, false, false, kinds);
		for (int i = 0; i < dsPKs.length; i++) {
			if(!dsTree.isExist(dsPKs[i]))
				return true;
		}
		return false;
	}

} 