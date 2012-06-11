package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import nc.ui.bi.query.manager.QueryModelBO_Client;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.vo.bi.query.manager.BIEnvInfo;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.BIQueryUtil;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.pub.ddc.datadict.Datadict;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * ��ѯ������ �������ڣ�(2005-5-16 18:35:14)
 * 
 * @author���쿡��
 */
public class QueryDesignPanel extends UIPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//��ѯ���ҳǩʵ��
	private AbstractQueryDesignTabPanel m_tabPn = null;

	//��������Դ
	private String m_defDsName = null;

	//�����ֵ�ʵ��
	private Datadict m_datadict = null;

	//��ѯ��ƶԻ���ʵ��
	private QueryDesignDlg m_designDlg = null;

	//��ѯ���Appletʵ��
	private QueryDesignApplet m_designApplet = null;

	private UIPanel ivjPnSouth = null;

	private UIButton ivjBnCancel = null;

	private UIButton ivjBnLast = null;

	private UIButton ivjBnNext = null;

	private UIButton ivjBnOK = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == QueryDesignPanel.this.getBnLast())
				connEtoC1(e);
			if (e.getSource() == QueryDesignPanel.this.getBnNext())
				connEtoC2(e);
			if (e.getSource() == QueryDesignPanel.this.getBnOK())
				connEtoC3(e);
			if (e.getSource() == QueryDesignPanel.this.getBnCancel())
				connEtoC4(e);
		};
	};

	/**
	 * QueryDesignPanel ������ע�⡣
	 */
	public QueryDesignPanel() {
		super();
		initialize();
	}

	/**
	 * Cancel
	 */
	public void bnCancel_ActionPerformed(ActionEvent actionEvent) {
		doCancel();
	}

	/**
	 * Last
	 */
	public void bnLast_ActionPerformed(ActionEvent actionEvent) {
		doLast();
	}

	/**
	 * Next
	 */
	public void bnNext_ActionPerformed(ActionEvent actionEvent) {
		doNext();
	}

	/**
	 * OK
	 */
	public void bnOK_ActionPerformed(ActionEvent actionEvent) {
		doOK();
	}

	/**
	 * connEtoC1: (BnLast.action.actionPerformed(ActionEvent) -->
	 * QueryDesignPanel.bnLast_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC1(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnLast_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC2: (BnNext.action.actionPerformed(ActionEvent) -->
	 * QueryDesignPanel.bnNext_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC2(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnNext_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3: (BnOK.action.actionPerformed(ActionEvent) -->
	 * QueryDesignPanel.bnOK_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC3(ActionEvent arg1) {
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
	 * connEtoC4: (BnCancel.action.actionPerformed(ActionEvent) -->
	 * QueryDesignPanel.bnCancel_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC4(ActionEvent arg1) {
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
				ivjBnCancel.setText(StringResource.getStringResource("miufo1000274"));//ȡ��
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
	 * ���� BnLast ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnLast() {
		if (ivjBnLast == null) {
			try {
				ivjBnLast = new UIButton();
				ivjBnLast.setName("BnLast");
				ivjBnLast.setText(StringResource.getStringResource("miufopublic260"));//��һ��
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnLast;
	}

	/**
	 * ���� BnNext ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnNext() {
		if (ivjBnNext == null) {
			try {
				ivjBnNext = new UIButton();
				ivjBnNext.setName("BnNext");
				ivjBnNext.setText(StringResource.getStringResource("miufopublic261"));//��һ��
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnNext;
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
				// user code begin {1}
				ivjBnOK.setText(StringResource.getStringResource("miufopublic108"));//����
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
	 * ��ò�ѯ��ƶԻ���ʵ�� �������ڣ�(2005-5-16 19:07:52)
	 * 
	 * @return nc.ui.bi.query.designer.QueryDesignDlg
	 */
	public QueryDesignDlg getDesignDlg() {
		return m_designDlg;
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
				getPnSouth().add(getBnLast(), getBnLast().getName());
				getPnSouth().add(getBnNext(), getBnNext().getName());
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
			ivjPnSouthFlowLayout.setHgap(10);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnSouthFlowLayout;
	}

	/**
	 * ��ò�ѯ�������ҳǩ �������ڣ�(2005-5-16 18:38:58)
	 */
	public AbstractQueryDesignTabPanel getTabPn() {
		return m_tabPn;
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
		getPnSouth().addMouseListener(this);
		// user code end
		getBnLast().addActionListener(ivjEventHandler);
		getBnNext().addActionListener(ivjEventHandler);
		getBnOK().addActionListener(ivjEventHandler);
		getBnCancel().addActionListener(ivjEventHandler);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("QueryDesignPanel");
			setLayout(new BorderLayout());
			setSize(680, 400);
			add(getPnSouth(), "South");
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		getBnOK().setEnabled(false);
		getBnLast().setEnabled(false);
		// user code end
	}

	/**
	 * ���ò�ѯ��ƶԻ���ʵ�� �������ڣ�(2005-5-16 19:07:52)
	 * 
	 * @param newDesignDlg
	 *            nc.ui.bi.query.designer.QueryDesignDlg
	 */
	public void setDesignDlg(QueryDesignDlg newDesignDlg) {
		m_designDlg = newDesignDlg;
	}

	/**
	 * ���ò�ѯ�������ҳǩ �������ڣ�(2005-5-16 18:38:58)
	 */
	public void setTabPn(AbstractQueryDesignTabPanel tabPn) {
		m_tabPn = tabPn;
		tabPn.setDesignPanel(this);
		add(tabPn, BorderLayout.CENTER);
	}

	/**
	 * ���ò�ѯ���� �������ڣ�(2005-5-16 18:38:58)
	 */
	public void setQueryModelDef(BIQueryModelDef qmd) {
		getTabPn().setQueryModelDef(qmd);
	}

	/**
	 * @return ���� datadict��
	 */
	public Datadict getDatadict() {
		return m_datadict;
	}

	/**
	 * @param datadict
	 *            Ҫ���õ� datadict��
	 */
	public void setDatadict(Datadict datadict) {
		m_datadict = datadict;
		getTabPn().setDatadict(datadict);
	}

	/**
	 * ������
	 */
	public void saveResult() {
		getTabPn().saveResult();
	}

	/**
	 * �Ϸ��Լ��
	 */
	public String doCheck() {
		return getTabPn().doCheck();
	}

	/**
	 * ��ò�ѯ���Appletʵ�� �������ڣ�(2005-5-16 19:07:52)
	 * 
	 * @return nc.ui.bi.query.designer.QueryDesignDlg
	 */
	public QueryDesignApplet getDesignApplet() {
		return m_designApplet;
	}

	/**
	 * ���ò�ѯ���Appletʵ�� �������ڣ�(2005-5-16 19:07:52)
	 * 
	 * @param newDesignDlg
	 *            nc.ui.bi.query.designer.QueryDesignDlg
	 */
	public void setDesignApplet(QueryDesignApplet newDesignApplet) {
		m_designApplet = newDesignApplet;
	}

	/**
	 * Cancel
	 */
	public void doCancel() {
		if (getDesignDlg() != null)
			getDesignDlg().closeCancel();
	}

	/**
	 * Last
	 */
	public void doLast() {
		int iSelIndex = getTabPn().getSelectedIndex();
		if (iSelIndex > 0)
			getTabPn().setSelectedIndex(iSelIndex - 1);
	}

	/**
	 * Next
	 */
	public void doNext() {
		int iSelIndex = getTabPn().getSelectedIndex();
		if (iSelIndex < getTabPn().getTabCount() - 1)
			getTabPn().setSelectedIndex(iSelIndex + 1);
	}

	/**
	 * OK
	 * @i18n uiufofurl530087=��ѯ���
	 * @i18n miufo00430=������ɣ��Ƿ����´����ﻯ��
	 * @i18n miufo00431=������ɣ��Ƿ񴴽��ﻯ��
	 * @i18n miufo00432=�ﻯ�����ɹ�
	 * @i18n miufo00433=û�д����ɹ�
	 */
	public void doOK() {
		//�Ϸ��Լ��
		String strErr = getTabPn().doCheck();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this, StringResource.getStringResource("uiufofurl530087"), strErr);
			return;
		}
		//������
		saveResult();
		//�ر�
		if (getDesignDlg() != null) {
			getDesignDlg().closeOK();
		} else{
			//zyjun + ѯ���Ƿ񴴽��ﻯ��������´����ﻯ��
			boolean		bMaterFactExist = false;
			BIQueryModelDef qmd = getTabPn().getQueryModelDef();
			try{
				String			strTableName = QueryModelSrv.getFactTableName(qmd.getID());
				bMaterFactExist = QueryModelBO_Client.isTempTableExisted(strTableName, getDefDsName());
			}catch(Exception e){
				AppDebug.debug(e);
			}
			String		strMsg = bMaterFactExist?StringResource.getStringResource("miufo00430"):StringResource.getStringResource("miufo00431");
			if( MessageDialog.showOkCancelDlg(this, StringResource.getStringResource("uiufofurl530087"), strMsg)==MessageDialog.ID_OK){
				//���ô����ﻯ��
				try{
					BIQueryUtil.createMaterTable(qmd, new BIEnvInfo(getDefDsName()));
					MessageDialog.showHintDlg(this, StringResource.getStringResource("uiufofurl530087"), StringResource.getStringResource("miufo00432"));
				}catch(Exception e){
					AppDebug.debug(e);
					MessageDialog.showHintDlg(this, StringResource.getStringResource("uiufofurl530087"), StringResource.getStringResource("miufo00433"));
				}
				
			} 
		}
	}

	/**
	 * ���ð�ť���ɼ���
	 */
	public void setBnPanelVisible(boolean bVisible) {
		getPnSouth().setVisible(bVisible);
	}

	/**
	 * ����ȡ����ť�ɼ���
	 */
	public void setBnCancelVisible(boolean bVisible) {
		getBnCancel().setVisible(bVisible);
	}

	/**
	 * ������ɰ�ť������
	 */
	public void setBnOkEnable(boolean bEnable) {
		getBnOK().setEnabled(bEnable);
	}
	
	public void setBnNextEnable(boolean bEnable){
		getBnNext().setEnabled(bEnable);
	}
	public void setBnLastEnable(boolean bEnable){
		getBnLast().setEnabled(bEnable);
	}

	/**
	 * @return ���� String��
	 */
	public String getDefDsName() {
		return m_defDsName;
	}

	/**
	 * ���ö�������Դ
	 */
	public void setDefDsName(String defDsName) {
		m_defDsName = defDsName;
		getTabPn().setDefDsName(defDsName);
	}

	/*
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == getPnSouth()) {
			//��ʾ��Ϣ
			String info = getTabPn().getShowInfo();
			MessageDialog.showHintDlg(this, "UFBI", info);
			return;
		}
	}

	/*
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/*
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
	}

	/*
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
	}
}  