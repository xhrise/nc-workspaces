package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Types;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITextField;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.JoinCondVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.core.ObjectTree;
import nc.vo.pub.ddc.datadict.Datadict;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 连接条件设置框 创建日期：(2005-7-12 11:18:32)
 * 
 * @author：朱俊彬
 */
@SuppressWarnings("serial")
public class JoinCondSetDlg extends UIDialog {

	private AbstractQueryDesignSetPanel m_parent = null;
	
	//数据字典实例
	private ObjectTree m_tree = null;

	//查询基本定义
	private QueryBaseDef m_qbd = null;

	//左表
	private FromTableVO m_leftTable = null;

	//右表
	private FromTableVO m_rightTable = null;

	private UIButton ivjBnCancel = null;

	private UIButton ivjBnOK = null;

	private UIPanel ivjPnNorth = null;

	private UIPanel ivjPnSouth = null;

	private JPanel ivjUIDialogContentPane = null;

	private UIComboBox ivjCbbType = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	private UILabel ivjLabelType = null;

	private UIPanel ivjPnCenter = null;

	private ButtonGroup ivjButtonGroup = null;

	private UIRadioButton ivjRadioBnChoose = null;

	private UIRadioButton ivjRadioBnHand = null;

	private UIComboBox ivjCbbLeftFld = null;

	private UIComboBox ivjCbbOperator = null;

	private UIComboBox ivjCbbRightFld = null;

	private UIButton ivjBnHand = null;

	private UITextField ivjTFCond = null;

