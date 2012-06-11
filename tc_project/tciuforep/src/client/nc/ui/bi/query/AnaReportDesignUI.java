package nc.ui.bi.query;

import java.awt.BorderLayout;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.dsmanager.BasicWizardStepPanel;
import nc.ui.pub.querytoolize.WizardShareObject;
import nc.vo.bi.report.manager.ReportResource;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.pub.dsmanager.AnaReportDesignObject;

import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.rep.model.BaseReportModel;
import com.ufsoft.iufo.fmtplugin.BDContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;

/**
 * UFOReport向导设计步骤类
 * 
 * @author zjb
 */

public class AnaReportDesignUI extends BasicWizardStepPanel {

	private static final long serialVersionUID = 1L;

	// 报表实例
	private UfoReport m_report = null;

	/**
	 * 界面初始化
	 */
	private void initUI() {
		setName("AnaReportDesignUI");
		setLayout(new BorderLayout());
		// 加入报表实例
		add(getUfoReport(), BorderLayout.CENTER);
	}

	/**
	 * 获得报表实例
	 */
	public UfoReport getUfoReport() {
		if (m_report == null) {
			String reportId = getArdo().getCurReport().getID();
			m_report = QECellReportUI.initReport(reportId,
					UfoReport.OPERATION_FORMAT, true, 1);

		}
		return m_report;
	}

	/**
	 * AnaReportDesignUI 工具化构造子注解
	 */
	public AnaReportDesignUI(WizardShareObject wso) {
		super(wso);
		// 初始化
		initUI();
	}

	/**
	 * 获得强制转换后的共享结构
	 */
	public AnaReportDesignObject getArdo() {
		return (AnaReportDesignObject) getWizardShareObject();
	}

	/**
	 * 校验
	 */
	public String check() {
		return null;
	}

	public boolean completeWizard() {
		// 执行校验
		String strErr = check();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */, strErr);
			return false;
		}
		// 从界面填充对象
		ReportVO vo = getArdo().getCurReport();
		vo.setDefinition(getSavedReport());
		return true;
	}

	/**
	 * @i18n ubirep0013=格式设计
	 */
	public String getStepTitle() {
		return StringResource.getStringResource("ubirep0013");
	}

	@Override
	public void initStep() {
		// 初始化引用数据集
		DataSetDefVO[] defs = getArdo().getDatasetDefs();
		if (defs != null && defs.length != 0) {
			AnaReportPlugin arp = QECellReportUI
					.getAnaReportPlugin(getUfoReport());
			if (arp != null) {
				arp.addDataSource(defs, false);
			}
		}
	}

	/*
	 * 保存格式定义
	 */
	public BaseReportModel getSavedReport() {
		getUfoReport().stopCellEditing();
		AnaReportPlugin ana = (AnaReportPlugin) getUfoReport()
				.getPluginManager().getPlugin(AnaReportPlugin.class.getName());
		return (ana == null) ? null : ana.getModel();
	}
}
 