package com.ufida.report.multidimension.applet;

import java.awt.Point;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.SelDimensionVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.sysplugin.dnd.IDndAdapter;

/**
 * 维度设置界面的JListDndAdapter
 * 
 * @author ll
 */
public class SelDimDndAdapter implements IDndAdapter {

	private JList m_list;

	private SelDimSetPanel m_processor = null;

	public SelDimDndAdapter(JList list, SelDimSetPanel panel) {
		m_list = list;
		m_processor = panel;
		ListModel dataModel = m_list.getModel();
		//转换模型为DefaultListModel的实例。
		if (!(dataModel instanceof DefaultListModel)) {
			DefaultListModel newModel = new DefaultListModel();
			for (int i = 0; i < dataModel.getSize(); i++) {
				newModel.addElement(dataModel.getElementAt(i));
			}
			m_list.setModel(newModel);
		}
	}

	/*
	 * @see dnd.IDndAdapter#getSourceObject()
	 */
	public Object getSourceObject() {
//		SelDimensionVO data = (SelDimensionVO) m_list.getSelectedValue();
//		if(data.canBeMoved())
			return m_list.getSelectedValue();
//		else
//			return null;
	}

	/*
	 * @see dnd.IDndAdapter#removeSourceNode()
	 */
	public void removeSourceNode() {
		int index = m_list.getSelectedIndex();
		getDefaultListModel().remove(index);
	}

	/*
	 * @see dnd.IDndAdapter#insertObject(java.awt.Point, java.lang.Object)
	 */
	public boolean insertObject(Point ap, Object obj) {
		SelDimensionVO data = (SelDimensionVO) obj;
		
		//指标不能放在页维度区
		if(data.getDimDef().getPrimaryKey().equals(IMultiDimConst.PK_MEASURE_DIMDEF)){
			if(StringResource.getStringResource("ubimultidim0004").equals(m_list.getName()))
				return false;
		}
		
		//如果还没有选择成员，弹出成员选择框
		if (data.getSelMembers() == null) {
			if (!m_processor.doSelMember(data))
				return false;
		}
		
		//插入对象
		int index = m_list.locationToIndex(ap);
		if (index >= 0)
			getDefaultListModel().add(index, obj);
		else
			getDefaultListModel().addElement(obj);

		return true;
	}

	private DefaultListModel getDefaultListModel() {
		return (DefaultListModel) m_list.getModel();
	}
}