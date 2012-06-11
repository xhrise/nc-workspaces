package com.ufsoft.report.fmtplugin.formula;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.TextAction;

import nc.ui.pub.beans.UIButton;
import nc.util.iufo.pub.UFOString;

import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.script.base.AbsFmlExecutor;
import com.ufsoft.table.CellsPane;

/*
 * 公式文本框输入后，要验证是否合法；点击按钮弹出对话框需要返回一个值。
 * Creation date: (2008-07-08 16:40:08)
 * @author: chxw
 */
public class FmlRefTextField extends JTextField {
	private static final long serialVersionUID = 1L;

	static Window getWindowForComponent(Component parentComponent)
			throws HeadlessException {
		if (parentComponent == null)
			return JOptionPane.getRootFrame();
		if (parentComponent instanceof Frame
				|| parentComponent instanceof Dialog)
			return (Window) parentComponent;
		return getWindowForComponent(parentComponent.getParent());
	}

	private JButton button = new UIButton("...");

	private MouseInputAdapter mia = new MouseInputAdapter() {
		public void mouseEntered(MouseEvent e) {
			if (e.getComponent() == button) {
				button
						.setCursor(Cursor
								.getPredefinedCursor(Cursor.HAND_CURSOR));
			} else {
				button.setCursor(Cursor.getDefaultCursor());
			}
		}

	};

	private CellsPane parent = null;

	private AbsFmlExecutor m_fmlExecutor = null;

	public FmlRefTextField(CellsPane pwner, AbsFmlExecutor fmlExecutor) {
		super();
		setLayout(new BorderLayout());
		button.setPreferredSize(new Dimension(22, 20));
		button.addMouseListener(mia);
		button.addMouseMotionListener(mia);
		add(button, BorderLayout.EAST);
		parent = pwner;
		m_fmlExecutor = fmlExecutor;
		initRefComp();
	}

	public void initRefComp() {
		ActionListener[] listeners = button.getActionListeners();
		for (int i = 0; i < listeners.length; i++) {
			button.removeActionListener(listeners[i]);
		}
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showDialog();
			}
		});
		getKeymap().addActionForKeyStroke(
				KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK),
				new TextAction("Reference") {
					private static final long serialVersionUID = 1L;

					public void actionPerformed(ActionEvent e) {
						showDialog();
					}
				});
	}

	/**
	 * 弹出公式编辑诓，并处理增加的公式信息
	 */
	private void showDialog() {
		parent.getContext().setAttribute(ReportContextKey.AREA_FORMULA_DIALOG_REF, true);
		ExpressionEditDlg fmlEditDlg = getExprEditDlg();
		fmlEditDlg.getCurrentFmlEditArea().getFormulaEditor().setText(getText());
		fmlEditDlg.show();

		if (fmlEditDlg.getResult() == UfoDialog.ID_OK) {
			String cellFormula = fmlEditDlg.getCurrentFmlEditArea().getFormulaEditor()
					.getText();
			if (!UFOString.isEmpty(cellFormula)) {
				setText(cellFormula);
				grabFocus();
			}
		}
		parent.getContext().setAttribute(ReportContextKey.AREA_FORMULA_DIALOG_REF, false);
	}

	private ExpressionEditDlg getExprEditDlg() {
		
		ExpressionEditDlg dialog = new ExpressionEditDlg(parent, m_fmlExecutor);
		Point p = button.getLocation();
		SwingUtilities.convertPointToScreen(p, button.getParent());
		dialog.setLocation(p);
		Dimension screenDim = getToolkit().getScreenSize();
		int dx = p.x + dialog.getSize().width - screenDim.width;
		if (dx > 0) {
			p.translate(-dx, 0);
		}
		int dy = p.y + dialog.getSize().height - screenDim.height;
		if (dy > 0) {
			p.translate(0, -dy);
		}
		dialog.setLocation(p);
		return dialog;
	}

	public JButton getButton() {
		return button;
	}

}
