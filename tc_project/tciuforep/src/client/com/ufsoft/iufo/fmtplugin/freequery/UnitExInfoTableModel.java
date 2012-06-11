package com.ufsoft.iufo.fmtplugin.freequery;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import nc.itf.iufo.exproperty.IExPropConstants;
import nc.util.iufo.product.ProductBizHelper;
import nc.util.iufo.pub.UFOString;
import nc.vo.iufo.exproperty.ExPropertyVO;
import nc.vo.iufo.measure.UnitExInfoVO;
import nc.vo.iufo.resmng.uitemplate.ResOperException;

import com.ufida.web.WebException;
import com.ufsoft.iufo.fmtplugin.measure.MeasureColumnModel;
import com.ufsoft.iufo.resource.StringResource;

public class UnitExInfoTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	String[][] items = {
			{ Integer.toString(ExPropertyVO.TYPE_CHAR),
					StringResource.getStringResource("miufopublic266") },// 0,字符
			{ Integer.toString(ExPropertyVO.TYPE_REF),
					StringResource.getStringResource("miufopublic283") },// 2,参照
			{ Integer.toString(ExPropertyVO.TYPE_LOGIC),
					StringResource.getStringResource("miufo1003713") },// 3,逻辑
	};

	protected Vector<UnitExInfoVO> r_vector = new Vector<UnitExInfoVO>();

	public static MeasureColumnModel columnNames[] = {
			new MeasureColumnModel(StringResource
					.getStringResource("miufo1003712"), 100, JLabel.LEFT), // =结构名称
			new MeasureColumnModel(StringResource
					.getStringResource("miufopublic263"), 100, JLabel.LEFT), // =类型
			new MeasureColumnModel(StringResource
					.getStringResource("ubiquery0104"), 100, JLabel.LEFT), // =长度
			new MeasureColumnModel(StringResource
					.getStringResource("miufo1000766"), 100, JLabel.CENTER) }; // =参照
	public static int columnWidths[] = { 150, 60, 80, 150 };
	protected int colIndex_name = 1;
	private ArrayList<UnitExInfoVO> m_alSelected = new ArrayList<UnitExInfoVO>();

	/**
	 * 此处插入方法描述。 创建日期：(2003-9-18 14:07:56)
	 */
	public UnitExInfoTableModel() {
	}

	public UnitExInfoTableModel(UnitExInfoVO[] vos) {
		resetTable(vos);
	}

	/**
	 * 按照所选的列排序 创建日期：(2002-6-3 16:24:47)
	 * 
	 * @param table
	 *            javax.swing.JTable
	 */
	public void addMouseListener(final javax.swing.JTable ta) {
		ta.getTableHeader().addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unchecked")
			public void mouseClicked(MouseEvent event) {
				int col = ta.columnAtPoint(event.getPoint());

				if (col == colIndex_name) {// 名称
					Collections.sort(r_vector, new Comparator() {
						public int compare(Object obj1, Object obj2) {
							String strName1 = ((UnitExInfoVO) obj1).getExPropertyVO().getName();
							String strName2 = ((UnitExInfoVO) obj2).getExPropertyVO().getName();
							return nc.util.iufo.pub.UFOString.compareHZString(
									strName1, strName2);
						}
					});
				} else if (col == colIndex_name + 1) {// 类型
					Vector vecLogin = new Vector();
					Vector vecChar = new Vector();
					Vector vecRefCode = new Vector();
					for (int i = 0; i < r_vector.size(); i++) {
						UnitExInfoVO vo = (UnitExInfoVO) r_vector.get(i);
						int type = vo.getExPropertyVO().getType();
						if (type == ExPropertyVO.TYPE_LOGIC) {
							vecLogin.addElement(vo);
						} else if (type == ExPropertyVO.TYPE_CHAR) {
							vecChar.addElement(vo);
						} else if (type == ExPropertyVO.TYPE_REF) {
							vecRefCode.addElement(vo);
						}
					}
					r_vector.clear();
					r_vector.addAll(vecLogin);
					r_vector.addAll(vecChar);
					r_vector.addAll(vecRefCode);
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
	public UnitExInfoVO[] getAllExpropVOByTable() {
		UnitExInfoVO[] meas = new UnitExInfoVO[r_vector.size()];
		r_vector.copyInto(meas);
		return meas;
	}

	public Class getColumnClass(int c) {
		if (c == 0)
			return Boolean.class;
		return r_vector.get(c).getClass();
	}

	public int getColumnCount() {
		return columnNames.length + 1;
	}

	public String getColumnName(int c) {
		if (c == 0)
			return StringResource.getStringResource("miufo1000589");// 选择
		return columnNames[c - 1].title;
	}

	public int getRowCount() {
		if (r_vector == null)
			return 0;
		return r_vector.size();
	}

	public Object getValueAt(int row, int column) {
		if (column == 0) {
			UnitExInfoVO vo = getVO(row);
			return isSelectedPropVO(vo);
		}
		return getPropValueAt(row, column);
	}

	private Object getPropValueAt(int row, int col) {
		UnitExInfoVO vo1 = (UnitExInfoVO) r_vector.get(row);
		ExPropertyVO vo = vo1.getExPropertyVO();
		if (col == colIndex_name) {// 名称
			return vo.getName();
		} else if (col == colIndex_name + 1) {// 类型
			return getTypeNameByVO(vo);
		} else if (col == colIndex_name + 2) {// 长度
			return vo.getLen();
		} else {// 参照
			if (vo.getRefTypePK() != null) {
				String strRefItemName;
				try {
					strRefItemName = ProductBizHelper.getRefItemName(
							IExPropConstants.EXPROP_MODULE_UNIT, vo
									.getRefTypePK());
				} catch (ResOperException e) {
					throw new WebException(e.getExResourceId());
				}
				return strRefItemName;
			}
		}
		return null;
	}

	private String getTypeNameByVO(ExPropertyVO vo) {
		int type = vo.getType();
		for (String[] strs : items) {
			if (strs[0].equals(String.valueOf(type)))
				return strs[1];
		}
		return null;
	}

	public UnitExInfoVO getVO(int row) {
		UnitExInfoVO result = null;
		try {
			result = (UnitExInfoVO) r_vector.get(row);
		} catch (Exception e) {
		}
		return result;
	}
    
	public int getRowIndex(UnitExInfoVO vo){
		return r_vector.indexOf(vo);
	}
	
	public boolean isCellEditable(int row, int col) {
		if(col == 0)
			return true;
		return false;
	}

	// 重新设着指标table
	@SuppressWarnings("unchecked")
	public void resetTable(UnitExInfoVO[] vos) {
		r_vector.clear();
		if (vos != null) {
			Arrays.sort(vos, new Comparator() {
				public int compare(Object a, Object b) {
					if (a != null && b != null) {
						String aName = ((UnitExInfoVO) a).getExPropertyVO().getName();
						String bName = ((UnitExInfoVO) b).getExPropertyVO().getName();
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
		if (col == 0) {
			if (obj instanceof Boolean)
				updateSelectedPropVO(row, ((Boolean) obj).booleanValue());
		}
		return;
	}

	private boolean isSelectedPropVO(UnitExInfoVO vo) {
		return m_alSelected.contains(vo);
	}

	public void updateSelectedPropVO(int row, boolean isSel) {
		if (isSel) {
			UnitExInfoVO selVO = getVO(row);
			if (!m_alSelected.contains(selVO))
				m_alSelected.add(selVO);
		} else
			m_alSelected.remove(getVO(row));
	}
	public UnitExInfoVO[] getSelPropVOs(){
		return m_alSelected.toArray(new UnitExInfoVO[0]);
	}
	
	public boolean removeSelectedPropVO(UnitExInfoVO vo){
		return  m_alSelected.remove(vo);
	}
	public void setSelPropVOs(UnitExInfoVO[] selVOs){
		m_alSelected.clear();
		if(selVOs != null){
			for(UnitExInfoVO vo:selVOs){
				m_alSelected.add(vo);
			}
		}
	}
}
