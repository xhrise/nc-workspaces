/*
 * �������� 2005-5-24
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
 * �ֹ�SQL���ý���
 */
public class SetHandSqlPanel extends AbstractQueryDesignSetPanel implements
		KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0051";//"��ѯ�ֶ�";

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
	 * SetTablePanel ������ע�⡣
	 */
	public SetHandSqlPanel() {
		super();
		initialize();
	}

	/**
	 * �պ������ֵ���
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	 * ���ô��������� �������ڣ�(2003-11-13 9:05:55)
	 * 
	 * @return java.lang.String
	 */
	private String fillParam() {
		String strParam = "";
		//������ڱ༭�Ľڵ�
		//ObjectNode node = getTabPn().getQueryDefDlg().getEditNode();
		BIQueryModelDef qmd = getTabPn().getQueryModelDef();
		ParamVO[] params = qmd.getBaseModel().getParamVOs();
		//����
		ParamDefDlg dlg = new ParamDefDlg(this, getTabPn().getDefDsName());
		dlg.setParamVOs(params, qmd.getDsName());
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//�洢
			params = dlg.getParamVOs();
			qmd.getBaseModel().setParamVOs(params);
			//node.saveObject(qmd);
			//node.setObject(qmd);
			//���ѡ�в���
			ParamVO selParam = dlg.getSelParamVO();
			if (selParam != null) {
				strParam = selParam.getParamCode();
				//���滻�Ͳ�����ʾ
				if (!strParam.startsWith(QueryConst.REPLACE_PARAM)
						|| !strParam.endsWith(QueryConst.REPLACE_PARAM))
					MessageDialog.showHintDlg(this, NCLangRes.getInstance()
							.getStrByID("10241201", "UPP10241201-000099")/*
																		  * @res
																		  * "��ѯ����"
																		  */, NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000899")/*
												  * @res "�ֹ�SQL��ֻʶ���滻�Ͳ���"
												  */);
			}
		}
		return strParam;
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
	 * ���� CheckBox ����ֵ��
	 * 
	 * @return UICheckBox
	 */
	/* ���棺�˷������������ɡ� */
	private UICheckBox getCheckBox() {
		if (ivjCheckBox == null) {
			try {
				ivjCheckBox = new UICheckBox();
				ivjCheckBox.setName("CheckBox");
				ivjCheckBox.setPreferredSize(new Dimension(128, 22));
				ivjCheckBox.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000900")/*
														  * @res "�����÷������"
														  */);
				// user code begin {1}
				ivjCheckBox.setToolTipText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000901")/*
														  * @res
														  * "���ѡȡ������������صĲ�ѯ���ɺ�SQL�����ܽ��޷�����ʹ��"
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
	 * ����ֹ�¼��SQL �������ڣ�(2003-5-29 11:27:25)
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
	 * ���� SclPnSql ����ֵ��
	 * 
	 * @return UIScrollPane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� TASql ����ֵ��
	 * 
	 * @return UITextArea
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� TreeDD ����ֵ��
	 * 
	 * @return nc.ui.pub.component.ObjectTreeView
	 */
	/* ���棺�˷������������ɡ� */
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
		getTASql().addKeyListener(ivjEventHandler);
		getTreeDD().addMouseListener(ivjEventHandler);
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
	 * ��ʼ�������ֵ��� �������ڣ�(2003-4-2 16:40:20)
	 */
	public void initTree() {
		//ɾ��ԭ�����ܵļ���
		getTreeDD().removeKeyListener(this);
		//��������ֵ�ʵ��
		Datadict dd = getTabPn().getDatadict();
		//������
		getTreeDD().setObjectTree(new ObjectTree[] { dd });
		//��ʾ��
		getTreeDD().expandRow(0);
		//��Ӽ���
		getTreeDD().addKeyListener(this);
	}

	/**
	 * �Ƿ�����SQL���� �������ڣ�(2004-3-19 10:24:37)
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
				//��������ֵ�ʵ��
				Datadict dd = getTabPn().getDatadict();
				//����
				FindDlg dlg = new FindDlg(this,
						NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000121")/* @res "����" */, true);
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
	 * ˫��ѡ�� �������ڣ�(2003-4-2 20:38:58)
	 * 
	 * @param str
	 *            java.lang.String
	 * @param iFunc
	 *            int
	 */
	private void onSelect(String str, int iFunc) {
		if (str != null) {
			//ѡ��
			int pos = getTASql().getSelectionStart();
			getTASql().insert(str, pos);
			getTASql().requestFocus();
			getTASql().setSelectionStart(pos + str.length() - iFunc);
			getTASql().setSelectionEnd(pos + str.length() - iFunc);
		}
	}

	/**
	 * �����ֹ�SQL �������ڣ�(2003-4-3 16:16:01)
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
	 * �����ͷ��¼���Ӧ
	 * 
	 * @param keyEvent
	 *            KeyEvent
	 */
	public void tASql_KeyReleased(KeyEvent keyEvent) {
		int c = keyEvent.getKeyCode();
		if (c == KeyEvent.VK_F10 && keyEvent.isControlDown())
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-000099")/* @res "��ѯ����" */,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000902")/* @res "��ʲô�أ�" */);
		else if (c == KeyEvent.VK_P && keyEvent.isControlDown())
			testParse(getTASql().getText());
		else if (c == KeyEvent.VK_S && keyEvent.isControlDown())
			onSelect("select ", 0);
		else if (c == KeyEvent.VK_F && keyEvent.isControlDown())
			onSelect(" from ", 0);
		else if (c == KeyEvent.VK_L && keyEvent.isControlDown())
			//������ʾ
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
	 * ���Խ��� �������ڣ�(2003-9-9 20:03:52)
	 * 
	 * @param sql
	 *            String
	 */
	public void testParse(String sql) {
		try {
			Select select = SqlParseMain.doParse(sql);
			SqlParseMain.transform(select);
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-000099")/* @res "��ѯ����" */,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000903")/* @res "����ͨ��" */);
		} catch (Exception e) {
			AppDebug.debug(e);
			MessageDialog.showErrorDlg(this,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "��ѯ����" */, NCLangRes
							.getInstance().getStrByID("10241201",
									"UPP10241201-000904")/* @res "�����﷨����" */);
		}
		return;
	}

	/**
	 * ������¼���Ӧ
	 * 
	 * @param mouseEvent
	 *            MouseEvent
	 */
	public void treeDD_MouseClicked(MouseEvent mouseEvent) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getTreeDD()
				.getSelectedTreeNode();
		if (node != null && mouseEvent.getClickCount() == 2) {
			if (node.getUserObject() instanceof FieldDef) {
				//����ֶζ���
				FieldDef fd = (FieldDef) node.getUserObject();
				//��ñ���
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
						.getParent().getParent();
				DatadictNode ddn = (DatadictNode) parent.getUserObject();
				TableDef td = (TableDef) ddn.getObject();
				//��ñ���
				String strTable = td.getID();
				//UFO����
				if (td instanceof TableDefWithAlias)
					strTable = ((TableDefWithAlias) td).getRealName();
				//����ֶ�ȫ��
				String strFld = strTable + "." + fd.getID();
				//¼��
				onSelect(strFld, 0);
			} else if (node.getUserObject() instanceof DatadictNode) {
				DatadictNode ddn = (DatadictNode) node.getUserObject();
				if (ddn.getObject() instanceof TableDef) {
					//��ñ���
					TableDef td = (TableDef) ddn.getObject();
					//��ñ���
					String strTable = td.getID();
					//UFO����
					if (td instanceof TableDefWithAlias)
						strTable = ((TableDefWithAlias) td).getRealName();
					//¼��
					onSelect(strTable, 0);
				}
			}
		}
	}

	/**
	 * ���û��������� �������ڣ�(2003-11-13 9:05:55)
	 * 
	 * @return java.lang.String
	 */
	private String fillEnvParam() {
		String strParam = null;
		/*
		 * //���ȫ������������ȱʡ+�Զ���ӿ�ʵ�֣� IEnvParam iEnvParam =
		 * getTabPn().getQueryDefDlg() .getIEnvParam(); String[][] envParams =
		 * ModelUtil.getEnvParams(iEnvParam); //���� ChooseEnvParamDlg dlg = new
		 * ChooseEnvParamDlg(this); dlg.setEnvParams(envParams);
		 * dlg.showModal(); dlg.destroy(); if (dlg.getResult() ==
		 * UIDialog.ID_OK) { //���ѡ�в����� strParam = dlg.getEnvParams(); }
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
	/**
	 * @i18n miufo00227=������
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		QueryBaseVO qb = qmd.getQueryBaseVO();
		//����
		qb.setHandSql(getHandSql());
		qb.setTempletId(isSqlTranslate() ? null : StringResource.getStringResource("miufo00227"));
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	/**
	 * @i18n miufo00227=������
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
	 * �Ϸ��Լ�� �������ڣ�(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {
		return null;
	}
}
  