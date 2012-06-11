package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.component.ObjectTreeView;
import nc.ui.pub.component.dialog.FindDlg;
import nc.ui.pub.querymodel.UIUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.BIQueryModelTree;
import nc.vo.bi.query.manager.BIQueryUtil;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.core.ObjectTree;
import nc.vo.pub.querymodel.QueryModelNode;
import nc.vo.pub.querymodel.QueryModelTree;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * 临时表选取对话框 创建日期：(2003-4-2 16:30:15)
 * 
 * @author：朱俊彬
 */
public class TempTableInfoDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//父组件
	private AbstractQueryDesignSetPanel m_parent = null;

	//用于判断是否选表重复的哈希表
	private Hashtable m_hashTableId = null;

	//查询对象树
	private ObjectTreeView m_tree = null;

	//查询对象树身体
	private QueryModelTree m_qmt = null;

	//定义数据源
	private String m_dsNameForDef = null;

	private UIButton ivjBnCancel = null;

	private UIButton ivjBnOK = null;

	private UIPanel ivjPnSouth = null;

	private UIScrollPane ivjSclPnTree = null;

	private JPanel ivjUIDialogContentPane = null;

	private UIButton ivjBnAdd = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == TempTableInfoDlg.this.getBnCancel())
				connEtoC1(e);
			if (e.getSource() == TempTableInfoDlg.this.getBnOK())
				connEtoC2(e);
			if (e.getSource() == TempTableInfoDlg.this.getBnAdd())
				connEtoC3(e);
		};
	};

	/**
	 * TableInfoDlg 构造子注解。
	 * @deprecated
	 */
	public TempTableInfoDlg() {
		super();
		initialize();
	}

	/**
	 * TableInfoDlg 构造子注解。
	 * 
	 * @param parent
	 *            Container
	 */
	public TempTableInfoDlg(Container parent) {
		super(parent);
		if (parent instanceof SetTablePanel)
			m_parent = (SetTablePanel) parent;
		initialize();
	}

	/**
	 * add
	 */
	@SuppressWarnings("unchecked")
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
		BIQueryModelDef qmd = getSelTableDef();
		if (qmd == null) {
			MessageDialog.showWarningDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000931")/* @res "选中的节点不是表" */);
			return;
		}
		if (m_hashTableId.containsKey(qmd.getID())) {
			MessageDialog.showWarningDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000936")/* @res "选中节点已添加" */);
			return;
		}
		try {
			String idSelf = m_parent.getTabPn().getQueryBaseDef().getID();
			if (qmd.getID().equals(idSelf)) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "查询引擎" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000937")/* @res "不能添加自己" */);
				return;
			}
		} catch (Exception e) {
		}
		//增加表处理
		if (m_parent instanceof SetTablePanel) {
			((SetTablePanel) m_parent).doAdd(qmd.getBaseModel());
		}
		//更新哈希表
		m_hashTableId.put(qmd.getID(), qmd.getDisplayName());
	}

	/**
	 * cancel
	 */
	public void bnCancel_ActionPerformed(ActionEvent actionEvent) {
		closeCancel();
	}

	/**
	 * OK
	 */
	public void bnOK_ActionPerformed(ActionEvent actionEvent) {
		String strErr = check();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */, strErr);
			return;
		}
		closeOK();
	}

	/**
	 * 合法性检查 创建日期：(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	private String check() {
		BIQueryModelDef qmd = getSelTableDef();
		if (qmd == null)
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000931")/* @res "选中的节点不是表" */;
		if (m_hashTableId.containsKey(qmd.getID()))
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000936")/* @res "选中节点已添加" */;
		try {
			String idSelf = m_parent.getTabPn().getQueryBaseDef().getID();
			if (qmd.getID().equals(idSelf))
				return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000937")/* @res "不能添加自己" */;
		} catch (Exception e) {
		}
		return null;
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
				ivjBnAdd.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000330")/* @res "增加" */);
				// user code begin {1}
				ivjBnAdd.setPreferredSize(new Dimension(70, 22));
				UIUtil.autoSizeComp(ivjBnAdd, ivjBnAdd.getText());
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
				ivjBnCancel
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000000")/* @res "取消" */);
				// user code begin {1}
				ivjBnCancel.setPreferredSize(new Dimension(70, 22));
				UIUtil.autoSizeComp(ivjBnCancel, ivjBnCancel.getText());
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
				ivjBnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000001")/* @res "确定" */);
				// user code begin {1}
				ivjBnOK.setPreferredSize(new Dimension(70, 22));
				UIUtil.autoSizeComp(ivjBnOK, ivjBnOK.getText());
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
			ivjPnSouthFlowLayout.setHgap(20);
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
	 * 获得选中的查询模型定义 创建日期：(2003-4-2 16:49:17)
	 * 
	 * @return nc.bs.com.datadict.TableDef
	 */
	public BIQueryModelDef getSelTableDef() {
		BIQueryModelDef qmd = null;
		//获得选中节点
		ObjectNode on = m_tree.getSelectedObjectNode();
		if (on.getKind() != null
				&& on.getKind().equals(QueryModelNode.MODEL_KIND)) {
			String id = on.getID();
			String dsNameForDef = getDsNameForDef();
			qmd = BIQueryUtil.getQueryModelDef(id, dsNameForDef);
			//qmd = (QueryModelDef) on.getObject();
		}
		return qmd;
	}

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * 
	 * @return javax.swing.JPanel
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
			setSize(320, 400);
			setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000934")/* @res "选取表" */);
			setContentPane(getUIDialogContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * 初始化查询模型树 创建日期：(2003-4-2 16:40:20)
	 */
	public void initTree(String dsName) {
		//定义数据源
		//	dsName = ModelUtil.getDsnameForDef();
		setDsNameForDef(dsName);

		//获得查询模型实例
		if (dsName == null)
			m_qmt = BIQueryModelTree.getDefaultInstance();
		else
			m_qmt = BIQueryModelTree.getInstance(dsName);
		//构造树
		m_tree = new ObjectTreeView();
		m_tree.setObjectTree(new ObjectTree[] { m_qmt });
		//显示树
		m_tree.expandRow(0);
		getSclPnTree().setViewportView(m_tree);
		//添加监听
		m_tree.addKeyListener(this);
	}

	public void keyReleased(KeyEvent e) {
		if (e.getSource() == m_tree)
			if ((e.getKeyCode() == KeyEvent.VK_F && e.isControlDown())) {
				//弹出查找框
				FindDlg dlg = new FindDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000121")/* @res "查找" */, true);
				dlg.setObjectTrees(new ObjectTree[] { m_qmt });
				if (dlg.showModal() == UIDialog.ID_OK) {
					ObjectNode objnode = dlg.getSearch();
					m_tree.selectNode(objnode);
				}
			}
	}

	/**
	 * 设置增加按钮可用性 创建日期：(2003-4-2 17:12:21)
	 * 
	 * @param hashTableId
	 *            java.util.Hashtable
	 */
	public void setBnAddEnabled(boolean bEnabled) {
		getBnAdd().setEnabled(bEnabled);
	}

	/**
	 * 设置表ID哈希表 创建日期：(2003-4-2 17:12:21)
	 * 
	 * @param hashTableId
	 *            java.util.Hashtable
	 */
	public void setHashTableId(Hashtable hashTableId) {
		m_hashTableId = hashTableId;
	}

	/**
	 * 获得定义数据源
	 */
	public String getDsNameForDef() {
		return m_dsNameForDef;
	}

	/**
	 * 设置定义数据源
	 */
	public void setDsNameForDef(String dsNameForDef) {
		m_dsNameForDef = dsNameForDef;
	}
} 