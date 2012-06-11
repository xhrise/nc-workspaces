package com.ufsoft.report.fmtplugin.formula.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.util.iufo.pub.UFOString;

import com.ufsoft.iufo.util.parser.IFuncDriver;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.fmtplugin.formula.CommonFmlEditDlg;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.script.function.UfoFuncInfo;
import com.ufsoft.script.function.UfoFuncList;
import com.ufsoft.table.IArea;

/**
 * ��ʽ�༭ʱ�ĺ���ѡ�����
 * 
 * @author zhaopq
 * @created at 2009-2-20,����01:57:54
 * 
 */
public class FunctionPanel extends UIPanel {

	private static final long serialVersionUID = -3290393184248556948L;

	/** ��ʾ���������Pane */
	private JScrollPane funcCategoryPane;

	/** ��ʾ���������JList */
	private JList funcCategoryJList;

	/** ��ʾ��������Pane */
	private JScrollPane funcNamePane;

	/** ��ʾ��������JList */
	private JList funcNameJList;

	/** ��ʾ����������Ϣ��Pane */
	private JScrollPane funcDescPane;

	/** ��ʾ����������Ϣ��JTextArea */
	private JTextArea funcDescTextArea;

	protected CommonFmlEditDlg fmlEditDlg;
	
	Timer timer ;

	public FunctionPanel(CommonFmlEditDlg fmlEditDlg) {
		this.fmlEditDlg = fmlEditDlg;
		initialize();
	}

	protected void initialize() {
		this.setLayout(new GridLayout(1, 3));
		this.add(getFuncCategoryPane());
		this.add(getFuncNamePane());
		this.add(getFuncDescPane());
	}

	public JScrollPane getFuncCategoryPane() {
		if (funcCategoryPane == null) {
			funcCategoryPane = new UIScrollPane();
			funcCategoryPane.setViewportView(getFuncCategoryList());
		}
		return funcCategoryPane;
	}

	public JScrollPane getFuncNamePane() {
		if (funcNamePane == null) {
			funcNamePane = new UIScrollPane();
			funcNamePane.setViewportView(getFuncList());
			funcNamePane.setPreferredSize(new Dimension(193, 180));
		}
		return funcNamePane;
	}

	public JScrollPane getFuncDescPane() {
		if (funcDescPane == null) {
			funcDescPane = new UIScrollPane();
			funcDescPane.setViewportView(getFuncDescTextArea());
		}
		return funcDescPane;
	}

	// �õ���������б�
	private JList getFuncCategoryList() {
		if (funcCategoryJList == null) {
			funcCategoryJList = new UIList();
			funcCategoryJList
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			funcCategoryJList.setListData(sortArray(getFuncCategory()));

			funcCategoryJList
					.addListSelectionListener(new ListSelectionListener() {

						public void valueChanged(ListSelectionEvent e) {
							UfoSimpleObject category = (UfoSimpleObject) funcCategoryJList
									.getSelectedValue();
							getFuncList().setListData(
									sortArray(getFuncs(category)));
							getFuncList().setSelectedIndex(0);
						}
					});
			funcCategoryJList.setSelectedIndex(0);
		}
		return funcCategoryJList;
	}

	/**
	 * ��ȡ�������༯�ϵķ�����������Ҫ����
	 * 
	 * @return
	 */
	protected UfoSimpleObject[] getFuncCategory() {
		return fmlEditDlg.getFmlExecutor().getFuncListInst().getCatList();
	}

