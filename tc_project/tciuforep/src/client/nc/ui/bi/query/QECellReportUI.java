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
 * UFOReport��NC���ܽڵ�ģʽ�µİ�����
 * 
 * @author zjb 2008-05-20
 */

public class QECellReportUI extends ToftPanel {

	private static final long serialVersionUID = 1L;

	// ����ʵ��
	private UfoReport m_report = null;

	// ����ID
	private String m_reportId = null;

	// ��ť����
	public final static int BUTTON_TYPE = -1;

	// ˢ��
	/**
	 * @i18n miufopublic251=ˢ��
	 */
	private ButtonObject m_boRefresh = new ButtonObject(StringResource.getStringResource("miufopublic251"), StringResource.getStringResource("miufopublic251"), BUTTON_TYPE, StringResource.getStringResource("miufopublic251"));

	// ������¹Ҵ�ӡ�͵���EXCEL��
	/**
	 * @i18n miufo00383=���
	 */
	private ButtonObject m_boExport = new ButtonObject(StringResource.getStringResource("miufo00383"), StringResource.getStringResource("miufo00383"), BUTTON_TYPE,
			StringResource.getStringResource("miufo00383"));

	/**
	 * @i18n miufo1001332=��ӡ
	 */
	private ButtonObject m_boPrint = new ButtonObject(StringResource.getStringResource("miufo1001332"), StringResource.getStringResource("miufo1001332"), BUTTON_TYPE,
			StringResource.getStringResource("miufo1001332"));

	/**
	 * @i18n miufo1001782=����Excel
	 */
	private ButtonObject m_boExportExcel = new ButtonObject(StringResource.getStringResource("miufo1001782"), StringResource.getStringResource("miufo1001782"), BUTTON_TYPE, StringResource.getStringResource("miufo1001782"));

	{
		m_boExport.setChildButtonGroup(new ButtonObject[] { m_boPrint,
				m_boExportExcel });
	}

	// ����ť��
	protected ButtonObject[] m_btngrpUI = new ButtonObject[] { m_boRefresh };

	/**
	 * ���ι�����
	 */
	public QECellReportUI(FramePanel fp) {
		super();
		setFrame(fp);
		// ��ñ���ID
		m_reportId = getParameter("reportId");
		// ��ʼ��
		initUI();
	}

	/**
	 * �����ʼ��
	 */
	private void initUI() {
		setName("QECellReportUI");
		setSize(800, 600);
		setLayout(new BorderLayout());
		// ��ʱ
		new Thread(new Runnable() {
			/**
			 * @i18n miufo00384=���������...
			 */
			public void run() {
				// ������ʾ
				QEBannerDialog dialog = new QEBannerDialog(QECellReportUI.this);
				dialog.setStartText(StringResource.getStringResource("miufo00384"));
				dialog.setWorkThread(Thread.currentThread());
				dialog.setClosable(false);
				try {
					dialog.start();
					// ���뱨��ʵ��
					add(getUFOReport(), BorderLayout.CENTER);
					// ���ð�ť
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
	 * @i18n uiufofurl0083=����
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
	 * ��ñ���ʵ��
	 */
	public UfoReport getUFOReport() {
		if (m_report == null) {
			m_report = initReport(m_reportId, ReportContextKey.OPERATION_INPUT, false,0);
		}
		return m_report;
	}

	/**
	 * ��ʼ������ʵ��
	 * @i18n miufo00604=��������
	 */
	public static UfoReport initReport(String reportId, int iOperType,
			boolean bMenuVisible,  Integer qeStatus) {

		// ��������
		String langCode = "simpchn";
		String classname = "nc.vo.ml.translator.SimpleChineseTranslator";
		Language lang = new Language();
		lang.setCode(langCode);
		lang.setDisplayName(StringResource.getStringResource("miufo00604"));
		lang.setTranslatorClassName(classname);
		NCLangRes.getInstance().setCurrLanguage(lang);
		MultiLangUtil.saveLanguage(langCode);

		int reportType = ReportResource.INT_REPORT_ANALYZE;
		// ����������
		BIContextVO contextVO = getBIContextVO(reportId);
		contextVO.setReportType(reportType);
		contextVO.setAttribute(ReportResource.OPEN_IN_MODAL_DIALOG, qeStatus);//��QE�����ģ�������ģ̬�Ի�����

		// ��֯����ֵ
		UfoReport report = BIReportApplet.getUfoReport(null, iOperType,
				reportType, contextVO);
		report.getReportMenuBar().setVisible(bMenuVisible);
		// report.getReportMenuBar().setVisible(false); // �ᵼ�������Ʋ�����
		Component comp = report.getReportNavPanel().getNorthComp();
		if (comp != null) {
			comp.setPreferredSize(new Dimension(20, 5));
		}
		// �ָ����
		// try {
		// UIManager.setLookAndFeel(new MetalLookAndFeel());
		// } catch (UnsupportedLookAndFeelException e) {
		// AppDebug.debug(e);
		// }
	    // V55ĩ��+
		UICloseableTabbedPane tabPanel= report.getReportNavPanel().getPanelById(2);
		if (tabPanel != null && !bMenuVisible) {
			tabPanel.close();
		}
		return report;
	}

	/**
	 * �����������Ϣ
	 */
	private static BIContextVO getBIContextVO(String reportId) {
//		// ���ģ��
//		BaseReportModel model = BIReportApplet.getBaseReportModel(reportId);
//		// ����������
//		BIContextVO contextVO = new BIContextVO();
//		contextVO.setBaseReportModel(model);
//		contextVO.setReportPK(reportId);
//		
//		//added by biancm 20090914 ��context��ѹ������Դ���ƣ����ò���ʱ�õ�
//		contextVO.setAttribute(IQEContextKey.NC_DATA_SOURCE, DataSetUtilities.getCurrentDsName());
//		
//		return contextVO;
		
		// @edit by ll at 2009-9-18,����03:42:22 ���QECellReportUI�еļ��ش������⣬
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
		
		//added by biancm 20091029  ��context��ѹ������Դ�����Դ��룬����ȡ��ʱ�õ�
		if (context.getAttribute(IQEContextKey.NC_DATA_SOURCE) == null && context.getAttribute(BIContextVO.CURRENT_LANG) == null) {
			  try {
				Context tempContext = ContextFactory.createContext();//������ʱcontext�����溬������Դ�����Դ�����Ϣ
				  context.setAttribute(IQEContextKey.NC_DATA_SOURCE, tempContext.getAttribute(IQEContextKey.NC_DATA_SOURCE));
				  context.setLang(tempContext.getAttribute(BIContextVO.CURRENT_LANG).toString());
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}

		return context;
	}

	/**
	 * ˢ��
	 */
	public void onRefresh() {
		m_report = null;
		removeAll();
		// ���뱨��ʵ��
		add(getUFOReport(), BorderLayout.CENTER);
	}

	/**
	 * ��ӡ
	 */
	public void onPrint() {
		// AnaReportPlugin arp = getAnaReportPlugin(getUFOReport());
	}

	/**
	 * ����EXCEL
	 */
	public void onExportExcel() {
		// AnaReportPlugin arp = getAnaReportPlugin(getUFOReport());
	}

	/**
	 * ��÷�������ʵ��
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
 