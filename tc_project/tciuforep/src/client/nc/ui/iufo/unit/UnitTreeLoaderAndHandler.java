package nc.ui.iufo.unit;

import java.awt.event.MouseEvent;

import javax.swing.JTree;

import com.ufida.zior.console.ActionHandler;

public class UnitTreeLoaderAndHandler implements ITreeDataLoader,ITreeNodeDblClickHandler,ITreeDataSearcher{
	private String m_strNodeType=null;
	private String m_strRootUnitPK=null;
	private String m_strOrgPK=null;
	
	public UnitTreeLoaderAndHandler(String strNodeType,String strRootUnitPK,String strOrgPK){
		m_strNodeType=strNodeType;
		m_strRootUnitPK=strRootUnitPK;
		m_strOrgPK=strOrgPK;
	}
	
	public String[] searchNodePaths(String strText, String strOldPK,boolean bDown) {
		Object response = ActionHandler.exec(
				"nc.ui.iufo.unit.UnitActionHandler", "searchNodePaths",
				new Object[]{m_strRootUnitPK,strText,strOldPK,Boolean.valueOf(bDown),m_strOrgPK});
		return (String[])response;
	}

	public ITreeNodeData getRootNodeData() {
		Object response = ActionHandler.exec(
				"nc.ui.iufo.unit.UnitActionHandler", "getRootNodeData",
				new String[]{m_strNodeType,m_strRootUnitPK,m_strOrgPK});
		return (ITreeNodeData)response;
	}

	public ITreeNodeData[] getSubNodeDatas(ITreeNodeData parentData) {
		Object response = ActionHandler.exec(
				"nc.ui.iufo.unit.UnitActionHandler", "getSubNodeDatas",
				new Object[]{m_strNodeType,parentData,m_strOrgPK});
		return (ITreeNodeData[])response;
	}

	public boolean onDblClickTreeNode(JTree tree, MouseEvent event) {
		return true;
	}

}
