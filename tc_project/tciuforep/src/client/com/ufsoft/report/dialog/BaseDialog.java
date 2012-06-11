/* Generated by Together */

package com.ufsoft.report.dialog;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import nc.ui.pub.beans.UIPanel;

import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;
/**
 * <br>
 * BaseDialog类提供一个对话框的的通用框架.</br><br>
 * 在JDialog的基础上将对话框面板划分为内容面板和按钮栏,内容面板容纳对话框内容组件,
 *  按钮栏容纳按钮组件,用户可以在构造函数中指定自己的内容面板.
 * <b>面板替换</b>可以同构构造函数传入需要在内容区域显示的面板；或者在子类中重载createDialogPane方法。
 * <b>键盘响应</b>ESC键响应cancelDialog()方法</br>
 * 
 * @author caijie
 */
public class BaseDialog extends UfoDialog   {

	/**
	 * 返回值当cancel按钮被按下
	 */
	public static final int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;

	/**
	 * 返回值当ok按钮被按下
	 */
	public static final int OK_OPTION = JOptionPane.OK_OPTION;

	/**
	 * 返回值当"否"按钮被按下
	 */
	public static final int NO_OPTION = JOptionPane.NO_OPTION;

	/** 按钮"是"被按下 */
	public static final int YES_OPTION = 0;

	/**
	 * 记录用户的点击的按钮,默认为没有按钮被按下
	 */
	private int m_iSelectOption = CANCEL_OPTION;
	/**
	 * 对话框的内容面板,存放对话框的内容
	 */
	private JPanel m_pnlDialogArea = null;
	/**
	 * 按钮栏
	 */
	private ButtonBar m_buttonBar = null;
	/**
	 * 取消按钮
	 */
	private JButton m_btnCancel = null;

	/**
	 * 确定按钮
	 */
	private JButton m_btnOK = null;

	/***************************************************************************
	 * 内容面板边框的宽度
	 */
	public final int PANEL_BORDER_WIDTH = 30;

	/**
	 * 构造函数 创建并且初始化内容面板和按钮面板
	 */
	public BaseDialog(Component parent) {
		this(parent, null, true);
	}
	
	/**
	 * 构造函数 创建并且初始化内容面板和按钮面板
	 * 
	 * @param parentComponent
	 *            Component 父窗口
	 * @param title
	 *            String 对话框标题
	 * @param modal
	 *            boolean 模式对话框/非模式对话框
	 */
	public BaseDialog(Component parentComponent, String title, boolean modal) {
		super(JOptionPane.getFrameForComponent(parentComponent), title);
		init();
	}

	private void init() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		JPanel rootPanel = new UIPanel();
		rootPanel.setBorder(BorderFactory.createEmptyBorder(PANEL_BORDER_WIDTH,
				PANEL_BORDER_WIDTH, PANEL_BORDER_WIDTH, PANEL_BORDER_WIDTH));
		contentPane.add(rootPanel, BorderLayout.CENTER);
		
