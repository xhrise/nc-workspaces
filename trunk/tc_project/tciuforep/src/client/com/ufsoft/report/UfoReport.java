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
 * @deprecated 禁止使用。 v6将直接删除。 liuyy.
 */

public class UfoReport extends JPanel implements IDockingContainer { // JRootPane
																		// { //
	private static final long serialVersionUID = 3958873967528202188L;

	/** 插件管理器 */
	private PluginManager m_pm;

	/** 状态栏 */
	private ReportStatusBar m_StatusBar;

	/** 菜单栏 */
	private ReportMenuBar m_MenuBar;
	/** 表格控件 */
	private UFOTable m_table;
	/** 报表上下文 */
	private ContextVO m_contextVO;
	/** 报表主面板(不含菜单) */
	private JPanel m_MainPane = null;
	/** 工具条面板 */
	private JPanel m_ToolBarPane = null;
	/** 包含表格和导航栏的面板 */
	private ReportNavPanel m_navPanel;

	private Hashtable<String, ArrayList<Component>> m_hashUIComps = new Hashtable<String, ArrayList<Component>>();
	/**
	 * 报表显示风格
	 */
	private ReportStyle m_reportStyle = new ReportStyle();

	/** 当前所处的状态 */
	private int m_operationState;
	/**
	 * <code>OPERATION_FORMAT</code> 格式定义状态
	 */
	public final static int OPERATION_FORMAT = ReportContextKey.OPERATION_FORMAT;
	/**
	 * <code>OPERATION_INPUT</code> 数据录入状态
	 */
	public final static int OPERATION_INPUT = ReportContextKey.OPERATION_INPUT;

	/**
	 * 汇总结果溯源状态
	 */
	public final static int OPERATION_TOTALSOURCE = 3;

	public final static int OPERATION_PRINT = 4;

	private Component m_focusComp;

	private PopupMenuListener m_rightMouseListener;

	private JPanel m_contentPane;

	// liuyy 为适配新框架
	private EventManager eventManager = null;

	public UfoReport() {

	}

