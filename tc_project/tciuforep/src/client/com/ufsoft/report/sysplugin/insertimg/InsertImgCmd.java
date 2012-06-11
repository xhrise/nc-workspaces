/*
 * InsertCellCmd.java
 * Created on 2004-10-19 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.insertimg;

import java.awt.Container;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectModel;

/**
 * 单元格插入图片
 * @author liuyy 
 */
public class InsertImgCmd extends UfoCommand {
	private UfoReport m_report;

 
	public InsertImgCmd(UfoReport rep) {
		super();
		this.m_report = rep;
	}
	/* Overrding method
	 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
	 */
	/**
	 * @i18n miufo00001=图片大小不能超过1M.
	 */
	public void execute(Object[] params) {
	 
	    File file = getImgFile(m_report);
	    if(file == null){
	    	return;
	    }
	    if(file.length() > 1024 * 120){
	    	  UfoPublic.showErrorDialog(m_report, MultiLang.getString("miufo00001"), MultiLang.getString("error"));
	    	  return;
	    }
	    
	    ImageIcon img = new ImageIcon(file.getAbsolutePath());
	    
	    CellsModel cellsModel = m_report.getCellsModel();
		SelectModel selectModel = cellsModel.getSelectModel();
	    CellPosition[] pos = selectModel.getSelectedCells();
	    if(pos == null || pos.length < 1){
	    	return;
	    }
	    for (int i = 0; i < pos.length; i++) {
	    	cellsModel.setCellValue(pos[i], img);			
		}
	}
	
    public static File getImgFile(Container report){
        File file = null;
        JFileChooser chooser = new JFileChooser();          
        chooser.setFileFilter(new FileFilter(){ 
			public boolean accept(File f) { 
				if (f.isDirectory())
					return true;

				String extension = null;
				String name = f.getName();
				int pos = name.lastIndexOf('.');
				if (pos > 0 && pos < name.length() - 1){
					extension = name.substring(pos + 1);
				}

				if (extension == null){
					return false;
				}
				String[] names = {"png", "jpeg", "jpg", "ico", "gif"};
				for (int i = 0; i < names.length; i++) {
					if(names[i].equalsIgnoreCase(extension)){
						return true;
					}
				}
				  
				return false;
			}

			@Override
			public String getDescription() { 
				return "Image files";
			}
        	
        });
        chooser.setMultiSelectionEnabled(false);   
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = chooser.showSaveDialog(report);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            file = chooser.getSelectedFile();   
        }   
        return file;
    }
 
}

