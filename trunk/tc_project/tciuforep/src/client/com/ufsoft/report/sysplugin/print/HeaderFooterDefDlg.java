package com.ufsoft.report.sysplugin.print;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;

import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.dialog.fontchooser.JFontChooser;
import com.ufsoft.report.util.MultiLang;

public class HeaderFooterDefDlg extends UfoDialog {
	private static final long serialVersionUID = -1142739628897015818L;
	private JPanel jContentPane = null;
	private JButton fontSetButton = null;
	private JButton pageButton = null;
	private JButton totalPageButton = null;
	private JButton insertImageButton = null;
	private JButton sysDataButton = null;
	private JButton sysTimeButton = null;
	private JButton okButton = null;
	private JButton cancleButton = null;
	private JTextArea leftTextArea = null;
	private JTextArea middleTextArea = null;
	private JTextArea rightTextArea = null;
	private JLabel leftLabel = null;
	private JLabel middleLabel = null;
	private JLabel rightLabel = null;
	private HeaderFooterSegmentModel _leftModel;
	private HeaderFooterSegmentModel _middleModel;
	private HeaderFooterSegmentModel _rightModel;
	/**当前处于活动状态的TextArea*/
	private JTextArea _focusTextArea;

	/**
	 * This is the default constructor
	 * @param parent 
	 * @param model3 
	 * @param model2 
	 * @param model 
	 */
	public HeaderFooterDefDlg(Container parent, HeaderFooterSegmentModel leftModel, HeaderFooterSegmentModel middleModel, HeaderFooterSegmentModel rightModel) {
		super(parent);
		_leftModel = leftModel==null?new HeaderFooterSegmentModel():(HeaderFooterSegmentModel) leftModel.clone();
		_middleModel = middleModel==null?new HeaderFooterSegmentModel():(HeaderFooterSegmentModel) middleModel.clone();
		_rightModel = rightModel==null?new HeaderFooterSegmentModel():(HeaderFooterSegmentModel) rightModel.clone();
		getLeftTextArea().setText( _leftModel.getValue());
		getMiddleTextArea().setText(_middleModel.getValue());
		getRightTextArea().setText(_rightModel.getValue());
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(700, 224);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 * @i18n report00038=右(R):
	 * @i18n report00039=中(C):
	 * @i18n report00040=左(L):
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			rightLabel = new com.ufsoft.table.beans.UFOLabel();
			rightLabel.setBounds(465, 72, 66, 25);
			rightLabel.setText(MultiLang.getString("report00038"));
			middleLabel = new com.ufsoft.table.beans.UFOLabel();
			middleLabel.setBounds(241, 72, 66, 25);
			middleLabel.setText(MultiLang.getString("report00039"));
			leftLabel = new com.ufsoft.table.beans.UFOLabel();
			leftLabel.setBounds(14, 72, 66, 25);
			leftLabel.setText(MultiLang.getString("report00040"));
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getFontSetButton(), null);
			jContentPane.add(getJButton(), null);			
			jContentPane.add(getJButton1(), null);
			jContentPane.add(getJButton2(), null);
			jContentPane.add(getJButton3(), null);
			jContentPane.add(getJButton4(), null);
			jContentPane.add(getJButton5(), null);
			jContentPane.add(getJButton6(), null);
			this.setFocusable(false);
			getFontSetButton().setFocusable(false);
			getJButton().setFocusable(false);
			getJButton1().setFocusable(false);
			getJButton2().setFocusable(false);
			getJButton3().setFocusable(false);
			getJButton4().setFocusable(false);
			getJButton5().setFocusable(false);
			getJButton6().setFocusable(false);
//			jContentPane.add(getLeftTextArea(), null);
//			jContentPane.add(getMiddleTextArea(), null);
//			jContentPane.add(getRightTextArea(), null);
			JScrollPane leftPane = new UIScrollPane();
			leftPane.setBounds(getLeftTextArea().getBounds());
			leftPane.setViewportView(getLeftTextArea());
			jContentPane.add(leftPane,null);
			JScrollPane middlePane = new UIScrollPane();
			middlePane.setBounds(getMiddleTextArea().getBounds());
			middlePane.setViewportView(getMiddleTextArea());
			jContentPane.add(middlePane,null);
			JScrollPane rightPane = new UIScrollPane();
			rightPane.setBounds(getRightTextArea().getBounds());
			rightPane.setViewportView(getRightTextArea());
			jContentPane.add(rightPane,null);
			jContentPane.add(leftLabel, null);
			jContentPane.add(middleLabel, null);
			jContentPane.add(rightLabel, null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes fontSetButton	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n report00042=字体设置
	 */    
	private JButton getFontSetButton() {
		if (fontSetButton == null) {
			fontSetButton = new nc.ui.pub.beans.UIButton();
			fontSetButton.setBounds(10, 32, 75, 22);
			fontSetButton.addActionListener(new java.awt.event.ActionListener() { 
				/**
				 * @i18n report00041=选择字体
				 */
				public void actionPerformed(java.awt.event.ActionEvent e) {  
				    Font selectedFont = JFontChooser.showDialog(HeaderFooterDefDlg.this,MultiLang.getString("report00041"), getFocusModel().getFont());
					if(selectedFont != null){
						getFocusModel().setFont(selectedFont);
					}
				}
			});
			fontSetButton.setText(MultiLang.getString("report00042"));
		}
		return fontSetButton;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n report00043=页码
	 */    
	private JButton getJButton() {
		if (pageButton == null) {
			pageButton = new nc.ui.pub.beans.UIButton();
			pageButton.setBounds(122, 32, 75, 22);
			pageButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					insertToFocusTextArea(HeaderFooterSegmentComp.PAGENUMBER_TEXT);
				}
			});
			pageButton.setText(MultiLang.getString("report00043"));
		}
		return pageButton;
	}

	private void insertToFocusTextArea(String insertText) {
		JTextArea focusTextArea = getFocusTextArea();
		focusTextArea.insert(insertText,focusTextArea.getCaretPosition());		
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n report00044=总页数
	 */    
	private JButton getJButton1() {
		if (totalPageButton == null) {
			totalPageButton = new nc.ui.pub.beans.UIButton();
			totalPageButton.setBounds(200, 32, 75, 22);
			totalPageButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					insertToFocusTextArea(HeaderFooterSegmentComp.TOTALPAGENUMBER_TEXT);
				}
			});
			totalPageButton.setText(MultiLang.getString("report00044"));
		}
		return totalPageButton;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n report00045=插入图片
	 */    
	private JButton getJButton2() {
		if (insertImageButton == null) {
			insertImageButton = new nc.ui.pub.beans.UIButton();
			insertImageButton.setBounds(288, 32, 75, 22);
			insertImageButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {  
					//选择图片
					JFileChooser chooser = new UIFileChooser();
					FileFilter xf = new FileFilter(){
						private String[] acceptTypes = new String[]{"jpg","jpeg","gif","png"};
						public boolean accept(File f) {
							if (f.isDirectory())
								return true;
							String extension = null;
							String name = f.getName();
							int pos = name.lastIndexOf('.');
							if (pos > 0 && pos < name.length() - 1)
								extension = name.substring(pos + 1);
							
							for(int i=0;extension != null&&i<acceptTypes.length;i++){
								if(acceptTypes[i].equalsIgnoreCase(extension))
									return true;
							}
							return false;
						}
						public String getDescription() {
							StringBuffer buffer = new StringBuffer();
							for(int i=0;i<acceptTypes.length;i++){
								buffer.append("*.").append(acceptTypes[i]).append(";");
							}
							return buffer.toString();
						}						
					};
					chooser.setFileFilter(xf);
					chooser.setMultiSelectionEnabled(false);
					int returnVal = chooser.showOpenDialog(HeaderFooterDefDlg.this);
					if(returnVal == JFileChooser.APPROVE_OPTION){
						File file = chooser.getSelectedFile();
						try {
							ImageIcon image = new ImageIcon(file.toURL());
							getFocusModel().setImage(image);
						} catch (MalformedURLException e1) {
							throw new RuntimeException(e1);
						}
						//插入图片标识
						insertToFocusTextArea(HeaderFooterSegmentComp.IMAGE_TEXT);
					}
				}
			});
			insertImageButton.setText(MultiLang.getString("report00045"));
		}
		return insertImageButton;
	}

	/**
	 * This method initializes jButton3	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n report00046=系统日期
	 */    
	private JButton getJButton3() {
		if (sysDataButton == null) {
			sysDataButton = new nc.ui.pub.beans.UIButton();
			sysDataButton.setBounds(396, 32, 75, 22);
			sysDataButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					insertToFocusTextArea(HeaderFooterSegmentComp.DATE_TEXT);
				}
			});
			sysDataButton.setText(MultiLang.getString("report00046"));
		}
		return sysDataButton;
	}

	/**
	 * This method initializes jButton4	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n report00047=系统时间
	 */    
	private JButton getJButton4() {
		if (sysTimeButton == null) {
			sysTimeButton = new nc.ui.pub.beans.UIButton();
			sysTimeButton.setBounds(498, 32, 75, 22);
			sysTimeButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					insertToFocusTextArea(HeaderFooterSegmentComp.TIME_TEXT);
				}
			});
			sysTimeButton.setText(MultiLang.getString("report00047"));
		}
		return sysTimeButton;
	}

	/**
	 * This method initializes jButton5	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton5() {
		if (okButton == null) {
			okButton = createOkButton();
			okButton.setBounds(600, 11, 75, 22);
		}
		return okButton;
	}

	/**
	 * This method initializes jButton6	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton6() {
		if (cancleButton == null) {
			cancleButton = createCancleButton();
			cancleButton.setBounds(602, 57, 75, 22);
		}
		return cancleButton;
	}

	/**
	 * This method initializes leftTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	private JTextArea getLeftTextArea() {
		if (leftTextArea == null) {
			leftTextArea = new UITextArea();
			leftTextArea.setBounds(15, 102, 207, 81);
			leftTextArea.setLineWrap(true);
			addFocusListenerTo(leftTextArea);
		}
		return leftTextArea;
	}
	private void addFocusListenerTo(final JTextArea textArea){
		textArea.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e) {
				_focusTextArea = textArea;
			}			
		});
	}
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	private JTextArea getMiddleTextArea() {
		if (middleTextArea == null) {
			middleTextArea = new UITextArea();
			middleTextArea.setBounds(242, 102, 207, 81);
			middleTextArea.setLineWrap(true);
			addFocusListenerTo(middleTextArea);
		}
		return middleTextArea;
	}

	/**
	 * This method initializes jTextArea1	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	private JTextArea getRightTextArea() {
		if (rightTextArea == null) {
			rightTextArea = new UITextArea();
			rightTextArea.setBounds(466, 102, 207, 81);
			rightTextArea.setLineWrap(true);
			addFocusListenerTo(rightTextArea);
		}
		return rightTextArea;
	}
	private JTextArea getFocusTextArea(){
		return _focusTextArea;
//		requestFocus();
//		Component focusComp = getFocusOwner();
//		if(focusComp == getLeftTextArea() || focusComp == getMiddleTextArea() || focusComp == getRightTextArea()){
//			return (JTextArea)focusComp;
//		}else{
//			throw new RuntimeException("焦点组件不对:"+focusComp);
//		}
	}
	private HeaderFooterSegmentModel getFocusModel(){
		JTextArea focusTextArea = getFocusTextArea();
		if(focusTextArea == getLeftTextArea()){
			return _leftModel;
		}else if(focusTextArea == getMiddleTextArea()){
			return _middleModel;
		}else if(focusTextArea == getRightTextArea()){
			return _rightModel;
		}else{
			throw new IllegalArgumentException();
		}
	}
	public HeaderFooterSegmentModel[] getSegmentModels() {
		_leftModel.setValue(getLeftTextArea().getText());
		_middleModel.setValue(getMiddleTextArea().getText());
		_rightModel.setValue(getRightTextArea().getText());
		return new HeaderFooterSegmentModel[]{_leftModel,_middleModel,_rightModel};
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
  