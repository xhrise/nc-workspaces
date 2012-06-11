package com.ufsoft.iufo.fmtplugin.statusshow;

import java.awt.Component;

import javax.swing.JLabel;

import nc.vo.iufo.datasource.DataSourceVO;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IStatusBarExt;

public class DataSourceStatusExt extends AbsActionExt implements IStatusBarExt,IUfoContextKey {

	private UfoContextVO _contextVO;
	public DataSourceStatusExt(UfoContextVO contextVO) {
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
		DataSourceVO dataSourceVo = (DataSourceVO)_contextVO.getAttribute(DATA_SOURCE);
		
		label.setText(StringResource.getStringResource("miufo1001422") + 
				(dataSourceVo != null ? dataSourceVo.getName() : 
					StringResource.getStringResource("miufopublic358")));
	}
}
