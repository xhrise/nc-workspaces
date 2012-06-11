package com.ufsoft.table.exarea;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import com.ufsoft.report.UfoReport;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.format.LineFactory;
import com.ufsoft.table.format.TableConstant;

public class ExAreaComponent extends JComponent {
	
 private static final long serialVersionUID = 1L;	
 
 private  ExAreaCell m_cell = null;
 private  CellsPane m_cellsPane = null;
 private AreaPosition crossArea=null;
 private CellPosition crossPoint=null;
 private Rectangle m_rect = null;
 
 private final Color LINE_COLOR = new Color(49, 106, 197); //线条颜色
 private final Color CROSSLINE_COLOR=Color.RED;
 private final int LineBorderWidth = 4;//线条周围的空白宽度
	
 private static final int STEP = 5;
 
	public ExAreaComponent(ExAreaCell cell, CellsPane cp){
		setExAreas(cell, cp);
//		
//		initData(); 
//		
//		ExAreaMouseActionHandler hander = new ExAreaMouseActionHandler(m_cellsPane, cell);
////				
//		this.addMouseMotionListener(hander);
//		this.addMouseListener(hander);
	}

	public ExAreaComponent(ExAreaCell cell, CellsPane cp,AreaPosition crossArea,CellPosition crossPoint){
		setExAreas(cell, cp);
		this.crossArea=crossArea;
		this.crossPoint=crossPoint;
	}

	public void setExAreas(ExAreaCell cell, CellsPane cp) {
		this.m_cell = cell;
		this.m_cellsPane = cp;
	}
	
	private void initData(){
		m_rect = m_cellsPane.getCellRect(m_cell.getArea(), true);
	}
	
	protected void paintComponent(Graphics g) {
		 
		if(m_cellsPane.getOperationState() != UfoReport.OPERATION_FORMAT){
			return;
		}
		
		this.setOpaque(true);
		
		Graphics2D g2d = (Graphics2D) g;

		Paint oriPaint = g2d.getPaint();
		g2d.setPaint(LINE_COLOR);
		g2d.drawRect(0, 0, this.getWidth() - 1, this.getHeight() -1);

		 
		Stroke lineStroke = LineFactory
		.createLineStroke(TableConstant.L_DASH);
		g2d.setStroke(lineStroke);
		g2d.setColor(new Color(144,175,224));  //Color.blue);//

		final int step = 8;
	    
		int x2 = this.getWidth() - 1 - 2 * step;
		int y2 = this.getHeight() - 1 - 2 * step;
		if(m_cell.getExMode() == ExAreaCell.EX_MODE_FIXATION){
			g2d.drawRect(step, step, x2, y2);
		}
		
		x2 = this.getWidth() - 1 ;
		y2 = this.getHeight() - 1 ;
		if(m_cell.getExMode() == ExAreaCell.EX_MODE_X || m_cell.getExMode() == ExAreaCell.EX_MODE_XY){
			g2d.drawLine(step, step, x2, step);
			g2d.drawLine(x2 -  step * 2, step / 2, x2 , step);
			g2d.drawLine(x2 -  step * 2, step * 3/2, x2 , step);
		}
		
		if(m_cell.getExMode() == ExAreaCell.EX_MODE_Y || m_cell.getExMode() == ExAreaCell.EX_MODE_XY){
			g2d.drawLine(step, step, step, y2);
			g2d.drawLine(step / 2, y2 - 2 * step, step, y2);
			g2d.drawLine(step * 3/2, y2 - 2 * step, step, y2);
		}
		if(crossArea!=null&&crossPoint!=null&&crossArea.contain(crossPoint)){
			paintCrossArea(g2d,crossArea,crossPoint);
		}
		
		g2d.setPaint(oriPaint);

	}
	
	private void paintCrossArea(Graphics2D g2d,AreaPosition crossArea,CellPosition crossPoint){
		g2d.setPaint(CROSSLINE_COLOR);
		Rectangle allRect=m_cellsPane.getCellRect(crossArea, false);
		g2d.drawRect(allRect.x-this.getBounds().x,allRect.y-this.getBounds().y,allRect.width,allRect.height);
		Rectangle pointRect=m_cellsPane.getCellRect(crossPoint, false);
		Rectangle hRect=new Rectangle(pointRect.x-this.getBounds().x,allRect.y-this.getBounds().y,allRect.x+allRect.width-pointRect.x,pointRect.y-allRect.y);
		Rectangle vRect=new Rectangle(allRect.x-this.getBounds().x,pointRect.y-this.getBounds().y,pointRect.x-allRect.x,allRect.y+allRect.height-pointRect.y);
		g2d.drawLine(allRect.x-this.getBounds().x, pointRect.y-this.getBounds().y, allRect.x+allRect.width-this.getBounds().x, pointRect.y-this.getBounds().y);
		g2d.drawLine(pointRect.x-this.getBounds().x, allRect.y-this.getBounds().y, pointRect.x-this.getBounds().x, allRect.y+allRect.height-this.getBounds().y);
		paintHorizontalArrows(g2d,hRect);
		paintVerticvalArrows(g2d,vRect);
	}
	
	private void paintHorizontalArrows(Graphics2D g2d,Rectangle paintRect){
		Point2D startPoint = null;//线条起始点
        Point2D endPoint = null;//线条终点   
        int lineCount = 0;
        while ((2 * lineCount + 1) * LineBorderWidth < paintRect.height) {
            startPoint = new Point2D.Double(
            		paintRect.x,
            		paintRect.y+(2 * lineCount + 1) * LineBorderWidth);
            endPoint = new Point2D.Double(
            		paintRect.x+paintRect.width, 
            		paintRect.y+(2 * lineCount + 1) * LineBorderWidth);
            drawArrow(g2d, startPoint, endPoint);
            lineCount++;
        }
	}
	
	private void paintVerticvalArrows(Graphics2D g2d,Rectangle paintRect){
		Point2D startPoint = null;//线条起始点
        Point2D endPoint = null;//线条终点
        
        int lineCount = 0;
        while ((2 * lineCount + 1) * LineBorderWidth < paintRect.width) {
            startPoint = new Point2D.Double(paintRect.x+(2 * lineCount + 1) * LineBorderWidth, paintRect.y);
            endPoint = new Point2D.Double(paintRect.x+(2 * lineCount + 1) * LineBorderWidth, paintRect.y+paintRect.height);
            drawArrow(g2d, startPoint, endPoint);
            lineCount++;
        }
    
	}
	private void drawArrow(Graphics2D g2d, Point2D fromPoint, Point2D toPoint) {


		
		g2d.draw(new Line2D.Double(fromPoint, toPoint));
		
	}
	
	private class ExMouseListener implements MouseInputListener {
		ExAreaComponent m_comp = null;
		ExMouseListener(ExAreaComponent component){
			m_comp = component;
		}
		public void mouseClicked(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent e) {
			int x  = e.getX();
			int rightX = m_rect.x + m_rect.width;
			if(x > rightX - STEP || x < rightX + STEP){
				m_cellsPane.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			} else {
				m_cellsPane.setCursor(Cursor.getDefaultCursor());
			}
			
		}

		public void mouseExited(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			
		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseDragged(MouseEvent e) {
			Point point = m_comp.getLocation();
			
			int dropRow = m_cellsPane.rowAtPoint(e.getPoint());
            int dropCol = m_cellsPane.columnAtPoint(e.getPoint());
            
            
			
		}

		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}}


	@Override
	public void setBounds(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		super.setBounds(x, y, width, height);
	}
	

	@Override
	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		super.setSize(width, height);
	}

	 
}
