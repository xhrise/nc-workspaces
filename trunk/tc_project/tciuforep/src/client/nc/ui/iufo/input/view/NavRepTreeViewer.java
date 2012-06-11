package nc.ui.iufo.input.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.ui.iufo.input.TaskRepTreeNodeData;
import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.pub.UfoPublic;
import nc.ui.pub.beans.UITree;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.docking.view.ToggleButton;
import com.ufida.zior.docking.view.actions.TitleAction;
import com.ufida.zior.util.ResourceManager;
import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.Viewer;
import com.ufida.zior.view.impl.TreeViewer;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;

public class NavRepTreeViewer extends TreeViewer{
	private static final long serialVersionUID = 8232011838671032093L;
	private UITree m_repTree=null;
	
	public void shutdown() {
	}

	public void startup() {
		super.startup();
		
		addTitleAction(new TitleAction(){
			public void execute(Viewer view, ActionEvent e) {
				RepDataControler controler=RepDataControler.getInstance(getMainboard());
				controler.setShowSealedTask(!controler.isShowSealedTask());
				refresh();
			}

			public KeyStroke getAccelerator() {
				return null;
			}

			public Icon getIcon() {
				RepDataControler controler=RepDataControler.getInstance(getMainboard());
				if (controler.isShowSealedTask())
					return ResConst.getImageIcon("reportcore/sealed_Task.gif");
				else
					return ResConst.getImageIcon("reportcore/sealed_Task.gif");
			}

			/**
			 * @i18n miufohbbb00207=显示封存任务
			 */
			public String getName() {
				return StringResource.getStringResource("miufohbbb00207");
			}

			/**
			 * @i18n miufohbbb00208=不显示封存任务
			 */
			@Override
			public String getTooltipSelected() {
				return StringResource.getStringResource("miufohbbb00208");
			}

			@Override
			public Component getComponent(Action action) {
				return new ToggleButton(action);
			}
		},2);
	}
	
	/**
	 * 根据单位刷新报表树内容
	 */
	public void refresh(){
		try{
			TreeNode[] selNodes=new TreeNode[1];
			m_repTree.setModel(getRepTreeModel(selNodes));
			m_repTree.getTreeNodeSearcher().refreshSearchContent();
			if (selNodes[0]!=null){
				m_repTree.setSelectionPath(new TreePath(((DefaultTreeModel)m_repTree.getModel()).getPathToRoot(selNodes[0])));
			}
		}catch(Exception e){
			AppDebug.debug(e);
		}
	}
	
	public void selectRepTreeNode(){
		RepDataControler controler=RepDataControler.getInstance(getMainboard());
		if (isLinkable() && controler.getSelectedRepPK()!=null){
			try{
				TreeModel model=m_repTree.getModel();
				DefaultMutableTreeNode root=(DefaultMutableTreeNode)model.getRoot();
				int iCount=root.getChildCount();
				for (int i=0;i<iCount;i++){
					DefaultMutableTreeNode node=(DefaultMutableTreeNode)root.getChildAt(i);
					TaskRepTreeNodeData data=(TaskRepTreeNodeData)node.getUserObject();
					if (!data.getTaskPK().equals(controler.getSelectedTaskPK()))
						continue;
					
					int iSubCount=node.getChildCount();
					for (int j=0;j<iSubCount;j++){
						DefaultMutableTreeNode subNode=(DefaultMutableTreeNode)node.getChildAt(j);
						if (((TaskRepTreeNodeData)subNode.getUserObject()).getRepPK().equals(controler.getSelectedRepPK())){
							m_repTree.setSelectionPath(new TreePath(((DefaultTreeModel)m_repTree.getModel()).getPathToRoot(subNode)));
							return;
						}
					}
				}
			}catch(Exception e){
			}
		}
	}

	public JTree createTree(){
		if (m_repTree!=null)
			return m_repTree;
		
		m_repTree=new UITree(true);
		try{
			TreeNode[] selNodes=new TreeNode[1];
			m_repTree.setModel(getRepTreeModel(selNodes));
			if (selNodes[0]!=null){
				m_repTree.setSelectionPath(new TreePath(((DefaultTreeModel)m_repTree.getModel()).getPathToRoot(selNodes[0])));
			}
		}catch(Exception e){
			AppDebug.debug(e);
		}
		
		m_repTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		m_repTree.addTreeSelectionListener(new TreeSelectionListener(){
			//选中树上节点的响应方法
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path=e.getNewLeadSelectionPath();
				
				if (path==null)
					return;
				
				TaskRepTreeNodeData nodeData=(TaskRepTreeNodeData)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
				String strTaskPK=nodeData.getTaskPK();
				String strRepPK=nodeData.getRepPK();
				
				//设置当前选中的任务、报表
				RepDataControler controler=RepDataControler.getInstance(getMainboard());
				controler.setSelectedTaskPK(strTaskPK);
				controler.setSelectedRepPK(strRepPK);
				
				//如果选中的是任务节点，则设为未有效选中报表，此时双击单位树不能打开报表
				if (strRepPK==null){
					controler.setRepValidSelect(true);
				}else{
					controler.setRepValidSelect(true);
				}
			}
		});
		
