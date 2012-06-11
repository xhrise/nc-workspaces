package com.ufsoft.table.print;

import java.awt.LayoutManager;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.*;

/**
 * <p>
 * Title:Ԥ������Ĳ��ֹ�����
 * </p>
 * <p>
 * Description: Ԥ������Ĳ���ԭ���ǣ�ֻ����2�������ť���͹�����塣��ť�����Ҫ��֤���ĸ߶�
 * ����ʾ���а�ť��Ҫ�Ŀ�ȣ����������Ҫ��֤����С�ĸ߶ȣ���֤Ԥ��������ʾ��
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
	 * <code>BUTTONPANEL</code> ��ť���
	 */
	public static final String BUTTONPANEL = "BUTTONPANEL";
	/**
	 * <code>SCROLL_PANEL</code> �����������
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
		//��֤���Ŀ�Ⱥ͸߶ȡ�
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