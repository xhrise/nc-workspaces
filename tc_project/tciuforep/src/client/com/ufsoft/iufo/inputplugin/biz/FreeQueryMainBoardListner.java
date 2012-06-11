package com.ufsoft.iufo.inputplugin.biz;

import javax.swing.JApplet;

import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.event.MainboardListener;

public class FreeQueryMainBoardListner implements MainboardListener{
	private static Mainboard m_mainBoard;
	private static JApplet m_applet;
	public void shutdown(Mainboard mainboard) {
		if (mainboard==m_mainBoard){
			m_mainBoard=null;
			m_applet=null;
		}
	}

	public void startup(Mainboard mainboard) {
	}

	public static Mainboard getMainBoard() {
		return m_mainBoard;
	}
	
	public static JApplet getApplet() {
		return m_applet;
	}

	public static void setMainBoard(Mainboard board,JApplet applet) {
		m_mainBoard = board;
		m_applet=applet;
	}
}
