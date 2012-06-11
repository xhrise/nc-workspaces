/*
 * 创建日期 2005-5-31
 *
 */
package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Constructor;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.beans.editor.RichEditor;
import nc.ui.pub.codingwiz.AbstractCodeWizDlg;
import nc.ui.pub.querymodel.DefaultCodeWizDlg;
import nc.ui.pub.querymodel.FormatText;
import nc.ui.pub.querymodel.PfColorSet;
import nc.ui.pub.querymodel.QuoteQueryFlddefDlg;
import nc.ui.pub.querymodel.UIUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.CodeWizManager;
import nc.vo.pub.beans.editor.DefaultLexAnalyzer;
import nc.vo.pub.beans.editor.sorter.JavaWordSorter;
import nc.vo.pub.codingwiz.AbstractCodeWizModel;
import nc.vo.pub.codingwiz.AdvCodeWizVO;
import nc.vo.pub.codingwiz.CodeWizParamVO;
import nc.vo.pub.codingwiz.CodeWizardVO;
import nc.vo.pub.ddc.datadict.Datadict;
import nc.vo.pub.querymodel.DataProcessVO;
import nc.vo.pub.querymodel.EmbedCodeVO;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * @author zjb
 * 
 * 数据加工代码设置界面
 */
public class SetProcessCodeUI extends AbstractQueryDesignSetPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0057";//"数据加工";

	//定义数据源
	private String m_defDsName = null;

	private JPanel ivjUIDialogContentPane = null;

	private UIButton ivjBnCancel = null;

	private UIButton ivjBnOK = null;

	private UIPanel ivjPnNorth = null;

	private UIPanel ivjPnSouth = null;

	private UIButton ivjBnCheck = null;

	private UIScrollPane ivjSclPnCode = null;

	private RichEditor ivjTACode = null;

	private UIScrollPane ivjSclPnTree = null;

	private UITree ivjTreeCode = null;

	private UIButton ivjBnDD = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener, KeyListener, MouseListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == SetProcessCodeUI.this.getBnOK())
				connEtoC1(e);
			if (e.getSource() == SetProcessCodeUI.this.getBnCancel())
				connEtoC2(e);
			if (e.getSource() == SetProcessCodeUI.this.getBnCheck())
				connEtoC3(e);
			if (e.getSource() == SetProcessCodeUI.this.getBnDD())
				connEtoC6(e);
		};

		public void keyPressed(KeyEvent e) {
		};

		public void keyReleased(KeyEvent e) {
			if (e.getSource() == SetProcessCodeUI.this.getTACode())
				connEtoC4(e);
		};

		public void keyTyped(KeyEvent e) {
		};

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == SetProcessCodeUI.this.getTreeCode())
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
	 * SetProcessCodeUI 构造子注解。
	 */
	public SetProcessCodeUI() {
		super();
		initialize();
	}

	/**
	 * 取消
	 */
	public void bnCancel_ActionPerformed(ActionEvent actionEvent) {
		//closeCancel();
	}

	/**
	 * 校验
	 */
	public void bnCheck_ActionPerformed(ActionEvent actionEvent) {
		doCheck();
	}

	/**
	 * 收合数据字典树
	 */
	public void bnDD_ActionPerformed(ActionEvent actionEvent) {
		showSlogan(NCLangRes.getInstance().getStrByID("10241201",
				"UPP10241201-001295")/* @res "用友万岁" */, getBnCheck().getText());
		//
		boolean bDDVisible = getSclPnTree().isVisible();
		getSclPnTree().setVisible(!bDDVisible);
		getBnDD().setText((bDDVisible) ? ">" : "<");
		validate();
	}

	/**
	 * OK
	 */
	public void bnOK_ActionPerformed(ActionEvent actionEvent) {
		//closeOK();
	}

	/**
	 * connEtoC1: (BnOK.action.actionPerformed(ActionEvent) -->
	 * SqlRepairDefDlg.bnOK_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(ActionEvent arg1) {
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
	 * connEtoC2: (BnCancel.action.actionPerformed(ActionEvent) -->
	 * SqlRepairDefDlg.bnCancel_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC2(ActionEvent arg1) {
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
	 * connEtoC3: (BnCheck.action.actionPerformed(ActionEvent) -->
	 * SqlRepairDefDlg.bnCheck_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnCheck_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4: (TACode.key.keyReleased(KeyEvent) -->
	 * SqlRepairDefDlg.tACode_KeyReleased(LKeyEvent;)V)
	 * 
	 * @param arg1
	 *            KeyEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC4(KeyEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.tACode_KeyReleased(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC5: (TreeCode.mouse.mouseClicked(MouseEvent) -->
	 * SqlRepairDefDlg.treeCode_MouseClicked(LMouseEvent;)V)
	 * 
	 * @param arg1
	 *            MouseEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC5(MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.treeCode_MouseClicked(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC6: (BnDD.action.actionPerformed(ActionEvent) -->
	 * SqlRepairDefDlg.bnDD_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC6(ActionEvent arg1) {
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
				ivjBnCancel.setPreferredSize(new Dimension(90, 22));
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
	 * 返回 BnCheck 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnCheck() {
		if (ivjBnCheck == null) {
			try {
				ivjBnCheck = new UIButton();
				ivjBnCheck.setName("BnCheck");
				ivjBnCheck.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-001296")/* @res "语法校验" */);
				// user code begin {1}
				ivjBnCheck.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnCheck, ivjBnCheck.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnCheck;
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
				ivjBnDD.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnDD, ivjBnDD.getText());
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
				ivjBnOK.setPreferredSize(new Dimension(90, 22));
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
	 * 获得嵌入代码 创建日期：(2003-8-12 16:18:42)
	 * 
	 * @return nc.vo.iuforeport.businessquery.SqlRepairVO
	 */
	public String getCode() {
		return getTACode().getText();
	}

	/**
	 * 获得查询字段定义参照框实例 创建日期：(2004-7-3 16:59:30)
	 * 
	 * @return nc.ui.pub.querymodel.QuoteQueryFlddefDlg
	 */
	private QuoteQueryFlddefDlg getDlgQqf() {
		//		if (m_dlgQqf == null) {
		//			m_dlgQqf = new QuoteQueryFlddefDlg(this, m_defDsName);
		//			m_dlgQqf.setCurrentQmd(m_qeUI.getSelectedObjectNode().getID());
		//		}
		//		return m_dlgQqf;
		return null;
	}

	/**
	 * 获得SQL整理/数据加工定义 创建日期：(2003-8-12 16:18:42)
	 * 
	 * @return nc.vo.iuforeport.businessquery.SqlRepairVO
	 */
	public EmbedCodeVO getEmbedCodeVO() {
		EmbedCodeVO embedCode = new DataProcessVO();
		embedCode.setEmbedCode(getCode());
		return embedCode;
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
				ivjPnSouth.setLayout(getPnSouthFlowLayout());
				getPnSouth().add(getBnCheck(), getBnCheck().getName());
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
	 * 返回 SclPnCode 特性值。
	 * 
	 * @return UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private UIScrollPane getSclPnCode() {
		if (ivjSclPnCode == null) {
			try {
				ivjSclPnCode = new UIScrollPane();
				ivjSclPnCode.setName("SclPnCode");
				ivjSclPnCode.setBorder(new EtchedBorder());
				getSclPnCode().setViewportView(getTACode());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnCode;
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
				ivjSclPnTree.setPreferredSize(new Dimension(152, 363));
				getSclPnTree().setViewportView(getTreeCode());
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
	 * 返回 TACode 特性值。
	 * 
	 * @return editor.RichEditor
	 */
	/* 警告：此方法将重新生成。 */
	private RichEditor getTACode() {
		if (ivjTACode == null) {
			try {
				ivjTACode = new RichEditor();
				ivjTACode.setName("TACode");
				ivjTACode.setBounds(0, 0, 160, 120);
				// user code begin {1}
				ivjTACode.setWordSorter(new JavaWordSorter());
				((DefaultLexAnalyzer) ivjTACode.getLexAnalyzer())
						.setColorSetting(new PfColorSet());
				ivjTACode.setSelectionColor(new Color(0XBCD5E7));
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTACode;
	}

	/**
	 * 返回 TreeCode 特性值。
	 * 
	 * @return UITree
	 */
	/* 警告：此方法将重新生成。 */
	private UITree getTreeCode() {
		if (ivjTreeCode == null) {
			try {
				ivjTreeCode = new UITree();
				ivjTreeCode.setName("TreeCode");
				ivjTreeCode.setBounds(0, 0, 78, 72);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTreeCode;
	}

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * 
	 * @return JPanel
	 */
	/* 警告：此方法将重新生成。 */
	@SuppressWarnings("unused")
	private JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new UIPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new BorderLayout());
				getUIDialogContentPane().add(getPnSouth(), "South");
				getUIDialogContentPane().add(getSclPnCode(), "Center");
				getUIDialogContentPane().add(getSclPnTree(), "West");
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
		getBnOK().addActionListener(ivjEventHandler);
		getBnCancel().addActionListener(ivjEventHandler);
		getBnCheck().addActionListener(ivjEventHandler);
		getTACode().addKeyListener(ivjEventHandler);
		getTreeCode().addMouseListener(ivjEventHandler);
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
			setName("SqlRepairDefDlg");
			setLayout(new BorderLayout());
			setSize(640, 480);
			//add(getPnSouth(), "South");
			add(getSclPnCode(), "Center");
			add(getSclPnTree(), "West");
			//add(getPnNorth(), "North");
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * 初始化树 创建日期：(2003-9-24 10:05:32)
	 */
	private void initTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(NCLangRes
				.getInstance().getStrByID("10241201", "UPP10241201-001297")/*
																		    * @res
																		    * "代码向导树"
																		    */);
		//加节点
		DefaultMutableTreeNode node = CodeWizManager.getRootNode("A",
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001298")/* @res "通用" */, null);
		root.add(node);

		node = CodeWizManager.getRootNode("C",
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001191")/* @res "数据加工" */, m_defDsName);
		root.add(node);
		//树模型
		DefaultTreeModel tm = new DefaultTreeModel(root);
		getTreeCode().setModel(tm);
		//修饰
		getTreeCode().setRootVisible(false);
		getTreeCode().expandRow(2);
		getTreeCode().expandRow(1);
		getTreeCode().expandRow(0);
	}

	/**
	 * 双击选中 创建日期：(2003-4-2 20:38:58)
	 * 
	 * @param str
	 *            java.lang.String
	 */
	private void onSelect(String str, int iFunc) {
		//选中
		int pos = getTACode().getSelectionStart();
		getTACode().insert(str, pos);
		getTACode().requestFocus();
		getTACode().setSelectionStart(pos + str.length() - iFunc);
		getTACode().setSelectionEnd(pos + str.length() - iFunc);
	}

	/**
	 * 创建日期：(2003-11-26 16:10:10)
	 * 
	 * @param newQeUI
	 *            nc.ui.pub.querymodel.QueryEngineUI
	 */
	private void setDsInfo(String dsName) {
	}

	/**
	 * 设置查询模型定义 创建日期：(2003-8-12 15:39:11)
	 * 
	 * @param qmd
	 *            nc.vo.iuforeport.businessquery.QueryModelDef
	 */
	private void setEmbedCodeVO(EmbedCodeVO embedCode) {
		if (embedCode != null) {
			String code = embedCode.getEmbedCode();
			getTACode().setText(code);
		}
	}

	/**
	 * 显示标语 创建日期：(2003-9-26 16:38:28)
	 */
	public void showSlogan(String strNew, final String strOld) {
		getBnCheck().setText(strNew);
		Runnable run = new Runnable() {
			public void run() {
				try {
					Thread.sleep(50); //延迟预览效果
					getBnCheck().setText(strOld);
				} catch (InterruptedException e) {
					AppDebug.debug(e);//@devTools System.out.println(e);
				}
			}
		};
		SwingUtilities.invokeLater(run);
	}

	/**
	 * 键盘释放事件响应
	 */
	public void tACode_KeyReleased(KeyEvent keyEvent) {
		int c = keyEvent.getKeyCode();
		if (c == KeyEvent.VK_E && keyEvent.isControlDown())
			onSelect("equalsIgnoreCase()", 1);
		else if (c == KeyEvent.VK_F && keyEvent.isControlDown())
			onSelect("for (int i = 0; i < ; i++){\n\n}\n", 12);
		else if (c == KeyEvent.VK_G && keyEvent.isControlDown())
			onSelect("getDataSet()", 0);
		else if (c == KeyEvent.VK_I && keyEvent.isControlDown())
			onSelect("if () {\n\n} else {\n\n}\n", 15);
		else if (c == KeyEvent.VK_L && keyEvent.isControlDown())
			//折行显示
			getTACode().setLineWrap(!getTACode().getLineWrap());
		else if (c == KeyEvent.VK_M && keyEvent.isControlDown())
			//收合代码向导树
			bnDD_ActionPerformed(null);
		else if (c == KeyEvent.VK_P && keyEvent.isControlDown())
			onSelect("AppDebug.debug()", 1);//@devTools System.out.println()", 1);
		else if (c == KeyEvent.VK_S && keyEvent.isControlDown())
			onSelect("setDataSet()", 1);
		else if (c == KeyEvent.VK_W && keyEvent.isControlDown()) {
			//格式化代码
			String code = FormatText.doFormat(getCode());
			getTACode().setText(code);
		} else if (c == KeyEvent.VK_SLASH && keyEvent.isControlDown()) {
			//全部注释
			getTACode().setText("/*\n" + getCode() + "\n*/");
		} else if (c == KeyEvent.VK_BACK_SLASH && keyEvent.isControlDown()) {
			//取消全部注释
			String strText = (getCode() == null) ? "" : getCode().trim();
			if (strText.startsWith("/*") && strText.endsWith("*/")) {
				strText = strText.substring(2, strText.length() - 2);
				getTACode().setText(strText);
			}
		} else if (c == KeyEvent.VK_1 && keyEvent.isControlDown()) {
			bnCheck_ActionPerformed(null);
		}
	}

	/**
	 * 代码向导树点击响应
	 */
	public void treeCode_MouseClicked(MouseEvent mouseEvent) {
		TreePath path = getTreeCode().getSelectionPath();
		if (path != null && mouseEvent.getClickCount() == 2) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (node.getUserObject() instanceof CodeWizardVO) {
				//获得向导信息
				CodeWizardVO cw = (CodeWizardVO) node.getUserObject();
				String strCode = "";

				String className = cw.getClassname();
				Class cls = null;
				try {
					cls = Class.forName(className);
				} catch (ClassNotFoundException e1) {
					AppDebug.debug(e1);
				}
				Object objClass = null;

				if (AbstractCodeWizDlg.class.isAssignableFrom(cls)) {
					try {
						//反射（使用能找到父组件的构造方式）
						Constructor constructor = cls
								.getConstructor(new Class[] { Container.class });
						objClass = constructor
								.newInstance(new Object[] { this });
					} catch (Exception e) {
						AppDebug.debug(e);
						return;
					}
					//获得向导实例
					AbstractCodeWizDlg icwd = (AbstractCodeWizDlg)objClass;
					icwd.getCodeWizParamVO().setDefDsName(m_defDsName);

					//组织传入信息
					AdvCodeWizVO acw = new AdvCodeWizVO();
					boolean bDataProcess = true;
					if (bDataProcess && cw.getWizcode().equals("C001")) {
						//获得数据字典
						Datadict dd = getTabPn().getDatadict();
						acw.setDatadict(dd);
					}
					//				icwd.setParent(this);
					icwd.setCodeWizParamVO(acw);
					icwd.showModal();
					icwd.destroy();
					if (icwd.getResult() == UIDialog.ID_OK) {
						//获得生成代码
						try {
							strCode = icwd.generateCode();
						} catch (Exception e) {
							AppDebug.debug(e);
							MessageDialog.showWarningDlg(this, NCLangRes
									.getInstance().getStrByID("10241201",
											"UPP10241201-000099")/*
																  * @res "查询引擎"
																  */, NCLangRes.getInstance().getStrByID("10241201",
									"UPP10241201-001299")/*
														  * @res "生成代码有误："
														  */
									+ e.getMessage());
						}
					}
				} else {
					try {
						//实例化
						objClass = cls.newInstance();
					} catch (Exception e) {
						AppDebug.debug(e);
						return;
					}
					//获得向导实例
					AbstractCodeWizModel icw = (AbstractCodeWizModel) objClass;
					icw.setDefDsName(m_defDsName);
					//获得向导参数
					CodeWizParamVO  cp = icw.getCodeParam();
					//设置参数值
					DefaultCodeWizDlg dlg = new DefaultCodeWizDlg(this, cp);
					//dlg.setCodeParam(cp, m_dsName, getDlgQqf());
					dlg.setCodeParam(cp,getDlgQqf());
					dlg.showModal();
					dlg.destroy();
					if (dlg.getResult() == UIDialog.ID_OK) {
						//获得生成代码（注意，cp已变化）
						try {
							strCode = icw.generateCode(cp);
						} catch (Exception e) {
							AppDebug.debug(e);
							MessageDialog.showWarningDlg(this, NCLangRes
									.getInstance().getStrByID("10241201",
											"UPP10241201-000099")/*
																  * @res "查询引擎"
																  */, NCLangRes.getInstance().getStrByID("10241201",
									"UPP10241201-001299")/*
														  * @res "生成代码有误："
														  */
									+ e.getMessage());
						}
					}
				}

				if (!"".equals(strCode)) {
					strCode = "\n" + strCode + "\n";
					//更新代码文本
					onSelect(strCode, 0);
					//刷新
					getTACode().repaint();
				}
			}
		}
	}

	private void setDefDsName(String defDsName) {
		m_defDsName = defDsName;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#check()
	 */
	public String check() {
		return null;
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
	public void getResultFromPanel(BIQueryModelDef qmd) {
		//回设
		qmd.getBaseModel().setDataProcessVO((DataProcessVO) getEmbedCodeVO());
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		setEmbedCodeVO(qmd.getDataProcessVO());
	}

	/**
	 * 初始化 创建日期：(2003-9-24 10:05:32)
	 */
	public void init() {
		String dsNameForDef = getTabPn().getDefDsName();
		setDefDsName(dsNameForDef);
		setDsInfo(getTabPn().getQueryModelDef().getDsName());
		initTree();
		setEmbedCodeVO(getTabPn().getQueryModelDef().getDataProcessVO());
	}

	/**
	 * 校验
	 */
	public void doCheck() {
		try {
//			DataProcessUtilBO_Client.checkDataProcess((DataProcessVO) getEmbedCodeVO(),
//					BIModelUtil.getLoginEnv());
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-000099")/* @res "查询引擎" */,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-001024")/* @res "编译成功" */);
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */, NCLangRes
							.getInstance().getStrByID("10241201",
									"UPP10241201-001294")/* @res "编译错误" */);
		}
	}
}
 