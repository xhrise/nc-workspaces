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
 *  设置行高对话框 
 * ERROR 应该和设置行宽的对话框用一个就可以了.
 * @author：CaiJie
 */
public class SetRowHeightDlg extends UfoDialog implements ActionListener {
	private JButton ivjJBOK = null;
	private JLabel ivjJLabel2 = null;
	private JPanel ivjUfoDialogContentPane = null;
	private int m_RowHeight=-1;//返回设置的行高
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
 * @param rowheight 行高
 * @param owner 父组件
 */
public SetRowHeightDlg(int rowheight, Frame owner) {
	super(owner);
	initialize();
	
	m_RowHeight = rowheight;//设置当前行高
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
 * 返回 JBCanel 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBCancel() {
	if (ivjJBCancel == null) {
		try {
			ivjJBCancel = new com.ufsoft.table.beans.UFOButton();
			ivjJBCancel.setName("JBCancel");
			ivjJBCancel.setText(MultiLang.getString("cancel"));  //"取  消"
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
 * 返回 JBOK 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBOK() {
	if (ivjJBOK == null) {
		try {
			ivjJBOK = new com.ufsoft.table.beans.UFOButton();
			ivjJBOK.setName("JBOK");
			ivjJBOK.setText(MultiLang.getString("ok"));  //"确  定"
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
 * 返回 JLabel1 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
//private javax.swing.JLabel getJLabel1() {
//	if (ivjJLabel1 == null) {
//		try {
//			ivjJLabel1 = new UILabel();
//			ivjJLabel1.setName("JLabel1");
//			ivjJLabel1.setFont(new java.awt.Font("dialog", 0, 14));
//			ivjJLabel1.setText(MultiLang.getString(this,"miufo1001143"));  //"选中区域："
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
 * 返回 JLabel2 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getJLabel2() {
	if (ivjJLabel2 == null) {
		try {
			ivjJLabel2 = new UFOLabel();
			ivjJLabel2.setName("JLabel2");
			ivjJLabel2.setText(MultiLang.getString("miufo1001164"));  //"行高[毫米]："
			ivjJLabel2.setBounds(15, 18, 80, 16);
			ivjJLabel2.setForeground(java.awt.Color.black);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjJLabel2;
}
/**
 * 获得改变后的行高值。
 * 创建日期：(00-11-27 10:49:28)
 * @return int
 */
public int getRowHeight() {
	return m_RowHeight;
}
/**
 * 返回 TFArea 特性值。
 * @return javax.swing.JTextField
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 JTextField1 特性值。
 * @return javax.swing.JTextField
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 UfoDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
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
		setName("SetRowHeightDlg");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
//		setFont(new java.awt.Font("dialog", 0, 14));
		setSize(230, 110);
		setTitle(MultiLang.getString("miufo1001165"));  //"设置行高"
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
 * 错误信息。
 * 创建日期：(00-11-28 14:58:24)
 */
private void showmessage() {
	String[] param = {Integer.toString((DefaultSetting.MAXROWHEIGHT/DefaultSetting.RODAINLEN))};
	String s = MultiLang.getString("miufo1001146",param);  //"请填入一个在0和{0}之间的数！"
	JOptionPane.showMessageDialog(this,s,MultiLang.getString("ufida_soft_group"),JOptionPane.INFORMATION_MESSAGE);  //"用友软件集团"
	ivjTFRowHeight.requestFocus();
	ivjTFRowHeight.selectAll();
}
}
