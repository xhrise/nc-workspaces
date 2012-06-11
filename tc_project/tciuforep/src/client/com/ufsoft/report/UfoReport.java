package com.ufsoft.report;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.FocusManager;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.docking.core.DockingManager;
import com.ufida.zior.docking.dockbar.DockbarManager;
import com.ufida.zior.docking.view.DockingViewport;
import com.ufida.zior.event.EventManager;
import com.ufida.zior.perspective.PerspectiveManager;
import com.ufida.zior.view.IDockingContainer;
import com.ufsoft.report.constant.UIStyle;
import com.ufsoft.report.lookandfeel.Office2003LookAndFeel;
import com.ufsoft.report.menu.MenuUtil;
import com.ufsoft.report.menu.UFButton;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.INavigationExt;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.plugin.IPlugInListener;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.plugin.IStatusBarExt;
import com.ufsoft.report.plugin.PluginManager;
import com.ufsoft.report.toolbar.dropdown.JPopupPanelButton;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsModelListener;
import com.ufsoft.table.IArea;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.event.HeaderModelListener;
import com.ufsoft.table.header.TreeModelListener;
import com.ufsoft.table.re.SheetCellEditor;

/**
 * @deprecated ��ֹʹ�á� v6��ֱ��ɾ���� liuyy.
 */

public class UfoReport extends JPanel implements IDockingContainer { // JRootPane
																		// { //
	private static final long serialVersionUID = 3958873967528202188L;

	/** ��������� */
	private PluginManager m_pm;

	/** ״̬�� */
	private ReportStatusBar m_StatusBar;

	/** �˵��� */
	private ReportMenuBar m_MenuBar;
	/** ���ؼ� */
	private UFOTable m_table;
	/** ���������� */
	private ContextVO m_contextVO;
	/** ���������(�����˵�) */
	private JPanel m_MainPane = null;
	/** ��������� */
	private JPanel m_ToolBarPane = null;
	/** �������͵���������� */
	private ReportNavPanel m_navPanel;

	private Hashtable<String, ArrayList<Component>> m_hashUIComps = new Hashtable<String, ArrayList<Component>>();
	/**
	 * ������ʾ���
	 */
	private ReportStyle m_reportStyle = new ReportStyle();

	/** ��ǰ������״̬ */
	private int m_operationState;
	/**
	 * <code>OPERATION_FORMAT</code> ��ʽ����״̬
	 */
	public final static int OPERATION_FORMAT = ReportContextKey.OPERATION_FORMAT;
	/**
	 * <code>OPERATION_INPUT</code> ����¼��״̬
	 */
	public final static int OPERATION_INPUT = ReportContextKey.OPERATION_INPUT;

	/**
	 * ���ܽ����Դ״̬
	 */
	public final static int OPERATION_TOTALSOURCE = 3;

	public final static int OPERATION_PRINT = 4;

	private Component m_focusComp;

	private PopupMenuListener m_rightMouseListener;

	private JPanel m_contentPane;

	// liuyy Ϊ�����¿��
	private EventManager eventManager = null;

	public UfoReport() {

	}

	/**
	 * ���캯�� PluginRegister pr�� ���ע��������ͬҵ��Ӧ�ã�ʹ�ò�ͬ���ע������
	 */
	public UfoReport(int oper, ContextVO contextVO, CellsModel cellsModel,
			PluginRegister pr) {
		this(oper, contextVO, cellsModel);
		pr.setReport(this);
		pr.register();
	}

	public UfoReport(int oper, ContextVO contextVO) {
		this(oper, contextVO, null);
	}

	public UfoReport(int oper, ContextVO contextVO, CellsModel cellsModel) {
		super();
		initLang(contextVO);// @edit by wangyga at 2009-3-3,����08:13:43 �ȳ�ʼ�����ﻷ��
		initStyle(contextVO != null ? contextVO.isReportStyle() : true);
		if(contextVO!=null)
		 contextVO.setAttribute(ReportContextKey.OPERATION_STATE, oper);
		m_contextVO = contextVO;
		init(cellsModel);
		setOperationState(oper);
		initRightMouseListener();
	}

