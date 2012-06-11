/*
 * �������� 2005-5-31
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
 * ���ݼӹ��������ý���
 */
public class SetProcessCodeUI extends AbstractQueryDesignSetPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0057";//"���ݼӹ�";

	//��������Դ
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
	 * SetProcessCodeUI ������ע�⡣
	 */
	public SetProcessCodeUI() {
		super();
		initialize();
	}

	/**
	 * ȡ��
	 */
	public void bnCancel_ActionPerformed(ActionEvent actionEvent) {
		//closeCancel();
	}

	/**
	 * У��
	 */
	public void bnCheck_ActionPerformed(ActionEvent actionEvent) {
		doCheck();
	}

	/**
	 * �պ������ֵ���
	 */
	public void bnDD_ActionPerformed(ActionEvent actionEvent) {
		showSlogan(NCLangRes.getInstance().getStrByID("10241201",
				"UPP10241201-001295")/* @res "��������" */, getBnCheck().getText());
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	 * ���� BnCancel ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000000")/* @res "ȡ��" */);
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
	 * ���� BnCheck ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnCheck() {
		if (ivjBnCheck == null) {
			try {
				ivjBnCheck = new UIButton();
				ivjBnCheck.setName("BnCheck");
				ivjBnCheck.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-001296")/* @res "�﷨У��" */);
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
	 * ���� BnDD ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnDD() {
		if (ivjBnDD == null) {
			try {
				ivjBnDD = new UIButton();
				ivjBnDD.setName("BnDD");
				ivjBnDD.setIButtonType(0/** JavaĬ��(�Զ���) */
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
	 * ���� BnOK ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnOK() {
		if (ivjBnOK == null) {
			try {
				ivjBnOK = new UIButton();
				ivjBnOK.setName("BnOK");
				ivjBnOK.setText(NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000001")/* @res "ȷ��" */);
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
	 * ���Ƕ����� �������ڣ�(2003-8-12 16:18:42)
	 * 
	 * @return nc.vo.iuforeport.businessquery.SqlRepairVO
	 */
	public String getCode() {
		return getTACode().getText();
	}

	/**
	 * ��ò�ѯ�ֶζ�����տ�ʵ�� �������ڣ�(2004-7-3 16:59:30)
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
	 * ���SQL����/���ݼӹ����� �������ڣ�(2003-8-12 16:18:42)
	 * 
	 * @return nc.vo.iuforeport.businessquery.SqlRepairVO
	 */
	public EmbedCodeVO getEmbedCodeVO() {
		EmbedCodeVO embedCode = new DataProcessVO();
		embedCode.setEmbedCode(getCode());
		return embedCode;
	}

	/**
	 * ���� PnNorth ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnSouth ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnSouthFlowLayout ����ֵ��
	 * 
	 * @return FlowLayout
	 */
	/* ���棺�˷������������ɡ� */
	private FlowLayout getPnSouthFlowLayout() {
		FlowLayout ivjPnSouthFlowLayout = null;
		try {
			/* �������� */
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
	 * ���� SclPnCode ����ֵ��
	 * 
	 * @return UIScrollPane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� SclPnTree ����ֵ��
	 * 
	 * @return UIScrollPane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� TACode ����ֵ��
	 * 
	 * @return editor.RichEditor
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� TreeCode ����ֵ��
	 * 
	 * @return UITree
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� UIDialogContentPane ����ֵ��
	 * 
	 * @return JPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		AppDebug.debug("--------- δ��׽�����쳣 ---------");//@devTools System.out.println("--------- δ��׽�����쳣 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ������
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ʼ���� �������ڣ�(2003-9-24 10:05:32)
	 */
	private void initTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(NCLangRes
				.getInstance().getStrByID("10241201", "UPP10241201-001297")/*
																		    * @res
																		    * "��������"
																		    */);
		//�ӽڵ�
		DefaultMutableTreeNode node = CodeWizManager.getRootNode("A",
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001298")/* @res "ͨ��" */, null);
		root.add(node);

		node = CodeWizManager.getRootNode("C",
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001191")/* @res "���ݼӹ�" */, m_defDsName);
		root.add(node);
		//��ģ��
		DefaultTreeModel tm = new DefaultTreeModel(root);
		getTreeCode().setModel(tm);
		//����
		getTreeCode().setRootVisible(false);
		getTreeCode().expandRow(2);
		getTreeCode().expandRow(1);
		getTreeCode().expandRow(0);
	}

	/**
	 * ˫��ѡ�� �������ڣ�(2003-4-2 20:38:58)
	 * 
	 * @param str
	 *            java.lang.String
	 */
	private void onSelect(String str, int iFunc) {
		//ѡ��
		int pos = getTACode().getSelectionStart();
		getTACode().insert(str, pos);
		getTACode().requestFocus();
		getTACode().setSelectionStart(pos + str.length() - iFunc);
		getTACode().setSelectionEnd(pos + str.length() - iFunc);
	}

	/**
	 * �������ڣ�(2003-11-26 16:10:10)
	 * 
	 * @param newQeUI
	 *            nc.ui.pub.querymodel.QueryEngineUI
	 */
	private void setDsInfo(String dsName) {
	}

	/**
	 * ���ò�ѯģ�Ͷ��� �������ڣ�(2003-8-12 15:39:11)
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
	 * ��ʾ���� �������ڣ�(2003-9-26 16:38:28)
	 */
	public void showSlogan(String strNew, final String strOld) {
		getBnCheck().setText(strNew);
		Runnable run = new Runnable() {
			public void run() {
				try {
					Thread.sleep(50); //�ӳ�Ԥ��Ч��
					getBnCheck().setText(strOld);
				} catch (InterruptedException e) {
					AppDebug.debug(e);//@devTools System.out.println(e);
				}
			}
		};
		SwingUtilities.invokeLater(run);
	}

	/**
	 * �����ͷ��¼���Ӧ
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
			//������ʾ
			getTACode().setLineWrap(!getTACode().getLineWrap());
		else if (c == KeyEvent.VK_M && keyEvent.isControlDown())
			//�պϴ�������
			bnDD_ActionPerformed(null);
		else if (c == KeyEvent.VK_P && keyEvent.isControlDown())
			onSelect("AppDebug.debug()", 1);//@devTools System.out.println()", 1);
		else if (c == KeyEvent.VK_S && keyEvent.isControlDown())
			onSelect("setDataSet()", 1);
		else if (c == KeyEvent.VK_W && keyEvent.isControlDown()) {
			//��ʽ������
			String code = FormatText.doFormat(getCode());
			getTACode().setText(code);
		} else if (c == KeyEvent.VK_SLASH && keyEvent.isControlDown()) {
			//ȫ��ע��
			getTACode().setText("/*\n" + getCode() + "\n*/");
		} else if (c == KeyEvent.VK_BACK_SLASH && keyEvent.isControlDown()) {
			//ȡ��ȫ��ע��
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
	 * �������������Ӧ
	 */
	public void treeCode_MouseClicked(MouseEvent mouseEvent) {
		TreePath path = getTreeCode().getSelectionPath();
		if (path != null && mouseEvent.getClickCount() == 2) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (node.getUserObject() instanceof CodeWizardVO) {
				//�������Ϣ
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
						//���䣨ʹ�����ҵ�������Ĺ��췽ʽ��
						Constructor constructor = cls
								.getConstructor(new Class[] { Container.class });
						objClass = constructor
								.newInstance(new Object[] { this });
					} catch (Exception e) {
						AppDebug.debug(e);
						return;
					}
					//�����ʵ��
					AbstractCodeWizDlg icwd = (AbstractCodeWizDlg)objClass;
					icwd.getCodeWizParamVO().setDefDsName(m_defDsName);

					//��֯������Ϣ
					AdvCodeWizVO acw = new AdvCodeWizVO();
					boolean bDataProcess = true;
					if (bDataProcess && cw.getWizcode().equals("C001")) {
						//��������ֵ�
						Datadict dd = getTabPn().getDatadict();
						acw.setDatadict(dd);
					}
					//				icwd.setParent(this);
					icwd.setCodeWizParamVO(acw);
					icwd.showModal();
					icwd.destroy();
					if (icwd.getResult() == UIDialog.ID_OK) {
						//������ɴ���
						try {
							strCode = icwd.generateCode();
						} catch (Exception e) {
							AppDebug.debug(e);
							MessageDialog.showWarningDlg(this, NCLangRes
									.getInstance().getStrByID("10241201",
											"UPP10241201-000099")/*
																  * @res "��ѯ����"
																  */, NCLangRes.getInstance().getStrByID("10241201",
									"UPP10241201-001299")/*
														  * @res "���ɴ�������"
														  */
									+ e.getMessage());
						}
					}
				} else {
					try {
						//ʵ����
						objClass = cls.newInstance();
					} catch (Exception e) {
						AppDebug.debug(e);
						return;
					}
					//�����ʵ��
					AbstractCodeWizModel icw = (AbstractCodeWizModel) objClass;
					icw.setDefDsName(m_defDsName);
					//����򵼲���
					CodeWizParamVO  cp = icw.getCodeParam();
					//���ò���ֵ
					DefaultCodeWizDlg dlg = new DefaultCodeWizDlg(this, cp);
					//dlg.setCodeParam(cp, m_dsName, getDlgQqf());
					dlg.setCodeParam(cp,getDlgQqf());
					dlg.showModal();
					dlg.destroy();
					if (dlg.getResult() == UIDialog.ID_OK) {
						//������ɴ��루ע�⣬cp�ѱ仯��
						try {
							strCode = icw.generateCode(cp);
						} catch (Exception e) {
							AppDebug.debug(e);
							MessageDialog.showWarningDlg(this, NCLangRes
									.getInstance().getStrByID("10241201",
											"UPP10241201-000099")/*
																  * @res "��ѯ����"
																  */, NCLangRes.getInstance().getStrByID("10241201",
									"UPP10241201-001299")/*
														  * @res "���ɴ�������"
														  */
									+ e.getMessage());
						}
					}
				}

				if (!"".equals(strCode)) {
					strCode = "\n" + strCode + "\n";
					//���´����ı�
					onSelect(strCode, 0);
					//ˢ��
					getTACode().repaint();
				}
			}
		}
	}

	private void setDefDsName(String defDsName) {
		m_defDsName = defDsName;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#check()
	 */
	public String check() {
		return null;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getPanelTitle()
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		//����
		qmd.getBaseModel().setDataProcessVO((DataProcessVO) getEmbedCodeVO());
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		setEmbedCodeVO(qmd.getDataProcessVO());
	}

	/**
	 * ��ʼ�� �������ڣ�(2003-9-24 10:05:32)
	 */
	public void init() {
		String dsNameForDef = getTabPn().getDefDsName();
		setDefDsName(dsNameForDef);
		setDsInfo(getTabPn().getQueryModelDef().getDsName());
		initTree();
		setEmbedCodeVO(getTabPn().getQueryModelDef().getDataProcessVO());
	}

	/**
	 * У��
	 */
	public void doCheck() {
		try {
//			DataProcessUtilBO_Client.checkDataProcess((DataProcessVO) getEmbedCodeVO(),
//					BIModelUtil.getLoginEnv());
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-000099")/* @res "��ѯ����" */,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-001024")/* @res "����ɹ�" */);
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "��ѯ����" */, NCLangRes
							.getInstance().getStrByID("10241201",
									"UPP10241201-001294")/* @res "�������" */);
		}
	}
}
 