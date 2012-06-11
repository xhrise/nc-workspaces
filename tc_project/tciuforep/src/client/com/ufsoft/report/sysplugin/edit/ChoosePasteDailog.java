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
 * </pre>  选择性粘贴对话框
 * @author 王宇光
 * @version 
 * Create on 2008-3-23
 */
public class ChoosePasteDailog extends UfoDialog implements ActionListener {

	/** 粘贴：全部 */
	private JRadioButton allRadio = null;
	/** 粘贴：数值 */
	private JRadioButton valueRadio = null;
	/** 粘贴：格式 */
	private JRadioButton formatRadio = null;
	/** 粘贴：批注 */
	private JRadioButton postilRadio = null;
	/** 粘贴：边框除外 */
	private JRadioButton noBorderRadio = null;
	/** 粘贴：列宽 */
	private JRadioButton columnWidthRadio = null;
	/** 粘贴：值和数字格式 */
	private JRadioButton valueNumFormatRadio = null;
	/** 确定按钮 */
	private JButton btOk = null;
	/** 取消按钮 */
	private JButton btCancel = null;
	/** 总面板 */
	private JPanel contentPanel = null;
	/** 粘贴面板 */
	JPanel pastePanel = null;
	/** 转置面板 */
	private JPanel transferPanel = null;
	/** 最下边面板 */
	private JPanel bottomPanel = null;
	/** 转置的checkBox */
	private JCheckBox transferCheckBox = null;
	/** 保存选中的值 */
	private String strSelectValue = null;
	/** 返回的对象 */
	private AbsChoosePaste choosePaste = null;
	/** 表格组件 */
	private UfoReport m_rep;

	/**
	 * @i18n miufo00120=选择性粘贴关联表格组件(UfoReport)失败
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
	 * 列宽radio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getColumnWidthRadio() {
		if (columnWidthRadio == null) {
			try {
				columnWidthRadio = new UIRadioButton();
				columnWidthRadio.setName("columnWidthRadio");
				columnWidthRadio.setText(MultiLang.getString("miufo1001345"));// 列宽
				columnWidthRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return columnWidthRadio;
	}

	/**
	 * 边框除外radio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getNoBorderRadio() {
		if (noBorderRadio == null) {
			try {
				noBorderRadio = new UIRadioButton();
				noBorderRadio.setName("noBorderRadio");
				noBorderRadio.setText(MultiLang.getString("miufo1004057"));// 边框除外
				noBorderRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return noBorderRadio;
	}

	/**
	 * 值和数字公式radio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getValueNumFormatRadio() {
		if (valueNumFormatRadio == null) {
			try {
				valueNumFormatRadio = new UIRadioButton();
				valueNumFormatRadio.setName("valueNumFormatRadio");
				valueNumFormatRadio.setText(MultiLang.getString("miufo1004050"));// 值和数字格式
				valueNumFormatRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return valueNumFormatRadio;
	}

	/**
	 * 格式radio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getFormatRadio() {
		if (formatRadio == null) {
			try {
				formatRadio = new UIRadioButton();
				formatRadio.setName("formatRadio");
				formatRadio.setText(MultiLang.getString("miufo1000877"));// 格式
				formatRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return formatRadio;
	}

	/**
	 * 批注radio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getPostilRadio() {
		if (postilRadio == null) {
			try {
				postilRadio = new UIRadioButton();
				postilRadio.setName("postilRadio");
				postilRadio.setText(MultiLang.getString("miufo1004051"));// 批注
				postilRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return postilRadio;
	}

	/**
	 * 内容radio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getValueRadio() {
		if (valueRadio == null) {
			try {
				valueRadio = new UIRadioButton();
				valueRadio.setName("valueRadio");
				valueRadio.setText(MultiLang.getString("miufo1000275"));// 内容
				valueRadio.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return valueRadio;
	}

	/**
	 * 全部radio
	 * @param
	 * @return JRadioButton
	 */
	private JRadioButton getAllRadio() {
		if (allRadio == null) {
			try {
				allRadio = new UIRadioButton();
				allRadio.setName("allRadio");
				allRadio.setText(MultiLang.getString("miufopublic329"));// 全部
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
	 * 确定按钮
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
	 * 转置的panel
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
	 * 转置的checkBox
	 * @param
	 * @return JCheckBox
	 */
	private JCheckBox getTransferBox() {
		if (transferCheckBox == null) {
			transferCheckBox = new UICheckBox();
			transferCheckBox.setName("transferPanel");
			transferCheckBox.setText(MultiLang.getString("miufo1004048"));// 转置
			transferCheckBox.addActionListener(this);
		}
		return transferCheckBox;
	}

	/**
	 * 取消按钮
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
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		System.out.println(MultiLang.getString("uiuforep0000805"));// ---------
		// 未捕捉到的异常
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
			if (btSource == btOk) { // 确定按钮的处理
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
 