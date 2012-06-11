package com.ufsoft.report.sysplugin.excel;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UITextField;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.sysplugin.xml.ExtNameFileFilter;
import com.ufsoft.report.util.MultiLang;

/**
 * 倒入Excel文件选取器
 * 
 * @author guogang
 * 
 */
public class ExcelFileChooserDialog extends UfoDialog {
	private static final long serialVersionUID = 1L;
	private Container m_Report;
	private int state;
	private File selectedFile;
	private JFileChooser excelFileChooser;
	private JTextField selectedFileName;
	private JButton selectedButton;
	private JCheckBox checkAutoCal;
	private JButton importButton;
	private JButton cancelButton;

	/**
	 * @i18n report00001=选择Excel数据文件
	 */
	public ExcelFileChooserDialog(Container report) {
		super(report, "", true);
		m_Report = report;
		setTitle(MultiLang.getString("report00001"));
		initialize();
	}

	/**
	 * xulm 2009-08-25 调整界面显示
	 */
	public void initialize() {
		setSize(400, 160);
		getContentPane().setLayout(null);
		if (excelFileChooser == null) {
			excelFileChooser = new UIFileChooser();
			ExtNameFileFilter xf = new ExtNameFileFilter("xls");
			excelFileChooser.setFileFilter(xf);
			excelFileChooser.setMultiSelectionEnabled(false);
		}
		
		if (selectedFileName == null) {
			selectedFileName = new UITextField();
			selectedFileName.setBounds(20, 20, 280, 20);
			selectedFileName.setEditable(false);
			getContentPane().add(selectedFileName);
		}
		
		if (selectedButton == null) {
			//report00003=浏览..
			selectedButton = new UIButton(MultiLang.getString("report00003"));
			selectedButton.setBounds(305, 20, 75, 20);
			getContentPane().add(selectedButton);
			selectedButton.addKeyListener(this);
			selectedButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					state = excelFileChooser.showOpenDialog(m_Report);
					selectedFile = excelFileChooser.getSelectedFile();
					if (selectedFile != null
							&& state == JFileChooser.APPROVE_OPTION) {
						selectedFileName.setText(selectedFile.getPath());
						SwingUtilities.invokeLater(new Runnable(){
							public void run() {
								importButton.requestFocusInWindow();
							}
						});
					}
				}
			});
		}
		if (checkAutoCal == null) {
			//report00002=导入后是否计算
			checkAutoCal = new UICheckBox(MultiLang.getString("report00002"));
			checkAutoCal.setBounds(20, 50, 120, 20);
			getContentPane().add(checkAutoCal);
			checkAutoCal.addKeyListener(this);
		}
		if (importButton == null)
			importButton = new UIButton();
		    importButton.setText( "    "+MultiLang.getString("ok")+"(Y)    ");
		    importButton.setBounds(225, 100, 75, 20);
		    getContentPane().add(importButton);
		    importButton.addKeyListener(this);
		    importButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doOK();
				}
			});
		    
		
		if (cancelButton == null)
		{
			cancelButton = new UIButton();
		    cancelButton.setText( "    "+StringResource.getStringResource("miufo1003315")+"(C)    ");
		    cancelButton.setBounds(305,100, 75, 20);
		    getContentPane().add(cancelButton);
		    cancelButton.addKeyListener(this);
		    cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doCancel();
				}
			});
		}
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				selectedButton.requestFocusInWindow();
			}
		});
	}
	
	
	/**
	 * xulm 2009-08-25 增加界面确定和取消按钮的快捷方式
	 */
	@Override
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
		
		if (e.getKeyCode()==KeyEvent.VK_C && (e.getModifiers()==0 || e.getModifiers()==InputEvent.ALT_MASK)){
			doCancel();
			return;
		}
	}
	
	/**
	 * 确定按钮的处理
	 */
	private void doOK(){
        if(selectedFile == null){
        	JOptionPane.showMessageDialog(this, StringResource.getStringResource("uiufotask00080"));
        	return;
        }
		setResult(ID_OK);
		close();
	}
	
	/**
	 * 取消按钮的处理
	 */
	private void doCancel(){
		setResult(ID_CANCEL);
		close();
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public boolean isAutoCal() {
		boolean isAutoCal = false;
		if (checkAutoCal != null) {
			isAutoCal = checkAutoCal.isSelected();
		}
		return isAutoCal;
	}

	public static void main(String[] args) {
		ExcelFileChooserDialog dialog = new ExcelFileChooserDialog(null);
		dialog.setVisible(true);
	}
	
}
