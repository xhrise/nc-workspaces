package com.ufsoft.iufo.fmtplugin.measure;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.pub.iufo.exception.CommonException;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.HBBBMeasParser;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.pub.license.LicenseValue;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellPosition;


/**
 * 指标提取对话框
 * @author：王海涛
 */

public class MeasureDefineDialog extends UfoDialog		implements
			ActionListener,
			MouseListener, 
			FocusListener,
			ListDataListener,
			KeyListener,DialogRefListener,IUfoContextKey{
	//标签
	private JLabel ivjJLCodeReference = null; //编码引用标签
	private JLabel ivjJLDirection = null; //方 向标签
	private JLabel ivjJLDXType = null; //抵消类型标签
	private JLabel ivjJLIsHBMeasure = null; //是否是合并指标标签
	private JLabel ivjJLMeasureAttribute = null; //指标单位标签
	private JLabel ivjJLMeasureLength = null; //指标长度标签
	private JLabel ivjJLMeasureName = null; //指标名称标签
	private JLabel ivjJLMeasureNote = null; //指标说明标签
	private JLabel ivjJLMeasureType = null; //指标类型标签

	//实体组件(所在的列与MeasureData中的列号相对应)
	private JButton ivjJBMeasureRef = null; //指标参照按钮
	private JButton ivjJBKeywordRef = null; //关键字参照按钮
	private JTextField ivjJTFMeasureName = null; //指标名称编辑框
	private JComboBox ivjJCBMeasureType = null; //指标类型复选框
	private JTextField ivjJTFMeasureType = null; //指标类型显示框
	private JComboBox ivjJCBMeasureAttribute = null; //指标单位复选框
	private JTextField ivjJTFMeasureLength = null; //指标长度编辑框
	private JComboBox ivjJCBCodeReference = null; //编码引用复选框
	private JTextField ivjJTFNote = null; //说 明编辑框
	private JRadioButton ivjJRBIsNotHBMeasure = null; //非合并指标单选按钮
	private JRadioButton ivjJRBIsHBMeasure = null; //合并指标单选按钮
	private JRadioButton ivjJRBNoDirection = null; //无方向单选按钮
	private JRadioButton ivjJRBDirectionD = null; //贷方向单选按钮
	private JRadioButton ivjJRBDirectionJ = null; //借方向单选按钮
	private JRadioButton ivjJRBIsDXType = null; //抵消类型单选按钮
	private JRadioButton ivjJRBNotDXType = null; //非抵消类型单选按钮
	private JRadioButton ivjJRBNoDXType = null; //非合并指标的抵消类型单选按钮

	//面板容器和确定、取消按钮
	private UITabbedPane jTabbedPane = null; //面板
	private JLabel ivjJLabelTabbedTitle = null;//属性编辑标签
	private JPanel ivjUfoDialogContentPane = null; //对话框容器面板
	private JPanel ivjJPanel = null; //“确定取消”面板
	private JTextArea ivjJStatusArea = null; //“状态提示”面板
	private JCheckBox ivjJCheckBox=null;//是否以位置为指标名称
	private JScrollPane ivjJStatusPane = null; //“状态提示”面板
	private JLabel ivjJLabelStatusTitle = null;//状态提示标签
	private JPanel ivjJPanelBase = null; //“基本属性”面板
	private JPanel ivjJPanelExtend = null; //“扩展属性”面板
	private JScrollPane ivjJSPMeasureList = null; //“指标表”面板
	private JButton ivjJButtonCancel = null; //“取消”按钮
	private JButton ivjJButtonOK = null; //“确定”按钮
	//自定义数据
	private JTable m_table; //装载m_data数据，构建表格
	private MeasureDefineTableModel m_measureDefineTableModel; //指标数据
	private int currentRow = -1; //指标列表中每一个指标在m_table中所对应的行
	private boolean isSelectFromMTableOrRadio = false; //标记是否是从m_table中选择一行或者是对RadioButton的选择，目的是减少程序中对contentsChanged方法的处理次数
	private int[] rows; //记录每一次m_table中多选的所有指标所在的行，处理完毕后要清空
	private int DEFAULT_INT = -1; //标记默认的组件显示,适用于CombBox，选择index==-1
	private String DEFAULT_STRING = ""; //标记默认的组件显示,适用于TextField，设置Text==""
	private Hashtable propsForRows; //多选时指标的扩展属性
	private boolean isDialog = false; //标示是否是由于弹出提示框而造成的焦点失去，防止重复的对“焦点失去”这一事件进行处理而弹出对话框
	private boolean isCtrlPressed = false; //表示ctrl键是否按下
	private Vector type = new Vector();

	private Component refcomp = null;
	private Component refDialog = null;
	private IContext context = null;
//	UfoReport m_ufoReport = null;
	private Container parent = null;
	class MeasureTable extends UITable {
		public MeasureTable() {
			super();
		}
		//重载父类方法，以实现向下向上箭头时的属性面板联动更新显示内容
		public void valueChanged(ListSelectionEvent e) {
			int firstIndex = e.getFirstIndex();
			int lastIndex = e.getLastIndex();
			if (firstIndex == -1 && lastIndex == -1) { // Selection cleared.
				repaint();
			}
			Rectangle firstRowRect = getCellRect(firstIndex, 0, false);
			Rectangle lastRowRect = getCellRect(lastIndex, getColumnCount(),
					false);
			Rectangle dirtyRegion = firstRowRect.union(lastRowRect);
			// This marks this entire row as dirty but the painting system will
			// intersect this with the clip rect of the viewport and redraw only
			// the visible cells.
			repaint(dirtyRegion.x, dirtyRegion.y, dirtyRegion.width,
					dirtyRegion.height);
			//设置更新显示内容
			if (firstIndex != lastIndex) {
				int row = this.getSelectedRow();
				if (row < 0)
					row = 0;
				currentRow = row;
				setValueToComponent(row);
			}
		}
	}
	/**
	 * CreateMeasureDialog 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Dialog
	 */
	public MeasureDefineDialog(Container parent, MeasureDefineTableModel data,IContext context) {
		super(parent);
		this.m_measureDefineTableModel = data;
		this.parent = parent;
		this.context = context;
		initialize();
		m_measureDefineTableModel.setDialog(this);
		// @edit by ll at 2009-5-14,下午04:18:11 
		setSelFirstRow();
	}
	

	public void actionPerformed(ActionEvent event) {
		try {
			if (event.getSource() == ivjJButtonOK) {
				m_measureDefineTableModel.checkAnaRepMeasure();
//				m_measureDefineTableModel.checkDynAreaKeys();
//				m_measureDefineTableModel.saveKeyWord();
				setResult(ID_OK);
				//关闭指标提取界面
				close();
			} else if (event.getSource() == ivjJButtonCancel) {
				//关闭指标提取界面
				setResult(ID_CANCEL);
				close();
//			} else if (event.getSource() == ivjJButtonKeyApply) {
//				m_measureDefineTableModel.checkDynAreaKeys();
//				m_measureDefineTableModel.saveKeyWord();

			} else if (event.getSource() == ivjJBMeasureRef) {
				onMeasureRef();
//			} 
//			else if (event.getSource() == ivjJBKeywordRef) {
//				onKeyRef();
			} else {
				if (rows == null || rows.length == 1)
					setValueToMeasureVO(event);
				else
					setValuesToMeasureVOs(event);
			}
		} catch (Exception e) {
			setStateMessage(e.getMessage(), true);
		}
	}
	/**
	 * 此处插入方法描述。 创建日期：(2003-4-15 10:27:28)
	 */
	public void addHelp() {
		javax.help.HelpBroker hb = ResConst.getHelpBroker();
		if (hb == null)
			return;
		hb.enableHelpKey(getContentPane(), "TM_Data_CreateMeas", null);
	}
	public void beforeDialogClosed(Component refDlg) {
		//只有当前注册的窗口才允许真正的取消注册组件
		if (this.refDialog == refDlg) {
			//将注册组件置空
			refcomp = null;
			refDialog = null;
		}
	}
	/**监听combox内容的变化。主要是指标类型，指标单位内容的变化*/
	public void contentsChanged(ListDataEvent lde) {
		if (!isSelectFromMTableOrRadio) {
			if (rows == null || rows.length == 1)
				setValueToMeasureVO(lde);
			else
				setValuesToMeasureVOs(lde);
		}
	}
	/**
	 * 此处插入方法描述。 创建日期：(2003-9-1 20:53:41)
	 * 
	 * @param row
	 *            int
	 */
	public void fireCompement(int row) {
		setStateMessage("", false);
		currentRow = row;
		isSelectFromMTableOrRadio = true;
		setValueToComponent(row);
		isSelectFromMTableOrRadio = false;
	}
	public void focusGained(FocusEvent fe) {
	}
	public void focusLost(FocusEvent fe) {
		if (!isDialog) {
			if (rows == null || rows.length < 2)
				setValueToMeasureVO(fe);
			else
				setValuesToMeasureVOs(fe);
		}
	}
	/**
	 * 返回 JBReference 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
//	private javax.swing.JButton getJBKeywordRef() {
//		if (ivjJBKeywordRef == null) {
//			try {
//				ivjJBKeywordRef = new IUFOButton();
//				ivjJBKeywordRef.setName("ivjJBKeywordRef");
//
//				ivjJBKeywordRef.setText(StringResource
//						.getStringResource("miufo1001056")); //"关键字参照"
//				ivjJBKeywordRef.setBounds(576, 82, 125, 22);
//				ivjJBKeywordRef.setNextFocusableComponent(ivjJTFMeasureName);
//				ivjJBKeywordRef.addActionListener(this);
//			} catch (java.lang.Throwable ivjExc) {
//				handleException(ivjExc);
//			}
//		}
//		return ivjJBKeywordRef;
//	}
	/**
	 * 返回 JBReference 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJBMeasureRef() {
		if (ivjJBMeasureRef == null) {
			try {
				ivjJBMeasureRef = new UIButton();
				ivjJBMeasureRef.setName("ivjJBMeasureRef");

				ivjJBMeasureRef.setText(StringResource
						.getStringResource("miufo1000770")); //"指标参照"
				ivjJBMeasureRef.setBounds(188, 3, 75, 22);
				ivjJBMeasureRef.setNextFocusableComponent(ivjJBKeywordRef);
				ivjJBMeasureRef.addActionListener(this);								
			} catch (java.lang.Throwable ivjExc) {								
				handleException(ivjExc);
			}
		}
		return ivjJBMeasureRef;
	}
	/**
	 * 返回 JButtonCancel 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJButtonCancel() {
		if (ivjJButtonCancel == null) {
			try {
				ivjJButtonCancel = new UIButton();
				ivjJButtonCancel.setName("JButtonCancel");

				ivjJButtonCancel.setText(StringResource
						.getStringResource("miufo1000757")); //"取 消"
				ivjJButtonCancel.setBounds(96, 3, 75, 22);
				ivjJButtonCancel.addActionListener(this);
				ivjJButtonCancel.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false),
						JComponent.WHEN_FOCUSED);
				
			} catch (java.lang.Throwable ivjExc) {						
				handleException(ivjExc);
			}
		}
		return ivjJButtonCancel;
	}
	/**
	 * 返回 JButtonOK 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJButtonOK() {
		if (ivjJButtonOK == null) {
			try {
				ivjJButtonOK = new UIButton();
				ivjJButtonOK.setName("JButtonOK");

				ivjJButtonOK.setText(StringResource
						.getStringResource("miufo1000758")); //"确 定"
				ivjJButtonOK.setBounds(7, 3, 75, 22);
				ivjJButtonOK.addActionListener(this);//sdklhdk
				ivjJButtonOK.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
				ivjJButtonOK.setNextFocusableComponent(ivjJButtonCancel);
				
				
			} catch (java.lang.Throwable ivjExc) {
				
				
				handleException(ivjExc);
			}
		}
		return ivjJButtonOK;
	}
	/**
	 * 返回 JCBCodeReference 特性值。
	 * 
	 * @return javax.swing.JComboBox
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JComboBox getJCBCodeReference() {
		if (ivjJCBCodeReference == null) {
			try {
				ivjJCBCodeReference = new UIComboBox();
				ivjJCBCodeReference.setName("JCBCodeReference");
				ivjJCBCodeReference.setBounds(new Rectangle(474, 42, 89, 20));
				ivjJCBCodeReference
						.setNextFocusableComponent(ivjJCBMeasureAttribute);
				//初始化编码引用复选框
				if (m_measureDefineTableModel.getCode() != null) {
					for (Enumeration codeEm = m_measureDefineTableModel.getCode().elements(); codeEm
							.hasMoreElements();) {
						nc.vo.iufo.code.CodeVO cvo = (nc.vo.iufo.code.CodeVO) codeEm
								.nextElement();
						ivjJCBCodeReference.addItem(cvo.getName());
					}
				}
				ivjJCBCodeReference.setSelectedIndex(-1);
				ivjJCBCodeReference.getModel().addListDataListener(this);
				ivjJCBCodeReference.setEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJCBCodeReference;
	}
	/**
	 * 返回 JCBMeasureAttribute 特性值。
	 * 
	 * @return javax.swing.JComboBox
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JComboBox getJCBMeasureAttribute() {
		if (ivjJCBMeasureAttribute == null) {
			try {
				ivjJCBMeasureAttribute = new UIComboBox();
				ivjJCBMeasureAttribute.setName("JCBMeasureAttribute");
				ivjJCBMeasureAttribute.setBounds(257, 42, 75, 20);
				ivjJCBMeasureAttribute
						.setNextFocusableComponent(ivjJTFMeasureLength);
				//初始化指标单位复选框
				for (int i = 0; i < MeasureDefineTableModel.UNITS.length; i++) {
					ivjJCBMeasureAttribute.addItem(MeasureDefineTableModel.UNITS[i]);
				}
				ivjJCBMeasureAttribute.getModel().addListDataListener(this);
				ivjJCBMeasureAttribute.setEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJCBMeasureAttribute;
	}
	/**
	 * 返回 JCBMeasureType 特性值。
	 * 
	 * @return javax.swing.JComboBox
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JComboBox getJCBMeasureType() {
		if (ivjJCBMeasureType == null) {
			try {
				ivjJCBMeasureType = new UIComboBox();
				ivjJCBMeasureType.setName("JCBMeasureType");
				ivjJCBMeasureType.setBounds(474, 12, 89, 20);
				ivjJCBMeasureType
						.setNextFocusableComponent(ivjJCBCodeReference);
				//初始化指标类型复选框
				for (int i = 0; i < MeasureDefineTableModel.TYPES.length; i++) {
					if (i == 2) {
						//如果编码缓存中有可供选择的编码，则允许将指标定义为编码类型，否则不允许将指标定义为编码类型
						if (m_measureDefineTableModel.getCode() != null
								&& !m_measureDefineTableModel.getCode().isEmpty()) {
							ivjJCBMeasureType
									.addItem((String) (MeasureDefineTableModel.TYPES[i]));
							type.addElement(new Integer(i));
						}
					} else {
						ivjJCBMeasureType
								.addItem((String) (MeasureDefineTableModel.TYPES[i]));
						type.addElement(new Integer(i));
					}
				}
				ivjJCBMeasureType.getModel().addListDataListener(this);
				ivjJCBMeasureType.setEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJCBMeasureType;
	}
	/**
	 * 返回 JLCodeReference 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLCodeReference() {
		if (ivjJLCodeReference == null) {
			try {
				ivjJLCodeReference = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLCodeReference.setName("JLCodeReference");

				ivjJLCodeReference.setBounds(new Rectangle(386, 44, 80, 20));
				ivjJLCodeReference.setText(StringResource
						.getStringResource("miufo1001442")); //"代码选择："
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLCodeReference;
	}
	/**
	 * 返回 JLDirection 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLDirection() {
		if (ivjJLDirection == null) {
			try {
				ivjJLDirection = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLDirection.setName("JLDirection");

				ivjJLDirection.setText(StringResource
						.getStringResource("miufo1001443")); //"合并方向："
				ivjJLDirection.setBounds(15, 33, 124, 16);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLDirection;
	}
	/**
	 * 返回 JLDXType 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLDXType() {
		if (ivjJLDXType == null) {
			try {
				ivjJLDXType = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLDXType.setName("JLDXType");

				ivjJLDXType.setText(StringResource
						.getStringResource("miufo1001444")); //"期初合并指标："
				ivjJLDXType.setBounds(15, 55, 124, 16);
			} catch (java.lang.Throwable ivjExc) {				
				handleException(ivjExc);
			}
		}
		return ivjJLDXType;
	}
	/**
	 * 返回 JLIsHBMeasure 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLIsHBMeasure() {
		if (ivjJLIsHBMeasure == null) {
			try {
				ivjJLIsHBMeasure = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLIsHBMeasure.setName("JLIsHBMeasure");

				ivjJLIsHBMeasure.setText(StringResource
						.getStringResource("miufo1001445")); //"是否是合并指标："
				ivjJLIsHBMeasure.setBounds(15, 10, 124, 16);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLIsHBMeasure;
	}
	/**
	 * 返回 JLMeasureAttribute 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLMeasureAttribute() {
		if (ivjJLMeasureAttribute == null) {
			try {
				ivjJLMeasureAttribute = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLMeasureAttribute.setName("JLMeasureAttribute");

				ivjJLMeasureAttribute.setText(StringResource
						.getStringResource("miufo1001446")); //"指标单位："
				ivjJLMeasureAttribute.setBounds(179, 82, 72, 20);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLMeasureAttribute;
	}
	/**
	 * 返回 JLMeasureLength 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLMeasureLength() {
		if (ivjJLMeasureLength == null) {
			try {
				ivjJLMeasureLength = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLMeasureLength.setName("JLMeasureLength");

				ivjJLMeasureLength.setText(StringResource
						.getStringResource("miufo1001447")); //"指标长度："
				ivjJLMeasureLength.setBounds(576, 12, 72, 20);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLMeasureLength;
	}
	/**
	 * 返回 JLMeasureName 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLMeasureName() {
		if (ivjJLMeasureName == null) {
			try {
				ivjJLMeasureName = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLMeasureName.setName("JLMeasureName");

				ivjJLMeasureName.setText(StringResource
						.getStringResource("miufo1001448")); //"指标名称："
				ivjJLMeasureName.setBounds(3, 12, 72, 20);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLMeasureName;
	}
	/**
	 * 返回 JLMeasureNote 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLMeasureNote() {
		if (ivjJLMeasureNote == null) {
			try {
				ivjJLMeasureNote = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLMeasureNote.setName("JLMeasureNote");

				ivjJLMeasureNote.setText(StringResource
						.getStringResource("miufo1001449")); //"说 明："
				ivjJLMeasureNote.setBounds(4, 42, 71, 20);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLMeasureNote;
	}
	private javax.swing.JLabel getJLTabbedTitle() {
		if (ivjJLabelTabbedTitle == null) {
			try {
				ivjJLabelTabbedTitle = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLabelTabbedTitle.setName("JLTabbedTitle");

				ivjJLabelTabbedTitle.setText(StringResource.getStringResource("miufo1001458")); //"属性编辑"
				ivjJLabelTabbedTitle.setBounds(7, 245, 97, 22);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabelTabbedTitle;
	}
	
	/**
	 * 返回 JLMeasureType 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLMeasureType() {
		if (ivjJLMeasureType == null) {
			try {
				ivjJLMeasureType = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLMeasureType.setName("JLMeasureType");

				ivjJLMeasureType.setText(StringResource
						.getStringResource("miufo1001450")); //"指标类型："
				ivjJLMeasureType.setBounds(386, 12, 80, 20);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLMeasureType;
	}
	/**
	 * 返回 JPanel1 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getJPanel() {
		if (ivjJPanel == null) {
			try {
				ivjJPanel = new UIPanel();
				ivjJPanel.setName("JPanel");
				ivjJPanel.setLayout(null);
//				ivjJPanel.setBounds(452, 438, 303, 75);
				ivjJPanel.setBounds(457, 520, 303, 35);
				getJPanel().add(getJButtonOK(), getJButtonOK().getName());
				getJPanel().add(getJButtonCancel(),
						getJButtonCancel().getName());
				ivjJPanel.add(getJBMeasureRef(), getJBMeasureRef().getName());
//				Border etched = BorderFactory.createEtchedBorder();
//				getJPanel().setBorder(etched);
			} catch (java.lang.Throwable ivjExc) {				
				handleException(ivjExc);
			}
		}
		return ivjJPanel;
	}
	/**
	 * 返回 JPanelBase 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getJPanelBase() {
		if (ivjJPanelBase == null) {
			try {
				ivjJPanelBase = new UIPanel();
				ivjJPanelBase.setBorder(null);
				ivjJPanelBase.setName("JPanelBase");
				ivjJPanelBase.setLayout(null);
				ivjJPanelBase.setBounds(19, 253, 365, 179);
				getJPanelBase().add(getJLMeasureName(),
						getJLMeasureName().getName());
//				getJPanelBase().add(getJLMeasureAttribute(),
//						getJLMeasureAttribute().getName());
				getJPanelBase().add(getJLMeasureLength(),
						getJLMeasureLength().getName());
				getJPanelBase().add(getJLMeasureNote(),
						getJLMeasureNote().getName());
				getJPanelBase().add(getJLMeasureType(),
						getJLMeasureType().getName());
				getJPanelBase().add(getJTFMeasureName(),
						getJTFMeasureName().getName());
				getJPanelBase().add(getJTFNote(), getJTFNote().getName());
				getJPanelBase().add(getJTFMeasureLength(),
						getJTFMeasureLength().getName());
				getJPanelBase().add(getJCBMeasureType(),
						getJCBMeasureType().getName());
				getJPanelBase().add(getJTFMeasureType(),
						getJTFMeasureType().getName());
//				getJPanelBase().add(getJCBMeasureAttribute(),
				ivjJPanelBase.add(getJLCodeReference(), null);
				ivjJPanelBase.add(getJCBCodeReference(), null);
//						getJCBMeasureAttribute().getName());
				getJCBMeasureAttribute();
			} catch (java.lang.Throwable ivjExc) {				
				handleException(ivjExc);
			}
		}
		return ivjJPanelBase;
	}
	/**
	 * 返回 JPanelExtend 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getJPanelExtend() {
		if (ivjJPanelExtend == null) {
			try {
				ivjJPanelExtend = new UIPanel();
				ivjJPanelExtend.setName("JPanelExtend");
				ivjJPanelExtend.setLayout(null);
				ivjJPanelExtend.setBounds(389, 253, 366, 179);
				getJPanelExtend().add(getJLDXType(), getJLDXType().getName());
				getJPanelExtend().add(getJLDirection(),
						getJLDirection().getName());
				getJPanelExtend().add(getJLIsHBMeasure(),
						getJLIsHBMeasure().getName());
				getJPanelExtend().add(getJRBIsHBMeasure(),
						getJRBIsHBMeasure().getName());
				getJPanelExtend().add(getJRBIsNotHBMeasure(),
						getJRBIsNotHBMeasure().getName());
				getJPanelExtend().add(getJRBDirectionJ(),
						getJRBDirectionJ().getName());
				getJPanelExtend().add(getJRBDirectionD(),
						getJRBDirectionD().getName());
				getJPanelExtend().add(getJRBIsDXType(),
						getJRBIsDXType().getName());
				getJPanelExtend().add(getJRBNotDXType(),
						getJRBNotDXType().getName());
				ButtonGroup bg = new ButtonGroup();
				bg.add(getJRBIsHBMeasure());
				bg.add(getJRBIsNotHBMeasure());
				getJPanelExtend().add(getJRBIsHBMeasure(),
						getJRBIsHBMeasure().getName());
				getJPanelExtend().add(getJRBIsNotHBMeasure(),
						getJRBIsNotHBMeasure().getName());

				bg = new ButtonGroup();
				ivjJRBNoDirection = new UIRadioButton();
				bg.add(ivjJRBNoDirection);
				bg.add(getJRBDirectionJ());
				bg.add(getJRBDirectionD());
				getJPanelExtend().add(getJRBDirectionD(),
						getJRBDirectionD().getName());
				getJPanelExtend().add(getJRBDirectionJ(),
						getJRBDirectionJ().getName());
				bg = new ButtonGroup();
				bg.add(getJRBIsDXType());
				bg.add(getJRBNotDXType());
				ivjJRBNoDXType = new UIRadioButton();
				bg.add(ivjJRBNoDXType);
				getJPanelExtend().add(getJRBIsDXType(),
						getJRBIsDXType().getName());
				getJPanelExtend().add(getJRBNotDXType(),
						getJRBNotDXType().getName());
				if (m_measureDefineTableModel.getPrivateRepUnitID() != null
						|| !LicenseValue.isHBBB()) {
					getJLDXType().setEnabled(false);
					getJLDirection().setEnabled(false);
					getJLIsHBMeasure().setEnabled(false);
					getJRBIsHBMeasure().setEnabled(false);
					getJRBIsNotHBMeasure().setEnabled(false);
					getJRBDirectionJ().setEnabled(false);
					getJRBDirectionD().setEnabled(false);
					getJRBIsDXType().setEnabled(false);
					getJRBNotDXType().setEnabled(false);
				}				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJPanelExtend;
	}
	/**
	 * 返回 JRBDirectionD 特性值。
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JRadioButton getJRBDirectionD() {
		if (ivjJRBDirectionD == null) {
			try {
				ivjJRBDirectionD = new UIRadioButton();
				ivjJRBDirectionD.setName("JRBDirectionD");

				ivjJRBDirectionD.setText(StringResource
						.getStringResource("miufo1001451")); //"贷方向"
				ivjJRBDirectionD.setBounds(294, 33, 61, 16);
				ivjJRBDirectionD.addActionListener(this);
				ivjJRBDirectionD.setNextFocusableComponent(ivjJRBNotDXType);				
			} catch (java.lang.Throwable ivjExc) {				
				handleException(ivjExc);
			}
		}
		return ivjJRBDirectionD;
	}
	/**
	 * 返回 JRBDirectionJ 特性值。
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JRadioButton getJRBDirectionJ() {
		if (ivjJRBDirectionJ == null) {
			try {
				ivjJRBDirectionJ = new UIRadioButton();
				ivjJRBDirectionJ.setName("JRBDirectionJ");

				ivjJRBDirectionJ.setText(StringResource
						.getStringResource("miufo1001452")); //"借方向"
				ivjJRBDirectionJ.setBounds(143, 33, 61, 16);
				ivjJRBDirectionJ.addActionListener(this);
				ivjJRBDirectionJ.setNextFocusableComponent(ivjJRBDirectionD);
				
				
			} catch (java.lang.Throwable ivjExc) {
				
				
				handleException(ivjExc);
			}
		}
		return ivjJRBDirectionJ;
	}
	/**
	 * 返回 JRBIsDXType 特性值。
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JRadioButton getJRBIsDXType() {
		if (ivjJRBIsDXType == null) {
			try {
				ivjJRBIsDXType = new UIRadioButton();
				ivjJRBIsDXType.setName("JRBIsDXType");

				ivjJRBIsDXType.setText(StringResource
						.getStringResource("miufo1001453")); //"期初合并指标"
				ivjJRBIsDXType.setBounds(294, 55, 150, 16);
//				addToolTipAuto(ivjJRBIsDXType);
				ivjJRBIsDXType.addActionListener(this);
				ivjJRBIsDXType.setNextFocusableComponent(ivjJButtonOK);
				
				
			} catch (java.lang.Throwable ivjExc) {
				
				
				handleException(ivjExc);
			}
		}
		return ivjJRBIsDXType;
	}
	/**
	 * 返回 JRBIsHBMeasure 特性值。
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JRadioButton getJRBIsHBMeasure() {
		if (ivjJRBIsHBMeasure == null) {
			try {
				ivjJRBIsHBMeasure = new UIRadioButton();
				ivjJRBIsHBMeasure.setName("JRBIsHBMeasure");

				ivjJRBIsHBMeasure.setText(StringResource
						.getStringResource("miufopublic311")); //"是"
				ivjJRBIsHBMeasure.setBounds(294, 10, 61, 16);

				ivjJRBIsHBMeasure.addActionListener(this);
				ivjJRBIsHBMeasure.setNextFocusableComponent(ivjJRBNoDirection);
				
				
			} catch (java.lang.Throwable ivjExc) {
				
				
				handleException(ivjExc);
			}
		}
		return ivjJRBIsHBMeasure;
	}
	/**
	 * 返回 JRBIsNotMeasure 特性值。
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JRadioButton getJRBIsNotHBMeasure() {
		if (ivjJRBIsNotHBMeasure == null) {
			try {
				ivjJRBIsNotHBMeasure = new UIRadioButton();
				ivjJRBIsNotHBMeasure.setName("JRBIsNotMeasure");

				ivjJRBIsNotHBMeasure.setText(StringResource
						.getStringResource("miufopublic312")); //"否"
				ivjJRBIsNotHBMeasure.setBounds(143, 10, 61, 16);
				ivjJRBIsNotHBMeasure.addActionListener(this);
				ivjJRBIsNotHBMeasure
						.setNextFocusableComponent(ivjJRBIsHBMeasure);
				
				
			} catch (java.lang.Throwable ivjExc) {
				
				
				handleException(ivjExc);
			}
		}
		return ivjJRBIsNotHBMeasure;
	}
	/**
	 * 返回 JRBNotDXType 特性值。
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JRadioButton getJRBNotDXType() {
		if (ivjJRBNotDXType == null) {
			try {
				ivjJRBNotDXType = new UIRadioButton();
				ivjJRBNotDXType.setName("JRBNotDXType");

				ivjJRBNotDXType.setText(StringResource
						.getStringResource("miufo1001454")); //"非期初合并指标"
				ivjJRBNotDXType.setBounds(143, 55, 135, 16);
//				addToolTipAuto(ivjJRBNotDXType);
				ivjJRBNotDXType.addActionListener(this);
				ivjJRBNotDXType.setNextFocusableComponent(ivjJRBIsDXType);
				
				
			} catch (java.lang.Throwable ivjExc) {
				
				
				handleException(ivjExc);
			}
		}
		return ivjJRBNotDXType;
	}
	/**
	 * 返回 JSPanelMeasureList 特性值。
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JScrollPane getJSPMeasureList() {
		if (ivjJSPMeasureList == null) {
			try {
				m_table = new MeasureTable();//m_data);
				ivjJSPMeasureList = new UIScrollPane(m_table);
				ivjJSPMeasureList.setName("JSPMeasureList");
				ivjJSPMeasureList.setBounds(7, 11, 760, 231);
				ivjJSPMeasureList.setNextFocusableComponent(ivjJTFMeasureName);
				//构造指标列表

				m_table.setAutoCreateColumnsFromModel(false);
				m_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				m_table.setModel(m_measureDefineTableModel);
				m_table
						.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				m_table.addMouseListener(this);

				m_table.addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) {
						if (e.getSource() == m_table
								&& (e.getKeyChar() == KeyEvent.VK_DOWN || e
										.getKeyChar() == KeyEvent.VK_UP)) {
							System.out
									.println("_____________________________________");
							int row = m_table.getSelectedRow();
							if (row < 0)
								row = 0;
							//避免出现当多次点击同一行时，程序因进行重复处理而变慢
							if (row != currentRow && !isCtrlPressed) {
								currentRow = row;
								rows = m_table.getSelectedRows();
								if (rows.length < 2) {
									isSelectFromMTableOrRadio = true;
									setValueToComponent(row);
									isSelectFromMTableOrRadio = false;
								} else {
									isSelectFromMTableOrRadio = true;
									propsForRows = new Hashtable();
									setValuesToComponent();
									isSelectFromMTableOrRadio = false;
									currentRow = -1;
								}

							}
						}
					}
				});

				JComboBox comboBox;
				for (int k = 0; k < MeasureDefineTableModel.columnNames.length; k++) {
					if (!LicenseValue.isHBBB()) {
						if (k > MeasureDefineTableModel.NOTE_COLUMN) {
							break;
						}
					}
					TableCellRenderer renderer;
					if (k <= MeasureDefineTableModel.REFERENCE_COLUMN) {
						renderer = new MeasureCheckCellRenderer();
					} else {
						DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
						textRenderer
								.setHorizontalAlignment(MeasureDefineTableModel.columnNames[k].alignment);
						renderer = textRenderer;
					}

					TableCellEditor editor = null;
					JCheckBox check;
					JTextField textField;

					if (k <= MeasureDefineTableModel.REFERENCE_COLUMN) {
						check = new UICheckBox();
						editor = new DefaultCellEditor(check);
					} else if (k == MeasureDefineTableModel.TYPE_COLUMN
//							|| k == MeasureDefineTableModel.ATTRIBUTE_COLUMN
							|| k == MeasureDefineTableModel.CODEREFERENCE_COLUMN
							|| k == MeasureDefineTableModel.HEBING_COLUMN
							|| k == MeasureDefineTableModel.DIRECTION_COLUMN
							|| k == MeasureDefineTableModel.DXTYPE_COLUMN) {
						comboBox = new UIComboBox();
						editor = new DefaultCellEditor(comboBox);
					} else {
						textField = new UITextField();
						((UITextField)textField).setMaxLength(Integer.MAX_VALUE);
						//实现自动在失去焦点时刻，进行赋值
						textField.addFocusListener(new FocusAdapter() {
							public void focusLost(FocusEvent e) {
								TableCellEditor editor = m_table
										.getCellEditor();
								if (editor != null) {
									Object value = editor.getCellEditorValue();
									if (!m_measureDefineTableModel.isEditing())
										m_measureDefineTableModel.setValueAt(value, m_table
												.getEditingRow(), m_table
												.getEditingColumn());
								}
							}
						});
						editor = new DefaultCellEditor(textField);
					}

					TableColumn column = new TableColumn(k,
							MeasureDefineTableModel.columnNames[k].width, renderer, editor);
					m_table.addColumn(column);

				}
				//指标类型下拉框
				TableColumnModel tcm = m_table.getColumnModel();
				TableColumn typetc = tcm.getColumn(MeasureDefineTableModel.TYPE_COLUMN);
				JComboBox comboBox1 = (JComboBox) ((DefaultCellEditor) typetc
						.getCellEditor()).getComponent();

				for (int i = 0; i < MeasureDefineTableModel.TYPES.length; i++)
					comboBox1.addItem((String) (MeasureDefineTableModel.TYPES[i]));

				//单位下拉框
//				typetc = tcm.getColumn(MeasureDefineTableModel.ATTRIBUTE_COLUMN);
//				comboBox = (JComboBox) ((DefaultCellEditor) typetc
//						.getCellEditor()).getComponent();
//				comboBox.addItem(MeasureDefineTableModel.UNITS[2]);
//				for (int i = 0; i < MeasureDefineTableModel.UNITS.length; i++) {
//					if (i != 2)
//						comboBox.addItem(MeasureDefineTableModel.UNITS[i]);
//				}

				//编码引用下拉框
				typetc = tcm.getColumn(MeasureDefineTableModel.CODEREFERENCE_COLUMN);
				comboBox = (JComboBox) ((DefaultCellEditor) typetc
						.getCellEditor()).getComponent();
				Enumeration codes = m_measureDefineTableModel.getCode().elements();
				if (codes != null) {
					while (codes.hasMoreElements()) {
						comboBox.addItem(((nc.vo.iufo.code.CodeVO) codes
								.nextElement()).getName());
					}
				}

				if (LicenseValue.isHBBB()) {
					//合并指标下拉框
					typetc = tcm.getColumn(MeasureDefineTableModel.HEBING_COLUMN);
					comboBox = (JComboBox) ((DefaultCellEditor) typetc
							.getCellEditor()).getComponent();
					for (int i = 0; i < MeasureDefineTableModel.HEBING.length; i++)
						comboBox.addItem((String) (MeasureDefineTableModel.HEBING[i]));

					//方向下拉框
					typetc = tcm.getColumn(MeasureDefineTableModel.DIRECTION_COLUMN);
					comboBox = (JComboBox) ((DefaultCellEditor) typetc
							.getCellEditor()).getComponent();
					for (int i = 1; i < MeasureDefineTableModel.DIRECTION.length; i++)
						comboBox.addItem((String) (MeasureDefineTableModel.DIRECTION[i]));

					//抵消类型下拉框
					typetc = tcm.getColumn(MeasureDefineTableModel.DXTYPE_COLUMN);
					comboBox = (JComboBox) ((DefaultCellEditor) typetc
							.getCellEditor()).getComponent();
					for (int i = 0; i < MeasureDefineTableModel.DXTYPE.length; i++)
						comboBox.addItem((String) (MeasureDefineTableModel.DXTYPE[i]));
				}
				
			} catch (java.lang.Throwable ivjExc) {				
				handleException(ivjExc);
			}
		}
		return ivjJSPMeasureList;
	}
	/**
	 * @i18n miufohbbb00206=使用单元名称作为指标名称
	 */
	private JCheckBox getNameCheckBox(){
		if(ivjJCheckBox==null){
			ivjJCheckBox=new UICheckBox(StringResource.getStringResource("miufohbbb00206"));
			ivjJCheckBox.setName("JNameCheckBox");
			ivjJCheckBox.setBounds(7, 400, 200, 22);		
			ivjJCheckBox.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					if(e.getSource()==ivjJCheckBox&&ivjJCheckBox.isSelected()){
						getMeasureDefineTableModel().updateAllMeasureName(true);
					}else if(e.getSource()==ivjJCheckBox&&!ivjJCheckBox.isSelected()){
						getMeasureDefineTableModel().updateAllMeasureName(false);
					}
					m_table.repaint();
					setSelFirstRow();
				}
				
			});
		}
		return ivjJCheckBox;
	}
	/**
	 * 返回 JPanel1 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextArea getJStatusArea() {
		if (ivjJStatusArea == null) {
			try {
				ivjJStatusArea = new UITextArea();
//				ivjJStatusArea.setLineWrap(true);
				ivjJStatusArea.setName("ivjJStatusArea");
				ivjJStatusArea.setEditable(false);
				ivjJStatusArea.setForeground(Color.red);
				ivjJStatusArea.setBackground(ivjJPanelBase.getBackground());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJStatusArea;
	}
	private javax.swing.JLabel getJLStatusTitle() {
		if (ivjJLabelStatusTitle == null) {
			try {
				ivjJLabelStatusTitle = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLabelStatusTitle.setName("JLStatusTitle");

				ivjJLabelStatusTitle.setText(StringResource.getStringResource("miufo1001455")); //"状态与提示"
				ivjJLabelStatusTitle.setBounds(7, 425, 97, 22);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabelStatusTitle;
	}
	/**
	 * 返回 JPanel1 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private JScrollPane getJStatusPanel() {
		if (ivjJStatusPane == null) {
			try {
				ivjJStatusPane = new UIScrollPane(getJStatusArea());

				ivjJStatusPane.setAutoscrolls(true);
				ivjJStatusPane.setName("ivjJStatusPane");

				ivjJStatusPane.setBounds(7, 450, 760, 65);
//				Border etched = BorderFactory.createEtchedBorder();
//				Border title = BorderFactory.createTitledBorder(etched,
//						StringResource.getStringResource("miufo1001455")); //"状态与提示"
				ivjJStatusPane.setBorder(null);
				
				
			} catch (java.lang.Throwable ivjExc) {
				
				
				handleException(ivjExc);
			}
		}
		return ivjJStatusPane;
	}
	/**
	 * 返回 JPanel1 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UITabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			try {
				jTabbedPane = new UITabbedPane();
				jTabbedPane.setName("jTabbedPane");

				jTabbedPane.setBounds(7, 270, 760, 120);
				jTabbedPane.setBorder(null);
				jTabbedPane.insertTab(StringResource
						.getStringResource("miufo1001456"), null,
						getJPanelBase(), null, 0); //"基本属性"
				getJPanelExtend();//防止出现空指针的情况
				if (LicenseValue.isHBBB()) {
					jTabbedPane.insertTab(StringResource
							.getStringResource("miufo1001457"), null,
							getJPanelExtend(), null, 1); //"扩展属性"
				}
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return jTabbedPane;
	}
	/**
	 * 返回 JTFMeasureLength 特性值。
	 * 
	 * @return javax.swing.JTextField
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextField getJTFMeasureLength() {
		if (ivjJTFMeasureLength == null) {
			try {
				ivjJTFMeasureLength = new UITextField();
				ivjJTFMeasureLength.setName("JTFMeasureLength");
				ivjJTFMeasureLength.setBounds(654, 12, 80, 20);
				ivjJTFMeasureLength.addFocusListener(this);
				ivjJTFMeasureLength.addKeyListener(this);
				ivjJTFMeasureLength.setNextFocusableComponent(ivjJTFNote);
				ivjJTFMeasureLength.setEnabled(false);								
			} catch (java.lang.Throwable ivjExc) {								
				handleException(ivjExc);
			}
		}
		return ivjJTFMeasureLength;
	}
	/**
	 * 返回 JTFMeasureName 特性值。
	 * 
	 * @return javax.swing.JTextField
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextField getJTFMeasureName() {
		if (ivjJTFMeasureName == null) {
			try {
				ivjJTFMeasureName = new UITextField();
				ivjJTFMeasureName.setName("JTFMeasureName");
				ivjJTFMeasureName.setBounds(82, 12, 278, 20);
				ivjJTFMeasureName.addFocusListener(this);
				ivjJTFMeasureName.addKeyListener(this);
				ivjJTFMeasureName.setNextFocusableComponent(ivjJCBMeasureType);
				ivjJTFMeasureName.setEnabled(false);	
				((UITextField)ivjJTFMeasureName).setMaxLength(Integer.MAX_VALUE);
			} catch (java.lang.Throwable ivjExc) {				
				handleException(ivjExc);
			}
		}
		return ivjJTFMeasureName;
	}
	/**
	 * 返回 JCBMeasureType 特性值。
	 * 
	 * @return javax.swing.JComboBox
	 */
	/* 警告：此方法将重新生成。 */
	private JTextField getJTFMeasureType() {
		if (ivjJTFMeasureType == null) {
			try {
				ivjJTFMeasureType = new UITextField();
				ivjJTFMeasureType.setName("JTFMeasureType");
				ivjJTFMeasureType.setBounds(474, 12, 89, 20);
				ivjJTFMeasureType
						.setNextFocusableComponent(ivjJCBCodeReference);
				ivjJTFMeasureType.setVisible(false);
				ivjJTFMeasureType.setEnabled(false);				
			} catch (java.lang.Throwable ivjExc) {		
				handleException(ivjExc);
			}
		}
		return ivjJTFMeasureType;
	}
	/**
	 * 返回 JTFNote 特性值。
	 * 
	 * @return javax.swing.JTextField
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextField getJTFNote() {
		if (ivjJTFNote == null) {
			try {
				ivjJTFNote = new UITextField();
				ivjJTFNote.setName("JTFNote");
				ivjJTFNote.setBounds(82, 42, 278, 20);
				ivjJTFNote.addFocusListener(this);
				ivjJTFNote.addKeyListener(this);
				ivjJTFNote.setNextFocusableComponent(ivjJBMeasureRef);
				((UITextField)ivjJTFNote).setMaxLength(Integer.MAX_VALUE);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJTFNote;
	}
	public MeasureDefineTableModel getMeasureDefineTableModel() {
		return m_measureDefineTableModel;
	}
	public Component getRefDialog() {
		return refDialog;
	}
	public Component getRefOper() {
		return refcomp;
	}
	/**
	 * 返回 UfoDialogContentPane 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getUfoDialogContentPane() {
		if (ivjUfoDialogContentPane == null) {
			try {
				ivjUfoDialogContentPane = new UIPanel();
				ivjUfoDialogContentPane.setName("UfoDialogContentPane");

				ivjUfoDialogContentPane.setLayout(null);

				getUfoDialogContentPane().add(getJLTabbedTitle(),
						getJLTabbedTitle().getName());
				getUfoDialogContentPane().add(getJTabbedPane(),
						getJTabbedPane().getName());
				getUfoDialogContentPane().add(getJSPMeasureList(),
						getJSPMeasureList().getName());
				getUfoDialogContentPane().add(getNameCheckBox(),getNameCheckBox().getName());
				getUfoDialogContentPane().add(getJLStatusTitle(),
						getJLStatusTitle().getName());
				getUfoDialogContentPane().add(getJStatusPanel(),
						getJStatusPanel().getName());
				getUfoDialogContentPane().add(getJPanel(),
						getJPanel().getName());

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUfoDialogContentPane;
	}
	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

	}
	/**
	 * 对于用户选择的多行指标进行处理， 使相同的属性作为新指标的属性，不同的属性设置为初始值， 目的是为了在用户选择多行时在属性组件中显示
	 */
	private MeasurePosVO initComponentByRows() {
		//如果没有选中多行或只选中一行，则不作处理，此处主要是为了防止在该类中的错误调用
		if (rows == null || rows.length < 2) {
			return null;
		}
		StringBuffer measName = new StringBuffer();
		MeasurePosVO vo = (MeasurePosVO) m_measureDefineTableModel.getSelectMeasurePosVO(
				rows[0]).clone();
//		if (m_measureDefineTableModel.isKeywordRow(rows[0]))
//			return null;
		MeasureVO mvo = vo.getMeasureVO();
		//如果是参照指标，则不允许编辑
		if (vo.isRefMeasure()) {
			return vo;
		}
		measName.append(vo.getMeasureVO().getName());

		for (int i = 1; i < rows.length; i++) {
//			if (m_measureDefineTableModel.isKeywordRow(rows[i]))
//				return null;
			MeasurePosVO voRow = (MeasurePosVO) m_measureDefineTableModel.getSelectMeasurePosVO(rows[i]);
			//构造全部的指标名称，显示在状态提示栏上
			if (i < rows.length)
				measName.append(",\r\n");
			measName.append(voRow.getMeasureVO().getName());

			if (vo.getMeasureVO().getType() != voRow.getMeasureVO().getType()) {
				vo.getMeasureVO().setType(DEFAULT_INT);
			}
			if (vo.getMeasureVO().getAttribute() != voRow.getMeasureVO()
					.getAttribute()) {
				vo.getMeasureVO().setAttribute(DEFAULT_INT);
			}
			if (!((vo.getMeasureVO().getRefPK() == null && voRow.getMeasureVO()
					.getRefPK() == null) || (vo.getMeasureVO().getRefPK() != null
					&& voRow.getMeasureVO().getRefPK() != null && vo
					.getMeasureVO().getRefPK().equals(
							voRow.getMeasureVO().getRefPK())))) {
				vo.getMeasureVO().setRefPK(DEFAULT_STRING);
			}
			if (!((vo.getMeasureVO().getNote() == null && voRow.getMeasureVO()
					.getNote() == null) || (vo.getMeasureVO().getNote() != null
					&& voRow.getMeasureVO().getNote() != null && vo
					.getMeasureVO().getNote().equals(
							voRow.getMeasureVO().getNote())))) {
				vo.getMeasureVO().setNote(DEFAULT_STRING);
			}
			if (vo.getMeasureVO().getLen() != voRow.getMeasureVO().getLen()) {
				vo.getMeasureVO().setLen(DEFAULT_INT);
			}
			if (vo.getMeasureVO().getExttype() != voRow.getMeasureVO()
					.getExttype()) {
				vo.getMeasureVO().setExttype(0);
				vo.getMeasureVO().setProps(null);
			}
			Hashtable propsVO = vo.getMeasureVO().getProps();
			Hashtable propsVORow = voRow.getMeasureVO().getProps();

			if (propsVO != null) {
				if (propsVORow != null) {
					if (HBBBMeasParser.getDirection(propsVO) != HBBBMeasParser
							.getDirection(propsVORow)) {
						HBBBMeasParser.setDirection(
								HBBBMeasParser.DIRECTIONG_J, propsVO);
					}
					if (HBBBMeasParser.getDXType(propsVO) != HBBBMeasParser
							.getDXType(propsVORow)) {
						HBBBMeasParser.setDxMeas(true, propsVO);
					}
				}
			}
		}
		vo.getMeasureVO().setName(measName.toString());
		return vo;
	}
	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			setName("CreateMeasureDialog");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(780, 580);
			setResizable(false);
			setContentPane(getUfoDialogContentPane());
			m_measureDefineTableModel.addMouseListener(m_table);
			setValueToComponent(0);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}		
	}
	public void intervalAdded(ListDataEvent lde) {
	}
	public void intervalRemoved(ListDataEvent lde) {
	}
	public boolean isSetedRefDialog() {
		return false;
	}
	public void keyTyped(KeyEvent e) {

		if ((e.getSource() == ivjJTFMeasureName
				|| e.getSource() == ivjJTFMeasureLength || e.getSource() == ivjJTFNote)
				&& e.getKeyChar() == KeyEvent.VK_ENTER) {
			if (rows == null || rows.length < 2) {
				setValueToMeasureVO(e);
			} else {
				setValuesToMeasureVOs(e);
			}
		}
		if (e.getKeyChar() == KeyEvent.VK_CONTROL) {
			isCtrlPressed = true;
		}
		if (e.getSource() == m_table
				&& (e.getKeyChar() == KeyEvent.VK_DOWN || e.getKeyChar() == KeyEvent.VK_UP)) {
			int row = m_table.getSelectedRow();
			if (row < 0)
				row = 0;
			//避免出现当多次点击同一行时，程序因进行重复处理而变慢
			if (row != currentRow && !isCtrlPressed) {
				currentRow = row;
				rows = m_table.getSelectedRows();
				if (rows.length < 2) {
					isSelectFromMTableOrRadio = true;
					setValueToComponent(row);
					isSelectFromMTableOrRadio = false;
				} else {
					isSelectFromMTableOrRadio = true;
					propsForRows = new Hashtable();
					setValuesToComponent();
					isSelectFromMTableOrRadio = false;
					currentRow = -1;
				}

			}
		}
	}
	
	public void mouseClicked(MouseEvent MEvent) {
		if (MEvent.getSource() == m_table) {
			int row = m_table.getSelectedRow();
			if (row < 0)
				row = 0;
			//避免出现当多次点击同一行时，程序因进行重复处理而变慢
			if (row != currentRow && !isCtrlPressed) {
				currentRow = row;
				rows = m_table.getSelectedRows();
				if (rows.length < 2) {
					isSelectFromMTableOrRadio = true;
					setValueToComponent(row);
					isSelectFromMTableOrRadio = false;
				} else {
					isSelectFromMTableOrRadio = true;
					propsForRows = new Hashtable();
					setValuesToComponent();
					isSelectFromMTableOrRadio = false;
					currentRow = -1;
				}

			} else {
				//如果是点击以取消指标参照，则刷新组件，其他情况则不作处理
				int col = m_table.getSelectedColumn();
				if (col == MeasureDefineTableModel.REFERENCE_COLUMN) {
					m_measureDefineTableModel.setValueAt(Boolean.FALSE, row, col);

				}
				return;
			}
		}
	}
	public void mouseEntered(MouseEvent MEvent) {
	}
	public void mouseExited(MouseEvent MEvent) {

	}
	public void mousePressed(MouseEvent MEvent) {
		if (MEvent.getSource() == m_table) {
			m_table.getComponentAt(MEvent.getPoint());
			int row = m_table.getSelectedRow();
			if (row < 0)
				row = 0;
			//避免出现当多次点击同一行时，程序因进行重复处理而变慢
			if (row == currentRow) {
				return;
			}

			//如果是点击以取消指标参照，则刷新组件，其他情况则不作处理
			int col = m_table.getSelectedColumn();
			if (col == MeasureDefineTableModel.REFERENCE_COLUMN) {
				Boolean isRefMeasure = (Boolean)m_measureDefineTableModel.getValueAt(row, col);
				isRefMeasure = (isRefMeasure.booleanValue())?Boolean.FALSE:Boolean.TRUE;
				m_measureDefineTableModel.setValueAt(isRefMeasure, row, col);
			}
			currentRow = row;
			isSelectFromMTableOrRadio = true;
			setValueToComponent(row);
			isSelectFromMTableOrRadio = false;
		}
	}
	public void mouseReleased(MouseEvent MEvent) {
		if (MEvent.getSource() == m_table) {
			rows = m_table.getSelectedRows();
			if (rows.length < 2) {
				//currentRow = rows[0];
				return;
			}
			isSelectFromMTableOrRadio = true;
			propsForRows = new Hashtable();
			setValuesToComponent();
			isSelectFromMTableOrRadio = false;
			currentRow = -1;
		}
	}
	/**
	 * 响应关键字参照 创建日期：(2003-8-29 10:12:03)
	 */
