/*
 * 创建日期 2004-12-27
 */
package com.ufsoft.table;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

import com.ufsoft.report.component.StyleUtil;

/**
 * 全选标签。位于表格左上角的区域，点击后激发全选事件。
 * @author wupeng
 * @version 3.1
 */
public class SelectAllCmp extends JComponent{
	private static final long serialVersionUID = 7827681961795800120L;
	private TablePane m_TablePane;

	public SelectAllCmp(TablePane tablePane) {
		super();
		m_TablePane = tablePane;
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				m_TablePane.getCells().getSelectionModel().setSelectAll(true);
			}
		});
		Border border = BorderFactory.createMatteBorder(1, 1,
				1, 1, StyleUtil.headerSeperatorColor);
		setBorder(border);
	}
}
