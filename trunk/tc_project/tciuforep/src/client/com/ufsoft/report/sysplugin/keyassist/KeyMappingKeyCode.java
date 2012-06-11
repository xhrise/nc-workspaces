package com.ufsoft.report.sysplugin.keyassist;

import java.awt.event.KeyEvent;
import java.util.HashMap;
/**
 * 按键与键码对照
 * @author xulm
 *
 */
public class KeyMappingKeyCode {
	
	private static HashMap<String, Integer> keycode=new HashMap<String, Integer>();
	
	static
	{
		//A-Z
		keycode.put("A", KeyEvent.VK_A);
		keycode.put("B", KeyEvent.VK_B);
		keycode.put("C", KeyEvent.VK_C);
		keycode.put("D", KeyEvent.VK_D);
		keycode.put("E", KeyEvent.VK_E);
		keycode.put("F", KeyEvent.VK_F);
		keycode.put("G", KeyEvent.VK_G);
		keycode.put("H", KeyEvent.VK_H);
		keycode.put("I", KeyEvent.VK_I);
		keycode.put("J", KeyEvent.VK_J);
		keycode.put("K", KeyEvent.VK_K);
		keycode.put("L", KeyEvent.VK_L);
		keycode.put("M", KeyEvent.VK_M);
		keycode.put("N", KeyEvent.VK_N);
		keycode.put("O", KeyEvent.VK_O);
		keycode.put("P", KeyEvent.VK_P);
		keycode.put("Q", KeyEvent.VK_Q);
		keycode.put("R", KeyEvent.VK_R);
		keycode.put("S", KeyEvent.VK_S);
		keycode.put("T", KeyEvent.VK_T);
		keycode.put("U", KeyEvent.VK_U);
		keycode.put("V", KeyEvent.VK_V);
		keycode.put("W", KeyEvent.VK_W);
		keycode.put("X", KeyEvent.VK_X);
		keycode.put("Y", KeyEvent.VK_Y );
		keycode.put("Z", KeyEvent.VK_Z);
		
		//F1-F12
		keycode.put("F1", KeyEvent.VK_F1);
		keycode.put("F2", KeyEvent.VK_F2);
		keycode.put("F3", KeyEvent.VK_F3);
		keycode.put("F4", KeyEvent.VK_F4);
		keycode.put("F5", KeyEvent.VK_F5);
		keycode.put("F6", KeyEvent.VK_F6);
		keycode.put("F7", KeyEvent.VK_F7);
		keycode.put("F8", KeyEvent.VK_F8);
		keycode.put("F9", KeyEvent.VK_F9);
		keycode.put("F10", KeyEvent.VK_F10);
		keycode.put("F11", KeyEvent.VK_F11);
		keycode.put("F12", KeyEvent.VK_F12);
		
		//其它功能键
		keycode.put("ENTER",KeyEvent.VK_ENTER);
		keycode.put("SHIFT", KeyEvent.VK_SHIFT);
		keycode.put("CTRL", KeyEvent.VK_CONTROL);
		keycode.put("ALT", KeyEvent.VK_ALT);
		keycode.put("ESC", KeyEvent.VK_ESCAPE);
		//keycode.put("Insert ", 45); 因为报表录入工具中有定义{ "插入多组(之后)(M)...", "Ctrl+Ins" }, 
		keycode.put("INSERT ", KeyEvent.VK_INSERT);
		keycode.put("INS", KeyEvent.VK_INSERT);
		keycode.put("DELETE", KeyEvent.VK_DELETE);
		keycode.put("TAB", KeyEvent.VK_TAB);
		keycode.put("HOME", KeyEvent.VK_HOME);
		keycode.put("END", KeyEvent.VK_END);
		keycode.put("PGUP", KeyEvent.VK_PAGE_UP);
		keycode.put("PGDN", KeyEvent.VK_PAGE_DOWN);
		keycode.put("UP", KeyEvent.VK_UP);
		keycode.put("DOWN", KeyEvent.VK_DOWN);
		keycode.put("LEFT", KeyEvent.VK_LEFT);
		keycode.put("RIGHT", KeyEvent.VK_RIGHT);
	}
	
	
	/**
	 * 根据按键关键字获取键码
	 * @param key
	 * @return
	 */
	public static int getKeycode(String key)
	{
		if (keycode.get(key.toUpperCase())!=null)
		{
            return   ((Integer)keycode.get(key.toUpperCase())).intValue();
		}else
		{
			return -1;
		}
	}
	
	

}