//	private void onKeyRef() {
//		//如果没有选中一行进行编辑，则不允许参照
//		if (currentRow < 0) {
//			return;
//		}
//		//如果表格中有编辑的项，应该先设值
//		TableCellEditor editor = m_table.getCellEditor();
//		if (editor != null) {
//			Object value = editor.getCellEditorValue();
//			m_data.setValueAt(value, m_table.getEditingRow(), m_table
//					.getEditingColumn());
//		}
//		com.ufsoft.iuforeport.reporttool.data.UfoTable ufoTable = m_data
//				.getUfoTable();
//		String UnitID = m_data.getPrivateRepUnitID();
//
//		Vector hasDefVec = new Vector();
//		Vector Keys = ufoTable.getVecKeyVO();
//		if (Keys != null)
//			hasDefVec.addAll(Keys);
//
//		//对时间关键字处理，如果主表定义了时间关键字，则子表只能参照KeyVO.getTimeKeyIndex()比当前关键字值大的关键字
//		KeyVO timeVO;
//		for (int i = 0; i < hasDefVec.size(); i++) {
//			timeVO = (KeyVO) hasDefVec.get(i);
//			int timeIndex = timeVO.getTimeKeyIndex();
//			if (timeIndex > 0) {
//
//				KeyVO buildInVO = UICacheManager.getSingleton()
//						.getKeywordCache().getByPK(KeyVO.YEAR_PK);
//				if (timeIndex > buildInVO.getTimeKeyIndex()) {
//					hasDefVec.addElement(buildInVO);
//				}
//				buildInVO = UICacheManager.getSingleton().getKeywordCache()
//						.getByPK(KeyVO.HALF_YEAR_PK);
//				if (timeIndex > buildInVO.getTimeKeyIndex()) {
//					hasDefVec.addElement(buildInVO);
//				}
//				buildInVO = UICacheManager.getSingleton().getKeywordCache()
//						.getByPK(KeyVO.QUARTER_PK);
//				if (timeIndex > buildInVO.getTimeKeyIndex()) {
//					hasDefVec.addElement(buildInVO);
//				}
//				buildInVO = UICacheManager.getSingleton().getKeywordCache()
//						.getByPK(KeyVO.MONTH_PK);
//				if (timeIndex > buildInVO.getTimeKeyIndex()) {
//					hasDefVec.addElement(buildInVO);
//				}
//				buildInVO = UICacheManager.getSingleton().getKeywordCache()
//						.getByPK(KeyVO.TENDAYS_PK);
//				if (timeIndex > buildInVO.getTimeKeyIndex()) {
//					hasDefVec.addElement(buildInVO);
//				}
//				buildInVO = UICacheManager.getSingleton().getKeywordCache()
//						.getByPK(KeyVO.WEEK_PK);
//				if (timeIndex > buildInVO.getTimeKeyIndex()) {
//					hasDefVec.addElement(buildInVO);
//				}
//				buildInVO = UICacheManager.getSingleton().getKeywordCache()
//						.getByPK(KeyVO.DAY_PK);
//				if (timeIndex > buildInVO.getTimeKeyIndex()) {
//					hasDefVec.addElement(buildInVO);
//				}
//				break;
//			}
//		}
//		Keys = m_data.getDynAreaVO().getKeyVO();
//		if (Keys != null)
//			hasDefVec.addAll(Keys);
//
//		final KeyVO[] vos = new KeyVO[hasDefVec.size()];
//		hasDefVec.copyInto(vos);
//
//		SwingUtilities.invokeLater(new Thread() {
//			public void run() {
//				KeywordRefDialog dlgKR = new KeywordRefDialog(
//						CreateMeasureDialog.this, vos);
//				if (dlgKR.isInitOver) {
//					dlgKR.setLocationRelativeTo(CreateMeasureDialog.this);
//					dlgKR.setModal(false);
//					dlgKR.show();
//				}
//			}
//		});
//
//	}
	/*
	 * 响应指标参照 创建日期：(2003-8-29 10:12:03)
	 * @i18n miufo1004043=修改指标参照会删除以前的数据！
	 */
	private void onMeasureRef() {
		//如果没有选中一行进行编辑，则不允许参照
		if (currentRow < 0) {
			UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1004044"), parent);
			return;
		}
		//如果表格中有编辑的项，应该先设值
		TableCellEditor editor = m_table.getCellEditor();
		if (editor != null) {
			Object value = editor.getCellEditorValue();
			m_measureDefineTableModel.setValueAt(value, m_table.getEditingRow(), m_table
					.getEditingColumn());
		}
		
		//modified by chxw, 2007-04-28, 修进行指标参照的指标如果已存在，则提示用户“”
		MeasurePosVO measurePosVO = m_measureDefineTableModel.getSelectMeasurePosVO(currentRow);
//		if(measurePosVO.getMeasureVO().getDbtable() == null){
		//modify by wangyga 2008-6-26 如果此单元位置有指标，才提示"修改指标参照会删除以前的数据！"，否则，不提示				
		if(!measurePosVO.isRefMeasure()){
			String strMeasurePK = measurePosVO.getMeasurePK();
			if(strMeasurePK != null && strMeasurePK.length() > 0){
				int userSel = UfoPublic.showConfirmDialog(parent, StringResource.getStringResource("miufo1004043"));
				if(userSel != JOptionPane.OK_OPTION){
					return;
				}
			}
				
		}
		
				
//		com.ufsoft.iuforeport.reporttool.data.UfoTable ufoTable = m_measureDefineTableModel
//				.getUfoReport();
		String UnitID = m_measureDefineTableModel.getPrivateRepUnitID();
//		m_measureDefineTableModel.checkDynAreaKeys();
//		boolean iskeySaved = m_measureDefineTableModel.saveKeyWord();
//		if (!iskeySaved) {
//			String erromessage = StringResource
//					.getStringResource("miufo1001459"); //"未保存关键字，不能参照指标！"
//			throw new CommonException(erromessage);
//		}
		//得到参照的指标
		MeasureVO mvo = null;
		final ReportVO rep = CacheProxy.getSingleton().getReportCache()
				.getByPks(new String[]{m_measureDefineTableModel.getReportID()})[0];

//		/------------------		//FIXME: 此处添加指标参照时的关键字条件  ------------------------------
		final KeyGroupVO keygroup = new KeyGroupVO();

		KeywordModel keyModel = KeywordModel.getInstance(this.m_measureDefineTableModel.getCellsModel());
		DynAreaModel dynModel = DynAreaModel.getInstance(this.m_measureDefineTableModel.getCellsModel());
		Vector keys = new Vector();//ufoTable.getFormatModel().getKeyVO();
//		KeyVO[] vos = null;
//		if (keys != null) {
//			vos = new KeyVO[keys.size()];
//			keys.copyInto(vos);
//			keygroup.addKeyToGroup(vos);
//		}
		CellPosition[] m_cells = m_measureDefineTableModel.getCells();
		for(int i = 0; i < m_cells.length; i++){
		    if(dynModel.getDynAreaCellByPos(m_cells[i]) != null) {
		        DynAreaCell dynArea = dynModel.getDynAreaCellByPos(m_cells[i]);
		        Hashtable list = keyModel.getKeyVOByArea(dynArea.getArea());
		        if(list == null || list.isEmpty()){
		            String erromessage = StringResource.getStringResource("miufo1001460"); //"动态区域还没有定义关键字，不能参照指标！"
					throw new CommonException(erromessage);
		        }
		        keygroup.addKeyToGroup((KeyVO[])list.values().toArray(new KeyVO[0]));
		        break; 
		    }
		}

		HashMap mainList = keyModel.getMainKeyVOPos() ;
		if((mainList != null) && (!mainList.isEmpty())){
		    keygroup.addKeyToGroup((KeyVO[])mainList.keySet().toArray(new KeyVO[0]));
		}
		    
		
		
		if (keygroup.getSize() == 0) {
			String erromessage = StringResource.getStringResource("miufo1001461"); //"主表还没有定义关键字，不能参照指标！"
			throw new CommonException(erromessage);
		}

		Object repManagerObj = context.getAttribute(REP_MANAGER); 
		final boolean bRepMgr = repManagerObj == null ? false : Boolean.parseBoolean(repManagerObj.toString());
		final String strUserPK = (String)context.getAttribute(CUR_USER_ID);
		
		//将报表中原来有的以及新加的指标VO的code保存到excludeMeasures，防止被重复参照引用
		final ArrayList excludeMeasures = new ArrayList();
		MeasureModel measureModel = getMeasureDefineTableModel().getMeasureModel();
		ArrayList tmpList = new ArrayList(measureModel.getMeasureVOPosByAll().values());
		MeasureVO vo = null;
		Iterator it = tmpList.iterator();
		while(it.hasNext()){
			 vo = (MeasureVO)it.next();
			excludeMeasures.add(vo.getCode());
		}		
		Vector mtvoVec = getMeasureDefineTableModel().getVector();
		for(int i=0;i<mtvoVec.size();i++){
		    MeasurePosVO mtvo = (MeasurePosVO) mtvoVec.get(i);
		    excludeMeasures.add(mtvo.getMeasureVO().getCode());
        }
		
		
		SwingUtilities.invokeLater(new Thread() {
			public void run() {
				MeasureRefDlg dlgMR = new MeasureRefDlg(
						MeasureDefineDialog.this, rep, keygroup, strUserPK,
						false, bRepMgr, false, excludeMeasures);
					dlgMR.setLocationRelativeTo(MeasureDefineDialog.this);
					dlgMR.setModal(false);
					dlgMR.show();
			}
		});

	}
	public void onRef(EventObject ev) {
		try {
			if (ev.getSource() == refcomp) {
//				if (this.refDialog instanceof KeywordRefDialog) {
//					KeyVO refvo = ((KeywordRefDialog) refDialog).getRefVO();
//					if (refvo != null) {
//						m_data.setRefkey(currentRow, refvo);
//					}
//				} else 
				    if (this.refDialog instanceof MeasureRefDlg) {
					MeasureVO mvo = ((MeasureRefDlg) refDialog)
							.getRefMeasure();
					MeasurePosVO vo = m_measureDefineTableModel
							.getSelectMeasurePosVO(currentRow);
					//不能在关键上参照指标
					if (vo.getKeyFlag().booleanValue()) {
						String strError = StringResource
								.getStringResource("miufo1001462"); //"不能将关键字参照为指标!"
						throw new CommonException(strError);
					}
					if (mvo == null) {
						return;
					}
					//判断该指标时都被引用过，引用过则给用户提示
					int refRes = m_measureDefineTableModel.isReferenced(mvo, vo);
					if (refRes != MeasureDefineTableModel.MEASREF_NO_ERR) {
						String erromessage;
						if (refRes == MeasureDefineTableModel.MEASREF_MEAS_REPETITION_ERR) {
							erromessage = StringResource
									.getStringResource("miufo1001463"); //"已经引用过该指标，不能再次引用，可以通过单元公式实现！"
							throw new CommonException(erromessage);
						} else if (refRes == MeasureDefineTableModel.MEASREF_NAME_REPETITION_ERR) {
							erromessage = StringResource
									.getStringResource("miufo1001464"); //"已经提取或引用了同名指标，不能再引用该名称的指标！"
							throw new CommonException(erromessage);
						}

					}
					//设置指标为新参照的指标
					if (vo.getState() == MeasurePosVO.MEASURE_TABLE_S_ORIGINAL)
						vo.setState(MeasurePosVO.MEASURE_TABLE_S_UPDATE);
					vo.setRefMeasure(mvo);
					vo.setFlag(Boolean.TRUE);
				}
			}
			m_measureDefineTableModel.fireTableDataChanged();//刷新显示
			isSelectFromMTableOrRadio = true;
			setValueToComponent(currentRow);
			isSelectFromMTableOrRadio = false;
			setStateMessage("", false);
		} catch (RuntimeException ce) {
			setStateMessage(ce.getMessage(), true);
		}
	}
	//设置编码引用，如果设置了编码引用，则返回true，表示指标的属性已经被更改了，false表示未修改指标属性
	private boolean setCodeRefence(String codeReference, MeasurePosVO vo) {
		boolean isedited = false;
		if (codeReference != null) {
			for (Enumeration em = m_measureDefineTableModel.getCode().elements(); em
					.hasMoreElements();) {
				nc.vo.iufo.code.CodeVO cvo = (nc.vo.iufo.code.CodeVO) em
						.nextElement();
				if (cvo.getName().equals(codeReference.trim())) {
					vo.getMeasureVO().setRefPK(cvo.getId());
					isedited = true;
					break;
				}
			}
		} else {
			vo.getMeasureVO().setRefPK(null);
		}
		return isedited;
	}
	public void setRefDialogAndRefOper(Component refDlg, Component comp) {
		if (this.refDialog == null && this.refcomp == null) {
			this.refDialog = refDlg;
			this.refcomp = comp;
		}
	}
	/**
	 * 此处插入方法描述。 创建日期：(2003-9-3 16:30:15)
	 * 
	 * @param msg
	 *            java.lang.String
	 */
	public void setStateMessage(String msg, boolean isError) {
		if (isError) {
			getJStatusArea().setForeground(Color.red);
			msg = StringResource.getStringResource("miufo1001465") + msg; //"错误提示：\r\n"
		} else
			getJStatusArea().setForeground(Color.blue);
		getJStatusArea().setText(msg);
	}
	/**
	 * 此处插入方法描述。 创建日期：(2003-10-20 16:42:05)
	 * 
	 * @param selrow
	 *            int
	 */
	public void setTableSelectRow(int selrow) {
		m_table.setRowSelectionInterval(selrow, selrow);
	}
	/**
	 * 此处插入方法描述。 创建日期：(2003-10-20 16:42:05)
	 * 
	 * @param selrow
	 *            int
	 */
	public void setTableSelectRows(int[] selrows) {
		m_table
				.setRowSelectionInterval(selrows[0],
						selrows[selrows.length - 1]);
	}
	/**
	 * 本方法只用来在用户将选择的多行指标的 类型、单位、长度、编码引用、说明属性和扩展属性 设置为相同时使用，此时不能编辑指标的名称和编码（显示为空），
	 * 当所有指标中在某一属性上不一致时，该属性对应的组件显示置空， 如果此时用户为该属性选择一个新的值，则所有被选中的指标的该属性均更改为此属性
	 */
	private void setValuesToComponent() {
		MeasurePosVO vo = initComponentByRows();
		//如果vo是指标引用过来的，则所有组件置灰
		if (vo == null || vo.isRefMeasure()) {

			ivjJTFMeasureName.setText(""); //指标名称置空，并禁用
			ivjJTFMeasureName.setEnabled(false);
			ivjJTFMeasureLength.setText(""); //指标长度置空，并禁用
			ivjJTFMeasureLength.setEnabled(false);
			ivjJTFNote.setText(""); //指标说明置空，并禁用
			ivjJTFNote.setEnabled(false);
			ivjJCBCodeReference.setSelectedIndex(-1); //编码引用置空，并禁用
			ivjJCBCodeReference.setEnabled(false);
			ivjJCBMeasureAttribute.setSelectedIndex(-1);//指标单位置空，并禁用
			ivjJCBMeasureAttribute.setEnabled(false);
			ivjJCBMeasureType.setSelectedIndex(-1); //指标类型置空，并禁用
			ivjJCBMeasureType.setEnabled(false);

			//设置指标扩展属性不可用
			ivjJRBDirectionD.setEnabled(false);
			ivjJRBDirectionJ.setEnabled(false);
			ivjJRBNoDirection.setEnabled(false);
			ivjJRBIsDXType.setEnabled(false);
			ivjJRBNotDXType.setEnabled(false);
			ivjJRBIsHBMeasure.setEnabled(false);
			ivjJRBIsNotHBMeasure.setEnabled(false);
			return;

		}
		//设置指标编码,因为要显示多个指标的指标编码，所以不显示

		//设置指标名称
		ivjJTFMeasureName.setText("");
		ivjJTFMeasureName.setEnabled(false);
		//设置指标参照
		ivjJBMeasureRef.setEnabled(false);
		//设置指标类型
		int type = vo.getMeasureVO().getType();
		if (type == DEFAULT_INT) {
			ivjJCBMeasureType.setSelectedIndex(DEFAULT_INT);
		} else {
			ivjJCBMeasureType.setSelectedIndex(type);
		}
		ivjJCBMeasureType.setEnabled(true);
		//设置指标编码引用
		try {
			if (vo.getMeasureVO().getRefPK() != null
					&& vo.getMeasureVO().getRefPK().length() > 0) {
				nc.vo.iufo.code.CodeVO cvo = (nc.vo.iufo.code.CodeVO) m_measureDefineTableModel
						.getCode().get(vo.getMeasureVO().getRefPK());
				ivjJCBCodeReference.setSelectedItem(cvo.getName());
			} else if (vo.getMeasureVO().getRefPK() == null
					|| vo.getMeasureVO().getRefPK().length() == 0) {
				ivjJCBCodeReference.setSelectedIndex(DEFAULT_INT);
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		//设置指标单位

		ivjJCBMeasureAttribute.setSelectedIndex(vo.getMeasureVO()
				.getAttribute());
		//设置指标长度
		int length = vo.getMeasureVO().getLen();
		if (length == -1) {
			length = 0;
		}
		ivjJTFMeasureLength.setText(Integer.toString(length));
		if (type == MeasureVO.TYPE_CHAR) {
			ivjJTFMeasureLength.setEnabled(true);
			ivjJCBMeasureAttribute.setEnabled(false);
			ivjJCBCodeReference.setEnabled(false);
		} else if (type == MeasureVO.TYPE_NUMBER) {
			ivjJTFMeasureLength.setEnabled(false);
			ivjJCBMeasureAttribute.setEnabled(true);
			ivjJCBCodeReference.setEnabled(false);
		} else {
			ivjJTFMeasureLength.setEnabled(false);
			ivjJCBMeasureAttribute.setEnabled(false);
			ivjJCBCodeReference.setEnabled(true);
		}
		//设置指标说明
		ivjJTFNote.setText(vo.getMeasureVO().getNote());
		//设置指标是否合并
		Hashtable props = vo.getMeasureVO().getProps();
		if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING) {
			ivjJRBIsHBMeasure.setSelected(true);
		} else {
			ivjJRBIsNotHBMeasure.setSelected(true);
		}
		//设置指标方向
		if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING) {
			if (HBBBMeasParser.getDirection(props) == 1)
				ivjJRBDirectionJ.setSelected(true);
			else if (HBBBMeasParser.getDirection(props) == 2)
				ivjJRBDirectionD.setSelected(true);
		} else {
			ivjJRBNoDirection.setSelected(true);
		}
		//设置指标抵消类型
		if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING) {
			if (HBBBMeasParser.isDxMeas(props))
				ivjJRBIsDXType.setSelected(true);
			else
				ivjJRBNotDXType.setSelected(true);
		} else {
			ivjJRBNoDXType.setSelected(true);
		}
		setStateMessage(StringResource.getStringResource("miufo1001466")
				+ vo.getMeasureVO().getName(), false); //"当前选中指标的名称：\n\r"
		setTableSelectRows(rows);
	}
	private void setValuesToMeasureVOs(EventObject event) {
		//如果当前选定行只有一行，则不作处理
		if (rows.length < 2) {
			return;
		}
		//在设置值的时候，设置isDialog为true,保证不会重复弹出对话框
		//设置完之后，在恢复该值
		isDialog = true;
		//取到当前定义的指标类型
		int type = 0;
		String strtype = (String) ivjJCBMeasureType.getSelectedItem();
		if (strtype != null) {
			strtype = strtype.trim();
			for (int i = 0; i < MeasureDefineTableModel.TYPES.length; i++) {
				if (strtype.equals(MeasureDefineTableModel.TYPES[i])) {
					type = i;
					break;
				}
			}
		}
		//根据用户所修改的组件来对当前指标进行赋值
		//指标编码和指标名称不允许修改
		try {
			if (event.getSource() == ivjJTFMeasureLength) {
				//指标长度要验证合法性，有错要提示用户
				String length = ivjJTFMeasureLength.getText().trim();
				setValusByRows(MeasureDefineTableModel.LENGTH_COLUMN, length);
			} else if (event.getSource() == ivjJTFNote) {
				//指标说明的设置
				String note = ivjJTFNote.getText().trim();
				setValusByRows(MeasureDefineTableModel.NOTE_COLUMN, note);
			} else if (event.getSource() == ivjJCBCodeReference
					|| event.getSource() == ivjJCBCodeReference.getModel()) {
				//如果编码引用复选框的选择改变，则根据显示的编码引用名称找到对应的编码引用ID进行保存
				String codeRef = (String) ivjJCBCodeReference.getSelectedItem();
				setValusByRows(MeasureDefineTableModel.CODEREFERENCE_COLUMN, codeRef);

			} else if (event.getSource() == ivjJCBMeasureAttribute
					|| event.getSource() == ivjJCBMeasureAttribute.getModel()) {
				//如果单位为%,则需要判断指标是否为百分比指标，在扩展属性中增加百分比指标一项以标识
				Object attribute = ivjJCBMeasureAttribute.getSelectedItem();
//				setValusByRows(MeasureDefineTableModel.ATTRIBUTE_COLUMN, attribute);
			} else if (event.getSource() == ivjJCBMeasureType
					|| event.getSource() == ivjJCBMeasureType.getModel()) {
				//如果指标类型是数值或浮动数值，则单位可选，编码引用不可选，指标长度不可定义
				//如果指标类型是字符或浮动字符，则单位不可选，编码引用不可选，指标长度可定义
				//如果指标类型是编码或浮动编码，则单位不可选，编码引用可选，指标长度不可定义
				setValusByRows(MeasureDefineTableModel.TYPE_COLUMN, strtype);
				ivjJTFMeasureLength.setEnabled(true);
				ivjJTFMeasureLength.setText("64");
				ivjJCBMeasureAttribute.setSelectedIndex(-1);
				ivjJCBMeasureAttribute.setEnabled(false);
				if (type == MeasureVO.TYPE_CODE)//编码或浮动编码时,编码参照可用
				{
					ivjJCBCodeReference.setSelectedIndex(0);
					ivjJCBCodeReference.setEnabled(true);
					ivjJTFMeasureLength.setText("0");
					ivjJTFMeasureLength.setEnabled(false);
				} else {
					ivjJCBCodeReference.setSelectedIndex(-1);
					ivjJCBCodeReference.setEnabled(false);
					ivjJTFMeasureLength.setEnabled(true);

					if (type == MeasureVO.TYPE_NUMBER) {
						ivjJCBMeasureAttribute.setSelectedIndex(2);
						ivjJCBMeasureAttribute.setEnabled(true);
						ivjJTFMeasureLength.setEnabled(false);
						ivjJTFMeasureLength.setText("0");
					}
				}
			} else if (event.getSource() == ivjJRBNoDirection) {
				//当指标为合并指标时，方向为必选项，
				if (ivjJRBIsHBMeasure.isSelected()) {
					setValusByRows(MeasureDefineTableModel.DIRECTION_COLUMN,
							MeasureDefineTableModel.DIRECTION[0]);
					//ivjJRBDirectionJ.setSelected(true);
				}
			} else if (event.getSource() == ivjJRBDirectionD) {
				//如果当前指标不是合并指标，但是选择了该方向，那么，该指标自动改为合并指标
				setValusByRows(MeasureDefineTableModel.DIRECTION_COLUMN,
						MeasureDefineTableModel.DIRECTION[2]);
				ivjJRBIsHBMeasure.setSelected(true);
				ivjJCBMeasureType.setEnabled(false);
			} else if (event.getSource() == ivjJRBDirectionJ) {
				//如果当前指标不是合并指标，但是选择了该方向，那么，该指标自动改为合并指标
				setValusByRows(MeasureDefineTableModel.DIRECTION_COLUMN,
						MeasureDefineTableModel.DIRECTION[1]);
				ivjJRBIsHBMeasure.setSelected(true);
				ivjJCBMeasureType.setEnabled(false);
			} else if (event.getSource() == ivjJRBIsDXType) {
				//如果当前指标不是合并指标，但是选择了该类型，那么，该指标自动改为合并指标，
				//此时，默认方向为借方向，并设置扩展属性为抵消类型
	
				setValusByRows(MeasureDefineTableModel.DXTYPE_COLUMN, MeasureDefineTableModel.DXTYPE[1]);
				ivjJRBIsHBMeasure.setSelected(true);
				ivjJCBMeasureType.setEnabled(false);
			} else if (event.getSource() == ivjJRBNotDXType) {
				//如果当前指标是合并指标，选择了该类型，设置扩展属性为非抵消类型
				if (ivjJRBIsHBMeasure.isSelected()) {
					HBBBMeasParser.setDxMeas(false, propsForRows);
					setValusByRows(MeasureDefineTableModel.DXTYPE_COLUMN,
							MeasureDefineTableModel.DXTYPE[0]);
				}

			} else if (event.getSource() == ivjJRBIsHBMeasure) {
				//如果指标被设为合并指标，则指标的类型默认为数值类型（类型复选框不允许编辑），默认为借方向，抵消类型
				if (type == MeasureVO.TYPE_NUMBER) {
					setValusByRows(MeasureDefineTableModel.HEBING_COLUMN,
							MeasureDefineTableModel.HEBING[1]);
				}
				ivjJCBMeasureType.setSelectedIndex(0);
				ivjJCBMeasureType.setEnabled(false);
			} else if (event.getSource() == ivjJRBIsNotHBMeasure) {
				//如果指标被设为非合并指标，则指标类型复选框允许编辑，默认为无方向，非抵消类型
				setValusByRows(MeasureDefineTableModel.HEBING_COLUMN, MeasureDefineTableModel.HEBING[0]);
				ivjJCBMeasureType.setEnabled(true);
			}
			//并刷新显示
			m_measureDefineTableModel.fireTableDataChanged();
			setStateMessage(ivjJStatusArea.getText(), false);
		} catch (RuntimeException e) {
			setStateMessage(e.getMessage(), true);
		}

		//设置完之后，恢复isDialog为true
		isDialog = false;
		setTableSelectRows(rows);
	}
	private void setValueToComponent(int row) {
		//已有指标编码的一般指标不允许编辑，只允许参照
		//已有指标编码的特殊指标只允许编辑指标名称，也允许参照
		MeasurePosVO vo = m_measureDefineTableModel.getSelectMeasurePosVO(row);
		////设置指标编码,如果还未分配指标编码,则编码显示为空
		
		//设置指标名称
		ivjJTFMeasureName.setText((String) m_measureDefineTableModel.getValueAt(row,
				MeasureDefineTableModel.NAME_COLUMN));
		ivjJTFMeasureName.setEnabled(true);
		//设置指标参照
		ivjJBMeasureRef.setEnabled(true);
		//设置指标类型
		String typeStr = (String) m_measureDefineTableModel.getValueAt(row,
				MeasureDefineTableModel.TYPE_COLUMN);
		if (m_measureDefineTableModel.isCellEditable(row, MeasureDefineTableModel.TYPE_COLUMN)) {
			ivjJCBMeasureType.setVisible(true);
			ivjJTFMeasureType.setVisible(false);
			ivjJCBMeasureType.setSelectedItem(typeStr);
		} else {
			ivjJCBMeasureType.setVisible(false);
			ivjJTFMeasureType.setVisible(true);
			ivjJTFMeasureType.setText(typeStr);
		}
		
		//设置指标编码引用
		try {
			ivjJCBCodeReference.setSelectedIndex(-1);
			String refName = (String) m_measureDefineTableModel.getValueAt(row,
					MeasureDefineTableModel.CODEREFERENCE_COLUMN);
			if (refName.length() > 0) {
				ivjJCBCodeReference.setSelectedItem(refName);
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
//		String attribute = (String) m_measureDefineTableModel.getValueAt(row,
//				MeasureDefineTableModel.ATTRIBUTE_COLUMN);
		ivjJCBMeasureAttribute.setSelectedIndex(-1);
//		if (attribute.length() > 0) {
//			ivjJCBMeasureAttribute.setSelectedItem(attribute);
//		}
		//设置指标长度
		ivjJTFMeasureLength.setText(m_measureDefineTableModel.getValueAt(row,
				MeasureDefineTableModel.LENGTH_COLUMN).toString());

		//设置指标说明
		ivjJTFNote.setText((String) m_measureDefineTableModel.getValueAt(row,
				MeasureDefineTableModel.NOTE_COLUMN));
		//设置指标是否合并
		String hbprop = (String) m_measureDefineTableModel.getValueAt(row,
				MeasureDefineTableModel.HEBING_COLUMN);
		if (MeasureDefineTableModel.HEBING[1].equals(hbprop)) {
			ivjJRBIsHBMeasure.setSelected(true);
		} else {
			ivjJRBIsNotHBMeasure.setSelected(true);
		}
		//设置指标方向
		hbprop = (String) m_measureDefineTableModel.getValueAt(row, MeasureDefineTableModel.DIRECTION_COLUMN);
		if (MeasureDefineTableModel.DIRECTION[1].equals(hbprop)) {

			ivjJRBDirectionJ.setSelected(true);
			ivjJRBNoDirection.setEnabled(false);
			ivjJRBDirectionJ.setEnabled(true);
			ivjJRBDirectionD.setEnabled(true);
		}

		else if (MeasureDefineTableModel.DIRECTION[2].equals(hbprop)) {
			ivjJRBDirectionD.setSelected(true);
			ivjJRBNoDirection.setEnabled(false);
			ivjJRBDirectionJ.setEnabled(true);
			ivjJRBDirectionD.setEnabled(true);
		} else {
			ivjJRBNoDirection.setSelected(true);
			ivjJRBNoDirection.setEnabled(true);
			ivjJRBDirectionJ.setEnabled(true);
			ivjJRBDirectionD.setEnabled(true);
		}
		//设置指标抵消类型
		hbprop = (String) m_measureDefineTableModel.getValueAt(row, MeasureDefineTableModel.DXTYPE_COLUMN);
		if (MeasureDefineTableModel.DXTYPE[1].equals(hbprop)) {
			ivjJRBIsDXType.setSelected(true);
		} else if (MeasureDefineTableModel.DXTYPE[0].equals(hbprop)) {
			ivjJRBNotDXType.setSelected(true);
		} else {
			ivjJRBNoDXType.setSelected(true);
		}
		//设置可编辑状态
		
		// //指标编码
		ivjJTFMeasureName.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.NAME_COLUMN)); //指标名称
		ivjJTFMeasureLength.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.LENGTH_COLUMN)); //指标长度，并禁用
		ivjJTFNote.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.NOTE_COLUMN)); //指标说明，并禁用
		ivjJCBCodeReference.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.CODEREFERENCE_COLUMN));
