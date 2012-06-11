/*
 * �������� 2004-12-7
 */
package com.ufsoft.iufo.fmtplugin.dynarea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * 
 * @author wupeng
 * @version 3.1
 */
public class DynAreaRender implements SheetCellRenderer {
	private DynComp render = null;

	/**
	 */
	public DynAreaRender() {
		render = new DynComp();
		render.setOpaque(false);
	}
	
	/* ���� Javadoc��
	 * @see com.ufsoft.table.re.SheetCellRenderer#getCellRendererComponent(com.ufsoft.table.CellsPane, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getCellRendererComponent(CellsPane cellsPane, Object obj,
			 boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {
		//�ж϶�̬������
		render.hor = false;		
		return render;
	}
	static class DynComp extends JComponent{
		 
		private static final long serialVersionUID = 1L;
		/**
		 * �Ƿ�������ˮƽ������չ�Ķ�̬����
		 */
		public boolean hor = false;
		int step = 12;
		/**
		 * ���ն�̬����ķ������Ч����
		 */
		protected  void paintComponent(Graphics g){			
			Rectangle rect = this.getBounds();
			int width = rect.width;
			int height = rect.height;
			Color c = Color.CYAN;
			Color oldColor = g.getColor();
			g.setColor(c);
			int stepNum = 1;
			if(hor){//ˮƽ����Ķ�̬���򣬻���ˮƽ�����������
				height-=step;
				while(height>0){
					g.drawLine(0,step*stepNum,width,step*stepNum);
					height-=step;
					stepNum++;
				}				
			}else {
				width-=step;
				while(width>0){
					g.drawLine(step*stepNum,0,step*stepNum,height);
					width-=step;
					stepNum++;
				}	
			}
	        g.setColor(oldColor);
		}
		/**
		 * Overridden for performance reasons.
		 * See the <a href="#override">Implementation Note</a>
		 * for more information.
		 */
		public void repaint(long tm, int x, int y, int width, int height) {
		}
		/**
		 * Overridden for performance reasons.
		 * See the <a href="#override">Implementation Note</a>
		 * for more information.
		 */
		public void repaint(Rectangle r) {
		}
		/**
		 * Overridden for performance reasons.
		 * See the <a href="#override">Implementation Note</a>
		 * for more information.
		 */
		public void revalidate() {
		}
		/**
		 * See the <a href="#override">Implementation Note</a>
		 */
		public void validate() {
		}
	}

}
