/*
 * Created on 2005-5-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import com.ufsoft.table.format.LineFactory;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 用于生成线型选择的列表绘制器
 */
public class LineRender extends nc.ui.pub.beans.UILabel implements ListCellRenderer{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 边线线型选择列表绘制器的一列，供LineRender类调用 */
    class LineIcon implements Icon{

		private int linetype;
		private int w, h;
		/**
		 * 构造函数
		 */
		public LineIcon() {
			this(0, 120, 15);
		}
		private LineIcon(int linetype, int w, int h) {
			super();
			this.linetype = linetype;
			this.w = w;
			this.h = h;
		}
		/**
		 * @i18n miufopublic358=无
		 */
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D ng = (Graphics2D) g;
			ng.setColor(Color.black);

			Line2D line;
			Stroke bs;

			switch (linetype) {
				case TableConstant.L_NULL :
					ng.drawString(StringResource.getStringResource("miufopublic358"), x + w / 3,
							y + 10);//无
					break;
				case TableConstant.L_SOLID1 :
				case TableConstant.L_DASH :
				case TableConstant.L_DOT :
				case TableConstant.L_DASHDOT :
				case TableConstant.L_SOLID2 :
				case TableConstant.L_SOLID3 :
				case TableConstant.L_SOLID4 :
					bs = LineFactory.createLineStroke(linetype);
					ng.setStroke(bs);
					line = new Line2D.Float(x + 4, (y + h) / 2, w - 2,
							(y + h) / 2);
					ng.draw(line);
					break;
				default :
					break;
			}
		}

		/**
		 * @param linetype
		 */
		public void setLineType(int linetype) {
			this.linetype = linetype;
		}

		public int getIconWidth() {
			return w;
		}

		public int getIconHeight() {
			return h;
		}
	
    }
	private LineIcon icon = new LineIcon();

	private Border redBorder = BorderFactory.createLineBorder(Color.black,
			2), emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		setText(" ");
		Object array = value;
		icon.setLineType(Integer.parseInt((String) array));

		setIcon(icon);

		if (isSelected) {
			setBorder(redBorder);
		} else {
			setBorder(emptyBorder);
		}
		return this;

	}

}
