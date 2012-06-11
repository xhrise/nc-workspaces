/*
 * Created on 2005-5-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;



import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;

import com.ufida.report.chart.property.GridlineProperty;
import com.ufida.report.chart.property.NumberAxisProperty;
import com.ufsoft.table.format.TableConstant;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import com.ufsoft.iufo.resource.StringResource;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NumberAxisPropPanel extends JPanel implements ActionListener, FocusListener{
  	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JCheckBox autoCalcBox = null;
	private JTextField maxValueField = null;
	private JTextField minValueField = null;
	private JLabel maxValueLabel = null;
	private JLabel minValueLabel = null;
	private JPanel jPanel1 = null;
	private JCheckBox lineVisiableBox = null;
	private JLabel lineTypeLabel = null;
	private JButton lineColorButton = null;
	private JLabel jLabel4 = null;
	private JTextField numberFormatField = null;
	
	NumberAxisProperty m_axisProperty = null;
	GridlineProperty m_gridProperty = null;	
	Plot m_sample = null;
	
	private JList lineTypeList = null;
	private JScrollPane lineTypeScrollPane = null;
	private JLabel lineColorLabel = null;
	
	/**
	 * This is the default constructor
	 */

	public NumberAxisPropPanel(final NumberAxisProperty axisProperty,final GridlineProperty gridProperty, Plot plot) {
	    super();
	    if(axisProperty == null || gridProperty == null || plot == null) 
		    throw new IllegalArgumentException();
	    m_axisProperty = axisProperty;
		m_gridProperty = gridProperty;
		m_sample = plot;		
		initialize();
		getNumberAxisProperty().applyProperties(getSampleAxis());
		this.getGridlineProperty().applyProperties(m_sample);
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n uibichart00009=数值轴
	 * @i18n miufopublic443=小数位数
	 */
	private  void initialize() {
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridx = 1;
		gridBagConstraints3.gridy = 2;
		gridBagConstraints3.ipadx = 200;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.insets = new java.awt.Insets(2,2,14,22);
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.insets = new java.awt.Insets(2,12,14,1);
		gridBagConstraints2.gridy = 2;
		gridBagConstraints2.ipadx = 7;
		gridBagConstraints2.ipady = 4;
		gridBagConstraints2.gridx = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.insets = new java.awt.Insets(11,11,3,22);
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.ipadx = 266;
		gridBagConstraints1.ipady = 109;
		gridBagConstraints1.gridwidth = 2;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(3,11,1,22);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.ipadx = 266;
		gridBagConstraints.ipady = 133;
		gridBagConstraints.gridwidth = 2;
		jLabel4 = new JLabel();
		this.setLayout(new GridBagLayout());
		this.setName(StringResource.getStringResource("uibichart00009"));
		this.setSize(300, 300);		
		this.setPreferredSize(new java.awt.Dimension(300,300));	
		jLabel4.setText(StringResource.getStringResource("miufopublic443"));
		this.add(getJPanel1(), gridBagConstraints);
		this.add(getJPanel(), gridBagConstraints1);
		this.add(jLabel4, gridBagConstraints2);
		this.add(getNumberFormatField(), gridBagConstraints3);
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n uibichart00010=比列
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.setName("");
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("uibichart00010"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			
			
			jPanel.add(getAutoCalcBox(), null);
			jPanel.add(getMaxValueField(), null);
			jPanel.add(getMinValueField(), null);
			jPanel.add(getMaxValueLabel(), null);
			jPanel.add(getMinValueLabel(), null);
		}
		return jPanel;
	}
	
	/**
	 * @i18n uibichart00011=最小值
	 */
	private JLabel getMinValueLabel() {
	    if(minValueLabel == null) {
	        minValueLabel = new JLabel();
	        minValueLabel.setBounds(47, 81, 42, 21);
			minValueLabel.setText(StringResource.getStringResource("uibichart00011"));
	    }
	    return minValueLabel;
	}
	
	/**
	 * @i18n uibichart00012=最大值
	 */
	private JLabel getMaxValueLabel() {
	    if(maxValueLabel == null) {
	        maxValueLabel = new JLabel();
	        maxValueLabel.setBounds(47, 50, 42, 21);
			maxValueLabel.setText(StringResource.getStringResource("uibichart00012"));
	    }
	    return maxValueLabel;
	}
	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 * @i18n uibichart00013=自动计算
	 */    
	private JCheckBox getAutoCalcBox() {
		if (autoCalcBox == null) {
			autoCalcBox = new JCheckBox();
			autoCalcBox.setText(StringResource.getStringResource("uibichart00013"));
			autoCalcBox.setBounds(45, 14, 76, 26);
			autoCalcBox.addActionListener(this);
			
			autoCalcBox.setSelected(this.getSampleAxis().isAutoRange());
			this.setValueFieldStatus(this.getSampleAxis().isAutoRange());
		}
		return autoCalcBox;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getMaxValueField() {
		if (maxValueField == null) {
			maxValueField = new JTextField();
			maxValueField.setText(String.valueOf(this.getNumberAxisProperty().getRangeUpper()));
			maxValueField.setBounds(104, 50, 75, 21);			
//			maxValueField.addActionListener(this);
			maxValueField.addFocusListener(this);
		}
		return maxValueField;
	}
	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getMinValueField() {
		if (minValueField == null) {
			minValueField = new JTextField();
			minValueField.setText(String.valueOf(this.getNumberAxisProperty().getRangeLower()));
			minValueField.setBounds(104, 81, 75, 21);
//			minValueField.addActionListener(this);
			minValueField.addFocusListener(this);
		}
		return minValueField;
	}
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n uibichart00014=格线
	 */    
	private JPanel getJPanel1() {
		if (jPanel1 == null) {			
			jPanel1 = new JPanel();			
			jPanel1.setLayout(null);
			jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("uibichart00014"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));			
			
			jPanel1.add(getLineVisiableBox(), null);
			jPanel1.add(lineTypeLabel, null);
			jPanel1.add(getLineColorButton(), null);
			jPanel1.add(getLineTypeScrollPane(), null);
			jPanel1.add(lineColorLabel, null);
		}
		return jPanel1;
	}
	
	/**
	 * @i18n uibichart00015=颜色:
	 */
	private JLabel getLineColorLabel() {
	    if(lineColorLabel == null) {
	        lineColorLabel = new JLabel();
	        lineColorLabel.setBounds(13, 105, 35, 21);
			lineColorLabel.setText(StringResource.getStringResource("uibichart00015"));
	    }
	    return lineColorLabel;
	}
	
	/**
	 * @i18n uibichart00016=线型:
	 * @i18n uibichart00016=线型:
	 */
	private JLabel getLineTypeLabel() {
	    if(lineTypeLabel == null) {
	        lineTypeLabel = new JLabel();
	        lineTypeLabel.setText(StringResource.getStringResource("uibichart00016"));
			lineTypeLabel.setBounds(15, 51, 35, 21);
	        lineTypeLabel.setText(StringResource.getStringResource("uibichart00016"));
	    }
	    return lineTypeLabel;
	}
	/**
	 * This method initializes jCheckBox1	
	 * 	
	 * @return javax.swing.JCheckBox	
	 * @i18n uibichart00017=显示格线
	 */    
	private JCheckBox getLineVisiableBox() {
		if (lineVisiableBox == null) {
			lineVisiableBox = new JCheckBox();
			lineVisiableBox.setText(StringResource.getStringResource("uibichart00017"));
			lineVisiableBox.setBounds(8, 16, 76, 21);
			lineVisiableBox.setSelected(this.getGridlineProperty().isVisible());
			lineVisiableBox.addActionListener(this);			
			setLineTypeStatus(this.getGridlineProperty().isVisible());
		}
		return lineVisiableBox;
	}
	
	private void setLineTypeStatus(boolean enable) {
	    this.getLineColorButton().setEnabled(enable);
	    this.getLineTypeLabel().setEnabled(enable);
	    this.getLineTypeList().setEnabled(enable);
	    this.getLineColorLabel().setEnabled(enable);
	    
	    if(enable) {
	        this.getLineTypeList().setBackground(Color.WHITE);
	    }else {
	        this.getLineTypeList().setBackground(this.getLineColorLabel().getBackground());
	    }
	}
	private void setValueFieldStatus(boolean enable) {	 
	    boolean status = !enable;
	    this.getMaxValueField().setEditable(status);
	    this.getMinValueField().setEditable(status);
	    
	    this.getMaxValueField().setEnabled(status);
	    this.getMinValueField().setEnabled(status);
	    this.getMaxValueLabel().setEnabled(status);
	    this.getMinValueLabel().setEnabled(status);
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getLineColorButton() {
		if (lineColorButton == null) {
			lineColorButton = new JButton();			
			lineColorButton.setBackground((Color) this.getGridlineProperty().getPaint());
		    lineColorButton.setBounds(61, 105, 49, 22);	
//		    lineColorButton = new ColorButton((Color) getGridlineProperty().getPaint(),this);			
//		    int x = (int) this.getLineTypeScrollPane().getBounds().getMinX();
//			int y = (int) this.getLineColorLabel().getBounds().getMinY();
//			lineColorButton.setBounds(x, y, 50, this.getLineColorLabel().getHeight());
			
					
			lineColorButton.addActionListener(this);
		}
		return lineColorButton;
	}
	/**
	 * This method initializes jTextField2	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getNumberFormatField() {
		if (numberFormatField == null) {
			numberFormatField = new JTextField();
			numberFormatField.addFocusListener(this);			
		}
		return numberFormatField;
	}
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    /**
	 * @i18n uibichart00018=颜色选取
	 */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()== this.autoCalcBox){  
            this.getNumberAxisProperty().setAutoRange(this.getAutoCalcBox().isSelected());
            setValueFieldStatus(this.getNumberAxisProperty().isAutoRange());
            showSamplePrivew();
		}	
        if(e.getSource()==lineVisiableBox){          
            this.getGridlineProperty().setVisible(this.getLineVisiableBox().isSelected());
            setLineTypeStatus(this.getGridlineProperty().isVisible());
			showSamplePrivew();
		}	
        
        if(e.getSource()== getLineColorButton()) {      
            Color c = JColorChooser.showDialog(this, StringResource.getStringResource("uibichart00018"), (Color) this.getGridlineProperty().getPaint());
    		if (c != null) {
    		    this.getGridlineProperty().setPaint(c);
    		    getLineColorButton().setBackground((Color) this.getGridlineProperty().getPaint());
    		    showSamplePrivew();    		    
    		}
        }      
        
    }
    
    private void showSamplePrivew() {
        this.getGridlineProperty().applyProperties(this.getSamplePlot());
        this.getNumberAxisProperty().applyProperties(this.getSampleAxis());
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
        if(e.getSource()==this.getMaxValueField() || e.getSource()==this.getMinValueField()){ 
            double oriMax = getNumberAxisProperty().getRangeUpper();
            double oriMin = getNumberAxisProperty().getRangeLower();            
            double max = oriMax;
            double min = oriMin;
            try {
                max = Double.parseDouble(this.getMaxValueField().getText());
                min = Double.parseDouble(this.getMinValueField().getText());
                if((max != oriMax)  || (min != oriMin)){                    
                    this.getNumberAxisProperty().setRangeLower(min);
                    this.getNumberAxisProperty().setRangeUpper(max);
                    showSamplePrivew();
                }
            } catch (Exception e1) {                
                return;
            }
		}	
        
        if(e.getSource()==this.getNumberFormatField()){ 
            int num = 0;           
            try {
                num = Integer.parseInt(getNumberFormatField().getText());
            } catch (NumberFormatException e1) {
                return;
            }
            if(num != this.getNumberAxisProperty().getNumberFormat().getMaximumFractionDigits()) {
               this.getNumberAxisProperty().getNumberFormat().setMaximumFractionDigits(num);
               this.getNumberFormatField().setText(String.valueOf(this.getNumberAxisProperty().getNumberFormat().getMaximumFractionDigits()));
               showSamplePrivew();
            }
                    
		}	 
        
        if(e.getSource()==this.getLineTypeList()){ 
            onLineTypeChange();
        }
        
    }
  
    /**
     * 处理线型变化
     */
    private void onLineTypeChange() {
        int index = this.getLineTypeList().getSelectedIndex();
        if(index != this.getGridlineProperty().getStroke()) {
            this.getGridlineProperty().setStroke(index);            
            showSamplePrivew();
        }        
    }
    
    private Plot getSamplePlot() {
        return m_sample;
    }
    
    private NumberAxis getSampleAxis() {
        if(m_sample instanceof CategoryPlot) {
            return (NumberAxis) ((CategoryPlot) m_sample).getRangeAxis();
        }
        
        throw new IllegalArgumentException("");      
    }
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getLineTypeList() {
		if (lineTypeList == null) {
			lineTypeList = new JList();
			String typeName[] = {
					new Integer(TableConstant.L_NULL).toString(),
					new Integer(TableConstant.L_SOLID1).toString(),
					new Integer(TableConstant.L_DASH).toString(),
					new Integer(TableConstant.L_DOT).toString(),
					new Integer(TableConstant.L_DASHDOT).toString(),
					new Integer(TableConstant.L_SOLID2).toString(),
					new Integer(TableConstant.L_SOLID3).toString(),
					new Integer(TableConstant.L_SOLID4).toString()};

			lineTypeList.setListData(typeName);
			lineTypeList.setCellRenderer(new LineRender());
			lineTypeList.setSelectedIndex(this.getGridlineProperty().getStroke());
			lineTypeList.addFocusListener(this);
			lineTypeList.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() >=2) {
                        onLineTypeChange();
                    }
                    
                }

                public void mouseEntered(MouseEvent e) {
                    // TODO Auto-generated method stub
                    
                }

                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub
                    
                }

                public void mousePressed(MouseEvent e) {
                    // TODO Auto-generated method stub
                    
                }

                public void mouseReleased(MouseEvent e) {
                    // TODO Auto-generated method stub
                    
                }
			    
			});
		}
		return lineTypeList;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getLineTypeScrollPane() {
		if (lineTypeScrollPane == null) {
			lineTypeScrollPane = new JScrollPane();
			lineTypeScrollPane.setBounds(56, 37, 202, 61);
			lineTypeScrollPane.setViewportView(getLineTypeList());
		}
		return lineTypeScrollPane;
	}
	private GridlineProperty getGridlineProperty() {
        return m_gridProperty;
    }

    private NumberAxisProperty getNumberAxisProperty() {
        return m_axisProperty;
    }
    
 }  //  @jve:decl-index=0:visual-constraint="165,40"
