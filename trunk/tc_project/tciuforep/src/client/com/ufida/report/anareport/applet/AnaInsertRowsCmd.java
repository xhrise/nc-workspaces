package com.ufida.report.anareport.applet;

import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.sysplugin.insertdelete.InsertRowsCmd;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.event.HeaderModelListener;

public class AnaInsertRowsCmd extends InsertRowsCmd {
	AnaReportPlugin m_plugin = null;

	public AnaInsertRowsCmd(UfoReport rep, AnaReportPlugin plugin) {
		super(rep);
		m_plugin = plugin;
	}

	@Override
	public void execute(Object[] params) {
		AnaReportModel model = m_plugin.getModel();
		if (!model.isFormatState() && !checkHeaderModelListener())
			getCellsModel().getRowHeaderModel().addHeaderModelListener(m_plugin.getFormatListener());
		super.execute(params);
		if (!model.isFormatState())
			doRefreshDataModel(); // 需要刷新数据,重新设置数据态和格式态的对应位置
	}

	protected void doRefreshDataModel() {
		m_plugin.refreshDataModel(false);
	}

	@Override
	protected CellsModel getCellsModel() {
		return m_plugin.getModel().getFormatModel();
	}

	@Override
	protected AreaPosition[] getSelectArea() {
		return getSelectedAreas();
	}

	private AreaPosition[] getSelectedAreas() {
		AnaReportModel anaRepModel = m_plugin.getModel();
		AreaPosition[] selectAreas = anaRepModel.getFormatModel().getSelectModel().getSelectedAreas();
		if (!anaRepModel.isFormatState()) {
			CellsModel dataModel = anaRepModel.getDataModel();
			selectAreas = anaRepModel.getFormatAreas(dataModel, dataModel.getSelectModel().getSelectedAreas());
		}
		return selectAreas;
	}

	private boolean checkHeaderModelListener() {
		AnaReportModel anaModel = m_plugin.getModel();
		CellsModel formatModel = anaModel.getFormatModel();
		HeaderModelListener[] headerListeners = formatModel.getRowHeaderModel().getListenerList();
		if (headerListeners == null || headerListeners.length == 0)
			return false;
		for (HeaderModelListener listener : headerListeners) {
			if (listener instanceof AnaFormatModelListener)
				return true;
		}

		return false;
	}
}