package com.ufida.report.multidimension.applet;

import java.awt.Point;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

import com.ufsoft.report.sysplugin.dnd.IDndAdapter;

/**
 * 维度设置界面的JListDndAdapter
 * 
 * @author ll
 */
public class SelMemberDndAdapter implements IDndAdapter {

	private JList m_list;

	public SelMemberDndAdapter(JList list) {
		m_list = list;
		ListModel dataModel = m_list.getModel();
		// 转换模型为DefaultListModel的实例。
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
		return m_list.getSelectedValue();
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
//		SelDimMemberVO data = (SelDimMemberVO) obj;

		// 插入对象
		int index = m_list.locationToIndex(ap);
		if (index >= 0){
			getDefaultListModel().add(index, obj);
		}
		else{
			getDefaultListModel().addElement(obj);
		}

		return true;
	}

	private DefaultListModel getDefaultListModel() {
		return (DefaultListModel) m_list.getModel();
	}
}