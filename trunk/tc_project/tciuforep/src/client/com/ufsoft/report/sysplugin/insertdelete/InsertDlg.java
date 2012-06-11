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
 * 插入对话框。包括插入行、插入列、插入表页
 * @author caijie
 */

public class InsertDlg extends UfoDialog implements ActionListener {
	
	private static final long serialVersionUID = -9003978339852131941L;
	/**
	 * 插入行 ERROR 使用表格控件Header中的常量
	 */
	public final static int INSERT_ROWS = 0;
	/**
	 * 插入列
	 */
	public final static int INSERT_COLUMNS= 1;
	
	private JButton ivjJBCancel = null;
	private JButton ivjJBOK = null;
	private JLabel ivjJLabel1 = null;
	private JPanel ivjUfoDialogContentPane = null;
	private int m_Num=1;//返回插入的数量
	BasicArrowButton up = new BasicArrowButton(BasicArrowButton.NORTH);
	BasicArrowButton down = new BasicArrowButton(BasicArrowButton.SOUTH);
	private JTextField ivjTFNum = null;
	
/**
 * 构造函数
 */
public InsertDlg() {
	super();
	initialize();
}
/**
type-插入的类别
0:插入行
1:插入列//ERROR 无用的内容删除，无用的构造器删除
2:插入表页
3:插入可变行
4:插入可变列
5:追加表页
6:追加可变行
7:追加可变列
 * @param type
 * @param owner
*/
public InsertDlg(int type,Container owner) 
{
	super(owner);
	initialize();

	
	switch (type){
		case 0://插入行
			setTitle(MultiLang.getString("miufo1001034"));  //"插入行"
			ivjJLabel1.setText(MultiLang.getString("miufo1001035"));  //"插入行数量："
			break;
		case 1://插入列
			setTitle(MultiLang.getString("miufo1001036"));  //"插入列"
			ivjJLabel1.setText(MultiLang.getString("miufo1001037"));  //"插入列数量："
			break;	
//		case 2://插入表页//ERROR
//			setTitle(StringResource.getStringResource("miufo1001038"));  //"插入表页"
//			ivjJLabel1.setText(StringResource.getStringResource("miufo1001039"));  //"插入表页数量："
//			break;
//		case 3://插入可变行
//			setTitle(StringResource.getStringResource("miufo1001040"));  //"插入可变行"
//			ivjJLabel1.setText(StringResource.getStringResource("miufo1001041"));  //"插入可变行数量："
//			break;
//		case 4://插入可变列
//			setTitle(StringResource.getStringResource("miufo1001042"));  //"插入可变列"
//			ivjJLabel1.setText(StringResource.getStringResource("miufo1001043"));  //"插入可变列数量："
//			break;
//		case 5://追加表页
//			setTitle(StringResource.getStringResource("miufo1001044"));  //"追加表页"
//			ivjJLabel1.setText(StringResource.getStringResource("miufo1001045"));  //"追加表页数量："
//			break;
//		case 6://追加可变行
//			setTitle(StringResource.getStringResource("miufo1001046"));  //"追加可变行"
//			ivjJLabel1.setText(StringResource.getStringResource("miufo1001047"));  //"追加可变行数量："
//			break;
//		case 7://追加可变列
//			setTitle(StringResource.getStringResource("miufo1001048"));  //"追加可变列"
//			ivjJLabel1.setText(StringResource.getStringResource("miufo1001049"));  //"追加可变列数量："
//			break;
		default:
			throw new IllegalArgumentException();
	}
}
/**
type-插入的类别
0:插入行
1:插入列
2:插入表页
3:插入可变行
4:插入可变列//ERROR
5:追加表页
6:追加可变行
7:追加可变列
 * @param type
 * @param owner
*/
public InsertDlg(int type,Frame owner) {
	super(owner);
	initialize();
	
	switch (type){
		case 0://插入行
			setTitle(MultiLang.getString("miufo1001034"));  //"插入行"
			ivjJLabel1.setText(MultiLang.getString("miufo1001035"));  //"插入行数量："
			break;
		case 1://插入列
			setTitle(MultiLang.getString("miufo1001036"));  //"插入列"
			ivjJLabel1.setText(MultiLang.getString("miufo1001037"));  //"插入列数量："
			break;	
		case 2://插入表页//ERROR
			setTitle(MultiLang.getString("miufo1001038"));  //"插入表页"
			ivjJLabel1.setText(MultiLang.getString("miufo1001039"));  //"插入表页数量："
			break;
		case 3://插入可变行
			setTitle(MultiLang.getString("miufo1001040"));  //"插入可变行"
			ivjJLabel1.setText(MultiLang.getString("miufo1001041"));  //"插入可变行数量："
			break;
		case 4://插入可变列
			setTitle(MultiLang.getString("miufo1001042"));  //"插入可变列"
			ivjJLabel1.setText(MultiLang.getString("miufo1001043"));  //"插入可变列数量："
			break;
		case 5://追加表页
			setTitle(MultiLang.getString("miufo1001044"));  //"追加表页"
			ivjJLabel1.setText(MultiLang.getString("miufo1001045"));  //"追加表页数量："
			break;
		case 6://追加可变行
			setTitle(MultiLang.getString("miufo1001046"));  //"追加可变行"
			ivjJLabel1.setText(MultiLang.getString("miufo1001047"));  //"追加可变行数量："
			break;
		case 7://追加可变列
			setTitle(MultiLang.getString("miufo1001048"));  //"追加可变列"
			ivjJLabel1.setText(MultiLang.getString("miufo1001049"));  //"追加可变列数量："
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
 * 返回 JBCancel 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBCancel() {
	if (ivjJBCancel == null) {
		try {
			ivjJBCancel = new nc.ui.pub.beans.UIButton();
			ivjJBCancel.setName("JBCancel");
			ivjJBCancel.setText(MultiLang.getString("miufo1000065"));  //"取  消"
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
 * 返回 JBOK 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBOK() {
	if (ivjJBOK == null) {
		try {
			ivjJBOK = new nc.ui.pub.beans.UIButton();
			ivjJBOK.setName("JBOK");

			ivjJBOK.setText(MultiLang.getString("miufo1000002"));  //"确  定"
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
 * 返回 JLabel1 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
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
 * 返回插入数目。
 * 创建日期：(00-11-27 10:50:08)
 * @return int
 */
public int getNum() {
	return m_Num;
}
/**
 * 返回 TFColNum 特性值。
 * @return javax.swing.JTextField
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 UfoDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
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
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	// System.out.println("--------- 未捕捉到的异常 ---------");
	// exception.printStackTrace(System.out);
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
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
 * 此处插入方法说明。
 * 创建日期：(00-11-28 14:58:24)
 //ERROR 不合规范
 */
private void showmessage() {
	JOptionPane.showMessageDialog(this,MultiLang.getString("miufo1001050"),MultiLang.getString("miufo1000761"),JOptionPane.ERROR_MESSAGE);
	ivjTFNum.requestFocus();
	ivjTFNum.selectAll();
}
}
