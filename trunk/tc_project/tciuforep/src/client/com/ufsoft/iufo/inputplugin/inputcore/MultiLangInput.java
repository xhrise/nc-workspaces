package com.ufsoft.iufo.inputplugin.inputcore;

import java.util.Locale;

import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.MultiLangProxy;

/**
 * Ä£·ÂMultiLang±àÐ´.
 * @author zzl
 */
public class MultiLangInput {
		
	private static MultiLangProxy proxy = null;
	private static MultiLangProxy getProxy(){
		if(proxy == null){
			proxy = new MultiLangProxy(){
				protected String getResFileName() {
					return "lang/reportinput/reportInputLang";
				}				
			};
		}
		return proxy;
	}
	
	public static void setLocale(Locale locale){
		MultiLang.setLocale(locale);
		getProxy().setLocale(locale);
	}
	
	public static String getString(String strID){
		return getProxy().getString(strID);
	}
	
	public static String getString(String strID, String[] params) {
	    return getProxy().getString(strID, params);
	}
}