//		ivjJCBMeasureAttribute.setEnabled(m_measureDefineTableModel.isCellEditable(row,
//				MeasureDefineTableModel.ATTRIBUTE_COLUMN));
		ivjJCBMeasureType.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.TYPE_COLUMN));
		ivjJBMeasureRef.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.MEASURE_REF_COLUMN)); //指标参照
//		ivjJBKeywordRef.setEnabled(m_measureDefineTableModel.isCellEditable(row,
//				MeasureDefineTableModel.KEY_REF_COLUMN)); //关键字参照
		ivjJRBDirectionD.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.DIRECTION_COLUMN));
		ivjJRBDirectionJ.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.DIRECTION_COLUMN));
		ivjJRBNoDirection.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.DIRECTION_COLUMN));
		ivjJRBIsDXType.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.DXTYPE_COLUMN));
		ivjJRBNotDXType.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.DXTYPE_COLUMN));
		ivjJRBIsHBMeasure.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.HEBING_COLUMN));
		ivjJRBIsNotHBMeasure.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.HEBING_COLUMN));
		StringBuffer msg = new StringBuffer();
		if (vo.getFlag().booleanValue()) {
			msg.append(StringResource.getStringResource("miufo1001466")); //当前选中指标的名称：\n\r
		} else if (vo.getKeyFlag().booleanValue()) {
			msg.append(StringResource.getStringResource("miufo1001467")); //当前选中关键字的名称：\n\r
		}
		msg.append(ivjJTFMeasureName.getText());
		setStateMessage(msg.toString(), false);
		//设置当前选中的行，因为在IE中浏览时，选中的行经过编辑后会恢复为选中状态，但此时因该还是选中的，所以，需要强制设置选中的行
		setTableSelectRow(currentRow);
	}
	private void setValueToMeasureVO(EventObject event) {
		//如果当前没有选定行，则不作处理
		if (currentRow < 0) {
			return;
		}
		//在设置值的时候，设置isDialog为true,保证不会重复弹出对话框
		//设置完之后，在恢复该值
		isDialog = true;
		//取出当前行对应的指标
		MeasurePosVO vo = m_measureDefineTableModel.getSelectMeasurePosVO(currentRow);
		//取出vo的扩展属性，

		try {
			//标记用户是否真正的修改了指标的某些属性，以此来更改指标的状态
			boolean isedited = false;
			//根据用户所修改的组件来对当前指标进行赋值
			if (event.getSource() == ivjJTFMeasureName) {
				String name = ivjJTFMeasureName.getText().trim();
				m_measureDefineTableModel.setValueAt(name, currentRow, MeasureDefineTableModel.NAME_COLUMN);
				setStateMessage("", false);
				isedited = true;

			} else if (event.getSource() == ivjJTFMeasureLength) {
				//指标长度要验证合法性，有错要提示用户
				String length = ivjJTFMeasureLength.getText().trim();
				m_measureDefineTableModel
						.setValueAt(length, currentRow,
								MeasureDefineTableModel.LENGTH_COLUMN);
				ivjJStatusArea.setText("");
				isedited = true;
			} else if (event.getSource() == ivjJTFNote) {
				//指标说明的设置
				String note = ivjJTFNote.getText().trim();
				m_measureDefineTableModel.setValueAt(note, currentRow, MeasureDefineTableModel.NOTE_COLUMN);
				isedited = true;
			} else if (event.getSource() == ivjJCBCodeReference
					|| event.getSource() == ivjJCBCodeReference.getModel()) {
				//如果编码引用复选框的选择改变，则根据显示的编码引用名称找到对应的编码引用ID进行保存
				String codeRef = (String) ivjJCBCodeReference.getSelectedItem();
				m_measureDefineTableModel.setValueAt(codeRef, currentRow,
						MeasureDefineTableModel.CODEREFERENCE_COLUMN);

			} else if (event.getSource() == ivjJCBMeasureAttribute
					|| event.getSource() == ivjJCBMeasureAttribute.getModel()) {
				//如果单位为%,则需要判断指标是否为百分比指标，在扩展属性中增加百分比指标一项以标识
				Object attribute = ivjJCBMeasureAttribute.getSelectedItem();
//				m_measureDefineTableModel.setValueAt(attribute, currentRow,
//						MeasureDefineTableModel.ATTRIBUTE_COLUMN);
				isedited = true;
			} else if (event.getSource() == ivjJCBMeasureType
					|| event.getSource() == ivjJCBMeasureType.getModel()) {
				//如果指标类型是数值或浮动数值，则单位可选，编码引用不可选，指标长度不可定义
				//如果指标类型是字符或浮动字符，则单位不可选，编码引用不可选，指标长度可定义
				//如果指标类型是编码或浮动编码，则单位不可选，编码引用可选，指标长度不可定义
				Object type = ivjJCBMeasureType.getSelectedItem();
				m_measureDefineTableModel.setValueAt(type, currentRow, MeasureDefineTableModel.TYPE_COLUMN);
				setValueToComponent(currentRow);
				isedited = true;
			} else if (event.getSource() == ivjJRBNoDirection) {
				//当指标为合并指标时，方向为必选项，
				if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING) {
					m_measureDefineTableModel.setValueAt(MeasureDefineTableModel.DIRECTION[0], currentRow,
							MeasureDefineTableModel.DIRECTION_COLUMN);
					ivjJRBNoDirection.setSelected(true);
					isedited = true;
				}
			} else if (event.getSource() == ivjJRBDirectionD) {

				m_measureDefineTableModel.setValueAt(MeasureDefineTableModel.DIRECTION[2], currentRow,
						MeasureDefineTableModel.DIRECTION_COLUMN);

				ivjJRBIsHBMeasure.setSelected(true);
				ivjJCBMeasureType.setEnabled(false);
				isedited = true;
			} else if (event.getSource() == ivjJRBDirectionJ) {
				m_measureDefineTableModel.setValueAt(MeasureDefineTableModel.DIRECTION[1], currentRow,
						MeasureDefineTableModel.DIRECTION_COLUMN);
				ivjJRBIsHBMeasure.setSelected(true);
				ivjJCBMeasureType.setEnabled(false);
				isedited = true;
			} else if (event.getSource() == ivjJRBIsDXType) {
				//如果当前指标不是合并指标，但是选择了该类型，那么，该指标自动改为合并指标，
				//此时，默认方向为借方向，并设置扩展属性为抵消类型
				m_measureDefineTableModel.setValueAt(MeasureDefineTableModel.DXTYPE[1], currentRow,
						MeasureDefineTableModel.DXTYPE_COLUMN);
				ivjJRBIsHBMeasure.setSelected(true);
				ivjJCBMeasureType.setEnabled(false);
				isedited = true;
			} else if (event.getSource() == ivjJRBNotDXType) {
				//如果当前指标是合并指标，选择了该类型，设置扩展属性为非抵消类型
				m_measureDefineTableModel.setValueAt(MeasureDefineTableModel.DXTYPE[0], currentRow,
						MeasureDefineTableModel.DXTYPE_COLUMN);
				isedited = true;
			} else if (event.getSource() == ivjJRBIsHBMeasure) {
				//如果指标被设为合并指标，则指标的类型默认为数值类型（类型复选框不允许编辑），默认为借方向，抵消类型
				m_measureDefineTableModel.setValueAt(MeasureDefineTableModel.HEBING[1], currentRow,
						MeasureDefineTableModel.HEBING_COLUMN);
				ivjJCBMeasureType.setSelectedIndex(0);
				ivjJCBMeasureType.setEnabled(false);
				isedited = true;
			} else if (event.getSource() == ivjJRBIsNotHBMeasure) {
				//如果指标被设为非合并指标，则指标类型复选框允许编辑，默认为无方向，非抵消类型
				m_measureDefineTableModel.setValueAt(MeasureDefineTableModel.HEBING[0], currentRow,
						MeasureDefineTableModel.HEBING_COLUMN);
				ivjJCBMeasureType.setEnabled(true);
				isedited = true;
			}
			if (isedited) {
				if (vo.getState() == MeasurePosVO.MEASURE_TABLE_S_ORIGINAL) {
					vo.setState(MeasurePosVO.MEASURE_TABLE_S_UPDATE);
				}
			}
			setStateMessage(StringResource.getStringResource("miufo1001469")
					+ ivjJTFMeasureName.getText(), false); //"当前选中指标：\n\r"
		} catch (RuntimeException ce) {
			setStateMessage(ce.getMessage(), true);
		}

		//设置完之后，恢复isDialog为true
		isDialog = false;

	}
	/**
	 * 供选中多行时，对多个指标设值用 colum--设置的指标属性对应于m_table中的列号，obj--该属性的值
	 */
	private void setValusByRows(int colum, Object obj) {

		//如果没有选中多行或只选中一行，则不作处理，此处主要是为了防止在该类中的错误调用
		if (rows == null || rows.length < 2) {
			return;
		}
		for (int i = 0; i < rows.length; i++) {
			m_measureDefineTableModel.setValueAt(obj, rows[i], colum);
		}

	}
	public void valueChanged(ListSelectionEvent event) {
	}
	private void setSelFirstRow(){
		m_table.getSelectionModel().setSelectionInterval(0, 0);
		ListSelectionEvent e = new ListSelectionEvent(m_table, 0, -1, false);
		m_table.valueChanged(e);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"


 