	// �õ������б�
	public JList getFuncList() {
		if (funcNameJList == null) {
			funcNameJList = new UIList();
			funcNameJList
					.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			funcNameJList.setListData(sortArray(getFuncs(null)));
			funcNameJList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					UfoSimpleObject category = (UfoSimpleObject) funcNameJList
							.getSelectedValue();
					getFuncDescTextArea().setText(
							getFuncDesc(fmlEditDlg.getFmlExecutor()
									.getFuncListInst(), category));
				}
			});
			funcNameJList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Object selectItem = FunctionPanel.this.getFuncList()
							.getSelectedValue();
					if (selectItem == null)
						return;
					if (e.getClickCount() >= 2 && funcNameJList.isEnabled()) {
						UfoFuncInfo funcInfo = getSelectedFuncInfo();
						if (funcInfo == null)
							return;
						if (funcInfo.getParamLen() > 0) {
							fmlEditDlg
									.setCaretPos(fmlEditDlg
											.getCurrentFmlEditArea().getFormulaEditor()
											.getCaretPosition());
                            if(!fmlEditDlg.isRefDlg()){
                            	fmlEditDlg.setVisible(false);
                            }
							
							// @edit by wangyga at 2009-7-24,����10:49:16 ���jre1.6,��fmlEditDlg.setVisible(false)���ٴ�һ��dlgʱ����һ�����沢
							//û�д���Ļ����ʧ��������Ļ��˸�����⣬��֪�������������ԭ������ʱ��������
							 timer = new Timer(10,new ActionListener(){
								public void actionPerformed(ActionEvent e) {
									IArea anchorCell = fmlEditDlg.getCellsModel().getSelectModel().getAnchorCell();
									fmlEditDlg.openFunctionWizard((UfoSimpleObject)getFuncList().getSelectedValue(),anchorCell);
									
									timer.stop();
									
									SwingUtilities.invokeLater(new Runnable(){
										public void run() {
											if(!fmlEditDlg.isRefDlg()){
												fmlEditDlg.setVisible(true);
											}
										}
									});
								}
								
							});
							timer.start();

						} else {
							CommonFmlEditDlg.addString(funcInfo, fmlEditDlg
									.getCurrentFmlEditArea().getFormulaEditor());
						}
					}
				}

			});
			funcNameJList.setSelectedIndex(0);
		}
		return funcNameJList;
	}
	
	public UfoFuncInfo getSelectedFuncInfo() {
		Object selectItem = getFuncList().getSelectedValue();
		if (selectItem instanceof UfoSimpleObject) {
			UfoSimpleObject module = (UfoSimpleObject) selectItem;
			return fmlEditDlg.getFmlExecutor().getFuncListInst().getFuncInfo(
					module.getID(), module.getName());
		}
		return null;
	}

	/** ���� */
	protected UfoSimpleObject[] sortArray(UfoSimpleObject[] array) {
		Arrays.sort(array, new Comparator<UfoSimpleObject>() {
			public int compare(UfoSimpleObject o1, UfoSimpleObject o2) {

				return UFOString.compareHZString(o1.getName(), o2.getName());
			}

		});
		return array;
	}

	/**
	 * ��ȡ���������ϵķ�����������Ҫ����
	 * 
	 * @param category
	 *            ����������
	 * @return
	 */
	protected UfoSimpleObject[] getFuncs(UfoSimpleObject category) {
		List<UfoSimpleObject> result = new ArrayList<UfoSimpleObject>();
		if (category != null) {
			UfoSimpleObject[] funcNameList = fmlEditDlg.getFmlExecutor()
					.getFuncListInst().getFuncList(category.getID());
			if (funcNameList != null) {
				for (int i = 0; i < funcNameList.length; i++) {
					UfoSimpleObject simeObj = funcNameList[i];
					if (simeObj == null)
						continue;
					// modify by wangyga 2008-7-24 ����IFF��PAI,SQR��������֧���������������ֹ�¼��
					int nFid = simeObj.getID();
					if (nFid == UfoFuncList.FPAI || nFid == UfoFuncList.FSQR
							|| nFid == UfoFuncList.FIFF)
						continue;
					result.add(simeObj);
				}
			}
		}
		return result.toArray(new UfoSimpleObject[result.size()]);
	}

	// ��ȡ����˵���ı���
	protected JTextArea getFuncDescTextArea() {
		if (funcDescTextArea == null) {
			funcDescTextArea = new UITextArea();
			funcDescTextArea.setLineWrap(true);
			funcDescTextArea.setFocusable(false);
		}
		return funcDescTextArea;
	}

	/**
	 * ����ָ�������б�ָ��������������Ϣ
	 * 
	 * @param funcList
	 * @param module
	 * @return
	 */
	public String getFuncDesc(FuncListInst funcList, UfoSimpleObject module) {
		if (funcList == null || module == null) {
			return "";
		}
		UfoFuncInfo funcInfo = funcList.getFuncInfo(module.getID(), module
				.getName());
		StringBuffer buf = new StringBuffer();
		buf.append(MultiLang.getString("miufo1000817"));// "�������ܣ�"
		buf.append(funcInfo.getFuncDescription());
		if (isOuterFunc(funcList, module.getName())) {
			buf.append("\r\n");
			buf.append(MultiLang.getString("miufo1000823")); // "����˵����"
			buf.append(getExtFuncInfo(funcList, module.getName()));
		} else {
			buf.append("\r\n");
			buf.append(MultiLang.getString("miufo1000821")); // "����˵����"
			buf.append(funcList.getParamDescription(module.getID(), module
					.getName()));
		}
		return buf.toString();
	}

	/** ��麯���Ƿ��ⲿ���� */
	public boolean isOuterFunc(FuncListInst funcList, String strFunName) {
		return fmlEditDlg.isOuterFunc(funcList, strFunName);
	}

	public String getExtFuncInfo(FuncListInst funcList, String strFuncName) {
		String strDriverName = funcList.getExtFuncDriver(strFuncName);
		IFuncDriver driver = (IFuncDriver) funcList.getExtDriver(strDriverName);
		if (driver != null)
			return driver.getFuncForm(strFuncName);
		return "";
	}

	public UfoSimpleObject getSelectedFuncCategory() {
		return (UfoSimpleObject) getFuncCategoryList().getSelectedValue();
	}

	public void setSelectedFuncCategory(UfoSimpleObject category) {
		if (category != null) {
			getFuncCategoryList().setSelectedValue(category, true);
		}
	}

	public UfoSimpleObject getSelectedFunc() {
		return (UfoSimpleObject) getFuncList().getSelectedValue();
	}

	public void setSelectedFunc(UfoSimpleObject func) {
		if (func != null) {
			getFuncList().setSelectedValue(func, true);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		setSubCompEnabled(this,enabled);
	}
	
	private void setSubCompEnabled(Container comp, boolean enabled){
		if(comp instanceof JList){
			comp.setEnabled(enabled);
		}
		Component[] comps = comp.getComponents();
		if(comps == null || comps.length == 0){
			comp.setEnabled(enabled);
			return;
		}
		for(Component com : comps){
			setSubCompEnabled((Container)com,enabled);
		}
	}
	
}
