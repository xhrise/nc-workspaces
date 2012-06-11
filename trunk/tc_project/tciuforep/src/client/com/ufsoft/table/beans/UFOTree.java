package com.ufsoft.table.beans;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import nc.ui.pub.tree.TreeNodeSearcher;
import com.ufsoft.report.util.MultiLang;

/**
 * 功能: 在报表工具和离线客户端使用的Tree控件，以树状结构展示数据。
 *   <p>类似UAP平台的UITree类。</p>
 * 创建日期:(2006-10-17 04:40:54)
 * @author chxiaowei
 */
public class UFOTree extends javax.swing.JTree implements
		javax.swing.event.TreeExpansionListener {
	private boolean fieldExclusiveExpand = false;
	private boolean fieldGradeExpand = false;
	private TreeNodeSearcher nodeSearcher = null;
	
	public UFOTree() {
		super();
		initialize();
	}
	
	public UFOTree(javax.swing.tree.TreeModel p0) {
		super(p0);
		initialize();
	}
	
	/**
	 * @i18n miufo00007=输入名称搜索:
	 */
	private void initialize(){
		nodeSearcher = new TreeNodeSearcher(this){

			@Override
			protected boolean isMatch(String inputText, Object node) {
				boolean isMatch = false;
				String objStr = node.toString();
				if (objStr != null) {
						//普通搜索
						isMatch = objStr.toLowerCase().indexOf(inputText.toLowerCase()) > -1;
				}
				return isMatch;
			}

		};
		nodeSearcher.setInputHint(MultiLang.getString("miufo00007"))/* @res "输入名称搜索:" */;
		addTreeExpansionListener(this);
		putClientProperty("JTree.lineStyle", "Angled");
	}
	
	/**
	 * 返回数搜索器
	 * 
	 * @return
	 */
	public TreeNodeSearcher getTreeNodeSearcher() {
		return nodeSearcher;
	}

	public void setTreeNodeSearcherHint(String text) {
		nodeSearcher.setInputHint(text);
	}
	
	/**
	 * 功能：得到提示信息
	 * @return java.lang.String
	 */
	public String getToolTipText() {
		return super.getToolTipText();
	}
	
	/**
	 * 功能：根据 <code>MouseEvent</code> 得到提示信息
	 * @param e java.lang.String
	 * @return java.lang.String
	 */
	public String getToolTipText(MouseEvent e) {
		return super.getToolTipText(e);
	}
	
	/**
	 * 功能：获取 fieldExclusiveExpand 特性 (boolean) 值.
	 * @return fieldExclusiveExpand 特性值.
	 * @see #setExclusiveExpand
	 */
	public boolean isExclusiveExpand() {
		return fieldExclusiveExpand;
	}

	/**
	 * 功能：获取 fieldGradeExpand 特性 (boolean) 值.
	 * @return fieldGradeExpand 特性值.
	 * @see #setGradeExpand
	 */
	public boolean isGradeExpand() {
		return fieldGradeExpand;
	}

	/**
	 * 功能：设置 fieldExclusiveExpand 特性 (boolean) 值.
	 * @param exclusiveExpand 新的特性值.
	 * @see #isExclusiveExpand
	 */
	public void setExclusiveExpand(boolean exclusiveExpand) {
		boolean oldValue = fieldExclusiveExpand;
		fieldExclusiveExpand = exclusiveExpand;
		firePropertyChange("exclusiveExpand", new Boolean(oldValue),
				new Boolean(exclusiveExpand));
	}

	/**
	 * 功能：设置 fieldGradeExpand 特性 (boolean) 值.
	 * @param fieldGradeExpand 新的特性值.
	 * @see #isGradeExpand
	 */
	public void setGradeExpand(boolean gradeExpand) {
		boolean oldValue = fieldGradeExpand;
		fieldGradeExpand = gradeExpand;
		firePropertyChange("gradeExpand", new Boolean(oldValue), new Boolean(
				gradeExpand));
	}

	/**
	 * 功能：树控件节点折叠
	 * @param event javax.swing.event.TreeExpansionEvent
	 * @return
	 */
	public void treeCollapsed(javax.swing.event.TreeExpansionEvent event) {
	}

	/**
	 * 功能：树控件节点展开
	 * @param event javax.swing.event.TreeExpansionEvent
	 * @return
	 */
	public void treeExpanded(javax.swing.event.TreeExpansionEvent event) {
		javax.swing.tree.TreePath currentPath = event.getPath();
		javax.swing.tree.TreeNode currentNode = (javax.swing.tree.TreeNode) currentPath
				.getLastPathComponent();
		javax.swing.tree.TreeNode parentNode = currentNode.getParent();
		if (fieldExclusiveExpand) {
			if (parentNode != null) {
				javax.swing.tree.TreePath parentPath = currentPath
						.getParentPath();
				for (int i = 0; i < parentNode.getChildCount(); i++) {
					javax.swing.tree.TreeNode brotherNode = parentNode
							.getChildAt(i);
					javax.swing.tree.TreePath brotherPath = parentPath
							.pathByAddingChild(brotherNode);
					if (brotherNode != currentNode && isExpanded(brotherPath)) {
						this.collapsePath(brotherPath);
					}
				}
			}
		}
		
		if (fieldGradeExpand) {
			for (int i = 0; i < currentNode.getChildCount(); i++) {
				javax.swing.tree.TreeNode childNode = currentNode.getChildAt(i);
				javax.swing.tree.TreePath childPath = currentPath
						.pathByAddingChild(childNode);
				if (isExpanded(childPath)) {
					collapsePath(childPath);
				}
			}
		}
	}
	
	public static void main(String[] args){
	    JFrame frame = new JFrame("test tree");
	    frame.setSize(300,400);
	    frame.getContentPane().setLayout(new BorderLayout());
	    UFOTree tree = new UFOTree();
	    frame.getContentPane().add(tree, BorderLayout.CENTER);
	    frame.addWindowListener(new WindowAdapter(){
	        public void windowClosing(WindowEvent e) {
	            System.exit(0);
	        }
	    });
	    frame.setVisible(true);
	}
}
 