	/**
	 * 构造函数 PluginRegister pr： 插件注册器，不同业务应用，使用不同插件注册器。
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
		initLang(contextVO);// @edit by wangyga at 2009-3-3,下午08:13:43 先初始化多语环境
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
		// // 设置环境
		// Environment.setReport(this);
		String strRepName = MultiLang.getString("miuforep000031");
		setName(strRepName);
		Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dimScreen.width - 10, dimScreen.height - 60);
		// 初始化表格控件以及菜单栏,工具栏和状态栏
		initMainFrame(table);
		getTable().getCells().setCellsAuthorization(new ReportAuth(getTable().getCells(),getContext()));
		setOperationState(m_operationState);
		initRightMouseListener();
  
		pr.setReport(this);
		pr.register();

	}

	/**
	 * @i18n miufo00113=出现异常。Key：
	 */
	private void initStyle(boolean isAddOffice) {
		if(true)// @edit by ll at 2009-5-19,下午01:22:16
			return;
		/* 设置自定义外观 */
		try {
			// 设置新的默认外观 （NC_USER: 用完后请恢复原有的外观！）
			if (isAddOffice) {
				UIManager.setLookAndFeel(new Office2003LookAndFeel());
			}
			// UIManager.put("MenuItem.acceleratorDelimiter", new String("+"));
			// 设置新的用户默认属性
			FontUIResource fontRes = new FontUIResource(UIStyle.DIALOGFONT);
			java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
			// 替换所有的缺省字体.
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
	 * 从环境中初始化多语言,解决预算加载UfoReport多语问题
	 * 
	 * @create by wangyga at 2008-12-29,上午11:26:50
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
		// 设置当前java虚拟机的语言类型。
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
	 * 给各个子组件添加右键菜单扩展。注意该事件应该在最后响应
	 */
	public void initRightMouseListener() {
		addGlobalPopMenuSupport(getContentPane(), getRightMouseListener());
	}

	/**
	 * 使其子组件支持右键弹出菜单。 void
	 */
	private void addGlobalPopMenuSupport(Container container, MouseListener lis) {
		// liuyy+，只有表格才显示右键。
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
	 * 当分栏后组件添加了新组件后，执行此方法。 void
	 */
	public void resetGlobalPopMenuSupport() {
		removeGlobalPopMenuSupport();
		addGlobalPopMenuSupport(getContentPane(), getRightMouseListener());
	}

	/**
	 * 当复杂报表中，需要去除子报表的右键监听。 void
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
	 * 通知工具栏改变可用状态。
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
	 * 刷新插件组件状态 应用于undo、导航等插件
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
		// 初始化表格控件以及菜单栏,工具栏和状态栏
		initMainFrame(createUFOTable(cellsModel));
		getTable().getCells().setCellsAuthorization(new ReportAuth(getTable().getCells(),getContext()));

	}

	/**
	 * 创建表格控件
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
	 * 生产Applet中面板显示内容。
	 */
	private void initMainFrame(UFOTable table) {
		m_MainPane = this;// new JPanel();
		m_MainPane.setLayout(new BorderLayout());
		// 菜单条
		m_MenuBar = new ReportMenuBar(this);
		// 状态条
		m_StatusBar = new ReportStatusBar(this);
		// 表格控件
		m_table = table;

		// 工具栏和工具栏菜单
		m_ToolBarPane = new ReportToolBarPanel();

		m_navPanel = new ReportNavPanel(m_table);
		m_MainPane.add(m_ToolBarPane, BorderLayout.NORTH);
		m_MainPane.add(m_navPanel, BorderLayout.CENTER);
		m_MainPane.add(m_StatusBar, BorderLayout.SOUTH);
		//
		// // 添加菜单条
		// setJMenuBar(m_MenuBar);

		// 添加主面板
		// getContentPane().add(m_MainPane);
	}

	/**
	 * 添加插件信息。
	 */
	public void addPlugIn(String piClassName) {
		addPlugIn(piClassName, getPluginManager().createNewPlugin(piClassName));
	}

	public void addPlugIn(String piName, IPlugIn piInstance) {
		getPluginManager().addPlugIn(piName, piInstance);
		addPluginExtImpl(piName);
	}

	/**
	 * 重新生成插件
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
	 * 卸载插件 liuyy
	 * 
	 * @param plClzName
	 */
	public void removePlugIn(String plClzName) {
		removePluginExtImpl(plClzName);
		getPluginManager().removePlugIn(plClzName);
	}

	/**
	 * 删除界面组件
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
	 * 维护插件与生成的界面组件关系
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
	 * 卸载插件实现 liuyy
	 * 
	 * @param piName
	 */
	public void removePluginExtImpl(String piName) {
		IPlugIn plugin = getPluginManager().getPlugin(piName);
		if (plugin == null) {
			return;
		}

		removeUIComp(piName);

		// removeListener(plugin);// 删除组件的监听器
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
					Component comp = addStatusBar(ext);// 注册组件的状态栏
					mngUIComp(piName, new Component[] { comp });

				}// else
				if (ext instanceof IActionExt) {
					Component[] comps = addActionExt((IActionExt) ext);
					mngUIComp(piName, comps);

				}// else
				if (ext instanceof INavigationExt) {
					addNavPanel(ext);// 注册导航栏
				}
			}
		}
		addDataRE(plugin);// 注册新的数据编辑器，数据渲染器。
		addListener(plugin);// 注册组件的监听器
	}

