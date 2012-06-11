package com.ufida.report.anareport.applet;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;

import nc.ui.pub.beans.UIFileChooser;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.sysplugin.excel.ExcelExpUtil;
import com.ufsoft.report.sysplugin.xml.ExtNameFileFilter;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellsModel;

public class AnaReportExpExt extends AbsActionExt{
      private AnaReportPlugin m_plugin = null;
	
      public AnaReportExpExt(AnaReportPlugin plugin) {
		super();
		this.m_plugin = plugin;
	}

	@Override
	public UfoCommand getCommand() {
        return new UfoCommand() {
			
			public void execute(Object[] params) {
				AnaReportModel reportModel=m_plugin.getModel();
				boolean isFormat = reportModel.isFormatState();
				reportModel.loadAllData(false);
				CellsModel dataModel=reportModel.getCellsModel();
				
				int realRowNum = 0;
				int realColNum=0;
				if (dataModel.getCells() != null&&dataModel.getCells().size()>0){
					realRowNum = dataModel.getCells().size();
					realColNum=CellsModel.MAX_COL_NUM;
					
				}	
				if (realRowNum > dataModel.getRowNum()) {
					dataModel.getRowHeaderModel().initHeader(realRowNum);
				}
				if(realColNum>dataModel.getColNum()){
					dataModel.getColumnHeaderModel().initHeader(realColNum);
				}
				
				HSSFWorkbook workBook = ExcelExpUtil.createWorkBook(m_plugin.getContextVO(),dataModel,null);
				saveWorkBook2Local(m_plugin.getReport(), workBook,"xls");
				if(!isFormat){
					reportModel.changeState(false, false);
					m_plugin.changeUIState(ReportContextKey.OPERATION_INPUT);
				}
			}
		};
	}

	@Override
	public Object[] getParams(UfoReport container) {
        return null;
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(" Excel ");
		uiDes.setPaths(new String[]{MultiLang.getString("file"),MultiLang.getString("export")});
		ActionUIDes uiDes1 = (ActionUIDes) uiDes.clone();
		uiDes1.setName(MultiLang.getString("export")+" Excel ");
		uiDes1.setTooltip(MultiLang.getString("export")+" Excel ");
		uiDes1.setPaths(new String[]{});
	    uiDes1.setImageFile("reportcore/export.gif");
	    uiDes1.setToolBar(true);
	    uiDes1.setGroup(MultiLang.getString("printToolBar"));
		return new ActionUIDes[]{uiDes,uiDes1};
	}
	
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
