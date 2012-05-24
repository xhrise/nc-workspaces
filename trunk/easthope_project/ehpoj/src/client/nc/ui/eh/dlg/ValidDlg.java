package nc.ui.eh.dlg;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.border.Border;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIPasswordField;
import nc.ui.pub.beans.UITextField;

/**
 * 消息筛选条件设置对话框
 * 
 * @author huangzg 2006-5-23
 */
public class ValidDlg extends UIDialog implements ActionListener {

	private UIButton btnCancel = null;

	private UIButton btnOK = null;

	private UIPanel btnPanel = null;

	private UIPanel contentPanel = null;

//	private MessageFilter filter = null;

	private UIPanel filterPanel = null;

	private String title = null;

//	private UIRefPane corpRef = null;
//
//	private UIComboBox typeComb = null;
//
//	private UIRefPane timeRef = null;
	
	private UITextField txtUser = null;
	
	private JPasswordField txtPass  = null;

	public static void main(String[] args) {
		ValidDlg dlg = new ValidDlg(null);
		dlg.show();
	}

	public ValidDlg(Container parent ) {
		super(parent);

		init();

	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getBtnCancel()) {
			this.dispose();
			this.closeCancel();
		} else if (e.getSource() == getBtnOK()) {
			if (validateInput()) {
				this.dispose();
				this.closeOK();
			}
		}
	}

	private UIButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel.setName("m_btnCancel");
			btnCancel.setText("取消");
		}
		return btnCancel;
	}

	private UIButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton();
			btnOK.setName("m_btnOK");
			btnOK.setText("确定");
		}
		return btnOK;
	}

	private Border getBorderByTitle(String title) {
		Border etched = BorderFactory.createEtchedBorder();
		return BorderFactory.createTitledBorder(etched, title);
	}

	private UIPanel getBtnPanel() {
		if (btnPanel == null) {
			btnPanel = new UIPanel();
			btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
			btnPanel.add(Box.createHorizontalGlue());
			btnPanel.add(getBtnOK());
			btnPanel.add(getBtnCancel());
			btnPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		}
		return btnPanel;
	}

	private UIPanel getFilterPanel() {
		if (filterPanel == null) {
			filterPanel = new UIPanel();

			Box hbox1 = Box.createHorizontalBox();
			hbox1.add(Box.createHorizontalStrut(5));
			hbox1.add(new UILabel("登录名"));
			hbox1.add(Box.createHorizontalStrut(5));
			hbox1.add(getTxtUser());

			Box hbox2 = Box.createHorizontalBox();
			hbox2.add(Box.createHorizontalStrut(5));
			hbox2.add(new UILabel("密    码"));
			hbox2.add(Box.createHorizontalStrut(5));
			hbox2.add(getTxtPass());

//			Box hbox3 = Box.createHorizontalBox();
//			hbox3.add(Box.createHorizontalStrut(5));
//			hbox3.add(new UILabel("CA认证"));
//			hbox3.add(Box.createHorizontalStrut(5));
//			hbox3.add(getTxtValid());
			

			Box vbox = Box.createVerticalBox();

			vbox.add(Box.createVerticalStrut(10));
			vbox.add(hbox1);
			vbox.add(Box.createVerticalStrut(10));
			vbox.add(hbox2);
//			vbox.add(Box.createVerticalStrut(10));
//			vbox.add(hbox3);

			filterPanel.add(vbox);

		}
		return filterPanel;
	}

	/**
	 * 得到最终的界面选择结果
	 * 
	 * @return 一个MessageFilter格式
	 */
//	public MessageFilter getResultFilter() {
//
//		return filter;
//	}

	private boolean isLegal(String string) {
		if (string != null && string.trim().length() > 0)
			return true;
		return false;
	}

	private UIPanel getUIDialogContentPane() {
		if (contentPanel == null) {
			contentPanel = new UIPanel();
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(getFilterPanel(), BorderLayout.WEST);
			contentPanel.add(getBtnPanel(), BorderLayout.SOUTH);
		}
		return contentPanel;
	}

	private void init() {
		initUI();
		initListener();

	}

	private void initListener() {
		getBtnOK().addActionListener(this);
		getBtnCancel().addActionListener(this);

	}

	/**
	 * 初始化状态
	 */
	public void initStates() {

	}

	private void setFieldStatus() {

	}

	private void initUI() {
		setName("Message filter config");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(230, 180);
		setResizable(true);
//		setTitle("");
//		this.getContentPane().setLayout(new BorderLayout());
		setContentPane(getUIDialogContentPane());
		initStates();
	}

	/**
	 * 恢复默认值
	 * 
	 * @return
	 */
	public void restoreDefaultValue() {

		setFieldStatus();
	}

	private boolean validateInput() {

		if(getTxtUser().getText() == null || "".equals(getTxtUser().getText())){
			JOptionPane.showMessageDialog(null, "登录名不能为空！" , "提示" , JOptionPane.OK_OPTION);
			
			return false;
		}
		
		if(new String(getTxtPass().getPassword()) == null || "".equals(new String(getTxtPass().getPassword()))) {
			JOptionPane.showMessageDialog(null, "密码不能为空！" , "提示" , JOptionPane.OK_OPTION);
			
			return false;
		}
		
//		if(new String(getTxtValid().getPassword()) == null || "".equals(new String(getTxtValid().getPassword()))) {
//			JOptionPane.showMessageDialog(null, "密保不能为空！" , "提示" , JOptionPane.OK_OPTION);
//			
//			return false;
//		}
			
		
		return true;
	}

	public UIPanel getContentPanel() {
		if(contentPanel == null) {
			contentPanel = new UIPanel();
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(getFilterPanel(), BorderLayout.CENTER);
			contentPanel.add(getBtnPanel(), BorderLayout.SOUTH);
			
		}
		return contentPanel;
	}

	public void setContentPanel(UIPanel contentPanel) {
		this.contentPanel = contentPanel;
	}

//	public MessageFilter getFilter() {
//		return filter;
//	}
//
//	public void setFilter(MessageFilter filter) {
//		this.filter = filter;
//	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public UITextField getTxtUser() {
		if(txtUser == null) {
			txtUser = new UITextField();
			txtUser.setName("txtUser");
			txtUser.setPreferredSize(new Dimension(100 , 22));
		}
		
		return txtUser;
	}

	public JPasswordField getTxtPass() {
		if(txtPass == null) {
			txtPass = new JPasswordField();
			txtPass.setName("txtPass");
			txtPass.setPreferredSize(new Dimension(100 , 22));
			
		}
		
		return txtPass;
	}

//	public UIPasswordField getTxtValid() {
//		if(txtValid == null) {
//			txtValid = new UIPasswordField();
//			txtValid.setName("txtValid");
//		}
//		
//		return txtValid;
//	}

//	public void setTxtValid(UIPasswordField txtValid) {
//		this.txtValid = txtValid;
//	}
	
	public void setTxtPass(UIPasswordField txtPass) {
		this.txtPass = txtPass;
	}
	
	public void setTxtUser(UITextField txtUser) {
		this.txtUser = txtUser;
	}

	public void setBtnCancel(UIButton btnCancel) {
		this.btnCancel = btnCancel;
	}

	public void setBtnOK(UIButton btnOK) {
		this.btnOK = btnOK;
	}

	public void setBtnPanel(UIPanel btnPanel) {
		this.btnPanel = btnPanel;
	}

	public void setFilterPanel(UIPanel filterPanel) {
		this.filterPanel = filterPanel;
	}
}
