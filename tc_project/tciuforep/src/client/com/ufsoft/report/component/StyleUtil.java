package com.ufsoft.report.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

public class StyleUtil {
	
	public static final Color headerSeperatorColor = new Color(0x8AA3BD);
	public static final Color rowHeaderColor = new Color(0xD2DBF1);
	public static final Color colHeaderStartColor = new Color(0xF0FDFF);
	public static final Color colHeaderEndColor = new Color(0xD2DBF1);
	public static final Color tableLeftTopCornerColor = rowHeaderColor;
	public static final Color reportUncoveredColor = rowHeaderColor;
	public static Color reportStatusBarColor = colHeaderStartColor;
	public static final Color headerBackground = new Color(0xFFD38E);
	
	public static void paintGradient(Component comp,Graphics g, Color startColor,Color endColor,boolean horizontal){
		Graphics2D g2d = (Graphics2D) g;
//		if(!comp.isOpaque()){
//			return;
//		}
//		Graphics2D g2d = (Graphics2D) comp.getGraphics();//这样取出的不可用！
		
		int compWidth = comp.getWidth();
		int compHeight = comp.getHeight();
		GradientPaint gp;
		if(horizontal){
			gp = new GradientPaint(0,0,startColor,compWidth, 0,endColor);
		}else{
			gp = new GradientPaint(0,0,startColor,0, compHeight,endColor);
		}
		Paint oldPaint = g2d.getPaint();
		g2d.setPaint(gp);
		g2d.fillRect(0,0,compWidth,compHeight);        
		g2d.setPaint(oldPaint);
	}
	public static void paintUniform(Component comp,Graphics g, Color color){
		Graphics2D g2d = (Graphics2D) g;		
		int compWidth = comp.getWidth();
		int compHeight = comp.getHeight();
		Paint oldPaint = g2d.getPaint();
		g2d.setPaint(color);
		g2d.fillRect(0,0,compWidth,compHeight);        
		g2d.setPaint(oldPaint);
	}
}
