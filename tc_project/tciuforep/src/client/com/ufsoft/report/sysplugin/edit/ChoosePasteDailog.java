package com.ufsoft.report.sysplugin.edit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

/**
 * <pre>
 * </pre>  ѡ����ճ���Ի���
 * @author �����
 * @version 
 * Create on 2008-3-23
 */
public class ChoosePasteDailog extends UfoDialog implements ActionListener {

	/** ճ����ȫ�� */
	private JRadioButton allRadio = null;
	/** ճ������ֵ */
	private JRadioButton valueRadio = null;
	/** ճ������ʽ */
	private JRadioButton formatRadio = null;
	/** ճ������ע */
	private JRadioButton postilRadio = null;
	/** ճ�����߿���� */
	private JRadioButton noBorderRadio = null;
	/** ճ�����п� */
	private JRadioButton columnWidthRadio = null;
	/** ճ����ֵ�����ָ�ʽ */
	private JRadioButton valueNumFormatRadio = null;
	/** ȷ����ť */
	private JButton btOk = null;
	/** ȡ����ť */
	private JButton btCancel = null;
	/** ����� */
	private JPanel contentPanel = null;
	/** ճ����� */
	JPanel pastePanel = null;
	/** ת����� */
	private JPanel transferPanel = null;
	/** ���±���� */
	private JPanel bottomPanel = null;
	/** ת�õ�checkBox */
	private JCheckBox transferCheckBox = null;
	/** ����ѡ�е�ֵ */
	private String strSelectValue = null;
	/** ���صĶ��� */
	private AbsChoosePaste choosePaste = null;
	/** ������ */
	private UfoReport m_rep;

	/**
	 * @i18n miufo00120=ѡ����ճ������������(UfoReport)ʧ��
	 */
	public ChoosePasteDailog(Container owner) {
		super(owner);
		if (owner == null) {
			throw new IllegalArgumentException(MultiLang.getString("miufo00120"));
		}
		m_rep = (UfoReport) owner;
		initialize();
	}

	private void initialize() {
		setResizable(false);
		setName("ChoosePasteDialog");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(260, 266);
		setTitle(MultiLang.getString("miufo1004049"));
		setContentPane(getContentPanel());
		setLocationRelativeTo(this);

	}

