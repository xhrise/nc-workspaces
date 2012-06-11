/*
 * �������� 2005-5-24
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
 * �����ֵ�ѡ���
 */
public class TableInfoDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//�����
	private AbstractQueryDesignSetPanel m_parent = null;

	//�����ж��Ƿ�ѡ���ظ��Ĺ�ϣ��
	private Hashtable<String, String> m_hashTableId = null;

	//�����ֵ���
	private ObjectTreeView m_tree = null;

	//ѡ�б���
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
	 * TableInfoDlg ������ע�⡣
	 * @deprecated
	 */
	public TableInfoDlg() {
		super();
		initialize();
	}

	/**
	 * TableInfoDlg ������ע�⡣
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
	 * ����
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
							"UPP10241201-000099")/* @res "��ѯ����" */, NCLangRes
							.getInstance().getStrByID("10241201",
									"UPP10241201-000933")/*
														  * @res
														  * "δѡ�нڵ��ѡ�еĽڵ㲻�Ǳ�"
														  */);
			return;
		}
		//��ñ���Ϣ
		String		strNote = null;//5.01
		String tableCode = td.getID();
		if (td instanceof TableDefWithAlias){
			tableCode = ((TableDefWithAlias) td).getRealName();
			strNote = ((TableDefWithAlias) td).getNote();
		}
		String tableAlias = td.getID();
		String tableName = td.getDisplayName();
		//ת��ΪVO
		m_ft = new FromTableVO();
		m_ft.setTablecode(tableCode);
		m_ft.setTablealias(tableAlias);
		m_ft.setTabledisname(tableName);
		m_ft.setNote(strNote);
		
		//��������
		if (m_hashTableId.containsKey(tableAlias)) {
			int iTemp = MessageDialog.showYesNoDlg(this, NCLangRes
					.getInstance().getStrByID("10241201", "UPP10241201-000099")/*
																			    * @res
																			    * "��ѯ����"
																			    */, NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000932")/*
										  * @res "�Ѿ�ѡ���ͬ�����Ƿ���ӱ���"
										  */);
			if (iTemp != UIDialog.ID_YES)
				return;
			//���ɱ��������ʾ��
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	 * ���ұ� �������ڣ�(2003-9-27 13:13:46)
	 * 
	 * @param strCode
	 *            java.lang.String
	 */
	private void doFind() {
		//��������ֵ�ʵ��
		Datadict dd = m_parent.getTabPn().getDatadict();
		//����
		FindDlg dlg = new FindDlg(this, NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-000121")/* @res "����" */, true);
		dlg.setObjectTrees(new ObjectTree[] { dd });
		if (dlg.showModal() == UIDialog.ID_OK) {
			ObjectNode objnode = dlg.getSearch();
			if (objnode != null)
				m_tree.selectNode(objnode);
		}
	}

	/**
	 * ��λ �������ڣ�(2003-9-27 13:13:46)
	 * 
	 * @param strCode
	 *            java.lang.String
	 */
	@SuppressWarnings("unused")
	private void doLocate(String strCode, String kind) {
		//���Ŀ�����ĸ�
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) m_tree
				.getModel().getRoot();
		//�������˳�����������õ�ö��
		Enumeration enums = root.depthFirstEnumeration();
		while (enums.hasMoreElements()) {
			//�����һ��ö��Ԫ��
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) enums
					.nextElement();
			if (node.getUserObject() instanceof ObjectNode) {
				ObjectNode dn = (ObjectNode) node.getUserObject();
				//ѡ��
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
	 * ���� BnAdd ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnAdd() {
		if (ivjBnAdd == null) {
			try {
				ivjBnAdd = new UIButton();
				ivjBnAdd.setName("BnAdd");
				ivjBnAdd.setText(NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000330")/* @res "����" */);
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
	 * ���� BnFind ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnFind() {
		if (ivjBnFind == null) {
			try {
				ivjBnFind = new UIButton();
				ivjBnFind.setName("BnFind");
				ivjBnFind.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000121")/* @res "����" */);
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
	 * ��ü��̼��� �������ڣ�(2003-9-27 14:25:02)
	 * 
	 * @return KeyListener
	 */
	@SuppressWarnings("unused")
	private KeyListener getKeyListener() {
		return null;
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
			ivjPnSouthFlowLayout.setHgap(8);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnSouthFlowLayout;
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
	 * ���ѡ�е�TABLE���� �������ڣ�(2003-4-2 16:49:17)
	 * 
	 * @return nc.bs.com.datadict.TableDef
	 */
	private BizObject getSelTableDef() {
		BizObject td = null;
		//���ѡ�нڵ�
		ObjectNode on = m_tree.getSelectedObjectNode();
		if (on != null && on.getKind() != null) {
			if (on.getKind().equals(DatadictNode.TableKind))
				td = on.getObject();
			else if (on.getKind().equals(DatadictNode.ViewKind))
				//��ͼ���崦��
				td = on.getObject();
		}
		return td;
	}

	/**
	 * ���ѡ�еı�VO �������ڣ�(2003-4-2 16:49:17)
	 * 
	 * @return nc.bs.com.datadict.TableDef
	 */
	public FromTableVO getSelTableVO() {
		return m_ft;
	}

	/**
	 * ���� UIDialogContentPane ����ֵ��
	 * 
	 * @return JPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 * @i18n miufo00155=--------- δ��׽�����쳣 ---------
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
		getBnCancel().addActionListener(ivjEventHandler);
		getBnOK().addActionListener(ivjEventHandler);
		getBnAdd().addActionListener(ivjEventHandler);
		getBnFind().addActionListener(ivjEventHandler);
		getPnSouth().addMouseListener(ivjEventHandler);
		getCbbBusiDatadict().addItemListener(ivjEventHandler);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("TableInfoDlg");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(340, 480);
			setTitle(NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000934")/* @res "ѡȡ��" */);
			setContentPane(getUIDialogContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		getPnNorth().setVisible(false);
		//��������ֵ�ʵ��
		Datadict dd = m_parent.getTabPn().getDatadict();
		//��ʼ�������ֵ���
		initTree(dd);
		//����������
		getCbbBusiDatadict().removeItemListener(ivjEventHandler);
		setItems();
		getCbbBusiDatadict().addItemListener(ivjEventHandler);
		//���ö�λ������
		//getTFLocate().setObjectTree(m_tree);
		//getTFLocate().setKind(DatadictNode.TableKind);
		setResizable(true);
		// user code end
	}

	/**
	 * ��ʼ�������ֵ��� �������ڣ�(2003-4-2 16:40:20)
	 */
	private void initTree(Datadict dd) {
		//������
		m_tree = new ObjectTreeView();
		//��ʾ����
		//m_tree.setCompareRule(ObjectTreeNode.COMPARE_ID);
		//m_tree.setDisRule(ObjectTreeNode.DIS_ID_AND_DISNAME);
		//������
		if( dd != null ){
			m_tree.setObjectTree(new ObjectTree[] { dd });
			//��ʾ��
			m_tree.expandRow(0);
		}else{
			//m_tree.setObjectTree(new ObjectTree[]{});
			MessageDialog.showErrorDlg(this, StringResource.getStringResource("ubiquery0126"), 
					StringResource.getStringResource("mbiquery0130"));//"�����ֵ�Ϊ�գ�û�п���ѡ��ı�");
			return;
		}
		getSclPnTree().setViewportView(m_tree);
		//��Ӽ���
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
	 * ���ñ�ID��ϣ�� �������ڣ�(2003-4-2 17:12:21)
	 * 
	 * @param hashTableId
	 *            java.util.Hashtable
	 */
	public void setHashTableId(Hashtable<String, String> hashTableId) {
		m_hashTableId = hashTableId;
		//getTFLocate().setText("");
	}

	/**
	 * ѡ�����ڵ㲢ʹ��ɼ� �������ڣ�(01-6-13 12:44:15)
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
	 * ����ҵ�������ֵ�ӿ����� �������ڣ�(2004-11-24 09:42:40)
	 * 
	 * @param iBusiDatadicts
	 *            nc.ui.pub.querymodel.IBusiDatadict[]
	 */
	public void setItems() {
		/*
		 * //��ÿ�ѡ�ֵ�ӿ����� IBusiDatadict[] iBusiDatadicts =
		 * m_parent.getTabPn().getQueryDefDlg() .getIBusiDatadicts(); //��õ�ǰ�ӿ����
		 * int iSelIndex = m_parent.getTabPn().getQueryDefDlg()
		 * .getSelBusiDatadict(); //���������� getCbbBusiDatadict().removeAllItems();
		 * int iLen = (iBusiDatadicts == null) ? 0 : iBusiDatadicts.length; for
		 * (int i = 0; i < iLen; i++)
		 * getCbbBusiDatadict().addItem(iBusiDatadicts[i].getNote()); if
		 * (iSelIndex < iLen) getCbbBusiDatadict().setSelectedIndex(iSelIndex);
		 */
	}

	/**
	 * ������ѡ��ı��¼���Ӧ
	 */
	public void cbbBusiDatadict_ItemStateChanged(ItemEvent itemEvent) {

		if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
			changeBusiDatadict();
		}
	}

	/**
	 * �л�ҵ�������ֵ� �������ڣ�(2004-11-24 09:12:41)
	 * 
	 * @param dsName
	 *            java.lang.String
	 * @param env
	 *            nc.vo.pub.querymodel.EnvInfo
	 */
	private void changeBusiDatadict() {
		/*
		 * //��õ�ǰ�ӿ���� int iOldIndex = m_parent.getTabPn().getQueryDefDlg()
		 * .getSelBusiDatadict(); // int iSelIndex =
		 * getCbbBusiDatadict().getSelectedIndex(); if (iSelIndex == iOldIndex)
		 * return; //��ÿ�ѡ�ֵ�ӿ����� IBusiDatadict[] iBusiDatadicts =
		 * m_parent.getTabPn().getQueryDefDlg() .getIBusiDatadicts();
		 * //���ѡ��ҵ�������ֵ� IBusiDatadict selBusiDatadict =
		 * iBusiDatadicts[iSelIndex]; //��ò�ѯ����ִ������Դ String dsName =
		 * m_parent.getTabPn().getQueryBaseDef().getDsName(); //��������ֵ��ϣ��
		 * Hashtable hashDatadict = m_parent.getTabPn().getQueryDefDlg()
		 * .getHashDatadict(); String strSelIndex = String.valueOf(iSelIndex);
		 * //����л���������ֵ� Datadict selDatadict = null; if
		 * (hashDatadict.containsKey(strSelIndex)) selDatadict = (Datadict)
		 * hashDatadict.get(strSelIndex); else { try { selDatadict =
		 * selBusiDatadict.makeDatadict(dsName, new EnvInfo( true)); } catch
		 * (Exception e) { AppDebug.debug(e);
		 * MessageDialog.showWarningDlg(this, NCLangRes
		 * .getInstance().getStrByID("10241201", "UPP10241201-000099")/* @res
		 * "��ѯ����" NCLangRes.getInstance().getStrByID("10241201",
		 * "UPP10241201-000935")/* @res "ҵ�������ֵ��������" + e.getMessage());
		 * getCbbBusiDatadict().setSelectedIndex(iOldIndex); return; }
		 * hashDatadict.put(strSelIndex, selDatadict); } //ˢ�½���
		 * initTree(selDatadict); //���豾�ζ����ҵ�������ֵ�ӿں������ֵ��ʵ�� QueryDefDlg qdDlg =
		 * m_parent.getTabPn().getQueryDefDlg();
		 * qdDlg.setSelBusiDatadict(iSelIndex); qdDlg.setDatadict(selDatadict);
		 * //������� m_parent.getTabPn().setDatadict(selDatadict);
		 */
	}

	/**
	 * connEtoC5: (PnSouth.mouse.mouseClicked(MouseEvent) -->
	 * TableInfoDlg.pnSouth_MouseClicked(LMouseEvent;)V)
	 * 
	 * @param arg1
	 *            MouseEvent
	 */
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	 * ���� CbbBusiDatadict ����ֵ��
	 * 
	 * @return UIComboBox
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnNorthFlowLayout ����ֵ��
	 * 
	 * @return FlowLayout
	 */
	/* ���棺�˷������������ɡ� */
	private FlowLayout getPnNorthFlowLayout() {
		FlowLayout ivjPnNorthFlowLayout = null;
		try {
			/* �������� */
			ivjPnNorthFlowLayout = new FlowLayout();
			ivjPnNorthFlowLayout.setVgap(9);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnNorthFlowLayout;
	}

	/**
	 * �ϲ����������¼���Ӧ���л�ҵ�������ֵ䣩
	 */
	public void pnSouth_MouseClicked(MouseEvent mouseEvent) {
		if (mouseEvent.isControlDown() && mouseEvent.getClickCount() == 2) {
			getPnNorth().setVisible(true);
		}
	}

	/**
	 * �ϲ����������¼���Ӧ���л�ҵ�������ֵ䣩
	 */
	public void doAdd() {
		BizObject td = getSelTableDef();
		if (td == null) {
			MessageDialog
					.showErrorDlg(this,
							NCLangRes.getInstance().getStrByID("10241201",
									"UPP10241201-000099")/* @res "��ѯ����" */,
							NCLangRes.getInstance().getStrByID("10241201",
									"UPP10241201-000931")/* @res "ѡ�еĽڵ㲻�Ǳ�" */);
			return;
		}
		//��ñ���Ϣ
		String	strNote = null;// 5.01xiugai
		String tableCode = td.getID();
		if (td instanceof TableDefWithAlias){
			tableCode = ((TableDefWithAlias) td).getRealName();
			strNote = ((TableDefWithAlias) td).getNote(); //5.01xiugai
		}
		String tableAlias = td.getID();
		String tableName = td.getDisplayName();
		//ת��ΪVO
		m_ft = new FromTableVO();
		m_ft.setTablecode(tableCode);
		m_ft.setTablealias(tableAlias);
		m_ft.setTabledisname(tableName);
		m_ft.setNote(strNote); //5.01xiugai
		
		//��������
		if (m_hashTableId.containsKey(tableAlias)) {
			int iTemp = MessageDialog.showYesNoDlg(this, NCLangRes
					.getInstance().getStrByID("10241201", "UPP10241201-000099")/*
																			    * @res
																			    * "��ѯ����"
																			    */, NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000932")/*
										  * @res "�Ѿ�ѡ���ͬ�����Ƿ���ӱ���"
										  */);
			if (iTemp != UIDialog.ID_YES)
				return;
			//���ɱ��������ʾ��
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
		//���ӱ���
		if (m_parent instanceof SetTablePanel) {
			((SetTablePanel) m_parent).doAdd(m_ft);
		} else if (m_parent instanceof SetTableJoinPanel) {
			((SetTableJoinPanel) m_parent).getGraphEd().doAdd(null, m_ft);
		}
		//���¹�ϣ��
		m_hashTableId.put(tableAlias, tableName);
	}
}
  