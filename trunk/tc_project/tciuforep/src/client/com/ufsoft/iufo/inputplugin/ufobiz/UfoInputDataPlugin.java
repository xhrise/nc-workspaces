package com.ufsoft.iufo.inputplugin.ufobiz;

import java.util.EventObject;

import nc.ui.iufo.input.control.RepDataControler;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoAreaCalExt;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoCalExt;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoCheckRenderer;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoCheckRepExt;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoCheckTaskExt;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoExcelExpExt;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoExportData2HtmlExt;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoImportExcelDataExt;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoImportIUfoDataExt;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoTraceDataExt;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoTraceSubExt;
import com.ufsoft.script.exception.CmdException;
import com.ufsoft.table.CellsEvent;
import com.ufsoft.table.CellsModelListener;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.IArea;
import com.ufsoft.table.re.CellRenderAndEditor;

public class UfoInputDataPlugin extends AbstractPlugin implements CellsModelListener,IUfoContextKey{

	static{
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new UfoCheckRenderer());
	}

	protected IPluginAction[] createActions() {
		return new IPluginAction[]{new UfoCalExt(),new UfoAreaCalExt(),new UfoTraceDataExt(),
				new UfoTraceSubExt(),new UfoCheckRepExt(),new UfoCheckTaskExt(),new UfoImportExcelDataExt(),
				new UfoImportIUfoDataExt(),new UfoExcelExpExt(),new UfoExportData2HtmlExt()};
	}

	public void shutdown() {	
	}

	public void startup() {	
		getMainboard().getEventManager().addListener(this);
	}
	
	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		return null;
	}

	public void cellsChanged(CellsEvent event) {
		if (event.getMessage() == CellsEvent.VALUE_CHANGED) {
			final IArea areaPos =  event.getArea();
			if (areaPos == null) {
				return;
			}
			// @edit by wangyga at 2009-6-23,ÏÂÎç03:16:23
			new Thread(new Runnable(){
				public void run() {
					try {
						RepDataControler.getInstance(getMainboard()).getReportCalUtil().calcFormula(areaPos);
					} catch (CmdException e) {
						AppDebug.debug(e);
					}
				}
				
			}).start();
			
		}
	}
}
