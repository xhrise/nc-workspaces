package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import nc.ui.pub.beans.UILabel;

import com.sun.java.swing.SwingUtilities2;

public class UnderLineLabel extends UILabel {
	private static final long serialVersionUID = 1L;
	private static Rectangle paintIconR = new Rectangle();
	    private static Rectangle paintTextR = new Rectangle();
	    private static Rectangle paintViewR = new Rectangle();
	    private static Insets paintViewInsets = new Insets(0, 0, 0, 0);
	  
	public UnderLineLabel() {
	}

	public UnderLineLabel(String p0) {
		super(p0);
	}

	public UnderLineLabel(Icon p0) {
		super(p0);
	}

	public UnderLineLabel(String p0, int p1) {
		super(p0, p1);
	}

	public UnderLineLabel(Icon p0, int p1) {
		super(p0, p1);
	}

	public UnderLineLabel(String p0, Icon p1, int p2) {
		super(p0, p1, p2);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//
		
        FontMetrics fm = SwingUtilities2.getFontMetrics(this, g);
        Insets insets = getInsets(paintViewInsets);

        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width =getWidth() - (insets.left + insets.right);
        paintViewR.height =getHeight() - (insets.top + insets.bottom);

        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

//        String clippedText = 
            layoutCL(this, fm, getText(), getIcon(), paintViewR, paintIconR, paintTextR);


//		Insets ins = 
			getInsets();
		int x1 = paintTextR.x;
		int y1 = paintTextR.y+paintTextR.height-2;
		int x2 =x1+paintTextR.width;
		int y2 = y1;
		Color c = g.getColor();
		
		g.setColor(getForeground());
		g.drawLine(x1, y1, x2, y2);
		g.setColor(c);
	}
	private String layoutCL(
	        JLabel label,                  
	        FontMetrics fontMetrics, 
	        String text, 
	        Icon icon, 
	        Rectangle viewR, 
	        Rectangle iconR, 
	        Rectangle textR) {
		return SwingUtilities.layoutCompoundLabel(
	            (JComponent) label,
	            fontMetrics,
	            text,
	            icon,
	            label.getVerticalAlignment(),
	            label.getHorizontalAlignment(),
	            label.getVerticalTextPosition(),
	            label.getHorizontalTextPosition(),
	            viewR,
	            iconR,
	            textR,
	            label.getIconTextGap());
	}


}

