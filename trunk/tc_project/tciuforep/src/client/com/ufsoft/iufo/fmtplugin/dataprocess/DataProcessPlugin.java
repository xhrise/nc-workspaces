package com.ufsoft.iufo.fmtplugin.dataprocess;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.FieldMapUtil;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.GroupFormulaVO;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.fmtplugin.service.DataProcessSrv;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.script.base.IParsed;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.script.extfunc.GAggrFuncDriver;
import com.ufsoft.script.spreadsheet.UfoCalcEnv;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.CellRenderAndEditor;
import com.ufsoft.table.re.DefaultSheetCellRenderer;

/**
 * 动态区数据处理
 * 
 * @author zzl
 */
public class DataProcessPlugin extends AbstractPlugIn implements IUfoContextKey {
	private static class GroupStatFuncRenderer extends DefaultSheetCellRenderer {
		private static final long serialVersionUID = -6841998667107459248L;

		public Component getCellRendererComponent(CellsPane cellsPane,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column, Cell cell) {
			//edit by wangyga 此版暂时这样处理
	     	if(cellsPane.getOperationState() != ReportContextKey.OPERATION_FORMAT){
				return null;
			}
	     	
			if (cell == null) {
				return null;
			}

			CellPosition fmtPos = CellPosition.getInstance(row, column);
			DynAreaModel dynModel = DynAreaModel.getInstance(cellsPane.getDataModel());
			DynAreaCell dynAreaCell = dynModel.getDynAreaCellByPos(
					fmtPos);
			UfoFmlExecutor proxy = FormulaModel.getInstance(cellsPane.getDataModel()).getUfoFmlExecutor();
			if (dynAreaCell != null
					&& dynModel.getDataProcess(
							dynAreaCell.getDynAreaPK()) != null) {
				CellPosition relativePos = (CellPosition) fmtPos.getMoveArea(
						-dynAreaCell.getArea().getStart().getRow(),
						-dynAreaCell.getArea().getStart().getColumn());

				Vector<GroupFormulaVO> groupStatFormulas = dynModel
						.getDataProcess(dynAreaCell.getDynAreaPK())
						.getGroupFormulaList();

				JComponent editorComp = (JComponent) super
						.getCellRendererComponent(cellsPane, value, isSelected,
								hasFocus, row, column, cell);

				for (GroupFormulaVO element : groupStatFormulas) {
					// GroupFormulaVO element = (GroupFormulaVO) iter.next();
					if (element.getPos().equals(relativePos)) {
						if (element.getLet() != null) {
							((JLabel) editorComp).setText(element.getLet()
									.toUserDefString(proxy.getCalcEnv()));
						} else {
							((JLabel) editorComp).setText(parsegGroupStatFml(cellsPane,
									dynAreaCell, element));
						}
						return editorComp;
					}
				}
			}
			return null;
		}

		/**
		 * 解析分组统计函数
		 * 
		 * @param editorComp
		 * @param dynAreaCell
		 * @param element
		 */
		private String parsegGroupStatFml(CellsPane cellsPane,DynAreaCell dynAreaCell,
				GroupFormulaVO element) {
			IParsed expr = null;
			CellsModel dataModel=cellsPane.getDataModel();
			UfoFmlExecutor proxy = FormulaModel.getInstance(dataModel).getUfoFmlExecutor();

			try {
				proxy.registerFuncDriver(new GAggrFuncDriver((UfoCalcEnv) proxy
						.getCalcEnv()));
				// 环境对象中设置动态区域的关键字、指标映射
				Context contextVO = (Context)cellsPane.getContext();
				DataProcessSrv dataProcessSrv = new DataProcessSrv(
						contextVO,
						dataModel, true);
				Object repIdObj = contextVO.getAttribute(REPORT_PK);
				String strRepId = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null; 
				
				(proxy.getCalcEnv()).setFieldMaps(FieldMapUtil.getFieldMaps(
						dynAreaCell.getDynAreaPK(), dataProcessSrv, strRepId, proxy.getCalcEnv()));

				try {
					expr = proxy.parseExpr(element.getFormulaContent());
					element.setLet(expr);
				} catch (ParseException ex) {
//					AppDebug.debug(ex);
				}

				proxy.unRegisterFuncDrivers(GAggrFuncDriver.class.getName());
				return element.getLet().toUserDefString(proxy.getCalcEnv());
			} catch (Exception ex) {
				AppDebug.debug(ex);
				return element.getFormulaContent();
			}
		}

	}

	static{
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new GroupStatFuncRenderer());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.IPlugIn#startup()
	 */
	public void startup() {
//		getReport().getTable().getCells().registExtSheetRenderer(
//				new GroupStatFuncRenderer());
	}

	protected IPluginDescriptor createDescriptor() {
		return new DataProcessDescripter(this);
	}

	public UfoFmlExecutor getFormulaHandler() {
		FormulaModel formulaModel = FormulaModel.getInstance(getCellsModel());
		return formulaModel.getUfoFmlExecutor();
//		
//		FormulaDefPlugin pi = (FormulaDefPlugin) getReport().getPluginManager()
//				.getPlugin(FormulaDefPlugin.class.getName());
//		return pi.getFmlExecutor();
	}

	private UfoContextVO getContextVO() {
		return (UfoContextVO) getReport().getContextVo();
	}
}
