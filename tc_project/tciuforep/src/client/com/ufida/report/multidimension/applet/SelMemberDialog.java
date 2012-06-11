/*
 * Created on 2005-6-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.multidimension.applet;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UICheckBoxMenuItem;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIPopupMenu;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.util.NCTreeCreateTool;
import nc.ui.pub.util.NCTreeModel;
import nc.ui.pub.util.NCTreeNode;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.MeasureVO;

import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.SelDimMemberVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.sysplugin.dnd.DndHandler;

/**
 * @author ll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SelMemberDialog extends UIDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jPanel = null;

	private JTree jTree = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private JButton jButton = null;

	private JButton jButton1 = null;

	private JButton jButton2 = null;

	private JButton jButton3 = null;

	private JScrollPane jScrollPane = null;

	private JScrollPane jScrollPane1 = null;

	private JList jList = null;

	private DefaultListModel jListModel = null;

	private UIPopupMenu m_popupMenu = null;

	private JCheckBoxMenuItem[] m_miType = null;

	// 已经选中的维度成员
	// private Vector m_vecMember = new Vector();

	// 所有成员Hash
	private Hashtable<String, IMember> m_htMember = new Hashtable<String, IMember>();

	private Hashtable<String, SelTreeNode> m_htTreeNode = new Hashtable<String, SelTreeNode>();

	private class SelTreeNode extends NCTreeNode {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public SelTreeNode(Object userObject) {
			super(userObject);
		}

		boolean m_bSelectable = true; // 主要用于指标根节点,不能选择

		boolean m_isCheckboxSelected = false;

		public void setCheckboxSelected(boolean selected) {
			m_isCheckboxSelected = selected;
		}

		public boolean isCheckboxSelected() {
			return m_isCheckboxSelected;
		}

		public boolean isSelectable() {
			return m_bSelectable;
		}

		public void setSelectable(boolean bSelectable) {
			m_bSelectable = bSelectable;
		}

	}

	private class SelCheckNodeRenderer extends UIPanel implements javax.swing.tree.TreeCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/** 选择框 */
		protected javax.swing.JCheckBox m_checkBox = null;

		/** 标签 */
		protected CheckNodeLabel m_checkNodeLabel = null;

		/**
		 * CheckNodeRenderer 构造子注解。
		 */
		public SelCheckNodeRenderer() {
			super();
			setLayout(new java.awt.BorderLayout());
			add(getCheckBox(), "West");
			add(getCheckNodeLabel(), "Center");
		}

		/**
		 * 选择框。 创建日期：(2001-11-7 19:23:19)
		 * 
		 * @since V1.00
		 * @return javax.swing.JCheckBox
		 */
		public javax.swing.JCheckBox getCheckBox() {
			if (m_checkBox == null) {
				m_checkBox = new UICheckBox();
				m_checkBox.setPreferredSize(new Dimension(20, 20));
			}
			return m_checkBox;
		}

		/**
		 * 选择标签。 创建日期：(2001-11-7 19:23:19)
		 * 
		 * @since V1.00
		 * @return nc.install.ui.CheckNodeLabel
		 */
		public CheckNodeLabel getCheckNodeLabel() {
			if (m_checkNodeLabel == null) {
				m_checkNodeLabel = new CheckNodeLabel();
			}
			return m_checkNodeLabel;
		}

		/**
		 * 返回首选大小。 创建日期：(2001-11-7 21:48:40)
		 * 
		 * @since V1.00
		 * @return java.awt.Dimension
		 */
		public java.awt.Dimension getPreferredSize() {
			Dimension dCheckBox = getCheckBox().getPreferredSize();
			Dimension dLabel = getCheckNodeLabel().getPreferredSize();
			return new Dimension(dCheckBox.width + dLabel.width, (dCheckBox.height < dLabel.height ? dLabel.height + 1
					: dCheckBox.height + 1));
		}

		/**
		 * Sets the value of the current tree cell to <code>value</code>. If
		 * <code>selected</code> is true, the cell will be drawn as if
		 * selected. If <code>expanded</code> is true the node is currently
		 * expanded and if <code>leaf</code> is true the node represets a leaf
		 * anf if <code>hasFocus</code> is true the node currently has focus.
		 * <code>tree</code> is the JTree the receiver is being configured
		 * for. Returns the Component that the renderer uses to draw the value.
		 * 
		 * @return Component that the renderer uses to draw the value.
		 */
		public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree, Object value, boolean selected,
				boolean expanded, boolean leaf, int row, boolean hasFocus) {
			if (value instanceof SelTreeNode) {
				SelTreeNode node = (SelTreeNode) value;
				String stringValue = node.getNodeName();

				getCheckBox().setSelected(node.isCheckboxSelected());

				getCheckNodeLabel().setEnabled(true);
				getCheckNodeLabel().setFont(tree.getFont());
				getCheckNodeLabel().setText(stringValue);
				getCheckNodeLabel().setSelected(selected);
				getCheckNodeLabel().setHasFocus(hasFocus);

				Color selColor = selected ? jList.getSelectionBackground() : jList.getBackground();
				setBackground(selColor);
				getCheckBox().setBackground(selColor);
				getCheckNodeLabel().setBackground(selColor);
			}
			return this;
		}
	}

	private class NodeSelectionListener extends MouseAdapter {

		JTree tree;

		NodeSelectionListener(JTree tree) {
			this.tree = tree;
		}

		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			int row = tree.getRowForLocation(x, y);
			TreePath path = tree.getPathForRow(row);
			if (path != null) {
				SelTreeNode node = (SelTreeNode) path.getLastPathComponent();
				// 未选中时，可以编辑，设置成选中；不可反向操作
				if (!node.isCheckboxSelected()) {
					// 树结点设置选中
					if (node.isSelectable()) {
						node.setCheckboxSelected(true);

						// 加入到已选列表中
						getJListModel().addElement(new SelDimMemberVO((IMember) m_htMember.get(node.getId())));
						refreshList();
						((DefaultTreeModel) tree.getModel()).nodeChanged(node);
					}
				}
			}
		}

	}

	/**
	 * This is the default constructor
	 */
	public SelMemberDialog(Container con) {
		super(con);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJPanel());
		this.setSize(560, 455);
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel1 = new nc.ui.pub.beans.UILabel();
			jLabel = new nc.ui.pub.beans.UILabel();
			jPanel = new UIPanel();
			jPanel.setLayout(null);
			jLabel.setBounds(19, 18, 96, 23);
			jLabel.setText(StringResource.getStringResource("ubimultidim0008"));
			jLabel1.setBounds(237, 18, 96, 23);
			jLabel1.setText(StringResource.getStringResource("ubimultidim0009"));
			jPanel.add(jLabel, null);
			jPanel.add(jLabel1, null);

			jPanel.add(getJButtonDelete(), null);
			jPanel.add(getJButtonDeleteAll(), null);
			jPanel.add(getJButtonOK(), null);
			jPanel.add(getJButtonCancel(), null);
			jPanel.add(getJScrollPane(), null);
			jPanel.add(getJScrollPane1(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jTree
	 * 
	 * @return javax.swing.JTree
	 */
	private JTree getJTree() {
		if (jTree == null) {
			jTree = new UITree();
			jTree.setCellRenderer(new SelCheckNodeRenderer());
			jTree.addMouseListener(new NodeSelectionListener(jTree));
		}
		return jTree;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonDelete() {
		if (jButton == null) {
			jButton = new nc.ui.pub.beans.UIButton();
			jButton.setBounds(450, 77, 75, 22);
			jButton.setText(StringResource.getStringResource("miufo1000930"));
			jButton.addActionListener(this);
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonDeleteAll() {
		if (jButton1 == null) {
			jButton1 = new nc.ui.pub.beans.UIButton();
			jButton1.setBounds(450, 137, 75, 22);
			jButton1.setText(StringResource.getStringResource("ubimultidim0018"));
			jButton1.addActionListener(this);
		}
		return jButton1;
	}

	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOK() {
		if (jButton2 == null) {
			jButton2 = new nc.ui.pub.beans.UIButton();
			jButton2.setBounds(162, 387, 75, 22);
			jButton2.setText(StringResource.getStringResource("miufo1000064"));
			jButton2.addActionListener(this);
		}
		return jButton2;
	}

	/**
	 * This method initializes jButton3
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCancel() {
		if (jButton3 == null) {
			jButton3 = new nc.ui.pub.beans.UIButton();
			jButton3.setBounds(316, 387, 75, 22);
			jButton3.setText(StringResource.getStringResource("miufo1000274"));
			jButton3.addActionListener(this);
		}
		return jButton3;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(19, 48, 210, 312);
			jScrollPane.setViewportView(getJTree());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new UIScrollPane();
			jScrollPane1.setBounds(240, 48, 190, 312);
			jScrollPane1.setViewportView(getJList());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJList() {
		if (jList == null) {
			jList = new UIList();
			jList.setModel(getJListModel());

			DndHandler.enableDndDrag(jList, new SelMemberDndAdapter(jList), DnDConstants.ACTION_MOVE);
			DndHandler.enableDndDrop(jList, new SelMemberDndAdapter(jList));

			jList.addMouseListener(new MemberListMouseListener());
			jList.setCellRenderer(new SelMemberListRenderer());
		}
		return jList;
	}

	private DefaultListModel getJListModel() {
		if (jListModel == null) {
			jListModel = new DefaultListModel();
		}
		return jListModel;
	}

	public void setParams(IMember[] allMembers, SelDimMemberVO[] selMembers) {
		m_htMember.clear();
		m_htTreeNode.clear();
		if (allMembers != null) {
			// 设置维度成员树模型
			SelTreeNode root = null;
			Vector<SelTreeNode> vec = new Vector<SelTreeNode>();
			for (int i = 0; i < allMembers.length; i++) {
				IMember mem = allMembers[i];

				SelTreeNode treeNode = new SelTreeNode(mem.getName());

				treeNode.setNodeCode(mem.getMemberID());
				treeNode.setNodeName(mem.getName());

				// 判断是否是指标的根节点
				treeNode.setSelectable(!isMeasureRoot(mem));

				String parentID = getMemberParentID(mem);

				treeNode.setParentCode(parentID);
				treeNode.setId(mem.getMemberID());
				// treeNode.setDataVO(mem);

				if (parentID == null) {
					root = treeNode;
				} else {
					vec.addElement(treeNode);
				}
				// 缓存所有维度成员和树结点
				m_htMember.put(mem.getMemberID(), mem);
				m_htTreeNode.put(mem.getMemberID(), treeNode);
			}
			SelTreeNode[] nodes = new SelTreeNode[vec.size()];
			vec.copyInto(nodes);

			NCTreeModel model = new NCTreeModel(root);
			NCTreeCreateTool.createSubTreeByID(nodes, root, model);
			getJTree().setModel(model);
		}

		// 设置已选择成员列表内容
		getJListModel().clear();
		if (selMembers != null) {
			for (int i = 0; i < selMembers.length; i++) {
				SelDimMemberVO selMem = selMembers[i];
				getJListModel().addElement(selMem);
				// 同时设置树结点的选中状态
				SelTreeNode treeNode = (SelTreeNode) m_htTreeNode.get(selMem.getMemberVO().getMemberID());
				if (treeNode != null)
					treeNode.setCheckboxSelected(true);
			}
		}
		refreshList();
	}

	private void refreshList() {
		// getJList().firePropertyChange(arg0, arg1, arg2);
	}

	public SelDimMemberVO[] getSelMembers() {
		if (getJListModel().getSize() == 0) {
			// 错误提示，至少应该选择一个成员
			return null;
		}
		SelDimMemberVO[] selMembers = new SelDimMemberVO[getJListModel().getSize()];
		getJListModel().copyInto(selMembers);
		return selMembers;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == getJButtonDelete()) {
			int[] indexes = getJList().getSelectedIndices();
			if (indexes != null && indexes.length > 0) {
				for (int i = indexes.length - 1; i >= 0; i--) {
					int index = indexes[i];
					SelDimMemberVO selDim = (SelDimMemberVO) getJListModel().getElementAt(index);

					String id = selDim.getMemberVO().getMemberID();
					changeSelTreeNode(id, false);

					getJListModel().remove(index);
				}
				refreshList();
			}
			return;
		}
		if (e.getSource() == getJButtonDeleteAll()) {
			changeSelTreeNode(null, false);

			getJListModel().clear();
			refreshList();
			return;
		}
		if (e.getSource() == getJButtonOK()) {
			super.closeOK();
		}
		if (e.getSource() == getJButtonCancel()) {
			super.closeCancel();
		}
	}

	/**
	 * 设置树节点的选中状态，参数nodeID为空表示处理所有节点
	 * 
	 * @param nodeID
	 * @param isCheckSelected
	 */
	private void changeSelTreeNode(String nodeID, boolean isCheckSelected) {
		if (nodeID != null) {
			SelTreeNode treeNode = (SelTreeNode) m_htTreeNode.get(nodeID);
			if (treeNode != null)
				treeNode.setCheckboxSelected(isCheckSelected);
			((DefaultTreeModel) getJTree().getModel()).nodeChanged(treeNode);
		} else {
			Enumeration nodes = m_htTreeNode.elements();
			while (nodes.hasMoreElements()) {
				SelTreeNode treeNode = (SelTreeNode) nodes.nextElement();
				treeNode.setCheckboxSelected(false);
				((DefaultTreeModel) getJTree().getModel()).nodeChanged(treeNode);
			}
		}

	}

	// 菜单监听
	class MenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			onMenuItemClick(e);
		};
	}

	class MemberListMouseListener extends MouseAdapter {
		public void mouseReleased(java.awt.event.MouseEvent e) {
			mouseReleasedMemberList(e);
		};
	}

	private UIPopupMenu getPopUpMenu() {
		if (m_popupMenu == null) {

			m_popupMenu = new UIPopupMenu();
			MenuItemListener itemListener = new MenuItemListener();

			JCheckBoxMenuItem[] items = getMenuItems();
			for (int i = 0; i < items.length; i++) {
				items[i].addActionListener(itemListener);

				m_popupMenu.add(items[i]);
			}
		}
		return m_popupMenu;
	}

	private JCheckBoxMenuItem[] getMenuItems() {
		if (m_miType == null) {
			m_miType = new UICheckBoxMenuItem[] {
					new UICheckBoxMenuItem(StringResource.getStringResource("ubimultidim0011")),
					new UICheckBoxMenuItem(StringResource.getStringResource("ubimultidim0012")),
					new UICheckBoxMenuItem(StringResource.getStringResource("ubimultidim0013")),
					new UICheckBoxMenuItem(StringResource.getStringResource("ubimultidim0014")),
					new UICheckBoxMenuItem(StringResource.getStringResource("ubimultidim0015")),
					new UICheckBoxMenuItem(StringResource.getStringResource("ubimultidim0016")) };
		}
		return m_miType;
	}

	/**
	 * 创建日期:(2003-7-2 17:15:00)
	 * 
	 * @param e
	 *            java.awt.event.MouseEvent
	 */
	private void mouseReleasedMemberList(MouseEvent e) {
		if (!e.isPopupTrigger())
			return;

		Point p = e.getPoint();
		if (!getJList().contains(p))
			return;

		if (getJList().getModel().getSize() == 0)
			return;

		int index = getJList().getSelectedIndex();

		// 如果没有选中成员，则将弹出菜单位置设置为选中
		if (index < 0 || index >= getJList().getModel().getSize()) {
			index = getJList().locationToIndex(p);
		}
		// 目前处理是为选中的成员设置属性

		if (index >= 0 && index < getJList().getModel().getSize()) {
			getJList().setSelectedIndex(index);
			SelDimMemberVO selMem = (SelDimMemberVO) getJList().getModel().getElementAt(index);
			if (selMem.getMemberVO().getDimID().equals(IMultiDimConst.PK_MEASURE_DIMDEF))
				return;
			int type = selMem.getSelectType();

			for (int i = 0; i < getMenuItems().length; i++) {
				getMenuItems()[i].setSelected(false);
			}
			getMenuItems()[type].setSelected(true);

			getPopUpMenu().show((Component) e.getSource(), e.getX(), e.getY());
		}
	}

	private void onMenuItemClick(ActionEvent e) {
		JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();

		JCheckBoxMenuItem[] menuItems = getMenuItems();
		if (!item.isSelected()) {
			menuItems[0].setSelected(true);
			return;
		}

		int type = -1;

		for (int i = 0; i < menuItems.length; i++) {
			if (item == menuItems[i]) {
				type = i;
				// break;
			} else {
				menuItems[i].setSelected(false);
			}
		}
		if (type == -1)
			return;

		menuItems[type].setSelected(true);

		int index = getJList().getSelectedIndex();
		if (index >= 0 && index < getJListModel().size()) {
			((SelDimMemberVO) getJListModel().elementAt(index)).setSelectType(type);
			refreshList();
		}

	}

	private String getMemberParentID(IMember mem) {
		if (mem instanceof DimMemberVO) {
			if (mem.getDepth().intValue() == 0)
				return null;

			String parentID = ((DimMemberVO) mem).getLevels()[mem.getDepth().intValue() - 1];
			return parentID;
		} else {
			if (mem instanceof MeasureVO) {
				MeasureVO new_mem = (MeasureVO) mem;
				if (new_mem.isRoot())
					return null;
				else
					return MeasureVO.ROOT_MEASURE_ID;
			}
		}
		return null;
	}

	private boolean isMeasureRoot(IMember member) {
		if (IMultiDimConst.PK_MEASURE_DIMDEF.equals(member.getDimID())) {
			MeasureVO mVO = (MeasureVO) member;
			return mVO.isRoot();
		}
		return false;
	}

} // @jve:decl-index=0:visual-constraint="61,10"
