package com.ufsoft.iufo.fmtplugin.dynarea;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

/**
 * @author caijie
 */
public class DynAreaPublic {
    /**
     *  画垂直箭头线
     */
    public static JComponent createVerticvalArrows(final Color lineColor, final int lineBorderWidth) {
		return new JComponent() {
		    protected void paintComponent(Graphics g) {
		        int width = this.getWidth();
		        int height = this.getHeight();
		        
		        this.setOpaque(false);               
		        Graphics2D g2d = (Graphics2D) g;
		        Point2D startPoint = null;//线条起始点
		        Point2D endPoint = null;//线条终点
		
		        Paint oriPaint = g2d.getPaint();                    
		        g2d.setPaint(lineColor);            
		        if (width < 2 * lineBorderWidth) {//组件的宽度很小,只能画一条线,而且必须画一条线
		            startPoint = new Point2D.Double((int) (width / 2),
		                    0);
		            endPoint = new Point2D.Double((int) (width / 2), 
		                     height);
		            drawArrow(g2d, startPoint, endPoint);
		        } else {                        
		            int lineCount = 0;
		            while ((2 * lineCount + 1) * lineBorderWidth < width) {
		                startPoint = new Point2D.Double((2 * lineCount + 1) * lineBorderWidth, 0);
		                endPoint = new Point2D.Double((2 * lineCount + 1) * lineBorderWidth, height);
		                drawArrow(g2d, startPoint, endPoint);
		                lineCount++;
		            }
		        }
		        g2d.setPaint(oriPaint);  
		    }
		};
    };
    
    /**
     * 画水平箭头线
     */
    public static JComponent createHorizontalArrows(final Color lineColor, final int lineBorderWidth) {
    return new JComponent() {
        protected void paintComponent(Graphics g) {
            int width = this.getWidth();
            int height = this.getHeight();
            
            this.setOpaque(false); 
	        Graphics2D g2d = (Graphics2D) g;
            Point2D startPoint = null;//线条起始点
            Point2D endPoint = null;//线条终点                   
            
            
            Paint oriPaint = g2d.getPaint();
            g2d.setPaint(lineColor);
            
            if (width < 2 * lineBorderWidth) {//组件的宽度很小,只能画一条线,而且必须画一条线
                startPoint = new Point2D.Double(0 , (int) (height / 2));
                endPoint = new Point2D.Double(width,(int) (height / 2));
                drawArrow(g2d, startPoint, endPoint);
            } else {                        
                int lineCount = 0;
                while ((2 * lineCount + 1) * lineBorderWidth < height) {
                    startPoint = new Point2D.Double(
                            0,
                            (2 * lineCount + 1) * lineBorderWidth);
                    endPoint = new Point2D.Double(
                            width, 
                            (2 * lineCount + 1) * lineBorderWidth);
                    drawArrow(g2d, startPoint, endPoint);
                    lineCount++;
                }
            }
            
            g2d.setPaint(oriPaint);                   
        }
    };
    };
    /**
     * 画动态区域图标
     * caijie  2004-12-6
     * @param g2d
     * @param fromPoint
     * @param toPoint
     */
     private static void drawArrow(Graphics2D g2d, Point2D fromPoint, Point2D toPoint) {
     double ARROW_LENGTH = 10.0;
     double ARROW_ANGLE = Math.toRadians(30);
     double ax = ARROW_LENGTH * Math.cos(ARROW_ANGLE);
     double ay = ARROW_LENGTH * Math.sin(ARROW_ANGLE);

     AffineTransform originTransform = g2d.getTransform();
     g2d.translate(toPoint.getX(), toPoint.getY());
     g2d.rotate((fromPoint.getY() > toPoint.getY() ? -1 : 1) * Math.acos((toPoint.getX() - fromPoint.getX()) / fromPoint.distance(toPoint)));
//     g2d.draw(new Line2D.Double(0, 0, -ax, -ay));
//     g2d.draw(new Line2D.Double(0, 0, -ax, ay));
     g2d.setTransform(originTransform);
     g2d.draw(new Line2D.Double(fromPoint, toPoint));
     }
}
