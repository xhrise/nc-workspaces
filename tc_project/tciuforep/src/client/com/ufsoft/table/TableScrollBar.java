package com.ufsoft.table;

import java.awt.*;
import javax.swing.*;

import com.ufsoft.table.header.*;

/**
 * <p>Title:表格使用的滚动条 </p>
 * <p>Description: 这个滚动条添加以下功能：如果无限表，当滚动到最末端，需要自动调节行列模型的
 * 长度。所以需要将行列组件设置在相应的滚动条上。</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public class TableScrollBar extends  nc.ui.pub.beans.UIScrollBar {  //JScrollBar {//
	private static final long serialVersionUID = -8772937661066158747L;
	/**滚动条对应的视图中的组件*/
	private CellsPane pane;
	/**当前是否是无限表*/
	private boolean infinite;
	/**
	 * 构造函数
	 * @param orientation 滚动条的方向，参见TableConstants
	 * @param pane 对应的组件，它实现了Scrollable接口，从中得到每次滚动的像素大小
	 * @param infinite 是否无限表。
	 */
	public TableScrollBar(int orientation, CellsPane pane, boolean infinite) {
		super(orientation);
		if (pane == null) {
			throw new IllegalArgumentException();
		}
		this.pane = pane;
		this.infinite = infinite;
		setUI(TableScrollBarUI.createUI(this));
	}

	/**
	 * 是否无限表
	 * @return boolean
	 */
	public boolean isInfinite() {
		return infinite;
	}

	

	/**
	 * 得到滚动条对应的头部的数据模型
	 * @return HeaderModel
	 */
	public HeaderModel getHeaderModel() {
		if(orientation==TableScrollBar.HORIZONTAL){//水平滚动条,关联列模型
			return pane.getDataModel().getColumnHeaderModel();
		}else {
			return pane.getDataModel().getRowHeaderModel();
		}
	}

	/* 
	 * @see javax.swing.JScrollBar#setValues(int, int, int, int)
	 */
	public void setValues(int newValue, int newExtent, int newMin, int newMax) {
		     super.setValues(newValue, newExtent, newMin, newMax);
//		//zzl将上边一句修改为下面一段：
//		//目的是保证视图改动时，滚动条无条件跟随，不再计算整格移动。
//		//解决了下面描述的现象：上下分栏时，向右拖动的选择事件发生后，下视图调整到合适的位置，主视图接着调整到合适的位置，
//		//主视图的调整引起滚动条事件，滚动条事件导致下视图计算整格移动，导致满足左边的整格，使右边出现半格。
//		javax.swing.event.ChangeListener[] aa = ((DefaultBoundedRangeModel) this
//				.getModel()).getChangeListeners();
//		com.ufsoft.table.TablePaneUI.SBChangeListener sb = null;
//		for (int i = 0; i < aa.length; i++) {
//			if (aa[i] instanceof com.ufsoft.table.TablePaneUI.SBChangeListener) {
//				sb = (com.ufsoft.table.TablePaneUI.SBChangeListener) aa[i];
//			}
//		}
////		this.getModel().removeChangeListener(sb);
//		super.setValues(newValue, newExtent, newMin, newMax);
////		this.getModel().addChangeListener(sb);
	}

	
	/**
	 * 计算当前容纳的ViewPort中的滚动组件的每个单位移动的距离
	 * @param direction <0标识向上向左；>0标识向下向右
	 * @return int
	 */
	public int getUnitIncrement(int direction) {
		JViewport vp = (JViewport) pane.getParent();
		Rectangle vr = vp.getViewRect();
		return pane.getScrollableUnitIncrement(vr, getOrientation(), direction);
	}

	/**
	 * 计算一个Block移动的象素位置
	 * @param direction <0标识向上向左；>0标识向下向右
	 * @return int
	 */
	public int getBlockIncrement(int direction) {
		JViewport vp = (JViewport) pane.getParent();
		Rectangle vr = vp.getViewRect();
		return pane
				.getScrollableBlockIncrement(vr, getOrientation(), direction);

	}
	public CellsPane getCellsPane(){
     return pane;   
    }
}