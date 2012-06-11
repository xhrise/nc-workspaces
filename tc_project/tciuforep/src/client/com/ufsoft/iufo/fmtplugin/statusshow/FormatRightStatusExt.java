package com.ufsoft.iufo.fmtplugin.statusshow;

import java.awt.Component;

import javax.swing.JLabel;

import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IStatusBarExt;

public class FormatRightStatusExt extends AbsActionExt implements IStatusBarExt {

	private UfoContextVO _contextVO;

	public FormatRightStatusExt(UfoContextVO contextVO) {
		_contextVO = contextVO;
	}

	@Override
	public UfoCommand getCommand() {
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		return null;
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		return null;
	}
	public void initListenerByComp(final Component stateChangeComp) {
		JLabel label = (JLabel) stateChangeComp;
		label.setText(StringResource.getStringResource("miufo1001799") + _contextVO.getFormatRightName());
	}
}
