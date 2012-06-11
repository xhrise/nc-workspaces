package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 数据源选择对话框 创建日期：(2005-7-24 23:00:16)
 * 
 * @author：朱俊彬
 */
public class ChooseDsnDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UIButton ivjBnCancel = null;

	private UIButton ivjBnOK = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	private UIPanel ivjPnSouth = null;

	private JPanel ivjUIDialogContentPane = null;

	private UIComboBox ivjCbbDsn = null;

	private UILabel ivjLabelDsn = null;

	private UIPanel ivjPnCenter = null;

	class IvjEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == ChooseDsnDlg.this.getBnOK())
				connEtoC1(e);
			if (e.getSource() == ChooseDsnDlg.this.getBnCancel())
				connEtoC2(e);
		};
	};

	/**
	 * ChooseDsnDlg 构造子注解。
	 * 
	 * @param parent
	 *            Container
	 */
	public ChooseDsnDlg(Container parent) {
		super(parent);
		initialize();
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
		closeOK();
	}

	/**
	 * connEtoC1: (BnOK.action.actionPerformed(ActionEvent) -->
	 * ChooseDsnDlg.bnOK_ActionPerformed(LActionEvent;)V)
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
	 * ChooseDsnDlg.bnCancel_ActionPerformed(LActionEvent;)V)
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
	 * 返回 BnCancel 特性值。
	 * 
	 * @return UIButton
	 * @i18n miufo1003315=取消
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel.setText(StringResource.getStringResource("miufo1003315"));
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
	 * 返回 BnOK 特性值。
	 * 
	 * @return UIButton
	 * @i18n miufo1003314=确定
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnOK() {
		if (ivjBnOK == null) {
			try {
				ivjBnOK = new UIButton();
				ivjBnOK.setName("BnOK");
				ivjBnOK.setText(StringResource.getStringResource("miufo1003314"));
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
	 * 返回 CbbDsn 特性值。
	 * 
	 * @return UIComboBox
	 */
	/* 警告：此方法将重新生成。 */
	private UIComboBox getCbbDsn() {
		if (ivjCbbDsn == null) {
			try {
				ivjCbbDsn = new UIComboBox();
				ivjCbbDsn.setName("CbbDsn");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCbbDsn;
	}

	/**
	 * 返回 LabelDsn 特性值。
	 * 
	 * @return UILabel
	 * @i18n miuforepcalc0010=数据源
	 */
	/* 警告：此方法将重新生成。 */
	private UILabel getLabelDsn() {
		if (ivjLabelDsn == null) {
			try {
				ivjLabelDsn = new UILabel();
				ivjLabelDsn.setName("LabelDsn");
				ivjLabelDsn.setText(StringResource.getStringResource("miuforepcalc0010"));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelDsn;
	}

	/**
	 * 返回 UIPanel1 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnCenter() {
		if (ivjPnCenter == null) {
			try {
				ivjPnCenter = new UIPanel();
				ivjPnCenter.setName("PnCenter");
				ivjPnCenter.setLayout(new GridBagLayout());

				GridBagConstraints constraintsCbbDsn = new GridBagConstraints();
				constraintsCbbDsn.gridx = 2;
				constraintsCbbDsn.gridy = 1;
				constraintsCbbDsn.fill = GridBagConstraints.HORIZONTAL;
				constraintsCbbDsn.weightx = 1.0;
				constraintsCbbDsn.ipadx = 60;
				constraintsCbbDsn.ipady = 2;
				constraintsCbbDsn.insets = new Insets(89, 2, 89, 89);
				getPnCenter().add(getCbbDsn(), constraintsCbbDsn);

				GridBagConstraints constraintsLabelDsn = new GridBagConstraints();
				constraintsLabelDsn.gridx = 1;
				constraintsLabelDsn.gridy = 1;
				constraintsLabelDsn.ipadx = 8;
				constraintsLabelDsn.insets = new Insets(90, 88, 90, 1);
				getPnCenter().add(getLabelDsn(), constraintsLabelDsn);
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
				getUIDialogContentPane().add(getPnCenter(), "Center");
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
	}

	/**
	 * 初始化数据源 创建日期：(2005-7-24 23:06:40)
	 */
	public void initDsn(String[] dses) {
		getCbbDsn().removeAllItems();
		int iLen = (dses == null) ? 0 : dses.length;
		for (int i = 0; i < iLen; i++) {
			getCbbDsn().addItem(dses[i]);
		}
	}

	/**
	 * 初始化类。
	 * @i18n miufo00245=选择执行数据源
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("ChooseDsnDlg");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(400, 240);
			setTitle(StringResource.getStringResource("miufo00245"));
			setContentPane(getUIDialogContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * 获得选中数据源
	 */
	public String getSelDs() {
		String ds = (String) getCbbDsn().getSelectedItem();
		return ds;
	}
}  