/*
 * Created on 2005-7-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.applet;
import nc.ui.bi.query.designer.BIDesignApplet;
import nc.vo.bi.report.manager.ReportResource;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.rep.model.BIContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.sysplugin.file.FilePlugin;

/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DimChartApplet extends BIDesignApplet{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.bi.query.designer.BIDesignApplet#initUI()
	 */
	public void initUI() {
		AppDebug.debug("begin loading ......");//@devTools System.out.println("begin loading ......");
		int reportType = ReportResource.INT_REPORT_CHART;
		if (getParameter(ReportResource.REPORT_TYPE) != null) {
			reportType = Integer
					.parseInt(getParameter(ReportResource.REPORT_TYPE));
		}
		;

		int operation = UfoReport.OPERATION_FORMAT;//��ʼ״̬��Ĭ��Ϊ��ʽ״̬
		if (getParameter(ReportResource.OPERATE_TYPE) != null) {
			try {
				operation = Integer
						.parseInt(getParameter(ReportResource.OPERATE_TYPE));
			} catch (Exception e) {
				operation = UfoReport.OPERATION_FORMAT;
			}
		}
		String pk_report = getParameter(ReportResource.REPORT_ID);

		String user = getParameter(ReportResource.USER_ID) == null ? ReportResource.DEFAULT_USER_ID : getParameter(ReportResource.USER_ID);
		String unitID = getParameter(ReportResource.UNIT_ID) == null ? ReportResource.DEFAULT_UNIT_ID : getParameter(ReportResource.UNIT_ID);
		BIContextVO contextVO = new BIContextVO();
//		BaseReportModel model = getBaseReportModel(pk_report);
//		contextVO.setBaseeportModel(model);
		contextVO.setReportPK(pk_report);
		contextVO.setCurUserID(user);
		contextVO.setLoginUnitID(unitID);
		
		UfoReport report = new UfoReport(operation, contextVO);
		AppDebug.debug("begin initPlugins!");//@devTools System.out.println("begin initPlugins!");
	
		initPlugins(report, reportType);
		setJMenuBar(report.getReportMenuBar());
		AppDebug.debug("beging setContentPane");//@devTools System.out.println("beging setContentPane");
		setContentPane(report.getContentPane());
		//        }

	}

	/**
	 * ���ݲ����д�����'ҵ������''��λ''�û�'��Ϣ��ʼ���ͻ��˻����� �������ڣ�(00-7-19 10:35:05)
	 */
	protected void initClientEnvironment() {

	}


	//  ��ʼ��Adhoc�������õĲ�����ϡ�
	private void initPlugins(UfoReport report, int reportType) {
		//ɾ����ϵͳ�����������в����

		//��Ӳ����
		report.addPlugIn(FilePlugin.class.getName());
		
		if (reportType == ReportResource.INT_REPORT_CHART) {
		    report.addPlugIn(DimChartPlugin.class.getName());
		}

	}


}
