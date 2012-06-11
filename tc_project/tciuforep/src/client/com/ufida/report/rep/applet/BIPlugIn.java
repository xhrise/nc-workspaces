package com.ufida.report.rep.applet;

import com.ufida.report.rep.model.BaseReportModel;
import com.ufsoft.report.plugin.AbstractPlugIn;

public abstract class BIPlugIn extends AbstractPlugIn {

	public abstract BaseReportModel getModel(); // BI报表模型

	public abstract void setOperationState(int newState);// 设置状态

	public abstract int getOperationState();// 获取状态

}
