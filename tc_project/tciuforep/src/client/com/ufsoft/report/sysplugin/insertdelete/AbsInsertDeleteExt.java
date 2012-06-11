package com.ufsoft.report.sysplugin.insertdelete;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbsActionExt;

public abstract class AbsInsertDeleteExt extends AbsActionExt {
	private UfoReport _report;
	protected AbsInsertDeleteExt(UfoReport report) {
		_report = report;
	}
	protected UfoReport getReport(){
		return _report;
	}
	protected boolean isFormatState(){
		return StateUtil.isFormatState(getReport(), null);
	}
}
