package com.ufsoft.report.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuBarUI;

public class RepMenuBarUI extends BasicMenuBarUI {
//	private static final RepMenuBarUI instance = new RepMenuBarUI();
	
	public void paint(Graphics g, JComponent c) {
		Component[] comps = c.getComponents();
		for (int i = 0; i < comps.length; i++) {
			if(comps[i] instanceof JComponent){
				((JComponent)comps[i]).setOpaque(false);
			}
		}
		super.paint(g, c);
		StyleUtil.paintGradient(c, (Graphics2D)g,new Color(0xDD,0xE7,0xF0), new Color(0xB0,0xC1,0xD5),true);
	}
	
    public static ComponentUI createUI(JComponent c) {
//    	return instance;
    	return new RepMenuBarUI();
    }
}