	/**
	 * 添加工具栏项目
	 */
	private Component addToolBarItem(IActionExt ext, ActionUIDes uiDes) {
		String BarName = uiDes.getGroup();
		// 获取工具条
		if (BarName == null) { // 名称为空则加入系统默认的工具条
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

	/** 添加右键菜单 */
	private Component addRightMenu(IActionExt ext, ActionUIDes uiDes) {
		JComponent comp = MenuUtil.createActionComp(ext, uiDes, this);

		JPopupMenu pop = getRightMouseListener().getPopupMenu();

		JComponent parent = MenuUtil.getCompByPath(uiDes.getPaths(), 0, pop,
				this, uiDes.getGroup());
		MenuUtil.addCompToParent(comp, parent, uiDes);
		return comp;
	}

	/** 添加导航栏 */
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
	 * 重绘区域。 liuyy
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
				comp = addToolBarItem(ext, uiDesArr[i]);// 注册组件工具栏
			} else if (uiDesArr[i].isPopup()) {
				comp = addRightMenu(ext, uiDesArr[i]);// 注册组件的右键菜单
			} else {
				comp = m_MenuBar.addExtension(ext, uiDesArr[i]);// 注册组件菜单
			}

			list.add(comp);

		}
		return list.toArray(new Component[0]);
	}

	/**
	 * 删除插件信息从表格数据模型。 liuyy，2007-10-17
	 * 
	 * @param plugin
	 */
	protected void removeListener(IPlugIn plugin) {
		CellsModel cellModel = getTable().getCells().getDataModel();
		// 向报表工,具中注册插件，其中只有键盘事件预先判断事件的权限。
		if (plugin instanceof UserActionListner) {
			getTable().removeUserActionListener((UserActionListner) plugin);// 用户键盘事件
		}
		if (plugin instanceof SelectListener) {// 选择模型
			SelectListener selLis = (SelectListener) plugin;
			cellModel.getSelectModel().removeSelectModelListener(selLis);
		}
		if (plugin instanceof HeaderModelListener) {// 行列模型
			HeaderModelListener headerLis = (HeaderModelListener) plugin;
			cellModel.getRowHeaderModel().removeHeaderModelListener(headerLis);
			cellModel.getColumnHeaderModel().removeHeaderModelListener(
					headerLis);

		}
		if (plugin instanceof TreeModelListener) {// 树模型
			// TreeModelListener treeLis = (TreeModelListener) plugin;

		}
		if (plugin instanceof CellsModelListener) {// 表格模型
			CellsModelListener cellLis = (CellsModelListener) plugin;
			cellModel.removeCellsModelListener(cellLis);
		}

		// 插件之间的注册。注意，插件数组的返回次序应该已经保证插件之间关联关系的正确性。
		String[] requiredPlugs = plugin.getDescriptor()
				.getPluginPrerequisites();
		if (requiredPlugs != null) {
			for (int i = 0; i < requiredPlugs.length; i++) {
				IPlugIn pluginReq = getPluginManager().getPlugin(
						requiredPlugs[i]);
				if (plugin instanceof IPlugInListener) {// 当前插件对于前置条件的插件需要监听。
					pluginReq.removeListener((IPlugInListener) plugin);
				}
			}
		}

	}

	/**
	 * 注册插件信息到表格数据模型中。
	 * 
	 * @param plugin
	 */
	private void addListener(IPlugIn plugin) {
		CellsModel cellModel = getTable().getCells().getDataModel();
		// 向报表工,具中注册插件，其中只有键盘事件预先判断事件的权限。
		if (plugin instanceof UserActionListner) {
			getTable().addUserActionListener((UserActionListner) plugin);// 用户键盘事件
		}
		if (plugin instanceof SelectListener) {// 选择模型
			SelectListener selLis = (SelectListener) plugin;
			cellModel.getSelectModel().addSelectModelListener(selLis);
		}
		if (plugin instanceof HeaderModelListener) {// 行列模型
			HeaderModelListener headerLis = (HeaderModelListener) plugin;
			cellModel.getRowHeaderModel().addHeaderModelListener(headerLis);
			cellModel.getColumnHeaderModel().addHeaderModelListener(headerLis);

		}
		if (plugin instanceof TreeModelListener) {// 树模型
			// TreeModelListener treeLis = (TreeModelListener) plugin;

		}
		if (plugin instanceof CellsModelListener) {// 表格模型
			CellsModelListener cellLis = (CellsModelListener) plugin;
			cellModel.addCellsModelListener(cellLis);
		}

		// 插件之间的注册。注意，插件数组的返回次序应该已经保证插件之间关联关系的正确性。
		String[] requiredPlugs = plugin.getDescriptor()
				.getPluginPrerequisites();
		if (requiredPlugs != null) {
			for (int i = 0; i < requiredPlugs.length; i++) {
				IPlugIn pluginReq = getPluginManager().getPlugin(
						requiredPlugs[i]);
				if (plugin instanceof IPlugInListener) {// 当前插件对于前置条件的插件需要监听。
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
	 * @return 得到表格控件
	 */
	public UFOTable getTable() {
		return m_table;
	}

	/**
	 * 设置表格控件
	 * 
	 * @param table
	 */
	public void setTable(UFOTable table) {
		m_table = table;
	}

	/**
	 * 结束编辑状态
	 * 
	 * @return boolean 是否结束成功。
	 */
	public boolean stopCellEditing() {
		SheetCellEditor editor = getTable().getCellEditor();
		if (editor != null) {
			return editor.stopCellEditing();
		}
		return true;
	}

	/**
	 * @return 得到工具条
	 */
	public JToolBar[] getToolBar() {
		Component[] comps = getToolBarPane().getComponents();
		return (JToolBar[]) Arrays.asList(comps).toArray(new JToolBar[0]);
	}

	// /**
	// * @return 得到菜单条
	// */
	// public ReportMenuBar getReportMB() {
	// return m_MenuBar;
	// }
	/**
	 * 
	 * @return 得到状态条
	 */
	public ReportStatusBar getStatusBar() {
		return m_StatusBar;
	}

	public ReportNavPanel getReportNavPanel() {
		return m_navPanel;
	}

	/**
	 * @return true:退出系统；false:不退出
	 * @i18n report00030=是否保存修改过的内容？
	 */
	public boolean exit() {
		if (isDirty()) {
			// 提示是否保存。
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
	 * 数据是否脏。检查每个插件中的标记。
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
	 * 保存当前报表工具的信息。
	 */
	public void store() {
		IPlugIn[] plugins = getPluginManager().getAllPlugin();
		IPlugIn plugin = null;
		for (int i = 0; i < plugins.length; i++) {
			plugin = plugins[i];
			plugin.store();
		}
		// setDirty(false); //noted by ll, 避免多次调用allPlugin.store()
		getCellsModel().setDirty(false);

	}

	/**
	 * @return 返回 报表显示风格。
	 */
	public ReportStyle getReportStyle() {
		return m_reportStyle;
	}

	/**
	 * @param reportStyle
	 *            要设置的 报表显示风格。
	 */
	public void setReportStyle(ReportStyle reportStyle) {
		m_reportStyle = reportStyle;
		// 修改页面显示的风格.
		getTable().setRowHeaderVisible(m_reportStyle.isShowRowHeader());
		getTable().setColHeaderVisible(m_reportStyle.isShowColHeader());
		getTable().getCells().setGridColor(m_reportStyle.getGrid());
		// 显示比例
		getTable().setViewScale((m_reportStyle.getPercent() * 0.01));
		// 为0是否显示.
		repaint();
	}

	/**
	 * @return 返回 ReportMenuBar。
	 */
	public ReportMenuBar getReportMenuBar() {
		return m_MenuBar;
	}

	/**
	 * 设置UfoReport激活是的活动组件,为区域参照用， 当区域参照用对话结束是，并将次置空(null)
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
	 *            要设置的 dirty。
	 */
	public void setDirty(boolean dirty) {
		getCellsModel().setDirty(false);
		// 插件脏标志由store方法修改。
		IPlugIn[] plugins = getPluginManager().getAllPlugin();
		for (IPlugIn pi : plugins) {
			pi.store();
		}
	}

	/**
	 * 得到当前的操作状态。
	 * 
	 * @return 参见常量定义
	 */
	public int getOperationState() {
		return m_operationState;
	}

	/**
	 * 设置当前的操作状态。
	 * 
	 * @param operationState
	 *            参见常量定义
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
	 * 得到报表目录信息。 创建日期：(2004-4-19 15:39:37)
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
	 * 得到当前表页的表格模型。
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
	 * 得到焦点组件所在的UfoReport.
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
	 * 得到当前Frame,用于生成Dialog. 注意,无论是Frame或者是Dialog都可以使用这个方法.
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
	 * 设置是否是只读表。
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

	
	//以下方法为兼容ZIOR框架
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