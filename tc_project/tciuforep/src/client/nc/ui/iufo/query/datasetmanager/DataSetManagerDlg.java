package nc.ui.iufo.query.datasetmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import nc.ui.iufo.query.datasetmanager.exts.DataSetDefDescriptor;
import nc.ui.iufo.query.datasetmanager.exts.DataSetDefPlugin;
import nc.ui.iufo.query.datasetmanager.exts.DataSetEditDescriptor;
import nc.ui.iufo.query.datasetmanager.exts.DataSetEditPlugin;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.style.Style;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportToolBarPanel;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.INavigationExt;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.plugin.IStatusBarExt;
import com.ufsoft.report.util.MultiLang;

public class DataSetManagerDlg extends UIDialog implements KeyListener{

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel m_ToolbarPanel = null;

	private JPanel jStatusPanel = null;

	private JPanel jCenterPanel = null;

	private JSplitPane jSplitPane = null;

	private JPanel m_treePanel = null;

	private UITree datasetDirTree = null;
	
	private JPanel jPanel = null;

	private UIScrollPane jScrollPane = null;
	//
	private UITable jTable = null;

	private String ownerID = null;
	private DataSetManagerMenuBar m_MenuBar;
	private Container contianer;
	private DataSetManager datasetManager;
	private ArrayList<IActionExt> actionExts = new ArrayList<IActionExt>();

