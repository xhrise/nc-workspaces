package com.ufsoft.report.sysplugin.help;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import nc.ui.pub.beans.UIPanel;

import com.ufsoft.report.constant.Environment;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;


public class AboutDialog extends UfoDialog implements ActionListener {
 
	private static final long serialVersionUID = -4500275434428761388L;
	private JButton ivjJBOK = null;
	private JButton ivjJBSYS = null;
	private JLabel ivjJLabelImg = null;
	private JPanel ivjUfoDialogContentPane = null; 
	private JTextField ivjJTextField1 = null;
	private JTextArea ivjJTextArea1 = null;
	private JTextArea ivjJTextArea2 = null;
	
	/**
	 * @param owner��������
	 */
	public AboutDialog(Container owner) {
		super(owner);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ivjJBOK) {
			setResult(ID_OK);
			close();
		}
	}
	/**
	 * ��Ӵ�ҳ������İ�����
	 * @return String
	 */
	protected String getHelpID() {
	    return "TM_Help_About";
	}
	/**
	 * ���� JBOK ����ֵ��
	 * 
	 * @return JButton
	 */
	/* ���棺�˷������������ɡ� */
	private JButton getJBOK() {
		if (ivjJBOK == null) {
			try {
				ivjJBOK = new nc.ui.pub.beans.UIButton();
				ivjJBOK.setName("JBOK");
				ivjJBOK.setText(MultiLang.getString("miufo1000775")); //"ȷ ��"
				ivjJBOK.setBounds(345, 171, 75, 22);
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
	 * ���� JBSYS ����ֵ��
	 * 
	 * @return JButton
	 */
	/* ���棺�˷������������ɡ� */
	private JButton getJBSYS() {
		if (ivjJBSYS == null) {
			try {
				ivjJBSYS = new nc.ui.pub.beans.UIButton();
				ivjJBSYS.setName("JBSYS");
				ivjJBSYS.setText(MultiLang.getString("miufo1000776")); //"ϵͳ��Ϣ"
				ivjJBSYS.setBounds(286, 192, 75, 22);
				ivjJBSYS.setVisible(false);
				// user code begin {1}
				ivjJBSYS.addActionListener(this);
				ivjJBSYS.registerKeyboardAction(this, KeyStroke.getKeyStroke(
						KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJBSYS;
	}
	/**
	 * ���� JLabelImg ����ֵ��
	 * 
	 * @return JLabel
	 */
	/* ���棺�˷������������ɡ� */
	private JLabel getJLabelImg() {
		if (ivjJLabelImg == null) {
			try {
				ivjJLabelImg = new nc.ui.pub.beans.UILabel();
				ivjJLabelImg.setName("JLabelImg");
				ivjJLabelImg.setBounds(13, 12, 45, 46);
				ivjJLabelImg.setRequestFocusEnabled(false);
				ivjJLabelImg.setIcon(ResConst.getImageIcon("reportcore/ufob.gif"));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjJLabelImg;
	}
	/**
	 * ���� JTextArea1 ����ֵ��
	 * 
	 * @return JTextArea
	 */
	/* ���棺�˷������������ɡ� */
	private JTextArea getJTextArea1() {
		if (ivjJTextArea1 == null) {
			try {
				ivjJTextArea1 = new JTextArea();
				ivjJTextArea1.setName("JTextArea1");
				ivjJTextArea1.setBounds(91, 24, 295, 72);
				ivjJTextArea1.setEditable(false);
				ivjJTextArea1.setBorder(null);
				ivjJTextArea1.setRequestFocusEnabled(false);
				// user code begin {1}
				if (Environment.isDemo())
					ivjJTextArea1.setText(MultiLang.getString("miufo1001323")
							+ MultiLang.getString("miufo1000777")); //"[��ʾ��]\n\n��Ȩ���У�C��\n��������ɷ����޹�˾"
				else {
					ivjJTextArea1.setText(MultiLang.getString("miufo1001323")
							+ MultiLang.getString("miufo1000778")); //"\n\n��Ȩ���У�C��\n��������ɷ����޹�˾"
				}
//				ivjJTextArea1.setText("");
				ivjJTextArea1.setLineWrap(true);
				ivjJTextArea1.setBackground(ivjUfoDialogContentPane
						.getBackground());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTextArea1;
	}
	/**
	 * ���� JTextArea11 ����ֵ��
	 * 
	 * @return JTextArea
	 */
	/* ���棺�˷������������ɡ� */
	private JTextArea getJTextArea2() {
		if (ivjJTextArea2 == null) {
			try {
				ivjJTextArea2 = new JTextArea();
				ivjJTextArea2.setName("JTextArea2");
				ivjJTextArea2.setBounds(19, 132, 295, 94);
				ivjJTextArea2.setEditable(false);
				ivjJTextArea2.setBorder(null);
				ivjJTextArea2.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjJTextArea2.setText(MultiLang.getString("miufo1000779")); //"���棺������ܵ���Ȩ���͹��ʹ�Լ�ı�������δ���Ϸ���Ȩ�����Ը��ƴ������ȫ���򲿷֣����е������ķ������Ρ�"
				ivjJTextArea2.setLineWrap(true);
				ivjJTextArea2.setBackground(ivjUfoDialogContentPane
						.getBackground());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTextArea2;
	}
	/**
	 * ���� JTextField1 ����ֵ��
	 * 
	 * @return JTextField
	 */
	/* ���棺�˷������������ɡ� */
	private JTextField getJTextField1() {
		if (ivjJTextField1 == null) {
			try {
				ivjJTextField1 = new JTextField();
				ivjJTextField1.setName("JTextField1");
				ivjJTextField1.setBounds(9, 102, 407, 1);
				
				ivjJTextField1.setRequestFocusEnabled(false);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJTextField1;
	}
	/**
	 * ���� UfoDialogContentPane ����ֵ��
	 * 
	 * @return JPanel
	 */
	/* ���棺�˷������������ɡ� */
	private JPanel getUfoDialogContentPane() {
		if (ivjUfoDialogContentPane == null) {
			try {
				ivjUfoDialogContentPane = new UIPanel();
				ivjUfoDialogContentPane.setName("UfoDialogContentPane");
				ivjUfoDialogContentPane.setLayout(null);
				ivjUfoDialogContentPane.setSize(new Dimension(454, 239));
				getUfoDialogContentPane().add(getJLabelImg(),
						getJLabelImg().getName());
				getUfoDialogContentPane().add(getJBSYS(), getJBSYS().getName());
				getUfoDialogContentPane().add(getJTextField1(),
						getJTextField1().getName());
				getUfoDialogContentPane().add(getJTextArea1(),
						getJTextArea1().getName());
				getUfoDialogContentPane().add(getJTextArea2(),
						getJTextArea2().getName());
				getUfoDialogContentPane().add(getJBOK(),
						getJBOK().getName());
				
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
			setName("AboutUfoDlg");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setResizable(false);
			setModal(true);
			setSize(451, 283);
			setContentPane(getUfoDialogContentPane());
			setLocationRelativeTo(getParent());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		if (Environment.isDemo())
			setTitle(MultiLang.getString("miufo1000780")); //"����iUFO
																		// ������[��ʾ]"
		else
			setTitle(MultiLang.getString("miufo1000781")); //"����iUFO
																		// ������"
		
//		ivjJBOK.setNextFocusableComponent(ivjJBOK);
		
		// user code end
	}	
}  //  @jve:decl-index=0:visual-constraint="176,43"