package nc.ui.bi.query;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIFrame;

import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.rep.applet.BIReportApplet;
import com.ufsoft.report.UIUtilities;
import com.ufsoft.report.UfoReport;
import com.ufsoft.iufo.resource.StringResource;

/**
 * UFOReport֡
 * 
 * @author zjb
 */

public class AnaReportFrame extends UIFrame {

	// ����ʵ��
	private UfoReport m_report = null;

	// ����ID
	private String m_reportId = null;
	

	private UfoReport report=null;
	/**
	 * ������
	 */
	public AnaReportFrame(String reportId) {
		this(reportId,null, -1);
	}

	/*
	 * qeStatus��-1=��QE������0=���ɱ������1=���ɱ�������
	 */
	/**
	 * @i18n iufobi00011=��֧�ֶԺ��б������ݼ������ɱ�������޸Ļ�ɾ����
	 */
	public AnaReportFrame(final String reportId,String reportName,Integer qeStatus) {
		super();
		//TODO V6��Ӧ��ɾ��
		if(qeStatus>=0&&BIReportApplet.isDataSetInTypes(reportId, new String[]{"R"})){
			throw new IllegalArgumentException(StringResource.getStringResource("iufobi00011"));
		}
		this.m_reportId = reportId;
		setTitle(reportName+"-"+StringResource.getStringResource("miufo00110"));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		report=getUfoReport(qeStatus);
		UIUtilities.ufoReport2JRootPane(report, getRootPane());
	}

	
	/**
	 * @i18n miufo1000845=��ʾ
	 * @i18n iufobi00013=�����ѱ��޸ģ��Ƿ�汣�棿
	 */
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			AnaReportPlugin plugin = (AnaReportPlugin) report.getPluginManager().getPlugin(
					AnaReportPlugin.class.getName());
			if(plugin.getReportPK()==null){
				plugin.setReportPK(m_reportId);
			}
			if (plugin.getModel().getFormatModel().isDirty()) {
				int result=nc.ui.pub.beans.MessageDialog.showYesNoCancelDlg(e.getWindow(), StringResource.getStringResource("miufo1000845"), StringResource.getStringResource("iufobi00013"));
				if (result== UIDialog.ID_YES) {
					plugin.store();
				}else if(result==UIDialog.ID_CANCEL){
					return;
				}
			}
		}
		super.processWindowEvent(e);
	}

	/**
	 * ��ñ���ʵ��
	 */
	public UfoReport getUfoReport( Integer qeStatus) {
		if (m_report == null) {
			m_report = QECellReportUI.initReport(m_reportId, UfoReport.OPERATION_FORMAT, true, qeStatus);
		}
		return m_report;
	}
	
	
}
 