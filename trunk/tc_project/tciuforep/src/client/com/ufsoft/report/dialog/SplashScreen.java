package com.ufsoft.report.dialog;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;

import com.ufida.iufo.pub.tools.AppDebug;
/**
 * 程序启动界面。
 */

public class SplashScreen extends JWindow {

	private class WindowHandler implements java.awt.event.WindowListener {
		public void windowActivated(java.awt.event.WindowEvent e) {
		};
		public void windowClosed(java.awt.event.WindowEvent e) {
		};
		public void windowClosing(java.awt.event.WindowEvent e) {
			if (e.getSource() == SplashScreen.this)
				SplashScreen.this.dispose();
		};
		public void windowDeactivated(java.awt.event.WindowEvent e) {
		};
		public void windowDeiconified(java.awt.event.WindowEvent e) {
		};
		public void windowIconified(java.awt.event.WindowEvent e) {
		};
		public void windowOpened(java.awt.event.WindowEvent e) {
		};
	};
	private Icon m_ScreanImage = null;

	/**
	 * 构造函数
	 * 
	 * @param owner
	 * @param image
	 */
	public SplashScreen(Frame owner, Icon image) {
		super(owner);
		if (image == null) {
			throw new IllegalArgumentException();
		}
		m_ScreanImage = image;
		initialize();

	}
	/**
	 * 启动动作页面
	 *  
	 */
	public void start() {
		//当前屏幕的尺寸
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		pack();
		Dimension size = getSize();
		if (size.height > screenSize.height)
			size.height = screenSize.height;
		if (size.width > screenSize.width)
			size.width = screenSize.width;
		setLocation((screenSize.width - size.width) / 2,
				(screenSize.height - size.height) / 2);
		setVisible(true);
	}
	/**
	 * 销毁 *
	 */
	public void end() {
		this.dispose();
	}
	/**
	 * 执行一定时间后自动关闭。
	 * 
	 * @param time
	 *            欢迎页面显示事件（ms）
	 */
	public void autoRun(long time) {
		try {
			start();
			Thread.sleep(time);
			end();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			AppDebug.debug(e);
		}
	}
	/**
	 * 初始化显示面板
	 */
	private void initialize() {
		JLabel screenLabel = new UILabel();
		screenLabel.setIcon(m_ScreanImage);
		//	图标
		JPanel panel = new UIPanel();
		panel.setBorder(new javax.swing.border.EtchedBorder());
		panel.setLayout(new java.awt.BorderLayout());
		panel.add(screenLabel, "Center");
		setContentPane(panel);
		addWindowListener(new WindowHandler());
	}

}