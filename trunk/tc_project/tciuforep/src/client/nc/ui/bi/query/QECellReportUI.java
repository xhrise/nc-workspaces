package nc.ui.bi.query;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import nc.ui.bi.query.manager.QueryModelBO_Client;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.pub.ToftPanel;
import nc.ui.reportquery.demo.QEBannerDialog;
import nc.vo.bi.report.manager.ReportResource;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.ml.Language;
import nc.vo.pub.dsmanager.ContextFactory;
import nc.vo.pub.dsmanager.IQEContextKey;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.rep.applet.BIReportApplet;
import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.rep.model.BaseReportModel;
import com.ufsoft.iufo.i18n.MultiLangUtil;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.UICloseableTabbedPane;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.IPlugIn;

/**
 * UFOReport在NC功能节点模式下的包裹类
 * 
 * @author zjb 2008-05-20
 */

public class QECellReportUI extends ToftPanel {

	private static final long serialVersionUID = 1L;

	// 报表实例
	private UfoReport m_report = null;

	// 报表ID
	private String m_reportId = null;

	// 按钮类型
	public final static int BUTTON_TYPE = -1;

	// 刷新
	/**
	 * @i18n miufopublic251=刷新
	 */
	private ButtonObject m_boRefresh = new ButtonObject(StringResource.getStringResource("miufopublic251"), StringResource.getStringResource("miufopublic251"), BUTTON_TYPE, StringResource.getStringResource("miufopublic251"));

	// 输出（下挂打印和导出EXCEL）
	/**
	 * @i18n miufo00383=输出
	 */
	private ButtonObject m_boExport = new ButtonObject(StringResource.getStringResource("miufo00383"), StringResource.getStringResource("miufo00383"), BUTTON_TYPE,
			StringResource.getStringResource("miufo00383"));

	/**
	 * @i18n miufo1001332=打印
	 */
	private ButtonObject m_boPrint = new ButtonObject(StringResource.getStringResource("miufo1001332"), StringResource.getStringResource("miufo1001332"), BUTTON_TYPE,
			StringResource.getStringResource("miufo1001332"));

	/**
	 * @i18n miufo1001782=导出Excel
	 */
	private ButtonObject m_boExportExcel = new ButtonObject(StringResource.getStringResource("miufo1001782"), StringResource.getStringResource("miufo1001782"), BUTTON_TYPE, StringResource.getStringResource("miufo1001782"));

	{
		m_boExport.setChildButtonGroup(new ButtonObject[] { m_boPrint,
				m_boExportExcel });
	}

	// 主按钮组
	protected ButtonObject[] m_btngrpUI = new ButtonObject[] { m_boRefresh };

	/**
	 * 带参构造子
	 */
	public QECellReportUI(FramePanel fp) {
		super();
		setFrame(fp);
		// 获得报表ID
		m_reportId = getParameter("reportId");
		// 初始化
		initUI();
	}

	/**
	 * 界面初始化
	 */
	private void initUI() {
		setName("QECellReportUI");
		setSize(800, 600);
		setLayout(new BorderLayout());
		// 耗时
		new Thread(new Runnable() {
			/**
			 * @i18n miufo00384=报表加载中...
			 */
			public void run() {
				// 进度提示
				QEBannerDialog dialog = new QEBannerDialog(QECellReportUI.this);
				dialog.setStartText(StringResource.getStringResource("miufo00384"));
				dialog.setWorkThread(Thread.currentThread());
				dialog.setClosable(false);
				try {
					dialog.start();
					// 加入报表实例
					add(getUFOReport(), BorderLayout.CENTER);
					// 设置按钮
					setButtons(m_btngrpUI);
				} catch (Exception e) {
					AppDebug.debug(e);
				} finally {
					dialog.end();
				}
			}
		}).start();
	}

	/**
	 * @i18n uiufofurl0083=报表
	 */
	@Override
	public String getTitle() {
		return StringResource.getStringResource("uiufofurl0083");
	}

	@Override
	public void onButtonClicked(ButtonObject bo) {
		if (bo == m_boRefresh) {
			onRefresh();
		} else if (bo == m_boPrint) {
			onRefresh();
		} else if (bo == m_boExportExcel) {
			onRefresh();
		}
	}

	/**
	 * 获得报表实例
	 */
	public UfoReport getUFOReport() {
		if (m_report == null) {
			m_report = initReport(m_reportId, ReportContextKey.OPERATION_INPUT, false,0);
		}
		return m_report;
	}

