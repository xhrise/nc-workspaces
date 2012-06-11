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
 * ��ɫ��ť
 * 
 * @author wupeng
 */
public class ColorButton extends JButton implements ActionListener {
 
	private static final long serialVersionUID = 5780896622110672403L;
	/**
	 * �û���ǰѡ�����ɫ��
	 */
	private Color m_userColor;
	/**
	 * �����
	 */
	private Component m_parent;
	private static String s_strTitle = MultiLang.getString("miufo1000942");
	/**
	 * 
	 * @param c
	 *            �����
	 * @param parent
	 *            �û�ѡ�����ɫ
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
	 * �õ��û�ѡ�����ɫ
	 * 
	 * @return Color
	 */
	public Color getUserColor() {
		return m_userColor;
	}
	/**
	 * �����û�ѡ�����ɫ
	 * 
	 * @param c
	 */
	public void setUserColor(Color c) {
		m_userColor = c;
		setBackground(c);
	}
	/*
	 * ���� Javadoc��
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