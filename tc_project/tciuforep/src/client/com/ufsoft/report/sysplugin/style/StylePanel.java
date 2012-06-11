/*
 * 创建日期 2004-11-1
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.ufsoft.report.sysplugin.style;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner.DefaultEditor;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UISpinner;

import com.ufsoft.report.ReportStyle;
import com.ufsoft.report.dialog.ColorButton;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
/**
 * @author wupeng
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class StylePanel extends nc.ui.pub.beans.UIPanel {
	private static final long serialVersionUID = -7336616627986411456L;
	private JPanel jPanel = null;
	private JCheckBox cbHindRow = null;
	private JCheckBox cbHindCol = null;
	private JLabel lbFieldColor = null;
	private ColorButton btnColor = null;
	private JLabel lbScale = null;
	private JSpinner tfScale = null;
	private JCheckBox cbShowZero = null;
	private JPanel jPanel1 = null;
	private static final int MIN_SCALE = 30;
	private static final int MAX_SCALE = 300;
	private static final int STEP = 5;
	private JLabel lbMark = null;
	
	private int m_nScale = 100;
	/**
	 * 报表显示风格设置
	 */
	private ReportStyle m_ReportStyle;
	/**
	 * This is the default constructor
	 */
	public StylePanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * @i18n report00023=％
	 */
	private  void initialize() {
		lbScale = new nc.ui.pub.beans.UILabel();
		lbFieldColor = new nc.ui.pub.beans.UILabel();
		lbMark = new nc.ui.pub.beans.UILabel();
		this.setLayout(null);
		this.setSize(346, 208);
		lbFieldColor.setText(MultiLang.getString("miufo1001184"));//网格颜色：
		lbFieldColor.setBounds(7, 62, 73, 22);
		lbScale.setBounds(20, 140, 77, 22);
		lbScale.setText(MultiLang.getString("miufo1001130"));//显示比例
		this.add(getJPanel(), null);
		this.add(lbScale, null);
		this.add(getTfScale(), null);
		this.add(lbMark, null);
		this.add(getJPanel1(), null);
		lbMark.setBounds(180, 140, 27, 25);
		lbMark.setText(MultiLang.getString("report00023"));
	
		
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new UIPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(0, 0, 129, 110);
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(MultiLang.getString("miufo1001186")));
			jPanel.add(getCbHindRow(), null);
			jPanel.add(getCbHindCol(), null);
		}
		return jPanel;
	}
	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getCbHindRow() {
		if (cbHindRow == null) {
			cbHindRow = new UICheckBox();
			cbHindRow.setBounds(16, 29, 99, 21);
			cbHindRow.setText(MultiLang.getString("miufo1001183"));
		}
		return cbHindRow;
	}
	/**
	 * This method initializes jCheckBox1	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getCbHindCol() {
		if (cbHindCol == null) {
			cbHindCol = new UICheckBox();
			cbHindCol.setBounds(16, 65, 99, 21);
			cbHindCol.setText(MultiLang.getString("miufo1001182"));
		}
		return cbHindCol;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnColor() {
		if (btnColor == null) {
			btnColor = new ColorButton(null,this);
			btnColor.setBounds(82, 62, 59, 21);
		}
		return btnColor;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JSpinner getTfScale() {
		if (tfScale == null) {
			SpinnerModel model = new SpinnerNumberModel(m_nScale,MIN_SCALE,MAX_SCALE,STEP);
			tfScale = new UISpinner(model);
			tfScale.setBounds(100, 140, 77, 22);
			final JTextField textField = ((DefaultEditor)tfScale.getEditor()).getTextField();
			textField.addFocusListener(new FocusAdapter(){
				/**
				 * @i18n report00106=显示比例必须输入数字且在30－300之间
				 */
				public void focusLost(FocusEvent e) {
					String value = textField.getText();
					try{
						int intValue = Integer.parseInt(value);
						if(intValue < MIN_SCALE || intValue > MAX_SCALE){
							throw new IllegalArgumentException();
						}
					}catch (Exception ee) {
						UfoPublic.sendWarningMessage(MultiLang.getString("report00106"), StylePanel.this);
					}
				}				
			});
		}
		return tfScale;
	}
	/**
	 * This method initializes jCheckBox2	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getCbShowZero() {
		if (cbShowZero == null) {
			cbShowZero = new UICheckBox();
			cbShowZero.setText(MultiLang.getString("miufo1000337"));
			cbShowZero.setBounds(4, 29, 160, 21);
		}
		return cbShowZero;
	}
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel1() {  
		
		if (jPanel1 == null) {
			jPanel1 = new UIPanel();
			jPanel1.setLayout(null);
			jPanel1.setBounds(160, 0, 172, 106);
			jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(MultiLang.getString("miuforep0000200")));
			jPanel1.add(lbFieldColor, null);
			jPanel1.add(getCbShowZero(), null);
			jPanel1.add(getBtnColor(), null);
		}
		return jPanel1;
	}
	
	/**
	 * @return 返回 reportStyle。
	 */
	public ReportStyle getReportStyle() {
		if(m_ReportStyle!=null){
			m_ReportStyle.setShowColHeader(!cbHindCol.isSelected());
			m_ReportStyle.setShowRowHeader(!cbHindRow.isSelected());
			m_ReportStyle.setShowZero(cbShowZero.isSelected());
			m_ReportStyle.setGrid(btnColor.getUserColor());
			m_ReportStyle.setPercent(((Integer)tfScale.getValue()).intValue());
		}
		return m_ReportStyle;
	}
	/**
	 * @param reportStyle 要设置的 reportStyle。
	 */
	public void setReportStyle(ReportStyle reportStyle) {
		m_ReportStyle = reportStyle;
		if(m_ReportStyle!=null){
			cbHindCol.setSelected(!m_ReportStyle.isShowColHeader());
			cbHindRow.setSelected(!m_ReportStyle.isShowRowHeader());
			cbShowZero.setSelected(m_ReportStyle.isShowZero());
			btnColor.setUserColor(m_ReportStyle.getGrid());
			tfScale.setValue(new Integer(m_ReportStyle.getPercent()));
		}
		
	}
       }  //  @jve:decl-index=0:visual-constraint="10,10"
   