	/**
	 * 初始化报表实例
	 * @i18n miufo00604=简体中文
	 */
	public static UfoReport initReport(String reportId, int iOperType,
			boolean bMenuVisible,  Integer qeStatus) {

		// 设置语种
		String langCode = "simpchn";
		String classname = "nc.vo.ml.translator.SimpleChineseTranslator";
		Language lang = new Language();
		lang.setCode(langCode);
		lang.setDisplayName(StringResource.getStringResource("miufo00604"));
		lang.setTranslatorClassName(classname);
		NCLangRes.getInstance().setCurrLanguage(lang);
		MultiLangUtil.saveLanguage(langCode);

		int reportType = ReportResource.INT_REPORT_ANALYZE;
		// 构造上下文
		BIContextVO contextVO = getBIContextVO(reportId);
		contextVO.setReportType(reportType);
		contextVO.setAttribute(ReportResource.OPEN_IN_MODAL_DIALOG, qeStatus);//从QE过来的，并且在模态对话框内

		// 组织返回值
		UfoReport report = BIReportApplet.getUfoReport(null, iOperType,
				reportType, contextVO);
		report.getReportMenuBar().setVisible(bMenuVisible);
		// report.getReportMenuBar().setVisible(false); // 会导致区域复制不可用
		Component comp = report.getReportNavPanel().getNorthComp();
		if (comp != null) {
			comp.setPreferredSize(new Dimension(20, 5));
		}
		// 恢复外观
		// try {
		// UIManager.setLookAndFeel(new MetalLookAndFeel());
		// } catch (UnsupportedLookAndFeelException e) {
		// AppDebug.debug(e);
		// }
	    // V55末期+
		UICloseableTabbedPane tabPanel= report.getReportNavPanel().getPanelById(2);
		if (tabPanel != null && !bMenuVisible) {
			tabPanel.close();
		}
		return report;
	}

	/**
	 * 获得上下文信息
	 */
	private static BIContextVO getBIContextVO(String reportId) {
//		// 获得模型
//		BaseReportModel model = BIReportApplet.getBaseReportModel(reportId);
//		// 构造上下文
//		BIContextVO contextVO = new BIContextVO();
//		contextVO.setBaseReportModel(model);
//		contextVO.setReportPK(reportId);
//		
//		//added by biancm 20090914 往context中压入数据源名称，设置参数时用到
//		contextVO.setAttribute(IQEContextKey.NC_DATA_SOURCE, DataSetUtilities.getCurrentDsName());
//		
//		return contextVO;
		
		// @edit by ll at 2009-9-18,下午03:42:22 解决QECellReportUI中的加载次数问题，
		BIContextVO context = null;
		try{
			context = QueryModelBO_Client.getFreeReportWithDS(reportId);
		}catch(Exception e){
			AppDebug.debug(e);
			return new BIContextVO();
		}
		BaseReportModel model = context.getBaseReportModel();
		if (model != null && model instanceof AnaReportModel) {
			AnaReportModel report = (AnaReportModel) model;
			String[] usedDS = report.getDataSource().getAllDSPKs();
			if (usedDS != null) {
				for (String defPK : usedDS) {
					DataSetDefVO defVO  =(DataSetDefVO) context.getAttribute(defPK);
					report.getDataSource().setDSDef(defPK, defVO);
					context.removeAttribute(defPK);
				}
			}
		}
		
		//added by biancm 20091029  往context中压入数据源、语言代码，总账取数时用到
		if (context.getAttribute(IQEContextKey.NC_DATA_SOURCE) == null && context.getAttribute(BIContextVO.CURRENT_LANG) == null) {
			  try {
				Context tempContext = ContextFactory.createContext();//构建临时context，里面含有数据源、语言代码信息
				  context.setAttribute(IQEContextKey.NC_DATA_SOURCE, tempContext.getAttribute(IQEContextKey.NC_DATA_SOURCE));
				  context.setLang(tempContext.getAttribute(BIContextVO.CURRENT_LANG).toString());
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}

		return context;
	}

	/**
	 * 刷新
	 */
	public void onRefresh() {
		m_report = null;
		removeAll();
		// 加入报表实例
		add(getUFOReport(), BorderLayout.CENTER);
	}

	/**
	 * 打印
	 */
	public void onPrint() {
		// AnaReportPlugin arp = getAnaReportPlugin(getUFOReport());
	}

	/**
	 * 导出EXCEL
	 */
	public void onExportExcel() {
		// AnaReportPlugin arp = getAnaReportPlugin(getUFOReport());
	}

	/**
	 * 获得分析表插件实例
	 */
	public static AnaReportPlugin getAnaReportPlugin(UfoReport report) {
		String className = AnaReportPlugin.class.getName();
		IPlugIn plugin = report.getPluginManager().getPlugin(className);
		if (plugin != null && plugin instanceof AnaReportPlugin) {
			return (AnaReportPlugin) plugin;
		} else {
			return null;
		}
	}
}
 