	public UfoReport(UFOTable table, PluginRegister pr) {
		super();

		initStyle(true);
		m_operationState = ReportContextKey.OPERATION_PRINT;
		if(m_contextVO==null){
			m_contextVO=new ContextVO();
		}
		m_contextVO.setAttribute(ReportContextKey.OPERATION_STATE,
					m_operationState);
		// // ���û���
		// Environment.setReport(this);
		String strRepName = MultiLang.getString("miuforep000031");
		setName(strRepName);
		Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dimScreen.width - 10, dimScreen.height - 60);
		// ��ʼ�����ؼ��Լ��˵���,��������״̬��
		initMainFrame(table);
		getTable().getCells().setCellsAuthorization(new ReportAuth(getTable().getCells(),getContext()));
		setOperationState(m_operationState);
		initRightMouseListener();
  
		pr.setReport(this);
		pr.register();

	}

	/**
	 * @i18n miufo00113=�����쳣��Key��
	 */
	private void initStyle(boolean isAddOffice) {
		if(true)// @edit by ll at 2009-5-19,����01:22:16
			return;
		/* �����Զ������ */
		try {
			// �����µ�Ĭ����� ��NC_USER: �������ָ�ԭ�е���ۣ���
			if (isAddOffice) {
				UIManager.setLookAndFeel(new Office2003LookAndFeel());
			}
			// UIManager.put("MenuItem.acceleratorDelimiter", new String("+"));
			// �����µ��û�Ĭ������
			FontUIResource fontRes = new FontUIResource(UIStyle.DIALOGFONT);
			java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
			// �滻���е�ȱʡ����.
			while (keys.hasMoreElements()) {
				Object key = null;
				try {
					key = keys.nextElement();
					Object value = UIManager.get(key);
					if (value instanceof javax.swing.plaf.FontUIResource)
						UIManager.put(key, fontRes);
				} catch (Throwable e) {
					System.out.println(MultiLang.getString("miufo00113") + key);
					AppDebug.debug(e);
				}

			}
			UIManager.put("Menu.font", new FontUIResource(UIStyle.MENUFONT));

			SwingUtilities.updateComponentTreeUI(this);// getContentPane());
		} catch (Exception e1) {
			AppDebug.debug(e1);
		}

	}

	private boolean isFloatableToolBar(){
		if(getContext()!=null&&getContext().getAttribute(ReportContextKey.REPORT_TOOLBAR_FLOATABLE) != null){
			return (Boolean)getContext().getAttribute(ReportContextKey.REPORT_TOOLBAR_FLOATABLE);
		}
		return true;
	}
	
	/**
	 * �ӻ����г�ʼ��������,���Ԥ�����UfoReport��������
	 * 
	 * @create by wangyga at 2008-12-29,����11:26:50
	 * 
	 * @param contextVO
	 */
	private void initLang(ContextVO contextVO) {
		if (contextVO == null) {
			return;
		}
		Object currentLangObj = contextVO
				.getAttribute(ReportContextKey.CURRENT_LANG);
		if (currentLangObj == null) {
			return;
		}
		String strLocalCode = currentLangObj.toString();
		System.setProperty("localCode", strLocalCode);
		// ���õ�ǰjava��������������͡�
		Locale curLocale = null;
		if (strLocalCode.equals("simpchn")) {
			curLocale = Locale.SIMPLIFIED_CHINESE;
		} else if (strLocalCode.equals("tradchn")) {
			curLocale = Locale.TRADITIONAL_CHINESE;
		} else if (strLocalCode.equals("english")) {
			curLocale = Locale.US;
		}
		Locale.setDefault(curLocale);
		JComponent.setDefaultLocale(curLocale);
		UIManager.getDefaults().setDefaultLocale(curLocale);
		UIManager.getLookAndFeelDefaults().setDefaultLocale(curLocale);

	}

	public PopupMenuListener getRightMouseListener() {
		if (m_rightMouseListener == null) {
			m_rightMouseListener = new PopupMenuListener(this);
		}
		return m_rightMouseListener;
	}

	/**
	 * ���������������Ҽ��˵���չ��ע����¼�Ӧ���������Ӧ
	 */
	public void initRightMouseListener() {
		addGlobalPopMenuSupport(getContentPane(), getRightMouseListener());
	}

	/**
	 * ʹ�������֧���Ҽ������˵��� void
	 */
	private void addGlobalPopMenuSupport(Container container, MouseListener lis) {
		// liuyy+��ֻ�б�����ʾ�Ҽ���
		if (StateUtil.isCPane1THeader(this, container)) {
			container.addMouseListener(lis);
		}

		Component[] coms = container.getComponents();
		for (Component comp : coms) {
			if (comp instanceof Container) {
				addGlobalPopMenuSupport((Container) comp, lis);
			}
		}
	}

	/**
	 * �����������������������ִ�д˷����� void
	 */
	public void resetGlobalPopMenuSupport() {
		removeGlobalPopMenuSupport();
		addGlobalPopMenuSupport(getContentPane(), getRightMouseListener());
	}

	/**
	 * �����ӱ����У���Ҫȥ���ӱ�����Ҽ������� void
	 */
	public void removeGlobalPopMenuSupport() {
		removeGlobalPopMenuSupport(getContentPane(), getRightMouseListener());
	}

	private void removeGlobalPopMenuSupport(Container c, MouseListener lis) {
		c.removeMouseListener(lis);

		Component[] coms = c.getComponents();
		for (int i = 0; i < coms.length; i++) {
			if (coms[i] instanceof Container) {
				removeGlobalPopMenuSupport((Container) coms[i], lis);
			}
		}
	}

	public void requestFocus() {
		AppDebug.debug("UfoReport.requestFocus()");
		super.requestFocus();
	}

	/**
	 * ֪ͨ�������ı����״̬��
	 * 
	 * @param component
	 *            void
	 */
	private void focusChanged(Component focusComp) {
		JToolBar[] bars = getToolBar();
		if (bars == null)
			return;
		for (int i = 0; i < bars.length && bars[i] instanceof ReportToolBar; i++) {
			((ReportToolBar) bars[i]).adjustEnabled(focusComp);
		}
	}

	/**
	 * ˢ�²�����״̬ Ӧ����undo�������Ȳ��
	 * 
	 * @param pluginClzName
	 */
	public void refershPluginState(String pluginClzName) {
		ArrayList<Component> comps = this.m_hashUIComps.get(pluginClzName);

		if(comps == null || comps.size() ==0){
			return;
		}
		for (Component comp : comps) {
			if (comp instanceof UFButton) {
				((UFButton) comp).adjustEnabled(this);
			} else if (comp instanceof JPopupPanelButton) {
				((JPopupPanelButton) comp).adjustEnabled(this);
			}

		}

	}

	private void init(CellsModel cellsModel) {
		String strRepName = MultiLang.getString("miuforep000031");
		setName(strRepName);
		Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dimScreen.width - 10, dimScreen.height - 60);
		// ��ʼ�����ؼ��Լ��˵���,��������״̬��
		initMainFrame(createUFOTable(cellsModel));
		getTable().getCells().setCellsAuthorization(new ReportAuth(getTable().getCells(),getContext()));

	}

	/**
	 * �������ؼ�
	 * 
	 * @param cellsModel
	 * @return
	 */
	public UFOTable createUFOTable(CellsModel cellsModel) {
		if (cellsModel != null) {
			return UFOTable.createTableByCellsModel(cellsModel);
		} else {
			return UFOTable.createInfiniteTable(true, true);
		}
	}

	/**
	 * ����Applet�������ʾ���ݡ�
	 */
	private void initMainFrame(UFOTable table) {
		m_MainPane = this;// new JPanel();
		m_MainPane.setLayout(new BorderLayout());
		// �˵���
		m_MenuBar = new ReportMenuBar(this);
		// ״̬��
		m_StatusBar = new ReportStatusBar(this);
		// ���ؼ�
		m_table = table;

		// �������͹������˵�
		m_ToolBarPane = new ReportToolBarPanel();

		m_navPanel = new ReportNavPanel(m_table);
		m_MainPane.add(m_ToolBarPane, BorderLayout.NORTH);
		m_MainPane.add(m_navPanel, BorderLayout.CENTER);
		m_MainPane.add(m_StatusBar, BorderLayout.SOUTH);
		//
		// // ��Ӳ˵���
		// setJMenuBar(m_MenuBar);

		// ��������
		// getContentPane().add(m_MainPane);
	}

	/**
	 * ��Ӳ����Ϣ��
	 */
	public void addPlugIn(String piClassName) {
		addPlugIn(piClassName, getPluginManager().createNewPlugin(piClassName));
	}

	public void addPlugIn(String piName, IPlugIn piInstance) {
		getPluginManager().addPlugIn(piName, piInstance);
		addPluginExtImpl(piName);
	}

	/**
	 * �������ɲ��
	 * 
	 * @param plClzName
	 */
	public void refreshPlugIn(String plClzName) {
		if (plClzName == null || plClzName.equals("")
				|| getPluginManager().getPlugin(plClzName) == null) {
			return;
		}

		removePlugIn(plClzName);

		addPlugIn(plClzName);
	}

	/**
	 * ж�ز�� liuyy
	 * 
	 * @param plClzName
	 */
	public void removePlugIn(String plClzName) {
		removePluginExtImpl(plClzName);
		getPluginManager().removePlugIn(plClzName);
	}

	/**
	 * ɾ���������
	 * 
	 * @param plclzName
	 */
	private void removeUIComp(String plclzName) {
		ArrayList<Component> list = m_hashUIComps.get(plclzName);
		if (list == null || list.size() < 1) {
			return;
		}

		m_hashUIComps.remove(plclzName);
		Component[] comps = list.toArray(new Component[0]);
		for (int i = 0; i < comps.length; i++) {
			comps[i].getParent().remove(comps[i]);
		}
	}

	/**
	 * ά����������ɵĽ��������ϵ
	 * 
	 * @param plclzName
	 * @param comps
	 */
	private void mngUIComp(String plclzName, Component[] comps) {
		if (comps == null || comps.length < 1) {
			return;
		}
		ArrayList<Component> list = m_hashUIComps.get(plclzName);
		if (list == null) {
			list = new ArrayList<Component>();
			m_hashUIComps.put(plclzName, list);
		}

		list.addAll(Arrays.asList(comps));

	}

	/**
	 * ж�ز��ʵ�� liuyy
	 * 
	 * @param piName
	 */
	public void removePluginExtImpl(String piName) {
		IPlugIn plugin = getPluginManager().getPlugin(piName);
		if (plugin == null) {
			return;
		}

		removeUIComp(piName);

		// removeListener(plugin);// ɾ������ļ�����
	}

	public void addPluginExtImpl(String piName) {
		IPlugIn plugin = getPluginManager().getPlugin(piName);
		plugin.setReport(this);
		IPluginDescriptor des = plugin.getDescriptor();
		plugin.startup();
		IExtension[] exts = des.getExtensions();
		if (exts != null) {
			IExtension ext = null;
			for (int j = 0; j < exts.length; j++) {
				ext = exts[j];
				if (ext instanceof IStatusBarExt) {
					Component comp = addStatusBar(ext);// ע�������״̬��
					mngUIComp(piName, new Component[] { comp });

				}// else
				if (ext instanceof IActionExt) {
					Component[] comps = addActionExt((IActionExt) ext);
					mngUIComp(piName, comps);

				}// else
				if (ext instanceof INavigationExt) {
					addNavPanel(ext);// ע�ᵼ����
				}
			}
		}
		addDataRE(plugin);// ע���µ����ݱ༭����������Ⱦ����
		addListener(plugin);// ע������ļ�����
	}

	/**
	 * ��ӹ�������Ŀ
	 */
	private Component addToolBarItem(IActionExt ext, ActionUIDes uiDes) {
		String BarName = uiDes.getGroup();
		// ��ȡ������
		if (BarName == null) { // ����Ϊ�������ϵͳĬ�ϵĹ�����
			BarName = MultiLang.getString("defaultToolBar");
		}

		JToolBar[] toolBars = getToolBar();
		ReportToolBar toolBar = null;
		for (int i = 0; i < toolBars.length; i++) {
			if (BarName.equals(toolBars[i].getName())
					&& toolBars[i] instanceof ReportToolBar) {
				toolBar = (ReportToolBar) toolBars[i];
				break;
			}
		}
		if (toolBar == null) {
			toolBar = new ReportToolBar(this);
			toolBar.setName(BarName);
			toolBar.setFloatable(isFloatableToolBar());
			getToolBarPane().add(toolBar);
		}

		return toolBar.addToolItem(ext, uiDes);

	}

	/** ����Ҽ��˵� */
	private Component addRightMenu(IActionExt ext, ActionUIDes uiDes) {
		JComponent comp = MenuUtil.createActionComp(ext, uiDes, this);

		JPopupMenu pop = getRightMouseListener().getPopupMenu();

		JComponent parent = MenuUtil.getCompByPath(uiDes.getPaths(), 0, pop,
				this, uiDes.getGroup());
		MenuUtil.addCompToParent(comp, parent, uiDes);
		return comp;
	}

	/** ��ӵ����� */
	private void addNavPanel(IExtension ext) {
		if (ext instanceof INavigationExt) {
			INavigationExt navExt = (INavigationExt) ext;
			m_navPanel.addExtent(navExt);
		}
	}

	private Component addStatusBar(IExtension ext) {
		if (ext instanceof IStatusBarExt) {
			IStatusBarExt statusBarExt = (IStatusBarExt) ext;
			return m_StatusBar.addExtension(statusBarExt);
		}
		return null;
	}

	public void updateUI() {
		super.updateUI();
	}

	/**
	 * �ػ����� liuyy
	 * 
	 * @param area
	 */
	public void repaint(IArea area) {
		Rectangle r = getCellsModel().getCellRect(area, true);
		repaint(r);
	}

	/**
	 * @param ext
	 *            void
	 */
	public Component[] addActionExt(IActionExt ext) {
		ArrayList<Component> list = new ArrayList<Component>();
		ActionUIDes[] uiDesArr = ext.getUIDesArr();

		for (int i = 0; uiDesArr != null && i < uiDesArr.length; i++) {
			Component comp = null;
			if (uiDesArr[i].isToolBar()) {
				comp = addToolBarItem(ext, uiDesArr[i]);// ע�����������
			} else if (uiDesArr[i].isPopup()) {
				comp = addRightMenu(ext, uiDesArr[i]);// ע��������Ҽ��˵�
			} else {
				comp = m_MenuBar.addExtension(ext, uiDesArr[i]);// ע������˵�
			}

			list.add(comp);

		}
		return list.toArray(new Component[0]);
	}

	/**
	 * ɾ�������Ϣ�ӱ������ģ�͡� liuyy��2007-10-17
	 * 
	 * @param plugin
	 */
	protected void removeListener(IPlugIn plugin) {
		CellsModel cellModel = getTable().getCells().getDataModel();
		// �򱨱�,����ע����������ֻ�м����¼�Ԥ���ж��¼���Ȩ�ޡ�
		if (plugin instanceof UserActionListner) {
			getTable().removeUserActionListener((UserActionListner) plugin);// �û������¼�
		}
		if (plugin instanceof SelectListener) {// ѡ��ģ��
			SelectListener selLis = (SelectListener) plugin;
			cellModel.getSelectModel().removeSelectModelListener(selLis);
		}
		if (plugin instanceof HeaderModelListener) {// ����ģ��
			HeaderModelListener headerLis = (HeaderModelListener) plugin;
			cellModel.getRowHeaderModel().removeHeaderModelListener(headerLis);
			cellModel.getColumnHeaderModel().removeHeaderModelListener(
					headerLis);

		}
		if (plugin instanceof TreeModelListener) {// ��ģ��
			// TreeModelListener treeLis = (TreeModelListener) plugin;

		}
		if (plugin instanceof CellsModelListener) {// ���ģ��
			CellsModelListener cellLis = (CellsModelListener) plugin;
			cellModel.removeCellsModelListener(cellLis);
		}

		// ���֮���ע�ᡣע�⣬�������ķ��ش���Ӧ���Ѿ���֤���֮�������ϵ����ȷ�ԡ�
		String[] requiredPlugs = plugin.getDescriptor()
				.getPluginPrerequisites();
		if (requiredPlugs != null) {
			for (int i = 0; i < requiredPlugs.length; i++) {
				IPlugIn pluginReq = getPluginManager().getPlugin(
						requiredPlugs[i]);
				if (plugin instanceof IPlugInListener) {// ��ǰ�������ǰ�������Ĳ����Ҫ������
					pluginReq.removeListener((IPlugInListener) plugin);
				}
			}
		}

	}

	/**
	 * ע������Ϣ���������ģ���С�
	 * 
	 * @param plugin
	 */
	private void addListener(IPlugIn plugin) {
		CellsModel cellModel = getTable().getCells().getDataModel();
		// �򱨱�,����ע����������ֻ�м����¼�Ԥ���ж��¼���Ȩ�ޡ�
		if (plugin instanceof UserActionListner) {
			getTable().addUserActionListener((UserActionListner) plugin);// �û������¼�
		}
		if (plugin instanceof SelectListener) {// ѡ��ģ��
			SelectListener selLis = (SelectListener) plugin;
			cellModel.getSelectModel().addSelectModelListener(selLis);
		}
		if (plugin instanceof HeaderModelListener) {// ����ģ��
			HeaderModelListener headerLis = (HeaderModelListener) plugin;
			cellModel.getRowHeaderModel().addHeaderModelListener(headerLis);
			cellModel.getColumnHeaderModel().addHeaderModelListener(headerLis);

		}
		if (plugin instanceof TreeModelListener) {// ��ģ��
			// TreeModelListener treeLis = (TreeModelListener) plugin;

		}
		if (plugin instanceof CellsModelListener) {// ���ģ��
			CellsModelListener cellLis = (CellsModelListener) plugin;
			cellModel.addCellsModelListener(cellLis);
		}

		// ���֮���ע�ᡣע�⣬�������ķ��ش���Ӧ���Ѿ���֤���֮�������ϵ����ȷ�ԡ�
		String[] requiredPlugs = plugin.getDescriptor()
				.getPluginPrerequisites();
		if (requiredPlugs != null) {
			for (int i = 0; i < requiredPlugs.length; i++) {
				IPlugIn pluginReq = getPluginManager().getPlugin(
						requiredPlugs[i]);
				if (plugin instanceof IPlugInListener) {// ��ǰ�������ǰ�������Ĳ����Ҫ������
					pluginReq.addListener((IPlugInListener) plugin);
				}
			}
		}

	}

	private void addDataRE(IPlugIn plugin) {
		// String[] extFmtNames = plugin.getSupportData();
		// if (extFmtNames != null) {
		// CellRenderAndEditor re = getTable().getReanderAndEditor();
		// for (int i = 0; i < extFmtNames.length; i++) {
		// SheetCellEditor edit = plugin.getDataEditor(extFmtNames[i]);
		// if (edit != null) {
		// re.registEditor(extFmtNames[i], edit);
		// }
		// SheetCellRenderer render = plugin.getDataRender(extFmtNames[i]);
		// if (render != null) {
		// re.registRender(extFmtNames[i], render);
		// }
		// }
		// }
	}

	/**
	 * @return �õ����ؼ�
	 */
	public UFOTable getTable() {
		return m_table;
	}

	/**
	 * ���ñ��ؼ�
	 * 
	 * @param table
	 */
	public void setTable(UFOTable table) {
		m_table = table;
	}

	/**
	 * �����༭״̬
	 * 
	 * @return boolean �Ƿ�����ɹ���
	 */
	public boolean stopCellEditing() {
		SheetCellEditor editor = getTable().getCellEditor();
		if (editor != null) {
			return editor.stopCellEditing();
		}
		return true;
	}

	/**
	 * @return �õ�������
	 */
	public JToolBar[] getToolBar() {
		Component[] comps = getToolBarPane().getComponents();
		return (JToolBar[]) Arrays.asList(comps).toArray(new JToolBar[0]);
	}

	// /**
	// * @return �õ��˵���
	// */
	// public ReportMenuBar getReportMB() {
	// return m_MenuBar;
	// }
	/**
	 * 
	 * @return �õ�״̬��
	 */
	public ReportStatusBar getStatusBar() {
		return m_StatusBar;
	}

	public ReportNavPanel getReportNavPanel() {
		return m_navPanel;
	}

	/**
	 * @return true:�˳�ϵͳ��false:���˳�
	 * @i18n report00030=�Ƿ񱣴��޸Ĺ������ݣ�
	 */
	public boolean exit() {
		if (isDirty()) {
			// ��ʾ�Ƿ񱣴档
			int value = JOptionPane.showConfirmDialog(this, MultiLang
					.getString("report00030"));
			if (value == JOptionPane.OK_OPTION) {
				store();
			} else if (value == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		return true;
	}

	/**
	 * �����Ƿ��ࡣ���ÿ������еı�ǡ�
	 * 
	 * @return boolean
	 */
	public boolean isDirty() {
		if (getCellsModel().isDirty()) {
			return true;
		}
		IPlugIn[] plugins = getPluginManager().getAllPlugin();
		IPlugIn plugin = null;
		for (int i = 0; i < plugins.length; i++) {
			plugin = plugins[i];
			if (plugin.isDirty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ���浱ǰ�����ߵ���Ϣ��
	 */
	public void store() {
		IPlugIn[] plugins = getPluginManager().getAllPlugin();
		IPlugIn plugin = null;
		for (int i = 0; i < plugins.length; i++) {
			plugin = plugins[i];
			plugin.store();
		}
		// setDirty(false); //noted by ll, �����ε���allPlugin.store()
		getCellsModel().setDirty(false);

	}

	/**
	 * @return ���� ������ʾ���
	 */
	public ReportStyle getReportStyle() {
		return m_reportStyle;
	}

	/**
	 * @param reportStyle
	 *            Ҫ���õ� ������ʾ���
	 */
	public void setReportStyle(ReportStyle reportStyle) {
		m_reportStyle = reportStyle;
		// �޸�ҳ����ʾ�ķ��.
		getTable().setRowHeaderVisible(m_reportStyle.isShowRowHeader());
		getTable().setColHeaderVisible(m_reportStyle.isShowColHeader());
		getTable().getCells().setGridColor(m_reportStyle.getGrid());
		// ��ʾ����
		getTable().setViewScale((m_reportStyle.getPercent() * 0.01));
		// Ϊ0�Ƿ���ʾ.
		repaint();
	}

	/**
	 * @return ���� ReportMenuBar��
	 */
	public ReportMenuBar getReportMenuBar() {
		return m_MenuBar;
	}

	/**
	 * ����UfoReport�����ǵĻ���,Ϊ��������ã� ����������öԻ������ǣ��������ÿ�(null)
	 * 
	 * @param com
	 * @author caijie
	 * @since 3.1
	 */

	public void setActivatingComponent(Component com) {
		// m_comActivating = com;
	}

	/**
	 * @param dirty
	 *            Ҫ���õ� dirty��
	 */
	public void setDirty(boolean dirty) {
		getCellsModel().setDirty(false);
		// ������־��store�����޸ġ�
		IPlugIn[] plugins = getPluginManager().getAllPlugin();
		for (IPlugIn pi : plugins) {
			pi.store();
		}
	}

	/**
	 * �õ���ǰ�Ĳ���״̬��
	 * 
	 * @return �μ���������
	 */
	public int getOperationState() {
		return m_operationState;
	}

	/**
	 * ���õ�ǰ�Ĳ���״̬��
	 * 
	 * @param operationState
	 *            �μ���������
	 */
	public void setOperationState(int operationState) {
		m_operationState = operationState;
		if(getTable()!=null)
		  getTable().setOperationState(m_operationState);
	}

	/**
	 * @param contextVO
	 */
	public void setContextVO(ContextVO contextVO) {
		m_contextVO = contextVO;
	}

	/**
	 * �õ�����Ŀ¼��Ϣ�� �������ڣ�(2004-4-19 15:39:37)
	 * 
	 * @return com.ufsoft.iufo.reporttool.ContextVO
	 */
	public ContextVO getContextVo() {
		if (m_contextVO == null) {
			m_contextVO = new ContextVO();
		}
		return m_contextVO;
	}

	/**
	 * �õ���ǰ��ҳ�ı��ģ�͡�
	 * 
	 * @return CellsModel
	 */
	public CellsModel getCellsModel() {
		return getTable().getCellsModel();
	}

	/**
	 * @return PluginManager
	 */
	public PluginManager getPluginManager() {
		if (m_pm == null) {
			m_pm = new PluginManager(this);
		}
		return m_pm;
	}

	/**
	 * @return Component
	 */
	public Component getFocusComp() {
		return m_focusComp;
	}

	/**
	 * @param component
	 *            void
	 */
	public void setFocusComp(Component comp) {
		if (!(comp instanceof JMenuItem)) {
			m_focusComp = comp;
			focusChanged(comp);
		}
	}

	public JPanel getContentPane() {
		return this;// UIUtilities.getRootPane(this);
		//		
		// return UIUtilities.
		// if (m_contentPane == null) {
		// m_contentPane = (JPanel) super.getParent();//.getContentPane();
		// }
		// return m_contentPane;
	}

	/**
	 * �õ�����������ڵ�UfoReport.
	 * 
	 * @return UfoReport
	 */
	public static UfoReport getFocusReport() {
		Component com = FocusManager.getCurrentKeyboardFocusManager()
				.getFocusOwner();
		while (com != null) {
			if (com instanceof UfoReport) {
				return (UfoReport) com;
			} else {
				com = com.getParent();
			}
		}
		return null;
	}

	/**
	 * �õ���ǰFrame,��������Dialog. ע��,������Frame������Dialog������ʹ���������.
	 * 
	 * @return Frame
	 */
	public Frame getFrame() {
		return JOptionPane.getFrameForComponent(this);
	}

	public void setTableShow(boolean bShowNorthNav, boolean bShowRowCol,
			boolean bResetPopMenu) {
		Component midComp = getReportNavPanel().getMidComp();
		Component northComp = getReportNavPanel().getNorthComp();
		m_MainPane.removeAll();
		if (bShowNorthNav) {
			m_MainPane.add(northComp, BorderLayout.NORTH);
		}
		m_MainPane.add(midComp, BorderLayout.CENTER);
		if (bShowRowCol) {
			getTable().setRowHeaderVisible(true);
			getTable().setColHeaderVisible(true);

		} else {
			getTable().setRowHeaderVisible(false);
			getTable().setColHeaderVisible(false);
		}
		getReportMenuBar().setVisible(false);
		if (bResetPopMenu)
			resetGlobalPopMenuSupport();
		revalidate();
		repaint();
	}

	/**
	 * �����Ƿ���ֻ����
	 * 
	 * @param bReadOnly
	 * @param newReportAuth
	 */
	public void setReadOnly(boolean bReadOnly, ReportAuth newReportAuth) {
		if (bReadOnly) {
			getTable().getCells().setCellsAuthorization(new ReportAuthReadOnly());
		} else {
			if (newReportAuth == null) {
				getTable().getCells().setCellsAuthorization(new ReportAuth(getTable().getCells(),getContext()));
			} else {
				getTable().getCells().setCellsAuthorization(newReportAuth);
			}
		}
	}

	public JPanel getToolBarPane() {
		return m_ToolBarPane;
	}

	public JMenuBar getJMenuBar() {
		return m_MenuBar;
	}

	
	//���·���Ϊ����ZIOR���
	public void setEventManager(EventManager eventManager) {
		this.eventManager = eventManager;
	}

	public EventManager getEventManager() {
		if (eventManager == null) {
			eventManager = new EventManager();
//			eventManager.addHandler(new CellsModelSelectedHandler());
		}
		return eventManager;
	}

	public IContext getContext() {
		return this.m_contextVO;
	}

	public DockbarManager getDockbarManager() {
		return null;
	}

	public DockingManager getDockingManager() {
		return null;
	}

	public PerspectiveManager getPerspectiveManager() {
		return null;
	}

	public DockingViewport getRootDockingViewport() {
		return null;
	}

}