		if (m_pnlDialogArea == null)
			m_pnlDialogArea = createDialogPane();
//		m_pnlDialogArea.setBorder(BorderFactory.createEmptyBorder(PANEL_BORDER_WIDTH,
//				PANEL_BORDER_WIDTH, PANEL_BORDER_WIDTH, PANEL_BORDER_WIDTH));
		rootPanel.setLayout(new BorderLayout());
		rootPanel.add(m_pnlDialogArea,BorderLayout.CENTER);
		m_buttonBar = createButtonBar();
		rootPanel.add(m_buttonBar, BorderLayout.SOUTH);
		initDialogListener();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		addHelp();

	}

	/**
	 * 初始化按钮栏 在按钮栏中添加ok,cancel按钮和注册ESC键
	 * 
	 * @return ButtonBar
	 */
	protected ButtonBar createButtonBar() {
		ButtonBar bar = new ButtonBar();
		//添加OK按钮
		addOKButton(bar);

		//添加cancel按钮
		addCancelButton(bar);

		getRootPane().setDefaultButton(m_btnOK);
		return bar;
	}

	/**
	 * 在按钮栏右边增加执行finishDialog()方法的ok按钮
	 */
	private void addOKButton(ButtonBar bar) {
		if (bar != null) {
			bar.addButton("OKButton", MultiLang.getString("uiuforep0000704"), ButtonBar.RIGHT, 9);//确定
			m_btnOK = bar.getButton("OKButton");

			m_btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					finishDialog();
//					EventQueue queue = Toolkit.
				}
			});
		}
	}

	/**
	 * 在按钮栏右边增加执行cancelDialog()方法的cancel按钮
	 */
	private void addCancelButton(ButtonBar bar) {
		if (bar != null) {
			bar.addButton("CancelButton", MultiLang.getString("uiuforep0000705"), ButtonBar.RIGHT, 10);//"取消"
			m_btnCancel = bar.getButton("CancelButton");
			ActionListener lis = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancelDialog();
				}
			};
			m_btnCancel.addActionListener(lis);
		}
	}

	/**
	 * 注册ESC键，当用户按ESC键时执行cancelDialog()方法退出对话框
	 */
	private void whenKeyEscPressed() {
		ActionListener keyESCListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelDialog();
			}
		};

		m_pnlDialogArea.registerKeyboardAction(keyESCListener, KeyStroke
				.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	/**
	 * 返回按钮面板
	 * 
	 * @return ButtonBar
	 */
	public ButtonBar getButtonBar() {
		return this.m_buttonBar;
	}

	/**
	 * 返回对话框内容面板
	 * 
	 * @return JPanel
	 */
	public JPanel getDialogArea() {
		return m_pnlDialogArea;
	}

	/**
	 * 完成对话框，继承类中应该覆写此方法
	 */
	protected void finishDialog() {
		setSelectOption(OK_OPTION);
		dispose();
	}
	/**
	 * 取消对话框，继承类中应该覆写此方法
	 */
	protected void cancelDialog() {
		setSelectOption(CANCEL_OPTION);
		dispose();
	}

	/**
	 * 获取OK按钮
	 * 
	 * @return JButton
	 */
	protected JButton getOKButton() {
		return getButtonBar().getButton("OKButton");
	}

	/**
	 * 获取cancel按钮
	 * 
	 * @return JButton
	 */
	protected JButton getCancelButton() {
		return getButtonBar().getButton("CancelButton");
	}

	/**
	 * 返回用户的选择值
	 * 
	 * @return int 用户的选择值
	 */
	public int getSelectOption() {
		return m_iSelectOption;
	}
	/**
	 * 设置选择值 创建时间2004-8-17 14:58:58
	 * @param selectionOption，参考当前类的静态常量.
	 *  
	 */
	protected void setSelectOption(int selectionOption) {
		m_iSelectOption = selectionOption;
	}
	/**
	 * 创建内容面板
	 * 在内容面板的四周加空白边框,边框的宽度由PANEL_BORDER_WIDTH指定
	 * @return JPanel
	 */
	protected JPanel createDialogPane() {
		return new UIPanel();
	}
	/**
	 * 初始化监听器 caijie 2004-10-29
	 *  
	 */
	private void initDialogListener() {
		whenKeyEscPressed();
	}
	/**
	 * 返回对话框对应的帮助文档的标记。
	 * 
	 * @return 如果希望当前对话框响应帮助热键，该值对应帮助文档标记。为空标识不响应。
	 */
	public String getHelpMark() {
		return null;
	}
	/**
	 * 添加帮助。
	 */
	private void addHelp() {
		javax.help.HelpBroker hb = ResConst.getHelpBroker();
		if (hb != null) {
			String strHelpMark = getHelpMark();
			if (strHelpMark != null) {
				hb.enableHelpKey(getContentPane(), strHelpMark, null);
			}
		}
	}

}

