package com.ufida.report.anareport.edit;

import java.awt.Component;

import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;

public class AnaReportFindExt extends AbsActionExt {

	private AnaReportPlugin m_plugin = null;
	
	public AnaReportFindExt(AnaReportPlugin plugin){
		super();
		m_plugin = plugin;
	}
	
	@Override
	public UfoCommand getCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		AnaReportFindDlg dlg=new AnaReportFindDlg(m_plugin.getReport(),m_plugin.getModel());
		dlg.show();
		return null;
	}
    
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("findData"));
		uiDes.setPaths(new String[]{ MultiLang.getString("edit")});
		uiDes.setGroup(MultiLang.getString("findAndReplace"));
		ActionUIDes uiDes1=(ActionUIDes)uiDes.clone();
		uiDes1.setPopup(true);
		uiDes1.setPaths(new String[]{});
		return new ActionUIDes[]{uiDes,uiDes1};
	}

	@Override
	public boolean isEnabled(Component focusComp) {
		if (!m_plugin.getModel().isFormatState()) {
			ExAreaCell[] exCells = ExAreaModel.getInstance(
					m_plugin.getModel().getCellsModel()).getExAreaCells();
			if (exCells != null && exCells.length > 0) {
				for (int i = 0; i < exCells.length; i++) {
					if (exCells[i].getModel() instanceof AreaDataModel) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
