package nc.ui.sm.funcreg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.bs.logging.Logger;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.bs.uap.sfapp.util.SFAppServiceUtil;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.pf.GroupLayout;
import nc.ui.pub.pf.LayoutStyle;
import nc.ui.sm.nodepower.OrgnizeType_client;
import nc.vo.pub.hotkey.HotkeyRegisterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.sm.funcreg.FuncModuleJudger;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.sm.nodepower.OrgnizeTypeVO;

/**
 * ����ע��
 * @author ��ɭ 2001-3-28
 * @modifier zjb
 * @modifier leijun 2003-7-22 �޸�A1��A2��B1�Ⱦ��庬����ʾ
 * @modifier leijun 2003-11-7 ���Ӱ�ť�Ŀ�ݼ�ע�Ṧ�� ��ʱû�����棿����
 * @modifier leijun 2003-11-10 ������һ����Ĭ�Ͽ�ݼ�����ť��Ϊ���ݿ������а�ť�ָ�Ĭ�Ͽ�ݼ�
 * @modifier leijun 2004-07-09 ������ϰ�ťע�Ṧ�� 
 * @modifier leijun 2007-10-12 ʹ��GroupLayout���²���
 * @modifier leijun 2007-12-6 ��ִ�й��ܵ��������ԣ��Ƿ����ð�ťȨ�޿���
 */
