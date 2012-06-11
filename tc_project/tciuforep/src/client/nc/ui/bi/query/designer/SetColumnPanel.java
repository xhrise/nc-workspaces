package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITree;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.BIQueryModelTree;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.core.ObjectTree;
import nc.vo.pub.querymodel.DataDictForNode;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 设置字段面板(新) 创建日期：(2005-6-21 10:35:33)
 * 
 * @author：朱俊彬
 */
public class SetColumnPanel extends AbstractQueryDesignSetPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0051";//"查询字段";

	//查询字段细节面板(右)
	private SetFldPanel m_pnSetFld = null;

	//表对应的字段列表
	private Map<String, SelectFldVO[]> m_hmFlds = new java.util.HashMap<String, SelectFldVO[]>();

	private UIPanel ivjPnButton = null;

	private UIPanel ivjPnCenter = null;

	private UIScrollPane ivjSclPnTree = null;

	private UIButton ivjBnAdd = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener, MouseListener,
			TreeSelectionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == SetColumnPanel.this.getBnAdd())
				connEtoC1(e);
		};

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == SetColumnPanel.this.getTreeTableFld())
				connEtoC3(e);
		};

		public void mouseEntered(MouseEvent e) {
		};

		public void mouseExited(MouseEvent e) {
		};

		public void mousePressed(MouseEvent e) {
		};

		public void mouseReleased(MouseEvent e) {
		};

		public void valueChanged(TreeSelectionEvent e) {
			if (e.getSource() == SetColumnPanel.this.getTreeTableFld())
				connEtoC2(e);
		};
	};

	/**
	 * SetColumnPanel 构造子注解。
	 */
	public SetColumnPanel() {
		super();
		initialize();
	}

	/**
	 * Add
	 */
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
		//获得选中字段
		SelectFldVO[] sfs = getGenFlds();
		//		if (sfs == null) {
		//			MessageDialog.showWarningDlg(this, "UFBI", "选中节点只能由字段构成");
		//			return;
		//		}
		//合法性检查
		Hashtable hashFldAlias = getFldPanel().getHashFldAlias(-1);
		String strErr = checkMultiSelect(sfs, hashFldAlias);
		if (strErr != null) {
			MessageDialog.showWarningDlg(this, "UFBI", strErr);
			return;
		}
		//加入字段细节面板
		getFldPanel().doAdd(sfs);
	}

	/**
	 * 合法性校验
	 */
	public String check() {
		return getFldPanel().check();
	}
	public String checkOnSwitch(){
		return getFldPanel().checkOnSwitch();
	}

	/**
	 * 获得面板标题 创建日期：(2005-5-13 16:43:08)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/**
	 * 获得结果 创建日期：(2005-5-13 16:43:08)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		getFldPanel().getResultFromPanel(qmd);
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
		getBnAdd().addActionListener(ivjEventHandler);
		getTreeTableFld().addMouseListener(ivjEventHandler);
		getTreeTableFld().addTreeSelectionListener(ivjEventHandler);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("SetColumnPanel");
			setSize(480, 320);
			add(getSclPnTree(), "West");
			add(getPnCenter(), "Center");
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * 设置结果 创建日期：(2005-5-13 16:43:08)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		getFldPanel().setResultToPanel(qmd);
	}

	private UITree ivjTreeTableFld = null;

	/**
	 * 字段树鼠标点击事件响应
	 */
	public void treeTableFld_MouseClicked(MouseEvent mouseEvent) {
	}

	/**
	 * 初始化树
	 */
	public void initTree() {
		getTreeTableFld().removeTreeSelectionListener(ivjEventHandler);
		//获得数据表
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		FromTableVO[] fts = qbd.getFromTables();
		//构造树
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");
		int iLen = (fts == null) ? 0 : fts.length;
		for (int i = 0; i < iLen; i++) {
			//加入表节点
			DefaultMutableTreeNode nodeTable = new DefaultMutableTreeNode(
					fts[i]);
			root.add(nodeTable);
			//获得字段
			SelectFldVO[] sfs = getFldVO(fts[i]);
			//加入字段节点
			int iLenFld = (sfs == null) ? 0 : sfs.length;
			for (int j = 0; j < iLenFld; j++) {
				DefaultMutableTreeNode nodeFld = new DefaultMutableTreeNode(
						sfs[j]);
				nodeTable.add(nodeFld);
			}
		}
		//树模型
		DefaultTreeModel tm = new DefaultTreeModel(root);
		getTreeTableFld().setModel(tm);
		//隐藏根
		getTreeTableFld().setRootVisible(false);
		//树节点着色器
		getTreeTableFld().setCellRenderer(new FieldCellRenderer());
		//展开
		//getTreeTableFld().expandRow(0);
		//for (int i = iLen - 1; i >= 0; i--) {
		//	getTreeTableFld().expandRow(i);
		//}
		//加监听
		getTreeTableFld().addTreeSelectionListener(ivjEventHandler);
		getBnAdd().setEnabled(false);
	}

	/**
	 * 获得查询字段细节面板(右)
	 */
	public SetFldPanel getFldPanel() {
		if (m_pnSetFld == null) {
			m_pnSetFld = new SetFldPanel();
			m_pnSetFld.setColumnPanel(this);
			//设置本实例
			//m_pnSetFld.getTablePn().setColumnPanel(this);
			//QueryFldTable table = m_pnSetFld.getTable();
			//table.setColumnPanel(this);
			//table.setHeader(new QueryFldTableHeader(table.getColumnModel()));
		}
		return m_pnSetFld;
	}

	/**
	 * 初始化界面
	 */
	public void initUI() {
		//初始化树
		initTree();
		//加入右界面
		getPnCenter().add(getFldPanel(), BorderLayout.CENTER);
		//设置细节面板的信息
		getFldPanel().setTabPn(getTabPn());
	}


		private SelectFldVO[] getFldVO(FromTableVO ft) {
			String		strTablePK = ft.getTablecode();
			//  获得数据字典实例
			ObjectTree dd = getTabPn().getDatadict();
			if( dd != null && dd instanceof DataDictForNode ){
				if( ((DataDictForNode)dd).isIUFO() ){
					strTablePK = ft.getTablealias();
				}
			}
			if (!m_hmFlds.containsKey(strTablePK)) {
				String dsName = getTabPn().getDefDsName();
				ObjectTree tree = (BIModelUtil.isTempTable(ft.getTablecode())) ? BIQueryModelTree
						.getInstance(dsName)
						: dd;
				//取得字段
				SelectFldVO[] sfs = BIModelUtil.getFldsFromTable(ft.getTablecode(),
						ft.getTablealias(), tree);
				m_hmFlds.put(strTablePK, sfs);
				return sfs;
			} else {
				return (SelectFldVO[]) m_hmFlds.get(strTablePK);
			}
		}

	/**
	 * 获得选中的查询字段VO数组 创建日期：(2003-4-3 9:09:08)
	 * 
	 * @return nc.vo.iuforeport.businessquery.SelectFldVO
	 */
	public SelectFldVO[] getGenFlds() {
		//获得选中路径
		TreePath[] selPaths = getTreeTableFld().getSelectionPaths();
		int iSelCount = (selPaths == null) ? 0 : selPaths.length;
		if (iSelCount == 0) {
			return null;
		}
		SelectFldVO[] sfs = new SelectFldVO[iSelCount];
		for (int i = 0; i < iSelCount; i++) {
			//获得字段VO
			DefaultMutableTreeNode selNodeFld = (DefaultMutableTreeNode) selPaths[i]
					.getLastPathComponent();
			if (!(selNodeFld.getUserObject() instanceof SelectFldVO)) {
				//选中了表
				return null;
			}
			sfs[i] = (SelectFldVO) selNodeFld.getUserObject();
			//获得表VO
			DefaultMutableTreeNode selNodeTable = (DefaultMutableTreeNode) selNodeFld
					.getParent();
			FromTableVO ft = (FromTableVO) selNodeTable.getUserObject();
			//构造表达式
			String strExp = sfs[i].getFldalias();
			String tableAlias = ft.getTablealias();
			if (tableAlias != null) {
				strExp = tableAlias + "." + strExp;
			}
			sfs[i].setExpression(strExp);
		}
		return sfs;
	}

	/**
	 * 合法性检查 创建日期：(2003-4-4 13:57:49)
	 * 
	 * @return java.lang.String
	 */
	@SuppressWarnings("unchecked")
	public String checkMultiSelect(SelectFldVO[] sfs, Hashtable hashFldAlias) {

		int iLen = (sfs == null) ? 0 : sfs.length;
		for (int i = 0; i < iLen; i++) {
			String fldAlias = sfs[i].getFldalias();
			String fldName = sfs[i].getFldname();
			//修改字段无妨
			if (hashFldAlias.containsKey(fldAlias))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000688")/* @res "字段别名不能重复：" */
						+ fldAlias + "(" + fldName + ")";
			if (hashFldAlias.containsValue(fldName))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000689")/* @res "字段显示名不能重复：" */
						+ fldName;
		}
		//注意分两次循环
		for (int i = 0; i < iLen; i++) {
			//更新哈希表
			hashFldAlias.put(sfs[i].getFldalias(), sfs[i].getFldname());
		}
		return null;
	}

	/**
	 * connEtoC1: (BnAdd.action.actionPerformed(ActionEvent) -->
	 * SetColumnPanel.bnAdd_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(ActionEvent arg1) {
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
	 * connEtoC2: (TreeTableFld.treeSelection.valueChanged(TreeSelectionEvent)
	 * --> SetColumnPanel.treeTableFld_ValueChanged(LTreeSelectionEvent;)V)
	 * 
	 * @param arg1
	 *            TreeSelectionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC2(TreeSelectionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.treeTableFld_ValueChanged(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3: (TreeTableFld.mouse.mouseClicked(MouseEvent) -->
	 * SetColumnPanel.treeTableFld_MouseClicked(LMouseEvent;)V)
	 * 
	 * @param arg1
	 *            MouseEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.treeTableFld_MouseClicked(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
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
				ivjBnAdd.setText(">");
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
	 * 返回 PnButton 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnButton() {
		if (ivjPnButton == null) {
			try {
				ivjPnButton = new UIPanel();
				ivjPnButton.setName("PnButton");
				ivjPnButton.setPreferredSize(new Dimension(56, 32));
				ivjPnButton.setLayout(new GridBagLayout());

				GridBagConstraints constraintsBnAdd = new GridBagConstraints();
				constraintsBnAdd.gridx = 1;
				constraintsBnAdd.gridy = 1;
				constraintsBnAdd.insets = new Insets(149, 8, 149, 8);
				getPnButton().add(getBnAdd(), constraintsBnAdd);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnButton;
	}

	/**
	 * 返回 PnCenter 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnCenter() {
		if (ivjPnCenter == null) {
			try {
				ivjPnCenter = new UIPanel();
				ivjPnCenter.setName("PnCenter");
				ivjPnCenter.setPreferredSize(new Dimension(180, 0));
				ivjPnCenter.setLayout(new BorderLayout());
				getPnCenter().add(getPnButton(), "West");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnCenter;
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
				ivjSclPnTree.setPreferredSize(new Dimension(168, 3));
				getSclPnTree().setViewportView(getTreeTableFld());
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
	 * 返回 TreeTableFld 特性值。
	 * 
	 * @return UITree
	 */
	/* 警告：此方法将重新生成。 */
	private UITree getTreeTableFld() {
		if (ivjTreeTableFld == null) {
			try {
				ivjTreeTableFld = new TableFldTree();
				ivjTreeTableFld.setName("TreeTableFld");
				ivjTreeTableFld.setBounds(0, 0, 78, 72);
				// user code begin {1}
				//ivjTreeTableFld = new TableFldTree();
				//设置本实例
				((TableFldTree) ivjTreeTableFld).setColumnPanel(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTreeTableFld;
	}

	/**
	 * 树节点改变选取事件响应
	 */
	public void treeTableFld_ValueChanged(TreeSelectionEvent treeSelectionEvent) {
		//按钮可用性控制
		boolean bAddable = (getGenFlds() != null);
		getBnAdd().setEnabled(bAddable);
	}

	/**
	 * 刷新查询基本定义
	 */
	public void refreshQbd() {
		//获得查询字段定义
		SelectFldVO[] sfs = getFldPanel().getResultFromFld();
		//刷新
		getTabPn().getQueryBaseDef().setSelectFlds(sfs);
	}
}  