	class IvjEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == JoinCondSetDlg.this.getBnOK())
				connEtoC1(e);
			if (e.getSource() == JoinCondSetDlg.this.getBnCancel())
				connEtoC2();
			if (e.getSource() == JoinCondSetDlg.this.getRadioBnChoose())
				connEtoC3(e);
			if (e.getSource() == JoinCondSetDlg.this.getRadioBnHand())
				connEtoC4();
			if (e.getSource() == JoinCondSetDlg.this.getBnHand())
				connEtoC5(e);
		};
	};

	/**
	 * JoinCondSetDlg 构造子注解。
	 * 
	 * @param parent
	 *            Container
	 */
	public JoinCondSetDlg(Container parent) {
		super(parent);
		m_parent = (AbstractQueryDesignSetPanel) parent;
		initialize();
	}

	/**
	 * Cancel
	 */
	public void bnCancel_ActionEvents() {
		closeCancel();
	}

	/**
	 * Hand
	 */
	public void bnHand_ActionPerformed(ActionEvent actionEvent) {
		doHandJoin(m_qbd);
	}

	/**
	 * OK
	 */
	public void bnOK_ActionPerformed(ActionEvent actionEvent) {
		closeOK();
	}

	/**
	 * connEtoC1: (BnOK.action.actionPerformed(ActionEvent) -->
	 * JoinCondSetDlg.bnOK_ActionPerformed(LActionEvent;)V)
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
	 * connEtoC2: (BnCancel.action. --> JoinCondSetDlg.bnCancel_ActionEvents()V)
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC2() {
		try {
			// user code begin {1}
			// user code end
			this.bnCancel_ActionEvents();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3: (RadioBnChoose.action.actionPerformed(ActionEvent) -->
	 * JoinCondSetDlg.radioBnChoose_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.radioBnChoose_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4: (RadioBnHand.action. -->
	 * JoinCondSetDlg.radioBnHand_ActionEvents()V)
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC4() {
		try {
			// user code begin {1}
			// user code end
			this.radioBnHand_ActionEvents();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC5: (BnHand.action.actionPerformed(ActionEvent) -->
	 * JoinCondSetDlg.bnHand_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC5(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnHand_ActionPerformed(arg1);
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
				ivjBnCancel.setText(StringResource.getStringResource("miufopublic247"));//取消
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
	 * 返回 BnHand 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnHand() {
		if (ivjBnHand == null) {
			try {
				ivjBnHand = new UIButton();
				ivjBnHand.setName("BnHand");
				ivjBnHand.setText(StringResource.getStringResource("miufopublic280"));//编辑
				ivjBnHand.setBounds(388, 178, 68, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnHand;
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
				ivjBnOK.setText(StringResource.getStringResource("miufopublic246"));//确定
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
	 * 返回 ButtonGroup 特性值。
	 * 
	 * @return ButtonGroup
	 */
	/* 警告：此方法将重新生成。 */
	private ButtonGroup getButtonGroup() {
		if (ivjButtonGroup == null) {
			try {
				ivjButtonGroup = new ButtonGroup();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjButtonGroup;
	}

	/**
	 * 返回 CbbLeftFld 特性值。
	 * 
	 * @return UIComboBox
	 */
	/* 警告：此方法将重新生成。 */
	private UIComboBox getCbbLeftFld() {
		if (ivjCbbLeftFld == null) {
			try {
				ivjCbbLeftFld = new UIComboBox();
				ivjCbbLeftFld.setName("CbbLeftFld");
				ivjCbbLeftFld.setPreferredSize(new Dimension(120, 22));
				ivjCbbLeftFld.setBounds(140, 78, 120, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCbbLeftFld;
	}

	/**
	 * 返回 CbbOperator 特性值。
	 * 
	 * @return UIComboBox
	 */
	/* 警告：此方法将重新生成。 */
	private UIComboBox getCbbOperator() {
		if (ivjCbbOperator == null) {
			try {
				ivjCbbOperator = new UIComboBox();
				ivjCbbOperator.setName("CbbOperator");
				ivjCbbOperator.setPreferredSize(new Dimension(80, 22));
				ivjCbbOperator.setBounds(268, 78, 60, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCbbOperator;
	}

	/**
	 * 返回 CbbRightFld 特性值。
	 * 
	 * @return UIComboBox
	 */
	/* 警告：此方法将重新生成。 */
	private UIComboBox getCbbRightFld() {
		if (ivjCbbRightFld == null) {
			try {
				ivjCbbRightFld = new UIComboBox();
				ivjCbbRightFld.setName("CbbRightFld");
				ivjCbbRightFld.setPreferredSize(new Dimension(120, 22));
				ivjCbbRightFld.setBounds(336, 78, 120, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCbbRightFld;
	}

	/**
	 * 返回 CbbType 特性值。
	 * 
	 * @return UIComboBox
	 */
	/* 警告：此方法将重新生成。 */
	private UIComboBox getCbbType() {
		if (ivjCbbType == null) {
			try {
				ivjCbbType = new UIComboBox();
				ivjCbbType.setName("CbbType");
				ivjCbbType.setPreferredSize(new Dimension(120, 22));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCbbType;
	}

	/**
	 * 返回 LabelType 特性值。
	 * 
	 * @return UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private UILabel getLabelType() {
		if (ivjLabelType == null) {
			try {
				ivjLabelType = new UILabel();
				ivjLabelType.setName("LabelType");
				ivjLabelType.setText(StringResource.getStringResource("mbiquery0156"));//"连接类型"
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelType;
	}

	/**
	 * 返回 PnCenter 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnCenter() {
		if (ivjPnCenter == null) {
			try {
				ivjPnCenter = new UIPanel();
				ivjPnCenter.setName("PnCenter");
				ivjPnCenter.setLayout(null);
				getPnCenter().add(getRadioBnChoose(),
						getRadioBnChoose().getName());
				getPnCenter().add(getRadioBnHand(), getRadioBnHand().getName());
				getPnCenter().add(getCbbLeftFld(), getCbbLeftFld().getName());
				getPnCenter().add(getCbbOperator(), getCbbOperator().getName());
				getPnCenter().add(getCbbRightFld(), getCbbRightFld().getName());
				getPnCenter().add(getTFCond(), getTFCond().getName());
				getPnCenter().add(getBnHand(), getBnHand().getName());
				// user code begin {1}
				getPnCenter().setBorder(BorderFactory.createEtchedBorder());
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
				ivjPnNorth.setPreferredSize(new Dimension(10, 42));
				ivjPnNorth.setLayout(getPnNorthFlowLayout());
				getPnNorth().add(getLabelType(), getLabelType().getName());
				getPnNorth().add(getCbbType(), getCbbType().getName());
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
	 * 返回 PnNorthFlowLayout 特性值。
	 * 
	 * @return FlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private FlowLayout getPnNorthFlowLayout() {
		FlowLayout ivjPnNorthFlowLayout = null;
		try {
			/* 创建部件 */
			ivjPnNorthFlowLayout = new FlowLayout();
			ivjPnNorthFlowLayout.setVgap(10);
			ivjPnNorthFlowLayout.setHgap(20);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnNorthFlowLayout;
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
	 * 返回 RadioBnChoose 特性值。
	 * 
	 * @return UIRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIRadioButton getRadioBnChoose() {
		if (ivjRadioBnChoose == null) {
			try {
				ivjRadioBnChoose = new UIRadioButton();
				ivjRadioBnChoose.setName("RadioBnChoose");
				ivjRadioBnChoose.setText(StringResource.getStringResource("mbiquery0152"));//"典型"
				ivjRadioBnChoose.setBounds(22, 78, 100, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRadioBnChoose;
	}

	/**
	 * 返回 RadioBnHand 特性值。
	 * 
	 * @return UIRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIRadioButton getRadioBnHand() {
		if (ivjRadioBnHand == null) {
			try {
				ivjRadioBnHand = new UIRadioButton();
				ivjRadioBnHand.setName("RadioBnHand");
				ivjRadioBnHand.setText(StringResource.getStringResource("miufopublic368"));//"高级"
				ivjRadioBnHand.setBounds(22, 178, 100, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRadioBnHand;
	}

	/**
	 * 返回 TFCond 特性值。
	 * 
	 * @return UITextField
	 */
	/* 警告：此方法将重新生成。 */
	private UITextField getTFCond() {
		if (ivjTFCond == null) {
			try {
				ivjTFCond = new UITextField();
				ivjTFCond.setName("TFCond");
				ivjTFCond.setBounds(140, 178, 240, 22);
				ivjTFCond.setMaxLength(1000);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTFCond;
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
				getUIDialogContentPane().add(getPnNorth(), "North");
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
	 * 初始化下拉框 创建日期：(2005-7-12 11:38:10)
	 */
	private void initCombo() {
		//连接类型
		getCbbType().addItem(StringResource.getStringResource("mbiquery0153"));//"内连接"
		getCbbType().addItem(StringResource.getStringResource("mbiquery0154"));//"左连接"
		getCbbType().addItem(StringResource.getStringResource("mbiquery0155"));//"右连接"
		//操作符
		getCbbOperator().addItem("=");
		getCbbOperator().addItem(">");
		getCbbOperator().addItem("<");
		getCbbOperator().addItem(">=");
		getCbbOperator().addItem("<=");
		getCbbOperator().addItem("<>");
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
		getRadioBnChoose().addActionListener(ivjEventHandler);
		getRadioBnHand().addActionListener(ivjEventHandler);
		getBnHand().addActionListener(ivjEventHandler);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("JoinCondSetDlg");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(520, 380);
			setResizable(false);
			setContentPane(getUIDialogContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		//按钮组
		getButtonGroup().add(getRadioBnChoose());
		getButtonGroup().add(getRadioBnHand());
		//初始化界面
		initCombo();
		// user code end
	}

	/**
	 * 典型单选按钮选中
	 */
	public void radioBnChoose_ActionPerformed(ActionEvent actionEvent) {
		setStatus(true);
	}

	/**
	 * 高级单选按钮选中
	 */
	public void radioBnHand_ActionEvents() {
		getTFCond().grabFocus();
		setStatus(false);
	}

	/**
	 * 设置数据字典实例 创建日期：(2005-7-12 11:43:58)
	 */
	public void setDataDict(ObjectTree tree) {
		m_tree = tree;
	}

	/**
	 * 设置查询基本定义
	 */
	public void setQueryBaseDef(QueryBaseDef qbd) {
		m_qbd = qbd;
	}

	/**
	 * 设置数据字典实例 创建日期：(2005-7-12 11:43:58)
	 * 
	 * @param datadict
	 *            nc.vo.pub.ddc.datadict.Datadict
	 */
	public void setJoinTable(FromTableVO ftLeft, FromTableVO ftRight) {
		Datadict datadict = (Datadict) m_tree;
		m_leftTable = ftLeft;
		m_rightTable = ftRight;
		//获得左表字段
		String leftTableCode = ftLeft.getTablecode();
		String leftTableAlias = ftLeft.getTablealias();
		SelectFldVO[] sfsLeft = BIModelUtil.getFldsFromTable(leftTableCode,
				leftTableAlias, datadict);
		//加入下拉框
		int iLenLeft = (sfsLeft == null) ? 0 : sfsLeft.length;
		for (int i = 0; i < iLenLeft; i++) {
			getCbbLeftFld().addItem(sfsLeft[i]);
		}
		//获得右表字段
		String rightTableCode = ftRight.getTablecode();
		String rightTableAlias = ftRight.getTablealias();
		SelectFldVO[] sfsRight = BIModelUtil.getFldsFromTable(rightTableCode,
				rightTableAlias, datadict);
		//加入下拉框
		int iLenRight = (sfsRight == null) ? 0 : sfsRight.length;
		for (int i = 0; i < iLenRight; i++) {
			getCbbRightFld().addItem(sfsRight[i]);
		}
	}

	/**
	 * 设置可用状态
	 */
	private void setStatus(boolean bChoose) {
		getCbbLeftFld().setEnabled(bChoose);
		getCbbOperator().setEnabled(bChoose);
		getCbbRightFld().setEnabled(bChoose);
		getTFCond().setEnabled(!bChoose);
		getBnHand().setEnabled(!bChoose);
	}

	/**
	 * 设置连接类型
	 */
	private void setJoinType(String joinType) {
		if (joinType.equals("L")) {
			getCbbType().setSelectedIndex(1);
		} else if (joinType.equals("R")) {
			getCbbType().setSelectedIndex(2);
		} else {
			getCbbType().setSelectedIndex(0);
		}
	}

	/**
	 * 匹配算法1
	 */
	private boolean autoMatch1() {
		//获得数据字典
		Datadict dd = (Datadict) m_tree;
		String dsNameForDDC = m_tree.getDatabaseParam().toString();
		//获得表信息
		FromTableVO[] fts = new FromTableVO[] { m_leftTable, m_rightTable };
		JoinCondVO[] jcs = BIModelUtil.getJoinCondBetweenTables(fts, dd,
				dsNameForDDC);
		int iLen = (jcs == null) ? 0 : jcs.length;
		if (iLen != 0) {
			//左表选中
			int iLenLeft = getCbbLeftFld().getModel().getSize();
			for (int i = 0; i < iLenLeft; i++) {
				SelectFldVO sf = (SelectFldVO) getCbbLeftFld().getItemAt(i);
				//获得表达式
				String exp = sf.getFldalias();
				if (exp.equalsIgnoreCase(jcs[0].getLeftfld())) {
					getCbbLeftFld().setSelectedIndex(i);
					break;
				}
			}
			//右表选中
			int iLenRight = getCbbRightFld().getModel().getSize();
			for (int i = 0; i < iLenRight; i++) {
				SelectFldVO sf = (SelectFldVO) getCbbRightFld().getItemAt(i);
				//获得表达式
				String exp = sf.getFldalias();
				if (exp.equalsIgnoreCase(jcs[0].getRightfld().toString())) {
					getCbbRightFld().setSelectedIndex(i);
					break;
				}
			}
			return true;
		}
		return true;
	}

	/**
	 * 匹配算法2
	 */
	private boolean autoMatch2() {
		int iPkLen = 20;
		if (m_leftTable.getTablecode().equalsIgnoreCase("bd_corp")
				|| m_rightTable.getTablecode().equalsIgnoreCase("bd_corp")) {
			iPkLen = 4;
		}
		//记录左表中所有char(20)类型的字段
		int iLen = getCbbLeftFld().getModel().getSize();
		Hashtable<String, Integer> hashFld = new Hashtable<String, Integer>();
		for (int i = 0; i < iLen; i++) {
			SelectFldVO sf = (SelectFldVO) getCbbLeftFld().getItemAt(i);
			//获得数据类型
			int iDataType = (sf.getColtype() == null) ? 0 : sf.getColtype()
					.intValue();
			int iLength = sf.getScale();
			if (iDataType == Types.CHAR && iLength == iPkLen) {
				//获得表达式
				String exp = sf.getFldalias();
				//记录
				hashFld.put(exp, new Integer(i));
			}
		}
		//到右表中查找第一个匹配项
		iLen = getCbbRightFld().getModel().getSize();
		if (hashFld.size() != 0) {
			for (int i = 0; i < iLen; i++) {
				SelectFldVO sf = (SelectFldVO) getCbbRightFld().getItemAt(i);
				//获得表达式
				String exp = sf.getFldalias();
				if (hashFld.containsKey(exp)) {
					//获得数据类型
					int iDataType = (sf.getColtype() == null) ? 0 : sf
							.getColtype().intValue();
					if (iDataType == Types.CHAR) {
						//选中
						int iLeftSelIndex = ((Integer) hashFld.get(exp))
								.intValue();
						getCbbLeftFld().setSelectedIndex(iLeftSelIndex);
						getCbbRightFld().setSelectedIndex(i);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 简单智能匹配
	 */
	private boolean autoMatch() {
		try {
			if (autoMatch1()) {
				return true;
			}
			if (autoMatch2()) {
				return true;
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		return false;
	}

	/**
	 * 手工录入连接条件
	 */
	public void doHandJoin(QueryBaseDef qbd) {

		String oldExp = getTFCond().getText();
		//弹出对话框
		FldGenDlg dlg = new FldGenDlg(m_parent);
		dlg.setQueryBaseDef(qbd);
		Object[] info = new Object[] { null, null, oldExp, null };
		dlg.doSetInfo(info);
		dlg.setBnAddEnabled(false);
		dlg.setSomeInvisibled();
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//连接条件
			String exp = dlg.getExp();
			getTFCond().setText(exp);
		}
		return;
	}

	/**
	 * 设置连接条件
	 */
	public void setJoindCond(JoinCondVO jc) {
		if (jc == null) {
			getRadioBnChoose().setSelected(true);
			setStatus(true);
			//自动匹配
			autoMatch();
			return;
		}
		//设置连接类型
		setJoinType(jc.getTypeflag());
		//设置状态
		boolean bChoose = (jc.getExpression0() == null);
		setStatus(bChoose);
		//设置细节
		if (bChoose) {
			getRadioBnChoose().setSelected(true);
			//左连接字段
			String leftFldCode = jc.getLeftfld();
			int iLen = getCbbLeftFld().getModel().getSize();
			for (int i = 0; i < iLen; i++) {
				SelectFldVO sf = (SelectFldVO) getCbbLeftFld().getItemAt(i);
				if (leftFldCode.equals(sf.getFldalias())) {
					getCbbLeftFld().setSelectedIndex(i);
					break;
				}
			}
			//操作符
			getCbbOperator().setSelectedItem(jc.getOperator());
			//右连接字段
			String rightFldCode = jc.getRightfld().toString();
			iLen = getCbbRightFld().getModel().getSize();
			for (int i = 0; i < iLen; i++) {
				SelectFldVO sf = (SelectFldVO) getCbbRightFld().getItemAt(i);
				if (rightFldCode.equals(sf.getFldalias())) {
					getCbbRightFld().setSelectedIndex(i);
					break;
				}
			}
		} else {
			getRadioBnHand().setSelected(true);
			//连接条件
			getTFCond().setText(jc.getExpression0());
		}
	}

	/**
	 * 获得连接条件
	 */
	public JoinCondVO getJoindCond() {
		JoinCondVO jc = new JoinCondVO();
		//获得连接类型
		jc.setTypeflag(getJoinType());
		//获得状态
		boolean bChoose = getRadioBnChoose().isSelected();
		if (bChoose) {
			//获得左字段
			SelectFldVO sfLeft = (SelectFldVO) getCbbLeftFld()
					.getSelectedItem();
			jc.setLeftfld(sfLeft.getFldalias());
			//获得操作符
			jc.setOperator(getCbbOperator().getSelectedItem().toString());
			//获得右字段
			SelectFldVO sfRight = (SelectFldVO) getCbbRightFld()
					.getSelectedItem();
			jc.setRightfld(sfRight.getFldalias());
		} else {
			//获得连接条件
			jc.setExpression0(getHandJoin());
		}
		return jc;
	}

	/**
	 * 获得连接类型
	 */
	private String getJoinType() {
		int iIndex = getCbbType().getSelectedIndex();
		String joinType = "I";
		if (iIndex == 1) {
			joinType = "L";
		} else if (iIndex == 2) {
			joinType = "R";
		}
		return joinType;
	}

	/**
	 * 获得手工连接条件
	 */
	private String getHandJoin() {
		String handJoin = getTFCond().getText().trim();
		if (handJoin.equals("")) {
			handJoin = "1=1";
		}
		return handJoin;
	}
}
 