public class FunRegisterUI extends ToftPanel implements TreeSelectionListener, FunRegisterConst,
		ItemListener {
	public final static String[] FUNC_NODE = new String[] { "��ִ�й��ܽڵ�", "�鹦�ܽڵ�", "������Ȩ�ް�ť", "Ĭ����Ȩ�ް�ť",
			"����", "��ִ�й���֡", "������Web�ڵ�" };

	public final static String[] PROP_TYPE = new String[] { "������ȫ���ƣ���˾�鿴", "���Ŷ������", "���Ų��ֿ��ƣ���˾����",
			"���Ų��ֿ��ƣ���˾����", "��˾˽�п���", "��˾˽�п��ƵĿ����֯����" };

	//zsb+:������֯������Ͽ�ͱ�ǩ
	private UIComboBox comboOrgnizeType = null;

	private ButtonGroup ivjBGForbid = null;

	private ButtonGroup ivjBGShowParam = null;

	private UIComboBox ivjComboBoxProperty = null;

	private UIComboBox ivjComboBoxPropType = null;

	private UILabel ivjLabelClassName = null;

	private UILabel ivjLabelCode = null;

	private UILabel ivjLabelDesc = null;

	private UILabel ivjLabelForbid = null;

	private UILabel ivjLabelHelpName = null;

	private UILabel ivjLabelResid = null;

	private UILabel ivjLabelName = null;

	private UILabel ivjLabelProperty = null;

	private UILabel ivjLabelPropType = null;

	private UILabel ivjLabelShowParam = null;

	//private ButtonObject m_boUnionFunc = new ButtonObject("��ϰ�ť", "��ϰ�ť", 0);

	private UIPanel ivjPnDetail = null;

	private UIPanel ivjPnWest = null;

	private UIPanel ivjPnWestEast = null;

	private UIPanel ivjPnWestNorth = null;

	private UIPanel ivjPnWestSouth = null;

	//public final static String[] PROP_TYPE =
	//new String[] { "A1", "A2", "B1", "B2", "C", "D" }; //�ڿ���ֱ��Ӧ0��1��2��3��4��5���ֶ�group_flag��

	private UIPanel ivjPnWestWest = null;

	private UIRadioButton ivjRadioForbidNo = null;

	private UIRadioButton ivjRadioForbidYes = null;

	private UIRadioButton ivjRadioShowParamNo = null;

	private UIRadioButton ivjRadioShowParamYes = null;

	private UIScrollPane ivjSclPnTextArea = null;

	private UIScrollPane ivjSclPnTree = null;

	private UISplitPane ivjSplitPn = null;

	private UITextArea ivjTextAreaDesc = null;

	private UITextField ivjTextFieldClass = null;

	private UITextField ivjTextFieldFunCode = null;

	private UITextField ivjTextFieldFunName = null;

	private UITextField ivjTextFieldHelp = null;

	private UITextField ivjTextFieldResid = null;

	private UILabel lbOrgnizeType = null;

	private boolean m_adding = true; //���ӱ�־

	private boolean m_bEdit = false; //�༭״̬��־

	//��ť
	private ButtonObject m_boAdd = new ButtonObject("����", "����", 0, "����");

	private ButtonObject m_boCancel = new ButtonObject("ȡ��", "ȡ��", 0, "ȡ��");

	private ButtonObject m_boCheck = new ButtonObject("����У��", "����У��", 0, "����У��");

	private ButtonObject m_boDefaultHotkey = new ButtonObject("�ָ�Ĭ�Ͽ�ݼ�", "�ָ����а�ť��ݼ�Ĭ��ֵ", 0, "�ָ�Ĭ�Ͽ�ݼ�");

	private ButtonObject m_boDel = new ButtonObject("ɾ��", "ɾ��", 0, "ɾ��");

	private ButtonObject m_boHotkey = new ButtonObject("��ݼ�", "��ݼ�", 0, "��ݼ�");

	//lj+ 2003-11-10 �����Ӳ˵���ʽ
	private ButtonObject m_boHotkeyReg = new ButtonObject("��ݼ�ע��", "Ϊ���ܽڵ��µİ�ťע���ݼ�", 0, "��ݼ�ע��");

	private boolean m_bOldForbidFlag = false; //�޸�ǰ����ԭ�������ñ�־

	private ButtonObject m_boRefresh = new ButtonObject("ˢ��", "ˢ��", 0, "ˢ��");

	private ButtonObject m_boSave = new ButtonObject("����", "����", 0, "����");

	private ButtonObject m_boUpdate = new ButtonObject("�޸�", "�޸�", 0, "�޸�");

	private UICheckBox m_ckbNeedButtonLog = null;

	private UITree m_funTree = null; //����Ȩ��ע����

	//lj+ ������
	Hashtable m_hashBtn4Hotkey = new Hashtable();

	private boolean m_haveSaveFlag = true; //�ѱ����־

	//״̬��־
	private boolean m_loading = true; //���ر�־

	//��ť��
	private ButtonObject[] m_MainButtonGroup = { m_boAdd, m_boUpdate, m_boDel, m_boSave, m_boCancel,
			m_boRefresh, m_boCheck, m_boHotkey };

	private FunRegNode m_selectedNode = null; //ѡ�нڵ�

	private TreePath m_selPath = null; //��ǰѡ��·��

	private UILabel ivjLabelButtonLog;

	private UICheckBox m_ckbButtonPower;

	private UILabel ivjLabelButtonPower;

	/**
	 * FunRegisterUI ������ע�⡣
	 */
	public FunRegisterUI() {
		super();
		initialize();
	}

	/**
	 * ��������������ѡ��ı��¼���Ӧ
	 */
	public void comboBoxProperty_ItemStateChanged(ItemEvent itemEvent) {
		if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
			String strSelItem = getComboBoxProperty().getSelectedItem().toString(); //ѡ����
			int iSelectedProp = getPropIndex(strSelItem);

			//�����鹦�ܽڵ㣬��Ӧ�ؼ����ı��򲻿��ã��������
			if (iSelectedProp == INEXECUTABLE_FUNC_NODE) {
				getTextFieldClass().setText("");
				getTextFieldClass().setEnabled(false);
			} else
				getTextFieldClass().setEnabled(true);
			//���ڿ�ִ�й��ܽڵ㣬����������������ã����򲻿���
			if (isExecutableNode(iSelectedProp)) {
				getComboBoxPropType().setEnabled(true);
				getOrgnizeTypeCombo().setEnabled(true);
				getOrgnizeTypeCombo().setSelectedIndex(0);
			} else {
				getComboBoxPropType().setSelectedItem(" ");
				getOrgnizeTypeCombo().setEnabled(false);
				getOrgnizeTypeCombo().setSelectedItem(null);
			}
			//���ڲ�������Ӧ�ؼ�����ǩ����
			if (iSelectedProp == PARAMETER) {
				getLabelName().setText("�� �� �� ��");
				getLabelClassName().setText("����ֵ");
			} else if (iSelectedProp == POWERFUL_BUTTON || iSelectedProp == POWERLESS_BUTTON) {
				getLabelName().setText("�� ť �� ��");
				getLabelClassName().setText("�� ť �� ��");
			} else
				getLabelClassName().setText("��Ӧ�ļ�����ؼ���");
		}
		return;
	}

	/**
	 * ��ʾ�ڵ��Ӧ��ϸ
	 * �������ڣ�(2001-3-29 20:11:17)
	 */
	public void displayNodeInfo() {
		FuncRegisterVO fr = getSelectedNode().getNodeVo();
		//���ܱ���
		String funCode = fr.getFunCode();
		if (funCode != null) {
			getTextFieldFunCode().setMaxLength(fr.getFunCode().trim().length());
			getTextFieldFunCode().setText(funCode);
		}
		//��������
		String funName = fr.getFunName();
		if (funName != null)
			getTextFieldFunName().setText(funName);
		//��������
		int iProp = fr.getFunProperty().intValue();
		if (isExecutableNode(iProp) || iProp == INEXECUTABLE_FUNC_NODE)
			//���Ʊ� update
			getComboBoxPropType().setSelectedIndex(
					fr.getGroupFlag() == null ? -1 : fr.getGroupFlag().intValue());
		//getComboBoxPropType().setSelectedIndex(fr.getGroupFlag().intValue());
		else
			getComboBoxPropType().setSelectedItem(" ");
		//��������
		int index = fr.getFunProperty().intValue();
		if (index == PARAMETER) {
			getLabelName().setText("�� �� �� ��");
			getLabelClassName().setText("����ֵ");
		} else if (index == POWERFUL_BUTTON || index == POWERLESS_BUTTON) {
			getLabelName().setText("�� ť �� ��");
			getLabelClassName().setText("�� ť �� ��");
		} else {
			getLabelName().setText("�� �� �� ��");
			getLabelClassName().setText("��Ӧ�ļ�����ؼ���");
		}
		//��ø��ڵ㹦������
		int iParentPropIndex = -1;
		TreeNode node = getSelectedNode().getParent();
		if (node != null)
			iParentPropIndex = ((FunRegNode) node).getNodeVo().getFunProperty().intValue();
		//�������Ӱ�ť����״̬��zjb��
		if (isExecutableNode(index) || index == INEXECUTABLE_FUNC_NODE) { //���ܽڵ�
			setButtonEnabled(m_boAdd, true);
		} else { //
			//zsb+��ֻ�зǲ�����һ����ť�������Ӷ�����ť
			boolean bAddEnable = (index != PARAMETER) && (isExecutableNode(iParentPropIndex));
			setButtonEnabled(m_boAdd, bAddEnable);
		}
		//���õ�ǰ�ڵ��ѡ�Ĺ�������
		getComboBoxProperty().removeItemListener(this);
		if (isExecutableNode(iParentPropIndex) || iParentPropIndex == POWERFUL_BUTTON
				|| iParentPropIndex == POWERLESS_BUTTON) {
			//zsb+������һ����ť�������Ӷ�����ť
			//��ִ�й��ܽڵ�
			getComboBoxProperty().removeAllItems();
			getComboBoxProperty().addItem(FUNC_NODE[POWERFUL_BUTTON]);
			getComboBoxProperty().addItem(FUNC_NODE[POWERLESS_BUTTON]);
			if (getSelectedNode().getChildCount() == 0) {
				getComboBoxProperty().addItem(FUNC_NODE[PARAMETER]);
			}

		} else if (iParentPropIndex == INEXECUTABLE_FUNC_NODE) { //�鹦�ܽڵ�
			getComboBoxProperty().removeAllItems();
			getComboBoxProperty().addItem(FUNC_NODE[EXECUTABLE_FUNC_NODE]);
			getComboBoxProperty().addItem(FUNC_NODE[INEXECUTABLE_FUNC_NODE]);
			getComboBoxProperty().addItem(FUNC_NODE[EXECUTABLE_FUNC_FRAME]);
			getComboBoxProperty().addItem(FUNC_NODE[LFW_FUNC_NODE]);

		} else
			System.out.println("������");

		int curtype = ((FunRegNode) getSelectedNode()).getNodeVo().getFunProperty().intValue();
		if (isExecutableNode(curtype) && m_bEdit) {
			getCkbNeedButtonLog().setEnabled(true);
			getCkbButtonPower().setEnabled(true);
		} else {
			getCkbNeedButtonLog().setEnabled(false);
			getCkbButtonPower().setEnabled(false);
		}

		//getComboBoxProperty().setSelectedIndex(index);
		getComboBoxProperty().setSelectedItem(FUNC_NODE[index]);
		getComboBoxProperty().addItemListener(this);
		//����
		if (getSelectedNode().getChildCount() > 0)
			getComboBoxProperty().setEnabled(false); //��֤�����ӽڵ���ǰ�ڵ㹦�����Բ��ɸĶ�
		//��Ӧ����
		String className = fr.getClassName();
		if (className == null)
			getTextFieldClass().setText("");
		else
			getTextFieldClass().setText(className);
		//��Ӧ�����ļ���
		String helpName = fr.getHelpName();
		if (helpName == null)
			getTextFieldHelp().setText("");
		else
			getTextFieldHelp().setText(helpName);

		//resid��Ϊ�Զ�����1
		String strResid = fr.getResid();
		if (strResid == null)
			getTextFieldResid().setText("");
		else
			getTextFieldResid().setText(strResid);

		//���ñ�־
		if (fr.getForbidFlag().intValue() == 1)
			getRadioForbidYes().setSelected(true);
		else
			getRadioForbidNo().setSelected(true);
		//������ʾ��־
		if (fr.hasPara().booleanValue())
			getRadioShowParamYes().setSelected(true);
		else
			getRadioShowParamNo().setSelected(true);
		//����
		String funDesc = fr.getFunDesc();
		if (funDesc != null)
			getTextAreaDesc().setText(funDesc);

		//�Ƿ���Ҫ��ʾ�ϻ���־
		getCkbNeedButtonLog().setSelected(fr.isIsneedbuttonlog().booleanValue());

		//�Ƿ����ð�ťȨ�޿���
		getCkbButtonPower().setSelected(fr.getIsbuttonpower().booleanValue());

		//zsb+:��֯����,��ѡ����
		getOrgnizeTypeCombo().setEnabled(false);
		String sOrgTypeCode = null;
		if (isExecutableNode(iParentPropIndex)) {
			//���ڵ��ǿ�ִ�нڵ㣬����֯�����ø��ڵ����֯����
			sOrgTypeCode = ((FunRegNode) node).getNodeVo().getOrgTypecode();
		} else if (iParentPropIndex == POWERFUL_BUTTON || iParentPropIndex == POWERLESS_BUTTON) {
			//���ڵ��ǰ�ť������֯�������鸸�ڵ����֯����
			sOrgTypeCode = ((FunRegNode) node.getParent()).getNodeVo().getOrgTypecode();
		} else if (isExecutableNode(index)) {
			//��ǰ�ڵ�Ϊ��ִ�нڵ㣬ȡ������
			sOrgTypeCode = fr.getOrgTypecode();
		}//���������Ķ�Ϊ��
		if (sOrgTypeCode == null) {
			getOrgnizeTypeCombo().setSelectedItem(null);
		} else {
			OrgnizeTypeVO voOrgType = null;
			int i = 0;
			for (; i < getOrgnizeTypeCombo().getItemCount(); i++) {
				voOrgType = (OrgnizeTypeVO) getOrgnizeTypeCombo().getItemAt(i);
				if (sOrgTypeCode.equals(voOrgType.getTypecode())) {
					getOrgnizeTypeCombo().setSelectedIndex(i);
					break;
				}
			}
			if (i == getOrgnizeTypeCombo().getItemCount()) {//��ʾû��ƥ�䵽
				getOrgnizeTypeCombo().setSelectedItem(null);
			}
		}
	}

	/**
	 * ��ʾ����Ϣ
	 * �������ڣ�(2001-3-29 20:11:17)
	 */
	public void displayNullInfo() {
		//���ܱ���
		String funCode = getSelectedNode().getNodeVo().getFunCode();
		String strCode = "";
		if (funCode != null && funCode.trim().length() >= LEVELLEN) {
			if ((funCode.trim().length() + LEVELLEN) > LEVELLEN * 30)
				getTextFieldFunCode().setMaxLength(LEVELLEN * 30);
			else
				getTextFieldFunCode().setMaxLength(funCode.trim().length() + LEVELLEN);
			//��������
			strCode = funCode.trim();
		} else {
			getTextFieldFunCode().setMaxLength(LEVELLEN);
		}
		//���ɹ��ܱ������һ�������ֵܽڵ㲻�ظ���
		String strTemp = generateCode();
		//���ñ���
		strCode += strTemp;
		getTextFieldFunCode().setText(strCode);
		if (strTemp.equals("**")) {
			//ѡ�б���ĩ��
			getTextFieldFunCode().setSelectionStart(strCode.length() - 2);
			getTextFieldFunCode().setSelectionEnd(strCode.length());
		}
		//��������
		getTextFieldFunName().setText("");
		//��ø��ڵ㹦������
		int iParentPropIndex = getSelectedNode().getNodeVo().getFunProperty().intValue();
		//��������
		if (isExecutableNode(iParentPropIndex) || iParentPropIndex == POWERFUL_BUTTON
				|| iParentPropIndex == POWERLESS_BUTTON || iParentPropIndex == PARAMETER) {
			getLabelName().setText("�� ť �� ��");
			getLabelClassName().setText("�� ť �� ��");
			getComboBoxPropType().setSelectedItem(" ");
			getComboBoxPropType().setEnabled(false); //��ť�����
			getTextFieldHelp().setEnabled(false);
			//zsb+:��֯����Ϊ���ڵ����֯���ͣ����ı䣬��֯���Ͳ�����		
			getOrgnizeTypeCombo().setEnabled(false);
		} else {
			getComboBoxPropType().setSelectedIndex(0); //��ڵ���ִ�нڵ�
			getTextFieldHelp().setEnabled(true);
			//zsb+:��֯����Ϊ��һ�������ڹ�������Ĭ��Ϊ��ִ�нڵ�
			getOrgnizeTypeCombo().setEnabled(true);
			getOrgnizeTypeCombo().setSelectedIndex(0);
		}
		//���õ�ǰ�ڵ��ѡ�Ĺ�������
		getComboBoxProperty().removeItemListener(this);
		if (isExecutableNode(iParentPropIndex) || iParentPropIndex == POWERFUL_BUTTON
				|| iParentPropIndex == POWERLESS_BUTTON) { //��ִ�й��ܽڵ�
			getComboBoxProperty().removeAllItems();
			getComboBoxProperty().addItem(FUNC_NODE[POWERFUL_BUTTON]);
			getComboBoxProperty().addItem(FUNC_NODE[POWERLESS_BUTTON]);
			getComboBoxProperty().addItem(FUNC_NODE[PARAMETER]);
		} else if (iParentPropIndex == INEXECUTABLE_FUNC_NODE) { //�鹦�ܽڵ�
			getComboBoxProperty().removeAllItems();
			getComboBoxProperty().addItem(FUNC_NODE[EXECUTABLE_FUNC_NODE]);
			getComboBoxProperty().addItem(FUNC_NODE[INEXECUTABLE_FUNC_NODE]);
			getComboBoxProperty().addItem(FUNC_NODE[EXECUTABLE_FUNC_FRAME]);
			getComboBoxProperty().addItem(FUNC_NODE[LFW_FUNC_NODE]);
		} else
			System.out.println("������");
		getComboBoxProperty().addItemListener(this);
		//��Ӧ����
		getTextFieldClass().setText("");
		//��Ӧ�����ļ���
		getTextFieldHelp().setText("");

		//resid��Ϊ�Զ�����1
		getTextFieldResid().setText("");
		//if (iParentPropIndex == EXECUTABLE_FUNC_NODE
		//|| iParentPropIndex == EXECUTABLE_FUNC_FRAME)
		//getTextFieldHelp().setEnabled(false);
		//else
		//getTextFieldHelp().setEnabled(true);
		//���ñ�־
		if (getSelectedNode().getNodeVo().getForbidFlag().intValue() == 1) //���ڵ�
			getRadioForbidYes().setSelected(true);
		else
			getRadioForbidNo().setSelected(true);
		//������ʾ��־
		getRadioShowParamNo().setSelected(true);
		//����
		getTextAreaDesc().setText("");
		//���ܱ����ı����ý���
		getTextFieldFunCode().grabFocus();
	}

	/**
	 * ��ʾ���ڵ��Ӧ��ϸ
	 * �������ڣ�(2001-3-29 20:11:17)
	 */
	public void displayRootInfo() {
		//���ñ༭̬
		setEditState(false);
		//��������
		//String funName=((FunRegNode) getSelectedNode().getRoot()).getNodeVo().getFunName();
		String funName = ((FunRegNode) ((DefaultTreeModel) m_funTree.getModel()).getRoot()).getNodeVo()
				.getFunName();
		if (funName != null)
			getTextFieldFunName().setText(funName);
		else
			getTextFieldFunName().setText("");
		//���ܱ���
		getTextFieldFunCode().setText("");
		//��������
		getComboBoxPropType().setSelectedItem(" ");
		//��������
		getComboBoxProperty().removeAllItems();
		getComboBoxProperty().addItem(FUNC_NODE[INEXECUTABLE_FUNC_NODE]);
		//��Ӧ����
		getTextFieldClass().setText("");
		//��Ӧ�����ļ���
		getTextFieldHelp().setText("");
		//resid��Ϊ�Զ�����1
		getTextFieldResid().setText("");
		//���ñ�־
		getRadioForbidNo().setSelected(true);
		//������ʾ��־
		getRadioShowParamNo().setSelected(true);
		//����
		getTextAreaDesc().setText("");
		//zsb+:��֯����
		getOrgnizeTypeCombo().setSelectedItem(null);
		getOrgnizeTypeCombo().setEnabled(false);
	}

	/**
	 * ��Ч������
	 * �������ڣ�(2001-4-11 14:50:20)
	 * @return boolean
	 * @param funCode java.lang.String
	 */
	public boolean exist(FuncRegisterVO vo) {
		try {
			FuncRegisterVO voArray = SFServiceFacility.getFuncRegisterQueryService()
					.findFuncRegisterVOByPrimaryKey(vo.getPrimaryKey());
			if (voArray == null) {
				return false;
			} else {
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * ��Ч������
	 * �������ڣ�(2001-4-11 14:50:20)
	 * @return boolean
	 * @param funCode java.lang.String
	 */
	public boolean exist(String funCode) {
		try {
			FuncRegisterVO[] voArray = SFServiceFacility.getFuncRegisterQueryService().queryByFunCode(
					funCode);
			if (voArray == null || voArray.length <= 0) {
				return false;
			} else {
				return true;
			}
		} catch (Exception ex) {
			return true;
		}
	}

	/**
	 * ���ɹ��ܱ������һ�������ֵܽڵ㲻�ظ���
	 * �������ڣ�(01-5-23 8:37:17)
	 * @return java.lang.String
	 */
	public String generateCode() {
		String strTemp = "";
		int iChildCount = getSelectedNode().getChildCount();
		if (iChildCount == 0)
			strTemp = "1";
		else {
			String strIndex = null;
			//�������б���ĩ������ֵ����
			int[] iIndices = new int[iChildCount];
			for (int i = 0; i < iChildCount; i++) {
				strIndex = ((FunRegNode) getSelectedNode().getChildAt(i)).getNodeVo().getFunCode();
				strIndex = strIndex.substring(strIndex.length() - LEVELLEN, strIndex.length());
				try {
					iIndices[i] = Integer.parseInt(strIndex);
				} catch (NumberFormatException e) {
					iIndices[i] = -1; //���������ֵĴ���
				}
			}
			//���ҵ�һ�����ظ��ı���ĩ����ֵ������ԭ��
			for (int i = 0; i <= iChildCount; i++) {
				boolean bEnd = true;
				for (int j = 0; j < iChildCount; j++)
					if (iIndices[j] == i + 1) {
						bEnd = false; //ĩ����ֵi+1�ѱ�ʹ��
						break;
					}
				if (bEnd) {
					strTemp = String.valueOf(i + 1);
					break;
				}
			}
			if (Integer.parseInt(strTemp) >= 100) {
				System.out.println("ͬһ�ڵ���ӽڵ����Ѵﵽ" + iChildCount + "��");
				return "**";
			}
		}
		//����ʹ�ñ���ĩ���뼶�����
		String strCodeLast = "";
		for (int i = 0; i < LEVELLEN - strTemp.length(); i++)
			strCodeLast += "0";
		strCodeLast += strTemp;
		return strCodeLast;
	}

	private ButtonGroup getBGForbid() {
		if (ivjBGForbid == null) {
			ivjBGForbid = new ButtonGroup();

		}
		return ivjBGForbid;
	}

	private ButtonGroup getBGShowParam() {
		if (ivjBGShowParam == null) {
			ivjBGShowParam = new ButtonGroup();

		}
		return ivjBGShowParam;
	}

	private UICheckBox getCkbNeedButtonLog() {
		if (m_ckbNeedButtonLog == null)
			m_ckbNeedButtonLog = new UICheckBox();
		return m_ckbNeedButtonLog;
	}

	private UICheckBox getCkbButtonPower() {
		if (m_ckbButtonPower == null)
			m_ckbButtonPower = new UICheckBox();
		return m_ckbButtonPower;
	}

	private UIComboBox getComboBoxProperty() {
		if (ivjComboBoxProperty == null) {
			ivjComboBoxProperty = new UIComboBox();
			ivjComboBoxProperty.setName("ComboBoxProperty");
			ivjComboBoxProperty.setBackground(Color.white);

		}
		return ivjComboBoxProperty;
	}

	private UIComboBox getComboBoxPropType() {
		if (ivjComboBoxPropType == null) {
			ivjComboBoxPropType = new UIComboBox();
			ivjComboBoxPropType.setName("ComboBoxPropType");
			ivjComboBoxPropType.setBackground(Color.white);
			// user code begin {1}
			if (PROP_TYPE != null)
				for (int i = 0; i < PROP_TYPE.length; i++)
					getComboBoxPropType().addItem(PROP_TYPE[i]);
			getComboBoxPropType().addItem(" ");

		}
		return ivjComboBoxPropType;
	}

	private UILabel getLabelClassName() {
		if (ivjLabelClassName == null) {
			ivjLabelClassName = new UILabel();
			ivjLabelClassName.setName("LabelClassName");
			ivjLabelClassName.setText("��Ӧ�ļ�����ؼ���");

		}
		return ivjLabelClassName;
	}

	private UILabel getLabelCode() {
		if (ivjLabelCode == null) {
			ivjLabelCode = new UILabel();
			ivjLabelCode.setName("LabelCode");
			ivjLabelCode.setText("�� �� �� ��");

		}
		return ivjLabelCode;
	}

	private UILabel getLabelDesc() {
		if (ivjLabelDesc == null) {
			ivjLabelDesc = new UILabel();
			ivjLabelDesc.setName("LabelDesc");
			ivjLabelDesc.setText("�� �� �� ��");

		}
		return ivjLabelDesc;
	}

	private UILabel getLabelForbid() {
		if (ivjLabelForbid == null) {
			ivjLabelForbid = new UILabel();
			ivjLabelForbid.setName("LabelForbid");
			ivjLabelForbid.setText("�� �� �� ��");

		}
		return ivjLabelForbid;
	}

	private UILabel getLabelButtonLog() {
		if (ivjLabelButtonLog == null) {
			ivjLabelButtonLog = new UILabel();
			ivjLabelButtonLog.setName("ivjLabelButtonLog");
			ivjLabelButtonLog.setText("�Ƿ����ð�ť����־");

		}
		return ivjLabelButtonLog;
	}

	private UILabel getLabelButtonPower() {
		if (ivjLabelButtonPower == null) {
			ivjLabelButtonPower = new UILabel();
			ivjLabelButtonPower.setName("ivjLabelButtonPower");
			ivjLabelButtonPower.setText("���ð�ťȨ�޿���");

		}
		return ivjLabelButtonPower;
	}

	private UILabel getLabelHelpName() {
		if (ivjLabelHelpName == null) {
			ivjLabelHelpName = new UILabel();
			ivjLabelHelpName.setName("LabelHelpName");
			ivjLabelHelpName.setText("��Ӧ�����ļ���");

		}
		return ivjLabelHelpName;
	}

	private UILabel getLabelResid() {
		if (ivjLabelResid == null) {
			ivjLabelResid = new UILabel();
			ivjLabelResid.setName("ivjLabelResid");
			ivjLabelResid.setText("�Զ�����1");

		}
		return ivjLabelResid;
	}

	private UILabel getLabelName() {
		if (ivjLabelName == null) {
			ivjLabelName = new UILabel();
			ivjLabelName.setName("LabelName");
			ivjLabelName.setText("�� �� �� ��");

		}
		return ivjLabelName;
	}

	private UILabel getLabelProperty() {
		if (ivjLabelProperty == null) {
			ivjLabelProperty = new UILabel();
			ivjLabelProperty.setName("LabelProperty");
			ivjLabelProperty.setText("�� �� �� ��");

		}
		return ivjLabelProperty;
	}

	private UILabel getLabelPropType() {
		if (ivjLabelPropType == null) {
			ivjLabelPropType = new UILabel();
			ivjLabelPropType.setName("LabelPropType");
			ivjLabelPropType.setText("�� �� �� ��");

		}
		return ivjLabelPropType;
	}

	private UILabel getLabelShowParam() {
		if (ivjLabelShowParam == null) {
			ivjLabelShowParam = new UILabel();
			ivjLabelShowParam.setName("LabelShowParam");
			ivjLabelShowParam.setText("���������Ƿ���ʾ");

		}
		return ivjLabelShowParam;
	}

	/**
	 * �ӡ���ִ�й��ܽڵ㡱�����п�ݼ����ҵ���ǰ��ťƥ���
	 * �������ڣ�(2003-11-7 17:11:26)
	 * @return nc.vo.pub.hotkey.HotkeyRegisterVO
	 * @param butnCode java.lang.String
	 * @param execFuncNodeHotkeys nc.vo.pub.hotkey.HotkeyRegisterVO[]
	 * @author���׾�
	 */
	private HotkeyRegisterVO getMatchedHotkey(String butnCode, HotkeyRegisterVO[] execFuncNodeHotkeys) {
		if (execFuncNodeHotkeys == null)
			return null;
		for (int i = 0; i < execFuncNodeHotkeys.length; i++) {
			if (execFuncNodeHotkeys[i].getButton_name().equals(butnCode))
				return execFuncNodeHotkeys[i];
		}
		return null;
	}

	private UIComboBox getOrgnizeTypeCombo() {
		if (comboOrgnizeType == null) {
			comboOrgnizeType = new UIComboBox();
			loadOrgnizeType();
		}
		return comboOrgnizeType;
	}

	private UILabel getOrgnizeTypeLabel() {
		if (lbOrgnizeType == null) {
			lbOrgnizeType = new UILabel();
			lbOrgnizeType.setText("�� ֯ �� ��");
		}
		return lbOrgnizeType;
	}

	/**
	 * ʹ��GroupLayout����
	 * @author leijun 2007-10-12
	 */
	private UIPanel getPnDetail2() {
		if (ivjPnDetail == null) {
			ivjPnDetail = new UIPanel();
			ivjPnDetail.setName("PnDetail");
			ivjPnDetail.setPreferredSize(new Dimension(480, 420));
			//ivjPnDetail.setLayout(new GridBagLayout());
			ivjPnDetail.setMinimumSize(new Dimension(360, 420));

			////
			GroupLayout layout = new GroupLayout(ivjPnDetail);
			ivjPnDetail.setLayout(layout);
			layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.LEADING).add(
					layout.createSequentialGroup().addContainerGap().add(
							layout.createParallelGroup(GroupLayout.LEADING).add(getLabelClassName()).add(
									getLabelName()).add(getLabelCode()).add(getLabelProperty()).add(
									getLabelHelpName()).add(getLabelResid()).add(getLabelPropType()).add(
									getOrgnizeTypeLabel()).add(getLabelButtonLog()).add(getLabelButtonPower()).add(
									getLabelForbid())./*add(
																																												getLabelShowParam()).*/add(getLabelDesc()))
							.addPreferredGap(LayoutStyle.RELATED).add(
									layout.createParallelGroup(GroupLayout.LEADING).add(getSclPnTextArea(),
											GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)./*add(
																																																		layout.createSequentialGroup().add(getRadioShowParamYes()).add(18, 18, 18)
																																																				.add(getRadioShowParamNo())).*/add(
											layout.createSequentialGroup().add(getRadioForbidNo()).add(18, 18, 18).add(
													getRadioForbidYes())).add(getCkbNeedButtonLog()).add(getCkbButtonPower())
											.add(GroupLayout.TRAILING, getTextFieldFunName(), GroupLayout.DEFAULT_SIZE,
													365, Short.MAX_VALUE).add(GroupLayout.TRAILING, getTextFieldFunCode(),
													GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE).add(GroupLayout.TRAILING,
													getComboBoxProperty(), 0, 365, Short.MAX_VALUE).add(getTextFieldClass(),
													GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE).add(getTextFieldHelp(),
													GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE).add(getTextFieldResid(),
													GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE).add(
													getComboBoxPropType(), 0, 365, Short.MAX_VALUE).add(
													getOrgnizeTypeCombo(), 0, 365, Short.MAX_VALUE)).addContainerGap()));
			layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.LEADING).add(
					layout.createSequentialGroup().addContainerGap().add(
							layout.createParallelGroup(GroupLayout.BASELINE).add(getLabelCode()).add(
									getTextFieldFunCode(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(
							layout.createParallelGroup(GroupLayout.BASELINE).add(getLabelName()).add(
									getTextFieldFunName(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(
							layout.createParallelGroup(GroupLayout.BASELINE).add(getLabelProperty()).add(
									getComboBoxProperty(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(
							layout.createParallelGroup(GroupLayout.BASELINE).add(getLabelClassName()).add(
									getTextFieldClass(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(
							layout.createParallelGroup(GroupLayout.BASELINE).add(getLabelHelpName()).add(
									getTextFieldHelp(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(
							layout.createParallelGroup(GroupLayout.BASELINE).add(getLabelResid()).add(
									getTextFieldResid(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(
							layout.createParallelGroup(GroupLayout.BASELINE).add(getLabelPropType()).add(
									getComboBoxPropType(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(
							layout.createParallelGroup(GroupLayout.BASELINE).add(getOrgnizeTypeLabel()).add(
									getOrgnizeTypeCombo(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(
							layout.createParallelGroup(GroupLayout.BASELINE).add(getLabelButtonLog()).add(
									getCkbNeedButtonLog())).addPreferredGap(LayoutStyle.RELATED).add(
							layout.createParallelGroup(GroupLayout.BASELINE).add(getLabelButtonPower()).add(
									getCkbButtonPower())).addPreferredGap(LayoutStyle.RELATED).add(
							layout.createParallelGroup(GroupLayout.BASELINE).add(getLabelForbid()).add(
									getRadioForbidNo()).add(getRadioForbidYes()))
							.addPreferredGap(LayoutStyle.RELATED)/*.add(
																																												layout.createParallelGroup(GroupLayout.BASELINE).add(getLabelShowParam()).add(
																																														getRadioShowParamYes()).add(getRadioShowParamNo())).addPreferredGap(
																																												LayoutStyle.RELATED).*/.add(
									layout.createParallelGroup(GroupLayout.LEADING).add(getLabelDesc()).add(
											getSclPnTextArea(), GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
							.addContainerGap()));
			////
		}
		return ivjPnDetail;
	}

	private UIPanel getPnWest() {
		if (ivjPnWest == null) {
			ivjPnWest = new UIPanel();
			ivjPnWest.setName("PnWest");
			ivjPnWest.setPreferredSize(new Dimension(260, 160));
			ivjPnWest.setBorder(new EtchedBorder());
			ivjPnWest.setLayout(new BorderLayout());
			ivjPnWest.setMinimumSize(new Dimension(60, 46));
			ivjPnWest.add(getPnWestWest(), "West");
			ivjPnWest.add(getPnWestNorth(), "North");
			ivjPnWest.add(getPnWestEast(), "East");
			ivjPnWest.add(getPnWestSouth(), "South");
			ivjPnWest.add(getSclPnTree(), "Center");

		}
		return ivjPnWest;
	}

	private UIPanel getPnWestEast() {
		if (ivjPnWestEast == null) {
			ivjPnWestEast = new UIPanel();
			ivjPnWestEast.setName("PnWestEast");
			ivjPnWestEast.setPreferredSize(new Dimension(2, 2));

		}
		return ivjPnWestEast;
	}

	private UIPanel getPnWestNorth() {
		if (ivjPnWestNorth == null) {
			ivjPnWestNorth = new UIPanel();
			ivjPnWestNorth.setName("PnWestNorth");
			ivjPnWestNorth.setPreferredSize(new Dimension(2, 2));

		}
		return ivjPnWestNorth;
	}

	private UIPanel getPnWestSouth() {
		if (ivjPnWestSouth == null) {
			ivjPnWestSouth = new UIPanel();
			ivjPnWestSouth.setName("PnWestSouth");
			ivjPnWestSouth.setPreferredSize(new Dimension(2, 2));

		}
		return ivjPnWestSouth;
	}

	private UIPanel getPnWestWest() {
		if (ivjPnWestWest == null) {
			ivjPnWestWest = new UIPanel();
			ivjPnWestWest.setName("PnWestWest");
			ivjPnWestWest.setPreferredSize(new Dimension(2, 2));

		}
		return ivjPnWestWest;
	}

	/**
	 * ת����������Ϊ����
	 * �������ڣ�(01-5-21 12:49:34)
	 * @return int
	 * @param strSelItem java.lang.String
	 */
	public int getPropIndex(String strSelItem) {
		if (strSelItem.equals(FUNC_NODE[EXECUTABLE_FUNC_NODE]))
			return EXECUTABLE_FUNC_NODE;
		else if (strSelItem.equals(FUNC_NODE[INEXECUTABLE_FUNC_NODE]))
			return INEXECUTABLE_FUNC_NODE;
		else if (strSelItem.equals(FUNC_NODE[POWERFUL_BUTTON]))
			return POWERFUL_BUTTON;
		else if (strSelItem.equals(FUNC_NODE[POWERLESS_BUTTON]))
			return POWERLESS_BUTTON;
		else if (strSelItem.equals(FUNC_NODE[PARAMETER]))
			return PARAMETER;
		else if (strSelItem.equals(FUNC_NODE[EXECUTABLE_FUNC_FRAME]))
			return EXECUTABLE_FUNC_FRAME;
		else if (strSelItem.equals(FUNC_NODE[LFW_FUNC_NODE]))
			return LFW_FUNC_NODE;
		System.out.println("�޴�����");
		return -1;
	}

	private UIRadioButton getRadioForbidNo() {
		if (ivjRadioForbidNo == null) {
			ivjRadioForbidNo = new UIRadioButton();
			ivjRadioForbidNo.setName("RadioForbidNo");
			ivjRadioForbidNo.setSelected(true);
			ivjRadioForbidNo.setText("��");
			// user code begin {1}
			//getRadioForbidNo().setBackground(getPnDetail().getBackground());
			//getRadioForbidNo().setForeground(Color.black);
			getRadioForbidNo().setToolTipText("���ĳ���ܽڵ㲻�����ã������������Ƚڵ����������");

		}
		return ivjRadioForbidNo;
	}

	private UIRadioButton getRadioForbidYes() {
		if (ivjRadioForbidYes == null) {
			ivjRadioForbidYes = new UIRadioButton();
			ivjRadioForbidYes.setName("RadioForbidYes");
			ivjRadioForbidYes.setSelected(false);
			ivjRadioForbidYes.setText("��");
			// user code begin {1}
			//getRadioForbidYes().setBackground(getPnDetail().getBackground());
			//getRadioForbidYes().setForeground(Color.black);
			getRadioForbidYes().setToolTipText("���ĳ���ܽڵ���Ϊ���ã�������������ڵ������Ϊ����");

		}
		return ivjRadioForbidYes;
	}

	private UIRadioButton getRadioShowParamNo() {
		if (ivjRadioShowParamNo == null) {
			ivjRadioShowParamNo = new UIRadioButton();
			ivjRadioShowParamNo.setName("RadioShowParamNo");
			ivjRadioShowParamNo.setSelected(true);
			ivjRadioShowParamNo.setText("��");

		}
		return ivjRadioShowParamNo;
	}

	private UIRadioButton getRadioShowParamYes() {
		if (ivjRadioShowParamYes == null) {
			ivjRadioShowParamYes = new UIRadioButton();
			ivjRadioShowParamYes.setName("RadioShowParamYes");
			ivjRadioShowParamYes.setSelected(false);
			ivjRadioShowParamYes.setText("��");

		}
		return ivjRadioShowParamYes;
	}

	private UIScrollPane getSclPnTextArea() {
		if (ivjSclPnTextArea == null) {
			ivjSclPnTextArea = new UIScrollPane();
			ivjSclPnTextArea.setName("SclPnTextArea");
			ivjSclPnTextArea.setViewportView(getTextAreaDesc());

		}
		return ivjSclPnTextArea;
	}

	private UIScrollPane getSclPnTree() {
		if (ivjSclPnTree == null) {
			ivjSclPnTree = new UIScrollPane();
			ivjSclPnTree.setName("SclPnTree");
			ivjSclPnTree.setMinimumSize(new Dimension(32, 22));

		}
		return ivjSclPnTree;
	}

	/**
	 * ���ѡ�нڵ�
	 * �������ڣ�(2001-3-29 14:38:36)
	 */
	public FunRegNode getSelectedNode() {
		return m_selectedNode;
	}

	private UISplitPane getSplitPn() {
		if (ivjSplitPn == null) {
			ivjSplitPn = new UISplitPane(1);
			ivjSplitPn.setName("SplitPn");
			ivjSplitPn.setDividerSize(3);
			ivjSplitPn.setPreferredSize(new Dimension(360, 10));
			ivjSplitPn.setContinuousLayout(true);
			ivjSplitPn.setDividerLocation(250);
			getSplitPn().add(getPnWest(), "left");
			getSplitPn().add(getPnDetail2(), "right");

		}
		return ivjSplitPn;
	}

	/**
	 * ����ַ����ֽڳ���
	 * �������ڣ�(2001-4-12 18:59:50)
	 * @return int
	 * @param arg java.lang.String
	 */
	public int getStrlength(String arg) {
		if (arg == null)
			return 0;
		int len = 0;
		for (int i = 0; i < arg.length(); i++) {
			char curChar = arg.charAt(i);
			if (curChar > 127)
				len += 2;
			else
				len += 1;
		}
		return len;
	}

	private UITextArea getTextAreaDesc() {
		if (ivjTextAreaDesc == null) {
			ivjTextAreaDesc = new UITextArea();
			ivjTextAreaDesc.setName("TextAreaDesc");
			ivjTextAreaDesc.setLineWrap(true);
			ivjTextAreaDesc.setBounds(0, 0, 160, 120);
			ivjTextAreaDesc.setMaxLength(512);
		}
		return ivjTextAreaDesc;
	}

	private UITextField getTextFieldClass() {
		if (ivjTextFieldClass == null) {
			ivjTextFieldClass = new UITextField();
			ivjTextFieldClass.setName("TextFieldClass");
			ivjTextFieldClass.setMaxLength(200);
		}
		return ivjTextFieldClass;
	}

	private UITextField getTextFieldFunCode() {
		if (ivjTextFieldFunCode == null) {
			ivjTextFieldFunCode = new UITextField();
			ivjTextFieldFunCode.setName("TextFieldFunCode");
			ivjTextFieldFunCode.setTextType("TextStr");
			ivjTextFieldFunCode.setMaxLength(60);
			ivjTextFieldFunCode.setHorizontalAlignment(JTextField.LEFT);

		}
		return ivjTextFieldFunCode;
	}

	private UITextField getTextFieldFunName() {
		if (ivjTextFieldFunName == null) {
			ivjTextFieldFunName = new UITextField();
			ivjTextFieldFunName.setName("TextFieldFunName");
			ivjTextFieldFunName.setTextType("TextStr");
			ivjTextFieldFunName.setMaxLength(40);
			ivjTextFieldFunName.setHorizontalAlignment(JTextField.LEFT);

		}
		return ivjTextFieldFunName;
	}

	private UITextField getTextFieldHelp() {
		if (ivjTextFieldHelp == null) {
			ivjTextFieldHelp = new UITextField();
			ivjTextFieldHelp.setName("TextFieldHelp");
			ivjTextFieldHelp.setMaxLength(500);

		}
		return ivjTextFieldHelp;
	}

	private UITextField getTextFieldResid() {
		if (ivjTextFieldResid == null) {
			ivjTextFieldResid = new UITextField();
			ivjTextFieldResid.setName("ivjTextFieldResid");
			ivjTextFieldResid.setMaxLength(500);

		}
		return ivjTextFieldResid;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return "����ע��";
	}

	/**
	 * ���ȫ�����ܽڵ�
	 * �������ڣ�(2001-3-29 11:02:30)
	 * @return nc.vo.sm.zs.funcreg.RegisterVO[]
	 */
	public FuncRegisterVO[] getVoArray() {
		try {
			return SFServiceFacility.getFuncRegisterQueryService().queryAllFuncRegisterVOs(null);
		} catch (Exception ex) {
			System.out.println(ex);
			return null;
		}
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * @param exception java.lang.Throwable
	 */
	public void handleException(java.lang.Throwable e) {
		Logger.error(e.getMessage(), e);
	}

	/**
	 * ��ʼ����ť��
	 * �������ڣ�(2001-3-28 20:21:47)
	 */
	public void initButtonGroup() {
		getBGForbid().add(getRadioForbidYes());
		getBGForbid().add(getRadioForbidNo());
		getBGShowParam().add(getRadioShowParamYes());
		getBGShowParam().add(getRadioShowParamNo());
	}

	private void initConnections() {

		getComboBoxProperty().addItemListener(this);
	}

	/**
	 * ��ʼ���ࡣ
	 */

	private void initialize() {

		setName("FunRegisterUI");
		setLayout(new BorderLayout());
		setSize(760, 420);
		add(getSplitPn(), "Center");
		initConnections();

		// user code begin {2}
		//lj+ 2003-11-10 �����Ӳ˵���ʽ
		m_boHotkey.addChildButton(m_boDefaultHotkey);
		m_boHotkey.addChildButton(m_boHotkeyReg);

		setButtons(m_MainButtonGroup);
		initButtonGroup();
		initTree();
		// user code end
		getCkbNeedButtonLog().setEnabled(false);
		getCkbButtonPower().setEnabled(false);
	}

	/**
	 * ��ʼ����
	 * �������ڣ�(2001-3-28 20:23:19)
	 */
	public void initTree() {
		try {
			//���ð�ť����״̬
			setButtonEnabled(m_boSave, false);
			setButtonEnabled(m_boCancel, false);
			//������
			FuncRegTree tree = new FuncRegTree();
			//���Ʊ� update
			m_funTree = tree.createTreeByAllnode(getVoArray());
			//m_funTree = tree.createTree(getVoArray());
			m_funTree.getTreeNodeSearcher().setWildcardEnabled(true);

			//�������Ԫ���Σ�����������Ȩ�ް�ť����Ȩ�ް�ť��
			m_funTree.setCellRenderer(new PowerCellRenderer());
			//����������
			m_funTree.putClientProperty("JTree.lineStyle", "Angled");
			//��������ѡģʽ
			m_funTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			//���ü��ر�־
			m_loading = false;
			//�����ѡȡ����
			m_funTree.addTreeSelectionListener(this);
			//ѡ�и�
			m_funTree.setSelectionRow(0);
			//������Ϊ��ͼ
			getSclPnTree().setViewportView(m_funTree);
		} catch (Exception ex) {
			handleException(ex);
		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == getComboBoxProperty())
			this.comboBoxProperty_ItemStateChanged(e);
	}

	private void loadOrgnizeType() {
		OrgnizeTypeVO[] voOrgType = OrgnizeType_client.getTypeVO();
		if (voOrgType == null) { return; }
		for (int i = 0; i < voOrgType.length; i++) {
			getOrgnizeTypeCombo().addItem(voOrgType[i]);
		}
	}

	/**
	 * ����
	 * �������ڣ�(2001-3-29 15:00:41)
	 */
	public void onBoAdd() {
		if (m_funTree.getSelectionPath() == null) {
			//errorFlag = true;
			showErrorMessage("����ѡ�нڵ㣡");
			return;
		}
		String strFuncCode = getSelectedNode().getNodeVo().getFunCode();
		if (strFuncCode != null && strFuncCode.length() + LEVELLEN > LEVELLEN * 30) {
			showErrorMessage("�Ѵﵽ��󼶴Σ������������ӽڵ㣡");
			return;
		}
		try {
			//���ð�ť����״̬
			setButtonEnabled(m_boAdd, false);
			setButtonEnabled(m_boUpdate, false);
			setButtonEnabled(m_boDel, false);
			setButtonEnabled(m_boSave, true);
			setButtonEnabled(m_boCancel, true);
			//���ñ༭̬
			setEditState(true);
			m_bEdit = true;
			//���ñ�־
			m_haveSaveFlag = false;
			m_adding = true;
			//��ʾ����ϸ
			displayNullInfo();
			showHintMessage("����״̬");
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	/**
	 * ȡ��
	 * �������ڣ�(01-5-21 13:28:45)
	 */
	public void onBoCancel() {
		//�����༭̬
		stopEditting();
		//�ָ����ӻ��޸�ǰ�Ľڵ���Ϣ
		setButtonEnabled(m_boAdd, true);
		if (getSelectedNode().isRoot()) {
			setButtonEnabled(m_boUpdate, false);
			setButtonEnabled(m_boDel, false);
			displayRootInfo(); //���ڵ���Ϣ
		} else {
			setButtonEnabled(m_boUpdate, true);
			setButtonEnabled(m_boDel, true);
			displayNodeInfo(); //�Ǹ��ڵ���Ϣ
		}
	}

	/**
	 * ɾ��
	 * �������ڣ�(2001-3-29 15:00:41)
	 */
	public void onBoDel() {
		if (m_funTree.getSelectionPath() == null) {
			showErrorMessage("����ѡ��Ҫɾ���Ľڵ㣡");
			return;
		} else if (!getSelectedNode().isLeaf()) {
			showErrorMessage("������ɾ���ӽڵ㣡");
			return;
		}
		try {
			if (MessageDialog.showOkCancelDlg(this, "ɾ��ȷ��", "ȷʵҪɾ����") == MessageDialog.ID_OK) {
				int index = SFServiceFacility.getFuncRegisterService().deleteByVO(
						getSelectedNode().getNodeVo());
				if (index == 0) {
					showErrorMessage("�ýڵ��ѱ��û�ʹ�ã�����ɾ����");
					return;
				}
				FunRegNode paNode = (FunRegNode) getSelectedNode().getParent();
				paNode.remove(getSelectedNode());
				((DefaultTreeModel) (m_funTree.getModel())).reload(paNode);
				if (paNode.isRoot())
					displayRootInfo();
				//ѡ�б�ɾ�ڵ�ĸ��ڵ�
				m_selPath = new TreePath(paNode.getPath());
				setSelPath(m_selPath);
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	/**
	 * ����
	 * �������ڣ�(2001-3-29 15:00:41)
	 */
	public void onBoSave() {
		if (getSelectedNode() == null) {
			showErrorMessage("����ѡ�и��ڵ㣡");
			return;
		}
		if (m_haveSaveFlag) {
			showErrorMessage("������������");
			return;
		}
		FuncRegisterVO voBack = null;
		//���ܱ���
		String funCode = getTextFieldFunCode().getText().trim();
		if (funCode == null || funCode.length() <= 0) {
			showErrorMessage("���ܱ��벻��Ϊ��");
			getTextFieldFunCode().grabFocus();
			return;
		} else {
			if (funCode.length() != funCode.getBytes().length) {
				showErrorMessage("��ֹ����˫�ֽ��ַ�");
				getTextFieldFunCode().grabFocus();
				return;
			}
			if (funCode.length() > 60) { //��ṹԽ��???������
				showErrorMessage("���ܱ��볤�Ȳ��ܴ���60��");
				getTextFieldFunCode().grabFocus();
				return;
			}
			if (funCode.length() != getTextFieldFunCode().getMaxLength()) { //???������
				showErrorMessage("���ݼ��ι��򣬸ù��ܱ���ĳ��ȱ���Ϊ" + getTextFieldFunCode().getMaxLength() + "λ��ǰ��ո񲻼ƣ�");
				getTextFieldFunCode().grabFocus();
				return;
			}
		}
		//��������
		String funName = getTextFieldFunName().getText().trim();
		if (funName == null || funName.length() <= 0) {
			showErrorMessage("�������Ʋ���Ϊ��");
			getTextFieldFunName().grabFocus();
			return;
		} else {
			if (getStrlength(funName) > 50) { //��ṹԽ��???������
				showErrorMessage("�������Ƴ��Ȳ��ܴ���50��");
				getTextFieldFunName().grabFocus();
				return;
			}
			//vo.setFunName(funName);
		}
		//��������
		if (getComboBoxProperty().getSelectedItem() == null) {
			showErrorMessage("�������ʲ���Ϊ��");
			return;
		}
		String strSelItem = getComboBoxProperty().getSelectedItem().toString();
		int iSelectedProp = getPropIndex(strSelItem);
		//��������
		if (isExecutableNode(iSelectedProp)
				&& getComboBoxPropType().getSelectedItem().toString().trim().equals("")) {
			showErrorMessage("��ִ�й��ܽڵ���������Ͳ���Ϊ��");
			return;
		}
		//��Ӧ������ؼ�����
		String className = getTextFieldClass().getText().trim();
		if (className == null || className.length() <= 0) {
			if (iSelectedProp != INEXECUTABLE_FUNC_NODE && iSelectedProp != POWERFUL_BUTTON
					&& iSelectedProp != POWERLESS_BUTTON) { //���鹦�ܽڵ㣬�ǰ�ť
				showErrorMessage("������ؼ����Ʋ���Ϊ��");
				getTextFieldClass().grabFocus();
				return;
			}
		} else {
			if (getStrlength(className) > 200) {
				showErrorMessage("������ؼ����Ƴ��Ȳ��ܴ���200");
				getTextFieldClass().grabFocus();
				return;
			}
			//vo.setClassName(className);
		}
		//��Ӧ�����ļ���
		String helpName = getTextFieldHelp().getText().trim();
		if (getStrlength(helpName) > 50) { //��ṹԽ��
			showErrorMessage("�����ļ������Ȳ��ܴ���50��");
			getTextFieldHelp().grabFocus();
			return;
		}
		//resid
		String strResid = getTextFieldResid().getText().trim();
		if (getStrlength(strResid) > 50) { //��ṹԽ��
			showErrorMessage("�Զ�����1 ���Ȳ��ܴ���50��");
			getTextFieldResid().grabFocus();
			return;
		}
		
		//��������
		String funDesc = getTextAreaDesc().getText().trim();
		if (funDesc != null) {
			if (getStrlength(funDesc) > 512) { //��ṹԽ��
				showErrorMessage("�����������ȳ��Ȳ��ܴ���512��");
				getTextAreaDesc().grabFocus();
				return;
			}
			//vo.setFunDesc(funDesc);
		}
		//���ģ������
		char cModuleType = FuncModuleJudger.getModuleType(funCode);
		try {
			if (m_adding) { //����״̬
				FuncRegisterVO vo = new FuncRegisterVO();
				//��������
				vo.setFunName(funName);
				//���ܱ���
				if (!getSelectedNode().isRoot()) {
					if (!funCode.substring(0, funCode.length() - LEVELLEN).equals(
							getSelectedNode().getNodeVo().getFunCode())) {
						showErrorMessage("���ܱ������̳и��ڵ㣡");
						getTextFieldFunCode().grabFocus();
						return;
					}
				}
				vo.setFunCode(funCode);
				//��������
				if (getComboBoxPropType().getSelectedItem().equals(""))
					vo.setGroupFlag(new Integer(-1));
				else
					vo.setGroupFlag(new Integer(getComboBoxPropType().getSelectedIndex()));
				//��������
				//int index = getComboBoxProperty().getSelectedIndex();
				vo.setFunProperty(new Integer(iSelectedProp));
				//��Ӧ������ؼ�����
				vo.setClassName(className);
				//��Ӧ�����ļ���
				vo.setHelpName(helpName);
				//resid
				vo.setResid(strResid);
				//���ñ�־
				if (getRadioForbidYes().isSelected())
					vo.setForbidFlag(new Integer("1"));
				else
					vo.setForbidFlag(new Integer("0"));
				//������ʾ��־
				if (getRadioShowParamYes().isSelected())
					vo.setHasPara(new UFBoolean(true));
				else
					vo.setHasPara(new UFBoolean(false));
				//NeedLog
				vo
						.setisneedbuttonlog(getCkbNeedButtonLog().isSelected() ? UFBoolean.TRUE
								: UFBoolean.FALSE);
				vo.setIsbuttonpower(getCkbButtonPower().isSelected() ? UFBoolean.TRUE : UFBoolean.FALSE);
				//��������
				vo.setFunDesc(funDesc);
				if (getSelectedNode().isRoot()) {
					vo.setFunLevel(new Integer(1)); //�ڵ㼶��
				} else {
					///
					FuncRegisterVO voParent = getSelectedNode().getNodeVo();
					int paLevel = voParent.getFunLevel().intValue();
					vo.setFunLevel(new Integer(paLevel + 1)); //�ڵ㼶��
					int iParentProp = voParent.getFunProperty().intValue();
					if (iParentProp == POWERFUL_BUTTON || iParentProp == POWERLESS_BUTTON)
						//���������ж�����ť������������ڵ�Ϊ��ť�������Ҹ��ڵ�Ϊ���ܽڵ�
						voParent = ((FunRegNode) getSelectedNode().getParent()).getNodeVo();
					String paID = voParent.getPrimaryKey();
					vo.setParentId(paID); //���ڵ�ID
					String subSysID = voParent.getSubsystemId();
					if (subSysID != null && subSysID.trim().length() == 20) {
						vo.setSubsystemId(subSysID); //��ϵͳID
					}
				}
				//ϵͳ���ܱ�־����ʱĬ��Ϊ1
				vo.setSystemFlag(new Integer(1));
				//����ʾ����
				vo.setDispCode(funCode);
				//����ģ������
				vo.setModuleType(cModuleType);
				//zsb+����֯����
				Object oOrg = getOrgnizeTypeCombo().getSelectedItem();
				String sOrgTypeCode = null;
				if (oOrg != null) {
					sOrgTypeCode = ((OrgnizeTypeVO) oOrg).getTypecode();
				}
				vo.setOrgTypecode(sOrgTypeCode);
				//ִ�в���
				String newOID = SFServiceFacility.getFuncRegisterService().insert(vo);
				if (newOID.equals("1")) {
					showErrorMessage("���ܱ���Ϊ" + funCode + "�Ľڵ��Ѵ��ڣ����޸ģ�");
					getTextFieldFunCode().grabFocus();
					return;
				}
				//������ؽ��ñ�־
				if (!getRadioForbidYes().isSelected() && !getSelectedNode().isRoot()) { //ע����ʱgetSelectedNode()�������ڵ�ĸ��ڵ�
					setAllParentsEnabled(getSelectedNode()); //ȫ�����Ƚڵ����
					SFServiceFacility.getFuncRegisterService().updateAllParentsEnabled(funCode, LEVELLEN); //�������Ƚڵ���ñ�־
				}
				//���������������
				if (getComboBoxPropType().getSelectedIndex() <= 3 && !getSelectedNode().isRoot()) { //ע����ʱgetSelectedNode()�������ڵ�ĸ��ڵ�
					setPropTypeOfAllParents(getSelectedNode()); //����ȫ�����Ƚڵ���������ΪA1
					SFServiceFacility.getFuncRegisterService().updatePropTypeOfAllParents(funCode, LEVELLEN);
					//�������Ƚڵ���������ΪA1
				}
				//�������
				vo.setPrimaryKey(newOID);
				if (getSelectedNode().isRoot())
					vo.setSubsystemId(newOID);
				reloadTree(vo);
				showHintMessage("���ӳɹ�");
				//�����༭̬
				stopEditting();
				setButtonEnabled(m_boUpdate, true);
				setButtonEnabled(m_boDel, true);
				//ѡ�������ڵ�
				TreeNode tn = (TreeNode) m_selPath.getLastPathComponent();
				if (tn.getChildCount() > 0) {
					//����·��
					DefaultMutableTreeNode tnChild = (DefaultMutableTreeNode) tn.getChildAt(tn
							.getChildCount() - 1);
					Object[] objPaths = (Object[]) tnChild.getPath();
					m_selPath = new TreePath(objPaths);
					//ѡ�������ڵ�
					//m_funTree.removeTreeSelectionListener(this);
					setSelPath(m_selPath);
					//m_funTree.addTreeSelectionListener(this);
				}
			} else { //�޸�״̬
				if (getSelectedNode().isRoot()) {
					showErrorMessage("�����޸ĸ��ڵ�!");
					return;
				}
				FuncRegisterVO vo = getSelectedNode().getNodeVo();

				voBack = (FuncRegisterVO) vo.clone();
				//��������
				vo.setFunName(funName);
				//���ܱ���
				if (getSelectedNode().isLeaf()) {
					if (!((FunRegNode) getSelectedNode().getParent()).isRoot()) {
						if (!funCode.substring(0, funCode.length() - LEVELLEN).equals(
								((FunRegNode) getSelectedNode().getParent()).getNodeVo().getFunCode())) {
							showErrorMessage("���ܱ������̳и��ڵ㣡");
							getTextFieldFunCode().grabFocus();
							return;
						}
					}
					vo.setFunCode(funCode);
				}
				//��������
				vo.setGroupFlag(new Integer(getComboBoxPropType().getSelectedIndex()));
				//��������
				vo.setFunProperty(new Integer(iSelectedProp));
				//��Ӧ������ؼ�����
				vo.setClassName(className);
				//��Ӧ�����ļ���
				vo.setHelpName(helpName);
				//resid
				vo.setResid(strResid);
				//���ñ�־
				if (getRadioForbidYes().isSelected())
					vo.setForbidFlag(new Integer("1"));
				else
					vo.setForbidFlag(new Integer("0"));
				//������ʾ��־
				if (getRadioShowParamYes().isSelected())
					vo.setHasPara(new UFBoolean(true));
				else
					vo.setHasPara(new UFBoolean(false));
				//��������
				vo.setFunDesc(funDesc);
				//���Ʊ�ע�͵�2005-5-31:�޸Ľڵ�ʱ�����޸���dispcode
				//			//����ʾ����
				//			vo.setDispCode(funCode);

				//			NeedLog
				vo
						.setisneedbuttonlog(getCkbNeedButtonLog().isSelected() ? UFBoolean.TRUE
								: UFBoolean.FALSE);
				vo.setIsbuttonpower(getCkbButtonPower().isSelected() ? UFBoolean.TRUE : UFBoolean.FALSE);
				//����ģ������
				vo.setModuleType(cModuleType);
				//zsb+����֯����
				Object oOrg = getOrgnizeTypeCombo().getSelectedItem();
				String sOrgTypeCode = null;
				if (oOrg != null) {
					sOrgTypeCode = ((OrgnizeTypeVO) oOrg).getTypecode();
				}
				vo.setOrgTypecode(sOrgTypeCode);
				//ִ�и���
				int flag = SFServiceFacility.getFuncRegisterService().update(vo);
				if (flag == 2) {
					showErrorMessage("�ù��ܽڵ��Ѳ����ڣ���ˢ�º�����!");
					return;
				} else if (flag == 1) {
					showErrorMessage("���ܱ���Ϊ" + funCode + "�Ľڵ��Ѵ��ڣ����޸ģ�");
					getTextFieldFunCode().grabFocus();
					return;
				}
				//������ؽ��ñ�־
				if (getRadioForbidYes().isSelected() && !m_bOldForbidFlag) {
					if (!getSelectedNode().isLeaf()) {
						setAllChildrenForbidden(getSelectedNode()); //ȫ������ڵ����
						SFServiceFacility.getFuncRegisterService().updateAllChildrenForbidden(funCode); //��������ڵ���ñ�־
					}
				} else if (!getRadioForbidYes().isSelected() && m_bOldForbidFlag) {
					setAllChildrenEnabled(getSelectedNode()); //ȫ������ڵ���ã����ĻԳ�����
					//setSomeChildrenEnabled(getSelectedNode()); //��������ڵ���ã����������
					if (getSelectedNode().getLevel() > 1)
						setAllParentsEnabled(getSelectedNode()); //ȫ�����Ƚڵ����
					SFServiceFacility.getFuncRegisterService().updateAllParentsEnabled(funCode, LEVELLEN); //�������Ƚڵ���ñ�־
				}
				//���������������
				if (getComboBoxPropType().getSelectedIndex() <= 3) {
					setPropTypeOfAllParents(getSelectedNode()); //����ȫ�����Ƚڵ���������ΪA1
					SFServiceFacility.getFuncRegisterService().updatePropTypeOfAllParents(funCode, LEVELLEN);
					//�������Ƚڵ���������ΪA1
				}
				//zsb+:������֯���Ͳ�����
				getOrgnizeTypeCombo().setEnabled(false);
				//�������
				getSelectedNode().setUserObject(vo.getFunCode() + "  " + vo.getFunName());
				((DefaultTreeModel) (m_funTree.getModel())).reload(getSelectedNode());
				//repaint();
				showHintMessage("�޸ĳɹ�");
				//���ù����������������״̬
				//getComboBoxProperty().setEnabled(true);
				//�����༭̬
				stopEditting();
				setButtonEnabled(m_boUpdate, true);
				setButtonEnabled(m_boDel, true);
			}
			m_haveSaveFlag = true;
		} catch (Exception ex) {
			System.out.println(ex);
			if (m_adding)
				showErrorMessage("����ʧ��");
			else {
				getSelectedNode().setNodeVo(voBack);
				showErrorMessage("�޸�ʧ��");
			}
		}
		//�������Ӱ�ť����״̬��zjb��
		if (isExecutableNode(iSelectedProp) || iSelectedProp == INEXECUTABLE_FUNC_NODE)
			setButtonEnabled(m_boAdd, true);
		else {
			//zsb+��ֻ�зǲ�����һ����ť�������Ӷ�����ť
			//��ø��ڵ㹦������
			int iParentPropIndex = ((FunRegNode) getSelectedNode().getParent()).getNodeVo()
					.getFunProperty().intValue();
			boolean bAddEnable = (iSelectedProp != PARAMETER) && (isExecutableNode(iParentPropIndex));
			setButtonEnabled(m_boAdd, bAddEnable);
		}
		getCkbNeedButtonLog().setEnabled(false);
		getCkbButtonPower().setEnabled(false);
	}

	/**
	 * �޸�
	 * �������ڣ�(2001-3-29 15:00:41)
	 */
	public void onBoUpdate() {
		if (m_funTree.getSelectionPath() == null) {
			showErrorMessage("����ѡ��Ҫ�޸ĵĽڵ㣡");
			return;
		}
		try {
			//���ñ༭̬
			setEditState(true);
			m_bEdit = true;

			//��ʾ��Ӧ�ڵ���Ϣ
			if (getSelectedNode().isRoot())
				displayRootInfo(); //���ڵ���Ϣ
			else
				displayNodeInfo(); //�Ǹ��ڵ���Ϣ
			//���ð�ť����״̬
			setButtonEnabled(m_boAdd, false);
			setButtonEnabled(m_boUpdate, false);
			setButtonEnabled(m_boDel, false);
			setButtonEnabled(m_boSave, true);
			setButtonEnabled(m_boCancel, true);

			//���ñ�־
			m_bOldForbidFlag = getRadioForbidYes().isSelected();
			m_haveSaveFlag = false;
			m_adding = false;
			if (!getSelectedNode().isLeaf()) {
				//�������ܱ����ı���
				getTextFieldFunCode().setEnabled(false);
				//����������
				getComboBoxProperty().setEnabled(false); //��֤�����ӽڵ���ǰ�ڵ㹦�����Բ��ɸĶ�
			}
			String strPropType = getComboBoxProperty().getSelectedItem().toString();
			int iSelectedProp = getPropIndex(strPropType);
			//�����鹦�ܽڵ㣬��Ӧ�ļ�����ؼ����ı��򲻿���
			if (iSelectedProp == INEXECUTABLE_FUNC_NODE)
				getTextFieldClass().setEnabled(false);

			//���ڰ�ť����������Ӧ�����ļ���������,zsb update�����ڰ�ť�ж�����ť����ť���߲����Ĺ������ʿ����޸�
			int iFuncIndex = getSelectedNode().getNodeVo().getFunProperty().intValue();
			if (iFuncIndex == POWERFUL_BUTTON || iFuncIndex == POWERLESS_BUTTON
					|| iFuncIndex == PARAMETER) {
				getTextFieldHelp().setEnabled(false);
				getComboBoxProperty().setEnabled(true);
			}
			//���ڿ�ִ�й��ܽڵ㣬����������������ã����򲻿��� zsb+:��֯����Ҳһ��

			if (isExecutableNode(iSelectedProp)) {
				getOrgnizeTypeCombo().setEnabled(true);
				getComboBoxPropType().setEnabled(true);
			} else {
				getOrgnizeTypeCombo().setEnabled(false);
				getComboBoxPropType().setEnabled(false);
			}
			//��ʾ
			showHintMessage("�޸�״̬");
		} catch (Exception ex) {
			handleException(ex);
		}
	}

	/**
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
	 * @version (00-6-1 10:32:59)
	 * 
	 * @param bo ButtonObject
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		if (bo == m_boAdd) {
			onBoAdd();
		} else if (bo == m_boDel) {
			onBoDel();
		} else if (bo == m_boUpdate) {
			onBoUpdate();
		} else if (bo == m_boSave) {
			onBoSave();
		} else if (bo == m_boRefresh) {
			m_loading = true;
			m_selPath = null;
			m_bEdit = false;
			initTree();
		} else if (bo == m_boCancel) {
			onBoCancel();
		} else if (bo == m_boCheck) {
			onCheck();
		} else if (bo == m_boHotkeyReg) {
			onHotkeyReg();
		} else if (bo == m_boDefaultHotkey) {
			onSetDefaultHotkey();
		}
	}

	/**
	 * ����У��
	 * �������ڣ�(2003-2-20 15:23:05)
	 */
	public void onCheck() {
		FuncCheckDlg dlg = new FuncCheckDlg(this);
		dlg.showModal();
		dlg.destroy();
	}

	/**
	 * ��ݼ�ע��Ի��򵯳���
	 ��û�������棿����
	 * �������ڣ�(2003-11-7 13:26:58)
	 * @author���׾�
	 */
	public void onHotkeyReg() {
		//�Ȼ�ȡ����ִ�й��ܽڵ㡱�����а�ť�����û��ע�ᰴť������ʾ��ݼ�ע��Ի���
		//����ִ�й��ܽڵ㡱��ֻ�����а�ť
		int childCount = m_selectedNode.getChildCount();
		//System.out.println("### childCount = " + childCount);
		if (childCount == 0)
			return;

		//�ҵ�����ִ�й��ܽڵ㡱�����а�ť�����п�ݼ�
		FuncRegisterVO execNodeVO = m_selectedNode.getNodeVo();

		//�ȴӻ����л�ȡ������ṹΪfunCode -  HotkeyRegisterVO[]��
		HotkeyRegisterVO[] hotkeys = null;
		//(HotkeyRegisterVO[]) m_hashBtn4Hotkey.get(execNodeVO.getFunCode());
		if (hotkeys == null) {
			//���������û�У�������ݿ�ȡ
			try {
				hotkeys = SFAppServiceUtil.getHotKeyRegisterQueryService().queryAllHotkeyRegisterVOs(
						execNodeVO.getFunCode());
			} catch (Exception ex) {
				handleException(ex);
			}

			if (hotkeys != null) {
				//���뵽���棨�������ݲ������� by �׾� 2003-11-7��
				//m_hashBtn4Hotkey.put(execNodeVO.getFunCode(), hotkeys);
			}
		}

		HotkeyEntry he = null;
		HotkeyRegisterVO matchedHotkey = null;
		Vector hotkeyEntrys4Btn = new Vector();
		for (int i = 0; i < childCount; i++) {
			FuncRegisterVO vo = ((FunRegNode) m_selectedNode.getChildAt(i)).getNodeVo();
			he = new HotkeyEntry();
			he.setStrBtnName(vo.getFunName());
			he.setStrBtnCode(vo.getClassName()); //lj+

			//���ݰ�ť����������ƥ�����ݼ� lj@
			//���Ʊ�2005-6-25�����ݰ�ť����fun_nameƥ�����ݼ���fun_nameΪ��ť���룬class_nameΪ��ť���ƣ�
			matchedHotkey = getMatchedHotkey(vo.getFunName(), hotkeys);//vo.getClassName(), hotkeys);
			if (matchedHotkey == null) {
				he.setNewCreated(true);
				he.setStrDisplayName("");
				he.setStrFunKeyType("");
				he.setStrKeyName("");
			} else {
				he.setNewCreated(false);
				he.setStrDisplayName(matchedHotkey.getDisplay());
				Integer iType = matchedHotkey.getAlt_meta_ctrl_shift();
				he.setStrFunKeyType(iType == null ? "0" : HotkeyEntry.getTypeByValue(iType.intValue()));
				he.setStrKeyName(matchedHotkey.getHotkey());
			}

			//��䵽ʸ����
			hotkeyEntrys4Btn.addElement(he);
		}

		/////// add by �׾� 12/11/2003 ///////
		// ����
		Collections.sort(hotkeyEntrys4Btn, new Comparator() {
			public int compare(Object o1, Object o2) {
				String str1 = ((HotkeyEntry) o1).getStrKeyName();
				String str2 = ((HotkeyEntry) o2).getStrKeyName();
				return str1.compareToIgnoreCase(str2);
			}
		});
		/////// add by �׾� 12/11/2003 ///////	

		//�����Ի���
		HotkeyRegDlg hotkeyRegDlg = new HotkeyRegDlg(this, hotkeyEntrys4Btn);
		//��ʼ�����
		hotkeyRegDlg.setColumnNames(new String[] { "��ť����", "��ť����", "���ܼ�", "����", "��ʾ" });
		hotkeyRegDlg.initTable();
		//��ʾ
		hotkeyRegDlg.showModal();
		hotkeyRegDlg.destroy();
		if (hotkeyRegDlg.getResult() == UIDialog.ID_OK) {
			//System.out.println("### OKOK!");

			//�����²���Ŀ�ݼ����Ǹ���ԭ�п�ݼ�
			try {
				saveOrUpdateHotkeys(execNodeVO.getFunCode(), hotkeyEntrys4Btn, hotkeys);
			} catch (Exception ex) {
				handleException(ex);
			}
		}
	}

	/**
	 * �ָ����а�ť��Ĭ�Ͽ�ݼ�
	 * �������ڣ�(2003-11-10 10:44:12)
	 * @�޸��ˣ��׾� 2003-12-2 ������ʾ���û�ѡ��
	 */
	public void onSetDefaultHotkey() {
		//lj+
		if (MessageDialog.showOkCancelDlg(this, "Ĭ��ֵȷ��", "ȷʵҪ�ָ���ť��ݼ���Ĭ��ֵ��") == MessageDialog.ID_OK) {
			try {
				//�������Ϸ��� 
				//			SFServiceFacility.getHotkeyRegisterService().updateDefaultHotKey();
				SFAppServiceUtil.getHotKeyRegisterService().updateDefaultHotKey();
			} catch (Exception ex) {
				handleException(ex);
			}
			showHintMessage("���п�ݼ��ָ�Ĭ��ֵ���");
		}
	}

	/**
	 * ��ϰ�ťע��Ի��򵯳���
	 *   �������Ʊ��ĶԻ���
	 * �������ڣ�(2004-7-9 13:26:58)
	 * @author���׾�
	 */
	//public void onUnionFunc() {
	//	//�Ȼ�ȡ����ִ�й��ܽڵ㡱�����а�ť�����û��ע�ᰴť������ʾ��ݼ�ע��Ի���
	//	//����ִ�й��ܽڵ㡱��ֻ�����а�ť
	//	int childCount = m_selectedNode.getChildCount();
	//	//System.out.println("### childCount = " + childCount);
	//	if (childCount == 0)
	//		return;
	//
	//	//�ҵ�����ִ�й��ܽڵ㡱�����а�ť
	//	FuncRegisterVO execNodeVO = m_selectedNode.getNodeVo();
	//
	//	ArrayList alBtns = new ArrayList();
	//	for (int i = 0; i < childCount; i++) {
	//		FuncRegisterVO vo = ((FunRegNode) m_selectedNode.getChildAt(i)).getNodeVo();
	//		alBtns.add(vo);
	//	}
	//
	//	//�����Ի���
	//	RegisterUnionFuncDlg unionfuncRegDlg = new RegisterUnionFuncDlg(this);
	//	unionfuncRegDlg
	//		.loadData(execNodeVO, (FuncRegisterVO[]) alBtns.toArray(new FuncRegisterVO[] {
	//	}));
	//
	//	//��ʾ
	//	unionfuncRegDlg.showModal();
	//	unionfuncRegDlg.destroy();
	//}
	/**
	 * ˢ����
	 * �������ڣ�(2001-3-29 18:40:04)
	 * @param vo nc.vo.sm.zs.funcreg.FuncRegisterVO
	 */
	public void reloadTree(FuncRegisterVO vo) {
		getSelectedNode().add(new FunRegNode(vo));
		((DefaultTreeModel) (m_funTree.getModel())).reload(getSelectedNode());
	}

	/**
	 * �������¿�ݼ�
	 * �������ڣ�(2003-11-7 18:48:03)
	 * @param vecHotkeyEntries
	 * @param hotkeys  �á���ִ�нڵ㡱  ��ԭ�п�ݼ�
	 */
	private void saveOrUpdateHotkeys(String execFunCode, Vector vecHotkeyEntries,
			HotkeyRegisterVO[] hotkeys) throws Exception {
		Enumeration enums = vecHotkeyEntries.elements();
		Vector vecNewCreated = new Vector();
		HotkeyRegisterVO oldHotkey = null;
		HotkeyRegisterVO newHotkey = null;
		int iUpdateRecords = 0;

		//�������ж��Ƿ�Ϊ�¼ӿ�ݼ�
		while (enums.hasMoreElements()) {
			HotkeyEntry entry = (HotkeyEntry) enums.nextElement();

			//���û�н�ֵ,�򲻱��� leijun 2006-8-28
			String key = entry.getStrKeyName();
			if (key == null || key.length() == 0)
				continue;

			if (entry.isNewCreated()) {
				newHotkey = new HotkeyRegisterVO();
				newHotkey.setFun_code(execFunCode);
				newHotkey.setButton_name(entry.getStrBtnName());
				newHotkey.setHotkey(entry.getStrKeyName());
				newHotkey.setAlt_meta_ctrl_shift(HotkeyEntry.getValueByType(entry.getStrFunKeyType()));
				newHotkey.setDisplay(entry.getStrDisplayName());

				vecNewCreated.addElement(newHotkey);
			} else if (entry.isChanged()) {
				oldHotkey = getMatchedHotkey(entry.getStrBtnName(), hotkeys);
				oldHotkey.setHotkey(entry.getStrKeyName());
				oldHotkey.setAlt_meta_ctrl_shift(HotkeyEntry.getValueByType(entry.getStrFunKeyType()));
				oldHotkey.setDisplay(entry.getStrDisplayName());
				SFAppServiceUtil.getHotKeyRegisterService().update(oldHotkey);
				iUpdateRecords++;
			}
		}
		if (vecNewCreated.size() != 0)
			SFAppServiceUtil.getHotKeyRegisterService().insertArray(
					(HotkeyRegisterVO[]) vecNewCreated.toArray(new HotkeyRegisterVO[] {}));

		//���������Ϣ
		System.out.println("�����ӿ�ݼ����� = " + vecNewCreated.size());
		System.out.println("���¿�ݼ����� = " + iUpdateRecords);
	}

	/**
	 * ����ȫ������ڵ�Ľ��ñ�־Ϊ��(�ݹ�)
	 * �������ڣ�(2001-4-4 16:21:17)
	 * @param b boolean
	 */
	public void setAllChildrenEnabled(FunRegNode parent) {
		if (parent != null)
			for (int i = 0; i < parent.getChildCount(); i++) {
				((FunRegNode) parent.getChildAt(i)).getNodeVo().setForbidFlag(new Integer(0));
				setAllChildrenEnabled((FunRegNode) parent.getChildAt(i));
			}
	}

	/**
	 * ����ȫ������ڵ�Ľ��ñ�־Ϊ��(�ݹ�)
	 * �������ڣ�(2001-4-4 16:21:17)
	 * @param b boolean
	 */
	public void setAllChildrenForbidden(FunRegNode parent) {
		if (parent != null)
			for (int i = 0; i < parent.getChildCount(); i++) {
				((FunRegNode) parent.getChildAt(i)).getNodeVo().setForbidFlag(new Integer(1));
				setAllChildrenForbidden((FunRegNode) parent.getChildAt(i));
			}
	}

	/**
	 * ����ȫ�����Ƚڵ�Ľ��ñ�־Ϊ��
	 * �������ڣ�(2001-4-4 16:21:17)
	 * @param b boolean
	 */
	public void setAllParentsEnabled(FunRegNode child) {
		if (child != null) {
			int iLevel = child.getLevel();
			for (int i = 0; i < iLevel; i++) {
				child.getNodeVo().setForbidFlag(new Integer(0));
				child = (FunRegNode) child.getParent();
			}
		}
	}

	/**
	 * �ı䰴ť����״̬
	 * �������ڣ�(00-11-28 10:25:58)
	 * @param bo ierp.sm.core.ui.ButtonObject
	 * @param bEnabled boolean
	 */
	public void setButtonEnabled(ButtonObject bo, boolean bEnabled) {
		bo.setEnabled(bEnabled);
		updateButton(bo);
	}

	/**
	 * ���ñ༭̬
	 * �������ڣ�(2001-3-29 20:39:38)
	 * @param flag boolean
	 */
	public void setEditState(boolean flag) {
		getTextFieldFunCode().setEnabled(flag); //�������ϵͳ�Զ�����
		getTextFieldFunName().setEnabled(flag);
		getTextFieldClass().setEnabled(flag);
		getTextFieldHelp().setEnabled(flag);
		getTextFieldResid().setEnabled(flag);
		getComboBoxProperty().setEnabled(flag);
		getComboBoxPropType().setEnabled(flag);
		getRadioForbidYes().setEnabled(flag);
		getRadioForbidNo().setEnabled(flag);
		getRadioShowParamYes().setEnabled(flag);
		getRadioShowParamNo().setEnabled(flag);
		getTextAreaDesc().setEnabled(flag);

	}

	/**
	 * ����ȫ�����Ƚڵ����������ΪA
	 * �������ڣ�(2001-4-4 16:21:17)
	 * @param b boolean
	 */
	public void setPropTypeOfAllParents(FunRegNode child) {
		if (child != null) {
			int iLevel = child.getLevel();
			for (int i = 0; i < iLevel; i++) {
				if (i > 0)
					child.getNodeVo().setGroupFlag(new Integer(0));
				child = (FunRegNode) child.getParent();
			}
		}
		return;
	}

	/**
	 * ����ѡ�нڵ�
	 * �������ڣ�(2001-3-29 14:36:08)
	 * @param node com.netscape.javascript.FunctionNode
	 */
	public void setSelectedNode(FunRegNode node) {
		m_selectedNode = node;
	}

	/**
	 * ѡ����·����ʹ��ɼ�
	 * �������ڣ�(01-6-13 12:44:15)
	 * @param path tree.TreePath
	 */
	public void setSelPath(TreePath selPath) {
		final TreePath path = selPath;
		m_funTree.setSelectionPath(path);
		Runnable run = new Runnable() {
			public void run() {
				m_funTree.scrollPathToVisible(path);
			}
		};
		SwingUtilities.invokeLater(run);
	}

	/**
	 * �����ӽڵ��еĿ�ִ�й��ܽڵ㼰���Ӱ�ť�Ľ��ñ�־Ϊ��
	 * �������ڣ�(2001-4-4 16:21:17)
	 * @param b boolean
	 */
	public void setSomeChildrenEnabled(FunRegNode parent) {
		if (parent != null)
			for (int i = 0; i < parent.getChildCount(); i++) {
				FunRegNode child = (FunRegNode) parent.getChildAt(i);
				int iParentProp = parent.getNodeVo().getFunProperty().intValue();
				int iChildProp = child.getNodeVo().getFunProperty().intValue();
				if (isExecutableNode(iParentProp))
					child.getNodeVo().setForbidFlag(new Integer(0));
				else if (isExecutableNode(iChildProp)) {
					child.getNodeVo().setForbidFlag(new Integer(0));
					for (int j = 0; j < child.getChildCount(); j++)
						((FunRegNode) child.getChildAt(j)).getNodeVo().setForbidFlag(new Integer(0));
				}
			}
	}

	private boolean isExecutableNode(int iProp) {
		if (iProp == EXECUTABLE_FUNC_NODE || iProp == EXECUTABLE_FUNC_FRAME || iProp == LFW_FUNC_NODE)
			return true;
		return false;
	}

	/**
	 * �����༭̬ʱ������еĹ���
	 * �������ڣ�(01-5-21 14:05:12)
	 */
	public void stopEditting() {
		//���ð�ť����״̬
		setButtonEnabled(m_boSave, false);
		setButtonEnabled(m_boCancel, false);
		//���ñ༭̬
		setEditState(false);
		m_bEdit = false;
		//������ʾ
		showHintMessage("");
	}

	/**
	 * ��ѡȡ�ı��¼���Ӧ
	 * �������ڣ�(2001-3-29 14:32:34)
	 * @param ev TreeSelectionEvent
	 */
	public void valueChanged(TreeSelectionEvent ev) {
		if (m_loading)
			return;
		TreePath path = ev.getNewLeadSelectionPath();
		if (path == null)
			return;
		if (m_bEdit && m_selPath != null && !m_selPath.equals(path)) {
			Toolkit.getDefaultToolkit().beep();
			//ѡ��ԭ·������ǰ�༭�ڵ�·����
			m_funTree.removeTreeSelectionListener(this);
			setSelPath(m_selPath);
			m_funTree.addTreeSelectionListener(this);
		} else {
			m_selPath = path;
			FunRegNode node = (FunRegNode) m_selPath.getLastPathComponent();
			if (node == null)
				return;
			setSelectedNode(node);
			if (getSelectedNode().isRoot()) {
				setButtonEnabled(m_boAdd, true);
				setButtonEnabled(m_boUpdate, false);
				setButtonEnabled(m_boDel, false);
				displayRootInfo(); //���ڵ���Ϣ
			} else {
				setButtonEnabled(m_boUpdate, true);
				setButtonEnabled(m_boDel, true);
				displayNodeInfo(); //�Ǹ��ڵ���Ϣ
				//���Ʊ� update
				if (node.getNodeVo().getPrimaryKey() == null) {
					setButtonEnabled(m_boAdd, false);
					setButtonEnabled(m_boUpdate, false);
					setButtonEnabled(m_boDel, false);
					MessageDialog.showErrorDlg(this, "����", "�ýڵ���Ϣ��ʧ,���޸�");
				}
			}
			int iProp = getSelectedNode().getNodeVo().getFunProperty().intValue();
			//���ÿ�ݼ���ť����ϰ�ť�Ŀ���״̬ lj+ 2003-11-7
			if (isExecutableNode(iProp)) {
				//��Ϊֻ�С���ִ�й��ܽڵ㡱������а�ť�����ԡ�����
				setButtonEnabled(m_boHotkeyReg, true);
			} else {
				setButtonEnabled(m_boHotkeyReg, false);
			}
		}
	}
}
