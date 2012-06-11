package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
/**
 * �˴���������������
 * �������ڣ�(2002-5-15 8:40:44)
 * @author��������
 */
public class MeasureCheckCellRenderer extends nc.ui.pub.beans.UICheckBox implements TableCellRenderer {
	//����border
  protected static Border m_noFocusBorder;

/**
 * ����
 * �������ڣ�(00-11-20 15:21:13)
 * @author��
 */ 
public MeasureCheckCellRenderer() {
	super();
	m_noFocusBorder = new EmptyBorder(1, 2, 1, 2);
	setOpaque(true);
	setBorder(m_noFocusBorder);
  }
/**
 * �ӿ�ʵ��
 * �������ڣ�(00-11-20 15:21:13)
 * @author��
 */
public Component getTableCellRendererComponent(JTable table,
   Object value, boolean isSelected, boolean hasFocus, 
   int row, int column) 
  {
	if (value instanceof Boolean) {
	  Boolean b = (Boolean)value;
	  setSelected(b.booleanValue());
	}

	setBackground(isSelected && !hasFocus ? 
	  table.getSelectionBackground() : table.getBackground());
	setForeground(isSelected && !hasFocus ? 
	  table.getSelectionForeground() : table.getForeground());

	setBorder(hasFocus ? UIManager.getBorder(
	  "Table.focusCellHighlightBorder") : m_noFocusBorder);

	return this;
  }
}


