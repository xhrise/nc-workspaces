package com.ufsoft.iufo.inputplugin.biz.file;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;

/**
 * 报表树模型类
 * 根据当前任务设置，构造包含该任务下当前用户可操作的全部报表的树模型
 * chxw
 * 2007-9-6
 */
public class RepTreeModel {
	/**
	 * 树模型
	 */
	private DefaultTreeModel treeModel = null;
	
	/**
	 * 报表集合
	 */
	private ChooseRepData[] m_reps = null;
	
	/**
	 * 是否包含可用报表
	 */
	private boolean m_hasReps = true; 
	
	/**
	 * RepTreeModel 构造函数
	 * @param strUnitID   单位ID
	 * @param nNodeIDType 
	 * @param bLoadLeaf	  是否加载末级单位
	 */
	public RepTreeModel(ChooseRepData[] repDatas){
		m_reps = repDatas;
		createTreeModel();
	}
	
	/**
	 * 创建报表树模型
	 * @return
	 */
	private DefaultTreeModel createTreeModel(){
		treeModel = new DefaultTreeModel(new DefaultMutableTreeNode(
				MultiLangInput.getString("uiufotableinput0015")));
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) (treeModel.getRoot());
		root.removeAllChildren();
		
		if(m_reps != null){
			for (int i = 0; i < m_reps.length; i++) {
				root.add(new DefaultMutableTreeNode(m_reps[i]));
			}
		}
		return new DefaultTreeModel(root, false);
	}
	
	/**
	 * 报表树模型，如未成功初始化，则返回为NULL
	 * @return
	 */
	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	public boolean hasReps() {
		if(m_reps == null){
			return false;
		}
		return true;
	}

	public void setHasReps(boolean hasReps) {
		this.m_hasReps = hasReps;
	}

	public ChooseRepData[] getReports() {
		return m_reps;
	}
	
}
