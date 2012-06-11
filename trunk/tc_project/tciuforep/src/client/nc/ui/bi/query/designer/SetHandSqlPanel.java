/*
 * 创建日期 2005-5-24
 *
 */
package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.component.dialog.FindDlg;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.iuforeport.businessquery.TableDefWithAlias;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.core.ObjectTree;
import nc.vo.pub.ddc.datadict.Datadict;
import nc.vo.pub.ddc.datadict.DatadictNode;
import nc.vo.pub.ddc.datadict.FieldDef;
import nc.vo.pub.ddc.datadict.TableDef;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.QueryBaseVO;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.SqlParseMain;
import net.sf.jsqlparser.statement.select.Select;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zjb
 * 
 * 手工SQL设置界面
 */
public class SetHandSqlPanel extends AbstractQueryDesignSetPanel implements
		KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0051";//"查询字段";

	private UIPanel ivjPnNorth = null;

	private UIScrollPane ivjSclPnSql = null;

	private UITextArea ivjTASql = null;

	private UIScrollPane ivjSclPnTree = null;

	private nc.ui.pub.component.ObjectTreeView ivjTreeDD = null;

	private UIButton ivjBnDD = null;

	private UICheckBox ivjCheckBox = null;

	private UIPanel ivjPnSouth = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener, KeyListener, MouseListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == SetHandSqlPanel.this.getBnDD())
				connEtoC3(e);
		};

		public void keyPressed(KeyEvent e) {
		};

		public void keyReleased(KeyEvent e) {
			if (e.getSource() == SetHandSqlPanel.this.getTASql())
				connEtoC1(e);
		};

		public void keyTyped(KeyEvent e) {
		};

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == SetHandSqlPanel.this.getTreeDD())
				connEtoC2(e);
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
	 * SetTablePanel 构造子注解。
	 */
	public SetHandSqlPanel() {
		super();
		initialize();
	}

	/**
	 * 收合数据字典树
	 * 
	 * @param actionEvent
	 *            ActionEvent
	 */
	public void bnDD_ActionPerformed(ActionEvent actionEvent) {
		boolean bDDVisible = getSclPnTree().isVisible();
		getSclPnTree().setVisible(!bDDVisible);
		getBnDD().setText((bDDVisible) ? ">" : "<");
		validate();
	}

	/**
	 * connEtoC1: (TASql.key.keyReleased(KeyEvent) -->
	 * SetHandSqlPanel.tASql_KeyReleased(LKeyEvent;)V)
	 * 
	 * @param arg1
	 *            KeyEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(KeyEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.tASql_KeyReleased(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC2: (TreeDD.mouse.mouseClicked(MouseEvent) -->
	 * SetHandSqlPanel.treeDD_MouseClicked(LMouseEvent;)V)
	 * 
	 * @param arg1
	 *            MouseEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC2(MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.treeDD_MouseClicked(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3: (BnDD.action.actionPerformed(ActionEvent) -->
	 * SetHandSqlPanel.bnDD_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnDD_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * 设置待定参数名 创建日期：(2003-11-13 9:05:55)
	 * 
	 * @return java.lang.String
	 */
	private String fillParam() {
		String strParam = "";
		//获得正在编辑的节点
		//ObjectNode node = getTabPn().getQueryDefDlg().getEditNode();
		BIQueryModelDef qmd = getTabPn().getQueryModelDef();
		ParamVO[] params = qmd.getBaseModel().getParamVOs();
		//弹框
		ParamDefDlg dlg = new ParamDefDlg(this, getTabPn().getDefDsName());
		dlg.setParamVOs(params, qmd.getDsName());
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//存储
			params = dlg.getParamVOs();
			qmd.getBaseModel().setParamVOs(params);
			//node.saveObject(qmd);
			//node.setObject(qmd);
			//获得选中参数
			ParamVO selParam = dlg.getSelParamVO();
			if (selParam != null) {
				strParam = selParam.getParamCode();
				//非替换型参数提示
				if (!strParam.startsWith(QueryConst.REPLACE_PARAM)
						|| !strParam.endsWith(QueryConst.REPLACE_PARAM))
					MessageDialog.showHintDlg(this, NCLangRes.getInstance()
							.getStrByID("10241201", "UPP10241201-000099")/*
																		  * @res
																		  * "查询引擎"
																		  */, NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000899")/*
												  * @res "手工SQL中只识别替换型参数"
												  */);
			}
		}
		return strParam;
	}

	/**
	 * 返回 BnDD 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnDD() {
		if (ivjBnDD == null) {
			try {
				ivjBnDD = new UIButton();
				ivjBnDD.setName("BnDD");
				ivjBnDD.setIButtonType(0/** Java默认(自定义) */
				);
