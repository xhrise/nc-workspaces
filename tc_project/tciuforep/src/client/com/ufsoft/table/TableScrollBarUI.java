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
 * Title:表格使用的滚动条
 * </p>
 * <p>
 * Description: 参考TableScrollBar
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
	// * 继承该类的目的在于，当无限表点击按钮时，如果已经到达当前模型的最大值，需要自动扩展模型的 范围。
	// */
	// class HeaderButtonListener implements ActionListener {
	// public void actionPerformed(ActionEvent e) {
	// // if(e.getSource()==incrButton) {
	// // //滚动条是否已经在区域的最低端，此时每点击一次增加一行；如果按着滚动条，那么按照加速度来处理
	// // int value = bar.getValue();
	// // if(value+bar.getModel().getExtent()==bar.getMaximum()){//
	// // 此时需要扩展模型的行数
	//
	// // }
	// // }
	// // 当滚动条延伸的时候，需要更改行列模型的数据范围：不断增加，直到极限；
	// // 当滚动条回滚的时候，需要检查当前的有效范围，如果回滚掉的是无效区域，需要减小行列数据模型。
	//
	// }
	//
	// }

	// **************************以下代码测试使用，发版删除
//	JTable table = null;

	// *************************test end

	/**
	 * 构造函数
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
	// * 重新设置监听器
	// */
	// public void reInstall() {
	// // 只有当设置了行列组件的时候，才构建监听器
	// // if (bar != null && bar.getHeader() != null) {
	// HeaderButtonListener old = l;
	// l = new HeaderButtonListener();
	// // 注册新的监听器
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
	 * 此函数重载父类的方法,目的是当滚动条到达最大位置点击按钮是可以继续增加行列的数量.
	 * 
	 * @param direction
	 *            正负区分方向;绝对值标识象素大小
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
	// * 卸载监听器
	// */
	// protected void uninstallListeners() {
	// super.uninstallListeners();
	// // 删除新的监听器
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