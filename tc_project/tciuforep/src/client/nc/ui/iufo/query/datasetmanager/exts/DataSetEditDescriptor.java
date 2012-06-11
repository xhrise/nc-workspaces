package nc.ui.iufo.query.datasetmanager.exts;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.bd.corp.ICorpQry;
import nc.pub.iufo.exception.UFOSrvException;
import nc.ui.iufo.datasetmanager.DataSetDefBO_Client;
import nc.ui.iufo.query.datasetmanager.DataSetManager;
import nc.ui.iufo.query.datasetmanager.DirTreeNode;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.dsmanager.AbstractTreeForEdit;
import nc.ui.pub.dsmanager.DataSetPreviewDlg;
import nc.ui.pub.dsmanager.DatasetTree;
import nc.ui.pub.dsmanager.DatasetTreeDlg;
import nc.ui.pub.dsmanager.TreeDatasetFindDlg;
import nc.ui.pub.util.NCTreeNode;
import nc.vo.bd.CorpVO;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.iufo.datasetmanager.DataSetDirVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.pub.DataManageObjectIufo;
import nc.vo.pub.dsmanager.exception.DSNotFoundException;
import nc.vo.pub.querymodel.ModelUtil;
import nc.vo.sm.UserVO;
import nc.vo.sm.config.Account;

import com.ufida.dataset.Context;
import com.ufida.dataset.DataSet;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.ContextFactory;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

public class DataSetEditDescriptor extends AbstractPlugDes {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DataSetEditPlugin plugin;

