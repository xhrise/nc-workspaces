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
 * UFOReport����Ʋ�����
 * 
 * @author zjb
 */

public class AnaReportDesignUI extends BasicWizardStepPanel {

	private static final long serialVersionUID = 1L;

	// ����ʵ��
	private UfoReport m_report = null;

	/**
	 * �����ʼ��
	 */
	private void initUI() {
		setName("AnaReportDesignUI");
		setLayout(new BorderLayout());
		// ���뱨��ʵ��
		add(getUfoReport(), BorderLayout.CENTER);
	}

	/**
	 * ��ñ���ʵ��
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
	 * AnaReportDesignUI ���߻�������ע��
	 */
	public AnaReportDesignUI(WizardShareObject wso) {
		super(wso);
		// ��ʼ��
		initUI();
	}

	/**
	 * ���ǿ��ת����Ĺ���ṹ
	 */
	public AnaReportDesignObject getArdo() {
		return (AnaReportDesignObject) getWizardShareObject();
	}

	/**
	 * У��
	 */
	public String check() {
		return null;
	}

	public boolean completeWizard() {
		// ִ��У��
		String strErr = check();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "��ѯ����" */, strErr);
			return false;
		}
		// �ӽ���������
		ReportVO vo = getArdo().getCurReport();
		vo.setDefinition(getSavedReport());
		return true;
	}

	/**
	 * @i18n ubirep0013=��ʽ���
	 */
	public String getStepTitle() {
		return StringResource.getStringResource("ubirep0013");
	}

	@Override
	public void initStep() {
		// ��ʼ���������ݼ�
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
	 * �����ʽ����
	 */
	public BaseReportModel getSavedReport() {
		getUfoReport().stopCellEditing();
		AnaReportPlugin ana = (AnaReportPlugin) getUfoReport()
				.getPluginManager().getPlugin(AnaReportPlugin.class.getName());
		return (ana == null) ? null : ana.getModel();
	}
}
 