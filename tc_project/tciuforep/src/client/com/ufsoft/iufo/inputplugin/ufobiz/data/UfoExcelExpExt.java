package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.pub.beans.UIFileChooser;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufida.zior.view.Viewer;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoBizCmd;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoOpenedRepBizMenuExt;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.sysplugin.excel.ExcelExpCmd;
import com.ufsoft.report.sysplugin.excel.ExcelExpUtil;
import com.ufsoft.report.sysplugin.xml.ExtNameFileFilter;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

public class UfoExcelExpExt extends AbsUfoOpenedRepBizMenuExt{
	public void execute(ActionEvent e) {
		ReportDesigner view=(ReportDesigner)getCurrentView();
		if (view instanceof RepDataEditor ==false){
			HSSFWorkbook workBook = ExcelExpUtil.createWorkBook(view.getContext(),view.getCellsModel(),null);
			ExcelExpCmd.saveWorkBook2Local(view, workBook,"xls");
			return;
		}
		
		RepDataEditor editor=(RepDataEditor)view;
    	if (!AbsUfoBizCmd.stopCellEditing(editor))
    		return;
		
		try{
    		byte[] bytes=(byte[])ActionHandler.execWithZip("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "export2Excel",
					new Object[]{editor.getRepDataParam(),RepDataControler.getInstance(view.getMainboard()).getLoginEnv(view.getMainboard()),editor.getCellsModel()});
    		
            JFileChooser chooser = new UIFileChooser();
    		ExtNameFileFilter xf = new ExtNameFileFilter("xls");
    		chooser.setFileFilter(xf);
    		chooser.setMultiSelectionEnabled(false);		
    		int returnVal = chooser.showSaveDialog(view);
    		if(returnVal == JFileChooser.APPROVE_OPTION){
    			File file = chooser.getSelectedFile();
    			file = xf.getModifiedFile(file);
    			FileOutputStream stream = null;
    			try {
    				stream = new FileOutputStream(file);
    				stream.write(bytes);
    				stream.flush();
    			} catch (Throwable tte) {
    				AppDebug.debug(e);
    				UfoPublic.sendErrorMessage(MultiLang.getString("report00032"), null, tte);
    			}finally{
    				try {
    					if (stream!=null)
    						stream.close();
    				} catch (IOException ioe) {
    					AppDebug.debug(ioe);
    				}
    			}
    		}
    	}catch(Exception te){
    		UfoPublic.sendMessage(te.getMessage(), view);
    	}
	}

	/**
	 * @i18n miufo1002425=Excel格式
	 * @i18n miufohbbb00113=导入导出
	 * @i18n miufohbbb00114=导出Excel格式
	 */
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(StringResource.getStringResource("miufo1002425"));
		pad.setIcon("images/reportcore/export.gif");
		pad.setGroupPaths(doGetExportMenuPaths(StringResource.getStringResource("miufohbbb00113")));
//		pad.setGroupPaths(doGetExportMenuPaths("ddd"));
		pad.setToolTipText(StringResource.getStringResource("miufohbbb00114"));
		pad.setExtensionPoints(XPOINT.MENU,XPOINT.TOOLBAR);
		pad.setMemonic('E');
		pad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_MASK));
		pad.setShowDialog(true);
		return pad;
	}

	public boolean isEnabled() {
		Viewer view=getCurrentView();
		if (view instanceof ReportDesigner==false)
			return false;
		
		if (view instanceof RepDataEditor ==false)
			return true;
		
		boolean bEnabled=super.isEnabled();
		if (bEnabled==false)
			return bEnabled;
		
		RepDataEditor editor=(RepDataEditor)getCurrentView();
		return editor.getMenuState()!=null && editor.getMenuState().isCanAreaCal();
	}
}
 