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
			 * @i18n miufohbbb00207=��ʾ�������
			 */
			public String getName() {
				return StringResource.getStringResource("miufohbbb00207");
			}

			/**
			 * @i18n miufohbbb00208=����ʾ�������
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
	 * ���ݵ�λˢ�±���������
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
			//ѡ�����Ͻڵ����Ӧ����
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path=e.getNewLeadSelectionPath();
				
				if (path==null)
					return;
				
				TaskRepTreeNodeData nodeData=(TaskRepTreeNodeData)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
				String strTaskPK=nodeData.getTaskPK();
				String strRepPK=nodeData.getRepPK();
				
				//���õ�ǰѡ�е����񡢱���
				RepDataControler controler=RepDataControler.getInstance(getMainboard());
				controler.setSelectedTaskPK(strTaskPK);
				controler.setSelectedRepPK(strRepPK);
				
				//���ѡ�е�������ڵ㣬����Ϊδ��Чѡ�б�����ʱ˫����λ�����ܴ򿪱���
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
			//�������ڵ�˫����Ӧ����
			/**
			 * @i18n miufohbbb00127=��ѡ��λ��ѡ��λ����ܴ򿪱������ݽ���
			 * @i18n miufohbbb00125=�򿪱���ʧ��
			 */
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()<2)
					return;
				
				TreePath path=m_repTree.getPathForLocation(e.getX(),e.getY());
				if (path==null)
					return;
				
				RepDataControler controler=RepDataControler.getInstance(getMainboard());
				
				TaskRepTreeNodeData nodeData=(TaskRepTreeNodeData)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
				//���˫����������ڵ㣬��������ֱ���˳�
				if (nodeData==null || nodeData.getRepPK()==null)
					return;
				
				//�����ڵ�ʱ���Ѿ����ù���ǰѡ�е����񡢱�������˴��õ���ֵ�뵥���ڵ�ʱ��һ�£�˵�������������⣬ֱ���˳�
				if (nodeData.getTaskPK().equals(controler.getSelectedTaskPK())==false || nodeData.getRepPK().equals(controler.getSelectedRepPK())==false)
					return;
				
				dblClickTreeNode();
			}
			
		});
		return m_repTree;
	}
	
	/**
	 * �õ�������Model
	 * @param selNodes,Ĭ��ѡ�е����ڵ�Ľڵ�·��
	 * @return
	 * @throws Exception
	 * @i18n miufohbbb00128=����-����
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
		
		//���δѡ�е�ǰ��λ������������ʾ
		if (controler.getSelectedUnitPK()==null){
			JOptionPane.showMessageDialog(NavRepTreeViewer.this.getMainboard(), MultiLang.getString(StringResource.getStringResource("miufohbbb00127")));
			return;
		}
		
		try{
			//�򿪱����������
			controler.doOpenRepEditWin(getMainboard(),false);
			
			//�ڴ򿪱��������������У����ܸı��˱�������ѡ�еĽڵ㣬�˴�ȷ����������ѡ�еĽڵ�Ϊԭ�ڵ�
			controler.setSelectedTaskPK(strSelectedTaskPK);
			controler.setSelectedRepPK(strSelectedRepPK);
			selectRepTreeNode();
		}catch(Exception te){
			AppDebug.debug(te);
			JOptionPane.showMessageDialog(NavRepTreeViewer.this.getMainboard(), MultiLang.getString(StringResource.getStringResource("miufohbbb00125")));
		}
	}
}
  