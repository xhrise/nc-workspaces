package com.ufsoft.table.print;

import java.awt.LayoutManager;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.*;

/**
 * <p>
 * Title:预览界面的布局管理器
 * </p>
 * <p>
 * Description: 预览界面的布局原则是：只管理2个组件按钮面板和滚动面板。按钮面板需要保证它的高度
 * 和显示所有按钮需要的宽度；滚动面板需要保证它最小的高度，保证预览可以显示。
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0
 */

public class PreviewLayout implements LayoutManager {
	/**
	 * <code>BUTTONPANEL</code> 按钮面板
	 */
	public static final String BUTTONPANEL = "BUTTONPANEL";
	/**
	 * <code>SCROLL_PANEL</code> 滚动区域面板
	 */
	public static final String SCROLL_PANEL = "SCROLL_PANEL";
	private Component btnPanel;
	private Component scrollPane;
	/**
	 *  
	 */
	public PreviewLayout() {
	}
	public void addLayoutComponent(String name, Component comp) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		if (name.equals(BUTTONPANEL)) {
			btnPanel = comp;
		} else if (name.equals(SCROLL_PANEL)) {
			scrollPane = comp;
		}
	}
	public void layoutContainer(Container parent) {
		Rectangle rect = parent.getBounds();
		Dimension dim = btnPanel.getPreferredSize();
		int x = rect.x, y = rect.y;
		btnPanel.setBounds(x, y, rect.width, dim.height);
		y += dim.height;
		scrollPane.setBounds(x, y, rect.width, rect.height - dim.height);

	}
	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}
	public Dimension preferredLayoutSize(Container parent) {
		//保证面板的宽度和高度。
		Dimension dim = btnPanel.getPreferredSize();
		Dimension scDim = Toolkit.getDefaultToolkit().getScreenSize();
		Insets insets = parent.getParent().getParent().getParent().getParent()
				.getInsets();
		dim.height += (scDim.height - insets.top - insets.bottom);
		dim.width += (scDim.width - insets.left - insets.right);
		return dim;
	}
	public void removeLayoutComponent(Component comp) {
		if (comp == null) {
			throw new IllegalArgumentException();
		}
		if (comp == scrollPane) {
			scrollPane = null;
		} else if (comp == btnPanel) {
			btnPanel = null;
		}
	}
}