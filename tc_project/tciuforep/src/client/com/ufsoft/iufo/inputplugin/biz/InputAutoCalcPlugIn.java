package com.ufsoft.iufo.inputplugin.biz;

import java.util.EventObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.autocalc.ReportCalUtil;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.script.exception.CmdException;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsEvent;
import com.ufsoft.table.CellsModelListener;
import com.ufsoft.table.ForbidedOprException;

public class InputAutoCalcPlugIn extends AbstractPlugIn implements
		CellsModelListener,IUfoContextKey {

	protected IPluginDescriptor createDescriptor() {
		return null;
	}

	private ReportCalUtil m_reportCalUtil = null;

	public ReportCalUtil getAutoCalcUtil() {
		Object repIdObj = getReport().getContextVo().getAttribute(REPORT_PK);
		String strRepPK = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null; 
		
//		String strRepPK = getReport().getContextVo().getContextId();
		if (m_reportCalUtil == null
				||(m_reportCalUtil.getReportPK() != null && !m_reportCalUtil.getReportPK().equals(strRepPK))
				||(strRepPK != null && !strRepPK.equals(m_reportCalUtil.getReportPK()))) {
			m_reportCalUtil = new ReportCalUtil(strRepPK, getReport().getCellsModel(), getReport().getContextVo());
		}

		return m_reportCalUtil;
	}
	public void initCalcUtil(){
		m_reportCalUtil=null;
	}

	public void cellsChanged(CellsEvent event) {
		if (event.getMessage() == CellsEvent.VALUE_CHANGED) {
			AreaPosition areaPos = (AreaPosition) event.getArea();
			if (areaPos == null) {
				return;
			}

			try {
				getAutoCalcUtil().calcFormula(areaPos);
			} catch (CmdException e) {
				AppDebug.debug(e);
			}
		}
	}

	public String isSupport(int source, EventObject e)
			throws ForbidedOprException {
		return null;
	}
}
