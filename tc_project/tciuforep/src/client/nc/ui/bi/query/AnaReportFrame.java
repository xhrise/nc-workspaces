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
 * UFOReport帧
 * 
 * @author zjb
 */

public class AnaReportFrame extends UIFrame {

	// 报表实例
	private UfoReport m_report = null;

	// 报表ID
	private String m_reportId = null;
	

	private UfoReport report=null;
	/**
	 * 构造子
	 */
	public AnaReportFrame(String reportId) {
		this(reportId,null, -1);
	}

	/*
	 * qeStatus：-1=非QE环境；0=自由报表管理；1=自由报表创建向导
	 */
	/**
	 * @i18n iufobi00011=不支持对含有报表数据集的自由报表进行修改或删除！
	 */
	public AnaReportFrame(final String reportId,String reportName,Integer qeStatus) {
		super();
		//TODO V6中应该删除
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
	 * @i18n miufo1000845=提示
	 * @i18n iufobi00013=报表已被修改，是否存保存？
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
	 * 获得报表实例
	 */
	public UfoReport getUfoReport( Integer qeStatus) {
		if (m_report == null) {
			m_report = QECellReportUI.initReport(m_reportId, UfoReport.OPERATION_FORMAT, true, qeStatus);
		}
		return m_report;
	}
	
	
}
 