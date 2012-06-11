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
 * 查询设计面板 创建日期：(2005-5-16 18:35:14)
 * 
 * @author：朱俊彬
 */
public class QueryDesignPanel extends UIPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//查询设计页签实例
	private AbstractQueryDesignTabPanel m_tabPn = null;

	//定义数据源
	private String m_defDsName = null;

	//数据字典实例
	private Datadict m_datadict = null;

	//查询设计对话框实例
	private QueryDesignDlg m_designDlg = null;

	//查询设计Applet实例
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
	 * QueryDesignPanel 构造子注解。
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
	/* 警告：此方法将重新生成。 */
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
	/* 警告：此方法将重新生成。 */
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
	/* 警告：此方法将重新生成。 */
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
	/* 警告：此方法将重新生成。 */
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
				ivjBnCancel.setText(StringResource.getStringResource("miufo1000274"));//取消
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
	 * 返回 BnLast 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnLast() {
		if (ivjBnLast == null) {
			try {
				ivjBnLast = new UIButton();
				ivjBnLast.setName("BnLast");
				ivjBnLast.setText(StringResource.getStringResource("miufopublic260"));//上一步
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
	 * 返回 BnNext 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnNext() {
		if (ivjBnNext == null) {
			try {
				ivjBnNext = new UIButton();
				ivjBnNext.setName("BnNext");
				ivjBnNext.setText(StringResource.getStringResource("miufopublic261"));//下一步
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
				// user code begin {1}
				ivjBnOK.setText(StringResource.getStringResource("miufopublic108"));//保存
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
	 * 获得查询设计对话框实例 创建日期：(2005-5-16 19:07:52)
	 * 
	 * @return nc.ui.bi.query.designer.QueryDesignDlg
	 */
	public QueryDesignDlg getDesignDlg() {
		return m_designDlg;
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
			ivjPnSouthFlowLayout.setHgap(10);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnSouthFlowLayout;
	}

	/**
	 * 获得查询设计设置页签 创建日期：(2005-5-16 18:38:58)
	 */
	public AbstractQueryDesignTabPanel getTabPn() {
		return m_tabPn;
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
		getPnSouth().addMouseListener(this);
		// user code end
		getBnLast().addActionListener(ivjEventHandler);
		getBnNext().addActionListener(ivjEventHandler);
		getBnOK().addActionListener(ivjEventHandler);
		getBnCancel().addActionListener(ivjEventHandler);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 设置查询设计对话框实例 创建日期：(2005-5-16 19:07:52)
	 * 
	 * @param newDesignDlg
	 *            nc.ui.bi.query.designer.QueryDesignDlg
	 */
	public void setDesignDlg(QueryDesignDlg newDesignDlg) {
		m_designDlg = newDesignDlg;
	}

	/**
	 * 设置查询设计设置页签 创建日期：(2005-5-16 18:38:58)
	 */
	public void setTabPn(AbstractQueryDesignTabPanel tabPn) {
		m_tabPn = tabPn;
		tabPn.setDesignPanel(this);
		add(tabPn, BorderLayout.CENTER);
	}

	/**
	 * 设置查询主键 创建日期：(2005-5-16 18:38:58)
	 */
	public void setQueryModelDef(BIQueryModelDef qmd) {
		getTabPn().setQueryModelDef(qmd);
	}

	/**
	 * @return 返回 datadict。
	 */
	public Datadict getDatadict() {
		return m_datadict;
	}

	/**
	 * @param datadict
	 *            要设置的 datadict。
	 */
	public void setDatadict(Datadict datadict) {
		m_datadict = datadict;
		getTabPn().setDatadict(datadict);
	}

	/**
	 * 保存结果
	 */
	public void saveResult() {
		getTabPn().saveResult();
	}

	/**
	 * 合法性检查
	 */
	public String doCheck() {
		return getTabPn().doCheck();
	}

	/**
	 * 获得查询设计Applet实例 创建日期：(2005-5-16 19:07:52)
	 * 
	 * @return nc.ui.bi.query.designer.QueryDesignDlg
	 */
	public QueryDesignApplet getDesignApplet() {
		return m_designApplet;
	}

	/**
	 * 设置查询设计Applet实例 创建日期：(2005-5-16 19:07:52)
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
	 * @i18n uiufofurl530087=查询设计
	 * @i18n miufo00430=保存完成！是否重新创建物化表
	 * @i18n miufo00431=保存完成！是否创建物化表
	 * @i18n miufo00432=物化表创建成功
	 * @i18n miufo00433=没有创建成功
	 */
	public void doOK() {
		//合法性检查
		String strErr = getTabPn().doCheck();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this, StringResource.getStringResource("uiufofurl530087"), strErr);
			return;
		}
		//保存结果
		saveResult();
		//关闭
		if (getDesignDlg() != null) {
			getDesignDlg().closeOK();
		} else{
			//zyjun + 询问是否创建物化表或者重新创建物化表
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
				//调用创建物化表
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
	 * 设置按钮面板可见性
	 */
	public void setBnPanelVisible(boolean bVisible) {
		getPnSouth().setVisible(bVisible);
	}

	/**
	 * 设置取消按钮可见性
	 */
	public void setBnCancelVisible(boolean bVisible) {
		getBnCancel().setVisible(bVisible);
	}

	/**
	 * 设置完成按钮可用性
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
	 * @return 返回 String。
	 */
	public String getDefDsName() {
		return m_defDsName;
	}

	/**
	 * 设置定义数据源
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
			//显示信息
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