//				ivjBnDD.setFont(new Font("dialog", 1, 10));
				ivjBnDD.setText("<");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnDD;
	}

	/**
	 * 返回 CheckBox 特性值。
	 * 
	 * @return UICheckBox
	 */
	/* 警告：此方法将重新生成。 */
	private UICheckBox getCheckBox() {
		if (ivjCheckBox == null) {
			try {
				ivjCheckBox = new UICheckBox();
				ivjCheckBox.setName("CheckBox");
				ivjCheckBox.setPreferredSize(new Dimension(128, 22));
				ivjCheckBox.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000900")/*
														  * @res "不启用翻译解析"
														  */);
				// user code begin {1}
				ivjCheckBox.setToolTipText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000901")/*
														  * @res
														  * "如果选取此项，则与解析相关的查询集成和SQL整理功能将无法正常使用"
														  */);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCheckBox;
	}

	/**
	 * 获得手工录入SQL 创建日期：(2003-5-29 11:27:25)
	 * 
	 * @return java.lang.String
	 */
	public String getHandSql() {
		String sql = getTASql().getText();
		if (sql != null)
			sql = sql.trim();
		return sql;
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
				ivjPnNorth.setPreferredSize(new Dimension(10, 16));
				ivjPnNorth.setLayout(new BorderLayout());
				getPnNorth().add(getBnDD(), "Center");
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
				getPnSouth().add(getCheckBox(), getCheckBox().getName());
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
	 * 返回 SclPnSql 特性值。
	 * 
	 * @return UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private UIScrollPane getSclPnSql() {
		if (ivjSclPnSql == null) {
			try {
				ivjSclPnSql = new UIScrollPane();
				ivjSclPnSql.setName("SclPnSql");
				ivjSclPnSql.setPreferredSize(new Dimension(480, 19));
				getSclPnSql().setViewportView(getTASql());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnSql;
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
				ivjSclPnTree.setPreferredSize(new Dimension(160, 3));
				getSclPnTree().setViewportView(getTreeDD());
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
	 * 返回 TASql 特性值。
	 * 
	 * @return UITextArea
	 */
	/* 警告：此方法将重新生成。 */
	private UITextArea getTASql() {
		if (ivjTASql == null) {
			try {
				ivjTASql = new UITextArea();
				ivjTASql.setName("TASql");
				ivjTASql.setLineWrap(true);
				ivjTASql.setBounds(0, 0, 160, 120);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTASql;
	}

	/**
	 * 返回 TreeDD 特性值。
	 * 
	 * @return nc.ui.pub.component.ObjectTreeView
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.component.ObjectTreeView getTreeDD() {
		if (ivjTreeDD == null) {
			try {
				ivjTreeDD = new nc.ui.pub.component.ObjectTreeView();
				ivjTreeDD.setName("TreeDD");
				ivjTreeDD.setBounds(0, 0, 78, 72);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTreeDD;
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
		getTASql().addKeyListener(ivjEventHandler);
		getTreeDD().addMouseListener(ivjEventHandler);
		getBnDD().addActionListener(ivjEventHandler);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("SetHandSqlPanel");
			setLayout(new BorderLayout());
			setSize(580, 240);
			add(getPnNorth(), "North");
			add(getSclPnSql(), "Center");
			add(getSclPnTree(), "West");
			add(getPnSouth(), "South");
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		getTreeDD().setShowChildren(true);
		// user code end
	}

	/**
	 * 初始化数据字典树 创建日期：(2003-4-2 16:40:20)
	 */
	public void initTree() {
		//删除原来可能的监听
		getTreeDD().removeKeyListener(this);
		//获得数据字典实例
		Datadict dd = getTabPn().getDatadict();
		//构造树
		getTreeDD().setObjectTree(new ObjectTree[] { dd });
		//显示树
		getTreeDD().expandRow(0);
		//添加监听
		getTreeDD().addKeyListener(this);
	}

	/**
	 * 是否启用SQL翻译 创建日期：(2004-3-19 10:24:37)
	 * 
	 * @return boolean
	 */
	public boolean isSqlTranslate() {
		return !getCheckBox().isSelected();
	}

	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param e
	 *            KeyEvent
	 */
	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		if (e.getSource() == getTreeDD()) {
			if ((e.getKeyCode() == KeyEvent.VK_F && e.isControlDown())) {
				//获得数据字典实例
				Datadict dd = getTabPn().getDatadict();
				//查找
				FindDlg dlg = new FindDlg(this,
						NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000121")/* @res "查找" */, true);
				dlg.setObjectTrees(new ObjectTree[] { dd });
				if (dlg.showModal() == UIDialog.ID_OK) {
					ObjectNode objnode = dlg.getSearch();
					getTreeDD().selectNode(objnode);
				}
			} else if ((e.getKeyCode() == KeyEvent.VK_R && e.isControlDown())) {
				getTreeDD().refreshTree();
			}
		}
	}

	/**
	 * 双击选中 创建日期：(2003-4-2 20:38:58)
	 * 
	 * @param str
	 *            java.lang.String
	 * @param iFunc
	 *            int
	 */
	private void onSelect(String str, int iFunc) {
		if (str != null) {
			//选中
			int pos = getTASql().getSelectionStart();
			getTASql().insert(str, pos);
			getTASql().requestFocus();
			getTASql().setSelectionStart(pos + str.length() - iFunc);
			getTASql().setSelectionEnd(pos + str.length() - iFunc);
		}
	}

	/**
	 * 设置手工SQL 创建日期：(2003-4-3 16:16:01)
	 * 
	 * @param sql
	 *            String
	 * @param bTranslate
	 *            boolean
	 */
	public void setResultToHandSql(String sql, boolean bTranslate) {
		getTASql().setText(sql);
		getCheckBox().setSelected(!bTranslate);
	}

	/**
	 * 键盘释放事件响应
	 * 
	 * @param keyEvent
	 *            KeyEvent
	 */
	public void tASql_KeyReleased(KeyEvent keyEvent) {
		int c = keyEvent.getKeyCode();
		if (c == KeyEvent.VK_F10 && keyEvent.isControlDown())
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-000099")/* @res "查询引擎" */,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000902")/* @res "干什么呢？" */);
		else if (c == KeyEvent.VK_P && keyEvent.isControlDown())
			testParse(getTASql().getText());
		else if (c == KeyEvent.VK_S && keyEvent.isControlDown())
			onSelect("select ", 0);
		else if (c == KeyEvent.VK_F && keyEvent.isControlDown())
			onSelect(" from ", 0);
		else if (c == KeyEvent.VK_L && keyEvent.isControlDown())
			//折行显示
			getTASql().setLineWrap(!getTASql().getLineWrap());
		else if (c == KeyEvent.VK_W && keyEvent.isControlDown())
			onSelect(" where ", 0);
		else if (c == KeyEvent.VK_O && keyEvent.isControlDown())
			onSelect(" order by ", 0);
		else if (c == KeyEvent.VK_U && keyEvent.isControlDown())
			onSelect(" union ", 0);
		else if (c == KeyEvent.VK_M && keyEvent.isControlDown())
			bnDD_ActionPerformed(null);
		else if (c == KeyEvent.VK_F12)
			onSelect(fillParam(), 0);
		else if (c == KeyEvent.VK_F11)
			onSelect(fillEnvParam(), 0);
		return;
	}

	/**
	 * 测试解析 创建日期：(2003-9-9 20:03:52)
	 * 
	 * @param sql
	 *            String
	 */
	public void testParse(String sql) {
		try {
			Select select = SqlParseMain.doParse(sql);
			SqlParseMain.transform(select);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-000099")/* @res "查询引擎" */,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000903")/* @res "解析通过" */);
		} catch (Exception e) {
			AppDebug.debug(e);
			MessageDialog.showErrorDlg(this,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */, NCLangRes
							.getInstance().getStrByID("10241201",
									"UPP10241201-000904")/* @res "解析语法有误" */);
		}
		return;
	}

	/**
	 * 树点击事件响应
	 * 
	 * @param mouseEvent
	 *            MouseEvent
	 */
	public void treeDD_MouseClicked(MouseEvent mouseEvent) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getTreeDD()
				.getSelectedTreeNode();
		if (node != null && mouseEvent.getClickCount() == 2) {
			if (node.getUserObject() instanceof FieldDef) {
				//获得字段定义
				FieldDef fd = (FieldDef) node.getUserObject();
				//获得表定义
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
						.getParent().getParent();
				DatadictNode ddn = (DatadictNode) parent.getUserObject();
				TableDef td = (TableDef) ddn.getObject();
				//获得表名
				String strTable = td.getID();
				//UFO处理
				if (td instanceof TableDefWithAlias)
					strTable = ((TableDefWithAlias) td).getRealName();
				//获得字段全名
				String strFld = strTable + "." + fd.getID();
				//录入
				onSelect(strFld, 0);
			} else if (node.getUserObject() instanceof DatadictNode) {
				DatadictNode ddn = (DatadictNode) node.getUserObject();
				if (ddn.getObject() instanceof TableDef) {
					//获得表定义
					TableDef td = (TableDef) ddn.getObject();
					//获得表名
					String strTable = td.getID();
					//UFO处理
					if (td instanceof TableDefWithAlias)
						strTable = ((TableDefWithAlias) td).getRealName();
					//录入
					onSelect(strTable, 0);
				}
			}
		}
	}

	/**
	 * 设置环境参数名 创建日期：(2003-11-13 9:05:55)
	 * 
	 * @return java.lang.String
	 */
	private String fillEnvParam() {
		String strParam = null;
		/*
		 * //获得全部环境参数（缺省+自定义接口实现） IEnvParam iEnvParam =
		 * getTabPn().getQueryDefDlg() .getIEnvParam(); String[][] envParams =
		 * ModelUtil.getEnvParams(iEnvParam); //弹框 ChooseEnvParamDlg dlg = new
		 * ChooseEnvParamDlg(this); dlg.setEnvParams(envParams);
		 * dlg.showModal(); dlg.destroy(); if (dlg.getResult() ==
		 * UIDialog.ID_OK) { //获得选中参数名 strParam = dlg.getEnvParams(); }
		 */
		return strParam;
	}

	/**
	 * Invoked when a key has been typed. This event occurs when a key press is
	 * followed by a key release.
	 * 
	 * @param e
	 *            KeyEvent
	 */
	public void keyTyped(java.awt.event.KeyEvent e) {
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getPanelTitle()
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	/**
	 * @i18n miufo00227=不翻译
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		QueryBaseVO qb = qmd.getQueryBaseVO();
		//回设
		qb.setHandSql(getHandSql());
		qb.setTempletId(isSqlTranslate() ? null : StringResource.getStringResource("miufo00227"));
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	/**
	 * @i18n miufo00227=不翻译
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		QueryBaseVO qb = qmd.getQueryBaseVO();
		//
		if (qb != null) {
			boolean bTranslate = (qb.getTempletId() == null || !qb
					.getTempletId().equals(StringResource.getStringResource("miufo00227")));
			setResultToHandSql(qb.getHandSql(), bTranslate);
		}
	}

	/**
	 * 合法性检查 创建日期：(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {
		return null;
	}
}
  