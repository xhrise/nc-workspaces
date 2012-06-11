package com.ufida.report.anareport.applet;

import java.awt.Point;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.dataset.metadata.Field;
import com.ufida.report.adhoc.model.IFldCountType;
import com.ufida.report.anareport.model.FieldCountDef;
import com.ufsoft.iufo.fmtplugin.freequery.UniqueList;
import com.ufsoft.report.sysplugin.dnd.IDndAdapter;

/**
 * 交叉表设置界面的JListDndAdapter
 * 
 * @author ll
 */
public class AnaCrossSetAdapter implements IDndAdapter {
	public static final int TYPE_ALL = 0;

	public static final int TYPE_ONLY_CHARACTER = 1;

	public static final int TYPE_ONLY_NUMERIC = 2;

	private UniqueList m_list;

	private boolean m_bInSameList = false;
	private int m_supportType = -1;
	private boolean canInsert = true;
	public AnaCrossSetAdapter(UniqueList list, int supportType){
		this(list, supportType, true);
	}
	public AnaCrossSetAdapter(UniqueList list, int supportType, boolean bInsert) {
		m_list = list;
		m_supportType = supportType;
		canInsert = bInsert;
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

	private class DragInfo {
		private Object dragObj;

		private Object dragSource;
		private boolean canInsert = true;
		public DragInfo(Object source, Object obj) {
			this(source, obj, true);
		}
		public DragInfo(Object source, Object obj, boolean bInsert) {
			dragSource = source;
			dragObj = obj;
			this.canInsert = bInsert;
		}
		public Object getDragObj() {
			return dragObj;
		}

		public Object getDragSource() {
			return dragSource;
		}
		public boolean bInsert(){
			return canInsert;
		}
	}

	/*
	 * @see dnd.IDndAdapter#getSourceObject()
	 */
	public Object getSourceObject() {
		return new DragInfo(m_list, m_list.getSelectedValue(), canInsert);
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
		if(!canInsert)
			return true;
		m_bInSameList = (((DragInfo) obj).getDragSource() == m_list);
		obj = ((DragInfo) obj).getDragObj();
		Field fld = (Field) obj;
		int existIndex = m_list.getObjIndex(fld);
		if (!m_bInSameList && existIndex >= 0)// 控制对象的重复插入
			return false;

		// 判断fld类型
		if (m_supportType == TYPE_ONLY_CHARACTER && DataTypeConstant.isNumberType(fld.getDataType()))
			return false;

		if (m_supportType == TYPE_ONLY_NUMERIC && !DataTypeConstant.isNumberType(fld.getDataType()))
			return false;

		Object insertObj = m_bInSameList ? obj : fld;
		if(!m_bInSameList && m_supportType == TYPE_ONLY_NUMERIC )
			insertObj = new FieldCountDef(fld, IFldCountType.TYPE_SUM, null,null, null);

		// 插入对象
		int index = m_list.locationToIndex(ap);
		if (index < 0)
			index = Math.max(0, getDefaultListModel().getSize() - 1);

		getDefaultListModel().add(index, insertObj);
		if (!m_bInSameList)
			m_list.setSelectedIndex(index);

		return true;
	}

	private DefaultListModel getDefaultListModel() {
		return (DefaultListModel) m_list.getModel();
	}

}
