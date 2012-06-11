package com.ufsoft.iufo.view.tree;

import java.util.Comparator;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import nc.util.iufo.pub.UFOString;
/**
 * 排序的树model
 * <p>
 * 排序方案：按目录、节点分组排序,目录在前。
 * 
 * @author zhaopq
 * 
 */
public class SortTreeModel extends DefaultTreeModel{
	
	private static final long serialVersionUID = -3562886248087981261L;
	
	private Comparator<TreeNode> comparator = new Comparator<TreeNode>() {
		public int compare(TreeNode node1, TreeNode node2) {
			return UFOString.compareHZString(node1.toString(), node2.toString());
		}

	};
	
	public SortTreeModel(TreeNode root){
		super(root);
	}

	@Override
	public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent, int index) {
		index = findIndexFor(newChild, parent);
		super.insertNodeInto(newChild, parent, index);
	}

	private int findIndexFor(MutableTreeNode child, MutableTreeNode parent) {
		int childCount = parent.getChildCount();
		if (childCount == 0) {
			return 0;
		}
		
		int firstLeafIndex = getFirstLeafIndex(parent,0,childCount-1);
		
		if(child.isLeaf()){
			if(firstLeafIndex==-1){
				return childCount;
			}
			return findIndexFor(child, parent, firstLeafIndex, childCount - 1);
		}else{
			return findIndexFor(child, parent, 0, firstLeafIndex==-1?childCount-1:firstLeafIndex);
		}
	}
	

	private int findIndexFor(MutableTreeNode child, MutableTreeNode parent,
			int i1, int i2) {
		if (i1 == i2) {
			return comparator.compare(child, parent.getChildAt(i1)) <= 0 ? i1
					: i1 + 1;
		}
		int half = (i1 + i2) / 2;
		if (comparator.compare(child, parent.getChildAt(half)) <= 0) {
			return findIndexFor(child, parent, i1, half);
		}
		return findIndexFor(child, parent, half + 1, i2);
	}

	
	
	/**
	 * 查找第一个叶子节点的位置
	 * <p>算法：折半查找
	 * @create by zhaopq at 2009-5-14,下午04:35:52
	 *
	 * @param parent  父节点
	 * @param from    查找的开始位置
	 * @param to      查找的结束位置
	 * @return   -1 表示没有查到
	 */
	private int getFirstLeafIndex(MutableTreeNode parent,int from,int to){
		if(from==to){
			if(parent.getChildAt(from).isLeaf()){
				return from;
			}else{
				return -1;
			}
		}
		
		int half = (from + to) / 2;
		if (parent.getChildAt(half).isLeaf()) {
			return getFirstLeafIndex(parent,from,half);
		}
		return getFirstLeafIndex(parent, half+1, to);
	}

	Comparator<TreeNode> getComparator() {
		return comparator;
	}

	void setComparator(Comparator<TreeNode> comparator) {
		this.comparator = comparator;
	}

}
