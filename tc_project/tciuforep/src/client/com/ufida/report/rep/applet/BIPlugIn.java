package com.ufida.report.rep.applet;

import com.ufida.report.rep.model.BaseReportModel;
import com.ufsoft.report.plugin.AbstractPlugIn;

public abstract class BIPlugIn extends AbstractPlugIn {

	public abstract BaseReportModel getModel(); // BI����ģ��

	public abstract void setOperationState(int newState);// ����״̬

	public abstract int getOperationState();// ��ȡ״̬

}
