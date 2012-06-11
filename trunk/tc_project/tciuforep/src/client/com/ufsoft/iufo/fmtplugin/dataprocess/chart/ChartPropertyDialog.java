package com.ufsoft.iufo.fmtplugin.dataprocess.chart;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.ufsoft.iufo.resource.StringResource;
//import com.ufsoft.iuforeport.reporttool.chart.HandleBorder;
//import com.ufsoft.iuforeport.reporttool.pub.UfoDialog;
//import com.ufsoft.iuforeport.reporttool.pub.UfoPublic;

/**
 * ˵����
 * ���ߣ�������
 * �������ڣ�(2003-4-7 16:31:53)
 * �汾��
 */
public class ChartPropertyDialog {//TODO extends UfoDialog {
	private ReportChart TheChart;
	private DefaultListModel SeriesList;
	private JComboBox ivjJComboBoxType = null;
	private JLabel ivjJLabel2 = null;
	private JPanel ivjJPanel3 = null;
	private JSeparator ivjJSeparator1 = null;
	private JPanel ivjUfoDialogContentPane = null;
	private JLabel ivjJLabel4 = null;
	private JLabel ivjJLabel5 = null;
	private JButton ivjBtnType1 = null;
	private JButton ivjBtnType2 = null;
	private JButton ivjBtnType3 = null;
	private JButton ivjBtnType4 = null;
	private JButton ivjJBtnCancle = null;
	private JButton ivjJBtnSure = null;
	private JCheckBox ivjJCheckBoxExample = null;
	private JCheckBox ivjJCheckBoxLine = null;
	private JTextField ivjJTextGraphTitle = null;
	private JTextField ivjJTextXTitle = null;
	private JTextField ivjJTextYTitle = null;



//	IvjEventHandler ivjEventHandler = new IvjEventHandler();

//class IvjEventHandler implements java.awt.event.ActionListener {
//		public void actionPerformed(java.awt.event.ActionEvent e) {
//			if (e.getSource() == ChartPropertyDialog.this.getJComboBoxType()) 
//				connEtoC1(e);
//			if (e.getSource() == ChartPropertyDialog.this.getBtnType1()) 
//				connEtoC5();
//			if (e.getSource() == ChartPropertyDialog.this.getBtnType2()) 
//				connEtoC7();
//			if (e.getSource() == ChartPropertyDialog.this.getBtnType3()) 
//				connEtoC8();
//			if (e.getSource() == ChartPropertyDialog.this.getBtnType4()) 
//				connEtoC9();
//			if (e.getSource() == ChartPropertyDialog.this.getJBtnCancle()) 
//				connEtoC11();
//			if (e.getSource() == ChartPropertyDialog.this.getJBtnSure()) 
//				connEtoC12();
//		};
//	}
//
//	private JLabel ivjJLabel31 = null;
//
///**
// * ChartPropertyDialog ������ע�⡣
// */
//public ChartPropertyDialog() {
//	super();
//	initialize();
//}
///**
// * ChartPropertyDialog ������ע�⡣
// * @param parent java.awt.Container
// */
//public ChartPropertyDialog(java.awt.Container parent) {
//	super(parent);
//}
///**
// * ChartPropertyDialog ������ע�⡣
// * @param parent java.awt.Container
// * @param title java.lang.String
// */
//public ChartPropertyDialog(java.awt.Container parent, String title) {
//	super(parent, title);
//}
///**
// * ChartPropertyDialog ������ע�⡣
// * @param owner java.awt.Container
// * @param title java.lang.String
// * @param modal boolean
// */
//public ChartPropertyDialog(java.awt.Container owner, String title, boolean modal) {
//	super(owner, title, modal);
//}
///**
// * ChartPropertyDialog ������ע�⡣
// * @param owner java.awt.Dialog
// */
//public ChartPropertyDialog(java.awt.Dialog owner) {
//	super(owner);
//}
///**
// * ChartPropertyDialog ������ע�⡣
// * @param owner java.awt.Dialog
// * @param title java.lang.String
// */
//public ChartPropertyDialog(java.awt.Dialog owner, String title) {
//	super(owner, title);
//}
///**
// * ChartPropertyDialog ������ע�⡣
// * @param owner java.awt.Dialog
// * @param title java.lang.String
// * @param modal boolean
// */
//public ChartPropertyDialog(java.awt.Dialog owner, String title, boolean modal) {
//	super(owner, title, modal);
//}
///**
// * ChartPropertyDialog ������ע�⡣
// * @param owner java.awt.Dialog
// * @param modal boolean
// */
//public ChartPropertyDialog(java.awt.Dialog owner, boolean modal) {
//	super(owner, modal);
//}
///**
// * ChartPropertyDialog ������ע�⡣
// * @param owner java.awt.Frame
// */
//public ChartPropertyDialog(java.awt.Frame owner) {
//	super(owner);
//}
///**
// * ChartPropertyDialog ������ע�⡣
// * @param owner java.awt.Frame
// * @param title java.lang.String
// */
//public ChartPropertyDialog(java.awt.Frame owner, String title) {
//	super(owner, title);
//}
///**
// * ChartPropertyDialog ������ע�⡣
// * @param owner java.awt.Frame
// * @param title java.lang.String
// * @param modal boolean
// */
//public ChartPropertyDialog(java.awt.Frame owner, String title, boolean modal) {
//	super(owner, title, modal);
//}
///**
// * ChartPropertyDialog ������ע�⡣
// * @param owner java.awt.Frame
// * @param modal boolean
// */
//public ChartPropertyDialog(java.awt.Frame owner, boolean modal) {
//	super(owner, modal);
//}
///**
// * Comment
// */
//public void btnType1_ActionEvents() {
//	getBtnType1().setSelected(true);
//	getBtnType2().setSelected(false);
//	getBtnType3().setSelected(false);
//	getBtnType4().setSelected(false);
//	getBtnType1().setBorder(new HandleBorder());
//	getBtnType2().setBorder(new javax.swing.border.CompoundBorder());
//	getBtnType3().setBorder(new javax.swing.border.CompoundBorder());
//	getBtnType4().setBorder(new javax.swing.border.CompoundBorder());
//	return;   
//}
///**
// * Comment
// */
//public void btnType2_ActionEvents() {
//	getBtnType2().setSelected(true);
//	getBtnType1().setSelected(false);
//	getBtnType3().setSelected(false);
//	getBtnType4().setSelected(false);
//	getBtnType2().setBorder(new HandleBorder());
//	getBtnType1().setBorder(new javax.swing.border.CompoundBorder());
//	getBtnType3().setBorder(new javax.swing.border.CompoundBorder());
//	getBtnType4().setBorder(new javax.swing.border.CompoundBorder());
//return;
//}
///**
// * Comment
// */
//public void btnType3_ActionEvents() {
//	getBtnType3().setSelected(true);
//	getBtnType1().setSelected(false);
//	getBtnType2().setSelected(false);
//	getBtnType4().setSelected(false);
//	getBtnType3().setBorder(new HandleBorder());
//	getBtnType1().setBorder(new javax.swing.border.CompoundBorder());
//	getBtnType2().setBorder(new javax.swing.border.CompoundBorder());
//	getBtnType4().setBorder(new javax.swing.border.CompoundBorder());
//	return;
//}
///**
// * Comment
// */
//public void btnType4_ActionEvents() {
//	getBtnType4().setSelected(true);
//	getBtnType1().setSelected(false);
//	getBtnType2().setSelected(false);
//	getBtnType3().setSelected(false);
//	getBtnType4().setBorder(new HandleBorder());
//	getBtnType1().setBorder(new javax.swing.border.CompoundBorder());
//	getBtnType2().setBorder(new javax.swing.border.CompoundBorder());
//	getBtnType3().setBorder(new javax.swing.border.CompoundBorder());
//return;
//}
///**
// * connEtoC1:  (JComboBoxType.action.actionPerformed(java.awt.event.ActionEvent) --> ChartPropertyDialog.jComboBoxType_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
// * @param arg1 java.awt.event.ActionEvent
// */
///* ���棺�˷������������ɡ� */
//private void connEtoC1(java.awt.event.ActionEvent arg1) {
//	try {
//		// user code begin {1}
//		// user code end
//		this.jComboBoxType_ActionPerformed(arg1);
//		// user code begin {2}
//		// user code end
//	} catch (java.lang.Throwable ivjExc) {
//		// user code begin {3}
//		// user code end
//		handleException(ivjExc);
//	}
//}
///**
// * connEtoC11:  (JBtnCancle.action. --> ChartPropertyDialog.jBtnCancle_ActionEvents()V)
// */
///* ���棺�˷������������ɡ� */
//private void connEtoC11() {
//	try {
//		// user code begin {1}
//		// user code end
//		this.jBtnCancle_ActionEvents();
//		// user code begin {2}
//		// user code end
//	} catch (java.lang.Throwable ivjExc) {
//		// user code begin {3}
//		// user code end
//		handleException(ivjExc);
//	}
//}
///**
// * connEtoC12:  (JBtnSure.action. --> ChartPropertyDialog.jBtnSure_ActionEvents()V)
// */
///* ���棺�˷������������ɡ� */
//private void connEtoC12() {
//	try {
//		// user code begin {1}
//		// user code end
//		this.jBtnSure_ActionEvents();
//		// user code begin {2}
//		// user code end
//	} catch (java.lang.Throwable ivjExc) {
//		// user code begin {3}
//		// user code end
//		handleException(ivjExc);
//	}
//}
///**
// * connEtoC5:  (BtnType1.action. --> ChartPropertyDialog.btnType1_ActionEvents()V)
// */
///* ���棺�˷������������ɡ� */
//private void connEtoC5() {
//	try {
//		// user code begin {1}
//		// user code end
//		this.btnType1_ActionEvents();
//		// user code begin {2}
//		// user code end
//	} catch (java.lang.Throwable ivjExc) {
//		// user code begin {3}
//		// user code end
//		handleException(ivjExc);
//	}
//}
///**
// * connEtoC7:  (BtnType2.action. --> ChartPropertyDialog.btnType2_ActionEvents()V)
// */
///* ���棺�˷������������ɡ� */
//private void connEtoC7() {
//	try {
//		// user code begin {1}
//		// user code end
//		this.btnType2_ActionEvents();
//		// user code begin {2}
//		// user code end
//	} catch (java.lang.Throwable ivjExc) {
//		// user code begin {3}
//		// user code end
//		handleException(ivjExc);
//	}
//}
///**
// * connEtoC8:  (BtnType3.action. --> ChartPropertyDialog.btnType3_ActionEvents()V)
// */
///* ���棺�˷������������ɡ� */
//private void connEtoC8() {
//	try {
//		// user code begin {1}
//		// user code end
//		this.btnType3_ActionEvents();
//		// user code begin {2}
//		// user code end
//	} catch (java.lang.Throwable ivjExc) {
//		// user code begin {3}
//		// user code end
//		handleException(ivjExc);
//	}
//}
///**
// * connEtoC9:  (BtnType4.action. --> ChartPropertyDialog.btnType4_ActionEvents()V)
// */
///* ���棺�˷������������ɡ� */
//private void connEtoC9() {
//	try {
//		// user code begin {1}
//		// user code end
//		this.btnType4_ActionEvents();
//		// user code begin {2}
//		// user code end
//	} catch (java.lang.Throwable ivjExc) {
//		// user code begin {3}
//		// user code end
//		handleException(ivjExc);
//	}
//}
///**
// * ���� JButton1 ����ֵ��
// * @return javax.swing.JButton
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JButton getBtnType1() {
//	if (ivjBtnType1 == null) {
//		try {
//			ivjBtnType1 = new UIButton();
//			ivjBtnType1.setName("BtnType1");
//			ivjBtnType1.setOpaque(false);
//			ivjBtnType1.setBorder(new HandleBorder());
//			ivjBtnType1.setText("");
//			ivjBtnType1.setBackground(new java.awt.Color(204,204,204));
//			ivjBtnType1.setFocusPainted(false);
//			ivjBtnType1.setSelected(true);
//			ivjBtnType1.setIcon(null);
//			ivjBtnType1.setRolloverEnabled(true);
//			ivjBtnType1.setBounds(15, 20, 93, 59);
//			ivjBtnType1.setMargin(new java.awt.Insets(2, 14, 2, 14));
//			// user code begin {1}
//			ImageIcon icoChart1=UfoPublic.getImageIcon("chart01.jpg");
//			ivjBtnType1.setIcon(icoChart1);
//			ivjBtnType1.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjBtnType1;
//}
///**
// * ���� JButton2 ����ֵ��
// * @return javax.swing.JButton
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JButton getBtnType2() {
//	if (ivjBtnType2 == null) {
//		try {
//			ivjBtnType2 = new UIButton();
//			ivjBtnType2.setName("BtnType2");
//			ivjBtnType2.setBorder(new javax.swing.border.CompoundBorder());
//			ivjBtnType2.setText("");
//			ivjBtnType2.setIcon(null);
//			ivjBtnType2.setBorderPainted(true);
//			ivjBtnType2.setRolloverEnabled(false);
//			ivjBtnType2.setBounds(123, 20, 93, 59);
//			// user code begin {1}
//			ImageIcon icoChart1=UfoPublic.getImageIcon("chart02.jpg");
//			ivjBtnType2.setIcon(icoChart1);
//			ivjBtnType2.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjBtnType2;
//}
///**
// * ���� JButton3 ����ֵ��
// * @return javax.swing.JButton
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JButton getBtnType3() {
//	if (ivjBtnType3 == null) {
//		try {
//			ivjBtnType3 = new UIButton();
//			ivjBtnType3.setName("BtnType3");
//			ivjBtnType3.setIcon(null);
//			ivjBtnType3.setBorderPainted(true);
//			ivjBtnType3.setBorder(new javax.swing.border.CompoundBorder());
//			ivjBtnType3.setText("");
//			ivjBtnType3.setBounds(231, 20, 93, 59);
//			// user code begin {1}
//			ImageIcon icoChart1=UfoPublic.getImageIcon("chart03.jpg");
//			ivjBtnType3.setIcon(icoChart1);
//			ivjBtnType3.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//			ivjBtnType3.setDefaultCapable(true);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjBtnType3;
//}
///**
// * ���� JButton4 ����ֵ��
// * @return javax.swing.JButton
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JButton getBtnType4() {
//	if (ivjBtnType4 == null) {
//		try {
//			ivjBtnType4 = new UIButton();
//			ivjBtnType4.setName("BtnType4");
//			ivjBtnType4.setIcon(null);
//			ivjBtnType4.setBorderPainted(true);
//			ivjBtnType4.setBorder(new javax.swing.border.CompoundBorder());
//			ivjBtnType4.setText("");
//			ivjBtnType4.setBounds(339, 20, 93, 59);
//			// user code begin {1}
//			ImageIcon icoChart1=UfoPublic.getImageIcon("chart04.jpg");
//			ivjBtnType4.setIcon(icoChart1);
//			ivjBtnType4.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//			
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjBtnType4;
//}
//	public ReportChart getChart(){
//	  return TheChart;}
///**
// * ���� JButton6 ����ֵ��
// * @return javax.swing.JButton
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JButton getJBtnCancle() {
//	if (ivjJBtnCancle == null) {
//		try {
//			ivjJBtnCancle = new UIButton();
//			ivjJBtnCancle.setName("JBtnCancle");
//	//		ivjJBtnCancle.setFont(new java.awt.Font("dialog", 0, 12));
//			ivjJBtnCancle.setText("Cancle");
//			ivjJBtnCancle.setBounds(365, 285, 73, 27);
//			// user code begin {1}
//			String strCancel = StringResource.getStringResource(StringResource.CANCEL);
//			ivjJBtnCancle.setText(strCancel);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJBtnCancle;
//}
///**
// * ���� JButton5 ����ֵ��
// * @return javax.swing.JButton
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JButton getJBtnSure() {
//	if (ivjJBtnSure == null) {
//		try {
//			ivjJBtnSure = new UIButton();
//			ivjJBtnSure.setName("JBtnSure");
//	//		ivjJBtnSure.setFont(new java.awt.Font("dialog", 0, 12));
//			ivjJBtnSure.setText("OK");
//			ivjJBtnSure.setBounds(261, 284, 73, 27);
//			// user code begin {1}
//			String strOK = StringResource.getStringResource(StringResource.CANCEL);
//			ivjJBtnSure.setText(strOK);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJBtnSure;
//}
///**
// * ���� JCheckBox2 ����ֵ��
// * @return javax.swing.JCheckBox
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JCheckBox getJCheckBoxExample() {
//	if (ivjJCheckBoxExample == null) {
//		try {
//			ivjJCheckBoxExample = new UICheckBox();
//			ivjJCheckBoxExample.setName("JCheckBoxExample");
//	//		ivjJCheckBoxExample.setFont(new java.awt.Font("dialog", 0, 12));
//			ivjJCheckBoxExample.setText("Example");
//			ivjJCheckBoxExample.setBounds(131, 245, 97, 24);
//			ivjJCheckBoxExample.setSelected(true);
//			// user code begin {1}
//			String strExample = StringResource.getStringResource("miufo1001316");  //"��ʾͼ��"
//			ivjJCheckBoxExample.setText(strExample);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJCheckBoxExample;
//}
///**
// * ���� JCheckBox1 ����ֵ��
// * @return javax.swing.JCheckBox
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JCheckBox getJCheckBoxLine() {
//	if (ivjJCheckBoxLine == null) {
//		try {
//			ivjJCheckBoxLine = new UICheckBox();
//			ivjJCheckBoxLine.setName("JCheckBoxLine");
//	//		ivjJCheckBoxLine.setFont(new java.awt.Font("dialog", 0, 12));
//			ivjJCheckBoxLine.setText("BoxLine");
//			ivjJCheckBoxLine.setBounds(24, 245, 97, 24);
//			// user code begin {1}
//			String strBoxLine = StringResource.getStringResource("miufo1001317");  //"ͼ����߿���"
//			ivjJCheckBoxLine.setText(strBoxLine);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJCheckBoxLine;
//}
///**
// * ���� JComboBoxType ����ֵ��
// * @return javax.swing.JComboBox
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JComboBox getJComboBoxType() {
//	if (ivjJComboBoxType == null) {
//		try {
//			ivjJComboBoxType = new UIComboBox();
//			ivjJComboBoxType.setName("JComboBoxType");
//	//		ivjJComboBoxType.setFont(new java.awt.Font("dialog", 0, 12));
//			ivjJComboBoxType.setBounds(70, 92, 146, 27);
//			ivjJComboBoxType.setSelectedIndex(-1);
//			// user code begin {1}
//			String[] strPicType={
//					StringResource.getStringResource("miufo1000629"),  //"����ͼ"
//					StringResource.getStringResource("miufo1000630"),  //"����ͼ"
//					StringResource.getStringResource("miufo1000611"),  //"����ͼ"
//					StringResource.getStringResource("miufo1000613"),  //"ɢ��ͼ"
//					StringResource.getStringResource("miufo1000616"),  //"���ͼ"
//					StringResource.getStringResource("miufo1000618")  //"��ͼ"
//					};
//			DefaultComboBoxModel model=new DefaultComboBoxModel();
//			for (int i=0;i<strPicType.length;i++)
//			{
//				model.addElement(strPicType[i]);
//			}
//			ivjJComboBoxType.setModel(model);
//		//	ivjJComboBoxType.setSelectedIndex(0);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJComboBoxType;
//}
///**
// * ���� JLabel2 ����ֵ��
// * @return javax.swing.JLabel
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JLabel getJLabel2() {
//	if (ivjJLabel2 == null) {
//		try {
//			ivjJLabel2 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
//			ivjJLabel2.setName("JLabel2");
//	//		ivjJLabel2.setFont(new java.awt.Font("dialog", 0, 12));
//			ivjJLabel2.setText("ChartType:");
//			ivjJLabel2.setBounds(14, 100, 60, 19);
//			ivjJLabel2.setForeground(java.awt.Color.black);
//			// user code begin {1}
//			String strChartType = StringResource.getStringResource("miufo1001318");  //"ͼ������:"
//			ivjJLabel2.setText(strChartType);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJLabel2;
//}
///**
// * ���� JLabel31 ����ֵ��
// * @return javax.swing.JLabel
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JLabel getJLabel31() {
//	if (ivjJLabel31 == null) {
//		try {
//			ivjJLabel31 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
//			ivjJLabel31.setName("JLabel31");
//	//		ivjJLabel31.setFont(new java.awt.Font("dialog", 0, 12));
//			ivjJLabel31.setText("ChartTitle:");
//			ivjJLabel31.setBounds(14, 18, 58, 16);
//			ivjJLabel31.setForeground(java.awt.Color.black);
//			// user code begin {1}
//			String strChartTitle = StringResource.getStringResource("miufo1001319");  //"ͼ�����:"
//			ivjJLabel31.setText(strChartTitle);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJLabel31;
//}
///**
// * ���� JLabel4 ����ֵ��
// * @return javax.swing.JLabel
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JLabel getJLabel4() {
//	if (ivjJLabel4 == null) {
//		try {
//			ivjJLabel4 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
//			ivjJLabel4.setName("JLabel4");
//	//		ivjJLabel4.setFont(new java.awt.Font("dialog", 0, 12));
//			ivjJLabel4.setText("XTitle:");
//			ivjJLabel4.setBounds(16, 57, 58, 16);
//			ivjJLabel4.setForeground(java.awt.Color.black);
//			// user code begin {1}
//			String strXTitle = StringResource.getStringResource("miufo1001320");  //"X�����:"
//			ivjJLabel4.setText(strXTitle);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJLabel4;
//}
///**
// * ���� JLabel5 ����ֵ��
// * @return javax.swing.JLabel
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JLabel getJLabel5() {
//	if (ivjJLabel5 == null) {
//		try {
//			ivjJLabel5 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
//			ivjJLabel5.setName("JLabel5");
//	//		ivjJLabel5.setFont(new java.awt.Font("dialog", 0, 12));
//			ivjJLabel5.setText("YTitle");
//			ivjJLabel5.setBounds(241, 57, 58, 16);
//			ivjJLabel5.setForeground(java.awt.Color.black);
//			// user code begin {1}
//			String strYTitle = StringResource.getStringResource("miufo1001321");  //"Y�����:"
//			ivjJLabel5.setText(strYTitle);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJLabel5;
//}
///**
// * ���� JPanel3 ����ֵ��
// * @return javax.swing.JPanel
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JPanel getJPanel3() {
//	if (ivjJPanel3 == null) {
//		try {
//			ivjJPanel3 = new UIPanel();
//			ivjJPanel3.setName("JPanel3");
//			ivjJPanel3.setLayout(null);
//			ivjJPanel3.setBounds(6, 140, 448, 93);
//			getJPanel3().add(getBtnType1(), getBtnType1().getName());
//			getJPanel3().add(getBtnType2(), getBtnType2().getName());
//			getJPanel3().add(getBtnType3(), getBtnType3().getName());
//			getJPanel3().add(getBtnType4(), getBtnType4().getName());
//			// user code begin {1}
//			String strSubChart = StringResource.getStringResource("miufo1001322");  //"��ͼ��"
//			getJPanel3().setBorder(new TitledBorder(new EtchedBorder(),strSubChart,TitledBorder.LEFT,TitledBorder.TOP,new java.awt.Font("dialog",0,12)));
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJPanel3;
//}
///**
// * ���� JSeparator1 ����ֵ��
// * @return javax.swing.JSeparator
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JSeparator getJSeparator1() {
//	if (ivjJSeparator1 == null) {
//		try {
//			ivjJSeparator1 = new UISeparator();
//			ivjJSeparator1.setName("JSeparator1");
//			ivjJSeparator1.setBounds(12, 276, 436, 5);
//			// user code begin {1}
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJSeparator1;
//}
///**
// * ���� JTextField2 ����ֵ��
// * @return javax.swing.JTextField
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JTextField getJTextGraphTitle() {
//	if (ivjJTextGraphTitle == null) {
//		try {
//			ivjJTextGraphTitle = new UITextField();
//			ivjJTextGraphTitle.setName("JTextGraphTitle");
//			ivjJTextGraphTitle.setBounds(70, 16, 146, 20);
//			// user code begin {1}
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJTextGraphTitle;
//}
///**
// * ���� JTextField3 ����ֵ��
// * @return javax.swing.JTextField
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JTextField getJTextXTitle() {
//	if (ivjJTextXTitle == null) {
//		try {
//			ivjJTextXTitle = new UITextField();
//			ivjJTextXTitle.setName("JTextXTitle");
//			ivjJTextXTitle.setBounds(70, 55, 146, 20);
//			// user code begin {1}
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJTextXTitle;
//}
///**
// * ���� JTextField4 ����ֵ��
// * @return javax.swing.JTextField
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JTextField getJTextYTitle() {
//	if (ivjJTextYTitle == null) {
//		try {
//			ivjJTextYTitle = new UITextField();
//			ivjJTextYTitle.setName("JTextYTitle");
//			ivjJTextYTitle.setBounds(296, 55, 146, 20);
//			// user code begin {1}
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJTextYTitle;
//}
//	public ReportChart getTheChart(){
//	  if (TheChart == null) {
//			TheChart = new ReportChart();
//	 	}
//	return TheChart;
//	}
///**
// * ���� UfoDialogContentPane ����ֵ��
// * @return javax.swing.JPanel
// */
///* ���棺�˷������������ɡ� */
//private javax.swing.JPanel getUfoDialogContentPane() {
//	if (ivjUfoDialogContentPane == null) {
//		try {
//			ivjUfoDialogContentPane = new UIPanel();
//			ivjUfoDialogContentPane.setName("UfoDialogContentPane");
//			ivjUfoDialogContentPane.setLayout(null);
//			getUfoDialogContentPane().add(getJSeparator1(), getJSeparator1().getName());
//			getUfoDialogContentPane().add(getJBtnSure(), getJBtnSure().getName());
//			getUfoDialogContentPane().add(getJBtnCancle(), getJBtnCancle().getName());
//			getUfoDialogContentPane().add(getJLabel31(), getJLabel31().getName());
//			getUfoDialogContentPane().add(getJTextGraphTitle(), getJTextGraphTitle().getName());
//			getUfoDialogContentPane().add(getJTextYTitle(), getJTextYTitle().getName());
//			getUfoDialogContentPane().add(getJLabel5(), getJLabel5().getName());
//			getUfoDialogContentPane().add(getJTextXTitle(), getJTextXTitle().getName());
//			getUfoDialogContentPane().add(getJLabel4(), getJLabel4().getName());
//			getUfoDialogContentPane().add(getJLabel2(), getJLabel2().getName());
//			getUfoDialogContentPane().add(getJComboBoxType(), getJComboBoxType().getName());
//			getUfoDialogContentPane().add(getJPanel3(), getJPanel3().getName());
//			getUfoDialogContentPane().add(getJCheckBoxLine(), getJCheckBoxLine().getName());
//			getUfoDialogContentPane().add(getJCheckBoxExample(), getJCheckBoxExample().getName());
//			// user code begin {1}
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjUfoDialogContentPane;
//}
///**
// * ÿ�������׳��쳣ʱ������
// * @param exception java.lang.Throwable
// */
//private void handleException(java.lang.Throwable exception) {
//
//	/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
//	// System.out.println("--------- δ��׽�����쳣 ---------");
//	// exception.printStackTrace(System.out);
//}
///**
// * ��ʼ������
// * @exception java.lang.Exception �쳣˵����
// */
///* ���棺�˷������������ɡ� */
//private void initConnections() throws java.lang.Exception {
//	// user code begin {1}
//	// user code end
//	getJComboBoxType().addActionListener(ivjEventHandler);
//	getBtnType1().addActionListener(ivjEventHandler);
//	getBtnType2().addActionListener(ivjEventHandler);
//	getBtnType3().addActionListener(ivjEventHandler);
//	getBtnType4().addActionListener(ivjEventHandler);
//	getJBtnCancle().addActionListener(ivjEventHandler);
//	getJBtnSure().addActionListener(ivjEventHandler);
//}
///**
// * ��ʼ���ࡣ
// */
///* ���棺�˷������������ɡ� */
//private void initialize() {
//	try {
//		// user code begin {1}
//		setResizable(false);
//		// user code end
//		setName("ChartPropertyDialog");
//		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
//		setSize(460, 353);
//		setContentPane(getUfoDialogContentPane());
//		initConnections();
//	} catch (java.lang.Throwable ivjExc) {
//		handleException(ivjExc);
//	}
//	// user code begin {2}
//	Dimension  dScreen=Toolkit.getDefaultToolkit().getScreenSize();
//	int x=(dScreen.width-this.getWidth())/2;
//	int y=(dScreen.height-this.getHeight())/2;
//	setLocation(x,y);
//	// user code end
//}
///**
// * Comment
// */
//public void jBtnCancle_ActionEvents() {
//	setResult(ID_CANCEL);
//	close();
//	return;
//}
///**
// * Comment
// */
//public void jBtnSure_ActionEvents() {
//	int GraphType=-1;
//	int i=getJComboBoxType().getSelectedIndex();
//
//	if (i<2) {GraphType=4*i-1;}
//	if (i==2) {GraphType=7;}
//	if (i==3) {GraphType=9;}
//	if (i==4) {GraphType=12;}
//	if (i==5) {GraphType=14;}
//
//	if (getBtnType1().isSelected()) {GraphType=GraphType+1;}
//	else if (getBtnType2().isSelected()) {GraphType=GraphType+2;}
//	else if (getBtnType3().isSelected()) {GraphType=GraphType+3;}
//	else if (getBtnType4().isSelected()) {GraphType=GraphType+4;}
//		
//   //����TheChart����ĸ���ֵ
//   getTheChart().setStrTitle(getJTextGraphTitle().getText()); //ͼ����� 
//   getTheChart().setStrXTitle(getJTextXTitle().getText());    //X�����
//   getTheChart().setStrYTitle(getJTextYTitle().getText());    //Y�����
//   getTheChart().setbBorder(getJCheckBoxLine().isSelected()); //�߿�
//   getTheChart().setShowInstance(getJCheckBoxExample().isSelected());//ͼ��
//   getTheChart().setnType(GraphType);//ͼ������
//
//	setResult(ID_OK);
//	close();
//	return;
//}
///**
// * Comment
// */
//public void jComboBoxType_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
//	//���ݲ�ͬ��������ʾ��ͬ��ͼ��
//	ImageIcon icoChart1;
//	int i=getJComboBoxType().getSelectedIndex(); 
//
//	if (i==0) {
//	icoChart1=UfoPublic.getImageIcon("chart01.jpg");
//	ivjBtnType1.setIcon(icoChart1);
//	ivjBtnType1.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType1.repaint();
//	
//	icoChart1=UfoPublic.getImageIcon("chart02.jpg");
//	ivjBtnType2.setIcon(icoChart1);
//	ivjBtnType2.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType2.repaint();
//
//	icoChart1=UfoPublic.getImageIcon("chart03.jpg");
//	ivjBtnType3.setIcon(icoChart1);
//	ivjBtnType3.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType3.repaint();
//
//	icoChart1=UfoPublic.getImageIcon("chart04.jpg");
//	ivjBtnType4.setIcon(icoChart1);
//	ivjBtnType4.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType4.repaint();
//	
//	ivjBtnType1.setVisible(true);
//	ivjBtnType2.setVisible(true);
//	ivjBtnType3.setVisible(true);
//	ivjBtnType4.setVisible(true);
//	}
//	else if (i==1){
//	icoChart1=UfoPublic.getImageIcon("chart05.jpg");
//	ivjBtnType1.setIcon(icoChart1);
//	ivjBtnType1.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType1.repaint();
//	
//	icoChart1=UfoPublic.getImageIcon("chart06.jpg");
//	ivjBtnType2.setIcon(icoChart1);
//	ivjBtnType2.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType2.repaint();
//
//	icoChart1=UfoPublic.getImageIcon("chart07.jpg");
//	ivjBtnType3.setIcon(icoChart1);
//	ivjBtnType3.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType3.repaint();
//
//	icoChart1=UfoPublic.getImageIcon("chart08.jpg");
//	ivjBtnType4.setIcon(icoChart1);
//	ivjBtnType4.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType4.repaint();
//
//	ivjBtnType1.setVisible(true);
//	ivjBtnType2.setVisible(true);
//	ivjBtnType3.setVisible(true);
//	ivjBtnType4.setVisible(true);
//	}
//	else if (i==2){
//	icoChart1=UfoPublic.getImageIcon("chart09.jpg");
//	ivjBtnType1.setIcon(icoChart1);
//	ivjBtnType1.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType1.repaint();
//	
//	icoChart1=UfoPublic.getImageIcon("chart10.jpg");
//	ivjBtnType2.setIcon(icoChart1);
//	ivjBtnType2.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType2.repaint();
//	
//	ivjBtnType1.setVisible(true);
//	ivjBtnType2.setVisible(true);
//	ivjBtnType3.setVisible(false);
//	ivjBtnType4.setVisible(false);
//	}
//	else if (i==3){
//	icoChart1=UfoPublic.getImageIcon("chart11.jpg");
//	ivjBtnType1.setIcon(icoChart1);
//	ivjBtnType1.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType1.repaint();
//	
//	icoChart1=UfoPublic.getImageIcon("chart12.jpg");
//	ivjBtnType2.setIcon(icoChart1);
//	ivjBtnType2.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType2.repaint();
//	
//	icoChart1=UfoPublic.getImageIcon("chart13.jpg");
//	ivjBtnType3.setIcon(icoChart1);
//	ivjBtnType3.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType3.repaint();
//	
//	ivjBtnType1.setVisible(true);
//	ivjBtnType2.setVisible(true);
//	ivjBtnType3.setVisible(true);
//	ivjBtnType4.setVisible(false);
//	}
//	else if (i==4){
//	icoChart1=UfoPublic.getImageIcon("chart14.jpg");
//	ivjBtnType1.setIcon(icoChart1);
//	ivjBtnType1.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType1.repaint();
//	
//	icoChart1=UfoPublic.getImageIcon("chart15.jpg");
//	ivjBtnType2.setIcon(icoChart1);
//	ivjBtnType2.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType2.repaint();
//	
//	ivjBtnType1.setVisible(true);
//	ivjBtnType2.setVisible(true);
//	ivjBtnType3.setVisible(false);
//	ivjBtnType4.setVisible(false);
//	}
//	else if (i==5){
//	icoChart1=UfoPublic.getImageIcon("chart16.jpg");
//	ivjBtnType1.setIcon(icoChart1);
//	ivjBtnType1.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType1.repaint();
//	
//	icoChart1=UfoPublic.getImageIcon("chart17.jpg");
//	ivjBtnType2.setIcon(icoChart1);
//	ivjBtnType2.setSize(icoChart1.getIconWidth()+1,icoChart1.getIconHeight()+1);
//	ivjBtnType2.repaint();
//	
//	ivjBtnType1.setVisible(true);
//	ivjBtnType2.setVisible(true);
//	ivjBtnType3.setVisible(false);
//	ivjBtnType4.setVisible(false);
//	}
//
//	getBtnType1().setSelected(true);
//	getBtnType2().setSelected(false);
//	getBtnType3().setSelected(false);
//	getBtnType4().setSelected(false);
//	getBtnType1().setBorder(new HandleBorder());
//	getBtnType2().setBorder(new javax.swing.border.CompoundBorder());
//	getBtnType3().setBorder(new javax.swing.border.CompoundBorder());
//	getBtnType4().setBorder(new javax.swing.border.CompoundBorder());
//
//	return;
//}
///**
// * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
// * @param args java.lang.String[]
// */
//public static void main(java.lang.String[] args) {
//	try {
//		ChartPropertyDialog aChartPropertyDialog;
//		aChartPropertyDialog = new ChartPropertyDialog();
//		aChartPropertyDialog.setModal(true);
//		aChartPropertyDialog.addWindowListener(new java.awt.event.WindowAdapter() {
//			public void windowClosing(java.awt.event.WindowEvent e) {
//				System.exit(0);
//			};
//		});
//		aChartPropertyDialog.show();
//		java.awt.Insets insets = aChartPropertyDialog.getInsets();
//		aChartPropertyDialog.setSize(aChartPropertyDialog.getWidth() + insets.left + insets.right, aChartPropertyDialog.getHeight() + insets.top + insets.bottom);
//		aChartPropertyDialog.setVisible(true);
//	} catch (Throwable exception) {
//		System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoDialog 's main():Exception");
//		exception.printStackTrace(System.out);
//	}
//}
//	public void setChart(ReportChart RigionalChart){
//	   TheChart=RigionalChart;
//	   //����TheChart�������ó�ʼֵ
//	   getJTextGraphTitle().setText(TheChart.getStrTitle()); //ͼ�����   
//	   getJTextXTitle().setText(TheChart.getStrXTitle());    //X�����
//	   getJTextYTitle().setText(TheChart.getStrYTitle());    //Y�����
//	   getJCheckBoxLine().setSelected(TheChart.getbBorder());//�߿� 
//	   getJCheckBoxExample().setSelected(TheChart.getShowInstance());//ͼ��
//	   
//	   int i=TheChart.getnType();
//	   int j=1;
//	   if (i<4){j=0;}
//	   else if (i<8){j=1;i=i-4;}
//	   else if (i<10){j=2;i=i-8;}
//	   else if (i<13){j=3;i=i-10;}
//	   else if (i<15){j=4;i=i-13;}
//	   else if (i<17){j=5;i=i-15;}
//	   getJComboBoxType().setSelectedIndex(j);//ѡ��ͼ������
//	  
//	   getBtnType1().setSelected(false);
//   	   getBtnType2().setSelected(false);
//	   getBtnType3().setSelected(false);
//	   getBtnType4().setSelected(false);
//	   getBtnType1().setBorder(new javax.swing.border.CompoundBorder());
//	   getBtnType2().setBorder(new javax.swing.border.CompoundBorder());
//	   getBtnType3().setBorder(new javax.swing.border.CompoundBorder());
//	   getBtnType4().setBorder(new javax.swing.border.CompoundBorder());
//
//	   //ѡ��ͼ��������
//	   if (i==0) {
//	        getBtnType1().setSelected(true);
//	       	getBtnType1().setBorder(new HandleBorder());
//		  }
//	   else if (i==1) {
//	        getBtnType2().setSelected(true);
//	       	getBtnType2().setBorder(new HandleBorder());
//		  }
//	   else if (i==2) {
//	        getBtnType3().setSelected(true);
//	       	getBtnType3().setBorder(new HandleBorder());
//		  }
//	   else if (i==3) {
//	        getBtnType4().setSelected(true);
//	       	getBtnType4().setBorder(new HandleBorder());
//		  }
//		
//	  }
}


