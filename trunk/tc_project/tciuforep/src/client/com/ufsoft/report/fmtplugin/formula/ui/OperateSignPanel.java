package com.ufsoft.report.fmtplugin.formula.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIScrollPane;
import nc.util.iufo.pub.UFOString;

import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.fmtplugin.formula.CommonFmlEditDlg;
/**
 * ���������
 * @author zhaopq
 * @created at 2009-2-20,����07:09:54
 * 
 */
public class OperateSignPanel extends FunctionPanel {
	private static final long serialVersionUID = 3737723842772939729L;

	/** ��ʾ��������Pane */
	private JScrollPane operateSignPane;

	/** ��ʾ��������JList */
	private JList operateSignJList;

	/** ���������� */
	private static final String[] operateSignAry = { "+", "-", "*", "?", "%", "/",
			"=", ">", ">=", "<", "<=", "<>", " AND ", " OR ", " LIKE " };


	public OperateSignPanel(CommonFmlEditDlg fmlEditDlg) {
		super(fmlEditDlg);
	}

	@Override
	protected void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getOperateSignPane(), BorderLayout.WEST);
	}

	public JScrollPane getOperateSignPane() {
		if (operateSignPane == null) {
			operateSignPane = new UIScrollPane();
			operateSignPane.setViewportView(getOperateSignList());
			operateSignPane.setPreferredSize(new Dimension(193, 180));
		}
		return operateSignPane;
	}

	/**�õ���������б�*/
	private JList getOperateSignList() {
		if (operateSignJList == null) {
			operateSignJList = new UIList();
			operateSignJList
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			operateSignJList.setListData(operateSignAry);
			operateSignJList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Object selectItem = OperateSignPanel.this
							.getOperateSignList().getSelectedValue();
					if (selectItem == null)
						return;
					if (e.getClickCount() >= 2) {
						addOperaSign(selectItem.toString(), fmlEditDlg
								.getCurrentFmlEditArea().getFormulaEditor());
						return;
					}

				}

			});
			operateSignJList.setSelectedIndex(0);
		}
		return operateSignJList;
	}

	/** ��Ӳ�������*/
	private void addOperaSign(String strSign, JTextArea areaDest) {
		if (!UFOString.isEmpty(strSign)) {
			int insertPos = areaDest.getCaretPosition();
			int iRealtiveCaretPos = strSign.length();
			try {
				areaDest.insert(strSign, insertPos);
			} catch (Exception e1) {
				areaDest.append(strSign);
			}
			areaDest.setCaretPosition(insertPos + iRealtiveCaretPos);
		}
	}
	@Override
	public UfoSimpleObject getSelectedFuncCategory() {
		return null;
	}

}
