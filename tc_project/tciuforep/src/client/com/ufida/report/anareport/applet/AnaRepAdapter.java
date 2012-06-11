package com.ufida.report.anareport.applet;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import nc.vo.iufo.datasetmanager.DataSetDefVO;

import com.ufida.dataset.metadata.Field;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.report.sysplugin.dnd.IDndAdapter;
import com.ufsoft.report.sysplugin.dnd.IDragObject;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IArea;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;

public class AnaRepAdapter implements IDndAdapter {
	public static final int TYPE_ALL = 0;

	public static final int TYPE_ONLY_CHARACTER = 1;

	public static final int TYPE_ONLY_NUMERIC = 2;

	private JTree m_tree;

	private CellsPane m_cellsPane;

	private AnaReportPlugin m_plugin;
	// add by wangyga 添加字段时的目标单元,主要用来判断目标单元和原选择区域是否处于同一组合单元
	private CellPosition aimPos = null;

	public AnaRepAdapter(CellsPane cellPane, AnaReportPlugin plugin) {
		m_cellsPane = cellPane;
		m_plugin = plugin;
	}

	public AnaRepAdapter(JTree tree) {
		m_tree = tree;
	}

	public class DragInfo implements IDragObject {
		private Object dragObj;

		private Object dragSource;
		private Object oInfo;

		public DragInfo(Object source, Object obj, Object otherInfo) {
			dragSource = source;
			dragObj = obj;
			oInfo = otherInfo;
		}

		public Object getDragObj() {
			return dragObj;
		}

		public Object getDragSource() {
			return dragSource;
		}

		public Object getOtherInfo() {
			return oInfo;
		}

		// ZJB+
		public String toString() {
			return dragObj.toString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.sysplugin.dnd.IDragObject#getArrayObj()
		 */
		public Object[] getArrayObj() {
			if (isArray())
				return (Object[]) dragObj;
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.sysplugin.dnd.IDragObject#isArray()
		 */
		public boolean isArray() {
			return (dragObj instanceof Object[]);
		}
	}

	/*
	 * @see dnd.IDndAdapter#getSourceObject()
	 */
	public Object getSourceObject() {
		if (m_tree != null) {
			TreePath[] paths = m_tree.getSelectionPaths();
			if (paths == null || paths.length == 0)
				return null;
			ArrayList<Object> selList = new ArrayList<Object>();
			ArrayList<Object> otherList = new ArrayList<Object>();
			if (paths.length == 1) {
				DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) paths[0].getLastPathComponent();
				Object selObj = selNode.getUserObject();
				Object otherObj = null;
				if (selObj instanceof Field)
					otherObj = ((DefaultMutableTreeNode) selNode.getParent()).getUserObject();
				return new DragInfo(m_tree, selObj, otherObj);
			}
			String dsPK = null;
			for (int i = 0; i < paths.length; i++) {
				DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
				Object selObj = selNode.getUserObject();
				if (selObj instanceof Field) {
					Object parent = ((DefaultMutableTreeNode) selNode.getParent()).getUserObject();
					if (parent instanceof DataSetDefVO) {
						String newPk = ((DataSetDefVO) parent).getPk_datasetdef();
						if (dsPK == null)
							dsPK = newPk;
						else if (!dsPK.equals(newPk)) {// 不同数据集不可一次处理
							break;
						}
					}
					selList.add(selObj);
					otherList.add(parent);
				} else if (selObj instanceof DataSetDefVO) {
					String newPk = ((DataSetDefVO) selObj).getPk_datasetdef();
					if (dsPK == null)
						dsPK = newPk;
					else if (!dsPK.equals(newPk)) {// 不同数据集不可一次处理
						break;
					}
					int count = selNode.getChildCount();
					for (int j = 0; j < count; j++) {
						DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) selNode.getChildAt(j);
						selList.add(subNode.getUserObject());
						otherList.add(selObj);
					}
				}
			}
			return new DragInfo(m_tree, selList.toArray(new Object[0]), otherList.toArray(new Object[0]));

		} else {
			// @edit by wangyga at 2009-2-25,下午02:16:40 获得锚点的单元，因为在拖拽的过程中区域可能变大
			CellPosition anchorCell = m_cellsPane.getSelectionModel()
					.getAnchorCell();
			AnaReportModel model = m_plugin.getModel();
			CellsModel dataModel = model.getCellsModel();
			CellPosition formatPos = model.getFormatCellPos(dataModel,
					anchorCell);
			if(formatPos == null)
				return null;
			Cell formatCell = model.getFormatModel().getCell(formatPos);
			if (formatCell == null)
				return null;

			Object formatFld = formatCell
					.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
			if (formatFld != null)
				return new DragInfo(m_cellsPane, formatFld, null);
}
		return null;
	}

	/*
	 * @see dnd.IDndAdapter#removeSourceNode()
	 */
	public void removeSourceNode() {
		if (m_tree != null)// 树节点不做删除
			return;
		if (m_cellsPane != null) {// add by wangyga
			// 数据态时字段的拖拽，清除对应格式态的字段和刷新数据态的数据
			AnaReportModel model = m_plugin.getModel();
			CellsModel formatModel = model.getFormatModel();
			CellsModel dataModel = model.getDataModel();
			CellPosition[] selectCells = new CellPosition[] { formatModel.getSelectModel().getAnchorCell() };
			if (!model.isFormatState()) {
				CellPosition anchorCell = dataModel.getSelectModel().getAnchorCell();
				AreaPosition area = AreaPosition.getInstance(anchorCell, anchorCell);
				selectCells = model.getFormatPoses(dataModel, new AreaPosition[] { area });
			}
			AnaReportFieldAction.removeFlds(model, selectCells, getAimPos());
			if (!model.isFormatState())
				m_plugin.refreshDataModel(true);
		}
	}

