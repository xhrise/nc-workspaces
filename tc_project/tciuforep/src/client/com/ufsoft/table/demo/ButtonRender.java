package com.ufsoft.table.demo;

import java.util.Date;

import javax.swing.JButton;

import com.ufsoft.table.Cell;

/**
 * ��ʾһ����Ⱦ����
 * �������ڣ�(2004-5-19 8:45:47)
 * @author������
 */
public class ButtonRender implements com.ufsoft.table.re.SheetCellRenderer {
	private JButton render ;
/**
 * ButtonRender ������ע�⡣
 */
public ButtonRender() {
	super();
	render = new nc.ui.pub.beans.UIButton();
//	ImageIcon icon = new ImageIcon(ButtonRender.class.getClassLoader().getResource("image/manheader.gif"));
//	render.setIcon(icon);
}
 /**
   *  �õ�����һ����Ԫ����������Ը��ݵ�Ԫ��λ�������ò�ͬ����Ⱦ����
   * @param cellsPane CellsPane ������������Sheet
   * @param isSelected boolean ��ǰ�Ƿ�ѡ��
   * @param hasFocus boolean �Ƿ��ý���
   * @param row int ������λ�ã�����ͷ����Ϣ������Ϊ-1��
   * @param column int ������λ�ã�����ͷ����Ϣ������Ϊ-1.
   */
public java.awt.Component getCellRendererComponent(com.ufsoft.table.CellsPane cellsPane, Object obj, boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
	 
	if(obj!=null&&obj instanceof Date){
		Date date = (Date)obj;
		int year = date.getYear()-2008;
		render.setText("����"+year+"��");
	}
	render.setForeground(java.awt.Color.cyan);
	render.setBackground(java.awt.Color.red);
	return render;
}
}
