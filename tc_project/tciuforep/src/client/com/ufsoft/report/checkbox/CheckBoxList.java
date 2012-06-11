package com.ufsoft.report.checkbox;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicListUI;


public class CheckBoxList extends JList { 

	public CheckBoxList(){ 
	super(); 
	initCheckBox(); 
	} 

	public CheckBoxList(final Vector<?> listData){ 
	super(listData); 
	initCheckBox(); 
	} 

	public CheckBoxList(final Object[] listData){ 
	super(listData); 
	initCheckBox(); 
	} 

	public CheckBoxList(ListModel dataModel){ 
	super(dataModel); 
	initCheckBox(); 
	} 

	private void initCheckBox() { 
	this.setCellRenderer(new CheckBoxRenderer()); 
	this.setUI(new CheckBoxListUI()); 
	} 

	class CheckBoxRenderer extends JCheckBox implements ListCellRenderer { 
	public Component getListCellRendererComponent( 
	JList list, 
	Object value, 
	int index, 
	boolean isSelected, 
	boolean cellHasFocus) { 

	this.setSelected(isSelected); 
	this.setText(value.toString()); 
	return this; 
	} 
	}
	class CheckBoxListUI extends BasicListUI implements MouseInputListener { 
		
	protected MouseInputListener createMouseInputListener() { 
	    return this; 
	} 

	public void mouseClicked(MouseEvent e) { 

	} 

	public void mousePressed(MouseEvent e) { 
		
	int row = CheckBoxList.this.locationToIndex(e.getPoint()); 

	boolean temp = CheckBoxList.this.getSelectionModel().isSelectedIndex(row); 

	if (!temp) { 
	CheckBoxList.this.addSelectionInterval(row, row); 
	} else { 
	CheckBoxList.this.removeSelectionInterval(row,row); 
	} 

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	} 	
	} 

	public static void main(String[] args){
		JFrame frame =new JFrame("Demo");
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new CheckBoxList(new Object[]{"gg","ff","tt"}), BorderLayout.CENTER);
        frame.setVisible(true);
		
	}
} 
