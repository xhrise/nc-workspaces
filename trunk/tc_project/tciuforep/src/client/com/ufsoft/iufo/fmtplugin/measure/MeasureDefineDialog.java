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
 * ָ����ȡ�Ի���
 * @author��������
 */

public class MeasureDefineDialog extends UfoDialog		implements
			ActionListener,
			MouseListener, 
			FocusListener,
			ListDataListener,
			KeyListener,DialogRefListener,IUfoContextKey{
	//��ǩ
	private JLabel ivjJLCodeReference = null; //�������ñ�ǩ
	private JLabel ivjJLDirection = null; //�� ���ǩ
	private JLabel ivjJLDXType = null; //�������ͱ�ǩ
	private JLabel ivjJLIsHBMeasure = null; //�Ƿ��Ǻϲ�ָ���ǩ
	private JLabel ivjJLMeasureAttribute = null; //ָ�굥λ��ǩ
	private JLabel ivjJLMeasureLength = null; //ָ�곤�ȱ�ǩ
	private JLabel ivjJLMeasureName = null; //ָ�����Ʊ�ǩ
	private JLabel ivjJLMeasureNote = null; //ָ��˵����ǩ
	private JLabel ivjJLMeasureType = null; //ָ�����ͱ�ǩ

	//ʵ�����(���ڵ�����MeasureData�е��к����Ӧ)
	private JButton ivjJBMeasureRef = null; //ָ����հ�ť
	private JButton ivjJBKeywordRef = null; //�ؼ��ֲ��հ�ť
	private JTextField ivjJTFMeasureName = null; //ָ�����Ʊ༭��
	private JComboBox ivjJCBMeasureType = null; //ָ�����͸�ѡ��
	private JTextField ivjJTFMeasureType = null; //ָ��������ʾ��
	private JComboBox ivjJCBMeasureAttribute = null; //ָ�굥λ��ѡ��
	private JTextField ivjJTFMeasureLength = null; //ָ�곤�ȱ༭��
	private JComboBox ivjJCBCodeReference = null; //�������ø�ѡ��
	private JTextField ivjJTFNote = null; //˵ ���༭��
	private JRadioButton ivjJRBIsNotHBMeasure = null; //�Ǻϲ�ָ�굥ѡ��ť
	private JRadioButton ivjJRBIsHBMeasure = null; //�ϲ�ָ�굥ѡ��ť
	private JRadioButton ivjJRBNoDirection = null; //�޷���ѡ��ť
	private JRadioButton ivjJRBDirectionD = null; //������ѡ��ť
	private JRadioButton ivjJRBDirectionJ = null; //�跽��ѡ��ť
	private JRadioButton ivjJRBIsDXType = null; //�������͵�ѡ��ť
	private JRadioButton ivjJRBNotDXType = null; //�ǵ������͵�ѡ��ť
	private JRadioButton ivjJRBNoDXType = null; //�Ǻϲ�ָ��ĵ������͵�ѡ��ť

	//���������ȷ����ȡ����ť
	private UITabbedPane jTabbedPane = null; //���
	private JLabel ivjJLabelTabbedTitle = null;//���Ա༭��ǩ
	private JPanel ivjUfoDialogContentPane = null; //�Ի����������
	private JPanel ivjJPanel = null; //��ȷ��ȡ�������
	private JTextArea ivjJStatusArea = null; //��״̬��ʾ�����
	private JCheckBox ivjJCheckBox=null;//�Ƿ���λ��Ϊָ������
	private JScrollPane ivjJStatusPane = null; //��״̬��ʾ�����
	private JLabel ivjJLabelStatusTitle = null;//״̬��ʾ��ǩ
	private JPanel ivjJPanelBase = null; //���������ԡ����
	private JPanel ivjJPanelExtend = null; //����չ���ԡ����
	private JScrollPane ivjJSPMeasureList = null; //��ָ������
	private JButton ivjJButtonCancel = null; //��ȡ������ť
	private JButton ivjJButtonOK = null; //��ȷ������ť
	//�Զ�������
	private JTable m_table; //װ��m_data���ݣ��������
	private MeasureDefineTableModel m_measureDefineTableModel; //ָ������
	private int currentRow = -1; //ָ���б���ÿһ��ָ����m_table������Ӧ����
	private boolean isSelectFromMTableOrRadio = false; //����Ƿ��Ǵ�m_table��ѡ��һ�л����Ƕ�RadioButton��ѡ��Ŀ���Ǽ��ٳ����ж�contentsChanged�����Ĵ������
	private int[] rows; //��¼ÿһ��m_table�ж�ѡ������ָ�����ڵ��У�������Ϻ�Ҫ���
	private int DEFAULT_INT = -1; //���Ĭ�ϵ������ʾ,������CombBox��ѡ��index==-1
	private String DEFAULT_STRING = ""; //���Ĭ�ϵ������ʾ,������TextField������Text==""
	private Hashtable propsForRows; //��ѡʱָ�����չ����
	private boolean isDialog = false; //��ʾ�Ƿ������ڵ�����ʾ�����ɵĽ���ʧȥ����ֹ�ظ��Ķԡ�����ʧȥ����һ�¼����д���������Ի���
	private boolean isCtrlPressed = false; //��ʾctrl���Ƿ���
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
		//���ظ��෽������ʵ���������ϼ�ͷʱ�������������������ʾ����
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
			//���ø�����ʾ����
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
	 * CreateMeasureDialog ������ע�⡣
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
		// @edit by ll at 2009-5-14,����04:18:11 
		setSelFirstRow();
	}
	

	public void actionPerformed(ActionEvent event) {
		try {
			if (event.getSource() == ivjJButtonOK) {
				m_measureDefineTableModel.checkAnaRepMeasure();
//				m_measureDefineTableModel.checkDynAreaKeys();
//				m_measureDefineTableModel.saveKeyWord();
				setResult(ID_OK);
				//�ر�ָ����ȡ����
				close();
			} else if (event.getSource() == ivjJButtonCancel) {
				//�ر�ָ����ȡ����
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
	 * �˴����뷽�������� �������ڣ�(2003-4-15 10:27:28)
	 */
	public void addHelp() {
		javax.help.HelpBroker hb = ResConst.getHelpBroker();
		if (hb == null)
			return;
		hb.enableHelpKey(getContentPane(), "TM_Data_CreateMeas", null);
	}
	public void beforeDialogClosed(Component refDlg) {
		//ֻ�е�ǰע��Ĵ��ڲ�����������ȡ��ע�����
		if (this.refDialog == refDlg) {
			//��ע������ÿ�
			refcomp = null;
			refDialog = null;
		}
	}
	/**����combox���ݵı仯����Ҫ��ָ�����ͣ�ָ�굥λ���ݵı仯*/
	public void contentsChanged(ListDataEvent lde) {
		if (!isSelectFromMTableOrRadio) {
			if (rows == null || rows.length == 1)
				setValueToMeasureVO(lde);
			else
				setValuesToMeasureVOs(lde);
		}
	}
	/**
	 * �˴����뷽�������� �������ڣ�(2003-9-1 20:53:41)
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
	 * ���� JBReference ����ֵ��
	 * 
	 * @return javax.swing.JButton
	 */
	/* ���棺�˷������������ɡ� */
//	private javax.swing.JButton getJBKeywordRef() {
//		if (ivjJBKeywordRef == null) {
//			try {
//				ivjJBKeywordRef = new IUFOButton();
//				ivjJBKeywordRef.setName("ivjJBKeywordRef");
//
//				ivjJBKeywordRef.setText(StringResource
//						.getStringResource("miufo1001056")); //"�ؼ��ֲ���"
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
	 * ���� JBReference ����ֵ��
	 * 
	 * @return javax.swing.JButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JButton getJBMeasureRef() {
		if (ivjJBMeasureRef == null) {
			try {
				ivjJBMeasureRef = new UIButton();
				ivjJBMeasureRef.setName("ivjJBMeasureRef");

				ivjJBMeasureRef.setText(StringResource
						.getStringResource("miufo1000770")); //"ָ�����"
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
	 * ���� JButtonCancel ����ֵ��
	 * 
	 * @return javax.swing.JButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JButton getJButtonCancel() {
		if (ivjJButtonCancel == null) {
			try {
				ivjJButtonCancel = new UIButton();
				ivjJButtonCancel.setName("JButtonCancel");

				ivjJButtonCancel.setText(StringResource
						.getStringResource("miufo1000757")); //"ȡ ��"
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
	 * ���� JButtonOK ����ֵ��
	 * 
	 * @return javax.swing.JButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JButton getJButtonOK() {
		if (ivjJButtonOK == null) {
			try {
				ivjJButtonOK = new UIButton();
				ivjJButtonOK.setName("JButtonOK");

				ivjJButtonOK.setText(StringResource
						.getStringResource("miufo1000758")); //"ȷ ��"
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
	 * ���� JCBCodeReference ����ֵ��
	 * 
	 * @return javax.swing.JComboBox
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JComboBox getJCBCodeReference() {
		if (ivjJCBCodeReference == null) {
			try {
				ivjJCBCodeReference = new UIComboBox();
				ivjJCBCodeReference.setName("JCBCodeReference");
				ivjJCBCodeReference.setBounds(new Rectangle(474, 42, 89, 20));
				ivjJCBCodeReference
						.setNextFocusableComponent(ivjJCBMeasureAttribute);
				//��ʼ���������ø�ѡ��
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
	 * ���� JCBMeasureAttribute ����ֵ��
	 * 
	 * @return javax.swing.JComboBox
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JComboBox getJCBMeasureAttribute() {
		if (ivjJCBMeasureAttribute == null) {
			try {
				ivjJCBMeasureAttribute = new UIComboBox();
				ivjJCBMeasureAttribute.setName("JCBMeasureAttribute");
				ivjJCBMeasureAttribute.setBounds(257, 42, 75, 20);
				ivjJCBMeasureAttribute
						.setNextFocusableComponent(ivjJTFMeasureLength);
				//��ʼ��ָ�굥λ��ѡ��
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
	 * ���� JCBMeasureType ����ֵ��
	 * 
	 * @return javax.swing.JComboBox
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JComboBox getJCBMeasureType() {
		if (ivjJCBMeasureType == null) {
			try {
				ivjJCBMeasureType = new UIComboBox();
				ivjJCBMeasureType.setName("JCBMeasureType");
				ivjJCBMeasureType.setBounds(474, 12, 89, 20);
				ivjJCBMeasureType
						.setNextFocusableComponent(ivjJCBCodeReference);
				//��ʼ��ָ�����͸�ѡ��
				for (int i = 0; i < MeasureDefineTableModel.TYPES.length; i++) {
					if (i == 2) {
						//������뻺�����пɹ�ѡ��ı��룬������ָ�궨��Ϊ�������ͣ���������ָ�궨��Ϊ��������
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
	 * ���� JLCodeReference ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JLabel getJLCodeReference() {
		if (ivjJLCodeReference == null) {
			try {
				ivjJLCodeReference = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLCodeReference.setName("JLCodeReference");

				ivjJLCodeReference.setBounds(new Rectangle(386, 44, 80, 20));
				ivjJLCodeReference.setText(StringResource
						.getStringResource("miufo1001442")); //"����ѡ��"
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLCodeReference;
	}
	/**
	 * ���� JLDirection ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JLabel getJLDirection() {
		if (ivjJLDirection == null) {
			try {
				ivjJLDirection = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLDirection.setName("JLDirection");

				ivjJLDirection.setText(StringResource
						.getStringResource("miufo1001443")); //"�ϲ�����"
				ivjJLDirection.setBounds(15, 33, 124, 16);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLDirection;
	}
	/**
	 * ���� JLDXType ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JLabel getJLDXType() {
		if (ivjJLDXType == null) {
			try {
				ivjJLDXType = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLDXType.setName("JLDXType");

				ivjJLDXType.setText(StringResource
						.getStringResource("miufo1001444")); //"�ڳ��ϲ�ָ�꣺"
				ivjJLDXType.setBounds(15, 55, 124, 16);
			} catch (java.lang.Throwable ivjExc) {				
				handleException(ivjExc);
			}
		}
		return ivjJLDXType;
	}
	/**
	 * ���� JLIsHBMeasure ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JLabel getJLIsHBMeasure() {
		if (ivjJLIsHBMeasure == null) {
			try {
				ivjJLIsHBMeasure = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLIsHBMeasure.setName("JLIsHBMeasure");

				ivjJLIsHBMeasure.setText(StringResource
						.getStringResource("miufo1001445")); //"�Ƿ��Ǻϲ�ָ�꣺"
				ivjJLIsHBMeasure.setBounds(15, 10, 124, 16);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLIsHBMeasure;
	}
	/**
	 * ���� JLMeasureAttribute ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JLabel getJLMeasureAttribute() {
		if (ivjJLMeasureAttribute == null) {
			try {
				ivjJLMeasureAttribute = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLMeasureAttribute.setName("JLMeasureAttribute");

				ivjJLMeasureAttribute.setText(StringResource
						.getStringResource("miufo1001446")); //"ָ�굥λ��"
				ivjJLMeasureAttribute.setBounds(179, 82, 72, 20);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLMeasureAttribute;
	}
	/**
	 * ���� JLMeasureLength ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JLabel getJLMeasureLength() {
		if (ivjJLMeasureLength == null) {
			try {
				ivjJLMeasureLength = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLMeasureLength.setName("JLMeasureLength");

				ivjJLMeasureLength.setText(StringResource
						.getStringResource("miufo1001447")); //"ָ�곤�ȣ�"
				ivjJLMeasureLength.setBounds(576, 12, 72, 20);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLMeasureLength;
	}
	/**
	 * ���� JLMeasureName ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JLabel getJLMeasureName() {
		if (ivjJLMeasureName == null) {
			try {
				ivjJLMeasureName = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLMeasureName.setName("JLMeasureName");

				ivjJLMeasureName.setText(StringResource
						.getStringResource("miufo1001448")); //"ָ�����ƣ�"
				ivjJLMeasureName.setBounds(3, 12, 72, 20);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLMeasureName;
	}
	/**
	 * ���� JLMeasureNote ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JLabel getJLMeasureNote() {
		if (ivjJLMeasureNote == null) {
			try {
				ivjJLMeasureNote = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLMeasureNote.setName("JLMeasureNote");

				ivjJLMeasureNote.setText(StringResource
						.getStringResource("miufo1001449")); //"˵ ����"
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

				ivjJLabelTabbedTitle.setText(StringResource.getStringResource("miufo1001458")); //"���Ա༭"
				ivjJLabelTabbedTitle.setBounds(7, 245, 97, 22);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabelTabbedTitle;
	}
	
	/**
	 * ���� JLMeasureType ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JLabel getJLMeasureType() {
		if (ivjJLMeasureType == null) {
			try {
				ivjJLMeasureType = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
				ivjJLMeasureType.setName("JLMeasureType");

				ivjJLMeasureType.setText(StringResource
						.getStringResource("miufo1001450")); //"ָ�����ͣ�"
				ivjJLMeasureType.setBounds(386, 12, 80, 20);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLMeasureType;
	}
	/**
	 * ���� JPanel1 ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� JPanelBase ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� JPanelExtend ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� JRBDirectionD ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JRadioButton getJRBDirectionD() {
		if (ivjJRBDirectionD == null) {
			try {
				ivjJRBDirectionD = new UIRadioButton();
				ivjJRBDirectionD.setName("JRBDirectionD");

				ivjJRBDirectionD.setText(StringResource
						.getStringResource("miufo1001451")); //"������"
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
	 * ���� JRBDirectionJ ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JRadioButton getJRBDirectionJ() {
		if (ivjJRBDirectionJ == null) {
			try {
				ivjJRBDirectionJ = new UIRadioButton();
				ivjJRBDirectionJ.setName("JRBDirectionJ");

				ivjJRBDirectionJ.setText(StringResource
						.getStringResource("miufo1001452")); //"�跽��"
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
	 * ���� JRBIsDXType ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JRadioButton getJRBIsDXType() {
		if (ivjJRBIsDXType == null) {
			try {
				ivjJRBIsDXType = new UIRadioButton();
				ivjJRBIsDXType.setName("JRBIsDXType");

				ivjJRBIsDXType.setText(StringResource
						.getStringResource("miufo1001453")); //"�ڳ��ϲ�ָ��"
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
	 * ���� JRBIsHBMeasure ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JRadioButton getJRBIsHBMeasure() {
		if (ivjJRBIsHBMeasure == null) {
			try {
				ivjJRBIsHBMeasure = new UIRadioButton();
				ivjJRBIsHBMeasure.setName("JRBIsHBMeasure");

				ivjJRBIsHBMeasure.setText(StringResource
						.getStringResource("miufopublic311")); //"��"
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
	 * ���� JRBIsNotMeasure ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JRadioButton getJRBIsNotHBMeasure() {
		if (ivjJRBIsNotHBMeasure == null) {
			try {
				ivjJRBIsNotHBMeasure = new UIRadioButton();
				ivjJRBIsNotHBMeasure.setName("JRBIsNotMeasure");

				ivjJRBIsNotHBMeasure.setText(StringResource
						.getStringResource("miufopublic312")); //"��"
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
	 * ���� JRBNotDXType ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JRadioButton getJRBNotDXType() {
		if (ivjJRBNotDXType == null) {
			try {
				ivjJRBNotDXType = new UIRadioButton();
				ivjJRBNotDXType.setName("JRBNotDXType");

				ivjJRBNotDXType.setText(StringResource
						.getStringResource("miufo1001454")); //"���ڳ��ϲ�ָ��"
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
	 * ���� JSPanelMeasureList ����ֵ��
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JScrollPane getJSPMeasureList() {
		if (ivjJSPMeasureList == null) {
			try {
				m_table = new MeasureTable();//m_data);
				ivjJSPMeasureList = new UIScrollPane(m_table);
				ivjJSPMeasureList.setName("JSPMeasureList");
				ivjJSPMeasureList.setBounds(7, 11, 760, 231);
				ivjJSPMeasureList.setNextFocusableComponent(ivjJTFMeasureName);
				//����ָ���б�

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
							//������ֵ���ε��ͬһ��ʱ������������ظ����������
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
						//ʵ���Զ���ʧȥ����ʱ�̣����и�ֵ
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
				//ָ������������
				TableColumnModel tcm = m_table.getColumnModel();
				TableColumn typetc = tcm.getColumn(MeasureDefineTableModel.TYPE_COLUMN);
				JComboBox comboBox1 = (JComboBox) ((DefaultCellEditor) typetc
						.getCellEditor()).getComponent();

				for (int i = 0; i < MeasureDefineTableModel.TYPES.length; i++)
					comboBox1.addItem((String) (MeasureDefineTableModel.TYPES[i]));

				//��λ������
//				typetc = tcm.getColumn(MeasureDefineTableModel.ATTRIBUTE_COLUMN);
//				comboBox = (JComboBox) ((DefaultCellEditor) typetc
//						.getCellEditor()).getComponent();
//				comboBox.addItem(MeasureDefineTableModel.UNITS[2]);
//				for (int i = 0; i < MeasureDefineTableModel.UNITS.length; i++) {
//					if (i != 2)
//						comboBox.addItem(MeasureDefineTableModel.UNITS[i]);
//				}

				//��������������
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
					//�ϲ�ָ��������
					typetc = tcm.getColumn(MeasureDefineTableModel.HEBING_COLUMN);
					comboBox = (JComboBox) ((DefaultCellEditor) typetc
							.getCellEditor()).getComponent();
					for (int i = 0; i < MeasureDefineTableModel.HEBING.length; i++)
						comboBox.addItem((String) (MeasureDefineTableModel.HEBING[i]));

					//����������
					typetc = tcm.getColumn(MeasureDefineTableModel.DIRECTION_COLUMN);
					comboBox = (JComboBox) ((DefaultCellEditor) typetc
							.getCellEditor()).getComponent();
					for (int i = 1; i < MeasureDefineTableModel.DIRECTION.length; i++)
						comboBox.addItem((String) (MeasureDefineTableModel.DIRECTION[i]));

					//��������������
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
	 * @i18n miufohbbb00206=ʹ�õ�Ԫ������Ϊָ������
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
	 * ���� JPanel1 ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
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

				ivjJLabelStatusTitle.setText(StringResource.getStringResource("miufo1001455")); //"״̬����ʾ"
				ivjJLabelStatusTitle.setBounds(7, 425, 97, 22);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabelStatusTitle;
	}
	/**
	 * ���� JPanel1 ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
	private JScrollPane getJStatusPanel() {
		if (ivjJStatusPane == null) {
			try {
				ivjJStatusPane = new UIScrollPane(getJStatusArea());

				ivjJStatusPane.setAutoscrolls(true);
				ivjJStatusPane.setName("ivjJStatusPane");

				ivjJStatusPane.setBounds(7, 450, 760, 65);
//				Border etched = BorderFactory.createEtchedBorder();
//				Border title = BorderFactory.createTitledBorder(etched,
//						StringResource.getStringResource("miufo1001455")); //"״̬����ʾ"
				ivjJStatusPane.setBorder(null);
				
				
			} catch (java.lang.Throwable ivjExc) {
				
				
				handleException(ivjExc);
			}
		}
		return ivjJStatusPane;
	}
	/**
	 * ���� JPanel1 ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
	private UITabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			try {
				jTabbedPane = new UITabbedPane();
				jTabbedPane.setName("jTabbedPane");

				jTabbedPane.setBounds(7, 270, 760, 120);
				jTabbedPane.setBorder(null);
				jTabbedPane.insertTab(StringResource
						.getStringResource("miufo1001456"), null,
						getJPanelBase(), null, 0); //"��������"
				getJPanelExtend();//��ֹ���ֿ�ָ������
				if (LicenseValue.isHBBB()) {
					jTabbedPane.insertTab(StringResource
							.getStringResource("miufo1001457"), null,
							getJPanelExtend(), null, 1); //"��չ����"
				}
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return jTabbedPane;
	}
	/**
	 * ���� JTFMeasureLength ����ֵ��
	 * 
	 * @return javax.swing.JTextField
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� JTFMeasureName ����ֵ��
	 * 
	 * @return javax.swing.JTextField
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� JCBMeasureType ����ֵ��
	 * 
	 * @return javax.swing.JComboBox
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� JTFNote ����ֵ��
	 * 
	 * @return javax.swing.JTextField
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� UfoDialogContentPane ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

	}
	/**
	 * �����û�ѡ��Ķ���ָ����д��� ʹ��ͬ��������Ϊ��ָ������ԣ���ͬ����������Ϊ��ʼֵ�� Ŀ����Ϊ�����û�ѡ�����ʱ�������������ʾ
	 */
	private MeasurePosVO initComponentByRows() {
		//���û��ѡ�ж��л�ֻѡ��һ�У����������˴���Ҫ��Ϊ�˷�ֹ�ڸ����еĴ������
		if (rows == null || rows.length < 2) {
			return null;
		}
		StringBuffer measName = new StringBuffer();
		MeasurePosVO vo = (MeasurePosVO) m_measureDefineTableModel.getSelectMeasurePosVO(
				rows[0]).clone();
//		if (m_measureDefineTableModel.isKeywordRow(rows[0]))
//			return null;
		MeasureVO mvo = vo.getMeasureVO();
		//����ǲ���ָ�꣬������༭
		if (vo.isRefMeasure()) {
			return vo;
		}
		measName.append(vo.getMeasureVO().getName());

		for (int i = 1; i < rows.length; i++) {
//			if (m_measureDefineTableModel.isKeywordRow(rows[i]))
//				return null;
			MeasurePosVO voRow = (MeasurePosVO) m_measureDefineTableModel.getSelectMeasurePosVO(rows[i]);
			//����ȫ����ָ�����ƣ���ʾ��״̬��ʾ����
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
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
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
			//������ֵ���ε��ͬһ��ʱ������������ظ����������
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
			//������ֵ���ε��ͬһ��ʱ������������ظ����������
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
				//����ǵ����ȡ��ָ����գ���ˢ����������������������
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
			//������ֵ���ε��ͬһ��ʱ������������ظ����������
			if (row == currentRow) {
				return;
			}

			//����ǵ����ȡ��ָ����գ���ˢ����������������������
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
	 * ��Ӧ�ؼ��ֲ��� �������ڣ�(2003-8-29 10:12:03)
	 */
//	private void onKeyRef() {
//		//���û��ѡ��һ�н��б༭�����������
//		if (currentRow < 0) {
//			return;
//		}
//		//���������б༭���Ӧ������ֵ
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
//		//��ʱ��ؼ��ִ��������������ʱ��ؼ��֣����ӱ�ֻ�ܲ���KeyVO.getTimeKeyIndex()�ȵ�ǰ�ؼ���ֵ��Ĺؼ���
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
	 * ��Ӧָ����� �������ڣ�(2003-8-29 10:12:03)
	 * @i18n miufo1004043=�޸�ָ����ջ�ɾ����ǰ�����ݣ�
	 */
	private void onMeasureRef() {
		//���û��ѡ��һ�н��б༭�����������
		if (currentRow < 0) {
			UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1004044"), parent);
			return;
		}
		//���������б༭���Ӧ������ֵ
		TableCellEditor editor = m_table.getCellEditor();
		if (editor != null) {
			Object value = editor.getCellEditorValue();
			m_measureDefineTableModel.setValueAt(value, m_table.getEditingRow(), m_table
					.getEditingColumn());
		}
		
		//modified by chxw, 2007-04-28, �޽���ָ����յ�ָ������Ѵ��ڣ�����ʾ�û�����
		MeasurePosVO measurePosVO = m_measureDefineTableModel.getSelectMeasurePosVO(currentRow);
//		if(measurePosVO.getMeasureVO().getDbtable() == null){
		//modify by wangyga 2008-6-26 ����˵�Ԫλ����ָ�꣬����ʾ"�޸�ָ����ջ�ɾ����ǰ�����ݣ�"�����򣬲���ʾ				
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
//					.getStringResource("miufo1001459"); //"δ����ؼ��֣����ܲ���ָ�꣡"
//			throw new CommonException(erromessage);
//		}
		//�õ����յ�ָ��
		MeasureVO mvo = null;
		final ReportVO rep = CacheProxy.getSingleton().getReportCache()
				.getByPks(new String[]{m_measureDefineTableModel.getReportID()})[0];

//		/------------------		//FIXME: �˴����ָ�����ʱ�Ĺؼ�������  ------------------------------
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
		            String erromessage = StringResource.getStringResource("miufo1001460"); //"��̬����û�ж���ؼ��֣����ܲ���ָ�꣡"
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
			String erromessage = StringResource.getStringResource("miufo1001461"); //"����û�ж���ؼ��֣����ܲ���ָ�꣡"
			throw new CommonException(erromessage);
		}

		Object repManagerObj = context.getAttribute(REP_MANAGER); 
		final boolean bRepMgr = repManagerObj == null ? false : Boolean.parseBoolean(repManagerObj.toString());
		final String strUserPK = (String)context.getAttribute(CUR_USER_ID);
		
		//��������ԭ���е��Լ��¼ӵ�ָ��VO��code���浽excludeMeasures����ֹ���ظ���������
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
					//�����ڹؼ��ϲ���ָ��
					if (vo.getKeyFlag().booleanValue()) {
						String strError = StringResource
								.getStringResource("miufo1001462"); //"���ܽ��ؼ��ֲ���Ϊָ��!"
						throw new CommonException(strError);
					}
					if (mvo == null) {
						return;
					}
					//�жϸ�ָ��ʱ�������ù������ù�����û���ʾ
					int refRes = m_measureDefineTableModel.isReferenced(mvo, vo);
					if (refRes != MeasureDefineTableModel.MEASREF_NO_ERR) {
						String erromessage;
						if (refRes == MeasureDefineTableModel.MEASREF_MEAS_REPETITION_ERR) {
							erromessage = StringResource
									.getStringResource("miufo1001463"); //"�Ѿ����ù���ָ�꣬�����ٴ����ã�����ͨ����Ԫ��ʽʵ�֣�"
							throw new CommonException(erromessage);
						} else if (refRes == MeasureDefineTableModel.MEASREF_NAME_REPETITION_ERR) {
							erromessage = StringResource
									.getStringResource("miufo1001464"); //"�Ѿ���ȡ��������ͬ��ָ�꣬���������ø����Ƶ�ָ�꣡"
							throw new CommonException(erromessage);
						}

					}
					//����ָ��Ϊ�²��յ�ָ��
					if (vo.getState() == MeasurePosVO.MEASURE_TABLE_S_ORIGINAL)
						vo.setState(MeasurePosVO.MEASURE_TABLE_S_UPDATE);
					vo.setRefMeasure(mvo);
					vo.setFlag(Boolean.TRUE);
				}
			}
			m_measureDefineTableModel.fireTableDataChanged();//ˢ����ʾ
			isSelectFromMTableOrRadio = true;
			setValueToComponent(currentRow);
			isSelectFromMTableOrRadio = false;
			setStateMessage("", false);
		} catch (RuntimeException ce) {
			setStateMessage(ce.getMessage(), true);
		}
	}
	//���ñ������ã���������˱������ã��򷵻�true����ʾָ��������Ѿ��������ˣ�false��ʾδ�޸�ָ������
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
	 * �˴����뷽�������� �������ڣ�(2003-9-3 16:30:15)
	 * 
	 * @param msg
	 *            java.lang.String
	 */
	public void setStateMessage(String msg, boolean isError) {
		if (isError) {
			getJStatusArea().setForeground(Color.red);
			msg = StringResource.getStringResource("miufo1001465") + msg; //"������ʾ��\r\n"
		} else
			getJStatusArea().setForeground(Color.blue);
		getJStatusArea().setText(msg);
	}
	/**
	 * �˴����뷽�������� �������ڣ�(2003-10-20 16:42:05)
	 * 
	 * @param selrow
	 *            int
	 */
	public void setTableSelectRow(int selrow) {
		m_table.setRowSelectionInterval(selrow, selrow);
	}
	/**
	 * �˴����뷽�������� �������ڣ�(2003-10-20 16:42:05)
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
	 * ������ֻ�������û���ѡ��Ķ���ָ��� ���͡���λ�����ȡ��������á�˵�����Ժ���չ���� ����Ϊ��ͬʱʹ�ã���ʱ���ܱ༭ָ������ƺͱ��루��ʾΪ�գ���
	 * ������ָ������ĳһ�����ϲ�һ��ʱ�������Զ�Ӧ�������ʾ�ÿգ� �����ʱ�û�Ϊ������ѡ��һ���µ�ֵ�������б�ѡ�е�ָ��ĸ����Ծ�����Ϊ������
	 */
	private void setValuesToComponent() {
		MeasurePosVO vo = initComponentByRows();
		//���vo��ָ�����ù����ģ�����������û�
		if (vo == null || vo.isRefMeasure()) {

			ivjJTFMeasureName.setText(""); //ָ�������ÿգ�������
			ivjJTFMeasureName.setEnabled(false);
			ivjJTFMeasureLength.setText(""); //ָ�곤���ÿգ�������
			ivjJTFMeasureLength.setEnabled(false);
			ivjJTFNote.setText(""); //ָ��˵���ÿգ�������
			ivjJTFNote.setEnabled(false);
			ivjJCBCodeReference.setSelectedIndex(-1); //���������ÿգ�������
			ivjJCBCodeReference.setEnabled(false);
			ivjJCBMeasureAttribute.setSelectedIndex(-1);//ָ�굥λ�ÿգ�������
			ivjJCBMeasureAttribute.setEnabled(false);
			ivjJCBMeasureType.setSelectedIndex(-1); //ָ�������ÿգ�������
			ivjJCBMeasureType.setEnabled(false);

			//����ָ����չ���Բ�����
			ivjJRBDirectionD.setEnabled(false);
			ivjJRBDirectionJ.setEnabled(false);
			ivjJRBNoDirection.setEnabled(false);
			ivjJRBIsDXType.setEnabled(false);
			ivjJRBNotDXType.setEnabled(false);
			ivjJRBIsHBMeasure.setEnabled(false);
			ivjJRBIsNotHBMeasure.setEnabled(false);
			return;

		}
		//����ָ�����,��ΪҪ��ʾ���ָ���ָ����룬���Բ���ʾ

		//����ָ������
		ivjJTFMeasureName.setText("");
		ivjJTFMeasureName.setEnabled(false);
		//����ָ�����
		ivjJBMeasureRef.setEnabled(false);
		//����ָ������
		int type = vo.getMeasureVO().getType();
		if (type == DEFAULT_INT) {
			ivjJCBMeasureType.setSelectedIndex(DEFAULT_INT);
		} else {
			ivjJCBMeasureType.setSelectedIndex(type);
		}
		ivjJCBMeasureType.setEnabled(true);
		//����ָ���������
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
		//����ָ�굥λ

		ivjJCBMeasureAttribute.setSelectedIndex(vo.getMeasureVO()
				.getAttribute());
		//����ָ�곤��
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
		//����ָ��˵��
		ivjJTFNote.setText(vo.getMeasureVO().getNote());
		//����ָ���Ƿ�ϲ�
		Hashtable props = vo.getMeasureVO().getProps();
		if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING) {
			ivjJRBIsHBMeasure.setSelected(true);
		} else {
			ivjJRBIsNotHBMeasure.setSelected(true);
		}
		//����ָ�귽��
		if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING) {
			if (HBBBMeasParser.getDirection(props) == 1)
				ivjJRBDirectionJ.setSelected(true);
			else if (HBBBMeasParser.getDirection(props) == 2)
				ivjJRBDirectionD.setSelected(true);
		} else {
			ivjJRBNoDirection.setSelected(true);
		}
		//����ָ���������
		if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING) {
			if (HBBBMeasParser.isDxMeas(props))
				ivjJRBIsDXType.setSelected(true);
			else
				ivjJRBNotDXType.setSelected(true);
		} else {
			ivjJRBNoDXType.setSelected(true);
		}
		setStateMessage(StringResource.getStringResource("miufo1001466")
				+ vo.getMeasureVO().getName(), false); //"��ǰѡ��ָ������ƣ�\n\r"
		setTableSelectRows(rows);
	}
	private void setValuesToMeasureVOs(EventObject event) {
		//�����ǰѡ����ֻ��һ�У���������
		if (rows.length < 2) {
			return;
		}
		//������ֵ��ʱ������isDialogΪtrue,��֤�����ظ������Ի���
		//������֮���ڻָ���ֵ
		isDialog = true;
		//ȡ����ǰ�����ָ������
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
		//�����û����޸ĵ�������Ե�ǰָ����и�ֵ
		//ָ������ָ�����Ʋ������޸�
		try {
			if (event.getSource() == ivjJTFMeasureLength) {
				//ָ�곤��Ҫ��֤�Ϸ��ԣ��д�Ҫ��ʾ�û�
				String length = ivjJTFMeasureLength.getText().trim();
				setValusByRows(MeasureDefineTableModel.LENGTH_COLUMN, length);
			} else if (event.getSource() == ivjJTFNote) {
				//ָ��˵��������
				String note = ivjJTFNote.getText().trim();
				setValusByRows(MeasureDefineTableModel.NOTE_COLUMN, note);
			} else if (event.getSource() == ivjJCBCodeReference
					|| event.getSource() == ivjJCBCodeReference.getModel()) {
				//����������ø�ѡ���ѡ��ı䣬�������ʾ�ı������������ҵ���Ӧ�ı�������ID���б���
				String codeRef = (String) ivjJCBCodeReference.getSelectedItem();
				setValusByRows(MeasureDefineTableModel.CODEREFERENCE_COLUMN, codeRef);

			} else if (event.getSource() == ivjJCBMeasureAttribute
					|| event.getSource() == ivjJCBMeasureAttribute.getModel()) {
				//�����λΪ%,����Ҫ�ж�ָ���Ƿ�Ϊ�ٷֱ�ָ�꣬����չ���������Ӱٷֱ�ָ��һ���Ա�ʶ
				Object attribute = ivjJCBMeasureAttribute.getSelectedItem();
//				setValusByRows(MeasureDefineTableModel.ATTRIBUTE_COLUMN, attribute);
			} else if (event.getSource() == ivjJCBMeasureType
					|| event.getSource() == ivjJCBMeasureType.getModel()) {
				//���ָ����������ֵ�򸡶���ֵ����λ��ѡ���������ò���ѡ��ָ�곤�Ȳ��ɶ���
				//���ָ���������ַ��򸡶��ַ�����λ����ѡ���������ò���ѡ��ָ�곤�ȿɶ���
				//���ָ�������Ǳ���򸡶����룬��λ����ѡ���������ÿ�ѡ��ָ�곤�Ȳ��ɶ���
				setValusByRows(MeasureDefineTableModel.TYPE_COLUMN, strtype);
				ivjJTFMeasureLength.setEnabled(true);
				ivjJTFMeasureLength.setText("64");
				ivjJCBMeasureAttribute.setSelectedIndex(-1);
				ivjJCBMeasureAttribute.setEnabled(false);
				if (type == MeasureVO.TYPE_CODE)//����򸡶�����ʱ,������տ���
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
				//��ָ��Ϊ�ϲ�ָ��ʱ������Ϊ��ѡ�
				if (ivjJRBIsHBMeasure.isSelected()) {
					setValusByRows(MeasureDefineTableModel.DIRECTION_COLUMN,
							MeasureDefineTableModel.DIRECTION[0]);
					//ivjJRBDirectionJ.setSelected(true);
				}
			} else if (event.getSource() == ivjJRBDirectionD) {
				//�����ǰָ�겻�Ǻϲ�ָ�꣬����ѡ���˸÷�����ô����ָ���Զ���Ϊ�ϲ�ָ��
				setValusByRows(MeasureDefineTableModel.DIRECTION_COLUMN,
						MeasureDefineTableModel.DIRECTION[2]);
				ivjJRBIsHBMeasure.setSelected(true);
				ivjJCBMeasureType.setEnabled(false);
			} else if (event.getSource() == ivjJRBDirectionJ) {
				//�����ǰָ�겻�Ǻϲ�ָ�꣬����ѡ���˸÷�����ô����ָ���Զ���Ϊ�ϲ�ָ��
				setValusByRows(MeasureDefineTableModel.DIRECTION_COLUMN,
						MeasureDefineTableModel.DIRECTION[1]);
				ivjJRBIsHBMeasure.setSelected(true);
				ivjJCBMeasureType.setEnabled(false);
			} else if (event.getSource() == ivjJRBIsDXType) {
				//�����ǰָ�겻�Ǻϲ�ָ�꣬����ѡ���˸����ͣ���ô����ָ���Զ���Ϊ�ϲ�ָ�꣬
				//��ʱ��Ĭ�Ϸ���Ϊ�跽�򣬲�������չ����Ϊ��������
	
				setValusByRows(MeasureDefineTableModel.DXTYPE_COLUMN, MeasureDefineTableModel.DXTYPE[1]);
				ivjJRBIsHBMeasure.setSelected(true);
				ivjJCBMeasureType.setEnabled(false);
			} else if (event.getSource() == ivjJRBNotDXType) {
				//�����ǰָ���Ǻϲ�ָ�꣬ѡ���˸����ͣ�������չ����Ϊ�ǵ�������
				if (ivjJRBIsHBMeasure.isSelected()) {
					HBBBMeasParser.setDxMeas(false, propsForRows);
					setValusByRows(MeasureDefineTableModel.DXTYPE_COLUMN,
							MeasureDefineTableModel.DXTYPE[0]);
				}

			} else if (event.getSource() == ivjJRBIsHBMeasure) {
				//���ָ�걻��Ϊ�ϲ�ָ�꣬��ָ�������Ĭ��Ϊ��ֵ���ͣ����͸�ѡ������༭����Ĭ��Ϊ�跽�򣬵�������
				if (type == MeasureVO.TYPE_NUMBER) {
					setValusByRows(MeasureDefineTableModel.HEBING_COLUMN,
							MeasureDefineTableModel.HEBING[1]);
				}
				ivjJCBMeasureType.setSelectedIndex(0);
				ivjJCBMeasureType.setEnabled(false);
			} else if (event.getSource() == ivjJRBIsNotHBMeasure) {
				//���ָ�걻��Ϊ�Ǻϲ�ָ�꣬��ָ�����͸�ѡ������༭��Ĭ��Ϊ�޷��򣬷ǵ�������
				setValusByRows(MeasureDefineTableModel.HEBING_COLUMN, MeasureDefineTableModel.HEBING[0]);
				ivjJCBMeasureType.setEnabled(true);
			}
			//��ˢ����ʾ
			m_measureDefineTableModel.fireTableDataChanged();
			setStateMessage(ivjJStatusArea.getText(), false);
		} catch (RuntimeException e) {
			setStateMessage(e.getMessage(), true);
		}

		//������֮�󣬻ָ�isDialogΪtrue
		isDialog = false;
		setTableSelectRows(rows);
	}
	private void setValueToComponent(int row) {
		//����ָ������һ��ָ�겻����༭��ֻ�������
		//����ָ����������ָ��ֻ����༭ָ�����ƣ�Ҳ�������
		MeasurePosVO vo = m_measureDefineTableModel.getSelectMeasurePosVO(row);
		////����ָ�����,�����δ����ָ�����,�������ʾΪ��
		
		//����ָ������
		ivjJTFMeasureName.setText((String) m_measureDefineTableModel.getValueAt(row,
				MeasureDefineTableModel.NAME_COLUMN));
		ivjJTFMeasureName.setEnabled(true);
		//����ָ�����
		ivjJBMeasureRef.setEnabled(true);
		//����ָ������
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
		
		//����ָ���������
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
		//����ָ�곤��
		ivjJTFMeasureLength.setText(m_measureDefineTableModel.getValueAt(row,
				MeasureDefineTableModel.LENGTH_COLUMN).toString());

		//����ָ��˵��
		ivjJTFNote.setText((String) m_measureDefineTableModel.getValueAt(row,
				MeasureDefineTableModel.NOTE_COLUMN));
		//����ָ���Ƿ�ϲ�
		String hbprop = (String) m_measureDefineTableModel.getValueAt(row,
				MeasureDefineTableModel.HEBING_COLUMN);
		if (MeasureDefineTableModel.HEBING[1].equals(hbprop)) {
			ivjJRBIsHBMeasure.setSelected(true);
		} else {
			ivjJRBIsNotHBMeasure.setSelected(true);
		}
		//����ָ�귽��
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
		//����ָ���������
		hbprop = (String) m_measureDefineTableModel.getValueAt(row, MeasureDefineTableModel.DXTYPE_COLUMN);
		if (MeasureDefineTableModel.DXTYPE[1].equals(hbprop)) {
			ivjJRBIsDXType.setSelected(true);
		} else if (MeasureDefineTableModel.DXTYPE[0].equals(hbprop)) {
			ivjJRBNotDXType.setSelected(true);
		} else {
			ivjJRBNoDXType.setSelected(true);
		}
		//���ÿɱ༭״̬
		
		// //ָ�����
		ivjJTFMeasureName.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.NAME_COLUMN)); //ָ������
		ivjJTFMeasureLength.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.LENGTH_COLUMN)); //ָ�곤�ȣ�������
		ivjJTFNote.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.NOTE_COLUMN)); //ָ��˵����������
		ivjJCBCodeReference.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.CODEREFERENCE_COLUMN));
//		ivjJCBMeasureAttribute.setEnabled(m_measureDefineTableModel.isCellEditable(row,
//				MeasureDefineTableModel.ATTRIBUTE_COLUMN));
		ivjJCBMeasureType.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.TYPE_COLUMN));
		ivjJBMeasureRef.setEnabled(m_measureDefineTableModel.isCellEditable(row,
				MeasureDefineTableModel.MEASURE_REF_COLUMN)); //ָ�����
//		ivjJBKeywordRef.setEnabled(m_measureDefineTableModel.isCellEditable(row,
//				MeasureDefineTableModel.KEY_REF_COLUMN)); //�ؼ��ֲ���
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
			msg.append(StringResource.getStringResource("miufo1001466")); //��ǰѡ��ָ������ƣ�\n\r
		} else if (vo.getKeyFlag().booleanValue()) {
			msg.append(StringResource.getStringResource("miufo1001467")); //��ǰѡ�йؼ��ֵ����ƣ�\n\r
		}
		msg.append(ivjJTFMeasureName.getText());
		setStateMessage(msg.toString(), false);
		//���õ�ǰѡ�е��У���Ϊ��IE�����ʱ��ѡ�е��о����༭���ָ�Ϊѡ��״̬������ʱ��û���ѡ�еģ����ԣ���Ҫǿ������ѡ�е���
		setTableSelectRow(currentRow);
	}
	private void setValueToMeasureVO(EventObject event) {
		//�����ǰû��ѡ���У���������
		if (currentRow < 0) {
			return;
		}
		//������ֵ��ʱ������isDialogΪtrue,��֤�����ظ������Ի���
		//������֮���ڻָ���ֵ
		isDialog = true;
		//ȡ����ǰ�ж�Ӧ��ָ��
		MeasurePosVO vo = m_measureDefineTableModel.getSelectMeasurePosVO(currentRow);
		//ȡ��vo����չ���ԣ�

		try {
			//����û��Ƿ��������޸���ָ���ĳЩ���ԣ��Դ�������ָ���״̬
			boolean isedited = false;
			//�����û����޸ĵ�������Ե�ǰָ����и�ֵ
			if (event.getSource() == ivjJTFMeasureName) {
				String name = ivjJTFMeasureName.getText().trim();
				m_measureDefineTableModel.setValueAt(name, currentRow, MeasureDefineTableModel.NAME_COLUMN);
				setStateMessage("", false);
				isedited = true;

			} else if (event.getSource() == ivjJTFMeasureLength) {
				//ָ�곤��Ҫ��֤�Ϸ��ԣ��д�Ҫ��ʾ�û�
				String length = ivjJTFMeasureLength.getText().trim();
				m_measureDefineTableModel
						.setValueAt(length, currentRow,
								MeasureDefineTableModel.LENGTH_COLUMN);
				ivjJStatusArea.setText("");
				isedited = true;
			} else if (event.getSource() == ivjJTFNote) {
				//ָ��˵��������
				String note = ivjJTFNote.getText().trim();
				m_measureDefineTableModel.setValueAt(note, currentRow, MeasureDefineTableModel.NOTE_COLUMN);
				isedited = true;
			} else if (event.getSource() == ivjJCBCodeReference
					|| event.getSource() == ivjJCBCodeReference.getModel()) {
				//����������ø�ѡ���ѡ��ı䣬�������ʾ�ı������������ҵ���Ӧ�ı�������ID���б���
				String codeRef = (String) ivjJCBCodeReference.getSelectedItem();
				m_measureDefineTableModel.setValueAt(codeRef, currentRow,
						MeasureDefineTableModel.CODEREFERENCE_COLUMN);

			} else if (event.getSource() == ivjJCBMeasureAttribute
					|| event.getSource() == ivjJCBMeasureAttribute.getModel()) {
				//�����λΪ%,����Ҫ�ж�ָ���Ƿ�Ϊ�ٷֱ�ָ�꣬����չ���������Ӱٷֱ�ָ��һ���Ա�ʶ
				Object attribute = ivjJCBMeasureAttribute.getSelectedItem();
//				m_measureDefineTableModel.setValueAt(attribute, currentRow,
//						MeasureDefineTableModel.ATTRIBUTE_COLUMN);
				isedited = true;
			} else if (event.getSource() == ivjJCBMeasureType
					|| event.getSource() == ivjJCBMeasureType.getModel()) {
				//���ָ����������ֵ�򸡶���ֵ����λ��ѡ���������ò���ѡ��ָ�곤�Ȳ��ɶ���
				//���ָ���������ַ��򸡶��ַ�����λ����ѡ���������ò���ѡ��ָ�곤�ȿɶ���
				//���ָ�������Ǳ���򸡶����룬��λ����ѡ���������ÿ�ѡ��ָ�곤�Ȳ��ɶ���
				Object type = ivjJCBMeasureType.getSelectedItem();
				m_measureDefineTableModel.setValueAt(type, currentRow, MeasureDefineTableModel.TYPE_COLUMN);
				setValueToComponent(currentRow);
				isedited = true;
			} else if (event.getSource() == ivjJRBNoDirection) {
				//��ָ��Ϊ�ϲ�ָ��ʱ������Ϊ��ѡ�
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
				//�����ǰָ�겻�Ǻϲ�ָ�꣬����ѡ���˸����ͣ���ô����ָ���Զ���Ϊ�ϲ�ָ�꣬
				//��ʱ��Ĭ�Ϸ���Ϊ�跽�򣬲�������չ����Ϊ��������
				m_measureDefineTableModel.setValueAt(MeasureDefineTableModel.DXTYPE[1], currentRow,
						MeasureDefineTableModel.DXTYPE_COLUMN);
				ivjJRBIsHBMeasure.setSelected(true);
				ivjJCBMeasureType.setEnabled(false);
				isedited = true;
			} else if (event.getSource() == ivjJRBNotDXType) {
				//�����ǰָ���Ǻϲ�ָ�꣬ѡ���˸����ͣ�������չ����Ϊ�ǵ�������
				m_measureDefineTableModel.setValueAt(MeasureDefineTableModel.DXTYPE[0], currentRow,
						MeasureDefineTableModel.DXTYPE_COLUMN);
				isedited = true;
			} else if (event.getSource() == ivjJRBIsHBMeasure) {
				//���ָ�걻��Ϊ�ϲ�ָ�꣬��ָ�������Ĭ��Ϊ��ֵ���ͣ����͸�ѡ������༭����Ĭ��Ϊ�跽�򣬵�������
				m_measureDefineTableModel.setValueAt(MeasureDefineTableModel.HEBING[1], currentRow,
						MeasureDefineTableModel.HEBING_COLUMN);
				ivjJCBMeasureType.setSelectedIndex(0);
				ivjJCBMeasureType.setEnabled(false);
				isedited = true;
			} else if (event.getSource() == ivjJRBIsNotHBMeasure) {
				//���ָ�걻��Ϊ�Ǻϲ�ָ�꣬��ָ�����͸�ѡ������༭��Ĭ��Ϊ�޷��򣬷ǵ�������
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
					+ ivjJTFMeasureName.getText(), false); //"��ǰѡ��ָ�꣺\n\r"
		} catch (RuntimeException ce) {
			setStateMessage(ce.getMessage(), true);
		}

		//������֮�󣬻ָ�isDialogΪtrue
		isDialog = false;

	}
	/**
	 * ��ѡ�ж���ʱ���Զ��ָ����ֵ�� colum--���õ�ָ�����Զ�Ӧ��m_table�е��кţ�obj--�����Ե�ֵ
	 */
	private void setValusByRows(int colum, Object obj) {

		//���û��ѡ�ж��л�ֻѡ��һ�У����������˴���Ҫ��Ϊ�˷�ֹ�ڸ����еĴ������
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


 