package com.ufsoft.report.dialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.ufsoft.report.util.MultiLang;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
/**
 * 设置组合单元对话框。 创建日期：(00-12-4 15:40:48)
 * 
 * @author：岳益民
 */
public class CombineCellDlg extends UfoDialog implements ActionListener {
	private JButton ivjJBAll = null;
	private JButton ivjJBCancel = null;
	private JButton ivjJBCol = null;
	private JButton ivjJBRow = null;
	private JLabel ivjJLabel1 = null;
	private JTextField ivjJTFCell = null;
	private JPanel ivjUfoDialogContentPane = null;
	/**
	 * <code>ID_COMALL</code> 整个合并
	 */
	public final static int ID_COMALL = 3;
	/**
	 * <code>ID_COMROW</code> 按行合并
	 */
	public final static int ID_COMROW = 4;
	/**
	 * <code>ID_COMCOL</code> 按列合并
	 */
	public final static int ID_COMCOL = 5;
	/**
	 * <code>ID_COMREMOVE</code> 删除组合单元
	 */
	public final static int ID_COMREMOVE = 6;
	private JButton ivjJBRemove = null;
	/**
	 * @param area 待组合区域
	 * @param owner 父容器
	 */
	public CombineCellDlg(String area, Frame owner) {
		super(owner);
		initialize();
		//获得区域信息
		if (area != null)
			ivjJTFCell.setText(area);
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ivjJBAll) {
			setResult(ID_COMALL);//整体组合
		}
		else if (e.getSource() == ivjJBCancel) {
			setResult(ID_CANCEL);
		}
		else if (e.getSource() == ivjJBCol) {
			setResult(ID_COMCOL);//按列组合
		}
		else if (e.getSource() == ivjJBRow) {
			setResult(ID_COMROW);//按行组合
		}
		else if (e.getSource() == ivjJBRemove) {
			setResult(ID_COMREMOVE);//删除组合单元
		}
		close();

	}
	/**
	 * 返回 JBAll 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJBAll() {
		if (ivjJBAll == null) {
			try {
				ivjJBAll = new nc.ui.pub.beans.UIButton();
				ivjJBAll.setName("JBAll");
				ivjJBAll.setText(MultiLang.getString("combination_all"));
				ivjJBAll.setBounds(132, 33, 75, 22);
				// user code begin {1}
				ivjJBAll.addActionListener(this);
				ivjJBAll.registerKeyboardAction(this, KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJBAll;
	}
	/**
	 * 返回 JBCancel 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJBCancel() {
		if (ivjJBCancel == null) {
			try {
				ivjJBCancel = new nc.ui.pub.beans.UIButton();
				ivjJBCancel.setName("JBCancel");
				ivjJBCancel.setText(MultiLang.getString("cancel"));
				ivjJBCancel.setBounds(241, 84, 75, 22);
				// user code begin {1}
				ivjJBCancel.addActionListener(this);
				ivjJBCancel.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
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
	 * 返回 JBCol 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJBCol() {
		if (ivjJBCol == null) {
			try {
				ivjJBCol = new nc.ui.pub.beans.UIButton();
				ivjJBCol.setName("JBCol");
				ivjJBCol.setText(MultiLang.getString("combination_col"));
				ivjJBCol.setBounds(132, 84, 75, 22);
				// user code begin {1}
				ivjJBCol.addActionListener(this);
				ivjJBCol.registerKeyboardAction(this, KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJBCol;
	}
	/**
	 * 返回 JBRomove 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJBRemove() {
		if (ivjJBRemove == null) {
			try {
				ivjJBRemove = new nc.ui.pub.beans.UIButton();
				ivjJBRemove.setName("JBRemove");
				ivjJBRemove.setText(MultiLang.getString("combination_cancel"));
				ivjJBRemove.setBounds(241, 33, 75, 22);
				// user code begin {1}
				ivjJBRemove.addActionListener(this);
				ivjJBRemove.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
						JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJBRemove;
	}
	/**
	 * 返回 JBRow 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JButton getJBRow() {
		if (ivjJBRow == null) {
			try {
				ivjJBRow = new nc.ui.pub.beans.UIButton();
				ivjJBRow.setName("JBRow");
				ivjJBRow.setText(MultiLang.getString("combination_row"));
				ivjJBRow.setBounds(23, 84, 75, 22);
				// user code begin {1}
				ivjJBRow.addActionListener(this);
				ivjJBRow.registerKeyboardAction(this, KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJBRow;
	}
	/**
	 * 返回 JLabel1 特性值。
	 * 
	 * @return javax.swing.JLabel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JLabel getJLabel1() {
		if (ivjJLabel1 == null) {
			try {
				ivjJLabel1 = new UILabel();
				ivjJLabel1.setName("JLabel1");
				ivjJLabel1.setText(MultiLang.getString("selected_combination_area"));
				ivjJLabel1.setBounds(24, 10, 102, 16);
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
	 * 返回 JTFCell 特性值。
	 * 
	 * @return javax.swing.JTextField
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JTextField getJTFCell() {
		if (ivjJTFCell == null) {
			try {
				ivjJTFCell = new UITextField();
				ivjJTFCell.setName("JTFCell");
				ivjJTFCell.setBounds(23, 33, 93, 27);
				ivjJTFCell.setEnabled(true);
				ivjJTFCell.setEditable(false);
				// user code begin {1}
				ivjJTFCell.setHorizontalAlignment(JTextField.CENTER);
				ivjJTFCell.setRequestFocusEnabled(false);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTFCell;
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
				getUfoDialogContentPane().add(getJBAll(), getJBAll().getName());
				getUfoDialogContentPane().add(getJLabel1(),
						getJLabel1().getName());
				getUfoDialogContentPane().add(getJTFCell(),
						getJTFCell().getName());
				getUfoDialogContentPane().add(getJBRemove(),
						getJBRemove().getName());
				getUfoDialogContentPane().add(getJBRow(), getJBRow().getName());
				getUfoDialogContentPane().add(getJBCol(), getJBCol().getName());
				getUfoDialogContentPane().add(getJBCancel(),
						getJBCancel().getName());
				// user code begin {1}
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
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
	}
	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("CombineCellDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setResizable(false);
			setModal(true);
			setSize(351, 163);
			setTitle(MultiLang.getString("miufo1000900"));
			setContentPane(getUfoDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		setLocationRelativeTo(this);
		ivjJBRemove.setNextFocusableComponent(ivjJBRow);
		ivjJBRow.setNextFocusableComponent(ivjJBCol);
		ivjJBCol.setNextFocusableComponent(ivjJBCancel);
		ivjJBCancel.setNextFocusableComponent(ivjJBAll);
		ivjJBAll.setNextFocusableComponent(ivjJBRemove);
		// user code end
	}
}