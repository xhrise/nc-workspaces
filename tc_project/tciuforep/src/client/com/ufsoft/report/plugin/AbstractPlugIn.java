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
 * @deprecated ��ֹʹ�á� v6��ֱ��ɾ���� liuyy.
 */
public abstract class AbstractPlugIn extends
		com.ufida.zior.plugin.AbstractPlugin implements IPlugIn {
	/** �������б� */
	private Vector m_vecListeners = new Vector();
	/** �����Ӧ�ı���*/
	private UfoReport m_report;

	/**
	 * ���һ��������
	 * 
	 * @param lis
	 *            IPlugInListener
	 * @throws NullPointerException
	 *             �������Ϊ���׳�.
	 */
	public void addListener(IPlugInListener lis) {
		if (lis == null)
			throw new NullPointerException();
		if (!m_vecListeners.contains(lis)) {
			m_vecListeners.addElement(lis);
		}
	}

	/**
	 * ɾ��һ����������
	 * 
	 * @param lis
	 *            ��ɾ���ļ�������
	 */
	public void removeListener(IPlugInListener lis) {
		if (lis == null) {
			return;
		}
		m_vecListeners.removeElement(lis);
	}

	/**
	 * ֪ͨ����ע���˵ļ�����ִ����Ӧ��
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
	 * ��һ�����û��������ʱ������Ĭ�ϵ������ࡣ
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
	/** �����������*/
	private IPluginDescriptor m_des;

	/**
	 * ��ȡ��������������m_des
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
	 * �������������ĳ��󷽷��������������ʵ��
	 * @return IPluginDescriptor
	 */
	protected abstract IPluginDescriptor createDescriptor();

	/**
	 * ��ȡ�ò����Ӧ�ı���ʵ��
	 * @return m_report��
	 */
	public final UfoReport getReport() {
		//����mainboard
		if(m_report == null){
			m_report = new UfoReport2Mainboard(getMainboard());
		}
		return m_report;
	}

	/**
	 * ��ȡ�ò����Ӧ�ı���ʵ��������ģ��
	 * @return CellsModel
	 */
	public CellsModel getCellsModel() {
		return getReport().getCellsModel();
	}

	/**
	 * ���øò����Ӧ�ı���ʵ��
	 * @param m_report
	 *            Ҫ���õ� m_report��
	 */
	public void setReport(UfoReport report) {
		m_report = report;
	}

	/**
	 * ����
	 */
	private boolean dirty = false;

	/**
	 * �������Ϣ�Ƿ��޸�
	 * @return ���� dirty��
	 * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * ���ò������Ϣ�Ƿ��޸�
	 * @param dirty
	 *            Ҫ���õ� dirty��
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
				AppDebug.debug("ִ�ж�����" + ext.getClass().getName());
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
			//��ó־ý���������
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
			//����Mainboard
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