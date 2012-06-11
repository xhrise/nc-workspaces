/*
 * Created on 2005-6-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.anareport.applet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventObject;

import com.ufida.report.adhoc.model.AdhocCrossProperty;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsEvent;
import com.ufsoft.table.CellsModelListener;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.format.ConditionFormat;
import com.ufsoft.table.format.Format;

/**
 * Adhoc报表的读写权限管理
 * 
 * @author caijie
 */
public class AnaReportCellListener implements CellsModelListener, Serializable {

	static final long serialVersionUID = -7116280144592434721L;

	/***/
	private AnaReportPlugin m_plugin;

	// private AdhocModel m_detailArea = null;
	/**
	 * @param report
	 */
	public AnaReportCellListener(AnaReportPlugin adhocPlugin) {
		super();
		this.m_plugin = adhocPlugin;
		// m_detailArea =
		// this.m_plugin.getModel().getAreaByType(AdhocArea.DETAIL_AREA_TYPE)[0];
	}

	public void cellsChanged(CellsEvent event) {
		if (!m_plugin.getCellsModel().isEnableEvent())
			return;

	}

	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		// TODO Auto-generated method stub
		return null;
	}

}
