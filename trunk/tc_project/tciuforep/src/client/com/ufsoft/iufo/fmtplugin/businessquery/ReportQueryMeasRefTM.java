package com.ufsoft.iufo.fmtplugin.businessquery;

import com.ufsoft.iufo.fmtplugin.measure.MeasureTableModel;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 此处插入类型描述。 创建日期：(2003-9-15 20:16:35)
 * 
 * @author：刘良萍
 */
public class ReportQueryMeasRefTM extends nc.ui.pub.beans.table.VOTableModel {
	private static final long serialVersionUID = 1480897330240974088L;

	public static int COUNT_OF_COLUMN = 4;

	private String[] COLUMN_NAMES = { StringResource.getStringResource("miufopublic473"), StringResource.getStringResource("miufo1001390"), StringResource.getStringResource("miufopublic475"),
			StringResource.getStringResource("miufo1001388") };  //"是否已被映射"

	/**
	 * ReportQueryMeasRefTM 构造子注解。
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

	//更新指定位置的指标，和col无关，col可以为任意值
	public void setValueAt(Object obj, int row, int col) {
		if (col == 3) {
			//是否已映射列

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


