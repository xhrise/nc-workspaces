package com.ufsoft.iufo.inputplugin.ufodynarea;

import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JSpinner.DefaultEditor;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UISpinner;

import com.ufida.zior.dialog.ButtonBar;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

public class SelInsRowNumDlg extends UfoDialog {
	private static final long serialVersionUID = -2026235769547992939L;
	
	private JSpinner spinner=null;
	private ButtonBar btnBar=null;
	private JLabel lblNote=null;
	
	private boolean bAddRow=false;
	private int iInputNum=0;
	
	private Action okAction=new OKAction();
	private Action cancelAction=new CancelAction();

	SelInsRowNumDlg(Container parent,boolean bAddRow){
		super(parent);
		this.bAddRow=bAddRow;
		initialize();
		
		createActionMap(getRootPane().getActionMap());
		getInputMap(getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT),JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

	}
	
	public int getInputNum(){
		return iInputNum;
	}
	
	private ActionMap createActionMap(ActionMap map){
		map.put("OKAction", okAction);
		map.put("CancelAction", cancelAction);
		return map;
	}
	
	private InputMap getInputMap(InputMap keyMap,int condition) {
		if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0),"OKAction");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.ALT_MASK),"OKAction");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0),"CancelAction");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.ALT_MASK),"CancelAction");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),"OKAction");
		}
		return keyMap;
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			setResult(ID_CANCEL);
			close();
			return;
		}
		if(e.getKeyCode() == KeyEvent.VK_P && e.getModifiers() == InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK){//InputEvent.ALT_MASK + 
			JOptionPane.showMessageDialog(this, this.getClass().getName());
			return;
		}
		
		if (e.getKeyCode()==KeyEvent.VK_Y && (e.getModifiers()==0 || e.getModifiers()==InputEvent.ALT_MASK)){
			doOK();
			return;
		}
		
		if (e.getKeyCode()==KeyEvent.VK_ENTER){
			doOK();
			return;
		}
		
		if (e.getKeyCode()==KeyEvent.VK_C && (e.getModifiers()==0 || e.getModifiers()==InputEvent.ALT_MASK)){
			doCancel();
			return;
		}
	}
	
	private void initialize(){
		setTitle(StringResource.getStringResource("uiufotask00070"));
		setSize(230, 130);
		setLayout(null);
		setResizable(false);
		add(getLabelNote());
		add(getSpinner());
		add(getBtnBar());
		setFocusCycleRoot(true);
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				JTextField textField = ((DefaultEditor)spinner.getEditor()).getTextField();
				textField.selectAll();
				textField.requestFocus();
			}
		});
	}
	
	private JSpinner getSpinner(){
		if (spinner==null){
			spinner=new UISpinner(new SpinnerNumberModel(5,1,500,1));
			spinner.setBounds(110, 10, 90, 25);
			
			final JTextField textField = ((DefaultEditor)spinner.getEditor()).getTextField();
			textField.addFocusListener(new FocusAdapter(){
				public void focusLost(FocusEvent e) {
					if (e.getOppositeComponent()==btnBar.getButton("CancelButton"))
						return;
					
					String value = textField.getText();
					try{
						int intValue = Integer.parseInt(value);
						if(intValue < 1 || intValue > 500){
							throw new IllegalArgumentException();
						}
					}catch (Exception ee) {
						UfoPublic.sendWarningMessage(StringResource.getStringResource("miufotasknew00128", new String[]{"1","500"}),SelInsRowNumDlg.this);
						SwingUtilities.invokeLater(new Runnable(){
							public void run() {
								JTextField textField = ((DefaultEditor)spinner.getEditor()).getTextField();
								textField.requestFocus();
							}
						});
					}
				}				
			});
		}
		return spinner;
	}
	
	private ButtonBar getBtnBar(){
		if (btnBar==null){
			btnBar=new ButtonBar();
			
			btnBar.addButton("OKButton", "    "+MultiLang.getString("ok")+"(Y)    ", ButtonBar.RIGHT, 9);//确定
			JButton btnOK = btnBar.getButton("OKButton");
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					doOK();
				}
			});
			
			btnBar.addButton("CancelButton", "    "+StringResource.getStringResource("miufo1003315")+"(C)    ", ButtonBar.RIGHT, 10);//确定
			JButton btnCancel = btnBar.getButton("CancelButton");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setResult(ID_CANCEL);
					close();
				}
			});
			
			btnBar.setBounds(0,50,200,45);
			
			btnBar.setBarInsets(new Insets(18,3,3,3));
		}
		return btnBar;
	}
	
	private JLabel getLabelNote(){
		if (lblNote==null){
			lblNote=new UILabel();
			lblNote.setText(StringResource.getStringResource("uiufotask00070"));
			lblNote.setBounds(10,10, 100,25);
		}
		return lblNote;
	}
	
	private void doOK(){
		String strNum=((DefaultEditor)spinner.getEditor()).getTextField().getText();
		int iNum=0;
		try{
			iNum=Integer.parseInt(strNum);
		}catch(Exception te){}
		
		if (bAddRow && (iNum<=0 || iNum>500)){
			JOptionPane.showConfirmDialog(SelInsRowNumDlg.this,StringResource.getStringResource("uiufotask00071"),UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
			return;
		}else if (!bAddRow && iNum<=0){
			JOptionPane.showConfirmDialog(SelInsRowNumDlg.this,StringResource.getStringResource("uiufotask00072"),UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
			return;
		}
		iInputNum=iNum;
		setResult(ID_OK);
		close();
	}
	
	private void doCancel(){
		setResult(ID_CANCEL);
		close();
	}
	
	class OKAction extends AbstractAction{
		private static final long serialVersionUID = 9192656109248239249L;

		public void actionPerformed(ActionEvent e) {
			doOK();
		}
	}
	
	class CancelAction extends AbstractAction{
		private static final long serialVersionUID = -5528749343013361959L;

		public void actionPerformed(ActionEvent e) {
			doCancel();
		}
	}
}
