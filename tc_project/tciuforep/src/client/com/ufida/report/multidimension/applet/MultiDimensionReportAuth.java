/*
 * �������� 2006-9-21
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.ufida.report.multidimension.applet;


import com.ufida.report.multidimension.model.MultiDimWriteBackUtil;
import com.ufida.report.multidimension.model.MultiReportEnv;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellsAuthorization;

public class MultiDimensionReportAuth implements CellsAuthorization {
	private		DimensionPlugin   	m_plugin;
	private     MultiReportEnv		m_reportEnv;
	public MultiDimensionReportAuth(DimensionPlugin plugin, MultiReportEnv env){
		m_plugin = plugin;
		m_reportEnv = env;
	}

	public void authorize(int row, int col, int type) {

	}

	public boolean isReadable(int row, int col) {
		return true;
	}

	public boolean isWritable(int row, int col) {
		UfoReport report = m_plugin.getReport();

		if (report.getOperationState() == UfoReport.OPERATION_FORMAT || 
			m_plugin.getModel().isDataEditable() == false ){
			return false;
		}else{
			return MultiDimWriteBackUtil.getCellEditableStatus(m_plugin.getModel(), m_reportEnv, row, col);
		}
	}

}
