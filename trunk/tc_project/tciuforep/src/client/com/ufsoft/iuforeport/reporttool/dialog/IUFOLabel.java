/*
 * �������� 2005-5-17
 */
package com.ufsoft.iuforeport.reporttool.dialog;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JLabel;

/**
 * 
 * @author wupeng
 * @version 3.1
 */
public class IUFOLabel extends nc.ui.pub.beans.UILabel {
	

    /**
     * 
     */
    public IUFOLabel() {
        super();
//        Font font;
//        Locale locale = Locale.getDefault();
//    	if(locale.equals(Locale.US)){
//    		font = new Font("Arial", 0, 9);
//    	}else if(locale.equals(Locale.TRADITIONAL_CHINESE)){
//    		font = new Font("�����w", 0, 14);
//    	}else {
//    	    font = new Font("����", 0, 14);
//    	}
//    	setFont(font);
    }
//	/* ���� Javadoc��
//	 * @see javax.swing.JComponent#getToolTipText()
//	 */
//	public String getToolTipText() {
//		Rectangle rect = getBounds();
//		Dimension dimPre = getPreferredSize();
//		return "999";
////		if(dimPre.width>rect.width){//����ߴ���ʣ���ô����ʾ��ʾ��Ϣ��
////			return null;
////		}
////		return getText();
//	}
//	/* ���� Javadoc��
//	 * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
//	 */
//	public String getToolTipText(MouseEvent arg0) {
//		Rectangle rect = getBounds();
//		Dimension dimPre = getPreferredSize();
//		return "999";
////		if(dimPre.width>rect.width){//����ߴ���ʣ���ô����ʾ��ʾ��Ϣ��
////			return null;
////		}
////		return getText();
//	}
	/* ���� Javadoc��
	 * @see java.awt.Component#setBounds(int, int, int, int)
	 */
	public void setBounds(int arg0, int arg1, int arg2, int arg3) {
		// TODO �Զ����ɷ������
		super.setBounds(arg0, arg1, arg2, arg3);
		Rectangle rect = getBounds();
		Dimension dimPre = getPreferredSize();
		if(dimPre.width>rect.width){//����ߴ���ʣ���ô����ʾ��ʾ��Ϣ��
			setToolTipText(getText());
		}
	}
}


