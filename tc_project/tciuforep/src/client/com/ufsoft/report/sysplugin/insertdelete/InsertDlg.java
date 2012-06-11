package com.ufsoft.report.sysplugin.insertdelete;


import java.awt.Container;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicArrowButton;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

/**
 * ����Ի��򡣰��������С������С������ҳ
 * @author caijie
 */

public class InsertDlg extends UfoDialog implements ActionListener {
	
	private static final long serialVersionUID = -9003978339852131941L;
	/**
	 * ������ ERROR ʹ�ñ��ؼ�Header�еĳ���
	 */
	public final static int INSERT_ROWS = 0;
	/**
	 * ������
	 */
	public final static int INSERT_COLUMNS= 1;
	
	private JButton ivjJBCancel = null;
	private JButton ivjJBOK = null;
	private JLabel ivjJLabel1 = null;
	private JPanel ivjUfoDialogContentPane = null;
	private int m_Num=1;//���ز��������
	BasicArrowButton up = new BasicArrowButton(BasicArrowButton.NORTH);
	BasicArrowButton down = new BasicArrowButton(BasicArrowButton.SOUTH);
	private JTextField ivjTFNum = null;
	
/**
 * ���캯��
 */
public InsertDlg() {
	super();
	initialize();
}
/**
type-��������
0:������
1:������//ERROR ���õ�����ɾ�������õĹ�����ɾ��
2:�����ҳ
3:����ɱ���
4:����ɱ���
5:׷�ӱ�ҳ
6:׷�ӿɱ���
7:׷�ӿɱ���
 * @param type
 * @param owner
*/
public InsertDlg(int type,Container owner) 
{
	super(owner);
	initialize();

	
	switch (type){
		case 0://������
			setTitle(MultiLang.getString("miufo1001034"));  //"������"
			ivjJLabel1.setText(MultiLang.getString("miufo1001035"));  //"������������"
			break;
		case 1://������
			setTitle(MultiLang.getString("miufo1001036"));  //"������"
			ivjJLabel1.setText(MultiLang.getString("miufo1001037"));  //"������������"
			break;	
//		case 2://�����ҳ//ERROR
//			setTitle(StringResource.getStringResource("miufo1001038"));  //"�����ҳ"
//			ivjJLabel1.setText(StringResource.getStringResource("miufo1001039"));  //"�����ҳ������"
//			break;
//		case 3://����ɱ���
//			setTitle(StringResource.getStringResource("miufo1001040"));  //"����ɱ���"
//			ivjJLabel1.setText(StringResource.getStringResource("miufo1001041"));  //"����ɱ���������"
//			break;
//		case 4://����ɱ���
//			setTitle(StringResource.getStringResource("miufo1001042"));  //"����ɱ���"
//			ivjJLabel1.setText(StringResource.getStringResource("miufo1001043"));  //"����ɱ���������"
//			break;
//		case 5://׷�ӱ�ҳ
//			setTitle(StringResource.getStringResource("miufo1001044"));  //"׷�ӱ�ҳ"
//			ivjJLabel1.setText(StringResource.getStringResource("miufo1001045"));  //"׷�ӱ�ҳ������"
//			break;
//		case 6://׷�ӿɱ���
//			setTitle(StringResource.getStringResource("miufo1001046"));  //"׷�ӿɱ���"
//			ivjJLabel1.setText(StringResource.getStringResource("miufo1001047"));  //"׷�ӿɱ���������"
//			break;
//		case 7://׷�ӿɱ���
//			setTitle(StringResource.getStringResource("miufo1001048"));  //"׷�ӿɱ���"
//			ivjJLabel1.setText(StringResource.getStringResource("miufo1001049"));  //"׷�ӿɱ���������"
//			break;
		default:
			throw new IllegalArgumentException();
	}
}
/**
type-��������
0:������
1:������
2:�����ҳ
3:����ɱ���
4:����ɱ���//ERROR
5:׷�ӱ�ҳ
6:׷�ӿɱ���
7:׷�ӿɱ���
 * @param type
 * @param owner
*/
public InsertDlg(int type,Frame owner) {
	super(owner);
	initialize();
	
	switch (type){
		case 0://������
			setTitle(MultiLang.getString("miufo1001034"));  //"������"
			ivjJLabel1.setText(MultiLang.getString("miufo1001035"));  //"������������"
			break;
		case 1://������
			setTitle(MultiLang.getString("miufo1001036"));  //"������"
			ivjJLabel1.setText(MultiLang.getString("miufo1001037"));  //"������������"
			break;	
		case 2://�����ҳ//ERROR
			setTitle(MultiLang.getString("miufo1001038"));  //"�����ҳ"
			ivjJLabel1.setText(MultiLang.getString("miufo1001039"));  //"�����ҳ������"
			break;
		case 3://����ɱ���
			setTitle(MultiLang.getString("miufo1001040"));  //"����ɱ���"
			ivjJLabel1.setText(MultiLang.getString("miufo1001041"));  //"����ɱ���������"
			break;
		case 4://����ɱ���
			setTitle(MultiLang.getString("miufo1001042"));  //"����ɱ���"
			ivjJLabel1.setText(MultiLang.getString("miufo1001043"));  //"����ɱ���������"
			break;
		case 5://׷�ӱ�ҳ
			setTitle(MultiLang.getString("miufo1001044"));  //"׷�ӱ�ҳ"
			ivjJLabel1.setText(MultiLang.getString("miufo1001045"));  //"׷�ӱ�ҳ������"
			break;
		case 6://׷�ӿɱ���
			setTitle(MultiLang.getString("miufo1001046"));  //"׷�ӿɱ���"
			ivjJLabel1.setText(MultiLang.getString("miufo1001047"));  //"׷�ӿɱ���������"
			break;
		case 7://׷�ӿɱ���
			setTitle(MultiLang.getString("miufo1001048"));  //"׷�ӿɱ���"
			ivjJLabel1.setText(MultiLang.getString("miufo1001049"));  //"׷�ӿɱ���������"
			break;
		default:
			break;	
	}
}
	/**
	 * Invoked when an action occurs.
	 * @param e
	 */
public void actionPerformed(ActionEvent e) {
	if (e.getSource()==ivjJBOK || e.getSource()==ivjTFNum){
		try{
			int num =Integer.parseInt(ivjTFNum.getText());
			if (num>=1) {
				setResult(ID_OK);
				m_Num= num;
				//whtao dispose();
				close();
			}
			else{
				showmessage();
				return;
			} 
		}catch (java.lang.Throwable ivjExc){
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
		numChanged(1);
		return;
	}
	if (e.getSource()==down){
		numChanged(-1);
		return;
	}			
}

/**
 * ���� JBCancel ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBCancel() {
	if (ivjJBCancel == null) {
		try {
			ivjJBCancel = new nc.ui.pub.beans.UIButton();
			ivjJBCancel.setName("JBCancel");
			ivjJBCancel.setText(MultiLang.getString("miufo1000065"));  //"ȡ  ��"
			ivjJBCancel.setBounds(148, 50, 75, 22);
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
			ivjJBOK = new nc.ui.pub.beans.UIButton();
			ivjJBOK.setName("JBOK");

			ivjJBOK.setText(MultiLang.getString("miufo1000002"));  //"ȷ  ��"
			ivjJBOK.setBounds(148, 13, 75, 22);
			// user code begin {1}
			ivjJBOK.addActionListener(this);
			ivjJBOK.registerKeyboardAction(this,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false),JComponent.WHEN_FOCUSED);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
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
private javax.swing.JLabel getJLabel1() {
	if (ivjJLabel1 == null) {
		try {
			ivjJLabel1 = new UILabel();
			ivjJLabel1.setName("JLabel1");
//			ivjJLabel1.setFont(new java.awt.Font("dialog", 0, 14));//ERROR
			ivjJLabel1.setText("");
			ivjJLabel1.setBounds(13, 18, 124, 16);
			ivjJLabel1.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel1;
}
/**
 * ���ز�����Ŀ��
 * �������ڣ�(00-11-27 10:50:08)
 * @return int
 */
public int getNum() {
	return m_Num;
}
/**
 * ���� TFColNum ����ֵ��
 * @return javax.swing.JTextField
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JTextField getTFNum() {
	if (ivjTFNum == null) {
		try {
			ivjTFNum = new UITextField();
			ivjTFNum.setName("TFNum");
//			ivjTFNum.setFont(new java.awt.Font("dialog", 0, 14));//ERROR
			ivjTFNum.setText("1");
			ivjTFNum.setBackground(java.awt.SystemColor.controlHighlight);
			ivjTFNum.setBounds(28, 46, 65, 23);
			// user code begin {1}
			ivjTFNum.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTFNum;
}
/**
 * ���� UfoDialogContentPane ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getUfoDialogContentPane() {
	if (ivjUfoDialogContentPane == null) {
		try {
			ivjUfoDialogContentPane = new UIPanel();
			ivjUfoDialogContentPane.setName("UfoDialogContentPane");
//			ivjUfoDialogContentPane.setFont(new java.awt.Font("dialog", 0, 14));//ERROR
			ivjUfoDialogContentPane.setLayout(null);
			getUfoDialogContentPane().add(getJLabel1(), getJLabel1().getName());
			getUfoDialogContentPane().add(getTFNum(), getTFNum().getName());
			getUfoDialogContentPane().add(getJBOK(), getJBOK().getName());
			getUfoDialogContentPane().add(getJBCancel(), getJBCancel().getName());
			// user code begin {1}
			Rectangle r = ivjTFNum.getBounds();
			up.setBounds(r.x+r.width,r.y,17,12);
			down.setBounds(r.x+r.width,r.y+11,17,12);
			getUfoDialogContentPane().add(up);
			getUfoDialogContentPane().add(down);
			up.addActionListener(this);
			down.addActionListener(this);
			up.setRequestFocusEnabled(false);
			down.setRequestFocusEnabled(false);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
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
		// user code begin {1}
		// user code end
		setName("InsertDlg");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
//		setFont(new java.awt.Font("dialog", 0, 14));//ERROR
		setSize(238, 117);
		setTitle("");
		setContentPane(getUfoDialogContentPane());
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	setLocationRelativeTo(this);
	ivjJBOK.setNextFocusableComponent(ivjJBCancel);
	ivjJBCancel.setNextFocusableComponent(ivjTFNum);
	ivjTFNum.setNextFocusableComponent(ivjJBOK);
	ivjTFNum.selectAll();
	// user code end
}

/**
 * Comment
 */
private void numChanged(int num) {
	try{
		int newNumVal = Integer.parseInt(ivjTFNum.getText());
		newNumVal =newNumVal+num;
		if (newNumVal>=1 ){
			ivjTFNum.setText(Integer.toString(newNumVal));
			ivjTFNum.selectAll();
		}
	}catch (java.lang.NumberFormatException ivjExc){
		showmessage();
	}
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(00-11-28 14:58:24)
 //ERROR ���Ϲ淶
 */
private void showmessage() {
	JOptionPane.showMessageDialog(this,MultiLang.getString("miufo1001050"),MultiLang.getString("miufo1000761"),JOptionPane.ERROR_MESSAGE);
	ivjTFNum.requestFocus();
	ivjTFNum.selectAll();
}
}
