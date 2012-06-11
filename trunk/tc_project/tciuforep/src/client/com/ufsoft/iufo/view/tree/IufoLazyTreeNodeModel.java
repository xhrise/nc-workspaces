package com.ufsoft.iufo.view.tree;

import java.util.ArrayList;
import java.util.List;

public class IufoLazyTreeNodeModel {
	
	private String id;
	
	private String parentId;
	
	/** �ڵ���ʾ����*/
	private String label;

	/** �ӽڵ����� */
	private IufoLazyTreeNodeModel[] subNodeDatas;
	
	/** �����ӽڵ�Ĳ���*/
	private Object[] loadSubNodeParams;

	/** �ӽڵ����ݵļ����� */
	private TreeDataLoader dataLoader;

	private boolean hasLoadSubNodeData;

	/** �Ƿ���Ҷ�ӽڵ� */
	private boolean leaf;
	
	/** ��ԴȨ��*/
	private int resRight = -1;

	/** ����¼�������*/
	private MouseEventHandler mouseEventHandler = new MouseEventHandlerAdapter();

	public MouseEventHandler getMouseEventHandler() {
		return mouseEventHandler;
	}

	public void setMouseEventHandler(MouseEventHandler mouseEventHandler) {
		this.mouseEventHandler = mouseEventHandler;
	}


	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public IufoLazyTreeNodeModel[] getSubNodeDatas() {
		if (!hasLoadSubNodeData && !isLeaf()) {
			if (dataLoader != null) {
				subNodeDatas = handleData(dataLoader.loadData(loadSubNodeParams));
				hasLoadSubNodeData = true;
			}
		}
		return subNodeDatas;
	}

	private IufoLazyTreeNodeModel[] handleData(IufoLazyTreeNodeModel[] datas) {
		if(datas == null){
			return null;
		}
		List<IufoLazyTreeNodeModel> result = new ArrayList<IufoLazyTreeNodeModel>();
		// ���˵�null
		for (IufoLazyTreeNodeModel data : datas) {
			if (data != null) {
				result.add(data);
			}
		}
		return result.toArray(new IufoLazyTreeNodeModel[result.size()]);
	}

	public void clearSubNodeData() {
		hasLoadSubNodeData = false;
		subNodeDatas = null;
	}

	public TreeDataLoader getDataLoader() {
		return dataLoader;
	}

	public void setDataLoader(TreeDataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public void doDoubleMouseClicked() {

	}

	public Object[] getLoadSubNodeParams() {
		return loadSubNodeParams;
	}

	public void setLoadSubNodeParams(Object[] loadSubNodeParams) {
		this.loadSubNodeParams = loadSubNodeParams;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getResRight() {
		return resRight;
	}

	public void setResRight(int resRight) {
		this.resRight = resRight;
	}

}
