package com.ufsoft.table.beans;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;

import com.ufsoft.table.format.FontFactory;

public class UfoLabelUI extends BasicLabelUI {
	//×××××××××××××××××××××××××× 安装、卸载UI××××××××××××××××××××××××
	/**
	 * 创建UI代理
	 */
	public static ComponentUI createUI(JComponent c) {
		return new UfoLabelUI();
	}
	
	public void paint(Graphics g, JComponent c) {
        UFOLabel label = (UFOLabel)c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();
        if ((icon == null) && (text == null)) {
            return;
        }
        
        if (icon!=null){
        	super.paint(g, c);
        	return;
        }
        
        if (label.isShrink()){
	        Rectangle bounds=label.getBounds();
	        Font font=label.getFont();
	        while (font.getSize()>0){
	        	double nSw=label.getPreferredSize().getWidth();
	            if (nSw<bounds.getWidth()){
	            	break;
	            }
	            
	            font=FontFactory.createFont(font.getFamily(),font.getStyle(),font.getSize()-1);
	            label.setFont(font);
	            g.setFont(font);
	        }   
        }
        
		super.paint(g, c);
	}
}