		m_repTree.setCellRenderer(new DefaultTreeCellRenderer(){

			private static final long serialVersionUID = 1L;

			@Override
			public Icon getLeafIcon() {
				return ResourceManager.createIcon("/images/reportcore/report.gif");
			}

			@Override
			public Icon getClosedIcon() {
				return ResourceManager.createIcon("/images/reportcore/task_report.gif");
			}

			@Override
			public Icon getOpenIcon() {
				return getClosedIcon();
			}
			
			
		});
		
		m_repTree.addMouseListener(new MouseAdapter(){
			//报表树节点双击响应方法
			/**
			 * @i18n miufohbbb00127=请选择单位，选择单位后才能打开报表数据界面
			 * @i18n miufohbbb00125=打开报表失败
			 */
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()<2)
					return;
				
				TreePath path=m_repTree.getPathForLocation(e.getX(),e.getY());
				if (path==null)
					return;
				
				RepDataControler controler=RepDataControler.getInstance(getMainboard());
				
				TaskRepTreeNodeData nodeData=(TaskRepTreeNodeData)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
				//如果双击的是任务节点，不作处理，直接退出
				if (nodeData==null || nodeData.getRepPK()==null)
					return;
				
				//单击节点时，已经设置过当前选中的任务、报表，如果此处得到的值与单击节点时不一致，说明程序处理有问题，直接退出
				if (nodeData.getTaskPK().equals(controler.getSelectedTaskPK())==false || nodeData.getRepPK().equals(controler.getSelectedRepPK())==false)
					return;
				
				dblClickTreeNode();
			}
			
		});
		return m_repTree;
	}
	
	/**
	 * 得到报表树Model
	 * @param selNodes,默认选中的树节点的节点路径
	 * @return
	 * @throws Exception
	 * @i18n miufohbbb00128=任务-报表
	 */
	private TreeModel getRepTreeModel(TreeNode[] selNodes) throws Exception{
		DefaultMutableTreeNode root=new DefaultMutableTreeNode(new TaskRepTreeNodeData(null,null,StringResource.getStringResource("miufohbbb00128")));
		TreeModel model=new DefaultTreeModel(root);
		
		Mainboard mainBoard=getMainboard();
		RepDataControler controler=RepDataControler.getInstance(mainBoard);
		
		TaskRepTreeNodeData[] nodeDatas=(TaskRepTreeNodeData[])ActionHandler.exec("nc.ui.iufo.input.RepDataActionHandler", "loadTaskRepTreeNodeDatas",
				new Object[]{controler.getCurUserInfo(mainBoard),controler.getSelectedUnitPK(),controler.isHBBBData(mainBoard),controler.isShowSealedTask(),controler.isGeneralQuery(mainBoard)});
		
		TreeNode selTreeNode=null;
        if (nodeDatas!=null){
        	DefaultMutableTreeNode taskNode=null;
        		
        	for (int i=0;i<nodeDatas.length;i++){
        		DefaultMutableTreeNode node=new DefaultMutableTreeNode(nodeDatas[i]);
        		if (nodeDatas[i].getRepPK()==null){
        			root.add(node);
        			taskNode=node;
        		}else{
        			taskNode.add(node);
        			if (UfoPublic.strIsEqual(nodeDatas[i].getTaskPK(),controler.getSelectedTaskPK()) 
        					&& UfoPublic.strIsEqual(nodeDatas[i].getRepPK(),controler.getSelectedRepPK())){
        				selTreeNode=node;
        			}
        		}
        		
        		
        	}
        }
        
        if(controler.getSelectedTaskPK() == null || controler.getSelectedRepPK() == null){
        	selTreeNode = root;
        }
        RepDataControler.getInstance(getMainboard()).setRepValidSelect(controler.getSelectedRepPK()!=null);
        selNodes[0]=selTreeNode;
        	
		return model;
	}

	@Override
	protected void doCollapseTree() {
		DefaultMutableTreeNode node=(DefaultMutableTreeNode)getTree().getModel().getRoot();
		NavUnitTreeViewer.collapseTreeNode(getTree(),node,true);
		
	}

	@Override
	protected void doLinkWithViewer(Viewer currentActiveView) {
		selectRepTreeNode();
	}

	protected void dblClickTreeNode() {
		RepDataControler controler=RepDataControler.getInstance(getMainboard());
		if (controler.getSelectedRepPK()==null || controler.getSelectedTaskPK()==null)
			return;
		
		String strSelectedRepPK=controler.getSelectedRepPK();
		String strSelectedTaskPK=controler.getSelectedTaskPK();
		
		//如果未选中当前单位，给出出错提示
		if (controler.getSelectedUnitPK()==null){
			JOptionPane.showMessageDialog(NavRepTreeViewer.this.getMainboard(), MultiLang.getString(StringResource.getStringResource("miufohbbb00127")));
			return;
		}
		
		try{
			//打开报表数据面板
			controler.doOpenRepEditWin(getMainboard(),false);
			
			//在打开报表数据面板过程中，可能改变了报表树中选中的节点，此处确保报表树中选中的节点为原节点
			controler.setSelectedTaskPK(strSelectedTaskPK);
			controler.setSelectedRepPK(strSelectedRepPK);
			selectRepTreeNode();
		}catch(Exception te){
			AppDebug.debug(te);
			JOptionPane.showMessageDialog(NavRepTreeViewer.this.getMainboard(), MultiLang.getString(StringResource.getStringResource("miufohbbb00125")));
		}
	}
}
  