package com.ufsoft.iufo.inputplugin.biz;

import com.ufida.dataset.tracedata.TraceDataParam;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizHelper;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceNavExt;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceNavPanel;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaTraceBizLink;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.MultiValueSelectDlg;
import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedData;
import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.FormulaTraceValueItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaTraceValueItem;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.script.extfunc.DataSetFuncCalcUtil;

public class FormulaTraceOperBizHelper {
	private FormulaTraceOperBizHelper() {
	}

	/**
	 * @i18n uiuforep00131=û�п�׷�ٵĶ�ֵѡ��
	 */
	public static void doFormulaTraceMultiValues(UfoReport ufoReport, IFormulaTraceValueItem[] formulaTraceValueItems) {
		if (ufoReport == null) {
			return;
		}
		if (formulaTraceValueItems == null) {
			UfoPublic.sendWarningMessage(MultiLang.getString("uiuforep00131"), ufoReport);// û�п�׷�ٵĶ�ֵѡ��
			// TODO
		}
		MultiValueSelectDlg multiValueSelectDlg = new MultiValueSelectDlg(ufoReport, formulaTraceValueItems);
		multiValueSelectDlg.show();
		if (multiValueSelectDlg.getResult() == UfoDialog.ID_OK) {
			// trace single value to other taks-report
			IFormulaTraceValueItem selectedValueItem = multiValueSelectDlg.getSelectedValueItem();
			// �����������������ֵ׷������
			doTraceValue(ufoReport, selectedValueItem);
		}
	}

	public static void doTraceValue(UfoReport ufoReport, IFormulaTraceValueItem formulaTraceValueItem) {
		IFormulaTraceBizLink formulaTraceBizLink = FormulaTraceBizHelper.getIFormulaTraceBizLink();
		formulaTraceBizLink.doTraceValue(ufoReport, formulaTraceValueItem);
	}

	/**
	 * @i18n uiuforep00132=û�п�׷�ٵ�ֵ
	 */
	public static void doFormulaTraceCal(UfoReport ufoReport, IFormulaTraceValueItem formulaTraceValueItem) {
		if (ufoReport == null) {
			return;
		}
		if (formulaTraceValueItem == null) {
			UfoPublic.sendWarningMessage(MultiLang.getString("uiuforep00132"), ufoReport);// û�п�׷�ٵ�ֵ
			// TODO
		}
		// ��ʽ׷��-����
		FormulaTracePlugIn plugIn = (FormulaTracePlugIn) ufoReport.getPluginManager().getPlugin(FormulaTracePlugIn.class.getName());
		IExtension[] exts = plugIn.getDescriptor().getExtensions();
		FormulaTraceNavPanel panel = (FormulaTraceNavPanel) ((FormulaTraceNavExt) exts[1]).getFormulaTraceNavPanel();
		FormulaParsedData formulaParedData = panel.getFormulaParsedData();
		if (formulaParedData != null && formulaParedData.getFormulaParedDataItems() != null
				&& formulaParedData.getFormulaParedDataItems().length > panel.getCurSelectedRow()) {
			FormulaParsedDataItem curFormulaParsedDataItem = formulaParedData.getFormulaParedDataItems()[panel.getCurSelectedRow()];
			// ˢ��ֵ
			if (!curFormulaParsedDataItem.isInTraceNow()) {// @edit by ll at
				// 2008-12-31,����02:21:05
				// ����ʱ���ø��¼�����
				curFormulaParsedDataItem.setNeedToCal(false);
				if (formulaTraceValueItem.getValue() == null) {
					curFormulaParsedDataItem.setFormulaValue("");
				} else {
					curFormulaParsedDataItem.setFormulaValue(formulaTraceValueItem.getValue());
				}
			}
			TraceDataParam traceParam = (TraceDataParam) ((FormulaTraceValueItem) formulaTraceValueItem)
					.getAttribute(DataSetFuncCalcUtil.EXT_FMT_TRACEDATAPARMA);
			if (traceParam != null)
				curFormulaParsedDataItem.setTraceDataParam(traceParam);
			panel.repaint();
		}
	}
}
