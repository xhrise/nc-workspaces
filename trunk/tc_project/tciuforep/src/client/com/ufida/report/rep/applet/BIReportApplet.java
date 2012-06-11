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
 * BI�����е�applet��
 * 
 * @author zzl 2005-5-12
 */
public class BIReportApplet extends BIDesignApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	   /**
	    * applet��Ӧ���ۺϲ�ѯ�����˵�
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
	 * @i18n mbirep00020=��ʼ����!
	 */
	public void initUI() {
		AppDebug.debug("��ʼ����!");// @devTools
																		// System.out.println("��ʼ����!");
		int reportType = ReportResource.INT_REPORT_ADHOC;
		if (getParameter(ReportResource.REPORT_TYPE) != null) {
			reportType = Integer.parseInt(getParameter(ReportResource.REPORT_TYPE));
		}

		int operation = UfoReport.OPERATION_FORMAT;// ��ʼ״̬��Ĭ��Ϊ��ʽ״̬
		if (getParameter(ReportResource.OPERATE_TYPE) != null) {
			try {
				operation = Integer.parseInt(getParameter(ReportResource.OPERATE_TYPE));
			} catch (Exception e) {
				operation = UfoReport.OPERATION_FORMAT;
			}
		}

		BIContextVO contextVO = getBIContextVO(this);
		contextVO.setReportType(reportType);
		isNeedSave = (operation == UfoReport.OPERATION_FORMAT);//����Ƿ�����޸ı����ʽ
		setAppletRootPane(getUfoReport(this, operation, reportType, contextVO));
	}

	public void setAppletRootPane(UfoReport report) {

		setUfoReport(report);
		
    	report.getTable().getCells().requestFocusInWindow();
    	report.setFocusComp(report.getTable().getCells());

	}

	/**
	 * @i18n mbirep00021=��ʼ����initPlugins!
	 * @i18n mbirep00022=��ʼ����setContentPane
	 */
	public static UfoReport getUfoReport(BIReportApplet applet, int operation, int reportType, BIContextVO contextVO) {
		UfoReport report = new UfoReport(operation, contextVO);
		AppDebug.debug("��ʼ����initPlugins!");// @devTools
		// System.out.println("��ʼ����initPlugins!");

		initPlugins(applet, report, reportType);
		AppDebug.debug("��ʼ����setContentPane");// @devTools
		// System.out.println("��ʼ����setContentPane");
		return report;
	}

	/**
	 * @i18n mbirep00020=��ʼ����!
	 * @i18n mbirep00023=Applet����ReportResource.OPERATE_TYPE��
	 * @i18n mbirep00024=Applet����ReportResource.REPORT_ID��
	 * @i18n mbirep00025=Applet����USER��
	 * @i18n miufo00303=Applet����UNITID��
	 */
	public static BIContextVO getBIContextVO(BIReportApplet applet) {
		AppDebug.debug("��ʼ����!");

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

		AppDebug.debug("Applet����ReportResource.OPERATE_TYPE��" + applet.getParameter(ReportResource.OPERATE_TYPE));
		AppDebug.debug("Applet����ReportResource.REPORT_ID��" + pk_report);
		AppDebug.debug("Applet����USER��" + user);
		AppDebug.debug("Applet����UNITID��" + unitID);

		BIContextVO contextVO = new BIContextVO();
		contextVO.setLang(applet.getParameter("localCode"));//������Ϣ
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

		// add by ljhua ������ӱ���й�ʽ�����Ҫ������������
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
		//����Դ��Ϣ
		DataSourceInfo curDSInfo = getCurDataSourceInfo(applet);
		contextVO.setAttribute(FreeQueryContextVO.DATA_SOURCEINFO,curDSInfo);
		
		//@edit by liuyy at 2008-12-25 ����08:49:04 ���datasource
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
				//@add by:yza :-> ����NC�����
				ContextFactory.addReplaceParam2Context(contextVO);
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		

		return contextVO;
	}

	/**
	 * ���ݲ����д�����'ҵ������''��λ''�û�'��Ϣ��ʼ���ͻ��˻����� �������ڣ�(00-7-19 10:35:05)
	 */
	protected void initClientEnvironment() {
		String strDisplayZero = getParameter(ITableInputAppletParam.PARAM_TI_DISPLAYZERO);
		boolean bDisplayZero = "true".equalsIgnoreCase(strDisplayZero) ? true
				: false;
		ReportStyle.setShowZero(bDisplayZero);
	}

	public static BaseReportModel getBaseReportModel(String reportPk) {
		BaseReportModel model = getBaseReportModel(reportPk, false, null);
		if (model != null)// ��д��������(����Ȩ����Ҫ)
			model.setPK(reportPk);
		return model;
	}

	
	public static BaseReportModel getBaseReportModel(String reportPk, boolean loadFromSession, String strSessionID) {
		boolean local = false; // ���Ե�ʱ��ɴӱ���ֱ�Ӵ�
		try {
			if (local) {// �ӱ��ش�

				boolean newReport = false; // �Ƿ����µı���

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
				// ����Ǵ�Session�м��صķ�ʽ�������Ȳ���Session
				if (loadFromSession){
					Object resFromSession =  doLinkServletTask(reportPk, strSessionID);
					if(resFromSession instanceof BaseReportModel)
						return (BaseReportModel)resFromSession;
				}

				// ���÷�������̨�������
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
	// ��ʼ��Adhoc�������õĲ�����ϡ�
	public static void initPlugins(final UfoReport report, int reportType) {
		// ɾ����ϵͳ�����������в����

		// ��Ӳ����
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

			// TODO ��Ҫ��ӹ�ʽ���
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
	 * Applet��ʽ�£����¼��ز���ͱ���ģ��
	 * 
	 */

	public static void drillThrough(BaseReportModel newModel, UfoReport report, int operationState) throws BIException {

		// ����Ŀ�걨��
		if (newModel == null) {
			throw new BIException(StringResource.getStringResource("uibiadhoc00005"));
		}

		// ���û�����Ϣ
		BIContextVO contextVO = (BIContextVO) report.getContextVo();
		contextVO.setReportPK(newModel.getPK());
		// add by ljhua ������ӱ���й�ʽ�����Ҫ������������
		String strRepCode = getReportCode(newModel.getPK());
		contextVO.setReportcode(strRepCode);

		contextVO.setBaseReportModel(newModel);

		// �滻AppletRootPane
		BIReportApplet applet = (BIReportApplet) report.getParent();

		// �����µĲ���������ø�ʽ/���״̬
		UfoReport newReport = BIReportApplet.getUfoReport(applet, operationState, newModel.getReportType(), contextVO);
		// newReport.setOperationState(operationState);

		applet.setAppletRootPane(newReport);

		applet.validate();
		applet.invalidate();
		applet.repaint();
	}

	/**
	 * ִ������Servlet�Ĳ���
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
	 * �������Դ��Ϣ��������TableInputApplet
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
	 * ��ѯ���ɱ������õ����ݼ����Ƿ����ָ���������ݼ�
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
		//noted by csli:�˴������kinds��exclude kind�������س�kinds������ݼ����͡�
		DatasetTree dsTree = (DatasetTree)DatasetTree.getInstance(DataManageObjectIufo.IUFO_DATASOURCE, reportPK, false, false, kinds);
		for (int i = 0; i < dsPKs.length; i++) {
			if(!dsTree.isExist(dsPKs[i]))
				return true;
		}
		return false;
	}

} 