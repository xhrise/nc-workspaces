package com.ufsoft.report.sysplugin.xml;

import java.io.File;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import nc.ui.pub.beans.UIFileChooser;

import org.w3c.dom.Document;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellsModel;

public class XMLImportCmd extends UfoCommand {

	/**
	 * @i18n file_not_exist=文件不存在！
	 * @i18n error=错误
	 */
	public void execute(Object[] params) {
		UfoReport report = (UfoReport) params[0];
		XMLParserManager manager = (XMLParserManager) params[1];
		
		JFileChooser chooser = new UIFileChooser();			
		ExtNameFileFilter xf = new ExtNameFileFilter("xml");
		chooser.setFileFilter(xf);
		chooser.setMultiSelectionEnabled(false);	
		int returnVal = chooser.showSaveDialog(report);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();	
			if(!file.exists()){
				UfoPublic.showErrorDialog(report,MultiLang.getString("file_not_exist"), MultiLang.getString("error"));
				return;
			}
			Document doc = null;
			try {
				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				doc = builder.parse(file);
			} catch (Exception e) {
				AppDebug.debug(e);
				throw new RuntimeException();
			}
			CellsModel cellsModel = XmlParserUtil.getCellsModelByXML(doc,manager);
			report.getTable().setCurCellsModel(cellsModel);
		}		
	}

}
  