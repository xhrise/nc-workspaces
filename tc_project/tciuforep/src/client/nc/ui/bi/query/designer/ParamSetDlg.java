package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.querymodel.UIUtil;
import nc.vo.pub.querymodel.IEnvParam;
import nc.vo.pub.querymodel.ParamVO;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * 参数设置对话框 创建日期：(2003-8-8 14:41:00)
 * 
 * @author：朱俊彬
 */
public class ParamSetDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//定义数据源
	private String m_defDsName = null;

	private UIPanel ivjPnSouth = null;

	private JPanel ivjUIDialogContentPane = null;

	private UIButton ivjBnCancel = null;

	private UIButton ivjBnOK = null;

	private ParamSetPanel ivjParamSetPanel = null;

	private UIButton ivjBnResemble = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == ParamSetDlg.this.getBnCancel())
				connEtoC3(e);
			if (e.getSource() == ParamSetDlg.this.getBnOK())
				connEtoC4(e);
			if (e.getSource() == ParamSetDlg.this.getBnResemble())
				connEtoC1(e);
		};
	};

	/**
	 * ParamDefDlg 构造子注解。
	 * @deprecated
	 */
	public ParamSetDlg() {
		super();
		initialize();
	}

	/**
	 * ParamDefDlg 构造子注解。
	 * 
	 * @param parent
	 *            Container
	 */
	public ParamSetDlg(Container parent, String defDsName) {
		super(parent);
		m_defDsName = defDsName;
		initialize();
	}

	/**
	 * 取消
	 */
	private void bnCancel_ActionPerformed(ActionEvent actionEvent) {
		closeCancel();
	}

	/**
	 * 确定
	 */
	private void bnOK_ActionPerformed(ActionEvent actionEvent) {
		getParamSetPanel().stopTableEditing();
		String strErr = getParamSetPanel().check();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */, strErr);
			return;
		}
		closeOK();
	}

	/**
	 * 多页签同名参数快捷录入
	 */
	private void bnResemble_ActionPerformed(ActionEvent actionEvent) {
		getParamSetPanel().resemble();
		//关闭
		bnOK_ActionPerformed(null);
	}

	/**
	 * connEtoC1: (BnResemble.action.actionPerformed(ActionEvent) -->
	 * ParamSetDlg.bnResemble_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnResemble_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3: (BnCancel.action.actionPerformed(ActionEvent) -->
	 * ParamDefDlg.bnCancel_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(ActionEvent arg1) {
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
	 * connEtoC4: (BnOK.action.actionPerformed(ActionEvent) -->
	 * ParamDefDlg.bnOK_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC4(ActionEvent arg1) {
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
	 * 返回 BnResemble 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnResemble() {
		if (ivjBnResemble == null) {
			try {
				ivjBnResemble = new UIButton();
				ivjBnResemble.setName("BnResemble");
				ivjBnResemble
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-001168")/* @res "快捷" */);
				// user code begin {1}
				ivjBnResemble.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnResemble, ivjBnResemble.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnResemble;
	}

	/**
	 * 获得多个参数VO数组 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public ParamVO[][] getParamsArray() {
		return getParamSetPanel().getParamsArray();
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
				getPnSouth().add(getBnResemble(), getBnResemble().getName());
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
	 * 返回 TabbedPn 特性值。
	 * 
	 * @return UITabbedPane
	 */
	/* 警告：此方法将重新生成。 */
	private ParamSetPanel getParamSetPanel() {
		if (ivjParamSetPanel == null) {
			try {
				ivjParamSetPanel = new ParamSetPanel();
				ivjParamSetPanel.setName("ParamSetPanel");
				ivjParamSetPanel.setDefDsName(m_defDsName);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjParamSetPanel;
	}

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * 
	 * @return JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new UIPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new BorderLayout());
				getUIDialogContentPane().add(getPnSouth(), "South");
				getUIDialogContentPane().add(getParamSetPanel(), "Center");
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
		getBnResemble().addActionListener(ivjEventHandler);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("ParamSetDlg");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(600, 400);
			setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000528")/* @res "参数设置" */);
			setContentPane(getUIDialogContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * 设置多个参数VO数组 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public void setParamsArray(ParamVO[][] paramsArray, String[] ids) {
		getParamSetPanel().setParamsArray(paramsArray, ids);
	}

	/**
	 * 设置自定义环境参数接口 创建日期：(2004-12-2 17:09:38)
	 * 
	 * @param newM_iEnvParam
	 *            nc.vo.pub.querymodel.IEnvParam
	 */
	public void setIEnvParam(IEnvParam newIEnvParam) {
		getParamSetPanel().setIEnvParam(newIEnvParam);
	}
} 