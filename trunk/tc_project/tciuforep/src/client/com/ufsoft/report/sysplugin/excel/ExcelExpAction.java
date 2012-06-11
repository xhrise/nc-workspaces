package com.ufsoft.report.sysplugin.excel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import nc.ui.pub.beans.UIFileChooser;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.AbstractRepPluginAction;
import com.ufsoft.report.sysplugin.xml.ExtNameFileFilter;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellsModel;

public class ExcelExpAction extends AbstractRepPluginAction {
	
	private IPostProcessor postProcessExpExcel;	

	@Override
	public void execute(ActionEvent e) {
		
		CellsModel cellsModel = getCellsModel();
		HSSFWorkbook workBook = ExcelExpUtil.createWorkBook(getContext(),cellsModel,getPostProcessExpExcel());
	    //xulm 2009-08-25 使导出对话框屏幕居中
		saveWorkBook2Local(getMainboard(), workBook,"xls");

	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(MultiLang.getString("file"), MultiLang
				.getString("export"), ExcelImpPlugin2.GROUP);
		descriptor.setName(MultiLang.getString("excelFormat"));
		descriptor.setToolTipText(MultiLang.getString("export")+MultiLang.getString("excelFormat"));
		descriptor.setExtensionPoints(new XPOINT[] { XPOINT.MENU,
				XPOINT.TOOLBAR });
		descriptor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				KeyEvent.CTRL_MASK));
		descriptor.setMemonic('O');
		descriptor.setShowDialog(true);
		descriptor.setIcon("images/reportcore/export.gif");
		return descriptor;
	}
	
	/**
     * 弹出文件选择界面，保存生成的Excel文件对象到用户本地
     * @param parent
     * @param workBook
     * @param strFilePostfix
     * @i18n report00032=文件保存失败。
     */
    private void saveWorkBook2Local(Component parent, HSSFWorkbook workBook,String strFilePostfix) {
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
				AppDebug.debug(e);
				UfoPublic.sendErrorMessage(MultiLang.getString("report00032"), null, e);
			}finally{
				try {
					if(stream != null)
						stream.close();
				} catch (IOException e) {
					AppDebug.debug(e);
				}
			}
		}
    }
    
    public IPostProcessor getPostProcessExpExcel() {
		return postProcessExpExcel;
	}

	public void setPostProcessExpExcel(IPostProcessor postProcessExpExcel) {
		this.postProcessExpExcel = postProcessExpExcel;
	}

}
