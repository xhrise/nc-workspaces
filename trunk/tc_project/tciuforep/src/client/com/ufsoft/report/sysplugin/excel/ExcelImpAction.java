package com.ufsoft.report.sysplugin.excel;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import nc.ui.pub.beans.UIFileChooser;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.AbstractRepPluginAction;
import com.ufsoft.report.sysplugin.xml.ExtNameFileFilter;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.script.AreaFmlExecutor;
import com.ufsoft.script.base.AreaFormulaModel;
import com.ufsoft.table.CellsModel;

public class ExcelImpAction extends AbstractRepPluginAction {

	@Override
	public void execute(ActionEvent e) {
		//xulm 2009-08-25 使导入对话框屏幕居中
		File file = doGetExcelFile(getMainboard());
		if (file == null || !file.exists()) {
			// UfoPublic.showErrorDialog(report,MultiLang.getString("file_not_exist"),MultiLang.getString("error"));
			return;
		}
		CellsModel cellsModel = ExcelImpUtil.importCellsModel(file);
		getTable().setCurCellsModel(cellsModel);
		dealAfterImpExcel(cellsModel);

	}

	protected void dealAfterImpExcel(CellsModel cellsModel) {
		AreaFormulaModel formulaModel = AreaFormulaModel
				.getInstance(cellsModel);
		formulaModel.setAreaFmlExecutor(new AreaFmlExecutor(cellsModel));
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(MultiLang.getString("file"), MultiLang
				.getString("import"), ExcelImpPlugin2.GROUP);
		descriptor.setName(MultiLang.getString("excelFormat"));
		descriptor.setToolTipText(MultiLang.getString("import")+MultiLang.getString("excelFormat"));
		descriptor.setExtensionPoints(new XPOINT[] { XPOINT.MENU,
				XPOINT.TOOLBAR });
		descriptor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				KeyEvent.CTRL_MASK));
		descriptor.setMemonic('I');
		descriptor.setShowDialog(true);
		descriptor.setIcon("images/reportcore/import.gif");
		return descriptor;
	}

	/**
	 * 得到选择的Excel文件
	 * 
	 * @param container
	 * @return
	 */
	private File doGetExcelFile(Container container) {
		File file = null;
		JFileChooser chooser = new UIFileChooser();
		ExtNameFileFilter xf = new ExtNameFileFilter("xls");
		chooser.setFileFilter(xf);
		chooser.setMultiSelectionEnabled(false);
		
		//xulm 2009-10-20  导入文件时应采用打开对话框
		int returnVal = chooser.showOpenDialog(container);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
		}
		return file;
	}

}
