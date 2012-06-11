package com.ufsoft.report.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.plaf.basic.BasicButtonUI;

import com.ufsoft.report.util.MultiLang;

/**
 * 颜色按钮
 * 
 * @author wupeng
 */
public class ColorButton extends JButton implements ActionListener {
 
	private static final long serialVersionUID = 5780896622110672403L;
	/**
	 * 用户当前选择的颜色。
	 */
	private Color m_userColor;
	/**
	 * 父组件
	 */
	private Component m_parent;
	private static String s_strTitle = MultiLang.getString("miufo1000942");
	/**
	 * 
	 * @param c
	 *            父组件
	 * @param parent
	 *            用户选择的颜色
	 */
	public ColorButton(Color c, Component parent) {
		if (c == null) {
			c = getBackground();
		}
        m_parent = parent;
		setUserColor(c);
		addActionListener(this);
	}
	/**
	 * 得到用户选择的颜色
	 * 
	 * @return Color
	 */
	public Color getUserColor() {
		return m_userColor;
	}
	/**
	 * 设置用户选择的颜色
	 * 
	 * @param c
	 */
	public void setUserColor(Color c) {
		m_userColor = c;
		setBackground(c);
	}
	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Color c = JColorChooser.showDialog(m_parent, s_strTitle, m_userColor);
		if (c != null) {
			setUserColor(c);
		}
	}
    public void updateUI() {
        setUI(new BasicButtonUI());
    }
}