	private JPanel createPastePanel() {
		if (pastePanel == null) {
			pastePanel = new UIPanel();
			pastePanel.setName("pastePanel");
			pastePanel.setLayout(null);
			GridLayout a = new GridLayout(5, 2);
			pastePanel.setLayout(a);
			createPastePanel().add(getAllRadio());
			createPastePanel().add(getNoBorderRadio());
			createPastePanel().add(getColumnWidthRadio());
			createPastePanel().add(getValueRadio());
			createPastePanel().add(getFormatRadio());
			createPastePanel().add(getValueNumFormatRadio());
			createPastePanel().add(getPostilRadio());
			pastePanel.setBorder(BorderFactory.createTitledBorder(MultiLang.getString("miufo1000655")));
			pastePanel.setSize(200, 150);

			ButtonGroup group = new ButtonGroup();
			group.add(getAllRadio());
			group.add(getNoBorderRadio());
			group.add(getColumnWidthRadio());
			group.add(getValueRadio());
			group.add(getFormatRadio());
			group.add(getValueNumFormatRadio());
			group.add(getPostilRadio());
		}
		return pastePanel;
	}

	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new UIPanel();
			contentPanel.setName("contentPanel");
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(createPastePanel(), BorderLayout.NORTH);
			contentPanel.add(getTransferPanel(), BorderLayout.CENTER);
			contentPanel.add(getBottomPanel(), BorderLayout.SOUTH);
		}
		return contentPanel;
	}

	/**
	 * �п�radio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getColumnWidthRadio() {
		if (columnWidthRadio == null) {
			try {
				columnWidthRadio = new UIRadioButton();
				columnWidthRadio.setName("columnWidthRadio");
				columnWidthRadio.setText(MultiLang.getString("miufo1001345"));// �п�
				columnWidthRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return columnWidthRadio;
	}

	/**
	 * �߿����radio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getNoBorderRadio() {
		if (noBorderRadio == null) {
			try {
				noBorderRadio = new UIRadioButton();
				noBorderRadio.setName("noBorderRadio");
				noBorderRadio.setText(MultiLang.getString("miufo1004057"));// �߿����
				noBorderRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return noBorderRadio;
	}

	/**
	 * ֵ�����ֹ�ʽradio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getValueNumFormatRadio() {
		if (valueNumFormatRadio == null) {
			try {
				valueNumFormatRadio = new UIRadioButton();
				valueNumFormatRadio.setName("valueNumFormatRadio");
				valueNumFormatRadio.setText(MultiLang.getString("miufo1004050"));// ֵ�����ָ�ʽ
				valueNumFormatRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return valueNumFormatRadio;
	}

	/**
	 * ��ʽradio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getFormatRadio() {
		if (formatRadio == null) {
			try {
				formatRadio = new UIRadioButton();
				formatRadio.setName("formatRadio");
				formatRadio.setText(MultiLang.getString("miufo1000877"));// ��ʽ
				formatRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return formatRadio;
	}

	/**
	 * ��עradio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getPostilRadio() {
		if (postilRadio == null) {
			try {
				postilRadio = new UIRadioButton();
				postilRadio.setName("postilRadio");
				postilRadio.setText(MultiLang.getString("miufo1004051"));// ��ע
				postilRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return postilRadio;
	}

	/**
	 * ����radio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getValueRadio() {
		if (valueRadio == null) {
			try {
				valueRadio = new UIRadioButton();
				valueRadio.setName("valueRadio");
				valueRadio.setText(MultiLang.getString("miufo1000275"));// ����
				valueRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return valueRadio;
	}

	/**
	 * ȫ��radio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getAllRadio() {
		if (allRadio == null) {
			try {
				allRadio = new UIRadioButton();
				allRadio.setName("allRadio");
				allRadio.setText(MultiLang.getString("miufopublic329"));// ȫ��
				allRadio.setSelected(true);
				allRadio.addActionListener(this);
				setChoosePaste(new EditPasteAll(m_rep, false));
				setSelectValue(EditPasteExt.ALL);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return allRadio;
	}

	/**
	 * ȷ����ť
	 * @param
	 * @return JButton
	 */
	private JButton getOkButton() {
		if (btOk == null) {
			try {
				btOk = new UIButton();
				btOk.setName("btOk");
				btOk.setText(MultiLang.getString("uiuforep0000782"));
				btOk.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return btOk;
	}

	private JPanel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new UIPanel();
			bottomPanel.add(getOkButton());
			bottomPanel.add(getCancelButton());
		}
		return bottomPanel;
	}

	/**
	 * ת�õ�panel
	 * @param
	 * @return JButton
	 */
	private JPanel getTransferPanel() {
		if (transferPanel == null) {
			transferPanel = new UIPanel();
			transferPanel.setLayout(new BorderLayout());
			transferPanel.setSize(200, 80);
			getTransferPanel().add(getTransferBox(), BorderLayout.WEST);
			transferPanel.setBorder(BorderFactory.createTitledBorder(MultiLang.getString("miufo1004048")));
		}
		return transferPanel;
	}

	/**
	 * ת�õ�checkBox
	 * @param
	 * @return JCheckBox
	 */
	private JCheckBox getTransferBox() {
		if (transferCheckBox == null) {
			transferCheckBox = new UICheckBox();
			transferCheckBox.setName("transferPanel");
			transferCheckBox.setText(MultiLang.getString("miufo1004048"));// ת��
			transferCheckBox.addActionListener(this);
		}
		return transferCheckBox;
	}

	/**
	 * ȡ����ť
	 * @param
	 * @return JButton
	 */
	private JButton getCancelButton() {
		if (btCancel == null) {
			try {
				btCancel = new UIButton();
				btCancel.setName("btCancel");
				btCancel.setText(MultiLang.getString("uiuforep0000739"));
				btCancel.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return btCancel;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		System.out.println(MultiLang.getString("uiuforep0000805"));// ---------
		// δ��׽�����쳣
		// ---------
		exception.printStackTrace(System.out);
	}

	public String getSelectValue() {
		return strSelectValue;
	}

	public void setSelectValue(String strSelectValue) {
		this.strSelectValue = strSelectValue;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source instanceof JButton) {
			JButton btSource = (JButton) source;
			if (btSource == btOk) { // ȷ����ť�Ĵ���
				setResult(UfoDialog.ID_OK);
				boolean b_isSelected = transferCheckBox.isSelected();
				if (allRadio.isSelected()) {
					setChoosePaste(new EditPasteAll(m_rep, b_isSelected));
				} else if (valueRadio.isSelected()) {
					setChoosePaste(new EditPasteContent(m_rep, b_isSelected));
				} else if (formatRadio.isSelected()) {
					setChoosePaste(new EditPasteFormat(m_rep, b_isSelected));
				} else if (postilRadio.isSelected()) {
					setChoosePaste(new EditPastePostil(m_rep, b_isSelected));
				} else if (noBorderRadio.isSelected()) {
					setChoosePaste(new EditPasteNoBorder(m_rep, b_isSelected));
				} else if (columnWidthRadio.isSelected()) {
					setChoosePaste(new EditPasteColumnWidth(m_rep, b_isSelected));
				} else if (valueNumFormatRadio.isSelected()) {
					setChoosePaste(new EditPasteValueNumFormat(m_rep, b_isSelected));
				}
				this.close();
				return;
			} else if (btSource == btCancel) {
				setResult(UfoDialog.ID_CANCEL);
				this.close();
				return;
			}
		}
	}

	public AbsChoosePaste getChoosePaste() {
		return choosePaste;
	}

	public void setChoosePaste(AbsChoosePaste choosePaste) {
		this.choosePaste = choosePaste;
	}
}
 