package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import nc.pub.iufo.cache.ReportCache;
import nc.util.iufo.pub.UFOString;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.resource.StringResource;

public class MeasureTableModel extends DefaultTableModel {
	public static String[] types = new String[] { StringResource.getStringResource("miufopublic265"),
			StringResource.getStringResource("miufopublic266"), StringResource.getStringResource("miufopublic283"),
			StringResource.getStringResource("miufopublic315") }; // @i18n

	// miufopublic315=日期

	protected Vector r_vector = new Vector();

	private MeasureVO[] allMeasureVos = null;//保存原指标缓存，在查找过滤的时候不用再查询指标

	public static MeasureColumnModel columnNames[] = {
			new MeasureColumnModel(StringResource.getStringResource("miufopublic262"), 100, JLabel.LEFT), // "指标名称"
			new MeasureColumnModel(StringResource.getStringResource("miufo1000773"), 100, JLabel.LEFT), // "指标类型"
			new MeasureColumnModel(StringResource.getStringResource("miufo1000774"), 100, JLabel.LEFT), // "指标说明"
			new MeasureColumnModel(StringResource.getStringResource("miufo1001530"), 100, JLabel.CENTER) }; // "所属报表"

	public static int columnWidths[] = { 150, 60, 80, 150 };
	protected int colIndex_name = 0;

	/**
	 * 此处插入方法描述。 创建日期：(2003-9-18 14:07:56)
	 */
	public MeasureTableModel() {
	}

	public MeasureTableModel(MeasureVO[] vos) {
		resetTable(vos, true);
	}

	/**
	 * 按照所选的列排序 创建日期：(2002-6-3 16:24:47)
	 * 
	 * @param table
	 *            javax.swing.JTable
	 */
	public void addMouseListener(final javax.swing.JTable ta) {
		ta.getTableHeader().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				int col = ta.columnAtPoint(event.getPoint());

				if (col == colIndex_name) {// 名称
					Collections.sort(r_vector, new Comparator() {
						public int compare(Object obj1, Object obj2) {
							String strName1 = ((MeasureVO) obj1).getName();
							String strName2 = ((MeasureVO) obj2).getName();
							return nc.util.iufo.pub.UFOString.compareHZString(strName1, strName2);
						}
					});
				} else if (col == colIndex_name + 1) {// 类型
					Vector vecNumb = new Vector();
					Vector vecChar = new Vector();
					Vector vecCode = new Vector();
					for (int i = 0; i < r_vector.size(); i++) {
						MeasureVO vo = (MeasureVO) r_vector.get(i);
						if (vo.getType() == MeasureVO.TYPE_NUMBER) {
							vecNumb.addElement(vo);
						} else if (vo.getType() == MeasureVO.TYPE_CHAR) {
							vecChar.addElement(vo);
						} else if (vo.getType() == MeasureVO.TYPE_CODE) {
							vecCode.addElement(vo);
						}
					}
					r_vector.clear();
					r_vector.addAll(vecNumb);
					r_vector.addAll(vecChar);
					r_vector.addAll(vecCode);
				} else {
					return;
				}

				fireTableDataChanged();
			}
		});
	}

	/**
	 * 得到当前TableModel中的全部指标 创建日期：(2003-10-13 17:38:48)
	 * 
	 * @return nc.vo.iufo.measure.MeasureVO[]
	 */
	public MeasureVO[] getAllMeasureVOByTable() {
		// MeasureVO[] meas = new MeasureVO[r_vector.size()];
		// r_vector.copyInto(meas);
		if (allMeasureVos == null) {
			this.allMeasureVos = new MeasureVO[0];
		}
		return this.allMeasureVos;
	}

	public Class getColumnClass(int c) {
		return r_vector.get(c).getClass();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int c) {
		return columnNames[c].title;
	}

	public int getRowCount() {
		if (r_vector == null)
			return 0;
		return r_vector.size();
	}

	public Object getValueAt(int row, int column) {
		return getMeasValueAt(row, column);
	}

	private Object getMeasValueAt(int row, int col) {
		MeasureVO vo = (MeasureVO) r_vector.get(row);
		if (col == colIndex_name) {// 名称
			return vo.getName();
		} else if (col == colIndex_name + 1) {// 类型
			return types[vo.getType()];
		} else if (col == colIndex_name + 2) {// 说明
			return vo.getNote();
		} else {
			ReportCache reportCache = CacheProxy.getSingleton().getReportCache();
			String repid = vo.getReportPK();
			if (repid != null && repid.length() > 0) {
				ReportVO repvo = (ReportVO) reportCache.get(repid);
				if (repvo != null)
					return "(" + repvo.getCode() + ")" + repvo.getName();
			}
		}
		return null;
	}

	public int getRowIndex(MeasureVO vo) {
		return r_vector.indexOf(vo);
	}

	public MeasureVO getVO(int row) {
		MeasureVO result = null;
		try {
			result = (MeasureVO) r_vector.get(row);
		} catch (Exception e) {
		}
		return result;
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/**
	 * modify by wangyga 2008-7-16 修改不能重复查询的BUG
	 * 
	 * @param vos
	 * @param isChangeRep
	 */
	// 重新设着指标table
	public void resetTable(MeasureVO[] vos, boolean isChangeRep) {
		if (isChangeRep) {
			allMeasureVos = vos;
		}
		r_vector.clear();
		if (vos != null) {
			Arrays.sort(vos, new Comparator() {
				public int compare(Object a, Object b) {
					if (a != null && b != null) {
						String aName = ((MeasureVO) a).getName();
						String bName = ((MeasureVO) b).getName();
						return UFOString.compareHZString(aName, bName);
					}
					return -1;
				}
			});

			for (int i = 0; i < vos.length; i++) {
				r_vector.addElement(vos[i]);
			}
		}
		fireTableDataChanged();
	}

	// 更新指定位置的指标，和col无关，col可以为任意值
	public void setValueAt(Object obj, int row, int col) {
		return;
	}
}
