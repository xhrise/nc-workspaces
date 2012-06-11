package com.ufsoft.table;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

import nc.ui.plaf.basic.UIScrollBarUI;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.table.header.HeaderModel;

/**
 * 
 * @author wupeng
 * @version 3.1
 */
/**
 * <p>
 * Title:���ʹ�õĹ�����
 * </p>
 * <p>
 * Description: �ο�TableScrollBar
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0.0.1
 */

public class TableScrollBarUI extends UIScrollBarUI { //javax.swing.plaf.metal.MetalScrollBarUI { //BasicScrollBarUI {//
// HeaderButtonListener l = null;
	TableScrollBar bar = null;

	// /**
	// * �̳и����Ŀ�����ڣ������ޱ�����ťʱ������Ѿ����ﵱǰģ�͵����ֵ����Ҫ�Զ���չģ�͵� ��Χ��
	// */
	// class HeaderButtonListener implements ActionListener {
	// public void actionPerformed(ActionEvent e) {
	// // if(e.getSource()==incrButton) {
	// // //�������Ƿ��Ѿ����������Ͷˣ���ʱÿ���һ������һ�У�������Ź���������ô���ռ��ٶ�������
	// // int value = bar.getValue();
	// // if(value+bar.getModel().getExtent()==bar.getMaximum()){//
	// // ��ʱ��Ҫ��չģ�͵�����
	//
	// // }
	// // }
	// // �������������ʱ����Ҫ��������ģ�͵����ݷ�Χ���������ӣ�ֱ�����ޣ�
	// // ���������ع���ʱ����Ҫ��鵱ǰ����Ч��Χ������ع���������Ч������Ҫ��С��������ģ�͡�
	//
	// }
	//
	// }

	// **************************���´������ʹ�ã�����ɾ��
//	JTable table = null;

	// *************************test end

	/**
	 * ���캯��
	 * 
	 * @param c
	 */
	public TableScrollBarUI(JComponent c) {
		super();
		bar = (TableScrollBar) c;
	}

	public static ComponentUI createUI(JComponent c) {
		return new TableScrollBarUI(c);
	}

	// protected void installListeners() {
	// super.installListeners();
	// trackListener = new TableTrackListener();
	// reInstall();
	// }
	boolean isInfinite() {
		return bar.isInfinite();
	}

	protected TrackListener createTrackListener() {
		return new TableTrackListener();
	}

	// /**
	// * �������ü�����
	// */
	// public void reInstall() {
	// // ֻ�е����������������ʱ�򣬲Ź���������
	// // if (bar != null && bar.getHeader() != null) {
	// HeaderButtonListener old = l;
	// l = new HeaderButtonListener();
	// // ע���µļ�����
	// if (decrButton != null) {
	// decrButton.removeActionListener(old);
	// decrButton.addActionListener(l);
	// }
	// if (incrButton != null) {
	// incrButton.removeActionListener(old);
	// incrButton.addActionListener(l);
	// }
	// // }
	// }

	/**
	 * �˺������ظ���ķ���,Ŀ���ǵ��������������λ�õ����ť�ǿ��Լ����������е�����.
	 * 
	 * @param direction
	 *            �������ַ���;����ֵ��ʶ���ش�С
	 */
	protected void scrollByUnit(int direction) {
		
		if (!isInfinite()) {
			super.scrollByUnit(direction);
			return;
		}
		HeaderModel model = bar.getHeaderModel();
		if(model == null){
			return;
		}
		if (direction > 0) {
			int value = bar.getValue();
			if (value + bar.getModel().getExtent() >= bar.getMaximum()
					- model.getPreferredSize()) {
					model.setTempCount(model.getTempCount() + 1);
			}
			// liuyy+, 2008-05-23
		} else {
			int tempCount = model.getTempCount();
			if (model.getCount() < tempCount) {
				model.setTempCount(tempCount - 1);
			}
		}
		
//		final int dir = direction;
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				TableScrollBarUI.super.scrollByUnit(dir);
//			}
//		});
		 super.scrollByUnit(direction);
		 
		 AppDebug.debug("scrollByUnit");
         
	}

	// /**
	// * ж�ؼ�����
	// */
	// protected void uninstallListeners() {
	// super.uninstallListeners();
	// // ɾ���µļ�����
	// if (decrButton != null && l != null) {
	// decrButton.removeActionListener(l);
	// }
	// if (incrButton != null && l != null) {
	// incrButton.removeActionListener(l);
	// }
	// }

	protected class TableTrackListener extends BasicScrollBarUI.TrackListener {
		public void mousePressed(MouseEvent e) {
//			System.out.println("Pressed.");
			super.mousePressed(e);
		}

		public void mouseDragged(MouseEvent e) {
//			System.out.println("Drag.");
			super.mouseDragged(e);
		}
		
		 public void mouseReleased(MouseEvent e){
//			 System.out.println("Released.");
			 
			 final Cursor oldCursor = bar.getCellsPane().getCursor();
			 final Cursor oldCursor2 = bar.getCursor();
			 
			 try{
				 Cursor waitingCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
				 bar.getCellsPane().setCursor(waitingCursor);
				 bar.setCursor(waitingCursor);
				 
				 TableScrollBarUI.this.bar.getCellsPane().paginalData();
			
				 super.mouseReleased(e);
				 
			 } finally {
				 bar.getCellsPane().setCursor(oldCursor);
				 bar.setCursor(oldCursor2);
			 }
		 }
		 

	}
	
//	  protected ScrollListener createScrollListener(){
//			return new ScrollListener(){
//				public void actionPerformed(ActionEvent e) {
////					scrollByUnit();
//				}
//			};
//	  }
//	  
//	  
	 
}