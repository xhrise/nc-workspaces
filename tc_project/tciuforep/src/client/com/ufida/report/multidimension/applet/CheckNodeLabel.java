package com.ufida.report.multidimension.applet;

import nc.vo.bi.base.util.StringUtil;


/**
 * ��ѡ����ı�ǩ��
 * �������ڣ�(2001-11-7 18:43:49)
 * @author��������
 * @since V1.00
 */
public class CheckNodeLabel extends javax.swing.JLabel {
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**�Ƿ�ѡ��*/
 private boolean m_isSelected;
 /**�Ƿ��н���*/
 private boolean m_hasFocus;
/**
 * CheckNodeLabel ������ע�⡣
 */
public CheckNodeLabel() {
 super();
}
/**
 * ������ѡ��С
 * �������ڣ�(2001-11-7 18:48:44)
 * @since V1.00
 * @return java.awt.Dimension
 */
public java.awt.Dimension getPreferredSize() {
 java.awt.Dimension retDimension = super.getPreferredSize();
 if (retDimension != null) {
  retDimension = new java.awt.Dimension(retDimension.width + 3, retDimension.height);
 }
 return retDimension;
}
/**
 * �Ƿ��н��㡣
 * �������ڣ�(2001-11-7 18:45:43)
 * @since V1.00
 * @return boolean
 */
public boolean hasFocus() {
 return m_hasFocus;
}
/**
 * �Ƿ�ѡ��
 * �������ڣ�(2001-11-7 18:45:43)
 * @since V1.00
 * @return boolean
 */
public boolean isSelected() {
 return m_isSelected;
}
/**
 * ���Ʊ�ǩ��
 * �������ڣ�(2001-11-7 18:49:48)
 * @since V1.00
 * @param g java.awt.Graphics
 */
public void paint(java.awt.Graphics g) {
 String str = StringUtil.replaceAll(getText(), " ", "");
 if (str != null) {
  if (isSelected()) {
   g.setColor(javax.swing.UIManager.getColor("List.selectionBackground"));
  } else {
   g.setColor(java.awt.Color.white);
  }
  java.awt.Dimension d = getPreferredSize();
  int imageOffset = 0;
  javax.swing.Icon currentIcon = getIcon();
  if (currentIcon != null) {
   imageOffset = currentIcon.getIconWidth() + Math.max(0, getIconTextGap() - 1);
  }
  g.fillRect(imageOffset, 0, d.width - 1 - imageOffset, d.height);
  if (hasFocus()) {
   g.setColor(javax.swing.UIManager.getColor("List.selectionBorderColor"));
   g.drawRect(imageOffset, 0, d.width - 1 - imageOffset, d.height - 1);
  }
 }
 super.paint(g);
}
/**
 * ���ñ���ɫ��
 * �������ڣ�(2001-11-7 18:47:39)
 * @since V1.00
 * @param color java.awt.Color
 */
public void setBackground(java.awt.Color color) {
 if (color instanceof javax.swing.plaf.ColorUIResource)
  color = null;
 super.setBackground(color);
}
/**
 * �Ƿ��н��㡣
 * �������ڣ�(2001-11-7 18:45:43)
 * @since V1.00
 * @param newHasFocus boolean
 */
public void setHasFocus(boolean newHasFocus) {
 m_hasFocus = newHasFocus;
}
/**
 * �Ƿ�ѡ��
 * �������ڣ�(2001-11-7 18:45:43)
 * @since V1.00
 * @param newSelected boolean
 */
public void setSelected(boolean newSelected) {
 m_isSelected = newSelected;
}
}


