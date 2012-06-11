package com.ufsoft.report.sysplugin.excel;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;

import nc.ui.pub.beans.UIFileChooser;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.sysplugin.xml.ExtNameFileFilter;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellsModel;
import com.ufsoft.report.util.MultiLang;

public class ExcelExpCmd extends UfoCommand {

	
	public void execute(Object[] params) {
		UfoReport report = (UfoReport) params[0];
		IPostProcessor processor=null;
		 if(params.length>1&&params[1] instanceof IPostProcessor)
		   processor=(IPostProcessor)params[1];
		if (processor!=null)
			processor.setUfoReport(report);
		
		ContextVO context = report.getContextVo();
		CellsModel cellsModel = report.getCellsModel();
		HSSFWorkbook workBook = ExcelExpUtil.createWorkBook(context,cellsModel,processor);
		saveWorkBook2Local(report, workBook,"xls");
	}

    /**
     * 弹出文件选择界面，保存生成的Excel文件对象到用户本地
     * @param parent
     * @param workBook
     * @param strFilePostfix
     * @i18n report00032=文件保存失败。
     */
    public static void saveWorkBook2Local(Component parent, HSSFWorkbook workBook,String strFilePostfix) {
        if(parent == null || workBook == null){
            return;
        }
        
        if(strFilePostfix == null){
            strFilePostfix = "xls";
        }
        JFileChooser chooser = new UIFileChooser();
		ExtNameFileFilter xf = new ExtNameFileFilter(strFilePostfix);
		chooser.setFileFilter(xf);
		chooser.setMultiSelectionEnabled(false);		
		int returnVal = chooser.showSaveDialog(parent);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			file = xf.getModifiedFile(file);
			FileOutputStream stream = null;
			try {
				stream = new FileOutputStream(file);
				workBook.write(stream);
			} catch (Throwable e) {
//				AppDebug.debug(e);
				AppDebug.debug(e);
				UfoPublic.sendErrorMessage(MultiLang.getString("report00032"), null, e);
			}finally{
				try {
					stream.close();
				} catch (IOException e) {
					AppDebug.debug(e);
				}
			}
		}
    }
}
 