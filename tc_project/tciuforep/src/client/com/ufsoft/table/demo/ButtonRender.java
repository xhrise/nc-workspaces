package com.ufsoft.table.demo;

import java.util.Date;

import javax.swing.JButton;

import com.ufsoft.table.Cell;

/**
 * 演示一个渲染器。
 * 创建日期：(2004-5-19 8:45:47)
 * @author：武鹏
 */
public class ButtonRender implements com.ufsoft.table.re.SheetCellRenderer {
	private JButton render ;
/**
 * ButtonRender 构造子注解。
 */
public ButtonRender() {
	super();
	render = new nc.ui.pub.beans.UIButton();
//	ImageIcon icon = new ImageIcon(ButtonRender.class.getClassLoader().getResource("image/manheader.gif"));
//	render.setIcon(icon);
}
 /**
   *  得到绘制一个单元的组件。可以根据单元的位置来设置不同的渲染器。
   * @param cellsPane CellsPane 请求绘制组件的Sheet
   * @param isSelected boolean 当前是否被选中
   * @param hasFocus boolean 是否获得焦点
   * @param row int 所处行位置，绘制头部信息，参数为-1。
   * @param column int 所处列位置，绘制头部信息，参数为-1.
   */
public java.awt.Component getCellRendererComponent(com.ufsoft.table.CellsPane cellsPane, Object obj, boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
	 
	if(obj!=null&&obj instanceof Date){
		Date date = (Date)obj;
		int year = date.getYear()-2008;
		render.setText("奥运"+year+"年");
	}
	render.setForeground(java.awt.Color.cyan);
	render.setBackground(java.awt.Color.red);
	return render;
}
}
