package com.ufsoft.iufo.inputplugin.biz.file;

import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIPasswordField;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

public class DatasourceSetDlg extends UfoDialog {

	private static final long serialVersionUID = 1L;

	private UIPanel jContentPane = null;

	private UIPanel mainPanel = null;

	private UIPanel cmdPanel = null;

	private UIButton okButton = null;

	private UIButton cancleButton = null;

	private UILabel loginUnitLabel = null;

	private UILabel userLabel = null;

	private UILabel passwordLabel = null;

	private UITextField unitTextField = null;

	private UITextField userTextField = null;

	private UIPasswordField passwordTextField = null;

	private JSeparator jSeparator = null;

	/**
	 * @param owner
	 */
	public DatasourceSetDlg(JComponent owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setTitle(StringResource.getStringResource("miufo1002840"));
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.UIPanel
	 */
	private UIPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getMainPanel(), null);
			jContentPane.add(getJSeparator(), null);
			jContentPane.add(getCmdPanel(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes mainPanel	
	 * 	
	 * @return javax.swing.UIPanel	
	 * @i18n repinput00003=用户口令
	 * @i18n repinput00004=用户编码
	 * @i18n repinput00005=登陆单位
	 */
	private UIPanel getMainPanel() {
		if (mainPanel == null) {
			passwordLabel = new UILabel();
			passwordLabel.setBounds(new Rectangle(69, 94, 57, 22));
			passwordLabel.setText(MultiLangInput.getString("repinput00003"));
			userLabel = new UILabel();
			userLabel.setBounds(new Rectangle(69, 53, 57, 22));
			userLabel.setText(MultiLangInput.getString("repinput00004"));
			loginUnitLabel = new UILabel();
			loginUnitLabel.setBounds(new Rectangle(69, 12, 57, 22));
			loginUnitLabel.setHorizontalTextPosition(SwingConstants.LEADING);
			loginUnitLabel.setText(MultiLangInput.getString("repinput00005"));
			mainPanel = new UIPanel();
			mainPanel.setLayout(null);
			mainPanel.setBounds(new Rectangle(0, 0, 292, 120));
			mainPanel.add(loginUnitLabel, null);
			mainPanel.add(userLabel, null);
			mainPanel.add(passwordLabel, null);
			mainPanel.add(getUnitTextField(), null);
			mainPanel.add(getUserTextField(), null);
			mainPanel.add(getPasswordTextField(), null);
		}
		return mainPanel;
	}

	/**
	 * This method initializes cmdPanel	
	 * 	
	 * @return javax.swing.UIPanel	
	 */
	private UIPanel getCmdPanel() {
		if (cmdPanel == null) {
			cmdPanel = new UIPanel();
			cmdPanel.setLayout(null);
			cmdPanel.setBounds(new Rectangle(0, 135, 292, 38));
			cmdPanel.add(getCancleButton(), null);
			cmdPanel.add(getOkButton(), null);
		}
		return cmdPanel;
	}

	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.UIButton	
	 * @i18n uiufotableinput0013=确定
	 */
	private UIButton getOkButton() {
		if (okButton == null) {
			okButton = new UIButton();
			okButton.setText(MultiLangInput.getString("uiufotableinput0013"));
			okButton.setBounds(new Rectangle(85, 8, 60, 22));
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setResult(ID_OK);
					setVisible(false);
				}
			});
		}
		return okButton;
	}

	/**
	 * This method initializes cancleButton	
	 * 	
	 * @return javax.swing.UIButton	
	 * @i18n uiufotableinput0014=取消
	 */
	private UIButton getCancleButton() {
		if (cancleButton == null) {
			cancleButton = new UIButton();
			cancleButton.setText(MultiLangInput.getString("uiufotableinput0014"));
			cancleButton.setBounds(new Rectangle(158, 8, 60, 22));
			cancleButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setResult(ID_CANCEL);
					close();
				}
			});
		}
		return cancleButton;
	}

	/**
	 * This method initializes unitTextField	
	 * 	
	 * @return javax.swing.UITextField	
	 */
	private UITextField getUnitTextField() {
		if (unitTextField == null) {
			unitTextField = new UITextField();
			unitTextField.setBounds(new Rectangle(129, 12, 99, 22));
		}
		return unitTextField;
	}

	/**
	 * This method initializes userTextField	
	 * 	
	 * @return javax.swing.UITextField	
	 */
	private UITextField getUserTextField() {
		if (userTextField == null) {
			userTextField = new UITextField();
			userTextField.setBounds(new Rectangle(129, 53, 99, 22));
		}
		return userTextField;
	}

	/**
	 * This method initializes passwordTextField	
	 * 	
	 * @return javax.swing.UITextField	
	 */
	private UIPasswordField getPasswordTextField() {
		if (passwordTextField == null) {
			passwordTextField = new UIPasswordField();
			passwordTextField.setBounds(new Rectangle(129, 94, 99, 22));
		}
		return passwordTextField;
	}
	public void setUnit(String unit){
		getUnitTextField().setText(unit);
	}
	public void setUser(String user){
		getUserTextField().setText(user);
	}
	public void setPassword(String passWord){
		getPasswordTextField().setText(passWord);
	}
	public String getUnit(){
		return getUnitTextField().getText();
	}
	public String getUser(){
		return getUserTextField().getText();
	}
	public String getPassword(){
		return getPasswordTextField().getText();
	}
	/**
	 * This method initializes jSeparator	
	 * 	
	 * @return javax.swing.JSeparator	
	 */
	private JSeparator getJSeparator() {
		if (jSeparator == null) {
			jSeparator = new JSeparator();
			jSeparator.setBounds(new Rectangle(10, 127, 263, 8));
		}
		return jSeparator;
	}

	public static void main(String[] args) {
		new DatasourceSetDlg(null).setVisible(true);
	}
}
