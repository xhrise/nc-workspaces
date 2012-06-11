package com.ufsoft.iufo.fmtplugin.rounddigitarea;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.ToggleMenuUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;

public class RoundDigitAreaDisExt extends AbsActionExt {

	private UfoReport _report;

	private ActionListener actionListener = null;
	
	public RoundDigitAreaDisExt(UfoReport report) {
		_report = report;
	}

	@Override
	public UfoCommand getCommand() {
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		return null;
	}

	/**
	 * @i18n uiiufofmt00004=显示非舍位区
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
		ToggleMenuUIDes uiDes = new ToggleMenuUIDes();
		uiDes.setPaths(new String[]{MultiLang.getString("format")});
		uiDes.setName(StringResource.getStringResource("uiiufofmt00004"));
		uiDes.setGroup("styleMngExt");
		uiDes.setCheckBox(true);
		uiDes.setSelected(false);
		return new ActionUIDes[]{uiDes};
	}

	@Override
	public void initListenerByComp(Component stateChangeComp) {
		final JMenuItem checkBox = (JMenuItem) stateChangeComp;
		if(actionListener == null){
			checkBox.addActionListener(createActionListener(checkBox));
		}		
	}
	
	private ActionListener createActionListener(final JMenuItem checkBox) {
		actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CellsModel cellsModel = _report.getCellsModel();
				RoundDigitAreaModel model = RoundDigitAreaModel.getInstance(cellsModel);						
				model.setDisplay(!model.isDisplay());
				checkBox.setSelected(model.isDisplay());
				cellsModel.fireExtPropChanged(null);
			}

		};
		return actionListener;
	}

}
  