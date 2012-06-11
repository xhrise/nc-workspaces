/*
 * Created on 2005-5-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.panel;

import java.awt.Color;
import java.awt.Stroke;
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

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;

import com.ufida.report.chart.property.CategoryAxisProperty;
import com.ufida.report.chart.property.GridlineProperty;
import com.ufsoft.table.format.LineFactory;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.iufo.resource.StringResource;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CategoryAxisPropPanel extends nc.ui.pub.beans.UIPanel implements ActionListener,FocusListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JCheckBox lineVisiableBox = null;	
	private JLabel lineColorLabel = null;
	
	CategoryAxisProperty m_axisProperty = null;
	GridlineProperty m_gridProperty = null;
	Plot m_sample = null;
	
	private JScrollPane lineTypeScrollPane = null;
	private JLabel lineTypeLabel = null;
	private JList lineTypeList = null;	
	private JButton lineColorButton = null;	
	/**
	 * This is the default constructor
	 */
	public CategoryAxisPropPanel(CategoryAxisProperty axisProperty , GridlineProperty gridProperty, Plot plot) {	    
		super();
		if(axisProperty == null || gridProperty == null || plot == null) 
		    throw new IllegalArgumentException();
		m_axisProperty = axisProperty;
		m_gridProperty = gridProperty;
		m_sample = plot;
		getCategoryAxisProperty().applyProperties(getSampleAxis());
		this.getGridlineProperty().applyProperties(m_sample);
		initialize();
	}
	private CategoryAxis getSampleAxis() {
        if(m_sample instanceof CategoryPlot) {
            return (CategoryAxis) ((CategoryPlot) m_sample).getDomainAxis();
        }
        
        throw new IllegalArgumentException("");      
    }
	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n uibichart00051=分类轴
	 */
	private  void initialize() {
		this.setLayout(null);
		this.setName(StringResource.getStringResource("uibichart00051"));
		this.setBounds(0, 0, 300, 300);
		this.add(getJPanel(), null);
		
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n uibichart00014=格线
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {		
			jPanel = new UIPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(20, 21, 244, 228);
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("uibichart00014"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));		
			
			jPanel.add(getLineVisiableBox(), null);
			jPanel.add(getLineColorButton(), null);
			jPanel.add(getLineColorLabel(), null);			
			jPanel.add(getLineTypeScrollPane(), null);
			jPanel.add(getLineTypeLabel(), null);
			jPanel.add(getLineColorButton(), null);
		}
		return jPanel;
	}
	/**
	 * @i18n uibichart00016=线型:
	 */
	private JLabel getLineTypeLabel() {
	    if(lineTypeLabel == null) {
	        lineTypeLabel = new nc.ui.pub.beans.UILabel();
	        lineTypeLabel.setText(StringResource.getStringResource("uibichart00016"));			
			lineTypeLabel.setBounds(14, 99, 41, 20);
			
	    }
	    return lineTypeLabel;
	}
	/**
	 * @i18n uibichart00015=颜色:
	 */
	private JLabel getLineColorLabel() {
	    if(lineColorLabel == null) {
	        lineColorLabel = new nc.ui.pub.beans.UILabel();			
			lineColorLabel.setBounds(14, 158, 41, 20);
			lineColorLabel.setText(StringResource.getStringResource("uibichart00015"));
	    }
	    
	    return lineColorLabel;	    
	}
	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 * @i18n uibichart00017=显示格线
	 */    
	private JCheckBox getLineVisiableBox() {
		if (lineVisiableBox == null) {
			lineVisiableBox = new UICheckBox();
			lineVisiableBox.setBounds(14, 39, 82, 21);
			lineVisiableBox.setText(StringResource.getStringResource("uibichart00017"));
			lineVisiableBox.setSelected(getGridlineProperty().isVisible());
			setLineTypeStatus(this.getGridlineProperty().isVisible());
			lineVisiableBox.addActionListener(this);
		}
		return lineVisiableBox;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
//	private JButton getLineColorButton() {
//		if (lineColorButton == null) {
//		    lineColorButton = new nc.ui.pub.beans.UIButton();
//		    lineColorButton.setBackground((Color) getGridlineProperty().getPaint());
//		    
//		    int x = (int) this.getLineTypeScrollPane().getBounds().getMinX();
//			int y = (int) this.getLineColorLabel().getBounds().getMinY();
//			lineColorButton.setBounds(x, y, 50, this.getLineColorLabel().getHeight());}
//		return lineColorButton;
//	}
	
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getLineColorButton() {
		if (lineColorButton == null) {
		    lineColorButton = new nc.ui.pub.beans.UIButton();		   
		    lineColorButton.setBackground((Color) this.getGridlineProperty().getPaint());
			lineColorButton.setBounds(57, 158, 75, 22);
			lineColorButton.addActionListener(this);
		}
		return lineColorButton;
	}
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n uibichart00018=颜色选取
	 */    
	
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource()==getLineVisiableBox()){
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
        this.getAxisProperty().applyProperties(this.getSampleAxis());
    }
    
    private GridlineProperty getGridlineProperty() {
        return 	m_gridProperty ;
    }
    private CategoryAxisProperty getCategoryAxisProperty() {
        return  m_axisProperty ;
    }
	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getLineTypeScrollPane() {
		if (lineTypeScrollPane == null) {
			lineTypeScrollPane = new UIScrollPane();
			lineTypeScrollPane.setBounds(56, 72, 176, 79);
			lineTypeScrollPane.setViewportView(getLineTypeList());
		}
		return lineTypeScrollPane;
	}
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getLineTypeList() {
		if (lineTypeList == null) {
			lineTypeList = new UIList();
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
        if(e.getSource()==this.getLineTypeList()){ 
            this.onLineTypeChange();
        }
    }
	
    /**处理线型变化*/
	private void onLineTypeChange() {
	    int index = this.getLineTypeList().getSelectedIndex();
        Stroke stroke = LineFactory.createLineStroke(index);
        if(m_sample instanceof CategoryPlot) {
            ((CategoryPlot) m_sample).setDomainGridlineStroke(stroke); 
        }
	}
	
    /**
     * @return Returns the m_axisProperty.
     */
    public CategoryAxisProperty getAxisProperty() {
        return m_axisProperty;
    }
    /**
     * @return Returns the m_sample.
     */
    private Plot getSamplePlot() {
        return m_sample;
    }
 }
	
