package com.ufsoft.table.exarea;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import nc.ui.pub.beans.UIButton;

import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.report.util.MultiLang;

public class ExAreaCombineDialog extends UfoDialog {
	private JList listCell;
	private JTextField fldArea;
	private static final long serialVersionUID = 1L;
	ArrayList<ExAreaCell> m_cells = null;
	AreaPosition area = null;
	Container container = null;
	
	DefaultListModel model = null;

	/**
	 * @i18n miufo00126=合并可扩展区
	 * @i18n miufo00127=合并后的区域：
	 * @i18n miufo00128=请选择要合并到的可扩展区：
	 * @i18n ok=确定
	 * @i18n cancel=取消
	 */
	ExAreaCombineDialog(Container container, AreaPosition area,
			ArrayList<ExAreaCell> list) {
		super(container);
		this.container = container;
		this.area = area;
		this.m_cells = list;
		
		setTitle(MultiLang.getString("miufo00126"));
		setResizable(false);
		setSize(337, 340);
		
		getContentPane().setLayout(null);

		final JLabel label = new JLabel();
		label.setText(MultiLang.getString("miufo00127"));
		label.setBounds(21, 10, 91, 18);
		getContentPane().add(label);

		fldArea = new JTextField();
		fldArea.setBounds(118, 8, 160, 22);
		getContentPane().add(fldArea);

		final JLabel label_1 = new JLabel();
		label_1.setText(MultiLang.getString("miufo00128"));
		label_1.setBounds(21, 45, 257, 18);
		getContentPane().add(label_1);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 69, 257, 166);
		getContentPane().add(scrollPane);

		listCell = new JList();
		scrollPane.setViewportView(listCell);

		final JButton button = new UIButton();
		button.addActionListener(new ActionListener() {
			/**
			 * @i18n miufo00129=请选择可扩展区。
			 */
			public void actionPerformed(final ActionEvent e) {
				ExAreaCell combineCell = getSelected();
				if(combineCell == null){
					UfoPublic.showErrorDialog(ExAreaCombineDialog.this.container, MultiLang.getString("miufo00129"), null);
					return;
				}
				setResult(UfoDialog.ID_OK);
				closeDlg();
			}
		});
		button.setText(MultiLang.getString("ok"));
		button.setBounds(35, 254, 106, 28);
		getContentPane().add(button);

		final JButton button_1 = new UIButton();
		button_1.setText(MultiLang.getString("cancel"));
		button_1.setBounds(159, 254, 106, 28);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setResult(UfoDialog.ID_CANCEL);
				closeDlg();
			}
		});
		getContentPane().add(button_1);
 
		initModel();
	}
	
	private void initModel(){
		model = new DefaultListModel();
		for(ExAreaCell cell: m_cells){
			model.addElement(cell);
		}
		listCell.setModel(model);
		
	}
	
	ExAreaCell getSelected(){
		return (ExAreaCell) listCell.getSelectedValue();
	}
	
	protected void closeDlg() {
		EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
		WindowEvent we = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		eq.postEvent(we);
	}

}
 