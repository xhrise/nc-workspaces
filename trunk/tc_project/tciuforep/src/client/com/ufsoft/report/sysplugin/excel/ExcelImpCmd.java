package com.ufsoft.report.sysplugin.excel;

import java.awt.Container;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;

import nc.ui.pub.beans.UIFileChooser;


import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.sysplugin.xml.ExtNameFileFilter;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

public class ExcelImpCmd extends UfoCommand {

	public void execute(Object[] params) {
		UfoReport report = (UfoReport) params[0];
		List<IPostProcessImpExcel> postProcesses = (List<IPostProcessImpExcel>) params[1];
		
        File file = doGetExcelFile(report);
        if(file == null || !file.exists()){
          //  UfoPublic.showErrorDialog(report,MultiLang.getString("file_not_exist"),MultiLang.getString("error"));
            return;
        }
        report.getTable().setCurCellsModel(ExcelImpUtil.importCellsModel(file));
        for(IPostProcessImpExcel postProcess : postProcesses){
        	postProcess.dealAfterImpExcel();
        }
	}
    /**
     * 得到选择的Excel文件
     * @param report
     * @return
     */
    public static File doGetExcelFile(Container report){
        File file = null;
        JFileChooser chooser = new UIFileChooser();         
        ExtNameFileFilter xf = new ExtNameFileFilter("xls");
        chooser.setFileFilter(xf);
        chooser.setMultiSelectionEnabled(false);    
        int returnVal = chooser.showSaveDialog(report);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            file = chooser.getSelectedFile();   
        }   
        return file;
    }
}
