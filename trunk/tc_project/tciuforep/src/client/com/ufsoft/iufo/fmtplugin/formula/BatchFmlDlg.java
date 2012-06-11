package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.util.iufo.pub.UfoException;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.iuforeport.reporttool.temp.TXTFileFilter;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.fmtplugin.formula.ui.FormulaEditor;
import com.ufsoft.report.sysplugin.log.LogWindow;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;

/**
 * 原类名：EditBatchFuncDlg
 * 
 * @author zzl 2005-12-21
 */
public class BatchFmlDlg extends UfoDialog implements
		java.awt.event.ActionListener, KeyListener, IUfoContextKey, ChangeListener{
	private static final long serialVersionUID = -6099410123591232495L;

	private JPanel jPanel = null;

	private nc.ui.pub.beans.UITabbedPane jTabbedPane = null;

	private JPanel jPButtonPanel = null;

	private JButton jBImport = null;

	private JButton jBExport = null;

	private JButton jBOK = null;

	private JButton jBCancel = null;

	private JLabel jLabel1 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();

	private JLabel jLabel2 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();

	private JLabel jLabel3 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();

	private JScrollPane jSPaneCellFunc = null;

	private JScrollPane jSPaneTotalFunc = null;

	private JScrollPane jSPanePublicFunc = null;

	private JTextArea jEPaneTotalFunc = null;

	private JTextArea jEPaneCellFunc = null;

	private JTextArea jEPanePublicCellFunc = null;

	public static final String FUNC_TOKEN = ";";

	private int Current_Cell_Func;

	private int Current_Total_Func;

	private int Current_PublicCell_Func;

	private boolean m_bisAnaRep = false;

	private boolean m_bIsCellFuncEdited = false;// 记录单元公式界面是否被编辑过

	private boolean m_bIsTotalFuncEdited = false;// 记录汇总公式界面是否被编辑过
	// private boolean m_bIsCheckFuncEdited = false;//记录审核公式界面是否被编辑过

	private LogWindow m_ologDlg = null;// 记录错误日志

	private IContext context;

	private CellsModel cellsModel;

	/**
	 * <p>
	 * Title: 公式对象，用于排序等功能的实现
	 * </p>
	 * <p>
	 * Description:
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
	class FormulaObject implements Comparable {
		IArea m_area;

		String m_funcContent;

		/**
		 * 构造器
		 */
		public FormulaObject() {
		}

		public FormulaObject(String area, String funcContent) {
			m_area = AreaPosition.getInstance(area);
			m_funcContent = funcContent;
		}

		public FormulaObject(IArea area, String funcContent) {
			m_area = area;
			m_funcContent = funcContent;
		}

		/**
		 * 设置属性
		 * 
		 * @param area
		 *            AreaPosition
		 */
		public void setArea(IArea area) {
			m_area = area;
		}

		/**
		 * 设置属性
		 * 
		 * @param strArea
		 *            String
		 */
		public void setArea(String strArea) {
			m_area = AreaPosition.getInstance(strArea);
		}

		/**
		 * 设置属性
		 * 
		 * @param content
		 *            String
		 */
		public void setContent(String content) {
			m_funcContent = content;
		}

		/**
		 * 读取属性
		 * 
		 * @return AreaPosition
		 */
		public IArea getArea() {
			return m_area;
		}

		/**
		 * 读取属性
		 * 
		 * @return String
		 */
		public String getContent() {
			return m_funcContent;
		}

		/**
		 * toString方法
		 * 
		 * @return String
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			if (m_area.getStart().equals(m_area.getEnd())) {
				buffer.append(m_area.getStart().toString());
			} else {
				buffer.append(m_area.toString());
			}
			buffer.append("=");
			buffer.append(m_funcContent);
			return buffer.toString();
		}

		/**
		 * 比较
		 * 
		 * @param obj
		 *            Object
		 * @return int
		 */
		public int compareTo(Object obj) {
			if (obj != null) {
				if (obj instanceof FormulaObject) {
					FormulaObject fo = (FormulaObject) obj;
					int msCol = m_area.getStart().getColumn(), msRow = m_area
							.getStart().getRow();
					int osCol = fo.m_area.getStart().getColumn(), osRow = fo.m_area
							.getStart().getRow();
					// 按照先列后行比较
					if (msCol != osCol) {
						return msCol - osCol;
					} else {
						return msRow - osRow;
					}
				}
			}
			return -1;
		}

	}

	/**
	 * 构造器,父容器为Container
	 * 
	 * @param p0
	 *            Container
	 * @param p1
	 *            String
	 * @param p2
	 *            boolean
	 */
	public BatchFmlDlg(Container parent, CellsModel cellsModel,
			IContext context, boolean isAnaRep, LogWindow logDlg) {
		super(parent);
		this.context = context;
		this.cellsModel = cellsModel;
		this.m_bisAnaRep = isAnaRep;
		this.m_ologDlg = logDlg;
		try {
			init();
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	public BatchFmlDlg() {
		try {
			init();
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	private nc.ui.pub.beans.UITabbedPane getJTabbedPane() throws Exception {
		if (jTabbedPane == null) {
			jTabbedPane = new nc.ui.pub.beans.UITabbedPane();
			jTabbedPane.setName("TabbedPane");
			jTabbedPane.setTabPlacement(nc.ui.pub.beans.UITabbedPane.TOP);
			jTabbedPane.setEnabled(true);
			jTabbedPane.addChangeListener(this);
			jTabbedPane.setDebugGraphicsOptions(0);
			jTabbedPane.setDoubleBuffered(false);
			jTabbedPane.setRequestFocusEnabled(true);
			jTabbedPane.setToolTipText("");
			jTabbedPane.setVerifyInputWhenFocusTarget(true);
			// jTabbedPane.setFont(new java.awt.Font("dialog", 0, 12));
			jTabbedPane.setBounds(new Rectangle(11, 17, 570, 400));
			int iStart = 0;
			// @edit by wangyga at 2009-8-31,下午03:12:02 5.6新需求
//			if (IufoFormulalUtil.isCreateUnit(context)) {
				Current_PublicCell_Func = iStart;
				jTabbedPane.add(getJPPublicCellFunc(), StringResource
						.getStringResource("miufofunc001"),
						Current_PublicCell_Func);// 公有公式
				iStart++;
//			}
			Current_Cell_Func = iStart;
			jTabbedPane.add(getJPCellFunc(), StringResource
					.getStringResource("miufofunc003"), Current_Cell_Func); // "个性化公式"

			iStart++;
			Current_Total_Func = iStart;

			// 分析表没有汇总公式编辑一项
			if (!m_bisAnaRep) {
				jTabbedPane.add(getJPTotalFunc(), StringResource
						.getStringResource("miufo1000910"), Current_Total_Func); // "汇总公式"
			} else {
				JPanel unusedPanel = new UIPanel();
				unusedPanel.add(new nc.ui.pub.beans.UILabel(StringResource
						.getStringResource("miufopublic139")
						+ StringResource.getStringResource("miufo1000297")
						+ StringResource.getStringResource("miufo1001127")
						+ StringResource.getStringResource("miufo1000910")));
				jTabbedPane.add(unusedPanel, StringResource
						.getStringResource("miufo1000910"), Current_Total_Func);
			}
			// iStart++;
			// Current_Check_Func=iStart;
			// jTabbedPane.add(getJPCheckFunc(),StringResource.getStringResource("miufo1001132"),Current_Check_Func);//审核公式

			jTabbedPane.setSelectedIndex(0);

		}
		return jTabbedPane;
	}

	private JPanel getJButtonPanel() throws Exception {
		if (jPButtonPanel == null) {
			jPButtonPanel = new UIPanel();
			jPButtonPanel.setName("jPButtonPanel");
			jPButtonPanel.setEnabled(true);
			jPButtonPanel.setVisible(true);
			jPButtonPanel.setBounds(new Rectangle(10, 400, 560, 46));
			jLabel1.setMinimumSize(new Dimension(86, 30));
			jLabel1.setText("");
			jLabel2.setMinimumSize(new Dimension(86, 30));
			jLabel2.setRequestFocusEnabled(true);
			jLabel2.setText("");
			jLabel3.setMinimumSize(new Dimension(86, 30));
			jLabel3.setText("");

			getJButtonPanel().add(getJBImport(), "1");
			getJButtonPanel().add(jLabel1, "2");
			getJButtonPanel().add(getJBExport(), "3");
			getJButtonPanel().add(jLabel3, "4");
			getJButtonPanel().add(getJBOK(), "5");
			getJButtonPanel().add(jLabel2, "6");
			getJButtonPanel().add(getJBCancel(), "7");
			Border etched = BorderFactory.createEtchedBorder();
			getJButtonPanel().setBorder(etched);
		}
		return jPButtonPanel;
	}

	private JButton getJBImport() throws Exception {
		if (jBImport == null) {
			jBImport = new nc.ui.pub.beans.UIButton();
			jBImport.setMinimumSize(new Dimension(75, 22));
			jBImport.setToolTipText(StringResource
					.getStringResource("miufo1000958")); // "导入.txt文件"
			jBImport.setText(StringResource.getStringResource("miufo1000959")); // "导
																				// 入"
			jBImport.addActionListener(this);
			jBImport.setEnabled(true);
			jBImport.setVisible(true);
		}
		return jBImport;
	}

	private JButton getJBExport() throws Exception {
		if (jBExport == null) {
			jBExport = new nc.ui.pub.beans.UIButton();
			jBExport.setMinimumSize(new Dimension(75, 22));
			// jBExport.setFont(new java.awt.Font("dialog", 0, 12));
			jBExport.setToolTipText(StringResource
					.getStringResource("miufo1000960")); // "导出.txt文件"
			jBExport.setText(StringResource.getStringResource("miufo1000961")); // "导
																				// 出"
			jBExport.addActionListener(this);
			jBExport.setEnabled(true);
			jBExport.setVisible(true);
		}
		return jBExport;
	}

	private JButton getJBOK() throws Exception {
		if (jBOK == null) {
			jBOK = new nc.ui.pub.beans.UIButton();
			jBOK.setMinimumSize(new Dimension(75, 22));
			// jBOK.setFont(new java.awt.Font("dialog", 0, 12));
			jBOK.setText(StringResource.getStringResource("miufo1000962")); // "确
																			// 认"
			jBOK.setToolTipText(StringResource
					.getStringResource("miufo1000963")); // "确认"
			jBOK.addActionListener(this);
			jBOK.setEnabled(true);
			jBOK.setVisible(true);
		}
		return jBOK;
	}

	private JButton getJBCancel() throws Exception {
		if (jBCancel == null) {
			jBCancel = new nc.ui.pub.beans.UIButton();
			jBCancel.setMinimumSize(new Dimension(75, 22));
			// jBCancel.setFont(new java.awt.Font("dialog", 0, 12));
			jBCancel.setText(StringResource.getStringResource("miufo1000757")); // "取
																				// 消"
			jBCancel.setToolTipText(StringResource
					.getStringResource("miufopublic247")); // "取消"
			jBCancel.addActionListener(this);
			jBCancel.setEnabled(true);
			jBCancel.setVisible(true);
		}
		return jBCancel;
	}

	private JScrollPane getJPPublicCellFunc() throws Exception {
		if (jSPanePublicFunc == null) {
			jSPanePublicFunc = new UIScrollPane();
			jSPanePublicFunc.setToolTipText(StringResource
					.getStringResource("miufofunc002")); // "编辑公有公式"
			jEPanePublicCellFunc = new FormulaEditor(getAllFuncName());
			jEPanePublicCellFunc.setText(getPublicFormulas());
			jEPanePublicCellFunc.setEditable(IufoFormulalUtil.isCreateUnit(context));
			jEPanePublicCellFunc.addKeyListener(this);
			jSPanePublicFunc.getViewport().add(jEPanePublicCellFunc, null);
		}
		return jSPanePublicFunc;
	}

	private JScrollPane getJPCellFunc() throws Exception {
		if (jSPaneCellFunc == null) {
			jSPaneCellFunc = new UIScrollPane();
			jSPaneCellFunc.setToolTipText(StringResource
					.getStringResource("miufo1000964")); // "编辑个性化公式"
			jEPaneCellFunc = new FormulaEditor(getAllFuncName());
			jEPaneCellFunc.setText(getPersonalFormulas());
			jEPaneCellFunc.addKeyListener(this);
			jSPaneCellFunc.getViewport().add(jEPaneCellFunc, null);
		}
		return jSPaneCellFunc;
	}

	// private JScrollPane getJPCheckFunc() throws Exception{
	// if(jSPaneCheckFunc == null){
	// jSPaneCheckFunc = new UIScrollPane();
	// jSPaneCheckFunc.setToolTipText(StringResource.getStringResource("miuforep011"));
	// //"编辑审核公式"
	// jEPaneCheckFunc = new UIEditorPane();
	// jEPaneCheckFunc.setText(getAllCheckAsText());
	// jEPaneCheckFunc.addKeyListener(this);
	// jSPaneCheckFunc.getViewport().add(jEPaneCheckFunc, null);
	// }
	// return jSPaneCheckFunc;
	// }

	private JScrollPane getJPTotalFunc() throws Exception {
		if (jEPaneTotalFunc == null) {
			jSPaneTotalFunc = new UIScrollPane();
			jSPaneTotalFunc.setToolTipText(StringResource
					.getStringResource("miufo1000965")); // "编辑汇总公式"
			// jSPaneTotalFunc.setFont(new java.awt.Font("dialog", 0, 12));
			jEPaneTotalFunc = new FormulaEditor(getAllFuncName());
			jEPaneTotalFunc.setText(getTotalFormulas());
			jEPaneTotalFunc.addKeyListener(this);
			jSPaneTotalFunc.getViewport().add(jEPaneTotalFunc, null);

		}
		return jSPaneTotalFunc;
	}

	private void init() throws Exception {
		setTitle(StringResource.getStringResource("miufo1000966")); // "批量公式"
		setSize(600, 500);
		setResizable(false);
		jPanel = new UIPanel();
		jPanel.setLayout(null);
		jPanel.add(getJTabbedPane(), getJTabbedPane().getName());
		jPanel.add(getJButtonPanel(), getJButtonPanel().getName());

		this.getContentPane().add(jPanel, BorderLayout.CENTER);
		this.getContentPane().add(getJButtonPanel(), BorderLayout.SOUTH);
	}

	private void exportFunc() {
		javax.swing.JFileChooser fc = new UIFileChooser();
		fc.setApproveButtonText(StringResource
				.getStringResource("miufopublic157")); // "导出"
		fc.setDialogTitle(StringResource.getStringResource("miufopublic157")); // "导出"
		fc.setApproveButtonToolTipText(StringResource
				.getStringResource("miufopublic157")); // "导出"
		TXTFileFilter xf = new TXTFileFilter();
		fc.setFileFilter(xf);
		fc.setMultiSelectionEnabled(false);
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
			java.io.File file = fc.getSelectedFile();
			String pathname = file.getPath();
			if (pathname.indexOf(".txt") == -1
					&& pathname.indexOf(".TXT") == -1) {
				pathname += ".txt";
			}
			try {
				File newFile = new File(pathname);
				BufferedWriter out = new BufferedWriter(new FileWriter(newFile));
				// 从编辑器中取值
				String outputString = getTextFromCurrentEdit();
				out.write(outputString);
				out.close();
			} catch (java.io.IOException ex) {
				AppDebug.debug(ex);
			}
		}
	}

	private void importFunc() {
		javax.swing.JFileChooser fc = new UIFileChooser();
		fc.setApproveButtonText(StringResource
				.getStringResource("miufopublic156")); // "导入"
		fc.setDialogTitle(StringResource.getStringResource("miufopublic156")); // "导入"
		fc.setApproveButtonToolTipText(StringResource
				.getStringResource("miufopublic156")); // "导入"
		TXTFileFilter xf = new TXTFileFilter();
		fc.setFileFilter(xf);
		fc.setMultiSelectionEnabled(false);
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
			java.io.File file = fc.getSelectedFile();
			String pathname = file.getPath();
			if (pathname.indexOf(".txt") == -1
					&& pathname.indexOf(".TXT") == -1) {
				UfoPublic.sendWarningMessage(StringResource
						.getStringResource("miufo1000968"), null); // "请选择TXT文本文件！"
			}

			try {
				// 得到文件,获得输入
				File newFile = new File(pathname);
				BufferedReader in = new BufferedReader(new FileReader(newFile));

				String readedLine = null;
				ArrayList<String> listReturn = new ArrayList<String>();
				while ((readedLine = in.readLine()) != null) {
					readedLine = readedLine.trim();
					if (readedLine.endsWith(";")) {
						readedLine = readedLine.substring(0, readedLine
								.length() - 1);
					}
					listReturn.add(readedLine);
				}
				in.close();

				StringBuffer inputString = new StringBuffer();
				if (listReturn.size() > 0) {
					for (int i = 0, size = listReturn.size(); i < size; i++) {
						inputString.append((String) listReturn.get(i));
						inputString.append("\r\n");
					}
				}

				// 将导入的文本显示在相应的界面上
				setTextToCurrentEdit(inputString.toString());
			} catch (java.io.IOException ex) {
				AppDebug.debug(ex);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jBExport) {
			exportFunc();
		} else if (e.getSource() == jBImport) {
			importFunc();
		} else if (e.getSource() == jBOK) {
			try {
				StringBuffer errFuncMsg = new StringBuffer();
				String errMsg = StringResource
						.getStringResource("miufo1000969"); // "公式中有错误公式！"
				String logmsg = StringResource
						.getStringResource("miufo1000970"); // "错误详情请看日志！"

				// 单元公式
				if (m_bIsCellFuncEdited) {
					// m_oTable.clearFormula(maxArea);

					String outputString_Cell = trimString(jEPaneCellFunc
							.getText());
					getFmlExecutor().clearAllFormula(true);

					// 保存定义的公式
					if (IufoFormulalUtil.isCreateUnit(context)) {
						String strPublicCellFunc = trimString(jEPanePublicCellFunc
								.getText());
						errFuncMsg.append(parseText(strPublicCellFunc, true,
								true, true));
					}

					errFuncMsg.append(parseText(outputString_Cell, true, true,
							false));
				}
				// 汇总公式
				if (m_bIsTotalFuncEdited) {
					getFmlExecutor().clearAllFormula(false);
					String outputString_Total = trimString(jEPaneTotalFunc
							.getText());
					errFuncMsg.append(parseText(outputString_Total, true,
							false, false));
				}

				// //保存审核公式
				// if (m_bIsCheckFuncEdited == true) {
				// String strCheckFormula = trimString(jEPaneCheckFunc
				// .getText());
				// errFuncMsg.append(parseCheckFormula(strCheckFormula));
				// }

				// formulaPI.setDirty(true);

				if (errFuncMsg.length() > 0) {
					if (m_ologDlg == null) {
						UfoPublic.sendWarningMessage(errMsg, null);
						return;
					} else {
						m_ologDlg.addLog(errFuncMsg.toString());
						UfoPublic.sendWarningMessage(errMsg + logmsg, null);
						return;
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace(System.out);
				UfoPublic.sendWarningMessage(ex.getMessage(), null);
				return;
			}
			this.close();
		} else if (e.getSource() == jBCancel) {
			this.close();
		}
	}

	@Override
	public void close() {
		super.close();
		cellsModel = null;
	}

	public void keyTyped(KeyEvent e) {
		// 目前仅控制到只要在编辑界面上进行过键盘操作就认为是编辑过公式了,
		// 具体键入哪些键之后才是编辑过公式
		// if(e.getKeyChar() > KeyEvent.VK_SPACE && e.getKeyChar() <
		// KeyEvent.VK_Z)

		if (e.getSource() == jEPaneCellFunc
				|| e.getSource() == jEPanePublicCellFunc) {
			m_bIsCellFuncEdited = true;
		} else if (e.getSource() == jEPaneTotalFunc) {
			m_bIsTotalFuncEdited = true;
		}
		// else if(e.getSource() == jEPaneCheckFunc)
		// {
		// m_bIsCheckFuncEdited = true;
		// }
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			if (e.getSource() == jEPaneCellFunc
					|| e.getSource() == jEPanePublicCellFunc) {
				m_bIsCellFuncEdited = true;
			} else if (e.getSource() == jEPaneTotalFunc) {
				m_bIsTotalFuncEdited = true;
			}
			// else if(e.getSource() == jEPaneCheckFunc)
			// {
			// m_bIsCheckFuncEdited = true;
			// }
		}
	}

	private String getTextFromCurrentEdit() {
		String outputString = "";
		if (jTabbedPane.getSelectedIndex() == Current_Cell_Func) {
			outputString = jEPaneCellFunc.getText().trim();
		} else if (jTabbedPane.getSelectedIndex() == Current_Total_Func) {
			outputString = jEPaneTotalFunc.getText().trim();
		} else if (jTabbedPane.getSelectedIndex() == Current_PublicCell_Func) {
			outputString = jEPanePublicCellFunc.getText().trim();
		}
		// else if(jTabbedPane.getSelectedIndex() ==Current_Check_Func)
		// {
		// outputString =jEPaneCheckFunc.getText().trim();
		// }
		return outputString;
	}

	private void setTextToCurrentEdit(String text) {
		if (jTabbedPane.getSelectedIndex() == Current_Cell_Func) {
			jEPaneCellFunc.setText(text);
			m_bIsCellFuncEdited = true;

		} else if (jTabbedPane.getSelectedIndex() == Current_Total_Func) {
			jEPaneTotalFunc.setText(text);
			m_bIsTotalFuncEdited = true;
		}
		/*
		 * else if(jTabbedPane.getSelectedIndex() ==Current_Check_Func) {
		 * jEPaneCheckFunc.setText(text); m_bIsCheckFuncEdited = true;
		 *  }
		 */else if (jTabbedPane.getSelectedIndex() == Current_PublicCell_Func) {
			jEPanePublicCellFunc.setText(text);
			m_bIsCellFuncEdited = true;

		}
	}

	public static void main(String[] args) {
		BatchFmlDlg editDlg = new BatchFmlDlg();
		editDlg.show();
	}

	/**
	 * parseText 解析传入的字符串，校验输入的合法性 并根据是否需要保存公式来对解析后的公式进行保存
	 * 
	 * @param strText
	 *            String
	 * @param isNeedSave
	 *            boolean
	 * @i18n uiiufocalc00001=无法加入公式
	 * @i18n uiiufocalc00001=无法加入公式
	 * @i18n miufopublic394=错误信息
	 */
	private String parseText(String strText, boolean isNeedSave,
			boolean isCellFunc, boolean bPublic) {
		if (strText == null || strText.length() == 0)
			return "";
		StringBuffer buffer = new StringBuffer();
		;
		try {
			BufferedReader reader = new BufferedReader(
					new StringReader(strText));
			String readedLine = null;
			while ((readedLine = reader.readLine()) != null) {
				readedLine = readedLine.trim();

				if (isNeedSave) {
					try {
						if (readedLine.endsWith(";")) {
							readedLine = readedLine.substring(0, readedLine
									.length() - 1);
						}
						if (addFormula(readedLine, isCellFunc, bPublic) == false) {
							buffer.append(StringResource
									.getStringResource("uiiufocalc00001"));
							buffer.append(" [");
							buffer.append(readedLine);
							buffer.append("] ;\r\n");
						}
					} catch (Exception e) {
						buffer.append(StringResource
								.getStringResource("uiiufocalc00001"));
						buffer.append(" [");
						buffer.append(readedLine);
						buffer.append("] ;");
						buffer.append(StringResource
								.getStringResource("miufopublic394"));
						buffer.append(e.getMessage());
						buffer.append(";]\r\n");
					}
				} else {
					buffer.append(readedLine);
					buffer.append("\r\n");
				}
			}
			reader.close();
		} catch (IOException e) {

			AppDebug.debug(e);
		}
		return buffer.toString();
	}

	private FuncListInst getFuncList() {
		return getFmlExecutor().getFuncListInst();
	}

	/**
	 * 获得所有函数的名称
	 * 
	 * @return
	 */
	public String[] getAllFuncName() {
		FuncListInst funcList = getFuncList();
		if (funcList == null)
			return null;
		UfoSimpleObject[] allModules = funcList.getCatList();
		Vector<String> funcNameVector = new Vector<String>();
		for (UfoSimpleObject module : allModules) {
			if (module == null)
				continue;
			UfoSimpleObject[] m_FuncNameList = funcList.getFuncList(module
					.getID());
			if (m_FuncNameList == null || m_FuncNameList.length == 0)
				continue;
			for (UfoSimpleObject simpleObj : m_FuncNameList) {
				if (simpleObj != null) {
					funcNameVector.addElement(simpleObj.getName());
				}
			}
		}
		return funcNameVector.toArray(new String[0]);
	}

	private boolean addFormula(String strfunc, boolean isCellFunc,
			boolean bPublic) throws Exception {
		if (strfunc == null || strfunc.length() <= 0)
			return true;
		// throw new UfoException("miufo1000973", new String[]{currentEditer});
		// //currentEditer+"公式的内容为空!"

		String currentEditer = StringResource.getStringResource("miufo1000971"); // "单元"
		if (!isCellFunc) {
			currentEditer = StringResource.getStringResource("miufopublic140"); // "汇总"
		}

		// 取到等号的位置
		int eqIndex = strfunc.indexOf("=");
		if (eqIndex < 0 || eqIndex == strfunc.length() - 1)
			return false;

		// 取到等号左右两边的值
		String strArea = strfunc.substring(0, eqIndex);
		String funcContent = strfunc.substring(eqIndex + 1);
		if (funcContent.trim().length() == 0)
			return false;

		try {
			AreaPosition area = AreaPosition.getInstance(strArea);
			// String oldFunc = getFormulaAt(area,isCellFunc);
			// //只有当公式和原有公式相比较发生改变的时候,才进行公式的保存,否则不进行保存
			// if(!funcContent.equalsIgnoreCase(oldFunc))
			// {

			// m_oTable.clearFormula(area);
			StringBuffer showErrMessage = new StringBuffer();
			return getFmlExecutor().addUserDefFormula(showErrMessage, area,
					funcContent, isCellFunc, bPublic, false);

		} catch (ParseException pe) {
			// pe.printStackTrace(System.out);
			AppDebug.debug(pe.getMessage());
			throw new UfoException("miufo1000974", new String[] {
					currentEditer, strfunc, pe.getMessage() }); // "错误的"+currentEditer+"公式:"+strfunc+",公式内容"+pe.getMessage()
		}
	}


	private String getPublicFormulas() {
//		if (!IufoFormulalUtil.isCreateUnit(context))
//			return "";

		Hashtable hashArea = getFormulaModel().getPublicFormulaAll();
		Hashtable hashFormula = getFormulaModel().getFormulaAllByType(true);

		return getAllFormulaTxts(getNewHashFormula(hashArea), hashFormula);
	}

	private String getPersonalFormulas() {

		String strUnitId = (String) context.getAttribute(CUR_UNIT_ID);
		Hashtable hashArea = getFormulaModel()
				.getUnitPersonalFormula(strUnitId);
		Hashtable hashFormula = getFormulaModel().getFormulaAllByType(true);

		return getAllFormulaTxts(getNewHashFormula(hashArea), hashFormula);

	}

	private String getTotalFormulas() {

		Hashtable hashAllFormula = getFormulaModel().getFormulaAllByType(false);
		return getAllFormulaTxts(hashAllFormula, hashAllFormula);

	}

	private Hashtable getNewHashFormula(Hashtable hashAllFormula) {
		if (hashAllFormula == null || hashAllFormula.size() == 0)
			return null;
		Hashtable rtn = new Hashtable();
		Iterator iter = hashAllFormula.keySet().iterator();
		while (iter.hasNext()) {
			String dynAreaPK = (String) iter.next();
			Hashtable dynAreaFormulas = (Hashtable) hashAllFormula
					.get(dynAreaPK);
			rtn.putAll(dynAreaFormulas);
		}

		return rtn;
	}

	/**
	 * @param hashArea
	 *            key=IArea, value=FormulaVO 区域集合
	 * @param hashFml
	 *            key=IArea, value=FormulaVO 公式内容集合
	 * 
	 * @return 公式集合，每一个元素为一个用户显示形式的公式内容String+\r\n
	 */
	@SuppressWarnings("unchecked")
	private String getAllFormulaTxts(Hashtable hashArea, Hashtable hashFml) {
		if (hashArea == null)
			return "";

		List<FormulaObject> vecReturn = new ArrayList<FormulaObject>();
		String funcContent = null;
		FormulaVO fvo = null;
		IArea areaKey = null;

		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);

		FormulaObject fo = null;
		Iterator iter = hashArea.keySet().iterator();
		while (iter.hasNext()) {
			areaKey = (IArea) iter.next();
			fvo = (FormulaVO) hashArea.get(areaKey);
			// fvo = (FormulaVO) hashFml.get(areaKey);
			if (fvo == null)
				continue;
			if (fvo.getLet() == null) {
				try {
					DynAreaCell dynAreaCell = dynAreaModel
							.getDynAreaCellByPos(areaKey.getStart());
					String dynAreaPK = dynAreaCell == null ? DynAreaVO.MAINTABLE_DYNAREAPK
							: dynAreaCell.getDynAreaVO().getDynamicAreaPK();
					funcContent = getFmlExecutor().getUserDefFmlContent(fvo,
							areaKey, dynAreaPK);
				} catch (Exception e) {
					e.printStackTrace(System.out);
					funcContent = fvo.getFormulaContent();
				}
			} else {
				try {
					funcContent = getFmlExecutor().getStrFmlByLet(fvo.getLet(),
							true);
				} catch (Exception e) {
					e.printStackTrace(System.out);
					funcContent = fvo.getFormulaContent();
				}
			}
			fo = new FormulaObject(areaKey, funcContent);
			vecReturn.add(fo);

		}

		if (vecReturn != null) {
			Collections.sort(vecReturn);
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < vecReturn.size(); i++) {
				buffer.append(vecReturn.get(i).toString());
				buffer.append(";\r\n");
			}
			return buffer.toString();
		}
		return "";

	}

	private UfoFmlExecutor getFmlExecutor() {
		return getFormulaModel().getUfoFmlExecutor();
	}

	private FormulaModel getFormulaModel() {
		return FormulaModel.getInstance(cellsModel);
	}

	/**
	 * 过滤回车换行 wupeng2005-3-11修改，原因是换行做法分割标记
	 */
	private String trimString(String str) {
		return str;
	}

	/**
	 * 
	 * @create by wangyga at 2009-8-31,下午03:39:35
	 *
	 * @param isEnabled
	 */
	public void stateChanged(ChangeEvent e) {
		try {
			JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
			int m_nTab = tabbedPane.getSelectedIndex();
			switch (m_nTab) {
			case 0:
				requestCompFocus(jEPanePublicCellFunc);
				validateBtnEnabled(IufoFormulalUtil.isCreateUnit(context));
				break;
			case 1:
				requestCompFocus(jEPaneCellFunc);
				validateBtnEnabled(true);
				break;
			case 2:
				requestCompFocus(jEPaneTotalFunc);
				validateBtnEnabled(true);
				break;

			default:
				break;
			}
		} catch (Exception e2) {
			AppDebug.debug(e);
		}
		
	}
	
	/**
	 * 
	 * @create by wangyga at 2009-8-31,下午03:39:45
	 *
	 * @param isEnabled
	 */
	private void validateBtnEnabled(boolean isEnabled){
		try {
			getJBImport().setEnabled(isEnabled);
			getJBOK().setEnabled(isEnabled);
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		
	}
	
	/**
	 * 
	 * @create by wangyga at 2009-8-31,下午03:41:56
	 *
	 * @param comp
	 */
	private void requestCompFocus(final JComponent comp){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				comp.requestFocus();
			}
		});
	}

}
