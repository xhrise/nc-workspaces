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
 * ��ʱ��ѡȡ�Ի��� �������ڣ�(2003-4-2 16:30:15)
 * 
 * @author���쿡��
 */
public class TempTableInfoDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//�����
	private AbstractQueryDesignSetPanel m_parent = null;

	//�����ж��Ƿ�ѡ���ظ��Ĺ�ϣ��
	private Hashtable m_hashTableId = null;

	//��ѯ������
	private ObjectTreeView m_tree = null;

	//��ѯ����������
	private QueryModelTree m_qmt = null;

	//��������Դ
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
	 * TableInfoDlg ������ע�⡣
	 * @deprecated
	 */
	public TempTableInfoDlg() {
		super();
		initialize();
	}

	/**
	 * TableInfoDlg ������ע�⡣
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
							"UPP10241201-000099")/* @res "��ѯ����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000931")/* @res "ѡ�еĽڵ㲻�Ǳ�" */);
			return;
		}
		if (m_hashTableId.containsKey(qmd.getID())) {
			MessageDialog.showWarningDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "��ѯ����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000936")/* @res "ѡ�нڵ������" */);
			return;
		}
		try {
			String idSelf = m_parent.getTabPn().getQueryBaseDef().getID();
			if (qmd.getID().equals(idSelf)) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "��ѯ����" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000937")/* @res "��������Լ�" */);
				return;
			}
		} catch (Exception e) {
		}
		//���ӱ���
		if (m_parent instanceof SetTablePanel) {
			((SetTablePanel) m_parent).doAdd(qmd.getBaseModel());
		}
		//���¹�ϣ��
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
							"UPP10241201-000099")/* @res "��ѯ����" */, strErr);
			return;
		}
		closeOK();
	}

	/**
	 * �Ϸ��Լ�� �������ڣ�(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	private String check() {
		BIQueryModelDef qmd = getSelTableDef();
		if (qmd == null)
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000931")/* @res "ѡ�еĽڵ㲻�Ǳ�" */;
		if (m_hashTableId.containsKey(qmd.getID()))
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000936")/* @res "ѡ�нڵ������" */;
		try {
			String idSelf = m_parent.getTabPn().getQueryBaseDef().getID();
			if (qmd.getID().equals(idSelf))
				return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000937")/* @res "��������Լ�" */;
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
				ivjBnAdd.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000330")/* @res "����" */);
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
				ivjBnCancel
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000000")/* @res "ȡ��" */);
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
				ivjBnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000001")/* @res "ȷ��" */);
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
			ivjPnSouthFlowLayout.setHgap(20);
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
	 * ���ѡ�еĲ�ѯģ�Ͷ��� �������ڣ�(2003-4-2 16:49:17)
	 * 
	 * @return nc.bs.com.datadict.TableDef
	 */
	public BIQueryModelDef getSelTableDef() {
		BIQueryModelDef qmd = null;
		//���ѡ�нڵ�
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
	 * ���� UIDialogContentPane ����ֵ��
	 * 
	 * @return javax.swing.JPanel
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
		getBnCancel().addActionListener(ivjEventHandler);
		getBnOK().addActionListener(ivjEventHandler);
		getBnAdd().addActionListener(ivjEventHandler);
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
			setSize(320, 400);
			setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000934")/* @res "ѡȡ��" */);
			setContentPane(getUIDialogContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * ��ʼ����ѯģ���� �������ڣ�(2003-4-2 16:40:20)
	 */
	public void initTree(String dsName) {
		//��������Դ
		//	dsName = ModelUtil.getDsnameForDef();
		setDsNameForDef(dsName);

		//��ò�ѯģ��ʵ��
		if (dsName == null)
			m_qmt = BIQueryModelTree.getDefaultInstance();
		else
			m_qmt = BIQueryModelTree.getInstance(dsName);
		//������
		m_tree = new ObjectTreeView();
		m_tree.setObjectTree(new ObjectTree[] { m_qmt });
		//��ʾ��
		m_tree.expandRow(0);
		getSclPnTree().setViewportView(m_tree);
		//��Ӽ���
		m_tree.addKeyListener(this);
	}

	public void keyReleased(KeyEvent e) {
		if (e.getSource() == m_tree)
			if ((e.getKeyCode() == KeyEvent.VK_F && e.isControlDown())) {
				//�������ҿ�
				FindDlg dlg = new FindDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000121")/* @res "����" */, true);
				dlg.setObjectTrees(new ObjectTree[] { m_qmt });
				if (dlg.showModal() == UIDialog.ID_OK) {
					ObjectNode objnode = dlg.getSearch();
					m_tree.selectNode(objnode);
				}
			}
	}

	/**
	 * �������Ӱ�ť������ �������ڣ�(2003-4-2 17:12:21)
	 * 
	 * @param hashTableId
	 *            java.util.Hashtable
	 */
	public void setBnAddEnabled(boolean bEnabled) {
		getBnAdd().setEnabled(bEnabled);
	}

	/**
	 * ���ñ�ID��ϣ�� �������ڣ�(2003-4-2 17:12:21)
	 * 
	 * @param hashTableId
	 *            java.util.Hashtable
	 */
	public void setHashTableId(Hashtable hashTableId) {
		m_hashTableId = hashTableId;
	}

	/**
	 * ��ö�������Դ
	 */
	public String getDsNameForDef() {
		return m_dsNameForDef;
	}

	/**
	 * ���ö�������Դ
	 */
	public void setDsNameForDef(String dsNameForDef) {
		m_dsNameForDef = dsNameForDef;
	}
} 