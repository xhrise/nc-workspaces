package com.ufsoft.report.sysplugin.trace;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Constructor;
import java.util.Hashtable;

import javax.swing.JApplet;

import nc.ui.pub.beans.UIDialog;

import com.ufida.dataset.Provider;
import com.ufida.dataset.metadata.Field;
import com.ufida.dataset.tracedata.IMultiTraceData;
import com.ufida.dataset.tracedata.ITraceDataResult;
import com.ufida.dataset.tracedata.TraceDataOperator;
import com.ufida.dataset.tracedata.TraceDataParam;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

public class TraceDataExt extends AbsActionExt {

	TraceDataPlugin m_plugin = null;

	public TraceDataExt(TraceDataPlugin plugin) {
		m_plugin = plugin;
	}

	public boolean isEnabled(Component focusComp) {
		CellsModel cellsModel = m_plugin.getCellsModel();

		CellPosition anchorCell = cellsModel.getSelectModel().getAnchorCell();

		if (anchorCell == null) {
			return false;
		}

		Cell cell = cellsModel.getCell(anchorCell);

		if (cell == null) {
			return false;
		}

		Hashtable<CellPosition, TraceDataParam> table = (Hashtable<CellPosition, TraceDataParam>) cellsModel
				.getExtProp(TraceDataParam.KEY);
		if (table == null || !table.containsKey(anchorCell))
			return false;
		Object ex = table.get(anchorCell);
		if (ex == null || !(ex instanceof TraceDataParam)) {
			return false;
		}

		if (((TraceDataParam) ex).getDataSet() == null
				|| !((TraceDataParam) ex).getDataSet().getProvider().supportTraceData()) {
			return false;
		}

		return true;
	}

	@Override
	public UfoCommand getCommand() {
		return null;
	}
	
	public static JApplet getReportApplet(UfoReport ufoReport){
		Container parent=ufoReport.getParent();
		while (parent!=null){
			if (parent instanceof JApplet)
				return (JApplet)parent;
			parent=parent.getParent();
		}
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {

		CellsModel cellsModel = m_plugin.getCellsModel();

		CellPosition anchorCell = cellsModel.getSelectModel().getAnchorCell();

		if (anchorCell == null) {
			return null;
		}

		Cell cell = cellsModel.getCell(anchorCell);
		if (cell == null) {
			return null;
		}

		Hashtable<CellPosition, TraceDataParam> table = (Hashtable<CellPosition, TraceDataParam>) cellsModel
				.getExtProp(TraceDataParam.KEY);
		if (table == null || !table.containsKey(anchorCell))
			return null;
		Object ex = table.get(anchorCell);
		if (ex == null || !(ex instanceof TraceDataParam)) {
			return null;
		}

		TraceDataParam param = ((TraceDataParam) ex);
		if (param.getDataSet() == null || param.getDataSet().getProvider() == null) {
			return null;
		}

		try {
			JApplet applet=getReportApplet(m_plugin.getReport());
			
			if (param.isTotal() && param instanceof IMultiTraceData)
				param = getOneTraceParam((IMultiTraceData) param);
			if (param == null)
				return null;
			
			if (applet!=null){
				param.addParam("SCHEME", applet.getParameter("SCHEME"));
				param.addParam("SERVER_PORT",applet.getParameter("SERVER_PORT"));
				param.addParam("localCode",applet.getParameter("localCode"));
			}
			
			Provider provider=param.getDataSet().getProvider();
			provider.setContext(m_plugin.getReport().getContextVo());
			
			ITraceDataResult result = param.getDataSet().getProvider().traceData(param);
			if (result == null) {
				return null;
			}
			String operName = result.getOperatorClzName();
			Class operClz = Class.forName(operName);
			Constructor<? extends TraceDataOperator> constructor = operClz.getConstructor(new Class[] {});
			TraceDataOperator oper = constructor.newInstance(new Object[] {});
			oper.trace(container, result);
			// } catch (ForbidedOprException fe){

		} catch (Throwable e) {
			AppDebug.debug(e);
			UfoPublic.showErrorDialog(container, e.getMessage(), "");

		}

		return null;
	}

	private TraceDataParam getOneTraceParam(IMultiTraceData param) {
		String msg = param.doQueryMultiData();
		if (msg != null) {
			AppDebug.debug(msg);
			return null;
		}
		Object[][] rowDatas = param.getMultiRowDatas();
		if (rowDatas == null || rowDatas.length == 0)
			return null;
		if (rowDatas.length == 1)
			return param.getOneValue(0);
		Field[] flds = param.getShowFields();
		RowSelectDlg dlg = new RowSelectDlg(m_plugin.getReport(), MultiLang.getString("miufo00118"), m_plugin.getReport().getContextVo());
		dlg.setDatas(flds, rowDatas, param.getTraceFld());
		if (dlg.showModal() == UIDialog.ID_OK) {
			int row = dlg.getSelectRow();
			if (row >= 0 && row < rowDatas.length)
				return param.getOneValue(row);
		}

		return null;

	}

	/**
	 * @i18n miufo00118=Êý¾Ý×·×Ù
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("miufo00118"));
		uiDes.setPaths(new String[] { MultiLang.getString("format") });
		uiDes.setGroup("tracedata");
		ActionUIDes uiDes2 = (ActionUIDes) uiDes.clone();
		uiDes2.setPopup(true);
		uiDes2.setPaths(new String[] {});
		return new ActionUIDes[] { uiDes, uiDes2 };
	}

}