	public DataSetEditDescriptor(DataSetEditPlugin plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbstractPlugDes#createExtensions()
	 */
	protected IExtension[] createExtensions() {
		ArrayList<IExtension> al_extensions = new ArrayList<IExtension>();

		al_extensions.add(new CutExt());
		al_extensions.add(new CopyExt());
		al_extensions.add(new PasteExt(plugin.getDataSetManager()));
		al_extensions.add(new UpdateDSExt(plugin.getDataSetManager()));
		al_extensions.add(new FindExt());
		al_extensions.add(new BrowseExt(plugin.getDataSetManager()));
		return al_extensions.toArray(new IExtension[0]);
	}

	public class CutExt extends DSMActionExt {

		@Override
		public Object[] getParams(DataSetManager dsManager) {
			try{
			dsManager.getEditManager().cut();
			}catch(Exception ex){
				AppDebug.debug(ex);
				UfoPublic.sendErrorMessage(MultiLang.getString("error"), DataSetEditDescriptor.this.getContainer(), ex);
			}
			return null;
		}

		@Override
		public UfoCommand getCommand() {
			return null;
		}

		/**
		 * @i18n miufo1000654=剪切
		 */
		@Override
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("miufo1000654"));
			uiDes1.setImageFile("reportcore/cut.gif");
			uiDes1.setPaths(new String[] { MultiLang.getString("edit") });
			
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setToolBar(true);
			uiDes2.setPaths(null);

			return new ActionUIDes[] { uiDes1,uiDes2};
		}
	}

	public class CopyExt extends DSMActionExt {

		public CopyExt() {
		}

		// private String
		@Override
		public Object[] getParams(DataSetManager dsManager) {
			try{
			dsManager.getEditManager().copy();
			}catch(Exception ex){
				AppDebug.debug(ex);
				UfoPublic.sendErrorMessage(MultiLang.getString("error"), DataSetEditDescriptor.this.getContainer(), ex);
			}
			return null;
		}

		@Override
		public UfoCommand getCommand() {
			return null;
		}

		/**
		 * @i18n miufo1000653=复制
		 */
		@Override
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("miufo1000653"));
			uiDes1.setImageFile("reportcore/copy.gif");
			uiDes1.setPaths(new String[] { MultiLang.getString("edit") });

			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setToolBar(true);
			uiDes2.setPaths(null);
			
			return new ActionUIDes[] { uiDes1,uiDes2 };
		}

	}

	public class PasteExt extends DSMActionExt implements IDataSetEditListener {

		private Vector<Component> stateComps = new Vector<Component>();

		public PasteExt(DataSetManager manager) {
			manager.getEditManager().addListener(this);
		}

		@Override
		public void initListenerByComp(Component stateChangeComp) {
			super.initListenerByComp(stateChangeComp);
			this.stateComps.add(stateChangeComp);
			stateChangeComp.setEnabled(false);
		}

		@Override
		public Object[] getParams(DataSetManager dsManager) {
			try {
//				DataSetDirVO dirVO = dsManager.getCurrentDir();
				Container container = getContainer();
				dsManager.getEditManager().paste(container);
			} catch (Exception ex) {
				AppDebug.debug(ex);
				UfoPublic.sendErrorMessage(MultiLang.getString("error"), DataSetEditDescriptor.this.getContainer(), ex);
			}
			return null;
		}

		@Override
		public UfoCommand getCommand() {

			return null;
		}

		/**
		 * @i18n miufo1000655=粘贴
		 */
		@Override
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("miufo1000655"));
			uiDes1.setImageFile("reportcore/paste.gif");
			uiDes1.setPaths(new String[] { MultiLang.getString("edit") });
			
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setToolBar(true);
			uiDes2.setPaths(null);
			return new ActionUIDes[] { uiDes1,uiDes2 };

		}

		public void stateChanged(DataSetEditManager manager) {
			for(Component c : this.stateComps)
			{
				c.setEnabled(manager.hasVO());
			}
			//this.stateComp.setEnabled();
		}
	}
	
	/*
	 * 查找DataSet
	 */
	public class FindExt extends DSMActionExt
	{
		private DataSetManager manager = null;

		/**
		 * @i18n miufo1001134=查找
		 */
		@Override
		public Object[] getParams(DataSetManager dsManager) {
			try{
			this.manager = dsManager;
			Container container = DataSetEditDescriptor.this.getContainer();
			TreeDatasetFindDlg dlg = new TreeDatasetFindDlg(container,StringResource.getStringResource("miufo1001134"), true);
			dlg.setObjectTrees(
					new AbstractTreeForEdit[]
					                        {DatasetTree.getInstance(DataManageObjectIufo.IUFO_DATASOURCE,
					                        		manager.getOwnerID(),false,false)});
			if(dlg.showModal() == UIDialog.ID_OK)
			{
				NCTreeNode objnode = dlg.getSearch();//数据集定义
				if (objnode != null)
				{
					// 所属目录Id
					String dirID =  objnode.getParentCode();
					DataSetDirVO dir = null;
					try {
						dir = DataSetDefBO_Client.loadDataSetDirVOByPk(dirID);
					} catch (UFOSrvException ex) {
						AppDebug.debug(ex);
					}
					if(dir!= null)
					{
						//定位树节点
						Object root = manager.getCurrentTree().getModel().getRoot();
						TreePath   treePath   =   new   TreePath(root);
						DirTreeNode node = new DirTreeNode(dir);	
						//递归查找目标的Path
						TreePath targetPath = findInPath(treePath,node);
						if(targetPath!=null)
						{
							manager.getCurrentTree().expandPath(targetPath);
							manager.getCurrentTree().setSelectionPath(targetPath);
							manager.getCurrentTree().scrollPathToVisible(targetPath);
						}
						
//						int columnPlace = 0; //编码列
//						if(dlg.getSearchFlag() == DatasetFindDlg.FIND_BY_NAME)
//							columnPlace = 1;//名称列
						
						//定位表格
						DataSetDefVO vo = (DataSetDefVO)dlg.getSearch().getDataVO();
						int row = findInTable(vo);
						if(row >= 0)
						{
						manager.getCurrentTable().getSelectionModel()
									.setSelectionInterval(row,row);
//							Rectangle cellRect = manager.getCurrentTable()
//									.getCellRect(row, columnPlace, true);
//							manager.getCurrentTable().scrollRectToVisible(
//									cellRect);
						}
					}
				}
			}
			}catch(Exception ex){
				AppDebug.debug(ex);
				UfoPublic.sendErrorMessage(MultiLang.getString("error"), DataSetEditDescriptor.this.getContainer(), ex);
			}
			return null;
		}
		
		private TreePath findInPath(TreePath treePath,DirTreeNode node)
		{
			Object   object   =   treePath.getLastPathComponent();   
			  if   (object   ==   null)   {   
				  return   null;   
			  }
			  DirTreeNode targetNode = (DirTreeNode)object;
			  if(targetNode.equals(node))
			  {
				  return treePath;
			  }
			  else
			  {
				  TreeModel   model   =   manager.getCurrentTree().getModel();
				  int   n   =   model.getChildCount(object);
				  for(int i=0;i<n;i++)
				  {
					  Object child = model.getChild(object,i);
					  TreePath path = treePath.pathByAddingChild(child);
					  path = findInPath(path,node);
					  if(path!=null)
						  return path;
				  }
			  } 
			  return null;
		}
		
		private int findInTable(DataSetDefVO vo)
		{
			int rowCount = manager.getCurrentTable().getRowCount();
			for(int i=0;i<rowCount;i++)
			{
				DataSetDefVO target = manager.getRowVO(i);
				if(vo.getPk_datasetdef()!=null &&
						target.getPk_datasetdef()!=null &&
						target.getPk_datasetdef().equals(vo.getPk_datasetdef()))
				{
					return i;
				}
			}
			return -1;
		}

		@Override
		public UfoCommand getCommand() {
			return null;
		}

		@Override
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(MultiLang.getString("find"));
			uiDes1.setImageFile("reportcore/query.gif");
			uiDes1.setPaths(new String[] { MultiLang.getString("edit") });
			
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setToolBar(true);
			uiDes2.setPaths(null);
			
			return new ActionUIDes[] { uiDes1,uiDes2};
		}
	}
	
	public class UpdateDSExt extends DSMActionExt implements ListSelectionListener{

		private Vector<Component> stateComps = new Vector<Component>();
		private DataSetManager manager = null;
		
		public UpdateDSExt(DataSetManager manager)
		{
			this.manager = manager;
			//添加控件是否可用事件
			JTable table = manager.getCurrentTable();
			if(table!=null)
				table.getSelectionModel().addListSelectionListener(this);
		}
		
		@Override
		public void initListenerByComp(Component stateChangeComp) {
			super.initListenerByComp(stateChangeComp);
			this.stateComps.add(stateChangeComp);
			stateChangeComp.setEnabled(false);
		}
		
		/**
		 * @i18n miufo00865=升级失败!
		 */
		@Override
		public Object[] getParams(DataSetManager dsManager) {
			try{
			if(manager == null || !manager.canUpdate())
				return null;
			Container container = DataSetEditDescriptor.this.getContainer();
			DatasetTreeDlg dlg = new DatasetTreeDlg(container,true,true);
			dlg.setTitle(StringResource.getStringResource("usrdef0043"));
			dlg.setUsingType(true, true);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				// 获得多选结果
				DataSetDirVO dir = dlg.getSelDatasetDir();
				if(dir!=null)
				{
					DataSetEditManager editManage = dsManager.getEditManager();
					editManage.cut();
					dsManager.setCurrentDir(dir);
					try {
						editManage.paste(container);
					} 
//					catch (UFOSrvException e) {
//						MessageDialog.showErrorDlg(container, MultiLang.getString("error"), StringResource.getStringResource("miufo00865"));
//						throw new RuntimeException("Update Private DataSet Error!");
//					}
					finally{
						//清理
						dsManager.refreshTable();
						editManage.setPasteDir(null);
						editManage.setCurrentVO(null);
					}
				}
			}
			}catch(Exception ex){
				AppDebug.debug(ex);
				UfoPublic.sendErrorMessage(MultiLang.getString("error"), DataSetEditDescriptor.this.getContainer(), ex);
			}
			return null;
		}

		@Override
		public UfoCommand getCommand() {
			return null;
		}

		@Override
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("usrdef0043"));
			uiDes1.setImageFile("biplugin/parent.gif");
			uiDes1.setPaths(new String[] { StringResource.getStringResource("usrdef0044") +"\tO"});
			
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setToolBar(true);
			uiDes2.setPaths(null);
			
			return new ActionUIDes[] {  uiDes1,uiDes2};
		}

		public void valueChanged(ListSelectionEvent e) {
			for(Component c : this.stateComps)
			{
				c.setEnabled(manager.canUpdate());
			}
		}
		
	}
	
	/**
	 * 数据集浏览
	 * @author yaoza
	 *
	 */
	public class BrowseExt extends DSMActionExt implements ListSelectionListener{

		private DataSetManager manager;
		private Vector<Component> stateComps = new Vector<Component>();
		
		public BrowseExt(DataSetManager manager)
		{
			this.manager = manager;
			//添加控件是否可用事件
			JTable table = manager.getCurrentTable();
			if(table!=null)
				table.getSelectionModel().addListSelectionListener(this);
		}
		
		@Override
		public void initListenerByComp(Component stateChangeComp) {
			super.initListenerByComp(stateChangeComp);
			this.stateComps.add(stateChangeComp);
			stateChangeComp.setEnabled(false);
		}
		
		@Override
		public Object[] getParams(DataSetManager dsManager) {
			try
			{
				DataSetDefVO[] vos = dsManager.getCurrentDataSetDef();
				DataSet ds = vos[0].getDataSetDef();
				Context context = createContext();
				initNCContext(context);
				try {
					DataSetPreviewDlg preview = new DataSetPreviewDlg(
							DataSetEditDescriptor.this.getContainer(), context);
					// 如果有已用参数先弹出参数对话框
					preview.setDatas(ds);
				} catch (DSNotFoundException ex) {
					AppDebug.debug(ex);
					MessageDialog.showErrorDlg(DataSetEditDescriptor.this.getContainer(),MultiLang.getString("error"), ex.getMessage());
					//UfoPublic.sendErrorMessage(MultiLang.getString("error"), DataSetEditDescriptor.this.getContainer(), ex);
				}
			}catch(Exception ex){
				AppDebug.debug(ex);
				UfoPublic.sendErrorMessage(MultiLang.getString("error"), DataSetEditDescriptor.this.getContainer(), ex);
			}
			
			return null;
		}

		@Override
		public UfoCommand getCommand() {
			return null;
		}

		@Override
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("miufo1000889"));
			uiDes1.setImageFile("reportcore/preview.gif");
			uiDes1.setPaths(new String[] { StringResource.getStringResource("miufo1000889") +"\tB" });
			
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setToolBar(true);
			uiDes2.setPaths(null);
			
			return new ActionUIDes[] {  uiDes1,uiDes2};
		}

		public void valueChanged(ListSelectionEvent arg0) {
			for(Component c : this.stateComps)
			{
				c.setEnabled(manager.getCurrentDataSetDef() != null 
						&& manager.getCurrentDataSetDef().length == 1);
			}
		}
		
	}
	
	private Context createContext() {
		Context context=null;
		
		if (getContainer() instanceof UfoReport) {
			// 创建UfoReport上下文
			UfoReport r = (UfoReport) getContainer();
			context = ContextFactory.createContext(r); 
		}
		return context;
	}
	
	/**
	 * 初始化NC环境信息
	 * @i18n miufohbbb00131=NC数据源无效,使用默认NC数据源:
	 */
	private void initNCContext(Context context){
		if(context == null)
			return;
		ClientEnvironment ce = ClientEnvironment.getInstance();
		DataSourceVO dsVO = null;
		if(context.getAttribute(IUfoContextKey.DATA_SOURCE)!=null &&
				(context.getAttribute(IUfoContextKey.DATA_SOURCE) instanceof DataSourceVO)){
			dsVO = (DataSourceVO)context.getAttribute(IUfoContextKey.DATA_SOURCE);
		}
		String dsn = ModelUtil.getDefaultDsname();
		String unitId = "";
		String loginName = "";
		try {
			if(dsVO == null || dsVO.getType()!=DataSourceVO.TYPENC2){
				AppDebug.debug(StringResource.getStringResource("miufohbbb00131")+(dsn==null?"":dsn));
			}else{
				dsn = dsVO.getAddr();
				unitId = dsVO.getUnitId();
				loginName = dsVO.getLoginName();
				InvocationInfoProxy.getInstance().setDefaultDataSource(dsn);
			}
			
			// 帐套信息 
			ce.setConfigAccount(new Account());
			ce.getConfigAccount().setAccountCode("");
			ce.getConfigAccount().setDataSourceName(dsn);
			// 公司信息
			//modified by biancm 20091202 unitId可能为iufo单位PK，为确保正确，根据loginUnit来获得NC单位信息
			try {
				//此时确保已设置正确的NC数据源信息
				ICorpQry corpQuery = (ICorpQry) NCLocator.getInstance().lookup(ICorpQry.class.getName());
				CorpVO[] vos = null;
				vos = corpQuery.queryCorpVOByWhereSQL(" unitcode = '" + dsVO.getLoginUnit() + "'");
				if (vos != null && vos.length > 0) {
					unitId = vos[0].getPk_corp();
			     }
			} catch (Exception e) {
				Logger.error(e);//异常全部吃掉
			}

			ce.setCorporation(new CorpVO());
			ce.getCorporation().setPrimaryKey(unitId);
			
			// 用户信息
			ce.setUser(new UserVO());
			ce.getUser().setUserCode(loginName);
		} catch (Exception e) {
			AppDebug.debug(e.getMessage());
		}
	}

	public Container getContainer() {
		return ((DataSetEditPlugin) getPlugin()).getContainer();
	}

}
 