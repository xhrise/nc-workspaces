package com.ufsoft.iufo.inputplugin.inputcore;

import com.ufsoft.iufo.inputplugin.MeasTraceInfo;
import com.ufsoft.iufo.inputplugin.biz.WindowNavUtil;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizUtil;
import com.ufsoft.iufo.inputplugin.querynavigation.FormulaTraceNavigation;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;

import nc.ui.iufo.input.edit.IPostRepDataEditorActive;
import nc.ui.iufo.input.edit.RepDataEditor;

public class WinNavPostRepDataActive implements IPostRepDataEditorActive{
	private static final long serialVersionUID = -2759426679556655233L;
	
	private MeasTraceInfo finMeasTraceInfo=null;
	private boolean bFormulaTrace=false;
	
	public WinNavPostRepDataActive(MeasTraceInfo finMeasTraceInfo,boolean bFormulaTrace){
		this.finMeasTraceInfo=finMeasTraceInfo;
		this.bFormulaTrace=bFormulaTrace;
	}
	public void afterRepDataActive(RepDataEditor editor) {
		CellsModel cellsModel = editor.getCellsModel();
		if (cellsModel == null
				|| (finMeasTraceInfo.isGeneryQuery() && editor.getVerFromPanel() == null)) {
			UfoPublic.sendWarningMessage(MultiLang.getString("uiuforep00104"),
					null);
		} else {
			// 计算获得公式位置信息
			String strMeasurePK = finMeasTraceInfo.getStrMeasurePK();
			String[] strKeyVals = finMeasTraceInfo.getStrKeyVals();
			// Object repIdObj = inputContextVO.getAttribute(REPORT_PK);
			// String strReportPK = repIdObj != null && (repIdObj instanceof
			// String)? (String)repIdObj:null;

			IArea[] curTracedPos = WindowNavUtil.calMeasureTracedPos(editor
					.getRepPK(), cellsModel, strMeasurePK, strKeyVals);

			// 追踪相关状态设置:主要是高亮显示
			WindowNavUtil.setTraceInfo(editor,cellsModel, curTracedPos);
			CellPosition selPos = editor.getTraceCells().get(0);

			if (bFormulaTrace)
				WindowNavUtil.refreshNavPanel(editor, selPos);
			
			FormulaTraceBizUtil.setView2HighlightArea(editor.getTable(),selPos,true);	

			FormulaTraceNavigation.getInstance(editor.getMainboard()).addView(editor, selPos);

		}
	}

}
