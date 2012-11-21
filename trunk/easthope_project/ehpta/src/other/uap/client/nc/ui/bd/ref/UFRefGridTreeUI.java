package nc.ui.bd.ref;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.beans.table.NCTableModel;
import nc.vo.bd.ref.RefClassPropVO;
import nc.vo.bd.ref.RefcolumnVO;
import nc.vo.bd.ref.ReftableVO;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;

/**
 * 树表型参照UI类 创建日期：(2001-8-27 15:24:32)
 * 
 * @author：张扬
 */
public class UFRefGridTreeUI extends nc.ui.pub.beans.UIDialog implements
		javax.swing.event.TreeSelectionListener, IRefUINew2,
		java.awt.event.ActionListener, ValueChangedListener {

	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel uIDialogContentPane = null;

	private nc.ui.pub.beans.UISplitPane splPane_all = null;

	private RefTablePane tbP_data = null;

	private RefTablePane tbP_selectedData = null;

	private nc.ui.pub.beans.UITree ivjUITree1 = null;

	EventHandler eventHandler = new EventHandler();

	private nc.ui.pub.beans.UIPanel pnl_addDel = null;

	private nc.ui.pub.beans.UIPanel pnl_Data = null;

	private nc.ui.pub.beans.UIPanel pnl_SelectedTable = null;

	private nc.ui.pub.beans.UIPanel pnlTable_data = null;

	private nc.ui.pub.beans.UISplitPane splpane_Data = null;

	private nc.ui.pub.beans.UIScrollPane SclPane_tree = null;

	private RefButtonPanelFactory buttonPanelFactory = null;

	//
	private AbstractRefGridTreeModel m_refModel = null;

	private Vector m_vecAllColumnNames = null;

	private Vector m_vecShownColumnNames = null;

	private boolean m_bMultiSelectedEnabled = false;

	private boolean m_bNotLeafSelectedEnabled = false;

	private int[] m_iCodingRule = null;

	private boolean isMultiCorpRef = false;

	// sxj 2003-08-18
	private boolean isTreeGridMultiSelected = false;

	private Hashtable htselectedVector = new Hashtable();

	private boolean isRemoveCom = false;

	// sxj 2003-10-28
	// 动态列
	IDynamicColumn dynamicColClass;

	int sourceColNum = 0;

	//
	Hashtable pkToNode = new Hashtable();

	Hashtable htPkToRow = new Hashtable();

	private RefcolumnVO[] columnVOs = null;

	private Hashtable htLocate = new Hashtable();

	private RefUIConfig refUIConfig = null;

	private Vector nullVec = new Vector();

	/**
	 * resolution 1024*768 refDialog default size
	 */
	int width = 658;

	int height = 390;

	private UIPanel classPropPanel = null;

	private UILabel classPropNameLb = null;

	private UIComboBox classPropCbb = null;

	class EventHandler implements java.awt.event.ActionListener,
			java.awt.event.ItemListener, java.awt.event.KeyListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == getUIButtonRefresh())
				onRefresh();
			if (e.getSource() == getUIButtonColumn())
				onColumn();
			if (e.getSource() == getUIButtonOK())
				onOK();
			if (e.getSource() == getUIButtonExit())
				uIButtonExit_ActionPerformed();
			if (e.getSource() == getChkSealedDataShow()) {

				onChkSealedDataButton();

			}
		};

		public void itemStateChanged(java.awt.event.ItemEvent e) {
			if (e.getSource() == getUIComboBoxColumn())
				uIComboBoxColumn_ItemStateChanged(e);
		};

		public void keyPressed(java.awt.event.KeyEvent e) {
			if (e.getKeyChar() == java.awt.event.KeyEvent.VK_ESCAPE) {
				String str = getTFLocate().getText();
				if (str == null || str.trim().length() == 0) {
					uIButtonExit_ActionPerformed();
				}
			}
		};

		public void keyReleased(java.awt.event.KeyEvent e) {
			if (e.getSource() == getTFLocate())
				uITextFieldLocate_KeyReleased(e);
		};

		public void keyTyped(java.awt.event.KeyEvent e) {
		};
	};

	MouseAdapter mouseAdapter = new MouseAdapter() {
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (e.getSource() == getUITablePane1().getTable()
					&& e.getClickCount() == 2)
				processMouseDoubleClicked(e);
		}
	};

	/**
	 * UFRefTreeGridUI 构造子注解。
	 */
	public UFRefGridTreeUI() {
		super();
		initialize();
	}

	/**
	 * UFRefTreeGridUI 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public UFRefGridTreeUI(java.awt.Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * UFRefTreeGridUI 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public UFRefGridTreeUI(java.awt.Container parent, AbstractRefModel refModel) {
		super(parent, refModel.getRefTitle());
		setRefModel(refModel);
		initialize();
	}

	/**
	 * UFRefTreeGridUI 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public UFRefGridTreeUI(java.awt.Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * UFRefTreeGridUI 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Frame
	 * @param title
	 *            java.lang.String
	 */
	public UFRefGridTreeUI(java.awt.Frame owner, String title) {
		super(owner, title);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource() == getUIButAdd()) {
			onAdd();

		} else if (e.getSource() == getUIButDel()) {
			onDel();
		} else if (e.getSource() == getBtnQuery()) {
			onQuery();
		} else if (e.getSource() == getUIbtnLocQuery()) {
			onLocQuery();
		} else if (e.getSource() == getBtnAddDoc()) {
			onAddDoc();
		} else if (e.getSource() == getTFLocate()) {
			// 解决汉字定位问题
			// blurInputValue("", 0);
			blurInputValue(getTFLocate().getText(), getLocateColumn());
		} else if (e.getSource() == getClassPropCbb()) {
			getRefModel().setClassProp(
					(RefClassPropVO) getClassPropCbb().getSelectedItem());
			getRefModel().reloadData();
			showModal();
		}
	}

	private void actionUITree(
			javax.swing.event.TreeSelectionEvent treeSelectionEvent) {
		if (treeSelectionEvent.getNewLeadSelectionPath() == null)
			return;
		TreePath treePath = treeSelectionEvent.getPath();
		ExTreeNode m_tnSelected = (ExTreeNode) treePath.getLastPathComponent();
		Object o = m_tnSelected.getUserObject();
		if (!(o instanceof Vector)) {
			// 置空数据
			// ((NCTableModel) getUITablePane1().getTable().getModel())
			// .setDataVector(nullVec);
			getUITablePane1().setDataVector(nullVec);
			getUITablePane1().getTable().clearSelection();
			m_refModel.setClassJoinValue(null);
			return;
		}
		// 当前节点级次---根为0
		int iLevel = m_tnSelected.getLevel();
		Vector vDataAll = null;
		// 展开档案的条件--1.末级 2.允许非末级且在展开级次内
		if ((isNotLeafSelectedEnabled() && iLevel >= m_refModel
				.getExpandLevel())
				|| m_tnSelected.isLeaf()) {
			String strJoinValue = getClassJoinValue(m_tnSelected);
			m_refModel.setClassJoinValue(strJoinValue);
			vDataAll = m_refModel.getRefData();
		} else {
			m_refModel.setClassJoinValue(null);
		}
		if (vDataAll == null)
			vDataAll = new Vector();
		getUITablePane1().setDataVector(vDataAll);
		// ((NCTableModel) getUITablePane1().getTable().getModel())
		// .setDataVector(vDataAll);
		// 根据BlurValue定位

		if (getUITablePane1().getTable().getModel().getRowCount() > 0
				&& getRefModel().getBlurValue() != null
				&& getRefModel().getBlurValue().trim().length() > 0
				&& getRefModel().getBlurValue().indexOf("*") == -1
				&& getRefModel().getBlurValue().indexOf("%") == -1
				&& getRefModel().getBlurValue().indexOf("?") == -1) {
			int col = -1;
			int iGridIndex = -1;
			col = getRefModel().getFieldIndex(getRefModel().getBlurFields()[0]);
			if (col >= 0)
				iGridIndex = findMatchRow(getRefModel().getBlurValue(), col);
			if (iGridIndex >= 0) {
				getUITablePane1().getTable().setRowSelectionInterval(
						iGridIndex, iGridIndex);
				getUITablePane1().getTable().scrollRectToVisible(
						getUITablePane1().getTable().getCellRect(iGridIndex,
								col, false));
			} else {
				getUITablePane1().getTable().clearSelection();
			}
		}
		getUITablePane1().getTable().clearSelection();
		// 清空模糊匹配的数据
		htLocate.clear();
	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-11-9</strong>
	 * <p>
	 * 
	 * @param
	 * @return String
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private String getClassJoinValue(ExTreeNode selectedNode) {
		String strJoinValue = null;
		if (selectedNode == null) {
			return null;
		}
		if (selectedNode.getUserObject() instanceof Vector) {

			Vector vSelected = (Vector) selectedNode.getUserObject();

			if (vSelected != null) {
				int col = m_refModel.getClassFieldIndex(m_refModel
						.getClassJoinField());
				if (col >= 0 && col < vSelected.size())
					strJoinValue = (String) vSelected.elementAt(col);
			}
		}

		return strJoinValue;
	}

	/**
	 * 把一条记录添加到被选择的表中
	 */
	private void addRowToTable() {
		int row = getUITablePane1().getTable().getSelectedRow();
		int pkIndex = getPKIndex();
		//
		Vector vDataTable = new Vector();
		if (row >= 0) {
			Vector vRecord = new Vector();
			String key = null;
			for (int j = 0; j < sourceColNum; j++) {
				Object value = getUITablePane1().getTable().getModel()
						.getValueAt(row, j);
				if (pkIndex == j && value != null) {
					// 被选择的表中已经有了该记录
					if (htselectedVector.get(value.toString()) != null) {
						vRecord = null;
						break;
					}
					key = value.toString();

				}
				vRecord.addElement(value);
			}
			if (vRecord != null) {
				vDataTable.addElement(vRecord);
				// 得到动态列数据；可能会慢
				if (getRefModel().isDynamicCol()) {

					AbstractRefModel refModel = new ParaRefModel();
					refModel.setFieldCode(getRefModel().getFieldCode());
					refModel.setFieldName(getRefModel().getFieldName());
					refModel.setHiddenFieldCode(getRefModel().getFieldName());
					refModel.setPkFieldCode(getRefModel().getPkFieldCode());
					refModel.setHiddenFieldCode(getRefModel()
							.getHiddenFieldCode());
					refModel.setData(vDataTable);
					refModel.setSelectedData(vDataTable);
					refModel.setIsDynamicCol(true);
					refModel.setDynamicFieldNames(getRefModel()
							.getDynamicFieldNames());
					// refModel.setHtCodeIndex(getRefModel().getHtCodeIndex());
					refModel = dynamicColClass.getDynamicInfo(getRefModel()
							.getUserParameter(), refModel);
					if (refModel == null) {

					}
				}
				//
				// htselectedVector.put(key, vRecord);
			}

		}

		addRecordToSelectedTable(vDataTable);
		return;
	}

	/**
	 * 此处插入方法说明。 创建日期：(02-7-4 18:46:38) sxj 2003-12-24 修改 解决效率问题
	 */
	private void blurInputValue(String strInput, int iSelectedIndex) {
		Vector matchVec = new Vector();
		if (strInput.equals("") && iSelectedIndex == 0) {

			htLocate.clear();
		}
		String classJoinValue = getClassJoinValue(getSelectedNode());

		// 根节点,不提供模糊查询功能。对于查询
		if (getClassJoinValue(getSelectedNode()) == null) {
			return;
		}

		if ((strInput == null || strInput.trim().length() == 0)
				&& classJoinValue != null) {

			// 原来的数据
			matchVec = getRefModel().getRefData();
			if (matchVec != null) {
				resetTableData(getUITablePane1().getTable(), matchVec, false);
			}
		}

		if (strInput != null && !strInput.equals("")) {
			// 从缓存取
			if (htLocate.get(strInput) != null) {
				matchVec = (Vector) htLocate.get(strInput);
			} else {
				// 比较
				for (int i = 0; i < getUITablePane1().getTable().getModel()
						.getRowCount(); i++) {
					for (int j = 0; j < getUITablePane1().getTable()
							.getColumnCount(); j++) {
						Object o = getUITablePane1().getTable()
								.getValueAt(i, j);
						// getModel().getValueAt(i, j);
						String strCell = (o == null ? "" : o.toString().trim());
						NCTableModel tm = ((NCTableModel) getUITablePane1()
								.getTable().getModel());
						if (RefPubUtil.toLowerCaseStr(getRefModel(), strCell)
								.indexOf(
										RefPubUtil.toLowerCaseStr(
												getRefModel(), strInput)) >= 0) {
							Vector v = (Vector) tm.getDataVector().elementAt(i);
							matchVec.add(v);
							break;
						}
					}

				}
				htLocate.put(strInput, matchVec);
			}

			if (matchVec != null) {
				resetTableData(getUITablePane1().getTable(), matchVec, false);
			}
		}

		return;
	}

	/**
	 * 表model中查找定位
	 * 
	 * @author 张扬
	 */
	private int findMatchRow(String strInput, int iSelectedIndex) {
		int iGridIndex = -1;
		if (strInput != null
				&& !strInput.equals("")
				&& getUITablePane1().getTable().getModel().getRowCount() > 0
				&& iSelectedIndex >= 0
				&& iSelectedIndex < getUITablePane1().getTable().getModel()
						.getColumnCount()) {
			for (int i = 0; i < getUITablePane1().getTable().getModel()
					.getRowCount(); i++) {
				Object o = getUITablePane1().getTable().getModel().getValueAt(
						i, iSelectedIndex);
				if (o == null)
					continue;
				String strCell = o.toString().trim();
				if (strInput.length() > strCell.length())
					continue;
				if (strCell.startsWith(strInput)) {
					iGridIndex = i;
					break;
				}
			}
		}
		return iGridIndex;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-25 18:29:08)
	 * 
	 * @return java.util.Vector
	 */
	private Vector getAllColumnNames() {

		if (m_vecAllColumnNames == null || m_vecAllColumnNames.size() == 0) {
			m_vecAllColumnNames = new Vector();
			if (getRefModel().getFieldCode() != null
					&& getRefModel().getFieldCode().length > 0) {
				if (getRefModel().getFieldName() == null
						|| getRefModel().getFieldName().length == 0) {
					for (int i = 0; i < getRefModel().getFieldCode().length; i++) {
						m_vecAllColumnNames.addElement(getRefModel()
								.getFieldCode()[i]);
					}
				} else {
					if (getRefModel().getFieldName().length >= getRefModel()
							.getFieldCode().length) {
						for (int i = 0; i < getRefModel().getFieldCode().length; i++) {
							m_vecAllColumnNames.addElement(getRefModel()
									.getFieldName()[i]);
						}
					} else {
						for (int i = 0; i < getRefModel().getFieldName().length; i++) {
							m_vecAllColumnNames.addElement(getRefModel()
									.getFieldName()[i]);
						}
						for (int i = getRefModel().getFieldName().length; i < getRefModel()
								.getFieldCode().length; i++) {
							m_vecAllColumnNames.addElement(getRefModel()
									.getFieldCode()[i]);
						}
					}
				}

			}

			// 加入隐藏列
			if (getRefModel().getHiddenFieldCode() != null) {
				for (int i = 0; i < getRefModel().getHiddenFieldCode().length; i++) {
					m_vecAllColumnNames.addElement(getRefModel()
							.getHiddenFieldCode()[i]);
				}
			}
			sourceColNum = m_vecAllColumnNames.size();

			// 加入动态列
			if (getRefModel().isDynamicCol()) {
				// String[][] dynamicColName =
				// dynamicColClass.getDynaminColNameAndLoc();
				String[] dynamicColNames = getRefModel().getDynamicFieldNames();
				if (getRefModel().getDynamicFieldNames() != null) {

					for (int i = 0; i < dynamicColNames.length; i++) {

						// 加入到显示的列名中
						m_vecAllColumnNames.addElement(dynamicColNames[i]);
					}
				}
			}
		}
		return m_vecAllColumnNames;
	}

	/**
	 * 
	 */
	private int getPKIndex() {

		return getRefModel().getFieldIndex(getRefModel().getPkFieldCode());
	}

	/**
	 * 返回参照模型。 创建日期：(2001-8-24 8:37:33)
	 * 
	 * @return nc.ui.bd.ref.AbstractRefGridTreeModel
	 */
	public AbstractRefGridTreeModel getRefModel() {
		return m_refModel;
	}

	/**
	 * Comment
	 */
	private Vector getSelectedData() {
		int[] selectedRows = null;
		int rowCount = -1;
		UITable table = null;

		if (isTreeGridMultiSelected) {/* 多分类模式选择 */

			table = getTbP_selectedData().getTable();
			rowCount = table.getRowCount();

			if (rowCount > 0) {
				selectedRows = new int[rowCount];
				for (int i = 0; i < selectedRows.length; i++) {
					selectedRows[i] = i;
				}
			}

		} else {/* 单分类模式选择 */
			table = getUITablePane1().getTable();
			selectedRows = table.getSelectedRows();

		}
		return getSelectedVector(table, selectedRows);

	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-8-3</strong>
	 * <p>
	 * 
	 * @param
	 * @return Vector
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private Vector getSelectedVector(UITable table, int[] selectedRows) {
		Vector vSelectedData = null;

		if (selectedRows != null && selectedRows.length > 0) {
			vSelectedData = new Vector();
			for (int i = 0; i < selectedRows.length; i++) {
				Vector vRecord = new Vector();
				for (int j = 0; j < table.getModel().getColumnCount(); j++) {
					vRecord.addElement(table.getModel().getValueAt(
							selectedRows[i], j));
				}
				vSelectedData.addElement(vRecord);
			}
		}
		return vSelectedData;
	}

	// 响应自定义键盘处理的Table
	// 支持Tab键转移焦点，Enter键选中
	// sxj 2003-02-26

	private nc.ui.pub.beans.UITable getSelfTable() {

		nc.ui.pub.beans.UITable table = new nc.ui.pub.beans.UITable() {

			protected void processKeyEvent(java.awt.event.KeyEvent keyEvent) {

				if (keyEvent.getID() == KeyEvent.KEY_PRESSED
						&& keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) { // 回车
					keyEvent.consume();
					java.awt.Component parent = keyEvent.getComponent();
					while (parent.getParent() != null) {
						if (parent instanceof javax.swing.JTable
								|| parent instanceof javax.swing.JViewport) {
							parent = parent.getParent();
						} else
							break;
					}
					if (isTreeGridMultiSelected) {
						onAdd();
					} else {
						onOK();
					}
					return;
				} else if (keyEvent.getID() == KeyEvent.KEY_PRESSED
						&& keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) { // Escape
					keyEvent.consume();
					uIButtonExit_ActionPerformed();
					return;
				}
				if (getCellEditor() instanceof DefaultCellEditor) {
					DefaultCellEditor dce = (DefaultCellEditor) getCellEditor();
					if (dce.getComponent() instanceof nc.ui.pub.beans.table.UIVarLenTextField) {
						nc.ui.pub.beans.table.UIVarLenTextField uvtf = (nc.ui.pub.beans.table.UIVarLenTextField) dce
								.getComponent();
						uvtf.adjustLength();
					}
				}
				super.processKeyEvent(keyEvent);
			}
		};
		return table;

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-25 18:29:08)
	 * 
	 * @return java.util.Vector
	 */
	private Vector getShownColumnNames() {

		if (m_vecShownColumnNames == null || m_vecShownColumnNames.size() == 0) {
			m_vecShownColumnNames = new Vector();
			if (getRefModel().getFieldCode() != null
					&& getRefModel().getFieldCode().length > 0) {
				if (getRefModel().getFieldName() == null
						|| getRefModel().getFieldName().length == 0) {
					for (int i = 0; i < getRefModel().getFieldCode().length; i++) {
						m_vecShownColumnNames.addElement(getRefModel()
								.getFieldCode()[i]);
					}
				} else {
					if (getRefModel().getFieldName().length >= getRefModel()
							.getFieldCode().length) {
						for (int i = 0; i < getRefModel().getFieldCode().length; i++) {
							m_vecShownColumnNames.addElement(getRefModel()
									.getFieldName()[i]);
						}
					} else {
						for (int i = 0; i < getRefModel().getFieldName().length; i++) {
							m_vecShownColumnNames.addElement(getRefModel()
									.getFieldName()[i]);
						}
						for (int i = getRefModel().getFieldName().length; i < getRefModel()
								.getFieldCode().length; i++) {
							m_vecShownColumnNames.addElement(getRefModel()
									.getFieldCode()[i]);
						}
					}
				}

			}
		}
		return m_vecShownColumnNames;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-27 19:31:43)
	 * 
	 * @return int[]
	 */
	private int[] getTreeColumn() {
		int[] cols = null;
		if (m_refModel != null && m_refModel.getClassDefaultFieldCount() > 0) {
			cols = new int[m_refModel.getClassDefaultFieldCount()];
			for (int i = 0; i < m_refModel.getClassDefaultFieldCount(); i++) {
				cols[i] = i;
			}
		}
		return cols;
	}

	/**
	 * 返回 UIButAdd 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getUIButAdd() {
		return getButtonPanelFactory().getBtnAdd();
	}

	/**
	 * 返回 UIButDel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getUIButDel() {

		return getButtonPanelFactory().getBtnRemove();
	}

	/**
	 * 返回 UIButtonColumn 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getUIButtonColumn() {

		return getButtonPanelFactory().getBtnColumn();
	}

	/**
	 * 返回 UIButtonExit 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getUIButtonExit() {

		return getButtonPanelFactory().getBtnExit();

	}

	/**
	 * 返回 UIButtonOK 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getUIButtonOK() {

		return getButtonPanelFactory().getBtnOK();
	}

	/**
	 * 返回 UIButtonRefresh 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getUIButtonRefresh() {

		return getButtonPanelFactory().getBtnRefresh();
	}

	/**
	 * 返回 UIComboBox1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIComboBox
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIComboBox getUIComboBoxColumn() {

		return getButtonPanelFactory().getCbbColumn();
	}

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (uIDialogContentPane == null) {

			uIDialogContentPane = new UIPanel();
			uIDialogContentPane.setName("UIDialogContentPane");
			uIDialogContentPane.setLayout(new java.awt.BorderLayout());

			uIDialogContentPane.add(getSplPane_all(), "Center");
			uIDialogContentPane.add(getPnl_CorpRef(), "North");

		}
		return uIDialogContentPane;
	}

	/**
	 * 返回 UIPanel5 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnl_addDel() {
		if (pnl_addDel == null) {

			pnl_addDel = new nc.ui.pub.beans.UIPanel();
			pnl_addDel.setName("pnl_addDel");
			pnl_addDel.setPreferredSize(new java.awt.Dimension(0, 28));
			pnl_addDel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
			pnl_addDel.add(getUIButAdd(), getUIButAdd().getName());
			pnl_addDel.add(getUIButDel(), getUIButDel().getName());

		}
		return pnl_addDel;
	}

	/**
	 * 返回 UIPanel2 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnlData() {
		if (pnl_Data == null) {

			pnl_Data = new nc.ui.pub.beans.UIPanel();
			pnl_Data.setName("pnl_Data");
			pnl_Data.setLayout(new java.awt.BorderLayout());

			pnl_Data.add(getPnl_locate_btn(), "North");
			pnl_Data.add(getSplpane_Data(), "Center");
			pnl_Data.add(getPnl_south(), "South");
			// pnl_Data.setPreferredSize(new Dimension(width*76/100,0));

		}
		return pnl_Data;
	}

	/**
	 * 返回 UIPnlRef 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnl_CorpRef() {

		return getButtonPanelFactory().getPnl_refCorp();
	}

	/**
	 * 返回 UIPnlSelectTable 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnlSelectedTable() {

		if (pnl_SelectedTable == null) {

			pnl_SelectedTable = new nc.ui.pub.beans.UIPanel();
			pnl_SelectedTable.setName("pnl_SelectedTable");
			// pnl_SelectedTable.setPreferredSize(new
			// java.awt.Dimension(width*76/100, (heigth-90)/2));
			pnl_SelectedTable.setLayout(new java.awt.BorderLayout());
			pnl_SelectedTable.add(getPnl_addDel(), "North");
			pnl_SelectedTable.add(getTbP_selectedData(), "Center");

		}
		return pnl_SelectedTable;
	}

	/**
	 * 返回 UIPnlTblAndLocate 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnl_TableData() {
		if (pnlTable_data == null) {

			pnlTable_data = new nc.ui.pub.beans.UIPanel();
			pnlTable_data.setName("pnlTable_data");
			pnlTable_data.setPreferredSize(new java.awt.Dimension(0,
					(height - 90) / 2));
			pnlTable_data.setLayout(new java.awt.BorderLayout());
			pnlTable_data.add(getUITablePane1(), "Center");

		}
		return pnlTable_data;
	}

	/**
	 * 返回 UIRefCorp 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	public nc.ui.pub.beans.UIRefPane getRefCorp() {

		return getButtonPanelFactory().getRefCorp();
	}

	/**
	 * 返回 UIScrollPane1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getSclPane() {
		if (SclPane_tree == null) {

			SclPane_tree = new nc.ui.pub.beans.UIScrollPane();
			SclPane_tree.setName("UIScrollPane1");
			SclPane_tree.setPreferredSize(new java.awt.Dimension(
					width * 24 / 100, 0));
			SclPane_tree.setViewportView(getUITree1());
			SclPane_tree.setBorder(null);

		}
		return SclPane_tree;
	}

	private UIPanel getClassPropPanel() {
		if (classPropPanel == null) {
			classPropPanel = new UIPanel();
			classPropPanel.setName("classPropPanel");
			setPreferredSize(new Dimension(30, 200));
			classPropPanel.setLayout(new FlowLayout());
			classPropPanel.add(getClassPropNameLb());
			classPropPanel.add(getClassPropCbb());

		}
		return classPropPanel;
	}

	private UILabel getClassPropNameLb() {

		if (classPropNameLb == null) {
			classPropNameLb = new UILabel("分类属性");
		}
		return classPropNameLb;

	}

	private UIComboBox getClassPropCbb() {

		if (classPropCbb == null) {
			classPropCbb = new UIComboBox();
			classPropCbb.addItems(getRefClassPropVOs());
			classPropCbb.addActionListener(this);
		}
		return classPropCbb;

	}

	private RefClassPropVO[] getRefClassPropVOs() {
		return getRefModel().getClassPropVOs();
	}

	/**
	 * 返回 UISplitPane1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UISplitPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UISplitPane getSplPane_all() {
		if (splPane_all == null) {

			splPane_all = new nc.ui.pub.beans.UISplitPane(1);
			splPane_all.setName("UISplitPane1");
			splPane_all.setPreferredSize(new java.awt.Dimension(width, 0));
			splPane_all.setDividerLocation(width * 24 / 100);
			splPane_all.add(getSclPane(), "left");
			// 调整加载顺序
			splPane_all.add(getPnlData(), "right");

		}
		return splPane_all;
	}

	/**
	 * 返回 UISplpane2 特性值。
	 * 
	 * @return nc.ui.pub.beans.UISplitPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UISplitPane getSplpane_Data() {
		if (splpane_Data == null) {

			splpane_Data = new nc.ui.pub.beans.UISplitPane(
					javax.swing.JSplitPane.VERTICAL_SPLIT);
			splpane_Data.setName("splpane_Data");
			splpane_Data.setPreferredSize(new java.awt.Dimension(
					width * 76 / 100, height - 90));
			splpane_Data.setDividerLocation((height - 90) / 2);
			splpane_Data.add(getPnl_TableData(), "top");
			splpane_Data.add(getPnlSelectedTable(), "bottom");
			((javax.swing.plaf.basic.BasicSplitPaneUI) splpane_Data.getUI())
					.getDivider().setBorder(null);
			splpane_Data.setBorder(null);

		}
		return splpane_Data;
	}

	/**
	 * 返回 UITablePane1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITablePane
	 */
	/* 警告：此方法将重新生成。 */
	public RefTablePane getUITablePane1() {
		if (tbP_data == null) {
			tbP_data = new RefTablePane();

		}
		return tbP_data;
	}

	/**
	 * 返回 UITablePane1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITablePane
	 */
	/* 警告：此方法将重新生成。 */
	protected RefTablePane getTbP_selectedData() {
		if (tbP_selectedData == null) {
			tbP_selectedData = new RefTablePane();
			tbP_selectedData.setBorder(null);
		}
		return tbP_selectedData;
	}

	/**
	 * 返回 UITextField1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* 警告：此方法将重新生成。 */
	public nc.ui.pub.beans.UITextField getTFLocate() {

		return getButtonPanelFactory().getTfLocate();
	}

	/**
	 * 返回 UITree1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITree
	 */
	/* 警告：此方法将重新生成。 */
	public nc.ui.pub.beans.UITree getUITree1() {
		if (ivjUITree1 == null) {
			try {
				ivjUITree1 = new nc.ui.pub.beans.UITree() {
					protected void processKeyEvent(
							java.awt.event.KeyEvent keyEvent) {
						// 处理Enter动作
						// sxj 2003-02-26
						if (keyEvent.getID() == KeyEvent.KEY_PRESSED
								&& (keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)) {
							keyEvent.consume();
							java.awt.Component parent = keyEvent.getComponent();
							while (parent.getParent() != null) {
								if (parent instanceof javax.swing.JTree
										|| parent instanceof javax.swing.JViewport) {
									parent = parent.getParent();
								} else
									break;
							}
							getUITablePane1().getTable().requestFocus();
							// 默认定位到第一行
							if (getUITablePane1().getTable().getModel()
									.getRowCount() > 0) {
								getUITablePane1().getTable()
										.setRowSelectionInterval(0, 0);
							}
							return;
						} else if (keyEvent.getID() == KeyEvent.KEY_PRESSED
								&& keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) { // Escape，退出参照
							keyEvent.consume();
							uIButtonExit_ActionPerformed();
							return;
						}
						super.processKeyEvent(keyEvent);
					}

				};
				ivjUITree1.setName("UITree1");
				// ivjUITree1.setPreferredSize(new
				// java.awt.Dimension(width*24/100, 0));
				// user code begin {1}
				getUITree1().addTreeSelectionListener(this);
				getUITree1().putClientProperty("JTree.lineStyle", "Angled");
				getUITree1().setCellRenderer(new RefTreeCellRenderer(null));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUITree1;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		// System.out.println("--------- 未捕捉到的异常 ---------");
		// exception.printStackTrace(System.out);
	}

	/**
	 * 初始化连接
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	/* 警告：此方法将重新生成。 */
	private void initConnections() {

		removeKeyListener(this);
		addKeyListener(this);
		getUIButtonRefresh().addActionListener(eventHandler);
		getUIButtonColumn().addActionListener(eventHandler);
		getUIButtonOK().addActionListener(eventHandler);
		getUIButtonExit().addActionListener(eventHandler);
		getTFLocate().addKeyListener(eventHandler);
		getTFLocate().addKeyListener(eventHandler);
		getChkSealedDataShow().addActionListener(eventHandler);
		getClassPropCbb().addActionListener(this);
	}

	/**
	 * 
	 */
	private IDynamicColumn initDynamicColClass() {

		String className = getRefModel().getDynamicColClassName();
		// 是否实现接口检查
		IDynamicColumn newDynamicClass = null;
		try {
			newDynamicClass = (IDynamicColumn) Class.forName(className)
					.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

		return newDynamicClass;
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {

		setName("UFRefGridTreeUI");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		setDefaultSize();

		setResizable(true);

		// setSize(790, 460);

		setContentPane(getUIDialogContentPane());

		// sxj 2003-02-26
		getUITablePane1().setTable(getSelfTable());

		getUITablePane1().getTable().addMouseListener(mouseAdapter);

		// 公司参照变化事件
		getRefCorp().addValueChangedListener(this);
		getBtnQuery().addActionListener(this);
		getUIbtnLocQuery().addActionListener(this);
		getBtnAddDoc().addActionListener(this);
		getTFLocate().addActionListener(this);

	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-8-24</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void setDefaultSize() {
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

		width = size.width * 64 / 100;
		height = size.height * 51 / 100;

		setSize(width, height);
	}

	/**
	 * 
	 */
	private void initTableModel(UITable table) {

		Vector vDataAll = new Vector();
		nc.ui.pub.beans.table.NCTableModel tm = new RefNCDataTableModle(
				vDataAll, getAllColumnNames());
		tm.setAndEditable(true);
		table.setModel(tm);
		table.setColumnModel(new RefTableColumnModel(getRefModel()));
		table.createDefaultColumnsFromModel();

	}

	private void initTable() {

		UITable table = getUITablePane1().getTable();
		initTableModel(table);
		table.addSortListener();
		setNewColumnSequence(table);
		getUITablePane1().initRowHeader();
		if (isTreeGridMultiSelected) {
			table = getTbP_selectedData().getTable();
			initTableModel(table);
			RefTableColumnModel cm = (RefTableColumnModel) table
					.getColumnModel();
			cm.setDynamicColumnNames(getRefModel().getDynamicFieldNames());
			table.addSortListener();
			setNewColumnSequence(table);
			getTbP_selectedData().initRowHeader();
		}

	}

	/**
	 * 按层次关系插入一个节点,即使无父节点也插入。也可循环插入整个树节点 sxj
	 */
	private void insertTreeNode(ExTreeNode root, ExTreeNode newNode,
			String sBm, HashMap hmCode, HashMap hmTreeNode) {

		int level = 0;
		int iBmLen = sBm.length(); // 得到编码长度
		int len = 0;
		int[] levelLen = new int[m_iCodingRule.length];
		// 得到本级层次
		for (int i = 0; i < m_iCodingRule.length; i++) {
			level++;
			len += m_iCodingRule[i];
			levelLen[i] = len;// 累计各级的长度
			if (iBmLen == len) {
				break;
			}
		}
		// 得到上级节点级次
		String fatherCode = null;
		ExTreeNode fatherNode = null;
		if (level == 1) {
			fatherNode = root;

		} else {
			for (int i = level; i >= 2; i--) {
				fatherCode = sBm.substring(0, levelLen[i - 2]);
				if (hmCode.get(fatherCode) == null) {
					continue;
				}
				fatherNode = (ExTreeNode) hmTreeNode.get(fatherCode);
				break;

			}
			if (fatherNode == null) {
				fatherNode = root;
			}

		}
		// 插入父节点
		fatherNode.insert(newNode, fatherNode.getChildCount());
		hmTreeNode.put(sBm, newNode);

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-25 18:38:24)
	 * 
	 * @return boolean
	 */
	private boolean isMultiSelectedEnabled() {
		return m_bMultiSelectedEnabled;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-26 9:46:41)
	 * 
	 * @return boolean
	 */
	private boolean isNotLeafSelectedEnabled() {
		return m_bNotLeafSelectedEnabled;
	}

	public void keyPressed(java.awt.event.KeyEvent e) {
		int keyCode = e.getKeyCode();
		int modifiers = e.getModifiers();
		if (modifiers < 2
				&& (getTFLocate().hasFocus() || getUIComboBoxColumn()
						.hasFocus()) || keyCode == 16 || keyCode == 17
				|| keyCode == 18)
			return;
		int index = -1;
		if (modifiers < 2) {
			if (keyCode == KeyEvent.VK_ENTER) {
				// 回车键,空格键
				if (getUIButtonRefresh().hasFocus())
					index = 0;
				else if (getUIButtonColumn().hasFocus())
					index = 1;
				else if (getUIButtonOK().hasFocus())
					index = 2;
				else if (getUIButtonExit().hasFocus())
					index = 3;
			} else if (keyCode == KeyEvent.VK_ESCAPE) {
				index = 3;
			}
		} else if ((modifiers & KeyEvent.ALT_MASK) != 0) {
			switch (keyCode) {
			case java.awt.event.KeyEvent.VK_R:
				index = 0;
				break;
			case java.awt.event.KeyEvent.VK_C:
				index = 1;
				break;
			case java.awt.event.KeyEvent.VK_O:
				index = 2;
				break;
			case java.awt.event.KeyEvent.VK_X:
				index = 3;
				break;
			case java.awt.event.KeyEvent.VK_A:
				index = 4;
				break;
			case java.awt.event.KeyEvent.VK_D:
				index = 5;
				break;
			case java.awt.event.KeyEvent.VK_M:
				index = 6;
				break;
			case java.awt.event.KeyEvent.VK_L:
				index = 7;
				break;
			}
		}
		switch (index) {
		case 0: // R
			e.consume();
			onRefresh();
			return;
		case 1: // C
			e.consume();
			onColumn();
			return;
		case 2: // O
			e.consume();
			onOK();
			return;
		case 3: // X
			e.consume();
			uIButtonExit_ActionPerformed();
			return;
		case 4: // A

			if (isTreeGridMultiSelected) {
				e.consume();
				onAdd();
			}
			return;
		case 5: // D
			if (isTreeGridMultiSelected) {
				e.consume();
				onDel();
			}
			return;
		case 6: // D
			e.consume();
			onAddDoc();
			return;

		case 7: // D
			e.consume();
			onLocQuery();
			return;
		}
		return;
	}

	/**
	 * Comment
	 */
	private void onAdd() {

		int[] selectedRows = getUITablePane1().getTable().getSelectedRows();
		Vector vDataTable = getSelectedVector(getUITablePane1().getTable(),
				selectedRows);

		// 处理动态列数据
		getDynamicColData(vDataTable);

		// 添加到选择表中
		addRecordToSelectedTable(vDataTable);
		getTbP_selectedData().getTable().requestFocus();
		return;
	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-8-3</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void getDynamicColData(Vector vDataTable) {
		// 得到动态列数据；可能会慢
		if (getRefModel().isDynamicCol()) {

			AbstractRefModel refModel = new ParaRefModel();
			refModel.setFieldCode(getRefModel().getFieldCode());
			refModel.setFieldName(getRefModel().getFieldName());
			refModel.setHiddenFieldCode(getRefModel().getFieldName());
			refModel.setPkFieldCode(getRefModel().getPkFieldCode());
			refModel.setHiddenFieldCode(getRefModel().getHiddenFieldCode());
			refModel.setData(vDataTable);
			refModel.setSelectedData(vDataTable);
			refModel.setIsDynamicCol(true);
			refModel.setDynamicFieldNames(getRefModel().getDynamicFieldNames());
			// refModel.setHtCodeIndex(getRefModel().getHtCodeIndex());
			refModel = dynamicColClass.getDynamicInfo(getRefModel()
					.getUserParameter(), refModel);

		}
	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-7-10</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void addRecordToSelectedTable(Vector vDataTable) {
		int pkIndex = getPKIndex();
		if (vDataTable.size() > 0) {

			Vector record = null;
			for (int i = 0; i < vDataTable.size(); i++) {

				record = (Vector) vDataTable.get(i);
				if (htselectedVector.get(record.get(pkIndex).toString()) == null) {

					getTbP_selectedData().addRow(record);
					htselectedVector
							.put(record.get(pkIndex).toString(), record);

				}
			}

		}
	}

	/**
	 * Comment
	 */
	private void onDel() {
		int pkIndex = getPKIndex();
		int[] selectedRows = getTbP_selectedData().getTable().getSelectedRows();
		if (selectedRows != null && selectedRows.length > 0) {

			for (int i = selectedRows.length - 1; i >= 0; i--) {

				Object value = getTbP_selectedData().getTable().getModel()
						.getValueAt(selectedRows[i], pkIndex);
				if (value != null) {
					// 被选择的表中已经有了该记录
					htselectedVector.remove(value.toString());
				}

				getTbP_selectedData().removeRow(selectedRows[i]);

			}

		}
		getTbP_selectedData().getTable().requestFocus();
		return;
	}

	/**
	 * 表格行双击事件响应
	 */
	private void processMouseDoubleClicked(java.awt.event.MouseEvent mouseEvent) {
		if (isTreeGridMultiSelected) {
			// 添加当前行到被选择表中
			if (mouseEvent.getSource() == getUITablePane1().getTable()) {
				addRowToTable();
			}
		} else {
			// 选中，返回
			if (getUITablePane1().getTable().getSelectedRow() != -1) {
				// 处理参照返回值
				// 添数据至RefModel
				Vector vSelectedData = null;
				int[] selectedRows = getUITablePane1().getTable()
						.getSelectedRows();
				if (selectedRows != null && selectedRows.length > 0) {
					vSelectedData = new Vector();
					for (int i = 0; i < selectedRows.length; i++) {
						Vector vRecord = new Vector();
						for (int j = 0; j < getUITablePane1().getTable()
								.getModel().getColumnCount(); j++) {
							vRecord.addElement(getUITablePane1().getTable()
									.getModel().getValueAt(selectedRows[i], j));
						}
						vSelectedData.addElement((Vector) vRecord);
					}
				}
				getRefModel().setSelectedData(vSelectedData);
				closeOK();
			}
		}
		return;
	}

	/**
	 * 
	 */
	public void setMultiCorpRef(boolean isMultiCorpRef) {
		this.isMultiCorpRef = isMultiCorpRef;
	}

	/**
	 * 设置是否允许多选择。 创建日期：(2001-8-24 21:33:23)
	 * 
	 * @param isMultiSelectedEnabled
	 *            boolean
	 */
	public void setMultiSelectedEnabled(boolean isMultiSelectedEnabled) {
		m_bMultiSelectedEnabled = isMultiSelectedEnabled;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-26 9:46:41)
	 * 
	 * @param newNotLeafSelectedEnabled
	 *            boolean
	 */
	public void setNotLeafSelectedEnabled(boolean newNotLeafSelectedEnabled) {
		m_bNotLeafSelectedEnabled = newNotLeafSelectedEnabled;
	}

	/**
	 * 设置参照模型。 创建日期：(2001-8-24 8:36:55)
	 * 
	 * @param refModel
	 *            nc.ui.bd.ref.AbstractRefModel
	 */
	public void setRefModel(AbstractRefModel refModel) {
		m_refModel = (AbstractRefGridTreeModel) refModel;
		if (getRefClassPropVOs() != null) {

			SclPane_tree.setColumnHeaderView(getClassPropPanel());
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-27 8:51:50)
	 */
	private void setTableColumn(UITable table, boolean isDynamicCol) {

		if (table.getColumnCount() < 4) {
			table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
		} else {
			int[] colWidth = new int[table.getColumnCount()];
			int tableWidth = setSpecialTableColumnWidth(table, isDynamicCol,
					colWidth);
			fillBlankSpace(table, tableWidth, colWidth);
			table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
			table.setColumnWidth(colWidth);
		}
		table.sizeColumnsToFit(-1);
	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-8-24</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private int setSpecialTableColumnWidth(UITable table, boolean isDynamicCol,
			int[] colWidth) {

		String nameField = getRefModel().getFieldShowName(
				getRefModel().getRefNameField());
		String columnName = "";
		String name_lang = nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"common", "UC000-0001155");
		/** @res "名称" */
		int tableWidth = 0;
		for (int i = 0; i < table.getColumnCount(); i++) {

			// 有动态列，减少各列的宽度。
			columnName = table.getColumnName(i).toString();
			if (isDynamicCol) {
				if (columnName.indexOf(name_lang) > -1
						|| columnName.equals(nameField)) {
					colWidth[i] = 100;

				} else {
					colWidth[i] = 60;
				}
			} else {
				// 加大名称列默认长度105->200
				// sxj 2003-02-24
				if (columnName.indexOf(name_lang) > -1
						|| columnName.equals(nameField)) {
					colWidth[i] = 140;// 200;

				} else {
					colWidth[i] = 100;
				}
			}
			tableWidth += colWidth[i];
		}
		return tableWidth;
	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-8-24</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void fillBlankSpace(UITable table, int tableWidth, int[] colWidth) {
		// 填充空白位置
		int dialogWidth = (int) getSplpane_Data().getPreferredSize().getWidth();
		int talbleTotalWidth = tableWidth + RefTablePane.ROWHEADERWIDTH + 10;
		if (talbleTotalWidth < dialogWidth) {

			for (int i = 0; i < table.getColumnCount(); i++) {
				colWidth[i] += (dialogWidth - talbleTotalWidth)
						/ table.getColumnCount();

			}
		}
	}

	/**
	 * 
	 * 创建日期：(2003-7-23 21:33:23) 设置第一次参照显示数据的公司
	 */
	public void setTreeGridNodeMultiSelected(boolean isMulti) {
		isTreeGridMultiSelected = isMulti;
	}

	/**
	 * 按照上下级关系生成树 创建日期：(2001-8-27 22:06:42)
	 */
	private void setTreeModel(Vector vTree) {
		ExTreeNode root = null;
		root = new ExTreeNode(m_refModel.getRootName(), true);
		DefaultTreeModel tm = new DefaultTreeModel(root, false);
		getUITree1().setModel(tm);
		Vector m_vecVOs = vTree;

		// sxj 2004-06-23 新构造树方法。

		Hashtable hAllNode = getPkToNode();
		hAllNode.clear();
		HashMap hm = new HashMap();
		Vector vAllTreeNode = new Vector();

		int classJoinField = m_refModel.getClassFieldIndex(m_refModel
				.getClassJoinField());
		for (int i = 0; i < m_vecVOs.size(); i++) {
			Vector row = (Vector) m_vecVOs.elementAt(i);
			ExTreeNode nodepar = new ExTreeNode(row, getTreeColumn(),
					m_refModel.getMark());
			vAllTreeNode.add(nodepar);
			hm.put(row.elementAt(classJoinField), row);
			hAllNode.put(row.elementAt(classJoinField), nodepar);

		}
		int fatherFieldIndex = 0;

		if (m_refModel.getClassFatherField() == null) {
			fatherFieldIndex = m_refModel.getClassFieldIndex(m_refModel
					.getFatherField());
		} else {
			fatherFieldIndex = m_refModel.getClassFieldIndex(m_refModel
					.getClassFatherField());
		}

		for (int i = 0; i < m_vecVOs.size(); i++) {
			ExTreeNode nodepar = (ExTreeNode) vAllTreeNode.get(i);
			Vector row = (Vector) m_vecVOs.elementAt(i);

			String fatherCodeValue = null;

			if (fatherFieldIndex >= 0 && fatherFieldIndex < row.size()) {
				fatherCodeValue = (String) row.get(fatherFieldIndex);
			}

			if (fatherCodeValue == null || fatherCodeValue.trim().length() == 0
					|| hm.get(fatherCodeValue) == null) {
				root.insert(nodepar, root.getChildCount());
				getUITree1().makeVisible(new TreePath(nodepar.getPath()));
			} else {

				ExTreeNode nodeparFather = (ExTreeNode) hAllNode
						.get(fatherCodeValue);
				if (nodeparFather == null) {
					Debug.debug("to find father error:" + fatherCodeValue + ":"
							+ nodepar);
					// 插入到根节点
					root.insert(nodepar, root.getChildCount());
				} else {
					nodeparFather
							.insert(nodepar, nodeparFather.getChildCount());
				}

			}
		}
		getUITree1().updateUI();
		// return tm;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-27 16:46:15)
	 * 
	 * @param strCodingRule
	 *            java.lang.String sxj 2003-07-08 大改，编码规则构造树方法
	 */
	private void setTreeModel(Vector vRecord, String strCodingRule) {

		Object errorObj = null;
		try {
			ExTreeNode root = null;
			root = new ExTreeNode(m_refModel.getRootName(), true);
			DefaultTreeModel tm = new DefaultTreeModel(root, false);

			// 设置成数字型编码规则
			// m_iCodingRule = new int[strCodingRule.length()];
			// for (int i = 0; i < strCodingRule.length(); i++) {
			// m_iCodingRule[i] = Integer.parseInt(strCodingRule.substring(i,
			// i + 1));
			// }
			m_iCodingRule = RefPubUtil.getCodingRule(strCodingRule);

			// 非递归加入树节点
			HashMap hmCode = new HashMap();
			HashMap hmTreeNode = new HashMap();
			int classCodeFieldIndex = m_refModel.getClassFieldIndex(m_refModel
					.getClassRefCodeField().toString());
			Vector vTemp = null;
			String sNewBm = null;
			Hashtable hAllNode = getPkToNode();
			hAllNode.clear();
			if (vRecord != null) {
				// 把数据放入HashMap
				for (int i = 0; i < vRecord.size(); i++) {

					vTemp = (Vector) vRecord.elementAt(i);
					sNewBm = null;
					if (classCodeFieldIndex >= 0 && vTemp != null
							&& classCodeFieldIndex < vTemp.size()) {
						sNewBm = (String) vTemp.elementAt(classCodeFieldIndex);
						hmCode.put(sNewBm, vRecord);

					}

				}

				// 插入树节点
				for (int i = 0; i < vRecord.size(); i++) {

					vTemp = (Vector) vRecord.elementAt(i);
					errorObj = vTemp;
					ExTreeNode newNode = new ExTreeNode(vTemp, getTreeColumn(),
							m_refModel.getMark());
					int classJoinFieldIndex = m_refModel
							.getClassFieldIndex(m_refModel.getClassJoinField()
									.toString());
					hAllNode.put(vTemp.elementAt(classJoinFieldIndex)
							.toString(), newNode);
					if (classCodeFieldIndex >= 0 && vTemp != null
							&& classCodeFieldIndex < vTemp.size())
						sNewBm = (String) vTemp.elementAt(classCodeFieldIndex);
					insertTreeNode(root, newNode, sNewBm, hmCode, hmTreeNode);

				}
			}
			// Model Data，display
			getUITree1().setModel(tm);
			getUITree1().revalidate();

		} catch (Exception e) {
			System.out.println(" errorRow: " + errorObj);
			handleException(e);
		}
	}

	/**
	 * 显示并返回关闭类型。 创建日期：(2001-8-25 13:53:58)
	 * 
	 * @return int
	 */
	public int showModal() {
		htselectedVector.clear();
		if (getRefModel() == null) {
			return -1;
		}

		setUserSize();

		// 封存数据是否显示按钮
		getChkSealedDataShow().setVisible(
				getRefUIConfig().isSealedDataButtonShow());
		if (getRefUIConfig().isSealedDataButtonShow()) {
			getRefModel()
					.setSealedDataShow(getChkSealedDataShow().isSelected());
		}
		// getChkSealedDataShow().setVisible(true);
		setColumnVOs(null);
		// 注册了查询类的参照，查询按钮enable
		String queryClassName = getRefModel().getRefQueryDlgClaseName();

		if (queryClassName != null) {
			getBtnQuery().setVisible(true);
			getUIbtnLocQuery().setVisible(getRefModel().isLocQueryEnable());

		} else {
			getBtnQuery().setVisible(false);
			getUIbtnLocQuery().setVisible(false);
			// getBtnQuery().setEnabled(false);
			// getUIbtnLocQuery().setEnabled(false);
		}

		// 是否分类支持多选择
		if (isTreeGridMultiSelected) {

			getUIButAdd().removeActionListener(this);
			getUIButDel().removeActionListener(this);
			getUIButAdd().addActionListener(this);
			getUIButDel().addActionListener(this);
		} else {
			// 第一次删除组件
			if (!isRemoveCom) {
				getPnlData().remove(getSplpane_Data());
				getPnlData().add(getPnl_TableData(), "Center");
				isRemoveCom = true;
			}
		}

		Vector vDataTree = m_refModel.getClassData();

		if (vDataTree == null)
			vDataTree = new Vector();
		if (isFatherSonTree()) {
			// 按上下级关系构造树
			setTreeModel(vDataTree);
		} else {
			// 按编码原则构造树
			setTreeModel(vDataTree, m_refModel.getCodingRule());
		}

		if (isMultiSelectedEnabled())
			getUITablePane1()
					.getTable()
					.setSelectionMode(
							javax.swing.DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		else
			getUITablePane1().getTable().setSelectionMode(
					javax.swing.DefaultListSelectionModel.SINGLE_SELECTION);

		// sxj 2003-10-28
		// 是否为动态列参照
		if (getRefModel().isDynamicCol()) {
			dynamicColClass = initDynamicColClass();
			String[][] dynamicFieldNames = dynamicColClass
					.getDynaminColNameAndLoc();
			if (dynamicFieldNames != null) {
				String[] strNames = new String[dynamicFieldNames.length];
				for (int i = 0; i < strNames.length; i++) {
					strNames[i] = dynamicFieldNames[i][0];
				}
				// 设置动态列到model
				getRefModel().setDynamicFieldNames(strNames);
				m_vecAllColumnNames = null;

			}

		}

		initTable();

		setShowIndexToModel();

		setComboBoxData(getColumnVOs());

		Vector selectedData = getRefModel().getSelectedData();

		// 初始化连接。
		initConnections();

		// 如果有选中的数据，定位。
		setMatchedDataToUI(selectedData);

		// 是否多公司参照
		if (isMultiCorpRef) {
			getRefCorp().setEnabled(isMultiCorpRef);
			getRefCorp().getRefModel().setFilterPks(
					getRefUIConfig().getMultiCorpRefPks());
			getRefCorp().setPK(m_refModel.getPk_corp());
		} else {
			getUIDialogContentPane().remove(getPnl_CorpRef());
		}

		getUIComboBoxColumn().addItemListener(eventHandler);
		// 维护按钮是否显示
		if (RefAddDocument.getInstance().getFunCode(getRefModel()) == null) {
			getBtnAddDoc().setVisible(false);// 维护按钮是否显示
		}

		int iResult = super.showModal();
		getTFLocate().setText("");

		// 持久化参照的尺寸
		RefUtil.putRefSize(getRefModel().getRefNodeName(), new Dimension(
				getWidth(), getHeight()));

		return iResult;
	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-8-3</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void setMatchedDataToUI(Vector selectedData) {
		if (selectedData != null && selectedData.size() > 0) {

			if (isTreeGridMultiSelected) {
				addRecordToSelectedTable(selectedData);
			} else {
				// 默认取第一个匹配的分类
				AbstractRefGridTreeModel model = (AbstractRefGridTreeModel) getRefModel();
				Vector firstMatchRecode = (Vector) selectedData.get(0);
				int colIndex = model.getFieldIndex(model.getDocJoinField());
				if (colIndex > -1) {
					Object obj = firstMatchRecode.get(colIndex);
					Object classJoinValue = null;
					if (obj instanceof RefValueVO) {
						classJoinValue = ((RefValueVO) obj).getOriginValue();
					} else {
						classJoinValue = obj;
					}

					if (classJoinValue != null) {
						ExTreeNode node = (ExTreeNode) getPkToNode().get(
								classJoinValue);
						if (node != null) {
							expandPath(getUITree1(), node);
							getUITree1().setSelectionPath(
									new TreePath(new Object[] { node }));
						}
					}
				}
				// 根据 setpks的默认值定位

				int[] rowIndexes = getRecordIndexes(getRefModel().getRefData());
				setSelectionRows(getUITablePane1().getTable(), rowIndexes);
			}

		} else {

			setDataToRoot();

		}
	}

	/**
	 * Comment
	 */
	private void onColumn() {
		// UFRefColumnsDlg refColumnsDlg = new UFRefColumnsDlg(this,
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("ref",
		// "UPPref-000340")/* @res "栏目选择" */,
		// getShownColumnNames(), getRefModel().getShownColumns(),
		// getRefModel().getDefaultFieldCount(), getRefModel());

		UFRefColumnsDlg refColumnsDlg = new UFRefColumnsDlg(this,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("ref",
						"UPPref-000340")/* @res "栏目选择" */, getRefModel());
		// refColumnsDlg.setModel(getRefModel());
		if (refColumnsDlg.showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
			// 栏目变化，带公式的数据要转换为Name。这里可以改进提高效率，如果栏目变化且包含有公式，才进行公式执行
			// 考虑用户的实际应用，变化栏目属于小概率事件，这里简单处理。
			getRefModel().reloadData();

			getRefModel().setShownColumns(refColumnsDlg.getSelectedColumns());

			setColumnVOs(null);
			setNewColumnSequence(getUITablePane1().getTable());
			if (isTreeGridMultiSelected) {
				setNewColumnSequence(getTbP_selectedData().getTable());
			}

			setComboBoxData(getColumnVOs());

		}

		refColumnsDlg.destroy();
	}

	/**
	 * Comment
	 */
	public void uIButtonExit_ActionPerformed() {
		closeCancel();
		return;
	}

	/**
	 * Comment
	 */
	protected void onOK() {

		getRefModel().setSelectedData(getSelectedData());
		closeOK();
		return;
	}

	/**
	 * Comment
	 */
	private void onRefresh() {
		// htselectedVector.clear();
		if (getRefModel() == null)
			return;
		// 得到原来选择的节点，以便刷新后再展开
		TreePath[] selectedPath = getUITree1().getSelectionPaths();
		ArrayList al = new ArrayList();
		if (selectedPath != null) {
			String str = "";
			for (int i = 0; i < selectedPath.length; i++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedPath[i]
						.getLastPathComponent();
				if (node.isRoot()) {
					continue;
				}
				ExTreeNode exnode = (ExTreeNode) node;
				Object vRecord = exnode.getUserObject();

				if (vRecord instanceof Vector) {
					// if (isFatherSonTree()) {
					// // pk
					// str = ((Vector) vRecord).elementAt(
					// m_refModel.getClassFieldIndex(m_refModel
					// .getClassJoinField())).toString();
					// } else {
					// str = ((Vector) vRecord).elementAt(
					// m_refModel.getClassFieldIndex(m_refModel
					// .getClassRefCodeField())).toString();
					// }
					str = ((Vector) vRecord).elementAt(
							m_refModel.getClassFieldIndex(m_refModel
									.getClassJoinField())).toString();
					al.add(str);
				}

			}

		}
		// 清除缓存数据---表的部分是当前节点的///////
		// m_refModel.clearData();
		m_refModel.reloadData1();
		// /////
		Vector vDataTree = m_refModel.reloadClassData();
		if (vDataTree == null)
			vDataTree = new Vector();
		if (m_refModel.getCodingRule() == null
				|| m_refModel.getCodingRule().length() == 0) {
			// 按上下级关系构造树
			setTreeModel(vDataTree);
		} else {
			// 按编码原则构造树
			setTreeModel(vDataTree, m_refModel.getCodingRule());
		}

		// initTable();

		// 展开原来选中的节点
		if (al.size() > 0) {
			Hashtable ht = getPkToNode();
			for (int i = 0; i < al.size(); i++) {
				ExTreeNode node = (ExTreeNode) ht.get(al.get(i));
				expandPath(getUITree1(), node);

			}

			// getUITree1().requestFocus();

		}
		getUITree1().requestFocus();
		// setMatchedDataToUI(m_refModel.getSelectedData(), false);
		return;
	}

	/**
	 * Comment
	 */
	private void uIComboBoxColumn_ItemStateChanged(
			java.awt.event.ItemEvent itemEvent) {
		if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
			getTFLocate().setText("");
			getTFLocate().grabFocus();
			blurInputValue("", 0);
		}
		return;
	}

	/**
	 * Comment
	 */
	private void uITextFieldLocate_KeyReleased(java.awt.event.KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_CAPS_LOCK
				|| keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_CONTROL
				|| keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_SHIFT
				|| keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_ALT)
			return;
		String strInput = getTFLocate().getText();
		int col = -1;
		if (getUIComboBoxColumn().getSelectedIndex() >= 0
				&& getUIComboBoxColumn().getSelectedIndex() < getRefModel()
						.getShownColumns().length)
			col = getLocateColumn();
		if (col >= 0) {
			blurInputValue(strInput, col);
		}

		return;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-27 16:15:47)
	 * 
	 * @param e
	 *            javax.swing.event.TreeSelectionEvent
	 */
	public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
		if ((e.getSource() == getUITree1())) {
			getTFLocate().setText("");
			// blurInputValue("",0);
			actionUITree(e);
		}
	}

	/**
	 * 参照内容变化事件监听者必须实现的接口方法
	 * 
	 * @param event
	 *            valueChangedEvent 参照内容变化事件
	 */
	public void valueChanged(nc.ui.pub.beans.ValueChangedEvent event) {
		String pk_corp = getRefCorp().getRefPK();
		// 如果选择根目录，默认为集团
		if (pk_corp == null) {
			pk_corp = "0001";
		}
		getRefModel().setPk_corp(pk_corp);
		onRefresh();

	}

	/**
	 * 返回 BtnQuery 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBtnQuery() {

		return getButtonPanelFactory().getBtnQuery();
	}

	/**
	 * Invoked when an action occurs.
	 */
	private int getLocateColumn() {
		int col = -1;
		if (getUIComboBoxColumn().getSelectedIndex() >= 0
				&& getUIComboBoxColumn().getSelectedIndex() < getRefModel()
						.getShownColumns().length) {
			int colCount = getUITablePane1().getTable().getModel()
					.getColumnCount();
			String colName;
			for (int i = 0; i < colCount; i++) {
				colName = getUITablePane1().getTable().getModel()
						.getColumnName(i);
				if (colName.equals(getUIComboBoxColumn().getSelectedItem()
						.toString())) {
					col = i;
				}
			}
			// col =
			// getRefModel().getShownColumnIndex(getUIComboBoxColumn().getSelectedItem().toString());
		}

		return col;
	}

	/**
	 * 返回 UIbtnLocQuery 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getUIbtnLocQuery() {
		return getButtonPanelFactory().getBtnSimpleQuery();
	}

	/**
	 * 取得当前栏目VO
	 */
	private nc.vo.bd.ref.ReftableVO getVO(String pk_corp) {

		return getRefModel().getRefTableVO(pk_corp);
	}

	/**
	 * 
	 */
	private void resetTableData(UITable table, Vector data, boolean isDynamicCol) {

		// NCTableModel tableModel = (NCTableModel) table.getModel();
		// tableModel.setDataVector(data);
		getUITablePane1().setDataVector(data);
		setTableColumn(table, isDynamicCol);

	}

	/**
	 * 根据定位条件,到后台查询
	 */
	private void onLocQuery() {
		if (!getUIbtnLocQuery().isVisible()) {
			return;
		}
		String locateValue = getTFLocate().getText();
		if (locateValue != null && locateValue.trim().length() > 0) {
			// RefcolumnVO fieldQueryVO = (RefcolumnVO) getUIComboBoxColumn()
			// .getSelectedItem();

			String queryWhere = getQueryWhere();
			// if (fieldQueryVO == null) {
			// System.out.println("fieldQueryVO==null,无法查询。");
			// return;
			//
			// }
			// if (fieldQueryVO.getDatatype() != null
			// && fieldQueryVO.getDatatype().intValue() != 0) {
			// queryWhere = " and " + fieldQueryVO.getFieldname() + "="
			// + locateValue + " ";
			//
			// } else {
			// // queryWhere = " and " + fieldQueryVO.getFieldname() + " like
			// // '%"
			// // + locateValue + "%' ";
			// // 两边都加%，所以用不上
			// queryWhere = " and " + fieldQueryVO.getFieldname() + " like '"
			// + locateValue + "%' ";
			// }
			String oldWherePart = getRefModel().getWherePart();
			String newWherePart = oldWherePart + queryWhere;

			getRefModel().setWherePart(newWherePart);

			m_refModel.setClassJoinValue(IRefConst.QUERY);

			// m_refModel.setClassJoinValue(null);

			Vector vDataAll = m_refModel.getRefData();

			if (vDataAll == null) {
				vDataAll = new Vector();
			}

			setDataToRoot();

			// ((NCTableModel) getUITablePane1().getTable().getModel())
			// .setDataVector(vDataAll);
			getUITablePane1().setDataVector(vDataAll);

			getRefModel().setWherePart(oldWherePart);
			m_refModel.setClassJoinValue(null);

		}

	}

	private String getQueryWhere() {
		String queryWhere = "";
		String locateValue = getTFLocate().getText();
		RefcolumnVO[] columnVOs = getColumnVOs();

		for (int i = 0; i < columnVOs.length; i++) {
			RefcolumnVO fieldQueryVO = columnVOs[i];

			if (!fieldQueryVO.getIscolumnshow().booleanValue()) {
				continue;
			}
			if (fieldQueryVO.getDatatype() != null
					&& fieldQueryVO.getDatatype().intValue() != 0) {
				queryWhere += fieldQueryVO.getFieldname() + "=" + locateValue
						+ " ";

			} else {
				// queryWhere = " and " + fieldQueryVO.getFieldname() + " like
				// '%"
				// + locateValue + "%' ";
				// 两边都加%，所以用不上
//				queryWhere += fieldQueryVO.getFieldname() + " like '"
//						+ locateValue + "%' ";
				
				// add by river for 2012-11-20
				// 添加左端模糊查询
				queryWhere += fieldQueryVO.getFieldname() + " like '%"
						+ locateValue + "%' ";
			}
			queryWhere += " or ";
		}

		return " and (" + queryWhere.substring(0, queryWhere.length() - 4)
				+ ") ";
	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-5-18</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void setDataToRoot() {
		getUITree1()
				.setSelectionPath(
						new TreePath(new Object[] { getUITree1().getModel()
								.getRoot() }));
	}

	/**
	 * Comment
	 */
	private void onQuery() {

		if (getRefModel().getRefQueryDlgClaseName() != null) {
			String className = getRefModel().getRefQueryDlgClaseName();
			Object interfaceClass = null;
			IRefQueryDlg queryDlg = null;
			try {
				// 是否实现接口检查
				try {
					// interfaceClass = Class.forName(className).newInstance();
					Class modelClass = Class.forName(className);
					java.lang.reflect.Constructor cs = null;
					try { // 用公司做构造子
						cs = modelClass
								.getConstructor(new Class[] { Container.class });
						interfaceClass = cs.newInstance(new Object[] { this });
					} catch (NoSuchMethodException ee) { // 缺省构造
						interfaceClass = modelClass.newInstance();
					}
				} catch (Exception e) {
					Debug.error(e.getMessage(), e);
					return;
				}
				// 类型转换
				if (interfaceClass == null) {
					return;
				}
				if (interfaceClass instanceof IRefQueryDlg) {
					queryDlg = (IRefQueryDlg) interfaceClass;
					if (interfaceClass instanceof IRefQueryDlg2) {
						((IRefQueryDlg2) queryDlg).setRefModel(getRefModel());
					}
				} else {
					MessageDialog.showErrorDlg(this,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("ref",
									"UPPref-000341")/* @res "错误" */,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("ref",
									"UPPref-000366")/*
													 * @res
													 * "未实现IRefQueryDlg或IRefQueryDlg2接口"
													 */);
					return;
				}

				// 显示对话框

				queryDlg.setParent(this);
				queryDlg.setPk_corp(getRefModel().getPk_corp());
				queryDlg.showModal();
				if (queryDlg.getResult() == UIDialog.ID_OK) {
					getUITree1().setSelectionPath(
							new TreePath(new Object[] { getUITree1().getModel()
									.getRoot() }));

					getRefModel().setQuerySql(queryDlg.getConditionSql());
					m_refModel.setClassJoinValue(IRefConst.QUERY);
					// m_refModel.setClassJoinValue(null);
					Vector vDataAll = m_refModel.getRefData();

					if (vDataAll == null)
						vDataAll = new Vector();
					getUITablePane1().setDataVector(vDataAll);
				}

				getRefModel().setQuerySql(null);

			} catch (Exception e) {
				Debug.error(e.getMessage(), e);

			}

		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-29 14:30:52)
	 * 
	 * @param iColumns
	 *            int[]
	 */
	private void setComboBoxData(RefcolumnVO[] items) {
		getUIComboBoxColumn().removeAllItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getIscolumnshow().booleanValue()) {
				getUIComboBoxColumn().addItem(items[i]);
			}
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-6-18 15:36:18)
	 */
	public void setFilterDlgShow(boolean isFilterDlgShow) {
	}

	/**
	 * setIncludeSubShow 方法注解。
	 */
	public void setIncludeSubShow(boolean newIncludeSubShow) {
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-6-18 15:36:18)
	 */
	public void setVersionButtonShow(boolean isVersionButtonShow) {
	}

	private void expandPath(UITree tree, ExTreeNode node) {
		if (node == null) {
			return;
		}
		TreePath treePath = new TreePath(node.getPath());
		tree.expandPath(treePath);
		tree.setSelectionPath(treePath);
		tree.makeVisible(treePath);
	}

	/**
	 * @return 返回 pkToNode。
	 */
	private Hashtable getPkToNode() {
		return pkToNode;
	}

	/**
	 * @return 返回 htPkToRow。
	 */
	private Hashtable getHtPkToRow() {
		return htPkToRow;
	}

	private void setSelectionRows(UITable table, int[] rowIndex) {
		if (rowIndex != null && rowIndex.length > 0) {
			int iGridIndex = -1;
			for (int i = 0; i < rowIndex.length; i++) {
				iGridIndex = rowIndex[i];
				if (iGridIndex >= 0
						&& iGridIndex < getUITablePane1().getTable()
								.getRowCount()) {
					table.getSelectionModel().addSelectionInterval(iGridIndex,
							iGridIndex);

					table.scrollRectToVisible(getUITablePane1().getTable()
							.getCellRect(iGridIndex, 0, false));
				} else {
					table.clearSelection();
				}
			}

		}

	}

	private int[] getRecordIndexes(Vector vDataAll) {
		int[] indexes = null;
		if (vDataAll != null && vDataAll.size() > 0) {
			getHtPkToRow().clear();
			String pk = null;
			for (int i = 0; i < vDataAll.size(); i++) {
				Vector record = (Vector) vDataAll.get(i);
				String pkField = getRefModel().getPkFieldCode();
				if (pkField != null) {
					int pkIndex = getRefModel().getFieldIndex(pkField);
					if (pkIndex >= 0 && pkIndex < record.size()) {
						pk = record.get(pkIndex).toString();
						getHtPkToRow().put(pk, new Integer(i));
					}
				}

			}
		}

		String[] selectedDatas = getRefModel().getPkValues();
		ArrayList al = new ArrayList();
		if (selectedDatas != null && selectedDatas.length > 0) {

			for (int i = 0; i < selectedDatas.length; i++) {
				Integer rowNumber = null;
				if (selectedDatas[i] != null) {
					rowNumber = ((Integer) getHtPkToRow().get(selectedDatas[i]));
				}
				if (rowNumber != null) {
					al.add(rowNumber);

				}

			}
			if (al.size() > 0) {
				indexes = new int[al.size()];
				for (int i = 0; i < indexes.length; i++) {
					indexes[i] = ((Integer) al.get(i)).intValue();
				}
			}
		}
		return indexes;
	}

	private RefTableColumnModel getTableColumnModel(UITable table) {
		return (RefTableColumnModel) table.getColumnModel();
	}

	/*
	 * 设置定位Combox,表的栏目的显示顺序
	 */
	private void setNewColumnSequence(UITable UITable) {

		boolean isDynamicCol = getRefModel().isDynamicCol();

		getTableColumnModel(UITable).setColumnVOs(getColumnVOs());
		getTableColumnModel(UITable).adjustColumnShowSequence();
		setTableColumn(UITable, isDynamicCol);

	}

	private void setShowIndexToModel() {
		ReftableVO vo = getVO(getRefModel().getPk_corp());
		if (vo != null && vo.getColumnVOs() != null) {

			ArrayList list = new ArrayList();
			for (int i = 0; i < vo.getColumnVOs().length; i++) {
				if (vo.getColumnVOs()[i].getIscolumnshow().booleanValue()) {
					list.add(vo.getColumnVOs()[i].getColumnshowindex());
				}

			}
			if (list.size() > 0) {
				int[] showIndex = new int[list.size()];
				for (int i = 0; i < showIndex.length; i++) {
					showIndex[i] = ((Integer) list.get(i)).intValue();
				}
				getRefModel().setShownColumns(showIndex);
			}

		}
	}

	/**
	 * @return 返回 btnAddDoc。
	 */
	private UIButton getBtnAddDoc() {

		return getButtonPanelFactory().getBtnMaintenanceDoc();
	}

	private void onAddDoc() {
		Object[] selectPks = getRefModel().getValues(
				getRefModel().getPkFieldCode(), getSelectedData());

		RefAddDocument.getInstance().openDocFrame(this, getRefModel(),
				new Object[] { selectPks, getSelectedData() });
		onRefresh();
	}

	private UICheckBox getChkSealedDataShow() {

		return getButtonPanelFactory().getChkSealedDataShow();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bd.ref.IRefUINew2#setRefUIConfig(nc.ui.bd.ref.RefUIConfig)
	 */
	public void setRefUIConfig(RefUIConfig refUIConfig) {
		this.refUIConfig = refUIConfig;

	}

	public RefUIConfig getRefUIConfig() {
		return refUIConfig;
	}

	protected void onChkSealedDataButton() {
		getRefModel().setSealedDataShow(getChkSealedDataShow().isSelected());
		onRefresh();

	}

	/**
	 * @return 返回 columnVOs。
	 */
	private RefcolumnVO[] getColumnVOs() {

		if (columnVOs == null) {
			columnVOs = RefPubUtil.getColumnSequences(getRefModel());
			// if (columnVOs != null) {
			// setDefRefName();
			// }

		}
		return columnVOs;
	}

	// private void setDefRefName() {
	// String[] showDefFields = getRefModel().getShowDefFields();
	// String[] newDefFieldNames = null;
	// if (showDefFields != null && showDefFields.length > 0) {
	// DefFieldInfo defFieldInfo = new DefFieldInfo(showDefFields);
	// try {
	// newDefFieldNames = UFRefDefTanslateUtil.getDefFiledsShowName(
	// defFieldInfo, Integer.valueOf(
	// getRefModel().getOrgTypeCode()).intValue(),
	// getRefModel().getPk_corp());
	// } catch (Exception e) {
	// Logger.error(e.getMessage(), e);
	// }
	// for (int i = 0; i < newDefFieldNames.length; i++) {
	// getRefColumnVOsMap().get(showDefFields[i]).setResid(
	// newDefFieldNames[i]);
	// }
	// }
	//
	// }
	//
	// private Map<String, RefcolumnVO> getRefColumnVOsMap() {
	//
	// Map<String, RefcolumnVO> map = new HashMap<String, RefcolumnVO>();
	//
	// for (int i = 0; i < columnVOs.length; i++) {
	//
	// map.put(columnVOs[i].getFieldname(), columnVOs[i]);
	// }
	// return map;
	// }

	/**
	 * @param columnVOs
	 *            要设置的 columnVOs。
	 */
	private void setColumnVOs(RefcolumnVO[] columnVOs) {
		this.columnVOs = columnVOs;
	}

	private nc.ui.pub.beans.UIPanel getPnl_south() {

		return getButtonPanelFactory().getPnl_south();
	}

	/**
	 * @return 返回 pnl_locate_btn。
	 */
	private UIPanel getPnl_locate_btn() {

		return getButtonPanelFactory().getPnl_locate_btn(true);
	}

	/**
	 * @return 返回 buttonPanelFactory。
	 */
	private RefButtonPanelFactory getButtonPanelFactory() {
		if (buttonPanelFactory == null) {
			buttonPanelFactory = new RefButtonPanelFactory();
		}
		return buttonPanelFactory;
	}

	private boolean isFatherSonTree() {
		return (m_refModel.getCodingRule() == null || m_refModel
				.getCodingRule().length() == 0);
	}

	/**
	 * <p>
	 * <strong>最后修改人：sxj</strong>
	 * <p>
	 * <strong>最后修改日期：2006-8-24</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void setUserSize() {
		// 设置参照大小
		Dimension dim = RefUtil.getRefSize(getRefModel().getRefNodeName(),
				new Dimension(width, height));
		width = (int) dim.getWidth();
		height = (int) dim.getHeight();
		setSize(width, height);
	}

	private ExTreeNode getSelectedNode() {
		TreePath path = getUITree1().getSelectionPath();

		if (path != null) {
			return (ExTreeNode) path.getLastPathComponent();
		}

		return null;

	}

	protected void processWindowEvent(WindowEvent e) {
		// TODO Auto-generated method stub
		super.processWindowEvent(e);

		getButtonPanelFactory().setTfLocateTextAndColor();

	}
}