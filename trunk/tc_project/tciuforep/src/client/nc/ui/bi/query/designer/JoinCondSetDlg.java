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
 * �����������ÿ� �������ڣ�(2005-7-12 11:18:32)
 * 
 * @author���쿡��
 */
@SuppressWarnings("serial")
public class JoinCondSetDlg extends UIDialog {

	private AbstractQueryDesignSetPanel m_parent = null;
	
	//�����ֵ�ʵ��
	private ObjectTree m_tree = null;

	//��ѯ��������
	private QueryBaseDef m_qbd = null;

	//���
	private FromTableVO m_leftTable = null;

	//�ұ�
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
	 * JoinCondSetDlg ������ע�⡣
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
	 * connEtoC2: (BnCancel.action. --> JoinCondSetDlg.bnCancel_ActionEvents()V)
	 */
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
				ivjBnCancel.setText(StringResource.getStringResource("miufopublic247"));//ȡ��
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
	 * ���� BnHand ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnHand() {
		if (ivjBnHand == null) {
			try {
				ivjBnHand = new UIButton();
				ivjBnHand.setName("BnHand");
				ivjBnHand.setText(StringResource.getStringResource("miufopublic280"));//�༭
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
				ivjBnOK.setText(StringResource.getStringResource("miufopublic246"));//ȷ��
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
	 * ���� ButtonGroup ����ֵ��
	 * 
	 * @return ButtonGroup
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� CbbLeftFld ����ֵ��
	 * 
	 * @return UIComboBox
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� CbbOperator ����ֵ��
	 * 
	 * @return UIComboBox
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� CbbRightFld ����ֵ��
	 * 
	 * @return UIComboBox
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� CbbType ����ֵ��
	 * 
	 * @return UIComboBox
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� LabelType ����ֵ��
	 * 
	 * @return UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private UILabel getLabelType() {
		if (ivjLabelType == null) {
			try {
				ivjLabelType = new UILabel();
				ivjLabelType.setName("LabelType");
				ivjLabelType.setText(StringResource.getStringResource("mbiquery0156"));//"��������"
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
			ivjPnNorthFlowLayout.setVgap(10);
			ivjPnNorthFlowLayout.setHgap(20);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnNorthFlowLayout;
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
	 * ���� RadioBnChoose ����ֵ��
	 * 
	 * @return UIRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIRadioButton getRadioBnChoose() {
		if (ivjRadioBnChoose == null) {
			try {
				ivjRadioBnChoose = new UIRadioButton();
				ivjRadioBnChoose.setName("RadioBnChoose");
				ivjRadioBnChoose.setText(StringResource.getStringResource("mbiquery0152"));//"����"
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
	 * ���� RadioBnHand ����ֵ��
	 * 
	 * @return UIRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIRadioButton getRadioBnHand() {
		if (ivjRadioBnHand == null) {
			try {
				ivjRadioBnHand = new UIRadioButton();
				ivjRadioBnHand.setName("RadioBnHand");
				ivjRadioBnHand.setText(StringResource.getStringResource("miufopublic368"));//"�߼�"
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
	 * ���� TFCond ����ֵ��
	 * 
	 * @return UITextField
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ʼ�������� �������ڣ�(2005-7-12 11:38:10)
	 */
	private void initCombo() {
		//��������
		getCbbType().addItem(StringResource.getStringResource("mbiquery0153"));//"������"
		getCbbType().addItem(StringResource.getStringResource("mbiquery0154"));//"������"
		getCbbType().addItem(StringResource.getStringResource("mbiquery0155"));//"������"
		//������
		getCbbOperator().addItem("=");
		getCbbOperator().addItem(">");
		getCbbOperator().addItem("<");
		getCbbOperator().addItem(">=");
		getCbbOperator().addItem("<=");
		getCbbOperator().addItem("<>");
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
		getRadioBnChoose().addActionListener(ivjEventHandler);
		getRadioBnHand().addActionListener(ivjEventHandler);
		getBnHand().addActionListener(ivjEventHandler);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
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
		//��ť��
		getButtonGroup().add(getRadioBnChoose());
		getButtonGroup().add(getRadioBnHand());
		//��ʼ������
		initCombo();
		// user code end
	}

	/**
	 * ���͵�ѡ��ťѡ��
	 */
	public void radioBnChoose_ActionPerformed(ActionEvent actionEvent) {
		setStatus(true);
	}

	/**
	 * �߼���ѡ��ťѡ��
	 */
	public void radioBnHand_ActionEvents() {
		getTFCond().grabFocus();
		setStatus(false);
	}

	/**
	 * ���������ֵ�ʵ�� �������ڣ�(2005-7-12 11:43:58)
	 */
	public void setDataDict(ObjectTree tree) {
		m_tree = tree;
	}

	/**
	 * ���ò�ѯ��������
	 */
	public void setQueryBaseDef(QueryBaseDef qbd) {
		m_qbd = qbd;
	}

	/**
	 * ���������ֵ�ʵ�� �������ڣ�(2005-7-12 11:43:58)
	 * 
	 * @param datadict
	 *            nc.vo.pub.ddc.datadict.Datadict
	 */
	public void setJoinTable(FromTableVO ftLeft, FromTableVO ftRight) {
		Datadict datadict = (Datadict) m_tree;
		m_leftTable = ftLeft;
		m_rightTable = ftRight;
		//�������ֶ�
		String leftTableCode = ftLeft.getTablecode();
		String leftTableAlias = ftLeft.getTablealias();
		SelectFldVO[] sfsLeft = BIModelUtil.getFldsFromTable(leftTableCode,
				leftTableAlias, datadict);
		//����������
		int iLenLeft = (sfsLeft == null) ? 0 : sfsLeft.length;
		for (int i = 0; i < iLenLeft; i++) {
			getCbbLeftFld().addItem(sfsLeft[i]);
		}
		//����ұ��ֶ�
		String rightTableCode = ftRight.getTablecode();
		String rightTableAlias = ftRight.getTablealias();
		SelectFldVO[] sfsRight = BIModelUtil.getFldsFromTable(rightTableCode,
				rightTableAlias, datadict);
		//����������
		int iLenRight = (sfsRight == null) ? 0 : sfsRight.length;
		for (int i = 0; i < iLenRight; i++) {
			getCbbRightFld().addItem(sfsRight[i]);
		}
	}

	/**
	 * ���ÿ���״̬
	 */
	private void setStatus(boolean bChoose) {
		getCbbLeftFld().setEnabled(bChoose);
		getCbbOperator().setEnabled(bChoose);
		getCbbRightFld().setEnabled(bChoose);
		getTFCond().setEnabled(!bChoose);
		getBnHand().setEnabled(!bChoose);
	}

	/**
	 * ������������
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
	 * ƥ���㷨1
	 */
	private boolean autoMatch1() {
		//��������ֵ�
		Datadict dd = (Datadict) m_tree;
		String dsNameForDDC = m_tree.getDatabaseParam().toString();
		//��ñ���Ϣ
		FromTableVO[] fts = new FromTableVO[] { m_leftTable, m_rightTable };
		JoinCondVO[] jcs = BIModelUtil.getJoinCondBetweenTables(fts, dd,
				dsNameForDDC);
		int iLen = (jcs == null) ? 0 : jcs.length;
		if (iLen != 0) {
			//���ѡ��
			int iLenLeft = getCbbLeftFld().getModel().getSize();
			for (int i = 0; i < iLenLeft; i++) {
				SelectFldVO sf = (SelectFldVO) getCbbLeftFld().getItemAt(i);
				//��ñ��ʽ
				String exp = sf.getFldalias();
				if (exp.equalsIgnoreCase(jcs[0].getLeftfld())) {
					getCbbLeftFld().setSelectedIndex(i);
					break;
				}
			}
			//�ұ�ѡ��
			int iLenRight = getCbbRightFld().getModel().getSize();
			for (int i = 0; i < iLenRight; i++) {
				SelectFldVO sf = (SelectFldVO) getCbbRightFld().getItemAt(i);
				//��ñ��ʽ
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
	 * ƥ���㷨2
	 */
	private boolean autoMatch2() {
		int iPkLen = 20;
		if (m_leftTable.getTablecode().equalsIgnoreCase("bd_corp")
				|| m_rightTable.getTablecode().equalsIgnoreCase("bd_corp")) {
			iPkLen = 4;
		}
		//��¼���������char(20)���͵��ֶ�
		int iLen = getCbbLeftFld().getModel().getSize();
		Hashtable<String, Integer> hashFld = new Hashtable<String, Integer>();
		for (int i = 0; i < iLen; i++) {
			SelectFldVO sf = (SelectFldVO) getCbbLeftFld().getItemAt(i);
			//�����������
			int iDataType = (sf.getColtype() == null) ? 0 : sf.getColtype()
					.intValue();
			int iLength = sf.getScale();
			if (iDataType == Types.CHAR && iLength == iPkLen) {
				//��ñ��ʽ
				String exp = sf.getFldalias();
				//��¼
				hashFld.put(exp, new Integer(i));
			}
		}
		//���ұ��в��ҵ�һ��ƥ����
		iLen = getCbbRightFld().getModel().getSize();
		if (hashFld.size() != 0) {
			for (int i = 0; i < iLen; i++) {
				SelectFldVO sf = (SelectFldVO) getCbbRightFld().getItemAt(i);
				//��ñ��ʽ
				String exp = sf.getFldalias();
				if (hashFld.containsKey(exp)) {
					//�����������
					int iDataType = (sf.getColtype() == null) ? 0 : sf
							.getColtype().intValue();
					if (iDataType == Types.CHAR) {
						//ѡ��
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
	 * ������ƥ��
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
	 * �ֹ�¼����������
	 */
	public void doHandJoin(QueryBaseDef qbd) {

		String oldExp = getTFCond().getText();
		//�����Ի���
		FldGenDlg dlg = new FldGenDlg(m_parent);
		dlg.setQueryBaseDef(qbd);
		Object[] info = new Object[] { null, null, oldExp, null };
		dlg.doSetInfo(info);
		dlg.setBnAddEnabled(false);
		dlg.setSomeInvisibled();
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//��������
			String exp = dlg.getExp();
			getTFCond().setText(exp);
		}
		return;
	}

	/**
	 * ������������
	 */
	public void setJoindCond(JoinCondVO jc) {
		if (jc == null) {
			getRadioBnChoose().setSelected(true);
			setStatus(true);
			//�Զ�ƥ��
			autoMatch();
			return;
		}
		//������������
		setJoinType(jc.getTypeflag());
		//����״̬
		boolean bChoose = (jc.getExpression0() == null);
		setStatus(bChoose);
		//����ϸ��
		if (bChoose) {
			getRadioBnChoose().setSelected(true);
			//�������ֶ�
			String leftFldCode = jc.getLeftfld();
			int iLen = getCbbLeftFld().getModel().getSize();
			for (int i = 0; i < iLen; i++) {
				SelectFldVO sf = (SelectFldVO) getCbbLeftFld().getItemAt(i);
				if (leftFldCode.equals(sf.getFldalias())) {
					getCbbLeftFld().setSelectedIndex(i);
					break;
				}
			}
			//������
			getCbbOperator().setSelectedItem(jc.getOperator());
			//�������ֶ�
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
			//��������
			getTFCond().setText(jc.getExpression0());
		}
	}

	/**
	 * �����������
	 */
	public JoinCondVO getJoindCond() {
		JoinCondVO jc = new JoinCondVO();
		//�����������
		jc.setTypeflag(getJoinType());
		//���״̬
		boolean bChoose = getRadioBnChoose().isSelected();
		if (bChoose) {
			//������ֶ�
			SelectFldVO sfLeft = (SelectFldVO) getCbbLeftFld()
					.getSelectedItem();
			jc.setLeftfld(sfLeft.getFldalias());
			//��ò�����
			jc.setOperator(getCbbOperator().getSelectedItem().toString());
			//������ֶ�
			SelectFldVO sfRight = (SelectFldVO) getCbbRightFld()
					.getSelectedItem();
			jc.setRightfld(sfRight.getFldalias());
		} else {
			//�����������
			jc.setExpression0(getHandJoin());
		}
		return jc;
	}

	/**
	 * �����������
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
	 * ����ֹ���������
	 */
	private String getHandJoin() {
		String handJoin = getTFCond().getText().trim();
		if (handJoin.equals("")) {
			handJoin = "1=1";
		}
		return handJoin;
	}
}
 