package com.ufsoft.report.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Enumeration;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;
import javax.swing.text.JTextComponent;

import nc.ui.pub.beans.UIFrame;

public class RepToolBarPanelUI extends BasicPanelUI {
	private final static RepToolBarPanelUI instance = new RepToolBarPanelUI(); 
	public void paint(Graphics g, JComponent c) {
		setSubCompsOpaque(c);
		super.paint(g, c);
		StyleUtil.paintGradient(c, (Graphics2D)g,new Color(0xDD,0xE7,0xF0), new Color(0xB0,0xC1,0xD5),true);
	}
	
    private void setSubCompsOpaque(JComponent c) {
		Component[] comps = c.getComponents();
		for (int i = 0; i < comps.length; i++) {
			if(!(comps[i] instanceof JTextComponent) && (comps[i] instanceof JComponent)){
				((JComponent)comps[i]).setOpaque(false);
				setSubCompsOpaque((JComponent) comps[i]);
			}
		}		
	}

	public static ComponentUI createUI(JComponent c) {
    	return instance;
    }
	
	
	public static void main(String[] args) {
		UIManager.put("PanelUI",RepToolBarPanelUI.class.getName());
		outUIManager();

		JFrame f = new UIFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setLocation(0,0);
		f.setSize(100,100);
		f.setVisible(true);
		System.out.println("**************************");
		outUIManager();
	}
	private static void outUIManager(){
        Enumeration enum1 = UIManager.getDefaults().keys();
        while(enum1.hasMoreElements()){
        	Object key = enum1.nextElement();
        	Object value =  UIManager.getDefaults().get(key);
//        	if(key.toString().indexOf("Panel")>=0){
        		System.out.println(key+"\t"+value);
//        	}
        }
	}
}
