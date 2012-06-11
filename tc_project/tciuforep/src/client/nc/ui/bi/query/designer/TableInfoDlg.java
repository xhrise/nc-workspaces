/*
 * 创建日期 2005-5-24
 *
 */
package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.component.ObjectTreeView;
import nc.ui.pub.component.dialog.FindDlg;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.TableDefWithAlias;
import nc.vo.pub.core.BizObject;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.core.ObjectTree;
import nc.vo.pub.ddc.datadict.Datadict;
import nc.vo.pub.ddc.datadict.DatadictNode;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zjb
 * 
 * 数据字典选择框
 */
public class TableInfoDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//父组件
	private AbstractQueryDesignSetPanel m_parent = null;

	//用于判断是否选表重复的哈希表
	private Hashtable<String, String> m_hashTableId = null;

	//数据字典树
	private ObjectTreeView m_tree = null;

	//选中表定义
	private FromTableVO m_ft = null;

	private UIButton ivjBnCancel = null;

	private UIButton ivjBnOK = null;

	private UIPanel ivjPnSouth = null;

	private UIScrollPane ivjSclPnTree = null;

	private JPanel ivjUIDialogContentPane = null;

	private UIButton ivjBnAdd = null;

	private UIButton ivjBnFind = null;

	private UIComboBox ivjCbbBusiDatadict = null;

	private UIPanel ivjPnNorth = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener, ItemListener,
			MouseListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == TableInfoDlg.this.getBnCancel())
				connEtoC1(e);
			if (e.getSource() == TableInfoDlg.this.getBnOK())
				connEtoC2(e);
			if (e.getSource() == TableInfoDlg.this.getBnAdd())
				connEtoC3(e);
			if (e.getSource() == TableInfoDlg.this.getBnFind())
				connEtoC4(e);
		};

		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == TableInfoDlg.this.getCbbBusiDatadict())
				connEtoC6(e);
		};

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == TableInfoDlg.this.getPnSouth())
				connEtoC5(e);
		};

		public void mouseEntered(MouseEvent e) {
		};

		public void mouseExited(MouseEvent e) {
		};

		public void mousePressed(MouseEvent e) {
		};

		public void mouseReleased(MouseEvent e) {
		};
	};

	/**
	 * TableInfoDlg 构造子注解。
	 * @deprecated
	 */
	public TableInfoDlg() {
		super();
		initialize();
	}

	/**
	 * TableInfoDlg 构造子注解。
	 * 
	 * @param parent
	 *            Container
	 */
	public TableInfoDlg(Container parent) {
		super(parent);
		m_parent = (AbstractQueryDesignSetPanel) parent;
		initialize();
	}

	/**
	 * add
	 */
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
		doAdd();
	}

	/**
	 * cancel
	 */
	public void bnCancel_ActionPerformed(ActionEvent actionEvent) {
		closeCancel();
	}

	/**
	 * 查找
	 */
	public void bnFind_ActionPerformed(ActionEvent actionEvent) {
		doFind();
	}

	/**
	 * OK
	 */
	public void bnOK_ActionPerformed(ActionEvent actionEvent) {
		BizObject td = getSelTableDef();
		if (td == null) {
			MessageDialog.showWarningDlg(this,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */, NCLangRes
							.getInstance().getStrByID("10241201",
									"UPP10241201-000933")/*
														  * @res
														  * "未选中节点或选中的节点不是表"
														  */);
			return;
		}
		//获得表信息
		String		strNote = null;//5.01
		String tableCode = td.getID();
		if (td instanceof TableDefWithAlias){
			tableCode = ((TableDefWithAlias) td).getRealName();
			strNote = ((TableDefWithAlias) td).getNote();
		}
		String tableAlias = td.getID();
		String tableName = td.getDisplayName();
		//转换为VO
		m_ft = new FromTableVO();
		m_ft.setTablecode(tableCode);
		m_ft.setTablealias(tableAlias);
		m_ft.setTabledisname(tableName);
		m_ft.setNote(strNote);
		
		//重名处理
		if (m_hashTableId.containsKey(tableAlias)) {
			int iTemp = MessageDialog.showYesNoDlg(this, NCLangRes
					.getInstance().getStrByID("10241201", "UPP10241201-000099")/*
																			    * @res
																			    * "查询引擎"
																			    */, NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000932")/*
										  * @res "已经选择过同名表，是否添加本表？"
										  */);
			if (iTemp != UIDialog.ID_YES)
				return;
			//生成表别名和显示名
			//if (!(td instanceof TableDefWithAlias)) {
			int s = 2;
			while (m_hashTableId.containsKey(tableAlias + "_" + s))
				s++;
			m_ft.setTablealias(tableAlias + "_" + s);
			m_ft.setTabledisname(tableName + "_" + s);
			//}
		}
		closeOK();
	}

	/**
	 * connEtoC1: (BnCancel.action.actionPerformed(ActionEvent) -->
	 * TableInfoDlg.bnCancel_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnCancel_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC2: (BnOK.action.actionPerformed(ActionEvent) -->
	 * TableInfoDlg.bnOK_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC2(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnOK_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3: (BnAdd.action.actionPerformed(ActionEvent) -->
	 * TableInfoDlg.bnAdd_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnAdd_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4: (BnFind.action.actionPerformed(ActionEvent) -->
	 * TableInfoDlg.bnFind_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC4(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnFind_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * 查找表 创建日期：(2003-9-27 13:13:46)
	 * 
	 * @param strCode
	 *            java.lang.String
	 */
	private void doFind() {
		//获得数据字典实例
		Datadict dd = m_parent.getTabPn().getDatadict();
		//查找
		FindDlg dlg = new FindDlg(this, NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-000121")/* @res "查找" */, true);
		dlg.setObjectTrees(new ObjectTree[] { dd });
		if (dlg.showModal() == UIDialog.ID_OK) {
			ObjectNode objnode = dlg.getSearch();
			if (objnode != null)
				m_tree.selectNode(objnode);
		}
	}

	/**
	 * 定位 创建日期：(2003-9-27 13:13:46)
	 * 
	 * @param strCode
	 *            java.lang.String
	 */
	@SuppressWarnings("unused")
	private void doLocate(String strCode, String kind) {
		//获得目标树的根
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) m_tree
				.getModel().getRoot();
		//深度优先顺序遍历子树获得的枚举
		Enumeration enums = root.depthFirstEnumeration();
		while (enums.hasMoreElements()) {
			//获得下一个枚举元素
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) enums
					.nextElement();
			if (node.getUserObject() instanceof ObjectNode) {
				ObjectNode dn = (ObjectNode) node.getUserObject();
				//选中
				if (kind == null || kind.equals(dn.getKind())) {
					if (dn.getID().startsWith(strCode)) {
						setSelNode(node);
						break;
					}
				}
			}
		}
	}

	/**
	 * 返回 BnAdd 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnAdd() {
		if (ivjBnAdd == null) {
			try {
				ivjBnAdd = new UIButton();
				ivjBnAdd.setName("BnAdd");
				ivjBnAdd.setText(NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000330")/* @res "增加" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnAdd;
	}

	/**
	 * 返回 BnCancel 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000000")/* @res "取消" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnCancel;
	}

	/**
	 * 返回 BnFind 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnFind() {
		if (ivjBnFind == null) {
			try {
				ivjBnFind = new UIButton();
				ivjBnFind.setName("BnFind");
				ivjBnFind.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000121")/* @res "查找" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnFind;
	}

	/**
	 * 返回 BnOK 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnOK() {
		if (ivjBnOK == null) {
			try {
				ivjBnOK = new UIButton();
				ivjBnOK.setName("BnOK");
				ivjBnOK.setText(NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000001")/* @res "确定" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnOK;
	}

	/**
	 * 获得键盘监听 创建日期：(2003-9-27 14:25:02)
	 * 
	 * @return KeyListener
	 */
	@SuppressWarnings("unused")
	private KeyListener getKeyListener() {
		return null;
	}

	/**
	 * 返回 PnSouth 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnSouth() {
		if (ivjPnSouth == null) {
			try {
				ivjPnSouth = new UIPanel();
				ivjPnSouth.setName("PnSouth");
				ivjPnSouth.setLayout(getPnSouthFlowLayout());
				getPnSouth().add(getBnFind(), getBnFind().getName());
				getPnSouth().add(getBnAdd(), getBnAdd().getName());
				getPnSouth().add(getBnOK(), getBnOK().getName());
				getPnSouth().add(getBnCancel(), getBnCancel().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnSouth;
	}

	/**
	 * 返回 PnSouthFlowLayout 特性值。
	 * 
	 * @return FlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private FlowLayout getPnSouthFlowLayout() {
		FlowLayout ivjPnSouthFlowLayout = null;
		try {
			/* 创建部件 */
			ivjPnSouthFlowLayout = new FlowLayout();
			ivjPnSouthFlowLayout.setVgap(8);
			ivjPnSouthFlowLayout.setHgap(8);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnSouthFlowLayout;
	}

	/**
	 * 返回 SclPnTree 特性值。
	 * 
	 * @return UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private UIScrollPane getSclPnTree() {
		if (ivjSclPnTree == null) {
			try {
				ivjSclPnTree = new UIScrollPane();
				ivjSclPnTree.setName("SclPnTree");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnTree;
	}

	/**
	 * 获得选中的TABLE定义 创建日期：(2003-4-2 16:49:17)
	 * 
	 * @return nc.bs.com.datadict.TableDef
	 */
	private BizObject getSelTableDef() {
		BizObject td = null;
		//获得选中节点
		ObjectNode on = m_tree.getSelectedObjectNode();
		if (on != null && on.getKind() != null) {
			if (on.getKind().equals(DatadictNode.TableKind))
				td = on.getObject();
			else if (on.getKind().equals(DatadictNode.ViewKind))
				//视图定义处理
				td = on.getObject();
		}
		return td;
	}

	/**
	 * 获得选中的表VO 创建日期：(2003-4-2 16:49:17)
	 * 
	 * @return nc.bs.com.datadict.TableDef
	 */
	public FromTableVO getSelTableVO() {
		return m_ft;
	}

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * 
	 * @return JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new UIPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new BorderLayout());
				getUIDialogContentPane().add(getPnSouth(), "South");
				getUIDialogContentPane().add(getSclPnTree(), "Center");
				getUIDialogContentPane().add(getPnNorth(), "North");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 * @i18n miufo00155=--------- 未捕捉到的异常 ---------
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		AppDebug.debug("--------- 未捕捉到的异常 ---------");//@devTools System.out.println("--------- 未捕捉到的异常 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * 初始化连接
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	/* 警告：此方法将重新生成。 */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		getBnCancel().addActionListener(ivjEventHandler);
		getBnOK().addActionListener(ivjEventHandler);
		getBnAdd().addActionListener(ivjEventHandler);
		getBnFind().addActionListener(ivjEventHandler);
		getPnSouth().addMouseListener(ivjEventHandler);
		getCbbBusiDatadict().addItemListener(ivjEventHandler);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("TableInfoDlg");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(340, 480);
			setTitle(NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000934")/* @res "选取表" */);
			setContentPane(getUIDialogContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		getPnNorth().setVisible(false);
		//获得数据字典实例
		Datadict dd = m_parent.getTabPn().getDatadict();
		//初始化数据字典树
		initTree(dd);
		//设置下拉框
		getCbbBusiDatadict().removeItemListener(ivjEventHandler);
		setItems();
		getCbbBusiDatadict().addItemListener(ivjEventHandler);
		//设置定位框属性
		//getTFLocate().setObjectTree(m_tree);
		//getTFLocate().setKind(DatadictNode.TableKind);
		setResizable(true);
		// user code end
	}

	/**
	 * 初始化数据字典树 创建日期：(2003-4-2 16:40:20)
	 */
	private void initTree(Datadict dd) {
		//构造树
		m_tree = new ObjectTreeView();
		//显示规则
		//m_tree.setCompareRule(ObjectTreeNode.COMPARE_ID);
		//m_tree.setDisRule(ObjectTreeNode.DIS_ID_AND_DISNAME);
		//树身体
		if( dd != null ){
			m_tree.setObjectTree(new ObjectTree[] { dd });
			//显示树
			m_tree.expandRow(0);
		}else{
			//m_tree.setObjectTree(new ObjectTree[]{});
			MessageDialog.showErrorDlg(this, StringResource.getStringResource("ubiquery0126"), 
					StringResource.getStringResource("mbiquery0130"));//"数据字典为空，没有可以选择的表。");
			return;
		}
		getSclPnTree().setViewportView(m_tree);
		//添加监听
		m_tree.addKeyListener(this);
	}

	public void keyReleased(KeyEvent e) {
		if (e.getSource() == m_tree) {
			if ((e.getKeyCode() == KeyEvent.VK_F && e.isControlDown())) {
				doFind();
			} else if ((e.getKeyCode() == KeyEvent.VK_R && e.isControlDown())) {
				m_tree.refreshTree();
			} else if ((e.getKeyCode() == KeyEvent.VK_F4)) {
				doAdd();
			}
		}
	}

	/**
	 * 设置表ID哈希表 创建日期：(2003-4-2 17:12:21)
	 * 
	 * @param hashTableId
	 *            java.util.Hashtable
	 */
	public void setHashTableId(Hashtable<String, String> hashTableId) {
		m_hashTableId = hashTableId;
		//getTFLocate().setText("");
	}

	/**
	 * 选中树节点并使其可见 创建日期：(01-6-13 12:44:15)
	 * 
	 * @param path
	 *            tree.TreePath
	 */
	private void setSelNode(DefaultMutableTreeNode node) {
		final TreePath path = new TreePath(node.getPath());
		m_tree.setSelectionPath(path);
		Runnable run = new Runnable() {
			public void run() {
				m_tree.scrollPathToVisible(path);
			}
		};
		SwingUtilities.invokeLater(run);
	}

	/**
	 * 设置业务数据字典接口数组 创建日期：(2004-11-24 09:42:40)
	 * 
	 * @param iBusiDatadicts
	 *            nc.ui.pub.querymodel.IBusiDatadict[]
	 */
	public void setItems() {
		/*
		 * //获得可选字典接口数组 IBusiDatadict[] iBusiDatadicts =
		 * m_parent.getTabPn().getQueryDefDlg() .getIBusiDatadicts(); //获得当前接口序号
		 * int iSelIndex = m_parent.getTabPn().getQueryDefDlg()
		 * .getSelBusiDatadict(); //设置下拉框 getCbbBusiDatadict().removeAllItems();
		 * int iLen = (iBusiDatadicts == null) ? 0 : iBusiDatadicts.length; for
		 * (int i = 0; i < iLen; i++)
		 * getCbbBusiDatadict().addItem(iBusiDatadicts[i].getNote()); if
		 * (iSelIndex < iLen) getCbbBusiDatadict().setSelectedIndex(iSelIndex);
		 */
	}

	/**
	 * 下拉框选项改变事件响应
	 */
	public void cbbBusiDatadict_ItemStateChanged(ItemEvent itemEvent) {

		if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
			changeBusiDatadict();
		}
	}

	/**
	 * 切换业务数据字典 创建日期：(2004-11-24 09:12:41)
	 * 
	 * @param dsName
	 *            java.lang.String
	 * @param env
	 *            nc.vo.pub.querymodel.EnvInfo
	 */
	private void changeBusiDatadict() {
		/*
		 * //获得当前接口序号 int iOldIndex = m_parent.getTabPn().getQueryDefDlg()
		 * .getSelBusiDatadict(); // int iSelIndex =
		 * getCbbBusiDatadict().getSelectedIndex(); if (iSelIndex == iOldIndex)
		 * return; //获得可选字典接口数组 IBusiDatadict[] iBusiDatadicts =
		 * m_parent.getTabPn().getQueryDefDlg() .getIBusiDatadicts();
		 * //获得选中业务数据字典 IBusiDatadict selBusiDatadict =
		 * iBusiDatadicts[iSelIndex]; //获得查询对象执行数据源 String dsName =
		 * m_parent.getTabPn().getQueryBaseDef().getDsName(); //获得数据字典哈希表
		 * Hashtable hashDatadict = m_parent.getTabPn().getQueryDefDlg()
		 * .getHashDatadict(); String strSelIndex = String.valueOf(iSelIndex);
		 * //获得切换后的数据字典 Datadict selDatadict = null; if
		 * (hashDatadict.containsKey(strSelIndex)) selDatadict = (Datadict)
		 * hashDatadict.get(strSelIndex); else { try { selDatadict =
		 * selBusiDatadict.makeDatadict(dsName, new EnvInfo( true)); } catch
		 * (Exception e) { AppDebug.debug(e);
		 * MessageDialog.showWarningDlg(this, NCLangRes
		 * .getInstance().getStrByID("10241201", "UPP10241201-000099")/* @res
		 * "查询引擎" NCLangRes.getInstance().getStrByID("10241201",
		 * "UPP10241201-000935")/* @res "业务数据字典加载有误：" + e.getMessage());
		 * getCbbBusiDatadict().setSelectedIndex(iOldIndex); return; }
		 * hashDatadict.put(strSelIndex, selDatadict); } //刷新界面
		 * initTree(selDatadict); //回设本次定义的业务数据字典接口和数据字典的实例 QueryDefDlg qdDlg =
		 * m_parent.getTabPn().getQueryDefDlg();
		 * qdDlg.setSelBusiDatadict(iSelIndex); qdDlg.setDatadict(selDatadict);
		 * //这句有用 m_parent.getTabPn().setDatadict(selDatadict);
		 */
	}

	/**
	 * connEtoC5: (PnSouth.mouse.mouseClicked(MouseEvent) -->
	 * TableInfoDlg.pnSouth_MouseClicked(LMouseEvent;)V)
	 * 
	 * @param arg1
	 *            MouseEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC5(MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.pnSouth_MouseClicked(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC6: (CbbBusiDatadict.item.itemStateChanged(ItemEvent) -->
	 * TableInfoDlg.cbbBusiDatadict_ItemStateChanged(LItemEvent;)V)
	 * 
	 * @param arg1
	 *            ItemEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC6(ItemEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.cbbBusiDatadict_ItemStateChanged(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * 返回 CbbBusiDatadict 特性值。
	 * 
	 * @return UIComboBox
	 */
	/* 警告：此方法将重新生成。 */
	private UIComboBox getCbbBusiDatadict() {
		if (ivjCbbBusiDatadict == null) {
			try {
				ivjCbbBusiDatadict = new UIComboBox();
				ivjCbbBusiDatadict.setName("CbbBusiDatadict");
				ivjCbbBusiDatadict.setPreferredSize(new Dimension(200, 24));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCbbBusiDatadict;
	}

	/**
	 * 返回 PnNorth 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnNorth() {
		if (ivjPnNorth == null) {
			try {
				ivjPnNorth = new UIPanel();
				ivjPnNorth.setName("PnNorth");
				ivjPnNorth.setPreferredSize(new Dimension(210, 42));
				ivjPnNorth.setLayout(getPnNorthFlowLayout());
				getPnNorth().add(getCbbBusiDatadict(),
						getCbbBusiDatadict().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnNorth;
	}

	/**
	 * 返回 PnNorthFlowLayout 特性值。
	 * 
	 * @return FlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private FlowLayout getPnNorthFlowLayout() {
		FlowLayout ivjPnNorthFlowLayout = null;
		try {
			/* 创建部件 */
			ivjPnNorthFlowLayout = new FlowLayout();
			ivjPnNorthFlowLayout.setVgap(9);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnNorthFlowLayout;
	}

	/**
	 * 南部面板鼠标点击事件响应（切换业务数据字典）
	 */
	public void pnSouth_MouseClicked(MouseEvent mouseEvent) {
		if (mouseEvent.isControlDown() && mouseEvent.getClickCount() == 2) {
			getPnNorth().setVisible(true);
		}
	}

	/**
	 * 南部面板鼠标点击事件响应（切换业务数据字典）
	 */
	public void doAdd() {
		BizObject td = getSelTableDef();
		if (td == null) {
			MessageDialog
					.showErrorDlg(this,
							NCLangRes.getInstance().getStrByID("10241201",
									"UPP10241201-000099")/* @res "查询引擎" */,
							NCLangRes.getInstance().getStrByID("10241201",
									"UPP10241201-000931")/* @res "选中的节点不是表" */);
			return;
		}
		//获得表信息
		String	strNote = null;// 5.01xiugai
		String tableCode = td.getID();
		if (td instanceof TableDefWithAlias){
			tableCode = ((TableDefWithAlias) td).getRealName();
			strNote = ((TableDefWithAlias) td).getNote(); //5.01xiugai
		}
		String tableAlias = td.getID();
		String tableName = td.getDisplayName();
		//转换为VO
		m_ft = new FromTableVO();
		m_ft.setTablecode(tableCode);
		m_ft.setTablealias(tableAlias);
		m_ft.setTabledisname(tableName);
		m_ft.setNote(strNote); //5.01xiugai
		
		//重名处理
		if (m_hashTableId.containsKey(tableAlias)) {
			int iTemp = MessageDialog.showYesNoDlg(this, NCLangRes
					.getInstance().getStrByID("10241201", "UPP10241201-000099")/*
																			    * @res
																			    * "查询引擎"
																			    */, NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000932")/*
										  * @res "已经选择过同名表，是否添加本表？"
										  */);
			if (iTemp != UIDialog.ID_YES)
				return;
			//生成表别名和显示名
			//if (!(td instanceof TableDefWithAlias)) {
			int s = 2;
			while (m_hashTableId.containsKey(tableAlias + "_" + s))
				s++;
			tableAlias += ("_" + s);
			tableName += ("_" + s);
			m_ft.setTablealias(tableAlias);
			m_ft.setTabledisname(tableName);
			//}
		}
		//增加表处理
		if (m_parent instanceof SetTablePanel) {
			((SetTablePanel) m_parent).doAdd(m_ft);
		} else if (m_parent instanceof SetTableJoinPanel) {
			((SetTableJoinPanel) m_parent).getGraphEd().doAdd(null, m_ft);
		}
		//更新哈希表
		m_hashTableId.put(tableAlias, tableName);
	}
}
  