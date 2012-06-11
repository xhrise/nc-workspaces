package com.ufsoft.iuforeport.tracedata;

import java.awt.Container;

import com.ufida.dataset.tracedata.ITraceDataResult;
import com.ufida.dataset.tracedata.TraceDataOperator;
import com.ufsoft.iufo.inputplugin.biz.file.TraceDataResultDlg;
import com.ufsoft.script.extfunc.HRTraceDataResult;

public class HRTraceDataOperator extends TraceDataOperator {
	private static final long serialVersionUID = -431134256472194842L;

	public void trace(Container container, ITraceDataResult result) {
		HRTraceDataResult r = (HRTraceDataResult) result;
		Object[][] sourDatas =r.getDatas();
		if (sourDatas != null && sourDatas.length > 0) {
			TraceDataResultDlg dlg = new TraceDataResultDlg(container,r);
			dlg.show();
		}
	}
}
