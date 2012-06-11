package com.ufsoft.report.plugin;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.FocusManager;
import javax.swing.Icon;
import javax.swing.JPanel;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.IPluginAction;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.Viewer;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.UFOTable;

/**
 * @deprecated 禁止使用。 v6将直接删除。 liuyy.
 */
public abstract class AbstractPlugIn extends
		com.ufida.zior.plugin.AbstractPlugin implements IPlugIn {
	/** 监听器列表 */
	private Vector m_vecListeners = new Vector();
	/** 插件对应的报表*/
	private UfoReport m_report;

	/**
	 * 添加一个监听器
	 * 
	 * @param lis
	 *            IPlugInListener
	 * @throws NullPointerException
	 *             输入参数为空抛出.
	 */
	public void addListener(IPlugInListener lis) {
		if (lis == null)
			throw new NullPointerException();
		if (!m_vecListeners.contains(lis)) {
			m_vecListeners.addElement(lis);
		}
	}

	/**
	 * 删除一个监听器。
	 * 
	 * @param lis
	 *            被删除的监听器。
	 */
	public void removeListener(IPlugInListener lis) {
		if (lis == null) {
			return;
		}
		m_vecListeners.removeElement(lis);
	}

	/**
	 * 通知所有注册了的监听器执行响应。
	 * 
	 * @param e
	 */
	public void notifyListener(PlugEvent e) {
		int nSize = m_vecListeners.size();
		for (int i = 0; i < nSize; i++) {
			((IPlugInListener) m_vecListeners.get(i)).pluginAction(e);
		}
	}

	/*
	 * 当一个插件没有描述类时，返回默认的描述类。
	 * 
	 * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
	 */
	private static IPluginDescriptor defaultDes = new IPluginDescriptor() {

		public String getName() {
			return null;
		}

		public String getNote() {
			return null;
		}

		public String[] getPluginPrerequisites() {
			return null;
		}

		public IExtension[] getExtensions() {
			return null;
		}

		public String getHelpNode() {
			return null;
		}
	};
	/** 插件的描述类*/
	private IPluginDescriptor m_des;

	/**
	 * 获取插件的描述类变量m_des
	 */
	public final IPluginDescriptor getDescriptor() {
		if (m_des == null) {
			m_des = createDescriptor();
			if (m_des == null) {
				m_des = defaultDes;
			}
		}
		return m_des;
	}

	protected void setDescriptor(IPluginDescriptor ds) {
		m_des = ds;
	}

	/**
	 * 创建插件描述类的抽象方法，由其子类具体实现
	 * @return IPluginDescriptor
	 */
	protected abstract IPluginDescriptor createDescriptor();

	/**
	 * 获取该插件对应的报表实例
	 * @return m_report。
	 */
	public final UfoReport getReport() {
		//适配mainboard
		if(m_report == null){
			m_report = new UfoReport2Mainboard(getMainboard());
		}
		return m_report;
	}

	/**
	 * 获取该插件对应的报表实例的数据模型
	 * @return CellsModel
	 */
	public CellsModel getCellsModel() {
		return getReport().getCellsModel();
	}

	/**
	 * 设置该插件对应的报表实例
	 * @param m_report
	 *            要设置的 m_report。
	 */
	public void setReport(UfoReport report) {
		m_report = report;
	}

	/**
	 * 脏标记
	 */
	private boolean dirty = false;

	/**
	 * 插件中信息是否被修改
	 * @return 返回 dirty。
	 * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * 设置插件中信息是否被修改
	 * @param dirty
	 *            要设置的 dirty。
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public void store() {
		setDirty(false);
	}

	/**
	 * @see com.ufsoft.report.plugin.IPlugIn#startup()
	 */
	public void startup() {
	}

	/**
	 * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
	 */
	public void shutdown() {
		store();
	}

//	public String[] getSupportData() {
//		return null;
//	}

//	public SheetCellRenderer getDataRender(String extFmtName) {
//		return null;
//	}
//
//	public SheetCellEditor getDataEditor(String extFmtName) {
//		return null;
//	}

	@Override
	protected IPluginAction[] createActions() {
		IPluginDescriptor desc = getDescriptor();
		IExtension[] exns = desc.getExtensions();
        if(exns == null){
        	return null;
        }
		List<IPluginAction> list = new ArrayList<IPluginAction>();		
		for (IExtension ext : exns) {
			if (!(ext instanceof IActionExt)) {
				continue;
			}
			list.add(new Ext2Action((IActionExt) ext,getReport()));
		}

		return list.toArray(new IPluginAction[0]);
	}

	private class Ext2Action extends AbstractPluginAction {
		private IActionExt ext = null;
		UfoReport report = null;
		
		Ext2Action(IActionExt e,UfoReport report) {
			ext = e;
			this.report = report;
		}

		@Override
		public void execute(ActionEvent e) {
			
			try {
                if(!isEnabled()){
                	return;
                }
				ext.initListenerByComp((Component)e.getSource());
				AppDebug.debug("执行动作：" + ext.getClass().getName());
				Object[] params = ext.getParams(report);
				UfoCommand cmd = ext.getCommand();
				if (cmd != null) {
					cmd.run(params, null);
				}

			} catch (com.ufida.zior.exception.MessageException e2) {
				UfoPublic.sendMessage(e2, report);

			} catch (Throwable t) {
				AppDebug.debug(t);
				UfoPublic.sendErrorMessage(t.getMessage(), report, t);
			} finally {
			}

		}

		@Override
		public boolean isEnabled() {
			if(report.getCellsModel() == null){
				return false;
			}
			//获得持久焦点所有者
			Component com = FocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
			return ext.isEnabled(com);
		}

		@Override
		public IPluginActionDescriptor getPluginActionDescriptor() {
		 
			return new PluginActionDesAdapter(ext.getUIDesArr());
			
		}

	}
	
	private class PluginActionDesAdapter extends PluginActionDescriptor{

		ActionUIDes[] actionUIDes = null;
		
		PluginActionDesAdapter(ActionUIDes[] uiDes){
			if(uiDes == null || uiDes.length ==0){
				throw new IllegalArgumentException();
			}
			this.actionUIDes = uiDes;
			initialize();
		}
		
		private void initialize(){
			setName(actionUIDes[0].getName());
			setMemonic(actionUIDes[0].getMnemonic());
			setAccelerator(actionUIDes[0].getAccelerator());
			setShowDialog(actionUIDes[0].isShowDialog());
			setToolTipText("");
			if(isSpecialComponet()){
				setCompentFactory(new SpecialComponentFactory(actionUIDes));
			}
		}
		
		public XPOINT[] getExtensionPoints() {
			Vector<XPOINT> pointVec = new Vector<XPOINT>();
			for(ActionUIDes uiDes : actionUIDes){
				if(uiDes.isToolBar()){
					pointVec.add(XPOINT.TOOLBAR);
				} else if(uiDes.isPopup()){
					pointVec.add(XPOINT.POPUPMENU);
				} else {
					pointVec.add(XPOINT.MENU);
				}
			}
			return pointVec.toArray(new XPOINT[0]);
		}

		public String[] getGroupPaths() {
			String[] newPathAry = null;
			for (ActionUIDes uiDes : actionUIDes) {
				if (uiDes.isToolBar() || uiDes.isPopup()) {
					continue;
				}
				if(uiDes.isDirectory()){
					continue;
				}
				String[] oldPathAry = uiDes.getPaths();
				if (oldPathAry == null) {
					continue;
				}
				int length = oldPathAry.length;
				newPathAry = new String[length + 1];
				System.arraycopy(oldPathAry, 0, newPathAry, 0,
						length);
				
				newPathAry[length] = uiDes.getGroup();
				
				return newPathAry;
			}
			
			for (ActionUIDes uiDes : actionUIDes) {
				if(uiDes.isDirectory()){
					continue;
				}
				if(uiDes.isPopup() || uiDes.isToolBar()){
					return new String[] { uiDes.getGroup()};
				}
			}
			return null;
		}

		@Override
		public Icon getIcon() {
			return ResConst.getImageIcon(actionUIDes[0].getImageFile());
		}
		
		@Override
		public void setToolTipText(String toolTipText) {
			for (ActionUIDes uiDes : actionUIDes) {
				String strToolTipText = uiDes.getTooltip();
				if(uiDes != null && strToolTipText != null && strToolTipText.length() != 0){
					super.setToolTipText(strToolTipText);
				}
			}
			
		}

		private boolean isSpecialComponet(){
			for(ActionUIDes uiDes : actionUIDes){
				if(uiDes.isListCombo() || uiDes instanceof ToggleMenuUIDes
						|| uiDes.isCheckBoxMenuItem()){
                  return true;
				}
			}
			return false;
		}
				
	}
	
	public class UfoReport2Mainboard  extends UfoReport {
		private static final long serialVersionUID = 1L;
		
		private Mainboard mainboard = null;
		
		public UfoReport2Mainboard(Mainboard mb){
			mainboard = mb;
			setEventManager(getMainboard().getEventManager());
		}
		
		public UfoReport2Mainboard(int oper, ContextVO contextVO) {
			super(oper, contextVO);
			// TODO Auto-generated constructor stub
		}
		
		public CellsModel getCellsModel() {
			ReportDesigner viewer = getViewer();
            return viewer == null ? null :viewer.getCellsModel();
		}
		
		@Override
		public UFOTable getTable() {
			ReportDesigner viewer = getViewer();
			return viewer == null ? null : viewer.getTable();
		}
				
		public CellsPane getCellsPane() {
			ReportDesigner viewer = getViewer();
			return viewer == null ? null : viewer.getCellsPane();
		}

		public ContextVO getContextVo(){
			ReportDesigner viewer = getViewer();
			return  viewer == null? new ContextVO((Context)mainboard.getContext()) : new ContextVO((Context)viewer.getContext());
		}
			
		/**
		 * add by wangyga
		 */
		@Override
		public Container getParent() {
			return mainboard;
		}

		@Override
		public JPanel getToolBarPane() {
			return mainboard.getToolBarPanel();
		}
		
		private ReportDesigner getViewer(){
			//适配Mainboard
			Viewer viewer = mainboard.getCurrentView();
			if(viewer != null){
				if(viewer instanceof ReportDesigner){
					return (ReportDesigner) viewer;
				}
			}
			return null;
		}
		
	}

}