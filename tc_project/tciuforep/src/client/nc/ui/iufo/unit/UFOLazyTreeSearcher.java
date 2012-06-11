package nc.ui.iufo.unit;

import com.ufsoft.report.UfoTree;

import nc.ui.pub.tree.TreeNodeSearcher;


public class UFOLazyTreeSearcher extends TreeNodeSearcher {
	private String m_strCurNodeId=null;
	private UfoTree m_tree=null;
	ITreeDataSearcher m_searcher=null;
	public UFOLazyTreeSearcher(UfoTree tree,ITreeDataSearcher searcher) {
		super(tree);
		m_tree=tree;
		m_searcher=searcher;
	}
	
	public UFOLazyTreeSearcher(UfoTree tree, boolean bNeedEnter,ITreeDataSearcher searcher) {
		super(tree,bNeedEnter);
		m_tree=tree;
		m_searcher=searcher;
	}

	protected Object[] loadSearchContent() {
		return new Object[100000];
	}
	
	protected void locationTree() {
		m_strCurNodeId=null;
		super.locationTree();
	}

	/**
	 * 提供给外接手动搜索树上的节点
	 * @param text
	 */
	public void locationTree(String text) {
		m_strCurNodeId=null;
		super.locationTree(text);
	}

	public boolean location(String text, int direction) {
		if (text==null || text.trim().length()<=0)
			return false;
		
		boolean bDown=true;
		if (direction==DIRECTION_UP)
			bDown=false;
		
		String[] strNodePKs=m_searcher.searchNodePaths(text, m_strCurNodeId, bDown);
		if (strNodePKs!=null && strNodePKs.length>0){
			m_strCurNodeId=strNodePKs[strNodePKs.length-1];
			m_tree.setSelectedTreeNode(strNodePKs);
			return true;
		}
		return false;
	}
}
