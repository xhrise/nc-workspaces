package com.ufsoft.report.fmtplugin.formula.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.MouseInputAdapter;

import nc.util.iufo.pub.UFOString;

import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.fmtplugin.formula.CommonFmlEditDlg;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.script.function.UfoFuncInfo;

/**
 * 函数搜索面板
 * 
 * @author zhaopq
 * @created at 2009-2-21,下午03:21:43
 * 
 */
public class SearchFuncPanel extends FunctionPanel {
	private static final long serialVersionUID = 4272997329342112991L;

	public final static int FIND_BY_NAME = 1;

	public final static int FIND_BY_FUNCTION = 2;

	private JLabel nameLabel;

	private JLabel funcLabel;

	private JTextField nameTextField;

	private JTextField funcTextField;

	public SearchFuncPanel(CommonFmlEditDlg fmlEditDlg) {
		super(fmlEditDlg);
	}

	@Override
	protected void initialize() {
		this.setLayout(new BorderLayout());
		JPanel findPanel = new JPanel();
		findPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		findPanel.add(getNameLabel());
		findPanel.add(getNameTextField());
		findPanel.add(getFuncLabel());
		findPanel.add(getFuncTextField());
		this.add(findPanel, BorderLayout.NORTH);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(getFuncNamePane(), BorderLayout.WEST);
		panel.add(getFuncDescPane(), BorderLayout.CENTER);

		this.add(panel, BorderLayout.CENTER);
	}

	/**
	 * miufopublic291=函数名称
	 */
	private JLabel getNameLabel() {
		// TODO:需要把这个多语资源添加到文件中
		if (nameLabel == null) {
			nameLabel = new JLabel(MultiLang.getString("miufopublic291"));
			nameLabel.setBounds(8, 2, 50, 20);
		}
		return nameLabel;
	}

	private JTextField getNameTextField() {
		if (nameTextField == null) {
			nameTextField = new FindTextField(FIND_BY_NAME);
			nameTextField.setPreferredSize(new Dimension(100, 21));
		}
		return nameTextField;
	}

	/**
	 * miufo00667=函数功能
	 */
	private JLabel getFuncLabel() {
		// TODO:需要把这个多语资源添加到文件中
		if (funcLabel == null) {
			funcLabel = new JLabel(MultiLang.getString("miufo00667"));
			funcLabel.setBounds(200, 2, 50, 20);
		}
		return funcLabel;
	}

	private JTextField getFuncTextField() {
		if (funcTextField == null) {
			funcTextField = new FindTextField(FIND_BY_FUNCTION);
			funcTextField.setPreferredSize(new Dimension(100, 21));
		}
		return funcTextField;
	}

	/**
	 * 根据条件查询函数
	 * 
	 * @param findeType
	 */
	public void find(int findeType) {
		String condition = null;
		if (findeType == FIND_BY_NAME) {
			condition = getNameTextField().getText();
			getFuncTextField().setText("");
		} else if (findeType == FIND_BY_FUNCTION) {
			condition = getFuncTextField().getText();
			getNameTextField().setText("");
		}

		if (UFOString.isEmpty(condition)){
			return;
		}
		condition = condition.trim().toUpperCase();
		FuncListInst funcList = fmlEditDlg.getFmlExecutor().getFuncListInst();
		List<UfoSimpleObject> funcs = new ArrayList<UfoSimpleObject>();
		UfoSimpleObject[] allSimpleObjAry = funcList.getCatList();
		for (UfoSimpleObject simpleObj : allSimpleObjAry) {
			if (simpleObj == null)
				continue;
			UfoSimpleObject[] funcObjAry = funcList.getFuncList(simpleObj
					.getID());
			boolean filtResult = false;
			if(funcObjAry==null){
				continue;
			}
			for (UfoSimpleObject funcObj : funcObjAry) {

				if (findeType == FIND_BY_NAME) {
					filtResult = filtrateByName(funcObj, condition);
				} else if (findeType == FIND_BY_FUNCTION) {
					filtResult = filtrateByFunction(funcList, funcObj,
							condition);
				}
				if (filtResult) {
					funcs.add(funcObj);
				}
			}
		}
		getFuncList().setListData(
				sortArray(funcs.toArray(new UfoSimpleObject[funcs.size()])));
		if (funcs.isEmpty()) {
			getFuncDescTextArea().setText("");
		} else {
			getFuncList().setSelectedIndex(0);
		}
	}

	private boolean filtrateByName(UfoSimpleObject funcObj, String condition) {
		return funcObj != null && funcObj.getName().indexOf(condition) >= 0;
	}

	private boolean filtrateByFunction(FuncListInst funcList,
			UfoSimpleObject funcObj, String condition) {
		UfoFuncInfo funcInfo = funcList.getFuncInfo(funcObj.getID(), funcObj
				.getName());
		if (funcInfo == null)
			return false;
		String strFuncDes = funcInfo.getFuncDescription();
		return strFuncDes != null && strFuncDes.indexOf(condition) >= 0;
	}

	@Override
	public UfoSimpleObject getSelectedFuncCategory() {
		return null;
	}

	/**
	 * 带查询按钮的文本框
	 * 
	 * @author wangyga
	 */
	private class FindTextField extends JTextField {
		private static final long serialVersionUID = 1L;

		private JButton button = new JButton();

		private int iFindType = 0;

		public FindTextField(int iFindType) {
			super();
			this.iFindType = iFindType;
			setLayout(new BorderLayout());
			button.setPreferredSize(new Dimension(22, 20));
			button.setIcon(ResConst.getImageIcon("reportcore/ref_down.gif"));
			button.addMouseListener(mouseListener);
			button.addMouseMotionListener(mouseListener);
			button.addActionListener(actionListener);
			add(button, BorderLayout.EAST);
			registerKeyboardAction(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					button.doClick();
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), WHEN_FOCUSED);
		}

		private MouseInputAdapter mouseListener = new MouseInputAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (e.getComponent() == button) {
					button.setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					button.setCursor(Cursor.getDefaultCursor());
				}
			}

		};

		private ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				find(getFindType());
			}

		};

		public int getFindType() {
			return iFindType;
		}

		public void setFindType(int findType) {
			iFindType = findType;
		}

	}

}
