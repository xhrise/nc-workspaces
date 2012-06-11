package nc.ui.iufo.input.view;

import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.unit.IUFOLazyTree;
import nc.ui.iufo.unit.IUFOTreeNode;
import nc.ui.iufo.unit.IUnitTreeNodeType;
import nc.ui.iufo.unit.UnitTreeLoaderAndHandler;
import nc.ui.iufo.unit.UnitTreeNodeData;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.util.ResourceManager;
import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.Viewer;
import com.ufida.zior.view.impl.TreeViewer;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.util.MultiLang;

/**
 * ������λ�����
 * @author weixl
 *
 */
public class NavUnitTreeViewer extends TreeViewer{

	private static final long serialVersionUID = 8232011838671032093L;
	private IUFOLazyTree m_unitTree=null;
	private MyTreeHandler m_treeHandler=null;
	
	public void shutdown() {
	}

	public void startup() {
		super.startup();
	}
	
	public void refresh() {
		m_unitTree.refreshContent();
		selectUnitTreeNode();
	}
	
	@Override
	protected JTree createTree() {
		if (m_unitTree!=null)
			return m_unitTree;
		
		final Mainboard mainBoard=getMainboard();
		RepDataControler controler=RepDataControler.getInstance(mainBoard);
		m_treeHandler=new MyTreeHandler(controler.getCurUserInfo(mainBoard).getUnitId(),(String)getMainboard().getContext().getAttribute(IUfoContextKey.ORG_PK));
		m_unitTree=new IUFOLazyTree(m_treeHandler,m_treeHandler,m_treeHandler);
		m_unitTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		m_unitTree.addTreeSelectionListener(new TreeSelectionListener(){
			//��λ���ڵ�ĵ�����Ӧ����
			public void valueChanged(TreeSelectionEvent e) {
				RepDataControler controler=RepDataControler.getInstance(getMainboard());
				TreePath path=e.getNewLeadSelectionPath();
				if (path!=null){
					IUFOTreeNode treeNode=(IUFOTreeNode)path.getLastPathComponent();
					String strUnitPK=((UnitTreeNodeData)treeNode.getDataObj()).getUnitPK();
					//���ѡ�еĵ�λ�����˱仯��ˢ�±�������Χ
					if (!strUnitPK.equals(controler.getSelectedUnitPK())){
						controler.setSelectedUnitPK(strUnitPK);
						controler.refreshNavRepTree(mainBoard);
					}
				}
			}
		});
		
		//������Ĭ��ѡ�еĵ�λ
		selectUnitTreeNode();
		
		m_unitTree.setCellRenderer(new DefaultTreeCellRenderer(){

			private static final long serialVersionUID = 1L;

			@Override
			public Icon getLeafIcon() {
				return ResourceManager.createIcon("/images/reportcore/report.gif");
			}

			@Override
			public Icon getClosedIcon() {
				return ResourceManager.createIcon("/images/reportcore/unit_info.gif");
			}

			@Override
			public Icon getOpenIcon() {
				return getClosedIcon();
			}
			
			
		});
		
		return m_unitTree;
	}

	/**
	 * ����ѡ�еĵ�λ���Ľڵ㣬���ö�̬���ṩ�ķ���
	 */
	public void selectUnitTreeNode(){
		Mainboard mainBoard=getMainboard();
		RepDataControler controler=RepDataControler.getInstance(mainBoard);
		if (isLinkable() && controler.getSelectedUnitPK()!=null){
			try{
				String[] strUnitPKs=(String[])ActionHandler.exec("nc.ui.iufo.unit.UnitActionHandler", "getAllParentUnitPKs",
						new Object[]{controler.getCurUserInfo(mainBoard).getUnitId(),controler.getSelectedUnitPK(),mainBoard.getContext().getAttribute(IUfoContextKey.ORG_PK)});
				m_unitTree.setSelectedTreeNode(strUnitPKs);
			}catch(Exception e){
			}
		}
	}
	
	/**
	 * ��̬����ʵ�ֽӿ�,��չ�Ե�λ��̬���ӿڣ����Ӷ�˫�����ڵ�򿪱������ݵĴ���
	 * @author weixl
	 *
	 */
	class MyTreeHandler extends UnitTreeLoaderAndHandler{
		public MyTreeHandler(String strRootUnitPK,String strOrgPK){
			super(IUnitTreeNodeType.TYPE_ID_UNITID,strRootUnitPK,strOrgPK);
		}
		
		/**
		 * @i18n miufohbbb00130=��ѡ�񱨱�ѡ�񱨱����ܴ򿪱������ݽ���
		 * @i18n miufohbbb00125=�򿪱���ʧ��
		 */
		public boolean onDblClickTreeNode(JTree tree, MouseEvent event) {
			RepDataControler controler=RepDataControler.getInstance(getMainboard());
			String strUnitPK=controler.getSelectedUnitPK();
			if (strUnitPK==null)
				return false;
			
			//δѡ�񱨱��򱨱���Чʱ������������ʾ
			if (controler.getSelectedRepPK()==null || controler.isRepValidSelect()==false){
				JOptionPane.showMessageDialog(NavUnitTreeViewer.this.getMainboard(), MultiLang.getString(StringResource.getStringResource("miufohbbb00130")));
				return false;
			}
			
			try{
				//�򿪱����������
				controler.doOpenRepEditWin(getMainboard(),false);
				
				//�ڴ򿪱��������������У����ܸı��˵�λ����ѡ�еĽڵ㣬�˴�ȷ����λ����ѡ�еĽڵ�Ϊԭ�ڵ�
				if (strUnitPK!=null){
					controler.setSelectedUnitPK(strUnitPK);
					selectUnitTreeNode();
				}
			}catch(Exception te){
				AppDebug.debug(te);
				JOptionPane.showMessageDialog(NavUnitTreeViewer.this.getMainboard(), MultiLang.getString(StringResource.getStringResource("miufohbbb00125")));
			}
			return false;
		}
	}

	@Override
	protected void doCollapseTree() {
		DefaultMutableTreeNode node=(DefaultMutableTreeNode)getTree().getModel().getRoot();
		collapseTreeNode(getTree(),node,true);
	}

	@Override
	protected void doLinkWithViewer(Viewer currentActiveView) {
		selectUnitTreeNode();
	}
	
	protected void dblClickTreeNode() {
		m_treeHandler.onDblClickTreeNode(m_unitTree, null);
	}
	
	/**
	 * �ݹ�����ȫ�����ڵ�ķ�����ֻ�����ڶ�������һ�������˵���ʾ
	 * @param tree
	 * @param node
	 * @param bRootNode
	 */
	public static void collapseTreeNode(JTree tree,DefaultMutableTreeNode node,boolean bRootNode){
		int iCount=node.getChildCount();
		for (int i=0;i<iCount;i++){
			DefaultMutableTreeNode subNode=(DefaultMutableTreeNode)node.getChildAt(i);
			if (subNode.getChildCount()>0)
				collapseTreeNode(tree,subNode,false);
		}
		if (!bRootNode)
			tree.collapsePath(new TreePath(((DefaultTreeModel)tree.getModel()).getPathToRoot(node)));
	}
}
 