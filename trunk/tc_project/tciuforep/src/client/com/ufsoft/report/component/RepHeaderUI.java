package com.ufsoft.report.component;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicLabelUI;

import com.ufsoft.table.header.HeaderRenderer;

public class RepHeaderUI extends BasicLabelUI {

	public void paint(Graphics g, JComponent c) {
		Color color = null;
		if(((HeaderRenderer)c).isSelected()){
			color =  StyleUtil.headerBackground;
			StyleUtil.paintUniform(c, g, color);
			
		} else if("colHeader".equals(c.getName())){
			Color startColor = StyleUtil.colHeaderStartColor;
			Color endColor = StyleUtil.colHeaderEndColor;
			
//			color = StyleUtil.rowHeaderColor;
			
			//StyleUtil.paintUniform(c, g, endColor);
			StyleUtil.paintGradient(c, g, startColor, endColor, false);
		} else{
			color = StyleUtil.rowHeaderColor;
			StyleUtil.paintUniform(c, g, color);
		}
		
		super.paint(g, c);
		
		 
		
//		 JLabel label = (JLabel)c;
//		 label.setOpaque(true);
//		 label.setBackground(color);
//		StyleUtil.paintUniform(c, g, color);
		
		
//	        String text = label.getText();
//	        
//		   int mnemIndex = label.getDisplayedMnemonicIndex();
//	        g.setColor(label.getForeground());
//	        SwingUtilities2.drawStringUnderlineCharAt(label, g, text, mnemIndex,
//	                                                     12, 12);
	}
	
}
