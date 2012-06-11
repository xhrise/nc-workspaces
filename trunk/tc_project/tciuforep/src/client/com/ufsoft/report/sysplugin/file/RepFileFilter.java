/*
 * RepFileFilter.java
 * Created on 2004-10-21 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.file;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 报表文件存储的默认文件过滤器
 * @author CaiJie
 * @since 3.1
 */
class RepFileFilter extends FileFilter{

	/**
	 * CaiJie 2004-10-21
	 */
	public RepFileFilter() {
		super();
		// TODO Auto-generated constructor stub
	}
	/* Overrding method
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		boolean accept = f.isDirectory();
		if (!accept){
			String suffix = this.getSuffix(f);
			if (suffix != null)
				accept = suffix.equals("rep");
		}
		return accept;
	}

	/* Overrding method
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {		
		return "StringResource.getStringResource(\"uiuforep000087\")(*.rep)";//报表
	}
	/**
	 * 获取文件后缀
	 */
	private String getSuffix(File f){
		String s = f.getPath();
		String suffix = null;
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1)
			suffix = s.substring(i+1).toLowerCase();
		return suffix;
	}	
}

