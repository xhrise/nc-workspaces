package com.ufsoft.report.fmtplugin.formula.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;

import nc.ui.pub.beans.editor.RichEditor;
import nc.vo.pub.beans.editor.DefaultLexAnalyzer;

import com.ufsoft.report.fmtplugin.formula.FormulaWordSorter;
import com.ufsoft.report.fmtplugin.formula.PfColorSet;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;

/**
 * ��ʽ�༭��
 * <p>
 * ֧�ֿ�����ճ�����������ָ�����
 * <p>
 * ���⣺Hr��ʽ�����ַ���Ϣ�Ƕ�����������ʽ������ϣ������HR�ĺ��� ֻ������ʾ��ʱ����һ���޸ģ���ʾͳһ�����ݡ���ˣ�������������⣬������ʾ�����Ѻá�
 * 
 * @author zhaopq
 * @created at 2009-2-19,����03:49:53
 * 
 */
public class FormulaEditor extends RichEditor implements ActionListener {
	private static final long serialVersionUID = -196442053303351103L;

	/* report00429=HR�򵼺��� */
	public static final String HRFORMULA = MultiLang.getString("report00429");

	public static final String HRFORMULASHOW = "HR_TOTAL()";

	private String[] functionNames;

	/** �Ƿ�Hr���� */
	private boolean isHRFunction = false;

	/** HR���� */
	private String HRFunction;

	private boolean changed;

	private JPopupMenu textPopupMenu;

	private JMenuItem copyItem;

	private JMenuItem pasteItem;

	private JMenuItem undoItem;

	private JMenuItem redoItem;

	private UndoManager undoManager;

	/**
	 * �򵼳�ʼʱ��ʽ������
	 */
	private String oldValue = null;

	public FormulaEditor(String[] functionNames) {
		super();
		this.functionNames = functionNames;
		initFmlEditor();
		this.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getUndoManager().canUndo()) {
					getUndoManager().undo();
				}
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getUndoManager().canRedo()) {
					getUndoManager().redo();
				}
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.getDocument().addUndoableEditListener(getUndoManager());
		this.add(getTextPopuMenu());
		this.addMouseListener(createMouseListener());
		this.setLineWrap(true);
		this.setEditable(true);
	}

	private void initFmlEditor() {
		FormulaWordSorter fmlWordSorter = new FormulaWordSorter();
		fmlWordSorter.setKeyWordArray(functionNames);
		setWordSorter(fmlWordSorter);
		((DefaultLexAnalyzer) getLexAnalyzer())
				.setColorSetting(new PfColorSet());
		getDocument().addDocumentListener(createDocListener());
	}

	// ֧�ֿ�����ճ�����������ָ�����
	private MouseListener createMouseListener() {
		return new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger() && SwingUtilities.isRightMouseButton(e)) {
					getTextPopuMenu()
							.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
	}

	private UndoManager getUndoManager() {
		if (undoManager == null) {
			undoManager = new UndoManager();
			undoManager.setLimit(100000);
		}
		return undoManager;
	}

	private JPopupMenu getTextPopuMenu() {
		if (textPopupMenu == null) {
			textPopupMenu = new JPopupMenu();
			textPopupMenu.add(getCopyMenuItem());
			textPopupMenu.add(getPasteMenuItem());
			textPopupMenu.add(getUndoMenuItem());
			textPopupMenu.add(getRedoMenuItem());
		}
		return textPopupMenu;
	}

	/**
	 * miufo00025=���� Ctrl+C
	 */
	private JMenuItem getCopyMenuItem() {
		if (copyItem == null) {
			copyItem = new JMenuItem(MultiLang.getString("miufo00025"));
			copyItem.setIcon(ResConst.getImageIcon("reportcore/copy.gif"));
			copyItem.addActionListener(this);
		}
		return copyItem;
	}

	/**
	 * miufo00026=ճ�� Ctrl+V
	 */
	private JMenuItem getPasteMenuItem() {
		if (pasteItem == null) {
			pasteItem = new JMenuItem(MultiLang.getString("miufo00026"));
			pasteItem.setIcon(ResConst.getImageIcon("reportcore/paste.gif"));
			pasteItem.addActionListener(this);
		}
		return pasteItem;
	}

	/**
	 * miufo00027=���� Ctrl+Z
	 */
	private JMenuItem getUndoMenuItem() {
		if (undoItem == null) {
			undoItem = new JMenuItem(MultiLang.getString("miufo00027"));
			undoItem.setIcon(ResConst.getImageIcon("reportcore/previous.gif"));
			undoItem.addActionListener(this);
		}
		return undoItem;
	}

	/**
	 * miufo00028=�ָ� Ctrl+Y
	 */
	private JMenuItem getRedoMenuItem() {
		if (redoItem == null) {
			redoItem = new JMenuItem(MultiLang.getString("miufo00028"));
			redoItem.setIcon(ResConst.getImageIcon("reportcore/next.gif"));
			redoItem.addActionListener(this);
		}
		return redoItem;
	}

	/**
	 * ����ʽ�༭�Ի���ť����
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getCopyMenuItem()) {
			this.copy();
		} else if (e.getSource() == getPasteMenuItem()) {
			this.paste();
		} else if (e.getSource() == getUndoMenuItem()) {
			if (getUndoManager().canUndo()) {
				getUndoManager().undo();
			}
		} else if (e.getSource() == getRedoMenuItem()
				&& getUndoManager().canRedo()) {
			getUndoManager().redo();
		}
	}

	public void setText(String text) {
		isHRFunction = text != null && text.startsWith(HRFORMULA);
		setEditable(!isHRFunction);// HR����������༭��
		if (isHRFunction) {
			HRFunction = text;
			text = HRFORMULASHOW;
		}
		super.setText(text);
	}

	public String getText() {
		if (isHRFunction) {
			return HRFunction;
		}
		return super.getText();
	}

	private DocumentListener createDocListener() {
		return new DocumentListener() {

			public void changedUpdate(DocumentEvent e) {
			}

			public void insertUpdate(DocumentEvent e) {
				String strNewValue = getText();
				String oldValue = getOldValue() == null ? "" : getOldValue();
				if (oldValue != null && strNewValue != null
						&& !oldValue.equalsIgnoreCase(strNewValue))
					setChanged(true);

			}

			public void removeUpdate(DocumentEvent e) {
				setChanged(true);
			}

		};
	}

	public void clear(){
		setText("");
	}
	
	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String strOldValue) {
		this.oldValue = strOldValue;
	}

	/** �༭�������Ƿ�ı� */
	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

}
