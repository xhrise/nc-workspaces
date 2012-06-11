package com.ufsoft.iufo.fmtplugin.businessquery;

import com.ufsoft.iufo.fmtplugin.measure.MeasureTableModel;
import com.ufsoft.iufo.resource.StringResource;

/**
 * �˴��������������� �������ڣ�(2003-9-15 20:16:35)
 * 
 * @author������Ƽ
 */
public class ReportQueryMeasRefTM extends nc.ui.pub.beans.table.VOTableModel {
	private static final long serialVersionUID = 1480897330240974088L;

	public static int COUNT_OF_COLUMN = 4;

	private String[] COLUMN_NAMES = { StringResource.getStringResource("miufopublic473"), StringResource.getStringResource("miufo1001390"), StringResource.getStringResource("miufopublic475"),
			StringResource.getStringResource("miufo1001388") };  //"�Ƿ��ѱ�ӳ��"

	/**
	 * ReportQueryMeasRefTM ������ע�⡣
	 * 
	 * @param vos
	 *            nc.vo.pub.ValueObject[]
	 */
	public ReportQueryMeasRefTM(nc.vo.pub.ValueObject[] vos) {
		super((ReportQueryRefMeasVO[]) vos);
	}

	public int getColumnCount() {
		return COLUMN_NAMES.length;

	}

	public String getColumnName(int col) {

		return COLUMN_NAMES[col];
	}

	public Object getValueAt(int row, int column) {
		ReportQueryRefMeasVO vo = (ReportQueryRefMeasVO) getVO(row);
		switch (column) {
		case 0:
			return vo.getName();
		case 1:
			return MeasureTableModel.types[vo
					.getType()];
		case 2:
			return vo.getNote();
		case 3:
			return ReportQueryMeasRefTM.getStrsOfMapType()[vo.getMapStatus()];
		}
		return null;
	}

	//����ָ��λ�õ�ָ�꣬��col�޹أ�col����Ϊ����ֵ
	public void setValueAt(Object obj, int row, int col) {
		if (col == 3) {
			//�Ƿ���ӳ����

			ReportQueryRefMeasVO rqRefMeasVO = (ReportQueryRefMeasVO) getVOs()
					.get(row);
			if (rqRefMeasVO != null) {
				String strValue = (String) obj;
				int nMapStatus = 0;
				if (ReportQueryMeasRefTM.getStrsOfMapType()[1].equals(strValue)) {
					nMapStatus = 1;
				} else if (ReportQueryMeasRefTM.getStrsOfMapType()[2]
						.equals(strValue)) {
					nMapStatus = 2;
				}
				rqRefMeasVO.setMapStatus(nMapStatus);
			}
		}
		return;
	}

	/**
	 * @return Returns the mAPPED_STRS.
	 */
	public static String[] getStrsOfMapType() {
		String[] MAPPED_STRS = new String[] { StringResource.getStringResource("miufo1001391"), StringResource.getStringResource("miufo1001392"), StringResource.getStringResource("miufo1001393") };
		return MAPPED_STRS;
	}
}


