package com.ufsoft.iufo.inputplugin.biz.file;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;

/**
 * ������ģ����
 * ���ݵ�ǰ�������ã���������������µ�ǰ�û��ɲ�����ȫ���������ģ��
 * chxw
 * 2007-9-6
 */
public class RepTreeModel {
	/**
	 * ��ģ��
	 */
	private DefaultTreeModel treeModel = null;
	
	/**
	 * ������
	 */
	private ChooseRepData[] m_reps = null;
	
	/**
	 * �Ƿ�������ñ���
	 */
	private boolean m_hasReps = true; 
	
	/**
	 * RepTreeModel ���캯��
	 * @param strUnitID   ��λID
	 * @param nNodeIDType 
	 * @param bLoadLeaf	  �Ƿ����ĩ����λ
	 */
	public RepTreeModel(ChooseRepData[] repDatas){
		m_reps = repDatas;
		createTreeModel();
	}
	
	/**
	 * ����������ģ��
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
	 * ������ģ�ͣ���δ�ɹ���ʼ�����򷵻�ΪNULL
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
