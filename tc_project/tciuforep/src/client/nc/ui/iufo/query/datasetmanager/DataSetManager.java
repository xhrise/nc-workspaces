package nc.ui.iufo.query.datasetmanager;

import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.pub.iufo.exception.UFOSrvException;
import nc.ui.iufo.datasetmanager.DataSetDefBO_Client;
import nc.ui.iufo.query.datasetmanager.exts.DataSetDefDescriptor;
import nc.ui.iufo.query.datasetmanager.exts.DataSetDefPlugin;
import nc.ui.iufo.query.datasetmanager.exts.DataSetEditManager;
import nc.ui.pub.dsmanager.DatasetTree;
import nc.ui.pub.util.NCTreeModel;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.iufo.datasetmanager.DataSetDirVO;
import nc.vo.iufo.datasetmanager.DataSetRegisterBean;
import nc.vo.iufo.datasetmanager.DataSetRegisterItem;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * ���ݼ��������ʵ�����ݼ���Ŀ¼��ά��
 * 
 * @author yangjiana 2008-4-23
 */
public class DataSetManager implements TreeSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DefaultTreeModel dirTreeModel;

	private JTree currentTree;

	private JTable currentTable;
	
	private DataSetDirVO currentDir;
	
	public void setCurrentDir(DataSetDirVO currentDir) {
		this.currentDir = currentDir;
	}

	private DataSetEditManager editManager;
	
	//˽�����ݼ�ӵ����ID
	private String ownerID = "";

	public DataSetManager() {

		dirTreeModel = new NCTreeModel(getDirRoot());
		editManager = new DataSetEditManager(this);
	}
	
	/**
	 * �༭������������Copy,Cut,Paste
	 * @return
	 */
	public DataSetEditManager getEditManager() {
		return editManager;
	}

	/**
	 * ��ȡ��ǰĿ¼VO
	 * 
	 * @return
	 */
	public DataSetDirVO getCurrentDir() {
		if(currentDir!=null)
			return currentDir;
		if (currentTree != null) {
			DirTreeNode node = (DirTreeNode) currentTree
					.getLastSelectedPathComponent();
			if (node != null) {
				currentDir = node.getDirVo();
				return currentDir;
			}
		}
		return null;
	}

	public void updateDataSetDef(DataSetDefVO vo) {

		int row = currentTable.getSelectedRow();
		if (row >= 0) {
			DataSetTableModel tm = (DataSetTableModel) currentTable.getModel();
			tm.removeRow(row);
			tm.insertDefVO(row, vo);
			//ˢ�����ݼ���
			DatasetTree.clearInstance();
			currentTable.getSelectionModel().setSelectionInterval(row, row);
		}

	}

	public void updateDir(DataSetDirVO dirVo) {
		try {
			DataSetDefBO_Client.updateDataSetDir(dirVo);
			DirTreeNode selectedNode = (DirTreeNode) currentTree
					.getLastSelectedPathComponent();

			getDirTreeModel().nodeChanged(selectedNode);
			//ˢ�����ݼ���
			DatasetTree.clearInstance();
			currentTree.repaint();
		} catch (UFOSrvException ex) {

		}
	}

	public DataSetDefVO[] getCurrentDataSetDef() {
		Vector<DataSetDefVO> defs = new Vector<DataSetDefVO>();
		if (currentTable != null) {
			for (int rowid : currentTable.getSelectedRows()) {
				defs.add((DataSetDefVO) currentTable.getModel().getValueAt(
						rowid, DataSetTableModel.COLUMN_VO));
			}
		}
		return defs.toArray(new DataSetDefVO[] {});
	}

	/**
	 * �ڵ�ǰ�ڵ��������Ŀ¼
	 * 
	 * @param currentDirVo
	 * @param dirName
	 * @param dirCode
	 * @return
	 */
	public DataSetDirVO addDir(DataSetDirVO currentDirVo, String dirName,
			String remark) {
		DataSetDirVO dirVo = null;
		try {

			// ����VO
			dirVo = new DataSetDirVO();
			dirVo.setName(dirName);
			dirVo.setRemark(remark);
//			dirVo.setCode(dirCode);
			dirVo.setPk_parentdir(currentDirVo.getPk_datasetdir());
			// ����
			dirVo = DataSetDefBO_Client.createDataSetDir(dirVo);

			// ����ڵ�
			DirTreeNode selectedNode = (DirTreeNode) currentTree
					.getLastSelectedPathComponent();
			DirTreeNode newNode = new DirTreeNode(dirVo);

			getDirTreeModel().insertNodeInto(newNode, selectedNode,
					selectedNode.getChildCount());

			TreePath tp = new TreePath(selectedNode.getPath());
			//ˢ�����ݼ���
			DatasetTree.clearInstance();
			currentTree.expandPath(tp);

		} catch (UFOSrvException ex) {
			AppDebug.debug(ex);
		}
		return dirVo;
	}

	/**
	 * ��ָ��Ŀ¼������µ����ݼ�����
	 * 
	 * @param dirVo
	 * @param defVo
	 * @return
	 */
	public DataSetDefVO addDataSetDef(DataSetDirVO dirVo, DataSetDefVO defVo) {
		DataSetTableModel tm = (DataSetTableModel) currentTable.getModel();
		tm.addDefVO(defVo);
		int row = currentTable.getRowCount()-1;
		currentTable.getSelectionModel().setSelectionInterval(row, row);
		return defVo;
	}

	/**
	 * ɾ��ָ��Ŀ¼
	 * 
	 * @param dirVo
	 * @return
	 */
	public boolean removeDir(DataSetDirVO dirVo) {
		try {

			DataSetDefBO_Client.removeDataSetDirByPk(dirVo.getPk_datasetdir());
			DirTreeNode selectedNode = (DirTreeNode) currentTree
					.getLastSelectedPathComponent();
			getDirTreeModel().removeNodeFromParent(selectedNode);
			//ˢ�����ݼ���
			DatasetTree.clearInstance();
			return true;
		} catch (UFOSrvException e) {
			return false;
		}
	}

	/**
	 * ɾ��ָ�����ݼ�����
	 * 
	 * @param defVo
	 * @return
	 */
	public boolean removeDataSetDef(DataSetDefVO defVo) {
		try {
			DataSetDefBO_Client.removeDataSetDefByPk(defVo.getPk_datasetdef());
			DataSetTableModel tm = (DataSetTableModel) currentTable.getModel();
			tm.removeDef(defVo.getPk_datasetdef());
			//ˢ�����ݼ���
			DatasetTree.clearInstance();
			return true;
		} catch (UFOSrvException e) {
			return false;
		}
	}

	/**
	 * ��ȡ��Ŀ¼�ڵ�
	 * 
	 * @return
	 */
	protected DirTreeNode getDirRoot() {

		DataSetDirVO rootVo = null;
		DataSetDirVO[] dirs = null;
		try {
			rootVo = DataSetDefBO_Client.loadRootDir();
			dirs = DataSetDefBO_Client.loadAllDir();
		} catch (UFOSrvException e) {
			AppDebug.debug(e);
		}
		if ((rootVo == null) || (dirs == null)) {
			return new DirTreeNode(rootVo);
		}
		DirTreeNode root = new DirTreeNode(rootVo);

		addChildNode(root, rootVo, dirs);
		return root;
	}

	/**
	 * �ڹ����Ŀ¼��
	 * 
	 * @param parentNode
	 * @param parentVo
	 * @param dirs
	 */
	private void addChildNode(DirTreeNode parentNode, DataSetDirVO parentVo,
			DataSetDirVO[] dirs) {
		for (DataSetDirVO dirVo : dirs) {
			if (dirVo.getPk_parentdir() != null
					&& dirVo.getPk_parentdir().equals(
							parentVo.getPk_datasetdir())) {
				DirTreeNode node = new DirTreeNode(dirVo);
				parentNode.add(node);
				addChildNode(node, dirVo, dirs);
			}
		}
	}

	/**
	 * Ϊ�ƶ���JTree�ؼ�����TreeModel
	 * 
	 * @param tree
	 */
	public void setTreeModel(JTree tree) {
		// ɾ����ǰ�ļ�����
		if (currentTree != null) {
			currentTree.removeTreeSelectionListener(this);
		}

		currentTree = tree;
		currentTree.addTreeSelectionListener(this);
		//yza+ 2008-6-10 ��������ѡ
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setModel(getDirTreeModel());
	}

	public void setTableModel(JTable table) {
		currentTable = table;
		//yza+ 2008-6-10 ���˫������
//		addMouseClick2();
	}
	
	public void setTableModel(JTable table,Container container)
	{
		setTableModel(table);
		addMouseClick2(container);
	}

	/**
	 * ��ȡTreeModel
	 * 
	 * @return
	 */
	protected DefaultTreeModel getDirTreeModel() {
		return dirTreeModel;
	}

	/**
	 * ˢ��Table
	 * 
	 */
	public void refreshTable() {
		DataSetDirVO dirVo = null;
		if (currentTree != null) {
			DirTreeNode node = (DirTreeNode) currentTree
					.getLastSelectedPathComponent();
			if (node != null) {
				dirVo = node.getDirVo();
			}
		}
		if (dirVo != null) {
			try {
				DataSetDefVO[] defs = null;
				if(dirVo.isPrivate())
				{
					//˽�����ݼ�Ŀ¼
					if(this.ownerID.trim().length()>0)
						defs = DataSetDefBO_Client.loadDataSetDefs4Private(this.ownerID.trim());
					else
						defs = new DataSetDefVO[]{};
				}
				else
				{
					defs = DataSetDefBO_Client
					.loadDataSetDefsByDir(dirVo);
//					DataSetDefVO[] defs = DataSetDefBO_Client
//					.loadDataSetDefsByDir(dirVo);
//					DataSetTableModel tm = new DataSetTableModel();
//					tm.addDefVO(defs);
//					currentTable.setModel(tm);
				}
				DataSetTableModel tm = new DataSetTableModel();
				tm.addDefVO(defs);
				currentTable.setModel(tm);
			} catch (UFOSrvException e) {
				AppDebug.debug(e);
			}finally{
				//���currentDir
				this.currentDir = null;
			}
		}
	}

	public void valueChanged(TreeSelectionEvent arg0) {
		refreshTable();
	}
	
	/**
	 * �Ƿ�������Ϊ�������ݼ�
	 * (ֻ��˽�����ݼ��������ɹ������ݼ�)
	 * @return
	 */
	public boolean canUpdate()
	{
		DataSetDefVO[] vos = getCurrentDataSetDef();
		if(vos == null || vos.length == 0)
			return false;
		else if(vos.length == 1)
			return vos[0].isPrivate();
		else
		{
			for(DataSetDefVO ds : vos)
			{
				if(!ds.isPrivate())
					return false;
			}
			return true;
		}
	}

	@SuppressWarnings("serial")
	class DataSetTableModel extends DefaultTableModel {

		public final static int COLUMN_NAME = 1;

		public final static int COLUMN_CODE = 0;

		public final static int COLUMN_PK = 4;

		public final static int COLUMN_VO = 3;
		
		public final static int COLUMN_TYPE = 2;

		/**
		 * @i18n miufo1001012=����
		 * @i18n miufo1001051=����
		 */
		public DataSetTableModel() {
			super(new String[] { StringResource.getStringResource("miufo1001012"), StringResource.getStringResource("miufo1001051"), StringResource.getStringResource("miufo1001390") }, 0);
		}

		public void addDefVO(DataSetDefVO vo) {
			addRow(new Object[] { vo });
		}

		public void insertDefVO(int row, DataSetDefVO vo) {
			insertRow(row, new Object[] { vo });
		}

		public void removeDef(String pk) {
			for (int i = 0; i < getRowCount(); i++) {
				if (getValueAt(i, COLUMN_PK).toString().equals(pk)) {
					removeRow(i);
					return;
				}
			}
		}

		public void addDefVO(DataSetDefVO[] vos) {
			for (DataSetDefVO vo : vos) {
				addDefVO(vo);
			}
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}

		/**
		 * @i18n uiufofurl0071=��ѯ����
		 * @i18n uiufofurl0083=����
		 * @i18n miufo00219=����
		 */
		public Object getValueAt(int arg0, int arg1) {
			DataSetDefVO defVO = (DataSetDefVO) super.getValueAt(arg0, 0);
			
			if (arg1 == COLUMN_PK) {
				return defVO.getPk_datasetdef();
			} else if (arg1 == COLUMN_NAME) {
				return defVO.getName();
			}else if (arg1 == COLUMN_VO){
				return defVO;
			} else if (arg1 == COLUMN_CODE) {
				return defVO.getCode();
			} else if (arg1 == COLUMN_TYPE) {
				if(defVO.getKind() == null || defVO.getKind().length() == 0){
					return "Unknown";
				}
				
				String type="";
				DataSetRegisterItem[] items=DataSetRegisterBean.getDefaultInstance().getItems();
				if(items!=null&&items.length>0){
					for(DataSetRegisterItem item:items){
						if (item.getKind() != null&& item.getKind().equalsIgnoreCase(defVO.getKind())) {
							type=StringResource.getStringResource(item.getProviderName());
						}
					}
				}else{
					type=defVO.getKind();
				}
				
				return type;
			}
			return "";
		}
	}
	
	/*
	 * ����˫���޸����ݼ�����
	 * yza+ 2008-6-10
	 */
	private void addMouseClick2(final Container container)
	{
		currentTable.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e)
			{
				if(e.getClickCount() == 2)
				{
					DataSetDefVO[] selects = getCurrentDataSetDef();
					if(selects.length == 1) //ֻ�Ե�ѡ������
					{
						DataSetDefDescriptor ddd = new DataSetDefDescriptor(new DataSetDefPlugin(container));
						DataSetDefDescriptor.EditExt ee = ddd.new EditExt();
						ee.getParams(DataSetManager.this);
					}
				}
			}
		});
	}
	
	/*
	 * ���¼���Ŀ¼��
	 * yza+ 2008-6-11
	 */
	public void reloadDirTree()
	{
		dirTreeModel = new NCTreeModel(getDirRoot());
		currentTree.setModel(getDirTreeModel());
		currentTree.repaint();
	}
	
	public String getOwnerID()
	{
		return this.ownerID;
	}
	
	public void setOwnerID(String ownerID)
	{
		this.ownerID = ownerID;
	}
	
	public JTree getCurrentTree()
	{
		return this.currentTree;
	}
	
	public JTable getCurrentTable()
	{
		return this.currentTable;
	}
	
	public DataSetDefVO getRowVO(int row)
	{
		DataSetTableModel model = (DataSetTableModel)this.currentTable.getModel();
		return (DataSetDefVO)model.getValueAt(row,DataSetTableModel.COLUMN_VO);
	}
	
}
   