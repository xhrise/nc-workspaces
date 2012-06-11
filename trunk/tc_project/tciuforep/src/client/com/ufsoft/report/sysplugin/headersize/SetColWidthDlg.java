package com.ufsoft.report.sysplugin.headersize;

import java.awt.Container;
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
 * �ı��п�Ի���
 * 
 * @author��CaiJie
 */
public class SetColWidthDlg extends UfoDialog implements ActionListener {
	private JButton ivjJBCancel = null;
	private JButton ivjJBOK = null;
	//	private JLabel ivjJLabel1 = null;
	private JLabel ivjJLabel2 = null;
	//	private JTextField ivjTFArea = null;
	private JTextField ivjTFColWidth = null;
	private JPanel ivjUfoDialogContentPane = null;
	//	private String m_area=null;
	private int m_ColWidth = -1;//�������õ��п�
	BasicArrowButton up = new BasicArrowButton(BasicArrowButton.NORTH);
	BasicArrowButton down = new BasicArrowButton(BasicArrowButton.SOUTH);
	/**
	 * @param colwidth-��ǰ�п�
	 * @param owner
	 */
	public SetColWidthDlg(int colwidth, Container owner) {
		super(owner);
		initialize();

		//	m_area = area;//��������
		//	ivjTFArea.setText(m_area);

		m_ColWidth = colwidth;//���õ�ǰ�п�
		double newColVal = (double) m_ColWidth / DefaultSetting.RODAINLEN;
		DecimalFormat df = new DecimalFormat("0.###");
		ivjTFColWidth.setText(df.format(newColVal));
		ivjTFColWidth.selectAll();
	}
	/**
	 * @param colwidth-��ǰ�п�
	 * @param owner
	 */
	public SetColWidthDlg(int colwidth, Frame owner) {
		super(owner);
		initialize();

		//	m_area = area;//��������
		//	ivjTFArea.setText(m_area);

		m_ColWidth = colwidth;//���õ�ǰ�п�
		double newColVal = (double) m_ColWidth / DefaultSetting.RODAINLEN;
		DecimalFormat df = new DecimalFormat("0.###");
		ivjTFColWidth.setText(df.format(newColVal));
		ivjTFColWidth.selectAll();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ivjJBOK || e.getSource() == ivjTFColWidth) {
			try {
				int ColVa = (int) ((Double.parseDouble(ivjTFColWidth.getText()) + 1d / DefaultSetting.RODAINLEN / 2) * DefaultSetting.RODAINLEN);
				if ((ColVa >= 0) && (ColVa <= DefaultSetting.MAXCOLWIDTH)) {
					setResult(UfoDialog.ID_OK);
					m_ColWidth = ColVa;
					//whtao dispose();
					close();
				} else {
					showmessage();
					return;
				}
			} catch (java.lang.NumberFormatException ivjExc) {
				showmessage();
				return;
			}
		}
		if (e.getSource() == ivjJBCancel) {
			setResult(UfoDialog.ID_CANCEL);
			//whtao dispose();
			close();
		}
		if (e.getSource() == up) {
			colValueChanged(1);
			return;
		}
		if (e.getSource() == down) {
			colValueChanged(-1);
			return;
		}
	}
	/**
	 * Comment
	 */
	private void colValueChanged(int num) {
		try {
			double newColVal = Double.parseDouble(ivjTFColWidth.getText());
			newColVal = newColVal + (double) num / DefaultSetting.RODAINLEN;
			DecimalFormat df = new DecimalFormat("0.###");
			if ((num == -1 && newColVal >= 0)
					|| (num == 1 && newColVal <= DefaultSetting.MAXCOLWIDTH
							/ DefaultSetting.RODAINLEN)) {
				ivjTFColWidth.setText(df.format(newColVal));
				ivjTFColWidth.selectAll();
			}
		} catch (java.lang.NumberFormatException ivjExc) {
			showmessage();
		}
	}
	/**
	 * ��øı����п�ֵ�� �������ڣ�(00-11-27 10:49:28)
	 * 
	 * @return int
	 */
	public int getColWidth() {
		return m_ColWidth;
	}
	/**
	 * ���� JBCancel ����ֵ��
	 * 
	 * @return javax.swing.JButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JButton getJBCancel() {
		if (ivjJBCancel == null) {
			try {
				ivjJBCancel = new com.ufsoft.table.beans.UFOButton();
				ivjJBCancel.setName("JBCancel");
				ivjJBCancel.setText(MultiLang.getString("cancel")); //"ȡ ��"
				ivjJBCancel.setBounds(125, 50, 75, 22);
				// user code begin {1}
				ivjJBCancel.addActionListener(this);
				ivjJBCancel.registerKeyboardAction(this, KeyStroke
						.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false),
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
	 * ���� JBOK ����ֵ��
	 * 
	 * @return javax.swing.JButton
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JButton getJBOK() {
		if (ivjJBOK == null) {
			try {
				ivjJBOK = new com.ufsoft.table.beans.UFOButton();
				ivjJBOK.setName("JBOK");
				ivjJBOK.setText(MultiLang.getString("ok")); //"ȷ ��"
				ivjJBOK.setBounds(30, 50, 75, 22);
				// user code begin {1}
				ivjJBOK.addActionListener(this);
				ivjJBOK.registerKeyboardAction(this, KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
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
	 * ���� JLabel2 ����ֵ��
	 * 
	 * @return javax.swing.JLabel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JLabel getJLabel2() {
		if (ivjJLabel2 == null) {
			try {
				ivjJLabel2 = new UFOLabel();
				ivjJLabel2.setName("JLabel2");
				ivjJLabel2.setText(MultiLang.getString("miufo1001144")); //"�п�[����]��"
				ivjJLabel2.setBounds(15, 18, 80, 16);
				ivjJLabel2.setForeground(java.awt.Color.black);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJLabel2;
	}
	
	/**
	 * ���� TFColWidth ����ֵ��
	 * 
	 * @return javax.swing.JTextField
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JTextField getTFColWidth() {
		if (ivjTFColWidth == null) {
			try {
				ivjTFColWidth = new JTextField();
				ivjTFColWidth.setName("TFColWidth");
				ivjTFColWidth
						.setBackground(java.awt.SystemColor.controlHighlight);
				ivjTFColWidth.setBounds(91, 14, 100, 23);
				// user code begin {1}
				ivjTFColWidth.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTFColWidth;
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
				ivjUfoDialogContentPane = new JPanel();
				ivjUfoDialogContentPane.setName("UfoDialogContentPane");						
				ivjUfoDialogContentPane.setLayout(null);
				getUfoDialogContentPane().add(getTFColWidth(),
						getTFColWidth().getName());
				//			getUfoDialogContentPane().add(getJLabel1(),
				// getJLabel1().getName());
				getUfoDialogContentPane().add(getJLabel2(),
						getJLabel2().getName());
				//			getUfoDialogContentPane().add(getTFArea(),
				// getTFArea().getName());
				getUfoDialogContentPane().add(getJBOK(), getJBOK().getName());
				getUfoDialogContentPane().add(getJBCancel(),
						getJBCancel().getName());
				// user code begin {1}
				Rectangle r = ivjTFColWidth.getBounds();
				up.setBounds(r.x + r.width, r.y, 17, 12);
				down.setBounds(r.x + r.width, r.y + 11, 17, 12);
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
			setName("SetColWidthDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setResizable(false);
			setModal(true);
			setSize(230, 110);
			setTitle(MultiLang.getString("miufo1001145")); //"�����п�"
			setContentPane(getUfoDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		setLocationRelativeTo(this);
		ivjJBOK.setNextFocusableComponent(ivjJBCancel);
		ivjJBCancel.setNextFocusableComponent(ivjTFColWidth);
		ivjTFColWidth.setNextFocusableComponent(ivjJBOK);
		ivjTFColWidth.selectAll();
	}
	/**
	 * ������Ϣ�� �������ڣ�(00-11-28 14:58:24)
	 */
	private void showmessage() {
		String[] params = {Integer
				.toString(DefaultSetting.MAXCOLWIDTH / DefaultSetting.RODAINLEN)};
		String s = MultiLang.getString("miufo1001146", params); //"������һ����0��{0}֮�������"
		JOptionPane.showMessageDialog(this, s, MultiLang.getString("ufida_soft_group"),
				JOptionPane.INFORMATION_MESSAGE); //"�����������"
		ivjTFColWidth.requestFocus();
		ivjTFColWidth.selectAll();
	}
}