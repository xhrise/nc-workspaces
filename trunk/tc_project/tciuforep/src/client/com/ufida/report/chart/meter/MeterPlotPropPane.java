/*
 * Created on 2005-5-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.meter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import org.jfree.chart.plot.MeterPlot;

import com.ufida.report.chart.common.ChartDefaultSettings;
import com.ufsoft.iufo.resource.StringResource;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MeterPlotPropPane extends nc.ui.pub.beans.UIPanel implements PropertyChangeListener, FocusListener, ChangeListener{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private class ColorBarPanel extends nc.ui.pub.beans.UIPanel implements MouseListener{   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient EventListenerList listenerList ;
    Color[] colorList = {ChartDefaultSettings.DefaultMeterNormalColor,
            ChartDefaultSettings.DefaultMeterWarningColor,
            ChartDefaultSettings.DefaultMeterCriticalColor};    
    double[] percentList = {0.4, 0.3, 0.3};
    /**
	 * @i18n uibichart00044=双击可以改变颜色
	 */
    public ColorBarPanel(double[] percentList) {
        super();    
        listenerList = new EventListenerList();   
        this.percentList = percentList;
        this.setToolTipText(StringResource.getStringResource("uibichart00044"));
        this.addMouseListener(this);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension size = getSize();
        Color ori = g.getColor();
        for(int i = 0; i < getColorList().length; i++) {
            g.setColor(getColorList()[i]);
            g.fillRect(0,getStartY(i),size.width,(int) (getPercentList()[i] * size.height));
        }        
        g.setColor(ori);
    }
    
    private int getStartY(int index) {        
        if(index == 0) return 0;
        int height = this.getSize().height;
        if(index == 1) {
            return (int) (getPercentList()[0] * height);
        }
        if(index == 2)
            return (int) ((getPercentList()[0] + getPercentList()[1]) * height);
        return 0;
    }
    
    public Color[] getColorList() {
        return this.colorList;
    }   
    public void setColorList(Color[] colorList) {       
        this.colorList = colorList;
        this.repaint();
//        notifyListeners(new PropertyChangeEvent(this, null, null,null));
    }
    
    public double[] getPercentList() {
        return percentList;
    }
    public void setPercentList(double[] percentList) {
        this.percentList = percentList;
        this.repaint();
//        notifyListeners(new PropertyChangeEvent(this, null, null,null));
    }
    /**双击的时候改变颜色
     * @i18n uibichart00018=颜色选取*/
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() >= 2) {  
            Color oriColor = colorList[getClickPart(e)];
            Color c = JColorChooser.showDialog((Component) e.getSource(), StringResource.getStringResource("uibichart00018"), oriColor);
       		if (c != null) {              		  
       		  PropertyChangeEvent event = new PropertyChangeEvent(e.getSource(), null, null, null);       		  
       		  colorList[getClickPart(e)] = c;	
       		  notifyListeners(event);
       		  this.repaint();
       		}
        }
     }
     public void mouseEntered(MouseEvent e) {               
     }

     public void mouseExited(MouseEvent e) {               
     }

     public void mousePressed(MouseEvent e) {               
     }

     public void mouseReleased(MouseEvent e) {               
     }
     private int getClickPart(MouseEvent e) {
         double y = e.getPoint().getY();
         int result = 0;
            if(y < getStartY(1)) {
                result = 0;
            }else if(y > getStartY(2)) {
                result = 2;
            }else {
                result = 1;
            }
          return result;
     }    
     /**
      * Registers an object for notification of changes to the dataset.
      *
      * @param listener  the object to register.
      */
     public void addPropertyChangeListener(PropertyChangeListener listener) {
         this.listenerList.add(PropertyChangeListener.class, listener);
     }

     /**
      * Deregisters an object for notification of changes to the dataset.
      *
      * @param listener  the object to deregister.
      */
     public void removePropertyChangeListener(PropertyChangeListener listener) {
         this.listenerList.remove(PropertyChangeListener.class, listener);
     }
     
     /**
      * Notifies all registered listeners that the dataset has changed.
      *
      * @param event  contains information about the event that triggered the notification.
      */
     public  void notifyListeners(final PropertyChangeEvent event) {
         final Object[] listeners = this.listenerList.getListenerList();
         for (int i = listeners.length - 2; i >= 0; i -= 2) {
             if (listeners[i] == PropertyChangeListener.class) {
                 ((PropertyChangeListener) listeners[i + 1]).propertyChange(event);
             }
         }

     }
}

	private JPanel jPanel = null;	
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JTextField minField = null;
	private JTextField normalField = null;
	private JTextField warningFiled = null;
	private JTextField maxFiled = null;
	private JSlider criticalSlider = null;
	private ColorBarPanel colorBarPanel = null;
	
	
	MeterPlotProperty property = null;
	MeterPlot sample = null;
	private JSlider NormalSlider = null;
	
	private boolean valueFieldChanging = false;
	/**
	 * This is the default constructor
	 */
	public MeterPlotPropPane(MeterPlotProperty plotProperty,  MeterPlot samplePlot) {
		super();		
		if(plotProperty == null || samplePlot == null)
		    throw new IllegalArgumentException();
		this.property = plotProperty;
		this.sample = samplePlot;
		this.property.addChangeListener(this);
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n ubichart00128=仪表图
	 */
	private  void initialize() {
        this.setLayout(null);
        this.setBounds(0, 0, 300, 300);
        this.add(getJPanel(), null);
        this.setName(StringResource.getStringResource("ubichart00128"));
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 * @i18n uibichart00045=分段设置
	 * @i18n uibichart00046=最小值:
	 * @i18n uibichart00047=分段值:
	 * @i18n uibichart00047=分段值:
	 * @i18n uibichart00048=最大值:
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel3 = new nc.ui.pub.beans.UILabel();
			jLabel2 = new nc.ui.pub.beans.UILabel();
			jLabel1 = new nc.ui.pub.beans.UILabel();
			jLabel = new nc.ui.pub.beans.UILabel();
			jPanel = new UIPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(14, 5, 273, 288);
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("uibichart00045"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jLabel.setBounds(20, 44, 46, 17);
			jLabel.setText(StringResource.getStringResource("uibichart00046"));
			jLabel1.setBounds(20, 105, 46, 17);
			jLabel1.setText(StringResource.getStringResource("uibichart00047"));
			jLabel2.setBounds(20, 166, 46, 17);
			jLabel2.setText(StringResource.getStringResource("uibichart00047"));
			jLabel3.setBounds(20, 227, 46, 17);
			jLabel3.setText(StringResource.getStringResource("uibichart00048"));
			jPanel.add(jLabel, null);
			jPanel.add(jLabel1, null);
			jPanel.add(jLabel2, null);
			jPanel.add(jLabel3, null);
			jPanel.add(getMinField(), null);
			jPanel.add(getNormalField(), null);
			jPanel.add(getWarningFiled(), null);
			jPanel.add(getMaxFiled(), null);
			jPanel.add(getCriticalSlider(), null);
			jPanel.add(getColorBarPanel(), null);
			jPanel.add(getNormalSlider(), null);
			
		}
		return jPanel;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getMinField() {
		if (minField == null) {
			minField = new UITextField();
			minField.setBounds(66, 40, 75, 23);
			minField.setText(String.valueOf(this.getProperty().getMin()));
			minField.addFocusListener(this);
			
		}
		return minField;
	}
	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getNormalField() {
		if (normalField == null) {
			normalField = new UITextField();
			normalField.setBounds(66, 102, 75, 23);
			normalField.setText(String.valueOf(this.getProperty().getMaxNormal()));
			normalField.addFocusListener(this);			
			normalField.addPropertyChangeListener(this);
		}
		return normalField;
	}
	/**
	 * This method initializes jTextField2	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getWarningFiled() {
		if (warningFiled == null) {
			warningFiled = new UITextField();
			warningFiled.setBounds(66, 164, 75, 23);
			warningFiled.setText(String.valueOf(this.getProperty().getMaxWarning()));
			warningFiled.addFocusListener(this);
		}
		return warningFiled;
	}
	/**
	 * This method initializes jTextField3	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getMaxFiled() {
		if (maxFiled == null) {
			maxFiled = new UITextField();
			maxFiled.setBounds(66, 226, 75, 23);
			maxFiled.setText(String.valueOf(this.getProperty().getMax()));
			maxFiled.addFocusListener(this);			
		}
		return maxFiled;
	}
	/**
	 * This method initializes jSlider	
	 * 	
	 * @return javax.swing.JSlider	
	 */    
	private JSlider getCriticalSlider() {
		if (criticalSlider == null) {
			criticalSlider = new JSlider(JSlider.VERTICAL);
			criticalSlider.setInverted(false);
			criticalSlider.setBounds(187, 16, 18, 263);			
			setCriticalSlider();
            criticalSlider.addChangeListener(this); 		
		}
		return criticalSlider;
	}
	private void setCriticalSlider() {
        int value = 0;
        int ext = 100;
        try {
            double d = (getProperty().getMax() - getProperty().getMaxWarning())/(getProperty().getMax() - getProperty().getMin()) * 100;               
            value = (int) Math.round(d);
            
            d = (getProperty().getMaxNormal() - getProperty().getMin()) /(getProperty().getMax() - getProperty().getMin()) * 100;
            ext =  (int) Math.round(d);
        } catch (Exception e) {
            
        }
        getCriticalSlider().setValue(value);
        getCriticalSlider().setExtent(ext);
    }
    /**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private ColorBarPanel getColorBarPanel() {
		if (colorBarPanel == null) {		    
			colorBarPanel = new ColorBarPanel(getPercentList());
			colorBarPanel.setBounds(210, 22, 46, 250);
			colorBarPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			colorBarPanel.addPropertyChangeListener(this);
		}
		return colorBarPanel;
	}
    private double[] getPercentList() {
        double range = this.getProperty().getMax() - this.getProperty().getMin();
        double[] percentList = new double[3];
        percentList[0] = this.getProperty().getMaxNormal()/range;
        percentList[1] = (this.getProperty().getMaxWarning() - this.getProperty().getMaxNormal())/range;
        percentList[2] = 1 - percentList[0] - percentList[1];
        return percentList;
    }
    /**
     * @return Returns the property.
     */
    public MeterPlotProperty getProperty() {
        return property;
    }
    /**
     * @return Returns the sample.
     */
    public MeterPlot getSample() {
        return sample;
    }
    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getSource() == this.getColorBarPanel()) {
            Color[] color = this.getColorBarPanel().getColorList();
            this.getProperty().setNormalColor(color[0]);
            this.getProperty().setWarningColor(color[1]);
            this.getProperty().setCriticalColor(color[2]);
        }
       
        if(evt.getSource() == this.getProperty()) {
            resetFieldValue();
            Color[] color = {this.getProperty().getNormalColor(),
                    this.getProperty().getWarningColor(),
                    this.getProperty().getCriticalColor()};
            this.getColorBarPanel().setColorList(color);
            
            this.getColorBarPanel().setPercentList(this.getPercentList());
            this.setNormalSlider();
            this.setCriticalSlider();
        }
        showSamplePrivew();
    }
    private void resetFieldValue() {
        getMinField().setText(String.valueOf(this.getProperty().getMin()));
        getNormalField().setText(String.valueOf(this.getProperty().getMaxNormal()));
        getWarningFiled().setText(String.valueOf(this.getProperty().getMaxWarning()));
        getMaxFiled().setText(String.valueOf(this.getProperty().getMax()));
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
        try {
            if(e.getSource() == this.getMinField()) {
                valueFieldChanging = true;
                double newValue = Double.parseDouble(this.getMinField().getText());                
                this.getProperty().setMin(newValue);  
                valueFieldChanging = false;              
            }
            if(e.getSource() == this.getNormalField()) {
                double newValue = Double.parseDouble(this.getNormalField().getText());
                this.getProperty().setMaxNormal(newValue);
            }
            if(e.getSource() == this.getWarningFiled()) {
                double newValue = Double.parseDouble(this.getWarningFiled().getText());
                this.getProperty().setMaxWarning(newValue);
            }
            if(e.getSource() == this.getMaxFiled()) {
                valueFieldChanging = true;
                double newValue = Double.parseDouble(this.getMaxFiled().getText());
                this.getProperty().setMax(newValue);
                valueFieldChanging = false;
            }
        } catch (Exception e1) {       
            resetFieldValue();
        }
    }
	/**
	 * This method initializes jSlider1	
	 * 	
	 * @return javax.swing.JSlider	
	 */    
	private JSlider getNormalSlider() {
		if (NormalSlider == null) {
			NormalSlider = new JSlider(JSlider.VERTICAL);
			NormalSlider.setBounds(162, 16, 18, 263);
			NormalSlider.setInverted(true);			
			setNormalSlider();
			NormalSlider.addChangeListener(this); 				
		}
		return NormalSlider;
	}
    private void setNormalSlider() {
        int value = 0;
        int ext = 100;
        try {
            double d = (getProperty().getMaxNormal() - getProperty().getMin())/(getProperty().getMax() - getProperty().getMin()) * 100;               
            value = (int) Math.round(d);
            
            d = (getProperty().getMax() - getProperty().getMaxWarning()) /(getProperty().getMax() - getProperty().getMin()) * 100;
            ext =  (int) Math.round(d);
        } catch (NumberFormatException e) {
            
        }  
        getNormalSlider().setValue(value);            
        getNormalSlider().setExtent(ext);
        
    }
    /* (non-Javadoc)
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e)  {
        if(e.getSource() == this.getNormalSlider()){
            JSlider s = (JSlider) e.getSource();
            double value = this.getProperty().getMin() + (this.getProperty().getMax() - this.getProperty().getMin()) * s.getValue() / 100;
            try {
                if(! valueFieldChanging) {
                    this.getProperty().setMaxNormal(value);     
                }
            } catch (Exception e1) {     
                throw new RuntimeException();
            }
            this.setNormalSlider();
            showSamplePrivew();
        }   
        
        if(e.getSource() == this.getCriticalSlider()){
            JSlider s = (JSlider) e.getSource();
            double value = this.getProperty().getMax() - (this.getProperty().getMax() - this.getProperty().getMin()) * s.getValue() / 100;
            try {
                if(! valueFieldChanging) {
                    this.getProperty().setMaxWarning(value);     
                }
                
            } catch (Exception e1) {
               
            }
            this.setCriticalSlider();
            showSamplePrivew();
        } 
    }
    
    /**
     * 显示预览效果
     */
    private void showSamplePrivew() {  
        this.getProperty().applyProperties(this.getSample());       
        }
 }
