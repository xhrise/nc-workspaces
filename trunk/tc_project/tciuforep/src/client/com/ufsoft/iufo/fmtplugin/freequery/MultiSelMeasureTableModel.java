package com.ufsoft.iufo.fmtplugin.freequery;

import java.util.ArrayList;
import java.util.Arrays;

import nc.vo.iufo.measure.MeasureVO;

import com.ufsoft.iufo.fmtplugin.measure.MeasureTableModel;
import com.ufsoft.iufo.resource.StringResource;

public class MultiSelMeasureTableModel extends MeasureTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int colIndex_select = 0;

	protected int colIndex_name = 1;

	private ArrayList<MeasureVO> m_alSelected = new ArrayList<MeasureVO>();

	/**
	 * 此处插入方法描述。 创建日期：(2003-9-18 14:07:56)
	 */
	public MultiSelMeasureTableModel() {
		
	}

	public Object getValueAt(int row, int column) {
		if (column == colIndex_select) {
			MeasureVO vo = getVO(row);
			return isSelectedMeasure(vo);
		}
		
		return super.getValueAt(row, column - 1);
	}

	public boolean isCellEditable(int row, int col) {
		if (col == colIndex_select)
			return true;
		return super.isCellEditable(row, col - 1);
	}

	private boolean isSelectedMeasure(MeasureVO vo) {
		return m_alSelected.contains(vo);
	}

	public void updateSelectedMeasure(int row, boolean isSel) {
		MeasureVO selVO = getVO(row);
		if (isSel) {
			addSelectedMeasure(selVO);
		} else
			removeSelectedMeasure(selVO);
	}
	
	public void addSelectedMeasure(MeasureVO selVO){
		if (isSameKeyGroup(m_alSelected, selVO)) {
			if (!m_alSelected.contains(selVO))
				m_alSelected.add(selVO);
		}
	}
	public void removeSelectedMeasure(MeasureVO selVO){
		m_alSelected.remove(selVO);
	}
	static boolean isSameKeyGroup(ArrayList<MeasureVO> als, MeasureVO newVO){
		if(als == null || als.size() == 0 || newVO == null)
			return true;
		return als.get(0).getKeyCombPK().equals(newVO.getKeyCombPK());
	}
	public boolean isSameKeyGroup(MeasureVO newVO){
		if(m_alSelected == null || m_alSelected.size() == 0 || newVO == null)
			return true;
		return m_alSelected.get(0).getKeyCombPK().equals(newVO.getKeyCombPK());
	}
	public MeasureVO[] getAllSelectedMeasureVOs() {
		return m_alSelected.toArray(new MeasureVO[0]);
	}

	public void setAllSelectedMeasureVOs(MeasureVO[] selMeasures) {
		if (m_alSelected == null)
			m_alSelected = new ArrayList<MeasureVO>();
		m_alSelected.clear();
		if (selMeasures != null)
			m_alSelected.addAll(Arrays.asList(selMeasures));
	}

	public int getColumnCount() {
		return super.getColumnCount() + 1;
	}

	public String getColumnName(int c) {
		if (c == 0)
			return StringResource.getStringResource("miufo1000589");// 选择
		return super.getColumnName(c - 1);
	}

	public Class getColumnClass(int c) {
		if (c == 0)
			return Boolean.class;
		return super.getColumnClass(c - 1);
	}

	// 更新指定位置的指标，和col无关，col可以为任意值
	public void setValueAt(Object obj, int row, int col) {
		if (col == colIndex_select) {
			if (obj instanceof Boolean)
				updateSelectedMeasure(row, ((Boolean) obj).booleanValue());
		}
		return;
	}

}
