package nc.ui.iufo.unit;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.ufsoft.report.UfoTree;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.tree.TreeNodeSearcher;


public class IUFOLazyTree extends UfoTree{
	private static final long serialVersionUID = 4012851033102499504L;
	
	private ITreeDataLoader m_dataLoader=null;
	private ITreeNodeDblClickHandler m_dblClickHandler=null;
	TreeNodeSearcher m_nodeSearcher=null;

	public IUFOLazyTree(ITreeDataLoader dataLoader,ITreeNodeDblClickHandler clickHandler,ITreeDataSearcher searcher){
		super(true);
		
		m_dataLoader=dataLoader;
		m_dblClickHandler=clickHandler;
		
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (m_dblClickHandler==null || e.getClickCount()<2)
					return;
				
				IUFOLazyTree tree=IUFOLazyTree.this;
				TreePath path=tree.getPathForLocation(e.getX(), e.getY());
				if (path==null)
					return;
				
				boolean bNeedExpand=m_dblClickHandler.onDblClickTreeNode(tree,e);
				if (bNeedExpand==false)
					return;
				
				if (!tree.isCollapsed(path)){
					tree.collapsePath(path);
				}else{
					tree.expandPath(path);
				}
			}
		});
		
		if (m_dataLoader!=null){
			ITreeNodeData rootData=m_dataLoader.getRootNodeData();
			IUFOTreeNode rootNode=new IUFOTreeNode(rootData);
			
			ITreeNodeData[] datas=m_dataLoader.getSubNodeDatas(rootData);
			if (datas!=null && datas.length>0){
				for (int i=0;i<datas.length;i++){
					IUFOTreeNode subNode=new IUFOTreeNode(datas[i]);
					rootNode.add(subNode);
				}
			}
			rootNode.setHasLoad(true);
			setModel(new DefaultTreeModel(rootNode));
		}
		
		if (searcher!=null){
			// ×¢²áÊ÷½ÚµãËÑË÷Æ÷
			m_nodeSearcher = new UFOLazyTreeSearcher(this,searcher);
			m_nodeSearcher.setInputHint(NCLangRes.getInstance().getStrByID("smcomm","UPP1005-000139"))/* @res "ÊäÈëÃû³ÆËÑË÷:" */;
		}
	}
	
	public void refreshContent(){
		if (m_dataLoader!=null){
			ITreeNodeData rootData=m_dataLoader.getRootNodeData();
			IUFOTreeNode rootNode=new IUFOTreeNode(rootData);
			
			ITreeNodeData[] datas=m_dataLoader.getSubNodeDatas(rootData);
			if (datas!=null && datas.length>0){
				for (int i=0;i<datas.length;i++){
					IUFOTreeNode subNode=new IUFOTreeNode(datas[i]);
					rootNode.add(subNode);
				}
			}
			rootNode.setHasLoad(true);
			setModel(new DefaultTreeModel(rootNode));
		}
	}
	
	public void setSelectedTreeNode(String[] strSelNodePKs){
		if (strSelNodePKs==null || strSelNodePKs.length<=0)
			return;
		
		DefaultTreeModel model=(DefaultTreeModel)getModel();
		IUFOTreeNode root=(IUFOTreeNode)model.getRoot();
		TreeNode node=findSelectedTreeNode(new IUFOTreeNode[]{root},strSelNodePKs,0);
		if (node!=null){
			TreePath path=new TreePath(((DefaultTreeModel)getModel()).getPathToRoot(node));
			setSelectionPath(path);
			scrollPathToVisible(path);
		}
	}
	
	private TreeNode findSelectedTreeNode(IUFOTreeNode[] parentNodes,String[] strSelNodePKs,int iPos){
		if (parentNodes==null || parentNodes.length<=0)
			return null;
		
		for (int i=0;i<parentNodes.length;i++){
			if (parentNodes[i].getDataObj().getPK().equals(strSelNodePKs[iPos])==false)
				continue;
			
			if (iPos==strSelNodePKs.length-1)
				return parentNodes[i];
			
			doDynLoadTreeNodes(parentNodes[i]);
			
			int iSubCount=parentNodes[i].getChildCount();
			IUFOTreeNode[] subNodes=new IUFOTreeNode[iSubCount];
			for (int j=0;j<iSubCount;j++)
				subNodes[j]=(IUFOTreeNode)parentNodes[i].getChildAt(j);
				
			return findSelectedTreeNode(subNodes,strSelNodePKs,++iPos);
		}
		return null;
	}
	
	public void treeExpanded(javax.swing.event.TreeExpansionEvent event) {
		if (m_dataLoader==null)
			return;
		
		TreePath path=event.getPath();
		IUFOTreeNode parentNode = (IUFOTreeNode)path.getLastPathComponent();
		doDynLoadTreeNodes(parentNode);
		
		if(isCollapsed(path)){
			expandPath(path);
		}
	}
	
    public int getToggleClickCount() {
    	if (m_dblClickHandler==null)
    		return 2;
    	return 6;
    }
	
	private void doDynLoadTreeNodes(IUFOTreeNode parentNode){
		if (parentNode.isLeaf()==false && parentNode.isHasLoad()==false){
			ITreeNodeData[] datas=m_dataLoader.getSubNodeDatas(parentNode.getDataObj());
			if (datas!=null && datas.length>0){
				for (int i=0;i<datas.length;i++){
					IUFOTreeNode subNode=new IUFOTreeNode(datas[i]);
					((DefaultTreeModel) getModel()).insertNodeInto(subNode,parentNode,parentNode.getChildCount());
				}
			}
			parentNode.setHasLoad(true);
		}
	}
}
