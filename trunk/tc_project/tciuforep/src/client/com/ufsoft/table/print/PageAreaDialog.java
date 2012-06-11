package com.ufsoft.table.print;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.FloatDocument;
import com.ufsoft.report.util.MultiLang;

/**
 * <p>
 * Title: ��ӡ�������öԻ���
 * </p>
 * <p>
 * Description: ���ô�ӡ��������ӡ˳�򣬴�ӡ������ʼ����λ��
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class PageAreaDialog extends UfoDialog {
	private JPanel mainPanle = new UIPanel();
	private JPanel panel1 = new UIPanel();
	private JPanel pnHeaderArea = new UIPanel();
	private JPanel pnPrintOrder1 = new UIPanel();
	private JPanel pnPrintScale = new UIPanel();
	private JPanel pnPrintArea = new UIPanel();
	private String strTo = MultiLang.getString("To"), strFrom = MultiLang
			.getString("From");
	private JRadioButton rbRowToCol = new UIRadioButton(MultiLang
			.getString("RowThenColumn"));
	private JRadioButton rbColToRow = new UIRadioButton(MultiLang
			.getString("ColumnThenRow"));
	//  private JRadioButton rbHeaderCol = new UIRadioButton("��");
	//  private JRadioButton rbHeaderRow = new UIRadioButton("��");
	private JLabel lbColHead = new nc.ui.pub.beans.UILabel(MultiLang.getString("RowHeadArea"));
	private JLabel lbRowHead = new nc.ui.pub.beans.UILabel(MultiLang.getString("ColumnHeadArea"));

	private JLabel lbCol = new nc.ui.pub.beans.UILabel(MultiLang.getString("Column"));
	private JLabel lbRow = new nc.ui.pub.beans.UILabel(MultiLang.getString("Row"));
	private JLabel lbTo2 = new nc.ui.pub.beans.UILabel(strTo);
	private JLabel lbTo1 = new nc.ui.pub.beans.UILabel(strTo);
	private JLabel lbTo3 = new nc.ui.pub.beans.UILabel(strTo);
	private JLabel lbTo4 = new nc.ui.pub.beans.UILabel(strTo);
	private JLabel lbFrom1 = new nc.ui.pub.beans.UILabel(strFrom);
	private JLabel lbFrom2 = new nc.ui.pub.beans.UILabel(strFrom);
	private JLabel lbFrom4 = new nc.ui.pub.beans.UILabel(strFrom);
	private JLabel lbFrom3 = new nc.ui.pub.beans.UILabel(strFrom);
	private JLabel lbScale = new nc.ui.pub.beans.UILabel(MultiLang.getString("PrintScale"));
	private ImageIcon iconCol2Row = ResConst.getImageIcon("printorder1.gif");//���к���

	private ImageIcon iconRow2Col = ResConst.getImageIcon("printorder2.gif");//���к���

	private JLabel lbImage = new nc.ui.pub.beans.UILabel(iconCol2Row);
	//  private JCheckBox cbTail = new UICheckBox("β");
	//  private JCheckBox cbHeader = new UICheckBox("ͷ");

	private JTextField tfPrintScale = new UITextField();
	private JTextField tfAreaRow1 = new UITextField();
	private JTextField tfAreaRow2 = new UITextField();
	private JTextField tfAreaCol1 = new UITextField();
	private JTextField tfAreaCol2 = new UITextField();
	private JTextField tfRowHeader1 = new UITextField();
	private JTextField tfColHeader1 = new UITextField();
	private JTextField tfRowHeader2 = new UITextField();
	private JTextField tfColHeader2 = new UITextField();
	private JButton btOk = new nc.ui.pub.beans.UIButton(MultiLang.getString("ok"));
	private JButton btCancel = new nc.ui.pub.beans.UIButton(MultiLang.getString("cancel"));
	//�����ǹ������ֵ�һЩ����
	private Insets PANEL_INSETS = new Insets(10, 10, 10, 10);
	private int NUMBERWIDTH = 25;//�ڲ��ֹ����п�������¼���textField�Ŀ��
	//����ҵ�������
	private float MAX_SCALE = 3.0f;//��������Ŵ����
	private float MIN_SCALE = 0.3f;//�������С�Ŵ����
	private boolean ok = false;
	private PrintSet printset;

	//��������¼�����ʼ����ֹ���е�λ��.
	/**
	 * ��������Ϊ����0������
	 * 
	 * @author wupeng
	 * @version 3.1
	 */
	private class IntegerDoc extends PlainDocument {
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			int value = 0;
			boolean error = false;
			try {
				value = Integer.parseInt(str);
			} catch (Exception e) {
				error = true;
			}
			if (error || value < 0) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			if (value == 0 && offs == 0) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			super.insertString(offs, str, a);
		}
	}

