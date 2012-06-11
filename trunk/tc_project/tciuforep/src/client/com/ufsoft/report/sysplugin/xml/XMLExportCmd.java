package com.ufsoft.report.sysplugin.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

import nc.ui.pub.beans.UIFileChooser;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.table.CellsModel;

public class XMLExportCmd extends UfoCommand {

	public void execute(Object[] params) {
		UfoReport report = (UfoReport) params[0];
		XMLParserManager manager = (XMLParserManager) params[1];
		CellsModel cellsModel = report.getCellsModel();
		String xmlContent = XmlParserUtil.getXmlByCellsModel(cellsModel,manager);
		JFileChooser chooser = new UIFileChooser();			
		ExtNameFileFilter xf = new ExtNameFileFilter("xml");
		chooser.setFileFilter(xf);
		chooser.setMultiSelectionEnabled(false);	        
		int returnVal = chooser.showSaveDialog(report);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();			
			file = xf.getModifiedFile(file);	
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(xmlContent);
			} catch (IOException e) {
				AppDebug.debug(e);
			} finally{
				try {
					writer.close();
				} catch (IOException e) {
					AppDebug.debug(e);
				}
			}
		}		
	}
}
