package com.ufida.report.anareport.edit;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;

import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.rep.model.BaseReportModel;
import com.ufida.report.rep.model.IBIContextKey;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class AnaReportRedoExt extends AbsActionExt {

	private AnaReportPlugin m_plugin = null;
	
	public AnaReportRedoExt(AnaReportPlugin plugin){
		super();
		m_plugin = plugin;
	}
	@Override
	public UfoCommand getCommand() {
		return new UfoCommand() {
			@Override
			public void execute(Object[] params) {
				 String strRepID=m_plugin.getReportPK();
				 ReportSrv reportSrv = new ReportSrv();
				 ReportVO[] reportVOs = (ReportVO[]) reportSrv.getByIDs(new String[] { strRepID });
				 if (reportVOs == null || reportVOs.length == 0 || reportVOs[0] == null) {
					 return;
				 }
				 BaseReportModel model = (BaseReportModel)reportVOs[0].getDefinition();
				 m_plugin.getReport().getContextVo().setAttribute(IBIContextKey.REPORTMODEL, model);
				 m_plugin.getConditionPanelDropTarget().removeChangeListener(m_plugin.getModel());
				 m_plugin.setSetParam(false);
				 m_plugin.startup();
				 if (m_plugin.isFromQueryReport() || !m_plugin.isAegisFormat()){
					 m_plugin.setOperationState(UfoReport.OPERATION_INPUT);
				 }
				 m_plugin.setDirty(false);
				 m_plugin.loadConditonPanelData(false);
				 m_plugin.refreshDataSource(false);
			}
			
		};
	}

	@Override
	public Object[] getParams(UfoReport container) {
		return new Object[]{container};
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("miufo00200047"));
		uiDes.setToolBar(true);
		uiDes.setGroup(MultiLang.getString("miufo00200048"));
		uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK));
		uiDes.setImageFile("reportcore/redo.gif");
		ActionUIDes uiDes1= new ActionUIDes();
		uiDes1.setName(MultiLang.getString("miufo00200047"));
		uiDes1.setGroup(MultiLang.getString("miufo00200048"));
		uiDes1.setPopup(true);
		uiDes1.setPaths(new String[]{});
		return new ActionUIDes[] { uiDes ,uiDes1};
	}
	
	@Override
	public boolean isEnabled(Component focusComp) {
		if(m_plugin.getModel().getFormatModel().isDirty()){
			return true;
		}
		return false;
	}

}
