package com.ufsoft.report.sysplugin.log;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
/**
 * 日志信息显示窗口类。 创建日期：(2001-3-29 16:34:55)
 * 
 * @author：朱亚军
 */
public class LogWindow extends UfoDialog {

	private JButton ivjJClear = null;
	private JButton ivjJClose = null;
	private JPanel ivjJDialogContentPane = null;
	private JTextArea ivjJLogText = null;
	private JScrollPane ivjJScrollPane1 = null;
	private javax.swing.text.Document doc = null;
	MyActionListener myListen = new MyActionListener();
	
	private UfoReport _report = null;

	class MyActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			Object source = event.getSource();
			if (source == ivjJClear) {
				clearLog();
			} else if (source == ivjJClose) {
				setResult(ID_OK);
				close();
			}
		}
	}

	class DocListener implements javax.swing.event.DocumentListener {
		public void changedUpdate(DocumentEvent e) {
		}
		public void removeUpdate(DocumentEvent e) {
		}
		public void insertUpdate(DocumentEvent e) {
			if (LogWindow.this.ivjJLogText.getText() != null
					&& LogWindow.this.ivjJClear.isEnabled() == false) {
				LogWindow.this.ivjJClear.setEnabled(true);
			}
		}

	}
	/**
	 * 此处插入方法描述。 创建日期：(2002-4-22 10:24:54)
	 * 
	 * @param owner
	 *            java.awt.Container
	 */
	public LogWindow(UfoReport owner) {
		super(owner);
		_report = owner;
		initialize();
	}
	/**
	 * @return
	 * @see com.ufsoft.report.dialog.UfoDialog#getHelpID()
	 */
	protected String getHelpID(){
	    return "TM_Tool_Log";
	}
	/**
	 * 填加日志信息
	 * 
	 * @param logMsg
	 */
	public void addLog(String logMsg) {
		ivjJLogText.append(logMsg);
		ivjJLogText.append("\n");
		if (ivjJClear.isEnabled() == false) {
			ivjJClear.setEnabled(true);
		}
		repaint();
	}
	/**
	 * 清除日志内容
	 */
	public void clearLog() {
		int nAnswer = JOptionPane.showConfirmDialog(_report,
		        MultiLang.getString("miufo1001086"), //"清除日志文件的内容？"
				MultiLang.getString("miufo1000185"),
				JOptionPane.YES_NO_OPTION); //"提示信息"
		if (nAnswer == JOptionPane.YES_OPTION) {
			ivjJLogText.setText(null);
			ivjJClear.setEnabled(false);
		}
		//	this.setVisible(true);
	}
	/**
	 * 返回 JClear 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJClear() {
		if (ivjJClear == null) {
			try {
				ivjJClear = new nc.ui.pub.beans.UIButton();
				ivjJClear.setName("JClear");
				ivjJClear.setText(MultiLang.getString("clear")); //"清除"
				ivjJClear.setBounds(94, 324, 75, 22);
				// user code begin {1}
				ivjJClear.setEnabled(false);
				ivjJClear.addActionListener(myListen);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJClear;
	}
	/**
	 * 返回 JClose 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJClose() {
		if (ivjJClose == null) {
			try {
				ivjJClose = new nc.ui.pub.beans.UIButton();
				ivjJClose.setName("JClose");
				ivjJClose.setText(MultiLang.getString("close")); //"关闭"
				ivjJClose.setBounds(234, 324, 75, 22);
				// user code begin {1}
				ivjJClose.addActionListener(myListen);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJClose;
	}
	/**
	 * 返回 JDialogContentPane 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getJDialogContentPane() {
		if (ivjJDialogContentPane == null) {
			try {
				ivjJDialogContentPane = new UIPanel();
				ivjJDialogContentPane.setName("JDialogContentPane");
				ivjJDialogContentPane.setLayout(null);
				getJDialogContentPane().add(getJScrollPane1(),
						getJScrollPane1().getName());
				getJDialogContentPane().add(getJClear(), getJClear().getName());
				getJDialogContentPane().add(getJClose(), getJClose().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJDialogContentPane;
	}
	/**
	 * 返回 JLogText 特性值。
	 * 
	 * @return javax.swing.JTextArea
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextArea getJLogText() {
		if (ivjJLogText == null) {
			try {
				ivjJLogText = new UITextArea();
				ivjJLogText.setName("JLogText");
				ivjJLogText.setLineWrap(true);
				ivjJLogText.setWrapStyleWord(true);
				ivjJLogText.setBounds(0, 0, 178, 120);
				ivjJLogText.setEditable(true);
				ivjJLogText.setDocument(doc);
				ivjJLogText.getDocument()
						.addDocumentListener(new DocListener());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLogText;
	}
	/**
	 * 返回 JScrollPane1 特性值。
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JScrollPane getJScrollPane1() {
		if (ivjJScrollPane1 == null) {
			try {
				ivjJScrollPane1 = new UIScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setAutoscrolls(true);
				ivjJScrollPane1
						.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				ivjJScrollPane1.setBounds(7, 8, 420, 289);
				getJScrollPane1().setViewportView(getJLogText());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPane1;
	}
	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
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
			doc = new javax.swing.text.PlainDocument();
			setTitle(MultiLang.getString("miufo1001087")); //"UFO
																		// 日志信息"
			setModal(true);
			//setVisible(false);
			setResizable(false);
			// user code end
			setName("LogWindow");
			setSize(442, 400);
			setContentPane(getJDialogContentPane());
			String text = ivjJLogText.getText();
			if (text != null && text.length() > 0) {
				getJClear().setEnabled(true);
			}
			setLocationRelativeTo(getParent());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

}