/*
 * InsertCellDlg.java
 * Created on 2004-10-25 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.insertdelete;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;

import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

/**
 * ���뵥Ԫ�Ի���
 * 
 * @author CaiJie
 * @since 3.1
 */

public class InsertCellDlg extends com.ufsoft.report.dialog.UfoDialog
		implements
			ActionListener {
	private JPanel ivjUfoDialogContentPane = null;
	private JButton ivjOk = null;
	private JButton ivjCancel = null;
	private JRadioButton ivjJRadioButtonRight = null;
	private JRadioButton ivjJRadioButtonDown = null;
	
	public static final int MOVE_RIGHT_WHEN_INSERT_CELL = 0;
	public static final int MOVE_DOWN_WHEN_INSERT_CELL = 1;
	//wss add
	private int iSelected = MOVE_RIGHT_WHEN_INSERT_CELL;
	
	/**
	 * DelCellDlg ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public InsertCellDlg(java.awt.Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param e
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource() == ivjOk) {
			setResult(UfoDialog.ID_OK);
			close();
			return;
		}
		if (e.getSource() == ivjCancel) {
			setResult(UfoDialog.ID_CANCEL);
			close();
			return;
		}

		if (e.getSource() instanceof JRadioButton) {
			JRadioButton source = (JRadioButton) e.getSource();
			if (source == ivjJRadioButtonRight) {
				iSelected = MOVE_RIGHT_WHEN_INSERT_CELL;
				return;
			}
			if (source == ivjJRadioButtonDown) {
				iSelected = MOVE_DOWN_WHEN_INSERT_CELL;
				return;
			}
		}
	}
	/**
	 * ���� JButton2 ����ֵ��
	 * 
	 * @return javax.swing.JButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JButton getCancel() {
		if (ivjCancel == null) {
			try {
				ivjCancel = new nc.ui.pub.beans.UIButton();
				ivjCancel.setName("Cancel"); 
				ivjCancel.setText(MultiLang.getString("miufo1000065")); //"ȡ��"
				ivjCancel.setBounds(165, 58, 75, 22);
				// user code begin {1}
				ivjCancel.addActionListener(this);
				ivjCancel.registerKeyboardAction(this, KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCancel;
	}
	/**
	 * ���� JRadioButton1 ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JRadioButton getJRadioButtonLeft() {
		if (ivjJRadioButtonRight == null) {
			try {
				ivjJRadioButtonRight = new UIRadioButton();
				ivjJRadioButtonRight.setName("JRadioButtonLeft");
				ivjJRadioButtonRight
						.setText(MultiLang.getString("miufo1001030")); //"���Ԫ������"
				ivjJRadioButtonRight.setBounds(24, 19, 125, 24);
				ivjJRadioButtonRight.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJRadioButtonRight;
	}
	/**
	 * ���� JRadioButton2 ����ֵ��
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JRadioButton getJRadioButtonTop() {
		if (ivjJRadioButtonDown == null) {
			try {
				ivjJRadioButtonDown = new UIRadioButton();
				ivjJRadioButtonDown.setName("JRadioButtonTop");
//				ivjJRadioButtonDown.setFont(new java.awt.Font("dialog", 0, 14));
				ivjJRadioButtonDown
						.setText(MultiLang.getString("miufo1001031")); //"���Ԫ������"
				ivjJRadioButtonDown.setBounds(24, 58, 125, 24);
				ivjJRadioButtonDown.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJRadioButtonDown;
	}
	/**
	 * ���� JButton1 ����ֵ��
	 * 
	 * @return javax.swing.JButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JButton getOk() {
		if (ivjOk == null) {
			try {
				ivjOk = new nc.ui.pub.beans.UIButton();
				ivjOk.setName("Ok");
				ivjOk.setText(MultiLang.getString("miufo1000002")); //"ȷ��"
				ivjOk.setBounds(165, 19, 75, 22);
				ivjOk.addActionListener(this);
				ivjOk.registerKeyboardAction(this, KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjOk;
	}
	/**
	 * �˴����뷽�������� �������ڣ�(2002-08-30 11:03:51)
	 * 
	 * @return int
	 */
	public int getSelected() {
		return iSelected;
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
				getUfoDialogContentPane().add(getJRadioButtonLeft(),
						getJRadioButtonLeft().getName());
				getUfoDialogContentPane().add(getJRadioButtonTop(),
						getJRadioButtonTop().getName());
				getUfoDialogContentPane().add(getOk(), getOk().getName());
				getUfoDialogContentPane().add(getCancel(),
						getCancel().getName());
				// user code begin {1}
				javax.swing.ButtonGroup groupH = new javax.swing.ButtonGroup();
				groupH.add(getJRadioButtonLeft());
				groupH.add(getJRadioButtonTop());
				getJRadioButtonLeft().setSelected(true);

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
	 * 
	 * @param exception
	 *            java.lang.Throwable
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
			setName("InsertCellDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(260, 126);
			setTitle(MultiLang.getString("miufo1001032")); //"���뵥Ԫ"
			setContentPane(getUfoDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}
	/**
	 * �˴����뷽�������� �������ڣ�(2002-08-30 11:03:51)
	 * 
	 * @param newSelected
	 *            int
	 */
	public void setSelected(int newSelected) {
		iSelected = newSelected;
	}
}

