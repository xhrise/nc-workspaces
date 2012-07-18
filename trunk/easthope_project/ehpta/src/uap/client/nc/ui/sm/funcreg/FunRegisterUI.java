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
 * 功能注册
 * @author 张森 2001-3-28
 * @modifier zjb
 * @modifier leijun 2003-7-22 修改A1、A2、B1等具体含义显示
 * @modifier leijun 2003-11-7 增加按钮的快捷键注册功能 暂时没做缓存？？？
 * @modifier leijun 2003-11-10 再增加一个“默认快捷键”按钮，为数据库中所有按钮恢复默认快捷键
 * @modifier leijun 2004-07-09 增加组合按钮注册功能 
 * @modifier leijun 2007-10-12 使用GroupLayout重新布局
 * @modifier leijun 2007-12-6 可执行功能点增加属性：是否启用按钮权限控制
 */
public class FunRegisterUI extends ToftPanel implements TreeSelectionListener, FunRegisterConst,
		ItemListener {
	public final static String[] FUNC_NODE = new String[] { "可执行功能节点", "虚功能节点", "待分配权限按钮", "默认有权限按钮",
			"参数", "可执行功能帧", "轻量级Web节点" };

	public final static String[] PROP_TYPE = new String[] { "集团完全控制，公司查看", "集团独享控制", "集团部分控制，公司拷贝",
			"集团部分控制，公司共享", "公司私有控制", "公司私有控制的库存组织数据" };

	//zsb+:增加组织类型组合框和标签
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

	//private ButtonObject m_boUnionFunc = new ButtonObject("组合按钮", "组合按钮", 0);

	private UIPanel ivjPnDetail = null;

	private UIPanel ivjPnWest = null;

	private UIPanel ivjPnWestEast = null;

	private UIPanel ivjPnWestNorth = null;

	private UIPanel ivjPnWestSouth = null;

	//public final static String[] PROP_TYPE =
	//new String[] { "A1", "A2", "B1", "B2", "C", "D" }; //在库里分别对应0，1，2，3，4，5（字段group_flag）

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

	private boolean m_adding = true; //增加标志

	private boolean m_bEdit = false; //编辑状态标志

	//按钮
	private ButtonObject m_boAdd = new ButtonObject("增加", "增加", 0, "增加");

	private ButtonObject m_boCancel = new ButtonObject("取消", "取消", 0, "取消");

	private ButtonObject m_boCheck = new ButtonObject("辅助校验", "辅助校验", 0, "辅助校验");

	private ButtonObject m_boDefaultHotkey = new ButtonObject("恢复默认快捷键", "恢复所有按钮快捷键默认值", 0, "恢复默认快捷键");

	private ButtonObject m_boDel = new ButtonObject("删除", "删除", 0, "删除");

	private ButtonObject m_boHotkey = new ButtonObject("快捷键", "快捷键", 0, "快捷键");

	//lj+ 2003-11-10 做成子菜单样式
	private ButtonObject m_boHotkeyReg = new ButtonObject("快捷键注册", "为功能节点下的按钮注册快捷键", 0, "快捷键注册");

	private boolean m_bOldForbidFlag = false; //修改前保存原来的启用标志

	private ButtonObject m_boRefresh = new ButtonObject("刷新", "刷新", 0, "刷新");

	private ButtonObject m_boSave = new ButtonObject("保存", "保存", 0, "保存");

	private ButtonObject m_boUpdate = new ButtonObject("修改", "修改", 0, "修改");

	private UICheckBox m_ckbNeedButtonLog = null;

	private UITree m_funTree = null; //功能权限注册树

	//lj+ 缓存用
	Hashtable m_hashBtn4Hotkey = new Hashtable();

	private boolean m_haveSaveFlag = true; //已保存标志

	//状态标志
	private boolean m_loading = true; //加载标志

	//按钮组
	private ButtonObject[] m_MainButtonGroup = { m_boAdd, m_boUpdate, m_boDel, m_boSave, m_boCancel,
			m_boRefresh, m_boCheck, m_boHotkey };

	private FunRegNode m_selectedNode = null; //选中节点

	private TreePath m_selPath = null; //当前选中路径

	private UILabel ivjLabelButtonLog;

	private UICheckBox m_ckbButtonPower;

	private UILabel ivjLabelButtonPower;

	/**
	 * FunRegisterUI 构造子注解。
	 */
	public FunRegisterUI() {
		super();
		initialize();
	}

	/**
	 * 功能性质下拉框选项改变事件响应
	 */
	public void comboBoxProperty_ItemStateChanged(ItemEvent itemEvent) {
		if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
			String strSelItem = getComboBoxProperty().getSelectedItem().toString(); //选中项
			int iSelectedProp = getPropIndex(strSelItem);

			//对于虚功能节点，对应控件名文本域不可用，否则可用
			if (iSelectedProp == INEXECUTABLE_FUNC_NODE) {
				getTextFieldClass().setText("");
				getTextFieldClass().setEnabled(false);
			} else
				getTextFieldClass().setEnabled(true);
			//对于可执行功能节点，属性类型下拉框可用，否则不可用
			if (isExecutableNode(iSelectedProp)) {
				getComboBoxPropType().setEnabled(true);
				getOrgnizeTypeCombo().setEnabled(true);
				getOrgnizeTypeCombo().setSelectedIndex(0);
			} else {
				getComboBoxPropType().setSelectedItem(" ");
				getOrgnizeTypeCombo().setEnabled(false);
				getOrgnizeTypeCombo().setSelectedItem(null);
			}
			//对于参数，对应控件名标签改名
			if (iSelectedProp == PARAMETER) {
				getLabelName().setText("参 数 编 码");
				getLabelClassName().setText("参数值");
			} else if (iSelectedProp == POWERFUL_BUTTON || iSelectedProp == POWERLESS_BUTTON) {
				getLabelName().setText("按 钮 编 码");
				getLabelClassName().setText("按 钮 名 称");
			} else
				getLabelClassName().setText("对应文件名或控件名");
		}
		return;
	}

	/**
	 * 显示节点对应明细
	 * 创建日期：(2001-3-29 20:11:17)
	 */
	public void displayNodeInfo() {
		FuncRegisterVO fr = getSelectedNode().getNodeVo();
		//功能编码
		String funCode = fr.getFunCode();
		if (funCode != null) {
			getTextFieldFunCode().setMaxLength(fr.getFunCode().trim().length());
			getTextFieldFunCode().setText(funCode);
		}
		//功能名称
		String funName = fr.getFunName();
		if (funName != null)
			getTextFieldFunName().setText(funName);
		//属性类型
		int iProp = fr.getFunProperty().intValue();
		if (isExecutableNode(iProp) || iProp == INEXECUTABLE_FUNC_NODE)
			//周善保 update
			getComboBoxPropType().setSelectedIndex(
					fr.getGroupFlag() == null ? -1 : fr.getGroupFlag().intValue());
		//getComboBoxPropType().setSelectedIndex(fr.getGroupFlag().intValue());
		else
			getComboBoxPropType().setSelectedItem(" ");
		//功能性质
		int index = fr.getFunProperty().intValue();
		if (index == PARAMETER) {
			getLabelName().setText("参 数 编 码");
			getLabelClassName().setText("参数值");
		} else if (index == POWERFUL_BUTTON || index == POWERLESS_BUTTON) {
			getLabelName().setText("按 钮 编 码");
			getLabelClassName().setText("按 钮 名 称");
		} else {
			getLabelName().setText("功 能 名 称");
			getLabelClassName().setText("对应文件名或控件名");
		}
		//获得父节点功能性质
		int iParentPropIndex = -1;
		TreeNode node = getSelectedNode().getParent();
		if (node != null)
			iParentPropIndex = ((FunRegNode) node).getNodeVo().getFunProperty().intValue();
		//设置增加按钮可用状态（zjb）
		if (isExecutableNode(index) || index == INEXECUTABLE_FUNC_NODE) { //功能节点
			setButtonEnabled(m_boAdd, true);
		} else { //
			//zsb+：只有非参数的一级按钮可以增加二级按钮
			boolean bAddEnable = (index != PARAMETER) && (isExecutableNode(iParentPropIndex));
			setButtonEnabled(m_boAdd, bAddEnable);
		}
		//设置当前节点可选的功能性质
		getComboBoxProperty().removeItemListener(this);
		if (isExecutableNode(iParentPropIndex) || iParentPropIndex == POWERFUL_BUTTON
				|| iParentPropIndex == POWERLESS_BUTTON) {
			//zsb+：由于一级按钮可以增加二级按钮
			//可执行功能节点
			getComboBoxProperty().removeAllItems();
			getComboBoxProperty().addItem(FUNC_NODE[POWERFUL_BUTTON]);
			getComboBoxProperty().addItem(FUNC_NODE[POWERLESS_BUTTON]);
			if (getSelectedNode().getChildCount() == 0) {
				getComboBoxProperty().addItem(FUNC_NODE[PARAMETER]);
			}

		} else if (iParentPropIndex == INEXECUTABLE_FUNC_NODE) { //虚功能节点
			getComboBoxProperty().removeAllItems();
			getComboBoxProperty().addItem(FUNC_NODE[EXECUTABLE_FUNC_NODE]);
			getComboBoxProperty().addItem(FUNC_NODE[INEXECUTABLE_FUNC_NODE]);
			getComboBoxProperty().addItem(FUNC_NODE[EXECUTABLE_FUNC_FRAME]);
			getComboBoxProperty().addItem(FUNC_NODE[LFW_FUNC_NODE]);

		} else
			System.out.println("不可能");

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
		//锁定
		if (getSelectedNode().getChildCount() > 0)
			getComboBoxProperty().setEnabled(false); //易证：有子节点则当前节点功能属性不可改动
		//对应类名
		String className = fr.getClassName();
		if (className == null)
			getTextFieldClass().setText("");
		else
			getTextFieldClass().setText(className);
		//对应帮助文件名
		String helpName = fr.getHelpName();
		if (helpName == null)
			getTextFieldHelp().setText("");
		else
			getTextFieldHelp().setText(helpName);

		//resid作为自定义项1
		String strResid = fr.getResid();
		if (strResid == null)
			getTextFieldResid().setText("");
		else
			getTextFieldResid().setText(strResid);

		//禁用标志
		if (fr.getForbidFlag().intValue() == 1)
			getRadioForbidYes().setSelected(true);
		else
			getRadioForbidNo().setSelected(true);
		//参数显示标志
		if (fr.hasPara().booleanValue())
			getRadioShowParamYes().setSelected(true);
		else
			getRadioShowParamNo().setSelected(true);
		//描述
		String funDesc = fr.getFunDesc();
		if (funDesc != null)
			getTextAreaDesc().setText(funDesc);

		//是否需要显示上机日志
		getCkbNeedButtonLog().setSelected(fr.isIsneedbuttonlog().booleanValue());

		//是否启用按钮权限控制
		getCkbButtonPower().setSelected(fr.getIsbuttonpower().booleanValue());

		//zsb+:组织类型,置选中项
		getOrgnizeTypeCombo().setEnabled(false);
		String sOrgTypeCode = null;
		if (isExecutableNode(iParentPropIndex)) {
			//父节点是可执行节点，则组织类型用父节点的组织类型
			sOrgTypeCode = ((FunRegNode) node).getNodeVo().getOrgTypecode();
		} else if (iParentPropIndex == POWERFUL_BUTTON || iParentPropIndex == POWERLESS_BUTTON) {
			//父节点是按钮，则组织类型用组父节点的组织类型
			sOrgTypeCode = ((FunRegNode) node.getParent()).getNodeVo().getOrgTypecode();
		} else if (isExecutableNode(index)) {
			//当前节点为可执行节点，取其自身
			sOrgTypeCode = fr.getOrgTypecode();
		}//否则其他的都为空
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
			if (i == getOrgnizeTypeCombo().getItemCount()) {//表示没有匹配到
				getOrgnizeTypeCombo().setSelectedItem(null);
			}
		}
	}

	/**
	 * 显示空信息
	 * 创建日期：(2001-3-29 20:11:17)
	 */
	public void displayNullInfo() {
		//功能编码
		String funCode = getSelectedNode().getNodeVo().getFunCode();
		String strCode = "";
		if (funCode != null && funCode.trim().length() >= LEVELLEN) {
			if ((funCode.trim().length() + LEVELLEN) > LEVELLEN * 30)
				getTextFieldFunCode().setMaxLength(LEVELLEN * 30);
			else
				getTextFieldFunCode().setMaxLength(funCode.trim().length() + LEVELLEN);
			//带出编码
			strCode = funCode.trim();
		} else {
			getTextFieldFunCode().setMaxLength(LEVELLEN);
		}
		//生成功能编码最后一级（与兄弟节点不重复）
		String strTemp = generateCode();
		//设置编码
		strCode += strTemp;
		getTextFieldFunCode().setText(strCode);
		if (strTemp.equals("**")) {
			//选中编码末级
			getTextFieldFunCode().setSelectionStart(strCode.length() - 2);
			getTextFieldFunCode().setSelectionEnd(strCode.length());
		}
		//功能名称
		getTextFieldFunName().setText("");
		//获得父节点功能性质
		int iParentPropIndex = getSelectedNode().getNodeVo().getFunProperty().intValue();
		//属性类型
		if (isExecutableNode(iParentPropIndex) || iParentPropIndex == POWERFUL_BUTTON
				|| iParentPropIndex == POWERLESS_BUTTON || iParentPropIndex == PARAMETER) {
			getLabelName().setText("按 钮 编 码");
			getLabelClassName().setText("按 钮 名 称");
			getComboBoxPropType().setSelectedItem(" ");
			getComboBoxPropType().setEnabled(false); //按钮或参数
			getTextFieldHelp().setEnabled(false);
			//zsb+:组织类型为父节点的组织类型，不改变，组织类型不可用		
			getOrgnizeTypeCombo().setEnabled(false);
		} else {
			getComboBoxPropType().setSelectedIndex(0); //虚节点或可执行节点
			getTextFieldHelp().setEnabled(true);
			//zsb+:组织类型为第一个，由于功能性质默认为可执行节点
			getOrgnizeTypeCombo().setEnabled(true);
			getOrgnizeTypeCombo().setSelectedIndex(0);
		}
		//设置当前节点可选的功能性质
		getComboBoxProperty().removeItemListener(this);
		if (isExecutableNode(iParentPropIndex) || iParentPropIndex == POWERFUL_BUTTON
				|| iParentPropIndex == POWERLESS_BUTTON) { //可执行功能节点
			getComboBoxProperty().removeAllItems();
			getComboBoxProperty().addItem(FUNC_NODE[POWERFUL_BUTTON]);
			getComboBoxProperty().addItem(FUNC_NODE[POWERLESS_BUTTON]);
			getComboBoxProperty().addItem(FUNC_NODE[PARAMETER]);
		} else if (iParentPropIndex == INEXECUTABLE_FUNC_NODE) { //虚功能节点
			getComboBoxProperty().removeAllItems();
			getComboBoxProperty().addItem(FUNC_NODE[EXECUTABLE_FUNC_NODE]);
			getComboBoxProperty().addItem(FUNC_NODE[INEXECUTABLE_FUNC_NODE]);
			getComboBoxProperty().addItem(FUNC_NODE[EXECUTABLE_FUNC_FRAME]);
			getComboBoxProperty().addItem(FUNC_NODE[LFW_FUNC_NODE]);
		} else
			System.out.println("不可能");
		getComboBoxProperty().addItemListener(this);
		//对应类名
		getTextFieldClass().setText("");
		//对应帮助文件名
		getTextFieldHelp().setText("");

		//resid作为自定义项1
		getTextFieldResid().setText("");
		//if (iParentPropIndex == EXECUTABLE_FUNC_NODE
		//|| iParentPropIndex == EXECUTABLE_FUNC_FRAME)
		//getTextFieldHelp().setEnabled(false);
		//else
		//getTextFieldHelp().setEnabled(true);
		//禁用标志
		if (getSelectedNode().getNodeVo().getForbidFlag().intValue() == 1) //父节点
			getRadioForbidYes().setSelected(true);
		else
			getRadioForbidNo().setSelected(true);
		//参数显示标志
		getRadioShowParamNo().setSelected(true);
		//描述
		getTextAreaDesc().setText("");
		//功能编码文本域获得焦点
		getTextFieldFunCode().grabFocus();
	}

	/**
	 * 显示根节点对应明细
	 * 创建日期：(2001-3-29 20:11:17)
	 */
	public void displayRootInfo() {
		//设置编辑态
		setEditState(false);
		//功能名称
		//String funName=((FunRegNode) getSelectedNode().getRoot()).getNodeVo().getFunName();
		String funName = ((FunRegNode) ((DefaultTreeModel) m_funTree.getModel()).getRoot()).getNodeVo()
				.getFunName();
		if (funName != null)
			getTextFieldFunName().setText(funName);
		else
			getTextFieldFunName().setText("");
		//功能编码
		getTextFieldFunCode().setText("");
		//属性类型
		getComboBoxPropType().setSelectedItem(" ");
		//功能性质
		getComboBoxProperty().removeAllItems();
		getComboBoxProperty().addItem(FUNC_NODE[INEXECUTABLE_FUNC_NODE]);
		//对应类名
		getTextFieldClass().setText("");
		//对应帮助文件名
		getTextFieldHelp().setText("");
		//resid作为自定义项1
		getTextFieldResid().setText("");
		//禁用标志
		getRadioForbidNo().setSelected(true);
		//参数显示标志
		getRadioShowParamNo().setSelected(true);
		//描述
		getTextAreaDesc().setText("");
		//zsb+:组织类型
		getOrgnizeTypeCombo().setSelectedItem(null);
		getOrgnizeTypeCombo().setEnabled(false);
	}

	/**
	 * 无效方法？
	 * 创建日期：(2001-4-11 14:50:20)
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
	 * 无效方法？
	 * 创建日期：(2001-4-11 14:50:20)
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
	 * 生成功能编码最后一级（与兄弟节点不重复）
	 * 创建日期：(01-5-23 8:37:17)
	 * @return java.lang.String
	 */
	public String generateCode() {
		String strTemp = "";
		int iChildCount = getSelectedNode().getChildCount();
		if (iChildCount == 0)
			strTemp = "1";
		else {
			String strIndex = null;
			//构造已有编码末级的数值数组
			int[] iIndices = new int[iChildCount];
			for (int i = 0; i < iChildCount; i++) {
				strIndex = ((FunRegNode) getSelectedNode().getChildAt(i)).getNodeVo().getFunCode();
				strIndex = strIndex.substring(strIndex.length() - LEVELLEN, strIndex.length());
				try {
					iIndices[i] = Integer.parseInt(strIndex);
				} catch (NumberFormatException e) {
					iIndices[i] = -1; //遇到非数字的处理
				}
			}
			//查找第一个不重复的编码末级数值（抽屉原则）
			for (int i = 0; i <= iChildCount; i++) {
				boolean bEnd = true;
				for (int j = 0; j < iChildCount; j++)
					if (iIndices[j] == i + 1) {
						bEnd = false; //末级数值i+1已被使用
						break;
					}
				if (bEnd) {
					strTemp = String.valueOf(i + 1);
					break;
				}
			}
			if (Integer.parseInt(strTemp) >= 100) {
				System.out.println("同一节点的子节点数已达到" + iChildCount + "个");
				return "**";
			}
		}
		//补零使得编码末级与级长相符
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
			ivjLabelClassName.setText("对应文件名或控件名");

		}
		return ivjLabelClassName;
	}

	private UILabel getLabelCode() {
		if (ivjLabelCode == null) {
			ivjLabelCode = new UILabel();
			ivjLabelCode.setName("LabelCode");
			ivjLabelCode.setText("功 能 编 码");

		}
		return ivjLabelCode;
	}

	private UILabel getLabelDesc() {
		if (ivjLabelDesc == null) {
			ivjLabelDesc = new UILabel();
			ivjLabelDesc.setName("LabelDesc");
			ivjLabelDesc.setText("功 能 描 述");

		}
		return ivjLabelDesc;
	}

	private UILabel getLabelForbid() {
		if (ivjLabelForbid == null) {
			ivjLabelForbid = new UILabel();
			ivjLabelForbid.setName("LabelForbid");
			ivjLabelForbid.setText("是 否 启 用");

		}
		return ivjLabelForbid;
	}

	private UILabel getLabelButtonLog() {
		if (ivjLabelButtonLog == null) {
			ivjLabelButtonLog = new UILabel();
			ivjLabelButtonLog.setName("ivjLabelButtonLog");
			ivjLabelButtonLog.setText("是否启用按钮级日志");

		}
		return ivjLabelButtonLog;
	}

	private UILabel getLabelButtonPower() {
		if (ivjLabelButtonPower == null) {
			ivjLabelButtonPower = new UILabel();
			ivjLabelButtonPower.setName("ivjLabelButtonPower");
			ivjLabelButtonPower.setText("启用按钮权限控制");

		}
		return ivjLabelButtonPower;
	}

	private UILabel getLabelHelpName() {
		if (ivjLabelHelpName == null) {
			ivjLabelHelpName = new UILabel();
			ivjLabelHelpName.setName("LabelHelpName");
			ivjLabelHelpName.setText("对应帮助文件名");

		}
		return ivjLabelHelpName;
	}

	private UILabel getLabelResid() {
		if (ivjLabelResid == null) {
			ivjLabelResid = new UILabel();
			ivjLabelResid.setName("ivjLabelResid");
			ivjLabelResid.setText("自定义项1");

		}
		return ivjLabelResid;
	}

	private UILabel getLabelName() {
		if (ivjLabelName == null) {
			ivjLabelName = new UILabel();
			ivjLabelName.setName("LabelName");
			ivjLabelName.setText("功 能 名 称");

		}
		return ivjLabelName;
	}

	private UILabel getLabelProperty() {
		if (ivjLabelProperty == null) {
			ivjLabelProperty = new UILabel();
			ivjLabelProperty.setName("LabelProperty");
			ivjLabelProperty.setText("功 能 性 质");

		}
		return ivjLabelProperty;
	}

	private UILabel getLabelPropType() {
		if (ivjLabelPropType == null) {
			ivjLabelPropType = new UILabel();
			ivjLabelPropType.setName("LabelPropType");
			ivjLabelPropType.setText("属 性 类 型");

		}
		return ivjLabelPropType;
	}

	private UILabel getLabelShowParam() {
		if (ivjLabelShowParam == null) {
			ivjLabelShowParam = new UILabel();
			ivjLabelShowParam.setName("LabelShowParam");
			ivjLabelShowParam.setText("参数界面是否显示");

		}
		return ivjLabelShowParam;
	}

	/**
	 * 从“可执行功能节点”下所有快捷键中找到当前按钮匹配的
	 * 创建日期：(2003-11-7 17:11:26)
	 * @return nc.vo.pub.hotkey.HotkeyRegisterVO
	 * @param butnCode java.lang.String
	 * @param execFuncNodeHotkeys nc.vo.pub.hotkey.HotkeyRegisterVO[]
	 * @author：雷军
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
			lbOrgnizeType.setText("组 织 类 型");
		}
		return lbOrgnizeType;
	}

	/**
	 * 使用GroupLayout布局
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
	 * 转换功能性质为常数
	 * 创建日期：(01-5-21 12:49:34)
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
		System.out.println("无此属性");
		return -1;
	}

	private UIRadioButton getRadioForbidNo() {
		if (ivjRadioForbidNo == null) {
			ivjRadioForbidNo = new UIRadioButton();
			ivjRadioForbidNo.setName("RadioForbidNo");
			ivjRadioForbidNo.setSelected(true);
			ivjRadioForbidNo.setText("是");
			// user code begin {1}
			//getRadioForbidNo().setBackground(getPnDetail().getBackground());
			//getRadioForbidNo().setForeground(Color.black);
			getRadioForbidNo().setToolTipText("如果某功能节点不被禁用，则其所有祖先节点均不被禁用");

		}
		return ivjRadioForbidNo;
	}

	private UIRadioButton getRadioForbidYes() {
		if (ivjRadioForbidYes == null) {
			ivjRadioForbidYes = new UIRadioButton();
			ivjRadioForbidYes.setName("RadioForbidYes");
			ivjRadioForbidYes.setSelected(false);
			ivjRadioForbidYes.setText("否");
			// user code begin {1}
			//getRadioForbidYes().setBackground(getPnDetail().getBackground());
			//getRadioForbidYes().setForeground(Color.black);
			getRadioForbidYes().setToolTipText("如果某功能节点设为禁用，则其所有子孙节点均被设为禁用");

		}
		return ivjRadioForbidYes;
	}

	private UIRadioButton getRadioShowParamNo() {
		if (ivjRadioShowParamNo == null) {
			ivjRadioShowParamNo = new UIRadioButton();
			ivjRadioShowParamNo.setName("RadioShowParamNo");
			ivjRadioShowParamNo.setSelected(true);
			ivjRadioShowParamNo.setText("否");

		}
		return ivjRadioShowParamNo;
	}

	private UIRadioButton getRadioShowParamYes() {
		if (ivjRadioShowParamYes == null) {
			ivjRadioShowParamYes = new UIRadioButton();
			ivjRadioShowParamYes.setName("RadioShowParamYes");
			ivjRadioShowParamYes.setSelected(false);
			ivjRadioShowParamYes.setText("是");

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
	 * 获得选中节点
	 * 创建日期：(2001-3-29 14:38:36)
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
	 * 获得字符串字节长度
	 * 创建日期：(2001-4-12 18:59:50)
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
	 * 子类实现该方法，返回业务界面的标题。
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return "功能注册";
	}

	/**
	 * 获得全部功能节点
	 * 创建日期：(2001-3-29 11:02:30)
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
	 * 每当部件抛出异常时被调用
	 * @param exception java.lang.Throwable
	 */
	public void handleException(java.lang.Throwable e) {
		Logger.error(e.getMessage(), e);
	}

	/**
	 * 初始化按钮组
	 * 创建日期：(2001-3-28 20:21:47)
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
	 * 初始化类。
	 */

	private void initialize() {

		setName("FunRegisterUI");
		setLayout(new BorderLayout());
		setSize(760, 420);
		add(getSplitPn(), "Center");
		initConnections();

		// user code begin {2}
		//lj+ 2003-11-10 做成子菜单样式
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
	 * 初始化树
	 * 创建日期：(2001-3-28 20:23:19)
	 */
	public void initTree() {
		try {
			//设置按钮可用状态
			setButtonEnabled(m_boSave, false);
			setButtonEnabled(m_boCancel, false);
			//构造树
			FuncRegTree tree = new FuncRegTree();
			//周善保 update
			m_funTree = tree.createTreeByAllnode(getVoArray());
			//m_funTree = tree.createTree(getVoArray());
			m_funTree.getTreeNodeSearcher().setWildcardEnabled(true);

			//添加树单元修饰（用于区分有权限按钮与无权限按钮）
			m_funTree.setCellRenderer(new PowerCellRenderer());
			//设置树线型
			m_funTree.putClientProperty("JTree.lineStyle", "Angled");
			//设置树单选模式
			m_funTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			//设置加载标志
			m_loading = false;
			//添加树选取监听
			m_funTree.addTreeSelectionListener(this);
			//选中根
			m_funTree.setSelectionRow(0);
			//设置树为视图
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
	 * 增加
	 * 创建日期：(2001-3-29 15:00:41)
	 */
	public void onBoAdd() {
		if (m_funTree.getSelectionPath() == null) {
			//errorFlag = true;
			showErrorMessage("必须选中节点！");
			return;
		}
		String strFuncCode = getSelectedNode().getNodeVo().getFunCode();
		if (strFuncCode != null && strFuncCode.length() + LEVELLEN > LEVELLEN * 30) {
			showErrorMessage("已达到最大级次，不能再增加子节点！");
			return;
		}
		try {
			//设置按钮可用状态
			setButtonEnabled(m_boAdd, false);
			setButtonEnabled(m_boUpdate, false);
			setButtonEnabled(m_boDel, false);
			setButtonEnabled(m_boSave, true);
			setButtonEnabled(m_boCancel, true);
			//设置编辑态
			setEditState(true);
			m_bEdit = true;
			//设置标志
			m_haveSaveFlag = false;
			m_adding = true;
			//显示空明细
			displayNullInfo();
			showHintMessage("增加状态");
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	/**
	 * 取消
	 * 创建日期：(01-5-21 13:28:45)
	 */
	public void onBoCancel() {
		//结束编辑态
		stopEditting();
		//恢复增加或修改前的节点信息
		setButtonEnabled(m_boAdd, true);
		if (getSelectedNode().isRoot()) {
			setButtonEnabled(m_boUpdate, false);
			setButtonEnabled(m_boDel, false);
			displayRootInfo(); //根节点信息
		} else {
			setButtonEnabled(m_boUpdate, true);
			setButtonEnabled(m_boDel, true);
			displayNodeInfo(); //非根节点信息
		}
	}

	/**
	 * 删除
	 * 创建日期：(2001-3-29 15:00:41)
	 */
	public void onBoDel() {
		if (m_funTree.getSelectionPath() == null) {
			showErrorMessage("必须选中要删除的节点！");
			return;
		} else if (!getSelectedNode().isLeaf()) {
			showErrorMessage("必须先删除子节点！");
			return;
		}
		try {
			if (MessageDialog.showOkCancelDlg(this, "删除确认", "确实要删除吗？") == MessageDialog.ID_OK) {
				int index = SFServiceFacility.getFuncRegisterService().deleteByVO(
						getSelectedNode().getNodeVo());
				if (index == 0) {
					showErrorMessage("该节点已被用户使用，不能删除！");
					return;
				}
				FunRegNode paNode = (FunRegNode) getSelectedNode().getParent();
				paNode.remove(getSelectedNode());
				((DefaultTreeModel) (m_funTree.getModel())).reload(paNode);
				if (paNode.isRoot())
					displayRootInfo();
				//选中被删节点的父节点
				m_selPath = new TreePath(paNode.getPath());
				setSelPath(m_selPath);
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	/**
	 * 保存
	 * 创建日期：(2001-3-29 15:00:41)
	 */
	public void onBoSave() {
		if (getSelectedNode() == null) {
			showErrorMessage("必须选中父节点！");
			return;
		}
		if (m_haveSaveFlag) {
			showErrorMessage("请先设置内容");
			return;
		}
		FuncRegisterVO voBack = null;
		//功能编码
		String funCode = getTextFieldFunCode().getText().trim();
		if (funCode == null || funCode.length() <= 0) {
			showErrorMessage("功能编码不能为空");
			getTextFieldFunCode().grabFocus();
			return;
		} else {
			if (funCode.length() != funCode.getBytes().length) {
				showErrorMessage("禁止输入双字节字符");
				getTextFieldFunCode().grabFocus();
				return;
			}
			if (funCode.length() > 60) { //表结构越界???有问题
				showErrorMessage("功能编码长度不能大于60！");
				getTextFieldFunCode().grabFocus();
				return;
			}
			if (funCode.length() != getTextFieldFunCode().getMaxLength()) { //???有问题
				showErrorMessage("根据级次规则，该功能编码的长度必须为" + getTextFieldFunCode().getMaxLength() + "位（前后空格不计）");
				getTextFieldFunCode().grabFocus();
				return;
			}
		}
		//功能名称
		String funName = getTextFieldFunName().getText().trim();
		if (funName == null || funName.length() <= 0) {
			showErrorMessage("功能名称不能为空");
			getTextFieldFunName().grabFocus();
			return;
		} else {
			if (getStrlength(funName) > 50) { //表结构越界???有问题
				showErrorMessage("功能名称长度不能大于50！");
				getTextFieldFunName().grabFocus();
				return;
			}
			//vo.setFunName(funName);
		}
		//功能性质
		if (getComboBoxProperty().getSelectedItem() == null) {
			showErrorMessage("功能性质不能为空");
			return;
		}
		String strSelItem = getComboBoxProperty().getSelectedItem().toString();
		int iSelectedProp = getPropIndex(strSelItem);
		//属性类型
		if (isExecutableNode(iSelectedProp)
				&& getComboBoxPropType().getSelectedItem().toString().trim().equals("")) {
			showErrorMessage("可执行功能节点的属性类型不能为空");
			return;
		}
		//对应类名或控件名称
		String className = getTextFieldClass().getText().trim();
		if (className == null || className.length() <= 0) {
			if (iSelectedProp != INEXECUTABLE_FUNC_NODE && iSelectedProp != POWERFUL_BUTTON
					&& iSelectedProp != POWERLESS_BUTTON) { //非虚功能节点，非按钮
				showErrorMessage("类名或控件名称不能为空");
				getTextFieldClass().grabFocus();
				return;
			}
		} else {
			if (getStrlength(className) > 200) {
				showErrorMessage("类名或控件名称长度不能大于200");
				getTextFieldClass().grabFocus();
				return;
			}
			//vo.setClassName(className);
		}
		//对应帮助文件名
		String helpName = getTextFieldHelp().getText().trim();
		if (getStrlength(helpName) > 50) { //表结构越界
			showErrorMessage("帮助文件名长度不能大于50！");
			getTextFieldHelp().grabFocus();
			return;
		}
		//resid
		String strResid = getTextFieldResid().getText().trim();
		if (getStrlength(strResid) > 50) { //表结构越界
			showErrorMessage("自定义项1 长度不能大于50！");
			getTextFieldResid().grabFocus();
			return;
		}
		
		//功能描述
		String funDesc = getTextAreaDesc().getText().trim();
		if (funDesc != null) {
			if (getStrlength(funDesc) > 512) { //表结构越界
				showErrorMessage("功能描述长度长度不能大于512！");
				getTextAreaDesc().grabFocus();
				return;
			}
			//vo.setFunDesc(funDesc);
		}
		//获得模块类型
		char cModuleType = FuncModuleJudger.getModuleType(funCode);
		try {
			if (m_adding) { //增加状态
				FuncRegisterVO vo = new FuncRegisterVO();
				//功能名称
				vo.setFunName(funName);
				//功能编码
				if (!getSelectedNode().isRoot()) {
					if (!funCode.substring(0, funCode.length() - LEVELLEN).equals(
							getSelectedNode().getNodeVo().getFunCode())) {
						showErrorMessage("功能编码必须继承父节点！");
						getTextFieldFunCode().grabFocus();
						return;
					}
				}
				vo.setFunCode(funCode);
				//属性类型
				if (getComboBoxPropType().getSelectedItem().equals(""))
					vo.setGroupFlag(new Integer(-1));
				else
					vo.setGroupFlag(new Integer(getComboBoxPropType().getSelectedIndex()));
				//功能属性
				//int index = getComboBoxProperty().getSelectedIndex();
				vo.setFunProperty(new Integer(iSelectedProp));
				//对应类名或控件名称
				vo.setClassName(className);
				//对应帮助文件名
				vo.setHelpName(helpName);
				//resid
				vo.setResid(strResid);
				//禁用标志
				if (getRadioForbidYes().isSelected())
					vo.setForbidFlag(new Integer("1"));
				else
					vo.setForbidFlag(new Integer("0"));
				//参数显示标志
				if (getRadioShowParamYes().isSelected())
					vo.setHasPara(new UFBoolean(true));
				else
					vo.setHasPara(new UFBoolean(false));
				//NeedLog
				vo
						.setisneedbuttonlog(getCkbNeedButtonLog().isSelected() ? UFBoolean.TRUE
								: UFBoolean.FALSE);
				vo.setIsbuttonpower(getCkbButtonPower().isSelected() ? UFBoolean.TRUE : UFBoolean.FALSE);
				//功能描述
				vo.setFunDesc(funDesc);
				if (getSelectedNode().isRoot()) {
					vo.setFunLevel(new Integer(1)); //节点级次
				} else {
					///
					FuncRegisterVO voParent = getSelectedNode().getNodeVo();
					int paLevel = voParent.getFunLevel().intValue();
					vo.setFunLevel(new Integer(paLevel + 1)); //节点级次
					int iParentProp = voParent.getFunProperty().intValue();
					if (iParentProp == POWERFUL_BUTTON || iParentProp == POWERLESS_BUTTON)
						//由于允许有二级按钮，所以如果父节点为按钮，则再找父节点为功能节点
						voParent = ((FunRegNode) getSelectedNode().getParent()).getNodeVo();
					String paID = voParent.getPrimaryKey();
					vo.setParentId(paID); //父节点ID
					String subSysID = voParent.getSubsystemId();
					if (subSysID != null && subSysID.trim().length() == 20) {
						vo.setSubsystemId(subSysID); //子系统ID
					}
				}
				//系统功能标志，暂时默认为1
				vo.setSystemFlag(new Integer(1));
				//树显示编码
				vo.setDispCode(funCode);
				//设置模块类型
				vo.setModuleType(cModuleType);
				//zsb+：组织类型
				Object oOrg = getOrgnizeTypeCombo().getSelectedItem();
				String sOrgTypeCode = null;
				if (oOrg != null) {
					sOrgTypeCode = ((OrgnizeTypeVO) oOrg).getTypecode();
				}
				vo.setOrgTypecode(sOrgTypeCode);
				//执行插入
				String newOID = SFServiceFacility.getFuncRegisterService().insert(vo);
				if (newOID.equals("1")) {
					showErrorMessage("功能编码为" + funCode + "的节点已存在，请修改！");
					getTextFieldFunCode().grabFocus();
					return;
				}
				//设置相关禁用标志
				if (!getRadioForbidYes().isSelected() && !getSelectedNode().isRoot()) { //注：此时getSelectedNode()是新增节点的父节点
					setAllParentsEnabled(getSelectedNode()); //全部祖先节点可用
					SFServiceFacility.getFuncRegisterService().updateAllParentsEnabled(funCode, LEVELLEN); //更新祖先节点禁用标志
				}
				//设置相关属性类型
				if (getComboBoxPropType().getSelectedIndex() <= 3 && !getSelectedNode().isRoot()) { //注：此时getSelectedNode()是新增节点的父节点
					setPropTypeOfAllParents(getSelectedNode()); //设置全部祖先节点属性类型为A1
					SFServiceFacility.getFuncRegisterService().updatePropTypeOfAllParents(funCode, LEVELLEN);
					//更新祖先节点属性类型为A1
				}
				//界面更新
				vo.setPrimaryKey(newOID);
				if (getSelectedNode().isRoot())
					vo.setSubsystemId(newOID);
				reloadTree(vo);
				showHintMessage("增加成功");
				//结束编辑态
				stopEditting();
				setButtonEnabled(m_boUpdate, true);
				setButtonEnabled(m_boDel, true);
				//选中新增节点
				TreeNode tn = (TreeNode) m_selPath.getLastPathComponent();
				if (tn.getChildCount() > 0) {
					//构造路径
					DefaultMutableTreeNode tnChild = (DefaultMutableTreeNode) tn.getChildAt(tn
							.getChildCount() - 1);
					Object[] objPaths = (Object[]) tnChild.getPath();
					m_selPath = new TreePath(objPaths);
					//选中新增节点
					//m_funTree.removeTreeSelectionListener(this);
					setSelPath(m_selPath);
					//m_funTree.addTreeSelectionListener(this);
				}
			} else { //修改状态
				if (getSelectedNode().isRoot()) {
					showErrorMessage("不能修改根节点!");
					return;
				}
				FuncRegisterVO vo = getSelectedNode().getNodeVo();

				voBack = (FuncRegisterVO) vo.clone();
				//功能名称
				vo.setFunName(funName);
				//功能编码
				if (getSelectedNode().isLeaf()) {
					if (!((FunRegNode) getSelectedNode().getParent()).isRoot()) {
						if (!funCode.substring(0, funCode.length() - LEVELLEN).equals(
								((FunRegNode) getSelectedNode().getParent()).getNodeVo().getFunCode())) {
							showErrorMessage("功能编码必须继承父节点！");
							getTextFieldFunCode().grabFocus();
							return;
						}
					}
					vo.setFunCode(funCode);
				}
				//属性类型
				vo.setGroupFlag(new Integer(getComboBoxPropType().getSelectedIndex()));
				//功能属性
				vo.setFunProperty(new Integer(iSelectedProp));
				//对应类名或控件名称
				vo.setClassName(className);
				//对应帮助文件名
				vo.setHelpName(helpName);
				//resid
				vo.setResid(strResid);
				//禁用标志
				if (getRadioForbidYes().isSelected())
					vo.setForbidFlag(new Integer("1"));
				else
					vo.setForbidFlag(new Integer("0"));
				//参数显示标志
				if (getRadioShowParamYes().isSelected())
					vo.setHasPara(new UFBoolean(true));
				else
					vo.setHasPara(new UFBoolean(false));
				//功能描述
				vo.setFunDesc(funDesc);
				//周善保注释掉2005-5-31:修改节点时不能修改其dispcode
				//			//树显示编码
				//			vo.setDispCode(funCode);

				//			NeedLog
				vo
						.setisneedbuttonlog(getCkbNeedButtonLog().isSelected() ? UFBoolean.TRUE
								: UFBoolean.FALSE);
				vo.setIsbuttonpower(getCkbButtonPower().isSelected() ? UFBoolean.TRUE : UFBoolean.FALSE);
				//设置模块类型
				vo.setModuleType(cModuleType);
				//zsb+：组织类型
				Object oOrg = getOrgnizeTypeCombo().getSelectedItem();
				String sOrgTypeCode = null;
				if (oOrg != null) {
					sOrgTypeCode = ((OrgnizeTypeVO) oOrg).getTypecode();
				}
				vo.setOrgTypecode(sOrgTypeCode);
				//执行更新
				int flag = SFServiceFacility.getFuncRegisterService().update(vo);
				if (flag == 2) {
					showErrorMessage("该功能节点已不存在，请刷新后再试!");
					return;
				} else if (flag == 1) {
					showErrorMessage("功能编码为" + funCode + "的节点已存在，请修改！");
					getTextFieldFunCode().grabFocus();
					return;
				}
				//设置相关禁用标志
				if (getRadioForbidYes().isSelected() && !m_bOldForbidFlag) {
					if (!getSelectedNode().isLeaf()) {
						setAllChildrenForbidden(getSelectedNode()); //全部子孙节点禁用
						SFServiceFacility.getFuncRegisterService().updateAllChildrenForbidden(funCode); //更新子孙节点禁用标志
					}
				} else if (!getRadioForbidYes().isSelected() && m_bOldForbidFlag) {
					setAllChildrenEnabled(getSelectedNode()); //全部子孙节点可用（李文辉倡导）
					//setSomeChildrenEnabled(getSelectedNode()); //部分子孙节点可用（陈新宇倡导）
					if (getSelectedNode().getLevel() > 1)
						setAllParentsEnabled(getSelectedNode()); //全部祖先节点可用
					SFServiceFacility.getFuncRegisterService().updateAllParentsEnabled(funCode, LEVELLEN); //更新祖先节点禁用标志
				}
				//设置相关属性类型
				if (getComboBoxPropType().getSelectedIndex() <= 3) {
					setPropTypeOfAllParents(getSelectedNode()); //设置全部祖先节点属性类型为A1
					SFServiceFacility.getFuncRegisterService().updatePropTypeOfAllParents(funCode, LEVELLEN);
					//更新祖先节点属性类型为A1
				}
				//zsb+:设置组织类型不可用
				getOrgnizeTypeCombo().setEnabled(false);
				//界面更新
				getSelectedNode().setUserObject(vo.getFunCode() + "  " + vo.getFunName());
				((DefaultTreeModel) (m_funTree.getModel())).reload(getSelectedNode());
				//repaint();
				showHintMessage("修改成功");
				//设置功能性质下拉框可用状态
				//getComboBoxProperty().setEnabled(true);
				//结束编辑态
				stopEditting();
				setButtonEnabled(m_boUpdate, true);
				setButtonEnabled(m_boDel, true);
			}
			m_haveSaveFlag = true;
		} catch (Exception ex) {
			System.out.println(ex);
			if (m_adding)
				showErrorMessage("增加失败");
			else {
				getSelectedNode().setNodeVo(voBack);
				showErrorMessage("修改失败");
			}
		}
		//设置增加按钮可用状态（zjb）
		if (isExecutableNode(iSelectedProp) || iSelectedProp == INEXECUTABLE_FUNC_NODE)
			setButtonEnabled(m_boAdd, true);
		else {
			//zsb+：只有非参数的一级按钮可以增加二级按钮
			//获得父节点功能性质
			int iParentPropIndex = ((FunRegNode) getSelectedNode().getParent()).getNodeVo()
					.getFunProperty().intValue();
			boolean bAddEnable = (iSelectedProp != PARAMETER) && (isExecutableNode(iParentPropIndex));
			setButtonEnabled(m_boAdd, bAddEnable);
		}
		getCkbNeedButtonLog().setEnabled(false);
		getCkbButtonPower().setEnabled(false);
	}

	/**
	 * 修改
	 * 创建日期：(2001-3-29 15:00:41)
	 */
	public void onBoUpdate() {
		if (m_funTree.getSelectionPath() == null) {
			showErrorMessage("必须选中要修改的节点！");
			return;
		}
		try {
			//设置编辑态
			setEditState(true);
			m_bEdit = true;

			//显示相应节点信息
			if (getSelectedNode().isRoot())
				displayRootInfo(); //根节点信息
			else
				displayNodeInfo(); //非根节点信息
			//设置按钮可用状态
			setButtonEnabled(m_boAdd, false);
			setButtonEnabled(m_boUpdate, false);
			setButtonEnabled(m_boDel, false);
			setButtonEnabled(m_boSave, true);
			setButtonEnabled(m_boCancel, true);

			//设置标志
			m_bOldForbidFlag = getRadioForbidYes().isSelected();
			m_haveSaveFlag = false;
			m_adding = false;
			if (!getSelectedNode().isLeaf()) {
				//锁定功能编码文本域
				getTextFieldFunCode().setEnabled(false);
				//锁定下拉框
				getComboBoxProperty().setEnabled(false); //易证：有子节点则当前节点功能属性不可改动
			}
			String strPropType = getComboBoxProperty().getSelectedItem().toString();
			int iSelectedProp = getPropIndex(strPropType);
			//对于虚功能节点，对应文件名或控件名文本域不可用
			if (iSelectedProp == INEXECUTABLE_FUNC_NODE)
				getTextFieldClass().setEnabled(false);

			//对于按钮、参数，对应帮助文件名不可用,zsb update：由于按钮有二级按钮，按钮或者参数的功能性质可以修改
			int iFuncIndex = getSelectedNode().getNodeVo().getFunProperty().intValue();
			if (iFuncIndex == POWERFUL_BUTTON || iFuncIndex == POWERLESS_BUTTON
					|| iFuncIndex == PARAMETER) {
				getTextFieldHelp().setEnabled(false);
				getComboBoxProperty().setEnabled(true);
			}
			//对于可执行功能节点，属性类型下拉框可用，否则不可用 zsb+:组织类型也一样

			if (isExecutableNode(iSelectedProp)) {
				getOrgnizeTypeCombo().setEnabled(true);
				getComboBoxPropType().setEnabled(true);
			} else {
				getOrgnizeTypeCombo().setEnabled(false);
				getComboBoxPropType().setEnabled(false);
			}
			//提示
			showHintMessage("修改状态");
		} catch (Exception ex) {
			handleException(ex);
		}
	}

	/**
	 * 子类实现该方法，响应按钮事件。
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
	 * 辅助校验
	 * 创建日期：(2003-2-20 15:23:05)
	 */
	public void onCheck() {
		FuncCheckDlg dlg = new FuncCheckDlg(this);
		dlg.showModal();
		dlg.destroy();
	}

	/**
	 * 快捷键注册对话框弹出来
	 暂没有做缓存？？？
	 * 创建日期：(2003-11-7 13:26:58)
	 * @author：雷军
	 */
	public void onHotkeyReg() {
		//先获取“可执行功能节点”下所有按钮，如果没有注册按钮，则不显示快捷键注册对话框
		//“可执行功能节点”下只可能有按钮
		int childCount = m_selectedNode.getChildCount();
		//System.out.println("### childCount = " + childCount);
		if (childCount == 0)
			return;

		//找到“可执行功能节点”下所有按钮的已有快捷键
		FuncRegisterVO execNodeVO = m_selectedNode.getNodeVo();

		//先从缓存中获取（缓存结构为funCode -  HotkeyRegisterVO[]）
		HotkeyRegisterVO[] hotkeys = null;
		//(HotkeyRegisterVO[]) m_hashBtn4Hotkey.get(execNodeVO.getFunCode());
		if (hotkeys == null) {
			//如果缓存中没有，则从数据库取
			try {
				hotkeys = SFAppServiceUtil.getHotKeyRegisterQueryService().queryAllHotkeyRegisterVOs(
						execNodeVO.getFunCode());
			} catch (Exception ex) {
				handleException(ex);
			}

			if (hotkeys != null) {
				//填入到缓存（？？？暂不做缓存 by 雷军 2003-11-7）
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

			//根据按钮操作名称来匹配其快捷键 lj@
			//周善保2005-6-25：根据按钮编码fun_name匹配其快捷键（fun_name为按钮编码，class_name为按钮名称）
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

			//填充到矢量中
			hotkeyEntrys4Btn.addElement(he);
		}

		/////// add by 雷军 12/11/2003 ///////
		// 排序
		Collections.sort(hotkeyEntrys4Btn, new Comparator() {
			public int compare(Object o1, Object o2) {
				String str1 = ((HotkeyEntry) o1).getStrKeyName();
				String str2 = ((HotkeyEntry) o2).getStrKeyName();
				return str1.compareToIgnoreCase(str2);
			}
		});
		/////// add by 雷军 12/11/2003 ///////	

		//弹出对话框
		HotkeyRegDlg hotkeyRegDlg = new HotkeyRegDlg(this, hotkeyEntrys4Btn);
		//初始化表格
		hotkeyRegDlg.setColumnNames(new String[] { "按钮编码", "按钮名称", "功能键", "按键", "显示" });
		hotkeyRegDlg.initTable();
		//显示
		hotkeyRegDlg.showModal();
		hotkeyRegDlg.destroy();
		if (hotkeyRegDlg.getResult() == UIDialog.ID_OK) {
			//System.out.println("### OKOK!");

			//保存新插入的快捷键或是更新原有快捷键
			try {
				saveOrUpdateHotkeys(execNodeVO.getFunCode(), hotkeyEntrys4Btn, hotkeys);
			} catch (Exception ex) {
				handleException(ex);
			}
		}
	}

	/**
	 * 恢复所有按钮的默认快捷键
	 * 创建日期：(2003-11-10 10:44:12)
	 * @修改人：雷军 2003-12-2 给出提示供用户选择
	 */
	public void onSetDefaultHotkey() {
		//lj+
		if (MessageDialog.showOkCancelDlg(this, "默认值确认", "确实要恢复按钮快捷键的默认值吗？") == MessageDialog.ID_OK) {
			try {
				//调用李氏方法 
				//			SFServiceFacility.getHotkeyRegisterService().updateDefaultHotKey();
				SFAppServiceUtil.getHotKeyRegisterService().updateDefaultHotKey();
			} catch (Exception ex) {
				handleException(ex);
			}
			showHintMessage("所有快捷键恢复默认值完成");
		}
	}

	/**
	 * 组合按钮注册对话框弹出来
	 *   调用周善保的对话框
	 * 创建日期：(2004-7-9 13:26:58)
	 * @author：雷军
	 */
	//public void onUnionFunc() {
	//	//先获取“可执行功能节点”下所有按钮，如果没有注册按钮，则不显示快捷键注册对话框
	//	//“可执行功能节点”下只可能有按钮
	//	int childCount = m_selectedNode.getChildCount();
	//	//System.out.println("### childCount = " + childCount);
	//	if (childCount == 0)
	//		return;
	//
	//	//找到“可执行功能节点”下所有按钮
	//	FuncRegisterVO execNodeVO = m_selectedNode.getNodeVo();
	//
	//	ArrayList alBtns = new ArrayList();
	//	for (int i = 0; i < childCount; i++) {
	//		FuncRegisterVO vo = ((FunRegNode) m_selectedNode.getChildAt(i)).getNodeVo();
	//		alBtns.add(vo);
	//	}
	//
	//	//弹出对话框
	//	RegisterUnionFuncDlg unionfuncRegDlg = new RegisterUnionFuncDlg(this);
	//	unionfuncRegDlg
	//		.loadData(execNodeVO, (FuncRegisterVO[]) alBtns.toArray(new FuncRegisterVO[] {
	//	}));
	//
	//	//显示
	//	unionfuncRegDlg.showModal();
	//	unionfuncRegDlg.destroy();
	//}
	/**
	 * 刷新树
	 * 创建日期：(2001-3-29 18:40:04)
	 * @param vo nc.vo.sm.zs.funcreg.FuncRegisterVO
	 */
	public void reloadTree(FuncRegisterVO vo) {
		getSelectedNode().add(new FunRegNode(vo));
		((DefaultTreeModel) (m_funTree.getModel())).reload(getSelectedNode());
	}

	/**
	 * 保存或更新快捷键
	 * 创建日期：(2003-11-7 18:48:03)
	 * @param vecHotkeyEntries
	 * @param hotkeys  该“可执行节点”  下原有快捷键
	 */
	private void saveOrUpdateHotkeys(String execFunCode, Vector vecHotkeyEntries,
			HotkeyRegisterVO[] hotkeys) throws Exception {
		Enumeration enums = vecHotkeyEntries.elements();
		Vector vecNewCreated = new Vector();
		HotkeyRegisterVO oldHotkey = null;
		HotkeyRegisterVO newHotkey = null;
		int iUpdateRecords = 0;

		//必须先判定是否为新加快捷键
		while (enums.hasMoreElements()) {
			HotkeyEntry entry = (HotkeyEntry) enums.nextElement();

			//如果没有健值,则不保存 leijun 2006-8-28
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

		//打出测试信息
		System.out.println("新增加快捷键条数 = " + vecNewCreated.size());
		System.out.println("更新快捷键条数 = " + iUpdateRecords);
	}

	/**
	 * 设置全部子孙节点的禁用标志为假(递归)
	 * 创建日期：(2001-4-4 16:21:17)
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
	 * 设置全部子孙节点的禁用标志为真(递归)
	 * 创建日期：(2001-4-4 16:21:17)
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
	 * 设置全部祖先节点的禁用标志为假
	 * 创建日期：(2001-4-4 16:21:17)
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
	 * 改变按钮可用状态
	 * 创建日期：(00-11-28 10:25:58)
	 * @param bo ierp.sm.core.ui.ButtonObject
	 * @param bEnabled boolean
	 */
	public void setButtonEnabled(ButtonObject bo, boolean bEnabled) {
		bo.setEnabled(bEnabled);
		updateButton(bo);
	}

	/**
	 * 设置编辑态
	 * 创建日期：(2001-3-29 20:39:38)
	 * @param flag boolean
	 */
	public void setEditState(boolean flag) {
		getTextFieldFunCode().setEnabled(flag); //编码可由系统自动带出
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
	 * 设置全部祖先节点的属性类型为A
	 * 创建日期：(2001-4-4 16:21:17)
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
	 * 设置选中节点
	 * 创建日期：(2001-3-29 14:36:08)
	 * @param node com.netscape.javascript.FunctionNode
	 */
	public void setSelectedNode(FunRegNode node) {
		m_selectedNode = node;
	}

	/**
	 * 选中树路径并使其可见
	 * 创建日期：(01-6-13 12:44:15)
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
	 * 设置子节点中的可执行功能节点及其子按钮的禁用标志为假
	 * 创建日期：(2001-4-4 16:21:17)
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
	 * 结束编辑态时所需进行的工作
	 * 创建日期：(01-5-21 14:05:12)
	 */
	public void stopEditting() {
		//设置按钮可用状态
		setButtonEnabled(m_boSave, false);
		setButtonEnabled(m_boCancel, false);
		//设置编辑态
		setEditState(false);
		m_bEdit = false;
		//设置提示
		showHintMessage("");
	}

	/**
	 * 树选取改变事件响应
	 * 创建日期：(2001-3-29 14:32:34)
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
			//选中原路径（当前编辑节点路径）
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
				displayRootInfo(); //根节点信息
			} else {
				setButtonEnabled(m_boUpdate, true);
				setButtonEnabled(m_boDel, true);
				displayNodeInfo(); //非根节点信息
				//周善保 update
				if (node.getNodeVo().getPrimaryKey() == null) {
					setButtonEnabled(m_boAdd, false);
					setButtonEnabled(m_boUpdate, false);
					setButtonEnabled(m_boDel, false);
					MessageDialog.showErrorDlg(this, "错误", "该节点信息丢失,请修复");
				}
			}
			int iProp = getSelectedNode().getNodeVo().getFunProperty().intValue();
			//设置快捷键按钮、组合按钮的可用状态 lj+ 2003-11-7
			if (isExecutableNode(iProp)) {
				//因为只有“可执行功能节点”下面才有按钮，所以。。。
				setButtonEnabled(m_boHotkeyReg, true);
			} else {
				setButtonEnabled(m_boHotkeyReg, false);
			}
		}
	}
}
