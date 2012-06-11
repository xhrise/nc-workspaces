package com.ufsoft.report.applet;

import java.util.Hashtable;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.applet.MainApplet;

public class AppletCache {

	private static Hashtable<String, MainApplet> cache = new Hashtable<String, MainApplet>();
	
	public static void put(String id, MainApplet a){
		AppDebug.debug("��Applet��" + a.getClass().getName() + "___" + id);
		cache.put(id, a);
	}
	
	public void remove(String id){
		AppDebug.debug("�ر�Applet:" + id);
		// @edit by wangyga at 2009-6-1,����09:07:40 ��������ɾ��
		MainApplet applet = cache.get(id);
		if(applet != null){
			applet.destroy();
		}
		cache.remove(id);
	}
	
	public String save(String id){
		MainApplet applet = cache.get(id);
		if(applet == null){
			return "";
		}
		AppDebug.debug("ִ�б��棺Applet��" + applet.getClass().getName() + "___" + id);
		String strSave = applet.save();
		remove(id);
		return strSave == null ? "": strSave;
	}
	
	public boolean isDirty(String id){
		MainApplet applet = cache.get(id);
		if(applet == null){
			return false;
		}
		return applet.isDirty();
		
	}
	
}
 