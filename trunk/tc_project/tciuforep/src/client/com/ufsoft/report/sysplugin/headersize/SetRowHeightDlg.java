package com.ufsoft.report.sysplugin.headersize;

import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicArrowButton;

import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.beans.UFOLabel;

/**
 *  �����и߶Ի��� 
 * ERROR Ӧ�ú������п�ĶԻ�����һ���Ϳ�����.
 * @author��CaiJie
 */
public class SetRowHeightDlg extends UfoDialog implements ActionListener {
	private JButton ivjJBOK = null;
	private JLabel ivjJLabel2 = null;
	private JPanel ivjUfoDialogContentPane = null;
	private int m_RowHeight=-1;//�������õ��и�
	BasicArrowButton up = new BasicArrowButton(BasicArrowButton.NORTH);
	BasicArrowButton down = new BasicArrowButton(BasicArrowButton.SOUTH);
	private JTextField ivjTFRowHeight = null;
	private JButton ivjJBCancel = null;
/**
 * 
 */
public SetRowHeightDlg() {
	super();
	initialize();
}
/**
 * 
 * @param rowheight �и�
 * @param owner �����
 */
public SetRowHeightDlg(int rowheight, Frame owner) {
	super(owner);
	initialize();
	
	m_RowHeight = rowheight;//���õ�ǰ�и�
	double newRowVal = (double)m_RowHeight/DefaultSetting.RODAINLEN;
	DecimalFormat df = new DecimalFormat("0.###");
	ivjTFRowHeight.setText(df.format(newRowVal));
	ivjTFRowHeight.selectAll();
}
	/**
	 * Invoked when an action occurs.
	 * @param e
	 */
public void actionPerformed(ActionEvent e) {
	if (e.getSource()==ivjJBOK || e.getSource()==ivjTFRowHeight){
		try{
			int RowVa =(int)((Double.parseDouble(ivjTFRowHeight.getText())+1d/DefaultSetting.RODAINLEN/2)*DefaultSetting.RODAINLEN);
			if (RowVa>=0 && RowVa<=DefaultSetting.MAXROWHEIGHT) {
				setResult(ID_OK);
				m_RowHeight=RowVa;
				//whtao dispose();
				close();
			}
			else{
				showmessage();
				return;
			} 
		}catch (java.lang.NumberFormatException ivjExc){
			showmessage();
			return;
		}
	}
	if (e.getSource()==ivjJBCancel){
		setResult(ID_CANCEL);
		//whtao dispose();
		close();
	}
	if (e.getSource()==up){
		rowValueChanged(1);
		return;
	}
	if (e.getSource()==down){
		rowValueChanged(-1);
		return;
	}	
}
/**
 * @return
 * @see com.ufsoft.report.dialog.UfoDialog#getHelpID()
 */
protected String getHelpID(){
    return "TM_Format_Hign";
}
/**
 * ���� JBCanel ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBCancel() {
	if (ivjJBCancel == null) {
		try {
			ivjJBCancel = new com.ufsoft.table.beans.UFOButton();
			ivjJBCancel.setName("JBCancel");
			ivjJBCancel.setText(MultiLang.getString("cancel"));  //"ȡ  ��"
			ivjJBCancel.setBounds(125, 50, 75, 22);
			// user code begin {1}
			ivjJBCancel.addActionListener(this);
			ivjJBCancel.registerKeyboardAction(this,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false),JComponent.WHEN_FOCUSED);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJBCancel;
}
/**
 * ���� JBOK ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBOK() {
	if (ivjJBOK == null) {
		try {
			ivjJBOK = new com.ufsoft.table.beans.UFOButton();
			ivjJBOK.setName("JBOK");
			ivjJBOK.setText(MultiLang.getString("ok"));  //"ȷ  ��"
			ivjJBOK.setBounds(30, 50, 75, 22);
			ivjJBOK.addActionListener(this);
			ivjJBOK.registerKeyboardAction(this,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false),JComponent.WHEN_FOCUSED);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjJBOK;
}
/**
 * ���� JLabel1 ����ֵ��
 * @return javax.swing.JLabel
 */
/* ���棺�˷������������ɡ� */
//private javax.swing.JLabel getJLabel1() {
//	if (ivjJLabel1 == null) {
//		try {
//			ivjJLabel1 = new UILabel();
//			ivjJLabel1.setName("JLabel1");
//			ivjJLabel1.setFont(new java.awt.Font("dialog", 0, 14));
//			ivjJLabel1.setText(MultiLang.getString(this,"miufo1001143"));  //"ѡ������"
//			ivjJLabel1.setBounds(15, 17, 71, 16);
//			ivjJLabel1.setForeground(java.awt.Color.black);
//			// user code begin {1}
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJLabel1;
//}
/**
 * ���� JLabel2 ����ֵ��
 * @return javax.swing.JLabel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JLabel getJLabel2() {
	if (ivjJLabel2 == null) {
		try {
			ivjJLabel2 = new UFOLabel();
			ivjJLabel2.setName("JLabel2");
			ivjJLabel2.setText(MultiLang.getString("miufo1001164"));  //"�и�[����]��"
			ivjJLabel2.setBounds(15, 18, 80, 16);
			ivjJLabel2.setForeground(java.awt.Color.black);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjJLabel2;
}
/**
 * ��øı����и�ֵ��
 * �������ڣ�(00-11-27 10:49:28)
 * @return int
 */
public int getRowHeight() {
	return m_RowHeight;
}
/**
 * ���� TFArea ����ֵ��
 * @return javax.swing.JTextField
 */
/* ���棺�˷������������ɡ� */
//private javax.swing.JTextField getTFArea() {
//	if (ivjTFArea == null) {
//		try {
//			ivjTFArea = new UITextField();
//			ivjTFArea.setName("TFArea");
//			ivjTFArea.setFont(new java.awt.Font("monospaced", 0, 14));
//			ivjTFArea.setText("");
//			ivjTFArea.setBounds(91, 13, 80, 23);
//			ivjTFArea.setEditable(false);
//			// user code begin {1}
//			ivjTFArea.setRequestFocusEnabled(false);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjTFArea;
//}
/**
 * ���� JTextField1 ����ֵ��
 * @return javax.swing.JTextField
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JTextField getTFRowHeight() {
	if (ivjTFRowHeight == null) {
		try {
			ivjTFRowHeight = new JTextField();
			ivjTFRowHeight.setName("TFRowHeight");
			ivjTFRowHeight.setBackground(java.awt.SystemColor.controlHighlight);
			ivjTFRowHeight.setBounds(91, 14, 100, 23);
			ivjTFRowHeight.addActionListener(this);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjTFRowHeight;
}
/**
 * ���� UfoDialogContentPane ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getUfoDialogContentPane() {
	if (ivjUfoDialogContentPane == null) {
		try {
			ivjUfoDialogContentPane = new JPanel();
			ivjUfoDialogContentPane.setName("UfoDialogContentPane");
			ivjUfoDialogContentPane.setLayout(null);
			ivjUfoDialogContentPane.add(getTFRowHeight(), getTFRowHeight().getName());
			ivjUfoDialogContentPane.add(getJLabel2(), getJLabel2().getName());
			ivjUfoDialogContentPane.add(getJBOK(), getJBOK().getName());
			ivjUfoDialogContentPane.add(getJBCancel(), getJBCancel().getName());

			Rectangle r = ivjTFRowHeight.getBounds();
			up.setBounds(r.x+r.width,r.y,17,12);
			down.setBounds(r.x+r.width,r.y+11,17,12);
			ivjUfoDialogContentPane.add(up);
			ivjUfoDialogContentPane.add(down);
			up.addActionListener(this);
			down.addActionListener(this);
			up.setRequestFocusEnabled(false);
			down.setRequestFocusEnabled(false);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjUfoDialogContentPane;
}
/**
 * ÿ�������׳��쳣ʱ������
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
	// System.out.println("--------- δ��׽�����쳣 ---------");
	// exception.printStackTrace(System.out);
}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
	try {
		setName("SetRowHeightDlg");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
//		setFont(new java.awt.Font("dialog", 0, 14));
		setSize(230, 110);
		setTitle(MultiLang.getString("miufo1001165"));  //"�����и�"
		setContentPane(getUfoDialogContentPane());
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	setLocationRelativeTo(this);
	ivjJBOK.setNextFocusableComponent(ivjJBCancel);
	ivjJBCancel.setNextFocusableComponent(ivjTFRowHeight);
	ivjTFRowHeight.setNextFocusableComponent(ivjJBOK);
	ivjTFRowHeight.selectAll();

	// user code end
}
/**
 * Comment
 */
private void rowValueChanged(int num) {
	try{
		double newRowVal = Double.parseDouble(ivjTFRowHeight.getText());
		newRowVal = newRowVal + (double)num/DefaultSetting.RODAINLEN ;
		DecimalFormat df = new DecimalFormat("0.###");
		if ((num==-1 && newRowVal>=0) || (num==1 && newRowVal<=DefaultSetting.MAXROWHEIGHT/DefaultSetting.RODAINLEN)){
			ivjTFRowHeight.setText(df.format(newRowVal));
			ivjTFRowHeight.selectAll();
		}
	}catch (java.lang.NumberFormatException ivjExc){
		showmessage();
	}
}
/**
 * ������Ϣ��
 * �������ڣ�(00-11-28 14:58:24)
 */
private void showmessage() {
	String[] param = {Integer.toString((DefaultSetting.MAXROWHEIGHT/DefaultSetting.RODAINLEN))};
	String s = MultiLang.getString("miufo1001146",param);  //"������һ����0��{0}֮�������"
	JOptionPane.showMessageDialog(this,s,MultiLang.getString("ufida_soft_group"),JOptionPane.INFORMATION_MESSAGE);  //"�����������"
	ivjTFRowHeight.requestFocus();
	ivjTFRowHeight.selectAll();
}
}
