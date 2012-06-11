/*
 * Created on 2005-7-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.rep.applet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JLabel;
import javax.swing.JToolBar;

import nc.itf.iufo.freequery.IMember;

import com.ufida.report.adhoc.model.AdhocPublic;
import com.ufida.report.adhoc.model.PageDimField;

/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PageDimItem extends JToolBar implements PropertyChangeListener{
    /**组件最小宽度*/
    static public final int ITEM_MIN_WIDTH= 100;
    /**组件宽度**/
    static public final int ITEM_HEIGHT = 25;
    JLabel label = null;
    PageDimRefTextField textField = null;
    PageDimField pageDimField;
    
    private PropertyChangeSupport propertyChangeSupport = null;
    
    public PageDimItem(PageDimField pageDimField) {
    	super();
        setFloatable(false);
    	setLayout(new BorderLayout());
        if(pageDimField == null) throw new IllegalArgumentException();
        this.pageDimField = pageDimField;
        this.pageDimField.addChangeListener(this);
        
        add(getLabel(),BorderLayout.WEST);
        add(getTextFieldt(),BorderLayout.CENTER);
        
    }   
    
    public PropertyChangeSupport getPropertyChangeSupport(){
    	if(propertyChangeSupport == null){
    		propertyChangeSupport = new PropertyChangeSupport(this);
    	}
    	return propertyChangeSupport;
    }
    public JLabel getLabel() {
        if(label == null) {
            label = new nc.ui.pub.beans.UILabel();
            label.setText(" " + pageDimField.getName()+": ");
        }
        return label;
    }
      
    public PageDimRefTextField getTextFieldt() {
        if(textField == null) {
            textField = new PageDimRefTextField(pageDimField);
//            ((UITextField)textField).setMaxLength(10000);
            Dimension minDimension =  new Dimension(ITEM_MIN_WIDTH,ITEM_HEIGHT);

            if(textField.getSize().getWidth() < ITEM_MIN_WIDTH) {
                textField.setSize(minDimension);
            }else {
                textField.setSize(new Dimension((int) textField.getSize().getWidth(),ITEM_HEIGHT));
            }

            if(textField.getMinimumSize().getWidth() < ITEM_MIN_WIDTH) {
                textField.setMinimumSize(minDimension);
            }else {
                textField.setMinimumSize(new Dimension((int) textField.getMinimumSize().getWidth(),ITEM_HEIGHT));
            }
            
            
            if(textField.getMaximumSize().getWidth() < ITEM_MIN_WIDTH) {
                textField.setMaximumSize(minDimension);
            }else {
                textField.setMaximumSize(new Dimension((int) textField.getMaximumSize().getWidth(),ITEM_HEIGHT));
            }
            
            
            if(textField.getPreferredSize().getWidth() < ITEM_MIN_WIDTH) {
                textField.setPreferredSize(minDimension);
            }else {
                textField.setPreferredSize(new Dimension((int) textField.getPreferredSize().getWidth(),ITEM_HEIGHT));
            }

            textField.setEditable(false);
         
            if(pageDimField.getSelectedValue() != null) {
                textField.setText(getMemberViewText(pageDimField.getSelectedValue()));
            }else {
                textField.setText(null);
            }
            
            
        }
        return textField;
    }
    /**
     * @return Returns the value.
     */
    public PageDimField getPageDimField() {
        return pageDimField;
    }
   

    private String getMemberViewText(IMember member){
    	if(member == null) return null;
    	if(getPageDimField().isShowCode()){
    		return member.getMemcode();
    	}else{
    		return member.getName();
    	}
    }
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(AdhocPublic.PAGE_DIM_CHANGED)){
			
		}
		if(evt.getPropertyName().equals(AdhocPublic.PAGE_DIMENSION_POS_CHANGED)){
			getPropertyChangeSupport().firePropertyChange(evt);
		}
		if(evt.getPropertyName().equals(AdhocPublic.PAGE_DIMENSION_LOCK_STATUS_CHANGED)){
			Boolean enable = (Boolean) evt.getNewValue();
			getTextFieldt().getButton().setEnabled(!enable.booleanValue());
			getPropertyChangeSupport().firePropertyChange(evt);
		}
	}

}