	/**
	 * @param owner
	 */
	@SuppressWarnings("deprecation")
	public DataSetManagerDlg(Container contianer,String ownerID) {
		super(contianer);
		this.contianer = contianer;
		this.ownerID = ownerID;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n miufo00389=���ݼ�����
	 */
	private void initialize() {
		this.setSize(800, 600);
		this.setBackground(Color.white);
		this.setTitle(StringResource.getStringResource("miufo00389"));		
		
		datasetManager = new DataSetManager();
		//yza+ 2008-6-16 ����˽������ӵ����ID
		datasetManager.setOwnerID(this.ownerID);
		
		this.setContentPane(getJContentPane());		
		DataSetDefPlugin plugin = new DataSetDefPlugin(this.contianer);
		DataSetEditPlugin plugin2 = new DataSetEditPlugin(datasetManager,this.contianer);
		addPlugin(plugin);
		addPlugin(plugin2);
		
		initStyle();
	}
	
	private void addPlugin(IPlugIn plugin)
	{
		IPluginDescriptor des = plugin.getDescriptor();
		plugin.startup();
		IExtension[] exts = des.getExtensions();
		if (exts != null) {
			IExtension ext = null;
			for (int j = 0; j < exts.length; j++) {
				ext = exts[j];
				if (ext instanceof IStatusBarExt) {
					//Component comp = addStatusBar(ext);// ע�������״̬��
					//mngUIComp(piName, new Component[]{comp});					
				}//else 
				if (ext instanceof IActionExt) {
					//Component[] comps = 
					addActionExt((IActionExt) ext);
					actionExts.add((IActionExt) ext); //�����б�
					//mngUIComp(piName, comps);					
				}//  else 
				if (ext instanceof INavigationExt) {
					//addNavPanel(ext);// ע�ᵼ����
				}
			}
		}
	}
	
	public void keyPressed(KeyEvent e){
		
		boolean bCtrl = e.isControlDown();
		boolean bShift = e.isShiftDown();
		boolean bAlt = e.isAltDown();
		
		if(actionExts == null || actionExts.isEmpty()){
			return;
		}
		
		if(bCtrl && bAlt && e.getKeyCode() == KeyEvent.VK_S){
			DataSetDefDescriptor.NewDataSetExt dirExt = getActionExt(DataSetDefDescriptor.NewDataSetExt.class);
			DataSetDefDescriptor.NewDataSetExt newExt = (DataSetDefDescriptor.NewDataSetExt)dirExt.clone();
			if(newExt!=null){
				newExt.setProviderClass("nc.vo.pub.dsmanager.SqlProvider");
				newExt.setProviderName("Sql Provider");
				newExt.getParams(datasetManager);
			}
		}
		
		//�½�Ŀ¼
		if(e.getKeyCode() == KeyEvent.VK_N && bCtrl && bShift){
			DataSetDefDescriptor.NewDirExt dirExt = getActionExt(DataSetDefDescriptor.NewDirExt.class);
			if(dirExt!=null){
				dirExt.getParams(datasetManager);
			}
			return;
		}
		
		//�޸�
		if(e.getKeyCode() == KeyEvent.VK_E && bCtrl && bShift){
			DataSetDefDescriptor.EditExt editExt = getActionExt(DataSetDefDescriptor.EditExt.class);
			if(editExt != null){
				editExt.getParams(datasetManager);
			}
			return;
		}
		
		//�h��
		if(e.getKeyCode() == KeyEvent.VK_D && bCtrl && bShift){
			DataSetDefDescriptor.RemoveExt removeExt = getActionExt(DataSetDefDescriptor.RemoveExt.class);
			if(removeExt != null){
				removeExt.getParams(datasetManager);
			}
			return;
		}
		
		//����
		if(e.getKeyCode() == KeyEvent.VK_X && bCtrl && bShift){
			DataSetEditDescriptor.CutExt cutExt = getActionExt(DataSetEditDescriptor.CutExt.class);
			if(cutExt != null){
				cutExt.getParams(datasetManager);
			}
			return;
		}
		
		//����
		if(e.getKeyCode() == KeyEvent.VK_C && bCtrl && bShift){
			DataSetEditDescriptor.CopyExt copyExt = getActionExt(DataSetEditDescriptor.CopyExt.class);
			if(copyExt != null){
				copyExt.getParams(datasetManager);
			}
			return;
		}
		
		//ճ��
		if(e.getKeyCode() == KeyEvent.VK_V && bCtrl && bShift){
			DataSetEditDescriptor.PasteExt pasteExt = getActionExt(DataSetEditDescriptor.PasteExt.class);
			if(pasteExt != null){
				pasteExt.getParams(datasetManager);
			}
			return;
		}
		
		//����˽�����ݼ�
		if(e.getKeyCode() == KeyEvent.VK_U && bCtrl && bShift){
			DataSetEditDescriptor.UpdateDSExt updateExt = getActionExt(DataSetEditDescriptor.UpdateDSExt.class);
			if(updateExt != null){
				updateExt.getParams(datasetManager);
			}
			return;
		}
		
		//����
		if(e.getKeyCode() == KeyEvent.VK_F && bCtrl){
			DataSetEditDescriptor.FindExt findExt = getActionExt(DataSetEditDescriptor.FindExt.class);
			if(findExt != null){
				findExt.getParams(datasetManager);
			}
			return;
		}
		
		//���
		if(e.getKeyCode() == KeyEvent.VK_P && bCtrl){
			DataSetEditDescriptor.BrowseExt browseExt = getActionExt(DataSetEditDescriptor.BrowseExt.class);
			if(browseExt != null){
				browseExt.getParams(datasetManager);
			}
			return;
		}
	}
	
	public void keyReleased(KeyEvent e) {
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IActionExt> T getActionExt(Class<T> type){
		if(type == null || actionExts == null || actionExts.isEmpty()){
			return null;
		}
		String className = type.getName();
		for(IActionExt ext : actionExts){
			if(ext.getClass().getName().equals(className)){
				return (T)ext;
			}
		}
		return null;
	}
	
	

	/**
     * @param ext void
     */
    public Component[] addActionExt(IActionExt ext) {
    	ArrayList<Component> list = new ArrayList<Component>();
        ActionUIDes[] uiDesArr = ext.getUIDesArr();
        for (int i = 0; uiDesArr != null && i < uiDesArr.length; i++) {
        	 Component comp = null;
            if(uiDesArr[i].isToolBar()){
    			comp = addToolBarItem(ext,uiDesArr[i]);//ע�����������
            }else if(uiDesArr[i].isPopup()){
                //comp = addRightMenu(ext,uiDesArr[i]);//ע��������Ҽ��˵�
            }else{
               comp = m_MenuBar.addExtension(ext,uiDesArr[i]);//ע������˵�
            }   
            
           list.add(comp);
            
        }
        return list.toArray(new Component[0]);
    }
    
 	/**
	 * ��ӹ�������Ŀ
	 */
	private Component addToolBarItem(IActionExt ext,ActionUIDes uiDes) {
	    String BarName = uiDes.getGroup();
		//��ȡ������
	    if(BarName == null){ //����Ϊ�������ϵͳĬ�ϵĹ�����
            BarName = MultiLang.getString("defaultToolBar");
	    }
	    
	    JToolBar[] toolBars = getToolBar();
	    DataSetManagerToolBar toolBar = null;
	    for(int i=0;i<toolBars.length;i++){
	    	if(BarName.equals(toolBars[i].getName()) && toolBars[i] instanceof DataSetManagerToolBar){
	    		toolBar = (DataSetManagerToolBar) toolBars[i];
	    		break;
	    	}
	    }
	    if(toolBar == null){
	    	toolBar = new DataSetManagerToolBar();
	    	toolBar.setName(BarName);
		    getM_ToolbarPanel().add(toolBar);
	    }    
	    
	    return toolBar.addToolItem(ext,uiDes, this.datasetManager);	
	     
	    
	}
    
	/**
	 * @return �õ�������
	 */
	public JToolBar[] getToolBar() {
		Component[] comps = getM_ToolbarPanel().getComponents();
		return (JToolBar[]) Arrays.asList(comps).toArray(new JToolBar[0]);      
    }
	
    private void initStyle(){
    	/* �����Զ������ */
//    	try {
//    		//�����µ�Ĭ�����
//			UIManager.setLookAndFeel(new Office2003LookAndFeel());
//			UIManager.put("MenuItem.acceleratorDelimiter", new String("+"));
//			//�����µ��û�Ĭ������
//			FontUIResource fontRes = new  FontUIResource(UIStyle.DIALOGFONT);
//			java.util.Enumeration keys = UIManager.getDefaults().keys();
//			//�滻���е�ȱʡ����.
//	        while (keys.hasMoreElements()) {
//	            Object key = keys.nextElement();
//	            Object value = UIManager.get (key);
//	            if (value instanceof javax.swing.plaf.FontUIResource)
//	                UIManager.put (key, fontRes);
//	        }
//	        UIManager.put ("Menu.font",new  FontUIResource(UIStyle.MENUFONT));
//	        
//	        SwingUtilities.updateComponentTreeUI(getContentPane());
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}

    }
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			m_MenuBar = new DataSetManagerMenuBar(datasetManager);
			setJMenuBar(m_MenuBar);
			jContentPane.add(getM_ToolbarPanel(), BorderLayout.NORTH);
			jContentPane.add(getJStatusPanel(), BorderLayout.SOUTH);
			jContentPane.add(getJCenterPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes m_ToolbarPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getM_ToolbarPanel() {
		if (m_ToolbarPanel == null) {
			m_ToolbarPanel = new ReportToolBarPanel();			
		}
		return m_ToolbarPanel;
	}

	/**
	 * This method initializes jStatusPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJStatusPanel() {
		if (jStatusPanel == null) {
			jStatusPanel = new UIPanel();
			jStatusPanel.setLayout(new GridBagLayout());
			jStatusPanel.setPreferredSize(new Dimension(0, 20));
		}
		return jStatusPanel;
	}

	/**
	 * This method initializes jCenterPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJCenterPanel() {
		if (jCenterPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.gridx = 0;
			jCenterPanel = new UIPanel();
			jCenterPanel.setLayout(new GridBagLayout());
			jCenterPanel.add(getJSplitPane(), gridBagConstraints1);
		}
		return jCenterPanel;
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new UISplitPane();
			jSplitPane.setPreferredSize(new Dimension(132, 30));
			jSplitPane.setLeftComponent(getM_treePanel());
			jSplitPane.setRightComponent(getJScrollPane());//getJPanel());
			jSplitPane.setDividerLocation(200);
			if (datasetDirTree != null) {
				// ���ý��㵽���ڵ�
				DirTreeNode dirNode = (DirTreeNode) datasetDirTree.getModel()
						.getRoot();
				TreePath tp = new TreePath(dirNode.getPath());
				datasetDirTree.setSelectionPath(tp);
			}
		}
		return jSplitPane;
	}

	/**
	 * This method initializes m_treePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getM_treePanel() {
		if (m_treePanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			m_treePanel = new JPanel();
			m_treePanel.setLayout(new GridBagLayout());
			m_treePanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
			m_treePanel.add(getJTree(), gridBagConstraints);
		}
		return m_treePanel;
	}

	/**
	 * This method initializes jTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getJTree() {
		if (datasetDirTree == null) {
			datasetDirTree = new UITree();
			datasetDirTree.setCellRenderer(new DataSetManagerTreeCellRenderer());
			datasetManager.setTreeModel(datasetDirTree);
		}
		return datasetDirTree;
	}
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridx = 0;
			jPanel = new UIPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			jPanel.add(getJScrollPane(), gridBagConstraints2);
		}
		return jPanel;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private UIScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane(getJTable());
//			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable1	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private UITable getJTable() {
		if (jTable == null) {
			jTable = new UITable();
			
//			jTable.getTableHeader().setResizingAllowed(true);
//			jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);		
			//datasetManager.setTableModel(jTable);
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			datasetManager.setTableModel(jTable, this.contianer);
		}
		return jTable;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
//		DataSetManagerDlg dlg = new DataSetManagerDlg();
//		dlg.showModal();
	}
	
	class DataSetManagerTreeCellRenderer extends DefaultTreeCellRenderer
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DataSetManagerTreeCellRenderer()
		{
		}
		
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			setBackGround(selected,hasFocus);
			expanded = leaf? selected : expanded;
			return super.getTreeCellRendererComponent(tree, value, selected, expanded, false, row, hasFocus);
		}
		
		private void setBackGround(boolean selected,boolean hasFocus) {
			if(!hasFocus && selected)
			{
				setBackgroundSelectionColor(Style.TreeSelectionNoFocusBackground);
			}
			else if(hasFocus && selected)
			{
				setBackgroundSelectionColor(Style.TreeSelectionBackground);
			}
		}
	}

}  
 