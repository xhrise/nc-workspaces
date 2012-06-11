/*
 * Created on 2005-5-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.pie;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;

import org.jfree.chart.plot.PiePlot;

import com.ufida.report.chart.panel.FontChooserDialog;
import com.ufsoft.iufo.resource.StringResource;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PiePlotPropPane extends nc.ui.pub.beans.UIPanel implements ActionListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private class DataListItem{
        private String value = null;
        private Color color = null;        
        
        public DataListItem(String value, Color color) {
            this.value = value;
            this.color = color;
        }
        public String getValue() {
            return value;
        }        
        public Color getColor() {
            return color;
        }
        
        public String toString() {
            return this.getValue();
        }
    }
	private JList sectionColorList = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JButton sectionColorBtn = null;
	private JCheckBox ValueLabelShowBox = null;
	private JLabel valueLabelFont = null;
	private JLabel valueLabelColor = null;
	private JLabel valueLabelBackgroundColor = null;
	
	PiePlotProperty m_property = null;
	PiePlot m_sample = null;  
    
	private JScrollPane jScrollPane = null;
	private JTextField valueLabelFontField = null;
	private JButton valueLabelColorBtn = null;
	private JButton valueLabelBackgroundColorBtn = null;
	private JButton valueLabelFontBtn = null;
    public PiePlotPropPane(PiePlotProperty plotProp, PiePlot plot) {
        super();
        if(plotProp == null ||  plot == null)
            throw new IllegalArgumentException();    
        m_property = plotProp;        
        m_sample = plot;        
        initialize();
    }
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n ubichart00111=饼状图
	 */
	private  void initialize() {
        this.setLayout(null);
        this.setBounds(0, 0, 300, 300);
        this.setName(StringResource.getStringResource("ubichart00111"));
        this.add(getJPanel(), null);
        this.add(getJPanel1(), null);
	}
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getSectionColorList() {
		if (sectionColorList == null) {
			sectionColorList = new UIList();
		    DefaultListModel model = new DefaultListModel();
		    DataListItem item = null;
		    for(int i = 0; i < this.getSample().getLegendItems().getItemCount(); i++) {
		        item = new DataListItem(this.getSample().getLegendItems().get(i).getLabel(), (Color) this.getSample().getLegendItems().get(i).getPaint());		        
		        model.addElement(item);
		        this.getPlotProperty().setSectionColor(i, item.getColor());
		        sectionColorList.setSelectedIndex(0);
		    }		    
		    
		    sectionColorList.setModel(model);
		    
		    sectionColorList.addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent e) {
		            updateSectionColorBtn();
		        }
		        });
		}
		return sectionColorList;
	}
	
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n uibichart00032=数据设置
	 * @i18n uibichart00033=数据系列:
	 * @i18n miufo1000947=颜色：
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel1 = new nc.ui.pub.beans.UILabel();
			jLabel = new nc.ui.pub.beans.UILabel();
			jPanel = new UIPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(9, 7, 276, 139);
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("uibichart00032"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jLabel.setBounds(20, 18, 79, 18);
			jLabel.setText(StringResource.getStringResource("uibichart00033"));
			jLabel1.setBounds(200, 59, 51, 25);
			jLabel1.setText(StringResource.getStringResource("miufo1000947"));
			jPanel.add(jLabel, null);
			jPanel.add(jLabel1, null);
			jPanel.add(getSectionColorBtn(), null);
			jPanel.add(getJScrollPane(), null);
		}
		return jPanel;
	}
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n uibichart00034=值标
	 */    
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new UIPanel();
			jPanel1.setLayout(null);
			jPanel1.setBounds(9, 153, 276, 139);
			jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("uibichart00034"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));			
			jPanel1.add(getValueLabelShowBox(), null);
			jPanel1.add(getValueLabelFontField(), null);
			jPanel1.add(getValueLabelColorBtn(), null);
			jPanel1.add(getValueLabelBackgroundColorBtn(), null);
			jPanel1.add(getValueLabelFontBtn(), null);
			jPanel1.add(getValueLabelFont(), null);
			jPanel1.add(getValueLabelColor(), null);
			jPanel1.add(getValueLabelBackgroundColor(), null);
		}
		return jPanel1;
	}
	/**
	 * @i18n miufo1000947=颜色：
	 */
	private JLabel getValueLabelColor() {
	    if(valueLabelColor== null) {
	        valueLabelColor = new nc.ui.pub.beans.UILabel();
	        valueLabelColor.setBounds(25, 79, 40, 21);
			valueLabelColor.setText(StringResource.getStringResource("miufo1000947"));
	    }
	    return valueLabelColor;
	}
	/**
	 * @i18n uibichart00035=字体：
	 */
	private JLabel getValueLabelFont() {
	    if(valueLabelFont== null) {
	        valueLabelFont = new nc.ui.pub.beans.UILabel();
	        valueLabelFont.setBounds(25, 47, 40, 21);
			valueLabelFont.setText(StringResource.getStringResource("uibichart00035"));
	    }
	    return valueLabelFont;
	}
	/**
	 * @i18n uibichart00036=背景：
	 */
	private JLabel getValueLabelBackgroundColor() {
	    if(valueLabelBackgroundColor== null) {
	        valueLabelBackgroundColor = new nc.ui.pub.beans.UILabel();
	        valueLabelBackgroundColor.setBounds(25, 111, 40, 21);
			valueLabelBackgroundColor.setText(StringResource.getStringResource("uibichart00036"));
	    }
	    return valueLabelBackgroundColor;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getSectionColorBtn() {
		if (sectionColorBtn == null) {
			sectionColorBtn = new nc.ui.pub.beans.UIButton();			
			sectionColorBtn.setBounds(200, 86, 75, 22);
			updateSectionColorBtn();
			sectionColorBtn.addActionListener(this);
		}
		return sectionColorBtn;
	}
	
	private void updateSectionColorBtn() {
	    if(this.getSectionColorList().getSelectedValue() != null) {
	        DataListItem item = (DataListItem) this.getSectionColorList().getSelectedValue();
		    getSectionColorBtn().setBackground(item.getColor());
	    }
	   
	}
	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 * @i18n uibichart00037=显示值标
	 */    
	private JCheckBox getValueLabelShowBox() {
		if (ValueLabelShowBox == null) {
			ValueLabelShowBox = new UICheckBox();
			ValueLabelShowBox.setText(StringResource.getStringResource("uibichart00037"));
			ValueLabelShowBox.setBounds(25, 15, 96, 21);
			ValueLabelShowBox.setSelected(this.getPlotProperty().isLabelVisible());
			ValueLabelShowBox.addActionListener(this);
			setValueLabelsStatus(this.getPlotProperty().isLabelVisible());
		}
		return ValueLabelShowBox;
	}
	
	private void setValueLabelsStatus(boolean enable) {
	    this.getValueLabelColor().setEnabled(enable);
	    this.getValueLabelBackgroundColor().setEnabled(enable);
	    this.getValueLabelFont().setEnabled(enable);
	    this.getValueLabelBackgroundColorBtn().setEnabled(enable);
	    this.getValueLabelColorBtn().setEnabled(enable);
	    this.getValueLabelFontBtn().setEnabled(enable);
	    this.getValueLabelFontField().setEnabled(enable);
	}
    /**
     * @return Returns the m_property.
     */
    public PiePlotProperty getPlotProperty() {
        return m_property;
    }
    /**
     * @return Returns the m_sample.
     */
    private PiePlot getSample() {
        return m_sample;
    }
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(16, 38, 176, 90);
			jScrollPane.setViewportView(getSectionColorList());
		}
		return jScrollPane;
	}

	private void showSamplePrivew() {
        this.getPlotProperty().applyProperties(this.getSample());       
    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    /**
	 * @i18n uibichart00018=颜色选取
	 * @i18n uibichart00018=颜色选取
	 * @i18n uibichart00018=颜色选取
	 */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.getSectionColorBtn()) {
            int selectedSection = this.getSectionColorList().getSelectedIndex();
            Color ori = this.getPlotProperty().getSectionColor(selectedSection);
            Color c = JColorChooser.showDialog(this, StringResource.getStringResource("uibichart00018"), ori );
    		if (c != null) {
    		    this.getPlotProperty().setSectionColor(selectedSection, c);
    		    this.getSectionColorBtn().setBackground(c);
    		    showSamplePrivew();    		    
    		}
        }
        
        if(e.getSource() == this.getValueLabelShowBox()) {
            this.getPlotProperty().setLabelVisible(this.getValueLabelShowBox().isSelected());
            setValueLabelsStatus(this.getPlotProperty().isLabelVisible());
            showSamplePrivew();
        }
        if(e.getSource() == this.getValueLabelFontBtn()) {
            FontChooserDialog dlg = new FontChooserDialog(this, this.getPlotProperty().getLabelFont());
            dlg.show();
            if(dlg.getResult() == JOptionPane.OK_OPTION) {
                if(dlg.getSelectedFont() != null) {
                    this.getPlotProperty().setLabelFont(dlg.getSelectedFont());
                    this.getValueLabelFontField().setText(FontChooserDialog.getFontString(this.getPlotProperty().getLabelFont()));                   
                    showSamplePrivew();                    
                }                
            }
        }
        
        if(e.getSource() == this.getValueLabelBackgroundColorBtn()) {
            Color ori = this.getPlotProperty().getLabelBackgroundColor();
            Color c = JColorChooser.showDialog(this, StringResource.getStringResource("uibichart00018"), ori );
    		if (c != null) {
    		    this.getPlotProperty().setLabelBackgroundColor(c);
                getValueLabelBackgroundColorBtn().setBackground(this.getPlotProperty().getLabelBackgroundColor());
                showSamplePrivew();  		    
    		}            
        }
        if(e.getSource() == this.getValueLabelColorBtn()) {
            Color ori = this.getPlotProperty().getLabelColor();
            Color c = JColorChooser.showDialog(this, StringResource.getStringResource("uibichart00018"), ori );
    		if (c != null) {
    		    this.getPlotProperty().setLabelColor(c);
    		    getValueLabelColorBtn().setBackground(this.getPlotProperty().getLabelColor());
                showSamplePrivew();  		    
    		}            
        }
    }
   
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getValueLabelFontField() {
		if (valueLabelFontField == null) {
			valueLabelFontField = new UITextField();
			valueLabelFontField.setBounds(63, 47, 138, 21);
			valueLabelFontField.setEditable(false);
			this.getValueLabelFontField().setText(FontChooserDialog.getFontString(this.getPlotProperty().getLabelFont()));                   
		}
		return valueLabelFontField;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getValueLabelColorBtn() {
		if (valueLabelColorBtn == null) {
			valueLabelColorBtn = new nc.ui.pub.beans.UIButton();
			valueLabelColorBtn.setBounds(63, 79, 138, 22);
			valueLabelColorBtn.setBackground(this.getPlotProperty().getLabelColor());
			valueLabelColorBtn.addActionListener(this);
		}
		return valueLabelColorBtn;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getValueLabelBackgroundColorBtn() {
		if (valueLabelBackgroundColorBtn == null) {
			valueLabelBackgroundColorBtn = new nc.ui.pub.beans.UIButton();
			valueLabelBackgroundColorBtn.setBounds(63, 111, 138, 22);
			valueLabelBackgroundColorBtn.setBackground(this.getPlotProperty().getLabelBackgroundColor());
			valueLabelBackgroundColorBtn.addActionListener(this);
		}
		return valueLabelBackgroundColorBtn;
	}
	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1001396=修改
	 */    
	private JButton getValueLabelFontBtn() {
		if (valueLabelFontBtn == null) {
			valueLabelFontBtn = new nc.ui.pub.beans.UIButton();
			valueLabelFontBtn.setBounds(201, 47, 75, 22);
			valueLabelFontBtn.setText(StringResource.getStringResource("miufo1001396"));
			valueLabelFontBtn.addActionListener(this);
		}
		return valueLabelFontBtn;
	}
      }
