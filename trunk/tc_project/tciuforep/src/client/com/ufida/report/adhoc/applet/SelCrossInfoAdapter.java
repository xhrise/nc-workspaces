package com.ufida.report.adhoc.applet;

import java.awt.Point;

import javax.swing.DefaultListModel;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.ufida.report.adhoc.model.AdhocPageDimField;
import com.ufida.report.rep.model.SortDescriptor;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.data.MetaDataTypes;
import com.ufsoft.iufo.fmtplugin.freequery.UniqueList;
import com.ufsoft.report.sysplugin.dnd.IDndAdapter;

/**
 * 交叉表设置界面的JListDndAdapter
 * 
 * @author ll
 */
public class SelCrossInfoAdapter implements IDndAdapter {
	public static final int TYPE_ALL = 0;

	public static final int TYPE_ONLY_CHARACTER = 1;

	public static final int TYPE_ONLY_NUMERIC = 2;

	private JTree m_tree;

	private UniqueList m_list;

	private boolean m_bInSameList = false;
	private int m_supportType = -1;

	public SelCrossInfoAdapter(UniqueList list, int supportType) {
		m_list = list;
		m_supportType = supportType;
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

	public SelCrossInfoAdapter(JTree tree) {
		m_tree = tree;
	}

	private class DragInfo {
		private Object dragObj;

		private Object dragSource;

		public DragInfo(Object source, Object obj) {
			dragSource = source;
			dragObj = obj;
		}

		public Object getDragObj() {
			return dragObj;
		}

		public Object getDragSource() {
			return dragSource;
		}
	}

	/*
	 * @see dnd.IDndAdapter#getSourceObject()
	 */
	public Object getSourceObject() {
		if (m_tree != null)
			return new DragInfo(m_tree, ((DefaultMutableTreeNode) m_tree.getLastSelectedPathComponent())
					.getUserObject());
		else
			return new DragInfo(m_list, m_list.getSelectedValue());
	}

	/*
	 * @see dnd.IDndAdapter#removeSourceNode()
	 */
	public void removeSourceNode() {
		if (m_tree != null)
			return;
		int index = m_list.getSelectedIndex();
		getDefaultListModel().remove(index);
		
	}

	/*
	 * @see dnd.IDndAdapter#insertObject(java.awt.Point, java.lang.Object)
	 */
	public boolean insertObject(Point ap, Object obj) {
		if (m_tree != null)// 树上不增加节点
			return true;
		m_bInSameList = (((DragInfo) obj).getDragSource() == m_list);
		obj = ((DragInfo) obj).getDragObj();
		IMetaData fld = null;
		if (obj instanceof AdhocPageDimField)
			fld = ((AdhocPageDimField) obj).getField();
		else if (obj instanceof SortDescriptor)
			fld = ((SortDescriptor) obj).getField();
		else
			fld = (IMetaData)obj;
		int existIndex = m_list.getObjIndex(fld);
		if (!m_bInSameList && existIndex>=0)// 控制对象的重复插入
			return false;

		// 判断fld类型
		if (m_supportType == TYPE_ONLY_CHARACTER && MetaDataTypes.isNumeric(fld.getDataType()))
			return false;

		if (m_supportType == TYPE_ONLY_NUMERIC && !MetaDataTypes.isNumeric(fld.getDataType()))
			return false;

		Object insertObj = m_bInSameList?obj:fld;

		// 插入对象
		int index = m_list.locationToIndex(ap);
		if (index < 0)
			index = Math.max(0,getDefaultListModel().getSize() -1);
		
		getDefaultListModel().add(index, insertObj);
		if(!m_bInSameList)
			m_list.setSelectedIndex(index);
		
		return true;
	}

	private DefaultListModel getDefaultListModel() {
		return (DefaultListModel) m_list.getModel();
	}
}