	/*
	 * @see dnd.IDndAdapter#insertObject(java.awt.Point, java.lang.Object)
	 */
	public boolean insertObject(Point ap, Object obj) {
		if (m_tree != null)// 树上不增加节点
			return true;
		// 插入位置
		int row = m_cellsPane.rowAtPoint(ap);
		int col = m_cellsPane.columnAtPoint(ap);
		CellPosition aimPos = CellPosition.getInstance(row, col);
		aimPos = getAnaModel().getFormatCellPos(getAnaModel().getCellsModel(), aimPos);
		if (aimPos == null)
			return false;

		setAimPos(aimPos);
		if (isHasChart(aimPos))
			return false;
		AnaReportModel model = m_plugin.getModel();
		boolean refresh = !model.isFormatState();
		// 转换要插入的对象
		boolean addTitle = false;
		boolean isInRep = false;
		Object otherObj = null;
		if (obj instanceof DragInfo) {
			// ZJB+
			isInRep = (((DragInfo) obj).getDragSource() == m_cellsPane);
			otherObj = ((DragInfo) obj).getOtherInfo();
			obj = ((DragInfo) obj).getDragObj();
		}

		AnaRepField[] flds = null;
		if (obj instanceof Object[] && otherObj instanceof Object[]) {
			try {
				Object[] f = (Object[]) obj;
				flds = new AnaRepField[f.length];
				for (int i = 0; i < f.length; i++) {
					Object other = ((Object[]) otherObj)[i];
					String dsPK = null;
					if (other instanceof DataSetDefVO)
						dsPK = ((DataSetDefVO) other).getPk_datasetdef();
					flds[i] = new AnaRepField((Field) f[i], AnaRepField.TYPE_DETAIL_FIELD, dsPK);
				}
			} catch (ClassCastException ex) {
				AppDebug.debug(ex);
				return false;
			}
		} else if (obj instanceof DataSetDefVO) {
			Field[] dsFlds = ((DataSetDefVO) obj).getDataSetDef().getMetaData().getFields(true);
			flds = AnaRepField.convertFieldToRepFlds(((DataSetDefVO) obj).getPk_datasetdef(), dsFlds);
			addTitle = true;
		} else if (obj instanceof Field) {
			String dsPK = null;
			if (otherObj instanceof DataSetDefVO)
				dsPK = ((DataSetDefVO) otherObj).getPk_datasetdef();
			flds = new AnaRepField[] { new AnaRepField((Field) obj, AnaRepField.TYPE_DETAIL_FIELD, dsPK) };

		} else if (obj instanceof AnaRepField) {
			if (refresh) {// 说明是数据态的移动,在removeSourceNode()中刷新
				refresh = false;
			}
			flds = new AnaRepField[] { (AnaRepField) obj };
		}

		if (isInRep && isSameCell(aimPos))// 表内且位置无变化时，不做事情
			return false;

		if (flds != null) {
			m_plugin.getReport().stopCellEditing();// 插入之前停止编辑
			boolean append = m_plugin.appendFields(aimPos, flds[0].getDSPK(), flds, addTitle);

			if (append && refresh) {
				m_plugin.refreshDataModel(true);
			}

			return append;
		} else {
			if (obj instanceof String) {

				m_plugin.getModel().getFormatModel().setCellValue(aimPos, obj);
				m_plugin.getModel().getFormatModel().getCell(aimPos).removeExtFmt(AnaRepField.EXKEY_FIELDINFO);
				return true;
			} else if (obj instanceof String[]) {
				String[] strs = (String[]) obj;
				for (int i = 0; i < strs.length; i++) {
					String ss = strs[i];
					CellPosition pos = CellPosition.getInstance(aimPos.getRow(), aimPos.getColumn() + i);
					m_plugin.getModel().getFormatModel().setCellValue(pos, ss);
					m_plugin.getModel().getFormatModel().getCell(pos).removeExtFmt(AnaRepField.EXKEY_FIELDINFO);
				}
				return true;
			}
		}
		return false;
	}

	private AnaReportModel getAnaModel() {
		return m_plugin.getModel();
	}

	private boolean isHasChart(IArea area) {
		AnaReportModel model = getAnaModel();
		ExAreaModel exAreaModel = ExAreaModel.getInstance(model.getFormatModel());
		ExAreaCell exAreaCell = exAreaModel.getExArea(area);
		if (exAreaCell == null)
			return false;
		if (exAreaCell.getExAreaType() == ExAreaCell.EX_TYPE_CHART)
			return true;
		return false;
	}

	private boolean isSameCell(CellPosition aimPos) {
		AnaReportModel model = getAnaModel();
		CellsModel cellsModel = model.getCellsModel();
		CellPosition anchorPos = model.getFormatCellPos(cellsModel, cellsModel.getSelectModel().getAnchorCell());
		if (anchorPos == null)
			return false;
		if (aimPos.equals(anchorPos))
			return true;

		return false;
	}

	private CellPosition getAimPos() {
		return aimPos;
	}

	private void setAimPos(CellPosition aimPos) {
		this.aimPos = aimPos;
	}

}
