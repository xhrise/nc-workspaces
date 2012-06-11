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
 * �����ֶ����(��) �������ڣ�(2005-6-21 10:35:33)
 * 
 * @author���쿡��
 */
public class SetColumnPanel extends AbstractQueryDesignSetPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0051";//"��ѯ�ֶ�";

	//��ѯ�ֶ�ϸ�����(��)
	private SetFldPanel m_pnSetFld = null;

	//���Ӧ���ֶ��б�
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
	 * SetColumnPanel ������ע�⡣
	 */
	public SetColumnPanel() {
		super();
		initialize();
	}

	/**
	 * Add
	 */
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
		//���ѡ���ֶ�
		SelectFldVO[] sfs = getGenFlds();
		//		if (sfs == null) {
		//			MessageDialog.showWarningDlg(this, "UFBI", "ѡ�нڵ�ֻ�����ֶι���");
		//			return;
		//		}
		//�Ϸ��Լ��
		Hashtable hashFldAlias = getFldPanel().getHashFldAlias(-1);
		String strErr = checkMultiSelect(sfs, hashFldAlias);
		if (strErr != null) {
			MessageDialog.showWarningDlg(this, "UFBI", strErr);
			return;
		}
		//�����ֶ�ϸ�����
		getFldPanel().doAdd(sfs);
	}

	/**
	 * �Ϸ���У��
	 */
	public String check() {
		return getFldPanel().check();
	}
	public String checkOnSwitch(){
		return getFldPanel().checkOnSwitch();
	}

	/**
	 * ��������� �������ڣ�(2005-5-13 16:43:08)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/**
	 * ��ý�� �������ڣ�(2005-5-13 16:43:08)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		getFldPanel().getResultFromPanel(qmd);
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
		getBnAdd().addActionListener(ivjEventHandler);
		getTreeTableFld().addMouseListener(ivjEventHandler);
		getTreeTableFld().addTreeSelectionListener(ivjEventHandler);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���ý�� �������ڣ�(2005-5-13 16:43:08)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		getFldPanel().setResultToPanel(qmd);
	}

	private UITree ivjTreeTableFld = null;

	/**
	 * �ֶ���������¼���Ӧ
	 */
	public void treeTableFld_MouseClicked(MouseEvent mouseEvent) {
	}

	/**
	 * ��ʼ����
	 */
	public void initTree() {
		getTreeTableFld().removeTreeSelectionListener(ivjEventHandler);
		//������ݱ�
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		FromTableVO[] fts = qbd.getFromTables();
		//������
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");
		int iLen = (fts == null) ? 0 : fts.length;
		for (int i = 0; i < iLen; i++) {
			//�����ڵ�
			DefaultMutableTreeNode nodeTable = new DefaultMutableTreeNode(
					fts[i]);
			root.add(nodeTable);
			//����ֶ�
			SelectFldVO[] sfs = getFldVO(fts[i]);
			//�����ֶνڵ�
			int iLenFld = (sfs == null) ? 0 : sfs.length;
			for (int j = 0; j < iLenFld; j++) {
				DefaultMutableTreeNode nodeFld = new DefaultMutableTreeNode(
						sfs[j]);
				nodeTable.add(nodeFld);
			}
		}
		//��ģ��
		DefaultTreeModel tm = new DefaultTreeModel(root);
		getTreeTableFld().setModel(tm);
		//���ظ�
		getTreeTableFld().setRootVisible(false);
		//���ڵ���ɫ��
		getTreeTableFld().setCellRenderer(new FieldCellRenderer());
		//չ��
		//getTreeTableFld().expandRow(0);
		//for (int i = iLen - 1; i >= 0; i--) {
		//	getTreeTableFld().expandRow(i);
		//}
		//�Ӽ���
		getTreeTableFld().addTreeSelectionListener(ivjEventHandler);
		getBnAdd().setEnabled(false);
	}

	/**
	 * ��ò�ѯ�ֶ�ϸ�����(��)
	 */
	public SetFldPanel getFldPanel() {
		if (m_pnSetFld == null) {
			m_pnSetFld = new SetFldPanel();
			m_pnSetFld.setColumnPanel(this);
			//���ñ�ʵ��
			//m_pnSetFld.getTablePn().setColumnPanel(this);
			//QueryFldTable table = m_pnSetFld.getTable();
			//table.setColumnPanel(this);
			//table.setHeader(new QueryFldTableHeader(table.getColumnModel()));
		}
		return m_pnSetFld;
	}

	/**
	 * ��ʼ������
	 */
	public void initUI() {
		//��ʼ����
		initTree();
		//�����ҽ���
		getPnCenter().add(getFldPanel(), BorderLayout.CENTER);
		//����ϸ��������Ϣ
		getFldPanel().setTabPn(getTabPn());
	}


		private SelectFldVO[] getFldVO(FromTableVO ft) {
			String		strTablePK = ft.getTablecode();
			//  ��������ֵ�ʵ��
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
				//ȡ���ֶ�
				SelectFldVO[] sfs = BIModelUtil.getFldsFromTable(ft.getTablecode(),
						ft.getTablealias(), tree);
				m_hmFlds.put(strTablePK, sfs);
				return sfs;
			} else {
				return (SelectFldVO[]) m_hmFlds.get(strTablePK);
			}
		}

	/**
	 * ���ѡ�еĲ�ѯ�ֶ�VO���� �������ڣ�(2003-4-3 9:09:08)
	 * 
	 * @return nc.vo.iuforeport.businessquery.SelectFldVO
	 */
	public SelectFldVO[] getGenFlds() {
		//���ѡ��·��
		TreePath[] selPaths = getTreeTableFld().getSelectionPaths();
		int iSelCount = (selPaths == null) ? 0 : selPaths.length;
		if (iSelCount == 0) {
			return null;
		}
		SelectFldVO[] sfs = new SelectFldVO[iSelCount];
		for (int i = 0; i < iSelCount; i++) {
			//����ֶ�VO
			DefaultMutableTreeNode selNodeFld = (DefaultMutableTreeNode) selPaths[i]
					.getLastPathComponent();
			if (!(selNodeFld.getUserObject() instanceof SelectFldVO)) {
				//ѡ���˱�
				return null;
			}
			sfs[i] = (SelectFldVO) selNodeFld.getUserObject();
			//��ñ�VO
			DefaultMutableTreeNode selNodeTable = (DefaultMutableTreeNode) selNodeFld
					.getParent();
			FromTableVO ft = (FromTableVO) selNodeTable.getUserObject();
			//������ʽ
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
	 * �Ϸ��Լ�� �������ڣ�(2003-4-4 13:57:49)
	 * 
	 * @return java.lang.String
	 */
	@SuppressWarnings("unchecked")
	public String checkMultiSelect(SelectFldVO[] sfs, Hashtable hashFldAlias) {

		int iLen = (sfs == null) ? 0 : sfs.length;
		for (int i = 0; i < iLen; i++) {
			String fldAlias = sfs[i].getFldalias();
			String fldName = sfs[i].getFldname();
			//�޸��ֶ��޷�
			if (hashFldAlias.containsKey(fldAlias))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000688")/* @res "�ֶα��������ظ���" */
						+ fldAlias + "(" + fldName + ")";
			if (hashFldAlias.containsValue(fldName))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000689")/* @res "�ֶ���ʾ�������ظ���" */
						+ fldName;
		}
		//ע�������ѭ��
		for (int i = 0; i < iLen; i++) {
			//���¹�ϣ��
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnButton ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnCenter ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� TreeTableFld ����ֵ��
	 * 
	 * @return UITree
	 */
	/* ���棺�˷������������ɡ� */
	private UITree getTreeTableFld() {
		if (ivjTreeTableFld == null) {
			try {
				ivjTreeTableFld = new TableFldTree();
				ivjTreeTableFld.setName("TreeTableFld");
				ivjTreeTableFld.setBounds(0, 0, 78, 72);
				// user code begin {1}
				//ivjTreeTableFld = new TableFldTree();
				//���ñ�ʵ��
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
	 * ���ڵ�ı�ѡȡ�¼���Ӧ
	 */
	public void treeTableFld_ValueChanged(TreeSelectionEvent treeSelectionEvent) {
		//��ť�����Կ���
		boolean bAddable = (getGenFlds() != null);
		getBnAdd().setEnabled(bAddable);
	}

	/**
	 * ˢ�²�ѯ��������
	 */
	public void refreshQbd() {
		//��ò�ѯ�ֶζ���
		SelectFldVO[] sfs = getFldPanel().getResultFromFld();
		//ˢ��
		getTabPn().getQueryBaseDef().setSelectFlds(sfs);
	}
}  