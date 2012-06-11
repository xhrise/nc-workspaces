/*
 * 创建日期 2005-5-17
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
//    		font = new Font("細明體", 0, 14);
//    	}else {
//    	    font = new Font("宋体", 0, 14);
//    	}
//    	setFont(font);
    }
//	/* （非 Javadoc）
//	 * @see javax.swing.JComponent#getToolTipText()
//	 */
//	public String getToolTipText() {
//		Rectangle rect = getBounds();
//		Dimension dimPre = getPreferredSize();
//		return "999";
////		if(dimPre.width>rect.width){//如果尺寸合适，那么不显示提示信息。
////			return null;
////		}
////		return getText();
//	}
//	/* （非 Javadoc）
//	 * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
//	 */
//	public String getToolTipText(MouseEvent arg0) {
//		Rectangle rect = getBounds();
//		Dimension dimPre = getPreferredSize();
//		return "999";
////		if(dimPre.width>rect.width){//如果尺寸合适，那么不显示提示信息。
////			return null;
////		}
////		return getText();
//	}
	/* （非 Javadoc）
	 * @see java.awt.Component#setBounds(int, int, int, int)
	 */
	public void setBounds(int arg0, int arg1, int arg2, int arg3) {
		// TODO 自动生成方法存根
		super.setBounds(arg0, arg1, arg2, arg3);
		Rectangle rect = getBounds();
		Dimension dimPre = getPreferredSize();
		if(dimPre.width>rect.width){//如果尺寸合适，那么不显示提示信息。
			setToolTipText(getText());
		}
	}
}