//	//��������¼��Ĵ�ӡ����.
//	/**
//	 * �ĵ�������,��װ����Ϊ������
//	 * 
//	 * @version 3.1
//	 */
//	public class FloatDoc extends PlainDocument {
//		//    public String getText(int
//		public void insertString(int offs, String str, AttributeSet a)
//				throws BadLocationException {
//			float value = 0;
//			boolean error = false;
//			try {
//				String name = getText(0, offs) + str
//						+ getText(offs, getLength() - offs);
//				value = Float.parseFloat(name);
//			} catch (Exception e) {
//				error = true;
//			}
//			if (error || (value != 0 && value < MIN_SCALE) || value > MAX_SCALE) {
//				Toolkit.getDefaultToolkit().beep();
//				return;
//			}
//			super.insertString(offs, str, a);
//		}
//	}
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//ȡ����ť
			if (e.getSource() == btCancel) {
				ok = false;
				PageAreaDialog.this.dispose();
			}
			//ȷ����ť
			else if (e.getSource() == btOk) {
				try {
					ok = true;
					fillPrintSet();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(PageAreaDialog.this, ex
							.getMessage());
					return;
				}
				dispose();
			}

		}
	}
	private class InputException extends Exception {
		/**
		 * @param msg
		 */
		public InputException(String msg) {
			super(msg);
		}
	}

	/**
	 * ���캯��
	 */
	public PageAreaDialog() {
		this(null, "", false);
	}
	/**
	 * @param frame
	 *            ������
	 * @param title
	 *            ����
	 * @param modal
	 *            �Ƿ�ģʽ�Ի���
	 */
	public PageAreaDialog(Frame frame, String title, boolean modal) {
		super(frame, title);
		try {
			jbInit();
			restrict();
			pack();
		} catch (Exception ex) {
			AppDebug.debug(ex);
		}
	}
	/**
	 * ������ť���
	 * 
	 * @return JPanel
	 */
	private Box createButtonPanel() {
		Box boxButton = Box.createHorizontalBox();
		boxButton.add(Box.createHorizontalGlue());
		boxButton.add(btOk, null);
		boxButton.add(Box.createHorizontalStrut(12));
		boxButton.add(btCancel, null);
		boxButton.add(Box.createHorizontalStrut(10));

		return boxButton;
	}
	/**
	 * �õ�ҳ�������,��Ҫ������е�����ֵ�Ƿ�Ϸ�.�ڷ���restrict()�ж���ĳЩ�����¼���Ѿ���������,���� ������Щ���ֵ�ļ��.
	 */
	private void fillPrintSet() throws InputException {
		String strScale = tfPrintScale.getText().trim();
		if("".equals(strScale)){
			strScale = "0";
		}
		float scale = Float.parseFloat(strScale);
		if (scale < MIN_SCALE || scale > MAX_SCALE) {
			throw new InputException(MultiLang.getString("m1"));//("��ӡ�������ô���(0.3-3.0)");
		}
		printset.setViewScale(scale);
		// printset.setHeadEnable(cbHeader.isSelected());
		printset.setColPriorityPrinted(rbColToRow.isSelected());
		// printset.setRowHeadTail(rbHeaderRow.isSelected());
		//  printset.setTailEnable(cbTail.isSelected());
		try {
			int[] rang = null;
			// if(cbHeader.isEnabled()) {
			int start1 = getIntValue(tfRowHeader1.getText());
			int end1 = getIntValue(tfRowHeader2.getText());
			if (start1 > end1) {
				throw new InputException(MultiLang.getString("m2"));//("��ͷ����Ϣ���ô���,����λ��С����ʼλ��.");
			}
			if (start1 != 0 || end1 != 0) {
				rang = new int[]{start1 - 1, end1};
			}
			//}
			printset.setRowHeadRang(rang);
			rang = null;
			//if(cbTail.isEnabled()) {
			//���ͻ�����ϰ��תΪ�������ϰ�ߣ���1��ʼ������0��ʼ���������ֵ�������������ֵ��
			int start2 = getIntValue(tfColHeader1.getText());
			int end2 = getIntValue(tfColHeader2.getText());
			if (start2 > end2) {
				throw new InputException(MultiLang.getString("m3"));//("��ͷ����Ϣ���ô���,����λ��С����ʼλ��.");
			}
			if (start2 != 0 || end2 != 0) {
				rang = new int[]{start2 - 1, end2};
			}
			//}
			printset.setColHeadRang(rang);
			int startRow = getIntValue(tfAreaRow1.getText());
			int startCol = getIntValue(tfAreaCol1.getText());
			int endRow = getIntValue(tfAreaRow2.getText());
			int endCol = getIntValue(tfAreaCol2.getText());
			if (startRow > endRow) {
				throw new InputException(MultiLang.getString("m4"));//("��ӡ�������ô���,�н���λ��С����ʼλ��.");
			}
			if (startCol > endCol) {
				throw new InputException(MultiLang.getString("m5"));//("��ӡ�������ô���,�н���λ��С����ʼλ��.");
			}
			int[] area = null;
			if (startRow != 0 || startCol != 0 || endRow != 0 || endCol != 0) {
				//����Ϊ0��գ���ʼλ��Ĭ��ֵΪ-1������λ��Ĭ��ֵΪ0��Ŀ��������������������㷨һ�¡�
				area = new int[]{startRow - 1, startCol - 1, endRow, endCol};
			}
			printset.setPrintArea(area);
		} catch (Exception e) {
			throw new InputException(MultiLang.getString("m6"));//("��ӡ�������ô���");
		}
	}
	private String getIntString(int value) {
		if (value <= 0) {
			return "";
		}
		return Integer.toString(value);
	}
	private int getIntValue(String s) {
		if (s == null || s.equals("")) {
			return 0;
		}
		return Integer.parseInt(s);
	}
	private PrintSet getPrintSet() {
		return printset;
	}
	/**
	 * ���ֽ���,������������
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		mainPanle.setLayout(new BorderLayout());
		mainPanle.add(panel1, BorderLayout.CENTER);
		Box boxOrder = Box.createVerticalBox();
		Box boxScale = Box.createVerticalBox();
		panel1.setLayout(new GridBagLayout());
		pnHeaderArea.setLayout(new GridBagLayout());
		pnPrintOrder1.setLayout(new BorderLayout());
		pnPrintArea.setLayout(new GridBagLayout());
		pnHeaderArea.setBorder(BorderFactory.createTitledBorder(MultiLang
				.getString("RowColumnHeadSetUp")));
		pnPrintArea.setBorder(BorderFactory.createTitledBorder(MultiLang
				.getString("PrintArea")));
		pnPrintOrder1.setBorder(BorderFactory.createTitledBorder(MultiLang
				.getString("PrintOrder")));
		pnPrintScale.setBorder(BorderFactory.createTitledBorder(MultiLang
				.getString("PrintScale")));

		//��ӡ˳�����幹��
		boxOrder.add(Box.createVerticalGlue());
		boxOrder.add(rbColToRow, null);
		boxOrder.add(rbRowToCol, null);
		boxOrder.add(Box.createVerticalGlue());
		pnPrintOrder1.add(lbImage, BorderLayout.CENTER);
		pnPrintOrder1.add(boxOrder, BorderLayout.WEST);
		panel1.add(pnPrintScale, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				PANEL_INSETS, 0, 0));
		//��ӡ�����Ĺ���
		boxScale.add(Box.createVerticalGlue());
		boxScale.add(lbScale, null);
		boxScale.add(Box.createVerticalStrut(5));
		boxScale.add(tfPrintScale, null);
		boxScale.add(Box.createVerticalGlue());
		panel1.add(pnPrintOrder1, new GridBagConstraints(2, 1, 1, 2, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				PANEL_INSETS, 0, 0));
		pnPrintScale.add(boxScale, null);
		//��ӡ����Ĺ���
		panel1.add(pnPrintArea, new GridBagConstraints(0, 1, 2, 1, 0.8, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, PANEL_INSETS,
				0, 0));
		//��ӡͷ����Ϣ�Ĺ���
		panel1.add(pnHeaderArea, new GridBagConstraints(1, 0, 2, 1, 0.75, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				PANEL_INSETS, 0, 0));
		pnPrintArea.add(lbTo4, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 10, 0, 10), 0, 0));
		pnPrintArea.add(lbRow, new GridBagConstraints(0, 0, 1, 1, 0.3, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				PANEL_INSETS, 0, 0));
		pnPrintArea.add(lbCol, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				PANEL_INSETS, 0, 0));
		pnPrintArea.add(lbFrom4, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						5, 20, 5, 10), 0, 0));
		pnPrintArea.add(lbFrom3, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 20, 0, 10), 0, 0));
		pnPrintArea.add(lbTo3, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 10, 0, 10), 0, 0));
		pnPrintArea.add(tfAreaRow1, new GridBagConstraints(2, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), NUMBERWIDTH, 0));
		pnPrintArea.add(tfAreaRow2, new GridBagConstraints(4, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 10), NUMBERWIDTH, 0));
		pnPrintArea.add(tfAreaCol1, new GridBagConstraints(2, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), NUMBERWIDTH, 0));
		pnPrintArea.add(tfAreaCol2, new GridBagConstraints(4, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 10), NUMBERWIDTH, 0));
		//    pnHeaderArea.add(rbHeaderCol, new GridBagConstraints(0, 1, 1, 1, 0.0,
		// 0.0
		//        , GridBagConstraints.WEST, GridBagConstraints.NONE,
		//        new Insets(10, 10, 10, 10), 0, 0));
		//    pnHeaderArea.add(rbHeaderRow, new GridBagConstraints(0, 0, 1, 1, 0.3,
		// 0.0
		//        , GridBagConstraints.WEST, GridBagConstraints.BOTH,
		//        new Insets(10, 10, 10, 10), 0, 0));
		pnHeaderArea.add(lbRowHead, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(10, 10, 10, 10), 0, 0));
		pnHeaderArea.add(lbColHead, new GridBagConstraints(0, 0, 1, 1, 0.3,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(10, 10, 10, 10), 0, 0));

		pnHeaderArea.add(lbFrom1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 20, 0, 10), 0, 0));
		pnHeaderArea.add(lbFrom2, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 20, 0, 10), 0, 0));
		pnHeaderArea.add(tfRowHeader1, new GridBagConstraints(3, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), NUMBERWIDTH, 0));
		pnHeaderArea.add(tfColHeader1, new GridBagConstraints(3, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), NUMBERWIDTH, 0));
		pnHeaderArea.add(lbTo1, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 10, 0, 10), 0, 0));
		pnHeaderArea.add(tfRowHeader2, new GridBagConstraints(5, 0, 1, 1, 0.7,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), NUMBERWIDTH, 0));
		pnHeaderArea.add(lbTo2, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 10, 0, 10), 0, 0));
		pnHeaderArea.add(tfColHeader2, new GridBagConstraints(5, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), NUMBERWIDTH, 0));
		//    pnHeaderArea.add(cbTail, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
		//        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
		//        new Insets(0, 0, 0, 5), 0, 0));
		//    pnHeaderArea.add(cbHeader, new GridBagConstraints(1, 0, 1, 1, 0.0,
		// 0.0
		//        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
		//        new Insets(0, 0, 0, 5), 0, 0));

		mainPanle.add(createButtonPanel(), BorderLayout.SOUTH);
		mainPanle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		this.getContentPane().add(mainPanle, BorderLayout.CENTER);
		//
		KeyListener kl = new KeyListener() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ok = true;
					dispose();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ok = false;
					dispose();
				}
			}
			public void keyPressed(KeyEvent e) {
			}
			public void keyReleased(KeyEvent e) {
			}
		};
		addKeyListener(kl);
	}
	/**
	 * �õ���ӡ������Ϣ.
	 * 
	 * @param ps
	 * @param title
	 * @return PrintSet
	 */
	public static PrintSet pageDialog(Component parent,PrintSet ps, String title) {
		PageAreaDialog dia = new PageAreaDialog(JOptionPane.getFrameForComponent(parent), title, true);
		dia.setPrintSet(ps);
		//��Ļ����.
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimScr = toolkit.getScreenSize();
		Dimension dimDia = dia.getSize();
		Point p = new Point((dimScr.width - dimDia.width) / 2,
				(dimScr.height - dimDia.height) / 2);
		dia.setLocation(p);
		dia.setVisible(true);
		if (dia.ok) {
			return dia.getPrintSet();
		} else {
			return ps;
		}
	}
	/**
	 * �������¼������Χ�������.
	 */
	private void restrict() {
		ButtonGroup g1 = new ButtonGroup();
		//���ð�ť��
		g1.add(rbColToRow);
		g1.add(rbRowToCol);
		//������������ļ��.
		tfPrintScale.setDocument(new FloatDocument(MIN_SCALE, MAX_SCALE));
		//RadioButton ��
		ItemListener itemL = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getSource() == rbColToRow) {
					if (rbColToRow.isSelected()) {
						lbImage.setIcon(iconCol2Row);
					}
				} else if (e.getSource() == rbRowToCol) {
					if (rbRowToCol.isSelected()) {
						lbImage.setIcon(iconRow2Col);
					}
				}
			}
		};
		rbColToRow.addItemListener(itemL);
		rbRowToCol.addItemListener(itemL);
		JTextField[] tfs = new JTextField[]{tfAreaCol1, tfAreaCol2, tfAreaRow1,
				tfAreaRow2, tfRowHeader1, tfRowHeader2, tfColHeader1,
				tfColHeader2};
		for (int i = 0; i < tfs.length; i++) {
			tfs[i].setDocument(new IntegerDoc());
			tfs[i].setHorizontalAlignment(JTextField.RIGHT);
		}
		tfPrintScale.setHorizontalAlignment(JTextField.RIGHT);
		ActionListener l = new ButtonListener();
		btCancel.addActionListener(l);
		btOk.addActionListener(l);
	}
	/**
	 * ���ߴ�ӡ���ó�ʼ��ҳ�档
	 */
	private void setPrintSet(PrintSet ps) {
		if (ps == null) {
			this.printset = new PrintSet();
		} else {
			this.printset = (PrintSet) ps.clone();
		}
		//��ӡ����
		tfPrintScale.setText(Float.toString(printset.getViewScale()));
		//����ҳ��ҳβ
		//    if(printset.isHeadEnable()){
		//      cbHeader.setEnabled(true);
		int[] rowHeadRang = printset.getRowHeadRang();
		if (rowHeadRang != null) {
			tfRowHeader1.setText(getIntString(rowHeadRang[0] + 1));
			tfRowHeader2.setText(getIntString(rowHeadRang[1]));
		}
		int[] colHeadRang = printset.getColHeadRang();
		if (colHeadRang != null) {
			tfColHeader1.setText(getIntString(colHeadRang[0] + 1));
			tfColHeader2.setText(getIntString(colHeadRang[1]));
		}
		//���ô�ӡ����.
		int[] printArea = printset.getPrintArea();
		if (printArea != null) {
			tfAreaRow1.setText(getIntString(printArea[0] + 1));
			tfAreaRow2.setText(getIntString(printArea[2]));
			tfAreaCol1.setText(getIntString(printArea[1] + 1));
			tfAreaCol2.setText(getIntString(printArea[3]));
		}
		//��������
		if (printset.isColPriorityPrinted()) {
			rbColToRow.setSelected(true);
		} else {
			rbRowToCol.setSelected(true);
		}
	}
}