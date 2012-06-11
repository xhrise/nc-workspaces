package nc.ui.iufo.unit;

import javax.swing.tree.DefaultMutableTreeNode;

public class IUFOTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = -1513279250852035748L;

	private boolean m_bHasLoad;
	private ITreeNodeData m_objData=null;
	
	public IUFOTreeNode(ITreeNodeData data){
		setDataObj(data);
	}
	
	public boolean isHasLoad() {
		return m_bHasLoad;
	}
	
	public void setHasLoad(boolean hasLoad) {
		m_bHasLoad = hasLoad;
	}
	
	public boolean isLeaf() {
		return m_objData.isLeaf();
	}

	public ITreeNodeData getDataObj() {
		return m_objData;
	}

	public void setDataObj(ITreeNodeData data) {
		m_objData = data;
		setUserObject(data);
	}
}
