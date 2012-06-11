/*
 * Created on 2005-5-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.panel;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import org.jfree.chart.JFreeChart;

import com.ufida.report.chart.property.ChartProperty;
import com.ufsoft.iufo.resource.StringResource;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ChartAppearncePanel extends nc.ui.pub.beans.UIPanel implements ActionListener,FocusListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel jLabel1 = null;
	private JLabel jLabel = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JTextField topField = null;
	private JTextField bottomField = null;
	private JTextField rightField = null;
	private JTextField leftField = null;
	private JPanel edgePanel = null;
	private JPanel jPanel = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel6 = null;
	private JButton bgColorBtn = null;
	private JButton plotAreaColorBtn = null;
	
	private ChartProperty m_property = null;
	private JFreeChart m_sample = null;	
	
	/**
	 * This is the default constructor
	 */
	public ChartAppearncePanel(ChartProperty property, JFreeChart sample) {
		super();
		if(property == null || sample == null) 
		    throw new IllegalArgumentException();
		m_property = property;
		m_sample = sample;
		 
		
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n uibichart00027=左
	 * @i18n uibichart00026=下
	 * @i18n uibichart00028=右
	 * @i18n uibichart00025=上
	 * @i18n uibichart00041=图表区域
	 */
	private  void initialize() {
	    this.setLayout(null);
		
		jLabel1 = new nc.ui.pub.beans.UILabel();
	    jLabel3 = new nc.ui.pub.beans.UILabel();
		jLabel = new nc.ui.pub.beans.UILabel();
		jLabel2 = new nc.ui.pub.beans.UILabel();	
		jLabel1.setText(StringResource.getStringResource("uibichart00027"));
		jLabel1.setBounds(37, 66, 13, 24);
		jLabel3.setText(StringResource.getStringResource("uibichart00026"));
		jLabel3.setBounds(131, 113, 13, 24);
		jLabel.setText(StringResource.getStringResource("uibichart00028"));
		jLabel.setBounds(230, 66, 13, 24);
		jLabel2.setText(StringResource.getStringResource("uibichart00025"));
		jLabel2.setBounds(131, 18, 13, 24);
			
		this.setName(StringResource.getStringResource("uibichart00041"));
		this.setBounds(0, 0, 300, 300);
		this.add(getEdgePanel(), null);
		this.add(getJPanel(), null);
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBgColorBtn() {
		if (bgColorBtn == null) {
			bgColorBtn = new nc.ui.pub.beans.UIButton();			
			bgColorBtn.setBackground(this.getBgColor());
			bgColorBtn.setBounds(120, 25, 75, 22);
			bgColorBtn.addActionListener(this);
		}
		return bgColorBtn;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getPlotAreaColorBtn() {
		if (plotAreaColorBtn == null) {
			plotAreaColorBtn = new nc.ui.pub.beans.UIButton();			
			plotAreaColorBtn.setBackground(this.getPlotColor());
			plotAreaColorBtn.setBounds(120, 75, 75, 22);
			plotAreaColorBtn.addActionListener(this);
		}
		return plotAreaColorBtn;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTopField() {
		if (topField == null) {
			topField = new UITextField();
			topField.setText(String.valueOf(this.getEdgeInsets().top));
			topField.setBounds(110, 41, 60, 25);
			topField.addFocusListener(this);
		}
		return topField;
	}
	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getBottomField() {
		if (bottomField == null) {
			bottomField = new UITextField();
			bottomField.setText(String.valueOf(this.getEdgeInsets().bottom));
			bottomField.setBounds(110, 89, 60, 25);
			bottomField.addFocusListener(this);
		}
		return bottomField;
	}
	/**
	 * This method initializes jTextField2	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getRightField() {
		if (rightField == null) {
			rightField = new UITextField();
			rightField.setText(String.valueOf(this.getEdgeInsets().right));
			rightField.setBounds(170, 65, 60, 25);
			rightField.addFocusListener(this);
		}
		return rightField;
	}
	/**
	 * This method initializes jTextField3	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getLeftField() {
		if (leftField == null) {
			leftField = new UITextField();
			leftField.setText(String.valueOf(this.getEdgeInsets().left));
			leftField.setBounds(50, 65, 60, 25);
			leftField.addFocusListener(this);
		}
		return leftField;
	}
	/**
	 * This method initializes edgePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n uibichart00042=边距
	 */    
	private JPanel getEdgePanel() {
		if (edgePanel == null) {
			edgePanel = new UIPanel();
			edgePanel.setLayout(null);
			edgePanel.setBounds(11, 141, 281, 145);
			edgePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("uibichart00042"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			edgePanel.add(jLabel, null);
			edgePanel.add(jLabel3, null);
			edgePanel.add(jLabel1, null);
			edgePanel.add(jLabel2, null);
			edgePanel.add(getTopField(), null);
			edgePanel.add(getRightField(), null);
			edgePanel.add(getLeftField(), null);
			edgePanel.add(getBottomField(), null);
		}
		return edgePanel;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n miufo1000860=颜色
	 * @i18n ubimultidim0055=背景颜色
	 * @i18n uibichart00043=绘图区域颜色
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new UIPanel();
			jLabel4 = new nc.ui.pub.beans.UILabel();
			jLabel6 = new nc.ui.pub.beans.UILabel();
			jPanel.setLayout(null);
			jPanel.setBounds(11, 5, 281, 125);
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("miufo1000860"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jLabel4.setText(StringResource.getStringResource("ubimultidim0055"));
			jLabel4.setBounds(42, 25, 79, 25);
			jLabel6.setBounds(42, 75, 79, 25);
			jLabel6.setText(StringResource.getStringResource("uibichart00043"));
			jPanel.add(jLabel4, null);
			jPanel.add(jLabel6, null);
			jPanel.add(getBgColorBtn(), null);
			jPanel.add(getPlotAreaColorBtn(), null);
		}
		return jPanel;
	}
   
    private ChartProperty getProperty() {
        return m_property;
    }
    
    private JFreeChart getSample() {
        return m_sample;
    }
    
    
    private void showSamplePrivew() {      
        this.getProperty().applyProperties(this.getSample());
    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    /**
	 * @i18n uibichart00018=颜色选取
	 * @i18n uibichart00018=颜色选取
	 */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()== this.getBgColorBtn()) {      
            Color c = JColorChooser.showDialog(this, StringResource.getStringResource("uibichart00018"), this.getBgColor());
    		if (c != null) {    		   
    		    getBgColorBtn().setBackground(c);
    		    this.setBgColor(c);
    		    showSamplePrivew();    		    
    		}
        }
        
        if(e.getSource()== this.getPlotAreaColorBtn()) {      
            Color c = JColorChooser.showDialog(this, StringResource.getStringResource("uibichart00018"), this.getPlotColor());
    		if (c != null) {    		    
    		    getPlotAreaColorBtn().setBackground(c);
    		    this.setPlotColor(c);
    		    showSamplePrivew();    		    
    		}
        }
    }
    /* (non-Javadoc)
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    public void focusGained(FocusEvent e) {
        // TODO Auto-generated method stub
        
    }
    /* (non-Javadoc)
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    public void focusLost(FocusEvent e) {       
        if(e.getSource() == this.getTopField()
                || e.getSource() == this.getLeftField()
                || e.getSource() == this.getBottomField()
                || e.getSource() == this.getRightField()) {
            try {
                Insets newInsets = new Insets(Integer.parseInt(this.getTopField().getText()),
                        Integer.parseInt(this.getLeftField().getText()),
                        Integer.parseInt(this.getBottomField().getText()),
                        Integer.parseInt(this.getRightField().getText()));
                this.setEdgeInsets(newInsets);
                
                showSamplePrivew();    
                
            } catch (Exception e1) {
                getTopField().setText(String.valueOf(getEdgeInsets().top));
                getLeftField().setText(String.valueOf(getEdgeInsets().left));
                getBottomField().setText(String.valueOf(getEdgeInsets().bottom));
                getRightField().setText(String.valueOf(getEdgeInsets().right));                
            }  
        }
        
    }
    
    public void applyProperties(ChartProperty chartProperty) {    
        if(chartProperty == null)
            throw new IllegalArgumentException();
        
        chartProperty.setBgColor(getBgColor());
        chartProperty.getPlotProperty().setBackgroundColor(this.getPlotColor());
        chartProperty.getPlotProperty().setEdgeInsets(this.getEdgeInsets());
    }
    /**
     * @return Returns the m_bgColor.
     */
    private Color getBgColor() {       
        return this.getProperty().getBgColor();
    }
    /**
     * @param color The m_bgColor to set.
     */
    private void setBgColor(Color color) {
        this.getProperty().setBgColor(color);
    }
    /**
     * @return Returns the m_plotColor.
     */
    private Color getPlotColor() {
        return  this.getProperty().getPlotProperty().getBackgroundColor();
    }
    /**
     * @param color The m_plotColor to set.
     */
    private void setPlotColor(Color color) {
        this.getProperty().getPlotProperty().setBackgroundColor(color);
    }
    /**
     * @return Returns the m_insets.
     */
    private Insets getEdgeInsets() {
        return this.getProperty().getPlotProperty().getEdgeInsets();
    }
    /**
     * @param m_insets The m_insets to set.
     */
    private void setEdgeInsets(Insets insets) {
        this.getProperty().getPlotProperty().setEdgeInsets(insets);
    }
                   }  //  @jve:decl-index=0:visual-constraint="102,72"
